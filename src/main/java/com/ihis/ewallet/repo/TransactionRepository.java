package com.ihis.ewallet.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ihis.ewallet.dtos.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, String>  {
	Iterable<Transaction> findByIdIn(List<String> ids);
}
