package rs.ac.uns.ftn.informatika.bibliography.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.InstitutionManagedBean;

/**
 * Represent a converter from InstitutionDTO to String and vice versa
 * 
 * @author chenejac@uns.ac.rs
 */
public class InstitutionDTOConverter implements javax.faces.convert.Converter {

	/**
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.String)
	 */
	public InstitutionDTO getAsObject(FacesContext context,
			UIComponent component, String value) throws ConverterException {
		InstitutionDTO retVal = null;
		
		ExternalContext extCtx = context.getExternalContext();
		
		InstitutionManagedBean mb = (InstitutionManagedBean) extCtx.getSessionMap().get(
		"institutionManagedBean");
		if (mb == null) {
			mb = new InstitutionManagedBean();
			extCtx.getSessionMap().put("institutionManagedBean", mb);
		}
		
		for (SelectItem selectItem : mb.getAllList()) {
			if((selectItem.getValue()!=null) && (((InstitutionDTO)selectItem.getValue()).getControlNumber().equals(value))){
				retVal = (InstitutionDTO)selectItem.getValue();
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
			return ((InstitutionDTO)value).getControlNumber();
		else
			return null;
	}

	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(InstitutionDTOConverter.class
			.getName());

}