package com.camelrouting;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringXMLRoute {

	public static void main(String[] args) throws Exception { 
	ApplicationContext appContext = new ClassPathXmlApplicationContext(
            "SpringXMLRouteContext.xml");
    CamelContext camelContext = SpringCamelContext.springCamelContext(
            appContext, false);
    try {            
        camelContext.start();
        ProducerTemplate template = camelContext.createProducerTemplate();
        InputStream orderxml = new FileInputStream("src/main/resources/order.xml");
        template.sendBody("direct:SpringXMLRouteStart", orderxml);
    } finally {
        camelContext.stop();
    }

	}

}
