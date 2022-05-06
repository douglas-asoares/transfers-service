package com.bank.transfers.http;

import com.bank.transfers.exceptions.*;
import com.bank.transfers.http.json.input.TransferInputJson;
import com.bank.transfers.http.json.output.TransferOutputJson;
import com.bank.transfers.usecases.PerformsTransfer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@RequestMapping("/api/transfer")
@Api(
        value = "Performs transfer between bank account",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE,
        tags = {"Rest endpoint for transfers service"})
public class TransferController {

    private final ObjectMapper objectMapper;

    private final PerformsTransfer performsTransfer;

    @PostMapping
    @ApiOperation(
            value = "Performs transfer between bank accounts")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK"),
                    @ApiResponse(code = 500, message = "Internal Server Error")
            })
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<String> performsTransfer(
            @RequestBody @Valid final TransferInputJson transferInputJson) {

        log.info("Request received {}", transferInputJson);

        try {
            return ResponseEntity.ok(payloadToString(performsTransfer.execute(transferInputJson)));
        } catch (final AccountNotFoundException | BankNotFoundException | CustomerNotFoundException |
                       FailedInterBankTransferException | NotAllowedTransferException ex) {
            log.error("An error occurred while performing transfer: {}",
                    ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (final Exception ex) {
            log.error("An error occurred while performing transfer: {}",
                    ex.getMessage());
            throw ex;
        }
    }

    private String payloadToString(final TransferOutputJson transferOutputJson) {
        try {
            return objectMapper.writeValueAsString(transferOutputJson);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
