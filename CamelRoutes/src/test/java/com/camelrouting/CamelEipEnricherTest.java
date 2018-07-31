package com.camelrouting;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
//import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;

public class CamelEipEnricherTest extends CamelSpringTestSupport {//CamelTestSupport {

    @Test
    public  void testEnrich() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMessageCount(1);

        template.sendBody("direct:start", "Hello world");
        
        assertMockEndpointsSatisfied();

    }
    /*
    @Override
    protected  RouteBuilder createRouteBuilder() throws Exception {
    	return 
    }*/
    
    @Override
	protected AbstractXmlApplicationContext createApplicationContext() {
		return new ClassPathXmlApplicationContext("SpringXMLEnrichRouteContext.xml");
	}

}