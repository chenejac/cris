package rs.ac.uns.ftn.informatika.bibliography.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.PaperProceedingsManagedBean;

/**
 * Represent a converter from ProceedingsDTO to String and vice versa
 * 
 * @author chenejac@uns.ac.rs
 */
public class ProceedingsDTOConverter implements javax.faces.convert.Converter {

	/**
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.String)
	 */
	public ProceedingsDTO getAsObject(FacesContext context,
			UIComponent component, String value) throws ConverterException {
		ProceedingsDTO retVal = null;
		
		ExternalContext extCtx = context.getExternalContext();
		
		PaperProceedingsManagedBean mb = (PaperProceedingsManagedBean) extCtx.getSessionMap().get(
		"paperProceedingsManagedBean");
		if (mb == null) {
			mb = new PaperProceedingsManagedBean();
			extCtx.getSessionMap().put("institutionManagedBean", mb);
		}
		
		for (SelectItem selectItem : mb.getAllProceedingsSelectItems()) {
			if((selectItem.getValue()!=null) && (((ProceedingsDTO)selectItem.getValue()).getControlNumber().equals(value))){
				retVal = (ProceedingsDTO)selectItem.getValue();
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
			return ((ProceedingsDTO)value).getControlNumber();
		else
			return null;
	}

	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(ProceedingsDTOConverter.class
			.getName());
}
