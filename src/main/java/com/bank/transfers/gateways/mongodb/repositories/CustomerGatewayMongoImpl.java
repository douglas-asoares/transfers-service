package com.bank.transfers.gateways.mongodb.repositories;

import com.bank.transfers.domains.Customer;
import com.bank.transfers.exceptions.CustomerNotFoundException;
import com.bank.transfers.gateways.CustomerGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerGatewayMongoImpl implements CustomerGateway {

    private final CustomerRepository customerRepository;

    @Override
    public Customer findById(final String cpf) {
        return customerRepository.findById(cpf).orElseThrow(() -> {
                    final var message = String.format("Customer with CPF %s not found", cpf);
                    return new CustomerNotFoundException(message);
                }
        );
    }

    @Override
    public Customer save(final Customer customer) {
        return customerRepository.save(customer);
    }
}
