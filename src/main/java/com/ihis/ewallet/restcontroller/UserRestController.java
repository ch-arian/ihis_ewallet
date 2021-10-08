package com.ihis.ewallet.restcontroller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ihis.ewallet.responsedtos.RegisterUserRequest;
import com.ihis.ewallet.responsedtos.RegisterUserResponse;

import io.swagger.annotations.ApiOperation;

@RestController
public class UserRestController {
	
	@ApiOperation("register user and give them initial balance")
	@PostMapping("register")
	public @ResponseBody RegisterUserResponse register(@Valid @RequestBody RegisterUserRequest user){
		RegisterUserResponse resp = new RegisterUserResponse();
		resp.setSuccess(true);
		
		return resp;
	}
}
