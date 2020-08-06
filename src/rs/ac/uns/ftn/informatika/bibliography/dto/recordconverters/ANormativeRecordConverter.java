package rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.ControlField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.Leader;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record;

/**
 * Abstract class for converting normative mARC21Record between MARC21 format and
 * RecordDTO classes.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public abstract class ANormativeRecordConverter extends AbstractRecordConverter {

	/**
	 * Convert the normative mARC21Record from DTO format to MARC21 format
	 * 
	 * @param dto
	 *            MARC21Record which should be converted
	 * 
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.AbstractRecordConverter#getRecord(rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 * 
	 * @return true if everything is ok, otherwise false
	 */
	@Override
	public boolean getRecord(RecordDTO dto) {
		try {
			if(super.getRecord(dto)){
				setRecordKeywords(dto.getRecord(), dto);
				return true;
			}
		} catch (Throwable e) {
			log.error("getRecord", e);
		}
		return false;
	}
	
	
	
	protected void setRecordKeywords(Record retVal, RecordDTO dto) {
	}
	
	@Override
	protected void setLeader(MARC21Record rec, RecordDTO dto) {
		Leader leader = new Leader();
		leader.setRecordLength(-1);
		leader.setRecordStatus('c');
		leader.setRecordType('z');
		leader.setBibliographicLevel(AbstractRecordConverter.BLANK);
		leader.setControlType(AbstractRecordConverter.BLANK);
		leader.setCharacterCodingScheme('a');
		leader.setIndicatorCount(2);
		leader.setSubfieldCodeCount(-1);
		leader.setDataBaseAddress(-1);
		leader.setEncodingLevel('n');
		leader.setDescriptiveCatalogingForm(AbstractRecordConverter.BLANK);
		leader.setLinkedRecordRequirement(AbstractRecordConverter.BLANK);
		leader.setLengthOfFieldPortion(-1);
		leader.setStartingCharacterPositionPortion(-1);
		leader.setImplementationDefinedPortion(-1);
		leader.setUndefinedEntryMapCharacterPosition(-1);
		rec.setLeader(leader);
	}

	@Override
	protected void setControlFields(MARC21Record rec, String controlNumber, RecordDTO dto) {
		ControlField cf001 = getControlField001(rec.getLeader(), controlNumber);
		if (cf001 != null)
			rec.addControlField(cf001);

		ControlField cf003 = getControlField003(rec.getLeader(), controlNumber);
		if (cf003 != null)
			rec.addControlField(cf003);

		// ControlField cf005 = getControlField005();
		// if(cf005!=null)
		// rec.addControlField(cf005);
		//		
		// ControlField cf008 = getControlField008();
		// if(cf008!=null)
		// rec.addControlField(cf008);

	}

	private ControlField getControlField001(Leader leader, String controlNumber) {
		if((controlNumber == null) || (controlNumber.equals("(TEMP)0")) || (!controlNumber.contains("BISIS")))
			return null;
		String[] list = controlNumber.split("[()]");
		ControlField retVal = new ControlField("001", list[2]);
		return retVal;
	}

	private ControlField getControlField003(Leader leader, String controlNumber) {
		if((controlNumber == null) || (controlNumber.equals("(TEMP)0")) || (!controlNumber.contains("BISIS")))
			return null;
		String[] list = controlNumber.split("[()]");
		ControlField retVal = new ControlField("003", list[1]);
		return retVal;
	}

	// private ControlField getControlField005(){
	// SimpleDateFormat sdf = new
	// SimpleDateFormat(AbstractRecordConverter.DATE_TIME_005_PATTERN);
	// Calendar date = new GregorianCalendar();
	// ControlField retVal = new ControlField("005", sdf.format(date));
	// return retVal;
	// }
	//	
	// private ControlField getControlField008(){
	// ControlField retVal = new ControlField("008");
	// SimpleDateFormat sdf = new
	// SimpleDateFormat(AbstractRecordConverter.DATE_008_PATTERN);
	// Calendar date = new GregorianCalendar();
	// retVal.addCharPositions(0, sdf.format(date));
	// retVal.addCharPositions(6,""+AbstractRecordConverter.NO_ATTEMPT_TO_CODE);
	// retVal.addCharPositions(7,""+AbstractRecordConverter.NO_ATTEMPT_TO_CODE);
	// retVal.addCharPositions(8,""+AbstractRecordConverter.NO_ATTEMPT_TO_CODE);
	// retVal.addCharPositions(9,""+AbstractRecordConverter.NO_ATTEMPT_TO_CODE);
	// retVal.addCharPositions(10,""+AbstractRecordConverter.NO_ATTEMPT_TO_CODE);
	// retVal.addCharPositions(11,""+AbstractRecordConverter.NO_ATTEMPT_TO_CODE);
	// retVal.addCharPositions(12,""+AbstractRecordConverter.NO_ATTEMPT_TO_CODE);
	// retVal.addCharPositions(13,""+AbstractRecordConverter.NO_ATTEMPT_TO_CODE);
	// retVal.addCharPositions(14,""+AbstractRecordConverter.NO_ATTEMPT_TO_CODE);
	// retVal.addCharPositions(15,""+AbstractRecordConverter.NO_ATTEMPT_TO_CODE);
	// retVal.addCharPositions(16,""+AbstractRecordConverter.NO_ATTEMPT_TO_CODE);
	// retVal.addCharPositions(17,""+AbstractRecordConverter.NO_ATTEMPT_TO_CODE);
	//retVal.addCharPositions(18,""+AbstractRecordConverter.BLANK+AbstractRecordConverter.BLANK
	// +AbstractRecordConverter.BLANK+AbstractRecordConverter.BLANK+AbstractRecordConverter.BLANK+
	// AbstractRecordConverter
	// .BLANK+AbstractRecordConverter.BLANK+AbstractRecordConverter.BLANK+AbstractRecordConverter
	// .BLANK+AbstractRecordConverter.BLANK);
	// retVal.addCharPositions(28,""+AbstractRecordConverter.NO_ATTEMPT_TO_CODE);
	// retVal.addCharPositions(29,""+AbstractRecordConverter.NO_ATTEMPT_TO_CODE);
	// retVal.addCharPositions(30,""+AbstractRecordConverter.BLANK);
	// retVal.addCharPositions(31,""+AbstractRecordConverter.NO_ATTEMPT_TO_CODE);
	// retVal.addCharPositions(32,""+AbstractRecordConverter.NO_ATTEMPT_TO_CODE);
	// retVal.addCharPositions(33,""+AbstractRecordConverter.NO_ATTEMPT_TO_CODE);
	//retVal.addCharPositions(34,""+AbstractRecordConverter.BLANK+AbstractRecordConverter.BLANK
	// +AbstractRecordConverter.BLANK+AbstractRecordConverter.BLANK);
	// retVal.addCharPositions(38,""+AbstractRecordConverter.NO_ATTEMPT_TO_CODE);
	// retVal.addCharPositions(39,""+AbstractRecordConverter.NO_ATTEMPT_TO_CODE);
	//		
	// return retVal;
	// }

	private static Log log = LogFactory.getLog(ANormativeRecordConverter.class
			.getName());
}
