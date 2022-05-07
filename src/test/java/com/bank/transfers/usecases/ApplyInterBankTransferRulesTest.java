package com.bank.transfers.usecases;

import com.bank.transfers.builders.domains.AccountBuilder;
import com.bank.transfers.builders.http.json.input.TransferInputJsonBuilder;
import com.bank.transfers.builders.http.json.output.TransferOutputJsonBuilder;
import com.bank.transfers.exceptions.FailedInterBankTransferException;
import com.bank.transfers.exceptions.NotAllowedTransferException;
import com.bank.transfers.gateways.AccountGateway;
import com.bank.transfers.utils.InterBankTransferUtils;
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
public class ApplyInterBankTransferRulesTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private InterBankTransferUtils interBankTransferUtils;

    @InjectMocks
    private ApplyInterBankTransferRules applyInterBankTransferRules;

    @Test(expected = FailedInterBankTransferException.class)
    public void execute_withFailedTransfer_shouldThrowFailedInterBankTransferException() {

        final var transferInputJson = TransferInputJsonBuilder.VALID_INTER_BANK_TRANSFER();

        when(interBankTransferUtils.shouldTransferFail()).thenReturn(true);

        final var accountFrom = AccountBuilder.VALID();

        final var accountTo = AccountBuilder.VALID_WITH("222.222.222-22", "22.222.222/2222-22", 300L);

        applyInterBankTransferRules.execute(accountFrom, accountTo, transferInputJson.getAmount());
    }

    @Test(expected = NotAllowedTransferException.class)
    public void execute_withTransferLimitExceeded_shouldThrowNotAllowedTransferException() {

        final var transferInputJson = TransferInputJsonBuilder.INVALID_INTER_BANK_TRANSFER();

        when(interBankTransferUtils.shouldTransferFail()).thenReturn(false);

        final var accountFrom = AccountBuilder.VALID();

        final var accountTo = AccountBuilder.VALID_WITH("222.222.222-22", "22.222.222/2222-22", 300L);

        applyInterBankTransferRules.execute(accountFrom, accountTo, transferInputJson.getAmount());
    }

    @Test
    public void execute_withValidInterBankPayload_shouldPerformsTransferWithSuccess() {

        final var transferInputJson = TransferInputJsonBuilder.VALID_INTER_BANK_TRANSFER();

        when(interBankTransferUtils.shouldTransferFail()).thenReturn(false);

        final var accountFrom = AccountBuilder.VALID();

        final var accountTo = AccountBuilder.VALID_WITH("222.222.222-22", "22.222.222/2222-22", 300L);

        final var expectedResult = TransferOutputJsonBuilder.VALID();

        final var result = applyInterBankTransferRules.execute(accountFrom, accountTo, transferInputJson.getAmount());

        assertThat(result, is(expectedResult));
        assertThat(accountFrom.getMoneyAmount(), is(495L));
        assertThat(accountTo.getMoneyAmount(), is(800L));
    }
}
