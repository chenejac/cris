package rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.ControlField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.Leader;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record;

/**
 * Abstract class for converting bibliographic mARC21Record between MARC21 format and
 * RecordDTO classes.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public abstract class ABibliographicRecordConverter extends AbstractRecordConverter {

	protected void setRelationsOtherRecordsThisRecord(Record retVal,
			RecordDTO dto) {
	}

	protected void setRelationsThisRecordOtherRecords(Record retVal,
			RecordDTO dto) {
	}

	protected abstract void setDataFields(MARC21Record rec, RecordDTO dto);

	@Override
	protected void setLeader(MARC21Record rec, RecordDTO dto) {
		Leader leader = new Leader();
		leader.setRecordLength(-1);
		leader.setRecordStatus('c');
		leader.setRecordType('a');
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
	protected void setControlFields(MARC21Record rec, String controlNumber,
			RecordDTO dto) {
		ControlField cf001 = getControlField001(rec.getLeader(), controlNumber);
		if (cf001 != null)
			rec.addControlField(cf001);
		ControlField cf003 = getControlField003(rec.getLeader(), controlNumber);
		if (cf003 != null)
			rec.addControlField(cf003);
		// ControlField cf005 = getControlField005(dto);
		// if(cf005!=null)
		// rec.addControlField(cf005);
		// ControlField cf006 = getControlField006(dto);
		// if(cf006!=null)
		// rec.addControlField(cf006);
		// ControlField cf007 = getControlField007(dto);
		// if(cf007!=null)
		// rec.addControlField(cf007);
		ControlField cf008 = getControlField008(dto);
		if(cf008!=null)
			rec.addControlField(cf008);
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

	@SuppressWarnings("unused")
	private ControlField getControlField005(RecordDTO dto) {
		SimpleDateFormat sdf = new SimpleDateFormat(
				AbstractRecordConverter.DATE_TIME_005_PATTERN);
		Calendar date = new GregorianCalendar();
		ControlField retVal = new ControlField("005", sdf.format(date));
		return retVal;
	}

	protected abstract ControlField getControlField006(RecordDTO dto);

	protected abstract ControlField getControlField007(RecordDTO dto);

	protected abstract ControlField getControlField008(RecordDTO dto);

}
