package com.bank.transfers.builders.domains;

import com.bank.transfers.domains.Customer;

import java.util.ArrayList;

public class CustomerBuilder {

    public static Customer VALID() {
        return Customer.builder()
                .cpf("111.111.111-11")
                .name("Cliente 1")
                .email("cliente1@teste.com.br")
                .phone("(11)11111-1111")
                .accountsIds(new ArrayList<>())
                .build();
    }
}
