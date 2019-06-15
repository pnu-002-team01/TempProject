package datateam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;


public class BaekjoonCrawler {
	
	private static final String MAINURL = "http://www.acmicpc.net/";
	private static final boolean SHOW_LOG = true;
	private static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36";
	private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final String LOG_PATH = "logs/";
	private String logName = "";
	private String logResult = "";
	public Document problemPageDocument = null;
	private Map<String,String> loginCookie = null;
	private Map<String,Integer> problemRating = null;
	private String language = "";
	
	// Constructor
	public BaekjoonCrawler(String userID, String userPassword) {
		checkInternetConnection();
		acquireLoginCookie(userID,userPassword);
    acquireProblemRatings();
		if(logName == "") {
			logName = getCurrentTimeString();
		}
	}
	
	public BaekjoonCrawler(Map<String, String> cookie) {
		loginCookie = cookie;
		acquireProblemRatings();
		if(logName == "") {
			logName = getCurrentTimeString();
		}
	}
	
	
	// log-related Methods
	public void updateLog(final String target) {
		logResult += "[" + getCurrentTimeString() + "] " + target;
	}
	
	public void exportLog() {
		File file = new File(LOG_PATH+logName+".log");	
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(logResult);
			fw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// Methods
	public void acquireProblemRatings() {
    problemRating = new HashMap<String, Integer>();
		File file = new File("stats/ratings.txt");
		try {
			FileReader fr = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(fr);
			String line = "";
			while((line = bufReader.readLine()) != null) {
				String[] data = line.split(":");
				problemRating.put(data[0], Integer.parseInt(data[1]));
			} 
			bufReader.close();
		} catch(FileNotFoundException e) {
			updateLog("Rating file not found.");
		} catch(IOException e) {
			updateLog("IO Exceptoin at ratings.txt");
		}
	}
	public String getLanguage(String str) throws FileNotFoundException, IOException, ParseException{
		JSONParser parser = new JSONParser();
		String path = this.getClass().getResource("").getPath() + "language.json";
		Object obj = parser.parse(new FileReader(path));
		JSONObject json = (JSONObject) obj;
		String language = (String)json.get(str);
		return language;
	}
	
	private String getCurrentTimeString() {
		LocalDate localDate = LocalDate.now();
		return DTF.format(localDate);
	}
	
	public void checkInternetConnection() {
		Document document = null;
		try {
			document = Jsoup.connect(MAINURL)
							.get();
		} catch(IOException e) {
			updateLog("Unable to connect.");
		}
		if(document != null) {
			updateLog("Successfully connected to " + MAINURL);
		}
		else {
			updateLog("Failed to receive main page document.");
		}
	}
	
	public String getuserID() {
		Document document = null;
		String userid = "";
		try {
			document = Jsoup.connect(MAINURL)
	                .userAgent(userAgent)
	                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
	                .header("Upgrade-Insecure-Requests", "1")	
	                .cookies(loginCookie) 
	                .get();
			Elements User = document.getElementsByClass("container");
			Elements u = User.get(0).getElementsByClass("username");
			userid = u.get(0).ownText();
		} catch(IOException e) {
			updateLog("Unable to connect.");
		}
		return userid;
	}
	
	public void acquireLoginCookie(String userID, String userPassword) {

		Map<String, String> data = new HashMap<>();
		data.put("login_user_id", userID);
		data.put("login_password", userPassword);
		data.put("auto_login", "0");
		
		Connection.Response response = null;
		try {
		response = Jsoup.connect("https://www.acmicpc.net/signin")
                        .userAgent(userAgent)
                        .timeout(7000)
                        .data(data)
                        .method(Connection.Method.POST)
                        .execute();
		} catch (IOException e) {
			updateLog("Failed to connect login server.");
		}
		
		this.loginCookie = response.cookies();
	}
	
	public Map<String, String> getCookie() {
		return loginCookie;
	}
	
	public void receiveProblemDocument(String problemID) {
		Document document = null;
		if(loginCookie == null) {
			updateLog("Login cookie is not acquired.");
		}
		else {
			try {
				final String problemURL = "https://www.acmicpc.net/problem/" + problemID;
				document = Jsoup.connect(problemURL)
				                .userAgent(userAgent)
				                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
				                .header("Upgrade-Insecure-Requests", "1")	
				                .cookies(loginCookie) 
				                .get();
			} catch(IOException e) {
				updateLog("Failed to crawl problem page");
			}
		}
		this.problemPageDocument = document;
	}
	
	public ArrayList<String> crawlAlgorithms() {
		ArrayList<String> algorithms = null;
		final String TARGET_CSS = "a.spoiler-link";
		if(problemPageDocument == null) {
			updateLog("Problem page document is empty.");
		}
		else {
			algorithms = new ArrayList<String>();
			Elements algorithmTagList = problemPageDocument.select(TARGET_CSS);
			for(Element algorithm : algorithmTagList) {
				if(SHOW_LOG) {
					updateLog(algorithm.text());
				}
				algorithms.add(algorithm.text());
			}
		}
		
		return algorithms;
	}
	
	public Map<String, String> crawlProblemSubmitState() {
		Map<String, String> problemState = null;
		final String TARGET_CSS = "table#problem-info";
		final int CATEGORY_LIMIT = 6;
		if(problemPageDocument == null) {
			updateLog("Problem page document is empty.");
		}
		else {
			problemState = new HashMap<String, String>();
			Element table = problemPageDocument.select(TARGET_CSS).get(0);
			Elements categories = table.select("thead").get(0).select("tr").get(0).select("th");
			Elements values = table.select("tbody").get(0).select("tr").get(0).select("td");
			for(int i=0; i<CATEGORY_LIMIT; i++) {
				String category = categories.get(i).text();
				String value = values.get(i).text();
				problemState.put(category,value);
				if(SHOW_LOG) {
					updateLog(category + ": " + value);
				}
			}

		}
		return problemState;
	}
	
	public ArrayList<String> getSourceList(String userID, String problemNum) {
		String myURL = "https://www.acmicpc.net/status?from_mine=1&problem_id=" + problemNum + "&user_id=" + userID;
		Document doc = null;
		ArrayList <String> res = new ArrayList<>();
		
		if(loginCookie == null) {
			updateLog("Login cookie is not acquired.");
		}
		
		try {
			doc = Jsoup.connect(myURL)
	                .userAgent(userAgent)
	                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
	                .header("Upgrade-Insecure-Requests", "1")	
	                .cookies(loginCookie)
	                .get();

			final String TARGET_CLASS = "table-bordered";
			Elements table = doc.getElementsByClass(TARGET_CLASS);
			Elements rows = table.get(0).select("tr");
			for ( int i = 1; i < rows.size(); i++ ) {
				Element row = rows.get(i);
				Elements cols = row.select("td");
				String tmp = "<tr onclick='setcompare("+cols.get(0).ownText()+")' onMouseOver=\"this.style.backgroundColor='#FFF4E9';\" onMouseOut=\"this.style.backgroundColor=''\">";
				tmp += "<td><a>"+cols.get(0).ownText()+"</a></td>";
				if ( cols.get(3).text().contains("맞았") )
					tmp += "<td style='font-weight:bold; color:green;'>"+cols.get(3).text()+"</td>";
				else
					tmp += "<td style='font-weight:bold; color:red;'>"+cols.get(3).text()+"</td>";
				if ( !cols.get(5).text().equals("") )
					tmp += "<td>"+cols.get(5).text()+" ms"+"</td>";
				else
					tmp += "<td></td>";
				String lang = cols.get(6).text().replace(" / 수정", "");
				if ( i == 1 )
					language = lang;
				tmp += "<td>"+lang+"</td>";
				tmp += "<td>"+cols.get(8).text()+"</td>";
				String val = "0";
				if ( cols.get(6).text().contains("Java") )
					val = "1";
				tmp += "<td><a href='#' ";
				tmp += "onclick=\"analysis("+cols.get(0).ownText()+","+val+")\">소스 분석</a></td>";
				tmp += "</tr>";
				res.add(tmp);
			}
		} catch(IOException e) {
			updateLog("Fail to get User Information");
		}
		return res;
	}
	
	public String getLastLanguage() {
		return this.language;
	}
	
	public String getSource(String solveNum) {
		Document document = null;
		String source = "";
		if(loginCookie == null) {
			updateLog("Login cookie is not acquired.");
		}
		else {
			try {
				final String problemURL = "https://www.acmicpc.net/source/"+solveNum;
				document = Jsoup.connect(problemURL)
				                .userAgent(userAgent)
				                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
				                .header("Upgrade-Insecure-Requests", "1")	
				                .cookies(loginCookie) 
				                .get();
				Elements box = document.getElementsByClass("col-lg-12");
				source = box.text();
			} catch(IOException e) {
				updateLog("Failed to crawl problem page");
			}
		}
		return source;
	}
	
public ArrayList<String> writeProblemCodes(String problemID, String languageName) throws Exception {
		
		Document doc = null;
		String language = getLanguage(languageName);
		ArrayList<String> res = new ArrayList<>();
		
		if(loginCookie == null) {
			updateLog("Login cookie is not acquired.");
		}
		
		try {
			
			String pageNum = "1";			
			String codePage = MAINURL + "problem/status/"+ problemID + "/" + language + "/"+pageNum;
			doc = Jsoup.connect(codePage)
	                .userAgent(userAgent)
	                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
	                .header("Upgrade-Insecure-Requests", "1")	
	                .cookies(loginCookie) 
	                .get();
						
			Elements elements = doc.getElementsByTag("tr");
			int count = 0;		
			for ( Element e: elements ) {
				Elements tdElements = e.select("td");
				if(tdElements.size() > 5) {
					Elements temp1 = tdElements.get(6).select("a");
					if(!temp1.isEmpty()) {
						String tmp = "<tr onclick='setcompare("+tdElements.get(1).ownText()+")' onMouseOver=\"this.style.backgroundColor='#FFF4E9';\" onMouseOut=\"this.style.backgroundColor=''\">";
						tmp += "<td>"+tdElements.get(0).text()+"</td>";
						tmp += "<td><a>"+tdElements.get(3).select("a").text()+"</a></td>";
						tmp += "<td>"+tdElements.get(5).text()+" ms"+"</td>";
						tmp += "<td>"+tdElements.get(6).text().replace(" / 수정", "")+"</td>";
						tmp += "<td>"+tdElements.get(8).text()+"</td>";
						String val = "0";
						if ( tdElements.get(6).text().contains("Java") )
							val = "1";
						tmp += "<td><a href='#' ";
						tmp += "onclick=\"analysis("+tdElements.get(1).ownText()+","+val+")\">소스 분석</a></td>";
						tmp += "</tr>";
						res.add(tmp);
						count++;
					}
				}
				if(count >= 5) break;
			}
		} catch ( IOException e ) {
			updateLog("Fail to get User Information");
		}
		
		return res;
	}
	
	public ArrayList<String> crawlSolvedProblem_kimjuho(String userID) {
		String UserPageURL = MAINURL + "/user/" +  userID;
		Document doc = null;
		ArrayList < String > res = new ArrayList< String >();
		
		if(loginCookie == null) {
			updateLog("Login cookie is not acquired.");
		}
		
		try {
			doc = Jsoup.connect(UserPageURL)
	                .userAgent(userAgent)
	                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
	                .header("Upgrade-Insecure-Requests", "1")	
	                .cookies(loginCookie)
	                .get();
			
			final String TARGET_CLASS = "panel-body";
			final String SPLIT_CLASS = "span.problem_number";
			
			
			Elements myProblemList = doc.getElementsByClass(TARGET_CLASS);
			Elements solvedProblem = myProblemList.get(0).select(SPLIT_CLASS);
			
			
			
			for( int i = 0; i < solvedProblem.size(); ++i ) {
				res.add(solvedProblem.get(i).text());
			}  

			
			
		} catch(IOException e) {
			updateLog("Fail to get User Information");
		}
		return res;
	}
	
	public ArrayList<String> crawlUnsolvedProblem_kimjuho(String userID){
		String UserPageURL = MAINURL + "/user/" +  userID;
		Document doc = null;
		ArrayList < String > res = new ArrayList< String >();
		
		if(loginCookie == null) {
			updateLog("Login cookie is not acquired.");
		}
		
		try {
			doc = Jsoup.connect(UserPageURL)
	                .userAgent(userAgent)
	                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
	                .header("Upgrade-Insecure-Requests", "1")	
	                .cookies(loginCookie)
	                .get();
			
			final String TARGET_CLASS = "panel-body";
			final String SPLIT_CLASS = "span.problem_number";
			
			
			Elements myProblemList = doc.getElementsByClass(TARGET_CLASS);
			Elements unsolvedProblem = myProblemList.get(1).select(SPLIT_CLASS);
			
			
			
			for( int i = 0; i < unsolvedProblem.size(); ++i ) {
				res.add(unsolvedProblem.get(i).text());
			}  

			
			
		} catch(IOException e) {
			updateLog("Fail to get User Information");
		}
		return res;
	}
	public ArrayList<String> crawlSolvedProblem(String userID){
		String UserPageURL = MAINURL + "/user/" +  userID;
		Document doc = null;
		ArrayList < String > res = new ArrayList< String >();
		res.add("<table><thead></thead><tbody>");
		if(loginCookie == null) {
			updateLog("Login cookie is not acquired.");
		}
		
		try {
			doc = Jsoup.connect(UserPageURL)
	                .userAgent(userAgent)
	                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
	                .header("Upgrade-Insecure-Requests", "1")	
	                .cookies(loginCookie)
	                .get();
			
			final String TARGET_CLASS = "panel-body";
			final String SPLIT_CLASS = "span.problem_number";
			
			
			Elements myProblemList = doc.getElementsByClass(TARGET_CLASS);
			Elements solvedProblem = myProblemList.get(0).select(SPLIT_CLASS);
			
			for( int i = 0; i < solvedProblem.size(); ++i ) {
				String tmp = "";
				if ( i % 18 == 0 )
					tmp += "<tr>";
				tmp += "<td><a href='#' ";
				tmp += "onclick=\"change("+solvedProblem.get(i).text()+")\">"+solvedProblem.get(i).text()+"</a></td>";
				if ( (i+1) % 18 == 0 )
					tmp += "</tr>";
				res.add(tmp);
			}
		} catch(IOException e) {
			updateLog("Fail to get User Information");
		}
		res.add("</tbody></table>");
		return res;
	}

	public ArrayList<String> crawlUnsolvedProblem(String userID){
		String UserPageURL = MAINURL + "/user/" +  userID;
		Document doc = null;
		ArrayList < String > res = new ArrayList< String >();
		res.add("<table><thead></thead><tbody>");
		if(loginCookie == null) {
			updateLog("Login cookie is not acquired.");
		}
		
		try {
			doc = Jsoup.connect(UserPageURL)
	                .userAgent(userAgent)
	                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
	                .header("Upgrade-Insecure-Requests", "1")	
	                .cookies(loginCookie) 
	                .get();
			
			final String TARGET_CLASS = "panel-body";
			final String SPLIT_CLASS = "span.problem_number";
						
			Elements myProblemList = doc.getElementsByClass(TARGET_CLASS);
			Elements unsolvedProblem = myProblemList.get(1).select(SPLIT_CLASS);

			for( int i = 0; i < unsolvedProblem.size(); ++i ) {
				String tmp = "";
				if ( i % 18 == 0 )
					tmp += "<tr>";
				tmp += "<td><a href='#' ";
				tmp += "onclick=\"change("+unsolvedProblem.get(i).text()+")\">"+unsolvedProblem.get(i).text()+"</a></td>";
				if ( (i+1) % 18 == 0 )
					tmp += "</tr>";
				res.add(tmp);
			}
			res.add("</tbody></table>");
		} catch(IOException e) {
			updateLog("Fail to get User Information");
		}
		return res;
	}
	
	public ArrayList<String> crawlProblemNumbers() {
		Document doc = null;		

		ArrayList<String> problemIDList = new ArrayList<>();
		
		for(int pageNumber = 1;; pageNumber++) {
			String problemPageURL = MAINURL + "/problemset/" + String.valueOf(pageNumber);
			try {
				doc = Jsoup.connect(problemPageURL).get();
			} catch(IOException e) {
				break;
			}
			final String TARGET_CSS = "td.list_problem_id";
			Elements currentProblemIDList = doc.select(TARGET_CSS);
			
			if(SHOW_LOG) {
				updateLog("Page number " + pageNumber);
			}
			if(currentProblemIDList.isEmpty()) {
				updateLog("Problem Number Crawling Finished.");
				break;
			}
			for(Element problemID : currentProblemIDList) {
				problemIDList.add(problemID.text());
				if(SHOW_LOG) {
					updateLog(problemID.text());
				}
			}
		}
		return problemIDList;
	}
	
	public void writeUserInfoJson( String userID) {
		
		if(loginCookie == null) {
			updateLog("Login cookie is not acquired.");
		}
		
		String jsonResult = "{\n\t\"userID\" : \""+userID+"\",\n";
		ArrayList<String> solvedProblems = this.crawlSolvedProblem(userID);
		
		jsonResult += "\t\"countSolvedProblems\" : \""+ solvedProblems.size() + "\",\n";
		jsonResult += "\t\"solvedProblems\" : [\n";
		int problemTagSize = solvedProblems.size();
		for(int i=0; i<problemTagSize; i++) {
			jsonResult += "\t\t\""+ solvedProblems.get(i) + "\"";
			if(i != problemTagSize-1) {
				jsonResult += ",\n";
			}
		}
		jsonResult += "\n\t],\n";
		
		ArrayList<String> unsolvedProblems = this.crawlUnsolvedProblem(userID);

		jsonResult += "\t\"countUnsolvedProblems\" : \""+ unsolvedProblems.size() + "\",\n";
		jsonResult += "\t\"unsolvedProblems\" : [\n";
		problemTagSize = unsolvedProblems.size();
		for(int i=0; i<problemTagSize; i++) {
			jsonResult += "\t\t\""+ unsolvedProblems.get(i) + "\"";
			if(i != problemTagSize-1) {
				jsonResult += ",\n";
			}
		}
		jsonResult += "\n\t],\n";
		
		
		LocalDate localDate = LocalDate.now();
		jsonResult += "\t\"updateDate\" : \""+DTF.format(localDate) + "\"";
		jsonResult += "\n}";
		
		if(SHOW_LOG) {
			updateLog(jsonResult);
		}
		
		//Write problem json as userID.json
		File file = new File("data/users/"+userID+".json");
		
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(jsonResult);
			fw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void writeProblemJson(String problemID) {
		
		ArrayList<String> json_categories = new ArrayList<String>();
		json_categories.add("percentCorrect");
		json_categories.add("memoryLimit");
		json_categories.add("correctNumber");
		json_categories.add("submittedNumber");
		json_categories.add("solvedPeopleNumber");
		json_categories.add("timeLimit");
		
		this.receiveProblemDocument(problemID);
		String jsonResult = "{\n\t\"problemNumber\" : \""+problemID+"\",\n";
		
		int i=0;
		Map <String,String> problemState = this.crawlProblemSubmitState();
		for(String category : problemState.keySet()) {
			String categoryContent = "\t\"" + json_categories.get(i) + "\" :\"" + problemState.get(category) + "\",\n";
			i+=1;
			jsonResult += categoryContent;
		}
		
		LocalDate localDate = LocalDate.now();
		jsonResult += "\t\"updateDate\" : \""+DTF.format(localDate) + "\",\n";
		
		ArrayList<String> problemAlgorithms = this.crawlAlgorithms();
		jsonResult += "\t\"algorithms\" : [\n";
		int algorithmTagSize = problemAlgorithms.size();
		for(i=0; i<algorithmTagSize; i++) {
			jsonResult += "\t\t\""+ problemAlgorithms.get(i) + "\"";
			if(i != algorithmTagSize-1) {
				jsonResult += ",\n";
			}
		}
		jsonResult += "\n\t]\n}";
		
		if(SHOW_LOG) {
			updateLog(jsonResult);
		}
		
		//Write problem json as problemID.json
		File file = new File("data/problems/"+problemID+".json");
		
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(jsonResult);
			fw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	public float calcRating(String prevProblem, String thisProblem,String exRating) {
		float floatExRating = Float.parseFloat(exRating);
		float rating= floatExRating;
		prevProblem = prevProblem.replace(" ","");
		thisProblem = thisProblem.replace(" ","");
		String[] prevProbList = prevProblem.split(",");
		String[] thisProbList = thisProblem.split(",");
		ArrayList < String > prevList = new ArrayList<>(Arrays.asList(prevProbList)); 
		ArrayList < String > thisList = new ArrayList<>(Arrays.asList(thisProbList)); 
		
		thisList.removeAll(prevList);
		
		for( String item: thisList) {
			int temp = problemRating.get(item);
			if(temp == -1) continue;
			rating += ((float)temp/floatExRating) * 25;
		}
		updateLog("rating" + Float.toString(rating));
		return rating;
	}
}
