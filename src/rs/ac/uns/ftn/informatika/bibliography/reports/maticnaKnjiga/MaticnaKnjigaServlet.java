package rs.ac.uns.ftn.informatika.bibliography.reports.maticnaKnjiga;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.RegisterEntryDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RegisterEntryDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.RegisterEntryDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;
import rs.ac.uns.ftn.informatika.bibliography.utils.LatCyrUtils;


/**
 * Servlet implementation class MaticnaKnjigaServlet
 */

// poziv MaticnaKnjigaServlet?type=generateData&startDate=20150101&endDate=20160101&startAcademicYearNumber=2014&startId=4064&regenerate=true

public class MaticnaKnjigaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MaticnaKnjigaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		if(type.equals("generateData")){
			generateData(request);		
		}else if(type.equals("generateReport")){		
			String fakultet = request.getParameter("fakultet");		
			ServletContext context = this.getServletContext();
			String reportsDirectory = context.getRealPath("/") + "/WEB-INF/classes/rs/ac/uns/ftn/informatika/bibliography/reports/jasper/";				
			if(request.getParameter("singleElement")!=null && request.getParameter("singleElement").equals("yes")){
				String jasperFile = reportsDirectory + "maticna_knjiga_disertacija_jedan" + ".jasper";	
				generateReport(jasperFile, reportsDirectory, response, getMaticnaKnjigaForSingleElement(request));
			}else if(request.getParameter("forPromotion")!=null && request.getParameter("forPromotion").equals("yes")){				
				String jasperFile = reportsDirectory + "maticna_knjiga_disertacija" + ".jasper";				
				List<MaticnaKnjigaItemBean> listBeans = getMaticnaKnjigaForControlNumbers(request);				
				generateReport(jasperFile, reportsDirectory, response, listBeans);				
			}else{		
				String jasperFile = reportsDirectory + "maticna_knjiga_disertacija" + ".jasper";	
				generateReport(jasperFile, reportsDirectory, response, getMaticnaKnjiga(request, fakultet));
			}
			}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	
	
	private void generateData(HttpServletRequest request){
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {	
		Date startDateDate =  sdf.parse(startDate);
		Date endDateDate =  sdf.parse(endDate);
		int registerEntryId = Integer.parseInt(request.getParameter("startId"));
		int academicYearNumber= Integer.parseInt(request.getParameter("startAcademicYearNumber"));
		String universityId = request.getParameter("universityId");
		if(universityId ==null)
			universityId = "UNS";
		boolean regenarate = ((request.getParameter("regenerate")!=null) && (request.getParameter("regenerate").equals("true")))?true:false;
		List<RegisterEntryDTO> entries = new RegisterEntryDAO(new RegisterEntryDB()).getPromotedInPeriod(startDateDate, endDateDate, universityId, "%");	
		System.out.println("entries.size="+entries.size());

		List<RegisterEntryDTO> forGeneratingData = MaticnaKnjigaUtils.generateData(entries, regenarate, registerEntryId, academicYearNumber);
		
		
		for(int i=0;i<forGeneratingData.size();i++){
			RegisterEntryDTO entry = forGeneratingData.get(i);
			
			
			System.out.println("Updating register entry, id="+entry.getId()+", academicYearNumber="+entry.getAcademicYear());
			new RegisterEntryDAO(new RegisterEntryDB()).update(entry);
			System.out.println("Updated");
		
		}	
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	private List<MaticnaKnjigaItemBean> getMaticnaKnjiga(HttpServletRequest request, String fakultet){
		
		
		String startDate = request.getParameter("startDate");	
		String endDate = request.getParameter("endDate");
		String universityID = request.getParameter("universityId");
		if(universityID ==null)
			universityID = "UNS";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		List<MaticnaKnjigaItemBean> retVal = new ArrayList<MaticnaKnjigaItemBean>();		
		
		try {	
		List<RegisterEntryDTO> entries;
		if(startDate!=null && endDate!=null){
			entries = new RegisterEntryDAO(new RegisterEntryDB()).getPromotedInPeriod(sdf.parse(startDate),sdf.parse(endDate), universityID, "%");
			System.out.println("entries.size() "+entries.size());
		}else		
			entries = new RegisterEntryDAO(new RegisterEntryDB()).getAllFromDatabase();			
	    String noPromotionDate = request.getParameter("noPromotionDate");	 
	    boolean isNoPromotionDate = noPromotionDate!=null && noPromotionDate.equals("yes");	 
		for(RegisterEntryDTO entry:entries){					
				if(entry!=null){		
					if(isNoPromotionDate){
						if (entry.getPromotionDate()==null){
						retVal.add(new MaticnaKnjigaItemBean(entry));						
						}
					}else	if(startDate!=null && startDate.equals("noNumber")){
						retVal.add(new MaticnaKnjigaItemBean(entry));
					}else if(entry.getPromotionDate()==null ||							
							((! (startDate!=null && entry.getPromotionDate().getTime().before(sdf.parse(startDate)))) && 
									(! (endDate!=null && entry.getPromotionDate().getTime().after(sdf.parse(endDate)))))){
						if(entry.getPromotionDate()!=null){  // && entry.getId()!=null && !entry.getId().equals("")){						
							retVal.add(new MaticnaKnjigaItemBean(entry));							
						}
					}		
				}			
		}		
		} catch (Exception e) {				
			e.printStackTrace();
		}			
	
		List<String> sortDirections = new ArrayList<String>();
		sortDirections.add("asc");
		sortDirections.add("asc");		
		
		List<String> sortAttributes = new ArrayList<String>();
		
		sortAttributes.add("skolskaGodina");
		sortAttributes.add("redniBroj");
		Collections.sort(retVal, new GenericComparator<MaticnaKnjigaItemBean>(
				sortAttributes, sortDirections));
		
		/*
		System.out.println("Provera rupa u maticnim brojevima");
		
		for(int i=0; i<retVal.size();i++){
			if(i>0){
				if(Integer.parseInt(retVal.get(i).getRedniBroj())-Integer.parseInt(retVal.get(i-1).getRedniBroj())!=1){
					System.out.println("Rupa kod broja "+retVal.get(i).getRedniBroj());
				}
			}			
		}
		
		*/
		if(fakultet==null) {
			return retVal;
		}	else {
			List<MaticnaKnjigaItemBean> retValFak = new ArrayList<MaticnaKnjigaItemBean>();
			for(MaticnaKnjigaItemBean bean:retVal){
				String orgJedOdbrane = LatCyrUtils.toLatinUnaccented(bean.getOrganizacionaJedinicaOdbrane()).toLowerCase();
				if(orgJedOdbrane.contains(fakultet)){
					retValFak.add(bean);
				}
			}
			return retValFak;
		}
	}
	
	private void generateReport(String jasperFile,String reportsDir,HttpServletResponse response,List<MaticnaKnjigaItemBean> data){
		try {
			if(data!=null) {
				
				HashMap<String, Object> params = new HashMap<String, Object>();				
				JasperPrint jasperPrint = null;
				if(data.size()!=0) {
					JRDataSource dataSource = new JRBeanCollectionDataSource(data);
					jasperPrint = JasperFillManager.fillReport(jasperFile,params, dataSource);
				}else {
					JREmptyDataSource dataSource = new JREmptyDataSource();
					jasperPrint = JasperFillManager.fillReport(jasperFile,params, dataSource);
			
				}
				ServletOutputStream servletOutputStream = response.getOutputStream();
				JRPdfExporter pdfExporter = new JRPdfExporter();
				JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);							
				pdfExporter.exportReport();				
				response.setContentType("application/pdf");
				
				servletOutputStream.flush();
				servletOutputStream.close();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}		
	}
	
	private List<MaticnaKnjigaItemBean> getMaticnaKnjigaForSingleElement(HttpServletRequest request){
		String dissertationControlNumber= request.getParameter("controlNumber");
	 StudyFinalDocumentDTO dissertation = (StudyFinalDocumentDTO)	new RecordDAO(new RecordDB()).getDTO(dissertationControlNumber);
	 RegisterEntryDTO reg = new RegisterEntryDAO(new RegisterEntryDB()).getRegisterEntryFromDatabase(dissertation, false);
	 List<MaticnaKnjigaItemBean> retVal = new ArrayList<MaticnaKnjigaItemBean>();
	 retVal.add(new MaticnaKnjigaItemBean(reg));
	 return retVal;
		
	}
	
	private List<MaticnaKnjigaItemBean> getMaticnaKnjigaForControlNumbers(HttpServletRequest request){
	 List<String> controlNumbers = (List<String>)request.getAttribute("controlNumbersForPromotion");
	 List<MaticnaKnjigaItemBean> retVal = new ArrayList<MaticnaKnjigaItemBean>();
	 RegisterEntryDAO regEntryDAO = new RegisterEntryDAO(new RegisterEntryDB());
	 for(String dissertationControlNumber: controlNumbers){	 
		 StudyFinalDocumentDTO dissertation = (StudyFinalDocumentDTO)new RecordDAO(new RecordDB()).getDTO(dissertationControlNumber);
		 RegisterEntryDTO reg = regEntryDAO.getRegisterEntryFromDatabase(dissertation, false);		
		 retVal.add(new MaticnaKnjigaItemBean(reg));
	 }	 
	 return retVal;
		
	}
	


}
