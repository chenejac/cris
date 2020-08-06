package rs.ac.uns.ftn.informatika.bibliography.reports.freemarker;

import java.io.File;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperMonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;

public class TemplateRunner {
	
	
	
	public static final String HARVARD = "harvard";
	public static final String CHICAGO = "chicago";
	public static final String PMFSAMOVREDNOVANJE = "pmf";
	
	private static Configuration cfg;
	private static String templates_dir = "templates/";
	private static String sep = "<BR>";
	
	
	static{
		try{
		 cfg = new Configuration();
		 cfg.setClassForTemplateLoading(TemplateRunner.class,templates_dir);	
		}catch(Exception e){
			
		}
		
		
	}
	
	public static String getRepresentation(RecordDTO record, String templateType){
		if(record==null) return "";		
		try {
			Template temp;
			StringWriter writer = new StringWriter();		
			if(record instanceof PaperJournalDTO){
				temp = cfg.getTemplate(templateType+"_paperJournal.ftl");
				temp.process((PaperJournalDTO)record, writer);				
			}
			if(record instanceof MonographDTO){
				temp = cfg.getTemplate(templateType+"_monograph.ftl");
				temp.process((MonographDTO)record, writer);				
			}
			if(record instanceof PaperMonographDTO){
				temp = cfg.getTemplate(templateType+"_paperMonograph.ftl");
				temp.process((PaperMonographDTO)record, writer);				
			}
			if(record instanceof PaperProceedingsDTO){
				temp = cfg.getTemplate(templateType+"_paperProceedings.ftl");
				temp.process((PaperProceedingsDTO)record, writer);				
			}
			if(record instanceof StudyFinalDocumentDTO){
				temp = cfg.getTemplate(templateType+"_thesis.ftl");
				temp.process((StudyFinalDocumentDTO)record, writer);				
			}
			writer.flush();
		
			return writer.toString();
		} catch (Exception e) {		
			e.printStackTrace();
			return null;
		}
		
	}
	
	

}
