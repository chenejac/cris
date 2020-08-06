/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.dto.eval;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
@SuppressWarnings("serial")
public class ResultsTypeDTO extends ClassDTO {
	
	public static final String RESULTS_TYPE_SCHEMA = "resultsType";
	
	private ResultsTypeGroupDTO superResultsTypeGroup;
	private ResultsTypeDTO superResultsType;
	
	private Calendar startDate;
	private Calendar endDate;
	
	public ResultsTypeDTO() {
		super();
		schemeId = ResultsTypeDTO.RESULTS_TYPE_SCHEMA;
		startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 2008);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
	}

	/**
	 * @param schemeId
	 * @param classId
	 * @param term
	 * @param termTranslations
	 * @param description
	 * @param descriptionTranslations
	 * @param superResultsTypeGroup
	 * @param superResultsType
	 */
	public ResultsTypeDTO(String schemeId, String classId,
			MultilingualContentDTO term,
			List<MultilingualContentDTO> termTranslations,
			MultilingualContentDTO description,
			List<MultilingualContentDTO> descriptionTranslations,
			ResultsTypeGroupDTO superResultsTypeGroup,
			ResultsTypeDTO superResultsType) {
		super(schemeId, classId, term, termTranslations, description,
				descriptionTranslations);
		this.superResultsTypeGroup = superResultsTypeGroup;
		this.superResultsType = superResultsType;
		
		startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 2008);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
	}
	
	/**
	 * @param schemeId
	 * @param classId
	 * @param term
	 * @param termTranslations
	 * @param description
	 * @param descriptionTranslations
	 * @param superResultsTypeGroup
	 * @param superResultsType
	 * @param startDate
	 * @param endDate
	 */
	public ResultsTypeDTO(String schemeId, String classId,
			MultilingualContentDTO term,
			List<MultilingualContentDTO> termTranslations,
			MultilingualContentDTO description,
			List<MultilingualContentDTO> descriptionTranslations,
			ResultsTypeGroupDTO superResultsTypeGroup,
			ResultsTypeDTO superResultsType, Calendar startDate,
			Calendar endDate) {
		super(schemeId, classId, term, termTranslations, description,
				descriptionTranslations);
		this.superResultsTypeGroup = superResultsTypeGroup;
		this.superResultsType = superResultsType;
		this.startDate = startDate;
		this.endDate = endDate;
	}





	/**
	 * @return the superResultsTypeGroup
	 */
	public ResultsTypeGroupDTO getSuperResultsTypeGroup() {
		return superResultsTypeGroup;
	}

	/**
	 * @param superResultsTypeGroup the superResultsTypeGroup to set
	 */
	public void setSuperResultsTypeGroup(ResultsTypeGroupDTO superResultsTypeGroup) {
		this.superResultsTypeGroup = superResultsTypeGroup;
	}

	/**
	 * @return the superResultsType
	 */
	public ResultsTypeDTO getSuperResultsType() {
		return superResultsType;
	}

	/**
	 * @param superResultsType the superResultsType to set
	 */
	public void setSuperResultsType(ResultsTypeDTO superResultsType) {
		this.superResultsType = superResultsType;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		return super.equals(arg0);
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO#getClassId()
	 */
	@Override
	public String getClassId() {
		return super.getClassId();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return classId + " - " + term.getContent();
	}
	
	public String toStringWithSupersResultsTypeGroupAndSuperResultsType() {
		String retVal = "";
		if(superResultsTypeGroup!=null)
			retVal += ("superGroup:" + superResultsTypeGroup);
		if(superResultsType!=null)
			retVal += ("superType:" + superResultsType);
		return classId + " - " + term.getContent() + "("+retVal+")";
	}
	
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

	public static List<ResultsTypeDTO> getSubResultsTypes(ResultsTypeDTO rt, List<ResultsTypeDTO> list){
		List<ResultsTypeDTO> retVal = new ArrayList<ResultsTypeDTO>();
		for (ResultsTypeDTO resultsTypeDTO : list) {
			if(resultsTypeDTO.getSuperResultsType()!=null && resultsTypeDTO.getSuperResultsType().equals(rt)){
				retVal.add(resultsTypeDTO);
				retVal.addAll(getSubResultsTypes(resultsTypeDTO, list));
			}
		}
		return retVal;
	}
	
	public static List<ResultsTypeDTO> getSubResultsTypesSorted(ResultsTypeDTO rt, List<ResultsTypeDTO> list, int pozicija1, int pozicija2, String option, String order){
		
		List<ResultsTypeDTO> retVal = new ArrayList<ResultsTypeDTO>();
		
		List<ResultsTypeDTO> tempLista = new ArrayList<ResultsTypeDTO>();
		for (ResultsTypeDTO resultsTypeDTO : list) {
			if(resultsTypeDTO.getSuperResultsType()!=null && resultsTypeDTO.getSuperResultsType().equals(rt)){
				tempLista.add(resultsTypeDTO);
			}
		}
		ResultsTypeDTO.sortList(tempLista, pozicija1, pozicija2, option, order);
		
		for (ResultsTypeDTO resultsTypeDTO : tempLista) {
				retVal.add(resultsTypeDTO);
				List<ResultsTypeDTO> tempList = getSubResultsTypesSorted(resultsTypeDTO, list, pozicija1, pozicija2, option, order);
				sortList(tempList, pozicija1, pozicija2, option, order);
				retVal.addAll(tempList);
		}
		return retVal;
	}
	
	public static boolean sortList(List<ResultsTypeDTO> list, int pozicija1, int pozicija2, String option, String order){
		
		try{
			if(option.equalsIgnoreCase("NUMBER")){
				for (int i = 0; i < list.size()-1; i++) {
					for (int j = i; j < list.size(); j++) {
						ResultsTypeDTO tmp1 = list.get(i);
						ResultsTypeDTO tmp2 = list.get(j);
						
						int tmp1Broj = Integer.parseInt(tmp1.getClassId().substring(pozicija1, pozicija2));
						int tmp2Broj = Integer.parseInt(tmp2.getClassId().substring(pozicija1, pozicija2));
						
						if( (tmp1Broj>tmp2Broj && order.equalsIgnoreCase("ASC")) || (tmp1Broj<tmp2Broj && order.equalsIgnoreCase("DESC"))){
							list.set(i, tmp2);
							list.set(j, tmp1);	
						}				
					}
				}
			}
			else if(option.equalsIgnoreCase("TEXT")){
				for (int i = 0; i < list.size()-1; i++) {
					for (int j = i; j < list.size(); j++) {
						ResultsTypeDTO tmp1 = list.get(i);
						ResultsTypeDTO tmp2 = list.get(j);
						
						String tmp1Txt = tmp1.getClassId().substring(pozicija1, pozicija2);
						String tmp2Txt = tmp2.getClassId().substring(pozicija1, pozicija2);
						
						if( (tmp1Txt.compareTo(tmp2Txt)>0 && order.equalsIgnoreCase("ASC")) || (tmp1Txt.compareTo(tmp2Txt)>-1 && order.equalsIgnoreCase("DESC"))){
							list.set(i, tmp2);
							list.set(j, tmp1);	
						}				
					}
				}
			}	
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}
}