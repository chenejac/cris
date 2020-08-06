package rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje;

import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PatentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProductDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.utils.Sanitizer;

public class ResultForTypeDTO {
	String resultType; // M21, M22,...
	String result;     // Harvard reprezentacija
	String firstAuthorId;
	String authorsInstitutions;
	Record record;
	
	public ResultForTypeDTO(Record record) {
		super();
		this.record = record;
		if((record.getDto() instanceof PublicationDTO) && (((PublicationDTO)record.getDto()).getAllAuthors() != null) && (((PublicationDTO)record.getDto()).getAllAuthors().size()>0)){
			authorsInstitutions = "";
			boolean check = false;
			for (AuthorDTO author : ((PublicationDTO)record.getDto()).getAllAuthors()) {
				if((author.getInstitutionName() != null) && ((author.getInstitutionName().contains("Tehnološki fakultet Novi Sad, Univerzitet u Novom Sadu")) || (author.getInstitutionName().contains("Prirodno-matematički fakultet u Novom Sadu, Univerzitet u Novom Sadu")))){
					if((authorsInstitutions.length() == 0)){
						authorsInstitutions = author.getInstitutionName();
					} else if(! authorsInstitutions.contains(author.getInstitutionName())){
							authorsInstitutions += "; " + author.getInstitutionName();
					}
				} else {
					check = true;
				}
			}
			if(check && (! authorsInstitutions.contains(";")))
				authorsInstitutions = "Potrebna provera";
		}
	}
	
	
	/**
	 * @param resultType
	 * @param result
	 */
	public ResultForTypeDTO(String resultType, String result, String authorsInstitutions, Record record) {
		this.record = record;
		this.resultType = resultType;
		this.result = result;
		this.authorsInstitutions = authorsInstitutions;
	}
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * @return the resultType
	 */
	public String getResultType() {
		return resultType;
	}
	/**
	 * @param resultType the resultType to set
	 */
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	/**
	 * @return the firstAuthorId
	 */
	public String getFirstAuthorId() {
		return firstAuthorId;
	}


	/**
	 * @param firstAuthorId the firstAuthorId to set
	 */
	public void setFirstAuthorId(String firstAuthorId) {
		this.firstAuthorId = firstAuthorId;
	}


	/**
	 * @return the record
	 */
	public Record getRecord() {
		return record;
	}


	/**
	 * @param record the record to set
	 */
	public void setRecord(Record record) {
		this.record = record;
	}


	/**
	 * @return the authorsInstitutions
	 */
	public String getAuthorsInstitutions() {
		return authorsInstitutions;
	}


	/**
	 * @param authorsInstitutions the authorsInstitutions to set
	 */
	public void setAuthorsInstitutions(String authorsInstitutions) {
		if(authorsInstitutions != null)
			this.authorsInstitutions = authorsInstitutions;
		else
			this.authorsInstitutions = "";
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		try{
			record.loadFromDatabase();
			String retVal = resultType + "," + Sanitizer.sanitizeCSV(result) + "," + Sanitizer.sanitizeCSV(record.getControlNumber()) + "," + Sanitizer.sanitizeCSV(record.getMARC21Record().toString());
			if(record.getDto() instanceof MonographDTO){
				MonographDTO dto= (MonographDTO) record.getDto();
				retVal += "," + Sanitizer.sanitizeCSV(dto.getSomeTitle()) + "," + Sanitizer.sanitizeCSV(dto.getKeywords().getContent()) + "," + Sanitizer.sanitizeCSV(dto.getAbstracT().getContent());
				for (AuthorDTO author : dto.getAllAuthors()) {
					retVal += "," + Sanitizer.sanitizeCSV(author.getControlNumber()) + "," + Sanitizer.sanitizeCSV(author.toString());
					author.setNotLoaded(true);
				}
				dto.setNotLoaded(true);
			}
			if(record.getDto() instanceof PaperJournalDTO){
				PaperJournalDTO dto= (PaperJournalDTO) record.getDto();
				retVal += "," + Sanitizer.sanitizeCSV(dto.getSomeTitle()) + "," + Sanitizer.sanitizeCSV(dto.getKeywords().getContent()) + "," + Sanitizer.sanitizeCSV(dto.getAbstracT().getContent());
				for (AuthorDTO author : dto.getAllAuthors()) {
					retVal += "," + Sanitizer.sanitizeCSV(author.getControlNumber()) + "," + Sanitizer.sanitizeCSV(author.toString());
					author.setNotLoaded(true);
				}
				dto.setNotLoaded(true);
			}
			if(record.getDto() instanceof PaperProceedingsDTO){
				PaperProceedingsDTO dto= (PaperProceedingsDTO) record.getDto();
				retVal += "," + Sanitizer.sanitizeCSV(dto.getSomeTitle()) + "," + Sanitizer.sanitizeCSV(dto.getKeywords().getContent()) + "," + Sanitizer.sanitizeCSV(dto.getAbstracT().getContent());
				for (AuthorDTO author : dto.getAllAuthors()) {
					retVal += "," + Sanitizer.sanitizeCSV(author.getControlNumber()) + "," + Sanitizer.sanitizeCSV(author.toString());
					author.setNotLoaded(true);
				}
				dto.setNotLoaded(true);
			}
			if(record.getDto() instanceof StudyFinalDocumentDTO){
				StudyFinalDocumentDTO dto= (StudyFinalDocumentDTO) record.getDto();
				retVal += "," + Sanitizer.sanitizeCSV(dto.getSomeTitle()) + "," + Sanitizer.sanitizeCSV(dto.getKeywords().getContent()) + "," + Sanitizer.sanitizeCSV(dto.getAbstracT().getContent());
				for (AuthorDTO author : dto.getAllAuthors()) {
					retVal += "," + Sanitizer.sanitizeCSV(author.getControlNumber()) + "," + Sanitizer.sanitizeCSV(author.toString());
					author.setNotLoaded(true);
				}
				dto.setNotLoaded(true);
			}
			if(record.getDto() instanceof PatentDTO){
				PatentDTO dto= (PatentDTO) record.getDto();
				retVal += "," + Sanitizer.sanitizeCSV(dto.getSomeTitle()) + "," + Sanitizer.sanitizeCSV(dto.getKeywords().getContent()) + "," + Sanitizer.sanitizeCSV(dto.getAbstracT().getContent());
				for (AuthorDTO author : dto.getAllAuthors()) {
					retVal += "," + Sanitizer.sanitizeCSV(author.getControlNumber()) + "," + Sanitizer.sanitizeCSV(author.toString());
					author.setNotLoaded(true);
				}
				dto.setNotLoaded(true);
			}
			if(record.getDto() instanceof ProductDTO){
				ProductDTO dto= (ProductDTO) record.getDto();
				retVal += "," + Sanitizer.sanitizeCSV(dto.getSomeName()) + "," + Sanitizer.sanitizeCSV(dto.getKeywords().getContent()) + ",";
				for (AuthorDTO author : dto.getAllAuthors()) {
					retVal += "," + Sanitizer.sanitizeCSV(author.getControlNumber()) + "," + Sanitizer.sanitizeCSV(author.toString());
					author.setNotLoaded(true);
				}
				dto.setNotLoaded(true);
			}
			record.clear();
			return retVal;
		} catch (Throwable e){
			e.printStackTrace();
			return null;
		}
	}
	
	

}
