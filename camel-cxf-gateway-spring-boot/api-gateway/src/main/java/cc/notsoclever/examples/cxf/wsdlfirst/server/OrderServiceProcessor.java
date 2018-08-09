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
package cc.notsoclever.examples.cxf.wsdlfirst.server;


import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class OrderServiceProcessor implements Processor {
    private static final transient Logger LOG = LoggerFactory.getLogger(OrderServiceProcessor.class);

   // @Autowired
   // CustomerService customerService;

    @Override
    public void process(Exchange exchange) throws Exception {
        Message inMessage = exchange.getIn();
        String operationName = inMessage.getHeader(CxfConstants.OPERATION_NAME, String.class);
        
        
        
        if ("saveOrder".equals(operationName)) {
            Order order = inMessage.getBody(Order.class);
            LOG.info("saveOrder called with: " + order.getId());
            LOG.info("saveOrder called with: " + order.getAmount());
            exchange.getIn().setBody(order);
            
        //    exchange.getOut().setBody(new Object[] {customers});
        } else if ("findAllOrders".equals(operationName)) {
        	
        	String inRestMessage = inMessage.getBody(String.class);
            LOG.info("Process called with: findAllOrders " + inRestMessage);
            
            
            LOG.info("findAllOrders called");
            
            ObjectMapper mapper = new ObjectMapper();
            List<Order> myObjects = mapper.readValue(inRestMessage, new TypeReference<List<Order>>(){});
            
           // Order[] myObjects = mapper.readValue(inRestMessage, Order[].class);
            
            for (Order order : myObjects) {
            	LOG.info("Order in loop called" + order.getId());
            	LOG.info("Order in loop called" + order.getAmount());
			}
            
            //inMessage.setBody(myObjects);
            exchange.getOut().setBody(new Object[] {myObjects});
            
        	
        	//Order order = new Order();
        	//order.set
            //Customer customer = inMessage.getBody(Customer.class);
            //customer = customerService.updateCustomer(customer);
            //exchange.getOut().setBody(customer);
        }
    }
/*
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }*/
}
