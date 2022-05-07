package com.bank.transfers.usecases;

import com.bank.transfers.domains.Customer;
import com.bank.transfers.gateways.CustomerGateway;
import com.bank.transfers.http.json.input.CustomerInputJson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CreateCustomer {

    private final CustomerGateway customerGateway;

    public Customer create(final CustomerInputJson customerInputJson) {

        return customerGateway.save(toDomain(customerInputJson));
    }

    private Customer toDomain(final CustomerInputJson customerInputJson) {
        return Customer.builder()
                .cpf(customerInputJson.getCpf())
                .name(customerInputJson.getName())
                .phone(customerInputJson.getPhone())
                .email(customerInputJson.getEmail())
                .accountsIds(new ArrayList<>())
                .build();
    }
}
