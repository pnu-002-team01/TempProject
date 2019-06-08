package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FunctionToCompare {
	
	private static ArrayList<String> interList;
	private static ArrayList<String>[] list = new ArrayList[2];
	
	public FunctionToCompare(ArrayList<String> userIDs) {
		try {
			for (int i = 0; i < 2; ++i) {
				list[i] = new ArrayList<>();
				String connUrl = "https://www.acmicpc.net/user/" + userIDs.get(i);
				Connection.Response response = Jsoup.connect(connUrl).method(Connection.Method.GET).execute();
				Document doc = response.parse();
			
				Element CorrectNum = doc.select(".panel-body").first();
				Elements problems = CorrectNum.select(".panel-body").select(".problem_number");

				for (Element e : problems) {
					list[i].add(e.select("a").text().toString());
				}
			}
			interList = intersection(list[0], list[1]);
			
			list[0].removeAll(interList);
			list[1].removeAll(interList);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> get(int i) {
		if ( i == 0 )
			return list[0];
		else
			return list[1];
	}
	
	public static ArrayList<String> inter() {
		return interList;
	}

    public static <T> ArrayList<T> intersection(List<T> list1, List<T> list2) {
        ArrayList<T> list = new ArrayList<T>(); 
        for (T t : list1) {
            if (list2.contains(t)) {
                list.add(t);
            }
        } 
        return list;
    }
}
