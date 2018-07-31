package  com.camelrouting;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class JavaDSLRoute {
	
	public static void main(String[] args) throws Exception {
		CamelContext context = new DefaultCamelContext();
		
		try {
			/*context.addComponent("activemq", ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"));*/
			
			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					from("direct:JavaDSLRouteStart").
				
					//To printout the filter routing message to the external file 
						//split(xpath("//order[@product='electronics']/items")).
						//to("file:src/main/resources/orderxmlroute/");
					
					// To printout the routing message on the IDE console  
						to("stream:out");
					
					
					//To send the filter routing message to the Messaging Queue {ERRRO}
					/*choice()
	                .when(header("foo").isEqualTo("bar"))
	                    .to("activemq:queue:Q1")
	                .when(header("foo").isEqualTo("cheese"))
	                    .to("activemq:queue:Q3")
	                .otherwise()
	                    .to("activemq:queue:Q3");*/
				}
				
			});
			
			
			
			context.start();
			ProducerTemplate template = context.createProducerTemplate();
			InputStream orderxml = new FileInputStream("src/main/resources/order.xml");
			template.sendBody("direct:JavaDSLRouteStart", orderxml);
		} finally {
			context.stop();
		}
	}
}
