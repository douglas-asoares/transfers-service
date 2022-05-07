package com.bank.transfers.http;

import com.bank.transfers.builders.http.json.input.TransferInputJsonBuilder;
import com.bank.transfers.builders.http.json.output.TransferOutputJsonBuilder;
import com.bank.transfers.exceptions.AccountNotFoundException;
import com.bank.transfers.exceptions.FailedInterBankTransferException;
import com.bank.transfers.exceptions.NotAllowedTransferException;
import com.bank.transfers.usecases.PerformsTransfer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class TransferControllerTest {

    private static final String API_URL = "/api/transfer";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Mock
    private PerformsTransfer performsTransfer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final var controller = new TransferController(objectMapper, performsTransfer);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void performsTransfer_withValidPayload_shouldPerformsTransferWithSuccess() throws Exception {

        final var transferInputJson = TransferInputJsonBuilder.VALID_INTER_BANK_TRANSFER();

        final var transferOutputJson = TransferOutputJsonBuilder.VALID();
        final var expectedPayload = objectMapper.writeValueAsString(transferOutputJson);

        when(performsTransfer.execute(transferInputJson)).thenReturn(transferOutputJson);

        final MvcResult mvcResult = mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(transferInputJson)))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus(), is(OK.value()));
        assertThat(mvcResult.getResponse().getContentAsString(), is(expectedPayload));
    }

    @Test(expected = NestedServletException.class)
    public void performsTransfer_withValidPayload_shouldThrowExceptionWhilePerformingTransfer() throws Exception {

        final var transferInputJson = TransferInputJsonBuilder.VALID_INTER_BANK_TRANSFER();
        doThrow(new RuntimeException()).when(performsTransfer).execute(transferInputJson);

        final MvcResult mvcResult = mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(transferInputJson)))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus(), is(INTERNAL_SERVER_ERROR.value()));
    }

    @Test
    public void deposit_withAccountNotFound_shouldThrowAccountNotFoundException() throws Exception {

        final var transferInputJson = TransferInputJsonBuilder.VALID_INTER_BANK_TRANSFER();

        final var errorMessage = String.format("Account with CPF %s and Bank with CNPJ %s not found", transferInputJson.getFromAccount().getCustomerCpf(), transferInputJson.getFromAccount().getBankCnpj());
        doThrow(new AccountNotFoundException(errorMessage)).when(performsTransfer).execute(transferInputJson);

        final MvcResult mvcResult = mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(transferInputJson)))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus(), is(BAD_REQUEST.value()));
        assertThat(mvcResult.getResponse().getContentAsString(), is(errorMessage));
    }

    @Test
    public void performsTransfer_withFailedInterBankTransfer_shouldThrowFailedInterBankTransferException() throws Exception {

        final var transferInputJson = TransferInputJsonBuilder.VALID_INTER_BANK_TRANSFER();

        final var errorMessage = "Inter bank transfer has failed";
        doThrow(new FailedInterBankTransferException(errorMessage)).when(performsTransfer).execute(transferInputJson);

        final MvcResult mvcResult = mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(transferInputJson)))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus(), is(BAD_REQUEST.value()));
        assertThat(mvcResult.getResponse().getContentAsString(), is(errorMessage));
    }

    @Test
    public void performsTransfer_withExceededTransferLimit_shouldThrowNotAllowedTransferException() throws Exception {

        final var transferInputJson = TransferInputJsonBuilder.VALID_INTER_BANK_TRANSFER();

        final var errorMessage = "Transfer limit exceeded: limit is R$5000 per transfer";
        doThrow(new NotAllowedTransferException(errorMessage)).when(performsTransfer).execute(transferInputJson);

        final MvcResult mvcResult = mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(transferInputJson)))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus(), is(BAD_REQUEST.value()));
        assertThat(mvcResult.getResponse().getContentAsString(), is(errorMessage));
    }

    @Test
    public void performsTransfer_withUnavailableMoneyInAccountFrom_shouldThrowNotAllowedTransferException() throws Exception {

        final var transferInputJson = TransferInputJsonBuilder.VALID_INTER_BANK_TRANSFER();

        final var errorMessage = "Unavailable money in account from";
        doThrow(new NotAllowedTransferException(errorMessage)).when(performsTransfer).execute(transferInputJson);

        final MvcResult mvcResult = mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(transferInputJson)))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus(), is(BAD_REQUEST.value()));
        assertThat(mvcResult.getResponse().getContentAsString(), is(errorMessage));
    }
}
