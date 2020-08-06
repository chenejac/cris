package rs.ac.uns.ftn.informatika.bibliography.reports.obrazci;

import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResultMeasureDTO;

public class EvaluatedRecord {
	
	private RecordDTO record;
	private ResultMeasureDTO resultType;
	
	
	
	/**
	 * @param record
	 * @param resultType
	 */
	public EvaluatedRecord(RecordDTO record, ResultMeasureDTO resultType) {
		super();
		this.record = record;
		this.resultType = resultType;
	}
	/**
	 * @return the record
	 */
	public RecordDTO getRecord() {
		return record;
	}
	/**
	 * @param record the record to set
	 */
	public void setRecord(RecordDTO record) {
		this.record = record;
	}
	/**
	 * @return the resultType
	 */
	public ResultMeasureDTO getResultType() {
		return resultType;
	}
	/**
	 * @param resultType the resultType to set
	 */
	public void setResultType(ResultMeasureDTO resultType) {
		this.resultType = resultType;
	}
	
	public int getResultTypeNumber(){
		try{
			
			String num = resultType.getResultType().getClassId().substring(1);
			return Integer.parseInt(num);			
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	

}
