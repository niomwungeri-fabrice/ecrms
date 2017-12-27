/**
 * 
 */
package rw.mentors.ecrms.util;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import rw.mentors.ecrms.domain.Employee;
import rw.mentors.ecrms.usecase.CreateMeeting;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 27, 2017
 */
@FacesConverter(value = "employeeConverter")
public class EmployeeConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent uiComponent, String employeeId) {
		ValueExpression vex = ctx.getApplication().getExpressionFactory().createValueExpression(ctx.getELContext(),
				"#{createMeeting}", CreateMeeting.class);

		CreateMeeting createMeeting = (CreateMeeting) vex.getValue(ctx.getELContext());
		return createMeeting.getEmployee(employeeId);
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object employee) {
		return ((Employee) employee).getId().toString();
	}

}
