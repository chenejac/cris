package rs.ac.uns.ftn.informatika.bibliography.jsf.converters;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Represent a converter from java.util.DateTime to String and vice versa
 * 
 * @author chenejac@uns.ac.rs
 */
public class DateTimeConverter implements javax.faces.convert.Converter {

	/**
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.String)
	 */
	public Date getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException {
		Date d = null;
		if (value == null || value.length() == 0) {
			return null;
		} else {
			try {
				d = sdf.parse(value);
			} catch (Exception ex) {
				log.warn("getAsObject", ex);
			}
		}
		return d;

	}

	/**
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.Object)
	 */
	public String getAsString(FacesContext context, UIComponent component,
			Object value) throws ConverterException {

		Date d = (Date) value;
		if (d != null)
			return sdf.format(d);
		else
			return "";
	}

	private static Log log = LogFactory.getLog(DateTimeConverter.class
			.getName());

	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
}