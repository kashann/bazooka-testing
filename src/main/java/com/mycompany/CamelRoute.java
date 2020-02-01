package com.mycompany;

import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.websocket.WebsocketComponent;
import org.apache.camel.model.dataformat.JsonLibrary;

public class CamelRoute extends RouteBuilder {

	private double latitude;
	private double longitude;
	private double distance;
	private int port;

	public CamelRoute(double latitude, double longitude, double distance, int port) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.distance = distance;
		this.port = port;
	}

	@Override
	public void configure() throws Exception {
		WebsocketComponent component = getContext().getComponent("websocket", WebsocketComponent.class);
		component.setPort(port);
		component.setStaticResources("classpath:.");
		onException(Exception.class).log("${body}");
		from("timer:poll?fixedRate=true&period=1000&delay=0").doTry()
		.setHeader(Exchange.HTTP_METHOD, constant("GET"))
		.setBody(constant(""))
		.to("jetty:https://www.flightradar24.com/balance.json")
		.convertBodyTo(String.class).unmarshal().json(JsonLibrary.Jackson)
		.bean(LoadBalancingInstance.class, "extract")
		.setHeader("id")
		.method(CamelRoute.class, "getBatchId")
		.split().body()
		.aggregate(new LoadBalancingInstanceAggregationStrategy())
		.header("id").completionTimeout(1000)
		.setHeader(Exchange.HTTP_METHOD, constant("GET"))
		.setHeader(Exchange.HTTP_QUERY, simple("bounds=44.73,44.19,25.56,26.50")) //bucharest bounds
		//.setHeader(Exchange.HTTP_QUERY, simple("bounds=48.09,20.54,43.75,28.56")) //romania bounds
		.setHeader("host", simple("data-live.flightradar24.com"))
		.setBody(constant(""))
		.recipientList(simple("jetty:https://${header.host}/zones/fcgi/feed.js"))
		.convertBodyTo(String.class)
		.unmarshal().json(JsonLibrary.Jackson)
		.setHeader("id")
		.method(CamelRoute.class, "getBatchId")
		.bean(Flight.class, "extract")
		.split().body()
		.filter(new FlightFilterPredicate(latitude, longitude, distance))
		.enrich("direct:flight", new FlightAggregationStrategy()) //problem
		.process(new Processor() { //trying to debug
			@Override
			public void process(Exchange exchange) throws Exception {
				System.out.println("processor");
			}
		})
		.resequence(simple("${body.distance}"))
		.aggregate(new FlightsAggregationStrategy()) //problem
		.header("id")
		.completionTimeout(1000)
		.marshal().json(JsonLibrary.Jackson)
		.to("websocket:flights?sendToAll=true");
		from("direct:flight").doTry()
		.setHeader(Exchange.HTTP_METHOD, constant("GET"))
		.setHeader(Exchange.HTTP_QUERY, simple("f=${body.code}"))
		.setBody(constant(""))
		.recipientList(simple("jetty:https://${header.host}/planedata"))
		.convertBodyTo(String.class)
		.unmarshal().json(JsonLibrary.Jackson);
	}
	
	public static String getBatchId() {
		return UUID.randomUUID().toString();
	}
}
