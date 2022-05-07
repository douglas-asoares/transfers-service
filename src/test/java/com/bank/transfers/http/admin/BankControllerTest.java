package com.bank.transfers.http.admin;

import com.bank.transfers.builders.domains.BankBuilder;
import com.bank.transfers.builders.http.json.input.BankInputJsonBuilder;
import com.bank.transfers.usecases.CreateBank;
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
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class BankControllerTest {

    private static final String API_URL = "/api/bank";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Mock
    private CreateBank createBank;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final var controller = new BankController(createBank);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void create_withValidPayload_shouldCreateBankWithSuccess() throws Exception {

        final var bankInputJson = BankInputJsonBuilder.VALID();

        final var createdBank = BankBuilder.VALID();
        when(createBank.create(bankInputJson)).thenReturn(createdBank);

        final MvcResult mvcResult = mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(bankInputJson)))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus(), is(OK.value()));
    }

    @Test
    public void create_withValidPayload_shouldThrowExceptionWhileCreatingBank() throws Exception {

        final var bankInputJson = BankInputJsonBuilder.VALID();
        doThrow(new RuntimeException()).when(createBank).create(bankInputJson);

        final MvcResult mvcResult = mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(bankInputJson)))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus(), is(INTERNAL_SERVER_ERROR.value()));
    }
}
