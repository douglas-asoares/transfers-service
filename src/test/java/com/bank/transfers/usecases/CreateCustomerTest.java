package com.bank.transfers.usecases;

import com.bank.transfers.builders.domains.CustomerBuilder;
import com.bank.transfers.builders.http.json.input.CustomerInputJsonBuilder;
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
public class CreateCustomerTest {

    @Mock
    private CustomerGateway customerGateway;

    @InjectMocks
    private CreateCustomer createCustomer;

    @Test
    public void create_withValidPayload_shouldCreateCustomerWithSuccess() {

        final var customerInputJson = CustomerInputJsonBuilder.VALID();

        final var expectedResult = CustomerBuilder.VALID();

        when(customerGateway.save(expectedResult)).thenReturn(expectedResult);

        final var result = createCustomer.create(customerInputJson);

        assertThat(expectedResult, is(result));
    }
}
