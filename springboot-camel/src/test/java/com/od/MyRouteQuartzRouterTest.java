package com.od;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.DisableJmx;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@DisableJmx(true)
public class MyRouteQuartzRouterTest extends CamelTestSupport {
	
	 private static final String URI_START_ENDPOINT = "direct:teststart";
	 private static final String from_uri = "file:D://projects//springboot-camel//src//main//resources//input/quartz?noop=true";

	@Autowired
	private CamelContext camelContext;

	@Override
	protected CamelContext createCamelContext() throws Exception {
		return camelContext;
	}

	@EndpointInject(uri = "direct:teststart")
	private ProducerTemplate endpoint;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		RouteDefinition definition = context().getRouteDefinitions().get(0);
		definition.adviceWith(context(), new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				//replaceRouteFromWith("MyRouteID" , URI_START_ENDPOINT);
				//weaveAddLast().to("mock:end");
				onException(Exception.class).maximumRedeliveries(0);
			}
		});
	}
/*
	@Override
	public String isMockEndpointsAndSkip() {
        	return "file:D://projects//springboot-camel//src//main//resources//output//quartz";
	}*/
	
	@Override
    protected void doPostSetup() throws Exception {
        context.getRouteDefinition("MyRouteID").adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                replaceFromWith(URI_START_ENDPOINT);
                weaveAddLast().to("mock:end");
            }
        });
        context.start();
    }

	@Test
	public void shouldSucceed() throws Exception {
		assertNotNull(camelContext);
		assertNotNull(endpoint);
		
		String expectedValue = "expectedValue";
		//MockEndpoint mock = getMockEndpoint("mock:file:D://projects//springboot-camel//src//main//resources//output//quartz");
		MockEndpoint mock = getMockEndpoint("mock:end");
		mock.expectedMessageCount(1);
		//mock.allMessages().body().isEqualTo(expectedValue);
		//mock.allMessages().header(MY_HEADER).isEqualTo("testHeader");
		String in = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<orders>\r\n" + 
				"	<order product=\"electronics\">\r\n" + 
				"		<items>\r\n" + 
				"			<item>Laptop</item>\r\n" + 
				"			<item>Mobile</item>\r\n" + 
				"		</items>\r\n" + 
				"	</order>\r\n" + 
				"	<order product=\"books\">\r\n" + 
				"		<items>\r\n" + 
				"			<item>Design Patterns</item>\r\n" + 
				"			<item>XML</item>\r\n" + 
				"		</items>\r\n" + 
				"	</order>\r\n" + 
				"</orders>";
		//endpoint.sendBodyAndHeader(in, "MY_HEADER", "testHeader");
		template.sendBody(URI_START_ENDPOINT,in);
		mock.assertIsSatisfied();
	}
}
