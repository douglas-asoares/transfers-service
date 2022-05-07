package com.bank.transfers.usecases;

import com.bank.transfers.domains.Account;
import com.bank.transfers.exceptions.NotAllowedTransferException;
import com.bank.transfers.gateways.AccountGateway;
import com.bank.transfers.http.json.input.TransferInputJson;
import com.bank.transfers.http.json.output.TransferOutputJson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerformsTransfer {

    private static final String UNAVAILABLE_MONEY = "Unavailable money in account from";

    private final ApplyInterBankTransferRules applyInterBankTransferRules;

    private final ApplyIntraBankTransferRules applyIntraBankTransferRules;

    private final AccountGateway accountGateway;

    public TransferOutputJson execute(final TransferInputJson transferInputJson) {
        final var accountFrom = accountGateway.findById(
                transferInputJson.getFromAccount().getCustomerCpf(), transferInputJson.getFromAccount().getBankCnpj());

        if (accountFrom.getMoneyAmount() < transferInputJson.getAmount())
            throw new NotAllowedTransferException(UNAVAILABLE_MONEY);

        final var accountTo = accountGateway.findById(
                transferInputJson.getToAccount().getCustomerCpf(), transferInputJson.getToAccount().getBankCnpj());

        return applyTransferTypeRules(accountFrom, accountTo, transferInputJson.getAmount());
    }

    private TransferOutputJson applyTransferTypeRules(final Account accountFrom, final Account accountTo, final Long amount) {
        if (isIntraBankTransfer(accountFrom, accountTo)) {
            return applyIntraBankTransferRules.execute(accountFrom, accountTo, amount);
        }
        return applyInterBankTransferRules.execute(accountFrom, accountTo, amount);
    }

    private boolean isIntraBankTransfer(final Account accountFrom, final Account accountTo) {
        return accountFrom.getId().getBankCnpj().equals(accountTo.getId().getBankCnpj());
    }
}
