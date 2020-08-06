package rs.ac.uns.ftn.informatika.bibliography.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dto.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.EvaluationManagedBean;

/**
 * Represent a converter from RuleBookDTO to String and vice versa
 * 
 * @author chenejac@uns.ac.rs
 */
public class RuleBookDTOConverter implements javax.faces.convert.Converter {

	/**
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.String)
	 */
	public RuleBookDTO getAsObject(FacesContext context,
			UIComponent component, String value) throws ConverterException {
		RuleBookDTO retVal = null;
		
		ExternalContext extCtx = context.getExternalContext();
		
		EvaluationManagedBean mb = (EvaluationManagedBean) extCtx.getSessionMap().get(
		"evaluationManagedBean");
		if (mb == null) {
			mb = new EvaluationManagedBean();
			extCtx.getSessionMap().put("evaluationManagedBean", mb);
		}
		
		for (SelectItem selectItem : mb.getAllRuleBooks()) {
			if((selectItem.getValue()!=null) && (("("+((RuleBookDTO)selectItem.getValue()).getSchemeId()+") " + ((RuleBookDTO)selectItem.getValue()).getClassId()).equals(value))){
				retVal = (RuleBookDTO)selectItem.getValue();
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
			return "("+((RuleBookDTO)value).getSchemeId()+") " + ((RuleBookDTO)value).getClassId();
		else
			return null;
	}

	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(RuleBookDTOConverter.class
			.getName());

}