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



import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static org.apache.camel.model.rest.RestParamType.body;


@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    //h2 console
    //JDBC URL: jdbc:h2:mem:testdb
    //http://localhost:8093/console
    //http://localhost:8093/camel-rest-jpa/books/
    
    //ActiveMQ admin : http://localhost:8161/admin  user:admin password:admin
    
    //{"id":5,"amount":2,"book":{"id":1,"item":"Camel","description":"Camel in Action"},"processed":true}
    
    //This servlet is for h2 console : http://localhost:8093/console
    /*
    @Bean
    ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }*/
    
    @Autowired
    CamelContext context;
    
    @Component
    class RestApi extends RouteBuilder {

        @Override
        public void configure() {
        	
        	context.setUseMDCLogging(true);
        	context.setTracing(true);
        	
        System.out.println("spring boot for rest JMS route started******");
            restConfiguration()
                .contextPath("/camel-rest-jms").apiContextPath("/api-doc")
                .apiProperty("host", "localhost:8093") //by default 0.0.0.0
                .apiProperty("base.path", "camel-rest-jms")
                    .apiProperty("api.title", "Camel REST Gateway Update Order API")
                    .apiProperty("api.version", "1.0")
                    .apiProperty("cors", "true")
                    .apiContextRouteId("doc-api");
            /*
             * uncommenting below line will cause double quote disappeared from json string and semicolon replaced by equal =
             */
                //.bindingMode(RestBindingMode.json);

            rest("/books").description("Books REST service")
                
                    
            	.put("/order").description("Book Order PUT (Update) REST service")
            	.consumes("application/json")
            	//.type(String.class)
            	.outType(String.class)
            	.param().name("body").type(body).description("The order to update {\"id\":1,\r\n" + 
            			"\"amount\":1800,\r\n" + 
            			"\"book\":{\"id\":1,\"item\":\"Camel\",\"description\":\"Camel in Action\"},\r\n" + 
            			"\"processed\":false}").endParam()
            	.route()
            	.setExchangePattern(ExchangePattern.InOnly)
            	.convertBodyTo(String.class)
            	           	
            	//.type(Order.class).outType(Order.class).route().tracing() 
            	/*
            	.doTry()
            	.to("OrderServiceProcessor")
            	.doCatch(Exception.class).process(new Processor() {
					public void process(Exchange exchange) throws Exception {
						System.out.println("save payload");
						String order = exchange.getIn().getBody(String.class);
						System.out.println("saveOrder called with: " + order);
				        
						Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
						System.out.println(cause);
					}})    */ 
            	
            	.to("log:saveOrderJMS?level=INFO&showAll=true")
            	.to("activemq:order")
            	.setBody(simple("{\"status\":\"OK\"}"));
            
                    /*
                .get("order/{id}").description("Details of an order by id")
                    .route().routeId("order-api")
                    .bean(Database.class, "findOrder(${header.id})");
                    */
        }
    }

    /*
    @Component
    class Backend extends RouteBuilder {

        @Override
        public void configure() {
            // A first route generates some orders and queue them in DB
            from("timer:new-order?delay=1s&period={{example.generateOrderPeriod:2s}}")
                .routeId("generate-order")
                .bean("orderService", "generateOrder")
                .to("jpa:org.apache.camel.example.spring.boot.rest.jpa.Order")
                .log("Inserted new order ${body.id}");

            // A second route polls the DB for new orders and processes them
            from("jpa:org.apache.camel.example.spring.boot.rest.jpa.Order"
                + "?consumer.namedQuery=new-orders"
                + "&consumer.delay={{example.processOrderPeriod:5s}}"
                + "&consumeDelete=false")
                .routeId("process-order")
                .log("Processed order #id ${body.id} with ${body.amount} copies of the «${body.book.description}» book and processed status -- ${body.processed} ");
        }
    }*/
}