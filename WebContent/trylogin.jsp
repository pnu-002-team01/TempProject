 <%@ page import="datateam.BaekjoonCrawler,datateam.Cookie,java.util.*" language="java" contentType="text/html; charset=EUC-KR"
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
String id = request.getParameter("id");
String pw = request.getParameter("pw");
BaekjoonCrawler bj = new BaekjoonCrawler(id, pw);
Cookie ck = Cookie.getInstance();
ck.try_login++;
Map<String, String> cookie = bj.getCookie();
if ( cookie.containsKey("bojautologin") ) {
	ck.setCookie(cookie);
	ck.setUserId(bj.getuserID());
}
%>
<script>location.href = "start.jsp"; </script>

<body>
</body>
	
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/skel.min.js"></script>
<script src="assets/js/util.js"></script>
<script src="assets/js/main.js"></script>
	
<script type="text/javascript">
</script>
</html>