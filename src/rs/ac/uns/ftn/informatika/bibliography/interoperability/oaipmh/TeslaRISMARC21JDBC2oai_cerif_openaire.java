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

import rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers.OpenAIRECRISXMLSerializer;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers.TeslaRISXMLSerializer;

import java.util.Properties;

/**
 * Convert native "item" to oai_cerif_openaire. 
 */
public class TeslaRISMARC21JDBC2oai_cerif_openaire extends CRISUNSCrosswalk {

    /**
     * The constructor assigns the schemaLocation associated with this crosswalk. Since
     * the crosswalk is trivial in this case, no properties are utilized.
     *
     * @param properties properties that are needed to configure the crosswalk.
     */
    public TeslaRISMARC21JDBC2oai_cerif_openaire(Properties properties) {
    	super(properties, "https://www.openaire.eu/cerif-profile/1.1/ https://www.openaire.eu/schema/cris/current/openaire-cerif-profile.xsd");
    	serializer = new TeslaRISXMLSerializer();
    }   
    
    
}
