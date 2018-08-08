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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.h2.server.web.WebServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    //h2 console
    //JDBC URL: jdbc:h2:mem:testdb
    //http://localhost:8091/console
    //http://localhost:8091/camel-rest-jpa/books/
    
    //{"id":5,"amount":2,"book":{"id":1,"item":"Camel","description":"Camel in Action"},"processed":true}
    
    //This servlet is for h2 console : http://localhost:8091/console
    @Bean
    ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }
    
    @Component
    class RestApi extends RouteBuilder {

        @Override
        public void configure() {
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
                    .to("sql:select id,amount from orders?" +
                            "dataSource=dataSource&" +
                            "outputClass=org.apache.camel.example.spring.boot.rest.jpa.Order")
                    .endRest()
            	.post("/order").consumes("application/json").type(Order.class)  
            	
            	.to("jpa:org.apache.camel.example.spring.boot.rest.jpa.Order");
            
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