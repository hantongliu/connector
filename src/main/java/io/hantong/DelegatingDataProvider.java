package io.hantong;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class DelegatingDataProvider {
	private static volatile DelegatingDataProvider instance;
	private static List<String> stationList;
	private static final String URL = "https://opendata.dwd.de/weather/weather_reports/road_weather_stations/";

	private DelegatingDataProvider() {
		try {
			stationList = getFromURL(URL, "/\">", "/</a>");
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	// get the content from url and return a list of items, which are in between of
	// "left" and "right" elements
	@SuppressWarnings("deprecation")
	private List<String> getFromURL(String url, String left, String right) throws MalformedURLException, IOException {
		List<String> list;
		InputStream in = new URL(url).openStream();
		try {
			String s = IOUtils.toString(in);
			//System.out.println(s);
			String[] stationArray = StringUtils.substringsBetween(s, left, right);
			list = new ArrayList<>(Arrays.asList(stationArray));
			list.remove(0);  //remove "../"
//			for (String l : list)
//				System.out.println(l);

		} finally {
			IOUtils.closeQuietly(in);
		}
		return list;
	}

	public Map<String, Collection<byte[]>> getRoadConditions() throws IOException {
		Map<String, Collection<byte[]>> map = new HashMap<>();
		for (String station : stationList) {
			map.put(station, getCollction(station));
		}
		return map;
	}

	private Collection<byte[]> getCollction(String station) {
		Collection<byte[]> values = new ArrayList<>();
		try {
			String url = URL + "/" + station;
			List<String> fileList = getFromURL(url, "\">", "</a>");
			for (String file : fileList) {
				values.add(getBytes(url + "/" + file));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
		// todo: fetch and return data from
		// https://opendata.dwd.de/weather/weather_reports/road_weather_stations/
		return null;
	}
}
