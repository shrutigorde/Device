package com.shruti.capstone;
/*
 * Capstone project by Shruti Gorde
 * getter and setter class for contents and messages
 * 
 */
import java.io.Serializable;

public class Memo implements Serializable {
								
	private static final long serialVersionUID = 5196404678300433611L;

	private String message;
	private Memokind kind;
	private byte[] content;

	public String getMemo() {
		return message;
	}

	public void setMemo(String message) {
		this.message = message;
	}
	public byte[] getcontent() {
		return content;
	}

	public void setcontent(byte[] content) {
		this.content = content;
	}
	
	public Memokind getMemoKind() {
		return kind;
	}

	public void setMemoKind(Memokind kind) {
		this.kind = kind;
	}

	
	

	
}
