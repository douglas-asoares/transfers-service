package com.bank.transfers.usecases;

import com.bank.transfers.builders.domains.AccountBuilder;
import com.bank.transfers.builders.domains.BankBuilder;
import com.bank.transfers.builders.domains.CustomerBuilder;
import com.bank.transfers.builders.http.json.input.AccountInputJsonBuilder;
import com.bank.transfers.gateways.AccountGateway;
import com.bank.transfers.gateways.BankGateway;
import com.bank.transfers.gateways.CustomerGateway;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class AccountCrudTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private CustomerGateway customerGateway;

    @Mock
    private BankGateway bankGateway;

    @InjectMocks
    private AccountCrud accountCrud;

    @Test
    public void create_withValidPayload_shouldCreateAccountWithSuccess() {

        final var accountInputJson = AccountInputJsonBuilder.VALID();

        final var customer = CustomerBuilder.VALID();

        when(customerGateway.findById(accountInputJson.getCustomerCpf())).thenReturn(customer);

        final var bank = BankBuilder.VALID();

        when(bankGateway.findById(accountInputJson.getBankCnpj())).thenReturn(bank);

        final var expectedResult = AccountBuilder.VALID();

        when(accountGateway.save(expectedResult)).thenReturn(expectedResult);

        final var result = accountCrud.create(accountInputJson);

        assertThat(expectedResult, is(result));
    }

    @Test
    public void deposit_withValidPayload_shouldDepositMoneyIntoAccountWithSuccess() {

        final var accountInputJson = AccountInputJsonBuilder.VALID();
        final var depositAmount = accountInputJson.getMoneyAmount();

        final var account = AccountBuilder.VALID();
        final var moneyAmountInAccountBeforeDeposit = account.getMoneyAmount();

        when(accountGateway.findById(accountInputJson.getCustomerCpf(),
                accountInputJson.getBankCnpj())).thenReturn(account);

        final var result = accountCrud.depositMoney(accountInputJson);

        assertThat(result.getMoneyAmount(), is(moneyAmountInAccountBeforeDeposit + depositAmount));
    }
}
