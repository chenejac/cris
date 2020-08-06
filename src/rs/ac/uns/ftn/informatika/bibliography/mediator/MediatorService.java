package rs.ac.uns.ftn.informatika.bibliography.mediator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.lucene.search.Query;
import org.z3950.zing.cql.CQLNode;
import org.z3950.zing.cql.CQLParseException;
import org.z3950.zing.cql.CQLParser;

import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;
import rs.ac.uns.ftn.informatika.bibliography.wrapper.LuceneWrapper;


public class MediatorService implements Mediator{

//	protected Retriever retriever = new Retriever(InitializerServlet.getIndexPath());
	public ResourceBundle rbSearch = PropertyResourceBundle.getBundle("messages.messages-search", new Locale("en"));
	
	public String errMessage = null;
	public Locale localeLan = new Locale("en");
	public static String contextSetPath = null;
	
	public LuceneWrapper luceneWraper = null;

	
	public List<RecordDTO> getRecords(Object query){
		List<Record> records=null;
		List<RecordDTO> retVal =null;
		Query q;
		errMessage = null;	
		CQLNode root = null;
		
		if(localeLan==null)
			setLocaleLan(new Locale("en"));
		
		if(contextSetPath==null)
		{
			errMessage = "No Contex Set path Defined";
			return null;
		}
		
		if(luceneWraper==null)
			luceneWraper = new LuceneWrapper();
		
//		System.out.println("**************************************");
//		System.out.println("Pocetni upit pre obrade: "+query);
		
		luceneWraper.setLocaleLan(localeLan);

		if (query instanceof String){
			CQLParser parser = new CQLParser();       	
    	    try {   	    	
				root = parser.parse((String)query);
				q=luceneWraper.makeRootQuery(root);
				if(q==null)
				{
					errMessage = rbSearch.getString("search.mediatorService.CQLNull") + (luceneWraper.errorMessage!=null? luceneWraper.errorMessage + "<br/>" + query: query);
					return null;
				}
//				System.out.println("Lucine query posle obrade: "+q.toString());
				records = Retriever.select(q, new AllDocCollector(true));

			} catch (CQLParseException e) {
				errMessage = rbSearch.getString("search.mediatorService.CQLParseException") + "<br/>"+ query;
			} catch (IOException e) {
				errMessage = rbSearch.getString("search.mediatorService.IOException")+ "<br/>"+ query;
			}
			catch (Exception e) {
				errMessage = rbSearch.getString("search.mediatorService.Exception")+ "<br/>"+ query;
			}
		}else if (query instanceof CQLNode){
			
			q=luceneWraper.makeRootQuery((CQLNode)query);
			if(q==null)
			{
				errMessage = rbSearch.getString("search.mediatorService.CQLNull") + (luceneWraper.errorMessage!=null? luceneWraper.errorMessage + "<br/>" + query: query);
				return null;
			}
			records = Retriever.select(q, new AllDocCollector(true));
		}
		
		if(records==null)
		{
			errMessage = rbSearch.getString("search.mediatorService.NoData");
			return null;
		}
		else if(records.size()==0)
		{
			errMessage = rbSearch.getString("search.mediatorService.NoData");
			return null;
		}
		else
		{
			retVal = new ArrayList<RecordDTO>();
		}
		
//		System.out.println("A imamo resenja: "+records.size());
		
		StringBuffer resultBufer = new StringBuffer();
		int i = 0;
		for(Record rec : records)
		{
			i += 1;
//			System.out.println(i+" Result----------BEGIN--------------");
//			System.out.println(rec.toString());
//			System.out.println(i+" Result************END************");
			
//			resultBufer.append(i+" Result----------BEGIN--------------\n");
//			resultBufer.append(rec.toString()+"\n");
//			resultBufer.append(i+" Result************END************\n");

//			if(i>219 && i<331)
//				System.out.println(((RecordDTO)rec.getDto()).getControlNumber()+((RecordDTO)rec.getDto()).getStringRepresentation());
			
			retVal.add((RecordDTO)rec.getDto());
			
//			if(i>1 && i<301)
//			{
//				System.out.println(i+" Result----------BEGIN--------------\n");
//				System.out.println(((RecordDTO)rec.getDto()).getStringRepresentation()+"\n");
//				System.out.println(i+" Result************END************\n");
//			}
		}
//		System.out.println(resultBufer.toString());
		
		return retVal;
	}

	/**
	 * @return the errMessage
	 */
	public String getErrMessage() {
		return errMessage;
	}

	/**
	 * @param errMessage the errMessage to set
	 */
	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}

	/**
	 * @return the localeLan
	 */
	public Locale getLocaleLan() {
		return localeLan;
	}

	/**
	 * @param localeLan the localeLan to set
	 */
	public void setLocaleLan(Locale localeLan) {
		this.rbSearch = PropertyResourceBundle.getBundle(
				"messages.messages-search", localeLan);
		this.localeLan = localeLan;
	}

	/**
	 * @return the contextSetPath
	 */
	public static String getContextSetPath() {
		return contextSetPath;
	}

	/**
	 * @param contextSetPath the contextSetPath to set
	 */
	public static void setContextSetPath(String contextSetPath) {
		MediatorService.contextSetPath = contextSetPath;
	}
	
}
