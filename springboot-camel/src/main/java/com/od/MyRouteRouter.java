package com.od;


import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
 
//@Component
public class MyRouteRouter extends RouteBuilder {
 
    @Override
    public void configure() throws Exception {
    	from("quartz://currentTimer?trigger.repeatInterval=1000&trigger.repeatCount=5")
		.setBody().simple("Preparaing for interview")
		.to("stream:out");
    }
}
