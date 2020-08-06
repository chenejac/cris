package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringEscapeUtils;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileDTO;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers.DublinCoreXMLSerializer;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers.MARC21slimXMLSerializer;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers.Serializer;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordRecord;

/**
 * Abstract class which must be extended by DTO classes which present some type
 * of bibliographic or normative MARC21Record.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public abstract class RecordDTO implements Serializable {

	protected String controlNumber;
	protected Record record;
	protected String scopusID = null;
	protected String ORCID = null;
	protected Locale locale;
	protected boolean notLoaded = false;
	protected String stringRepresentation="";
	protected String HTMLRepresentation="";
	protected String harvardRepresentation="";
	protected String MARC21="";
	protected String dublinCore="";
	protected List<FileDTO> files;
	protected List<FileDTO> deletedFiles;
	
	protected Float relevanceScore = new Float(0f);
	protected Float recommendationScore = new Float(0f);
	protected Float mixedScore = new Float(0f);
	
	protected List<RecordDTO> relatedRecords;
	
	public RecordDTO() {
		super();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		locale = (facesContext==null)?new Locale("sr"):facesContext.getViewRoot().getLocale();	
		record = new Record();
		relatedRecords = null;
		files = new ArrayList<FileDTO>();
		deletedFiles = new ArrayList<FileDTO>();
	}

	/**
	 * @param controlNumber
	 */
	public RecordDTO(String controlNumber) {
		super();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		locale = (facesContext==null)?new Locale("sr"):facesContext.getViewRoot().getLocale();
		this.controlNumber = controlNumber;
		record = new Record();
		relatedRecords = null;
		files = new ArrayList<FileDTO>();
		deletedFiles = new ArrayList<FileDTO>();
	}



	/**
	 * @return controlNumber of the mARC21Record
	 */
	public String getControlNumber() {
		return controlNumber;
	}

	/**
	 * Set controlNumber of the mARC21Record.
	 * 
	 * @param controlNumber
	 *            ControlNumber of the mARC21Record
	 */
	public void setControlNumber(String controlNumber) {
		this.controlNumber = controlNumber;
	}

	/**
	 * @return record
	 */
	public Record getRecord() {
		return record;
	}

	/**
	 * Set record.
	 * 
	 * @param record
	 *            record according to MARC21 format and CERIF
	 */
	public void setRecord(Record record) {
		this.record = record;
	}
	
	
		
	/**
	 * @return the scopusID
	 */
	public String getScopusID() {
		return scopusID;
	}

	/**
	 * @param scopusID the scopusID to set
	 */
	public void setScopusID(String scopusID) {
		this.scopusID = scopusID;
	}
	
	

	/**
	 * @return the oRCID
	 */
	public String getORCID() {
		return ORCID;
	}

	/**
	 * @param oRCID the oRCID to set
	 */
	public void setORCID(String oRCID) {
		ORCID = oRCID;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		locale = (facesContext==null)?new Locale("sr"):facesContext.getViewRoot().getLocale();
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @return the notLoaded
	 */
	public boolean isNotLoaded() {
		return notLoaded;
	}

	/**
	 * @param notLoaded the notLoaded to set
	 */
	public void setNotLoaded(boolean notLoaded) {
		this.notLoaded = notLoaded;
		if(notLoaded){
			clear();
		}
	}

	/**
	 * @return the string representation of a record - human readable
	 */
	public String getStringRepresentation() {
		return this.toString();
	}
	
	/**
	 * @return the string representation of a record - human readable
	 */
	public String getStringRepresentation(String institutinonId) {
		return this.toString(institutinonId);
	}
	
	/**
	 * @param stringRepresentation the stringRepresentation to set
	 */
	public void setStringRepresentation(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}
	
	
	/**
	 * @return the HTML representation of a record - human readable
	 */
	public String getHTMLRepresentation() {
		return HTMLRepresentation;
	}


	/**
	 * @param hTMLRepresentation the hTMLRepresentation to set
	 */
	public void setHTMLRepresentation(String hTMLRepresentation) {
		HTMLRepresentation = hTMLRepresentation;
	}
	
	/**
	 * @return the harvardRepresentation
	 */
	public String getHarvardRepresentation() {
		if("".equals(harvardRepresentation))
			return getStringRepresentation();
		else
			return harvardRepresentation;
	}

	/**
	 * @param harvardRepresentation the harvardRepresentation to set
	 */
	public void setHarvardRepresentation(String harvardRepresentation) {
		this.harvardRepresentation = harvardRepresentation;
	}

	/**
	 * @return the relatedRecords
	 */
	public List<RecordDTO> getRelatedRecords() {
		if(relatedRecords == null){
			loadRelatedRecords();
		}
		return relatedRecords;
	}

	/**
	 * @param relatedRecords the relatedRecords to set
	 */
	public void setRelatedRecords(List<RecordDTO> relatedRecords) {
		this.relatedRecords = relatedRecords;
	}
	
	public void addRelatedRecord(RecordDTO record){
		if(relatedRecords == null){
			loadRelatedRecords();
		}
		relatedRecords.add(record);
	}
	
	public void removeRelatedRecord(RecordDTO record){
		if(relatedRecords == null){
			loadRelatedRecords();
		}
		relatedRecords.remove(record);
	}
	
	protected void loadRelatedRecords(){
		relatedRecords = new ArrayList<RecordDTO>();
		if(controlNumber != null){
			if(notLoaded)
				record.loadFromDatabase();
			List<String> recordsControlNumbers = new ArrayList<String>(); 
			for (RecordRecord recordRecord : record.getRelationsThisRecordOtherRecords()) {
				if(recordRecord.getCfClassSchemeId().equals("authorshipType")){
					if((recordRecord.getCfClassId().equals("is author of")) || (recordRecord.getCfClassId().equals("is editor of")) || (recordRecord.getCfClassId().equals("is advisor of")) || (recordRecord.getCfClassId().equals("is committee member of"))){
						recordsControlNumbers.add(recordRecord.getRecordId());
					}
				} 
			}
			for (RecordRecord recordRecord : record.getRelationsOtherRecordsThisRecord()) {
				if(! (recordRecord.getCfClassSchemeId().equals("authorshipType"))){
					recordsControlNumbers.add(recordRecord.getRecordId());
				} 
			}
			RecordDAO recordDAO = new RecordDAO(new RecordDB());
			List<Record> records = recordDAO.getRecords(recordsControlNumbers);
			for (Record temp : records) {
				relatedRecords.add(temp.getDto());
			}
		}
	}
	
	
	
	/**
	 * @return the files
	 */
	public List<FileDTO> getFiles() {
		return files;
	}

	/**
	 * @param files the files to set
	 */
	public void setFiles(List<FileDTO> files) {
		this.files = files;
	}

	/**
	 * @return the deletedFiles
	 */
	public List<FileDTO> getDeletedFiles() {
		return deletedFiles;
	}

	/**
	 * @param deletedFiles the deletedFiles to set
	 */
	public void setDeletedFiles(List<FileDTO> deletedFiles) {
		this.deletedFiles = deletedFiles;
	}

	public abstract void clear();
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean retVal = true;
		RecordDTO dto = (RecordDTO) obj;
		try {
			if (!(dto.controlNumber.equals(this.controlNumber))) {
				retVal = false;
			}
		} catch (Exception e) {
			retVal = false;
		}
		return retVal;
	}
	
	public String getMARC21(){
		if(notLoaded){
			record.loadFromDatabase();
		} 
//		else {
			Serializer serializer = new MARC21slimXMLSerializer();
			String retVal = serializer.fromRecord(record, "");
			retVal = retVal.replace("\n", "tempEnterCRIS").replace("\t", "tempTabCRIS");
			retVal = StringEscapeUtils.escapeHtml(retVal);
			retVal = retVal.replace("tempEnterCRIS", "<br/>").replace("tempTabCRIS", "&nbsp;&nbsp;&nbsp;");
			return retVal;
//		}
	}
	
	/**
	 * @param mARC21 the mARC21 to set
	 */
	public void setMARC21(String mARC21) {
//		MARC21 = mARC21;
	}

	public String getDublinCore(){
		if(notLoaded){
			record.loadFromDatabase();
		} 
		//		if(notLoaded){
//			return dublinCore;
//		} 
//		else {
			Serializer serializer = new DublinCoreXMLSerializer();
			String retVal = serializer.fromRecord(record, "");
			retVal = retVal.replace("\n", "tempEnterCRIS").replace("\t", "tempTabCRIS");
			retVal = StringEscapeUtils.escapeHtml(retVal);
			retVal = retVal.replace("tempEnterCRIS", "<br/>").replace("tempTabCRIS", "&nbsp;&nbsp;&nbsp;");
			return retVal;
//		}
	}
	
	/**
	 * @param dublinCore the dublinCore to set
	 */
	public void setDublinCore(String dublinCore) {
//		this.dublinCore = dublinCore;
	}
	
	public String getKnrXML(String indent, ResultMeasureDTO resultMeasure){
		return "";
	}

	/**
	 * @return the relevanceScore
	 */
	public Float getRelevanceScore() {
		return relevanceScore;
	}

	/**
	 * @param relevanceScore the relevanceScore to set
	 */
	public void setRelevanceScore(Float relevanceScore) {
		this.relevanceScore = relevanceScore;
	}

	/**
	 * @return the recommendationScore
	 */
	public Float getRecommendationScore() {
		return recommendationScore;
	}

	/**
	 * @param recommendationScore the recommendationScore to set
	 */
	public void setRecommendationScore(Float recommendationScore) {
		this.recommendationScore = recommendationScore;
	}

	/**
	 * @return the mixedScore
	 */
	public Float getMixedScore() {
		return (relevanceScore + recommendationScore)/2.0f;
	}

	/**
	 * @param mixedScore the mixedScore to set
	 */
	public void setMixedScore(Float mixedScore) {
		this.mixedScore = mixedScore;
	}
	
	public abstract String toString(String id); 
	
	
	
}
