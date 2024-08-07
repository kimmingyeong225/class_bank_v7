package com.tenco.bank.dto;

import com.tenco.bank.repository.model.Account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SaveDTO {
	
	private String number;
	private String password;
	private Long balance;
	private int userId;
	
	// 반복되는 코드 라서 로직 처리를 해주는건가?... 
	// (this 생략 가능)
	public Account toAccount(Integer userId) {
		return Account.builder()
				      .number(this.number)
				      .password(this.password)
				      .balance(this.balance)
				      .userId(userId)
				      .build();
	}
	
	

}
