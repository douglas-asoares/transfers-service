package com.bank.transfers.builders.http.json.input;

import com.bank.transfers.http.json.input.TransferAccountInputJson;

public class TransferAccountInputJsonBuilder {

    public static TransferAccountInputJson VALID_WITH(final String bankCnpj, final String customerCpf) {
        return TransferAccountInputJson.builder()
                .bankCnpj(bankCnpj)
                .customerCpf(customerCpf)
                .build();
    }
}
