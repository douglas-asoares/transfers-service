package com.bank.transfers.usecases;

import com.bank.transfers.builders.domains.AccountBuilder;
import com.bank.transfers.builders.http.json.input.TransferInputJsonBuilder;
import com.bank.transfers.builders.http.json.output.TransferOutputJsonBuilder;
import com.bank.transfers.gateways.AccountGateway;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ApplyIntraBankTransferRulesTest {

    @Mock
    private AccountGateway accountGateway;

    @InjectMocks
    private ApplyIntraBankTransferRules applyIntraBankTransferRules;

    @Test
    public void execute_withValidIntraBankPayload_shouldPerformsTransferWithSuccess() {

        final var transferInputJson = TransferInputJsonBuilder.VALID_INTRA_BANK_TRANSFER();

        final var accountFrom = AccountBuilder.VALID();

        final var accountTo = AccountBuilder.VALID_WITH("222.222.222-22", "11.111.111/1111-11", 300L);

        final var expectedResult = TransferOutputJsonBuilder.VALID();

        final var result = applyIntraBankTransferRules.execute(accountFrom, accountTo, transferInputJson.getAmount());

        assertThat(result, is(expectedResult));
        assertThat(accountFrom.getMoneyAmount(), is(500L));
        assertThat(accountTo.getMoneyAmount(), is(800L));
    }
}
