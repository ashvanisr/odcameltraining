/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.camel.example.spring.boot.rest.jpa;


import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("OrderUpdateServiceProcessor")
public class OrderUpdateServiceProcessor implements Processor {
    private static final transient Logger LOG = LoggerFactory.getLogger(OrderUpdateServiceProcessor.class);

   // @Autowired
   // CustomerService customerService;


    //@Override
    public void process(Exchange exchange) throws Exception {
        Message inMessage = exchange.getIn();
        LOG.info("saveOrderProcessor **********************: ");
        
       Object messageObject = exchange.getIn().getBody();
       Order order3 = new Order();
       Order order = null;
       if (messageObject instanceof String) {
    	   String orderString = exchange.getIn().getBody(String.class);
    	   ObjectMapper mapper = new ObjectMapper();
           List<Order> myObjects = mapper.readValue(orderString, new TypeReference<List<Order>>(){});
          
              // Order[] orderList = inMessage.getBody(Order[].class);
               for (Order order2 : myObjects) {
               	order3.setId(order2.getId());
               	order3.setAmount(order2.getAmount());
               	 LOG.info("saveOrder called with: " + order2.getId());
                    LOG.info("saveOrder called with: " + order2.getAmount());
   			}
               
            order = order3;
       }else if (messageObject instanceof Order) {
    	    order = inMessage.getBody(Order.class);
       }
       
       
        
        
        LOG.info("saveOrderProcessor called with: " + order);
        
        Map<String,Object> orderMap = new HashMap<String,Object>();
        orderMap.put("id", order.getId());
        orderMap.put("amount", order.getAmount());
        LOG.info("putOrderProcessor called with: processed" + order.isProcessed());
        orderMap.put("processed", order.isProcessed());
        
        /*
        ObjectMapper mapper = new ObjectMapper();
        List<Order> myObjects = mapper.readValue(order, new TypeReference<List<Order>>(){});
        Order order3 = new Order();
           // Order[] orderList = inMessage.getBody(Order[].class);
            for (Order order2 : myObjects) {
            	order3.setId(order2.getId());
            	order3.setAmount(order2.getAmount());
            	 LOG.info("saveOrder called with: " + order2.getId());
                 LOG.info("saveOrder called with: " + order2.getAmount());
			}*/
           /*
          System.out.println("body...."+ myObjects.get(0));
          Order order2 = new Order();
          order2.setAmount(2233);
          */
            
         // exchange.getIn().setBody(order2,Order.class);
          //only new objects are able to marshalled..
          exchange.getIn().setBody(orderMap,HashMap.class);
            //exchange.getOut().setBody(myObjects.get(0),Order.class);
            
            
       
        }
    
/*
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }*/
}
