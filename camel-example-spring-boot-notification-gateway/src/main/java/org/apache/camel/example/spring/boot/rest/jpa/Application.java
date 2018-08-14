/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.example.spring.boot.rest.jpa;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParser;





@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
    	
        SpringApplication.run(Application.class, args);
    }
    //h2 console
    //JDBC URL: jdbc:h2:mem:testdb username: sa password:
    //h2 server mode: jdbc url: jdbc:h2:~/testdb
    
    // h2 server console: http://192.168.252.186:8082
    
    //Active MQ console : http://localhost:8161/admin/queues.jsp
    	
    //embedded h2 console url: http://localhost:8091/console
    //http://localhost:8091/camel-rest-jpa/books/
    
    //{"id":5,"amount":2,"book":{"id":1,"item":"Camel","description":"Camel in Action"},"processed":true}
    
    //This servlet is for h2 console : http://localhost:8091/console
    /*
    @Bean
    ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }*/
    
    @Autowired
    CamelContext context;
    
    /*
    @Component
    class RestApi extends RouteBuilder {
    	
    	
    

        @Override
        public void configure() {
        	
        	context.setUseMDCLogging(true);
        	//context.setTracing(true);
        	
        	JacksonDataFormat orderJacksonDataFormat = new JacksonDataFormat(Order.class);
        	ObjectMapper objectMapper = new ObjectMapper();
        	objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        	orderJacksonDataFormat.setObjectMapper(objectMapper);
        	
        System.out.println("spring boot for rest route started******");
            restConfiguration()
                .contextPath("/camel-rest-jpa").apiContextPath("/api-doc")
                    .apiProperty("api.title", "Camel REST API")
                    .apiProperty("api.version", "1.0")
                    .apiProperty("cors", "true")
                    .apiContextRouteId("doc-api")
                .bindingMode(RestBindingMode.json);

            rest("/books").description("Books REST service")
                .get("/").description("The list of all the books")
                    .route().routeId("books-api")
                    .log("find invoked******${body}")
                    .to("log:findInvoked?level=INFO&showAll=true")
                    .to("sql:select id,amount,processed from orders?" +
                            "dataSource=dataSource&" +
                            "outputClass=org.apache.camel.example.spring.boot.rest.jpa.Order")
                    .endRest()
                    
            	.post("/order").consumes("application/json").type(Order.class).outType(Order.class).route().tracing()          	
            	.doTry()
            	.to("OrderServiceProcessor")
            	.doCatch(Exception.class).process(new Processor() {
					public void process(Exchange exchange) throws Exception {
						System.out.println("save payload");
						String order = exchange.getIn().getBody(String.class);
						System.out.println("saveOrder called with: " + order);
				        
						Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
						System.out.println(cause);
					}})
            	.to("log:save Invoked?level=DEBUG&showAll=true")
            	.to("log:saveOrder?level=INFO&showAll=true")          	
            	.to("log:saveOrderAfter?level=INFO&showAll=true")
            	.to("jpa:org.apache.camel.example.spring.boot.rest.jpa.Order");
            
                    
                //.get("order/{id}").description("Details of an order by id")
                  //  .route().routeId("order-api")
                    //.bean(Database.class, "findOrder(${header.id})");
                    
            
            	from("activemq:order")
            	.routeId("orderdequeue")
            	.convertBodyTo(String.class)
            	.routeId("orderconvertstring")
            	.to("log:updatePUTOrder?level=INFO&showAll=true")
            	.doTry()
            		.unmarshal(orderJacksonDataFormat)
            	.doCatch(Exception.class).process(new Processor() {
					public void process(Exchange exchange) throws Exception {
						System.out.println("put payload");
						String order = exchange.getIn().getBody(String.class);
						System.out.println("putOrder called with: " + order);
					}})
            	.routeId("orderunmarshal")
            	.to("log:updatePUT_MarshallOrder?level=INFO&showAll=true")
            	.routeId("orderunServiceProcessor")
            	.to("OrderUpdateServiceProcessor")
            	.to("log:updatePUT_HASHMAP_Order?level=INFO&showAll=true")
            	//.to("sql:update orders set amount=${body.amount},processed=${body.processed} where id=${body.id} ?" +
                    //    "dataSource=dataSource")
            	//
            	
            	.to("sql:update orders set amount=:#amount,processed=:#processed where id=:#id ?" +
                        "dataSource=dataSource")
            	.to("log:updatePUT_Final?level=INFO&showAll=true");
            	
        }
    }
*/
    
    @Component
    class Backend extends RouteBuilder {

        @Override
        public void configure() {
        	
        	context.setUseMDCLogging(true);
        	ExecutorService executor =  Executors.newFixedThreadPool(12);
        	
            // A first route generates some orders and queue them in DB
        	
        	from("file:src/main/resources/OrderSimulation/")
        	.to("controlbus:route?routeId=generateOrder&action=start");
        	
        	
        	//from("direct:triggerOrderSimulation")
            from("timer:new-order?delay=1s&repeatCount=100")
                .routeId("generateOrder").autoStartup(false)
                .bean("orderService", "generateOrder")
                .to("jpa:org.apache.camel.example.spring.boot.rest.jpa.Order")
                .log("Inserted new order ${body.id}")
                .end()
                .to("log:timeEndOrder?level=INFO&showAll=true");
                
               // .to("direct:processOrder");
			
            // A second route polls the DB for new orders and processes them
            
        	from("file:src/main/resources/OrderProcess/")
        	.to("direct:triggerOrderProcess");
        	
        	from("direct:triggerOrderProcess")
//        	.to("timer:new-order?delay=1s&repeatCount=1")
        	.to("direct:processOrder");
        	
            from("direct:processOrder")
            .to("sql:select * from orders where processed = false?dataSource=dataSource")
            .split().body().parallelProcessing().executorService(executor)
            .inOnly("activemq:notifyorder")
            .to("log:splitEachOrderForProcessing?level=INFO&showAll=true")
            .to("sql:update orders set processed=true where id=:#id ?" +
                        "dataSource=dataSource")
            
            ;
            
          
        }
    }
}