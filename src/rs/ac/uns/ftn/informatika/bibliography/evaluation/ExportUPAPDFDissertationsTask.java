package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import rs.ac.uns.ftn.informatika.bibliography.backup.Task;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.RegisterEntryDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.reports.maticnaKnjiga.MaticnaKnjigaItemBean;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;
import rs.ac.uns.ftn.informatika.bibliography.utils.Sanitizer;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ExportUPAPDFDissertationsTask implements Task {

	public ExportUPAPDFDissertationsTask(File exportFolder, ChannelSftp channelSftp) throws Exception {
		this.exportFolder = exportFolder;
		this.channelSftp = channelSftp;
		this.outMetadata = new PrintWriter(new File(exportFolder.getAbsolutePath() + File.separator + "bibliotečkiPodaci.csv"), "UTF8");
		this.outRegistryEntry = new PrintWriter(new File(exportFolder.getAbsolutePath() + File.separator + "maticnaKnjiga.csv"), "UTF8");
		outMetadata.println("Redni broj, Identifikator, Autor, Naslov, Fakultet, Godina, Jezik, Pismo, Apstrakt, Prošireni apstrakt, Fizički opis (broj strana/sekcija/refernci/tabela/slika/grafikona/dodataka)," +
				"Datum objavljivanja, Datum prihvatanja, Datum odbrane, Mentor(i), Članovi komisije");
		outRegistryEntry.println("Редни број, Школска година, Редни број у школској години, Име и презиме, Датум - место - општина рођења и држава, Презиме и име родитеља, Назив завршене високошколске установе, " +
				"Претходне студије, Институција где је одбрањена дисертација, Наслов дисертације, Комисија и Ментор, Датум одбране, Научни назив, Број дипломе, Датум промоције");
	}

	@Override
	public boolean execute() {
		try {
			List<StudyFinalDocumentDTO> dissertations = collectAllDissertations();
			channelSftp.connect();
			copyDissertations(dissertations);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex);
			return false;
		} catch (Throwable e) {
			e.printStackTrace();
			log.error(e);
			return false;
		}
	}

	public List<StudyFinalDocumentDTO> collectAllDissertations() {
		String whereClause = "ARCHIVED != 100 AND TYPE like 'resPubl' AND (RECORDSTRING like '%710_2#__0(BISIS)9489%' OR RECORDSTRING like '%710_2#__0(BISIS)107024%')";
		return getThesesByWhereClause(whereClause);
	}

	public List<StudyFinalDocumentDTO> getThesesByWhereClause(String whereClause){
		List<StudyFinalDocumentDTO> retVal = new ArrayList<StudyFinalDocumentDTO>();
		List<Record> records = recordDAO.getRecordsIdsFromDatabaseByWhereClause(whereClause);
		if(records != null) {
			for (Record record : records) {
				retVal.add((StudyFinalDocumentDTO) record.getDto());
			}
		}
		List<String> orderByList = new ArrayList<String>();
		List<String> directionsList = new ArrayList<String>();
		orderByList.add("dateModified");
		directionsList.add("desc");
		orderByList.add("controlNumber");
		directionsList.add("desc");
		Collections.sort(retVal, new GenericComparator<StudyFinalDocumentDTO>(
				orderByList, directionsList));
		return retVal;
	}



	private void copyDissertations(List<StudyFinalDocumentDTO> dissertations) {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		int i = 1;
		for (StudyFinalDocumentDTO dissertation : dissertations) {
			try {
				dissertation.loadRegisterEntry(true);
				String publicationDateString = null;
				if (dissertation.getPublicationDate() != null)
					publicationDateString = formatter.format(dissertation.getPublicationDate().getTime());
				String acceptedOnString = null;
				if (dissertation.getAcceptedOn() != null)
					acceptedOnString = formatter.format(dissertation.getAcceptedOn().getTime());
				String defendedOnString = null;
				if (dissertation.getDefendedOn() != null)
					defendedOnString = formatter.format(dissertation.getDefendedOn().getTime());
				outMetadata.println(
					"" + (i++) + "," +
					Sanitizer.sanitizeCSV(dissertation.getControlNumber()) + "," +
					Sanitizer.sanitizeCSV(dissertation.getAuthor().getName().toString()) + "," +
					Sanitizer.sanitizeCSV(dissertation.getSomeTitle()) + "," +
					Sanitizer.sanitizeCSV(dissertation.getInstitution().getSomeName()) + "," +
					Sanitizer.sanitizeCSV(dissertation.getPublicationYear()) + "," +
					Sanitizer.sanitizeCSV(dissertation.getLanguage()) + "," +
					Sanitizer.sanitizeCSV(dissertation.getAlphabet()) + "," +
					Sanitizer.sanitizeCSV(dissertation.getAbstracT().getContent()) + "," +
					Sanitizer.sanitizeCSV(dissertation.getExtendedAbstract().getContent()) + "," +
					Sanitizer.sanitizeCSV(dissertation.getPhysicalDescription().toString()) + "," +
					((publicationDateString!=null)?(Sanitizer.sanitizeCSV(publicationDateString) + ","):",") +
					((acceptedOnString!=null)?(Sanitizer.sanitizeCSV(acceptedOnString) + ","):",") +
					((defendedOnString!=null)?(Sanitizer.sanitizeCSV(defendedOnString) + ","):",") +
					Sanitizer.sanitizeCSV(dissertation.getRegisterEntry().getAdvisors()) + "," +
					Sanitizer.sanitizeCSV(dissertation.getRegisterEntry().getCommissionMembers())
				);
	//			outRegistryEntry.println("Редни број, Школска година, Редни број у школској години, Име и презиме, Датум - место - општина рођења и држава, Презиме и име родитеља, Назив завршене високошколске установе, " +
	//					"Претходне студије, Институција где је одбрањена дисертација, Наслов дисертације, Комисија и Ментор, Датум одбране, Научни назив, Број дипломе, Датум промоције");
				/*MaticnaKnjigaItemBean registerEntry = new MaticnaKnjigaItemBean(dissertation.getRegisterEntry());
				outRegistryEntry.println(
					Sanitizer.sanitizeCSV(registerEntry.getRedniBroj()) + "," +
					Sanitizer.sanitizeCSV(registerEntry.getSkolskaGodina()) + "," +
					Sanitizer.sanitizeCSV(registerEntry.getRedniBrojUGodini()) + "," +
					Sanitizer.sanitizeCSV(registerEntry.getImeAutora()) + "," +
					Sanitizer.sanitizeCSV("ДАТУМ:\n" + registerEntry.getDatumRodjenja() + "\nMESTO:\n" + registerEntry.getMestoRodjenja() + "\nОпштина:\n" +
							registerEntry.getOpstina() + "\nДРЖАВА:\n" + registerEntry.getDrzava()) + "," +
					Sanitizer.sanitizeCSV(registerEntry.getRoditeljIliStaratelj()) + "," +
					Sanitizer.sanitizeCSV(registerEntry.getNazivZavrseneVisokoskolskeUstanove()) + ",,,,,,,,"
				);*/
				File directory = new File (exportFolder, dissertation.getAuthor().getName().toString());
				if (directory.exists()){
					directory = new File (exportFolder, dissertation.getAuthor().getName().toString() + " " + new GregorianCalendar().getTimeInMillis());
				}
				if(directory.mkdir()){
					if (dissertation.getFile() != null){
						File newFile = new File (directory, "disertacija.pdf");
						if(newFile.createNewFile()){
							copyFileUsingSFTPChannels(FileStorage.getFullPath(dissertation.getFile()), newFile);
//							copyFileUsingFileChannels(new File(FileStorage.getFullPath(dissertation.getFile())), newFile);
						} else {
							System.out.println("Dissertation Final pdf creation problem: " + dissertation.getRecord().toString());
						}
					} else if (dissertation.getPreliminaryTheses() != null){
						File newFile = new File (directory, "disertacija.pdf");
						if(newFile.createNewFile()){
							copyFileUsingSFTPChannels(FileStorage.getFullPath(dissertation.getPreliminaryTheses()), newFile);
//							copyFileUsingFileChannels(new File(FileStorage.getFullPath(dissertation.getPreliminaryTheses())), newFile);
						} else {
							System.out.println("Dissertation Public Review pdf creation problem: " + dissertation.getRecord().toString());
						}
					}
					if (dissertation.getSupplement() != null){
						File newFile = new File (directory, "dodatak." + dissertation.getSupplement().getFileName().substring(dissertation.getSupplement().getFileName().lastIndexOf('.') + 1));
						if(newFile.createNewFile()){
							copyFileUsingSFTPChannels(FileStorage.getFullPath(dissertation.getSupplement()), newFile);
//							copyFileUsingFileChannels(new File(FileStorage.getFullPath(dissertation.getSupplement())), newFile);
						} else {
							System.out.println("Supplement Final pdf creation problem: " + dissertation.getRecord().toString());
						}
					} else if (dissertation.getPreliminarySupplement() != null){
						File newFile = new File (directory, "dodatak." + dissertation.getPreliminarySupplement().getFileName().substring(dissertation.getPreliminarySupplement().getFileName().lastIndexOf('.') + 1));
						if(newFile.createNewFile()){
							copyFileUsingSFTPChannels(FileStorage.getFullPath(dissertation.getPreliminarySupplement()), newFile);
//							copyFileUsingFileChannels(new File(FileStorage.getFullPath(dissertation.getPreliminarySupplement())), newFile);
						} else {
							System.out.println("Supplement Public Review pdf creation problem: " + dissertation.getRecord().toString());
						}
					}
					if (dissertation.getReport() != null){
						File newFile = new File (directory, "izvestaj.pdf");
						if(newFile.createNewFile()){
							copyFileUsingSFTPChannels(FileStorage.getFullPath(dissertation.getReport()), newFile);
//							copyFileUsingFileChannels(new File(FileStorage.getFullPath(dissertation.getReport())), newFile);
						} else {
							System.out.println("Report pdf creation problem: " + dissertation.getRecord().toString());
						}
					}
					if (dissertation.getFileCopyright() != null){
						File newFile = new File (directory, "licenca.pdf");
						if(newFile.createNewFile()){
							copyFileUsingSFTPChannels(FileStorage.getFullPath(dissertation.getFileCopyright()), newFile);
//							copyFileUsingFileChannels(new File(FileStorage.getFullPath(dissertation.getFileCopyright())), newFile);
						} else {
							System.out.println("License pdf creation problem: " + dissertation.getRecord().toString());
						}
					}
					if (dissertation.getMetadataCopyright() != null){
						File newFile = new File (directory, "izjavaIstovetnost.pdf");
						if(newFile.createNewFile()){
							copyFileUsingSFTPChannels(FileStorage.getFullPath(dissertation.getMetadataCopyright()), newFile);
//							copyFileUsingFileChannels(new File(FileStorage.getFullPath(dissertation.getMetadataCopyright())), newFile);
						} else {
							System.out.println("Metadata copyright pdf creation problem: " + dissertation.getRecord().toString());
						}
					}
				} else {
					System.out.println("Make dir problem: " + dissertation.getRecord().toString());
				}
			} catch (IOException e) {
				System.out.println("Some file pdf creation problem: " + dissertation.getRecord().toString());
				e.printStackTrace();
			}	finally {
				dissertation.clear();
			}
		}
		outMetadata.flush();
		outRegistryEntry.flush();
		outMetadata.close();
		outRegistryEntry.close();
	}

	private void copyFileUsingSFTPChannels(String source, File dest)
			throws IOException {
		try {
			System.out.println(source);
			channelSftp.get(source, dest.getAbsolutePath());
		} catch (SftpException e){
			e.printStackTrace();
		}
	}

	private static void copyFileUsingFileChannels(File source, File dest)
	        throws IOException {
	    FileChannel inputChannel = null;
	    FileChannel outputChannel = null;
	    try {
	        inputChannel = new FileInputStream(source).getChannel();
	        outputChannel = new FileOutputStream(dest).getChannel();
	        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
	    } finally {
	    	System.out.println(source.getAbsolutePath());
	        if(inputChannel == null)
	        	System.out.println("NEMA");
	        else {
	        	inputChannel.close();

	        	outputChannel.close();
	        }
	    }
	}


	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	private File exportFolder;

	private ChannelSftp channelSftp;

	private PrintWriter outMetadata;

	private PrintWriter outRegistryEntry;

	private static Log log = LogFactory.getLog(ExportUPAPDFDissertationsTask.class.getName());

}
