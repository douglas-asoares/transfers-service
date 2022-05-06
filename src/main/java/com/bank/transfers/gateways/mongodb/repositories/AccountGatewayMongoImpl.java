package com.bank.transfers.gateways.mongodb.repositories;

import com.bank.transfers.domains.Account;
import com.bank.transfers.domains.AccountId;
import com.bank.transfers.exceptions.AccountNotFoundException;
import com.bank.transfers.gateways.AccountGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountGatewayMongoImpl implements AccountGateway {

    private final AccountRepository accountRepository;

    @Override
    public Account findById(final String customerCpf, final String bankCnpj) {
        return accountRepository.findById(new AccountId(customerCpf, bankCnpj)).orElseThrow(() -> {
                    final var message = String.format("Account with CPF %s and Bank with CNPJ %s not found", customerCpf, bankCnpj);
                    return new AccountNotFoundException(message);
                }
        );
    }

    @Override
    public Account save(final Account account) {
        return accountRepository.save(account);
    }
}
