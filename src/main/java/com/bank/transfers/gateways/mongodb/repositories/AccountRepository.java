package com.bank.transfers.gateways.mongodb.repositories;

import com.bank.transfers.domains.Account;
import com.bank.transfers.domains.AccountId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<Account, AccountId> {
}
