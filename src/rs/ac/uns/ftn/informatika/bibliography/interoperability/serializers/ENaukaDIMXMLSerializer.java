/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import rs.ac.uns.ftn.informatika.bibliography.dto.*;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.SamovrednovanjeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class for conversions between object of class MARC21Record and XML format according
 * to http://www.dspace.org/schema/dim.xsd XMLSchema
 * 
 * 
 */
public class ENaukaDIMXMLSerializer extends AbstractDIMCRISXMLSerializer {


	public ENaukaDIMXMLSerializer(){
		super(Arrays.asList("en"), "eNauka");
	}

	protected List<XMLTag> getIdentifiers(String mdschema, String element, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		String tagName = "dim:field";
		if(record instanceof PublicationDTO){
			if((((PublicationDTO)record).getFile() != null) && (((PublicationDTO)record).getFile().getLicense() != null) && (!((PublicationDTO)record).getFile().getLicense().equals("Usage forbidden"))){
				for (String language : languages) {
					XMLTag textTag = new XMLTag(tagName, (Serializer.serverURL + ((PublicationDTO)record).getFileURL() + ((source != null)?("&source=" + source):("")) + "&language="+language).replace(" ", "%20"));
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					textTag.getAttributes().add(new AttributeValue("qualifier", "url"));
					textTag.getAttributes().add(new AttributeValue("lang", language));
					retVal.add(textTag);
				}
			}

			if(record instanceof MonographDTO){
				MonographDTO monograph = (MonographDTO)record;
				if((monograph.getEditionISSN() != null) && (! "".equals(monograph.getEditionISSN())) && (! "NN".equals(monograph.getEditionISSN()))){
					String[] splited = monograph.getEditionISSN().replace('x', 'X').replace(";", "").trim().split("\\(|\\)");
					for(String split: splited)
						if(split.matches("\\d{4}-?\\d{3}[\\dX]")){
							XMLTag textTag = new XMLTag(tagName, split);
							textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
							textTag.getAttributes().add(new AttributeValue("element", element));
							textTag.getAttributes().add(new AttributeValue("qualifier", "issn"));
							retVal.add(textTag);
						}
				}
				if((monograph.getIsbn() != null) && (! "".equals(monograph.getIsbn()))){
					String split = monograph.getIsbn().toUpperCase().replace("ISBN:", "").replace("ISBN", "").replace(";", "").trim();
					if((((split.matches("978-\\d+-\\d+-\\d+-\\d"))
							|| (split.matches("978 \\d+ \\d+ \\d+ \\d"))
							|| (split.matches("979-[1-9]\\d*-\\d+-\\d+-\\d"))
							|| (split.matches("979 [1-9]\\d* \\d+ \\d+ \\d"))) && (split.length() == 17))
							|| (((split.matches("978\\d{10}"))
							|| (split.matches("979[1-9]\\d{9}"))
							|| (split.matches("\\d+-\\d+-\\d+-[\\dX]"))
							|| (split.matches("\\d+ \\d+ \\d+ [\\dX]"))) && (split.length() == 13))
							|| (split.matches("\\d{9}[\\dX]"))){
						XMLTag textTag = new XMLTag(tagName, split);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "isbn"));
						retVal.add(textTag);
					}
				}
				if((monograph.getDoi() != null) && (! "".equals(monograph.getDoi()))){
					String correctDoi = null;
					String[] splited = monograph.getDoi().trim().split(":|\\s");
					for(String split: splited)
						if(split.matches("10\\.\\d{4,}(\\.\\d+)*/[^\\s]+")){
							correctDoi = split;
							break;
						}
					if(correctDoi != null){
						XMLTag textTag = new XMLTag(tagName, correctDoi);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "isbn"));
						retVal.add(textTag);
					}
				}
			} else if(record instanceof JournalDTO){
				JournalDTO journal = (JournalDTO)record;
				if((journal.getIssn() != null) && (! "".equals(journal.getIssn())) && (! "NN".equals(journal.getIssn()))){
					String[] splited = journal.getIssn().replace('x', 'X').replace(";", "").trim().split("\\(|\\)");
					for(String split: splited)
						if(split.matches("\\d{4}-?\\d{3}[\\dX]")){
							XMLTag textTag = new XMLTag(tagName, split);
							textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
							textTag.getAttributes().add(new AttributeValue("element", element));
							textTag.getAttributes().add(new AttributeValue("qualifier", "issn"));
							retVal.add(textTag);
						}
				}
				if((journal.getDoi() != null) && (! "".equals(journal.getDoi()))){
					String correctDoi = null;
					String[] splited = journal.getDoi().trim().split(":|\\s");
					for(String split: splited)
						if(split.matches("10\\.\\d{4,}(\\.\\d+)*/[^\\s]+")){
							correctDoi = split;
							break;
						}
					if(correctDoi != null){
						XMLTag textTag = new XMLTag(tagName, correctDoi);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "doi"));
						retVal.add(textTag);
					}
				}
			}  else if(record instanceof ProceedingsDTO){
				ProceedingsDTO proceedings = (ProceedingsDTO)record;
				if((proceedings.getIsbn() != null) && (! "".equals(proceedings.getIsbn()))){
					String split = proceedings.getIsbn().toUpperCase().replace("ISBN:", "").replace("ISBN", "").replace(";", "").trim();
					if((((split.matches("978-\\d+-\\d+-\\d+-\\d"))
							|| (split.matches("978 \\d+ \\d+ \\d+ \\d"))
							|| (split.matches("979-[1-9]\\d*-\\d+-\\d+-\\d"))
							|| (split.matches("979 [1-9]\\d* \\d+ \\d+ \\d"))) && (split.length() == 17))
							|| (((split.matches("978\\d{10}"))
							|| (split.matches("979[1-9]\\d{9}"))
							|| (split.matches("\\d+-\\d+-\\d+-[\\dX]"))
							|| (split.matches("\\d+ \\d+ \\d+ [\\dX]"))) && (split.length() == 13))
							|| (split.matches("\\d{9}[\\dX]"))){
						XMLTag textTag = new XMLTag(tagName, split);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "isbn"));
						retVal.add(textTag);
					}
				}
				if((proceedings.getDoi() != null) && (! "".equals(proceedings.getDoi()))){
					String correctDoi = null;
					String[] splited = proceedings.getDoi().trim().split(":|\\s");
					for(String split: splited)
						if(split.matches("10\\.\\d{4,}(\\.\\d+)*/[^\\s]+")){
							correctDoi = split;
							break;
						}
					if(correctDoi != null){
						XMLTag textTag = new XMLTag(tagName, correctDoi);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "doi"));
						retVal.add(textTag);
					}
				}
			} else if(record instanceof StudyFinalDocumentDTO){
				StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
				if((studyFinalDocument.getDoi() != null) && (! "".equals(studyFinalDocument.getDoi()))){
					String correctDoi = null;
					String[] splited = studyFinalDocument.getDoi().trim().split(":|\\s");
					for(String split: splited)
						if(split.matches("10\\.\\d{4,}(\\.\\d+)*/[^\\s]+")){
							correctDoi = split;
							break;
						}
					if(correctDoi != null){
						XMLTag textTag = new XMLTag(tagName, correctDoi);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "doi"));
						retVal.add(textTag);
					}
				}
			} else if(record instanceof PaperProceedingsDTO){
				PaperProceedingsDTO paper = (PaperProceedingsDTO)record;
				if((paper.getDoi() != null) && (! "".equals(paper.getDoi()))){
					String correctDoi = null;
					String[] splited = paper.getDoi().trim().split(":|\\s");
					for(String split: splited)
						if(split.matches("10\\.\\d{4,}(\\.\\d+)*/[^\\s]+")){
							correctDoi = split;
							break;
						}
					if(correctDoi != null){
						XMLTag textTag = new XMLTag(tagName, correctDoi);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "doi"));
						retVal.add(textTag);
					}
				}
				ProceedingsDTO proceedings = paper.getProceedings();
				if((proceedings.getIsbn() != null) && (! "".equals(proceedings.getIsbn()))){
					String split = proceedings.getIsbn().toUpperCase().replace("ISBN:", "").replace("ISBN", "").replace(";", "").trim();
					if((((split.matches("978-\\d+-\\d+-\\d+-\\d"))
							|| (split.matches("978 \\d+ \\d+ \\d+ \\d"))
							|| (split.matches("979-[1-9]\\d*-\\d+-\\d+-\\d"))
							|| (split.matches("979 [1-9]\\d* \\d+ \\d+ \\d"))) && (split.length() == 17))
							|| (((split.matches("978\\d{10}"))
							|| (split.matches("979[1-9]\\d{9}"))
							|| (split.matches("\\d+-\\d+-\\d+-[\\dX]"))
							|| (split.matches("\\d+ \\d+ \\d+ [\\dX]"))) && (split.length() == 13))
							|| (split.matches("\\d{9}[\\dX]"))){
						XMLTag textTag = new XMLTag(tagName, split);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "isbn"));
						retVal.add(textTag);
					}
				}
			}  else if(record instanceof PaperJournalDTO){
				PaperJournalDTO paper = (PaperJournalDTO)record;
				if((paper.getDoi() != null) && (! "".equals(paper.getDoi()))){
					String correctDoi = null;
					String[] splited = paper.getDoi().trim().split(":|\\s");
					for(String split: splited)
						if(split.matches("10\\.\\d{4,}(\\.\\d+)*/[^\\s]+")){
							correctDoi = split;
							break;
						}
					if(correctDoi != null){
						XMLTag textTag = new XMLTag(tagName, correctDoi);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "doi"));
						retVal.add(textTag);
					}
				}
				JournalDTO journal = paper.getJournal();
				if((journal.getIssn() != null) && (! "".equals(journal.getIssn())) && (! "NN".equals(journal.getIssn()))){
					String[] splited = journal.getIssn().replace('x', 'X').replace(";", "").trim().split("\\(|\\)");
					for(String split: splited)
						if(split.matches("\\d{4}-?\\d{3}[\\dX]")){
							XMLTag textTag = new XMLTag(tagName, split);
							textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
							textTag.getAttributes().add(new AttributeValue("element", element));
							textTag.getAttributes().add(new AttributeValue("qualifier", "issn"));
							retVal.add(textTag);
						}
				}
			} else if(record instanceof PaperMonographDTO){
				PaperMonographDTO paper = (PaperMonographDTO)record;
				if((paper.getDoi() != null) && (! "".equals(paper.getDoi()))){
					String correctDoi = null;
					String[] splited = paper.getDoi().trim().split(":|\\s");
					for(String split: splited)
						if(split.matches("10\\.\\d{4,}(\\.\\d+)*/[^\\s]+")){
							correctDoi = split;
							break;
						}
					if(correctDoi != null){
						XMLTag textTag = new XMLTag(tagName, correctDoi);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "doi"));
						retVal.add(textTag);
					}
				}
				MonographDTO monograph = paper.getMonograph();
				if((monograph.getEditionISSN() != null) && (! "".equals(monograph.getEditionISSN())) && (! "NN".equals(monograph.getEditionISSN()))){
					String[] splited = monograph.getEditionISSN().replace('x', 'X').replace(";", "").trim().split("\\(|\\)");
					for(String split: splited)
						if(split.matches("\\d{4}-?\\d{3}[\\dX]")){
							XMLTag textTag = new XMLTag(tagName, split);
							textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
							textTag.getAttributes().add(new AttributeValue("element", element));
							textTag.getAttributes().add(new AttributeValue("qualifier", "issn"));
							retVal.add(textTag);
						}
				}
				if((monograph.getIsbn() != null) && (! "".equals(monograph.getIsbn()))){
					String split = monograph.getIsbn().toUpperCase().replace("ISBN:", "").replace("ISBN", "").replace(";", "").trim();
					if((((split.matches("978-\\d+-\\d+-\\d+-\\d"))
							|| (split.matches("978 \\d+ \\d+ \\d+ \\d"))
							|| (split.matches("979-[1-9]\\d*-\\d+-\\d+-\\d"))
							|| (split.matches("979 [1-9]\\d* \\d+ \\d+ \\d"))) && (split.length() == 17))
							|| (((split.matches("978\\d{10}"))
							|| (split.matches("979[1-9]\\d{9}"))
							|| (split.matches("\\d+-\\d+-\\d+-[\\dX]"))
							|| (split.matches("\\d+ \\d+ \\d+ [\\dX]"))) && (split.length() == 13))
							|| (split.matches("\\d{9}[\\dX]"))){
						XMLTag textTag = new XMLTag(tagName, split);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "isbn"));
						retVal.add(textTag);
					}
				}
			} else if(record instanceof ProductDTO){
				ProductDTO product = (ProductDTO)record;
				if((product.getDoi() != null) && (! "".equals(product.getDoi()))){
					String correctDoi = null;
					String[] splited = product.getDoi().trim().split(":|\\s");
					for(String split: splited)
						if(split.matches("10\\.\\d{4,}(\\.\\d+)*/[^\\s]+")){
							correctDoi = split;
							break;
						}
					if(correctDoi != null){
						XMLTag textTag = new XMLTag(tagName, correctDoi);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "doi"));
						retVal.add(textTag);
					}
				}
			}
		} else if (record instanceof AuthorDTO){
			AuthorDTO author = (AuthorDTO)record;
			if(author.getEmail() != null){
				XMLTag textTag = new XMLTag(tagName,author.getEmail());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "email"));
				retVal.add(textTag);
			}
		} else if (record instanceof ConferenceDTO){
			ConferenceDTO conference = (ConferenceDTO)record;
			if(conference.getNumber() != null){
				XMLTag textTag = new XMLTag(tagName,conference.getNumber());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "number"));
				retVal.add(textTag);
			}
		}
		for (String language : languages) {
			XMLTag textTag = new XMLTag(tagName, (Serializer.serverURL + "/record.jsf?recordId="+record.getControlNumber().substring(7) + ((source != null)?("&source=" + source):("")) + "&language="+language).replace(" ", "%20"));
			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
			textTag.getAttributes().add(new AttributeValue("element", element));
			textTag.getAttributes().add(new AttributeValue("qualifier", "uri"));
			textTag.getAttributes().add(new AttributeValue("lang", language));
			retVal.add(textTag);
		}
		XMLTag textTag = new XMLTag(tagName, record.getControlNumber());
		textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
		textTag.getAttributes().add(new AttributeValue("element", element));
		textTag.getAttributes().add(new AttributeValue("qualifier", "externalcrisreference"));
		retVal.add(textTag);

		if(record.getScopusID() != null){
			textTag = new XMLTag(tagName, record.getScopusID());
			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
			textTag.getAttributes().add(new AttributeValue("element", element));
			textTag.getAttributes().add(new AttributeValue("qualifier", "scopus"));
			retVal.add(textTag);
		}
		if(record.getORCID() != null){
			textTag = new XMLTag(tagName, record.getORCID());
			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
			textTag.getAttributes().add(new AttributeValue("element", element));
			textTag.getAttributes().add(new AttributeValue("qualifier", "orcid"));
			retVal.add(textTag);
		}
//		if(record instanceof StudyFinalDocumentDTO){
//
//			if(((((StudyFinalDocumentDTO)record).getReportURL() != null)) && (!((StudyFinalDocumentDTO)record).getReportURL().equals(""))){
//				XMLTag textTag = new XMLTag(tagName, ((StudyFinalDocumentDTO)record).getReportURL().replace(" ", "%20"));
//				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
//				textTag.getAttributes().add(new AttributeValue("element", element));
//				textTag.getAttributes().add(new AttributeValue("qualifier", "url"));
//				retVal.add(textTag);
//			}
//		}
		return retVal;
	}


	protected List<XMLTag> getSources(String mdschema, String element, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		String tagName = "dim:field";
		if(record instanceof PaperJournalDTO){
			PaperJournalDTO paperJournal = (PaperJournalDTO)record;
			JournalDTO journal = paperJournal.getJournal();
			XMLTag textTag = new XMLTag(tagName, (journal.getSomeName() != null)?journal.getSomeName():"");
			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
			textTag.getAttributes().add(new AttributeValue("element", element));
//			textTag.getAttributes().add(new AttributeValue("qualifier", "ispartof"));
//			textTag.getAttributes().add(new AttributeValue("authority", journal.getControlNumber()));
			if(journal.getName().getLanguage() != null)
				textTag.getAttributes().add(new AttributeValue("lang", journal.getName().getLanguage().substring(0,2)));
			retVal.add(textTag);
		} else if(record instanceof PaperMonographDTO){
			PaperMonographDTO paperMonograph = (PaperMonographDTO)record;
			MonographDTO monograph = paperMonograph.getMonograph();
			XMLTag textTag = new XMLTag(tagName, (monograph.getSomeTitle() != null)?monograph.getSomeTitle():"");
			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
			textTag.getAttributes().add(new AttributeValue("element", element));
//			textTag.getAttributes().add(new AttributeValue("qualifier", "ispartof"));
//			textTag.getAttributes().add(new AttributeValue("authority", monograph.getControlNumber()));
			if(monograph.getTitle().getLanguage() != null)
				textTag.getAttributes().add(new AttributeValue("lang", monograph.getTitle().getLanguage().substring(0,2)));
			retVal.add(textTag);
		} else if(record instanceof PaperProceedingsDTO){
			PaperProceedingsDTO paperProceedings = (PaperProceedingsDTO)record;
			ProceedingsDTO proceedings = paperProceedings.getProceedings();
			XMLTag textTag = new XMLTag(tagName, ((proceedings.getSomeTitle() != null)?proceedings.getSomeTitle()+", ":"") + ((proceedings.getConference()!=null)?proceedings.getConference().toString():""));
			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
			textTag.getAttributes().add(new AttributeValue("element", element));
//			textTag.getAttributes().add(new AttributeValue("qualifier", "ispartof"));
//			textTag.getAttributes().add(new AttributeValue("authority", proceedings.getControlNumber()));
			if(proceedings.getTitle().getLanguage() != null)
				textTag.getAttributes().add(new AttributeValue("lang", proceedings.getTitle().getLanguage().substring(0,2)));
			retVal.add(textTag);
			ConferenceDTO conference = proceedings.getConference();
			textTag = new XMLTag(tagName, (conference.getName().getContent() != null)?conference.getName().getContent():"");
			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
			textTag.getAttributes().add(new AttributeValue("element", element));
			textTag.getAttributes().add(new AttributeValue("qualifier", "conference"));
//			textTag.getAttributes().add(new AttributeValue("authority", conference.getControlNumber()));
			if(conference.getName().getLanguage() != null)
				textTag.getAttributes().add(new AttributeValue("lang", conference.getName().getLanguage().substring(0,2)));
			retVal.add(textTag);

		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO proceedings = (ProceedingsDTO)record;

			ConferenceDTO conference = proceedings.getConference();
			XMLTag textTag = new XMLTag(tagName, (conference.getSomeName() != null)?conference.getSomeName():"");
			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
			textTag.getAttributes().add(new AttributeValue("element", element));
			textTag.getAttributes().add(new AttributeValue("qualifier", "conference"));
//			textTag.getAttributes().add(new AttributeValue("authority", conference.getControlNumber()));
			if(conference.getName().getLanguage() != null)
				textTag.getAttributes().add(new AttributeValue("lang", conference.getName().getLanguage().substring(0,2)));
			retVal.add(textTag);

		} else if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO publication = (StudyFinalDocumentDTO)record;
			if((publication.getInstitution()!=null) && (publication.getInstitution().getControlNumber()!=null)){
				String name = (publication.getInstitution().getName()!= null)?publication.getInstitution().getName().getContent():"";
				String language = (publication.getInstitution().getName()!= null)?publication.getInstitution().getName().getLanguage():null;
				String id = publication.getInstitution().getControlNumber();

				if(name != null){
					XMLTag institutionTag = new XMLTag(tagName, name);
					institutionTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					institutionTag.getAttributes().add(new AttributeValue("element", element));
					institutionTag.getAttributes().add(new AttributeValue("qualifier", "institution"));
//					institutionTag.getAttributes().add(new AttributeValue("authority", id));
					if(language != null)
						institutionTag.getAttributes().add(new AttributeValue("lang", language.substring(0, 2)));
					retVal.add(institutionTag);
				}
			}
		} else if(record instanceof AuthorDTO){
			AuthorDTO author = (AuthorDTO)record;
			if((author.getInstitution()!=null) && (author.getInstitution().getControlNumber()!=null)){
				String name = (author.getInstitution().getName()!= null)?author.getInstitution().getName().getContent():"";
				String language = (author.getInstitution().getName()!= null)?author.getInstitution().getName().getLanguage():null;
				String id = author.getInstitution().getControlNumber();

				if(name != null){
					XMLTag institutionTag = new XMLTag(tagName, name);
					institutionTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					institutionTag.getAttributes().add(new AttributeValue("element", element));
					institutionTag.getAttributes().add(new AttributeValue("qualifier", "institution"));
//					institutionTag.getAttributes().add(new AttributeValue("authority", id));
					if(language != null)
						institutionTag.getAttributes().add(new AttributeValue("lang", language.substring(0, 2)));
					retVal.add(institutionTag);
				}
			}
			if((author.getOrganizationUnit()!=null) && (author.getOrganizationUnit().getControlNumber()!=null)){
				String name = (author.getOrganizationUnit().getName()!= null)?author.getOrganizationUnit().getName().getContent():"";
				String language = (author.getOrganizationUnit().getName()!= null)?author.getOrganizationUnit().getName().getLanguage():null;
				String id = author.getOrganizationUnit().getControlNumber();

				if(name != null){
					XMLTag institutionTag = new XMLTag(tagName, name);
					institutionTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					institutionTag.getAttributes().add(new AttributeValue("element", element));
					institutionTag.getAttributes().add(new AttributeValue("qualifier", "organizationUnit"));
//					institutionTag.getAttributes().add(new AttributeValue("authority", id));
					if(language != null)
						institutionTag.getAttributes().add(new AttributeValue("lang", language.substring(0, 2)));
					retVal.add(institutionTag);
				}
			}
			if(author.getState() != null){
				XMLTag state = new XMLTag(tagName, author.getState());
				state.getAttributes().add(new AttributeValue("mdschema", mdschema));
				state.getAttributes().add(new AttributeValue("element", element));
				state.getAttributes().add(new AttributeValue("qualifier", "country"));
				retVal.add(state);
			}
		} else if(record instanceof ConferenceDTO){
			ConferenceDTO conference = (ConferenceDTO)record;
			if(conference.getState() != null){
				XMLTag state = new XMLTag(tagName, conference.getState());
				state.getAttributes().add(new AttributeValue("mdschema", mdschema));
				state.getAttributes().add(new AttributeValue("element", element));
				state.getAttributes().add(new AttributeValue("qualifier", "country"));
				retVal.add(state);
			}
			if(conference.getPlace() != null){
				XMLTag place = new XMLTag(tagName, conference.getPlace());
				place.getAttributes().add(new AttributeValue("mdschema", mdschema));
				place.getAttributes().add(new AttributeValue("element", element));
				place.getAttributes().add(new AttributeValue("qualifier", "place"));
				retVal.add(place);
			}
		} else if(record instanceof OrganizationUnitDTO){
			OrganizationUnitDTO orgUnit = (OrganizationUnitDTO)record;
			if((orgUnit.getInstitution()!=null) && (orgUnit.getInstitution().getControlNumber()!=null)){
				String name = (orgUnit.getInstitution().getName()!= null)?orgUnit.getInstitution().getName().getContent():"";
				String language = (orgUnit.getInstitution().getName()!= null)?orgUnit.getInstitution().getName().getLanguage():null;
				String id = orgUnit.getInstitution().getControlNumber();
				if(name != null){
					XMLTag institutionTag = new XMLTag(tagName, name);
					institutionTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					institutionTag.getAttributes().add(new AttributeValue("element", element));
					institutionTag.getAttributes().add(new AttributeValue("qualifier", "institution"));
//					institutionTag.getAttributes().add(new AttributeValue("authority", id));
					if(language != null)
						institutionTag.getAttributes().add(new AttributeValue("lang", language.substring(0, 2)));
					retVal.add(institutionTag);
				}
			}
			if((orgUnit.getSuperOrganizationUnit()!=null) && (orgUnit.getSuperOrganizationUnit().getControlNumber()!=null)){
				String name = (orgUnit.getSuperOrganizationUnit().getName()!= null)?orgUnit.getSuperOrganizationUnit().getName().getContent():"";
				String language = (orgUnit.getSuperOrganizationUnit().getName()!= null)?orgUnit.getSuperOrganizationUnit().getName().getLanguage():null;
				String id = orgUnit.getSuperOrganizationUnit().getControlNumber();
				if(name != null){
					XMLTag institutionTag = new XMLTag(tagName, name);
					institutionTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					institutionTag.getAttributes().add(new AttributeValue("element", element));
					institutionTag.getAttributes().add(new AttributeValue("qualifier", "organizationUnit"));
//					institutionTag.getAttributes().add(new AttributeValue("authority", id));
					if(language != null)
						institutionTag.getAttributes().add(new AttributeValue("lang", language.substring(0, 2)));
					retVal.add(institutionTag);
				}
			}
		} else if(record instanceof InstitutionDTO){
			InstitutionDTO institution = (InstitutionDTO)record;
			if((institution.getSuperInstitution()!=null) && (institution.getSuperInstitution().getControlNumber()!=null)){
				String name = (institution.getSuperInstitution().getName() != null)?institution.getSuperInstitution().getName().getContent():"";
				String language = (institution.getSuperInstitution().getName() != null)?institution.getSuperInstitution().getName().getLanguage():null;
				String id = institution.getSuperInstitution().getControlNumber();

				if(name != null){
					XMLTag institutionTag = new XMLTag(tagName, name);
					institutionTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					institutionTag.getAttributes().add(new AttributeValue("element", element));
					institutionTag.getAttributes().add(new AttributeValue("qualifier", "institution"));
//					institutionTag.getAttributes().add(new AttributeValue("authority", id));
					if(language != null)
						institutionTag.getAttributes().add(new AttributeValue("lang", language.substring(0, 2)));
					retVal.add(institutionTag);
				}
			}
		}

		return retVal;
	}


	protected List<XMLTag> getRelations(String mdschema, String element, RecordDTO record) {
		element = "citation";
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		String tagName = "dim:field";
		if(record instanceof PaperJournalDTO){
			PaperJournalDTO paperJournal = (PaperJournalDTO)record;
			JournalDTO journal = paperJournal.getJournal();
			XMLTag textTag = new XMLTag(tagName, (journal.getSomeName() != null)?journal.getSomeName():"");
			if(((PaperJournalDTO)record).getEndPage() != null){
				textTag = new XMLTag(tagName, ((PaperJournalDTO)record).getEndPage());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "epage"));
				retVal.add(textTag);
			}
			if(((PaperJournalDTO)record).getStartPage() != null){
				textTag = new XMLTag(tagName, ((PaperJournalDTO)record).getStartPage());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "spage"));
				retVal.add(textTag);
			}
			if(((PaperJournalDTO)record).getVolume() != null){
				textTag = new XMLTag(tagName, ((PaperJournalDTO)record).getVolume());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "volume"));
				retVal.add(textTag);
			}
			if(((PaperJournalDTO)record).getNumber() != null){
				textTag = new XMLTag(tagName, ((PaperJournalDTO)record).getNumber());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "issue"));
				retVal.add(textTag);
			}
		} else if(record instanceof PaperMonographDTO){
			PaperMonographDTO paperMonograph = (PaperMonographDTO)record;
			MonographDTO monograph = paperMonograph.getMonograph();
			XMLTag textTag = new XMLTag(tagName, (monograph.getSomeTitle() != null)?monograph.getSomeTitle():"");
			if(((PaperMonographDTO)record).getEndPage() != null){
				textTag = new XMLTag(tagName, ((PaperMonographDTO)record).getEndPage());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "epage"));
				retVal.add(textTag);
			}
			if(((PaperMonographDTO)record).getStartPage() != null){
				textTag = new XMLTag(tagName, ((PaperMonographDTO)record).getStartPage());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "spage"));
				retVal.add(textTag);
			}
		} else if(record instanceof PaperProceedingsDTO){
			PaperProceedingsDTO paperProceedings = (PaperProceedingsDTO)record;
			ProceedingsDTO proceedings = paperProceedings.getProceedings();
			XMLTag textTag = new XMLTag(tagName, ((proceedings.getSomeTitle() != null)?proceedings.getSomeTitle()+", ":"") + ((proceedings.getConference()!=null)?proceedings.getConference().toString():""));
			if(((PaperProceedingsDTO)record).getEndPage() != null){
				textTag = new XMLTag(tagName, ((PaperProceedingsDTO)record).getEndPage());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "epage"));
				retVal.add(textTag);
			}
			if(((PaperProceedingsDTO)record).getStartPage() != null){
				textTag = new XMLTag(tagName, ((PaperProceedingsDTO)record).getStartPage());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "spage"));
				retVal.add(textTag);
			}

//			ConferenceDTO conference = proceedings.getConference();
//			textTag = new XMLTag(tagName, (conference.getName().getContent() != null)?conference.getName().getContent():"");
//			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
//			textTag.getAttributes().add(new AttributeValue("element", element));
//			textTag.getAttributes().add(new AttributeValue("qualifier", "conference"));
//			textTag.getAttributes().add(new AttributeValue("authority", conference.getControlNumber()));
//			if(conference.getName().getLanguage() != null)
//				textTag.getAttributes().add(new AttributeValue("lang", conference.getName().getLanguage().substring(0,2)));
//			retVal.add(textTag);

		}

		String rank = SamovrednovanjeUtils.getResultType(record.getRecord());
		if (rank != null){
			XMLTag textTag = new XMLTag(tagName, rank);
			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
			textTag.getAttributes().add(new AttributeValue("element", element));
			textTag.getAttributes().add(new AttributeValue("qualifier", "rank"));
			retVal.add(textTag);
		}
		return retVal;
	}



}
