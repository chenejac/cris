package rs.ac.uns.ftn.informatika.bibliography.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dto.CommissionDTO;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.EvaluationManagedBean;

/**
 * Represent a converter from CommissionDTO to String and vice versa
 * 
 * @author chenejac@uns.ac.rs
 */
public class CommissionDTOConverter implements javax.faces.convert.Converter {

	/**
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.String)
	 */
	public CommissionDTO getAsObject(FacesContext context,
			UIComponent component, String value) throws ConverterException {
		CommissionDTO retVal = null;
		
		ExternalContext extCtx = context.getExternalContext();
		
		EvaluationManagedBean mb = (EvaluationManagedBean) extCtx.getSessionMap().get(
		"evaluationManagedBean");
		if (mb == null) {
			mb = new EvaluationManagedBean();
			extCtx.getSessionMap().put("evaluationManagedBean", mb);
		}
		
		for (SelectItem selectItem : mb.getAllCommissions()) {
			if((selectItem.getValue()!=null) && (((CommissionDTO)selectItem.getValue()).getCommissionId().toString().equals(value))){
				retVal = (CommissionDTO)selectItem.getValue();
				break;
			}	
		}
		return retVal;

	}

	/**
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.Object)
	 */
	public String getAsString(FacesContext context, UIComponent component,
			Object value) throws ConverterException {
		if(value != null)
			return ((CommissionDTO)value).getCommissionId().toString();
		else
			return null;
	}

	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(CommissionDTOConverter.class
			.getName());

}