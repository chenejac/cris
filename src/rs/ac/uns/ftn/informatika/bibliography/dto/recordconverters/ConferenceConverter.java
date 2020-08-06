package rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters;

import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.dto.ConferenceDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.DataField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.Subfield;
import rs.ac.uns.ftn.informatika.bibliography.utils.Sanitizer;

/**
 * Class for converting normative mARC21Record between MARC21 format and ConferenceDTO
 * object.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class ConferenceConverter extends ANormativeRecordConverter {

	/**
	 * Convert the mARC21Record with data about Conference from MARC21 format to DTO
	 * format
	 * 
	 * @param rec
	 *            MARC21Record which should be converted
	 * 
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.AbstractRecordConverter#getDTO(rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record)
	 * 
	 * @return true if everything is ok, otherwise false
	 */
	@Override
	public boolean getDTO(Record rec){
		
		MARC21Record mARC21Record = rec.getMARC21Record();
		if(rec.getDto() == null)
			rec.setDto(ConverterFactory.getDTO(rec.getDtoType()));
		ConferenceDTO retVal = (ConferenceDTO)rec.getDto();
		retVal.setControlNumber(mARC21Record.getControlNumber());
		retVal.setScopusID(rec.getScopusID());
		String originalLanguage = MultilingualContentDTO.LANGUAGE_SERBIAN;
		DataField df040 = mARC21Record.getDataField("040");
		try {
			Subfield b = df040.getSubfield('b');
			originalLanguage = b.getContent();
		} catch (Exception e) {
		}
		
		DataField df111 = mARC21Record.getDataField("111");
		Subfield sub6 = df111.getSubfield('6');
		String ordNum = null;
		if(sub6 != null){
			ordNum = sub6.getContent().split("\\-")[1];
		}
		
		if (df111.getInd1() == '2') {
			try {
				Subfield a = df111.getSubfield('a');
				retVal.getName().setLanguage(originalLanguage);
				retVal.getName().setContent(a.getContent());
			} catch (Exception e) {
			}
			try {
				Subfield d = df111.getSubfield('d');
				retVal.setYear(Integer.parseInt(d.getContent()));
			} catch (Exception e) {
				return false;
			}
			try {
				Subfield c = df111.getSubfield('c');
				String content = c.getContent().trim();
				String place = Sanitizer.nextPar(content, ',');
				content = Sanitizer.remPar(content, ',');
				String state = Sanitizer.nextPar(content, ',');
				content = Sanitizer.remPar(content, ',');
				retVal.setPlace(place);
				retVal.setState(state);
			} catch (Exception e) {
			}
			try {
				Subfield n = df111.getSubfield('n');
				retVal.setNumber(n.getContent());
			} catch (Exception e) {
			}
			try {
				Subfield g = df111.getSubfield('g');
				retVal.setFee(g.getContent());
			} catch (Exception e) {
			}
		} else
			return false;
		
		if(ordNum != null){
			List<DataField> dfs880 = mARC21Record.getDataFields("880");
			List<MultilingualContentDTO> nameTranslations = new ArrayList<MultilingualContentDTO>();
			for (DataField dataField : dfs880) {
				try {
					if (dataField.getSubfield('6').getContent().equals("111-"+ordNum)) {
						Subfield a = dataField.getSubfield('a');
						String content = a.getContent().trim();
						String transType = content.substring(0, 1);
						String language = content.substring(2, 5);
						String transContent = content.substring(6);
						MultilingualContentDTO nameTranslation = new MultilingualContentDTO(transContent, language, transType);
						nameTranslations.add(nameTranslation);
					}
				} catch (Exception e) {
				}
			}
			retVal.setNameTranslations(nameTranslations);
		}
		
		DataField df148 = mARC21Record.getDataField("148");
		try {
			Subfield a = df148.getSubfield('a');
			if ((a!=null) && (a.getContent()!=null)){
				retVal.setPeriod(a.getContent());
			}
		} catch (Exception e) {
		}
		
		try {
			ordNum = null;
			DataField df680 = mARC21Record.getDataField("680");
			sub6 = df680.getSubfield('6');
			if(sub6 != null){
				ordNum = sub6.getContent().split("\\-")[1];
			}
			
			Subfield i = df680.getSubfield('i');
			if ((i!=null) && (i.getContent()!=null)){
				retVal.getDescription().setLanguage(originalLanguage);
				retVal.getDescription().setContent(i.getContent());
			}
		} catch (Exception e) {
		}	
		
		if(ordNum != null){
			List<DataField> dfs880 = mARC21Record.getDataFields("880");
			List<MultilingualContentDTO> descriptionTranslations = new ArrayList<MultilingualContentDTO>();
			for (DataField dataField : dfs880) {
				try {
					if (dataField.getSubfield('6').getContent().equals("680-"+ordNum)) {
						Subfield i = dataField.getSubfield('i');
						if(i!=null){
							String content = i.getContent().trim();
							String transType = content.substring(0, 1);
							String language = content.substring(2, 5);
							String transContent = content.substring(6);
							MultilingualContentDTO descriptionTranslation = new MultilingualContentDTO(transContent, language, transType);
							descriptionTranslations.add(descriptionTranslation);
						}
					}
				} catch (Exception e) {
				}
			}
			retVal.setDescriptionTranslations(descriptionTranslations);
		}
		
		
		DataField df856 = mARC21Record.getDataField("856");
		try {
			Subfield u = df856.getSubfield('u');
			if ((u!=null) && (u.getContent()!=null)){
				retVal.setUri(u.getContent());
			}
		} catch (Exception e) {
		}
		
		for (MultilingualContentDTO keywords : rec.getRecordKeywords()) {
			if(keywords.getTransType().equals(MultilingualContentDTO.TRANS_ORIGINAL)){
				retVal.setKeywords(keywords);
			} else {
				retVal.getKeywordsTranslations().add(keywords);
			}	
		}
		
//		retVal.setClassifications(rec.getRecordClasses());
		return true;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ANormativeRecordConverter#setDataFields(rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record,
	 *      rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setDataFields(MARC21Record rec, RecordDTO dto) {
		
		Integer ordNum = 0;
		
		ConferenceDTO conferenceDTO = (ConferenceDTO) dto;

		DataField data040 = new DataField("040", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		Subfield b = new Subfield('b');
		if(conferenceDTO.getName().getLanguage() != null)
			b.setContent(conferenceDTO.getName().getLanguage());
		else 
			b.setContent(MultilingualContentDTO.LANGUAGE_SERBIAN);
		data040.addSubfield(b);
		rec.addDataField(data040);
		
		DataField data111 = new DataField("111", '2', AbstractRecordConverter.BLANK);
		Subfield sub6 = new Subfield('6');
		if(conferenceDTO.getNameTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data111.addSubfield(sub6);
		}
		StringBuffer tempContent = new StringBuffer();
		Subfield a = new Subfield('a');
		if ((conferenceDTO.getName().getContent() != null)
				&& (!"".equals(conferenceDTO.getName().getContent())))
			tempContent.append(conferenceDTO.getName().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data111.addSubfield(a);
		}
		tempContent = new StringBuffer();
		Subfield c = new Subfield('c');
		if ((conferenceDTO.getPlace() != null)
				&& (!"".equals(conferenceDTO.getPlace())))
			tempContent.append(Sanitizer.sanitize(conferenceDTO.getPlace()));
		if ((conferenceDTO.getState() != null)
				&& (!"".equals(conferenceDTO.getState())))
			tempContent.append(", " + Sanitizer.sanitize(conferenceDTO.getState()));
		if (tempContent.toString().length() > 0) {
			c.setContent(tempContent.toString());
			data111.addSubfield(c);
		}
		tempContent = new StringBuffer();
		Subfield d = new Subfield('d');
		if (conferenceDTO.getYear() != null)
			tempContent.append(conferenceDTO.getYear().toString());
		if (tempContent.toString().length() > 0) {
			d.setContent(tempContent.toString());
			data111.addSubfield(d);
		}
		tempContent = new StringBuffer();
		Subfield g = new Subfield('g');
		if (conferenceDTO.getFee() != null)
			tempContent.append(conferenceDTO.getFee());
		if (tempContent.toString().length() > 0) {
			g.setContent(tempContent.toString());
			data111.addSubfield(g);
		}
		tempContent = new StringBuffer();
		Subfield n = new Subfield('n');
		if (conferenceDTO.getNumber() != null)
			tempContent.append(conferenceDTO.getNumber().toString());
		if (tempContent.toString().length() > 0) {
			n.setContent(tempContent.toString());
			data111.addSubfield(n);
		}
		if(data111.getSubfieldCount() > 0)
			rec.addDataField(data111);
		
		for (MultilingualContentDTO nameTranslation : conferenceDTO.getNameTranslations()) {
			DataField data880 = new DataField("880", '2', AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			sub6 = new Subfield('6');
			sub6.setContent("111-0" + ordNum);
			data880.addSubfield(sub6);
			a = new Subfield('a');
			a.setContent(nameTranslation.getTransType() + "-" + nameTranslation.getLanguage() + ":" + nameTranslation.getContent());
			data880.addSubfield(a);
			rec.addDataField(data880);
		}
		
		DataField data148 = new DataField("148", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((conferenceDTO.getPeriod() != null)
				&& (!"".equals(conferenceDTO.getPeriod().trim())))
			tempContent.append(conferenceDTO.getPeriod());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data148.addSubfield(a);
		}
		if(data148.getSubfieldCount() > 0)
			rec.addDataField(data148);
		
		DataField data680 = new DataField("680", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		sub6 = new Subfield('6');
		if(conferenceDTO.getDescriptionTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data680.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		Subfield i = new Subfield('i');
		if ((conferenceDTO.getDescription().getContent() != null)
				&& (!"".equals(conferenceDTO.getDescription().getContent())))
			tempContent.append(conferenceDTO.getDescription().getContent());
		if (tempContent.toString().length() > 0) {
			i.setContent(tempContent.toString());
			data680.addSubfield(i);
		}
		if(data680.getSubfieldCount() > 0)
			rec.addDataField(data680);
		
		for (MultilingualContentDTO descriptionTranslation : conferenceDTO.getDescriptionTranslations()) {
			DataField data880 = new DataField("880", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			sub6 = new Subfield('6');
			sub6.setContent("680-0" + ordNum);
			data880.addSubfield(sub6);
			i = new Subfield('i');
			i.setContent(descriptionTranslation.getTransType() + "-" + descriptionTranslation.getLanguage() + ":" + descriptionTranslation.getContent());
			data880.addSubfield(i);
			rec.addDataField(data880);
		}
		
		DataField data856 = new DataField("856", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		Subfield u = new Subfield('u');
		if ((conferenceDTO.getUri() != null)
				&& (!"".equals(conferenceDTO.getUri())))
			tempContent.append(conferenceDTO.getUri());
		if (tempContent.toString().length() > 0) {
			u.setContent(tempContent.toString());
			data856.addSubfield(u);
		}
		if(data856.getSubfieldCount() > 0)
			rec.addDataField(data856);
		
	}

	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ANormativeRecordConverter#setRecordKeywords(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRecordKeywords(Record retVal, RecordDTO dto) {
		ConferenceDTO conference = (ConferenceDTO)dto;
		retVal.setRecordKeywords(new ArrayList<MultilingualContentDTO>());
		if((conference.getKeywords().getContent() != null) && (conference.getKeywords().getContent().trim().length() > 0)){
			if(conference.getKeywords().getLanguage() == null)
				conference.getKeywords().setLanguage(conference.getName().getLanguage());
			retVal.getRecordKeywords().add(conference.getKeywords());
		}
		for (MultilingualContentDTO keywords : conference.getKeywordsTranslations()) {
			retVal.getRecordKeywords().add(keywords);
		}
	}

}
