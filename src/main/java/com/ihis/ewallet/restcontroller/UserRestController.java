package com.ihis.ewallet.restcontroller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ihis.ewallet.dtos.Transaction;
import com.ihis.ewallet.responsedtos.RegisterUserRequest;
import com.ihis.ewallet.responsedtos.RegisterUserResponse;
import com.ihis.ewallet.responsedtos.TransferDetailsRequest;
import com.ihis.ewallet.responsedtos.TransferDetailsResponse;
import com.ihis.ewallet.responsedtos.ViewBalanceResponse;
import com.ihis.ewallet.responsedtos.ViewTransactionResponse;
import com.ihis.ewallet.service.EwalletUserService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserRestController {
	
	@Autowired
	EwalletUserService userService;
	
	@ApiOperation("register user and give them initial balance")
	@PostMapping("register")
	public @ResponseBody RegisterUserResponse register(@Valid @RequestBody RegisterUserRequest user){
		List<String> errorList = new ArrayList<>();
		RegisterUserResponse response = userService.registerUser(user, errorList);
		if(response == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(",", errorList));
		}
		return response;
	}
	
	//transfer
	@ApiOperation("transfer amount to other user")
	@PostMapping("transfer")
	public @ResponseBody TransferDetailsResponse transferUser(@Valid @RequestBody TransferDetailsRequest transferDetails) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		List<String> errorList = new ArrayList<>();
		
		transferDetails.setType(Transaction.TRANSFER_TYPE.TRANSFER.name());
		transferDetails.setEmail(userDetails.getUsername());
		
		TransferDetailsResponse response = userService.transferUser(transferDetails, errorList);
		if(response == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(",", errorList));
		}
		return response;
	}
	
	//view transactions
	@ApiOperation("view transaction")
	@GetMapping("viewTransaction")
	public @ResponseBody ViewTransactionResponse viewTransaction() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		log.info("view balance of " + userDetails.getUsername());
		return userService.viewTransaction(userDetails.getUsername());
	}
	
	//view balance
	@ApiOperation("view balance")
	@GetMapping("viewBalance")
	public @ResponseBody ViewBalanceResponse viewBalance() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		log.info("view balance of " + userDetails.getUsername());
		return userService.viewBalance(userDetails.getUsername());
	}
}
