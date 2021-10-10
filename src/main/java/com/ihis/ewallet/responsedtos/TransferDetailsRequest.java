package com.ihis.ewallet.responsedtos;

import java.math.BigDecimal;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;

import org.springframework.util.ObjectUtils;

import com.ihis.ewallet.MessageConstants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferDetailsRequest {
	@Pattern(regexp="^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message=MessageConstants.ERROR_INVALID_EMAIL_MSG)
	private String email;
	@Pattern(regexp="^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message=MessageConstants.ERROR_INVALID_EMAIL_MSG)
	private String transferee;
	@DecimalMin(value = "0.0", inclusive = false, message=MessageConstants.ERROR_TRANSFER_USER_INVALID_AMT)
    @Digits(integer=3, fraction=2, message=MessageConstants.ERROR_TRANSFER_USER_NOT_2_DECIMAL)
	private BigDecimal amount;
	private String type;
}
