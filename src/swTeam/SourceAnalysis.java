package swTeam;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SourceAnalysis {

	private static final String PUBLIC_IP = "http://3.16.83.76/";
	private static final String[] Source = { "cppcheck.php", "javacheck.php" };
	private static URL url;
	private static URLConnection conn;
	private static int type;
	private static int remove = 3;
	
	public SourceAnalysis(String t) {
		type = Integer.parseInt(t);
		try {
			url = new URL(PUBLIC_IP+Source[type]);
			conn = url.openConnection();
		} catch ( MalformedURLException e ) {
			e.printStackTrace();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	public String Analysis(String num, String code, String company) {
		ArrayList<String> result = new ArrayList<>();
		num = "p"+num;
		try {
			String param = URLEncoder.encode("source", "UTF-8")+"="+URLEncoder.encode(num, "UTF-8");
			param += "&"+URLEncoder.encode("content", "UTF-8")+"="+URLEncoder.encode(code, "UTF-8");
			param += "&"+URLEncoder.encode("company", "UTF-8")+"="+URLEncoder.encode(company, "UTF-8");
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		
			DataOutputStream out = null;
			
			try {
				out = new DataOutputStream(conn.getOutputStream());
				out.writeBytes(param);
				out.flush();
			} finally {
				if (out != null) out.close();
			}
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			String line;
			if ( type == 1 ) {
				while ( (line = rd.readLine()) != null ) {
					line = line.replaceAll("\\[[\\w]*\\] /home/ubuntu/workspace/./javasource/[\\w]*.java:", "");
					line = line.replaceAll(":[0-9]*:",":");
					line = "Line " + line;
					result.add(line);
				}
			} else {
				line = rd.readLine();	// Remove the first line that includes file info.
				while ( (line = rd.readLine()) != null ) {
					line = line.replaceAll("./cppsource/[\\w]*.cpp:", "");
					line = "Line " + line;
					result.add(line);
				}
			}
			out.close();
			rd.close();
		} catch ( MalformedURLException e ) {
			e.printStackTrace();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		if ( result.isEmpty() )
			result.add("There is no code smells or Analysis is not done");
		String r = "";
		for ( int i = 0; i < result.size()-remove; i++ )
			if ( i > 1 )
				r += result.get(i)+"\n";
		return r;
	}

}
