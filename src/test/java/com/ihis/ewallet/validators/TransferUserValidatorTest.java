package com.ihis.ewallet.validators;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.ihis.ewallet.dtos.EWalletUser;
import com.ihis.ewallet.dtos.Transaction;
import com.ihis.ewallet.repo.EwalletUserRepository;
import com.ihis.ewallet.responsedtos.TransferDetailsRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@MockitoSettings(strictness = Strictness.LENIENT)
public class TransferUserValidatorTest {
	
	@Mock
	EwalletUserRepository eWalletUserRepository;
	
	@InjectMocks
	TransferUserValidator transferUserValidator;
	
	private static String VALID_EMAIL  = "asd@asd.com";
	private static String[] INVALID_EMAIL_LIST = new String[] {"asd",".asd@asd","asd/asd@.com","asd asd@.com","asd.asd@asd.com"};
	
	//test user not valid email
	@ParameterizedTest
	@MethodSource("combinationOfToAndFromInvalidEamils")
	void testInvalidUserEmail(TransferDetailsRequest transferDetails) {
		Assertions.assertEquals(false, transferUserValidator.validate(transferDetails, new ArrayList<>()));
	}
	
	//test user not enough balance
	@ParameterizedTest
	@ValueSource(strings  = {"1000","101","100.01"})
	void testNotEnoughBalance(String transferAmt) {
		Mockito.when(eWalletUserRepository.findByEmailIn(Arrays.asList("asd@asd.com","asd2@asd.com"))).thenReturn(
			Arrays.asList(
					new EWalletUser("asd@asd.com", BigDecimal.valueOf(100)),
					new EWalletUser("asd2@asd.com", BigDecimal.valueOf(100))
			)
		);
		
		TransferDetailsRequest transferDetails = new TransferDetailsRequest("asd@asd.com","asd2@asd.com", new BigDecimal(transferAmt), Transaction.TRANSFER_TYPE.TRANSFER.name());
		Assertions.assertEquals(false, transferUserValidator.validate(transferDetails, new ArrayList<>()));
	}
	
	//test user not found
	@Test
	void testWhenUserNotFound() {
		Mockito.when(eWalletUserRepository.findByEmailIn(Arrays.asList("asd@asd.com","asd2@asd.com"))).thenReturn(
				Arrays.asList(
						new EWalletUser("asd2@asd.com", BigDecimal.valueOf(100))
				)
			);
		TransferDetailsRequest transferDetails = new TransferDetailsRequest("asd@asd.com","asd2@asd.com", new BigDecimal("1"), Transaction.TRANSFER_TYPE.TRANSFER.name());
		Assertions.assertEquals(false, transferUserValidator.validate(transferDetails, new ArrayList<>()));
	}
	@Test
	void testWhenUserNotFound2() {
		Mockito.when(eWalletUserRepository.findByEmailIn(Arrays.asList("asd@asd.com","asd2@asd.com"))).thenReturn(
				Arrays.asList(
						new EWalletUser("asd@asd.com", BigDecimal.valueOf(100))
				)
			);
		TransferDetailsRequest transferDetails = new TransferDetailsRequest("asd@asd.com","asd2@asd.com", new BigDecimal("1"), Transaction.TRANSFER_TYPE.TRANSFER.name());
		Assertions.assertEquals(false, transferUserValidator.validate(transferDetails, new ArrayList<>()));
	}
	
	//test user transfer amount is not 2 decimal place
	@ParameterizedTest
	@ValueSource(strings  = {"10","10.01","10.1","010.01", "010.1"})
	void testTransferAmtNot2DecimalPlaces(String transferAmt) {
		Mockito.when(eWalletUserRepository.findByEmailIn(Arrays.asList("asd@asd.com","asd2@asd.com"))).thenReturn(
			Arrays.asList(
					new EWalletUser("asd@asd.com", BigDecimal.valueOf(100)),
					new EWalletUser("asd2@asd.com", BigDecimal.valueOf(100))
			)
		);
		
		TransferDetailsRequest transferDetails = new TransferDetailsRequest("asd@asd.com","asd2@asd.com", new BigDecimal(transferAmt), Transaction.TRANSFER_TYPE.TRANSFER.name());
		Assertions.assertEquals(true, transferUserValidator.validate(transferDetails, new ArrayList<>()));
	}
	@ParameterizedTest
	@ValueSource(strings  = {"10.001","10.00001"})
	void testTransferAmtMoreThan2DecimalPlaces(String transferAmt) {
		Mockito.when(eWalletUserRepository.findByEmailIn(Arrays.asList("asd@asd.com","asd2@asd.com"))).thenReturn(
			Arrays.asList(
					new EWalletUser("asd@asd.com", BigDecimal.valueOf(100)),
					new EWalletUser("asd2@asd.com", BigDecimal.valueOf(100))
			)
		);
		
		TransferDetailsRequest transferDetails = new TransferDetailsRequest("asd@asd.com","asd2@asd.com", new BigDecimal(transferAmt), Transaction.TRANSFER_TYPE.TRANSFER.name());
		Assertions.assertEquals(false, transferUserValidator.validate(transferDetails, new ArrayList<>()));
	}
	
	//test user transfer amount is negative
	@Test
	void testTransferAmtNegative() {
		Mockito.when(eWalletUserRepository.findByEmailIn(Arrays.asList("asd@asd.com","asd2@asd.com"))).thenReturn(
			Arrays.asList(
					new EWalletUser("asd@asd.com", BigDecimal.valueOf(100)),
					new EWalletUser("asd2@asd.com", BigDecimal.valueOf(100))
			)
		);
		
		TransferDetailsRequest transferDetails = new TransferDetailsRequest("asd@asd.com","asd2@asd.com", new BigDecimal("-0.01"), Transaction.TRANSFER_TYPE.TRANSFER.name());
		Assertions.assertEquals(false, transferUserValidator.validate(transferDetails, new ArrayList<>()));
	}
	
	//test user transfer amount is 0
	@ParameterizedTest
	@ValueSource(strings  = {"0","0.00","0.000"})
	void testTransferAmtIsZero(String transferAmt) {
		Mockito.when(eWalletUserRepository.findByEmailIn(Arrays.asList("asd@asd.com","asd2@asd.com"))).thenReturn(
			Arrays.asList(
					new EWalletUser("asd@asd.com", BigDecimal.valueOf(100)),
					new EWalletUser("asd2@asd.com", BigDecimal.valueOf(100))
			)
		);
		
		TransferDetailsRequest transferDetails = new TransferDetailsRequest("asd@asd.com","asd2@asd.com", new BigDecimal(transferAmt), Transaction.TRANSFER_TYPE.TRANSFER.name());
		Assertions.assertEquals(false, transferUserValidator.validate(transferDetails, new ArrayList<>()));
	}
	
	//test if user and transferee is the same
	@Test
	void testUserAndTransfereeIsSame() {
		
		TransferDetailsRequest transferDetails = new TransferDetailsRequest("asd@asd.com","asd@asd.com", new BigDecimal("1"), Transaction.TRANSFER_TYPE.TRANSFER.name());
		Assertions.assertEquals(false, transferUserValidator.validate(transferDetails, new ArrayList<>()));
	}
	
	
	private static Stream<Arguments> combinationOfToAndFromInvalidEamils() {
		
		List<Arguments> args = new ArrayList<>();
		for(String invalidEmail : INVALID_EMAIL_LIST) {
			TransferDetailsRequest invalidFromUser = new TransferDetailsRequest();
			invalidFromUser.setEmail(invalidEmail);
			invalidFromUser.setTransferee(VALID_EMAIL);
			invalidFromUser.setAmount(BigDecimal.valueOf(100));
			args.add(Arguments.of(invalidFromUser));

			TransferDetailsRequest invalidToUser = new TransferDetailsRequest();
			invalidToUser.setTransferee(invalidEmail);
			invalidToUser.setEmail(VALID_EMAIL);
			invalidToUser.setAmount(BigDecimal.valueOf(100));
			args.add(Arguments.of(invalidToUser));
		}
		
	    return args.stream();
	}
}
