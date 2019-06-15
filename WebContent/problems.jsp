<%@ page import="datateam.BaekjoonCrawler,java.util.*" language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.*" %>
<%
	Database db = new Database();
	request.setCharacterEncoding("UTF-8");
	BaekjoonCrawler boj = new BaekjoonCrawler((Map<String, String>)session.getAttribute("cookie"));
	String userid = (String)session.getAttribute("name");
	ArrayList<String> problems = boj.crawlSolvedProblem(userid);
	ArrayList<String> unproblems = boj.crawlUnsolvedProblem(userid);
	ArrayList<String> problems_kimjuho = boj.crawlSolvedProblem_kimjuho(userid);
	ArrayList<String> unproblems_kimjuho = boj.crawlUnsolvedProblem_kimjuho(userid);
	db.insert(userid, "solvedproblem", problems_kimjuho);
	db.insert(userid, "unsolvedproblem", unproblems_kimjuho);
	ArrayList<String[]> ans = db.readUserdata(userid, "solvedproblem");
%>

<!DOCTYPE html>
<html>
<head>
<style>
.nav {
  text-align: center;
  margin: 20px;
  padding: 5px;
}

.nav li {
  display: inline-block;
  font-size: 20px;
  padding: 20px;
}
</style>
<meta charset="EUC-KR">
<title>BAEKJOON.GG</title>
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel="stylesheet" href="assets/css/main.css" />
</head>
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/skel.min.js"></script>
<script src="assets/js/util.js"></script>
<script src="assets/js/main.js"></script>
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>

<script type="text/javascript">
	google.charts.load('current', {'packages':['line']});
	google.charts.setOnLoadCallback(drawChart);	
	function drawChart() {
	    var data = new google.visualization.DataTable();
	    data.addColumn('string', '날짜');
	    data.addColumn('number', '레이팅');
	    <%
	
		    for(int i = 0 ; i < ans.size(); ++i) {
		    	if(ans.get(i)[3] == null) {
		    		String tmp;
		    		if(i == 0) {
		    			tmp = Float.toString(boj.calcRating("", ans.get(i)[2], "1500") );
		    		}
		    		else {
		    			tmp = Float.toString(boj.calcRating(ans.get(i - 1)[2], ans.get(i)[2], ans.get(i - 1)[3]) );
		    		}
		    		db.update(userid, "solvedproblem", tmp);
		    		out.println("data.addRow([\'" +ans.get(i)[0] + "\', " + Float.parseFloat(tmp) + "]);");
		    	}
		    	else {
		    		out.println("data.addRow([\'" +ans.get(i)[0] + "\', " + ans.get(i)[3] + "]);");
		    	}
		    }
	 
	    %>
	    var options = {
			chart: {
	        	title: '<%=userid%>' + '님의 레이팅 그래프',
			},
	      	width: 700,
	      	height: 400
	    };
	    var chart = new google.charts.Line(document.getElementById('linechart_material'));
	    chart.draw(data, google.charts.Line.convertOptions(options));
	}
	function change(val) {
		var value = val;
		document.getElementById("problem").value = value;
		document.getElementById("send").submit();
	}
</script>
 
<body>

<header id="header">
				<div class="inner">
					<a href="start.jsp" class="logo">BACKJOON.GG</a>
					<nav id="nav">
						<a href="problems.jsp">내정보</a>
						<a href="login.jsp">로그 아웃</a>
					</nav>
				</div>
</header>

<section id="one">
	<div class="inner">
		<center>
				<p id="data"> </p>
				<div id='linechart_material'></div>
				<form action="sourcelist.jsp" id="send" method="post">
					<input type="hidden" name="problem" id="problem">
					<div style=\"line-height:130%\"></br>
						<h2 style="color:green">내가 푼 문제</h2>
							<h3 style="color:MediumSeaGreen; line-height:100%; font-size:15px">
							
								<%
									HashMap<String, Integer> map = new HashMap<String, Integer>();
									
									for(String PN : problems_kimjuho){

										ArrayList<String> tags = db.getTags(PN);
										for(String ProblemType : tags) {
											if(map.containsKey(ProblemType) == true) map.put(ProblemType, map.get(ProblemType) + 1);
											else map.put(ProblemType, 1);
										}	
									}
														
									for ( int i = 0; i < problems.size(); i++ ){
										out.print(problems.get(i));
									}
									
							        for(String key : map.keySet()){
							            int value = map.get(key);
							           // System.out.print(key+" : ");
							           // System.out.println(value);
							         }
							        List<String> list = problemTags.sortByValue(map);
							        for(String s : list){
							        	System.out.print(s+" : ");
							        	int value = map.get(s);
							        	System.out.println(value);
							        }
							        	
							        
							        String visible = "";
							        if (!list.isEmpty()) {
							        	visible = "none";
							        } else {
							        	visible = "block";
							        }
							        
							        
								%>
							</h3>
					</div>
					<div style=\"line-height:130%\">
						<h2 style="color:red">틀린 문제</h2>
							<h3 style="color:Tomato; line-height:100%; font-size:15px">
								<%
									for ( int i = 0; i < unproblems.size(); i++ )
										out.print(unproblems.get(i)+"\t");
								%>
							</h3>
					</div>
					
					<ul class="nav">
					<li><font color="black">강점</font><br>
							
								<%
									if(!list.isEmpty())
										out.println(list.get(0));
								%>
					</li>
					<li><font color="black">약점</font><br>
								<%
									String Weakness = list.get(list.size()-1);
									if(!list.isEmpty())
										out.println(Weakness);
									
									ArrayList<String> nums = db.getNumbers(Weakness);
									Random rand = new Random();
									int idx = rand.nextInt(nums.size());
									String probNumber = nums.get(idx);
									String probURL = "http://boj.kr/" + probNumber;
								%>
					</li>
					<li><font color="black">추천문제</font><br>
							<a href="<%=probURL%>"><%=probNumber %></a>	
					</li>
					</ul>
				</form>
		</center>
	</div>				
</section>

</body>
</html>