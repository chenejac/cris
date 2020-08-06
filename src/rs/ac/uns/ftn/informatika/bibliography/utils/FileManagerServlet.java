package rs.ac.uns.ftn.informatika.bibliography.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;

import javax.faces.FactoryFinder;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.eval.FileDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileDTO;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.StudyFinalDocumentManagedBean;
import rs.ac.uns.ftn.informatika.bibliography.personalization.FeedbackLogger;


@SuppressWarnings("serial")
public class FileManagerServlet extends HttpServlet {

  public void doGet(HttpServletRequest req, HttpServletResponse res) {
  		handleDownload(req,res); 
  }
  
  
  private void handleDownload(HttpServletRequest req, HttpServletResponse res) {  	
	  		String userAgent = req.getHeader("User-Agent");
			String controlNumber = (req.getParameter("controlNumber")!=null)?req.getParameter("controlNumber"):req.getParameter("amp;controlNumber");
			String fileName = (req.getParameter("fileName")!=null)?req.getParameter("fileName"):req.getParameter("amp;fileName");
			String idString = (req.getParameter("id")!=null)?req.getParameter("id"):req.getParameter("amp;id");
			int id = Integer.parseInt(idString);
			String source = (req.getParameter("source")!=null)?req.getParameter("source"):req.getParameter("amp;source");
			String licenceAccepted = (req.getParameter("licenseAccepted")!=null)?req.getParameter("licenseAccepted"):req.getParameter("amp;licenseAccepted");
			
			String recommendationString = (req.getParameter("recommendationScore")!=null)?req.getParameter("recommendationScore"):req.getParameter("amp;recommendationScore");
			String relevanceString = (req.getParameter("relevanceScore")!=null)?req.getParameter("relevanceScore"):req.getParameter("amp;relevanceScore");
			String mixedString = (req.getParameter("mixedScore")!=null)?req.getParameter("mixedScore"):req.getParameter("amp;mixedScore");
			
			String searchingMode = (req.getParameter("searchingMode")!=null)?req.getParameter("searchingMode"):req.getParameter("amp;searchingMode");
			
			String positionString = (req.getParameter("position")!=null)?req.getParameter("position"):req.getParameter("amp;position");
			
			
			
			if(source == null){
				source = "CRIS UNS";
			}
			FileDAO fileDAO = new FileDAO();
			FileDTO fileDTO = fileDAO.getFileFromDatabase(id);
			if((canDownload(req, res, id) == false) && ((((fileDTO.getType().equals("document")) && (licenceAccepted == null)) || (fileDTO.getType().equals("preliminaryTheses"))) && (!"NaRDuS".equals(source)) && (!"OATD".equals(source)))){
				RecordDAO recordDAO = new RecordDAO(new RecordDB());
				RecordDTO selectedRecord = recordDAO.getDTO(controlNumber);
				String ipAddress = null;
				try{
					ipAddress  = req.getHeader("X-FORWARDED-FOR");  
					if(ipAddress == null)  
			        {  
						ipAddress = req.getRemoteAddr();  
			        }  else {
			        	ipAddress += "(proxy = " + req.getRemoteAddr() + ")";
			        }
				} catch (Exception ex){
				}
				
				ClientLocation location = GeolocationManager.getLocation((req.getHeader("X-FORWARDED-FOR")==null)?req.getRemoteAddr():req.getHeader("X-FORWARDED-FOR"));
				
				String referrer = req.getHeader("referer");
				if(referrer != null)
					source += " (" + referrer +")";
				
				String stringRepresentation = " | record: notLoaded";
				if(selectedRecord != null)
					stringRepresentation = " | record: " + selectedRecord.getStringRepresentation();
				
				String license = "not defined";
				/*if(fileDTO.getLicense() != null)
						license = fileDTO.getLicense();*/
				
				if(selectedRecord instanceof StudyFinalDocumentDTO)
					if(((StudyFinalDocumentDTO)selectedRecord).getFileLicense() != null)
						license = ((StudyFinalDocumentDTO)selectedRecord).getFileLicense();
				
				
				String sessionId = "not defined";
				String userId = null;
				if(req.getCookies() != null){
					for (Cookie cookie:req.getCookies())
						if(cookie.getName().equals("JSESSIONID"))
							sessionId = cookie.getValue();
						else if (cookie.getName().equals("userId"))
							userId = cookie.getValue();
				}
				
				if(userId == null){
					userId = "" + System.currentTimeMillis() + "" + (int)(Math.random() * 100);
					Cookie userCookie = new Cookie("userId", userId);
					userCookie.setMaxAge(60*60*24*365*10); //Store cookie for 10 years
					userCookie.setPath("/");
					res.addCookie(userCookie);
				}
				
				float recommendationScore = -1f;
				
				try{
					if(recommendationString != null)
						recommendationScore = Float.parseFloat(recommendationString);
				} catch (Exception e){
					
				}
				
				float relevanceScore = -1f;
				
				try{
					if(relevanceString != null)
						relevanceScore = Float.parseFloat(relevanceString);
				} catch (Exception e){
					
				}
				
				float mixedScore = -1f;
				
				try{
					if(mixedString != null)
						mixedScore = Float.parseFloat(mixedString);
				} catch (Exception e){
					
				}
				
				int position = -1;
				
				try{
					if(positionString != null)
						position = Integer.parseInt(positionString);
				} catch (Exception e){
					
				}
				Date date = new Date();
				
				logSource.info("Date and time: " + date + "| miliseconds: " + date.getTime() + "| + session id: " +
						"" + sessionId + "| userId: " + userId + "| ip address: " + ipAddress + "| location: " + location.toString() + "" +
						"| user agent (device): " + userAgent + "| download record id: " + controlNumber + "| file id: " + id + 
						"| file name: " + fileName + "| source: " + source  + "| license: " + license + stringRepresentation );	
				
				if(recommendationScore > 0f){
					FeedbackLogger.logRecommendationEvaluation(userId, controlNumber, searchingMode, position, relevanceScore, recommendationScore, mixedScore, "implicit", "true positive");
				} else if (recommendationScore == 0f){
					FeedbackLogger.logRecommendationEvaluation(userId, controlNumber, searchingMode, position, relevanceScore, recommendationScore, mixedScore, "implicit", "false negative");
				}
			}
			
			if ((canDownload(req, res, id)) || ((((!fileDTO.getType().equals("document"))) || (((licenceAccepted != null) && ("true".equals(licenceAccepted))) || ("NaRDuS".equals(source)) || ("OATD".equals(source)))))){
				if(("NaRDuS".equals(source)) || ("OATD".equals(source)) || canDownload(req, res, id) || (isOpenAccess(req, res, id)) || (fileDTO.getType().equals("wordCloudImage"))) {
					InputStream stream = FileStorage.get(fileDTO);
					res.setContentType(fileDTO.getMime());
					ServletOutputStream out;
					try {
						out = res.getOutputStream();
					 byte[] bbuf = new byte[100];
					 int length = 0;
						while ((stream != null) && ((length = stream.read(bbuf)) != -1))
						   {
						       out.write(bbuf,0,length);
						   }
						out.close();
					} catch (ClientAbortException e) {
					} catch (IOException e) {
						log.fatal(e);
						writeError(res,e);
					}
				} else {
					writeUnauthorized(res);
				}
			} else {
				if(isOpenAccess(req, res, id)){
					try {
						String language = (req.getParameter("language")!=null)?req.getParameter("language"):req.getParameter("amp;language");
						if(language == null){
							language = "sr";
						}
						res.sendRedirect("../license.jsf?language=" + language);
					} catch (IOException e) {
					}
				} else
					writeUnauthorized(res);
			}
		}			
	 
  private boolean canDownload(HttpServletRequest request, HttpServletResponse response, int id) {
	  	FacesContext facesCtx = getFacesContext(request, response);
		ExternalContext extCtx = facesCtx.getExternalContext();
		StudyFinalDocumentManagedBean sfmb = (StudyFinalDocumentManagedBean) extCtx.getSessionMap().get(
				"studyFinalDocumentManagedBean");
		if (sfmb == null) {
			sfmb = new StudyFinalDocumentManagedBean();
			extCtx.getSessionMap().put("studyFinalDocumentManagedBean", sfmb);
		}
		return sfmb.canDownload(id); 
}
  
  private boolean isOpenAccess(HttpServletRequest request, HttpServletResponse response, int id) {
	  	FacesContext facesCtx = getFacesContext(request, response);
		ExternalContext extCtx = facesCtx.getExternalContext();
		StudyFinalDocumentManagedBean sfmb = (StudyFinalDocumentManagedBean) extCtx.getSessionMap().get(
				"studyFinalDocumentManagedBean");
		if (sfmb == null) {
			sfmb = new StudyFinalDocumentManagedBean();
			extCtx.getSessionMap().put("studyFinalDocumentManagedBean", sfmb);
		}
		return sfmb.isOpenAccess(id); 
}


private void writeError(HttpServletResponse res, Exception ex) {
    res.setContentType("text/plain");
    res.setStatus(500);
    try {
      PrintWriter out = res.getWriter();
      out.println("ERROR");
      out.println(ex);      
    } catch (IOException ex1) {
    	 ex1.printStackTrace();
      log.fatal(ex1);
    }
  }
  
  private void writeUnauthorized(HttpServletResponse res) {
//	  res.setContentType("text/plain");
//	    res.setStatus(401);
	    try {
	  		res.sendRedirect("../notAllowed.jsf"); 
//	      PrintWriter out = res.getWriter();
//	      out.println("The request requires user authentication!!!");    
	    } catch (IOException ex1) {
//	    	 ex1.printStackTrace();
	      log.fatal(ex1);
	    }
	  }
  
  
  public void destroy() {
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
  
  private static Log log = LogFactory.getLog(FileManagerServlet.class.getName());
  private static Log logSource = LogFactory.getLog("rs.download");
}
