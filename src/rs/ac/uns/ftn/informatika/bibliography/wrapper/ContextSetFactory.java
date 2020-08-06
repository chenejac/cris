package rs.ac.uns.ftn.informatika.bibliography.wrapper;

import java.io.File;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import rs.ac.uns.ftn.informatika.bibliography.contextset.ContextSet;
import rs.ac.uns.ftn.informatika.bibliography.contextset.util.MyValidationEventHandler;
import rs.ac.uns.ftn.informatika.bibliography.mediator.MediatorService;

public class ContextSetFactory {
	
	public static ContextSet getContextSet(String setName){
		try {
			String pathToXSDFile = null;
	    	String pathToXMLFile = null;
	    	
	    	ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.wrapper.contextSet");
	    	String contexSet = MediatorService.contextSetPath;
	    	
	    	pathToXSDFile = contexSet + rb.getString("contextSet");
	    	pathToXMLFile = contexSet + rb.getString("cql");

			if (setName.equalsIgnoreCase("dc")){
				pathToXMLFile = contexSet + rb.getString("dc");
			}
			else if(setName.equalsIgnoreCase("cris"))
			{
				pathToXMLFile = contexSet + rb.getString("cris");
			}
			
			JAXBContext context = JAXBContext.newInstance("rs.ac.uns.ftn.informatika.bibliography.contextset");
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			//postavljanje validacije
			//W3C sema
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			//lokacija seme
			Schema schema = schemaFactory.newSchema(new File(pathToXSDFile));
			
            //setuje se sema
			unmarshaller.setSchema(schema);
			//EventHandler, koji obradjuje greske, ako se dese prilikom validacije
            unmarshaller.setEventHandler(new MyValidationEventHandler());
			
            //ucitava se objektni model, a da se pri tome radi i validacija
            ContextSet contextSet = (ContextSet) unmarshaller.unmarshal(new File(pathToXMLFile));
			return contextSet;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

}
