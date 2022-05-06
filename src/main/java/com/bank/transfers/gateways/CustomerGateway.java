package com.bank.transfers.gateways;

import com.bank.transfers.domains.Customer;

public interface CustomerGateway {

    Customer findById(String cpf);

    Customer save(Customer customer);
}
