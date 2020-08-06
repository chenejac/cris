package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorPosition;
import rs.ac.uns.ftn.informatika.bibliography.dto.PositionDTO;
import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;
import rs.ac.uns.ftn.informatika.bibliography.utils.ResourceBoundles;

/**
 * Managed bean with CRUD operations for author position
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class AuthorPositionManagedBean implements Serializable{

	protected FacesMessages facesMessages = new FacesMessages(
			new ResourceBoundles());

	protected Log log = LogFactory
		.getLog(AuthorPositionManagedBean.class.getName());
		
	private List<AuthorPosition> positions = null;

	private Date startDate = null;
	
	private Date endDate = null;
	
	private PositionDTO position = null;
	
	private String researchArea = null;
	
	protected int editMode = ModesManagedBean.MODE_NONE;		
	
	/**
	 * @return the positions
	 */
	public List<AuthorPosition> getPositions() {
		return positions;
	}

	/**
	 * @param positions the positions to set
	 */
	public void setPositions(List<AuthorPosition> positions) {
		this.positions = positions;
	}
	
	/**
	 * @return the researchArea
	 */
	public String getResearchArea() {
		return researchArea;
	}

	/**
	 * @param researchArea the researchArea to set
	 */
	public void setResearchArea(String researchArea) {
		this.researchArea = researchArea;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the position
	 */
	public PositionDTO getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(PositionDTO position) {
		this.position = position;
	}
	
	/**
	 * @return the editMode
	 */
	public int getEditMode() {
		return editMode;
	}

	/**
	 * @param editMode the editMode to set
	 */
	public void setEditMode(int editMode) {
		this.editMode = editMode;
	}

	public void exit(){
		editMode = ModesManagedBean.MODE_NONE;
	}	

	public void add() {
		AuthorPosition ap = new AuthorPosition();
		Calendar sd = new GregorianCalendar();
		sd.setTime(startDate);
		ap.setStartDate(sd);
		Calendar ed = new GregorianCalendar();
		if(endDate!=null)
			ed.setTime(endDate);
		else
			ed.setTime(startDate);
		ap.setEndDate(ed);
		ap.setPosition(position);
		ap.setResearchArea(researchArea);
		positions.add(ap);
		startDate = null;
		endDate = null;
		position = null;
	}

	public void remove() {
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String ordNum = facesCtx.getExternalContext()
					.getRequestParameterMap().get("ordNum");
			positions.remove(Integer.parseInt(ordNum));
		} catch (Exception e) {
		}
	}


}
