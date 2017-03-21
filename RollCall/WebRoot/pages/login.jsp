<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.net.URLDecoder"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html dir="ltr" lang="zh">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

	<title>Login</title>

	<!--- CSS 注意，此处的路径容易出错，在初次登录时使用相对路径一般可以找到CSS，但重定向页面时相对路径改为相对servlet的，因此会找不到CSS文件，需要改为绝对路径 --->
	<link rel="stylesheet" href="<%=request.getContextPath()%>/pages/css/style.css" type="text/css" />


	<!--- Javascript libraries (jQuery and Selectivizr) used for the custom checkbox --->

	<!--[if (gte IE 6)&(lte IE 8)]>
		<script type="text/javascript" src="<%=request.getContextPath()%>/pages/js/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/pages/js/selectivizr.js"></script>
		<noscript><link rel="stylesheet" href="fallback.css" /></noscript>
	<![endif]-->

	</head>

	<body>
	<% 	Cookie[] cookies = request.getCookies();	
		Cookie name_cook = null;
		Cookie sid_cook = null;
		if(cookies != null) {
			for(Cookie cook : cookies) {
				if(cook.getName().equals("loginName")) {
					name_cook = cook;
				} else if(cook.getName().equals("loginSid")) {
					sid_cook = cook;
				}
			}
		}
	%>
		<div id="container">
			<form action="/RollCall/servlet/LoginServlet/" method="post">
				<div class="login">登录</div>
				<div class="username-text">姓名：</div>
				<div class="password-text">学号：</div>
				<div class="username-field">
					<% if(name_cook == null) {%>
						<input type="text" name="name" value="name" />
					<% } else {%>
						<input type="text" name="name" value=<%= URLDecoder.decode(name_cook.getValue(), "UTF-8") %> />
					<% } %>
				</div>
				<div class="password-field">
					<% if(sid_cook == null) {%>
						<input type="text" name="sid" value="student id" />
					<% } else {%>
						<input type="text" name="sid" value=<%= sid_cook.getValue() %> />
					<% } %>
				</div>
				<input type="checkbox" name="remember-me" id="remember-me" /><label for="remember-me">记住我</label>
				<div class="forgot-usr-pwd">
				<% if (request.getAttribute("login") != null && request.getAttribute("login").equals("fail")) {%>
					<p class="hint">您输入的姓名或学号不正确！</p>
				<% } %>
				</div>
				<input type="submit" name="submit" value="GO" />
			</form>
		</div>
		<div id="footer">
			上课了，请登录签到...
		</div>
	</body>
</html>
