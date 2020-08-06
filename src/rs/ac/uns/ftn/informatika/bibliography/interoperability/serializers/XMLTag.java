/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import java.util.ArrayList;
import java.util.List;

import ORG.oclc.oai.util.OAIUtil;

/**
 * @author Dragan Ivanovic
 *
 */
public class XMLTag {

	private String indent="";
	
	private String name;
	
	private String body;
	
	private List<AttributeValue> attributes;
	private List<XMLTag> subTags;
	

	/**
	 * @param name
	 */
	public XMLTag(String name) {
		super();
		this.name = name;
		attributes = new ArrayList<AttributeValue>();
		subTags = new ArrayList<XMLTag>();
	}

	/**
	 * @param name
	 * @param body
	 */
	public XMLTag(String name, String body) {
		super();
		this.name = name;
		this.body = body;
		attributes = new ArrayList<AttributeValue>();
		subTags = new ArrayList<XMLTag>();
	}
	
	/**
	 * @param indent
	 * @param name
	 * @param body
	 */
	public XMLTag(String indent, String name, String body) {
		super();
		this.indent = indent;
		this.name = name;
		this.body = body;
		attributes = new ArrayList<AttributeValue>();
		subTags = new ArrayList<XMLTag>();
	}

	/**
	 * @param name
	 * @param body
	 * @param attributes
	 */
	public XMLTag(String name, String body, List<AttributeValue> attributes) {
		super();
		this.name = name;
		this.body = body;
		attributes = new ArrayList<AttributeValue>();
		subTags = new ArrayList<XMLTag>();
	}


	/**
	 * @return the indent
	 */
	public String getIndent() {
		return indent;
	}

	/**
	 * @param indent the indent to set
	 */
	public void setIndent(String indent) {
		this.indent = indent;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the attributes
	 */
	public List<AttributeValue> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(List<AttributeValue> attributes) {
		this.attributes = attributes;
	}
	
	/**
	 * @return the subTags
	 */
	public List<XMLTag> getSubTags() {
		return subTags;
	}

	/**
	 * @param subTags the subTags to set
	 */
	public void setSubTags(List<XMLTag> subTags) {
		this.subTags = subTags;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		retVal.append(indent + "<" + name);
		for (AttributeValue attribute : attributes) {
			retVal.append(" " + attribute.getPrefName() + "=\"" + OAIUtil.xmlEncode(attribute.getValue()) + "\"");
		}
		retVal.append(">");
		if(body!=null)
			retVal.append(OAIUtil.xmlEncode(body));
		for (XMLTag subTag : subTags) {
			retVal.append("\n" + subTag.toString());
		}
	    if(subTags.size() > 0)
	    	retVal.append("\n" + indent);
		retVal.append("</" + name + ">");
	    return retVal.toString();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean retVal = false;
		if(obj instanceof XMLTag){
			if(((XMLTag)obj).getName().equals(name))
				if((body != null) && (body.equals(((XMLTag)obj).getBody())))
					retVal = true;
		} else if (obj instanceof String){
			if((body != null) && (body.equals(obj.toString())))
				retVal = true;
		}
		return retVal;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if(body != null)
			return body.hashCode();
		return super.hashCode();
	}
	
	

}
