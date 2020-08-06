package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.Serializable;

/**
 * DTO class which presents research title.
 *  
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class TitleDTO implements Serializable {

	private ClassDTO titleClass;
	private StudyFinalDocumentDTO diploma;
	
	public TitleDTO() {
		titleClass = new ClassDTO();
//		diploma = new StudyFinalDocumentDTO();
	}
	
	public TitleDTO(ClassDTO titleClass, StudyFinalDocumentDTO diploma) {
		this.titleClass = titleClass;
		this.diploma = diploma;
	}

	/**
	 * @return the titleClass
	 */
	public ClassDTO getTitleClass() {
		return titleClass;
	}

	/**
	 * @param titleClass the titleClass to set
	 */
	public void setTitleClass(ClassDTO titleClass) {
		this.titleClass = titleClass;
	}

	/**
	 * @return the diploma
	 */
	public StudyFinalDocumentDTO getDiploma() {
		return diploma;
	}

	/**
	 * @param diploma the diploma to set
	 */
	public void setDiploma(StudyFinalDocumentDTO diploma) {
		this.diploma = diploma;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		TitleDTO temp = (TitleDTO) arg0;
		if ((this.titleClass != null)
				&& (this.titleClass.equals(temp.titleClass)))
			return true;
		else
			return false;
	}

}
