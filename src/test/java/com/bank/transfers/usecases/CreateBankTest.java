package com.bank.transfers.usecases;

import com.bank.transfers.builders.domains.BankBuilder;
import com.bank.transfers.builders.http.json.input.BankInputJsonBuilder;
import com.bank.transfers.gateways.BankGateway;
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
public class CreateBankTest {

    @Mock
    private BankGateway bankGateway;

    @InjectMocks
    private CreateBank createBank;

    @Test
    public void create_withValidPayload_shouldCreateBankWithSuccess() {

        final var bankInputJson = BankInputJsonBuilder.VALID();

        final var expectedResult = BankBuilder.VALID();

        when(bankGateway.save(expectedResult)).thenReturn(expectedResult);

        final var result = createBank.create(bankInputJson);

        assertThat(expectedResult, is(result));
    }
}
