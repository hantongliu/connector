package io.hantong;

import java.util.Date;

public class RoadDataMeasurement {

	private Date date;
	private String weatherStationId;
	private byte[] measuredData;

	public RoadDataMeasurement(Date date, String weatherStationId, byte[] measuredData) {
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

	public byte[] getMeasuredData() {
		return measuredData;
	}

	public void setMeasuredData(byte[] measuredData) {
		this.measuredData = measuredData;
	}

}
