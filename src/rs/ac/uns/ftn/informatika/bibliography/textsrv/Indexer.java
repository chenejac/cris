package rs.ac.uns.ftn.informatika.bibliography.textsrv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.DefaultSimilarity;

import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes.PrefixConverter;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes.PrefixValue;

import com.gint.util.string.StringUtils;

/**
 * Implements a indexer.
 * 
 * @author Dragan Ivanovic chenejac@uns.ac.rs
 */
public class Indexer {

	private int numberOfChangesFromLastOptimize = -1;
	
	/**
	 * @param indexPath
	 *            path to the folder with Lucene index files
	 * @param analyzer
	 *            analyzer for Lucene
	 */
	public Indexer(String indexPath, Analyzer analyzer) {
		this.indexPath = indexPath;
		if (analyzer != null)
			this.analyzer = analyzer;
		else
			this.analyzer = new CrisAnalyzer();
	}

	/**
	 * Adds a new mARC21Record to the index.
	 * 
	 * @param rec
	 *            MARC21Record to be added
	 * @return true if successful
	 */
	public boolean add(Record rec) {
		try {
			Document doc = getDocument(rec);
			numberOfChangesFromLastOptimize++;
			synchronized(this){
				IndexWriter iw = getIndexWriter();
				iw.addDocument(doc);
				iw.commit();
				if(numberOfChangesFromLastOptimize % 1000 == 0){
					numberOfChangesFromLastOptimize = 0;
					iw.optimize();
				}
			}
		} catch (IOException ex) {
			log.fatal(ex);
			return false;
		} catch (Exception e){
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Updates a mARC21Record in the index
	 * 
	 * @param rec
	 *            MARC21Record to be updated
	 * @return true if successful
	 */
	public boolean update(Record rec) {
		boolean retVal = false;
		Document doc = Retriever.selectDocument(rec.getControlNumber());
		if(doc == null){
			retVal = add(rec);
		} else {
			Document document = getDocument(rec);
			try{
				numberOfChangesFromLastOptimize++;
				synchronized(this){
					IndexWriter iw = getIndexWriter();
					iw.updateDocument(new Term("CN",rec.getControlNumber()), document);
					iw.commit();
					retVal = true;
					if(numberOfChangesFromLastOptimize % 1000 == 0){
						numberOfChangesFromLastOptimize = 0;
						iw.optimize();
					}
				}
			} catch (Exception ex) {
				log.fatal(ex);
			}
		}
		return retVal;
	}
	
	/**
	 * Updates a mARC21Record in the index
	 * 
	 * @param controlNumber
	 *            controlNumber of MARC21Record to be updated
	 * @return true if successful
	 */
	public boolean update(String controlNumber, List<PrefixValue> fields) {
		boolean retVal = false;
		Document doc = Retriever.selectDocument(controlNumber);
		if(doc != null){
			for (PrefixValue prefixValue : fields) {
				doc.removeField(prefixValue.getPrefName());
				String value = prefixValue.getValue();//LatCyrUtils.toLatin(pref.getValue()
				// );
				Field f = null;
				if (notAnalyzedNotStored.contains(prefixValue.getPrefName())) {
					f = new Field(prefixValue.getPrefName(), value,
							Field.Store.NO, Field.Index.NOT_ANALYZED,
							Field.TermVector.NO);
				} else if (notAnalyzed.contains(prefixValue.getPrefName())) {
					f = new Field(prefixValue.getPrefName(), value,
							Field.Store.YES, Field.Index.NOT_ANALYZED,
							Field.TermVector.NO);
				}else if (notIndexed.contains(prefixValue.getPrefName())) {
					f = new Field(prefixValue.getPrefName(), value, Field.Store.YES,
							Field.Index.NO, Field.TermVector.NO);
				} else {
					value = StringUtils.clearDelimiters(value, delims);
					
					// da bi
					// izbacio
					// sve
					// znakove
					// interpukcije
					// osim za
					// UDK,ISBN,
					// ISSN
					f = new Field(prefixValue.getPrefName(), value, Field.Store.NO,
							Field.Index.ANALYZED,
							Field.TermVector.NO);
				}
				doc.add(f);
			}
			try{
				numberOfChangesFromLastOptimize++;
				synchronized(this){
					IndexWriter iw = getIndexWriter();
					iw.updateDocument(new Term("CN",controlNumber), doc);
					iw.commit();
					retVal = true;
					if(numberOfChangesFromLastOptimize % 1000 == 0){
						numberOfChangesFromLastOptimize = 0;
						iw.optimize();
					}
				}
			} catch (IOException ex) {
				log.fatal(ex);
			}
		}
		return retVal;
	}

	/**
	 * Deletes a mARC21Record from the index
	 * 
	 * @param rec
	 *            MARC21Record to delete
	 * @return true if successful
	 */
	public  boolean delete(Record rec) {
		return delete(rec.getMARC21Record().getControlNumber());
	}

	/**
	 * Deletes a mARC21Record from the index
	 * 
	 * @param controlNumber
	 *            MARC21Record control number
	 * @return true if successful
	 */
	public boolean delete(String controlNumber) {
		try {
			synchronized(this){
				if(writer!=null){
					writer.close();
					writer = null;
				}
				IndexReader indexReader = IndexReader.open(indexPath);
				Term term = new Term("CN", controlNumber);
				indexReader.deleteDocuments(term);
				indexReader.close();
			}
			return true;
		} catch (IOException ex) {
			log.fatal(ex);
			return false;
		}
	}

	/**
	 * Optimizes the Lucene index.
	 * 
	 * @return true if successful
	 */
	public boolean optimize() {
		try {
			synchronized(this){
				IndexWriter indexWriter = getIndexWriter();
				indexWriter.optimize();
				indexWriter.commit();
			}
			return true;
		} catch (IOException ex) {
			log.fatal(ex);
			return false;
		}
	}
	
	/**
	 * Optimizes and closes writer
	 */
	public void close() {
		try {
			if(writer != null) {
				writer.close();
			}
		} catch (Exception ex) {
			log.fatal(ex);
		}
	}

	/**
	 * Constructs a Lucene document containing prefixes from the given mARC21Record.
	 * 
	 * @param rec
	 *            Source mARC21Record
	 * @return A new Lucene document
	 */
	protected Document getDocument(Record rec) {
		Document doc = new Document();
//		if(rec.getConverter() instanceof AuthorConverter){
//			if(rec.getDto() == null)
//				rec.loadDTOFromMARC21();
//			Field registeredField = new Field("REGISTERED", new Boolean(((AuthorDTO)rec.getDto()).isAlreadyRegistered()).toString(), Field.Store.YES,
//					Field.Index.NOT_ANALYZED, Field.TermVector.NO);
//			doc.add(registeredField);
//		}
//		System.out.println("Indexer metoda getDocument 0");
		Iterator<PrefixValue> prefixes = PrefixConverter.toPrefixes(rec)
				.iterator();
//		System.out.println("Indexer metoda getDocument 1");
		
		while (prefixes.hasNext()) {
			PrefixValue pref = prefixes.next();
//			System.out.print("Indexer metoda getDocument 2 " + pref.getPrefName());
//			System.out.println(" vrednost "+ pref.getValue());
			String value = pref.getValue();//LatCyrUtils.toLatin(pref.getValue()
			// );
			Field f = null;
			if (notAnalyzedNotStored.contains(pref.getPrefName())) {
				f = new Field(pref.getPrefName(), value,
						Field.Store.NO, Field.Index.NOT_ANALYZED,
						Field.TermVector.NO);
			} else if (notAnalyzed.contains(pref.getPrefName())) {
				f = new Field(pref.getPrefName(), value,
						Field.Store.YES, Field.Index.NOT_ANALYZED,
						Field.TermVector.NO);
			}else if (notIndexed.contains(pref.getPrefName())) {
				f = new Field(pref.getPrefName(), value, Field.Store.YES,
						Field.Index.NO, Field.TermVector.NO);
			} else {
				value = StringUtils.clearDelimiters(value, delims);
				
				// da bi
				// izbacio
				// sve
				// znakove
				// interpukcije
				// osim za
				// UDK,ISBN,
				// ISSN
				f = new Field(pref.getPrefName(), value, Field.Store.NO,
						Field.Index.ANALYZED,
						Field.TermVector.NO);
			}
//			System.out.print("Indexer metoda getDocument 3 " + pref.getPrefName());
			doc.add(f);
		}
		log.info("getDocument: " + doc);
		return doc;
	}

	/**
	 * Returns a new Lucene index writer. Creates the index if necessary.
	 * 
	 * @return a new Lucene index writer
	 */
	protected IndexWriter getIndexWriter() {
		if (writer == null){
			try {
				boolean createIndex = true;
				File testIndexPath = new File(indexPath);
				if (!testIndexPath.exists())
					testIndexPath.mkdirs();
				if (testIndexPath.isDirectory()) {
					if (testIndexPath.list().length > 0)
						createIndex = false;
					writer = new IndexWriter(indexPath, analyzer, createIndex,
							IndexWriter.MaxFieldLength.UNLIMITED);
					writer.setSimilarity(new DefaultSimilarity());
				}
			} catch (Exception ex) {
				log.fatal(ex);
			}
		}
		return writer;
	}

	protected String indexPath;
	protected IndexWriter writer = null;
	protected Analyzer analyzer;
	private static List<String> notAnalyzedNotStored = new ArrayList<String>();
	static {
		notAnalyzedNotStored.add("AUCN");
		notAnalyzedNotStored.add("ADCN");
		notAnalyzedNotStored.add("CMCN");
		notAnalyzedNotStored.add("CCCN");
		notAnalyzedNotStored.add("CACN");
		notAnalyzedNotStored.add("PRCN");
		notAnalyzedNotStored.add("COCN");
		notAnalyzedNotStored.add("EDCN");
		notAnalyzedNotStored.add("FACN");
		notAnalyzedNotStored.add("JOCN");
		notAnalyzedNotStored.add("MOCN");
		notAnalyzedNotStored.add("INCN");
		notAnalyzedNotStored.add("OUCN");
		notAnalyzedNotStored.add("JACN");
		notAnalyzedNotStored.add("APCN");
		notAnalyzedNotStored.add("CLS");
		notAnalyzedNotStored.add("SD");
		notAnalyzedNotStored.add("ED");
		notAnalyzedNotStored.add("RBCN");
		notAnalyzedNotStored.add("RBICN");
		notAnalyzedNotStored.add("SVLCN");
	}
	
	private static List<String> notAnalyzed = new ArrayList<String>();
	static {
		notAnalyzed.add("TYPE");
		notAnalyzed.add("SUBTYPE");
		notAnalyzed.add("CN");
		notAnalyzed.add("ISSN");
		notAnalyzed.add("APVNT");
		notAnalyzed.add("SCOPUSID");
		notAnalyzed.add("ORCID");
		notAnalyzed.add("REGISTERED");
		notAnalyzed.add("UNSDISSERTATION");
		notAnalyzed.add("PADISSERTATION");
		notAnalyzed.add("AUTHORUNSDISSERTATIONS");
		notAnalyzed.add("ADVISORUNSDISSERTATIONS");
		notAnalyzed.add("COMMISSIONMEMBERUNSDISSERTATIONS");
		notAnalyzed.add("COMMISSIONCHAIRUNSDISSERTATIONS");
		notAnalyzed.add("AUTHORPADISSERTATIONS");
		notAnalyzed.add("ADVISORPADISSERTATIONS");
		notAnalyzed.add("COMMISSIONMEMBERPADISSERTATIONS");
		notAnalyzed.add("COMMISSIONCHAIRPADISSERTATIONS");
		notAnalyzed.add("ARCHIVED");
		notAnalyzed.add("DEFENDED");
	}

	private static List<String> notIndexed = new ArrayList<String>();
	static {
		notIndexed.add("JMBG");
		notIndexed.add("DP");
		notIndexed.add("LP");
		notIndexed.add("NAMES");
		notIndexed.add("YEAR");
		notIndexed.add("INSTITUTIONNAME");
		notIndexed.add("TITLE");
		notIndexed.add("CURRENTPOSITIONNAME");
		notIndexed.add("SOMENAME");
		notIndexed.add("SUPERINSTITUTIONCN");
		notIndexed.add("SUPERORGANIZATIONUNITCN");
		notIndexed.add("PLACE");
		notIndexed.add("STUDYTYPE");
		notIndexed.add("PUBLICPERIOD");
		notIndexed.add("DATEMODIFIED");
		notIndexed.add("PLACE");
		notIndexed.add("NUMBER");
		notIndexed.add("ISBN");
		notIndexed.add("AUTHORNAME");
		notIndexed.add("SOMETITLE");
		notIndexed.add("SOMERESEARCHAREA");
		notIndexed.add("PAPERTYPE");
		notIndexed.add("CONFERENCENAME");
		notIndexed.add("RECORDHTMLREPRESENTATION");
		notIndexed.add("REGISTERENTRYHTMLREPRESENTATION");
		notIndexed.add("REGISTERENTRYHTMLREPRESENTATIONEN");
		notIndexed.add("RECORDHARVARDREPRESENTATION");
		notIndexed.add("RECORDHARVARDREPRESENTATIONEN");
		notIndexed.add("RECORDSTRINGREPRESENTATION");
		notIndexed.add("RECORDMARC21REPRESENTATION");
		notIndexed.add("RECORDDUBLINCOREREPRESENTATION");
		notIndexed.add("RECORDETDMSREPRESENTATION");
		notIndexed.add("RELATEDPUBLICATIONS");
		notIndexed.add("WCIURL");		
		notIndexed.add("FILEURL");
		notIndexed.add("FILENAME");
		notIndexed.add("FILEID");
		notIndexed.add("FILELICENSE");
		notIndexed.add("SUPPLEMENTURL");
		notIndexed.add("PRELIMINARYTHESESURL");
		notIndexed.add("PRELIMINARYSUPPLEMENTURL");
		notIndexed.add("REPORTURL");
		notIndexed.add("URI");
	}
	public static String delims = ",;:\"()[]{}+/.!-";
	private static Log log = LogFactory.getLog(Indexer.class.getName());
}
