package rs.ac.uns.ftn.informatika.bibliography.jsf.converters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.AuthorManagedBean;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.InstitutionManagedBean;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;

/**
 * Represent a converter from InstitutionDTO to String and vice versa
 * 
 * @author chenejac@uns.ac.rs
 */
public class AuthorDTOConverter implements javax.faces.convert.Converter {

	private final RecordDAO recordDAO = new RecordDAO(new RecordDB());
	/**
	 * @see javax.faces.convert.Converter#getAsObject(FacesContext,
	 *      UIComponent, String)
	 */
	public AuthorDTO getAsObject(FacesContext context,
								 UIComponent component, String value) throws ConverterException {
		AuthorDTO retVal = null;
		

		retVal = (AuthorDTO)recordDAO.getDTO(value);

		return retVal;

	}

	/**
	 * @see javax.faces.convert.Converter#getAsString(FacesContext,
	 *      UIComponent, Object)
	 */
	public String getAsString(FacesContext context, UIComponent component,
			Object value) throws ConverterException {
		if(value != null)
			return ((InstitutionDTO)value).getControlNumber();
		else
			return null;
	}

	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(AuthorDTOConverter.class
			.getName());

}