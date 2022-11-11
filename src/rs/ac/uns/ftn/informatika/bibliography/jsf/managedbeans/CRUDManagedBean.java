package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gint.util.string.StringUtils;

import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.EmailMessage;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublisherNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;
import rs.ac.uns.ftn.informatika.bibliography.utils.ResourceBoundles;

/**
 * Abstract class with basic CRUD operations
 * 
 * @author chenejac@uns.ac.rs
 * 
 */
@SuppressWarnings("serial")
public abstract class CRUDManagedBean implements Serializable{

	protected FacesMessages facesMessages = new FacesMessages(
			new ResourceBoundles());

	protected static Log log = LogFactory.getLog(CRUDManagedBean.class.getName());
	
	public static ConnectionFactory cf;
	
	public static Queue mailQueue;
	
	public static Queue updateQueue;
	
	public static javax.mail.Session mailSession;
	
	
	/**
	 * 
	 */
	public CRUDManagedBean() {
		super();
	}

	/**
	 * Logs a debug message
	 * 
	 * @param str
	 *            a message which should be logged
	 */
	protected void debug(String str) {
		log.debug(((getUserManagedBean().getLoggedUser().getEmail() != null) ? "{user: "
						+ getUserManagedBean().getLoggedUser().getEmail()
						+ "} "
						: "{visitor} ")
						+ str);
	}

	protected boolean populateList = true;
	protected boolean orderList = true;
	
	/**
	 *  Populates the list of appropriate records
	 */
	public abstract void populateList();
	
	/**
	 * Logs an error message
	 * 
	 * @param str
	 *            the message which should be logged
	 * @param e
	 *            the exception which has happened and should be logged
	 */
	protected void error(String str, Throwable e) {
		log.error(
						((getUserManagedBean().getLoggedUser().getEmail() != null) ? "{user: "
								+ getUserManagedBean().getLoggedUser()
										.getEmail() + "} "
								: "{visitor}")
								+ str, e);
	}

	protected boolean init = false;

	
	
	/**
	 * Prepares CRUD page for using
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String enterCRUDPage() {
		getUserManagedBean().resetAllForms();
		String retVal = resetForm();
		tableModalPanel = "";
		switchToBrowseMode();
		return retVal;
	}
	
	/**
	 * Resets form to the initial state
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String resetForm() {
		renderModalPanels = "";
		setTableMode(ModesManagedBean.MODE_NONE);
		setEditMode(ModesManagedBean.MODE_NONE);
		returnPage = "";
		whereStr = "";
		orderBy = null;
		direction = null;
		setCustomPick(false);
		return dispetcher(0);
	}
	
	
	protected int tableMode = ModesManagedBean.MODE_NONE;
	protected int editMode = ModesManagedBean.MODE_NONE;

	/**
	 * @return current table mode (browse, pick ...)
	 */
	public int getTableMode() {
		return tableMode;
	}

	/**
	 * @param tableMode the tableMode to set
	 */
	public void setTableMode(int tableMode) {
		this.tableMode = tableMode;
		if(tableMode == ModesManagedBean.MODE_NONE){
			getUserManagedBean().removeModalPanelFromRenderModalPanels(tableModalPanel);
		} else {
			getUserManagedBean().addModalPanelToRenderModalPanels(tableModalPanel);
			renderModalPanels = getUserManagedBean().getRenderModalPanels();
		}
	}

	/**
	 * @param editMode the editMode to set
	 */
	public void setEditMode(int editMode) {		
		this.editMode = editMode;		
		if(editMode == ModesManagedBean.MODE_NONE){
			if(simpleForm){
				getUserManagedBean().removeModalPanelFromRenderModalPanels(simpleEditModalPanel);
			}else{ 
				getUserManagedBean().removeModalPanelFromRenderModalPanels(editModalPanel);
			}
		} else {
			if(simpleForm){
				getUserManagedBean().addModalPanelToRenderModalPanels(simpleEditModalPanel);
			}else {				
				getUserManagedBean().addModalPanelToRenderModalPanels(editModalPanel);				
			}
			renderModalPanels = getUserManagedBean().getRenderModalPanels();
		}
	}

	/**
	 * @return current edit mode (new, details ...)
	 */
	public int getEditMode() {
		return editMode;
	}
	
	protected boolean simpleForm = false;

	/**
	 * @return the simpleForm
	 */
	public boolean isSimpleForm() {
		return simpleForm;
	}

	/**
	 * @param simpleForm the simpleForm to set
	 */
	public void setSimpleForm(boolean simpleForm) {
		this.simpleForm = simpleForm;
	}
	
	public void switchSimple(){
		int tempEditMode = this.editMode;
		setEditMode(ModesManagedBean.MODE_NONE);
		simpleForm = !simpleForm;
		if(simpleForm)
			editTabNumber = 0;
		setEditMode(tempEditMode);
	}

	/**
	 * Switches table mode to NONE mode
	 */
	public void switchToTableNoneMode() {
		setTableMode(ModesManagedBean.MODE_NONE);
		debug("switchToTableNoneMode");
	}

	/**
	 * Switches form to browse mode
	 */
	public void switchToBrowseMode() {
		setTableMode(ModesManagedBean.MODE_BROWSE);
		setEditMode(ModesManagedBean.MODE_NONE);
		customPick = false;
		populateList = true;
		orderList = true;
		debug("switchToBrowseMode");
	}

	/**
	 * Switches form to pick mode
	 */
	public void switchToPickMode() {
		setTableMode(ModesManagedBean.MODE_PICK);
		setEditMode(ModesManagedBean.MODE_NONE);
		tableTabNumber = 0;
		if (customPick) {
			orderBy = null;
			direction = null;
		} else {
			populateList = true;
			orderList = true;
			tableTabNumber = 1;
		}
		init = true;
		debug("switchToPickMode");
	}

	/**
	 * Switches form to new mode
	 */
	public void switchToAddMode() {
		simpleForm = false;
		setEditMode(ModesManagedBean.MODE_ADD);
		editTabNumber = 0;
	}
	
	/**
	 * Switches form to new simple mode
	 */
	public void switchToSimpleAddMode() {
		simpleForm = true;
		setEditMode(ModesManagedBean.MODE_ADD);
		editTabNumber = 0;
	}

	/**
	 * Switches form to edit mode
	 */
	public void switchToUpdateMode() {
		simpleForm = false;
		setEditMode(ModesManagedBean.MODE_UPDATE);
		editTabNumber = 0;
	}
	
	/**
	 * Switches form to edit simple mode
	 */
	public void switchToSimpleUpdateMode() {
		simpleForm = true;
		if(editMode == ModesManagedBean.MODE_IMPORT)
			setEditMode(ModesManagedBean.MODE_IMPORT);
		else
			setEditMode(ModesManagedBean.MODE_UPDATE);
		editTabNumber = 0;
	}

	/**
	 * Switches form to details mode
	 */
	public void switchToViewDetailsMode() {
		simpleForm = false;
		setEditMode(ModesManagedBean.MODE_VIEW_DETAILS);
		editTabNumber = 0;
	}
	
	/**
	 * Switches form to details simple mode
	 */
	public void switchToSimpleViewDetailsMode() {
		simpleForm = true;
		setEditMode(ModesManagedBean.MODE_VIEW_DETAILS);
		editTabNumber = 0;
	}

	/**
	 * Switches edit mode to import mode
	 */
	public void switchToImportMode() {
		simpleForm = false;
		setEditMode(ModesManagedBean.MODE_IMPORT);
		debug("switchToImportMode");
	}
	
	/**
	 * Switches edit mode to NONE mode
	 */
	public void switchToEditNoneMode() {
		setEditMode(ModesManagedBean.MODE_NONE);
		if (tableMode == ModesManagedBean.MODE_PICK) {
			tableTabNumber = 1;
		}
		debug("switchToEditNoneMode");
	}

	/**
	 * Updates current object
	 */
	public abstract void update();

	/**
	 * Adds current object
	 */
	public abstract void add();

	/**
	 * Deletes selected object
	 */
	public abstract void delete();
	
	/**
	 * Archives selected object
	 */
	public void archive(){
		
	}
	
	/**
	 * Extract archive selected object
	 */
	public void extractArchive(){
		
	}
	
	/**
	 * Imports current object
	 */
	public void importCurrent(){
		debug("import started");
		add();
		debug("import finished");
	}

	/**
	 * Exits from the form and backs to the previous form
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String exit() {
		setTableMode(ModesManagedBean.MODE_BROWSE);
		setEditMode(ModesManagedBean.MODE_NONE);
		if((returnPage != null) && (returnPage.equals("indexPage")) && (getUserManagedBean().getJobAd()))
			return "jobAdsIndexPage";
		else 
			return returnPage;
	}

	/**
	 * Finishes wizard and does ALL necessary cleanup
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public abstract String finishWizard();

	protected String returnPage = "";

	/**
	 * @param returnPage
	 *            return page to set
	 */
	public void setReturnPage(String returnPage) {
		this.returnPage = returnPage;
	}

	/**
	 * Sets attribute name and direction which will be used for sorting
	 */
	public void sort() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();

		String orderBy = facesCtx.getExternalContext().getRequestParameterMap()
				.get("orderBy");
		if (this.orderBy != null && this.orderBy.equals(orderBy)) {
			if (this.direction.equals("asc"))
				this.direction = "desc";
			else
				this.direction = "asc";
		} else {
			this.orderBy = orderBy;
			this.direction = "asc";
		}
		orderList = true;
	}

	protected String orderBy;

	/**
	 * @return the attribute name used for sorting
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * Sets an attribute name used for sorting
	 * 
	 * @param orderBy
	 *            the attribute name to set
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	protected String direction;

	/**
	 * @return the direction used for sorting
	 */
	public String getDirection() {
		return direction;
	}
	
	/**
	 * Sets direction used for sorting
	 * 
	 * @param direction
	 *            direction to set
	 */
	public void setDirection(String direction) {
		this.direction = direction;
	}

	protected String whereStr = "";

	/**
	 * @return the string used for filtering
	 */
	public String getWhereStr() {
		return whereStr;
	}

	/**
	 * Sets a string used for filtering
	 * 
	 * @param whereStr
	 *            the string to set
	 */
	public void setWhereStr(String whereStr) {
		this.whereStr = whereStr;
		if((whereStr == null) || (!whereStr.equals("M99"))){
			populateList = true;
			orderList = true;
		}
//		getBindingManagedBean().getTableComponent().setFirst(0);
	}

	/**
	 * Removes filter for getting the list of objects
	 */
	public void removeFilter() {
		setWhereStr("");
	}

	protected int tableTabNumber;
	protected int editTabNumber;

	/**
	 * @return the table tab number
	 */
	public int getTableTabNumber() {
		return tableTabNumber;
	}

	/**
	 * @param tableTabNumber
	 *            the table tab number to set
	 */
	public void setTableTabNumber(int tableTabNumber) {
		this.tableTabNumber = tableTabNumber;
	}
	
	public String getActiveTableTab(){
		return "tab" + tableTabNumber;
	}

	/**
	 * Increases the table tab number for one
	 */
	public void nextTableTab() {
		tableTabNumber++;
		if(tableTabNumber == 1){
			orderBy = null;
			direction = null;
			whereStr = "";
			populateList = true;
		}
	}

	/**
	 * Decreases table tab number for one
	 */
	public void previousTableTab() {
		tableTabNumber--;
	}

	/**
	 * @return the edit tab number
	 */
	public int getEditTabNumber() {
		return editTabNumber;
	}

	/**
	 * @param editTabNumber
	 *            the edit tab number to set
	 */
	public void setEditTabNumber(int editTabNumber) {
		this.editTabNumber = editTabNumber;
	}
	
	public String getActiveEditTab(){
		return "tab" + editTabNumber;
	}

	/**
	 * Increases the edit tab number for one
	 */
	public void nextEditTab() {
		editTabNumber++;
	}

	/**
	 * Decreases edit tab number for one
	 */
	public void previousEditTab() {
		editTabNumber--;
	}

	protected String tableModalPanel;
	protected String editModalPanel;
	protected String simpleEditModalPanel;
	
	
	/**
	 * @return the tableModalPanel
	 */
	public String getTableModalPanel() {
		return tableModalPanel;
	}

	/**
	 * @param tableModalPanel the tableModalPanel to set
	 */
	public void setTableModalPanel(String tableModalPanel) {
		this.tableModalPanel = tableModalPanel;
	}

	/**
	 * @return the editModalPanel
	 */
	public String getEditModalPanel() {
		return editModalPanel;
	}

	/**
	 * @param editModalPanel the editModalPanel to set
	 */
	public void setEditModalPanel(String editModalPanel) {
		this.editModalPanel = editModalPanel;
	}
	
	/**
	 * @return the simpleEditModalPanel
	 */
	public String getSimpleEditModalPanel() {
		return simpleEditModalPanel;
	}

	/**
	 * @param simpleEditModalPanel the simpleEditModalPanel to set
	 */
	public void setSimpleEditModalPanel(String simpleEditModalPanel) {
		this.simpleEditModalPanel = simpleEditModalPanel;
	}

	protected boolean customPick;

	/**
	 * @return true if it is custom pick, else false
	 */
	public boolean isCustomPick() {
		return customPick;
	}

	/**
	 * @param customPick
	 *            the custom pick to set
	 */
	public void setCustomPick(boolean customPick) {
		this.customPick = customPick;
		if (customPick) {
			orderBy = null;
			direction = null;
			whereStr = "";
		}
	}

	/**
	 * @param condition
	 *            condition for creating outcome string
	 * @return the outcome string for JSF navigation
	 */
	protected abstract String dispetcher(int condition);

	private UserManagedBean userManagedBean = null;

	/**
	 * @return the UserManagedBean from current session
	 */
	public UserManagedBean getUserManagedBean() {
		if (userManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			UserManagedBean mb = (UserManagedBean) extCtx.getSessionMap().get(
					"userManagedBean");
			if (mb == null) {
				mb = new UserManagedBean();
				extCtx.getSessionMap().put("userManagedBean", mb);
			}
			userManagedBean = mb;
		}
		return userManagedBean;
	}

	
	
	protected String pickSimilarMessage = null;

	/**
	 * @return the pickSimilarMessage
	 */
	public String getPickSimilarMessage() {
		return facesMessages.getMessageFromResourceBundle(pickSimilarMessage);
	}

	/**
	 * @param pickSimilarMessage the pickSimilarMessage to set
	 */
	public void setPickSimilarMessage(String pickSimilarMessage) {
		this.pickSimilarMessage = pickSimilarMessage;
	}
	
	/**
	 * Adding current mARC21Record is without sense because mARC21Record already exists
	 */
	public void chooseSimilar(){
		
	} 
	
	private MultilingualContentManagedBean multilingualContentManagedBean = null;
	
	/**
	 * @return the MultilingualContentManagedBean from current session
	 */
	protected MultilingualContentManagedBean getMultilingualContentManagedBean() {
		if (multilingualContentManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			MultilingualContentManagedBean mb = (MultilingualContentManagedBean) extCtx.getSessionMap().get(
					"multilingualContentManagedBean");
			if (mb == null) {
				mb = new MultilingualContentManagedBean();
				extCtx.getSessionMap().put("multilingualContentManagedBean", mb);
			}
			multilingualContentManagedBean = mb;
		}
		return multilingualContentManagedBean;
	}
	
	/**
	 * @param mode
	 * @param contents
	 * @param contentHeader
	 */
	protected void openMultilingualContentForm(int mode, List<MultilingualContentDTO> contents, boolean htmlContent, String panelHeader, String contentHeader){
		MultilingualContentManagedBean mb = getMultilingualContentManagedBean();
		mb.setContent(null);
		mb.setLanguage(null);
		mb.setContents(contents);
		mb.setHtmlContent(htmlContent);
		mb.setContentHeader(contentHeader);
		mb.setPanelHeader(panelHeader);
		mb.setEditMode(mode);
//		userManagedBean.addModalPanelToRenderModalPanels(mb.get)
	}
	
	private MultilingualContentPublisherManagedBean multilingualContentPublisherManagedBean = null;
	
	/**
	 * @return the MultilingualContentPublisherManagedBean from current session
	 */
	protected MultilingualContentPublisherManagedBean getMultilingualContentPublisherManagedBean() {
		if (multilingualContentPublisherManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			MultilingualContentPublisherManagedBean mb = (MultilingualContentPublisherManagedBean) extCtx.getSessionMap().get(
					"multilingualContentPublisherManagedBean");
			if (mb == null) {
				mb = new MultilingualContentPublisherManagedBean();
				extCtx.getSessionMap().put("multilingualContentPublisherManagedBean", mb);
			}
			multilingualContentPublisherManagedBean = mb;
		}
		return multilingualContentPublisherManagedBean;
	}
	
	/**
	 * @param mode
	 * @param publisherTranslations
	 */
	protected void openMultilingualContentPublisherForm(int mode, List<PublisherNameDTO> publisherTranslations, String panelHeader){
		MultilingualContentPublisherManagedBean mb = getMultilingualContentPublisherManagedBean();
		mb.setPubName(null);
		mb.setPubPlace(null);
		mb.setPubState(null);
		mb.setLanguage(null);
		mb.setPublisherTranslations(publisherTranslations);
		mb.setPanelHeader(panelHeader);
		mb.setEditMode(mode);
	}
	
	
	/**
	 */
	protected void sendMessage(EmailMessage emailMessage){
		try {
			Connection connEmailer = CRUDManagedBean.cf.createConnection();
			Session mailSession = connEmailer.createSession(false,
					 Session.AUTO_ACKNOWLEDGE);
			MessageProducer mailProducer = mailSession.createProducer(
					(Destination)CRUDManagedBean.mailQueue);
			ObjectMessage objMessage = mailSession.createObjectMessage();
			objMessage.setObject(emailMessage);
			mailProducer.send(objMessage);
			connEmailer.close();
		} catch (JMSException e) {
			error("sendMessage", e);
		}
	}
	
	/**
	 */
	protected void sendRecordMessage(RecordDTO dto, String operation){
		try {
			Connection connUpdater = CRUDManagedBean.cf.createConnection();
			Session updateSession = connUpdater.createSession(false,
					 Session.AUTO_ACKNOWLEDGE);
			MessageProducer updateProducer = updateSession.createProducer(
					(Destination)CRUDManagedBean.updateQueue);
			ObjectMessage objMessage = updateSession.createObjectMessage();
			objMessage.setStringProperty("operation", operation);
			objMessage.setObject(dto);
			updateProducer.send(objMessage);
			connUpdater.close();
		} catch (JMSException e) {
			log.error("RecordDAO send message: " + e);
		}
	}
	
	/**
	 *  Sends email notification to administrator with details about new record
	 */
	protected void newRecordEmailNotification(RecordDTO newRecord, String subject){
		ResourceBundle rbAdministration = PropertyResourceBundle.getBundle(
				"messages.messages-administration", new Locale(this.getUserManagedBean().getLanguage()));
		sendMessage(new EmailMessage(rbAdministration.getString("administration.email.cris"), rbAdministration.getString("administration.email.administrator"), null, null, subject, "<pre>" + newRecord.getRecord().toString() + "</pre>"));
	}
	
	/**
	 *  Sends email notification to other authors with details about added reference
	 */
	protected void newReferenceAuthorsEmailNotification(PublicationDTO newReference, String subject){
		ResourceBundle rbAdministration = PropertyResourceBundle.getBundle(
				"messages.messages-administration", new Locale(this.getUserManagedBean().getLanguage()));
		String responsiblePersonName = null;
		String responsiblePersonEmail = null;
		StringBuffer mailRecepient = new StringBuffer();
		for (AuthorDTO author : newReference.getAllAuthors()) {
			if(author.getEmail()!=null) {
				if(author.getEmail().equals(getUserManagedBean().getLoggedUser().getEmail())){
					responsiblePersonName = author.getName().toString();
					responsiblePersonEmail = author.getEmail();
				} else if(author.isAlreadyRegistered())
					mailRecepient.append(author.getEmail() + " ");
			}
		}
		if(responsiblePersonName != null)
			if(! ("".equals(mailRecepient.toString().trim())))
				sendMessage(new EmailMessage(rbAdministration.getString("administration.email.cris"), mailRecepient.toString().trim(), null, null, subject, getNewReferenceEmailHeader(responsiblePersonName, responsiblePersonEmail) + newReference.getHTMLRepresentation() +  getNewReferenceEmailFooter() ));
	}
	
	/**
	 *  Sends email notification to other authors with details about changed reference
	 */
	protected void changedReferenceAuthorsEmailNotification(PublicationDTO changedReference, String subject){
		ResourceBundle rbAdministration = PropertyResourceBundle.getBundle(
				"messages.messages-administration", new Locale(this.getUserManagedBean().getLanguage()));
		String responsiblePersonName = null;
		String responsiblePersonEmail = null;
		StringBuffer mailRecepient = new StringBuffer();
		AuthorDTO loggedAuthor = getUserManagedBean().getLoggedUser().getAuthor();
		if((loggedAuthor.getControlNumber() != null) && (loggedAuthor.getEmail()!=null)){
			for (AuthorDTO author : changedReference.getAllAuthors()) {
				if((author.getControlNumber().equals(loggedAuthor.getControlNumber()))){
					responsiblePersonName = author.getName().toString();
					responsiblePersonEmail = author.getEmail();
				} else if(author.isAlreadyRegistered())
							mailRecepient.append(author.getEmail() + " ");
			}
			if(responsiblePersonName != null)
				if(! ("".equals(mailRecepient.toString().trim())))
					sendMessage(new EmailMessage(rbAdministration.getString("administration.email.cris"), mailRecepient.toString().trim(), null, null, subject, getChangedReferenceEmailHeader(responsiblePersonName, responsiblePersonEmail) + changedReference.getHTMLRepresentation() +  getChangedReferenceEmailFooter() ));
		}
	}
	
	protected String getNewReferenceEmailHeader(String responsiblePersonName, String responsiblePersonEmail){
		String retVal = "";
		ResourceBundle rbAdministration = PropertyResourceBundle.getBundle(
				"messages.messages-administration", new Locale(this.getUserManagedBean().getLanguage()));
		retVal += rbAdministration.getString("administration.notification.header.dear") + "<br/><br/>";
		retVal += rbAdministration.getString("administration.notification.header.referenceAddedBeforeName")  + responsiblePersonName + " (<a href=mailto:" + responsiblePersonEmail + ">"+responsiblePersonEmail+"</a>)" + rbAdministration.getString("administration.notification.header.referenceAddedAfterName") + "<br/>";
		
		retVal += "<br/><br/><br/>";
		return retVal;
	}
	
	protected String getNewReferenceEmailFooter(){
		ResourceBundle rbAdministration = PropertyResourceBundle.getBundle(
				"messages.messages-administration", new Locale(this.getUserManagedBean().getLanguage()));
		
		String retVal = "<br/><br/><br/>";
		
		retVal += rbAdministration.getString("administration.notification.footer.canEdit");
		retVal += "<a href="+rbAdministration.getString("administration.notification.footer.applicationLink") + ">"+rbAdministration.getString("administration.notification.footer.applicationLinkText")+"</a>";
		
		retVal += getEmailFooter();
		
		return retVal;
	}
	
	protected String getChangedReferenceEmailHeader(String responsiblePersonName, String responsiblePersonEmail){
		String retVal = "";
		ResourceBundle rbAdministration = PropertyResourceBundle.getBundle(
				"messages.messages-administration", new Locale(this.getUserManagedBean().getLanguage()));
		retVal += rbAdministration.getString("administration.notification.header.dear") + "<br/><br/>";
		if((responsiblePersonEmail != null) && (responsiblePersonEmail.equals(getUserManagedBean().getLoggedUser().getEmail())))
			retVal += rbAdministration.getString("administration.notification.header.referenceChangedBeforeName")  + responsiblePersonName + " (<a href=mailto:" + responsiblePersonEmail + ">"+responsiblePersonEmail+"</a>)" + rbAdministration.getString("administration.notification.header.referenceChangedAfterName") + "<br/>";
		else 
			retVal += rbAdministration.getString("administration.notification.header.referenceChangedBeforeEmail")  + " (<a href=mailto:" + getUserManagedBean().getLoggedUser().getEmail() + ">"+getUserManagedBean().getLoggedUser().getEmail()+"</a>)" + rbAdministration.getString("administration.notification.header.referenceChangedAfterEmail") + "<br/>";
		retVal += "<br/><br/><br/>";
		return retVal;
	}
	
	protected String getChangedReferenceEmailFooter(){
		String retVal = "<br/><br/><br/>";
		ResourceBundle rbAdministration = PropertyResourceBundle.getBundle(
				"messages.messages-administration", new Locale(this.getUserManagedBean().getLanguage()));
		
		retVal += rbAdministration.getString("administration.notification.footer.canEdit");
		retVal += "<a href="+rbAdministration.getString("administration.notification.footer.applicationLink") + ">"+rbAdministration.getString("administration.notification.footer.applicationLinkText")+"</a>";
		
		retVal += getEmailFooter();
			
		return retVal;
	}
	
	protected String getEmailFooter(){
		String retVal = "<br/><br/><br/>";
		ResourceBundle rbAdministration = PropertyResourceBundle.getBundle(
				"messages.messages-administration", new Locale(this.getUserManagedBean().getLanguage()));
		retVal += rbAdministration.getString("administration.notification.footer.kindRegards") + "<br/>";
		retVal += rbAdministration.getString("administration.notification.footer.sign") + "<br/>";
		retVal += "<a href=mailto:"+rbAdministration.getString("administration.email.cris") + ">"+rbAdministration.getString("administration.email.cris")+"</a>";
		
		return retVal;
	}
	
	protected String renderModalPanels = "";
	
	/**
	 * @return the renderModalPanels
	 */
	public String getRenderModalPanels() {
		return renderModalPanels;
	}

	/**
	 * @param renderModalPanels the renderModalPanels to set
	 */
	public void setRenderModalPanels(String renderModalPanels) {
		this.renderModalPanels = renderModalPanels;
	}
	
	protected boolean simpleAuthorList = false;

	/**
	 * @return the simpleAuthorList
	 */
	public boolean isSimpleAuthorList() {
		return simpleAuthorList;
	}

	/**
	 * @param simpleAuthorList the simpleAuthorList to set
	 */
	public void setSimpleAuthorList(boolean simpleAuthorList) {
		this.simpleAuthorList = simpleAuthorList;
	}
	
	public void switchSimpleAuthorList(){
		simpleAuthorList = !simpleAuthorList;
	}
	
	protected String authorName = "";

	/**
	 * @return the authorName
	 */
	public String getAuthorName() {
		return authorName;
	}

	/**
	 * @param authorName the authorName to set
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	
	/**
	 * Sets all necessary things for AuthorManageBean in pick mode
	 */
	public void setAuthorManageBeanToPick(IPickAuthorManagedBean pickAuthorManagedBean) {
		
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		AuthorManagedBean mb = (AuthorManagedBean) extCtx.getSessionMap().get(
				"authorManagedBean");
		if (mb == null) {
			mb = new AuthorManagedBean();
			extCtx.getSessionMap().put("authorManagedBean", mb);
		}

		mb.setIPickAuthorManagedBean(pickAuthorManagedBean);
		mb.setSelectedAuthor(null);
		mb.setFirstname(null);
		mb.setCustomPick(false);
		mb.setQueryFieldName(null);
	}
	
	public void simpleFormPickAuthor(AuthorManagedBean mb){
		mb.setTableTabNumber(1);
		mb.populateList = true;
		mb.orderList = true;
		mb.orderBy = null;
		
		
		mb.setOtherName(null);
        StringTokenizer st = new StringTokenizer(authorName);
        if (st.countTokens() > 2){
        	mb.setLastname(StringUtils.clearDelimiters(st.nextToken(), ",;:\"()[]{}+/!-"));
        	String otherName = st.nextToken().replace(".", "");
        	if(otherName.length() == 1){
//        		mb.setOtherName(otherName);
        		mb.setFirstname(StringUtils.clearDelimiters(st.nextToken(), ",;:\"()[]{}+/!-"));
        	} else {
        		mb.setFirstname(otherName + " " + StringUtils.clearDelimiters(st.nextToken(), ",;:\"()[]{}+/!-"));
        	}
        } else {
        	try {
    			String[] names = authorName.split(" ");
    			mb.setLastname(StringUtils.clearDelimiters(names[0], ",;:\"()[]{}+/!-")); 
    			mb.setFirstname(StringUtils.clearDelimiters(names[1], ",;:\"()[]{}+/!-"));
    		} catch (Exception e) {
    		}
        }
	}

	public String getCurrentPageReportTemplate(){
		String retVal = "";
		ResourceBundle rbRecords = PropertyResourceBundle.getBundle(
				"messages.messages-records", new Locale(this.getUserManagedBean().getLanguage()));
		retVal = "{startRecord}-{endRecord} " + rbRecords.getString("records.tablePanel.pagination.of") + " {totalRecords}" + rbRecords.getString("records.tablePanel.pagination.records");
		return retVal;
	}

	public String getRowsPerPageTemplate(){
		String retVal = "";
		ResourceBundle rbRecords = PropertyResourceBundle.getBundle(
				"messages.messages-records", new Locale(this.getUserManagedBean().getLanguage()));
		retVal = "5,10,{ShowAll|'" + rbRecords.getString("records.tablePanel.pagination.showAll") + "'}";
		return retVal;
	}
	

}
