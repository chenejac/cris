package rs.ac.uns.ftn.informatika.bibliography.reports.unsdesertacije;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.QueryDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

/**
 * Servlet implementation class UnsDisertacijeServlet
 */
public class UnsDisertacijeServlet extends HttpServlet {
	
	HashMap<String, String> fakulteti = new HashMap<String, String>();
	HashMap<String, String> fakultetiSk = new HashMap<String, String>();
	
	List<UnsDisertacijeZbirnoBean> zbirno = new ArrayList<UnsDisertacijeZbirnoBean>();
	
	
	private static final long serialVersionUID = 1L;
	
	
	@Override
	public void init() throws ServletException {		
		super.init();
		fakulteti.put("(BISIS)5928", "Факултет техничких наука у Новом Саду");
		fakulteti.put("(BISIS)5929", "Природно-математички факултет у Новом Саду");		
		fakulteti.put("(BISIS)5930", "Филозофски факултет у Новом Саду");
		fakulteti.put("(BISIS)5931", "Пољопривредни факултет у Новом Саду");
		fakulteti.put("(BISIS)5932", "Правни факултет у Новом Саду");
		fakulteti.put("(BISIS)5933", "Технолошки факултет у Новом Саду");
		fakulteti.put("(BISIS)5934", "Економски факултет у Суботици");
		fakulteti.put("(BISIS)5935", "Медицински факултет у Новом Саду");
		fakulteti.put("(BISIS)5936", "Академија уметности у Новом Саду");
		fakulteti.put("(BISIS)5937", "Грађевински факултет у Суботици");
		fakulteti.put("(BISIS)5938", "Технички факултет Михајло Пупин у Зрењанину");
		fakulteti.put("(BISIS)5939", "Факултет спорта и физичког васпитања у Новом Саду");
		fakulteti.put("(BISIS)5940", "Педагошки факултет у Сомбору");
		fakulteti.put("(BISIS)5941", "Учитељски факултет на мађарском наставном језику у Суботици");
		fakulteti.put("(BISIS)8011", "Асоцијација центра за интердисциплинарне и мултидисциплинарне студије и истраживања");
		
		fakultetiSk.put("(BISIS)5928", "ftn");
		fakultetiSk.put("(BISIS)5929", "pmf");		
		fakultetiSk.put("(BISIS)5930", "fil");
		fakultetiSk.put("(BISIS)5931", "polj");
		fakultetiSk.put("(BISIS)5932", "pravni");
		fakultetiSk.put("(BISIS)5933", "tehn");
		fakultetiSk.put("(BISIS)5934", "ek");
		fakultetiSk.put("(BISIS)5935", "med");
		fakultetiSk.put("(BISIS)5936", "akademija");
		fakultetiSk.put("(BISIS)5937", "gradj");
		fakultetiSk.put("(BISIS)5938", "tehnZrenjanin");
		fakultetiSk.put("(BISIS)5939", "dif");
		fakultetiSk.put("(BISIS)5940", "pedagoski");
		fakultetiSk.put("(BISIS)5941", "uciteljski madj");
		fakultetiSk.put("(BISIS)8011", "acimsi");
		
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ServletContext context = this.getServletContext();
		String reportsDirectory = context.getRealPath("/") + "/WEB-INF/classes/rs/ac/uns/ftn/informatika/bibliography/reports/jasper/";
		String jasperFile = reportsDirectory + "unsDisertacije" + ".jasper";		
		
	
		for(String id:fakulteti.keySet())
			generateReport(jasperFile, reportsDirectory, response, getUnsDisertacije(id), id);
		
		List<String> sortDirections = new ArrayList<String>();
		sortDirections.add("asc");
	
		List<String> sortAttributes = new ArrayList<String>();
		sortAttributes.add("nazivFakulteta");
		
	
		
		Collections.sort(zbirno, new GenericComparator<UnsDisertacijeZbirnoBean>(
				sortAttributes, sortDirections));
		generateReportZbirno(reportsDirectory + "UnsDisertacijeZbirno" + ".jasper", reportsDirectory, response, zbirno);
	}

	

	private List<UnsDisertacijeBean> getUnsDisertacije(String id) {
		List<StudyFinalDocumentDTO> records;
		List<UnsDisertacijeBean> disertacije;
	 String luceneQuery = "+PY:[2006 TO 2012]";  QueryParser queryParser = new QueryParser("FT", new CrisAnalyzer());
	        Query query = null;
	        try {
	            query = queryParser.parse(luceneQuery);
	        } catch (Exception e) {
	           e.printStackTrace();
	        }
	        if(query!=null){
	        	BooleanQuery type = new BooleanQuery();
				type.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
				type.add(new TermQuery(new Term("TYPE", Types.PHD_ART_PROJECT)), Occur.SHOULD);
	        	BooleanQuery bq = QueryUtils.makeBooleanQuery(query, type, QueryDTO.AND);
	        	bq = QueryUtils.makeBooleanQuery(bq, new TermQuery(new Term("INCN", id)), QueryDTO.AND); // ovo je PMF, mogu pronaci i za ostale institucije ID-eve, ali za sada probaj za ovo
	            RecordDAO recordDAO = new RecordDAO(new RecordDB());
	            List<Record> recordsTemp = recordDAO.getDTOs(bq, new AllDocCollector(true));
	            records = new ArrayList<StudyFinalDocumentDTO>();
	            disertacije = new ArrayList<UnsDisertacijeBean>();
	            System.out.println("recordsTemp.size()="+recordsTemp.size());
	            zbirno.add(new UnsDisertacijeZbirnoBean(fakulteti.get(id),recordsTemp.size()));
	            int i =0;
	            for (Record record : recordsTemp) {
	            		i++;
	            		System.out.println(i+". Processing disertacija id="+record.getControlNumber());
	              disertacije.add(new UnsDisertacijeBean((StudyFinalDocumentDTO)record.getDto()));
	              record.getDto().clear();
	              //if(i==30) break;
	            }
	           
	           
	           	List<String> sortAttributes = new ArrayList<String>();
	          		
	           	List<String> sortDirections = new ArrayList<String>();
	          		sortDirections.add("asc");
	          		sortDirections.add("asc");
	           	
	          		sortAttributes.add("godinaOdbrane");
	          		sortAttributes.add("imeIPrezimeAutora");
	          		
	          		Collections.sort(disertacije, new GenericComparator<UnsDisertacijeBean>(
	          				sortAttributes, sortDirections));
	          		
	            return disertacije;
	        }  

  
		return null;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void generateReport(String jasperFile,String reportsDir,HttpServletResponse response,List<UnsDisertacijeBean> data, String id){
		try {
			if(data!=null) {
				
				HashMap params = new HashMap<String, String>();		
				params.put("fakultet", fakulteti.get(id));
				
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
				pdfExporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");				
				pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				File f = new File("disertacije-"+fakultetiSk.get(id)+".pdf");
				pdfExporter.setParameter(JRExporterParameter.OUTPUT_FILE,f);
				
				
				pdfExporter.exportReport();
				
				response.setContentType("application/pdf");
				
				servletOutputStream.flush();
				servletOutputStream.close();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}		
	}
	
	private void generateReportZbirno(String jasperFile,String reportsDir,HttpServletResponse response,List<UnsDisertacijeZbirnoBean> data ){
		try {
			
	
			if(data!=null) {
				
				
				HashMap params = new HashMap<String, String>();		
				
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
				pdfExporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");				
				pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				File f = new File("disertacijeZbirno"+".pdf");
				pdfExporter.setParameter(JRExporterParameter.OUTPUT_FILE,f);
				
				
				pdfExporter.exportReport();
				
				response.setContentType("application/pdf");
				
				servletOutputStream.flush();
				servletOutputStream.close();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}		
	}

}
