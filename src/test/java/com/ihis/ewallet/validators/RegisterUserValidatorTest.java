package com.ihis.ewallet.validators;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.quality.Strictness;

import com.ihis.ewallet.dtos.EWalletUser;
import com.ihis.ewallet.repo.EwalletUserRepository;
import com.ihis.ewallet.responsedtos.RegisterUserRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@MockitoSettings(strictness = Strictness.LENIENT)
public class RegisterUserValidatorTest {
	
	@Mock
	EwalletUserRepository eWalletUserRepository;
	
	@InjectMocks
	RegisterUserValidator registerUserValidator;
	
	@ParameterizedTest
	@CsvSource({
		"'When User enter empty email address','',false,false",
		"'When user enter invalid email address','asd@asd',false,false",
		"'When user enter invalid email address','asd',false,false",
		"'When user enter invalid email address','.asd@asd.com',false,false",
		"'When user enter invalid email address','asd/asd@.com',false,false",
		"'When user enter invalid email address','asd asd@.com',false,false",
		"'When email address is found in repo','asd@asd.com',true,false",
		"'When user enter correct email address','asd@asd.com',false,true",
		"'When user enter correct email address','asd.asd@asd.com',false,true"
	})
	void testValidateWhenUserRegister(String description, String email, boolean isEmailFoundInRepo, boolean expectedResult) {
		log.info("testing ---"+description);
		log.info(email);
		log.info(isEmailFoundInRepo + "");
		log.info(expectedResult + "");
		
		//Mocks
		Optional<EWalletUser> emailInRepo;
		if(isEmailFoundInRepo) {
			emailInRepo = Optional.of(new EWalletUser());
		}else {
			emailInRepo = Optional.empty();
		}
		
		when(eWalletUserRepository.findByEmail(Mockito.anyString())).thenReturn(emailInRepo);
		
		//Assert
		RegisterUserRequest userRequest = new RegisterUserRequest();
		userRequest.setEmail(email);
		
		List<String> errorList = new ArrayList<>();
		boolean result = registerUserValidator.validate(userRequest,errorList);
		errorList.forEach(log::info);
		Assertions.assertEquals(expectedResult, result);
		
	}
}
