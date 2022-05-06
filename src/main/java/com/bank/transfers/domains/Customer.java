package com.bank.transfers.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Document(collection = "customers")
public class Customer {

    @Id
    private String cpf;

    private String name;

    private String phone;

    private String email;

    private List<AccountId> accountsIds;
}
