package cc.notsoclever.examples.cxf.wsdlfirst.server;

import java.util.List;


public interface ApiGateway {
	
	
    Order saveOrder(Order input);
    List<Order> findAllOrders(String order);

}
