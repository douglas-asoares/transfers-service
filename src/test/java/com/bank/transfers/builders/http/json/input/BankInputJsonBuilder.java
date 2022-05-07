package com.bank.transfers.builders.http.json.input;

import com.bank.transfers.http.json.input.BankInputJson;

public class BankInputJsonBuilder {

    public static BankInputJson VALID() {
        return BankInputJson.builder()
                .cnpj("11.111.111/1111-11")
                .name("Banco 1")
                .build();
    }
}
