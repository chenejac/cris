package rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;

import rs.ac.uns.ftn.informatika.bibliography.dao.CommissionDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.dao.MyDataSource;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.EvaluationDB;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.CommissionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperMonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResultMeasureDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.ResultEvaluator;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.reports.freemarker.TemplateRunner;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;
import rs.ac.uns.ftn.informatika.bibliography.utils.LatCyrUtils;

public class SamovrednovanjeUtils {
	
	
	public static int brojNastavnika = 0;
	public static int brojMentora = 0;
	
	
	public static String hostname;
	public static String port;
	public static String schema;
	public static String connectionParameters;
	public static String username;
	public static String password;
	public static String luceneIndex;
	
	private static DataSource dataSource;
	private static Connection conn;
	
	private static Log log = LogFactory.getLog(SamovrednovanjeUtils.class.getName());
	
	static {
		SamovrednovanjeUtils.dataSource = DataSourceFactory.getDataSource();
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * TODO
	 * 
	 * u strukturi List<ResultForYearDTO> vratati sve rezultate koje zadovoljavaju sledece kriterijume
	 * 
	 * 1. neko od autora ima id iz liste @param ids 
	 * 2. objavljen je u godini koja se nalazi u listi  @param years 
	 * 3. pripada kategoriji koja se nalazi u listi @param resultTypes (za tabelu 6.1 lista ce sadrzati M21, M22, M23), 
	 * 			lista resultTypes moze biti null onda da vrati za sve kategorije (za tabelu 6.4).  
	 * 
	 * ako je komlikovano napraviti ovakav opsti upit mogu i dve operacije 
	 * jedna za tabelu 6.1 (2007-2011) i jedna za 6.4 (2011)
	 * 
	 * 
	 */
	public static List<ResultForYearDTO> getAllResults(List<String> ids, List<String> years){
		try{
		List<ResultForYearDTO> retVal = null;
		TermQuery termQuery = null;
		BooleanQuery query = new BooleanQuery();
		if(ids != null){
			BooleanQuery idsQuery = new BooleanQuery();
			for (String id : ids) {		
				termQuery = new TermQuery(new Term("AUCN", id));
				idsQuery.add(termQuery, Occur.SHOULD);
				termQuery = new TermQuery(new Term("EDCN", id));
				idsQuery.add(termQuery, Occur.SHOULD);
				termQuery = new TermQuery(new Term("ADCN", id));
				idsQuery.add(termQuery, Occur.SHOULD);
			}
			query.add(idsQuery, Occur.MUST);
		}
		if(years != null){
			BooleanQuery yearsQuery = new BooleanQuery();
			for (String year : years) {
				termQuery = new TermQuery(new Term("PY", year));
				yearsQuery.add(termQuery, Occur.SHOULD);
			}
			query.add(yearsQuery, Occur.MUST);
		}
		List<Record> records = Retriever.select(query, new AllDocCollector(false));		
		Map<String, ResultForYearDTO> map = new HashMap<String, ResultForYearDTO>();
		for (Record record : records) {
			String publicationYear = ((PublicationDTO)(record.getDto())).getPublicationYear();//1960
		
			if(years.contains(publicationYear)){
			log.info("Processing control number: "+record.getControlNumber());
			String resultType = SamovrednovanjeUtils.getResultType(record);
			if(resultType == null){
				resultType = "M99";
			}
			ResultForTypeDTO resultForType = new ResultForTypeDTO(record);
			
			resultForType.setResultType(resultType); 
			resultForType.setResult(TemplateRunner.getRepresentation(record.getDto(),TemplateRunner.PMFSAMOVREDNOVANJE));				
			if(! (map.containsKey(publicationYear))){
				map.put(publicationYear, new ResultForYearDTO());
				map.get(publicationYear).setYear(publicationYear);
			} 
			map.get(publicationYear).getResultsForType().add(resultForType);
		} 
		
			record.clear();			
		}
		
		/*
		ResultForTypeDTO resultForType = new ResultForTypeDTO();
		resultForType.setResultType("M26"); // Bojana ce ovo srediti 
		resultForType.setResult("Mirjana Ivanovic, Comsis, 2011");
		map.get("2011").getResultsForType().add(resultForType);
		resultForType = new ResultForTypeDTO();
		resultForType.setResultType("M26"); // Bojana ce ovo srediti
		resultForType.setResult("Bikit Istvan, 2011");
		map.get("2011").getResultsForType().add(resultForType);
		*/
		
		retVal = new ArrayList<ResultForYearDTO>(map.values());
		Collections.sort(retVal, new GenericComparator<ResultForYearDTO>(
				"resultType", "asc"));
		for(ResultForYearDTO resForYear:retVal){			
			prepareResults(resForYear);
		}	 
		
		return retVal;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<ResultForYearDTO> getAllResultsWord(List<String> ids, List<String> years){
		try{
		List<ResultForYearDTO> retVal = null;
		TermQuery termQuery = null;
		BooleanQuery query = new BooleanQuery();
		if(ids != null){
			BooleanQuery idsQuery = new BooleanQuery();
			for (String id : ids) {		
				termQuery = new TermQuery(new Term("AUCN", id));
				idsQuery.add(termQuery, Occur.SHOULD);
				termQuery = new TermQuery(new Term("EDCN", id));
				idsQuery.add(termQuery, Occur.SHOULD);
				termQuery = new TermQuery(new Term("ADCN", id));
				idsQuery.add(termQuery, Occur.SHOULD);
			}
			query.add(idsQuery, Occur.MUST);
		}
		if(years != null){
			BooleanQuery yearsQuery = new BooleanQuery();
			for (String year : years) {
				termQuery = new TermQuery(new Term("PY", year));
				yearsQuery.add(termQuery, Occur.SHOULD);
			}
			query.add(yearsQuery, Occur.MUST);
		}
		List<Record> records = Retriever.select(query, new AllDocCollector(false));		
		Map<String, ResultForYearDTO> map = new HashMap<String, ResultForYearDTO>();
		for (Record record : records) {
			String publicationYear = ((PublicationDTO)(record.getDto())).getPublicationYear();//1960
		
			if(years.contains(publicationYear)){
			log.info("Processing control number: "+record.getControlNumber());
			String resultType = SamovrednovanjeUtils.getResultType(record);
			if(resultType == null){
				resultType = "M99";
			}
				ResultForTypeDTO resultForType = new ResultForTypeDTO(record);
				
				resultForType.setResultType(resultType); 
				resultForType.setResult(record.getDto().getStringRepresentation());
				if(record.getDto() instanceof PaperJournalDTO){
					resultForType.setFirstAuthorId(((PaperJournalDTO)record.getDto()).getMainAuthor().getControlNumber());
					String authorsInstitutions = "";
					if((record.getDto() instanceof PublicationDTO) && (((PublicationDTO)record.getDto()).getAllAuthors() != null) && (((PublicationDTO)record.getDto()).getAllAuthors().size()>0)){
						authorsInstitutions = "";
						boolean check = false;
						for (AuthorDTO author : ((PublicationDTO)record.getDto()).getAllAuthors()) {
							if((author.getInstitutionName() != null) && ((author.getInstitutionName().contains("Tehnološki fakultet Novi Sad, Univerzitet u Novom Sadu")) || (author.getInstitutionName().contains("Prirodno-matematički fakultet u Novom Sadu, Univerzitet u Novom Sadu")))){
								if((authorsInstitutions.length() == 0)){
									authorsInstitutions = author.getInstitutionName();
								} else if(! authorsInstitutions.contains(author.getInstitutionName())){
										authorsInstitutions += "; " + author.getInstitutionName();
								}
							} else {
								check = true;
							}
						}
						if(check && (! authorsInstitutions.contains(";")))
							authorsInstitutions = "Potrebna provera";
					}
					resultForType.setAuthorsInstitutions(authorsInstitutions);
				} else
				if(record.getDto() instanceof PaperMonographDTO){
					resultForType.setFirstAuthorId(((PaperMonographDTO)record.getDto()).getMainAuthor().getControlNumber());
					String authorsInstitutions = "";
					if((record.getDto() instanceof PublicationDTO) && (((PublicationDTO)record.getDto()).getAllAuthors() != null) && (((PublicationDTO)record.getDto()).getAllAuthors().size()>0)){
						authorsInstitutions = "";
						boolean check = false;
						for (AuthorDTO author : ((PublicationDTO)record.getDto()).getAllAuthors()) {
							if((author.getInstitutionName() != null) && ((author.getInstitutionName().contains("Tehnološki fakultet Novi Sad, Univerzitet u Novom Sadu")) || (author.getInstitutionName().contains("Prirodno-matematički fakultet u Novom Sadu, Univerzitet u Novom Sadu")))){
								if((authorsInstitutions.length() == 0)){
									authorsInstitutions = author.getInstitutionName();
								} else if(! authorsInstitutions.contains(author.getInstitutionName())){
										authorsInstitutions += "; " + author.getInstitutionName();
								}
							} else {
								check = true;
							}
						}
						if(check && (! authorsInstitutions.contains(";")))
							authorsInstitutions = "Potrebna provera";
					}
					resultForType.setAuthorsInstitutions(authorsInstitutions);
				} else
				if(record.getDto() instanceof MonographDTO){
					resultForType.setFirstAuthorId(((MonographDTO)record.getDto()).getMainAuthor().getControlNumber());
					String authorsInstitutions = "";
					if((record.getDto() instanceof PublicationDTO) && (((PublicationDTO)record.getDto()).getAllAuthors() != null) && (((PublicationDTO)record.getDto()).getAllAuthors().size()>0)){
						authorsInstitutions = "";
						boolean check = false;
						for (AuthorDTO author : ((PublicationDTO)record.getDto()).getAllAuthors()) {
							if((author.getInstitutionName() != null) && ((author.getInstitutionName().contains("Tehnološki fakultet Novi Sad, Univerzitet u Novom Sadu")) || (author.getInstitutionName().contains("Prirodno-matematički fakultet u Novom Sadu, Univerzitet u Novom Sadu")))){
								if((authorsInstitutions.length() == 0)){
									authorsInstitutions = author.getInstitutionName();
								} else if(! authorsInstitutions.contains(author.getInstitutionName())){
										authorsInstitutions += "; " + author.getInstitutionName();
								}
							} else {
								check = true;
							}
						}
						if(check && (! authorsInstitutions.contains(";")))
							authorsInstitutions = "Potrebna provera";
					}
					resultForType.setAuthorsInstitutions(authorsInstitutions);
				} else
				if(record.getDto() instanceof PaperProceedingsDTO ){
					resultForType.setFirstAuthorId(((PaperProceedingsDTO)record.getDto()).getMainAuthor().getControlNumber());
					String authorsInstitutions = "";
					if((record.getDto() instanceof PublicationDTO) && (((PublicationDTO)record.getDto()).getAllAuthors() != null) && (((PublicationDTO)record.getDto()).getAllAuthors().size()>0)){
						authorsInstitutions = "";
						boolean check = false;
						for (AuthorDTO author : ((PublicationDTO)record.getDto()).getAllAuthors()) {
							if((author.getInstitutionName() != null) && ((author.getInstitutionName().contains("Tehnološki fakultet Novi Sad, Univerzitet u Novom Sadu")) || (author.getInstitutionName().contains("Prirodno-matematički fakultet u Novom Sadu, Univerzitet u Novom Sadu")))){
								if((authorsInstitutions.length() == 0)){
									authorsInstitutions = author.getInstitutionName();
								} else if(! authorsInstitutions.contains(author.getInstitutionName())){
										authorsInstitutions += "; " + author.getInstitutionName();
								}
							} else {
								check = true;
							}
						}
						if(check && (! authorsInstitutions.contains(";")))
							authorsInstitutions = "Potrebna provera";
					}
					resultForType.setAuthorsInstitutions(authorsInstitutions);
				} else
				if(record.getDto() instanceof StudyFinalDocumentDTO){
					resultForType.setFirstAuthorId(((StudyFinalDocumentDTO)record.getDto()).getAuthor().getControlNumber());
					String authorsInstitutions = "";
					if((record.getDto() instanceof PublicationDTO) && (((PublicationDTO)record.getDto()).getAllAuthors() != null) && (((PublicationDTO)record.getDto()).getAllAuthors().size()>0)){
						authorsInstitutions = "";
						boolean check = false;
						for (AuthorDTO author : ((PublicationDTO)record.getDto()).getAllAuthors()) {
							if((author.getInstitutionName() != null) && ((author.getInstitutionName().contains("Tehnološki fakultet Novi Sad, Univerzitet u Novom Sadu")) || (author.getInstitutionName().contains("Prirodno-matematički fakultet u Novom Sadu, Univerzitet u Novom Sadu")))){
								if((authorsInstitutions.length() == 0)){
									authorsInstitutions = author.getInstitutionName();
								} else if(! authorsInstitutions.contains(author.getInstitutionName())){
										authorsInstitutions += "; " + author.getInstitutionName();
								}
							} else {
								check = true;
							}
						}
						if(check && (! authorsInstitutions.contains(";")))
							authorsInstitutions = "Potrebna provera";
					}
					resultForType.setAuthorsInstitutions(authorsInstitutions);
				} 
					
				if(! (map.containsKey(publicationYear))){
					map.put(publicationYear, new ResultForYearDTO());
					map.get(publicationYear).setYear(publicationYear);
				} 
				map.get(publicationYear).getResultsForType().add(resultForType);
			} 
			
			record.clear();			
		}
		
		/*
		ResultForTypeDTO resultForType = new ResultForTypeDTO();
		resultForType.setResultType("M26"); // Bojana ce ovo srediti 
		resultForType.setResult("Mirjana Ivanovic, Comsis, 2011");
		map.get("2011").getResultsForType().add(resultForType);
		resultForType = new ResultForTypeDTO();
		resultForType.setResultType("M26"); // Bojana ce ovo srediti
		resultForType.setResult("Bikit Istvan, 2011");
		map.get("2011").getResultsForType().add(resultForType);
		*/
		
		retVal = new ArrayList<ResultForYearDTO>(map.values());
		Collections.sort(retVal, new GenericComparator<ResultForYearDTO>(
				"resultType", "asc"));
		for(ResultForYearDTO resForYear:retVal){			
			prepareResults(resForYear);
		}	 
		
		return retVal;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<ResultForYearDTO> getAllResultsWord(List<String> ids, List<String> years, String instiutionId){
		List<ResultForYearDTO> retVal = null;
		try{
		TermQuery termQuery = null;
		BooleanQuery query = new BooleanQuery();
		if(ids != null){
			BooleanQuery idsQuery = new BooleanQuery();
			for (String id : ids) {		
				termQuery = new TermQuery(new Term("AUCN", id));
				idsQuery.add(termQuery, Occur.SHOULD);
				termQuery = new TermQuery(new Term("EDCN", id));
				idsQuery.add(termQuery, Occur.SHOULD);
				termQuery = new TermQuery(new Term("ADCN", id));
				idsQuery.add(termQuery, Occur.SHOULD);
			}
			query.add(idsQuery, Occur.MUST);
		}
		if(years != null){
			BooleanQuery yearsQuery = new BooleanQuery();
			for (String year : years) {
				termQuery = new TermQuery(new Term("PY", year));
				yearsQuery.add(termQuery, Occur.SHOULD);
			}
			query.add(yearsQuery, Occur.MUST);
		}
		List<Record> records = Retriever.select(query, new AllDocCollector(false));		
		Map<String, ResultForYearDTO> map = new HashMap<String, ResultForYearDTO>();
		for (Record record : records) {
			try{
				String publicationYear = ((PublicationDTO)(record.getDto())).getPublicationYear();//1960
			
				if(years.contains(publicationYear)){
				log.info("Processing control number: "+record.getControlNumber());
				String resultType = SamovrednovanjeUtils.getResultType(record);
				if(resultType == null){
					resultType = "M99";
				}
					ResultForTypeDTO resultForType = new ResultForTypeDTO(record);
					
					resultForType.setResultType(resultType); 
					resultForType.setResult(record.getDto().getStringRepresentation(instiutionId));
					if(record.getDto() instanceof PaperJournalDTO){
						resultForType.setFirstAuthorId(((PaperJournalDTO)record.getDto()).getMainAuthor().getControlNumber());
						String authorsInstitutions = "";
						if((record.getDto() instanceof PublicationDTO) && (((PublicationDTO)record.getDto()).getAllAuthors() != null) && (((PublicationDTO)record.getDto()).getAllAuthors().size()>0)){
							authorsInstitutions = "";
							boolean check = false;
							for (AuthorDTO author : ((PublicationDTO)record.getDto()).getAllAuthors()) {
								if((author.getInstitutionName() != null) && ((author.getInstitutionName().contains("Tehnološki fakultet Novi Sad, Univerzitet u Novom Sadu")) || (author.getInstitutionName().contains("Prirodno-matematički fakultet u Novom Sadu, Univerzitet u Novom Sadu")))){
									if((authorsInstitutions.length() == 0)){
										authorsInstitutions = author.getInstitutionName();
									} else if(! authorsInstitutions.contains(author.getInstitutionName())){
											authorsInstitutions += "; " + author.getInstitutionName();
									}
								} else {
									check = true;
								}
							}
							if(check && (! authorsInstitutions.contains(";")))
								authorsInstitutions = "Potrebna provera";
						}
						resultForType.setAuthorsInstitutions(authorsInstitutions);
					} else
					if(record.getDto() instanceof PaperMonographDTO){
						resultForType.setFirstAuthorId(((PaperMonographDTO)record.getDto()).getMainAuthor().getControlNumber());
						String authorsInstitutions = "";
						if((record.getDto() instanceof PublicationDTO) && (((PublicationDTO)record.getDto()).getAllAuthors() != null) && (((PublicationDTO)record.getDto()).getAllAuthors().size()>0)){
							authorsInstitutions = "";
							boolean check = false;
							for (AuthorDTO author : ((PublicationDTO)record.getDto()).getAllAuthors()) {
								if((author.getInstitutionName() != null) && ((author.getInstitutionName().contains("Tehnološki fakultet Novi Sad, Univerzitet u Novom Sadu")) || (author.getInstitutionName().contains("Prirodno-matematički fakultet u Novom Sadu, Univerzitet u Novom Sadu")))){
									if((authorsInstitutions.length() == 0)){
										authorsInstitutions = author.getInstitutionName();
									} else if(! authorsInstitutions.contains(author.getInstitutionName())){
											authorsInstitutions += "; " + author.getInstitutionName();
									}
								} else {
									check = true;
								}
							}
							if(check && (! authorsInstitutions.contains(";")))
								authorsInstitutions = "Potrebna provera";
						}
						resultForType.setAuthorsInstitutions(authorsInstitutions);
					} else
					if(record.getDto() instanceof MonographDTO){
						resultForType.setFirstAuthorId(((MonographDTO)record.getDto()).getMainAuthor().getControlNumber());
						String authorsInstitutions = "";
						if((record.getDto() instanceof PublicationDTO) && (((PublicationDTO)record.getDto()).getAllAuthors() != null) && (((PublicationDTO)record.getDto()).getAllAuthors().size()>0)){
							authorsInstitutions = "";
							boolean check = false;
							for (AuthorDTO author : ((PublicationDTO)record.getDto()).getAllAuthors()) {
								if((author.getInstitutionName() != null) && ((author.getInstitutionName().contains("Tehnološki fakultet Novi Sad, Univerzitet u Novom Sadu")) || (author.getInstitutionName().contains("Prirodno-matematički fakultet u Novom Sadu, Univerzitet u Novom Sadu")))){
									if((authorsInstitutions.length() == 0)){
										authorsInstitutions = author.getInstitutionName();
									} else if(! authorsInstitutions.contains(author.getInstitutionName())){
											authorsInstitutions += "; " + author.getInstitutionName();
									}
								} else {
									check = true;
								}
							}
							if(check && (! authorsInstitutions.contains(";")))
								authorsInstitutions = "Potrebna provera";
						}
						resultForType.setAuthorsInstitutions(authorsInstitutions);
					} else
					if(record.getDto() instanceof PaperProceedingsDTO ){
						resultForType.setFirstAuthorId(((PaperProceedingsDTO)record.getDto()).getMainAuthor().getControlNumber());
						String authorsInstitutions = "";
						if((record.getDto() instanceof PublicationDTO) && (((PublicationDTO)record.getDto()).getAllAuthors() != null) && (((PublicationDTO)record.getDto()).getAllAuthors().size()>0)){
							authorsInstitutions = "";
							boolean check = false;
							for (AuthorDTO author : ((PublicationDTO)record.getDto()).getAllAuthors()) {
								if((author.getInstitutionName() != null) && ((author.getInstitutionName().contains("Tehnološki fakultet Novi Sad, Univerzitet u Novom Sadu")) || (author.getInstitutionName().contains("Prirodno-matematički fakultet u Novom Sadu, Univerzitet u Novom Sadu")))){
									if((authorsInstitutions.length() == 0)){
										authorsInstitutions = author.getInstitutionName();
									} else if(! authorsInstitutions.contains(author.getInstitutionName())){
											authorsInstitutions += "; " + author.getInstitutionName();
									}
								} else {
									check = true;
								}
							}
							if(check && (! authorsInstitutions.contains(";")))
								authorsInstitutions = "Potrebna provera";
						}
						resultForType.setAuthorsInstitutions(authorsInstitutions);
					} else
					if(record.getDto() instanceof StudyFinalDocumentDTO){
						resultForType.setFirstAuthorId(((StudyFinalDocumentDTO)record.getDto()).getAuthor().getControlNumber());
						String authorsInstitutions = "";
						if((record.getDto() instanceof PublicationDTO) && (((PublicationDTO)record.getDto()).getAllAuthors() != null) && (((PublicationDTO)record.getDto()).getAllAuthors().size()>0)){
							authorsInstitutions = "";
							boolean check = false;
							for (AuthorDTO author : ((PublicationDTO)record.getDto()).getAllAuthors()) {
								if((author.getInstitutionName() != null) && ((author.getInstitutionName().contains("Tehnološki fakultet Novi Sad, Univerzitet u Novom Sadu")) || (author.getInstitutionName().contains("Prirodno-matematički fakultet u Novom Sadu, Univerzitet u Novom Sadu")))){
									if((authorsInstitutions.length() == 0)){
										authorsInstitutions = author.getInstitutionName();
									} else if(! authorsInstitutions.contains(author.getInstitutionName())){
											authorsInstitutions += "; " + author.getInstitutionName();
									}
								} else {
									check = true;
								}
							}
							if(check && (! authorsInstitutions.contains(";")))
								authorsInstitutions = "Potrebna provera";
						}
						resultForType.setAuthorsInstitutions(authorsInstitutions);
					} 
						
					if(! (map.containsKey(publicationYear))){
						map.put(publicationYear, new ResultForYearDTO());
						map.get(publicationYear).setYear(publicationYear);
					} 
					map.get(publicationYear).getResultsForType().add(resultForType);
				} 
			}catch(Exception ex){
				ex.printStackTrace();
				System.out.println(record.getControlNumber());
			}
			record.clear();	
		}
		
		/*
		ResultForTypeDTO resultForType = new ResultForTypeDTO();
		resultForType.setResultType("M26"); // Bojana ce ovo srediti 
		resultForType.setResult("Mirjana Ivanovic, Comsis, 2011");
		map.get("2011").getResultsForType().add(resultForType);
		resultForType = new ResultForTypeDTO();
		resultForType.setResultType("M26"); // Bojana ce ovo srediti
		resultForType.setResult("Bikit Istvan, 2011");
		map.get("2011").getResultsForType().add(resultForType);
		*/
		
		retVal = new ArrayList<ResultForYearDTO>(map.values());
//		Collections.sort(retVal, new GenericComparator<ResultForYearDTO>(
//				"resultType", "asc"));
		for(ResultForYearDTO resForYear:retVal){			
			prepareResults(resForYear);
		}	 
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return retVal;
	}
	
	
	public static List<ResultForYearDTO> getSCIResultsWord(List<String> ids, List<String> years){
		List<ResultForYearDTO> retVal = null;
		TermQuery termQuery = null;
		BooleanQuery query = new BooleanQuery();
		if(ids != null){
			BooleanQuery idsQuery = new BooleanQuery();
			for (String id : ids) {		
				termQuery = new TermQuery(new Term("AUCN", id));
				idsQuery.add(termQuery, Occur.SHOULD);
				termQuery = new TermQuery(new Term("EDCN", id));
	//			idsQuery.add(termQuery, Occur.SHOULD);
			}
			query.add(idsQuery, Occur.MUST);
		}
		if(years != null){
			BooleanQuery yearsQuery = new BooleanQuery();
			for (String year : years) {
				termQuery = new TermQuery(new Term("PY", year));
				yearsQuery.add(termQuery, Occur.SHOULD);
			}
			query.add(yearsQuery, Occur.MUST);
		}
		BooleanQuery resultTypesQuery = new BooleanQuery();
		termQuery = new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL));
		resultTypesQuery.add(termQuery, Occur.SHOULD);
		termQuery = new TermQuery(new Term("TYPE", Types.SCIENTIFIC_CRITICISM_JOURNAL));
		resultTypesQuery.add(termQuery, Occur.SHOULD);
		query.add(resultTypesQuery, Occur.MUST);
		
		List<Record> records = Retriever.select(query, new AllDocCollector(false));		
		Map<String, ResultForYearDTO> map = new HashMap<String, ResultForYearDTO>();
		for (Record record : records) {
			log.info("Processing control number: "+record.getControlNumber());
			String resultType = SamovrednovanjeUtils.getResultType(record);
			if((resultType != null) && (("M21a".equals(resultType)) || ("M21".equals(resultType)) || ("M22".equals(resultType)) || ("M23".equals(resultType)))){
				ResultForTypeDTO resultForType = new ResultForTypeDTO(record);
				resultForType.setResultType(resultType); 
				resultForType.setResult(record.getDto().getStringRepresentation());
				String publicationYear = ((PublicationDTO)(record.getDto())).getPublicationYear();
				if(! (map.containsKey(publicationYear))){
					map.put(publicationYear, new ResultForYearDTO());
					map.get(publicationYear).setYear(publicationYear);
				} 
				map.get(publicationYear).getResultsForType().add(resultForType);
			}
			record.clear();
		}
		retVal = new ArrayList<ResultForYearDTO>(map.values());
		Collections.sort(retVal, new GenericComparator<ResultForYearDTO>(
				"year", "asc")); 
		for(ResultForYearDTO resForYear:retVal){
			resForYear.setNumbers();
			prepareResults(resForYear);
			
		}	 
		return retVal;
	}
	
	public static List<ResultForYearDTO> getM99Word(List<String> ids, List<String> years){
		List<ResultForYearDTO> retVal = null;
		TermQuery termQuery = null;
		BooleanQuery query = new BooleanQuery();
		if(ids != null){
			BooleanQuery idsQuery = new BooleanQuery();
			for (String id : ids) {		
				termQuery = new TermQuery(new Term("AUCN", id));
				idsQuery.add(termQuery, Occur.SHOULD);
				termQuery = new TermQuery(new Term("EDCN", id));
	//			idsQuery.add(termQuery, Occur.SHOULD);
			}
			query.add(idsQuery, Occur.MUST);
		}
		if(years != null){
			BooleanQuery yearsQuery = new BooleanQuery();
			for (String year : years) {
				termQuery = new TermQuery(new Term("PY", year));
				yearsQuery.add(termQuery, Occur.SHOULD);
			}
			query.add(yearsQuery, Occur.MUST);
		}
		BooleanQuery resultTypesQuery = new BooleanQuery();
		termQuery = new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL));
		resultTypesQuery.add(termQuery, Occur.SHOULD);
		termQuery = new TermQuery(new Term("TYPE", Types.SCIENTIFIC_CRITICISM_JOURNAL));
		resultTypesQuery.add(termQuery, Occur.SHOULD);
		query.add(resultTypesQuery, Occur.MUST);
		
		List<Record> records = Retriever.select(query, new AllDocCollector(false));		
		Map<String, ResultForYearDTO> map = new HashMap<String, ResultForYearDTO>();
		for (Record record : records) {
			log.info("Processing control number: "+record.getControlNumber());
			String resultType = SamovrednovanjeUtils.getResultType(record);
			if((resultType == null) || ("M99".equals(resultType))) {
				ResultForTypeDTO resultForType = new ResultForTypeDTO(record);
				resultForType.setResultType(resultType); 
				resultForType.setResult(record.getDto().getStringRepresentation());
				String publicationYear = ((PublicationDTO)(record.getDto())).getPublicationYear();
				if(! (map.containsKey(publicationYear))){
					map.put(publicationYear, new ResultForYearDTO());
					map.get(publicationYear).setYear(publicationYear);
				} 
				map.get(publicationYear).getResultsForType().add(resultForType);
			}
			record.clear();
		}
		retVal = new ArrayList<ResultForYearDTO>(map.values());
		Collections.sort(retVal, new GenericComparator<ResultForYearDTO>(
				"year", "asc")); 
		/*for(ResultForYearDTO resForYear:retVal){
			resForYear.setNumbers();
			prepareResults(resForYear);
			
		}	 */
		return retVal;
	}


	
	
	/**
	 * TODO
	 * 
	 * u strukturi List<ResultForYearDTO> vratati sve rezultate koje zadovoljavaju sledece kriterijume
	 * 
	 * 1. neko od autora ima id iz liste @param ids 
	 * 2. objavljen je u godini koja se nalazi u listi  @param years 
	 * 3. pripada kategoriji koja se nalazi u listi @param resultTypes (za tabelu 6.1 lista ce sadrzati M21, M22, M23), 
	 * 			lista resultTypes moze biti null onda da vrati za sve kategorije (za tabelu 6.4).  
	 * 
	 * ako je komlikovano napraviti ovakav opsti upit mogu i dve operacije 
	 * jedna za tabelu 6.1 (2007-2011) i jedna za 6.4 (2011)
	 * 
	 * 
	 */
	public static List<ResultForYearDTO> getSCIResults(List<String> ids, List<String> years){
		List<ResultForYearDTO> retVal = null;
		TermQuery termQuery = null;
		BooleanQuery query = new BooleanQuery();
		if(ids != null){
			BooleanQuery idsQuery = new BooleanQuery();
			for (String id : ids) {		
				termQuery = new TermQuery(new Term("AUCN", id));
				idsQuery.add(termQuery, Occur.SHOULD);
				termQuery = new TermQuery(new Term("EDCN", id));
	//			idsQuery.add(termQuery, Occur.SHOULD);
			}
			query.add(idsQuery, Occur.MUST);
		}
		if(years != null){
			BooleanQuery yearsQuery = new BooleanQuery();
			for (String year : years) {
				termQuery = new TermQuery(new Term("PY", year));
				yearsQuery.add(termQuery, Occur.SHOULD);
			}
			query.add(yearsQuery, Occur.MUST);
		}
		BooleanQuery resultTypesQuery = new BooleanQuery();
		termQuery = new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL));
		resultTypesQuery.add(termQuery, Occur.SHOULD);
		termQuery = new TermQuery(new Term("TYPE", Types.SCIENTIFIC_CRITICISM_JOURNAL));
		resultTypesQuery.add(termQuery, Occur.SHOULD);
		query.add(resultTypesQuery, Occur.MUST);
		
		List<Record> records = Retriever.select(query, new AllDocCollector(false));		
		Map<String, ResultForYearDTO> map = new HashMap<String, ResultForYearDTO>();
		for (Record record : records) {
			log.info("Processing control number: "+record.getControlNumber());
			String resultType = SamovrednovanjeUtils.getResultType(record);
			if((resultType != null) && (("M21".equals(resultType)) || ("M22".equals(resultType)) || ("M23".equals(resultType)))){
				ResultForTypeDTO resultForType = new ResultForTypeDTO(record);
				resultForType.setResultType(resultType); 
				resultForType.setResult(TemplateRunner.getRepresentation(record.getDto(),TemplateRunner.PMFSAMOVREDNOVANJE));
				String publicationYear = ((PublicationDTO)(record.getDto())).getPublicationYear();
				if(! (map.containsKey(publicationYear))){
					map.put(publicationYear, new ResultForYearDTO());
					map.get(publicationYear).setYear(publicationYear);
				} 
				map.get(publicationYear).getResultsForType().add(resultForType);
			}
			record.clear();
		}
		retVal = new ArrayList<ResultForYearDTO>(map.values());
		Collections.sort(retVal, new GenericComparator<ResultForYearDTO>(
				"year", "asc")); 
		for(ResultForYearDTO resForYear:retVal){
			resForYear.setNumbers();
			prepareResults(resForYear);
			
		}	 
		return retVal;
	}
	
	
	public static List<ResultForYearDTO> getSCIResultsCount(List<String> ids, List<String> years){
		List<ResultForYearDTO> retVal = null;
		TermQuery termQuery = null;
		BooleanQuery query = new BooleanQuery();
		if(ids != null){
			BooleanQuery idsQuery = new BooleanQuery();
			for (String id : ids) {		
				termQuery = new TermQuery(new Term("AUCN", id));
				idsQuery.add(termQuery, Occur.SHOULD);
				termQuery = new TermQuery(new Term("EDCN", id));
	//			idsQuery.add(termQuery, Occur.SHOULD);
			}
			query.add(idsQuery, Occur.MUST);
		}
		if(years != null){
			BooleanQuery yearsQuery = new BooleanQuery();
			for (String year : years) {
				termQuery = new TermQuery(new Term("PY", year));
				yearsQuery.add(termQuery, Occur.SHOULD);
			}
			query.add(yearsQuery, Occur.MUST);
		}
		BooleanQuery resultTypesQuery = new BooleanQuery();
		termQuery = new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL));
		resultTypesQuery.add(termQuery, Occur.SHOULD);
		termQuery = new TermQuery(new Term("TYPE", Types.SCIENTIFIC_CRITICISM_JOURNAL));
		resultTypesQuery.add(termQuery, Occur.SHOULD);
		query.add(resultTypesQuery, Occur.MUST);
		
		List<Record> records = Retriever.select(query, new AllDocCollector(false));	
		
		Map<String, ResultForYearDTO> map = new HashMap<String, ResultForYearDTO>();
		for (Record record : records) {
			log.info("Processing control number: "+record.getControlNumber());
			String resultType = SamovrednovanjeUtils.getResultType(record);
			if((resultType != null) && (("M21a".equals(resultType)) || ("M21".equals(resultType)) || ("M22".equals(resultType)) || ("M23".equals(resultType)))){
				ResultForTypeDTO resultForType = new ResultForTypeDTO(record);
				resultForType.setResultType(resultType); 
				//resultForType.setResult(TemplateRunner.getRepresentation(record.getDto(),TemplateRunner.PMFSAMOVREDNOVANJE));
				String publicationYear = ((PublicationDTO)(record.getDto())).getPublicationYear();
				if(! (map.containsKey(publicationYear))){
					map.put(publicationYear, new ResultForYearDTO());
					map.get(publicationYear).setYear(publicationYear);
				} 
				map.get(publicationYear).getResultsForType().add(resultForType);
			}
			record.clear();
		}
		retVal = new ArrayList<ResultForYearDTO>(map.values());
		Collections.sort(retVal, new GenericComparator<ResultForYearDTO>(
				"year", "asc")); 
		for(ResultForYearDTO resForYear:retVal){
			resForYear.setNumbers();
			prepareResults(resForYear);
			
		}	 
		return retVal;
	}
	
	
	public static List<ResultForTypeDTO> getResultsForType(String type, List<String> ids, List<String> years){
		
		String whereClause = "RECORDID in (SELECT RECORDID FROM MARC21RECORD_CLASS M where CFCLASSID like '"+type+"')";
	 RecordDAO recordDAO = new RecordDAO(new RecordDB());
		List<Record> recordsByType = 		recordDAO.getRecordsIdsFromDatabaseByWhereClause(whereClause);

		List<ResultForTypeDTO> retVal = new ArrayList<ResultForTypeDTO>();
		TermQuery termQuery = null;
		BooleanQuery query = new BooleanQuery();
		if(ids != null){
			BooleanQuery idsQuery = new BooleanQuery();
			for (String id : ids) {		
				termQuery = new TermQuery(new Term("AUCN", id));
				idsQuery.add(termQuery, Occur.SHOULD);
				termQuery = new TermQuery(new Term("EDCN", id));
	//			idsQuery.add(termQuery, Occur.SHOULD);
			
				query.add(idsQuery, Occur.MUST);
		
				if(years != null){
					BooleanQuery yearsQuery = new BooleanQuery();
					for (String year : years) {
						termQuery = new TermQuery(new Term("PY", year));
						yearsQuery.add(termQuery, Occur.SHOULD);
					}
					query.add(yearsQuery, Occur.MUST);
				}
				BooleanQuery resultTypesQuery = new BooleanQuery();
				termQuery = new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL));
				resultTypesQuery.add(termQuery, Occur.SHOULD);
				termQuery = new TermQuery(new Term("TYPE", Types.SCIENTIFIC_CRITICISM_JOURNAL));
				resultTypesQuery.add(termQuery, Occur.SHOULD);
				query.add(resultTypesQuery, Occur.MUST);		
				List<Record> records = Retriever.select(query, new AllDocCollector(false));		
				for (Record record : records) {
					log.info("Processing control number: "+record.getControlNumber());
					String resultType = SamovrednovanjeUtils.getResultType(record);
					log.info("resultType="+resultType+", type="+type);
					if((resultType != null) && ((type.equals(resultType)))){	
						String institution = "";
						if((record.getDto() instanceof PublicationDTO) && (((PublicationDTO)record.getDto()).getAllAuthors() != null) && (((PublicationDTO)record.getDto()).getAllAuthors().size()>0)){
							institution = "";
							boolean check = false;
							for (AuthorDTO author : ((PublicationDTO)record.getDto()).getAllAuthors()) {
								if((author.getInstitutionName() != null) && ((author.getInstitutionName().contains("Tehnološki fakultet Novi Sad, Univerzitet u Novom Sadu")) || (author.getInstitutionName().contains("Prirodno-matematički fakultet u Novom Sadu, Univerzitet u Novom Sadu")))){
									if((institution.length() == 0)){
										institution = author.getInstitutionName();
									} else if(! institution.contains(author.getInstitutionName())){
										institution += "; " + author.getInstitutionName();
									}
								} else {
									check = true;
								}
							}
							if(check && (! institution.contains(";")))
								institution = "Potrebna provera";
						}
						retVal.add(new ResultForTypeDTO(type, TemplateRunner.getRepresentation(record.getDto(),TemplateRunner.PMFSAMOVREDNOVANJE), institution, record));		
						}
					record.clear();
				}
			}
		}else{
			for (Record record : recordsByType) {
				log.info("Processing control number: "+record.getControlNumber());
				String resultType = SamovrednovanjeUtils.getResultType(record);
				log.info("resultType="+resultType+", type="+type);
				if((resultType != null) && ((type.equals(resultType)))){	
					String institution = "";
					if((record.getDto() instanceof PublicationDTO) && (((PublicationDTO)record.getDto()).getAllAuthors() != null) && (((PublicationDTO)record.getDto()).getAllAuthors().size()>0) && (((PublicationDTO)record.getDto()).getAllAuthors().get(0).getInstitutionName() != null))
						institution = ((PublicationDTO)record.getDto()).getAllAuthors().get(0).getInstitutionName();
					retVal.add(new ResultForTypeDTO(type, TemplateRunner.getRepresentation(record.getDto(),TemplateRunner.PMFSAMOVREDNOVANJE), institution, record));		
					}
				record.clear();
			}
		}	
		return retVal;
	}
	
	
	
	

	public static String getResultType(Record record){
		ResultMeasureDTO retVal = SamovrednovanjeUtils.getResultMeasure(record);
		if(retVal == null)
			return null;
		else			
			return retVal.getResultType().getClassId();		
	}
	
	public static ResultMeasureDTO getResultMeasure(Record record) {
		ResultMeasureDTO retVal = null;
	    //Connection conn = null;
		EvaluationDB evaluationDB = new EvaluationDB();
		CommissionDAO commissionDAO = new CommissionDAO(); 
		try{
			conn = null;
			conn = dataSource.getConnection();
			
			List<RuleBookDTO> ruleBookList = evaluationDB.getRuleBooks(conn);
	        RuleBookDTO ruleBook = null;
			for (RuleBookDTO rb : ruleBookList){
	        	if("serbianResearchersEvaluation".equals(rb.getClassId()))
	        		ruleBook = rb;
	        }
		
			List<CommissionDTO> allCommissionList = commissionDAO.getCommissions();
		
			
			return SamovrednovanjeUtils.getResultMeasure(record, ruleBook, allCommissionList, false);
		}catch (Throwable e) {
			
			e.printStackTrace();		
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
		 }
		}
		return retVal;
	}
	
	public static ResultMeasureDTO getResultMeasure(Record record, RuleBookDTO ruleBook, List<CommissionDTO> allCommissionList, boolean calculate) {
		ResultMeasureDTO retVal = null;
		try{
			List<CommissionDTO> commissionList = new ArrayList<CommissionDTO>();
			PublicationDTO publicationDTO = (PublicationDTO)(record.getDto());
			List<AuthorDTO> authorsAndEditors = publicationDTO.getAllAuthors();
			authorsAndEditors.addAll(publicationDTO.getEditors());
			if(publicationDTO instanceof StudyFinalDocumentDTO) {
				StudyFinalDocumentDTO theses = (StudyFinalDocumentDTO)publicationDTO;
				if((theses.getInstitution().getControlNumber() != null) && (theses.getInstitution().getControlNumber().equals("(BISIS)5929"))){
					authorsAndEditors.addAll(theses.getAdvisors());
				}
			}
			for (AuthorDTO author : authorsAndEditors) {
				Integer commissionId = null;
				OrganizationUnitDTO rootOrganizationUnit = author.getOrganizationUnit();
				if((rootOrganizationUnit != null) && (rootOrganizationUnit.getControlNumber()!=null)){
					String orgUnitControlNumber = rootOrganizationUnit.getControlNumber();
					 if(orgUnitControlNumber.equals("(BISIS)6868"))
						    commissionId = 725;
					 else  if(orgUnitControlNumber.equals("(BISIS)6873"))
						    commissionId = 724;
					 else  if(orgUnitControlNumber.equals("(BISIS)6883"))
						    commissionId = 722;
					 else  if(orgUnitControlNumber.equals("(BISIS)6875") || orgUnitControlNumber.equals("(BISIS)6877"))
						 commissionId = 723;
					 else {
						 while (rootOrganizationUnit.getSuperOrganizationUnit() != null){
							 rootOrganizationUnit = rootOrganizationUnit.getSuperOrganizationUnit();
						 }
							orgUnitControlNumber = rootOrganizationUnit.getControlNumber();
							if(orgUnitControlNumber != null){
							   if(orgUnitControlNumber.equals("(BISIS)6782"))
							    commissionId = 711;
							   if(orgUnitControlNumber.equals("(BISIS)6781"))
							    commissionId = 712;
							   if(orgUnitControlNumber.equals("(BISIS)6780"))
							    commissionId = 713;
							   if(orgUnitControlNumber.equals("(BISIS)6779"))
							    commissionId = 714;
							   if(orgUnitControlNumber.equals("(BISIS)6778"))
							    commissionId = 715;
							}	
					 }
				}
					if(commissionId == null && author.getInstitution()!=null && author.getInstitution().getControlNumber()!=null) {					
//						if	(author.getInstitution().getControlNumber().equals("(BISIS)5933")){
//								commissionId = 701;
//						} else
						if	(author.getInstitution().getControlNumber().equals("(BISIS)5929")){
							commissionId = 710;
						} 	
					}				
						
				
				if(commissionId!=null){
					if(publicationDTO instanceof PaperJournalDTO)
						for (CommissionDTO commissionDTO : allCommissionList) {
							if((commissionDTO.getCommissionId().equals(commissionId)) || (commissionDTO.getCommissionId().equals(commissionId + 10))){
								if(! commissionList.contains(commissionDTO))
									commissionList.add(commissionDTO);
							}
						}
					else 
						for (CommissionDTO commissionDTO : allCommissionList) {
							if((! commissionList.contains(commissionDTO)) && (commissionDTO.getCommissionId() != 0)){
									commissionList.add(commissionDTO);
							}
						}
						
				}
			}
			retVal = ResultEvaluator.getResultType(record, ruleBook, commissionList, calculate);
		}catch (Throwable e) {
			e.printStackTrace();		
		}finally {
		/*	try {
				conn.close();
			} catch (SQLException e) {
			}*/
		}
		return retVal;
	}
		
	
	
	/**
	 * TODO 
	 * 
	 * Vraca sve ljude ciji je id u listi ids 
	 * koji imaju pravo da budu mentori, a to su oni koji u prethdnih 10 godina [2002,2011]
	 * imaju bar 5 radova kategorije M21, M22, M23 
	 * 
	 * Klasa Mentor ima i brojeve radova po kategorijama
	 * 
	 */
	
	public static List<MentorDTO> getMentors(List<String> ids, String endYear, int numberOfYears, int minNumber ){
		List<MentorDTO> retVal = null; 
		
		RecordDAO recordDAO = new RecordDAO(new PersonDB());
		
		List<String> years = new ArrayList<String>();
		int endYearInt = Integer.valueOf(endYear);
		for(int i=0;i<numberOfYears;i++){
			years.add(String.valueOf(endYearInt-i));
		}
		
		log.info("PMF ist: " + ids.size());
		
		List<String> nastavnici =  new ArrayList<String>();
		nastavnici.add("100");  // naucni savetnik
		nastavnici.add("80");   // naucni saradnik
		nastavnici.add("90");   // visi naucni saradnik
		nastavnici.add("30");   // docent
		nastavnici.add("40");   // vanredni profesor
		nastavnici.add("50");   // redovni profesor
		
		
		
		
		for (String id : ids) {
			AuthorDTO author = (AuthorDTO)recordDAO.getDTO(id);
			if((!nastavnici.contains(author.getCurrentPosition().getPosition().getClassId()))
					|| (author.getControlNumber().equals("(BISIS)941"))) continue;	
			log.info("Processing author for control number "+author.getControlNumber());
			brojNastavnika++;	
			List<String> idsTemp = new ArrayList<String>();
			idsTemp.add(id);
			List<ResultForYearDTO> results = SamovrednovanjeUtils.getSCIResultsCount(idsTemp, years);
			int m21aCounter = 0;
			int m21Counter = 0;
			int m22Counter = 0;
			int m23Counter = 0;
			for (ResultForYearDTO resultForYearDTO : results) {
				resultForYearDTO.setNumbers();
				m21aCounter += resultForYearDTO.getNumM21a();
				m21Counter += resultForYearDTO.getNumM21();
				m22Counter += resultForYearDTO.getNumM22();
				m23Counter += resultForYearDTO.getNumM23();
			}
			if((m21aCounter + m21Counter + m22Counter + m23Counter) >= minNumber){
				// ovde mozda treba dodati i neki uslov za trenutno zvanje istrazivaca, 
//				ne moze asistent biti mentor za doktorat pa sve i da ima 5 radova sa SCI liste
				
				brojMentora++;
			
				MentorDTO mentor = new MentorDTO();
				mentor.setCurrentPositionName(author.getCurrentPositionName());
				mentor.setFirstName(author.getName().getFirstname());
				mentor.setLastName(author.getName().getLastname());
				mentor.setTitle(author.getTitle());
				mentor.setJmbg(author.getJmbg());
				/*if ((author.getJmbg() == null) || ("".equals(author.getJmbg().trim()))) {
					if ((author.getYearOfBirth() != null) && (author.getYearOfBirth() > 1981) && (author.getYearOfBirth() < 1989)){
						mentor.setJmbg(author.getYearOfBirth().toString());
					}
				}*/
				mentor.setNumM21a(m21aCounter);
				mentor.setNumM21(m21Counter);
				mentor.setNumM22(m22Counter);
				mentor.setNumM23(m23Counter);
				if(retVal == null)
					retVal = new ArrayList<MentorDTO>();
				retVal.add(mentor);
				/*if((mentor.getJmbg() != null) && 
						((mentor.getJmbg().contains("982")) || (mentor.getJmbg().contains("983")) || (mentor.getJmbg().contains("984")) || (mentor.getJmbg().contains("985")) || (mentor.getJmbg().contains("986")) || (mentor.getJmbg().contains("987")) || (mentor.getJmbg().contains("988")))){
					System.out.println(mentor.getJmbg());
					retVal.add(mentor);
				} else if((mentor.getJmbg() == null) || ("".equals(mentor.getJmbg().trim()))){
					System.out.println("NULLLL");
					retVal.add(mentor);
				}*/
			}
		}
		Collections.sort(retVal, new GenericComparator<MentorDTO>(
				"lastName", "asc")); 
		
		return retVal;
	}
	
	public static void prepareResults(ResultForYearDTO resForYear){
		List<String> attributes = new ArrayList<String>();
		attributes.add("resultType");
		attributes.add("result");
		
		List<String> directions = new ArrayList<String>();
		directions.add("asc");
		directions.add("asc");		
		
		try{
		Collections.sort(resForYear.getResultsForType(),new GenericComparator<ResultForTypeDTO>(
				attributes, directions)); 
		} catch(Exception e){
			
		}
		
		
	}
	
	//TODO Dragan
	// endYear - poslednja godina, pa tri unazad
	public static List<ThreeYearsSCIResults> getThreeYearsSCIResults(String endYear, List<String> ids){
		
		List<ThreeYearsSCIResults> retVal = new ArrayList<ThreeYearsSCIResults>();
		
		TermQuery termQuery = null;
		
		
		String[] yearsArray = {"" + (Integer.parseInt(endYear) - 2), "" + (Integer.parseInt(endYear) - 1), endYear};
		List<String> years = Arrays.asList(yearsArray); 
		BooleanQuery yearsQuery = new BooleanQuery();
		if(years != null){
			for (String year : years) {
				termQuery = new TermQuery(new Term("PY", year));
				yearsQuery.add(termQuery, Occur.SHOULD);
			}
		}
		RecordDAO recordDAO = new RecordDAO(new PersonDB());
		
		for (String id : ids) {
			BooleanQuery query = new BooleanQuery();
			query.add(yearsQuery, Occur.MUST);
			AuthorDTO author = (AuthorDTO)recordDAO.getDTO(id);
			BooleanQuery idsQuery = new BooleanQuery();
			termQuery = new TermQuery(new Term("AUCN", id));
			idsQuery.add(termQuery, Occur.SHOULD);
			query.add(idsQuery, Occur.MUST);
			
			int M211 = 0;
			int M221 = 0;
			int M231 = 0;
			int M212 = 0;
			int M222 = 0;
			int M232 = 0;
			int M213 = 0;
			int M223 = 0;
			int M233 = 0;
			
			BooleanQuery resultTypesQuery = new BooleanQuery();
			termQuery = new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL));
			resultTypesQuery.add(termQuery, Occur.SHOULD);
			query.add(resultTypesQuery, Occur.MUST);
//			log.info(query);
			List<Record> records = Retriever.select(query, new AllDocCollector(false));		
			for (Record record : records) {
//				log.info("Processing control number: "+record.getControlNumber());
				String resultType = SamovrednovanjeUtils.getResultType(record);
//				log.info(resultType);
				if((resultType != null) && (("M21".equals(resultType)) || ("M22".equals(resultType)) || ("M23".equals(resultType)))){
//					log.info(resultType);
					if(record.getDto() instanceof PublicationDTO){
						PublicationDTO publication = (PublicationDTO) record.getDto();
						String publicationYear = publication.getPublicationYear();
						if(publicationYear != null) {
//							log.info(publicationYear);
							if(publicationYear.equals("" + (Integer.parseInt(endYear) - 2))){
								if(("M21".equals(resultType)))
									M211++;
								else if(("M22".equals(resultType)))
									M221++;
								else if(("M23".equals(resultType)))
									M231++;
							} else if (publicationYear.equals("" + (Integer.parseInt(endYear) - 1))){
								if(("M21".equals(resultType)))
									M212++;
								else if(("M22".equals(resultType)))
									M222++;
								else if(("M23".equals(resultType)))
									M232++;
							} else if (publicationYear.equals(endYear)) {
								if(("M21".equals(resultType)))
									M213++;
								else if(("M22".equals(resultType)))
									M223++;
								else if(("M23".equals(resultType)))
									M233++;
							}
						}
					}
				}
				record.clear();
			}
			retVal.add(new ThreeYearsSCIResults(LatCyrUtils.toLatin(author.getName().getFirstname()), LatCyrUtils.toLatin(author.getName().getLastname()),LatCyrUtils.toLatin(author.getCurrentPositionName()), M211, M221, M231, M212, M222, M223, M213, M223, M233));
		}
		Collections.sort(retVal, new GenericComparator<ThreeYearsSCIResults>(
				"currentPositionName", "asc"));  
		return retVal;
	}
	
	public static void setMyDataSource(String hostname, String port, String schema, String connectionParameters, String username, String password){
		dataSource = new MyDataSource(hostname, port, schema, connectionParameters, username, password);
	}
	
	
	
	
	
	
	
}
