package rs.ac.uns.ftn.informatika.bibliography.evaluation.wosImport;

public class ISIJournal {
	
	private String category;
	private String year; // year for which the data are related
	private String list; // se ili sse
	
	private String abbvTitle;
	private String title;
	private String issn;
	private String eIssn;
	private String totalCites;
	private Double impactFactor;
	private Double impactFactor5;
	private String immediacyIndex;
	private String articles;
	private String citedHalfLife;
	private String eigenfactorScore;
	private String articleInfluenceScore;
	
	private int redniBrojIF2;
	private int redniBrojIF5;
	private int ukupnoUKategorijiIF2;
	private int ukupnoUKategorijiIF5;	
	
	
	
	
	/**
	 * 
	 */
	public ISIJournal() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * @param category
	 * @param year
	 * @param abbvTitle
	 * @param issn
	 * @param eIssn
	 * @param totalCites
	 * @param impactFactor
	 * @param impactFactor5
	 * @param immediacyIndex
	 * @param articles
	 * @param citedHalfLife
	 * @param eigenfactorScore
	 * @param articleInfluenceScore
	 */
	public ISIJournal(String category, String year, String list, String abbvTitle, String title, String issn, String eIssn,
			String totalCites, Double impactFactor, Double impactFactor5,
			String immediacyIndex, String articles, String citedHalfLife,
			String eigenfactorScore, String articleInfluenceScore) {
		super();
		this.category = category;
		this.year = year;
		this.list = list;
		this.abbvTitle = abbvTitle;
		this.title = title;
		this.issn = issn;
		this.eIssn = eIssn;
		this.totalCites = totalCites;
		this.impactFactor = impactFactor;
		this.impactFactor5 = impactFactor5;
		this.immediacyIndex = immediacyIndex;
		this.articles = articles;
		this.citedHalfLife = citedHalfLife;
		this.eigenfactorScore = eigenfactorScore;
		this.articleInfluenceScore = articleInfluenceScore;
	}


	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}
	
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}


	/**
	 * @return the abbvTitle
	 */
	public String getAbbvTitle() {
		return abbvTitle;
	}
	/**
	 * @param abbvTitle the abbvTitle to set
	 */
	public void setAbbvTitle(String abbvTitle) {
		this.abbvTitle = abbvTitle;
	}
	/**
	 * @return the issn
	 */
	public String getIssn() {
		return issn;
	}
	/**
	 * @param issn the issn to set
	 */
	public void setIssn(String issn) {
		this.issn = issn;
	}

	public String geteIssn() {
		return eIssn;
	}

	public void seteIssn(String eIssn) {
		this.eIssn = eIssn;
	}

	/**
	 * @return the totalCites
	 */
	public String getTotalCites() {
		return totalCites;
	}
	/**
	 * @param totalCites the totalCites to set
	 */
	public void setTotalCites(String totalCites) {
		this.totalCites = totalCites;
	}
	/**
	 * @return the impactFactor
	 */
	public Double getImpactFactor() {
		return impactFactor;
	}
	/**
	 * @param impactFactor the impactFactor to set
	 */
	public void setImpactFactor(Double impactFactor) {
		this.impactFactor = impactFactor;
	}
	/**
	 * @return the impactFactor5
	 */
	public Double getImpactFactor5() {
		return impactFactor5;
	}
	/**
	 * @param impactFactor5 the impactFactor5 to set
	 */
	public void setImpactFactor5(Double impactFactor5) {
		this.impactFactor5 = impactFactor5;
	}
	/**
	 * @return the immediacyIndex
	 */
	public String getImmediacyIndex() {
		return immediacyIndex;
	}
	/**
	 * @param immediacyIndex the immediacyIndex to set
	 */
	public void setImmediacyIndex(String immediacyIndex) {
		this.immediacyIndex = immediacyIndex;
	}
	/**
	 * @return the articles
	 */
	public String getArticles() {
		return articles;
	}
	/**
	 * @param articles the articles to set
	 */
	public void setArticles(String articles) {
		this.articles = articles;
	}
	/**
	 * @return the citedHalfLife
	 */
	public String getCitedHalfLife() {
		return citedHalfLife;
	}
	/**
	 * @param citedHalfLife the citedHalfLife to set
	 */
	public void setCitedHalfLife(String citedHalfLife) {
		this.citedHalfLife = citedHalfLife;
	}
	/**
	 * @return the eigenfactorScore
	 */
	public String getEigenfactorScore() {
		return eigenfactorScore;
	}
	/**
	 * @param eigenfactorScore the eigenfactorScore to set
	 */
	public void setEigenfactorScore(String eigenfactorScore) {
		this.eigenfactorScore = eigenfactorScore;
	}
	/**
	 * @return the articleInfluenceScore
	 */
	public String getArticleInfluenceScore() {
		return articleInfluenceScore;
	}
	/**
	 * @param articleInfluenceScore the articleInfluenceScore to set
	 */
	public void setArticleInfluenceScore(String articleInfluenceScore) {
		this.articleInfluenceScore = articleInfluenceScore;
	}


	/**
	 * @return the list
	 */
	public String getList() {
		return list;
	}


	/**
	 * @param list the list to set
	 */
	public void setList(String list) {
		this.list = list;
	}


	/**
	 * @return the redniBrojIF2
	 */
	public int getRedniBrojIF2() {
		return redniBrojIF2;
	}


	/**
	 * @param redniBrojIF2 the redniBrojIF2 to set
	 */
	public void setRedniBrojIF2(int redniBrojIF2) {
		this.redniBrojIF2 = redniBrojIF2;
	}


	/**
	 * @return the redniBrojIF5
	 */
	public int getRedniBrojIF5() {
		return redniBrojIF5;
	}


	/**
	 * @param redniBrojIF5 the redniBrojIF5 to set
	 */
	public void setRedniBrojIF5(int redniBrojIF5) {
		this.redniBrojIF5 = redniBrojIF5;
	}


	/**
	 * @return the ukupnoUKategorijiIF2
	 */
	public int getUkupnoUKategorijiIF2() {
		return ukupnoUKategorijiIF2;
	}


	/**
	 * @param ukupnoUKategorijiIF2 the ukupnoUKategorijiIF2 to set
	 */
	public void setUkupnoUKategorijiIF2(int ukupnoUKategorijiIF2) {
		this.ukupnoUKategorijiIF2 = ukupnoUKategorijiIF2;
	}


	/**
	 * @return the ukupnoUKategorijiIF5
	 */
	public int getUkupnoUKategorijiIF5() {
		return ukupnoUKategorijiIF5;
	}


	/**
	 * @param ukupnoUKategorijiIF5 the ukupnoUKategorijiIF5 to set
	 */
	public void setUkupnoUKategorijiIF5(int ukupnoUKategorijiIF5) {
		this.ukupnoUKategorijiIF5 = ukupnoUKategorijiIF5;
	}
	

}
