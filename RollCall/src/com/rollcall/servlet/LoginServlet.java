package com.rollcall.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rollcall.info.StudentInfo;

public class LoginServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		boolean login_success = false;
		RequestDispatcher requestDispatcher = null;
		
		//����HTTP�������ͣ����ڴ�����������
		req.setCharacterEncoding("UTF-8");
		//����HTTP��Ӧ���ͣ�text/html�����ĵ����ͣ�UTF-8������뷽ʽ
		resp.setContentType("text/html;charset=UTF-8");
		
		//��.properties�ļ�����/WEB-INFĿ¼�¿���ʹ��������ʽ��ȡ
		Properties properties = new Properties();
		properties.load(getServletContext().
			getResourceAsStream("/WEB-INF/mysql.properties"));
		
		//Java�ڱȽ���ȵ������ַ���ʱ����ʹ�Ǳ�����ͬҲ����ֱȽϽ��Ϊ���ȣ��������Ĵ��ıȽ�
		//�������ĵı�������
		//String input_name = new String(req.getParameter("name").
		//		getBytes("ISO8859-1"),"UTF-8");
		
		//ѧ������
		StudentInfo student = new StudentInfo();
		//ѧ������
		String name = req.getParameter("name");
		//ѧ��
		String input_sid = req.getParameter("sid");
		//ǩ������
		int rc_time = 0;
		//�����Ƿ�ǩ��
		int rc_flag = 0;
		//ǩ����ʷ��¼�����γ�һ��10��ǩ��
		String rc_history = new String("");
		//�û��Ƿ�ѡ��'��ס��'
		if(req.getParameter("remember-me") != null){
			Cookie[] cookie = req.getCookies();
			boolean flag = false;
			for(Cookie cook : cookie) {
				if(cook.getName().compareTo("loginSid") == 1) {
					flag = true;
				}
			}
			if(!flag) { //û�д���cookie���½�
				Cookie loginSid = new Cookie("loginSid", input_sid);
				//����cookie�е�����
				Cookie loginName = new Cookie("loginName", URLEncoder.encode(name, "UTF-8"));
				//����cookie����Чʱ��
				loginSid.setMaxAge(7*24*60*60);
				loginName.setMaxAge(7*24*60*60);
				//�ڱ���վ��Ŀ���������г��򶼿ɷ��ʸ�cookie
				loginSid.setPath("/");
				loginName.setPath("/");
				//��cookieͨ��HTTP��Ӧ���ص��û�ϵͳ
				resp.addCookie(loginSid);
				resp.addCookie(loginName);
				
			}
		}
		String sql = "select name, rc_time, rc_flag, rc_history from rollcall where sid='" 
				+ input_sid + "';";

		//��ȡ.properties�ļ��й������ݿ����ӵ�����
		String driver = properties.getProperty("driver");
		String url = properties.getProperty("url");
		String user = properties.getProperty("user");
		String pwd = properties.getProperty("pwd");

		
		try {
			//����JDBC����
			Class.forName(driver);
			//��������
			Connection connection = DriverManager.
					getConnection(url, user, pwd);
			//������ѯ���
			Statement statement = connection.createStatement();
			//ִ����䲢���ؽ��
			ResultSet resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				name = resultSet.getString("name");
				rc_time = resultSet.getInt("rc_time");
				rc_flag = resultSet.getInt("rc_flag");
				rc_history = resultSet.getString("rc_history");
				login_success = true;
			}
			resultSet.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//����JavaBean����
		student.setSid(input_sid);
		student.setName(name);
		student.setRc_flag(rc_flag);
		student.setRc_time(rc_time);
		student.setRc_history(rc_history);
		//ʹ��JavaBean-StudentInfo�����ݸ�session�������JSPҳ���пɷ���
		req.getSession().setAttribute("student", student);
		//��loginҳ���½��rollcallҳ��ʱ���ᵯ��ǩ��ʧ����Ϣ
		req.getSession().setAttribute("rc_success", 1);
		
		if(login_success){
			//��½·�ɹ�������ǩ��ҳ��
			requestDispatcher = 
				req.getRequestDispatcher("/pages/rollcall.jsp");
			requestDispatcher.forward(req, resp);
		} else {
			req.setAttribute("login", "fail");
			requestDispatcher = 
					req.getRequestDispatcher("/pages/login.jsp");
			requestDispatcher.forward(req, resp);
		}
	}

	
}
