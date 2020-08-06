package rs.ac.uns.ftn.informatika.bibliography.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorNameDTO;

/**
 * Represent a converter from AuthorNameDTO to String and vice versa
 * 
 * @author chenejac@uns.ac.rs
 */
public class AuthorNameDTOConverter implements javax.faces.convert.Converter {

	/**
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.String)
	 */
	public AuthorNameDTO getAsObject(FacesContext context,
			UIComponent component, String value) throws ConverterException {
		AuthorNameDTO retVal = new AuthorNameDTO();
		if ((value != null) && (value.length() > 0)) {
			try {
				retVal = new AuthorNameDTO(value.substring(
						value.indexOf(',') + 1).trim(), value.substring(0,
						value.indexOf(',')).trim(), "");
			} catch (Exception ex) {
				log.warn("getAsObject", ex);
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
		AuthorNameDTO an = (AuthorNameDTO) value;
		if (an != null) {
			StringBuffer temp = new StringBuffer();
			if ((an.getLastname() != null) && (!"".equals(an.getLastname())))
				temp.append(an.getLastname());
			if ((an.getFirstname() != null) && (!"".equals(an.getFirstname())))
				temp.append(", " + an.getFirstname());
			return temp.toString();
		} else
			return "";
	}

	private static Log log = LogFactory.getLog(AuthorNameDTOConverter.class
			.getName());

}