package com.bank.transfers.builders.domains;

import com.bank.transfers.domains.Account;
import com.bank.transfers.domains.AccountId;

import java.util.ArrayList;

public class AccountBuilder {

    public static Account VALID() {
        return Account.builder()
                .id(new AccountId("111.111.111-11", "11.111.111/1111-11"))
                .moneyAmount(1000L)
                .transfers(new ArrayList<>())
                .build();
    }

    public static Account VALID_WITH(final String customerCpf, final String bankCnpj, final Long moneyAmount) {
        return Account.builder()
                .id(new AccountId(customerCpf, bankCnpj))
                .moneyAmount(moneyAmount)
                .transfers(new ArrayList<>())
                .build();
    }
}
