package com.mycompany;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Flight {
	private String code;
	private double latitude;
	private double longitude;
	private String number;
	private String from;
	private String to;
	private String airline;
	private String aircraft;
	private String image;
	private double distance;
	
	public Flight(String code, double latitude, double longitude, String number) {
		super();
		this.code = code;
		this.latitude = latitude;
		this.longitude = longitude;
		this.number = number;
	}
	
	public void setDetails(String from, String to, String airline, String aircraft, String image) {
		this.from = from;
		this.to = to;
		this.airline = airline;
		this.aircraft = aircraft;
		this.image = image;
		System.out.println("SETTING DETAILS" + this.toString());
	}

	public String getCode() {
		return code;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getNumber() {
		return number;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public String getAirline() {
		return airline;
	}

	public String getAircraft() {
		return aircraft;
	}

	public String getImage() {
		return image;
	}

	public double getDistance() {
		return distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Flight> extract(Map<String, Object> map){
		List<Flight> flights = new ArrayList<Flight>();
		for(String key : map.keySet()) {
			Object value = map.get(key);
			if(!(value instanceof  List<?>))
				continue;
			List<Object> list = (List<Object>) value;
			String code = key;
			double latitude = (Double) list.get(1);
			double longitude = (Double) list.get(2);
			String number = (String) list.get(7);
			Flight flight = new Flight(code, latitude, longitude, number);
			flights.add(flight);
		}
		return flights;
	}

	@Override
	public String toString() {
		return "Flight [code=" + code + ", latitude=" + latitude + ", longitude=" + longitude + ", number=" + number
				+ ", from=" + from + ", to=" + to + ", airline=" + airline + ", aircraft=" + aircraft + ", image="
				+ image + ", distance=" + distance + "]";
	}
	
	
}
