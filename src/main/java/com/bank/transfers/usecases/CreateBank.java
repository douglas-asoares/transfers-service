package com.bank.transfers.usecases;

import com.bank.transfers.domains.Bank;
import com.bank.transfers.gateways.BankGateway;
import com.bank.transfers.http.json.input.BankInputJson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CreateBank {

    private final BankGateway bankGateway;

    public Bank create(final BankInputJson bankInputJson) {

        return bankGateway.save(toDomain(bankInputJson));
    }

    private Bank toDomain(final BankInputJson bankInputJson) {
        return Bank.builder()
                .cnpj(bankInputJson.getCnpj())
                .name(bankInputJson.getName())
                .customersCpfs(new ArrayList<>())
                .build();
    }
}
