/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.record.formula.functions.False;

import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.utils.Sanitizer;

/**
 * @author chenejac@uns.ac.rs
 *
 */
public class JournalAndPapers {
	
	private static Set<ResearchAreaDTO> researchAreasMat;
	
	static {
		researchAreasMat = new HashSet<ResearchAreaDTO>();
		ResearchAreaDTO wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos38");
		researchAreasMat.add(wos);
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos79");
		researchAreasMat.add(wos);
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos164");
		researchAreasMat.add(wos);
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos166");
		researchAreasMat.add(wos);
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos165");
		researchAreasMat.add(wos);
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos169");
		researchAreasMat.add(wos);
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos216");
		researchAreasMat.add(wos);
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos78");
		researchAreasMat.add(wos);
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos80");
		researchAreasMat.add(wos);
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos72");
		researchAreasMat.add(wos);
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos240");
		researchAreasMat.add(wos);
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos76");
		researchAreasMat.add(wos);
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos148");
		researchAreasMat.add(wos);
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos74");
		researchAreasMat.add(wos);
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos38");
		researchAreasMat.add(wos);
	}
	
	private List<PaperJournalDTO> papers;
	private JournalDTO journal;
	private List<ImpactFactor> impactFactors;

	
	/**
	 * 
	 */
	public JournalAndPapers() {
		super();
	}

	/**
	 * @param journal
	 * @param papers
	 */
	public JournalAndPapers(JournalDTO journal, List<PaperJournalDTO> papers, List<ImpactFactor> impactFactors) {
		super();
		this.journal = journal;
		this.papers = papers;
		this.impactFactors = impactFactors;
	}

	/**
	 * @return the papers
	 */
	public List<PaperJournalDTO> getPapers() {
		return papers;
	}

	/**
	 * @param papers the papers to set
	 */
	public void setPapers(List<PaperJournalDTO> papers) {
		this.papers = papers;
	}

	/**
	 * @return the journal
	 */
	public JournalDTO getJournal() {
		return journal;
	}

	/**
	 * @param journal the journal to set
	 */
	public void setJournal(JournalDTO journal) {
		this.journal = journal;
	}

	/**
	 * @return the impactFactors
	 */
	public List<ImpactFactor> getImpactFactors() {
		return impactFactors;
	}

	/**
	 * @param impactFactors the impactFactors to set
	 */
	public void setImpactFactors(List<ImpactFactor> impactFactors) {
		this.impactFactors = impactFactors;
	}

	
	public int getFirstImpactFactorYear(){
		return getImpactFactors().get(0).getYear();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer retVal  = new StringBuffer();
		retVal.append("id," + journal.getControlNumber() + "\n");
		retVal.append("Ime,\"" + journal.getSomeName() + "\"\n");
		retVal.append("Skraceno ime,\"" + ((journal.getNameAbbreviation().getContent()!=null)?(journal.getNameAbbreviation().getContent()):("nema")) + "\"\n");
		retVal.append("ISSN,\"" + ((journal.getIssn()!=null)?(journal.getIssn()):("nema")) + "\"\n");
		retVal.append("URL,\"" + ((journal.getUri()!=null)?(journal.getUri()):("nema")) + "\"\n");
		List<Integer> years = getYears();
		retVal.append(getNumberOfPapers(years) + "\n");
//		retVal.append("Vrednovanje");
//		for (Integer year : years) {
//			retVal.append(",M53");
//		}
		retVal.append(getJournalEvaluations(years));
		retVal.append("\nGodine");
		for (Integer year : years) {
			retVal.append("," + year);
		}
		retVal.append("\nOblasti/impakt faktor");
		retVal.append(getImpactFactors(years));
		retVal.append("\n" + getImpactFactorsPositions(years));
		retVal.append("\n\n");
		return retVal.toString();
//		return journal.getControlNumber() + ",\"" + journal.getSomeName() + "\",\"" + ((journal.getIssn()!=null)?journal.getIssn():"") + "\",\"" + ((journal.getUri()!=null)?journal.getUri():"") + "\",\"" + getDiferentPapersYears() + "\",\""+ getLeadingInternationalJournalYears() +
//				"\",\"" + getOutstandingInternationalJournalYears() + "\",\"" + getInternationalJournalYears() + "\",,,";
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString2() {
		StringBuffer retVal  = new StringBuffer();
		try {
			
		journal.getRecord().loadFromDatabase();
		
		retVal.append(journal.getControlNumber() + ",");
		retVal.append(Sanitizer.sanitizeCSV(journal.getSomeName()) + ",");
		retVal.append("" + ((journal.getNameAbbreviation().getContent()!=null)?(Sanitizer.sanitizeCSV(journal.getNameAbbreviation().getContent())):("nema")) + ",");
		retVal.append("" + ((journal.getIssn()!=null)?(Sanitizer.sanitizeCSV(journal.getIssn())):("nema")) + ",");
		retVal.append("" + ((journal.getUri()!=null)?(Sanitizer.sanitizeCSV(journal.getUri())):("nema")) + "");
		} catch (Exception e) {
			System.out.println(journal.getControlNumber());
			retVal.append(journal.getControlNumber() + ",");
			retVal.append("\"" + journal.getSomeName() + "\",,");
			retVal.append("\"" + ((journal.getIssn()!=null)?(journal.getIssn()):("nema")) + "\",,");
		}

		return retVal.toString();
//		return journal.getControlNumber() + ",\"" + journal.getSomeName() + "\",\"" + ((journal.getIssn()!=null)?journal.getIssn():"") + "\",\"" + ((journal.getUri()!=null)?journal.getUri():"") + "\",\"" + getDiferentPapersYears() + "\",\""+ getLeadingInternationalJournalYears() +
//				"\",\"" + getOutstandingInternationalJournalYears() + "\",\"" + getInternationalJournalYears() + "\",,,";
	}
	
	public String toString2(List<Integer> years) {
		StringBuffer retVal  = new StringBuffer();
		retVal.append("\""+journal.getSomeName()+"\",");
		for (PaperJournalDTO paper : papers) {
			if((paper.getPublicationYear()!=null) && (! "".equals(paper.getPublicationYear().trim()))){
				retVal.append(paper.getPublicationYear());
				break;
			}
		}
		retVal.append(getImpactFactors(years));
		return retVal.toString();
//		return journal.getControlNumber() + ",\"" + journal.getSomeName() + "\",\"" + ((journal.getIssn()!=null)?journal.getIssn():"") + "\",\"" + ((journal.getUri()!=null)?journal.getUri():"") + "\",\"" + getDiferentPapersYears() + "\",\""+ getLeadingInternationalJournalYears() +
//				"\",\"" + getOutstandingInternationalJournalYears() + "\",\"" + getInternationalJournalYears() + "\",,,";
	}
	
	public boolean isInteger(String s){
		try {
			Integer.parseInt(s);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public String toString2(List<Integer> years, int yearFirstIF) {
		StringBuffer retVal  = new StringBuffer();
		retVal.append("\""+journal.getSomeName()+"\",");
		
		int firstEvaluationYear=-1;
		
		if(yearFirstIF!=-1)
			firstEvaluationYear = yearFirstIF;

		for (PaperJournalDTO paper : papers) {
			if((paper.getPublicationYear()!=null)){
				String temp = paper.getPublicationYear();
				if(temp.contains("/")){
					temp = paper.getPublicationYear().substring(0, paper.getPublicationYear().indexOf("/"));
				}
				if (isInteger(temp) == false) {
					continue;
				}
				
				Integer publicationYear = null;
				if(paper.getPublicationYear().contains("/")){
					publicationYear = Integer.parseInt(paper.getPublicationYear().substring(0, paper.getPublicationYear().indexOf("/")));
				} else 
					publicationYear = Integer.parseInt(paper.getPublicationYear());
				
				if(yearFirstIF>publicationYear || yearFirstIF==-1)
					firstEvaluationYear = publicationYear;
			}
		}
		
		if(firstEvaluationYear!=-1)
			retVal.append(firstEvaluationYear);
		
		retVal.append(getImpactFactors(years));
		return retVal.toString();
//		return journal.getControlNumber() + ",\"" + journal.getSomeName() + "\",\"" + ((journal.getIssn()!=null)?journal.getIssn():"") + "\",\"" + ((journal.getUri()!=null)?journal.getUri():"") + "\",\"" + getDiferentPapersYears() + "\",\""+ getLeadingInternationalJournalYears() +
//				"\",\"" + getOutstandingInternationalJournalYears() + "\",\"" + getInternationalJournalYears() + "\",,,";
	}

	private Object getNumberOfPapers(List<Integer> years) {
		StringBuffer retVal = new StringBuffer();
		retVal.append("Broj radova u CRIS-u");
		for (Integer year : years) {
			int count = 0;
			for (PaperJournalDTO paper : papers) {
				if((paper.getPublicationYear()!=null) && (paper.getPublicationYear().equals(year)))
					count++;
			}
			retVal.append(","+count);
		}
		return retVal.toString();
	}

	private String getJournalEvaluations(List<Integer> years) {
		StringBuffer retVal = new StringBuffer();	
		Integer best19861990 = null;
		Integer best19791985 = null;
		for (Integer year=years.get(years.size()-1);year >= years.get(0);year--) {
			Integer evaluation = getJournalEvaluation(year, researchAreasMat);
			if(evaluation != 4){
				String m = null;
				if(evaluation == 3)
					m = "M23";
				else if (evaluation == 2)
					m = "M22";
				else if (evaluation == 1)
					m = "M21";
				retVal.insert(0, "," + m);
			}
			else {
				Integer evaluation1 = getJournalEvaluation(year-1, researchAreasMat);	
				Integer evaluation2 = getJournalEvaluation(year-2, researchAreasMat);
				if(evaluation1 > evaluation2)
					evaluation1 = evaluation2;
				evaluation2 = getJournalEvaluation(year-3, researchAreasMat);
				if(evaluation1 > evaluation2)
					evaluation1 = evaluation2;
				evaluation2 = getJournalEvaluation(year-4, researchAreasMat);
				if(evaluation1 > evaluation2)
					evaluation1 = evaluation2;
				evaluation2 = getJournalEvaluation(year+1, researchAreasMat);
				if(evaluation1 > evaluation2)
					evaluation1 = evaluation2;
				evaluation2 = getJournalEvaluation(year+2, researchAreasMat);
				if(evaluation1 > evaluation2)
					evaluation1 = evaluation2;
				evaluation = evaluation1;
				String m = "M52";
				if(evaluation == 4){
					if ((year >= 1979) && (year <=1985)){
						if (best19861990 != null){
							if(best19861990 == 3)
								m = "M23";
							else if (best19861990 == 2)
								m = "M22";
							else if (best19861990 == 1)
								m = "M21";
						}
					} else if ((year >= 1976) && (year <=1978)){
						if (best19791985 != null) {
							if(best19791985 == 3)
								m = "M23";
							else if (best19791985 == 2)
								m = "M22";
							else if (best19791985 == 1)
								m = "M21";
						} else if (best19861990 != null){
							if(best19861990 == 3)
								m = "M23";
							else if (best19861990 == 2)
								m = "M22";
							else if (best19861990 == 1)
								m = "M21";
						}
					}else if (year < 1976){
							if (best19791985 != null) {
								if(best19791985 == 3)
									m = "M23";
								else if (best19791985 == 2)
									m = "M22";
								else if (best19791985 == 1)
									m = "M21";
							}	
					}
				} else {
					if(evaluation == 3)
						m = "M23";
					else if (evaluation == 2)
						m = "M22";
					else if (evaluation == 1)
						m = "M21";
				}
				retVal.insert(0, "," + m);
			}
			if ((year >= 1979) && (year <=1985)){
				if(evaluation == 4)
					best19791985 = 4; 
				else if ((best19791985 == null) || ((best19791985!=4) && (evaluation < best19791985)))
					best19791985 = evaluation;
						
			}
			if ((year >= 1986) && (year <=1990)){
				if(evaluation == 4)
					best19861990 = 4; 
				else if ((best19861990 == null) || ((best19861990!=4) && (evaluation < best19861990)))
					best19861990 = evaluation;
			}
		}
		retVal.insert(0, "Vrednovanje");
		
		return retVal.toString();
	}
	
	private Integer getJournalEvaluation(Integer year, Set<ResearchAreaDTO> researchAreas) {
		Integer retVal = 4;
		Integer withinResearchAreas = null;
		Integer outsideResearchAreas = null;
		ImpactFactor imf = new ImpactFactor();
		imf.setYear(year);
		imf = (impactFactors.indexOf(imf)!=-1)?impactFactors.get(impactFactors.indexOf(imf)):null;
		if (imf != null){
			for (ResearchAreaRanking rar : imf.getResearchAreas()) {
				boolean m21 = false;
				boolean m22 = false;
				boolean m23 = false;
				if(rar.getDividend() <= 0.3)
					m21 = true;
				else if (rar.getDividend() > 0.5)
						m23 = true;
					else
						m22 = true;
				if(researchAreas.contains(rar.getResearchAreaDTO())){
					if(((withinResearchAreas==null) || (withinResearchAreas > 1)) && (m21))
						withinResearchAreas = 1;
					else if (((withinResearchAreas==null) || (withinResearchAreas > 2)) && (m22))
							withinResearchAreas = 2;
					else if ((withinResearchAreas==null)  && (m23))
							withinResearchAreas = 3;
				} else {
					if(((outsideResearchAreas==null) || (outsideResearchAreas < 3)) && (m23))
						outsideResearchAreas = 3;
					else if (((outsideResearchAreas==null) || (outsideResearchAreas < 2)) && (m22))
							outsideResearchAreas = 2;
					else if ((outsideResearchAreas==null) && (m21))
							outsideResearchAreas = 1;
				}
				
			}
		}
		if(withinResearchAreas != null){
			retVal = withinResearchAreas;
		} else if (outsideResearchAreas != null){
			retVal = outsideResearchAreas;
		}
		return retVal;
	}

	private String getImpactFactorsPositions(List<Integer> years) {
		StringBuffer retVal = new StringBuffer();
		Map<ResearchAreaDTO, String> map = getResearchAreasHashMap();
		for (Integer year : years) {
			ImpactFactor imf = new ImpactFactor();
			imf.setYear(year);
			imf = (impactFactors.indexOf(imf)!=-1)?impactFactors.get(impactFactors.indexOf(imf)):null;
			for (ResearchAreaDTO ra : map.keySet()) {
				String researchArea = map.get(ra);
				researchArea += ",";
				if (imf != null){
					for (ResearchAreaRanking rar : imf.getResearchAreas()) {
						if(rar.getResearchAreaDTO().equals(ra)){
							researchArea += rar.getPosition().intValue() + "/" + Math.round((rar.getPosition()/rar.getDividend()));
							if(rar.getDividend() <= 0.3)
								researchArea += "(M21)";
							else if (rar.getDividend() > 0.5)
									researchArea += "(M23)";
								else
									researchArea += "(M22)";
						}
					}
				}
				map.put(ra, researchArea);
			}
		}
		for (String value : map.values()) {
			retVal.append(value + "\n");
		}
		return retVal.toString();
	}

	private Map<ResearchAreaDTO, String> getResearchAreasHashMap() {
		HashMap<ResearchAreaDTO, String> retVal = new HashMap<ResearchAreaDTO, String>();
		for (ImpactFactor impactFactor : impactFactors) {
			for (ResearchAreaRanking researchArea : impactFactor.getResearchAreas()) {
				if(! retVal.containsKey(researchArea.getResearchAreaDTO())){
					retVal.put(researchArea.getResearchAreaDTO(), "\"" + researchArea.getResearchAreaDTO().getSomeTerm() + "\"");
				}
			}
		}
		return retVal;
	}

	private String getImpactFactors(List<Integer> years) {
		StringBuffer retVal = new StringBuffer();
		ImpactFactor imf = new ImpactFactor();
		for (Integer year : years) {
			retVal.append(",");
			imf.setYear(year);
			if(impactFactors.contains(imf))
				retVal.append(impactFactors.get(impactFactors.indexOf(imf)).getValueOfImpactFactor());
		}
		return retVal.toString();
	}

	@SuppressWarnings("unused")
	private String getDiferentPapersYears() {
		StringBuffer retVal = new StringBuffer();
		HashSet<Integer> years = new HashSet<Integer>();
		if(papers.size() > 0){
			for (PaperJournalDTO paper : papers) {
				if(paper.getPublicationYear() == null)
					retVal.append("Godina nije specificirana!!! ");
				else if (! (years.contains(paper.getPublicationYear()))) {
						years.add(new Integer(paper.getPublicationYear()));
						retVal.append(paper.getPublicationYear().toString() + " ");
					}
			}
		} else {
			retVal.append("Nema radova u ovom casopisu!!!");
		}
		return retVal.toString();
	}
	
	private List<Integer> getYears(){
//		Integer minYear = new Integer(2012);
		Integer minYear = impactFactors.get(0).getYear();
		for (PaperJournalDTO paper : papers) {
			if((paper.getPublicationYear()!=null) && (! "".equals(paper.getPublicationYear().trim()))){
				Integer publicationYear = null;
				if(paper.getPublicationYear().contains("/")){
					publicationYear = Integer.parseInt(paper.getPublicationYear().substring(0, paper.getPublicationYear().indexOf("/")-1));
				} else 
					publicationYear = Integer.parseInt(paper.getPublicationYear());
				if(minYear > publicationYear)
					minYear = publicationYear;
			}
		}
		List<Integer> retVal = new ArrayList<Integer>();
		for(int i = minYear; i <= 2011; i++)
			retVal.add(i);
		return retVal;
	}
	
	@SuppressWarnings("unused")
	private String getLeadingInternationalJournalYears() {
		StringBuffer retVal = new StringBuffer();
		for (ImpactFactor impactFactor : impactFactors) {
			String temp = impactFactor.getLeadingInternationalJournalResearchAreas();
			if((temp != null) && (! "".equals(temp))){
				if(retVal.length() > 0)
					retVal.append(", ");
				retVal.append(impactFactor.getYear() + "(" + temp + ")");
			}
		}
		return retVal.toString();
	}
	
	@SuppressWarnings("unused")
	private String getOutstandingInternationalJournalYears() {
		StringBuffer retVal = new StringBuffer();
		for (ImpactFactor impactFactor : impactFactors) {
			String temp = impactFactor.getOutstandingInternationalJournalResearchAreas();
			if((temp != null) && (! "".equals(temp))){
				if(retVal.length() > 0)
					retVal.append(", ");
				retVal.append(impactFactor.getYear() + "(" + temp + ")");
			}
		}
		return retVal.toString();
	}
	
	@SuppressWarnings("unused")
	private String getInternationalJournalYears() {
		StringBuffer retVal = new StringBuffer();
		for (ImpactFactor impactFactor : impactFactors) {
			String temp = impactFactor.getInternationalJournalResearchAreas();
			if((temp != null) && (! "".equals(temp))){
				if(retVal.length() > 0)
					retVal.append(", ");
				retVal.append(impactFactor.getYear() + "(" + temp + ")");
			}
		}
		return retVal.toString();
	}

}
