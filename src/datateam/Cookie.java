package datateam;

import java.util.Map;

public class Cookie {
	private static Cookie cookie = new Cookie();
	public Map<String, String> loginCookie = null;
	public String userID = null;
	public int try_login = 0;
	
	private Cookie() {
	}
	
	public void setCookie(Map<String, String> cookie) {
		this.loginCookie = cookie;
	}
	
	public void setUserId(String userId) {
		this.userID = userId;
	}
	
	public static Cookie getInstance() {
		return cookie;
	}
	
	public void logout() {
		this.loginCookie = null;
		this.userID = null;
	}
}
