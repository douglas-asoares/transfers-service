package com.bank.transfers.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BankNotFoundException extends RuntimeException {
    
    public BankNotFoundException(final String message) {
        super(message);
    }
}
