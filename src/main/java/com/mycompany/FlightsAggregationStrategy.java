package com.mycompany;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class FlightsAggregationStrategy implements AggregationStrategy {

	@SuppressWarnings("unchecked")
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		System.out.println(oldExchange); //log
		System.out.println(newExchange); //log
		System.out.println("test flights"); //log
		Flight flight = newExchange.getIn().getBody(Flight.class);
		System.out.println("flightsagrg " + flight.toString()); //log
		if(oldExchange == null) {
			List<Flight> flights = new ArrayList<Flight>();
			flights.add(flight);
			newExchange.getIn().setBody(flights);
			System.out.println(flights.toString()); //log
			return newExchange;
		}
		else{
			List<Flight> flights = oldExchange.getIn().getBody(List.class);
			flights.add(flight);
			System.out.println(flights.toString()); //log
			return oldExchange;
		}
	}

}
