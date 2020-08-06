/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.reports.knr;

import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResultMeasureDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResultTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers.DublinCoreXMLSerializer;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers.Serializer;

/**
 * @author chenejac@uns.ac.rs
 *
 */
@SuppressWarnings("serial")
public class ResultsGroupDTO extends ResultMeasureDTO{

	private List<ResultDTO> results;
	
	
	/**
	 * 
	 */
	public ResultsGroupDTO() {
		super();
	}
	
	/**
	 * @param resultTypeMarc
	 * @param resultTypeName
	 * @param numberOfResults
	 * @param numberOfPoints
	 */
	public ResultsGroupDTO(ResultMeasureDTO resultMeasureDTO, List<ResultDTO> results) {
		super();
		this.resultType = resultMeasureDTO.getResultType();
		this.scienceArea = resultMeasureDTO.getScienceArea();
		this.quantitativeMeasure = resultMeasureDTO.getQuantitativeMeasure();
		this.commissionDTO = resultMeasureDTO.getCommissionDTO();
		this.results = results;
	}
	
	/**
	 * @return the results
	 */
	public List<ResultDTO> getResults() {
		return results;
	}
	
	/**
	 * @param results the results to set
	 */
	public void setResults(List<ResultDTO> publications) {
		this.results = publications;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.ResultMeasureDTO#getResultType()
	 */
	@Override
	public ResultTypeDTO getResultType() {
		return super.getResultType();
	}

	public String getKnrXML(String indent) {
		StringBuffer retVal = new StringBuffer("");
		retVal.append(indent + "<resultGroup>\n");
		retVal.append(indent + "\t<code>" + resultType.getClassId() + "</code>\n");
		retVal.append(indent + "\t<description>" + resultType.getSomeTerm() + "</description>\n");
		retVal.append(indent + "\t<results>\n");
		Serializer serializer = new DublinCoreXMLSerializer();
		for (ResultDTO result : results) {
			retVal.append(serializer.fromRecord(result.getPublication().getRecord(), indent + "\t\t"));
		}
		retVal.append(indent + "\t</results>\n");
		retVal.append(indent + "</resultGroup>\n");
		return retVal.toString();
	}
	
	

}
