package com.bank.transfers.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotAllowedTransferException extends RuntimeException {

    public NotAllowedTransferException(final String message) {
        super(message);
    }
}
