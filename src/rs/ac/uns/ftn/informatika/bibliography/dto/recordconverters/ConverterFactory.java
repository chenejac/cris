package rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters;

import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ConferenceDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JobAdDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperMonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PatentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProductDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookImplementationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.SpecVerLstDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.eval.RuleBookConverter;
import rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.eval.RuleBookImplementationConverter;
import rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.eval.SpecVerLstConverter;

public class ConverterFactory {

	private static ConverterFactory converterFactory = new ConverterFactory();

	protected ConverterFactory() {

	}

	public static ConverterFactory getInstance() {
		return converterFactory;
	}

	public static AbstractRecordConverter getConverter(RecordDTO dto) {
		if (dto instanceof OrganizationUnitDTO)
			return new OrganizationUnitConverter();
		if (dto instanceof InstitutionDTO)
			return new InstitutionConverter();
		if (dto instanceof AuthorDTO)
			return new AuthorConverter();
		if (dto instanceof ConferenceDTO)
			return new ConferenceConverter();
		if (dto instanceof ProceedingsDTO)
			return new ProceedingsConverter();
		if (dto instanceof PaperProceedingsDTO)
			return new PaperProceedingsConverter();
		if (dto instanceof JournalDTO)
			return new JournalConverter();
		if (dto instanceof PaperJournalDTO)
			return new PaperJournalConverter();
		if (dto instanceof MonographDTO)
			return new MonographConverter();
		if (dto instanceof PaperMonographDTO)
			return new PaperMonographConverter();
		if (dto instanceof StudyFinalDocumentDTO)
			return new StudyFinalDocumentConverter();
		if (dto instanceof PatentDTO)
			return new PatentConverter();
		if (dto instanceof ProductDTO)
			return new ProductConverter();
		if (dto instanceof JobAdDTO)
			return new JobAdConverter();
		if (dto instanceof RuleBookImplementationDTO)
			return new RuleBookImplementationConverter();
		if (dto instanceof RuleBookDTO)
			return new RuleBookConverter();
		if (dto instanceof SpecVerLstDTO)
			return new SpecVerLstConverter();
		return null;
	}
	
	public static String getConverterType(RecordDTO dto) {
		if (dto instanceof OrganizationUnitDTO)
			return Types.ORGANIZATION_UNIT;
		if (dto instanceof InstitutionDTO)
			return Types.INSTITUTION;
		if (dto instanceof AuthorDTO)
			return Types.AUTHOR;
		if (dto instanceof ConferenceDTO)
			return Types.CONFERENCE;
		if (dto instanceof ProceedingsDTO)
			return Types.PROCEEDINGS;
		if (dto instanceof PaperProceedingsDTO)
			return Types.PAPER_PROCEEDINGS;
		if (dto instanceof JournalDTO)
			return Types.JOURNAL;
		if (dto instanceof PaperJournalDTO)
			return Types.PAPER_JOURNAL;
		if (dto instanceof MonographDTO)
			return Types.MONOGRAPH;
		if (dto instanceof PaperMonographDTO)
			return Types.PAPER_MONOGRAPH;
		if (dto instanceof StudyFinalDocumentDTO)
			return Types.STUDY_FINAL_DOCUMENT;
		if (dto instanceof PatentDTO)
			return Types.PATENT;
		if (dto instanceof ProductDTO)
			return Types.PRODUCT;
		if (dto instanceof JobAdDTO)
			return Types.JOB_AD;
		if (dto instanceof RuleBookImplementationDTO)
			return Types.RULEBOOK_IMPLEMENTATION;
		if (dto instanceof RuleBookDTO)
			return Types.RULEBOOK;
		if (dto instanceof SpecVerLstDTO)
			return Types.SPECIALLY_VERIFIED_LIST;
		return null;
	}
	
	public static AbstractRecordConverter getConverter(String type) {
		if (Types.ORGANIZATION_UNIT.equals(type))
			return new OrganizationUnitConverter();
		if (Types.INSTITUTION.equals(type))
			return new InstitutionConverter();
		if (Types.AUTHOR.equals(type))
			return new AuthorConverter();
		if (Types.CONFERENCE.equals(type))
			return new ConferenceConverter();
		if (Types.PROCEEDINGS.equals(type))
			return new ProceedingsConverter();
		if ((Types.PAPER_PROCEEDINGS.equals(type)) ||
				(Types.FULL_PAPER_PROCEEDINGS.equals(type)) ||
				(Types.ABSTRACT_PAPER_PROCEEDINGS.equals(type)) ||
				(Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS.equals(type)) || 
				(Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS.equals(type)) ||
				(Types.DISCUSSION_PAPER_PROCEEDINGS.equals(type))){
			return new PaperProceedingsConverter();
		}
		if (Types.JOURNAL.equals(type))
			return new JournalConverter();
		if ((Types.PAPER_JOURNAL.equals(type)) ||
				(Types.SCIENTIFIC_CRITICISM_JOURNAL.equals(type)) ||
				(Types.OTHER_JOURNAL.equals(type))){
			return new PaperJournalConverter();
		}
		if (Types.MONOGRAPH.equals(type))
			return new MonographConverter();
		if (Types.PAPER_MONOGRAPH.equals(type))
			return new PaperMonographConverter();
		if ((Types.STUDY_FINAL_DOCUMENT.equals(type)) ||
				(Types.PHD_STUDY_FINAL_DOCUMENT.equals(type)) ||
				(Types.PHD_ART_PROJECT.equals(type)) ||
				(Types.OLD_MASTER_STUDY_FINAL_DOCUMENT.equals(type)) ||
				(Types.MASTER_STUDY_FINAL_DOCUMENT.equals(type)) || 
				(Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT.equals(type)) ||
				(Types.BACHELOR_STUDY_FINAL_DOCUMENT.equals(type)) ||
				(Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT.equals(type))){
			return new StudyFinalDocumentConverter();
		}
		if (Types.PATENT.equals(type))
			return new PatentConverter();
		if (Types.PRODUCT.equals(type))
			return new ProductConverter();
		if (Types.JOB_AD.equals(type))
			return new JobAdConverter();
		if (Types.RULEBOOK_IMPLEMENTATION.equals(type))
			return new RuleBookImplementationConverter();
		if (Types.RULEBOOK.equals(type))
			return new RuleBookConverter();
		if (Types.SPECIALLY_VERIFIED_LIST.equals(type))
			return new SpecVerLstConverter();
		return null;
	}
	
	public static RecordDTO getDTO(String type) {
		if (Types.ORGANIZATION_UNIT.equals(type))
			return new OrganizationUnitDTO();
		if (Types.INSTITUTION.equals(type))
			return new InstitutionDTO();
		if (Types.AUTHOR.equals(type))
			return new AuthorDTO();
		if (Types.CONFERENCE.equals(type))
			return new ConferenceDTO();
		if (Types.PROCEEDINGS.equals(type))
			return new ProceedingsDTO();
		if ((Types.PAPER_PROCEEDINGS.equals(type)) ||
				(Types.FULL_PAPER_PROCEEDINGS.equals(type)) ||
				(Types.ABSTRACT_PAPER_PROCEEDINGS.equals(type)) ||
				(Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS.equals(type)) || 
				(Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS.equals(type)) ||
				(Types.DISCUSSION_PAPER_PROCEEDINGS.equals(type))){
			return new PaperProceedingsDTO();
		}
		if (Types.JOURNAL.equals(type))
			return new JournalDTO();
		if ((Types.PAPER_JOURNAL.equals(type)) ||
				(Types.SCIENTIFIC_CRITICISM_JOURNAL.equals(type)) ||
				(Types.OTHER_JOURNAL.equals(type))){
			return new PaperJournalDTO();
		}
		if (Types.MONOGRAPH.equals(type))
			return new MonographDTO();
		if (Types.PAPER_MONOGRAPH.equals(type))
			return new PaperMonographDTO();
		if ((Types.STUDY_FINAL_DOCUMENT.equals(type)) ||
				(Types.PHD_STUDY_FINAL_DOCUMENT.equals(type)) ||
				(Types.PHD_ART_PROJECT.equals(type)) ||
				(Types.OLD_MASTER_STUDY_FINAL_DOCUMENT.equals(type)) ||
				(Types.MASTER_STUDY_FINAL_DOCUMENT.equals(type)) || 
				(Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT.equals(type)) ||
				(Types.BACHELOR_STUDY_FINAL_DOCUMENT.equals(type)) ||
				(Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT.equals(type))){
			return new StudyFinalDocumentDTO();
		}
		if (Types.PATENT.equals(type))
			return new PatentDTO();
		if (Types.PRODUCT.equals(type))
			return new ProductDTO();
		if (Types.JOB_AD.equals(type))
			return new JobAdDTO();
		if (Types.RULEBOOK_IMPLEMENTATION.equals(type))
			return new RuleBookImplementationDTO();
		if (Types.RULEBOOK.equals(type))
			return new RuleBookDTO();
		if (Types.SPECIALLY_VERIFIED_LIST.equals(type))
			return new SpecVerLstDTO();
		return null;
	}
}
