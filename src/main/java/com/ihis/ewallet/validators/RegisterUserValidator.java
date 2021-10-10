package com.ihis.ewallet.validators;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ihis.ewallet.MessageConstants;
import com.ihis.ewallet.repo.EwalletUserRepository;
import com.ihis.ewallet.responsedtos.RegisterUserRequest;

@Service
public class RegisterUserValidator {
	
	@Autowired
	EwalletUserRepository eWalletUserRepository;
	
	/**
	 * validate registering of user.
	 * 
	 * @param user registering user
	 * @return if user has pass validation
	 */
	public boolean validate(RegisterUserRequest user, List<String> errorList) {
		//check if user is pass basic checks from javax validator
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Set<ConstraintViolation<RegisterUserRequest>> violations = factory.getValidator().validate(user);
		if(violations.size() > 0) {
			errorList.addAll(
				violations.stream().map(violation -> violation.getMessage()).collect(Collectors.toList())
			);
			return false;
		}
		
		//check if user is registered
		if(eWalletUserRepository.findByEmail(user.getEmail()).isPresent()) {
			errorList.add(MessageConstants.ERROR_DUPLICATE_EMAIL_MSG);
			return false;
		}
		
		return true;
	}

}
