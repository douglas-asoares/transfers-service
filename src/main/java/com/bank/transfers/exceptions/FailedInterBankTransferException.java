package com.bank.transfers.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FailedInterBankTransferException extends RuntimeException {

    public FailedInterBankTransferException(final String message) {
        super(message);
    }
}
