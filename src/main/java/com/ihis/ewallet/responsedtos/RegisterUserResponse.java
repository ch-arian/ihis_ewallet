package com.ihis.ewallet.responsedtos;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class RegisterUserResponse extends EWalletResponseBody {
	private BigDecimal balance;
}
