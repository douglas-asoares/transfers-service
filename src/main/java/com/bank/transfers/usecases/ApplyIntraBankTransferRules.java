package com.bank.transfers.usecases;

import com.bank.transfers.domains.Account;
import com.bank.transfers.domains.Transfer;
import com.bank.transfers.gateways.AccountGateway;
import com.bank.transfers.http.json.output.TransferOutputJson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplyIntraBankTransferRules {

    private final AccountGateway accountGateway;

    public TransferOutputJson execute(final Account accountFrom, final Account accountTo, final Long amount) {
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

        accountFrom.setMoneyAmount(accountFrom.getMoneyAmount() - amount);
        accountFrom.getTransfers().add(transfer);
        accountGateway.save(accountFrom);

        accountTo.setMoneyAmount(accountTo.getMoneyAmount() + amount);
        accountTo.getTransfers().add(transfer);
        accountGateway.save(accountTo);
    }
}
