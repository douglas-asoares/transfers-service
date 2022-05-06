package com.bank.transfers.gateways.mongodb.repositories;

import com.bank.transfers.domains.Bank;
import com.bank.transfers.exceptions.BankNotFoundException;
import com.bank.transfers.gateways.BankGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BankGatewayMongoImpl implements BankGateway {

    private final BankRepository bankRepository;

    @Override
    public Bank findById(final String cnpj) {
        return bankRepository.findById(cnpj).orElseThrow(() -> {
            final var message = String.format("Bank with CNPJ %s not found", cnpj);
            return new BankNotFoundException(message);
        });
    }

    @Override
    public Bank save(final Bank bank) {
        return bankRepository.save(bank);
    }
}
