package rs.ac.uns.ftn.informatika.bibliography.reports;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.Samovrednovanje;


public class ReportsGeneratorServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	

	
	public ReportsGeneratorServlet() {
		super();
		// TODO Auto-generated constructor stub
	}


	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("Report generator started...");
		String organisation = (String)req.getAttribute("organisation");
		List<String> reportTypesToGenerate = (List<String>)req.getAttribute("reportTypes");
		int numberOfYearsForMentors = Integer.parseInt((String)req.getAttribute("numberOfYearsForMentors"));
		System.out.println("Organization: "+organisation);
		
		Samovrednovanje.organisation = organisation;
		Samovrednovanje.reportTypesToGenerate = reportTypesToGenerate;
		Samovrednovanje.numberOfYearsForMentors = numberOfYearsForMentors;		
		boolean initOk = Samovrednovanje.init();
		if(initOk){
			Samovrednovanje.runReportGeneration();
		}	
	}
	
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

}
