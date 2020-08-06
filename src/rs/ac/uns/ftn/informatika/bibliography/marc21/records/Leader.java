package rs.ac.uns.ftn.informatika.bibliography.marc21.records;

import java.text.DecimalFormat;

/**
 * Represents a LEADER in a MARC21 mARC21Record. Leader is a fixed field that
 * comprises the first 24 character positions (00-23) of each mARC21Record and
 * provides information for the processing of the mARC21Record.
 * 
 * @author bojana
 */

@SuppressWarnings("serial")
public class Leader implements java.io.Serializable {

	static private final int UNDEFINED_NUMBER = -1;

	/**
	 * 00-04 - MARC21Record length (number)
	 */
	private int recordLength;
	/**
	 * 05 - MARC21Record status (code)
	 */
	private char recordStatus;
	/**
	 * 06 - Type of mARC21Record (code)
	 */
	private char recordType;
	/**
	 * 07 - Bibliographic level (code)
	 */
	private char bibliographicLevel;
	/**
	 * 08 - Type of control (code)
	 */
	private char controlType;
	/**
	 * 09 - Character coding scheme
	 */
	private char characterCodingScheme;
	/**
	 * 10 - Indicator count (number)
	 */
	private int indicatorCount;
	/**
	 * 11 - Subfield code count (number)
	 */
	private int subfieldCodeCount;
	/**
	 * 12-16 - Base address of data (number)
	 */
	private int dataBaseAddress;
	/**
	 * 17 - Encoding level (code)
	 */
	private char encodingLevel;
	/**
	 * 18 - Descriptive cataloging form (code)
	 */
	private char descriptiveCatalogingForm;
	/**
	 * 19 - Linked mARC21Record requirement (code)
	 */
	private char linkedRecordRequirement;
	/**
	 * 20-23 - Entry map 20 - Length of the length-of-field portion (number)
	 */
	private int lengthOfFieldPortion;
	/**
	 * 21 - Length of the starting-character-position portion(number)
	 */
	private int startingCharacterPositionPortion;
	/**
	 * 22 - Length of the implementation-defined portion (number)
	 */
	private int implementationDefinedPortion;
	/**
	 * 23 - Undefined Entry map character position (number)
	 */
	private int undefinedEntryMapCharacterPosition;

	/**
	 * Number format for both mARC21Record length and base address of data
	 */
	private static DecimalFormat df = new DecimalFormat("00000");

	/**
	 * Default constructor.
	 */
	public Leader() {
	}

	/**
	 * @param content
	 *            The content which should be parsed to extract data.
	 */
	public Leader(String content) {
		parseContent(content);
	}

	/**
	 * 
	 * (attributes from oai_marc element according to oai_marc scheme)
	 * 
	 */
	public Leader(char status, char type, char level, char ctlType,
			char charEnc, char encLvl, char catForm, char lrRqrd) {
		recordStatus = status;
		recordType = type;
		bibliographicLevel = level;
		controlType = ctlType;
		characterCodingScheme = charEnc;
		encodingLevel = encLvl;
		descriptiveCatalogingForm = catForm;
		linkedRecordRequirement = lrRqrd;

		// default values
		recordLength = UNDEFINED_NUMBER;
		dataBaseAddress = UNDEFINED_NUMBER;
		indicatorCount = 2;
		subfieldCodeCount = 2;
		lengthOfFieldPortion = 4;
		startingCharacterPositionPortion = 5;
		implementationDefinedPortion = 0;
		undefinedEntryMapCharacterPosition = 0;
	}

	/*
	 * parsira ceo string i dodeljuje vrednost poljima koji su deo lidera
	 */
	private void parseContent(String content) {
		if(content.length() > 24)
			content = content.substring(3);
		try {
			recordLength = Integer.parseInt(content.substring(0, 5));
		} catch (NumberFormatException e) {
			recordLength = UNDEFINED_NUMBER;
		}
		recordStatus = content.charAt(5);
		recordType = content.charAt(6);
		bibliographicLevel = content.charAt(7);
		controlType = content.charAt(8);
		characterCodingScheme = content.charAt(9);
		try{
			indicatorCount = Integer.parseInt(""+content.charAt(10));
		} catch (NumberFormatException e) {
			indicatorCount = UNDEFINED_NUMBER;
		}
		try{
			subfieldCodeCount = Integer.parseInt(""+content.charAt(11));
		} catch (NumberFormatException e) {
			subfieldCodeCount = UNDEFINED_NUMBER;
		}
		try {
			dataBaseAddress = Integer.parseInt(content.substring(12, 17));
		} catch (NumberFormatException e) {
			dataBaseAddress = UNDEFINED_NUMBER;
		}
		encodingLevel = content.charAt(17);
		descriptiveCatalogingForm = content.charAt(18);
		linkedRecordRequirement = content.charAt(19);
		try{
			lengthOfFieldPortion = Integer.parseInt(""+content.charAt(20));
		} catch (NumberFormatException e) {
			lengthOfFieldPortion = UNDEFINED_NUMBER;
		}
		try{
			startingCharacterPositionPortion = Integer.parseInt(""+content.charAt(21));
		} catch (NumberFormatException e) {
			startingCharacterPositionPortion = UNDEFINED_NUMBER;
		}
		try{
			implementationDefinedPortion = Integer.parseInt(""+content.charAt(22));
		} catch (NumberFormatException e) {
			implementationDefinedPortion = UNDEFINED_NUMBER;
		}
		try{
			undefinedEntryMapCharacterPosition = Integer.parseInt(""+content.charAt(23));
		} catch (NumberFormatException e) {
			undefinedEntryMapCharacterPosition = UNDEFINED_NUMBER;
		}
	}

	/* delove content-a sakuplja u jedan string */
	private String gatherContent() {
		StringBuffer buff = new StringBuffer();
		if (recordLength == UNDEFINED_NUMBER)
			buff.append("     ");
		else
			buff.append(df.format(recordLength).toString());
		buff.append(recordStatus).append(recordType).append(bibliographicLevel)
				.append(controlType).append(characterCodingScheme);
		if(indicatorCount == UNDEFINED_NUMBER)
			buff.append(" ");
		else
			buff.append(indicatorCount);
		if(subfieldCodeCount == UNDEFINED_NUMBER)
			buff.append(" ");
		else
			buff.append(subfieldCodeCount);
		if (dataBaseAddress == UNDEFINED_NUMBER)
			buff.append("     ");
		else
			buff.append(df.format(dataBaseAddress).toString());
		buff.append(encodingLevel).append(descriptiveCatalogingForm).append(
				linkedRecordRequirement);
		if(lengthOfFieldPortion == UNDEFINED_NUMBER)
			buff.append(" ");
		else
			buff.append(lengthOfFieldPortion);
		if(startingCharacterPositionPortion == UNDEFINED_NUMBER)
			buff.append(" ");
		else
			buff.append(startingCharacterPositionPortion);
		if(implementationDefinedPortion == UNDEFINED_NUMBER)
			buff.append(" ");
		else
			buff.append(implementationDefinedPortion);
		if(undefinedEntryMapCharacterPosition == UNDEFINED_NUMBER)
			buff.append(" ");
		else
			buff.append(undefinedEntryMapCharacterPosition);
		return buff.toString();

	}

	/**
	 * @return the leader content
	 */
	public String getContent() {
		return gatherContent();
	}

	/**
	 * @param content
	 *            The content to set.
	 */
	public void setContent(String content) {
		parseContent(content);
	}

	/**
	 * @return a printable string representation of this content.
	 */
	public String toString() {
		return "LDR    " + gatherContent();
	}

	/**
	 * Create an object Leader from Marc21 string.
	 * 
	 * @param str
	 *            The string content to parse.
	 */
	public void fromString(String str) {
		parseContent(str);
	}

	/**
	 * @return the bibliographicLevel.
	 */
	public char getBibliographicLevel() {
		return bibliographicLevel;
	}

	/**
	 * @param bibliographicLevel
	 *            The bibliographicLevel to set.
	 */
	public void setBibliographicLevel(char bibliographicLevel) {
		this.bibliographicLevel = bibliographicLevel;
	}

	/**
	 * @return the characterCodingScheme.
	 */
	public char getCharacterCodingScheme() {
		return characterCodingScheme;
	}

	/**
	 * @param characterCodingScheme
	 *            The characterCodingScheme to set.
	 */
	public void setCharacterCodingScheme(char characterCodingScheme) {
		this.characterCodingScheme = characterCodingScheme;
	}

	/**
	 * @return the controlType.
	 */
	public char getControlType() {
		return controlType;
	}

	/**
	 * @param controlType
	 *            The controlType to set.
	 */
	public void setControlType(char controlType) {
		this.controlType = controlType;
	}

	/**
	 * @return the dataBaseAddress.
	 */
	public int getDataBaseAddress() {
		return dataBaseAddress;
	}

	/**
	 * @param dataBaseAddress
	 *            The dataBaseAddress to set.
	 */
	public void setDataBaseAddress(int dataBaseAddress) {
		this.dataBaseAddress = dataBaseAddress;
	}

	/**
	 * @return the descriptiveCatalogingForm.
	 */
	public char getDescriptiveCatalogingForm() {
		return descriptiveCatalogingForm;
	}

	/**
	 * @param descriptiveCatalogingForm
	 *            The descriptiveCatalogingForm to set.
	 */
	public void setDescriptiveCatalogingForm(char descriptiveCatalogingForm) {
		this.descriptiveCatalogingForm = descriptiveCatalogingForm;
	}

	/**
	 * @return decimal format.
	 */
	public static DecimalFormat getDecimalFormat() {
		return df;
	}

	/**
	 * @return the encodingLevel.
	 */
	public char getEncodingLevel() {
		return encodingLevel;
	}

	/**
	 * @param encodingLevel
	 *            The encodingLevel to set.
	 */
	public void setEncodingLevel(char encodingLevel) {
		this.encodingLevel = encodingLevel;
	}

	/**
	 * @return the implementationDefinedPortion.
	 */
	public int getImplementationDefinedPortion() {
		return implementationDefinedPortion;
	}

	/**
	 * @param implementationDefinedPortion
	 *            The implementationDefinedPortion to set.
	 */
	public void setImplementationDefinedPortion(int implementationDefinedPortion) {
		this.implementationDefinedPortion = implementationDefinedPortion;
	}

	/**
	 * @return the indicatorCount.
	 */
	public int getIndicatorCount() {
		return indicatorCount;
	}

	/**
	 * @param indicatorCount
	 *            The indicatorCount to set.
	 */
	public void setIndicatorCount(int indicatorCount) {
		this.indicatorCount = indicatorCount;
	}

	/**
	 * @return the lengthOfFieldPortion.
	 */
	public int getLengthOfFieldPortion() {
		return lengthOfFieldPortion;
	}

	/**
	 * @param lengthOfFieldPortion
	 *            The lengthOfFieldPortion to set.
	 */
	public void setLengthOfFieldPortion(int lengthOfFieldPortion) {
		this.lengthOfFieldPortion = lengthOfFieldPortion;
	}

	/**
	 * @return the linkedRecordRequirement.
	 */
	public char getLinkedRecordRequirement() {
		return linkedRecordRequirement;
	}

	/**
	 * @param linkedRecordRequirement
	 *            The linkedRecordRequirement to set.
	 */
	public void setLinkedRecordRequirement(char linkedRecordRequirement) {
		this.linkedRecordRequirement = linkedRecordRequirement;
	}

	/**
	 * @return the recordLength.
	 */
	public int getRecordLength() {
		return recordLength;
	}

	/**
	 * @param recordLength
	 *            The recordLength to set.
	 */
	public void setRecordLength(int recordLength) {
		this.recordLength = recordLength;
	}

	/**
	 * @return the recordStatus.
	 */
	public char getRecordStatus() {
		return recordStatus;
	}

	/**
	 * @param recordStatus
	 *            The recordStatus to set.
	 */
	public void setRecordStatus(char recordStatus) {
		this.recordStatus = recordStatus;
	}

	/**
	 * @return the recordType.
	 */
	public char getRecordType() {
		return recordType;
	}

	/**
	 * @param recordType
	 *            The recordType to set.
	 */
	public void setRecordType(char recordType) {
		this.recordType = recordType;
	}

	/**
	 * @return the startingCharacterPositionPortion.
	 */
	public int getStartingCharacterPositionPortion() {
		return startingCharacterPositionPortion;
	}

	/**
	 * @param startingCharacterPositionPortion
	 *            The startingCharacterPositionPortion to set.
	 */
	public void setStartingCharacterPositionPortion(
			int startingCharacterPositionPortion) {
		this.startingCharacterPositionPortion = startingCharacterPositionPortion;
	}

	/**
	 * @return the subfieldCodeCount.
	 */
	public int getSubfieldCodeCount() {
		return subfieldCodeCount;
	}

	/**
	 * @param subfieldCodeCount
	 *            The subfieldCodeCount to set.
	 */
	public void setSubfieldCodeCount(int subfieldCodeCount) {
		this.subfieldCodeCount = subfieldCodeCount;
	}

	/**
	 * @return the undefinedEntryMapCharacterPosition.
	 */
	public int getUndefinedEntryMapCharacterPosition() {
		return undefinedEntryMapCharacterPosition;
	}

	/**
	 * @param undefinedEntryMapCharacterPosition
	 *            The undefinedEntryMapCharacterPosition to set.
	 */
	public void setUndefinedEntryMapCharacterPosition(
			int undefinedEntryMapCharacterPosition) {
		this.undefinedEntryMapCharacterPosition = undefinedEntryMapCharacterPosition;
	}

}
