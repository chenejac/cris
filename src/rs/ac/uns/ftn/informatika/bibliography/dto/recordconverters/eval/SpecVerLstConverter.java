package rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.eval;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.SpecVerLstDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter;
import rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.AbstractRecordConverter;
import rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ConverterFactory;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.ControlField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.DataField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.Subfield;

/**
 * Class for converting bibliographic mARC21Record between MARC21 format and
 * SpecVerLstDTO object.
 * 
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 */
@SuppressWarnings("serial")
public class SpecVerLstConverter extends ABibliographicRecordConverter {

	/**
	 * Converts the mARC21Record with bibliographic data about RuleBook from MARC21 format to DTO format
	 * 
	 * @param rec
	 *            MARC21Record which should be converted
	 * 
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.AbstractRecordConverter#getDTO(rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record)
	 * 
	 * @return true if everything is ok, otherwise false
	 */
	@Override
	public boolean getDTO(Record rec) {
		
//		System.out.println("SpecVerLstConverter - getDTO - 1");
		
		MARC21Record mARC21Record = rec.getMARC21Record();
		if(rec.getDto() == null)
			rec.setDto(ConverterFactory.getDTO(rec.getDtoType()));
		
		SpecVerLstDTO retVal = (SpecVerLstDTO)rec.getDto();
		retVal.setControlNumber(mARC21Record.getControlNumber());
		
//		System.out.println("SpecVerLstConverter - getDTO - 2");

		//za smestanje fajlova nema jer je prebaceno preko FileDAOs
		
//		System.out.println("SpecVerLstConverter - getDTO - 3");
		
		String originalLanguage = MultilingualContentDTO.LANGUAGE_SERBIAN;
		try{
			ControlField cf008 = mARC21Record.getControlField("008");
			originalLanguage = cf008.getCharPositions(35, 3);
//			System.out.println("SpecVerLstConverter - getDTO - 4");
		} catch (Exception e){
			
		}

		DataField df046 = mARC21Record.getDataField("046");
		try {
			Subfield m = df046.getSubfield('m');
			String dateString = m.getContent();
			DateFormat formatter ; 
		    Date date ; 
		    formatter = new SimpleDateFormat("dd.MM.yyyy");
		    date = (Date)formatter.parse(dateString); 
		    Calendar cal=GregorianCalendar.getInstance();
		    cal.setTime(date);
			retVal.setStartDate(cal);
//			System.out.println("SpecVerLstConverter - getDTO - 5");
		} catch (Exception e) {
			try {
				Subfield m = df046.getSubfield('m');
				String yearString = m.getContent();
				Calendar cal = new GregorianCalendar();
				cal.set(Calendar.YEAR, Integer.parseInt(yearString));
				cal.set(Calendar.DAY_OF_YEAR, 1);
				retVal.setStartDate(cal);
			} catch (Exception ex) {
			}
		}
		
		try {
			Subfield n = df046.getSubfield('n');
			String dateString = n.getContent();
			DateFormat formatter ; 
		    Date date ; 
		    formatter = new SimpleDateFormat("dd.MM.yyyy");
		    date = (Date)formatter.parse(dateString); 
		    Calendar cal=GregorianCalendar.getInstance();
		    cal.setTime(date);
			retVal.setEndDate(cal);
//			System.out.println("SpecVerLstConverter - getDTO - 6");
		} catch (Exception e) {
			try {
				Subfield n = df046.getSubfield('n');
				String yearString = n.getContent();
				Calendar cal = new GregorianCalendar();
				cal.set(Calendar.YEAR, Integer.parseInt(yearString));
				cal.set(Calendar.DAY_OF_YEAR, 1);
				retVal.setStartDate(cal);
			} catch (Exception ex) {
			}
		}
		
		DataField df084 = mARC21Record.getDataField("084");
		try {
			Subfield b = df084.getSubfield('b');
			retVal.setSpecVerLstUserCode(b.getContent());
//			System.out.println("SpecVerLstConverter - getDTO - 7");
		} catch (Exception e) {
		}
		
		DataField df245 = mARC21Record.getDataField("245");
		String ordNum = null;
//		System.out.println("SpecVerLstConverter - getDTO - 8");
		
		if(df245!=null){
			Subfield sub6 = df245.getSubfield('6');
			 
			if(sub6 != null){
					ordNum = sub6.getContent().split("\\-")[1];
			}
			
			if ((df245.getInd1() == '0') && (df245.getInd2() == '0')) {
				try {
					Subfield a = df245.getSubfield('a');
					retVal.getName().setLanguage(originalLanguage);
					retVal.getName().setContent(a.getContent());
//					System.out.println("SpecVerLstConverter - getDTO - 9");
				} catch (Exception e) {
				}
			} else {
				return false;
			}
		}
		
		if(ordNum != null){
			List<DataField> dfs880 = mARC21Record.getDataFields("880");
			List<MultilingualContentDTO> nameTranslations = new ArrayList<MultilingualContentDTO>();
			for (DataField dataField : dfs880) {
				try {
					if (dataField.getSubfield('6').getContent().equals("245-"+ordNum)) {
						Subfield a = dataField.getSubfield('a');
						if(a!=null){
							String content = a.getContent().trim();
							String transType = content.substring(0, 1);
							String language = content.substring(2, 5);
							String transContent = content.substring(6);
							MultilingualContentDTO nameTranslation = new MultilingualContentDTO(transContent, language, transType);
							nameTranslations.add(nameTranslation);
//							System.out.println("SpecVerLstConverter - getDTO - 10");
						}
					}
				} catch (Exception e) {
				}
			}
			retVal.setNameTranslations(nameTranslations);
		}

		try {
			ordNum = null;
			DataField df653 = mARC21Record.getDataField("653");
			Subfield sub6 = df653.getSubfield('6');
			if(sub6 != null){
				ordNum = sub6.getContent().split("\\-")[1];
			}
		
			Subfield a = df653.getSubfield('a');
			retVal.getKeywords().setLanguage(originalLanguage);
			retVal.getKeywords().setContent(a.getContent());
//			System.out.println("SpecVerLstConverter - getDTO - 13");
		} catch (Exception e) {
		}		 
		
		if(ordNum != null){
			List<DataField> dfs880 = mARC21Record.getDataFields("880");
			List<MultilingualContentDTO> keywordsTranslations = new ArrayList<MultilingualContentDTO>();
			for (DataField dataField : dfs880) {
				try {
					if (dataField.getSubfield('6').getContent().equals("653-"+ordNum)) {
						Subfield a = dataField.getSubfield('a');
						String content = a.getContent().trim();
						String transType = content.substring(0, 1);
						String language = content.substring(2, 5);
						String transContent = content.substring(6);
						MultilingualContentDTO keywordsTranslation = new MultilingualContentDTO(transContent, language, transType);
						keywordsTranslations.add(keywordsTranslation);
//						System.out.println("SpecVerLstConverter - getDTO - 14");
					}
				} catch (Exception e) {
				}
			}
			retVal.setKeywordsTranslations(keywordsTranslations);
		}
//		System.out.println("SpecVerLstConverter - getDTO - 15");
		return true;	
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#setDataFields(rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record,
	 *      rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setDataFields(MARC21Record rec, RecordDTO dto) {
		
//		System.out.println("SpecVerLstConverter - setDataFields - 1");
		
		Integer ordNum = 0;
		SpecVerLstDTO specVerLstDTO = (SpecVerLstDTO) dto;
		
		DataField df046 = new DataField("046", AbstractRecordConverter.BLANK,	AbstractRecordConverter.BLANK);
		StringBuffer tempContent = new StringBuffer();
		Subfield m = new Subfield('m');
		if (specVerLstDTO.getStartDate() != null){
			try{
				String startDate = null;
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				startDate = sdf.format(specVerLstDTO.getStartDate().getTime());
				tempContent.append(startDate);
//				System.out.println("SpecVerLstConverter - setDataFields - 2");
			} catch (Exception e) {
			}
		}
		if (tempContent.toString().length() > 0) {
			m.setContent(tempContent.toString());
			df046.addSubfield(m);
		}
		tempContent = new StringBuffer();
		Subfield n = new Subfield('n');
		if (specVerLstDTO.getEndDate() != null){
			try{
				String endDate = null;
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				endDate = sdf.format(specVerLstDTO.getEndDate().getTime());
				tempContent.append(endDate);
//				System.out.println("SpecVerLstConverter - setDataFields - 3");
			} catch (Exception e) {
			}
		}
		if (tempContent.toString().length() > 0) {
			n.setContent(tempContent.toString());
			df046.addSubfield(n);
//			System.out.println("SpecVerLstConverter - setDataFields - 4");
		}
		
		if(df046.getSubfieldCount() > 0)
			rec.addDataField(df046);
		
//		System.out.println("SpecVerLstConverter - setDataFields - 5");
		
		DataField data084 = new DataField("084", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		Subfield b = new Subfield('b');
		if ((specVerLstDTO.getSpecVerLstUserCode() != null)
				&& (!"".equals(specVerLstDTO.getSpecVerLstUserCode().trim()))){
			tempContent.append(specVerLstDTO.getSpecVerLstUserCode());
//			System.out.println("SpecVerLstConverter - setDataFields - 6");
		}

		if (tempContent.toString().length() > 0) {
			b.setContent(tempContent.toString());
			data084.addSubfield(b);
//			System.out.println("SpecVerLstConverter - setDataFields - 7");
		}
		if(data084.getSubfieldCount() > 0)
		rec.addDataField(data084);
		
		DataField data245 = new DataField("245", '0', '0');
		tempContent = new StringBuffer();
		Subfield a = new Subfield('a');
		if ((specVerLstDTO.getName().getContent() != null)
				&& (!"".equals(specVerLstDTO.getName().getContent()))){
			tempContent.append(specVerLstDTO.getName().getContent());
//			System.out.println("SpecVerLstConverter - setDataFields - 8");
		}
		Subfield sub6 = new Subfield('6');
		if(specVerLstDTO.getNameTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data245.addSubfield(sub6);
//			System.out.println("SpecVerLstConverter - setDataFields - 9");
		}
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data245.addSubfield(a);
//			System.out.println("SpecVerLstConverter - setDataFields - 10");
		}
		if(data245.getSubfieldCount() > 0)
			rec.addDataField(data245);
		
		for (MultilingualContentDTO nameTranslation : specVerLstDTO.getNameTranslations()) {
			DataField data880 = new DataField("880", '0', '0');
			tempContent = new StringBuffer();
			sub6 = new Subfield('6');
			sub6.setContent("245-0" + ordNum);
			data880.addSubfield(sub6);
			a = new Subfield('a');
			a.setContent(nameTranslation.getTransType() + "-" + nameTranslation.getLanguage() + ":" + nameTranslation.getContent());
			data880.addSubfield(a);
			rec.addDataField(data880);
		}
		
		DataField data653 = new DataField("653", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((specVerLstDTO.getKeywords().getContent() != null)
				&& (!"".equals(specVerLstDTO.getKeywords().getContent()))){
			tempContent.append(specVerLstDTO.getKeywords().getContent());
//			System.out.println("SpecVerLstConverter - setDataFields - 11");
		}
		sub6 = new Subfield('6');
		if(specVerLstDTO.getKeywordsTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data653.addSubfield(sub6);
//			System.out.println("SpecVerLstConverter - setDataFields - 12");
		}
		
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data653.addSubfield(a);
		}
		
		if(data653.getSubfieldCount() > 0)
			rec.addDataField(data653);
		
		for (MultilingualContentDTO keywordsTranslation : specVerLstDTO.getKeywordsTranslations()) {
			DataField data880 = new DataField("880", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			sub6 = new Subfield('6');
			sub6.setContent("653-0" + ordNum);
			data880.addSubfield(sub6);
			a = new Subfield('a');
			a.setContent(keywordsTranslation.getTransType() + "-" + keywordsTranslation.getLanguage() + ":" + keywordsTranslation.getContent());
			data880.addSubfield(a);
			rec.addDataField(data880);
		}
//		System.out.println("SpecVerLstConverter - setDataFields - 13");
		
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#setRelationsOtherRecordsThisRecord(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRelationsOtherRecordsThisRecord(Record retVal,
			RecordDTO dto) {
		// TODO Auto-generated method stub
		super.setRelationsOtherRecordsThisRecord(retVal, dto);
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#setRelationsThisRecordOtherRecords(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRelationsThisRecordOtherRecords(Record retVal,
			RecordDTO dto) {
		// TODO Auto-generated method stub
		super.setRelationsThisRecordOtherRecords(retVal, dto);
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#getControlField006(rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected ControlField getControlField006(RecordDTO dto) {
	
		return null;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#getControlField007(rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected ControlField getControlField007(RecordDTO dto) {
		
		return null;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#getControlField008(rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected ControlField getControlField008(RecordDTO dto) {
		
		SpecVerLstDTO specVerLst = (SpecVerLstDTO) dto;
		ControlField retVal = new ControlField("008");
		String content = "";
		for(int i=0;i<35;i++)
			content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
		if(specVerLst.getName().getLanguage() != null)
			content += specVerLst.getName().getLanguage();
		else 
			content += MultilingualContentDTO.LANGUAGE_SERBIAN;
		content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
		content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
		retVal.setContent(content);
		return retVal;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.AbstractRecordConverter#setRecordClasses(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRecordClasses(Record retVal, RecordDTO dto) {
//		System.out.println("SpecVerLstConverter - setRecordClasses - 1");
		super.setRecordClasses(retVal, dto);
//		System.out.println("SpecVerLstConverter - setRecordClasses - 2");
	}
}
