/**
 * 
 */
package rw.mentors.ecrms.testingPurpose;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.stereotype.Component;

@Component
@ManagedBean
public class TestBean {

	private List<String> list;
	private RepeatPaginator paginator;

	@PostConstruct
	public void init() {
		this.list = new ArrayList<String>();
		this.list.add("Item 1");
		this.list.add("Item 2");
		this.list.add("Item 3");
		this.list.add("Item 4");
		this.list.add("Item 5");
		this.list.add("Item 6");
		this.list.add("Item 7");
		this.list.add("Item 8");
		this.list.add("Item 9");
		this.list.add("Item 10");
		this.list.add("Item 11");
		paginator = new RepeatPaginator(this.list);
	}

	public RepeatPaginator getPaginator() {
		return paginator;
	}

}