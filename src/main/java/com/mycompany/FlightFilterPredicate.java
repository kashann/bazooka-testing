package com.mycompany;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;

public class FlightFilterPredicate implements Predicate {

	private double latitude;
	private double longitude;
	private double distance;

	public FlightFilterPredicate(double latitude, double longitude, double distance) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.distance = distance;
	}

	@Override
	public boolean matches(Exchange exchange) {
		Flight flight = exchange.getIn().getBody(Flight.class);
		double distance = distanceBetween(latitude, longitude, flight.getLatitude(), flight.getLongitude());
		flight.setDistance(distance / 1000);
		return flight.getCode() != null && flight.getDistance() <= this.distance;
	}

	private static double distanceBetween(double sourceLatitude, double sourceLongitude, 
			double destinationLatitude, double destinationLongitude) {
		double R = 6378137;
		double dLat = (destinationLatitude - sourceLatitude) * Math.PI / 180;
		double dLng = (destinationLongitude - sourceLongitude) * Math.PI / 180;
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(sourceLatitude * Math.PI / 180)
				* Math.cos(destinationLatitude * Math.PI / 180)
				+ Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = R * c;
		return Math.round(d);
	}
}
