package rs.ac.uns.ftn.informatika.bibliography.dto;

public class SearchInstitutionOrganizationTreeDTO {

	private InstitutionDTO institution;
	private OrganizationUnitDTO organization;
	private String type="";
	
	SearchInstitutionOrganizationTreeDTO(InstitutionDTO institution)
	{
		this.institution=institution;
		type = "InstitutionDTO";
		this.organization=null;
	}
	
	SearchInstitutionOrganizationTreeDTO(OrganizationUnitDTO organization)
	{
		this.organization=organization;
		type = "OrganizationUnitDTO";
		this.institution=null;
	}
	
	/**
	 * @return the institution
	 */
	public InstitutionDTO getInstitution() {
		return institution;
	}

	/**
	 * @param institution the institution to set
	 */
	public void setInstitution(InstitutionDTO institution) {
		this.institution = institution;
	}

	/**
	 * @return the organization
	 */
	public OrganizationUnitDTO getOrganization() {
		return organization;
	}

	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(OrganizationUnitDTO organization) {
		this.organization = organization;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		if (type.equals("InstitutionDTO"))
		{
			return institution.toString();
		}
		else
		{
			return organization.toString();
		}	
	}
	
	@Override
	public boolean equals(Object obj) {
		if (type.equals("InstitutionDTO"))
		{
			return institution.equals(obj);
		}
		else
		{
			return organization.equals(obj);
		}
	}

}
