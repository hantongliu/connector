package io.hantong;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

	public Map<String, Collection<byte[]>> getRoadConditions() {
		Map<String, Collection<byte[]>> map = new HashMap<>();
		for (String station : stationList) {
			map.put(station, getCollction(station));
		}
		return map;
	}

	public Collection<RoadDataMeasurement> getRoadConditions(String stationId, Date since) {
		Collection<RoadDataMeasurement> res = new ArrayList<>();
		try {
			String url = "https://opendata.dwd.de/weather/weather_reports/road_weather_stations/" + stationId;
			Document doc = Jsoup.connect(url).get();
			String s = doc.body().text();
			List<String> list = new ArrayList<>(Arrays.asList(s.split(System.getProperty("line.separator"))));
			if (list.size() > 0)
				list.remove(0); // remove the title
			for (String line : list) {
				// sa[0]:fileName, sa[1]:Date(dd-MM-yyyy) sa[2]:Date(HH:mm) sa[3]:byte[].length
				String[] sa = line.split("\\s+");
				SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
				Date date = format.parse(sa[1] + " " + sa[2]);
				if (date.after(since) || date.equals(since)) {
					res.add(new RoadDataMeasurement(date, stationId, getBytes(url + "/" + sa[0])));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("Total number of valid data: " + res.size());
		return res;
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

	// get the href content from url page. removeLast:remove ending "/" in a folder.
	private List<String> getHrefFromURL(String url, boolean removeLast) {
		List<String> list = new ArrayList<>();
		try {
			Document doc = Jsoup.connect(url).get();
			Elements links = doc.select("a[href]");
			if (links.size() > 0) {
				links.remove(0);
			}
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

	private Collection<byte[]> getCollction(String station) {
		Collection<byte[]> values = new ArrayList<>();
		String url = URL + "/" + station;
		List<String> fileList = getHrefFromURL(url, false);
		for (String file : fileList) {
			values.add(getBytes(url + "/" + file));
		}
		System.out.println("Total number of data: " + values.size());
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

}
