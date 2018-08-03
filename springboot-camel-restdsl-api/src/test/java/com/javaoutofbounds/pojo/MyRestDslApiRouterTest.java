package com.javaoutofbounds.pojo;

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assume.*;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MyRestDslApiRouterTest {
	
	@Autowired
	   private TestRestTemplate restTemplate;

	@Autowired
	private CamelContext camelContext;
/*
	@Override
	protected CamelContext createCamelContext() throws Exception {
		return camelContext;
	}

	@EndpointInject(uri = "file:D://projects//springboot-camel//src//main//resources//input/quartz?noop=true")
	private ProducerTemplate endpoint;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		RouteDefinition definition = context().getRouteDefinitions().get(0);
		definition.adviceWith(context(), new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				onException(Exception.class).maximumRedeliveries(0);
			}
		});
	}

	@Override
	public String isMockEndpointsAndSkip() {
        	return "file:D://projects//springboot-camel//src//main//resources//output//quartz";
	}

	@Test
	public void shouldSucceed() throws Exception {
		assertNotNull(camelContext);
		assertNotNull(endpoint);
		
		String expectedValue = "expectedValue";
		MockEndpoint mock = getMockEndpoint("mock:file:D://projects//springboot-camel//src//main//resources//output//quartz");
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
		endpoint.sendBodyAndHeader(in, "MY_HEADER", "testHeader");
		
		mock.assertIsSatisfied();
	}*/
	@Test
	public void sayHelloTest() {
	   // Call the REST API
	   ResponseEntity<String> response = restTemplate.getForEntity("/student/hello/Peter", String.class);
	   System.out.println("response.getStatusCode()--->"+ response.getStatusCode());
	   System.out.println("response.getBody()--->"+ response.getBody());
	 
	   //assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	   String s = response.getBody();
	   assertEquals("200",response.getStatusCode().toString());
	   assertEquals("\"Hello Peter, Welcome to JavaOutOfBounds.com\"",response.getBody().toString());
	   //assertThat(s.equals("Hello World"));
	}
	
	@Test
	public void sayRecordTest() {
	   // Call the REST API
	   ResponseEntity<Student> response = restTemplate.getForEntity("/student/records/Sam", Student.class);
	   
	   Student student = response.getBody();
	   
	   System.out.println("response.getStatusCode()--->"+ response.getStatusCode());
	   System.out.println("response.getBody() Name--->"+ student.getName());
	   System.out.println("response.getBody() Subject--->"+ student.getSubject());
	   
	   //assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	   Student s = response.getBody();
	   //assertThat(s.equals("Hello World"));
	}
}
