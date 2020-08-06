package rs.ac.uns.ftn.informatika.bibliography.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dto.PositionDTO;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.PositionManagedBean;

/**
 * Represent a converter from PositionDTO to String and vice versa
 * 
 * @author chenejac@uns.ac.rs
 */
public class PositionDTOConverter implements javax.faces.convert.Converter {

	/**
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.String)
	 */
	public PositionDTO getAsObject(FacesContext context,
			UIComponent component, String value) throws ConverterException {
		PositionDTO retVal = null;
		
		ExternalContext extCtx = context.getExternalContext();
		
		PositionManagedBean mb = (PositionManagedBean) extCtx.getSessionMap().get(
							"positionManagedBean");
		if (mb == null) {
			mb = new PositionManagedBean();
			extCtx.getSessionMap().put("positionManagedBean", mb);
		}
		
		for (SelectItem selectItem : mb.getAllList()) {
			if((selectItem.getValue()!=null) && (((PositionDTO)selectItem.getValue()).getClassId().equals(value))){
				retVal = (PositionDTO)selectItem.getValue();
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
		if(value != null){
			return ((PositionDTO)value).getClassId();
		}
		else{
			return null;
		}
	}

	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(PositionDTOConverter.class
			.getName());

}