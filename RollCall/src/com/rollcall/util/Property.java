package com.rollcall.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Property {

	//���ദ��.properties�ļ��Ķ�ȡ
	public static Properties getProperty(){
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("mysql.properties"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return properties;
	}
}
