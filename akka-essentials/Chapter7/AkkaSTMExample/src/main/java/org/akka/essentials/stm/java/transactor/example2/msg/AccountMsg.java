package org.akka.essentials.stm.java.transactor.example2.msg;

public abstract interface AccountMsg {

	public void setAmount(Float bal);

	public Float getAmount();
}
