package com.bank.transfers.gateways.mongodb.repositories;

import com.bank.transfers.domains.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
}
