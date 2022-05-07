package com.bank.transfers.http.admin;

import com.bank.transfers.exceptions.AccountNotFoundException;
import com.bank.transfers.exceptions.BankNotFoundException;
import com.bank.transfers.exceptions.CustomerNotFoundException;
import com.bank.transfers.http.json.input.AccountInputJson;
import com.bank.transfers.usecases.AccountCrud;
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
@RequestMapping("/api/account")
@Api(
        value = "Allow CRUD operations with accounts data",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE,
        tags = {"Rest endpoint for management accounts data"})
public class AccountController {

    private final AccountCrud accountCrud;

    @PostMapping
    @ApiOperation(
            value = "Create new Account")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK"),
                    @ApiResponse(code = 400, message = "Bad Request"),
                    @ApiResponse(code = 500, message = "Internal Server Error")
            })
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<String> create(
            @RequestBody @Valid final AccountInputJson accountInputJson) {

        log.info("Request received to create an account: {}", accountInputJson);

        try {
            accountCrud.create(accountInputJson);
            return ResponseEntity.ok().build();
        } catch (final CustomerNotFoundException | BankNotFoundException ex) {
            log.error("An error occurred while creating account: {}",
                    ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (final Exception ex) {
            log.error("An error occurred while creating account: {}",
                    ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @PostMapping("/deposit")
    @ApiOperation(
            value = "Performs a deposit into an account")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK"),
                    @ApiResponse(code = 400, message = "Bad Request"),
                    @ApiResponse(code = 500, message = "Internal Server Error")
            })
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<String> deposit(
            @RequestBody @Valid final AccountInputJson accountInputJson) {

        log.info("Request received to deposit money into an account: {}", accountInputJson);

        try {
            accountCrud.depositMoney(accountInputJson);
            return ResponseEntity.ok().build();
        } catch (final AccountNotFoundException ex) {
            log.error("An error occurred while depositing money into an account: {}",
                    ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (final Exception ex) {
            log.error("An error occurred while depositing money into an account: {}",
                    ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }
}
