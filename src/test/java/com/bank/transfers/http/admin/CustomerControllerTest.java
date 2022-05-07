package com.bank.transfers.http.admin;

import com.bank.transfers.builders.domains.CustomerBuilder;
import com.bank.transfers.builders.http.json.input.CustomerInputJsonBuilder;
import com.bank.transfers.usecases.CreateCustomer;
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
public class CustomerControllerTest {

    private static final String API_URL = "/api/customer";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Mock
    private CreateCustomer createCustomer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final var controller = new CustomerController(createCustomer);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void create_withValidPayload_shouldCreateCustomerWithSuccess() throws Exception {

        final var customerInputJson = CustomerInputJsonBuilder.VALID();

        final var createdCustomer = CustomerBuilder.VALID();
        when(createCustomer.create(customerInputJson)).thenReturn(createdCustomer);

        final MvcResult mvcResult = mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(customerInputJson)))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus(), is(OK.value()));
    }

    @Test
    public void create_withValidPayload_shouldThrowExceptionWhileCreatingCustomer() throws Exception {

        final var customerInputJson = CustomerInputJsonBuilder.VALID();
        doThrow(new RuntimeException()).when(createCustomer).create(customerInputJson);

        final MvcResult mvcResult = mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(customerInputJson)))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus(), is(INTERNAL_SERVER_ERROR.value()));
    }
}
