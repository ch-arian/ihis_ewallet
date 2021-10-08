package com.ihis.ewallet.repo;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface TestUserRepository extends CrudRepository<com.ihis.ewallet.repo.TestUser, String>{

}
