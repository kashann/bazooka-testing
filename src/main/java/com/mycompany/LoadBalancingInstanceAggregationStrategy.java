package com.mycompany;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class LoadBalancingInstanceAggregationStrategy implements AggregationStrategy {

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		if(oldExchange == null) {
			return newExchange;
		}
		else {
			LoadBalancingInstance oldInstance = oldExchange.getIn().getBody(LoadBalancingInstance.class);
			LoadBalancingInstance newInstance = newExchange.getIn().getBody(LoadBalancingInstance.class);
			oldInstance = oldInstance.selectBetween(newInstance);
			oldExchange.getIn().setBody(oldInstance);
			return oldExchange;
		}
	}
}
