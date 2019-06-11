<%@ page import="datateam.BaekjoonCrawler,swTeam.CodeCompare,swTeam.SourceAnalysis,java.util.*" language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
   <style>
      .code {
         border:inset 2px silver; 
         width:700px;
         height:800px; 
         padding:1 1 1 1;
         font-size:9pt; 
         overflow:auto;
      }
   </style>
<meta charset="EUC-KR">
<link rel="stylesheet" href="assets/css/main.css" />

<title>BAEKJOON.GG</title>
</head>
<body>

		<header id="header">
				<div class="inner">
					<a href="main.jsp" class="logo">BACKJOON.GG</a>
					<nav id="nav">
						<a href="problems.jsp">내정보</a>
						<a href="login.jsp">로그 아웃</a>
					</nav>
				</div>
			</header>

<%
BaekjoonCrawler boj = new BaekjoonCrawler((Map<String, String>)session.getAttribute("cookie"));
   String code1 = boj.getSource(request.getParameter("code1"));
   String code2 = boj.getSource(request.getParameter("code2"));
   CodeCompare cp = new CodeCompare(code1, code2);
%>
<div style='display:inline; width:1200px;'>
<div class="code" style="float:left">
<h2 style="color:black" > 제출 번호 : <%=request.getParameter("code1") %> </h2>

<pre style="font-size:15px; font-family:Roboto; color:black">
<%
out.print(cp.printCode(0));
%>
</pre>


</div>
<div class="code">
<h2 style="color:black"> 제출 번호 : <%=request.getParameter("code2") %> </h2>
<pre style="font-size:15px; font-family:Roboto; color:black">
<%
out.print(cp.printCode(1));
%>
</pre>
</div>
</div>
</body>
	
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/skel.min.js"></script>
<script src="assets/js/util.js"></script>
<script src="assets/js/main.js"></script>
	
<script type="text/javascript">
</html>