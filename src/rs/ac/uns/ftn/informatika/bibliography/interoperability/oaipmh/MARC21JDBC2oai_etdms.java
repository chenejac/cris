/**
 * Copyright 2006 OCLC Online Computer Library Center Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or
 * agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.oaipmh;

import java.util.HashMap;
import java.util.Properties;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers.ETDMSXMLSerializer;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;

/**
 * Convert native "item" to oai_etdms. 
 */
public class MARC21JDBC2oai_etdms extends CRISUNSCrosswalk {
      
	private String identifierLabel;
	
	/**
     * The constructor assigns the schemaLocation associated with this crosswalk. Since
     * the crosswalk is trivial in this case, no properties are utilized.
     *
     * @param properties properties that are needed to configure the crosswalk.
     */
    public MARC21JDBC2oai_etdms(Properties properties) {
    	super(properties, "http://www.ndltd.org/standards/metadata/etdms/1.0/ http://www.ndltd.org/standards/metadata/etdms/1.0/etdms.xsd");
    	identifierLabel = properties.getProperty("JDBCRecordFactory.identifierLabel");
    	serializer = new ETDMSXMLSerializer();
    	 if (identifierLabel == null) {
             throw new IllegalArgumentException("JDBCRecordFactory.identifierLabel is missing from the properties file");
         } 
    }

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.interoperability.oaipmh.CRISUNSCrosswalk#isAvailableFor(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean isAvailableFor(Object nativeItem) {
		HashMap<Object, Object> coreTable = (HashMap<Object, Object>)nativeItem;
		RecordDAO recordDAO = new RecordDAO(new RecordDB());
		Record record = recordDAO.getRecord(coreTable.get(identifierLabel).toString());
		if(record.getDto() instanceof StudyFinalDocumentDTO){
//			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record.getDto();
//			if(("records.studyFinalDocument.editPanel.studyType.phd".equals(studyFinalDocument.getStudyType())) || ("records.studyFinalDocument.editPanel.studyType.oldMaster".equals(studyFinalDocument.getStudyType())))
				return true;
		}
		return false;
	}   
    
    
}
