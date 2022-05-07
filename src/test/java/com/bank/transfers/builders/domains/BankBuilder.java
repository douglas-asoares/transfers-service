package com.bank.transfers.builders.domains;

import com.bank.transfers.domains.Bank;

import java.util.ArrayList;

public class BankBuilder {

    public static Bank VALID() {
        return Bank.builder()
                .cnpj("11.111.111/1111-11")
                .name("Banco 1")
                .customersCpfs(new ArrayList<>())
                .build();
    }
}
