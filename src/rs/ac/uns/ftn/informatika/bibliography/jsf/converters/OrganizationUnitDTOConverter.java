package rs.ac.uns.ftn.informatika.bibliography.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.OrganizationUnitManagedBean;

/**
 * Represent a converter from OrganizationUnitDTO to String and vice versa
 * 
 * @author chenejac@uns.ac.rs
 */
public class OrganizationUnitDTOConverter implements javax.faces.convert.Converter {

	/**
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.String)
	 */
	public OrganizationUnitDTO getAsObject(FacesContext context,
			UIComponent component, String value) throws ConverterException {
		OrganizationUnitDTO retVal = null;
		
		ExternalContext extCtx = context.getExternalContext();
		
		OrganizationUnitManagedBean mb = (OrganizationUnitManagedBean) extCtx.getSessionMap().get(
			"organizationUnitManagedBean");
		if (mb == null) {
			mb = new OrganizationUnitManagedBean();
			extCtx.getSessionMap().put("organizationUnitManagedBean", mb);
		}
		
		for (SelectItem selectItem : mb.getAllList()) {
			if((selectItem.getValue()!=null) && (((OrganizationUnitDTO)selectItem.getValue()).getControlNumber().equals(value))){
				retVal = (OrganizationUnitDTO)selectItem.getValue();
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
			return ((OrganizationUnitDTO)value).getControlNumber();
		else
			return null;
	}

	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(OrganizationUnitDTOConverter.class
			.getName());

}