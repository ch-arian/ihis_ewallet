package com.ihis.ewallet.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class User {
	
	private String email;
	
	private BigDecimal balance;
	
	private LocalDateTime dteCreated;
	
	private LocalDateTime dteLastUpdated;
	
	private String updatedBy;
}
