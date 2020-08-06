package rs.ac.uns.ftn.informatika.bibliography.dto.eval;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
@SuppressWarnings("serial")
public class ResultsTypeGroupDTO extends ClassDTO {

	public static final String RESULTS_TYPE_GROUP_SCHEMA = "ruleBookResultsTypeGroup";
	private List<ResultsTypeDTO> resultsTypes;
	
	private Calendar startDate;
	private Calendar endDate;
	
	public ResultsTypeGroupDTO(){
		super();
		schemeId = ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA;
		
		startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 1900);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
		
		resultsTypes = new ArrayList<ResultsTypeDTO>();
	}
	
	/**
	 * @param schemeId
	 * @param classId
	 * @param name
	 * @param description
	 * @param startDate
	 * @param endDate
	 */
	public ResultsTypeGroupDTO(String schemeId, String classId, MultilingualContentDTO term, List<MultilingualContentDTO> termTranslations,
			MultilingualContentDTO description, List<MultilingualContentDTO> descriptionTranslations,Calendar startDate, Calendar endDate) {
		super(schemeId, classId, term, termTranslations, description, descriptionTranslations);
		
		this.startDate = startDate;
		
		if(this.startDate==null){
			this.startDate = new GregorianCalendar();
			this.startDate.set(Calendar.YEAR, 1900);
			this.startDate.set(Calendar.MONTH, Calendar.JANUARY);
			this.startDate.set(Calendar.DAY_OF_MONTH, 1);
		}
		
		this.endDate = endDate;
		
		if(this.endDate==null){
			this.endDate = new GregorianCalendar();
			this.endDate.set(Calendar.YEAR, 2099);
			this.endDate.set(Calendar.MONTH, Calendar.DECEMBER);
			this.endDate.set(Calendar.DAY_OF_MONTH, 31);
		}
		resultsTypes = new ArrayList<ResultsTypeDTO>();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		return super.equals(arg0);
	}
	
//	/**
//	 * @see java.lang.Object#toString()
//	 */
//	@Override
//	public String toString() {
//		StringBuffer retVal = new StringBuffer();
//		retVal.append(term.getContent());
//		
//		return retVal.toString();
//	}
	
	/**
	 * @return the startDate
	 */
	public Calendar getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	public List<ResultsTypeDTO> getResultsTypes() {
		return resultsTypes;
	}

	public void setResultsTypes(List<ResultsTypeDTO> resultsTypes) {
		this.resultsTypes = resultsTypes;
	}
	/* (non-Javadoc)
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO#getSomeTerm()
	 */
	@Override
	public String getSomeTerm() {
		// TODO Auto-generated method stub
		return super.getSomeTerm();
	}
	
	public void arangeResultsTypes(){
		
		List<ResultsTypeDTO> tempLista = new ArrayList<ResultsTypeDTO>();
		tempLista.addAll(resultsTypes);
		resultsTypes.clear();
		
		for (ResultsTypeDTO resultsTypeDTO : tempLista) {
			if(resultsTypeDTO.getSuperResultsType()==null){
				resultsTypes.add(resultsTypeDTO);
				resultsTypes.addAll(ResultsTypeDTO.getSubResultsTypes(resultsTypeDTO, tempLista));
			}
		}
	}
	
	public boolean arangeResultsTypes(int pozicija1, int pozicija2, String option, String order){
		
		List<ResultsTypeDTO> tempListaALL = new ArrayList<ResultsTypeDTO>();
		tempListaALL.addAll(resultsTypes);
		resultsTypes.clear();
		
		List<ResultsTypeDTO> tempListaParent = new ArrayList<ResultsTypeDTO>();
		for (ResultsTypeDTO resultsTypeDTO : tempListaALL) {
			if(resultsTypeDTO.getSuperResultsType()==null){
				tempListaParent.add(resultsTypeDTO);
			}
		}
		if(ResultsTypeDTO.sortList(tempListaParent, pozicija1, pozicija2, option, order)==false)
			return false;
		
		try{
			for (ResultsTypeDTO resultsTypeDTO : tempListaParent) {
					resultsTypes.add(resultsTypeDTO);
					resultsTypes.addAll(ResultsTypeDTO.getSubResultsTypesSorted(resultsTypeDTO, tempListaALL, pozicija1, pozicija2, option, order));
			}
		}catch (Exception e) {
			return false;
		}
		
		return true;
	}

	public void arangeRandomResultsTypes(){
		HashMap<String, ResultsTypeDTO> tempMapa = new HashMap<String, ResultsTypeDTO>();
		for (ResultsTypeDTO resultsTypeDTO : resultsTypes) {
			tempMapa.put(resultsTypeDTO.getClassId(), resultsTypeDTO);
		}
		resultsTypes.clear();
		resultsTypes.addAll(tempMapa.values());
	}
	
	
}
