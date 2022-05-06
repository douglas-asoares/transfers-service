package com.bank.transfers.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountId {

    private String customerCpf;

    private String bankCnpj;
}
