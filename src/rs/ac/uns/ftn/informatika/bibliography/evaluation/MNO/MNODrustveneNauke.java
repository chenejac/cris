package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNODrustveneNauke extends MNO{

	private MNODrustveneNauke() {
		super();
		this.nameMNO = "Društvene nauke";
		this.yearsSpecial = new int [4];
		yearsSpecial[0] = 2008;
		yearsSpecial[1] = 2009;
		yearsSpecial[2] = 2010;
		yearsSpecial[3] = 2011;
		this.specialJournalsAllYears = new HashMap<Integer,HashMap<String,String>>();
		
		//spceijalni casopisi u godinama
		HashMap <String, String> special2008;
		HashMap <String, String> special2009;
		HashMap <String, String> special2010;
		HashMap <String, String> special2011;
		
		//kreiranje liste casopisa u odredjenim godinama za maticne odbore 
		special2008 = new HashMap<String, String>();
		special2008.put("0048-5705", "M23"); 			//(BISIS)23977	0048-5705	Psihologija
		special2008.put("0003-2565", "M24"); 			//(BISIS)38122	0003-2565	Anali Pravnog fakulteta u Beogradu
		special2008.put("1820-6700", "M24"); 			//NEMA			1820-6700	Godišnjak Fakulteta političkih nauka
		special2008.put("0013-3264", "M24"); 			//(BISIS)35939	0013-3264	Economic Annals
		special2008.put("0351-2665", "M24"); 			//(BISIS)37665	0351-2665	Engrami
		special2008.put("1451-3455", "M24"); 			//NEMA			1451-3455	Philotheos: International journal for philosophy and theology
		special2008.put("0038-0318", "M24"); 			//(BISIS)8471	0038-0318	Sociologija
		special2008.put("0085-6320", "M24"); 			//(BISIS)35093	0085-6320	Sociološki pregled
		special2008.put("0038-982X", "M24"); 			//(BISIS)8472	0038-982X	Stanovnistvo
		special2008.put("0353-7919", "M24"); 			//(BISIS)24284	0353-7919	Teme
		special2008.put("0351-2274", "M24"); 			//(BISIS)35515	0351-2274	Theoria
		special2008.put("0579-6431", "M24"); 			//(BISIS)34533	0579-6431	Zbornik Instituta za pedagoška istraživanja
		special2008.put("0352-5732", "M24"); 			//(BISIS)8485	0352-5732	Zbornik Matice srpske za društvene nauke
		special2008.put("0354-0243", "M24"); 			//(BISIS)3039	0354-0243	Yugoslav journal of operations research
		special2008.put("1451-5040", "M24"); 			//(BISIS)47857	1451-5040	Filozofski godišnjak
		special2008.put("0354-5415", "M51"); 			//NEMA			0354-5415	Andragoške studije
		special2008.put("1820-0958", "M51"); 			//(BISIS)39881	1820-0958	Arhe
		special2008.put("1820-5461", "M51"); 			//NEMA			1820-5461	Godišnjak srpske akademije obrazovanja
		special2008.put("0352-3462", "M51"); 			//(BISIS)32973	0352-3462	Ekonomika poljoprivrede
		special2008.put("0353-8648", "M51"); 			//(BISIS)37207	0353-8648	Ekonomske teme
		special2008.put("0350-0373", "M51"); 			//(BISIS)46506	0350-0373	Industrija
		special2008.put("0025-8555", "M51"); 			//(BISIS)34632	0025-8555	Međunarodni problemi
		special2008.put("0547-3330", "M51"); 			//NEMA			0547-3330	Nastava i vaspitanje
		special2008.put("0031-3807", "M51"); 			//(BISIS)2961	0031-3807	Pedagogija
		special2008.put("1451-4281", "M51"); 			//(BISIS)37946	1451-4281	Politička revija
		special2008.put("0350-0500", "M51"); 			//NEMA			0350-0500	Pravni život
		special2008.put("0350-2538", "M51"); 			//(BISIS)38309	0350-2538	Psihijatrija danas
		special2008.put("0354-5989", "M51"); 			//(BISIS)33422	0354-5989	Srpska politicka misao
		special2008.put("0564-7010", "M51"); 			//(BISIS)34957	0564-7010	Treći program
		special2008.put("0353-5738", "M51"); 			//(BISIS)35870	0353-5738	Filozofija i društvo
		special2008.put("0409-2953", "M52"); 			//(BISIS)35067	0409-2953	Bezbednost
		special2008.put("0354-8759", "M52"); 			//NEMA			0354-8759	Beogradska defektološka škola
		special2008.put("0374-0730", "M52"); 			//				0374-0730	Godišnjak Filozofskog fakulteta, Novi Sad
		special2008.put("1451-9739", "M52"); 			//				1451-9739	Godišnjak za sociologiju Filozofskog fakulteta u Nišu
		special2008.put("1820-4244", "M52"); 			//				1820-4244	Demografija
		special2008.put("0353-443X", "M52"); 			//				0353-443X	Ekonomika preduzeća
		special2008.put("0350-2694", "M52"); 			//NEMA			0350-2694	Zbornik Instituta za kriminološka i sociološka istraživanja
		special2008.put("1450-6718", "M52"); 			//NEMA			1450-6718	Zbornik radova Učiteljskog fakulteta, Užice
		special2008.put("0350-8501", "M52"); 			//				0350-8501	Zbornik radova Pravnog fakulteta, Niš
		special2008.put("1451-7361", "M52"); 			//NEMA			1451-7361	Žurnal za sociologiju
		special2008.put("0352-2334", "M52"); 			//NEMA			0352-2334	Inovacije u nastavi - časopis za savremenu nastavu
		special2008.put("1820-4589", "M52"); 			//NEMA			1820-4589	Kultura polisa
		special2008.put("0354-8635", "M52"); 			//NEMA			0354-8635	Management - časopis za teoriju i praksu menadžmenta
		special2008.put("0354-3471", "M52"); 			//NEMA			0354-3471	Marketing
		special2008.put("0237-1995", "M52"); 			//				0237-1995	Marketing
		special2008.put("1820-3159", "M52"); 			//NEMA			1820-3159	Megatrend revija
		special2008.put("0543-3657", "M52"); 			//NEMA			0543-3657	Međunarodna politika
		special2008.put("0354-8872", "M52"); 			//				0354-8872	Nauka, bezbednost, policija
		special2008.put("1820-4996", "M52"); 			//				1820-4996	Nacionalni interes
		special2008.put("1450-7382", "M52"); 			//				1450-7382	Nova srpska politička misao
		special2008.put("1452-595X", "M52"); 			//				1452-595X	Panoeconomicus
		special2008.put("0553-4569", "M52"); 			//				0553-4569	Pedagoška stvarnost
		special2008.put("1820-659X", "M52"); 			//				1820-659X	Politikologija religije
		special2008.put("1450-6114", "M52"); 			//NEMA			1450-6114	Računovodstvo
		special2008.put("0353-7935", "M52"); 			//				0353-7935	Revizor
		special2008.put("0486-6096", "M52"); 			//				0486-6096	Review of International Affairs
		special2008.put("1820-2969", "M52"); 			//NEMA			1820-2969	Revija za kriminologiju i krivično pravo
		special2008.put("1451-8759", "M52"); 			//				1451-8759	Religija i tolerancija
		special2008.put("0354-401X", "M52"); 			//				0354-401X	Socijalna misao
		special2008.put("1452-7367", "M52"); 			//				1452-7367	Specijalna edukacija i rehabilitacija
		special2008.put("0354-8414", "M52"); 			//NEMA			0354-8414	Strategijski menadžment
		special2008.put("1450-9911", "M52"); 			//NEMA			1450-9911	Tehnika – Menadžment
		special2008.put("1450-6637", "M52"); 			//				1450-6637	Temida
		special2008.put("0354-4699", "M52"); 			//				0354-4699	Facta Universitatis: Series Economics and Organization
		special2008.put("0015-2145", "M52"); 			//				0015-2145	Finansije
		special2008.put("1820-0214", "M52"); 			//				1820-0214	Computer Science and Information Systems / ComSIS
		special2008.put("0350-2120", "M53"); 			//NEMA			0350-2120	Anali Ekonomskog fakulteta u Subotici
		special2008.put("1450-7129", "M53"); 			//NEMA			1450-7129	Balkans Law Review
		special2008.put("0042-8426", "M53"); 			//				0042-8426	Vojno delo
		special2008.put("0017-0933", "M53"); 			//				0017-0933	Glasnik Advokatske komore Vojvodine
		special2008.put("1451-5407", "M53"); 			//				1451-5407	Godišnjak za psihologiju
		special2008.put("1452-5917", "M53"); 			//				1452-5917	Godišnjak Fakulteta sporta i fizičkog vaspitanja
		special2008.put("0419-3903", "M53"); 			//NEMA			0419-3903	Direktor
		special2008.put("0350-137x", "M53"); 			//				0350-137x	Ekonomika
		special2008.put("0354-9135", "M53"); 			//NEMA			0354-9135	Ekonomski vidici
		special2008.put("1451-3188", "M53"); 			//NEMA			1451-3188	Evropsko zakonodavstvo
		special2008.put("1452-3620", "M53"); 			//NEMA			1452-3620	Evropski pravnik
		special2008.put("0550-2179", "M53"); 			//NEMA			0550-2179	Zbornik radova Pravnog fakulteta, Novi Sad
		special2008.put("0354-3293", "M53"); 			//				0354-3293	Zbornik radova Filozofskog fakulteta u Prištini
		special2008.put("1451-4397", "M53"); 			//				1451-4397	Info M
		special2008.put("1451-6861", "M53"); 			//NEMA			1451-6861	Ljudska Bezbednost
		special2008.put("1451-642X", "M53"); 			//NEMA			1451-642X	Nastava i istorija
		special2008.put("0353-7129", "M53"); 			//NEMA			0353-7129	Norma
		special2008.put("1450-9407", "M53"); 			//				1450-9407	Obrazovna tehnologija
		special2008.put("0354-3501", "M53"); 			//NEMA			0354-3501	Pravo i privreda
		special2008.put("0352-3713", "M53"); 			//NEMA			0352-3713	Pravo - teorija i praksa
		special2008.put("1820-6859", "M53"); 			//				1820-6859	Poslovna ekonomija
		special2008.put("1450-7986", "M53"); 			//				1450-7986	Revija za evropsko pravo
		special2008.put("1452-4864", "M53"); 			//				1452-4864	Serbian Journal of Management
		special2008.put("0039-2138", "M53"); 			//				0039-2138	Strani pravni život
		special2008.put("0564-3619", "M53"); 			//NEMA			0564-3619	Tržište, novac, kapital
		special2008.put("0354-4648", "M53"); 			//				0354-4648	Facta Universitatis: Series Philosophy, Sociology and Psychology
		special2008.put("1451-740X", "M53"); 			//				1451-740X	Facta Universitatis: Series Physical Education and Sport
		special2008.put("0350-3828", "M53"); 			//				0350-3828	Fizička kultura
		special2008.put("1820-0702", "M53"); 			//NEMA			1820-0702	Finansije, bankarstvo, revizija i osiguranje
		special2008.put("1451-5822", "M53"); 			//				1451-5822	Hereticus
		this.specialJournalsAllYears.put(2008, special2008);
		
		//kreiranje liste casopisa u odredjenim godinama za maticne odbore 
		special2009 = new HashMap<String, String>();
		special2009.put("1452-595X", "M23"); 			//				1452-595X	Panoeconomicus
		special2009.put("0048-5705", "M23"); 			//				0048-5705	Psihologija
		special2009.put("0003-2565", "M24"); 			//				0003-2565	Anali Pravnog fakulteta u Beogradu
		special2009.put("0013-3264", "M24"); 			//				0013-3264	Economic Annals
		special2009.put("0351-2665", "M24"); 			//				0351-2665	Engrami
		special2009.put("1451-5040", "M24"); 			//				1451-5040	Filozofski godišnjak
		special2009.put("1820-659X", "M24"); 			//				1820-659X	Politikologija religije
		special2009.put("0038-0318", "M24"); 			//				0038-0318	Sociologija
		special2009.put("0085-6320", "M24"); 			//				0085-6320	Sociološki pregled
		special2009.put("0354-5989", "M24"); 			//				0354-5989	Srpska politicka misao
		special2009.put("0038-982X", "M24"); 			//				0038-982X	Stanovnistvo
		special2009.put("0353-7919", "M24"); 			//				0353-7919	Teme
		special2009.put("0351-2274", "M24"); 			//				0351-2274	Theoria
		special2009.put("0354-0243", "M24"); 			//				0354-0243	Yugoslav journal of operations research
		special2009.put("0579-6431", "M24"); 			//				0579-6431	Zbornik Instituta za pedagoška istraživanja
		special2009.put("0352-5732", "M24"); 			//				0352-5732	Zbornik Matice srpske za društvene nauke
		special2009.put("0354-5415", "M51"); 			//				0354-5415	Andragoške studije
		special2009.put("1820-0958", "M51"); 			//				1820-0958	Arhe
		special2009.put("0352-3462", "M51"); 			//				0352-3462	Ekonomika poljoprivrede
		special2009.put("0353-443X", "M51"); 			//				0353-443X	Ekonomika preduzeća
		special2009.put("0353-8648", "M51"); 			//				0353-8648	Ekonomske teme
		special2009.put("1451-3455", "M51"); 			//				1451-3455	Philotheos: International journal for philosophy and theology
		special2009.put("0353-5738", "M51"); 			//				0353-5738	Filozofija i društvo
		special2009.put("1820-6700", "M51"); 			//				1820-6700	Godišnjak Fakulteta političkih nauka
		special2009.put("1820-5461", "M51"); 			//				1820-5461	Godišnjak srpske akademije obrazovanja
		special2009.put("0350-0373", "M51"); 			//				0350-0373	Industrija
		special2009.put("0025-8555", "M51"); 			//				0025-8555	Međunarodni problemi
		special2009.put("1820-3159", "M51"); 			//				1820-3159	Megatrend revija
		special2009.put("1820-4996", "M51"); 			//				1820-4996	Nacionalni interes
		special2009.put("0547-3330", "M51"); 			//				0547-3330	Nastava i vaspitanje
		special2009.put("1450-7382", "M51"); 			//				1450-7382	Nova srpska politička misao
		special2009.put("0031-3807", "M51"); 			//				0031-3807	Pedagogija
		special2009.put("1451-4281", "M51"); 			//				1451-4281	Politička revija
		special2009.put("0350-0500", "M51"); 			//				0350-0500	Pravni život
		special2009.put("0350-2538", "M51"); 			//				0350-2538	Psihijatrija danas
		special2009.put("0564-7010", "M51"); 			//				0564-7010	Treći program
		special2009.put("0350-2120", "M52"); 			//				0350-2120	Anali Ekonomskog fakulteta u Subotici
		special2009.put("0354-8759", "M52"); 			//				0354-8759	Beogradska defektološka škola
		special2009.put("0409-2953", "M52"); 			//				0409-2953	Bezbednost
		special2009.put("1452-7405", "M52"); 			//				1452-7405	CM - časopis za upravljanje komuniciranjem
		special2009.put("1820-0214", "M52"); 			//				1820-0214	Computer Science and Information Systems / ComSIS
		special2009.put("1820-4244", "M52"); 			//				1820-4244	Demografija
		special2009.put("0354-4699", "M52"); 			//				0354-4699	Facta Universitatis: Series Economics and Organization
		special2009.put("0015-2145", "M52"); 			//				0015-2145	Finansije
		special2009.put("0374-0730", "M52"); 			//				0374-0730	Godišnjak Filozofskog fakulteta, Novi Sad
		special2009.put("1451-9739", "M52"); 			//				1451-9739	Godišnjak za sociologiju Filozofskog fakulteta u Nišu
		special2009.put("1451-4397", "M52"); 			//				1451-4397	Info M
		special2009.put("0352-2334", "M52"); 			//				0352-2334	Inovacije u nastavi - časopis za savremenu nastavu
		special2009.put("1820-4589", "M52"); 			//				1820-4589	Kultura polisa
		special2009.put("1451-6861", "M52"); 			//				1451-6861	Ljudska Bezbednost
		special2009.put("0354-8635", "M52"); 			//				0354-8635	Management - časopis za teoriju i praksu menadžmenta
		special2009.put("0354-3471", "M52"); 			//				0354-3471	Marketing
		special2009.put("0237-1995", "M52"); 			//				0237-1995	Marketing
		special2009.put("0543-3657", "M52"); 			//				0543-3657	Međunarodna politika
		special2009.put("0354-8872", "M52"); 			//				0354-8872	Nauka, bezbednost, policija
		special2009.put("0553-4569", "M52"); 			//				0553-4569	Pedagoška stvarnost
		special2009.put("1820-6859", "M52"); 			//				1820-6859	Poslovna ekonomija
		special2009.put("0354-3501", "M52"); 			//				0354-3501	Pravo i privreda
		special2009.put("1450-6114", "M52"); 			//				1450-6114	Računovodstvo
		special2009.put("1451-8759", "M52"); 			//				1451-8759	Religija i tolerancija
		special2009.put("0486-6096", "M52"); 			//				0486-6096	Review of International Affairs
		special2009.put("1820-2969", "M52"); 			//				1820-2969	Revija za kriminologiju i krivično pravo
		special2009.put("0353-7935", "M52"); 			//				0353-7935	Revizor
		special2009.put("1452-4864", "M52"); 			//				1452-4864	Serbian Journal of Management
		special2009.put("0354-401X", "M52"); 			//				0354-401X	Socijalna misao
		special2009.put("1452-7367", "M52"); 			//				1452-7367	Specijalna edukacija i rehabilitacija
		special2009.put("0039-2138", "M52"); 			//				0039-2138	Strani pravni život
		special2009.put("0354-8414", "M52"); 			//				0354-8414	Strategijski menadžment
		special2009.put("1450-9911", "M52"); 			//				1450-9911	Tehnika – Menadžment
		special2009.put("1450-6637", "M52"); 			//				1450-6637	Temida
		special2009.put("0042-8426", "M52"); 			//				0042-8426	Vojno delo
		special2009.put("0350-2694", "M52"); 			//				0350-2694	Zbornik Instituta za kriminološka i sociološka istraživanja
		special2009.put("0550-2179", "M52"); 			//				0550-2179	Zbornik radova Pravnog fakulteta, Novi Sad
		special2009.put("0350-8501", "M52"); 			//				0350-8501	Zbornik radova Pravnog fakulteta, Niš
		special2009.put("1450-6718", "M52"); 			//				1450-6718	Zbornik radova Učiteljskog fakulteta, Užice
		special2009.put("1451-7361", "M52"); 			//				1451-7361	Žurnal za sociologiju
		special2009.put("0006-5714", "M53"); 			//NEMA			0006-5714	Bogoslovlje
		special2009.put("1450-7196", "M53"); 			//NEMA			1450-7196	Communications in Dependability and Quality Management
		special2009.put("0419-3903", "M53"); 			//				0419-3903	Direktor
		special2009.put("1452-3620", "M53"); 			//				1452-3620	Evropski pravnik
		special2009.put("1451-3188", "M53"); 			//				1451-3188	Evropsko zakonodavstvo
		special2009.put("0350-137x", "M53"); 			//				0350-137x	Ekonomika
		special2009.put("0354-9135", "M53"); 			//				0354-9135	Ekonomski vidici
		special2009.put("1450-863X", "M53"); 			//				1450-863X	Ekonomski horizonti
		special2009.put("0354-4648", "M53"); 			//				0354-4648	Facta Universitatis: Series Philosophy, Sociology and Psychology
		special2009.put("1451-740X", "M53"); 			//				1451-740X	Facta Universitatis: Series Physical Education and Sport
		special2009.put("0350-3828", "M53"); 			//				0350-3828	Fizička kultura
		special2009.put("0354-415x", "M53"); 			//				0354-415x	Gerontologija
		special2009.put("0017-0933", "M53"); 			//				0017-0933	Glasnik Advokatske komore Vojvodine
		special2009.put("1451-5407", "M53"); 			//				1451-5407	Godišnjak za psihologiju
		special2009.put("1821-150X", "M53"); 			//NEMA			1821-150X	Godisnjak fakulteta bezbednosti
		special2009.put("1452-5917", "M53"); 			//				1452-5917	Godišnjak Fakulteta sporta i fizičkog vaspitanja
		special2009.put("1451-5822", "M53"); 			//				1451-5822	Hereticus
		special2009.put("1451-5113", "M53"); 			//NEMA			1451-5113	HOTEL link -časopis za teoriju i praksu hotelijerstva
		special2009.put("0354-9801", "M53"); 			//NEMA			0354-9801	Metodička praksa
		special2009.put("1451-642X", "M53"); 			//				1451-642X	Nastava i istorija
		special2009.put("0353-7129", "M53"); 			//				0353-7129	Norma
		special2009.put("1450-9407", "M53"); 			//				1450-9407	Obrazovna tehnologija
		special2009.put("0352-3713", "M53"); 			//				0352-3713	Pravo - teorija i praksa
		special2009.put("1452-9335", "M53"); 			//				1452-9335	Revija za bezbednost
		special2009.put("1450-9148", "M53"); 			//NEMA			1450-9148	Sabornost
		special2009.put("0354-3293", "M53"); 			//				0354-3293	Zbornik radova Filozofskog fakulteta u Prištini
		special2009.put("1840-1538", "M53"); 			//NEMA			1840-1538	Sociološki godišnjak
		this.specialJournalsAllYears.put(2009, special2009);
		
		//kreiranje liste casopisa u odredjenim godinama za maticne odbore 
		special2010 = new HashMap<String, String>();
		special2010.put("1452-595X", "M23"); 			//				1452-595X	Panoeconomicus
		special2010.put("0048-5705", "M23"); 			//				0048-5705	Psihologija
		special2010.put("0003-2565", "M24"); 			//				0003-2565	Anali Pravnog fakulteta u Beogradu
		special2010.put("0354-5415", "M24"); 			//				0354-5415	Andragoške studije
		special2010.put("0352-3462", "M24"); 			//				0352-3462	Ekonomika poljoprivrede
		special2010.put("0013-3264", "M24"); 			//				0013-3264	Economic Annals
		special2010.put("0351-2665", "M24"); 			//				0351-2665	Engrami
		special2010.put("1451-5040", "M24"); 			//				1451-5040	Filozofski godišnjak
		special2010.put("0350-0373", "M24"); 			//				0350-0373	Industrija
		special2010.put("0547-3330", "M24"); 			//				0547-3330	Nastava i vaspitanje
		special2010.put("1820-659X", "M24"); 			//				1820-659X	Politikologija religije
		special2010.put("0038-0318", "M24"); 			//				0038-0318	Sociologija
		special2010.put("0085-6320", "M24"); 			//				0085-6320	Sociološki pregled
		special2010.put("0354-5989", "M24"); 			//				0354-5989	Srpska politicka misao
		special2010.put("0038-982X", "M24"); 			//				0038-982X	Stanovnistvo
		special2010.put("0353-7919", "M24"); 			//				0353-7919	Teme
		special2010.put("0351-2274", "M24"); 			//				0351-2274	Theoria
		special2010.put("0579-6431", "M24"); 			//				0579-6431	Zbornik Instituta za pedagoška istraživanja
		special2010.put("0352-5732", "M24"); 			//				0352-5732	Zbornik Matice srpske za društvene nauke
		special2010.put("1820-0958", "M51"); 			//				1820-0958	Arhe
		special2010.put("1452-7405", "M51"); 			//				1452-7405	CM - časopis za upravljanje komuniciranjem
		special2010.put("0353-443X", "M51"); 			//				0353-443X	Ekonomika preduzeća
		special2010.put("0353-8648", "M51"); 			//				0353-8648	Ekonomske teme
		special2010.put("1451-3455", "M51"); 			//				1451-3455	Philotheos: International journal for philosophy and theology
		special2010.put("0353-5738", "M51"); 			//				0353-5738	Filozofija i društvo
		special2010.put("1820-6700", "M51"); 			//				1820-6700	Godišnjak Fakulteta političkih nauka
		special2010.put("1820-4589", "M51"); 			//				1820-4589	Kultura polisa
		special2010.put("0354-8635", "M51"); 			//				0354-8635	Management - časopis za teoriju i praksu menadžmenta
		special2010.put("0025-8555", "M51"); 			//				0025-8555	Međunarodni problemi
		special2010.put("1820-3159", "M51"); 			//				1820-3159	Megatrend revija
		special2010.put("1820-4996", "M51"); 			//				1820-4996	Nacionalni interes
		special2010.put("1450-7382", "M51"); 			//				1450-7382	Nova srpska politička misao
		special2010.put("0031-3807", "M51"); 			//				0031-3807	Pedagogija
		special2010.put("1451-4281", "M51"); 			//				1451-4281	Politička revija
		special2010.put("1820-6859", "M51"); 			//				1820-6859	Poslovna ekonomija
		special2010.put("0350-0500", "M51"); 			//				0350-0500	Pravni život
		special2010.put("0350-2538", "M51"); 			//				0350-2538	Psihijatrija danas
		special2010.put("0486-6096", "M51"); 			//				0486-6096	Review of International Affairs
		special2010.put("0564-7010", "M51"); 			//				0564-7010	Treći program
		special2010.put("0354-0243", "M51"); 			//				0354-0243	Yugoslav journal of operations research
		special2010.put("0550-2179", "M51"); 			//				0550-2179	Zbornik radova Pravnog fakulteta, Novi Sad
		special2010.put("0350-2120", "M52"); 			//				0350-2120	Anali Ekonomskog fakulteta u Subotici
		special2010.put("0354-8759", "M52"); 			//				0354-8759	Beogradska defektološka škola
		special2010.put("0409-2953", "M52"); 			//				0409-2953	Bezbednost
		special2010.put("1452-7405", "M52"); 			//				1452-7405	CM - časopis za upravljanje komuniciranjem
		special2010.put("1820-0214", "M52"); 			//				1820-0214	Computer Science and Information Systems / ComSIS
		special2010.put("1820-4244", "M52"); 			//				1820-4244	Demografija
		special2010.put("1451-3188", "M52"); 			//				1451-3188	Evropsko zakonodavstvo
		special2010.put("0354-5091", "M52"); 			//NEMA			0354-5091	Ekonomski signali
		special2010.put("1450-863X", "M52"); 			//				1450-863X	Ekonomski horizonti
		special2010.put("0354-4648", "M52"); 			//				0354-4648	Facta Universitatis: Series Philosophy, Sociology and Psychology
		special2010.put("1451-740X", "M52"); 			//				1451-740X	Facta Universitatis: Series Physical Education and Sport
		special2010.put("0015-2145", "M52"); 			//				0015-2145	Finansije
		special2010.put("0354-415x", "M52"); 			//				0354-415x	Gerontologija
		special2010.put("0374-0730", "M52"); 			//				0374-0730	Godišnjak Filozofskog fakulteta, Novi Sad
		special2010.put("1451-9739", "M52"); 			//				1451-9739	Godišnjak za sociologiju Filozofskog fakulteta u Nišu
		special2010.put("1451-4397", "M52"); 			//				1451-4397	Info M
		special2010.put("0352-2334", "M52"); 			//				0352-2334	Inovacije u nastavi - časopis za savremenu nastavu
		special2010.put("0354-3471", "M52"); 			//				0354-3471	Marketing
		special2010.put("0237-1995", "M52"); 			//				0237-1995	Marketing
		special2010.put("0543-3657", "M52"); 			//				0543-3657	Međunarodna politika
		special2010.put("0354-8872", "M52"); 			//				0354-8872	Nauka, bezbednost, policija
		special2010.put("0553-4569", "M52"); 			//				0553-4569	Pedagoška stvarnost
		special2010.put("0354-3501", "M52"); 			//				0354-3501	Pravo i privreda
		special2010.put("1450-6114", "M52"); 			//				1450-6114	Računovodstvo
		special2010.put("1820-2969", "M52"); 			//				1820-2969	Revija za kriminologiju i krivično pravo
		special2010.put("0353-7935", "M52"); 			//				0353-7935	Revizor
		special2010.put("1451-8759", "M52"); 			//				1451-8759	Religija i tolerancija
		special2010.put("1452-4864", "M52"); 			//				1452-4864	Serbian Journal of Management
		special2010.put("0354-401X", "M52"); 			//				0354-401X	Socijalna misao
		special2010.put("1452-7367", "M52"); 			//				1452-7367	Specijalna edukacija i rehabilitacija
		special2010.put("0039-2138", "M52"); 			//				0039-2138	Strani pravni život
		special2010.put("0354-8414", "M52"); 			//				0354-8414	Strategijski menadžment
		special2010.put("1450-9911", "M52"); 			//				1450-9911	Tehnika – Menadžment
		special2010.put("1450-6637", "M52"); 			//				1450-6637	Temida
		special2010.put("0042-8426", "M52"); 			//				0042-8426	Vojno delo
		special2010.put("0350-2694", "M52"); 			//				0350-2694	Zbornik Instituta za kriminološka i sociološka istraživanja
		special2010.put("0350-8501", "M52"); 			//				0350-8501	Zbornik radova Pravnog fakulteta, Niš
		special2010.put("1450-6718", "M52"); 			//				1450-6718	Zbornik radova Učiteljskog fakulteta, Užice
		special2010.put("0350-5928", "M53"); 			//				0350-5928	Agroekonomika
		special2010.put("0004-1270", "M53"); 			//NEMA			0004-1270	Arhiv za pravne i društvene nauke
		special2010.put("0006-5714", "M53"); 			//				0006-5714	Bogoslovlje
		special2010.put("1450-7196", "M53"); 			//				1450-7196	Communications in Dependability and Quality Management
		special2010.put("0350-137x", "M53"); 			//				0350-137x	Ekonomika
		special2010.put("0350-3828", "M53"); 			//				0350-3828	Fizička kultura
		special2010.put("0017-0933", "M53"); 			//				0017-0933	Glasnik Advokatske komore Vojvodine
		special2010.put("1452-5917", "M53"); 			//				1452-5917	Godišnjak Fakulteta sporta i fizičkog vaspitanja
		special2010.put("1451-5407", "M53"); 			//				1451-5407	Godišnjak za psihologiju
		special2010.put("1451-5822", "M53"); 			//				1451-5822	Hereticus
		special2010.put("1451-5113", "M53"); 			//				1451-5113	HOTEL link -časopis za teoriju i praksu hotelijerstva
		special2010.put("1820-9459", "M53"); 			//NEMA			1820-9459	Izazovi evropskih integracija
		special2010.put("2217-6985", "M53"); 			//NEMA			2217-6985	Lokalna samouprava
		special2010.put("1450-9407", "M53"); 			//				1450-9407	Obrazovna tehnologija
		special2010.put("0352-3713", "M53"); 			//				0352-3713	Pravo - teorija i praksa
		special2010.put("1821-0147", "M53"); 			//NEMA			1821-0147	Primenjena psihologija
		special2010.put("1820-8819", "M53"); 			//NEMA			1820-8819	Singidunum revija
		special2010.put("1451-7841", "M53"); 			//NEMA			1451-7841	Svet rada
		special2010.put("1452-0680", "M53"); 			//				1452-0680	Menadžment totalnim kvalitetom and izvrsnost
		special2010.put("0564-3619", "M53"); 			//				0564-3619	Tržište, novac, kapital
		special2010.put("0354-3099", "M53"); 			//NEMA			0354-3099	Turističko poslovanje
		this.specialJournalsAllYears.put(2010, special2010);
		
		//kreiranje liste casopisa u odredjenim godinama za maticne odbore 
		special2011 = new HashMap<String, String>();
		special2011.put("1820-0214", "M23"); 			//				1820-0214	Computer Science and Information Systems / ComSIS
		special2011.put("1452-595X", "M23"); 			//				1452-595X	Panoeconomicus
		special2011.put("0048-5705", "M23"); 			//				0048-5705	Psihologija
		special2011.put("0370-8179", "M23"); 			//				0370-8179	Srpski arhiv za celokupno lekarstvo
		special2011.put("1451-740X", "M24"); 			//				1451-740X	Facta Universitatis: Series Physical Education and Sport
		special2011.put("0351-2274", "M24"); 			//				0351-2274	Theoria
		special2011.put("0003-2565", "M24"); 			//				0003-2565	Anali Pravnog fakulteta u Beogradu
		special2011.put("0354-5415", "M24"); 			//				0354-5415	Andragoške studije
		special2011.put("0352-3462", "M24"); 			//				0352-3462	Ekonomika poljoprivrede
		special2011.put("0013-3264", "M24"); 			//				0013-3264	Economic Annals
		special2011.put("0579-6431", "M24"); 			//				0579-6431	Zbornik Instituta za pedagoška istraživanja
		special2011.put("0352-5732", "M24"); 			//				0352-5732	Zbornik Matice srpske za društvene nauke
		special2011.put("0350-0373", "M24"); 			//				0350-0373	Industrija
		special2011.put("0547-3330", "M24"); 			//				0547-3330	Nastava i vaspitanje
		special2011.put("1820-659X", "M24"); 			//				1820-659X	Politikologija religije
		special2011.put("0038-0318", "M24"); 			//				0038-0318	Sociologija
		special2011.put("0085-6320", "M24"); 			//				0085-6320	Sociološki pregled
		special2011.put("0354-5989", "M24"); 			//				0354-5989	Srpska politicka misao
		special2011.put("0038-982X", "M24"); 			//				0038-982X	Stanovnistvo
		special2011.put("0353-7919", "M24"); 			//				0353-7919	Teme
		special2011.put("1451-5040", "M24"); 			//				1451-5040	Filozofski godišnjak
		special2011.put("1820-0958", "M51"); 			//				1820-0958	Arhe
		special2011.put("0354-4699", "M51"); 			//				0354-4699	Facta Universitatis: Series Economics and Organization
		special2011.put("0354-8635", "M51"); 			//				0354-8635	Management - časopis za teoriju i praksu menadžmenta
		special2011.put("0354-3471", "M51"); 			//				0354-3471	Marketing
		special2011.put("0237-1995", "M51"); 			//				0237-1995	Marketing
		special2011.put("1451-3455", "M51"); 			//				1451-3455	Philotheos: International journal for philosophy and theology
		special2011.put("0486-6096", "M51"); 			//				0486-6096	Review of International Affairs
		special2011.put("1452-4864", "M51"); 			//				1452-4864	Serbian Journal of Management
		special2011.put("1450-6637", "M51"); 			//				1450-6637	Temida
		special2011.put("0354-0243", "M51"); 			//				0354-0243	Yugoslav journal of operations research
		special2011.put("0350-2120", "M51"); 			//				0350-2120	Anali Ekonomskog fakulteta u Subotici
		special2011.put("1820-6700", "M51"); 			//				1820-6700	Godišnjak Fakulteta političkih nauka
		special2011.put("0353-443X", "M51"); 			//				0353-443X	Ekonomika preduzeća
		special2011.put("0353-8648", "M51"); 			//				0353-8648	Ekonomske teme
		special2011.put("0550-2179", "M51"); 			//				0550-2179	Zbornik radova Pravnog fakulteta, Novi Sad
		special2011.put("1820-4589", "M51"); 			//				1820-4589	Kultura polisa
		special2011.put("1820-3159", "M51"); 			//				1820-3159	Megatrend revija
		special2011.put("0025-8105", "M51"); 			//				0025-8105	Medicinski pregled
		special2011.put("0025-8555", "M51"); 			//				0025-8555	Međunarodni problemi
		special2011.put("1820-4996", "M51"); 			//				1820-4996	Nacionalni interes
		special2011.put("1450-7382", "M51"); 			//				1450-7382	Nova srpska politička misao
		special2011.put("0031-3807", "M51"); 			//				0031-3807	Pedagogija
		special2011.put("1451-4281", "M51"); 			//				1451-4281	Politička revija
		special2011.put("1820-6859", "M51"); 			//				1820-6859	Poslovna ekonomija
		special2011.put("0350-0500", "M51"); 			//				0350-0500	Pravni život
		special2011.put("1452-7367", "M51"); 			//				1452-7367	Specijalna edukacija i rehabilitacija
		special2011.put("0039-2138", "M51"); 			//				0039-2138	Strani pravni život
		special2011.put("0564-7010", "M51"); 			//				0564-7010	Treći program
		special2011.put("0353-5738", "M51"); 			//				0353-5738	Filozofija i društvo
		special2011.put("1452-7405", "M52"); 			//				1452-7405	CM - časopis za upravljanje komuniciranjem
		special2011.put("1451-4397", "M52"); 			//				1451-4397	Info M
		special2011.put("1820-6301", "M52"); 			//				1820-6301	Serbian Journal of Sports Sciences
		special2011.put("1821-3448", "M52"); 			//NEMA			1821-3448	Strategic managment
		special2011.put("0350-5928", "M52"); 			//				0350-5928	Agroekonomika
		special2011.put("0409-2953", "M52"); 			//				0409-2953	Bezbednost
		special2011.put("0354-8759", "M52"); 			//				0354-8759	Beogradska defektološka škola
		special2011.put("0042-8426", "M52"); 			//				0042-8426	Vojno delo
		special2011.put("0354-415x", "M52"); 			//				0354-415x	Gerontologija
		special2011.put("1451-9739", "M52"); 			//				1451-9739	Godišnjak za sociologiju Filozofskog fakulteta u Nišu
		special2011.put("0374-0730", "M52"); 			//				0374-0730	Godišnjak Filozofskog fakulteta, Novi Sad
		special2011.put("1820-4244", "M52"); 			//				1820-4244	Demografija
		special2011.put("1451-3188", "M52"); 			//				1451-3188	Evropsko zakonodavstvo
		special2011.put("1450-863X", "M52"); 			//				1450-863X	Ekonomski horizonti
		special2011.put("0351-2665", "M52"); 			//				0351-2665	Engrami
		special2011.put("0350-2694", "M52"); 			//				0350-2694	Zbornik Instituta za kriminološka i sociološka istraživanja
		special2011.put("0350-8501", "M52"); 			//				0350-8501	Zbornik radova Pravnog fakulteta, Niš
		special2011.put("1450-6718", "M52"); 			//				1450-6718	Zbornik radova Učiteljskog fakulteta, Užice
		special2011.put("0352-2334", "M52"); 			//				0352-2334	Inovacije u nastavi - časopis za savremenu nastavu
		special2011.put("0543-3657", "M52"); 			//				0543-3657	Međunarodna politika
		special2011.put("0354-8872", "M52"); 			//				0354-8872	Nauka, bezbednost, policija
		special2011.put("0553-4569", "M52"); 			//				0553-4569	Pedagoška stvarnost
		special2011.put("0354-3501", "M52"); 			//				0354-3501	Pravo i privreda
		special2011.put("1821-0147", "M52"); 			//				1821-0147	Primenjena psihologija
		special2011.put("0352-7379", "M52"); 			//				0352-7379	Psihološka istraživanja
		special2011.put("1450-6114", "M52"); 			//				1450-6114	Računovodstvo
		special2011.put("0353-7935", "M52"); 			//				0353-7935	Revizor
		special2011.put("1820-2969", "M52"); 			//				1820-2969	Revija za kriminologiju i krivično pravo
		special2011.put("1451-8759", "M52"); 			//				1451-8759	Religija i tolerancija
		special2011.put("0354-401X", "M52"); 			//				0354-401X	Socijalna misao
		special2011.put("1450-9911", "M52"); 			//				1450-9911	Tehnika – Menadžment
		special2011.put("0015-2145", "M52"); 			//				0015-2145	Finansije
		special2011.put("1450-7196", "M53"); 			//				1450-7196	Communications in Dependability and Quality Management
		special2011.put("1821-3480", "M53"); 			//				1821-3480	Exercise and Quality of Life
		special2011.put("0354-4648", "M53"); 			//				0354-4648	Facta Universitatis: Series Philosophy, Sociology and Psychology
		special2011.put("1451-5822", "M53"); 			//				1451-5822	Hereticus
		special2011.put("1821-1283", "M53"); 			//				1821-1283	Journal of Women's Entrepreneurship and Education
		special2011.put("0354-9801", "M53"); 			//NEMA			0354-9801	Metodička praksa
		special2011.put("1452-6050", "M53"); 			//NEMA			1452-6050	Bezbednost Zapadnog Balkana
		special2011.put("1452-6115", "M53"); 			//NEMA			1452-6115	Western Balkans Security Observer
		special2011.put("0006-5714", "M53"); 			//				0006-5714	Bogoslovlje
		special2011.put("0353-9644", "M53"); 			//				0353-9644	Branič
		special2011.put("0017-0933", "M53"); 			//				0017-0933	Glasnik Advokatske komore Vojvodine
		special2011.put("1451-5407", "M53"); 			//				1451-5407	Godišnjak za psihologiju
		special2011.put("1820-5461", "M53"); 			//				1820-5461	Godišnjak srpske akademije obrazovanja
		special2011.put("1820-3396", "M53"); 			//NEMA			1820-3396	Godišnjak Učiteljskog fakulteta u Vranju
		special2011.put("1821-150X", "M53"); 			//				1821-150X	Godisnjak fakulteta bezbednosti
		special2011.put("1452-5917", "M53"); 			//				1452-5917	Godišnjak Fakulteta sporta i fizičkog vaspitanja
		special2011.put("1821-2573", "M53"); 			//				1821-2573	Economic analysis: Twice a Year Scientific Journal
		special2011.put("0350-137x", "M53"); 			//				0350-137x	Ekonomika
		special2011.put("0354-9135", "M53"); 			//				0354-9135	Ekonomski vidici
		special2011.put("1450-7951", "M53"); 			//				1450-7951	Ekonomski pogledi
		special2011.put("0354-5091", "M53"); 			//				0354-5091	Ekonomski signali
		special2011.put("1452-9343", "M53"); 			//				1452-9343	Zbornik radova Učiteljskog fakulteta Prizren - Leposavić
		special2011.put("0354-3293", "M53"); 			//				0354-3293	Zbornik radova Filozofskog fakulteta u Prištini
		special2011.put("1820-9459", "M53"); 			//				1820-9459	Izazovi evropskih integracija
		special2011.put("2217-6985", "M53"); 			//				2217-6985	Lokalna samouprava
		special2011.put("1450-9407", "M53"); 			//				1450-9407	Obrazovna tehnologija
		this.specialJournalsAllYears.put(2011, special2011);
	}

	public static MNODrustveneNauke MNODrustveneNauke= null;
	
	public static MNODrustveneNauke getMNODrustveneNauke() {
		if(MNODrustveneNauke==null)
			MNODrustveneNauke = new MNODrustveneNauke();
		return MNODrustveneNauke;
	}
}
