/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.dto.PaperProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.utils.Sanitizer;

/**
 * @author chenejac@uns.ac.rs
 *
 */
public class ProceedingsAndPapers {
	
	List<PaperProceedingsDTO> papers;
	ProceedingsDTO proceedings;
	
	/**
	 * 
	 */
	public ProceedingsAndPapers() {
		super();
	}

	/**
	 * @param proceedings
	 * @param papers
	 */
	public ProceedingsAndPapers(ProceedingsDTO proceedings, List<PaperProceedingsDTO> papers) {
		super();
		this.proceedings = proceedings;
		this.papers = papers;
	}

	/**
	 * @return the papers
	 */
	public List<PaperProceedingsDTO> getPapers() {
		return papers;
	}

	/**
	 * @param papers the papers to set
	 */
	public void setPapers(List<PaperProceedingsDTO> papers) {
		this.papers = papers;
	}

	/**
	 * @return the proceedings
	 */
	public ProceedingsDTO getProceedings() {
		return proceedings;
	}

	/**
	 * @param proceedings the proceedings to set
	 */
	public void setProceedings(ProceedingsDTO proceedings) {
		this.proceedings = proceedings;
	}


	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer(proceedings.getControlNumber() + "," + Sanitizer.sanitizeCSV(proceedings.getSomeTitle()) + "," + ((proceedings.getPublicationYear()!=null)?Sanitizer.sanitizeCSV(proceedings.getPublicationYear().toString()):"") + "," + ((proceedings.getUri()!=null)?Sanitizer.sanitizeCSV(proceedings.getUri()):"") + "," + ((proceedings.getTitle().getLanguage()!=null)?Sanitizer.sanitizeCSV(proceedings.getTitle().getLanguage()):proceedings.getTitleTranslations().get(0).getLanguage()));
		return retVal.toString();
	}

}
