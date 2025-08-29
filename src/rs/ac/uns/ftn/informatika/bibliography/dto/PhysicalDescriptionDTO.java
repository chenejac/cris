/**
 *
 */
package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.Serializable;

/**
 * @author chenejac@uns.ac.rs
 *
 */
@SuppressWarnings("serial")
public class PhysicalDescriptionDTO implements Serializable{

	private Integer numberOfChapters;
	private Integer numberOfPages;
	private Integer numberOfReferences;
	private Integer numberOfTables;
	private Integer numberOfPictures;
	private Integer numberOfGraphs;
	private Integer numberOfAppendixes;

	/**
	 *
	 */
	public PhysicalDescriptionDTO() {
		super();
	}

	/**
	 * @return the numberOfChapters
	 */
	public Integer getNumberOfChapters() {
		return numberOfChapters;
	}

	/**
	 * @param numberOfChapters the numberOfChapters to set
	 */
	public void setNumberOfChapters(Integer numberOfChapters) {
		this.numberOfChapters = numberOfChapters;
	}

	/**
	 * @return the numberOfPages
	 */
	public Integer getNumberOfPages() {
		return numberOfPages;
	}

	/**
	 * @param numberOfPages the numberOfPages to set
	 */
	public void setNumberOfPages(Integer numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	/**
	 * @return the numberOfReferences
	 */
	public Integer getNumberOfReferences() {
		return numberOfReferences;
	}

	/**
	 * @param numberOfReferences the numberOfReferences to set
	 */
	public void setNumberOfReferences(Integer numberOfReferences) {
		this.numberOfReferences = numberOfReferences;
	}

	/**
	 * @return the numberOfTables
	 */
	public Integer getNumberOfTables() {
		return numberOfTables;
	}

	/**
	 * @param numberOfTables the numberOfTables to set
	 */
	public void setNumberOfTables(Integer numberOfTables) {
		this.numberOfTables = numberOfTables;
	}

	/**
	 * @return the numberOfPictures
	 */
	public Integer getNumberOfPictures() {
		return numberOfPictures;
	}

	/**
	 * @param numberOfPictures the numberOfPictures to set
	 */
	public void setNumberOfPictures(Integer numberOfPictures) {
		this.numberOfPictures = numberOfPictures;
	}

	/**
	 * @return the numberOfGraphs
	 */
	public Integer getNumberOfGraphs() {
		return numberOfGraphs;
	}

	/**
	 * @param numberOfGraphs the numberOfGraphs to set
	 */
	public void setNumberOfGraphs(Integer numberOfGraphs) {
		this.numberOfGraphs = numberOfGraphs;
	}

	/**
	 * @return the numberOfAppendixes
	 */
	public Integer getNumberOfAppendixes() {
		return numberOfAppendixes;
	}

	/**
	 * @param numberOfAppendixes the numberOfAppendixes to set
	 */
	public void setNumberOfAppendixes(Integer numberOfAppendixes) {
		this.numberOfAppendixes = numberOfAppendixes;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ""+ numberOfPages + "/"
				+ numberOfChapters + "/"
				+ numberOfReferences + "/"
				+ numberOfTables + "/"
				+ numberOfPictures + "/"
				+ numberOfGraphs + "/"
				+ numberOfAppendixes;
	}



}
