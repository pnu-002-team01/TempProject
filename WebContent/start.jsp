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
	Map<String, String> cookie = (Map<String, String>)session.getAttribute("cookie");
	if ( cookie == null ) { %>
		<script>location.href = "login.jsp"; </script>
<%	} else { %>
		<script>location.href = "main.jsp"; </script>
<%	} %>

<body>

</body>
	
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/skel.min.js"></script>
<script src="assets/js/util.js"></script>
<script src="assets/js/main.js"></script>
	
<script type="text/javascript">

</script>
</html>