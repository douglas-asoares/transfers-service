package com.bank.transfers.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Transfer {

    @Id
    private String id;

    private AccountId fromAccountId;

    private AccountId toAccountId;

    private Long amount;
}
