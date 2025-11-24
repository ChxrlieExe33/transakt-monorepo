package com.cdcrane.transakt.transactions.service;

import com.cdcrane.transakt.transactions.event.CashDepositedEvent;
import com.cdcrane.transakt.transactions.event.CashWithdrawnEvent;
import com.cdcrane.transakt.transactions.event.TransferRequestedEvent;

import java.util.UUID;

public interface TransactionProjectionUseCase {

    void saveNewTransferAsProjection(TransferRequestedEvent event);

    void handleTransferRejectedInProjection(UUID transferId);

    void handleTransferCompletedInProjection(UUID transferId);

    void saveNewCashDepositAsProjection(CashDepositedEvent event);

    void saveNewCashWithdrawalAsProjection(CashWithdrawnEvent event);
}
