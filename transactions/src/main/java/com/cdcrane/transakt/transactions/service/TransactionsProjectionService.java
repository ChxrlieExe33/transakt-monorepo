package com.cdcrane.transakt.transactions.service;

import com.cdcrane.transakt.transactions.entity.BankTransfer;
import com.cdcrane.transakt.transactions.entity.TransactionProjection;
import com.cdcrane.transakt.transactions.enums.TransactionProjectionStatus;
import com.cdcrane.transakt.transactions.enums.TransactionType;
import com.cdcrane.transakt.transactions.event.CashDepositedEvent;
import com.cdcrane.transakt.transactions.event.CashWithdrawnEvent;
import com.cdcrane.transakt.transactions.event.TransferRequestedEvent;
import com.cdcrane.transakt.transactions.repository.TransactionProjectionRepository;
import com.cdcrane.transakt.transactions.repository.TransferRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class TransactionsProjectionService implements TransactionProjectionUseCase {


    private final TransactionProjectionRepository transactionProjectionRepo;
    private final TransferRepository transferRepo;

    public TransactionsProjectionService(TransactionProjectionRepository transactionProjectionRepository, TransferRepository transferRepository) {
        this.transactionProjectionRepo = transactionProjectionRepository;
        this.transferRepo = transferRepository;
    }

    @Override
    @Transactional
    public void saveNewTransferAsProjection(TransferRequestedEvent event) {

        // Only create a pending one for the sender for now.
        // Receiver will get it on transfer completion.
        TransactionProjection projection = TransactionProjection.builder()
                .affectedAccountId(event.sourceAccountId())
                .amount(event.amount() * -1) // * -1 To make it negative, since the sender is losing this money, the projection should reflect that.
                .transactionType(TransactionType.TRANSFER)
                .transactionStatus(TransactionProjectionStatus.PENDING)
                .transferId(event.transferId())
                .build();

        transactionProjectionRepo.save(projection);

    }

    @Override
    @Transactional
    public void handleTransferRejectedInProjection(UUID transferId) {

        Optional<TransactionProjection> projection = transactionProjectionRepo.findByTransferId(transferId);

        if (projection.isEmpty()) {
            throw new IllegalStateException("Transfer " + transferId + " not found in the local database. Could not process transfer rejection in the projection.");
        }

        TransactionProjection op = projection.get();

        op.setTransactionStatus(TransactionProjectionStatus.FAILED);

        transactionProjectionRepo.save(op);

    }

    @Override
    @Transactional
    public void handleTransferCompletedInProjection(UUID transferId) {

        Optional<TransactionProjection> projection = transactionProjectionRepo.findByTransferId(transferId);

        if (projection.isEmpty()) {
            throw new IllegalStateException("Transfer " + transferId + " not found in the local database. Could not process transfer completion in the projection.");
        }

        TransactionProjection senderProjection = projection.get();

        senderProjection.setTransactionStatus(TransactionProjectionStatus.COMPLETED);

        // Get the receiver account ID from the transfer.
        Optional<BankTransfer> transfer = transferRepo.findById(transferId);

        if (transfer.isEmpty()) {
            throw new IllegalStateException("Transfer " + transferId + " not found in the local database. Could not process transfer completion in the receiver projection.");
        }

        BankTransfer op = transfer.get();

        TransactionProjection receiverProjection = TransactionProjection.builder()
                .transferId(transferId)
                .affectedAccountId(op.getTargetAccountId())
                .amount(op.getAmount())
                .transactionType(TransactionType.TRANSFER)
                .transactionStatus(TransactionProjectionStatus.COMPLETED)
                .build();

        transactionProjectionRepo.save(senderProjection);
        transactionProjectionRepo.save(receiverProjection);

    }

    @Override
    @Transactional
    public void saveNewCashDepositAsProjection(CashDepositedEvent event) {

        TransactionProjection projection = TransactionProjection.builder()
                .affectedAccountId(event.accountId())
                .amount(event.amount())
                .transactionType(TransactionType.CASH_DEPOSIT)
                .transactionStatus(TransactionProjectionStatus.COMPLETED)
                .cashDepositId(event.cashDepositId())
                .build();

        transactionProjectionRepo.save(projection);
    }

    @Override
    public void saveNewCashWithdrawalAsProjection(CashWithdrawnEvent event) {

        TransactionProjection projection = TransactionProjection.builder()
                .affectedAccountId(event.accountId())
                .amount(event.amount() * -1)  // * -1 To make it negative, since the sender is losing this money, the projection should reflect that.
                .transactionType(TransactionType.CASH_WITHDRAWAL)
                .transactionStatus(TransactionProjectionStatus.COMPLETED)
                .cashWithdrawalId(event.cashWithdrawalId())
                .build();

        transactionProjectionRepo.save(projection);

    }
}
