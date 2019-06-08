<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.util.*" %>
<%@ page import="web.*" %>
<%
	Database db = new Database();
	request.setCharacterEncoding("UTF-8");
	String userid = request.getParameter("userId");
	ArrayList<String[]> ans = db.readUserdata(userid, "solvedproblem");
%>

<!DOCTYPE html>
<html>
<head>
	<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
	    google.charts.load('current', {'packages':['line']});
	    google.charts.setOnLoadCallback(drawChart);

		function drawChart() {
	
		    var data = new google.visualization.DataTable();
		    data.addColumn('string', 'date');
		    data.addColumn('number', '푼 문제 수');
		    
		    <%
			    for(int i = 0 ; i < ans.size(); ++i) {
			    	out.println("data.addRow([\'" +ans.get(i)[0] + "\', " + ans.get(i)[1] + "]);");
			    }
		    %>
		    
		    var options = {
				chart: {
		        	title: '<%=userid%>' + '님이 푼 문제 수 그래프',
				},
		      	width: 700,
		      	height: 250
		    };
		
		    var chart = new google.charts.Line(document.getElementById('linechart_material'));
		
		    chart.draw(data, google.charts.Line.convertOptions(options));
	    }
    </script>
<meta charset="UTF-8">
<title>데이터베이스 정보 읽어오기</title>
</head>
<body>
	<p id="data"> </p>
	<div id = 'linechart_material'></div>
</body>
</html>
