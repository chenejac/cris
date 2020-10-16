package rs.ac.uns.ftn.informatika.bibliography.reports.maticnaKnjiga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import rs.ac.uns.ftn.informatika.bibliography.dao.RegisterEntryDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RegisterEntryDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.RegisterEntryDTO;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

public class MaticnaKnjigaReportUtils {
	
	private static String reportsDirectory ;
	private static ServletContext context;
	private static HttpServletResponse response;
	
	
	
	public static void generateMaticnaKnjigaPoDatumu(Date startDate, Date endDate, String universityId, String institution){	
		if(startDate!=null && endDate!=null){
			List<RegisterEntryDTO> entries = new RegisterEntryDAO(new RegisterEntryDB()).getPromotedInPeriod(startDate,endDate, universityId, institution);
			List<MaticnaKnjigaItemBean> data = new ArrayList<MaticnaKnjigaItemBean>();
			for(RegisterEntryDTO entry:entries){
				data.add(new MaticnaKnjigaItemBean(entry));
				
			}
			List<String> sortDirections = new ArrayList<String>();
			sortDirections.add("asc");
			sortDirections.add("asc");		
			
			List<String> sortAttributes = new ArrayList<String>();
			
			sortAttributes.add("skolskaGodina");
			sortAttributes.add("redniBroj");
			Collections.sort(data, new GenericComparator<MaticnaKnjigaItemBean>(
					sortAttributes, sortDirections));
			
			try {
				generateReport("maticna_knjiga_disertacija" + ".jasper", data);
			} catch (Throwable t){
				t.printStackTrace();
			}
		}
		
	}
	
	
	
	public static void generateMaticnaKnjigaZaInstituciju(String institucija){
		if(institucija!=null){
			List<RegisterEntryDTO> entries = new RegisterEntryDAO(new RegisterEntryDB()).getPromotedForInstitution(institucija);			
			List<MaticnaKnjigaItemBean> data = new ArrayList<MaticnaKnjigaItemBean>();
			for(RegisterEntryDTO entry:entries){
				data.add(new MaticnaKnjigaItemBean(entry));
				
			}
			List<String> sortDirections = new ArrayList<String>();
			sortDirections.add("asc");
			sortDirections.add("asc");		
			
			List<String> sortAttributes = new ArrayList<String>();
			
			sortAttributes.add("skolskaGodina");
			sortAttributes.add("redniBroj");
			Collections.sort(data, new GenericComparator<MaticnaKnjigaItemBean>(
					sortAttributes, sortDirections));
			generateReport("maticna_knjiga_disertacija" + ".jasper", data);
		}
	}
	
	private static void generateReport(String jasperFile,List<MaticnaKnjigaItemBean> data){
		FacesContext facesContext = FacesContext.getCurrentInstance();	
		ExternalContext externalContext = facesContext.getExternalContext();
		context = (ServletContext)externalContext.getContext(); 
		reportsDirectory = context.getRealPath("/") + "/WEB-INF/classes/rs/ac/uns/ftn/informatika/bibliography/reports/jasper/";
		response = (HttpServletResponse) externalContext.getResponse();		
		try {
			if(data!=null) {
				
				HashMap<String, Object> params = new HashMap<String, Object>();				
				JasperPrint jasperPrint = null;
				if(data.size()!=0) {
					JRDataSource dataSource = new JRBeanCollectionDataSource(data);
					jasperPrint = JasperFillManager.fillReport(reportsDirectory+jasperFile,params, dataSource);
				}else {
					JREmptyDataSource dataSource = new JREmptyDataSource();
					jasperPrint = JasperFillManager.fillReport(reportsDirectory+jasperFile,params, dataSource);
			
				}	
				
				ServletOutputStream servletOutputStream = response.getOutputStream();
				//JRPdfExporter pdfExporter = new JRPdfExporter();
				response.setContentType("application/pdf");			
				JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);	
				// pdfExporter.exportReport();
				servletOutputStream.flush();
				servletOutputStream.close();
				
				
			
			
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		finally{    
		    facesContext.responseComplete();   
			facesContext.renderResponse();
		}
	}
}
