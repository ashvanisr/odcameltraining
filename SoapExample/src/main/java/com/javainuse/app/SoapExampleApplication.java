package com.javainuse.app;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SoapExampleApplication {

	public static void main(String[] args) throws Exception { 
		ApplicationContext appContext = new ClassPathXmlApplicationContext(
	            "META-INF\\spring\\applicationContext.xml");
	    CamelContext camelContext = SpringCamelContext.springCamelContext(
	            appContext, false);
	    try {            
	        camelContext.start();
	        while(true) {
	        	
	        }
	       // ProducerTemplate template = camelContext.createProducerTemplate();
	       // template.sendBody("direct:start", "hello");
	    } catch(Exception e){
	    	System.out.println("exception"+e);
	    	
	    }finally {
	      //  camelContext.stop();
	    }

		}
}
