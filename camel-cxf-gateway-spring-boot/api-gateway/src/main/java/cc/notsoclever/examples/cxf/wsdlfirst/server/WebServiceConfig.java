/**
 *  Copyright 2005-2016 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package cc.notsoclever.examples.cxf.wsdlfirst.server;

import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.cxf.Bus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebServiceConfig {

    @Autowired
    private Bus bus;
    
    
    //NOTE THE VALUE OF cxf.path in application.properties this leads to 
    //the URL of the soap service being of the form /service/CustomerServicePort
    
    //http://localhost:8092/service/apigateway?wsdl
    
    @Bean(name="OrderServiceProcessor")
    public OrderServiceProcessor getProcessor(){
    	return new OrderServiceProcessor();
    }

    @Bean
    public CxfEndpoint orderServiceEndpoint() {
    	
    	CxfEndpoint cxfEndpoint = new CxfEndpoint();
    	cxfEndpoint.setAddress("/apigateway");
    //	cxfEndpoint.setServiceNameString("s:customer:apiGatewayService");
    	cxfEndpoint.setServiceClass(ApiGateway.class);
    //	cxfEndpoint.setDefaultOperationNamespace("http://apache.org/hello_world_soap_http");
    	//cxfEndpoint.setBus(bus);
        return cxfEndpoint;
    }
    
    /*
    @Bean
    public CustomerService customerService()
    {
    	return new CustomerServiceImpl();
    }*/
}
