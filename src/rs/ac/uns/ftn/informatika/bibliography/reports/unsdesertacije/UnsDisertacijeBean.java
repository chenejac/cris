package rs.ac.uns.ftn.informatika.bibliography.reports.unsdesertacije;

import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;

public class UnsDisertacijeBean {

	private String imeIPrezimeAutora;
	private String naslovDisertacije;
	private String mentor;
	private String godinaOdbrane;
	
	
	public UnsDisertacijeBean(){
		
	}
	
	public UnsDisertacijeBean(StudyFinalDocumentDTO disertacija){
		if(disertacija.getAuthor()!=null && disertacija.getAuthor().getName()!=null)
			imeIPrezimeAutora = disertacija.getAuthor().getName().toString();
		naslovDisertacije = disertacija.getSomeTitle();
		if(disertacija.getAdvisors()!=null && disertacija.getAdvisors().size()>0)
			mentor = "";
			for(AuthorDTO advisor:disertacija.getAdvisors()){
				if(disertacija.getAdvisors().indexOf(advisor)==0)
					mentor = mentor.concat("dr "+ advisor.getName().toString());
				else
					mentor = mentor.concat(", dr "+ advisor.getName().toString());
			}				
		godinaOdbrane = disertacija.getPublicationYear();		
	}
	
	
	/**
	 * @return the imeIPrezimeAutora
	 */
	public String getImeIPrezimeAutora() {
		return imeIPrezimeAutora;
	}
	/**
	 * @param imeIPrezimeAutora the imeIPrezimeAutora to set
	 */
	public void setImeIPrezimeAutora(String imeIPrezimeAutora) {
		this.imeIPrezimeAutora = imeIPrezimeAutora;
	}
	/**
	 * @return the naslovDisertacije
	 */
	public String getNaslovDisertacije() {
		return naslovDisertacije;
	}
	/**
	 * @param naslovDisertacije the naslovDisertacije to set
	 */
	public void setNaslovDisertacije(String naslovDisertacije) {
		this.naslovDisertacije = naslovDisertacije;
	}
	/**
	 * @return the mentor
	 */
	public String getMentor() {
		return mentor;
	}
	/**
	 * @param mentor the mentor to set
	 */
	public void setMentor(String mentor) {
		this.mentor = mentor;
	}
	/**
	 * @return the godinaOdbrane
	 */
	public String getGodinaOdbrane() {
		return godinaOdbrane;
	}
	/**
	 * @param godinaOdbrane the godinaOdbrane to set
	 */
	public void setGodinaOdbrane(String godinaOdbrane) {
		this.godinaOdbrane = godinaOdbrane;
	}

	
	
	
	
	
	
}
