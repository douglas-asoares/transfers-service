package com.bank.transfers.gateways;

import com.bank.transfers.domains.Account;

public interface AccountGateway {

    Account findById(String customerCpf, String bankCnpj);

    Account save(Account account);
}
