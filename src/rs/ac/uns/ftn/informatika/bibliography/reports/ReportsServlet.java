package rs.ac.uns.ftn.informatika.bibliography.reports;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.faces.FactoryFinder;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
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
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;

import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.UserDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.CommissionDB;
import rs.ac.uns.ftn.informatika.bibliography.db.EvaluationDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.CommissionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResultMeasureDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.dto.UserDTO;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.EvaluationManagedBean;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.StudyFinalDocumentManagedBean;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.UserManagedBean;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.reports.knr.KnrDTO;
import rs.ac.uns.ftn.informatika.bibliography.reports.knr.ResultDTO;
import rs.ac.uns.ftn.informatika.bibliography.reports.knr.ResultsGroupDTO;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.SamovrednovanjeUtils;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;
/**
 * Servlet implementation class for Servlet: ReportsServlet
 *
 */
@SuppressWarnings("serial")
public class ReportsServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   	
	
	/**
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ServletContext context = this.getServletContext();
		String reportType = request.getParameter("reportType");
		String reportsDirectory = context.getRealPath("/") + "/WEB-INF/classes/rs/ac/uns/ftn/informatika/bibliography/reports/jasper/";
			
		String ImgURL = reportsDirectory + "logo.jpg";
		
		if(reportType.equalsIgnoreCase("knr")) {
			String jasperFile = reportsDirectory + "knr" + ".jasper";
			List<KnrDTO> knrDTO = getKnr(request, response);
			GenerateReport(jasperFile,reportsDirectory,response,knrDTO,ImgURL,null);
		} else if(reportType.equalsIgnoreCase("knrXML")) {
//			String jasperFile = reportsDirectory + "knr" + ".jasper";
			List<KnrDTO> knrDTO = getKnr(request, response);
			GenerateXML(response,knrDTO);
		} else if(reportType.equalsIgnoreCase("resultsKnrXML")) {
//			String jasperFile = reportsDirectory + "knr" + ".jasper";
			List<Record> results = getPublications(request, response);
			String apvnt = request.getParameter("apvnt");
			GenerateResultsXML(response,results, apvnt);
		}
	}  	
	
	
	private void GenerateResultsXML(HttpServletResponse response,
			List<Record> results, String apvnt) {
		try {
			response.setContentType("text/xml; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			PrintWriter writer = response.getWriter();
			
			Connection conn = DataSourceFactory.getDataSource().getConnection();
			CommissionDB commissionDB = new CommissionDB();
			EvaluationDB evaluationDB = new EvaluationDB();
					
			List<RuleBookDTO> ruleBookList = evaluationDB.getRuleBooks(conn);
			RuleBookDTO ruleBook = null;
			for (RuleBookDTO rb : ruleBookList){
				if("serbianResearchersEvaluation".equals(rb.getClassId()))
					ruleBook = rb;
			}
			List<CommissionDTO> allCommissionList = commissionDB.getCommissions(conn);
			
			writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			writer.println("<cris_results xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://cris.uns.ac.rs/interoperability/sema-cris.xsd\">");
			if((ruleBook != null))
				writer.println("\t<ruleBook>" + ruleBook.getSomeTerm() + "</ruleBook>");
			
			if(apvnt != null)
				writer.println("\t<apvnt>" + apvnt + "</apvnt>");
			for (Record result : results) {
				ResultMeasureDTO resultMeasure = SamovrednovanjeUtils.getResultMeasure(result, ruleBook, allCommissionList, false);
				if((resultMeasure != null) && (! "M99".equals(resultMeasure.getResultType().getClassId())))
					writer.print(result.getDto().getKnrXML("\t", resultMeasure));
				result.getDto().clear();
			}
			writer.println("</cris_results>");
			writer.flush();
			writer.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}


	private void GenerateXML(HttpServletResponse response, List<KnrDTO> knrDTO) {
		try {
				response.setContentType("text/xml; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				
				PrintWriter writer = response.getWriter();
				
				
				for (KnrDTO knr : knrDTO) {
					writer.println("<knr>");
					writer.print(knr.getResearcher().getKnrXML("\t", null));
					for (ResultsGroupDTO resultsGroup : knr.getResultsGroups()) {
						writer.print(resultsGroup.getKnrXML("\t"));
					}
					writer.println("</knr>");
					
				}
				writer.flush();
				writer.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void GenerateReport(String jasperFile,String reportsDir,HttpServletResponse response,List data,String imgURL,String period) {
		try {
			if(data!=null) {
				
				HashMap params = new HashMap<String, String>();
				params.put("SUBREPORT_DIR", subreportDir);
//				params.put("period", period);
				
				JasperPrint jasperPrint = null;;
				if(data.size()!=0) {
					JRDataSource dataSource = new JRBeanCollectionDataSource(data);
					jasperPrint = JasperFillManager.fillReport(jasperFile,params, dataSource);
				}else {
					JREmptyDataSource dataSource = new JREmptyDataSource();
					jasperPrint = JasperFillManager.fillReport(jasperFile,params, dataSource);
				}
				ServletOutputStream servletOutputStream = response.getOutputStream();

				JRPdfExporter pdfExporter = new JRPdfExporter();
				
				
				
				pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				pdfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM,servletOutputStream);
				
				
				pdfExporter.exportReport();
				
				response.setContentType("application/pdf");
				
				servletOutputStream.flush();
				servletOutputStream.close();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	private List<KnrDTO> getKnr(HttpServletRequest request, HttpServletResponse response) {
		ArrayList<KnrDTO> retVal = new ArrayList<KnrDTO>();
		FacesContext facesCtx = getFacesContext(request, response);
		ExternalContext extCtx = facesCtx.getExternalContext();
		String researcherId = request.getParameter("researcherId");
		UserDTO researcher = null;
		UserDTO loggedUser = null;
		boolean loggedIn = false;
		if(researcherId!=null){
			UserDAO userDAO = new UserDAO();
			researcher = userDAO.getFakeUserByAuthorControlNumber("(BISIS)"+researcherId);
			researcher.getAuthor().getAddress();
		}
		UserManagedBean umb = (UserManagedBean) extCtx.getSessionMap().get(
				"userManagedBean");
		if (umb == null) {
			umb = new UserManagedBean();
			extCtx.getSessionMap().put("userManagedBean", umb);
		}
		if(researcher!=null){
			loggedUser = umb.getLoggedUser();
			loggedIn = umb.isLoggedIn();
			umb.setLoggedUser(researcher);
			umb.setLoggedIn(true);
		}
		StudyFinalDocumentManagedBean sfdmb = (StudyFinalDocumentManagedBean) extCtx.getSessionMap().get(
			"studyFinalDocumentManagedBean");
		if (sfdmb == null) {
			sfdmb = new StudyFinalDocumentManagedBean();
			extCtx.getSessionMap().put("studyFinalDocumentManagedBean", sfdmb);
		}
		EvaluationManagedBean emb = (EvaluationManagedBean) extCtx.getSessionMap().get(
			"evaluationManagedBean");
		if (emb == null) {
			emb = new EvaluationManagedBean();
			emb.enterPage();
			extCtx.getSessionMap().put("evaluationManagedBean", emb);
		}
		if(researcher!=null){
			emb.setCurrentAuthor(researcher.getAuthor());
			emb.loadRuleBook();
			emb.loadCommission();
			emb.populateMap();
		}
		HashMap<ResultMeasureDTO, List<PublicationDTO>> allResults = emb.getEvaluatedResults();
		List<ResultsGroupDTO> resultsGroupDTOlist = new ArrayList<ResultsGroupDTO>();
		for (ResultMeasureDTO resultMeasureDTO : allResults.keySet()) {
			if(resultMeasureDTO.getResultType().getClassId().equals("M99"))
				continue;
			List<PublicationDTO> publications = allResults.get(resultMeasureDTO);
			List<ResultDTO> results = new ArrayList<ResultDTO>();
			for (PublicationDTO publicationDTO : publications) {
				results.add(new ResultDTO(resultMeasureDTO.getResultType(), publicationDTO));
			}
			ResultsGroupDTO rtsDTO = new ResultsGroupDTO(resultMeasureDTO, results);
			resultsGroupDTOlist.add(rtsDTO);
		}
		KnrDTO knrDTO = new KnrDTO();
		knrDTO.setResearcher(umb.getLoggedUser().getAuthor());
		sfdmb.setOrderBy("publicationYear");
		sfdmb.setDirection("desc");
		sfdmb.populateList();
		knrDTO.setDiplomas(sfdmb.getStudyFinalDocuments());
		Collections.sort(resultsGroupDTOlist, new GenericComparator<ResultsGroupDTO>(
				"resultType.classId", "asc"));
		knrDTO.setResultsGroups(resultsGroupDTOlist);
		retVal.add(knrDTO);
		if(researcher!=null){
			umb.setLoggedUser(loggedUser);
			umb.setLoggedIn(loggedIn);
		}
		return retVal;
	}
	
	private List<Record> getPublications(HttpServletRequest request,
			HttpServletResponse response) {
		List<Record> retVal = new ArrayList<Record>();
		String researcherId = request.getParameter("researcherId");
		String typeP = request.getParameter("type");
		String resultId = request.getParameter("resultId");
		String apvnt = request.getParameter("apvnt");
		BooleanQuery bq = new BooleanQuery();
		RecordDAO recordDAO = new RecordDAO(new RecordDB());
		if(researcherId != null){
			bq.add(new TermQuery(new Term("AUCN", "(BISIS)" + researcherId)), Occur.MUST);
		}
		if(apvnt != null){
			List<Record> researchers = recordDAO.getDTOs(new TermQuery(new Term("APVNT", apvnt)), new AllDocCollector(true));
			if((researchers != null) && (researchers.size() > 0))
				bq.add(new TermQuery(new Term("AUCN", researchers.get(0).getControlNumber())), Occur.MUST);
		}
		if(typeP != null){
			bq.add(new TermQuery(new Term("TYPE", typeP)), Occur.MUST);
		} else {
			BooleanQuery type = new BooleanQuery();
			type.add(new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.SCIENTIFIC_CRITICISM_JOURNAL)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.OTHER_JOURNAL)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.DISCUSSION_PAPER_PROCEEDINGS)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.PAPER_PROCEEDINGS)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.PAPER_MONOGRAPH)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.MONOGRAPH)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.PHD_ART_PROJECT)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.OLD_MASTER_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.MASTER_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.BACHELOR_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.PATENT)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.PRODUCT)), Occur.SHOULD);
			bq.add(type, Occur.MUST);
		}
		if(resultId != null){
			bq.add(new TermQuery(new Term("CN", "(BISIS)" + resultId)), Occur.MUST);
		}
		retVal = recordDAO.getDTOs(bq, new AllDocCollector(true));
		return retVal;
	}
	
	protected FacesContext getFacesContext(HttpServletRequest request, HttpServletResponse response) {
	     
		FacesContext facesContext = null;
		FacesContextFactory contextFactory  =
			(FacesContextFactory)FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
		LifecycleFactory lifecycleFactory =
			(LifecycleFactory)FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
		Lifecycle lifecycle =
			lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);

		facesContext =
			contextFactory.getFacesContext(request.getSession().getServletContext(), request, response ,lifecycle);
		InnerFacesContext.setFacesContextAsCurrentInstance(facesContext);
		UIViewRoot view = facesContext.getApplication().getViewHandler().createView(facesContext, "cms");
		facesContext.setViewRoot(view);
	
		return facesContext;
	}

	private abstract static class InnerFacesContext extends FacesContext {
		protected static void setFacesContextAsCurrentInstance(FacesContext facesContext) {
			FacesContext.setCurrentInstance(facesContext);
		}
	}
	
	
	/**
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}   
	
	public static String subreportDir;
}