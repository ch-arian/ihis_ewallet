package com.ihis.ewallet.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.ihis.ewallet.converter.LocalDateTimeConverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@DynamoDBTable(tableName = "EWalletUser")
@AllArgsConstructor
@NoArgsConstructor
public class EWalletUser {

	@DynamoDBHashKey
	private String email;
	
	private BigDecimal balance;
	
	@DynamoDBIgnore
	private List<Transaction> transactions = new ArrayList<>();
	private List<String> transactionIds = new ArrayList<>();
	
	@DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
	private LocalDateTime dteCreated;

	@DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
	private LocalDateTime dteLastUpdated;
	
	private String updatedBy;
	
	public EWalletUser(String email, BigDecimal balance) {
		this.email = email;
		this.balance = balance;
	}
	
}
