package web;

import datateam.BaekjoonCrawler;
import java.util.ArrayList;

public class ratingUpdate {
	public static void main(String[] args) {
		Database db = new Database();
		BaekjoonCrawler bj = new BaekjoonCrawler("ksaid0203", "rlawngh2@");
		ArrayList<String[]> ans = db.readUserdata("ksaid0203", "solvedproblem");
		String prev = "";
		String cur = "";
		String rating = "1500";
		for(int i = 0 ; i< ans.size();  ++i) {
			cur = ans.get(i)[2];
			int ao = (int)bj.calcRating(prev, cur, rating);
			System.out.println(ao);
			rating = Integer.toString(ao);
			
			prev = cur;
		}
	}
}
