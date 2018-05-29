package io.hantong;

import java.util.Collection;
import java.util.Date;

public class RoadDataMeasurement {

	private Date date;
	private String weatherStationId;
	private Collection<byte[]> measuredData;

	public RoadDataMeasurement(Date date, String weatherStationId, Collection<byte[]> measuredData) {
		super();
		this.date = date;
		this.weatherStationId = weatherStationId;
		this.measuredData = measuredData;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getWeatherStationId() {
		return weatherStationId;
	}

	public void setWeatherStationId(String weatherStationId) {
		this.weatherStationId = weatherStationId;
	}

	public Collection<byte[]> getMeasuredData() {
		return measuredData;
	}

	public void setMeasuredData(Collection<byte[]> measuredData) {
		this.measuredData = measuredData;
	}

}
