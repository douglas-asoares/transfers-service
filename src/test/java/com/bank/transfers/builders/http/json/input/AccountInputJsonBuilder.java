package com.bank.transfers.builders.http.json.input;

import com.bank.transfers.http.json.input.AccountInputJson;

public class AccountInputJsonBuilder {

    public static AccountInputJson VALID() {
        return AccountInputJson.builder()
                .bankCnpj("11.111.111/1111-11")
                .customerCpf("111.111.111-11")
                .moneyAmount(1000L)
                .build();
    }
}
