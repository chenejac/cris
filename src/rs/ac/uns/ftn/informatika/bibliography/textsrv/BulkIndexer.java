package rs.ac.uns.ftn.informatika.bibliography.textsrv;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;

import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;

/**
 * Implements a bulk mARC21Record indexer.
 * 
 * @author mbranko@uns.ac.rs
 */
public class BulkIndexer extends Indexer {

	/**
	 * @param indexPath
	 *            path to the folder with Lucene index files
	 * @param analyzer
	 *            analyzer for Lucene
	 */
	public BulkIndexer(String indexPath, Analyzer analyzer) {
		super(indexPath, analyzer);
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.textsrv.Indexer#add(rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	public synchronized boolean add(Record rec) {
		try {
			getIndexWriter().addDocument(getDocument(rec));
		} catch (IOException ex) {
			log.fatal(ex);
			return false;
		}
		return true;
	}

	/**
	 * Optimizes and closes writer
	 */
	public void close() {
		try {
			if(writer != null) {
				writer.optimize();
				writer.commit();
				writer.close();
			}
		} catch (Exception ex) {
			log.fatal(ex);
		}
	}

	/**
	 * 
	 * Returns a new Lucene index writer if it is used 1000 times else returns
	 * an existing Lucene index writer. Creates the index if necessary.
	 * 
	 * @return a new Lucene index writer
	 * @see rs.ac.uns.ftn.informatika.bibliography.textsrv.Indexer#getIndexWriter()
	 */
	@Override
	protected IndexWriter getIndexWriter() {
		if (++useCount % 1000 == 0) {
			close();
			writer = null;
		}
		if (writer == null) {
			try {
				boolean createIndex = true;
				File testIndexPath = new File(indexPath);
				if (!testIndexPath.exists())
					testIndexPath.mkdirs();
				if (testIndexPath.isDirectory()) {
					if (testIndexPath.list().length > 0)
						createIndex = false;
					writer = new IndexWriter(indexPath,
							analyzer, createIndex,
							IndexWriter.MaxFieldLength.UNLIMITED);
				}
			} catch (Exception ex) {
				log.fatal(ex);
			}
		}
		return writer;
	}

	private int useCount = 0;
	private static Log log = LogFactory.getLog(BulkIndexer.class.getName());
}
