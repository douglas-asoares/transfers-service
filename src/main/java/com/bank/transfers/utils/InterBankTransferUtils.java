package com.bank.transfers.utils;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
@NoArgsConstructor
public class InterBankTransferUtils {

    public boolean shouldTransferFail() {
        return ThreadLocalRandom.current().nextInt(1, 11) <= 3;
    }
}
