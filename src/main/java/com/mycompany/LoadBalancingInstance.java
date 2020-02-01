package com.mycompany;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoadBalancingInstance {
	private String URL;
	private int workload;
	
	public LoadBalancingInstance(String url, int workload) {
		super();
		this.URL = url;
		this.workload = workload;
	}

	public String getURL() {
		return URL;
	}

	public int getWorkload() {
		return workload;
	}
	
	public static List<LoadBalancingInstance> extract(Map<String, Object> map){
		List<LoadBalancingInstance> instances = new ArrayList<LoadBalancingInstance>();
		for(String key : map.keySet()) {
			int workload = (Integer)map.get(key);
			LoadBalancingInstance instance = new LoadBalancingInstance(key, workload);
			instances.add(instance);
		}
		return instances;
	}

	public LoadBalancingInstance selectBetween(LoadBalancingInstance newInstance) {
		return newInstance.workload < workload ? newInstance : this;
	}
}
