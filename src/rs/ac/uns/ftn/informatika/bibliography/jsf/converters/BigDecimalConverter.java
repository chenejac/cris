package rs.ac.uns.ftn.informatika.bibliography.jsf.converters;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Represent a converter from BigDecimal to String and vice versa
 * 
 * @author chenejac@uns.ac.rs
 */
public class BigDecimalConverter implements javax.faces.convert.Converter {

	/**
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.String)
	 */
	public BigDecimal getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException {
		if (value == null || value.length() == 0) {
			return null;
		} else {
			try {
				def.setParseBigDecimal(true);
				return (BigDecimal) def.parse(value);
			} catch (ParseException ex) {
				log.warn("getAsObject", ex);
			}
		}
		return null;

	}

	/**
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.Object)
	 */
	public String getAsString(FacesContext context, UIComponent component,
			Object value) throws ConverterException {

		BigDecimal bd = (BigDecimal) value;
		if (bd != null)
			return def.format(bd);
		else
			return "";
	}

	private static Log log = LogFactory.getLog(BigDecimalConverter.class
			.getName());

	private DecimalFormat def = new DecimalFormat("###.##");

}