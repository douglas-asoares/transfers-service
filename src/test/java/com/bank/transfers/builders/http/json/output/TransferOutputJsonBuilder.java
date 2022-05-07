package com.bank.transfers.builders.http.json.output;

import com.bank.transfers.http.json.output.TransferOutputJson;

public class TransferOutputJsonBuilder {

    public static TransferOutputJson VALID() {
        return TransferOutputJson.builder()
                .amount(500L)
                .senderCustomerCpf("111.111.111-11")
                .receiverCustomerCpf("222.222.222-22")
                .build();
    }
}
