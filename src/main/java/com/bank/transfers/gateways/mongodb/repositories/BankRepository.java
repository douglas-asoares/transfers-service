package com.bank.transfers.gateways.mongodb.repositories;

import com.bank.transfers.domains.Bank;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BankRepository extends MongoRepository<Bank, String> {
}
