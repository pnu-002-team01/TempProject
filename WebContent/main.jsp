 <%@ page import="datateam.BaekjoonCrawler,java.util.*" language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>BAEKJOON.GG</title>
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel="stylesheet" href="assets/css/main.css" />

</head>

<%
	String userid = (String)session.getAttribute("name");
	session.setAttribute("trylogin", 0);
%>

<body>
	<form id="problem" action="problems.jsp" method="post" style="display:none"></form>
		<!-- <img alt="MainLogo" src="img/main_logo.png" width="650" height="220"><p> -->
		
			<header id="header">
				<div class="inner">
					<a href="start.jsp" class="logo">BACKJOON.GG</a>
					<nav id="nav">
						<!-- <a href="main.jsp">메인</a> -->
						<a href="problems.jsp">내정보</a>
						<a href="login.jsp">로그 아웃</a>
					</nav>
				</div>
			</header>
			<a href="#menu" class="navPanelToggle"><span class="fa fa-bars"></span></a>
			
			<section id="banner" style="min-height:550px">
			<div id="welcome" class="inner">
				<h1><%=userid %> 님 환영합니다.</h1>
			</div>
	
</body>
	
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/skel.min.js"></script>
<script src="assets/js/util.js"></script>
<script src="assets/js/main.js"></script>
	
<script type="text/javascript">
</script>
</html>