package com.ihis.ewallet.responsedtos;

import java.util.List;

import com.ihis.ewallet.dtos.Transaction;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ViewTransactionResponse extends EWalletResponseBody  {
	List<Transaction> transactions;
}
