package com.rollcall.info;

public class StudentInfo {

	//ѧ������
	private String name;
	//ѧ��
	private String sid;
	//ǩ������
	private int rc_time;
	//�����Ƿ�ǩ��
	private int rc_flag;
	//ǩ����ʷ��¼
	private String rc_history;
	//�ܹ�ǩ������
	private static int total_times = 10;
	
	
	public static int getTotal_times() {
		return total_times;
	}

	public String getRc_history() {
		return rc_history;
	}

	public void setRc_history(String rc_history) {
		this.rc_history = rc_history;
	}

	public StudentInfo() {
		super();
		this.name = "";
		this.sid = "";
		this.rc_time = 0;
		this.rc_flag = 0;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public int getRc_time() {
		return rc_time;
	}
	public void setRc_time(int rc_time) {
		this.rc_time = rc_time;
	}
	public int getRc_flag() {
		return rc_flag;
	}
	public void setRc_flag(int rc_flag) {
		this.rc_flag = rc_flag;
	}
	
	
}
