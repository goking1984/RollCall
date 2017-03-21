<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import ="java.util.Calendar, java.util.Date, java.text.SimpleDateFormat, com.rollcall.info.StudentInfo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>RollCall</title>
<!--- CSS 注意，此处的路径容易出错，在初次登录时使用相对路径一般可以找到CSS，但重定向页面时相对路径改为相对servlet的，因此会找不到CSS文件，需要改为绝对路径 --->
	<link rel="stylesheet" href="<%=request.getContextPath()%>/pages/css/rollcall.css" type="text/css" />
</head>
<body>
<jsp:useBean id="student" class="com.rollcall.info.StudentInfo" scope="session" />
<% // 在JSP中将session保存的变量取出，注意这里初始返回值类型是Oject，需要先转String再转int %>
<% //本次课是否已经签到
   	int rc_flag = student.getRc_flag(); 
   	//签到操作是否成功
   	int rc_success = Integer.parseInt(request.getSession().getAttribute("rc_success").toString());
   	//当前已签到次数
   	int current = student.getRc_time();

   	Calendar calendar = Calendar.getInstance();
   	int year = calendar.get(Calendar.YEAR);
   	int month = calendar.get(Calendar.MONTH);
   	int day= calendar.get(Calendar.DAY_OF_MONTH);
   	String[] class_date = {"2017年3月7日（上午）", "2017年3月9日（上午）", "2017年3月14日（上午）", "2017年3月16日（上午）",
   						   "2017年3月20日（上午）", "2017年3月21日（全天）", "2017年3月23日（上午）",
   						   "2017年3月27日（上午）", "2017年3月28日（全天）", "2017年3月30日（上午）"};
   	String[] class_date_format = {"2017-03-07", "2017-03-09", "2017-03-14", "2017-03-16",
   			               "2017-03-20", "2017-03-21", "2017-03-23",
			  					  "2017-03-27", "2017-03-28", "2017-03-30"};
   	//获取当前日期，并转换为String类型
   	String today = (new SimpleDateFormat("yyyy-MM-dd")).format(calendar.getTime());
   	int rc = 0;
   	while(rc < class_date_format.length) {
   		if(today.equals(class_date_format[rc])) {
   			break;
   		}
   		rc++;
   	}
   	if(rc >= class_date_format.length) {
   		rc = -1;
   	}
   	request.getSession().setAttribute("rc", rc);
%>
<div id="container">
	<div id="top-item">
		<div id="welcome-div">
			<p class="welcome-text">欢迎来到Jave EE课堂，<jsp:getProperty property="name" name="student"/></p>
			<p class="welcome-text">网站构建技术——Java解决方案</p>
		</div>
		<div id="form-div">		
			<form action="/RollCall/servlet/RollCallServlet/" method="post" id="rc_form">
				<input type="hidden" id="rc_flag" value=<%= rc_flag %> />
				<input type="hidden" id="rc_success" value=<%= rc_success %> />
				<input type="hidden" id="rc" value=<%= rc %> />
				<input type="submit" value="签到" id="rc_submit"/>
			</form>
			<% if(rc != -1) {%>
				<p><%= year %>年<%= month + 1 %>月<%= day %>日，第<%= rc + 1 %>次课</p>
			<% } else {%>
				<p>非上课时间，无法签到！</p>
			<% } %>
		</div>
	</div>
	<div id="prom">
		<hr />
		<p>您已签到<jsp:getProperty property="rc_time" name="student"/>次，签到记录如下：</p>
	</div>
</div>

<div id="rc-items">
	<% for(int i = 0; i < StudentInfo.getTotal_times(); i++) {%>
		<% if((student.getRc_history().toCharArray())[i] == '1') { //签到的条目%>
			<div id="rollcalled">
				<p class="class-date">第<%= i + 1 %>次课：<%= class_date[i] %></p>
				<p class="state">状态：已签到</p>
			</div>
		<% } else { //未签到的条目%>
			<div id="failed">
				<p class="class-date">第<%= i + 1 %>次课：<%= class_date[i] %></p>
				<p class="state">状态：未签到</p>
			</div>
		<% } %>
	<% } %>
</div>

<div class="roll-container">

</div>

<div class="roll-footer">

</div>
<script>
var rc_flag = document.getElementById("rc_flag").value;
var rc_success = document.getElementById("rc_success").value;
var rc = document.getElementById("rc").value;
if(rc_flag == 1 || rc == -1){
	document.getElementById("rc_submit").disabled = true;
}
if(rc_success == 0){
	alert("签到失败！");
}
</script>
</body>
</html>