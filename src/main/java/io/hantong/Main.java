package io.hantong;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class Main {

	public static void main(String[] args) {
		//DelegatingDataProvider.getInstance().getRoadConditions();
		DelegatingDataProvider.getInstance().getRoadConditions();
		
		String sDate = "28-May-2018 10:17";
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
		try {
			Date date = formatter.parse(sDate);
			System.out.println(sDate+"\t"+date);
			DelegatingDataProvider.getInstance().getRoadConditions("DD", date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			String url = "https://opendata.dwd.de/weather/weather_reports/road_weather_stations/DD";
			Document doc = Jsoup.connect(url).get();
			String s = doc.body().text();
			String[] lines = s.split(System.getProperty("line.separator"));
			System.out.println(lines[1]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
