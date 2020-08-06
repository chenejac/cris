package rs.ac.uns.ftn.informatika.bibliography.dto.eval;

import java.io.Serializable;

import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
@SuppressWarnings("serial")
public class SpecVerLstResultsTypeOfResPublDTO implements Serializable {
	
	private SpecVerLstDTO specVerLst;
	private PublicationDTO publication;
	private ResultsTypeDTO resultsType;
	private String publHumanReadId;
	private String publDisplName;
	protected int year;

	
	public SpecVerLstResultsTypeOfResPublDTO() {
		super();
		year=-1;
	}

//	Konstruktor za novi zapis koji treba da se upise u bazu
	/**
	 * @param specVerLst
	 * @param resultsType
	 * @param publication
	 * @param year
	 */
	public SpecVerLstResultsTypeOfResPublDTO(SpecVerLstDTO specVerLst,
			ResultsTypeDTO resultsType,
			PublicationDTO publication, int year) {
		super();
		this.specVerLst = specVerLst;
		this.resultsType = resultsType;
		this.publication = publication;
		if(publication instanceof JournalDTO){
			this.publHumanReadId= ((JournalDTO) publication).getIssn();
			this.publDisplName = ((JournalDTO) publication).getSomeName();
		}
		this.year = year;
	}
	
//	Konstruktor za postojeci zapis koji se ocitava iz baze
	/**
	 * @param specVerLst
	 * @param resultsType
	 * @param publication
	 * @param publHumanIdentifier
	 * @param publDisplName
	 * @param year
	 */
	public SpecVerLstResultsTypeOfResPublDTO(SpecVerLstDTO specVerLst,
			ResultsTypeDTO resultsType, PublicationDTO publication,
			String publHumanReadId, String publDisplName, int year) {
		super();
		this.specVerLst = specVerLst;
		this.resultsType = resultsType;
		this.publication = publication;
		this.publHumanReadId = publHumanReadId;
		this.publDisplName = publDisplName;
		this.year = year;
	}

	/**
	 * @return the specVerLst
	 */
	public SpecVerLstDTO getSpecVerLst() {
		return specVerLst;
	}

	/**
	 * @param specVerLst the specVerLst to set
	 */
	public void setSpecVerLst(SpecVerLstDTO specVerLst) {
		this.specVerLst = specVerLst;
	}
	
	/**
	 * @return the resultsType
	 */
	public ResultsTypeDTO getResultsType() {
		return resultsType;
	}

	/**
	 * @param resultsType the resultsType to set
	 */
	public void setResultsType(ResultsTypeDTO resultsType) {
		this.resultsType = resultsType;
	}

	/**
	 * @return the publication
	 */
	public PublicationDTO getPublication() {
		return publication;
	}

	/**
	 * @param publication the publication to set
	 */
	public void setPublication(PublicationDTO publication) {
		this.publication = publication;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return the publHumanReadId
	 */
	public String getPublHumanReadId() {
		return publHumanReadId;
	}

	/**
	 * @param publHumanReadId the publHumanReadId to set
	 */
	public void setPublHumanReadId(String publHumanReadId) {
		this.publHumanReadId = publHumanReadId;
	}

	/**
	 * @return the publDisplName
	 */
	public String getPublDisplName() {
		return publDisplName;
	}

	/**
	 * @param publDisplName the publDisplName to set
	 */
	public void setPublDisplName(String publDisplName) {
		this.publDisplName = publDisplName;
	}

	
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		
		try {
			SpecVerLstResultsTypeOfResPublDTO temp = (SpecVerLstResultsTypeOfResPublDTO) arg0;
			if ((this.specVerLst != null) && (this.specVerLst.getControlNumber().equals(temp.specVerLst.getControlNumber())) && 
					(this.publHumanReadId != null) && (this.publHumanReadId.equals(temp.publHumanReadId)) &&
					(this.year == temp.year))
				return true;
			else
				return false;
		} catch (Throwable e) {

			return false;
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return specVerLst+ ";"+ publHumanReadId +";" + year + ";"+ resultsType + ";" + publDisplName + "("+(publication==null?"/":publication) + ");";
	}
	
	public static boolean isValidISSN(String ISSN){
		boolean retVal = true;
		if(ISSN.length() != 9 || ISSN.charAt(4) != '-')
			retVal = false;
		return retVal;
	}
	
	public static boolean isValidPublHumanReadId(String publicationHumanReadableId){
		boolean retVal = true;
		String[] tokens = publicationHumanReadableId.split(";");
		String comparableID = null;
		
		if (publicationHumanReadableId.contains("ISBN")) {
			return false;
		}
		
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].endsWith("(pISSN)") || tokens[i].endsWith("(eISSN)") || tokens[i].endsWith("(oldISSN)")) {
				comparableID = tokens[i].substring(0, tokens[i].lastIndexOf("("));
			} else {
				comparableID = tokens[i];
			}	
			if(isValidISSN(comparableID)==false){
				retVal = false;
				break;
			}
		}
		
		return retVal;
	}
	
	public static String [] getValidPublHumanReadId(String publicationHumanReadableId){
		String[] tokens = publicationHumanReadableId.split(";");
		String [] retVal = new String[tokens.length];
		String comparableID = null;
		
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].endsWith("(pISSN)") || tokens[i].endsWith("(eISSN)") || tokens[i].endsWith("(oldISSN)")) {
				comparableID = tokens[i].substring(0, tokens[i].lastIndexOf("("));
			} else {
				comparableID = tokens[i];
			}	
			retVal[i]=comparableID;
		}
		
		return retVal;
	}
}
