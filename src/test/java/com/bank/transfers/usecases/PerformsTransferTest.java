package com.bank.transfers.usecases;

import com.bank.transfers.builders.domains.AccountBuilder;
import com.bank.transfers.builders.http.json.input.TransferInputJsonBuilder;
import com.bank.transfers.builders.http.json.output.TransferOutputJsonBuilder;
import com.bank.transfers.exceptions.NotAllowedTransferException;
import com.bank.transfers.gateways.AccountGateway;
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
public class PerformsTransferTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private ApplyInterBankTransferRules applyInterBankTransferRules;

    @Mock
    private ApplyIntraBankTransferRules applyIntraBankTransferRules;

    @InjectMocks
    private PerformsTransfer performsTransfer;

    @Test
    public void execute_withValidIntraBankPayload_shouldPerfomsTransferWithSuccess() {

        final var transferInputJson = TransferInputJsonBuilder.VALID_INTRA_BANK_TRANSFER();

        final var accountFrom = AccountBuilder.VALID();

        when(accountGateway.findById(transferInputJson.getFromAccount().getCustomerCpf(), transferInputJson.getFromAccount().getBankCnpj()))
                .thenReturn(accountFrom);

        final var accountTo = AccountBuilder.VALID_WITH("222.222.222-22", "11.111.111/1111-11", 300L);

        when(accountGateway.findById(transferInputJson.getToAccount().getCustomerCpf(), transferInputJson.getToAccount().getBankCnpj()))
                .thenReturn(accountTo);

        final var expectedResult = TransferOutputJsonBuilder.VALID();

        when(applyIntraBankTransferRules.execute(accountFrom, accountTo, transferInputJson.getAmount())).thenReturn(expectedResult);

        final var result = performsTransfer.execute(transferInputJson);

        assertThat(result, is(expectedResult));
    }

    @Test
    public void execute_withValidInterBankPayload_shouldPerfomsTransferWithSuccess() {

        final var transferInputJson = TransferInputJsonBuilder.VALID_INTER_BANK_TRANSFER();

        final var accountFrom = AccountBuilder.VALID();

        when(accountGateway.findById(transferInputJson.getFromAccount().getCustomerCpf(), transferInputJson.getFromAccount().getBankCnpj()))
                .thenReturn(accountFrom);

        final var accountTo = AccountBuilder.VALID_WITH("222.222.222-22", "22.222.222/2222-22", 300L);

        when(accountGateway.findById(transferInputJson.getToAccount().getCustomerCpf(), transferInputJson.getToAccount().getBankCnpj()))
                .thenReturn(accountTo);

        final var expectedResult = TransferOutputJsonBuilder.VALID();

        when(applyInterBankTransferRules.execute(accountFrom, accountTo, transferInputJson.getAmount())).thenReturn(expectedResult);

        final var result = performsTransfer.execute(transferInputJson);

        assertThat(result, is(expectedResult));
    }

    @Test(expected = NotAllowedTransferException.class)
    public void execute_withUnavailableMoney_shouldThrowNotAllowedException() {

        final var transferInputJson = TransferInputJsonBuilder.VALID_INTER_BANK_TRANSFER();

        final var accountFrom = AccountBuilder.VALID_WITH("111.111.111-11", "11.111.111/1111-11", 300L);

        when(accountGateway.findById(transferInputJson.getFromAccount().getCustomerCpf(), transferInputJson.getFromAccount().getBankCnpj()))
                .thenReturn(accountFrom);

        performsTransfer.execute(transferInputJson);
    }
}
