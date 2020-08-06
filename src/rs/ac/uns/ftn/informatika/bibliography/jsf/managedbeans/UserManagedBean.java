package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.richfaces.component.UIDataTable;

import rs.ac.uns.ftn.informatika.bibliography.dao.UserDAO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorPosition;
import rs.ac.uns.ftn.informatika.bibliography.dto.EmailMessage;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PositionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.UserDTO;

/**
 * Managed bean with CRUD operations for users of system
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class UserManagedBean extends CRUDManagedBean implements IPickAuthorManagedBean, IPickPositionManagedBean, IPickResearchAreaManagedBean {

	private List<UserDTO> users;

	private UserDAO userDAO = new UserDAO();

	private UserDTO selectedUser = null;

	private boolean loggedIn = false;

	private UserDTO loggedUser = new UserDTO();
	
	private boolean jobAd = false;
	
	public UserManagedBean() {
		tableModalPanel = "userBrowseModalPanel";
		editModalPanel = "userEditModalPanel";
		Cookie[] cookies = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("language")) {
					setLocale(cookie.getValue());
				}
			}
		}
	}

	/**
	 * @return the map with privileges for currently logged in user
	 */
	public HashMap<String, HashMap<String, Boolean>> getPrivileges() {
		if (!loggedIn) {
			return PrivilegesManagedBean.getPrivileges().get("visitor");
		} else {
			return PrivilegesManagedBean.getPrivileges().get(loggedUser.getType());
		}
	}
	
	public boolean isMathematicDepartmant(){
		if (!loggedIn) {
			return false;
		} else {
			boolean retVal = false;
			if((loggedUser.getAuthor().getControlNumber()!=null)&&(loggedUser.getAuthor().getOrganizationUnit()!=null) && (loggedUser.getAuthor().getOrganizationUnit().getControlNumber()!=null) 
					&& (loggedUser.getAuthor().getOrganizationUnit().getControlNumber().equals("(BISIS)6782") ||
							loggedUser.getAuthor().getOrganizationUnit().getControlNumber().equals("(BISIS)6887") || 
							loggedUser.getAuthor().getOrganizationUnit().getControlNumber().equals("(BISIS)6888") || 
							loggedUser.getAuthor().getOrganizationUnit().getControlNumber().equals("(BISIS)6889") || 
							loggedUser.getAuthor().getOrganizationUnit().getControlNumber().equals("(BISIS)6890") ||
							loggedUser.getAuthor().getOrganizationUnit().getControlNumber().equals("(BISIS)6891") ||
							loggedUser.getAuthor().getOrganizationUnit().getControlNumber().equals("(BISIS)6892") ||
							loggedUser.getAuthor().getOrganizationUnit().getControlNumber().equals("(BISIS)6893") ||
							loggedUser.getAuthor().getOrganizationUnit().getControlNumber().equals("(BISIS)6894") ||
							loggedUser.getAuthor().getOrganizationUnit().getControlNumber().equals("(BISIS)6895")))
				retVal = true;
			return retVal;
		}
	}
	
	public boolean isFacultyOfSciences(){
		if (!loggedIn) {
			return false;
		} else {
			boolean retVal = false;
			if((loggedUser.getAuthor().getControlNumber()!=null)&&(loggedUser.getAuthor().getInstitution()!=null) && (loggedUser.getAuthor().getInstitution().getControlNumber()!=null) 
					&& (loggedUser.getAuthor().getInstitution().getControlNumber().equals("(BISIS)5929")))
				retVal = true;
			return retVal;
		}
	}
	
	
	/**
	 *  Sends email notification with details about account
	 */
	public void accountEmailNotification(){
		ResourceBundle rbAdministration = PropertyResourceBundle.getBundle(
				"messages.messages-administration", new Locale(getLanguage()));
		UserDTO u = userDAO.getUserByUsername(loggedUser.getEmail());
		if(u!=null){
			loggedUser.setPassword(u.getPassword());
			sendMessage(new EmailMessage(rbAdministration.getString("administration.email.cris"), loggedUser.getEmail(), null, null, rbAdministration.getString("administration.login.notification.subject"), rbAdministration.getString("administration.login.notification.textHeader") + "<br/><br/>" + rbAdministration.getString("administration.login.email") + ": " + loggedUser.getEmail() + "<br/>" + rbAdministration.getString("administration.login.password") + ": " + loggedUser.getPassword() + getEmailFooter()));
			facesMessages.addToControlFromResourceBundle(
					"login:general", FacesMessage.SEVERITY_INFO, 
					"administration.login.notification.success",
					FacesContext.getCurrentInstance());
		} else {
			facesMessages.addToControlFromResourceBundle(
					"login:general", FacesMessage.SEVERITY_ERROR, 
					"administration.login.notification.error",
					FacesContext.getCurrentInstance());
		}
	}
	
	/**
	 *  Sends email notification with account activation link
	 */
	public void activationLinkEmailNotification(){
		ResourceBundle rbAdministration = PropertyResourceBundle.getBundle(
				"messages.messages-administration", new Locale(getLanguage()));
		sendMessage(new EmailMessage(rbAdministration.getString("administration.email.cris"), selectedUser.getEmail(), null, null, rbAdministration.getString("administration.activation.notification.subject"), rbAdministration.getString("administration.activation.notification.textHeader") + " <a href=\"http://www.cris.uns.ac.rs/activation.jsf?activationCode=" + selectedUser.getActivationCode() + "&jobAd=" + jobAd + "\"/>http://www.cris.uns.ac.rs/activation.jsf?activationCode="+ selectedUser.getActivationCode() + "&jobAd=" + jobAd + "</a><br/><br/>"  + rbAdministration.getString("administration.activation.notification.textFooter")+ getEmailFooter()));
	}



	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		selectedUser = new UserDTO();
		newPassword = "";
		repeatedNewPassword = "";
		firstnameOtherFormat = "";
		lastnameOtherFormat = "";
		authorAlreadyRegisteredMessage = null;
		dateOfBirth = null;
		tableModalPanel = "userBrowseModalPanel";
		editModalPanel = "userEditModalPanel";
		loggedUser.getAuthor().setNotLoaded(true);
		return super.resetForm();
	}

	
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList() {
		debug("populateList");
		List<UserDTO> listTmp = userDAO.getUsers(whereStr, orderBy, direction);
		if (init == true && listTmp.size() != 0 && selectedUser != null
				&& selectedUser.getEmail() != null) {

			int index = -1;
			for (int i = 0; i < listTmp.size(); i++) {
				if (listTmp.get(i).getEmail().equals(selectedUser.getEmail())) {
					index = i;
				}
			}
			if (index != -1) {
				UIDataTable table = (UIDataTable)FacesContext.getCurrentInstance().getViewRoot().findComponent("userTable");
				if(table!=null){
					int page = index / table.getRows();
					table.setFirst(table.getRows()*page);
				}
			}
			init = false;
		}
		users = listTmp;
	}

	/**
	 * @return the list of users (filtered and ordered by ...)
	 */
	public List<UserDTO> getUsers() {
		if(populateList){
			populateList();
			populateList = false;
		}
		return users;
	}

	/**
	 * @return the selected user
	 */
	public UserDTO getSelectedUser() {
		return selectedUser;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		if ((selectedUser != null) && (selectedUser.getEmail() != null)) {
			super.switchToUpdateMode();
			firstnameOtherFormat = "";
			lastnameOtherFormat = "";
			newPassword = new String(selectedUser.getPassword());
			repeatedNewPassword = new String(selectedUser.getPassword());
			if(selectedUser.getAuthor().getDateOfBirth() != null)
				dateOfBirth = selectedUser.getAuthor().getDateOfBirth().getTime();
			debug("switchToUpdateMode: \n" + selectedUser);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	@Override
	public void switchToAddMode() {
		super.switchToAddMode();
		selectedUser = new UserDTO();
		firstnameOtherFormat = "";
		lastnameOtherFormat = "";
		newPassword = "";
		repeatedNewPassword = "";
		dateOfBirth = null;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToViewDetailsMode()
	 */
	@Override
	public void switchToViewDetailsMode() {
		if ((selectedUser != null) && (selectedUser.getEmail() != null)) {
			super.switchToViewDetailsMode();
			firstnameOtherFormat = "";
			lastnameOtherFormat = "";
			newPassword = new String(selectedUser.getPassword());
			repeatedNewPassword = new String(selectedUser.getPassword());
			debug("switchToViewDetailsMode: \n" + selectedUser);
		}
	}

	/**
	 * Switches form to registration mode
	 */
	public void switchToRegisterMode() {
		selectedUser = new UserDTO();
		if(jobAd)
			selectedUser.setType("applicant");
		else 
			selectedUser.setType("author");
		tableModalPanel = "";
		setTableMode(ModesManagedBean.MODE_NONE);
		editModalPanel = "";
		setEditMode(ModesManagedBean.MODE_REGISTER);
		pickAuthor();
		authorAlreadyRegisteredMessage = "administration.login.alreadyRegistered";
		newPassword = "";
		repeatedNewPassword = "";
		editTabNumber = 0;
		dateOfBirth = null;
		debug("switchToRegisterMode");
	}

	/**
	 * Switches form to change logged user basic data mode
	 */
	public void switchToChangeLoggedUserBasicDataMode() {
		editModalPanel = "";
		setTableMode(ModesManagedBean.MODE_NONE);
		setEditMode(ModesManagedBean.MODE_UPDATE_USER_BASIC_DATA);
		selectedUser = loggedUser;
		newPassword = new String(selectedUser.getPassword());
		repeatedNewPassword = new String(selectedUser.getPassword());
		setInstitution(selectedUser.getAuthor().getInstitution());
		editTabNumber = 0;
		if(selectedUser.getAuthor().getDateOfBirth() != null){
			dateOfBirth = selectedUser.getAuthor().getDateOfBirth().getTime();
		}
		debug("switchToChangeLoggedUserDataMode: \n" + selectedUser);
	}
	
	/**
	 * Switches form to change logged user advanced data mode
	 */
	public void switchToChangeLoggedUserAdvancedDataMode() {
		editModalPanel = "";
		setTableMode(ModesManagedBean.MODE_NONE);
		setEditMode(ModesManagedBean.MODE_UPDATE_USER_ADVANCED_DATA);
		selectedUser = loggedUser;
		newPassword = new String(selectedUser.getPassword());
		repeatedNewPassword = new String(selectedUser.getPassword());
		editTabNumber = 1;
		debug("switchToChangeLoggedUserDataMode: \n" + selectedUser);
	}
	
	/**
	 * Switches form to change password
	 */
	public void switchToChangePassword() {
		editModalPanel = "";
		setTableMode(ModesManagedBean.MODE_NONE);
		setEditMode(ModesManagedBean.MODE_CHANGE_PASSWORD);
		selectedUser = loggedUser;
		oldPassword = "";
		newPassword = "";
		repeatedNewPassword = "";
		editTabNumber = 0;
		debug("switchToChangePassword: \n" + selectedUser);
	}

	/**
	 * Switches form to add format name mode
	 */
	public void switchToAddFormatNameMode() {
		editModalPanel = "";
		setTableMode(ModesManagedBean.MODE_NONE);
		setEditMode(ModesManagedBean.MODE_ADD_FORMAT_NAME);
		selectedUser = loggedUser;
		newPassword = new String(selectedUser.getPassword());
		repeatedNewPassword = new String(selectedUser.getPassword());
		editTabNumber = 0;
		debug("switchToAddFormatNameMode: \n" + selectedUser);
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#update()
	 */
	@Override
	public void update() {
		String formName = "userEditForm";
		if (editMode == ModesManagedBean.MODE_UPDATE_USER_BASIC_DATA)
			formName = "userEditBasicDataForm";
		else if (editMode == ModesManagedBean.MODE_UPDATE_USER_ADVANCED_DATA)
				formName = "userEditAdvancedDataForm";
			else if (editMode == ModesManagedBean.MODE_ADD_FORMAT_NAME)
					formName = "userEditFormatNamesForm";
					
		if (newPassword.length() < 6) {
			facesMessages.addToControlFromResourceBundle(
					formName + ":newPassword", FacesMessage.SEVERITY_ERROR, 
					"administration.changePassword.shortNewPassword",
					FacesContext.getCurrentInstance());
			return;
		}
		if (!newPassword.equals(repeatedNewPassword)) {
			facesMessages
					.addToControlFromResourceBundle(
							formName + ":repeatedNewPassword", FacesMessage.SEVERITY_ERROR, 
							"administration.changePassword.repeatedNewPasswordDifferentFromNewPassword",
							FacesContext.getCurrentInstance());
			return;
		}
		selectedUser.setPassword(newPassword);
		if((selectedUser.getAuthor().getControlNumber() != null) && ("userEditBasicDataForm".equals(formName))){
			Calendar dob = new GregorianCalendar();
			dob.setTime(dateOfBirth);
			selectedUser.getAuthor().setDateOfBirth(dob);
		}
		if (userDAO.update(selectedUser) == false) {
			facesMessages.addToControlFromResourceBundle(
					formName + ":general", FacesMessage.SEVERITY_ERROR, 
					"administration.user.update.error",
					FacesContext.getCurrentInstance());
		} else {
			if (selectedUser == loggedUser)
				setLocale(loggedUser.getLanguage());
			facesMessages.addToControlFromResourceBundle(
					formName + ":general", FacesMessage.SEVERITY_INFO, 
					"administration.user.update.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			debug("updated: \n" + selectedUser);
			sendRecordMessage(selectedUser.getAuthor(), "update");
		}
		nextEditTab();
		nextEditTab();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#add()
	 */
	@Override
	public void add() {
		if (newPassword.length() < 6) {
			facesMessages.addToControlFromResourceBundle(
					"userEditBasicDataForm:newPassword", FacesMessage.SEVERITY_ERROR, 
					"administration.changePassword.shortNewPassword",
					FacesContext.getCurrentInstance());
			return;
		}
		if (!newPassword.equals(repeatedNewPassword)) {
			facesMessages
					.addToControlFromResourceBundle(
							"userEditBasicDataForm:repeatedNewPassword", FacesMessage.SEVERITY_ERROR, 
							"administration.changePassword.repeatedNewPasswordDifferentFromNewPassword",
							FacesContext.getCurrentInstance());
			return;
		}
		selectedUser.setPassword(newPassword);
		if(selectedUser.getAuthor().getControlNumber() != null){
			Calendar dob = new GregorianCalendar();
			dob.setTime(dateOfBirth);
			selectedUser.getAuthor().setDateOfBirth(dob);
		}
		if (userDAO.add(selectedUser) == false) {
			facesMessages.addToControlFromResourceBundle(
					"userEditBasicDataForm:general", FacesMessage.SEVERITY_ERROR, 
					"administration.user.add.error",
					FacesContext.getCurrentInstance());
		} else {
			facesMessages.addToControlFromResourceBundle(
					"userEditBasicDataForm:general", FacesMessage.SEVERITY_INFO,  
					"administration.user.add.success",
					FacesContext.getCurrentInstance());
			populateList = true;
			if(selectedUser.getAuthor() != null){
				sendRecordMessage(selectedUser.getAuthor(), "update");
			}
			debug("added: \n" + selectedUser);
		}
		nextEditTab();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#delete()
	 */
	@Override
	public void delete() {
		if (userDAO.delete(selectedUser) == false) {
			facesMessages.addToControlFromResourceBundle(
					"userTableForm:general", FacesMessage.SEVERITY_ERROR, 
					"administration.user.delete.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("deleted: \n" + selectedUser);
			selectedUser = null;
			populateList = true;
		}
	}

	/**
	 * Prepares web form where user can choose author connected to selected user
	 */
	public void pickAuthor() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		AuthorManagedBean mb = (AuthorManagedBean) extCtx.getSessionMap().get(
				"authorManagedBean");
		if (mb == null) {
			mb = new AuthorManagedBean();
			extCtx.getSessionMap().put("authorManagedBean", mb);
		}

		mb.setIPickAuthorManagedBean(this);
		mb.setSelectedAuthor(null);
		mb.setPickMessageFirstTab("administration.user.pickAuthorMessageFirstTab");
		mb.setPickMessageSecondTabSimilarNotExistFirstSentence("administration.user.pickAuthorMessageSecondTabSimilarNotExistFirstSentence");
		mb.setPickMessageSecondTabSimilarNotExistSecondSentence("administration.user.pickAuthorMessageSecondTabSimilarNotExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistFirstSentence("administration.user.pickAuthorMessageSecondTabSimilarExistFirstSentence");
		mb.setPickMessageSecondTabSimilarExistSecondSentence("administration.user.pickAuthorMessageSecondTabSimilarExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistThirdSentence("administration.user.pickAuthorMessageSecondTabSimilarExistThirdSentence");
		mb.setPickMessageSecondTabSimilarExistFourthSentence("administration.user.pickAuthorMessageSecondTabSimilarExistFourthSentence");
		mb.setCustomPick(true);
		if(editMode == ModesManagedBean.MODE_REGISTER){
			mb.setTableModalPanel("");
		}
		mb.switchToPickMode();
		if ((selectedUser.getAuthor().getControlNumber() != null)
				&& (mb.isCustomPick())) {
			mb.setFirstnameOtherFormat(selectedUser.getAuthor().getName()
					.getFirstname());
			mb.setLastnameOtherFormat(selectedUser.getAuthor().getName()
					.getLastname());
		}
	}

	/**
	 * Disconnects author from selected user
	 */
	public void setNullAuthor() {
		selectedUser.setAuthor(new AuthorDTO());
	}

	private String authorAlreadyRegisteredMessage = null;
	
	/**
	 * @return the authorAlreadyRegisteredMessage
	 */
	public String getAuthorAlreadyRegisteredMessage() {
		return facesMessages.getMessageFromResourceBundle(authorAlreadyRegisteredMessage);
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickAuthorManagedBean#setAuthor(rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO)
	 */
	public void setAuthor(AuthorDTO author) {
		if ((editMode == ModesManagedBean.MODE_REGISTER) && (author.isAlreadyRegistered())) {
			try {
				UserDTO u = userDAO.getUserByAuthorControlNumber(author.getControlNumber());
				loggedUser.setEmail(u.getEmail());
				loggedUser.setPassword("");
				FacesContext.getCurrentInstance().getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				selectedUser.setAuthor(author);
				setInstitution(author.getInstitution());
				if(author.getDateOfBirth() != null)
					dateOfBirth = author.getDateOfBirth().getTime();
			}
		} else {
			selectedUser.setAuthor(author);
			setInstitution(author.getInstitution());
			if(author.getDateOfBirth() != null)
				dateOfBirth = author.getDateOfBirth().getTime();
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickAuthorManagedBean#cancelPickingAuthor()
	 */
	@Override
	public void cancelPickingAuthor() {
	}


	/**
	 * Prepares web form where user can choose position of author connected to selected user
	 */
	public void pickPosition() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		PositionManagedBean mb = (PositionManagedBean) extCtx.getSessionMap().get(
				"positionManagedBean");
		if (mb == null) {
			mb = new PositionManagedBean();
			extCtx.getSessionMap().put("positionManagedBean", mb);
		}

		mb.setIPickPositionManagedBean(this);
		mb.setSelectedPosition(null);
		mb.setPickMessage("administration.user.pickAuthorMessageFirstTab");
		mb.setCustomPick(false);
		mb.switchToPickMode();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickPositionManagedBean#setPosition(rs.ac.uns.ftn.informatika.bibliography.dto.PositionDTO)
	 */
	@Override
	public void setPosition(PositionDTO position) {
		selectedUser.getAuthor().getCurrentPosition().setPosition(position);
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickPositionManagedBean#cancelPickingPosition()
	 */
	@Override
	public void cancelPickingPosition() {
		
		
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#getUserManagedBean()
	 */
	@Override
	public UserManagedBean getUserManagedBean() {
		return this;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#dispetcher(int)
	 */
	@Override
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "indexPage";
		if((retVal != null) && (retVal.equals("indexPage")) && (getUserManagedBean().getJobAd()))
			return "jobAdsIndexPage";
		else 
			return retVal;
	}

	/**
	 * Resets all forms
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public void resetAllForms() {
		this.resetForm();
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		CRUDManagedBean mb = (AuthorManagedBean) extCtx.getSessionMap().get(
				"authorManagedBean");
		if (mb == null) {
			mb = new AuthorManagedBean();
			extCtx.getSessionMap().put("authorManagedBean", mb);
		}
		mb.resetForm();

		mb = (ConferenceManagedBean) extCtx.getSessionMap().get(
				"conferenceManagedBean");
		if (mb == null) {
			mb = new ConferenceManagedBean();
			extCtx.getSessionMap().put("conferenceManagedBean", mb);
		}
		mb.resetForm();

		mb = (ProceedingsManagedBean) extCtx.getSessionMap().get(
				"proceedingsManagedBean");
		if (mb == null) {
			mb = new ProceedingsManagedBean();
			extCtx.getSessionMap().put("proceedingsManagedBean", mb);
		}
		mb.resetForm();

		mb = (PaperProceedingsManagedBean) extCtx.getSessionMap().get(
				"paperProceedingsManagedBean");
		if (mb == null) {
			mb = new PaperProceedingsManagedBean();
			extCtx.getSessionMap().put("paperProceedingsManagedBean", mb);
		}
		mb.resetForm();
		
		mb = (JournalManagedBean) extCtx.getSessionMap().get(
				"journalManagedBean");
		if (mb == null) {
			mb = new JournalManagedBean();
			extCtx.getSessionMap().put("journalManagedBean", mb);
		}
		mb.resetForm();
		
		mb = (PaperJournalManagedBean) extCtx.getSessionMap().get(
				"paperJournalManagedBean");
		if (mb == null) {
			mb = new PaperJournalManagedBean();
			extCtx.getSessionMap().put("paperJournalManagedBean", mb);
		}
		mb.resetForm();

		mb = (MonographManagedBean) extCtx.getSessionMap().get(
				"monographManagedBean");
		if (mb == null) {
			mb = new MonographManagedBean();
			extCtx.getSessionMap().put("monographManagedBean", mb);
		}
		mb.resetForm();
		
		mb = (PaperMonographManagedBean) extCtx.getSessionMap().get(
				"paperMonographManagedBean");
		if (mb == null) {
			mb = new PaperMonographManagedBean();
			extCtx.getSessionMap().put("paperMonographManagedBean", mb);
		}
		mb.resetForm();
		
		mb = (BibliographyManagedBean) extCtx.getSessionMap().get(
				"bibliographyManagedBean");
		if (mb == null) {
			mb = new BibliographyManagedBean();
			extCtx.getSessionMap().put("bibliographyManagedBean", mb);
		}
		mb.resetForm();
		
		mb = (InstitutionManagedBean) extCtx.getSessionMap().get(
				"institutionManagedBean");
		if (mb == null) {
			mb = new InstitutionManagedBean();
			extCtx.getSessionMap().put("institutionManagedBean", mb);
		}
		mb.resetForm();
		
		mb = (ResearchAreaManagedBean) extCtx.getSessionMap().get(
				"researchAreaManagedBean");
		if (mb == null) {
			mb = new ResearchAreaManagedBean();
			extCtx.getSessionMap().put("researchAreaManagedBean", mb);
		}
		mb.resetForm();
		
		mb = (PatentManagedBean) extCtx.getSessionMap().get(
			"patentManagedBean");
		if (mb == null) {
			mb = new PatentManagedBean();
			extCtx.getSessionMap().put("patentManagedBean", mb);
		}
		mb.resetForm();
		
		mb = (ProductManagedBean) extCtx.getSessionMap().get(
			"productManagedBean");
		if (mb == null) {
			mb = new ProductManagedBean();
			extCtx.getSessionMap().put("productManagedBean", mb);
		}
		mb.resetForm();

		mb = (PositionManagedBean) extCtx.getSessionMap().get(
			"positionManagedBean");
		if (mb == null) {
			mb = new PositionManagedBean();
			extCtx.getSessionMap().put("positionManagedBean", mb);
		}
		mb.resetForm();
		
		//proveri da li je potrebno da se ubaci za SciencesGroup, ResultTypeGroup, ResultType, RuleBook, RuleBookImplementation
	}

	/**
	 * @return true if user is logged in, else false
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * @param loggedIn
	 *            the logged in status to set
	 */
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	/**
	 * @return the outcome string for JSF navigation
	 */
	public String loginPageEnter() {
		return "loginPage";
	}
	
	public void loginPageEnter(PhaseEvent event) {
	}

	/**
	 * Logs in user.
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String login() {
		if (loggedIn) {
			return dispetcher(0);
		}
		try {
			UserDTO u = userDAO.getUserByUsernameAndPassword(loggedUser
					.getEmail(), loggedUser.getPassword());
			if (u == null) {
				loggedIn = false;
				facesMessages.addToControlFromResourceBundle(
						"login:general", FacesMessage.SEVERITY_ERROR, 
 						"administration.login.badUsernamePasswordCombination",
						FacesContext.getCurrentInstance());
				return null;
			}
			loggedUser = u;
			loggedIn = true;
			setLocale(loggedUser.getLanguage());
			debug("logged in");
			FacesContext fc = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) fc.getExternalContext().getSession(
					false);
			session.setMaxInactiveInterval(2*60*60);
			if((loggedUser.getAuthor()!=null) && (loggedUser.getAuthor().getControlNumber()!=null)){
				HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
				Cookie userCookie = null;
				if (request.getCookies() != null) {
		            for (Cookie cookie : request.getCookies()) {
		                if (cookie.getName().equals("userId")) {
		                	userCookie = cookie;
		                	break;
		                }
		            }
		        }
				if(userCookie == null)
					userCookie = new Cookie("userId", loggedUser.getAuthor().getControlNumber());
				else 
					userCookie.setValue(loggedUser.getAuthor().getControlNumber() + " [" + userCookie.getValue() + "]");
				userCookie.setMaxAge(60*60*24*365*10); //Store cookie for 10 years
				userCookie.setPath("/");
				((HttpServletResponse) FacesContext.getCurrentInstance()
					.getExternalContext().getResponse()).addCookie(userCookie);
			}
			
			return dispetcher(0);
		} catch (Exception ex) {
			loggedIn = false;
			loggedUser = null;
			facesMessages.addToControlFromResourceBundle(
					"login:general", FacesMessage.SEVERITY_ERROR, 
					"administration.login.error", FacesContext
							.getCurrentInstance());
			return null;
		} catch (Throwable e) {
			loggedIn = false;
			loggedUser = null;
			error("login", e);
			return null;
		}
	}

	/**
	 * Logs out user.
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String logout() {
		debug("logged out");
		loggedIn = false;
		loggedUser = new UserDTO();
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(
				false);
		session.invalidate();
		ExternalContext extCtx = fc.getExternalContext();
		UserManagedBean mb = new UserManagedBean();
		mb.setJobAd(this.jobAd);
		extCtx.getSessionMap().put("userManagedBean", mb);
		returnPage = "indexPage";
		return dispetcher(0);
	}


	/**
	 * Prepares registration page
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String registrationPageEnter() {
		resetAllForms();
		switchToRegisterMode();
		returnPage = "indexPage";
		return "registrationPage";
	}
	
	/**
	 * Prepares registration page
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String registrationCommissionPageEnter() {
		resetAllForms();
		switchToRegisterMode();
		returnPage = "indexPage";
		return "registrationPage";
	}

	/**
	 * Registers user
	 */
	public void register() {
		if (newPassword.length() < 6) {
			facesMessages.addToControlFromResourceBundle(
					"userEditBasicDataForm:newPassword", FacesMessage.SEVERITY_ERROR, 
					"administration.changePassword.shortNewPassword",
					FacesContext.getCurrentInstance());
			return;
		}
		if (!newPassword.equals(repeatedNewPassword)) {
			facesMessages.addToControlFromResourceBundle(
							"userEditBasicDataForm:repeatedNewPassword", FacesMessage.SEVERITY_ERROR, 
							"administration.changePassword.repeatedNewPasswordDifferentFromNewPassword",
							FacesContext.getCurrentInstance());
			return;
		}

		selectedUser.setPassword(newPassword);
		if(selectedUser.getAuthor() != null){
			selectedUser.getAuthor().setAlreadyRegistered(true);
			selectedUser.setActivationCode(UUID.randomUUID().toString());
			Calendar dob = new GregorianCalendar();
			dob.setTime(dateOfBirth);
			selectedUser.getAuthor().setDateOfBirth(dob);
		} 
		if (userDAO.add(selectedUser) == false) {
			facesMessages.addToControlFromResourceBundle(
					"userEditBasicDataForm:general", FacesMessage.SEVERITY_ERROR, 
					"administration.user.registration.error", FacesContext
							.getCurrentInstance());
			return;
		} else {
			facesMessages.addToControlFromResourceBundle(
					"userEditBasicDataForm:general", FacesMessage.SEVERITY_INFO, 
					"administration.user.registration.success", FacesContext
							.getCurrentInstance());
			debug("registered: " + selectedUser);
			if(selectedUser.getAuthor() != null){
				activationLinkEmailNotification();
				sendRecordMessage(selectedUser.getAuthor(), "update");
			}
		}
		nextEditTab();
	}
	
	/**
	 * Performs the account activation
	 */
	public void performActivation(PhaseEvent event){
		String activationCode = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("activationCode");
		String jobAdString = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("jobAd");
		selectedUser = userDAO.activateAccount(activationCode);
		if(selectedUser != null){
			loggedUser = selectedUser;
			loggedIn = true;
			setLocale(loggedUser.getLanguage());
		}
		if((jobAdString != null) && (jobAdString.equals("true"))){
			jobAd = true;
		}
		
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#finishWizard()
	 */
	public String finishWizard() {
		return exit();
	}

	// -------------------- metode za promenu lozinke ------------------
	private String oldPassword;
	private String newPassword;
	private String repeatedNewPassword;

	/**
	 * @return the old password
	 */
	public String getOldPassword() {
		return oldPassword;
	}

	/**
	 * @param oldPassword
	 *            the old password to set
	 */
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	/**
	 * @return the new password
	 */
	public String getNewPassword() {
		return newPassword;
	}

	/**
	 * @param newPassword
	 *            the new password to set
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	/**
	 * @return the repeated new password
	 */
	public String getRepeatedNewPassword() {
		return repeatedNewPassword;
	}

	/**
	 * @param repeatedNewPassword
	 *            the repeated new password to set
	 */
	public void setRepeatedNewPassword(String repeatedNewPassword) {
		this.repeatedNewPassword = repeatedNewPassword;
	}

	/**
	 * Prepares page for changing password
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String changePasswordPageEnter() {
		resetAllForms();
		switchToChangePassword();
		returnPage = "indexPage";
		return "changePasswordPage";
	}

	/**
	 * Changes password of the currently logged in user
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String changePassword() {
		if (!oldPassword.equals(loggedUser.getPassword())) {
			facesMessages.addToControlFromResourceBundle(
					"form:oldPassword", FacesMessage.SEVERITY_ERROR, 
					"administration.changePassword.badPassword", FacesContext
							.getCurrentInstance());
			return null;
		}
		if (newPassword.length() < 6) {
			facesMessages.addToControlFromResourceBundle(
					"form:newPassword", FacesMessage.SEVERITY_ERROR, 
					"administration.changePassword.shortNewPassword",
					FacesContext.getCurrentInstance());
			return null;
		}
		if (!newPassword.equals(repeatedNewPassword)) {
			facesMessages.addToControlFromResourceBundle(
					"form:repeatedNewPassword", FacesMessage.SEVERITY_ERROR, 
					"administration.changePassword.repeatedNewPasswordDifferentFromNewPassword",
					FacesContext.getCurrentInstance());
			return null;
		}

		loggedUser.setPassword(newPassword);

		if (userDAO.changePassword(loggedUser)) {
			facesMessages.addToControlFromResourceBundle(
					"form:general", FacesMessage.SEVERITY_INFO, 
					"administration.changePassword.passwordChanged",
					FacesContext.getCurrentInstance());
			debug("password changed: " + loggedUser);
		} else {
			loggedUser.setPassword(oldPassword);
			facesMessages.addToControlFromResourceBundle(
					"form:general", FacesMessage.SEVERITY_ERROR, 
					"administration.changePassword.passwordNotChanged",
					FacesContext.getCurrentInstance());
		}
		return null;

	}
	
	public String coauthorsPageEnter(){
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		AuthorManagedBean mb = (AuthorManagedBean) extCtx.getSessionMap().get(
				"authorManagedBean");
		if (mb == null) {
			mb = new AuthorManagedBean();
			extCtx.getSessionMap().put("authorManagedBean", mb);
		}
		resetAllForms();
		mb.resetForm();
		mb.switchToBrowseMode();
		if(loggedUser.getAuthor().getControlNumber()!=null)
			mb.setCoauthor(loggedUser.getAuthor());
		return "coauthorsPage";
	}

	/**
	 * @return the currently logged in user
	 */
	public UserDTO getLoggedUser() {
		return loggedUser;
	}

	/**
	 * @param loggedUser the loggedUser to set
	 */
	public void setLoggedUser(UserDTO loggedUser) {
		this.loggedUser = loggedUser;
	}

	/**
	 * Prepares page for changing logged user basic data
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String changeLoggedUserBasicDataPageEnter() {
		resetAllForms();
		switchToChangeLoggedUserBasicDataMode();
		returnPage = "indexPage";
		return "changeLoggedUserBasicDataPage";
	}
	
	/**
	 * Prepares page for changing logged user advanced data
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String changeLoggedUserAdvancedDataPageEnter() {
		resetAllForms();
		switchToChangeLoggedUserAdvancedDataMode();
		returnPage = "indexPage";
		return "changeLoggedUserAdvancedDataPage";
	}

	/**
	 * Prepares page for adding other format name for currently logged in user
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String addLoggedUserFormatNamePageEnter() {
		resetAllForms();
		switchToAddFormatNameMode();
		returnPage = "indexPage";
		return "addLoggedUserFormatNamePage";
	}

	private String firstnameOtherFormat;

	private String lastnameOtherFormat;

	/**
	 * @return the first name other format
	 */
	public String getFirstnameOtherFormat() {
		return firstnameOtherFormat;
	}

	/**
	 * @param firstnameOtherFormat
	 *            the first name other format to set
	 */
	public void setFirstnameOtherFormat(String firstnameOtherFormat) {
		this.firstnameOtherFormat = firstnameOtherFormat;
	}

	/**
	 * @return the last name other format
	 */
	public String getLastnameOtherFormat() {
		return lastnameOtherFormat;
	}

	/**
	 * @param lastnameOtherFormat
	 *            the last name other format to set
	 */
	public void setLastnameOtherFormat(String lastnameOtherFormat) {
		this.lastnameOtherFormat = lastnameOtherFormat;
	}

	/**
	 * Adds the new format of personal name for author connected to selected
	 * user
	 */
	public void addAuthorName() {
		if ((!("".equals(firstnameOtherFormat)))
				&& (!("".equals(lastnameOtherFormat)))) {
			if (("".equals(selectedUser.getAuthor().getName().getFirstname())))
				selectedUser.getAuthor().setName(
						new AuthorNameDTO(firstnameOtherFormat,
								lastnameOtherFormat, ""));
			else
				selectedUser.getAuthor().getOtherFormatNames().add(
						new AuthorNameDTO(firstnameOtherFormat,
								lastnameOtherFormat, ""));
			firstnameOtherFormat = "";
			lastnameOtherFormat = "";
		} else {
			facesMessages.addToControlFromResourceBundle(
					"userEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"administration.user.author.addName.error", FacesContext
							.getCurrentInstance());
		}

	}

	/**
	 * Removes the existing format of personal name for author connected to
	 * selected user
	 */
	public void removeAuthorName() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();

		String firstname = facesCtx.getExternalContext()
				.getRequestParameterMap().get("firstname");
		String lastname = facesCtx.getExternalContext()
				.getRequestParameterMap().get("lastname");

		int index = -1;
		AuthorNameDTO authorNameDTO = null;
		for (int i = 0; i < selectedUser.getAuthor().getOtherFormatNames()
				.size(); i++) {
			authorNameDTO = selectedUser.getAuthor().getOtherFormatNames().get(
					i);
			if ((authorNameDTO.getFirstname().equals(firstname))
					&& (authorNameDTO.getLastname().equals(lastname))) {
				index = i;
				break;
			}
		}
		if (index != -1)
			selectedUser.getAuthor().getOtherFormatNames().remove(index);
	}

	/**
	 * Switches the selected format of personal name with previous
	 */
	public void moveAuthorNameUp() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();

		String firstname = facesCtx.getExternalContext()
				.getRequestParameterMap().get("firstname");
		String lastname = facesCtx.getExternalContext()
				.getRequestParameterMap().get("lastname");

		int index = -1;
		AuthorNameDTO authorNameDTO = null;
		for (int i = 0; i < selectedUser.getAuthor().getOtherFormatNames()
				.size(); i++) {
			authorNameDTO = selectedUser.getAuthor().getOtherFormatNames().get(
					i);
			if ((authorNameDTO.getFirstname().equals(firstname))
					&& (authorNameDTO.getLastname().equals(lastname))) {
				index = i;
				break;
			}
		}
		switch (index) {
			case -1:
				switchNames(index, selectedUser.getAuthor()
						.getOtherFormatNames().size() - 1);
				break;
			default:
				switchNames(index, index - 1);
				break;
		}

	}

	/**
	 * Switches the selected format of personal name with next
	 */
	public void moveAuthorNameDown() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();

		String firstname = facesCtx.getExternalContext()
				.getRequestParameterMap().get("firstname");
		String lastname = facesCtx.getExternalContext()
				.getRequestParameterMap().get("lastname");

		int index = -1;
		AuthorNameDTO authorNameDTO = null;
		for (int i = 0; i < selectedUser.getAuthor().getOtherFormatNames()
				.size(); i++) {
			authorNameDTO = selectedUser.getAuthor().getOtherFormatNames().get(
					i);
			if ((authorNameDTO.getFirstname().equals(firstname))
					&& (authorNameDTO.getLastname().equals(lastname))) {
				index = i;
				break;
			}
		}
		switch (index) {
			default:
				switchNames(index, ((index + 1) == selectedUser.getAuthor()
						.getOtherFormatNames().size()) ? (-1) : (index + 1));
				break;
		}

	}

	private void switchNames(int firstIndex, int secondIndex) {
		AuthorNameDTO first = (firstIndex == -1) ? selectedUser.getAuthor()
				.getName() : selectedUser.getAuthor().getOtherFormatNames()
				.get(firstIndex);
		AuthorNameDTO second = (secondIndex == -1) ? selectedUser.getAuthor()
				.getName() : selectedUser.getAuthor().getOtherFormatNames()
				.get(secondIndex);
		if ((firstIndex == -1) || (secondIndex == -1)) {
			if (firstIndex == -1)
				selectedUser.getAuthor().setName(second);
			else
				selectedUser.getAuthor().setName(first);
		} else {
			selectedUser.getAuthor().getOtherFormatNames().set(firstIndex,
					second);
			selectedUser.getAuthor().getOtherFormatNames().set(secondIndex,
					first);
		}
	}

	/**
	 * Switches interface to English language
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String englishLanguage() {
		setLocale("en");
		return null;
	}

	/**
	 * Switches interface to Serbian language
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String serbianLanguage() {
		setLocale("sr");
		return null;
	}

	private String language = "sr";

	/**
	 * @return the current language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLocale(String language) {
		Calendar date = new GregorianCalendar();
		date.set(GregorianCalendar.YEAR, new GregorianCalendar().get(GregorianCalendar.YEAR)+1);
		((HttpServletResponse) FacesContext.getCurrentInstance()
				.getExternalContext().getResponse()).addHeader("Set-Cookie",
				"language=" + language + ";HttpOnly;expires=" + date.getTime());
		FacesContext.getCurrentInstance().getViewRoot().setLocale(
				new Locale(language));
		this.language = language;
	}
	
	public void biographyTranslations(){
		int mode = editMode;
		if(mode == ModesManagedBean.MODE_REGISTER)
			mode = ModesManagedBean.MODE_ADD;
		else if ((mode == ModesManagedBean.MODE_UPDATE_USER_BASIC_DATA) || (mode == ModesManagedBean.MODE_UPDATE_USER_ADVANCED_DATA))
			mode = ModesManagedBean.MODE_UPDATE;
		this.openMultilingualContentForm(mode, selectedUser.getAuthor().getBiographyTranslations(), true, "administration.user.editPanel.biographyTranslations.panelHeader", "administration.user.editPanel.biographyTranslations.contentHeader");
	}
	
	public void keywordsTranslations(){
		int mode = editMode;
		if(mode == ModesManagedBean.MODE_REGISTER)
			mode = ModesManagedBean.MODE_ADD;
		else if ((mode == ModesManagedBean.MODE_UPDATE_USER_BASIC_DATA) || (mode == ModesManagedBean.MODE_UPDATE_USER_ADVANCED_DATA))
			mode = ModesManagedBean.MODE_UPDATE;
		this.openMultilingualContentForm(mode, selectedUser.getAuthor().getKeywordsTranslations(), true, "administration.user.editPanel.keywordsTranslations.panelHeader", "administration.user.editPanel.keywordsTranslations.contentHeader");
	}

	
	public InstitutionDTO getInstitution(){
		return selectedUser.getAuthor().getInstitution();
	}
	
	public void setInstitution(InstitutionDTO institution){
		selectedUser.getAuthor().setInstitution(institution);
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
			
		OrganizationUnitManagedBean mb = (OrganizationUnitManagedBean) extCtx.getSessionMap().get(
			"organizationUnitManagedBean");
		if (mb == null) {
			mb = new OrganizationUnitManagedBean();
			extCtx.getSessionMap().put("organizationUnitManagedBean", mb);
		}
		mb.setInstitution(selectedUser.getAuthor().getInstitution());
	}
	
	
	private AuthorPositionManagedBean authorPositionManagedBean = null;
	
	/**
	 * @return the AuthorPositionManagedBean from current session
	 */
	protected AuthorPositionManagedBean getAuthorPositionManagedBean() {
		if (authorPositionManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			AuthorPositionManagedBean mb = (AuthorPositionManagedBean) extCtx.getSessionMap().get(
					"authorPositionManagedBean");
			if (mb == null) {
				mb = new AuthorPositionManagedBean();
				extCtx.getSessionMap().put("authorPositionManagedBean", mb);
			}
			authorPositionManagedBean = mb;
		}
		return authorPositionManagedBean;
	}
	
	/**
	 * @param mode
	 * @param positions
	 */
	protected void openAuthorPositionForm(int mode, List<AuthorPosition> positions){
		AuthorPositionManagedBean mb = getAuthorPositionManagedBean();
		mb.setPosition(null);
		mb.setStartDate(null);
		mb.setEndDate(null);
		mb.setPositions(positions);
		mb.setEditMode(mode);
	}
	
	public void authorPositions(){
		int mode = editMode;
		if(mode == ModesManagedBean.MODE_REGISTER)
			mode = ModesManagedBean.MODE_ADD;
		else if ((mode == ModesManagedBean.MODE_UPDATE_USER_BASIC_DATA) || (mode == ModesManagedBean.MODE_UPDATE_USER_ADVANCED_DATA))
			mode = ModesManagedBean.MODE_UPDATE;
		this.openAuthorPositionForm(mode, selectedUser.getAuthor().getFormerPositions());
	}
	
	/**
	 * Prepares web form where user can choose Research area for selected user
	 */
	public void pickResearchArea() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		ResearchAreaManagedBean mb = (ResearchAreaManagedBean) extCtx.getSessionMap().get(
				"researchAreaManagedBean");
		if (mb == null) {
			mb = new ResearchAreaManagedBean();
			extCtx.getSessionMap().put("researchAreaManagedBean", mb);
		}

		mb.setInstitution(null);
		mb.setIPickResearchAreaManagedBean(this);
		mb.setSelectedResearchArea(new ResearchAreaDTO());
		mb.setPickMessage("administration.user.pickResearchAreaMessage");
		mb.setCustomPick(false);
		mb.switchToPickMode();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickResearchAreaManagedBean#setResearchArea(rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO)
	 */
	@Override
	public void setResearchArea(ResearchAreaDTO researchArea) {
		if ((selectedUser.getAuthor().getResearchAreas().contains(researchArea))) {
			selectedUser.getAuthor().getResearchAreas().set(
					selectedUser.getAuthor().getResearchAreas().indexOf(researchArea), researchArea);
		} else {
			selectedUser.getAuthor().getResearchAreas().add(researchArea);
		}
	}
	
	/**
	 * Removes the selected research area from the list of research Areas
	 */
	public void removeResearchArea() {
		ResearchAreaDTO selectedResearchArea = findResearchAreaByControlNumber();
		if (selectedResearchArea != null) 
			selectedUser.getAuthor().getResearchAreas().remove(selectedResearchArea);
	}
	
	private ResearchAreaDTO findResearchAreaByControlNumber() {
		ResearchAreaDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String classId = facesCtx.getExternalContext()
					.getRequestParameterMap().get("classId");
			for (ResearchAreaDTO dto : selectedUser.getAuthor().getResearchAreas()) {
				if ((dto.getClassId() != null)
						&& (dto.getClassId()
								.equalsIgnoreCase(classId))) {
					retVal = dto;
					break;
				}
			}
		} catch (Exception e) {
		}
		return retVal;
	}

	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickResearchAreaManagedBean#cancelPickingResearchArea()
	 */
	@Override
	public void cancelPickingResearchArea() {
	}	
	
	
	public void addModalPanelToRenderModalPanels(String modalPanel){
		if((modalPanel!=null) && (modalPanel.trim().length()>0) && (! renderModalPanels.contains(modalPanel)))
			renderModalPanels += (((renderModalPanels.length()>0)?(","):("")) + modalPanel);
	}
	
	public void removeModalPanelFromRenderModalPanels(String modalPanel){
		String temp = renderModalPanels;
		if((modalPanel!=null) && (modalPanel.trim().length()>0) && (renderModalPanels.contains(modalPanel))){
			StringTokenizer strToken = new StringTokenizer(renderModalPanels, ",");
			temp = "";
			while(strToken.hasMoreTokens()){
				String nextToken = strToken.nextToken();
				if(!nextToken.equals(modalPanel)){
					temp += (((temp.length()>0)?(","):("")) + nextToken);
				}
			}
		}
		renderModalPanels = temp;	
	}

	/**
	 * @return the jobAd
	 */
	public boolean getJobAd() {
		return jobAd;
	}

	/**
	 * @param jobAd the jobAd to set
	 */
	public void setJobAd(boolean jobAd) {
		this.jobAd = jobAd;
	}
	
	private Date dateOfBirth = null;

	/**
	 * @return the dateOfBirth
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth the dateOfBirth to set
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	public String getHomePage(){
		if(jobAd)
			return "jobAdsIndex.jsf";
		else 
			return "index.jsf";
	}
	
}
