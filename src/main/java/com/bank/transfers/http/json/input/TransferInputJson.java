package com.bank.transfers.http.json.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferInputJson {

    private TransferAccountInputJson fromAccount;

    private TransferAccountInputJson toAccount;

    private Long amount;
}
