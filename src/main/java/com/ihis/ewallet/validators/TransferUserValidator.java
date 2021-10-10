package com.ihis.ewallet.validators;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.AssertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.ihis.ewallet.MessageConstants;
import com.ihis.ewallet.dtos.EWalletUser;
import com.ihis.ewallet.repo.EwalletUserRepository;
import com.ihis.ewallet.responsedtos.TransferDetailsRequest;

@Service
public class TransferUserValidator {
	
	@Autowired
	EwalletUserRepository eWalletUserRepository;
	
	public boolean validate(TransferDetailsRequest transferDetails, List<String> errorList) {
		
		//validate basic javax validation
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Set<ConstraintViolation<TransferDetailsRequest>> violations = factory.getValidator().validate(transferDetails);
		if(violations.size() > 0) {
			errorList.addAll(
				violations.stream().map(violation -> violation.getMessage()).collect(Collectors.toList())
			);
			return false;
		}
		
		if(transferDetails.getEmail().equals(transferDetails.getTransferee())) {
			errorList.add(MessageConstants.ERROR_SAME_EMAIL);
			return false;
		}
		
		List<EWalletUser> users = new ArrayList<>();
		eWalletUserRepository.findByEmailIn(Arrays.asList(transferDetails.getEmail(), transferDetails.getTransferee())).forEach(user -> users.add(user));
		

		//validate if users exist
		if(users.size() != 2) {
			errorList.add(MessageConstants.ERROR_TRANSFER_USER_USER_NOT_FOUND);
			return false;
		}
		
		//validate if user have enough balance
		EWalletUser fromUser = users.stream().filter(repoUser -> repoUser.getEmail().equals(transferDetails.getEmail())).findFirst().get();
		BigDecimal finalBalance = fromUser.getBalance().subtract(transferDetails.getAmount());
		if(finalBalance.compareTo(BigDecimal.ZERO) < 0) {
			errorList.add(MessageConstants.ERROR_TRANSFER_USER_NOT_ENOUGH_BALANCE);
			return false;
		}
		
		return true;
	}

}
