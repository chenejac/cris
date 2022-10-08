package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.model.SelectItem;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rs.ac.uns.ftn.informatika.bibliography.dao.RegisterEntryDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RegisterEntryDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RegisterEntryDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.UserDTO;
import rs.ac.uns.ftn.informatika.bibliography.reports.maticnaKnjiga.MaticnaKnjigaItemBean;
import rs.ac.uns.ftn.informatika.bibliography.reports.maticnaKnjiga.MaticnaKnjigaReportUtils;
import rs.ac.uns.ftn.informatika.bibliography.reports.maticnaKnjiga.MaticnaKnjigaUtils;
import rs.ac.uns.ftn.informatika.bibliography.reports.maticnaKnjiga.NumberPhDPerInstitution;
import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;
import rs.ac.uns.ftn.informatika.bibliography.utils.LatCyrUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.ResourceBoundles;
import edu.emory.mathcs.backport.java.util.Collections;

public class PrepareUNSPromotionManagedBean extends CRUDManagedBean {
	
	protected FacesMessages facesMessages = new FacesMessages(
			new ResourceBoundles());
	
	
	protected List<RegisterEntryDTO> list;
	//protected List<RegisterEntryDTO> selectedForPromotion;
	
	private RegisterEntryDAO registerEntryDAO = new RegisterEntryDAO(new RegisterEntryDB());
	
	private List<String> distinctPromotionNames = new ArrayList<String>();
	
	private String selectedPromotionName = "";
	
	private String choosenView = "1"; // 1 - Spisak, 2- Adrese 3-Za diplomu
	
	private Date promotionDate;
	
	private String universityID = "UNS" ; // UNS UPA
	
	private String institution;
	  
	// reports
	
	private Date dateFrom;
	private Date dateTo;
	private List<NumberPhDPerInstitution> listNumberPhDPerInstitution;
	private int totalOldProgram;
	private int totalNewProgram;
	
	// moze biti poDatumu i poInstituciji
	
	private String registerType = "poDatumu";
	private Date dateFromRegister;
	private Date dateToRegister;
	private String institutionRegister;
	private List<String> distinctInstitutions;
	
	private List<RegisterEntryDTO> listOfPromoted = null;
	
	private List<RegisterEntryDTO> listForGeneratingIds = null;
	
	
	
	
	

	
	public void enterPage(PhaseEvent event){
		if(init == false){
			UserDTO userDTO = getUserManagedBean().getLoggedUser();
			if(userDTO != null)
				if(((userDTO.getInstitution() != null) && (userDTO.getInstitution().getControlNumber() != null) && (userDTO.getInstitution().getControlNumber().equals("(BISIS)94894")) || 
						((userDTO.getInstitution().getSuperInstitution() != null) && (userDTO.getInstitution().getSuperInstitution().getControlNumber() != null) && (userDTO.getInstitution().getSuperInstitution().getControlNumber().equals("(BISIS)94894")))))
					universityID = "UPA";
				else 
					universityID = "UNS";
			enterCRUDPage();
			init = true;
		}
	}

	@Override
	public void populateList() {
		try{
			setOrderBy("authorName.lastname");
			setDirection("asc");
			distinctInstitutions = registerEntryDAO.getDistinctInstitutions(universityID);
			
			if(institution == null)
				calculateInstitution(distinctInstitutions);
			
			List<RegisterEntryDTO> listTmp = registerEntryDAO.getRegisterEntriesWithoutId(universityID, institution);		
			list = listTmp;	
			
			
		}catch(Exception e){
			error("populateList", e);
			list = new ArrayList<RegisterEntryDTO>();
		}

	}
	
	private void calculateInstitution(List<String> distinctInstitutions) {
		if((getUserManagedBean().getLoggedUser().getInstitution().getControlNumber() != null) && (!"(BISIS)5920".equals(getUserManagedBean().getLoggedUser().getInstitution().getControlNumber())) && (!"(BISIS)94894".equals(getUserManagedBean().getLoggedUser().getInstitution().getControlNumber()))){
			InstitutionDTO userInstitution = getUserManagedBean().getLoggedUser().getInstitution();
			for (String string : distinctInstitutions) {
				if(LatCyrUtils.toLatinUnaccented(userInstitution.toString().toLowerCase()).startsWith(LatCyrUtils.toLatinUnaccented(string).toLowerCase())){
					institution = string;
					break;
				}
			}
		} else 
			institution = "%";
	}
	
	
	

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void add() {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	@Override
	public String finishWizard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "promotionPage";
		return retVal;
	}
	
	public void changeTab(javax.faces.event.FacesEvent event){
		
	}
	
	public List<SelectItem> getDistinctInstitutionsItems(){
		List<SelectItem> retVal = new ArrayList<SelectItem>();
		if((institution == null) || (institution.equals("%"))){
			retVal.add(new SelectItem(null, "-- institucija --"));
			for(String str:distinctInstitutions){
				if(str!=null && !str.equals(""))
					retVal.add(new SelectItem(str));
			}
		} else 
			retVal.add(new SelectItem(institution));
		return retVal;
			
	}
	
	public List<RegisterEntryDTO> getRegisterEntriesWithoutId(){		
		if(populateList){			
			populateList();
			populateList = false;
		}	
			return list;
	}

	
	/*
	public List<RegisterEntryDTO> getSelectedForPromotion(){
		List<RegisterEntryDTO> selectedForPromotion = new ArrayList<RegisterEntryDTO>();
		for(RegisterEntryDTO regEntry : list){
			if(regEntry.getId()!=null && regEntry.getId().equals("-1"))
				selectedForPromotion.add(regEntry);
		}		
		return selectedForPromotion;
	}*/
	
	
	public void selectedPromotionChanged(){
		
	}
	
	public List<RegisterEntryDTO> getSelectedForPromotion(){
		List<RegisterEntryDTO> selectedForPromotion = new ArrayList<RegisterEntryDTO>();
		for(RegisterEntryDTO regEntry : list){
			if(regEntry.getFuturePromotionName()!=null){			
				regEntry.setSelectedForPromotion(true);
				selectedForPromotion.add(regEntry);
			}
		}		
		return selectedForPromotion;
	}
	
	public List<RegisterEntryDTO> getSelectedForNamedPromotion(){
		List<RegisterEntryDTO> selectedForPromotion = new ArrayList<RegisterEntryDTO>();
		for(RegisterEntryDTO regEntry : list){
			if(selectedPromotionName!=null && !selectedPromotionName.equals("") && 
					(regEntry.getFuturePromotionName()!=null && regEntry.getFuturePromotionName().equals(selectedPromotionName))){				
				selectedForPromotion.add(regEntry);
			}
		}
		List<String> sortAttributesForGen = new ArrayList<String>();
		sortAttributesForGen.add("institution");	
		
		List<String> sortDirections = new ArrayList<String>();
		sortDirections.add("asc");
	
		Collections.sort(selectedForPromotion, new GenericComparator<RegisterEntryDTO>(
				sortAttributesForGen, sortDirections));
		return selectedForPromotion;
	}
	
	
	
	public void  prepareListForPrinting(){
		showListForPrinting = true;
		List<RegisterEntryDTO> list = getSelectedForNamedPromotion();
		listForPrinting = "Selektovani za promociju: "+selectedPromotionName+"<br><br>";
		listForPrintingAdress = "Adrese selektovanih za promociju: "+selectedPromotionName+"<br><br>";
		listForPrintingForDiploma = "Podaci za diplomu (za kandidate po starom programu), promocija: "+selectedPromotionName+"<br><br>";
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		for(int i=0; i<list.size();i++){
			RegisterEntryDTO reg = list.get(i);
			int number = i+1; 
			listForPrinting = listForPrinting + number+". "+ reg.getAuthorName().getFirstname() +" "
								+ reg.getAuthorName().getLastname() + ", "
								+ (reg.getNameOfAuthorDegree()!=null ? 
								   reg.getNameOfAuthorDegree().toLowerCase() : "") + " (email: " + reg.getEmail() + ") <br>" ;
			listForPrintingAdress = listForPrintingAdress + reg.getAuthorName().getFirstname() +" "
															+ reg.getAuthorName().getLastname()+"<br>"
			            + new MaticnaKnjigaItemBean(reg).getAdresa()+"<br><br>"; 
			if(reg.isOldProgram()){			
				RegisterEntryDTO regTemp = registerEntryDAO.getRegisterEntryFromDatabase(reg.getDissertation(), true);
				if(regTemp!=null){
				regTemp.setFuturePromotionName(getSelectedPromotionName());				
				listForPrintingForDiploma = listForPrintingForDiploma + "Institucija <BR>"+
					(regTemp.getInstitution()!=null ? regTemp.getInstitution().toUpperCase():"")+"<BR>"+
					(regTemp.getInstitutionPlace()!=null ? regTemp.getInstitutionPlace().toUpperCase():"")+"<BR><BR>"+
					"Autor <BR> "+
					(regTemp.getAuthorName()!=null ? regTemp.getAuthorName().getFirstname().toUpperCase():"")+"  ("+(regTemp.getFatherName().getFirstname()!=null ? regTemp.getFatherName().getFirstname() : regTemp.getGuardiansName())+") "+(regTemp.getAuthorName()!=null ? regTemp.getAuthorName().getLastname().toUpperCase():"")+"<BR><BR>"+
					"rodjen "+(regTemp.getBirthDate()!=null ? sdf.format(regTemp.getBirthDate().getTime()) : "")+" u "+(regTemp.getBirthPlace()!=null ? regTemp.getBirthPlace().toUpperCase():"")+", "+(regTemp.getBirthCity()!=null ? regTemp.getBirthCity().toUpperCase() : "")+" "+(regTemp.getBirthCountry()!=null ? regTemp.getBirthCountry().toUpperCase(): "")+", <BR> "+
					(regTemp.getPreviouslyNameOfAuthorDegreeDateOld()!=null ? sdf.format(regTemp.getPreviouslyNameOfAuthorDegreeDateOld().getTime()) : "")+" stečena "+
					(regTemp.getDefendedOn()!=null ? sdf.format(regTemp.getDefendedOn().getTime()) : "")+" na "+(regTemp.getInstitution()!=null ? regTemp.getInstitution().toUpperCase():"")+" u "+(regTemp.getInstitutionPlace()!=null ? regTemp.getInstitutionPlace().toUpperCase() : "")+" naziv titule:<BR>"+
					(	regTemp.getTitle()!=null ? regTemp.getTitle().toUpperCase() : "")+"<BR>"+
					"naziv zvanja koje se stiče <BR>"+
					(regTemp.getNameOfAuthorDegree()!=null ? regTemp.getNameOfAuthorDegree().toUpperCase():"")+"<BR><BR>";			
				}																							
				 																												
			}
		}
	}
	
	public void  prepareListForPromotion(){		
	 listOfPromoted = getSelectedForNamedPromotion();	
	 listForPromotion = "";
		for(RegisterEntryDTO reg:listOfPromoted){
			listForPromotion = listForPromotion + reg.getAuthorName().getFirstname() +" "
								+ reg.getAuthorName().getLastname() + ", "
								+ (reg.getNameOfAuthorDegree()!=null ? 
								   reg.getNameOfAuthorDegree().toLowerCase() : "") 
																																							    + "<br>";
			}
	}
	
	public void unselectForPromotion(RegisterEntryDTO regEntry){
		regEntry.unselectForPromotion();
		registerEntryDAO.update(regEntry);
		
	}
	
	public void executeRegisterEntryInput(){	
		listOfPromoted = getSelectedForNamedPromotion();	
		for(RegisterEntryDTO reg:listOfPromoted){
			boolean successful = registerEntryDAO.savePromotionDateForRegisterEntry(reg, promotionDate);			
			if(!successful){
				promotionDateSaved = false;
				facesMessages.addToControlFromResourceBundle(
						"preparePromotionForm:general", FacesMessage.SEVERITY_ERROR,  "records.paperJournal.update.error", 	
						FacesContext.getCurrentInstance());
				setShowRegisterEntryInput(false);
				return;			
			}else{
				populateList();
			}
		}
		promotionDateSaved = true;
		list.removeAll(listOfPromoted);
		facesMessages.addToControlFromResourceBundle(
				"preparePromotionForm:general", FacesMessage.SEVERITY_INFO,  "records.paperJournal.update.success", 	
				FacesContext.getCurrentInstance());
		setShowRegisterEntryInput(false);
		return;		
		}
	
	
	
	public void exitShowList(){
		showListForPrinting = false;
	}
	
	public void exitShowRegisterEntryInput(){
		showRegisterEntryInput = false;
	}
	
	public void registerEntryInput(){
	 showRegisterEntryInput = true;
	 prepareListForPromotion();
	}
	
	public void sortByLastName(){
		List<String> orderByList = new ArrayList<String>();
		List<String> directionsList = new ArrayList<String>();
		orderByList.add("authorName.lastname");
		orderByList.add("authorName.firstname");
		directionsList.add("asc");
		directionsList.add("asc");
		Collections.sort(list, new GenericComparator<RegisterEntryDTO>(orderByList, directionsList));		
	}	
	
	
	public void sortBy(String propertyName){
		List<String> orderByList = new ArrayList<String>();
		List<String> directionsList = new ArrayList<String>();
		orderByList.add(propertyName);		
		directionsList.add("asc");		
		Collections.sort(list, new GenericComparator<RegisterEntryDTO>(orderByList, directionsList));		
	}
	
	public void saveListForPromotion(){	
		List<RegisterEntryDTO> selectedForPromotion = getSelectedForPromotion();
		 
		for(RegisterEntryDTO regEntry:selectedForPromotion){
			boolean successful = registerEntryDAO.saveRegisterEntryForPromotion(regEntry);
			if(!successful){
				listForPromotionSaved = false;
				facesMessages.addToControlFromResourceBundle(
						"preparePromotionForm:general", FacesMessage.SEVERITY_ERROR,  "records.paperJournal.update.error", 	
						FacesContext.getCurrentInstance());
				return;			
			}
		}
		listForPromotionSaved = true;
		facesMessages.addToControlFromResourceBundle(
				"preparePromotionForm:general", FacesMessage.SEVERITY_INFO,  "records.paperJournal.update.success", 	
				FacesContext.getCurrentInstance());
		
		return;
		
	}
	
	public void executeNumberPhDPerIntitutionForPeriod(){
		listNumberPhDPerInstitution = registerEntryDAO.getNumberPhDPerIntitution(dateFrom, dateTo, universityID);
		totalNewProgram = 0;
		totalOldProgram = 0;
		for(NumberPhDPerInstitution obj:listNumberPhDPerInstitution){
			totalNewProgram+=obj.getNumPhDNew();
			totalOldProgram+=obj.getNumPhDOld();
		}
	}
	
	public void checkMaticnaKnjigaForPromotion(){
		
		 FacesContext context = FacesContext.getCurrentInstance();    
		  try {    
		        
		       HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();  
		       HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse(); 
		       List<String> controlNumbersForPromotion = new ArrayList<String>();
		       List<RegisterEntryDTO> selectedForPromotion =  getSelectedForNamedPromotion();
		       for(RegisterEntryDTO reg:selectedForPromotion)
		    	   controlNumbersForPromotion.add(reg.getDissertation().getControlNumber());		       
		       request.setAttribute("controlNumbersForPromotion", controlNumbersForPromotion);
		       RequestDispatcher dispatcher = request.getRequestDispatcher("/MaticnaKnjigaServlet?type=generateReport&forPromotion=yes");  
		       dispatcher.forward(request, response);         
		  }catch (Exception e) {    
		     e.printStackTrace();    
		  }    
		  finally{    
		     context.responseComplete();    
		  }  
	}
	
	
	public void executeShowRegisterEntry(){
//		if(registerType.equals("poDatumu"))
			MaticnaKnjigaReportUtils.generateMaticnaKnjigaPoDatumu((dateFromRegister == null)?new Date(0l):dateFromRegister, (dateToRegister == null)? new Date():dateToRegister, universityID, institutionRegister);
//		else if(registerType.equals("poInstituciji"))
//			MaticnaKnjigaReportUtils.generateMaticnaKnjigaZaInstituciju(institutionRegister);
	}
	
	
	
	
	
	protected boolean init = false;
	protected boolean populateList = true;
	private boolean showListForPrinting = false;
	private boolean showSaveReport = false;
	private boolean listForPromotionSaved = false;
	private String listForPrinting = "";
	private String listForPrintingAdress = "";
	private String listForPrintingForDiploma = "";
	private boolean showRegisterEntryInput = false;
	private String listForPromotion = "";
	private boolean promotionDateSaved = false;
	
	
	public void executeObtainRegisterEntriesForGeneratingId(){
		listForGeneratingIds = registerEntryDAO.getRegisterEntriesForIdGeneration(universityID);
	}
	
	public void generateIds(){
		listForGeneratingIds = MaticnaKnjigaUtils.generateData(listForGeneratingIds, false, registerEntryDAO.getNextAvailableId(universityID), ((listForGeneratingIds != null && listForGeneratingIds.size() > 0)?registerEntryDAO.getNextAvailableAcademicYearNumber(universityID, MaticnaKnjigaUtils.calculateSkolskaGodina(listForGeneratingIds.get(0).getPromotionDate())):1));
	}
	
	public void storeGeneratedIds(){
		for(int i=0;i<listForGeneratingIds.size();i++){
			RegisterEntryDTO entry = listForGeneratingIds.get(i);
			registerEntryDAO.update(entry);
		}
		executeObtainRegisterEntriesForGeneratingId();
	}

	/**
	 * @return the showSaveReport
	 */
	public boolean isShowSaveReport() {
		return showSaveReport;
	}

	/**
	 * @param showSaveReport the showSaveReport to set
	 */
	public void setShowSaveReport(boolean showSaveReport) {
		this.showSaveReport = showSaveReport;
	}


	/**
	 * @return the showListForPrinting
	 */
	public boolean isShowListForPrinting() {
		return showListForPrinting;
	}

	/**
	 * @param showListForPrinting the showListForPrinting to set
	 */
	public void setShowListForPrinting(boolean showListForPrinting) {
		this.showListForPrinting = showListForPrinting;
	}

	/**
	 * @return the listForPrinting
	 */
	public String getListForPrinting() {
		return listForPrinting;
	}

	/**
	 * @param listForPrinting the listForPrinting to set
	 */
	public void setListForPrinting(String listForPrinting) {
		this.listForPrinting = listForPrinting;
	}

	/**
	 * @return the listForPromotionSaved
	 */
	public boolean isListForPromotionSaved() {
		return listForPromotionSaved;
	}

	/**
	 * @param listForPromotionSaved the listForPromotionSaved to set
	 */
	public void setListForPromotionSaved(boolean listForPromotionSaved) {
		this.listForPromotionSaved = listForPromotionSaved;
	}

	/**
	 * @return the distinctPromotionNames
	 */
	public List<String> getDistinctPromotionNames() {
		distinctPromotionNames = new ArrayList<String>();	
		for(RegisterEntryDTO regEntry:list){
			if((regEntry.getFuturePromotionName()!=null) && (!"".equals(regEntry.getFuturePromotionName().trim())))
					if(!distinctPromotionNames.contains(regEntry.getFuturePromotionName()))
						distinctPromotionNames.add(regEntry.getFuturePromotionName());
		}
		if(distinctPromotionNames.size()>0)
			selectedPromotionName = distinctPromotionNames.get(0);
		return distinctPromotionNames;
	}

	/**
	 * @param distinctPromotionNames the distinctPromotionNames to set
	 */
	public void setDistinctPromotionNames(List<String> distinctPromotionNames) {
		this.distinctPromotionNames = distinctPromotionNames;
	}

	/**
	 * @return the selectedPromotionName
	 */
	public String getSelectedPromotionName() {
		return selectedPromotionName;
	}

	/**
	 * @param selectedPromotionName the selectedPromotionName to set
	 */
	public void setSelectedPromotionName(String selectedPromotionName) {
		this.selectedPromotionName = selectedPromotionName;
	}

	/**
	 * @return the choosenView
	 */
	public String getChoosenView() {
		return choosenView;
	}

	/**
	 * @param choosenView the choosenView to set
	 */
	public void setChoosenView(String choosenView) {
		this.choosenView = choosenView;
	}


	/**
	 * @return the listForPrintingAdress
	 */
	public String getListForPrintingAdress() {
		return listForPrintingAdress;
	}

	/**
	 * @param listForPrintingAdress the listForPrintingAdress to set
	 */
	public void setListForPrintingAdress(String listForPrintingAdress) {
		this.listForPrintingAdress = listForPrintingAdress;
	}

	/**
	 * @return the showRegisterEntryInput
	 */
	public boolean isShowRegisterEntryInput() {
		return showRegisterEntryInput;
	}

	/**
	 * @param showRegisterEntryInput the showRegisterEntryInput to set
	 */
	public void setShowRegisterEntryInput(boolean showRegisterEntryInput) {
		this.showRegisterEntryInput = showRegisterEntryInput;
	}

	/**
	 * @return the promotionDate
	 */
	public Date getPromotionDate() {
		return promotionDate;
	}

	/**
	 * @param promotionDate the promotionDate to set
	 */
	public void setPromotionDate(Date promotionDate) {
		this.promotionDate = promotionDate;
	}

	/**
	 * @return the listForPromotion
	 */
	public String getListForPromotion() {
		return listForPromotion;
	}

	/**
	 * @param listForPromotion the listForPromotion to set
	 */
	public void setListForPromotion(String listForPromotion) {
		this.listForPromotion = listForPromotion;
	}

	/**
	 * @return the promotionDateSaved
	 */
	public boolean isPromotionDateSaved() {
		return promotionDateSaved;
	}

	/**
	 * @param promotionDateSaved the promotionDateSaved to set
	 */
	public void setPromotionDateSaved(boolean promotionDateSaved) {
		this.promotionDateSaved = promotionDateSaved;
	}

	/**
	 * @return the listForPrintingForDiploma
	 */
	public String getListForPrintingForDiploma() {
		return listForPrintingForDiploma;
	}

	/**
	 * @param listForPrintingForDiploma the listForPrintingForDiploma to set
	 */
	public void setListForPrintingForDiploma(String listForPrintingForDiploma) {
		this.listForPrintingForDiploma = listForPrintingForDiploma;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public List<NumberPhDPerInstitution> getListNumberPhDPerInstitution() {
		return listNumberPhDPerInstitution;
	}

	public void setListNumberPhDPerInstitution(
			List<NumberPhDPerInstitution> listNumberPhDPerInstitution) {
		this.listNumberPhDPerInstitution = listNumberPhDPerInstitution;
	}

	public int getTotalOldProgram() {
		return totalOldProgram;
	}

	public void setTotalOldProgram(int totalOldProgram) {
		this.totalOldProgram = totalOldProgram;
	}

	public int getTotalNewProgram() {
		return totalNewProgram;
	}

	public void setTotalNewProgram(int totalNewProgram) {
		this.totalNewProgram = totalNewProgram;
	}

	public String getRegisterType() {
		return registerType;
	}

	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}

	public Date getDateFromRegister() {
		return dateFromRegister;
	}

	public void setDateFromRegister(Date dateFromRegister) {
		this.dateFromRegister = dateFromRegister;
	}

	public Date getDateToRegister() {
		return dateToRegister;
	}

	public void setDateToRegister(Date dateToRegister) {
		this.dateToRegister = dateToRegister;
	}

	public String getInstitutionRegister() {
		return institutionRegister;
	}

	public void setInstitutionRegister(String institutionRegister) {
		this.institutionRegister = institutionRegister;
	}

	public List<String> getDistinctInstitutions() {
		return distinctInstitutions;
	}

	public void setDistinctInstitutions(List<String> distinctInstitutions) {
		this.distinctInstitutions = distinctInstitutions;
	}

	public String getUniversityID() {
		return universityID;
	}

	public void setUniversityID(String universityID) {
		this.universityID = universityID;
	}

	/**
	 * @return the listForGeneratingIds
	 */
	public List<RegisterEntryDTO> getListForGeneratingIds() {
		if(listForGeneratingIds == null)
			executeObtainRegisterEntriesForGeneratingId();
		return listForGeneratingIds;
	}

	/**
	 * @param listForGeneratingIds the listForGeneratingIds to set
	 */
	public void setListForGeneratingIds(List<RegisterEntryDTO> listForGeneratingIds) {
		this.listForGeneratingIds = listForGeneratingIds;
	}

	/**
	 * @return the institution
	 */
	public String getInstitution() {
		return institution;
	}

	/**
	 * @param institution the institution to set
	 */
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	
	
	
	
	
	

	
}
