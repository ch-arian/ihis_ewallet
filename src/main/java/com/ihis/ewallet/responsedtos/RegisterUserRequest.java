package com.ihis.ewallet.responsedtos;

import javax.validation.constraints.Pattern;

import com.ihis.ewallet.MessageConstants;

import lombok.Data;

@Data
public class RegisterUserRequest {
	
	@Pattern(regexp="^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$", message=MessageConstants.ERROR_INVALID_EMAIL_MSG)
	private String email;
}
