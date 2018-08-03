package com.javaoutofbounds.pojo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.apache.camel.swagger.servlet.RestSwaggerServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import io.swagger.annotations.ApiResponse;

@SpringBootApplication
public class SpringbootCamelRestdslApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootCamelRestdslApiApplication.class, args);
	}
	
	/*
	 @Bean
	    public ServletRegistrationBean swaggerServlet() {
	        ServletRegistrationBean swagger = new ServletRegistrationBean(new RestSwaggerServlet(), "/api-doc/*");
	        Map<String, String> params = new HashMap<>();
	        params.put("base.path", "api");
	        params.put("api.title", "my api title");
	        params.put("api.description", "my api description");
	        params.put("api.termsOfServiceUrl", "termsOfServiceUrl");
	        params.put("api.license", "license");
	        params.put("api.licenseUrl", "licenseUrl");
	        swagger.setInitParameters(params);
	        return swagger;
	    }*/
	 
	@Component
	class StudentRoute extends RouteBuilder {

		@Override
		public void configure() {
			
			//https://developers.redhat.com/blog/2018/04/04/testing-camel-rest-dsl-spring-boot/
			//http://localhost:8091/student/hello/Peter
			//http://localhost:8091/student/records/Sam
			//http://localhost:8091/swagger
			//http://localhost:8091/webjars/swagger-ui/index.html?url=/swagger&validatorUrl=
			
			restConfiguration()
			.component("servlet")
			.bindingMode(RestBindingMode.json)
			//Enable swagger endpoint.
			.apiContextPath("/swagger") //swagger endpoint path
			.apiContextRouteId("swagger") //id of route providing the swagger endpoint

			 //Swagger properties
			.contextPath("/api") //base.path swagger property; use the mapping path set for CamelServlet
			.apiProperty("api.title", "Example REST api")
			.apiProperty("api.version", "1.0")
			;

			rest("/student").description("Students API").produces("application/json")
			.get("/hello/{name}")
			//swagger
			  .description("Query student")
			  .param().name("name").type(RestParamType.path).description("name of the student").required(true).dataType("string").endParam()
			  .responseMessage().code(200).responseModel(Student.class).endResponseMessage() //OK
			  .responseMessage().code(500).responseModel(ApiResponse.class).endResponseMessage() //Not-OK
			.route().transform().simple("Hello ${header.name}, Welcome to JavaOutOfBounds.com")
			.endRest()
			.get("/records/{name}").to("direct:records");

			from("direct:records")
			.process(new Processor() {

				final AtomicLong counter = new AtomicLong();

				@Override
				public void process(Exchange exchange) throws Exception {

					final String name = exchange.getIn().getHeader("name",String.class);
					exchange.getIn().setBody(new Student(counter.incrementAndGet(),name,"Camel + SpringBoot"));
				}
			});
		}
	}
}
