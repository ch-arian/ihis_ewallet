package com.ihis.ewallet.repo;

import java.util.List;
import java.util.Optional;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ihis.ewallet.dtos.EWalletUser;

@Repository
@EnableScan
public interface EwalletUserRepository extends CrudRepository<EWalletUser, String> {
	Optional<EWalletUser> findByEmail(String email);
	Iterable<EWalletUser> findByEmailIn(List<String> emails);
}
