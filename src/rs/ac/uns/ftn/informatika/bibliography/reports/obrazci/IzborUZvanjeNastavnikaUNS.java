package rs.ac.uns.ftn.informatika.bibliography.reports.obrazci;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.docx4j.jaxb.Context;
import org.docx4j.model.table.TblFactory;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.SdtBlock;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;


public class IzborUZvanjeNastavnikaUNS {
	
	private static String imeIPrezime = "BojanaDimicSurla";
	
	private static String filePath = "src/rs/ac/uns/ftn/informatika/bibliography/reports/worddocs/"; 
	
	public static void generateObrazacInWord(){
		File f = new File(filePath+"OBRAZAC1.docx");
		try {
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(f);
			List<Object> elements = DocxUtils.getAllElementFromObject(wordMLPackage.getMainDocumentPart(), SdtBlock.class);		
		((SdtBlock)elements.get(0)).getSdtContent().getContent().clear();
		
		// obtain data
		
		List<EvaluatedRecord> retVal = ObrazciDataUtils.getAllResults("(BISIS)5514", 1);	
		
		// mapa po grupi kategorija
		
		Map<String, List<EvaluatedRecord>> resultGroupMap = new TreeMap<String, List<EvaluatedRecord>>();
	
		for(EvaluatedRecord rec:retVal){
			System.out.println("M"+rec.getResultTypeNumber()+" "+rec.getRecord().toString());
			if(rec.getResultTypeNumber()>=11 && rec.getResultTypeNumber()<=14){
				if(resultGroupMap.get("M11-M14")==null){
					resultGroupMap.put("M11-M14", new ArrayList<EvaluatedRecord>());				
				}
				resultGroupMap.get("M11-M14").add(rec);				
			}
			if(rec.getResultTypeNumber()>=15 && rec.getResultTypeNumber()<=18){
				if(resultGroupMap.get("M15-M18")==null){
					resultGroupMap.put("M15-M18", new ArrayList<EvaluatedRecord>());				
				}
				resultGroupMap.get("M15-M18").add(rec);				
			}
			if(rec.getResultTypeNumber()==21){
				if(resultGroupMap.get("M21")==null){
					resultGroupMap.put("M21", new ArrayList<EvaluatedRecord>());				
				}
				resultGroupMap.get("M21").add(rec);				
			}
			if(rec.getResultTypeNumber()==22){
				if(resultGroupMap.get("M22")==null){
					resultGroupMap.put("M22", new ArrayList<EvaluatedRecord>());				
				}
				resultGroupMap.get("M22").add(rec);				
			}
			if(rec.getResultTypeNumber()==23){
				if(resultGroupMap.get("M23")==null){
					resultGroupMap.put("M23", new ArrayList<EvaluatedRecord>());				
				}
				resultGroupMap.get("M23").add(rec);				
			}
			if(rec.getResultTypeNumber()==24){
				if(resultGroupMap.get("M24")==null){
					resultGroupMap.put("M24", new ArrayList<EvaluatedRecord>());				
				}
				resultGroupMap.get("M24").add(rec);				
			}
			if(rec.getResultTypeNumber()>=25 && rec.getResultTypeNumber()<=28){
				if(resultGroupMap.get("M25-M28")==null){
					resultGroupMap.put("M25-M28", new ArrayList<EvaluatedRecord>());				
				}
				resultGroupMap.get("M25-M28").add(rec);				
			}
			if(rec.getResultTypeNumber()>=31 && rec.getResultTypeNumber()<=36){
				if(resultGroupMap.get("M31-M36")==null){
					resultGroupMap.put("M31-M36", new ArrayList<EvaluatedRecord>());				
				}
				resultGroupMap.get("M31-M36").add(rec);				
			}
			if(rec.getResultTypeNumber()>=41 && rec.getResultTypeNumber()<=49){
				if(resultGroupMap.get("M41-M49")==null){
					resultGroupMap.put("M41-M49", new ArrayList<EvaluatedRecord>());
				}
				resultGroupMap.get("M41-M49").add(rec);
			}			
			if(rec.getResultTypeNumber()==51){
				if(resultGroupMap.get("M51")==null){
					resultGroupMap.put("M51", new ArrayList<EvaluatedRecord>());
				}
				resultGroupMap.get("M51").add(rec);
			}		
			if(rec.getResultTypeNumber()==52){
				if(resultGroupMap.get("M52")==null){
					resultGroupMap.put("M52", new ArrayList<EvaluatedRecord>());
				}
				resultGroupMap.get("M52").add(rec);
			}			
			if(rec.getResultTypeNumber()==53){
				if(resultGroupMap.get("M53")==null){
					resultGroupMap.put("M53", new ArrayList<EvaluatedRecord>());
				}
				resultGroupMap.get("M53").add(rec);
			}
			if(rec.getResultTypeNumber()>=55 && rec.getResultTypeNumber()<=56 ){
				if(resultGroupMap.get("M55-M56")==null){
					resultGroupMap.put("M55-M56", new ArrayList<EvaluatedRecord>());
				}
				resultGroupMap.get("M55-M56").add(rec);
			}			
			if(rec.getResultTypeNumber()>=61 && rec.getResultTypeNumber()<=66 ){
				if(resultGroupMap.get("M61-M66")==null){
					resultGroupMap.put("M61-M66", new ArrayList<EvaluatedRecord>());
				}
				resultGroupMap.get("M61-M66").add(rec);
			}			
			if(rec.getResultTypeNumber()>=71 && rec.getResultTypeNumber()<=72 ){
				if(resultGroupMap.get("M71-M72")==null){
					resultGroupMap.put("M71-M72", new ArrayList<EvaluatedRecord>());
				}
				resultGroupMap.get("M71-M72").add(rec);
			}			
			if(rec.getResultTypeNumber()>=81 && rec.getResultTypeNumber()<=86 ){
				if(resultGroupMap.get("M81-M86")==null){
					resultGroupMap.put("M81-M86", new ArrayList<EvaluatedRecord>());
				}
				resultGroupMap.get("M81-M86").add(rec);
			}		
			if(rec.getResultTypeNumber()>=91 && rec.getResultTypeNumber()<=93 ){
				if(resultGroupMap.get("M91-M93")==null){
					resultGroupMap.put("M91-M93", new ArrayList<EvaluatedRecord>());
				}
				resultGroupMap.get("M91-M93").add(rec);
			}		
		}
		for(String resultGroup:resultGroupMap.keySet()){
			Collections.sort(resultGroupMap.get(resultGroup), new GenericComparator<EvaluatedRecord>("resultType.resultType.classId", "asc"));
		}
		
		// mapa po pojedina;nim kategorijama
		
		Map<String,List<EvaluatedRecord>> resultMap = new TreeMap<String, List<EvaluatedRecord>>();
		
		for(EvaluatedRecord rec:retVal){
			
			if(resultMap.get(rec.getResultType().getResultType().getClassId())==null){
				resultMap.put(rec.getResultType().getResultType().getClassId(), new ArrayList<EvaluatedRecord>());
			}
			resultMap.get(rec.getResultType().getResultType().getClassId()).add(rec);
			
		}
		
		
		
		// print data
		
		// naucne publikacije
		
		
		ObjectFactory factory = Context.getWmlObjectFactory();
	 
	 for(String resultTypeGroup:resultGroupMap.keySet()){	 	
			
			((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createParagraphWithText("КАТЕГОРИЈЕ "+resultTypeGroup+" ("+getStringRepForResultTypeGroup(resultTypeGroup)+")\n"));		 	
			Tbl tbl = factory.createTbl();			
			Tr tableHeader = factory.createTr();
			Tc tableCell1 = factory.createTc();
			tableCell1.getContent().add(wordMLPackage.getMainDocumentPart().createParagraphOfText("Р. бр. "));
			tableHeader.getContent().add(tableCell1);
			Tc tableCell2 = factory.createTc();
			tableCell2.getContent().add(wordMLPackage.getMainDocumentPart().createParagraphOfText("аутори, наслов, часопис, број, странице"));
			tableHeader.getContent().add(tableCell2);
			Tc tableCell3 = factory.createTc();
			tableCell3.getContent().add(wordMLPackage.getMainDocumentPart().createParagraphOfText("категорија"));
			tableHeader.getContent().add(tableCell3);
			tbl.getContent().add(tableHeader);
			int i=1;
			for(EvaluatedRecord rec:resultGroupMap.get(resultTypeGroup)){
				Tr tableRow = factory.createTr();				
				Tc tc1 = factory.createTc();
				tc1.getContent().add(wordMLPackage.getMainDocumentPart().createParagraphOfText(""+i++));
				tableRow.getContent().add(tc1);
				Tc tc2 = factory.createTc();
				tc2.getContent().add(wordMLPackage.getMainDocumentPart().createParagraphOfText(rec.getRecord().getStringRepresentation()));
				tableRow.getContent().add(tc2);
				Tc tc3 = factory.createTc();
				tc3.getContent().add(wordMLPackage.getMainDocumentPart().createParagraphOfText(rec.getResultType().getResultType().getClassId()));
				tableRow.getContent().add(tc3);
				tbl.getContent().add(tableRow);
			}				
		
			DocxUtils.addBorders(tbl);
			((SdtBlock)elements.get(0)).getSdtContent().getContent().add(tbl);
			
			((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());			
	 }
	 ((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());			
	 ((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());			
		
	 // indeks kompetencije
		
	 ((SdtBlock)elements.get(0)).getSdtContent().getContent().add(wordMLPackage.getMainDocumentPart().createStyledParagraphOfText("Heading3", "III.2а Индекс компетенције (у последњем изборном периоду)"));
	 ((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());
	 
	 Double ukupnoBodova = 0.0;
	 for(int i=0;i<4;i++){	
	 	Tbl tblIndeks = factory.createTbl();
	 	Tr tableHeaderIndeks = factory.createTr();
	 	tblIndeks.getContent().add(tableHeaderIndeks);
	 	Tc tableCellKat = factory.createTc();
			tableCellKat.getContent().add(wordMLPackage.getMainDocumentPart().createStyledParagraphOfText("TableIndeksKompetencije", "категорија"));
			tableHeaderIndeks.getContent().add(tableCellKat);
			
			for(int j=0;j<14;j++){
				if(14*i+j<createResultTypeList().size()){
					Tc tableCellKatM = factory.createTc();
					tableCellKatM.getContent().add(wordMLPackage.getMainDocumentPart()
							.createStyledParagraphOfText("TableIndeksKompetencije", createResultTypeList().get(14*i+j)));
					tableHeaderIndeks.getContent().add(tableCellKatM);
				}else{
					Tc tableCellKatM = factory.createTc();
					tableCellKatM.getContent().add(wordMLPackage.getMainDocumentPart()
							.createStyledParagraphOfText("TableIndeksKompetencije", "  "));
					tableHeaderIndeks.getContent().add(tableCellKatM);					
				}
			}	
			
			Tr tableRowBrojPub = factory.createTr();
			tblIndeks.getContent().add(tableRowBrojPub);
	 	Tc tableCellBrojPub = factory.createTc();
	 	tableCellBrojPub.getContent().add(wordMLPackage.getMainDocumentPart().createStyledParagraphOfText("TableIndeksKompetencije", "број пуб."));
	 	tableRowBrojPub.getContent().add(tableCellBrojPub);
			
			
			for(int j=0;j<14;j++){
				if(14*i+j<createResultTypeList().size()){
					
					String text = resultMap.get(createResultTypeList().get(14*i+j))==null ? "" : ""+resultMap.get(createResultTypeList().get(14*i+j)).size();
					
					Tc tableCellBrojPubM = factory.createTc();
					tableCellBrojPubM.getContent().add(wordMLPackage.getMainDocumentPart()
							.createStyledParagraphOfText("TableIndeksKompetencije",text));
					tableRowBrojPub.getContent().add(tableCellBrojPubM);					
					
				}else{
					Tc tableCellBrojPubM = factory.createTc();
					tableCellBrojPubM.getContent().add(wordMLPackage.getMainDocumentPart()
							.createStyledParagraphOfText("TableIndeksKompetencije", "  "));
					tableRowBrojPub.getContent().add(tableCellBrojPubM);					
				}
			}
			
			
			Tr tableRowBrojBodova = factory.createTr();
			tblIndeks.getContent().add(tableRowBrojBodova);
	 	Tc tableCellBrojBodova = factory.createTc();
	 	tableCellBrojBodova.getContent().add(wordMLPackage.getMainDocumentPart().createStyledParagraphOfText("TableIndeksKompetencije", "број бодова"));
	 	tableRowBrojBodova.getContent().add(tableCellBrojBodova);
			
			
			for(int j=0;j<14;j++){
				if(14*i+j<createResultTypeList().size()){
					
					
					Double brojPoena = 0.0;
					
					if(resultMap.get(createResultTypeList().get(14*i+j))!=null){
						List<EvaluatedRecord> resPom = resultMap.get(createResultTypeList().get(14*i+j));
						for(EvaluatedRecord rec:resPom){
							brojPoena += rec.getResultType().getQuantitativeMeasure();
						}					
					}					
					ukupnoBodova = ukupnoBodova + brojPoena;					
					String text = brojPoena==0.0 ? "" : ""+brojPoena;
					
					Tc tableCellBrojPubM = factory.createTc();
					tableCellBrojPubM.getContent().add(wordMLPackage.getMainDocumentPart()
							.createStyledParagraphOfText("TableIndeksKompetencije",text));
					tableRowBrojBodova.getContent().add(tableCellBrojPubM);					
					
				}else{
					Tc tableCellBrojPubM = factory.createTc();
					tableCellBrojPubM.getContent().add(wordMLPackage.getMainDocumentPart()
							.createStyledParagraphOfText("TableIndeksKompetencije", ""));
					tableRowBrojBodova.getContent().add(tableCellBrojPubM);					
				}
			}		
			
			DocxUtils.addBorders(tblIndeks);
			((SdtBlock)elements.get(0)).getSdtContent().getContent().add(tblIndeks);
			
			((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());
	 	
	 }
	 
	 ((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());
		
		((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createParagraphWithText("укупно бодова = "+ukupnoBodova));			
		((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());
		
		((SdtBlock)elements.get(0)).getSdtContent().getContent().add(wordMLPackage.getMainDocumentPart()
							.createStyledParagraphOfText("Heading3", "III.2б списак публикацаија из претходног изборног периода (М10-М18,М21-М24,М51-М54)"));
		
	 ((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());
	 
	 int brojac = 1;
	
	 if(resultMap.get("M10")!=null)
		 for(EvaluatedRecord rec: resultMap.get("M10")){
		 ((SdtBlock)elements.get(0)).getSdtContent().getContent()
		 .add(DocxUtils.createParagraphWithText(brojac+++". "+ rec.getRecord().getStringRepresentation()));
		 ((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());
			
		 }
	 
	 
	 if(resultMap.get("M11")!=null)
		 for(EvaluatedRecord rec: resultMap.get("M11")){
			 ((SdtBlock)elements.get(0)).getSdtContent().getContent()
			 .add(DocxUtils.createParagraphWithText(brojac+++". "+ rec.getRecord().getStringRepresentation()));
			 ((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());
				
			 }
	 if(resultMap.get("M12")!=null)
	 for(EvaluatedRecord rec: resultMap.get("M12")){
		 ((SdtBlock)elements.get(0)).getSdtContent().getContent()
		 .add(DocxUtils.createParagraphWithText(brojac+++". "+ rec.getRecord().getStringRepresentation()));
		 ((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());
			
		 }
	 
	 if(resultMap.get("M13")!=null)
		 for(EvaluatedRecord rec: resultMap.get("M13")){
			 ((SdtBlock)elements.get(0)).getSdtContent().getContent()
			 .add(DocxUtils.createParagraphWithText(brojac+++". "+ rec.getRecord().getStringRepresentation()));
			 ((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());
				
			 }
	 if(resultMap.get("M14")!=null)
		 for(EvaluatedRecord rec: resultMap.get("M14")){
			 ((SdtBlock)elements.get(0)).getSdtContent().getContent()
			 .add(DocxUtils.createParagraphWithText(brojac+++". "+ rec.getRecord().getStringRepresentation()));
			 ((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());
				
			 }
	 if(resultMap.get("M15")!=null)
		 for(EvaluatedRecord rec: resultMap.get("M15")){
			 ((SdtBlock)elements.get(0)).getSdtContent().getContent()
			 .add(DocxUtils.createParagraphWithText(brojac+++". "+ rec.getRecord().getStringRepresentation()));
			 ((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());
				
			 }
	 
	 if(resultMap.get("M16")!=null)
		 for(EvaluatedRecord rec: resultMap.get("M16")){
			 ((SdtBlock)elements.get(0)).getSdtContent().getContent()
			 .add(DocxUtils.createParagraphWithText(brojac+++". "+ rec.getRecord().getStringRepresentation()));
			 ((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());
				
			 }
	 
	 if(resultMap.get("M17")!=null)
		 for(EvaluatedRecord rec: resultMap.get("M17")){
			 ((SdtBlock)elements.get(0)).getSdtContent().getContent()
			 .add(DocxUtils.createParagraphWithText(brojac+++". "+ rec.getRecord().getStringRepresentation()));
			 ((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());
				
			 }
	 
	 if(resultMap.get("M18")!=null)
		 for(EvaluatedRecord rec: resultMap.get("M18")){
			 ((SdtBlock)elements.get(0)).getSdtContent().getContent()
			 .add(DocxUtils.createParagraphWithText(brojac+++". "+ rec.getRecord().getStringRepresentation()));
			 ((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());
				
			 }
	 
	 if(resultMap.get("M21")!=null)
	 for(EvaluatedRecord rec: resultMap.get("M21")){
		 ((SdtBlock)elements.get(0)).getSdtContent().getContent()
		 .add(DocxUtils.createParagraphWithText(brojac+++". "+ rec.getRecord().getStringRepresentation()));
		 ((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());
			
		 }
	 
	 if(resultMap.get("M22")!=null)
	 for(EvaluatedRecord rec: resultMap.get("M22")){
		 ((SdtBlock)elements.get(0)).getSdtContent().getContent()
		 .add(DocxUtils.createParagraphWithText(brojac+++". "+ rec.getRecord().getStringRepresentation()));
		 ((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());
			
		 }
	 
	 if(resultMap.get("M23")!=null)
	 for(EvaluatedRecord rec: resultMap.get("M23")){
		 ((SdtBlock)elements.get(0)).getSdtContent().getContent()
		 .add(DocxUtils.createParagraphWithText(brojac+++". "+ rec.getRecord().getStringRepresentation()));
		 ((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());
			
		 }
	 
	 if(resultMap.get("M24")!=null)
	 for(EvaluatedRecord rec: resultMap.get("M24")){
		 ((SdtBlock)elements.get(0)).getSdtContent().getContent()
		 .add(DocxUtils.createParagraphWithText(brojac+++". "+ rec.getRecord().getStringRepresentation()));
		 ((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());
			
		 }
	 
	 if(resultMap.get("M51")!=null)
	 for(EvaluatedRecord rec: resultMap.get("M51")){
		 ((SdtBlock)elements.get(0)).getSdtContent().getContent()
		 .add(DocxUtils.createParagraphWithText(brojac+++". "+ rec.getRecord().getStringRepresentation()));
		 ((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());
			
		 }
	 
	 if(resultMap.get("M52")!=null)
	 for(EvaluatedRecord rec: resultMap.get("M52")){
		 ((SdtBlock)elements.get(0)).getSdtContent().getContent()
		 .add(DocxUtils.createParagraphWithText(brojac+++". "+ rec.getRecord().getStringRepresentation()));
		 ((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());
			
		 }
	 
	 if(resultMap.get("M53")!=null)
	 for(EvaluatedRecord rec: resultMap.get("M53")){
		 ((SdtBlock)elements.get(0)).getSdtContent().getContent()
		 .add(DocxUtils.createParagraphWithText(brojac+++". "+ rec.getRecord().getStringRepresentation()));
		 ((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());
			
		 }
	 
	 if(resultMap.get("M54")!=null)
	 for(EvaluatedRecord rec: resultMap.get("M54")){
		 ((SdtBlock)elements.get(0)).getSdtContent().getContent()
		 .add(DocxUtils.createParagraphWithText(brojac+++". "+ rec.getRecord().getStringRepresentation()));
		 ((SdtBlock)elements.get(0)).getSdtContent().getContent().add(DocxUtils.createEmptyParagraph());
			
		 }
	
	 
	 
				
				
				
		
		wordMLPackage.save(new File(filePath+"OBRAZAC1"+imeIPrezime+".docx"));	
		
		
		} catch (Docx4JException e) {	
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	private static String getStringRepForResultTypeGroup(String resTypeGroup){
		if(resTypeGroup.equals("М11-М14")) return "Монографије, монографске студије";
		if(resTypeGroup.equals("M15-M18")) return "Лексикографске и картографске публикацијемеђународног значаја";
		if(resTypeGroup.equals("M21")) return "Рад у врхунском међународном часопису";
		if(resTypeGroup.equals("M22")) return "Рад у истакнутом међународном часопису";
		if(resTypeGroup.equals("M23")) return "Рад у међународном часопису";
		if(resTypeGroup.equals("M24")) return "Рад у часопису међународног значаја верификованог посебном одлуком";
		if(resTypeGroup.equals("M25-M28")) return "Научна критика и полемика у истакнутом међународном часопису; Научна критика и полемика у међународном часопису; Уређивање истакнутог међународног научног часописа на год. нивоу; Уређивање међународног научног часописа";
		if(resTypeGroup.equals("M31-M36")) return "Зборници међународних научних скупова";
		if(resTypeGroup.equals("M41-M49")) return "Националне монографије, тематски зборници, лексикографске и картографске публикације националног значаја; научни преводи и критичка издања грађе, библиографске публикације";
		if(resTypeGroup.equals("M51")) return "Рад у водећем часопису националног значаја";
		if(resTypeGroup.equals("M52")) return "Рад у часопису националног значаја";
		if(resTypeGroup.equals("M53")) return "Рад у научном часопису";
		if(resTypeGroup.equals("M55-M56")) return "Уређивање (водећег) научног часописа националног нивоа (на годишњем нивоу)";
		if(resTypeGroup.equals("M61-M66")) return "Зборници скупова националног значаја";
		if(resTypeGroup.equals("M71-M72")) return "магистарске и докторске дисертације";
		if(resTypeGroup.equals("M81-M86")) return "Техничка и развоја решења";
		if(resTypeGroup.equals("M91-M93")) return "Патенти, ауторске изложбе,тестови";
			
		return "";
		
	}
	
	
	private static List<String> createResultTypeList(){
		List<String> retVal = new ArrayList<String>();
		retVal.add("M11");
		retVal.add("M12");
		retVal.add("M13");
		retVal.add("M14");
		retVal.add("M15");
		retVal.add("M16");
		retVal.add("M17");
		retVal.add("M18");
		retVal.add("M21");
		retVal.add("M22");
		retVal.add("M23");
		retVal.add("M24");
		retVal.add("M25");
		retVal.add("M26");
		retVal.add("M27");
		retVal.add("M28");
		retVal.add("M31");
		retVal.add("M32");
		retVal.add("M33");
		retVal.add("M34");
		retVal.add("M35");
		retVal.add("M36");
		retVal.add("M41");
		retVal.add("M42");
		retVal.add("M43");
		retVal.add("M44");
		retVal.add("M45");
		retVal.add("M46");
		retVal.add("M47");
		retVal.add("M48");
		retVal.add("M49");		
		retVal.add("M51");
		retVal.add("M52");
		retVal.add("M53");
		retVal.add("M55");
		retVal.add("M56");
		retVal.add("M61");
		retVal.add("M62");
		retVal.add("M63");
		retVal.add("M64");
		retVal.add("M65");
		retVal.add("M66");
		retVal.add("M71");
		retVal.add("M72");
		retVal.add("M81");
		retVal.add("M82");
		retVal.add("M83");
		retVal.add("M84");
		retVal.add("M85");
		retVal.add("M86");
		retVal.add("M91");
		retVal.add("M92");
	
		
		
		
		return retVal;
		
	}
	
	
	
	public static void main(String[] args){
		// bojana id (BISIS)5514
		
		generateObrazacInWord();
	
		
		
		
	}
	
}
