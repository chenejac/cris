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

import rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers.Serializer;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import ORG.oclc.oai.server.crosswalk.Crosswalk;

/**
 * 
 */
public class CRISUNSCrosswalk extends Crosswalk {
      
	protected Serializer serializer;
	
	/**
     * The constructor assigns the schemaLocation associated with this crosswalk. Since
     * the crosswalk is trivial in this case, no properties are utilized.
     *
     * @param properties properties that are needed to configure the crosswalk.
     */
    public CRISUNSCrosswalk(Properties properties, String schemaLocation) {
    	super(schemaLocation);
    }

    /**
     * Can this nativeItem be represented in DC format?
     * @param nativeItem a record in native format
     * @return true if DC format is possible, false otherwise.
     */
    public boolean isAvailableFor(Object nativeItem) {
    	return true; 
    }

    /**
     * Perform the actual crosswalk.
     *
     * @param nativeItem the native "item". In this case, it is
     * already formatted as an OAI <record> element, with the
     * possible exception that multiple metadataFormats are
     * present in the <metadata> element.
     * @return a String containing the XML to be stored within the <metadata> element.
     */
    @SuppressWarnings("unchecked")
	public String createMetadata(Object nativeItem) {
    	HashMap<Object, Object> coreTable = (HashMap<Object, Object>)nativeItem;
		Record record = ((Record)coreTable.get("CRISUNSRECORD"));
		
		String retVal = serializer.fromRecord(record, "");
		record.getDto().setNotLoaded(true);
		return retVal;
	   
    }
}
