package com.ihis.ewallet.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ihis.ewallet.Constants;
import com.ihis.ewallet.dtos.EWalletUser;
import com.ihis.ewallet.dtos.Transaction;
import com.ihis.ewallet.repo.EwalletUserRepository;
import com.ihis.ewallet.repo.TransactionRepository;
import com.ihis.ewallet.responsedtos.RegisterUserRequest;
import com.ihis.ewallet.responsedtos.RegisterUserResponse;
import com.ihis.ewallet.responsedtos.TransferDetailsRequest;
import com.ihis.ewallet.responsedtos.TransferDetailsResponse;
import com.ihis.ewallet.responsedtos.ViewBalanceResponse;
import com.ihis.ewallet.responsedtos.ViewTransactionResponse;
import com.ihis.ewallet.validators.RegisterUserValidator;
import com.ihis.ewallet.validators.TransferUserValidator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EwalletUserService {
	
	@Autowired
	EwalletUserRepository eWalletUserRepository;
	@Autowired
	TransactionRepository transactionRepository;
	
	@Autowired
	RegisterUserValidator registerUserValidator;
	
	@Autowired
	TransferUserValidator transferUserValidator;
	
	public RegisterUserResponse registerUser(RegisterUserRequest user, List<String> errorList) {
		
		//validate user
		boolean validate = registerUserValidator.validate(user, errorList);
		log.debug("validate result :" + validate);
		if(validate) {
			log.debug("---saving");
			/*validate success*/
			
			//save to db
			EWalletUser eWalletUser = new EWalletUser();
			//add initial amount
			eWalletUser.setBalance(Constants.INITIAL_REGISTER_BALANCE);
			eWalletUser.setEmail(user.getEmail());
			eWalletUser.setDteCreated(LocalDateTime.now());
			eWalletUser.setDteLastUpdated(LocalDateTime.now());
			eWalletUser.setUpdatedBy(Constants.UPDATE_BY_SYSTEM);
			
			eWalletUserRepository.save(eWalletUser);

			RegisterUserResponse response = new RegisterUserResponse();
			response.setBalance(eWalletUser.getBalance());
			response.setSuccess(true);
			return response;
		}
		return null;
	}
	
	public TransferDetailsResponse transferUser(TransferDetailsRequest transferDetails, List<String> errorList) {
		
		//validate user transfer details
		boolean validate = transferUserValidator.validate(transferDetails, errorList);
		log.debug("validate result :" + validate);
		if(validate) {
			log.debug("---saving");
			/*validate success*/
			
			//amend balance and add transaction
			List<EWalletUser> users = new ArrayList<>();
			eWalletUserRepository.findByEmailIn(Arrays.asList(transferDetails.getEmail(), transferDetails.getTransferee())).forEach(user -> users.add(user));
			
			if(users.isEmpty()) {
				return null;
			}
			String uniqueID = UUID.randomUUID().toString();
			Transaction transactionDetails = Transaction.format(transferDetails, uniqueID, LocalDateTime.now());
			users.forEach(user ->{
				//user is transferrer
				if(user.getEmail().equals(transferDetails.getEmail())) {
					user.setBalance(user.getBalance().subtract(transferDetails.getAmount()));
					user.getTransactionIds().add(uniqueID);
				}
				//user is transferee
				if(user.getEmail().equals(transferDetails.getTransferee())) {
					user.setBalance(user.getBalance().add(transferDetails.getAmount()));
					user.getTransactionIds().add(uniqueID);
				}
			});
			
			//save to db
			eWalletUserRepository.saveAll(users);
			transactionRepository.save(transactionDetails);
			
			TransferDetailsResponse response = new TransferDetailsResponse();
			response.setSuccess(true);
			return response;
		}
		
		return null;
	}
	
	public ViewBalanceResponse viewBalance(String email) {
		ViewBalanceResponse resp = new ViewBalanceResponse();
		Optional<EWalletUser> userOpt = eWalletUserRepository.findByEmail(email);
		if(userOpt.isPresent()) {
			resp.setBalance(userOpt.get().getBalance());
		}
		return resp;
	}
	
	public ViewTransactionResponse viewTransaction(String email) {
		ViewTransactionResponse resp = new ViewTransactionResponse();
		Optional<EWalletUser> userOpt = eWalletUserRepository.findByEmail(email);
		if(!userOpt.isPresent()) {
			return null;
		}
		List<Transaction> transactions = new ArrayList<>();
		for(Transaction trans : transactionRepository.findAllById(userOpt.get().getTransactionIds())) {
			transactions.add(trans);
		}
		resp.setSuccess(true);
		resp.setTransactions(transactions);
		return resp;
	}
}
