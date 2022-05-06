package com.bank.transfers.http.admin;

import com.bank.transfers.http.json.input.CustomerInputJson;
import com.bank.transfers.usecases.CreateCustomer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
@Api(
        value = "Allow CRUD operations with customers data",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE,
        tags = {"Rest endpoint for management customers data"})
public class CustomerController {

    private final CreateCustomer createCustomer;

    @PostMapping
    @ApiOperation(
            value = "Create new Customer")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK"),
                    @ApiResponse(code = 500, message = "Internal Server Error")
            })
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<HttpStatus> create(
            @RequestBody @Valid final CustomerInputJson customerInputJson) {

        log.info("Request received to create a customer: {}", customerInputJson);

        try {
            createCustomer.create(customerInputJson);
            return ResponseEntity.ok().build();
        } catch (final Exception ex) {
            log.error("An error occurred while creating customer: {}",
                    ex.getMessage());
            throw ex;
        }
    }
}
