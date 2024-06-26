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

import java.util.Properties;

import rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers.NDLTDDublinCoreXMLSerializer;

/**
 * Convert native "item" to oai_dc. 
 */
public class NDLTDMARC21JDBC2oai_dc extends CRISUNSCrosswalk {
      
    /**
     * The constructor assigns the schemaLocation associated with this crosswalk. Since
     * the crosswalk is trivial in this case, no properties are utilized.
     *
     * @param properties properties that are needed to configure the crosswalk.
     */
    public NDLTDMARC21JDBC2oai_dc(Properties properties) {
    	super(properties, "http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd");
    	serializer = new NDLTDDublinCoreXMLSerializer();
    }   
}
