package rs.ac.uns.ftn.informatika.bibliography.reports.obrazci;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;


import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.CTBorder;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.STBorder;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblBorders;
import org.docx4j.wml.TblPr;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

public class DocxUtils {
	
	private static ObjectFactory factory;
	
	static{
		factory = Context.getWmlObjectFactory();
	}
	
	
	public static List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
		List<Object> result = new ArrayList<Object>();
		if (obj instanceof JAXBElement<?>) obj = ((JAXBElement<?>) obj).getValue();
 
		if (obj.getClass().equals(toSearch))
			result.add(obj);
		else if (obj instanceof ContentAccessor) {
			List<?> children = ((ContentAccessor) obj).getContent();
			for (Object child : children) {
				result.addAll(getAllElementFromObject(child, toSearch));
			}
		}
		return result;
	}
	
	public static void addBorders(Tbl table) {
		table.setTblPr(new TblPr());
		CTBorder border = new CTBorder();
		border.setColor("auto");
		border.setSz(new BigInteger("4"));
		border.setSpace(new BigInteger("0"));
		border.setVal(STBorder.SINGLE);
	 TblBorders borders = new TblBorders();
		borders.setBottom(border);
		borders.setLeft(border);
		borders.setRight(border);
		borders.setTop(border);
		borders.setInsideH(border);
		borders.setInsideV(border);
		table.getTblPr().setTblBorders(borders);
	}
	
	public static P createEmptyParagraph(){		
		Text text1 = factory.createText();
		text1.setValue("");			
		R run1 = factory.createR();
		run1.getContent().add(text1);				
		P paragraph1 = factory.createP();			
		paragraph1.getContent().add(run1);				
		return paragraph1;
		
	}
	
	public static P createParagraphWithText(String text){
		ObjectFactory factory = Context.getWmlObjectFactory();
		Text textToAdd = factory.createText();
		textToAdd.setValue(text);
					
		R run = factory.createR();
		run.getContent().add(textToAdd);		
	
		P paragraph = factory.createP();			
		paragraph.getContent().add(run);	
		
		return paragraph;
	}
	
 public static void addTableCell(Tr tableRow, String content, WordprocessingMLPackage wordMLPackage) {
  Tc tableCell = factory.createTc();
  tableCell.getContent().add(
      wordMLPackage.getMainDocumentPart().createParagraphOfText(content));
  tableRow.getContent().add(tableCell);
  }
	

}
