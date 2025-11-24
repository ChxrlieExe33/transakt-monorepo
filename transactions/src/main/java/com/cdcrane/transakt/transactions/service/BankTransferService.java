package com.cdcrane.transakt.transactions.service;

import com.cdcrane.transakt.transactions.dto.BankTransferRequest;
import com.cdcrane.transakt.transactions.entity.BankAccountProjection;
import com.cdcrane.transakt.transactions.entity.BankTransfer;
import com.cdcrane.transakt.transactions.enums.BankTransferStatus;
import com.cdcrane.transakt.transactions.event.TransferRequestedEvent;
import com.cdcrane.transakt.transactions.exception.AccountNotFoundException;
import com.cdcrane.transakt.transactions.exception.CannotTransferToSameAccountException;
import com.cdcrane.transakt.transactions.exception.NotAuthorizedForTransferException;
import com.cdcrane.transakt.transactions.exception.NotEnoughFundsException;
import com.cdcrane.transakt.transactions.repository.BankAccountProjectionRepository;
import com.cdcrane.transakt.transactions.repository.TransferRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class BankTransferService implements BankTransferUseCase{

    private final BankAccountProjectionRepository bankAccountProjectionRepo;
    private final TransferRepository transferRepository;
    private final StreamBridge streamBridge;
    private final ApplicationEventPublisher localPublisher;

    public BankTransferService(BankAccountProjectionRepository bankAccountProjectionRepository, TransferRepository transferRepository, StreamBridge streamBridge, ApplicationEventPublisher applicationEventPublisher) {
        this.bankAccountProjectionRepo = bankAccountProjectionRepository;
        this.transferRepository = transferRepository;
        this.streamBridge = streamBridge;
        this.localPublisher = applicationEventPublisher;
    }

    @Override
    @Transactional
    public void transferMoney(BankTransferRequest request, UUID currentCustomerId) {

        if (request.sourceAccountId().equals(request.destinationAccountId())) {
            throw new CannotTransferToSameAccountException("Source account " + request.sourceAccountId() + " and destination account " + request.destinationAccountId() + " are the same, cannot transfer money.");
        }

        BankAccountProjection source = bankAccountProjectionRepo.findById(request.sourceAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Source account " + request.sourceAccountId() + " not found, cannot transfer money."));

        BankAccountProjection destination = bankAccountProjectionRepo.findById(request.destinationAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Destination account " + request.sourceAccountId() + " not found, cannot transfer money."));

        if (!source.getCustomerId().equals(currentCustomerId)) {
            throw new NotAuthorizedForTransferException("Source account " + request.sourceAccountId() + " belongs to customer " + source.getCustomerId() + ", customer " + currentCustomerId + " is not authorized to transfer money.");
        }

        if (source.getCurrentBalance() < request.amount()) {
            throw new NotEnoughFundsException("Source account " + request.sourceAccountId() + " does not have enough funds to transfer " + request.amount() + " for concept " + request.concept() + ". If this seems incorrect, please contact your local branch.");
        }

        BankTransfer transfer = BankTransfer.builder()
                .sourceAccountId(request.sourceAccountId())
                .sourceName(request.sourceName())
                .targetName(request.destinationName())
                .targetAccountId(request.destinationAccountId())
                .amount(request.amount())
                .concept(request.concept())
                .status(BankTransferStatus.PENDING)
                .build();

        BankTransfer saved = transferRepository.save(transfer);

        TransferRequestedEvent event = new TransferRequestedEvent(saved.getBankTransferId(), saved.getSourceAccountId(), saved.getTargetAccountId(), saved.getAmount(), saved.getSourceName(), saved.getTargetName(), saved.getConcept());

        streamBridge.send("transferSubmitted-out-0", event);
        localPublisher.publishEvent(event);

        log.info("Transfer submitted from sender account {} to receiver account {} with amount {} and concept '{}' saved.", request.sourceAccountId(), request.destinationAccountId(), request.amount(), request.concept());

    }

    @Override
    @Transactional
    public void handleTransferRejected(UUID transferId, String reason) {

        Optional<BankTransfer> trans = transferRepository.findById(transferId);

        if (trans.isEmpty()) {
            log.error("Transfer {} not found in the local database. Could not process transfer rejection.", transferId);
            return;
        }

        var transfer = trans.get();
        transfer.setStatus(BankTransferStatus.FAILED);
        transfer.setFailureReason(reason);

        transferRepository.save(transfer);

        log.info("Transfer {} rejected with reason {}.", transferId, reason);

    }

    @Override
    @Transactional
    public void handleTransferCompleted(UUID transferId) {

        Optional<BankTransfer> trans = transferRepository.findById(transferId);

        if (trans.isEmpty()) {
            log.error("Transfer {} not found in the local database. Could not process transfer completion.", transferId);
            return;
        }

        var transfer = trans.get();

        Optional<BankAccountProjection> sender = bankAccountProjectionRepo.findById(transfer.getSourceAccountId());
        Optional<BankAccountProjection> receiver = bankAccountProjectionRepo.findById(transfer.getTargetAccountId());

        if (sender.isEmpty() || receiver.isEmpty()) {
            log.error("Sender account {} or receiver account {} not found in the local database. Could not process transfer completion.", transfer.getSourceAccountId(), transfer.getTargetAccountId());
            return;
        }

        var senderProjection = sender.get();
        var receiverProjection = receiver.get();

        senderProjection.setCurrentBalance(senderProjection.getCurrentBalance() - transfer.getAmount());
        receiverProjection.setCurrentBalance(receiverProjection.getCurrentBalance() + transfer.getAmount());

        bankAccountProjectionRepo.save(senderProjection);
        bankAccountProjectionRepo.save(receiverProjection);

        transfer.setStatus(BankTransferStatus.SUCCESS);

        transferRepository.save(transfer);

        log.info("Transfer {} completed.", transferId);

    }

}
