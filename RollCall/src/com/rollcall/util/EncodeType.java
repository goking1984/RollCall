package com.rollcall.util;

import java.io.UnsupportedEncodingException;

public class EncodeType {

	private String inputString;
	
	public EncodeType(String inputString) {
		super();
		this.inputString = inputString;
	}
	
	public String getEncoding() {
		String encoding = new String();
		try {
			if(this.inputString.equals(new String(this.inputString.
					getBytes("ISO8859_1"), "ISO8859_1"))){
				encoding = "ISO8859_1";
			}
			else if(this.inputString.equals(new String(this.inputString.
					getBytes("UTF-8"), "UTF-8"))){
				encoding = "UTF-8";
			}
			else if(this.inputString.equals(new String(this.inputString.
					getBytes("GB2312"), "GB2312"))){
				encoding = "GB2312";
			}
			else if(this.inputString.equals(new String(this.inputString.
					getBytes("GBK"), "GBK"))){
				encoding = "GBK";
			}
			else {
				encoding = "others";
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encoding;
	}
	
}
