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

import rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers.BEOPENDIMXMLSerializer;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers.ENaukaDIMXMLSerializer;

import java.util.Properties;


/**
 * Convert native "item" to oai_dc. 
 */
public class ENaukaMARC21JDBC2dim extends CRISUNSCrosswalk {

    /**
     * The constructor assigns the schemaLocation associated with this crosswalk. Since
     * the crosswalk is trivial in this case, no properties are utilized.
     *
     * @param properties properties that are needed to configure the crosswalk.
     */
    public ENaukaMARC21JDBC2dim(Properties properties) {
    	super(properties, "http://www.dspace.org/xmlns/dspace/dim http://www.dspace.org/schema/dim.xsd");
        serializer = new ENaukaDIMXMLSerializer();
    }   
}
