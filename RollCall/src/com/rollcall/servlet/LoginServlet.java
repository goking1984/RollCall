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
		
		//设置HTTP请求类型，用于处理中文输入
		req.setCharacterEncoding("UTF-8");
		//设置HTTP响应类型，text/html代表文档类型，UTF-8代表编码方式
		resp.setContentType("text/html;charset=UTF-8");
		
		//将.properties文件存在/WEB-INF目录下可以使用下述方式读取
		Properties properties = new Properties();
		properties.load(getServletContext().
			getResourceAsStream("/WEB-INF/mysql.properties"));
		
		//Java在比较相等的中文字符串时，即使是编码相同也会出现比较结果为不等，避免中文串的比较
		//处理中文的编码问题
		//String input_name = new String(req.getParameter("name").
		//		getBytes("ISO8859-1"),"UTF-8");
		
		//学生对象
		StudentInfo student = new StudentInfo();
		//学生姓名
		String name = req.getParameter("name");
		//学号
		String input_sid = req.getParameter("sid");
		//签到次数
		int rc_time = 0;
		//本次是否签到
		int rc_flag = 0;
		//签到历史记录，本课程一共10次签到
		String rc_history = new String("");
		//用户是否选择'记住我'
		if(req.getParameter("remember-me") != null){
			Cookie[] cookie = req.getCookies();
			boolean flag = false;
			for(Cookie cook : cookie) {
				if(cook.getName().compareTo("loginSid") == 1) {
					flag = true;
				}
			}
			if(!flag) { //没有创建cookie则新建
				Cookie loginSid = new Cookie("loginSid", input_sid);
				//处理cookie中的中文
				Cookie loginName = new Cookie("loginName", URLEncoder.encode(name, "UTF-8"));
				//设置cookie的有效时间
				loginSid.setMaxAge(7*24*60*60);
				loginName.setMaxAge(7*24*60*60);
				//在本网站项目中设置所有程序都可访问该cookie
				loginSid.setPath("/");
				loginName.setPath("/");
				//将cookie通过HTTP响应发回到用户系统
				resp.addCookie(loginSid);
				resp.addCookie(loginName);
				
			}
		}
		String sql = "select name, rc_time, rc_flag, rc_history from rollcall where sid='" 
				+ input_sid + "';";

		//读取.properties文件中关于数据库连接的属性
		String driver = properties.getProperty("driver");
		String url = properties.getProperty("url");
		String user = properties.getProperty("user");
		String pwd = properties.getProperty("pwd");

		
		try {
			//加载JDBC驱动
			Class.forName(driver);
			//创建连接
			Connection connection = DriverManager.
					getConnection(url, user, pwd);
			//创建查询语句
			Statement statement = connection.createStatement();
			//执行语句并返回结果
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
		
		//设置JavaBean属性
		student.setSid(input_sid);
		student.setName(name);
		student.setRc_flag(rc_flag);
		student.setRc_time(rc_time);
		student.setRc_history(rc_history);
		//使用JavaBean-StudentInfo，传递给session对象后，在JSP页面中可访问
		req.getSession().setAttribute("student", student);
		//从login页面登陆到rollcall页面时不会弹出签到失败信息
		req.getSession().setAttribute("rc_success", 1);
		
		if(login_success){
			//登陆路成功，进入签到页面
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
