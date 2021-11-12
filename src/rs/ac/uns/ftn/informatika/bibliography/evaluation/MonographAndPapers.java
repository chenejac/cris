/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperMonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.utils.Sanitizer;

/**
 * @author chenejac@uns.ac.rs
 *
 */
public class MonographAndPapers {
	
	List<PaperMonographDTO> papers;
	MonographDTO monograph;
	String classification = "";
	
	/**
	 * 
	 */
	public MonographAndPapers() {
		super();
	}

	/**
	 * @param monograph
	 * @param papers
	 */
	public MonographAndPapers(MonographDTO monograph, List<PaperMonographDTO> papers, String classification) {
		super();
		this.monograph = monograph;
		this.papers = papers;
		this.classification = classification;
	}

	/**
	 * @return the papers
	 */
	public List<PaperMonographDTO> getPapers() {
		return papers;
	}

	/**
	 * @param papers the papers to set
	 */
	public void setPapers(List<PaperMonographDTO> papers) {
		this.papers = papers;
	}
	
	/**
	 * @return the papers size
	 */
	public Integer getPapersSize() {
		return papers.size();
	}


	/**
	 * @return the monograph
	 */
	public MonographDTO getMonograph() {
		return monograph;
	}

	/**
	 * @param monograph the monograph to set
	 */
	public void setMonograph(MonographDTO monograph) {
		this.monograph = monograph;
	}


	/**
	 * @return the classification
	 */
	public String getClassification() {
		return classification;
	}

	/**
	 * @param classification the classification to set
	 */
	public void setClassification(String classification) {
		this.classification = classification;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer email = new StringBuffer("");
		boolean pmf = false;
		StringBuffer retVal = new StringBuffer("");
//		System.out.println(monograph.getControlNumber());
		for (PaperMonographDTO paper : papers) {
			retVal.append("ID rada," + paper.getControlNumber() + "\nRad u monografiji," + Sanitizer.sanitizeCSV(paper.toString()) + "\nBroj strana rada,");
			try{
				retVal.append(new Integer(Integer.parseInt(paper.getEndPage()) - Integer.parseInt(paper.getStartPage()) + 1).toString());
			} catch (Exception e) {
				
			}
			retVal.append("\nAutori,Broj autocitata iz grupe rezultata M20,Broj autocitata iz grupe rezultata M50\n");
			for (AuthorDTO authorDTO : paper.getAllAuthors()) {
				if((authorDTO.getEmail() != null) && (! authorDTO.getEmail().equals("proba1"))){
					if(! email.toString().contains(authorDTO.getEmail()))
						email.append(authorDTO.getEmail()+"; ");
				}
//				if((authorDTO.getInstitution().getControlNumber() != null) && (authorDTO.getInstitution().getControlNumber().equals("(BISIS)5933")))
				if((authorDTO.getInstitution().getControlNumber() != null) && (authorDTO.getInstitution().getControlNumber().equals("(BISIS)5929")))
//				if((authorDTO.getOrganizationUnit().getControlNumber() != null) && ((authorDTO.getOrganizationUnit().getControlNumber().equals("(BISIS)6782")) || ((authorDTO.getOrganizationUnit().getSuperOrganizationUnit() != null) && (authorDTO.getOrganizationUnit().getSuperOrganizationUnit().getControlNumber() != null) && (authorDTO.getOrganizationUnit().getSuperOrganizationUnit().getControlNumber().equals("(BISIS)6782")))))
					pmf = true;
				retVal.append(authorDTO.getName().toString()+",,,\n");
			}
			retVal.append("\n");
		}
		retVal.append("ID monografije," + monograph.getControlNumber() + "\n" +
				"Naslov," + Sanitizer.sanitizeCSV(monograph.getSomeTitle()) + "\n" + 
				"Jezik," + ((monograph.getTitle().getLanguage()!=null)?Sanitizer.sanitizeCSV(monograph.getTitle().getLanguage()):"") + "\n" +//((monograph.getTitle().getLanguage()!=null)?Sanitizer.sanitizeCSV(monograph.getTitle().getLanguage()):monograph.getTitleTranslations().get(0).getLanguage()) + "\n" +
				"ISBN," + ((monograph.getIsbn()!=null)?Sanitizer.sanitizeCSV(monograph.getIsbn()):"") + "\n" +
				"Broj str.," + ((monograph.getNumberOfPages()!=null)?monograph.getNumberOfPages().toString():"") + "\n" +
				"URL," + ((monograph.getUri()!=null)?Sanitizer.sanitizeCSV(monograph.getUri()):"") + "\n" +
				"Izdavac,"+((monograph.getPublisher().toString()!=null)?Sanitizer.sanitizeCSV(monograph.getPublisher().toString()):"") + "\n" + 
				"Godina,"+((monograph.getPublicationYear()!=null)?Sanitizer.sanitizeCSV(monograph.getPublicationYear().toString()):"") + "\n" + 
				 "Prikaz kriticki (navesti referencu)\nBroj Recenzenata\nAutori,");
		/* retVal.append("Monograph ID," + monograph.getControlNumber() + "\n" +
				"Title," + Sanitizer.sanitizeCSV(monograph.getSomeTitle()) + "\n" + 
				"Language," + ((monograph.getTitle().getLanguage()!=null)?Sanitizer.sanitizeCSV(monograph.getTitle().getLanguage()):"") + "\n" +//((monograph.getTitle().getLanguage()!=null)?Sanitizer.sanitizeCSV(monograph.getTitle().getLanguage()):monograph.getTitleTranslations().get(0).getLanguage()) + "\n" +
				"ISBN," + ((monograph.getIsbn()!=null)?Sanitizer.sanitizeCSV(monograph.getIsbn()):"") + "\n" +
				"Number of pages," + ((monograph.getNumberOfPages()!=null)?monograph.getNumberOfPages().toString():"") + "\n" +
				"URL," + ((monograph.getUri()!=null)?Sanitizer.sanitizeCSV(monograph.getUri()):"") + "\n" +
				"Publisher,"+((monograph.getPublisher().toString()!=null)?Sanitizer.sanitizeCSV(monograph.getPublisher().toString()):"") + "\n" + 
				"Year,"+((monograph.getPublicationYear()!=null)?Sanitizer.sanitizeCSV(monograph.getPublicationYear().toString()):"") + "\n" + 
				 "Critical Review published in journal (reference)\nNumber of peer reviewers\nAuthors,");
				*/
		for (AuthorDTO authorDTO : monograph.getAllAuthors()) {
			retVal.append(authorDTO.getName().toString()+"; ");
			
			if(authorDTO.getEmail()!=null)
				email.append(authorDTO.getEmail()+"; ");
//			if((authorDTO.getInstitution().getControlNumber() != null) && (authorDTO.getInstitution().getControlNumber().equals("(BISIS)5933")))
			if((authorDTO.getInstitution().getControlNumber() != null) && (authorDTO.getInstitution().getControlNumber().equals("(BISIS)5929")))
//			if((authorDTO.getOrganizationUnit().getControlNumber() != null) && ((authorDTO.getOrganizationUnit().getControlNumber().equals("(BISIS)6782")) || ((authorDTO.getOrganizationUnit().getSuperOrganizationUnit() != null) && (authorDTO.getOrganizationUnit().getSuperOrganizationUnit().getControlNumber() != null) && (authorDTO.getOrganizationUnit().getSuperOrganizationUnit().getControlNumber().equals("(BISIS)6782")))))
				pmf = true;
		}
		retVal.append("\nUrednici,");
//		retVal.append("\nEditors,");
		for (AuthorDTO authorDTO : monograph.getEditors()) {
			retVal.append(authorDTO.getName().toString()+"; ");
			
			if(authorDTO.getEmail()!=null)
				email.append(authorDTO.getEmail()+"; ");
//			if((authorDTO.getInstitution().getControlNumber() != null) && (authorDTO.getInstitution().getControlNumber().equals("(BISIS)5933")))
			if((authorDTO.getInstitution().getControlNumber() != null) && (authorDTO.getInstitution().getControlNumber().equals("(BISIS)5929")))
//			if((authorDTO.getOrganizationUnit().getControlNumber() != null) && ((authorDTO.getOrganizationUnit().getControlNumber().equals("(BISIS)6782")) || ((authorDTO.getOrganizationUnit().getSuperOrganizationUnit() != null) && (authorDTO.getOrganizationUnit().getSuperOrganizationUnit().getControlNumber() != null) && (authorDTO.getOrganizationUnit().getSuperOrganizationUnit().getControlNumber().equals("(BISIS)6782")))))
				pmf = true;
		}
		retVal.insert(0, "Email,"+email.toString()+"\n");
		retVal.append("\nVrednovanje," + classification);
//		retVal.append("\nEvaluation," + classification);
		retVal.append("\n,\n,");
		if(! pmf)
			return "";
		return retVal.toString();
	}

}
