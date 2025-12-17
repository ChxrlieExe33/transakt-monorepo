package com.cdcrane.transakt.transactions.service;

import com.cdcrane.transakt.transactions.dto.TransactionProjectionDTO;
import com.cdcrane.transakt.transactions.event.CashDepositedEvent;
import com.cdcrane.transakt.transactions.event.CashWithdrawnEvent;
import com.cdcrane.transakt.transactions.event.TransferRequestedEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TransactionProjectionUseCase {

    void saveNewTransferAsProjection(TransferRequestedEvent event);

    void handleTransferRejectedInProjection(UUID transferId);

    void handleTransferCompletedInProjection(UUID transferId);

    void saveNewCashDepositAsProjection(CashDepositedEvent event);

    void saveNewCashWithdrawalAsProjection(CashWithdrawnEvent event);

    Page<TransactionProjectionDTO> getTransactionsByAffectedAccount(UUID accountId, UUID currentCustomerId, Pageable pageable);
}
