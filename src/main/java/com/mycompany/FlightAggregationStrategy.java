package com.mycompany;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class FlightAggregationStrategy implements AggregationStrategy {

	@SuppressWarnings("unchecked")
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		System.out.println(oldExchange); //log
		System.out.println(newExchange); //log
		System.out.println("test flight"); //log
		if (oldExchange == null) {
            return newExchange;
        }
		Flight flight = oldExchange.getIn().getBody(Flight.class);
		System.out.println("FLIGHT:" + flight.toString()); //log
		Map<String, Object> map = newExchange.getIn().getBody(Map.class);
		String from = (String) map.get("from_city");
		String to = (String) map.get("to_city");
		String airline = (String) map.get("airline");
		String aircraft = (String) map.get("aircraft");
		String image = (String) map.get("image");
		flight.setDetails(from, to, airline, aircraft, image);
		System.out.println("aggregation "+flight.toString()); //log
		return oldExchange;
	}

}
