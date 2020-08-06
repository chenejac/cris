package rs.ac.uns.ftn.informatika.bibliography.marc21.records;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class which presents mARC21Record with data in MARC21 format.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class MARC21Record implements java.io.Serializable {

	private Leader leader = new Leader();
	private List<DataField> dataFields = new ArrayList<DataField>();
	private List<ControlField> controlFields = new ArrayList<ControlField>();

	public MARC21Record() {
	}

	/**
	 * @return the control number of mARC21Record = (ControlField003)ControlField001.
	 */
	public String getControlNumber() {
		StringBuffer retVal = new StringBuffer();
		if ((getControlField("001") != null)
				&& (getControlField("003") != null))
			retVal.append("(" + getControlField("003").getContent() + ")"
					+ getControlField("001").getContent());
		return retVal.toString();
	}
	
	/**
	 * @param controlNumber
	 * 				controlNumber in format (ControlField003)ControlField001
	 */
	public void setControlNumber(String controlNumber) {
		String[] list = controlNumber.split("[()]");
		ControlField cf001 = new ControlField("001", list[2]);
		ControlField cf003 = new ControlField("003", list[1]);
		this.addControlField(cf001);
		this.addControlField(cf003);
	}
	

	/**
	 * @return the leader.
	 */
	public Leader getLeader() {
		return leader;
	}

	/**
	 * @param leader
	 *            The leader to set.
	 */
	public void setLeader(Leader leader) {
		this.leader = leader;
	}

	/**
	 * @return the dataFields.
	 */
	public List<DataField> getDataFields() {
		return dataFields;
	}

	/**
	 * @param dataFields
	 *            The data fields to set.
	 */
	public void setDataFields(List<DataField> dataFields) {
		for (DataField dataField : dataFields) {
			addDataField(dataField);
		}
	}

	/**
	 * Adds data field to the list of data fields
	 * 
	 * @param df
	 *            The data field to add.
	 * @return true if success else false.
	 */
	public boolean addDataField(DataField df) {
		df.setOwner(this);
		return dataFields.add(df);
	}

	/**
	 * Retrieves data field from the list of data fields
	 * 
	 * @param index
	 *            index of data field which should be retrieved.
	 * @return data field
	 */
	public DataField getDataField(int index) {
		if (index >= dataFields.size() || index < 0)
			return null;
		return (DataField) dataFields.get(index);
	}

	/**
	 * Retrieves number of data fields in list
	 * 
	 * @return number of data fields
	 */
	public int getDataFieldsCount() {
		return dataFields.size();
	}

	/**
	 * Retrieves data field from the list of data fields
	 * 
	 * @param name
	 *            name of data field which should be retrieved.
	 * @return data field
	 */
	public DataField getDataField(String name) {
		DataField retVal = null;
		for (DataField dataField : dataFields) {
			if (dataField.getName().equals(name)) {
				retVal = dataField;
			}
		}
		return retVal;
	}

	/**
	 * Retrieves the list of data fields with given name
	 * 
	 * @param name
	 *            name of data fields which should be retrieved.
	 * @return list of data fields
	 */
	public List<DataField> getDataFields(String name) {
		List<DataField> retVal = new ArrayList<DataField>();
		for (DataField dataField : dataFields) {
			if (dataField.getName().equals(name)) {
				retVal.add(dataField);
			}
		}
		return retVal;
	}

	/**
	 * @param name
	 *            name of data field which somebody looking for
	 * @return true if data field exist in list, else false
	 */
	public boolean hasDataField(String name) {
		return getDataField(name) != null;
	}

	private void sortDataFields() {
		for (int i = 1; i < dataFields.size(); i++) {
			for (int j = 0; j < dataFields.size() - i; j++) {
				DataField df1 = (DataField) dataFields.get(j);
				DataField df2 = (DataField) dataFields.get(j + 1);
				if (df1.getName().compareTo(df2.getName()) > 0) {
					dataFields.set(j, df2);
					dataFields.set(j + 1, df1);
				}
			}
		}
//		for (int i = 0; i < dataFields.size(); i++) {
//			DataField df = (DataField) dataFields.get(i);
//			df.sort();
//		}
	}

	/**
	 * @return the list of control fields.
	 */
	public List<ControlField> getControlFields() {
		return controlFields;
	}

	/**
	 * @param controlFields
	 *            The control fields to set.
	 */
	public void setControlFields(List<ControlField> controlFields) {
		for (ControlField controlField : controlFields) {
			addControlField(controlField);
		}
	}

	/**
	 * Adds control field to the list of control fields
	 * 
	 * @param cf
	 *            The control field to add.
	 * @return true if success else false.
	 */
	public boolean addControlField(ControlField cf) {
		cf.setOwner(this);
		return controlFields.add(cf);
	}

	/**
	 * Retrieves control field from the list of control fields
	 * 
	 * @param index
	 *            index of control field which should be retrieved.
	 * @return control field
	 */
	public ControlField getControlField(int index) {
		if (index >= controlFields.size() || index < 0)
			return null;
		return (ControlField) controlFields.get(index);
	}

	/**
	 * Retrieves number of control fields in list
	 * 
	 * @return number of control fields
	 */
	public int getControlFieldsCount() {
		return controlFields.size();
	}

	/**
	 * Retrieves control field from the list of control fields
	 * 
	 * @param name
	 *            name of control field which should be retrieved.
	 * @return data field
	 */
	public ControlField getControlField(String name) {
		ControlField retVal = null;
		for (ControlField controlField : controlFields) {
			if (controlField.getName().equals(name)) {
				retVal = controlField;
			}
		}
		return retVal;
	}

	/**
	 * Retrieves the list of control fields with given name
	 * 
	 * @param name
	 *            name of control fields which should be retrieved.
	 * @return list of control fields
	 */
	public List<ControlField> getControlFields(String name) {
		List<ControlField> retVal = new ArrayList<ControlField>();
		for (ControlField controlField : controlFields) {
			if (controlField.getName().equals(name)) {
				retVal.add(controlField);
			}
		}
		return retVal;
	}

	/**
	 * @param name
	 *            name of control field which somebody looking for
	 * @return true if control field exist in list, else false
	 */
	public boolean hasControlField(String name) {
		return getControlField(name) != null;
	}

	private void sortControlFields() {
		for (int i = 1; i < controlFields.size(); i++) {
			for (int j = 0; j < controlFields.size() - i; j++) {
				ControlField cf1 = (ControlField) controlFields.get(j);
				ControlField cf2 = (ControlField) controlFields.get(j + 1);
				if (cf1.getName().compareTo(cf2.getName()) > 0) {
					controlFields.set(j, cf2);
					controlFields.set(j + 1, cf1);
				}
			}
		}
		for (int i = 0; i < controlFields.size(); i++) {
			ControlField cf = (ControlField) controlFields.get(i);
			cf.sort();
		}
	}

	/**
	 * Sorts the list of control fields and the list of data fields
	 * 
	 */
	public void sortFields() {
		sortControlFields();
		sortDataFields();
	}

	/**
	 * Returns a non-printable string representation of this content.
	 * 
	 * @return string representation of the mARC21Record
	 */
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		retVal.append(leader);
		retVal.append(""+Field.separator);
		for (int i = 0; i < controlFields.size(); i++) {
			ControlField cf = (ControlField) controlFields.get(i);
			retVal.append(cf);
			retVal.append(""+Field.separator);
		}
		for (int i = 0; i < dataFields.size(); i++) {
			DataField df = (DataField) dataFields.get(i);
			retVal.append(df);
			retVal.append(""+Field.separator);
		}
		return retVal.toString();
	}

	/**
	 * Create an object MARC21Record from MARC21 string.
	 * 
	 * @param str
	 *            mARC21Record in MARC21 format
	 */
	public void fromString(String str) {
		try {
			String[] rows = null;
//			if(str.contains(""+Field.separator))
				rows = str.split(""+Field.separator);
//			else
//				rows = str.split("\r\n|\r|\n");
			String row = "";
			for (int i = 0; i < rows.length; i++) {
				row += rows[i];
				if(i+1 < rows.length){
					if((row.contains(""+Subfield.separator) && (!(rows[i+1].contains(""+Subfield.separator))))){
						row += " " + rows[i+1];
						continue;
					}
//					if((!(row.contains(""+Subfield.separator)) && (row.contains("$") && (!(rows[i+1].contains("$")))))){
//							row += " " + rows[i+1];
//							continue;
//					}
				}
				String[] pair = row.split("\\s", 2);
				if ("LDR".equalsIgnoreCase(pair[0])) {
					leader.fromString(pair[1]);
				} else if (pair[0].startsWith("00")) {
					ControlField cf = new ControlField();
					cf.fromString(row);
					addControlField(cf);
				} else {
					DataField df = new DataField();
					df.fromString(row);
					addDataField(df);
				}
				row = "";
			}
		} catch (Exception e) {
			log.error("fromString", e);
		}
	}
	
	/**
	 * Returns a printable string representation of this content.
	 * 
	 * @return string representation of the mARC21Record
	 */
	public String getPrintableString() {
		String retVal = toString();
		String leaderString = leader.toString();
		String newLeaderString = leaderString.replace(" ", "#");
		newLeaderString = newLeaderString.replace("LDR####", "LDR    ");
		retVal = retVal.replace(leaderString, newLeaderString);
		retVal = retVal.replace(DataField.separator, '\n').replace(Subfield.separator, '$');
		return retVal;
	}

	private static Log log = LogFactory.getLog(MARC21Record.class.getName());
}
