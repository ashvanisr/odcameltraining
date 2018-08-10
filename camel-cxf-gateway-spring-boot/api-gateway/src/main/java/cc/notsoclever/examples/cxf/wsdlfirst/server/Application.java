package cc.notsoclever.examples.cxf.wsdlfirst.server;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A spring-boot application that includes a Camel route builder to setup the Camel routes
 */
@SpringBootApplication
public class Application extends RouteBuilder {

    // must have a main method spring-boot can run
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    private static final transient Logger LOG = LoggerFactory.getLogger(Application.class);

    @Override
    public void configure() throws Exception {
    	
    	 //restConfiguration().host("localhost").port(8091);
         
    	 restConfiguration().producerComponent("http4");
    	 JacksonDataFormat order= new JacksonDataFormat(Order.class);
    	 JacksonDataFormat orderList= new JacksonDataFormat(Order.class);
    	 orderList.useList();
    	 
    	
        from("cxf:bean:orderServiceEndpoint")
    	//from("cxf:/CustomerServicePort?serviceClass=" + CustomerService.class.getName())
       //.to("OrderServiceProcessor");
        .choice().when(header(CxfConstants.OPERATION_NAME).isEqualTo("findAllOrders"))
        
        .setHeader(Exchange.HTTP_METHOD, constant("GET")).
        setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
        //setHeader(Exchange.HTTP_QUERY, constant("Username=ABC&Password=ABC&Country=UK"))
        .to("http4://localhost:8091/camel-rest-jpa/books/?bridgeEndpoint=true")
        .to("direct:get")
        	//.to("rest:get:http://localhost:8091/camel-rest-jpa/books/")
        
        .when(header(CxfConstants.OPERATION_NAME).isEqualTo("saveOrder"))
            .setHeader(Exchange.HTTP_METHOD, constant("POST")).
            setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
            //.to("OrderServiceProcessor")
            .marshal().json(JsonLibrary.Jackson,Order.class)
            .to("log:saveOrder?level=INFO&showAll=true")       
            .to("http4://localhost:8091/camel-rest-jpa/books/order?bridgeEndpoint=true")
            .to("direct:post")
        	//.to("rest:post:http://localhost:8091/camel-rest-jpa/books/order");
        
        .when(header(CxfConstants.OPERATION_NAME).isEqualTo("putOrder"))
        .setHeader(Exchange.HTTP_METHOD, constant("PUT")).
        setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
        //.to("OrderServiceProcessor")
        .marshal().json(JsonLibrary.Jackson,Order.class)
        .convertBodyTo(String.class)
        .to("log:putOrder?level=INFO&showAll=true")       
        .to("http4://localhost:8093/camel-rest-jms/books/order?bridgeEndpoint=true")
        .to("direct:put");
        
        
        from("direct:get")
        //.convertBodyTo(String.class)
        .to("log:findAllBooks****${body}")
        .to("OrderServiceProcessor");
       // .unmarshal().json(JsonLibrary.Jackson,Order[].class);
      //  .unmarshal(orderList);
        
        
        from("direct:post")
        .to("log:saveBooks****${body}")
        .unmarshal().json(JsonLibrary.Jackson, Order.class);
        
        from("direct:put")
        .to("log:putOrder?level=INFO&showAll=true")
        .convertBodyTo(String.class);
        
        
    }
}
