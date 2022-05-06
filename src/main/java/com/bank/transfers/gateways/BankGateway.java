package com.bank.transfers.gateways;

import com.bank.transfers.domains.Bank;

public interface BankGateway {

    Bank findById(String cnpj);

    Bank save(Bank bank);
}
