package com.bank.transfers.usecases;

import com.bank.transfers.domains.Account;
import com.bank.transfers.domains.Transfer;
import com.bank.transfers.exceptions.FailedInterBankTransferException;
import com.bank.transfers.exceptions.NotAllowedTransferException;
import com.bank.transfers.gateways.AccountGateway;
import com.bank.transfers.http.json.output.TransferOutputJson;
import com.bank.transfers.utils.InterBankTransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplyInterBankTransferRules {

    private static final String FAILED_TRANSFER = "Inter bank transfer has failed";

    private static final String LIMIT_EXCEEDED = "Transfer limit exceeded: limit is R$5000 per transfer";

    private static final Long COMMISSION_FEE = 5L;

    private final AccountGateway accountGateway;

    private final InterBankTransferUtils interBankTransferUtils;

    public TransferOutputJson execute(final Account accountFrom, final Account accountTo, final Long amount) {
        if (interBankTransferUtils.shouldTransferFail()) {
            throw new FailedInterBankTransferException(FAILED_TRANSFER);
        }

        if (amount > 5000L) {
            throw new NotAllowedTransferException(LIMIT_EXCEEDED);
        }

        performsTransfer(accountFrom, accountTo, amount);

        return TransferOutputJson.builder()
                .senderCustomerCpf(accountFrom.getId().getCustomerCpf())
                .receiverCustomerCpf(accountTo.getId().getCustomerCpf())
                .amount(amount)
                .build();
    }

    private void performsTransfer(final Account accountFrom, final Account accountTo, final Long amount) {
        final var transfer = Transfer.builder()
                .id(UUID.randomUUID().toString())
                .fromAccountId(accountFrom.getId())
                .toAccountId(accountTo.getId())
                .amount(amount)
                .build();

        accountFrom.setMoneyAmount(accountFrom.getMoneyAmount() - amount - COMMISSION_FEE);
        accountFrom.getTransfers().add(transfer);
        accountGateway.save(accountFrom);

        accountTo.setMoneyAmount(accountTo.getMoneyAmount() + amount);
        accountTo.getTransfers().add(transfer);
        accountGateway.save(accountTo);
    }
}
