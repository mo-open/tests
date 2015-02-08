package org.akka.essentials.stm.java.transactor.example.msg;

public class AccountBalance {

	String accountNumber = "";
	float accountBalance = 0;

	public AccountBalance(String no) {
		accountNumber = no;
	}

	public AccountBalance(String no, float bal) {
		accountNumber = no;
		accountBalance = bal;
	}

	public float getBalance() {
		return accountBalance;
	}

	public String getAccountNumber() {
		return accountNumber;
	}
}
