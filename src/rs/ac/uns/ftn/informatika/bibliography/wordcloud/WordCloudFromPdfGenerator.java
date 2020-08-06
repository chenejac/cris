package rs.ac.uns.ftn.informatika.bibliography.wordcloud;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

import rs.ac.uns.ftn.informatika.bibliography.filesrv.PdfDocTextExtractor;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Indexer;

import com.gint.util.string.StringUtils;
import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.palette.ColorPalette;

/**
 * @author Georgia Kapitsaki
 *
 * Apr 12, 2017
 */

public class WordCloudFromPdfGenerator {

	private static final int DEFAULT_HEIGHT = 200;
	private static final int DEFAULT_WIDTH = 800;
	private static final int DEFAULT_MIN_WORD_LENGTH = 4;
	private static final int DEFAULT_WORDS_RETURNED = 50;
	private static final boolean DEFAULT_INCLUDE_NUMBERS = false;
	private static final Color[] DEFAULT_CLOUD_COLORS = new Color[] { 
			new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF) };
	private static final Color DEFAULT_BACKGROUND_COLORS = Color.WHITE;
	private static final String DEFAULT_IMG_TYPE = "png";
	
//	private static final String ANALYZER_JAR_PATH = "Analyzer.jar";
	
//	public static String outputFilename = "E:/output.txt";
	
	private int heightDimension = DEFAULT_HEIGHT;
	private int widthDimension = DEFAULT_WIDTH;
	private int minWordLength = DEFAULT_MIN_WORD_LENGTH; // words of this length or lower are ignored
	private int wordsToReturn = DEFAULT_WORDS_RETURNED; // number of words to be returned in the cloud
	private boolean ignoreNumbers = DEFAULT_INCLUDE_NUMBERS; // indicates if numbers in text are ignored 
	private Color[] cloudColors = DEFAULT_CLOUD_COLORS;
	private Color backgroundColor = DEFAULT_BACKGROUND_COLORS;
	private String imgType = DEFAULT_IMG_TYPE;
	
	public void generateWordCLoud(byte[] inputPdf, OutputStream outputImgStream) {
		// extract text from PDF (works also for Cyrillic alphabet)
		String text=null;
		try {
			text = PdfDocTextExtractor.extractText(inputPdf);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			log.error(e1);
		}//extractTextFromPdf(inputPdf);
		List<String> cleaned = new ArrayList<String>();
		try {
			// remove stop words and perform stemming
			cleaned = stem(text);
		} catch (Exception e) {
			log.error(e);
		}

		final FrequencyAnalyzerWithOriginal frequencyAnalyzer = new FrequencyAnalyzerWithOriginal();
		frequencyAnalyzer.setWordFrequenciesToReturn(wordsToReturn);
		frequencyAnalyzer.setMinWordLength(minWordLength);
		frequencyAnalyzer.setIgnoreNumbers(ignoreNumbers);
		List<WordFrequency> wordFrequencies;
		
		wordFrequencies = frequencyAnalyzer.load(cleaned);
		// retrieve the original word with maximum frequency for the stem
		for (WordFrequency wordfreq: wordFrequencies) 
			wordfreq.setWord(frequencyAnalyzer.getFirstOriginalFromStem(wordfreq.getWord()));
		
		final Dimension dimension = new Dimension(widthDimension, heightDimension);
		final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
		wordCloud.setPadding(2); 
		wordCloud.setBackgroundColor(backgroundColor);
		wordCloud.setColorPalette(new ColorPalette(cloudColors));
		wordCloud.setFontScalar(new LinearFontScalar(10, 40));
		wordCloud.build(wordFrequencies);
		wordCloud.writeToStream(imgType, outputImgStream);
	}
	
	/*private String extractTextFromPdf(byte[] file) {
		PDFTextStripper pdfStripper = null;
		String parsedText = null;
		PDDocument document = null;
		try {
			document = PDDocument.load(file);
			pdfStripper = new PDFTextStripper();
			parsedText = pdfStripper.getText(document);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(document!=null)
				try {
					document.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return parsedText;
	}*/

//	private String stemText(String input) {
//		File tempOutput = null;
//		try {
//			File tempInput = File.createTempFile("input", ".txt");
//			FileUtil.write(tempInput, input);
//			tempOutput = File.createTempFile("output", ".txt");
//
//			ProcessBuilder pb = new ProcessBuilder("java", "-jar", ANALYZER_JAR_PATH,
//					tempInput.getAbsolutePath(), tempOutput.getAbsolutePath());
//			final Process process = pb.start();
//			process.waitFor();
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		if (tempOutput != null)
//			try {
//				return FileUtil.read(new File(tempOutput.getAbsolutePath()));
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		return null;
//	}
	
	public static List<String> stem(String value){
		value = StringUtils.clearDelimiters(value, Indexer.delims);
		final TokenStream tokenStream = new CrisAnalyzer().tokenStream( "FT" , new StringReader( value ) );
		final List<String> result = new ArrayList< String >();
		Token tok;
		try {
			tok = tokenStream.next();
			while ( tok != null ) {
				result.add(tok.term());
				tok = tokenStream.next();
			}
		} catch (IOException e) {
			log.error(e);
		}
		return result;
	}
	
	public int getHeightDimension() {
		return heightDimension;
	}
	
	public void setHeightDimension(int heightDimension) {
		this.heightDimension = heightDimension;
	}
	
	public int getWidthDimension() {
		return widthDimension;
	}
	
	public void setWidthtDimension(int widthDimension) {
		this.widthDimension = widthDimension;
	}
	
	public int getwWordsToReturn() {
		return wordsToReturn;
	}
	
	public void setWordsToReturn(int wordsToReturn) {
		this.wordsToReturn = wordsToReturn;
	}

	public int getMinWordLength() {
		return minWordLength;
	}
	
	public void setMinWordLength(int minWordLength) {
		this.minWordLength = minWordLength;
	}
	
	public boolean getIgnoreNumbers() {
		return ignoreNumbers;
	}
	
	public void setIgnoreNumbers(boolean ignoreNumbers) {
		this.ignoreNumbers = ignoreNumbers;
	}
	
	public String getImgType() {
		return imgType;
	}
	
	public void setImgType(String imgType) {
		this.imgType = imgType;
	}
	
	public Color[] getCloudColors() {
		return cloudColors;
	}
	
	public void setImgType(Color[] cloudColors) {
		this.cloudColors = cloudColors;
	}
	
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public WordCloudFromPdfGenerator() {
	}
	
	public WordCloudFromPdfGenerator(int heightDimension, int widthDimension) {
		this.heightDimension = heightDimension;
		this.widthDimension = widthDimension;
	}
	
	public WordCloudFromPdfGenerator(int heightDimension, int widthDimension, int wordsToReturn) {
		this(heightDimension, widthDimension);
		this.wordsToReturn = wordsToReturn;
	}
	
	public WordCloudFromPdfGenerator(int heightDimension, int widthDimension, int wordsToReturn, int minWordLength) {
		this(heightDimension, widthDimension, wordsToReturn);
		this.minWordLength = minWordLength;
	}
	
	public WordCloudFromPdfGenerator(int heightDimension, int widthDimension, int wordsToReturn, int minWordLength, boolean ignoreNumbers) {
		this(heightDimension, widthDimension, wordsToReturn, minWordLength);
		this.ignoreNumbers = ignoreNumbers;
	}
	
	/*public static void main(String[] args) {		
		WordCloudFromPdfGenerator generator = new WordCloudFromPdfGenerator();
		if (args.length > 0 && args[0] != null && args[1] != null)
			try {
				generator.generateWordCLoud(args[0], new FileOutputStream(args[1]));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	}*/
	
	private static Log log = LogFactory.getLog(WordCloudFromPdfGenerator.class.getName());
}