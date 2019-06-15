<%@ page import="datateam.BaekjoonCrawler,java.util.*" language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>BAEKJOON.GG</title>
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel="stylesheet" href="assets/css/main.css" />
<title>BAEKJOON.GG</title>
</head>
<style type=text/css>
	#view-on {width: 150px; height: 30px;}
</style>
<body>
<%
	BaekjoonCrawler boj = new BaekjoonCrawler((Map<String, String>)session.getAttribute("cookie"));
	String proNum = request.getParameter("problem");
	ArrayList<String> list = boj.getSourceList((String)session.getAttribute("name"), proNum);
%>
	<header id="header">
		<div class="inner">
			<a href="start.jsp" class="logo">BACKJOON.GG</a>
			<nav id="nav">
				<a href="problems.jsp">내정보</a>
				<a href="login.jsp">로그 아웃</a>
			</nav>
		</div>
	</header>

<center>
	<div id="view-on" style="display:inline">
		<form action="codecompare.jsp" id="compare">		
			<div style="margin-top:20px; width:25%">
				<input type="text" id="code1" name="code1" style="background-color:hsla(0, 0%, 100%, 0.7)">
				<button class="btn btn-primary" type="submit">소스 비교</button>
				<input type="text" id="code2" name="code2" style="background-color:hsla(0, 0%, 100%, 0.7)">
			</div>
		</form>
	</div>

	
	<form id="analy" action="analysis.jsp" method="post">
		<input type="hidden" id="source" name="source">
		<input type="hidden" id="type" name="type">
		<input type="hidden" value="sun" name="company">
		<input type="hidden" id="pronum" name="pronum">
	</form>
	<form id="send" action="source.jsp" method="post">
	<input type="hidden" id="num2" name="source">
		<div style=\"line-height:130%\">
			<h2 class="text">제출한 소스</h2>
			<div class="container">
				<table class="table">
						<thead>
							<tr>
								<th>제출 번호</th>
								<th>결 과</th>
								<th>시 간</th>
								<th>언 어</th>
								<th>제출 시간</th>
								<th></th>
							</tr>
						</thead>
						<tbody style="color:black">
							<%
								for ( int i = 0; i < list.size(); i++ ) {
									out.println(list.get(i));
								}
							%>		
						</tbody>
				</table>
			</div>
			<h2 class="text">TOP 5</h2>
			<h1 style="color:red; font-size:10px">마지막으로 제출한 언어로 작성된 공개된 소스 중 '시간'을 기준으로 빠른 5개의 소스만 보여집니다.</h1>
			<div class="container">
				<table class="table">
						<thead>
							<tr>
								<th>등 수</th>
								<th>아이디</th>
								<th>시 간</th>
								<th>언 어</th>
								<th>제출 시간</th>
								<th></th>
							</tr>
						</thead>
						<tbody style="color:black">
							<%
								String lang = boj.getLastLanguage().trim();
								ArrayList<String> top5 = boj.writeProblemCodes(proNum, lang);
								for ( int i = 0; i < top5.size(); i++ ) {
									out.println(top5.get(i));
								}
							%>		
						</tbody>
				</table>
			</div>
			</div>
	</form>
</center>
</body>
<script type="text/javascript">
	var idx = 0;
	function change(event) {
		var event_test = event;
		document.getElementById("num2").value = event_test;
		document.getElementById("send").submit();
	}
	function analysis(event, n) {
		var event_test = event;
		var nn = n;
		var pro = <%=proNum%>;
		document.getElementById("source").value = event_test;
		document.getElementById("type").value = nn;
		document.getElementById("pronum").value = pro;
		document.getElementById("analy").submit();
	}
	function setcompare(event) {
		var event_test = event;
		if ( idx % 2 == 0 ) {
			document.getElementById("code1").value = event_test;
		} else {
			document.getElementById("code2").value = event_test;
		}
		idx++;
	}
</script>
	
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/skel.min.js"></script>
<script src="assets/js/util.js"></script>
<script src="assets/js/main.js"></script>
</html>