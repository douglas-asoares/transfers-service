package com.bank.transfers.usecases;

import com.bank.transfers.domains.Account;
import com.bank.transfers.domains.AccountId;
import com.bank.transfers.domains.Customer;
import com.bank.transfers.gateways.AccountGateway;
import com.bank.transfers.gateways.BankGateway;
import com.bank.transfers.gateways.CustomerGateway;
import com.bank.transfers.http.json.input.AccountInputJson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AccountCrud {

    private final AccountGateway accountGateway;

    private final CustomerGateway customerGateway;

    private final BankGateway bankGateway;

    public void create(final AccountInputJson accountInputJson) {

        final var customer = customerGateway.findById(accountInputJson.getCustomerCpf());

        associateCustomerToBank(accountInputJson);

        final var account = accountGateway.save(toDomain(accountInputJson));

        associateAccountToCustomer(customer, account);
    }

    private void associateCustomerToBank(final AccountInputJson accountInputJson) {

        final var bank = bankGateway.findById(accountInputJson.getBankCnpj());
        bank.getCustomersCpfs().add(accountInputJson.getCustomerCpf());
        bankGateway.save(bank);
    }

    private void associateAccountToCustomer(final Customer customer, final Account account) {

        customer.getAccountsIds().add(account.getId());
        customerGateway.save(customer);
    }

    private Account toDomain(final AccountInputJson accountInputJson) {
        return Account.builder()
                .id(new AccountId(accountInputJson.getCustomerCpf(), accountInputJson.getBankCnpj()))
                .moneyAmount(accountInputJson.getMoneyAmount())
                .transfers(new ArrayList<>())
                .build();
    }

    public void depositMoney(final AccountInputJson accountInputJson) {
        final var account = accountGateway.findById(accountInputJson.getCustomerCpf(),
                accountInputJson.getBankCnpj());
        account.setMoneyAmount(account.getMoneyAmount() + accountInputJson.getMoneyAmount());
        accountGateway.save(account);
    }
}