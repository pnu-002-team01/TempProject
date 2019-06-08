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

public class CheckDuplication {
	
	private static final String PUBLIC_IP = "http://3.16.83.76/";
	private static final String Check = "checkfile.php";
	private static String num;
	private static URL url;
	private static URLConnection conn;
	
	public CheckDuplication(String num) {
		this.num = "p"+num;
		try {
			url = new URL(PUBLIC_IP+Check);
			conn = url.openConnection();
		} catch ( MalformedURLException e ) {
			e.printStackTrace();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	public int Check() {
		String line = "";
		try {
			String param = URLEncoder.encode("source", "UTF-8")+"="+URLEncoder.encode(num, "UTF-8");
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
			line = rd.readLine();
			out.close();
			rd.close();
		} catch ( MalformedURLException e ) {
			e.printStackTrace();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		return Integer.parseInt(line);
	}
	
	public String getResult() {
		String r = "";
		ArrayList<String> result = new ArrayList<>();
		try {
			URL url2 = new URL(PUBLIC_IP+"/result/"+num+".txt");
			URLConnection conn2 = url2.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn2.getInputStream(),"UTF-8"));
			String line;
			while ( (line = rd.readLine()) != null ) {
				if ( line.contains("java") ) {
					line = line.replaceAll("\\[[\\w]*\\] /home/ubuntu/workspace/./javasource/[\\w]*.java:", "");
					line = line.replaceAll(":[0-9]*:",":");
					line = "Line " + line;
					result.add(line);
				} else if ( line.contains("cppsource") ) {
					line = line.replaceAll("\\[cppsource/[\\w]*.cpp:", "");
					line = line.replace("]", "");
					line = "Line " + line;
					result.add(line);
				}
			}
			rd.close();
		} catch ( MalformedURLException e ) {
			e.printStackTrace();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		for ( int i = 0; i < result.size()-3; i++ )
			if ( i > 1 )
				r += result.get(i) + "\n";
		return r;
	}
}
