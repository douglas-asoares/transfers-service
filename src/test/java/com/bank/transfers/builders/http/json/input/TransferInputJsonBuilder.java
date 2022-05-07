package com.bank.transfers.builders.http.json.input;

import com.bank.transfers.http.json.input.TransferInputJson;

public class TransferInputJsonBuilder {

    public static TransferInputJson VALID_INTER_BANK_TRANSFER() {
        return TransferInputJson.builder()
                .amount(500L)
                .fromAccount(TransferAccountInputJsonBuilder.VALID_WITH("11.111.111/1111-11", "111.111.111-11"))
                .toAccount(TransferAccountInputJsonBuilder.VALID_WITH("22.222.222/2222-22", "222.222.222-22"))
                .build();
    }

    public static TransferInputJson INVALID_INTER_BANK_TRANSFER() {
        return TransferInputJson.builder()
                .amount(5500L)
                .fromAccount(TransferAccountInputJsonBuilder.VALID_WITH("11.111.111/1111-11", "111.111.111-11"))
                .toAccount(TransferAccountInputJsonBuilder.VALID_WITH("22.222.222/2222-22", "222.222.222-22"))
                .build();
    }

    public static TransferInputJson VALID_INTRA_BANK_TRANSFER() {
        return TransferInputJson.builder()
                .amount(500L)
                .fromAccount(TransferAccountInputJsonBuilder.VALID_WITH("11.111.111/1111-11", "111.111.111-11"))
                .toAccount(TransferAccountInputJsonBuilder.VALID_WITH("11.111.111/1111-11", "222.222.222-22"))
                .build();
    }
}
