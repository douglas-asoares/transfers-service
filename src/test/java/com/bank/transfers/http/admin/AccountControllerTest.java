package com.bank.transfers.http.admin;

import com.bank.transfers.builders.domains.AccountBuilder;
import com.bank.transfers.builders.http.json.input.AccountInputJsonBuilder;
import com.bank.transfers.exceptions.AccountNotFoundException;
import com.bank.transfers.exceptions.BankNotFoundException;
import com.bank.transfers.exceptions.CustomerNotFoundException;
import com.bank.transfers.usecases.AccountCrud;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class AccountControllerTest {

    private static final String API_URL = "/api/account";

    private static final String DEPOSIT_ENDPOINT = API_URL + "/deposit";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Mock
    private AccountCrud accountCrud;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final var controller = new AccountController(accountCrud);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void create_withValidPayload_shouldCreateAccountWithSuccess() throws Exception {

        final var accountInputJson = AccountInputJsonBuilder.VALID();

        final var createdAccount = AccountBuilder.VALID();
        when(accountCrud.create(accountInputJson)).thenReturn(createdAccount);

        final MvcResult mvcResult = mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(accountInputJson)))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus(), is(OK.value()));
    }

    @Test
    public void create_withValidPayload_shouldThrowExceptionWhileCreatingAccount() throws Exception {

        final var accountInputJson = AccountInputJsonBuilder.VALID();
        doThrow(new RuntimeException()).when(accountCrud).create(accountInputJson);

        final MvcResult mvcResult = mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(accountInputJson)))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus(), is(INTERNAL_SERVER_ERROR.value()));
    }

    @Test
    public void create_withCustomerNotFound_shouldThrowCustomerNotFoundException() throws Exception {

        final var accountInputJson = AccountInputJsonBuilder.VALID();

        final var errorMessage = String.format("Customer with CPF %s not found", accountInputJson.getCustomerCpf());
        doThrow(new CustomerNotFoundException(errorMessage)).when(accountCrud).create(accountInputJson);

        final MvcResult mvcResult = mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(accountInputJson)))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus(), is(BAD_REQUEST.value()));
        assertThat(mvcResult.getResponse().getContentAsString(), is(errorMessage));
    }

    @Test
    public void create_withBankNotFound_shouldThrowBankNotFoundException() throws Exception {

        final var accountInputJson = AccountInputJsonBuilder.VALID();

        final var errorMessage = String.format("Bank with CNPJ %s not found", accountInputJson.getBankCnpj());
        doThrow(new BankNotFoundException(errorMessage)).when(accountCrud).create(accountInputJson);

        final MvcResult mvcResult = mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(accountInputJson)))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus(), is(BAD_REQUEST.value()));
        assertThat(mvcResult.getResponse().getContentAsString(), is(errorMessage));
    }

    @Test
    public void deposit_withValidPayload_shouldDepositMoneyIntoAccountWithSuccess() throws Exception {

        final var accountInputJson = AccountInputJsonBuilder.VALID();

        final var updatedAccount = AccountBuilder.VALID();
        when(accountCrud.depositMoney(accountInputJson)).thenReturn(updatedAccount);

        final MvcResult mvcResult = mockMvc.perform(post(DEPOSIT_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(accountInputJson)))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus(), is(OK.value()));
    }

    @Test
    public void deposit_withValidPayload_shouldThrowExceptionWhileDepositingMoneyIntoAccount() throws Exception {

        final var accountInputJson = AccountInputJsonBuilder.VALID();
        doThrow(new RuntimeException()).when(accountCrud).depositMoney(accountInputJson);

        final MvcResult mvcResult = mockMvc.perform(post(DEPOSIT_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(accountInputJson)))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus(), is(INTERNAL_SERVER_ERROR.value()));
    }

    @Test
    public void deposit_withAccountNotFound_shouldThrowAccountNotFoundException() throws Exception {

        final var accountInputJson = AccountInputJsonBuilder.VALID();

        final var errorMessage = String.format("Account with CPF %s and Bank with CNPJ %s not found", accountInputJson.getCustomerCpf(), accountInputJson.getBankCnpj());
        doThrow(new AccountNotFoundException(errorMessage)).when(accountCrud).depositMoney(accountInputJson);

        final MvcResult mvcResult = mockMvc.perform(post(DEPOSIT_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(accountInputJson)))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus(), is(BAD_REQUEST.value()));
        assertThat(mvcResult.getResponse().getContentAsString(), is(errorMessage));
    }
}
