package com.cdcrane.transakt.transactions.listener;

import com.cdcrane.transakt.transactions.event.*;
import com.cdcrane.transakt.transactions.service.TransactionProjectionUseCase;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@EnableAsync
@Component
public class LocalEventListener {

    private final TransactionProjectionUseCase transactionProjectionUseCase;

    public LocalEventListener(TransactionProjectionUseCase transactionProjectionUseCase) {
        this.transactionProjectionUseCase = transactionProjectionUseCase;
    }

    @Async
    @EventListener(TransferRequestedEvent.class)
    public void createTransferProjection(TransferRequestedEvent event) {

        transactionProjectionUseCase.saveNewTransferAsProjection(event);

    }

    @Async
    @EventListener(TransferCompleteEvent.class)
    public void markTransferProjectionAsComplete(TransferCompleteEvent event) {

        transactionProjectionUseCase.handleTransferCompletedInProjection(event.transferId());
    }

    @Async
    @EventListener(TransferRejectedEvent.class)
    public void markTransferProjectionAsRejected(TransferRejectedEvent event) {

        transactionProjectionUseCase.handleTransferRejectedInProjection(event.transferId());

    }

    @Async
    @EventListener(CashDepositedEvent.class)
    public void createCashDepositProjection(CashDepositedEvent event) {

        transactionProjectionUseCase.saveNewCashDepositAsProjection(event);

    }

    @Async
    @EventListener(CashWithdrawnEvent.class)
    public void createCashWithdrawalProjection(CashWithdrawnEvent event) {

        transactionProjectionUseCase.saveNewCashWithdrawalAsProjection(event);

    }

}
