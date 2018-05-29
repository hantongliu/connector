package io.hantong;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DelegatingDataProvider {
	private static volatile DelegatingDataProvider instance;
	private static List<String> stationList;
	private static final String URL = "https://opendata.dwd.de/weather/weather_reports/road_weather_stations/";

	private DelegatingDataProvider() {
		stationList = getHrefFromURL(URL, true);
	}

	public static DelegatingDataProvider getInstance() {
		if (instance == null) {
			synchronized (DelegatingDataProvider.class) {
				if (instance == null) {
					instance = new DelegatingDataProvider();
				}
			}
		}
		return instance;
	}

	public List<String> getStationList() {
		return stationList;
	}

	private List<String> getHrefFromURL(String url, boolean removeLast) {
		List<String> list = new ArrayList<>();
		try {
			Document doc = Jsoup.connect(url).get();
			Elements links = doc.select("a[href]");
			links.remove(0);
			for (Element link : links) {
				String s = link.text();
				if (removeLast && s != null && s.length() > 0) {
					list.add(s.substring(0, s.length() - 1));
				} else {
					list.add(s);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	public Map<String, Collection<byte[]>> getRoadConditions() {
		Map<String, Collection<byte[]>> map = new HashMap<>();
		for (String station : stationList) {
			map.put(station, getCollction(station));
		}
		return map;
	}

	private Collection<byte[]> getCollction(String station) {
		Collection<byte[]> values = new ArrayList<>();
		String url = URL + "/" + station;
		List<String> fileList = getHrefFromURL(url, false);
		for (String file : fileList) {
			values.add(getBytes(url + "/" + file));
		}
		return values;
	}

	private byte[] getBytes(String url) {
		byte[] b;
		try {
			URLConnection conn = new URL(url).openConnection();
			InputStream is = conn.getInputStream();
			b = IOUtils.toByteArray(is);
			System.out.println(url + " has Byte length: " + b.length);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return b;
	}

	public Collection<RoadDataMeasurement> getRoadConditions(String stationId, Date since) {
		Collection<RoadDataMeasurement> res = new ArrayList<>();
		return res;
	}
}
