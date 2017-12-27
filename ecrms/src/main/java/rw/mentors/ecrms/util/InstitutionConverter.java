/**
 * 
 */
package rw.mentors.ecrms.util;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import rw.mentors.ecrms.domain.Institution;
import rw.mentors.ecrms.usecase.CreateMeeting;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 27, 2017
 */
@FacesConverter(value = "institutionConverter")
public class InstitutionConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent uiComponent, String institutionId) {
		ValueExpression vex = ctx.getApplication().getExpressionFactory().createValueExpression(ctx.getELContext(),
				"#{createMeeting}", CreateMeeting.class);
		CreateMeeting createMeeting = (CreateMeeting) vex.getValue(ctx.getELContext());
		return createMeeting.getInstitution(institutionId);
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object institution) {
		return ((Institution) institution).getId().toString();
	}

}
