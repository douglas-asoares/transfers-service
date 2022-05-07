package com.bank.transfers.builders.http.json.input;

import com.bank.transfers.http.json.input.CustomerInputJson;

public class CustomerInputJsonBuilder {

    public static CustomerInputJson VALID() {
        return CustomerInputJson.builder()
                .cpf("111.111.111-11")
                .name("Cliente 1")
                .email("cliente1@teste.com.br")
                .phone("(11)11111-1111")
                .build();
    }
}
