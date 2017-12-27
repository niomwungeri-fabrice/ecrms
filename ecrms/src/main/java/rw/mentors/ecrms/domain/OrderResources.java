package rw.mentors.ecrms.domain;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class OrderResources extends ResourceSupport{
	 	
	@JsonUnwrapped
	    private final Order order;
	    public OrderResources(Order order) {
	        this.order = order;
	    }
}
