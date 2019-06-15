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
	String log = "";
	session.removeAttribute("cookie");
	session.removeAttribute("name");
	if ( session.getAttribute("trylogin") == null || (int)session.getAttribute("trylogin") == 0 ) {
		log = "none";
	} else {
		log = "block";
	}
%>

<body>
	<form id="problem" action="problems.jsp" method="post" style="display:none"></form>
		<header id="header">
			<div class="inner">
				<a href="start.jsp" class="logo">BACKJOON.GG</a>
				<nav id="nav">
				</nav>
			</div>
		</header>
		<a href="#menu" class="navPanelToggle"><span class="fa fa-bars"></span></a>
		
		<section id="banner" style="min-height:550px">

		<form id="send" class="form-inline" action="trylogin.jsp" method="post">
			<div class="inner">
				<h1>BackJoon.GG:</h1>
				<h2 align="right" style="font-size:15px; color:red; font-weight:bold; display:<%=log%>">아이디 / 이메일 또는 비밀번호가 잘못되었습니다.</h2>
			</div>
			
			<div class="inner">
				<form method="post" action="#">
					<div class="field half first">
						<label for="name"></label>
						<input id="id" type="text" name="id" placeholder="ID" style="font-weight: bold; color:black; background-color: white; background-color:rgba(256,256,256,0.8)" />
					</div>
					<div class="field half">
						<label for="email"></label>
						<input id="pw" type="password" name="pw" placeholder="Password" style="font-weight: bold; color:black; background-color: white; background-color:rgba(256,256,256,0.8)"/>
					</div>
					<ul class="actions">
						<center><li><input type="submit" onclick="Login()" value="Login" class="alt"/></li></center>
					</ul>
				</form>
			</div>
		</form>
</body>
	
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/skel.min.js"></script>
<script src="assets/js/util.js"></script>
<script src="assets/js/main.js"></script>
z
<script type="text/javascript">
</script>
</html>