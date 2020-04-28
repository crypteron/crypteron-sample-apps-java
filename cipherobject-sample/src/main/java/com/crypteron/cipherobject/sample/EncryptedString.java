package com.crypteron.cipherobject.sample;

import com.crypteron.Secure;

public class EncryptedString {
	@Secure
	private String string;

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

}
