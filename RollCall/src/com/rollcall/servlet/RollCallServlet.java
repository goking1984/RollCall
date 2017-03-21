package com.rollcall.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rollcall.info.StudentInfo;

public class RollCallServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		resp.setContentType("text/html;charset=utf-8");
		RequestDispatcher requestDispatcher = null;		
		int rollCall_success = 0;
		
		StudentInfo student = (StudentInfo)(req.getSession().
				getAttribute("student"));
		
		//ֻ��δǩ�����û���ִ��ǩ������
		if(student == null || student.getRc_flag() == 0) {
			char[] rc_history = student.getRc_history().toCharArray();
			int rc = (int)(req.getSession().getAttribute("rc"));
			if(rc != -1) {
				rc_history[rc] = '1';
			}
			String history = new String(rc_history);
			//ǩ����������ݿ��е���Ӧֵ
			String sql = new String("update rollcall set rc_time=rc_time+1, rc_flag=1, rc_history='" 
						 	 + history + "' where sid='" + student.getSid() + "';");
			
			//��������Ĵ�����LoginServlet�е������ԣ��ܷ񴴽�һ������ר��������ݿ�ķ��ʲ�����
			//����Ŀα������������ϣ�����Hibernate�е�DAOģʽ
			Properties properties = new Properties();
			properties.load(getServletContext().
				getResourceAsStream("/WEB-INF/mysql.properties"));
			
			//��ȡ.properties�ļ��й������ݿ����ӵ�����
			String driver = properties.getProperty("driver");
			String url = properties.getProperty("url");
			String user = properties.getProperty("user");
			String pwd = properties.getProperty("pwd");
			
			try {
				Class.forName(driver);
				Connection connection = DriverManager.
						getConnection(url, user, pwd);
				Statement statement = connection.createStatement();
				int update = statement.executeUpdate(sql);
				if(update > 0){
					sql = "select * from rollcall where sid='"+ student.getSid() + "';";
					ResultSet resultSet = statement.executeQuery(sql);
					if(resultSet.next()){
						rollCall_success = 1;
						student.setRc_time(resultSet.getInt("rc_time"));
						student.setRc_flag(resultSet.getInt("rc_flag"));
						student.setRc_history(resultSet.getString("rc_history"));
					}
					resultSet.close();
					connection.close();
				}
				statement.close();
								
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		req.getSession().setAttribute("rc_flag", student.getRc_flag());
		req.getSession().setAttribute("rc_success", rollCall_success);
		
		requestDispatcher = 
			req.getRequestDispatcher("/pages/rollcall.jsp");
		requestDispatcher.forward(req, resp);	
	}
	
}
