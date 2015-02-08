package org.akka.essentials.stm.java.transactor.example.msg;

public class TransferMsg {

	Float amtToBeTransferred;

	public TransferMsg(Float amt) {
		amtToBeTransferred = amt;
	}

	public Float getAmtToBeTransferred() {
		return amtToBeTransferred;
	}

}
