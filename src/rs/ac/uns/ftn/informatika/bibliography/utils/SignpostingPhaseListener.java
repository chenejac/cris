package rs.ac.uns.ftn.informatika.bibliography.utils;

import rs.ac.uns.ftn.informatika.bibliography.dto.*;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileDTO;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.RelatedRecordsManagedBean;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SignpostingPhaseListener implements PhaseListener {


        public PhaseId getPhaseId()
        {
            return PhaseId.RENDER_RESPONSE;
        }

        public void afterPhase(PhaseEvent event)
        {
        }

        public void beforePhase(PhaseEvent event)
        {
            FacesContext facesContext = event.getFacesContext();
            ExternalContext extCtx = facesContext.getExternalContext();
            if (((HttpServletRequest)extCtx.getRequest()).getRequestURI().contains("/record.jsf")) {
                RelatedRecordsManagedBean mb = (RelatedRecordsManagedBean) extCtx.getRequestMap().get(
                        "reletedRecordsManagedBean");
                if (mb == null) {
                    mb = new RelatedRecordsManagedBean();
                    mb.loadRecord(event);
                }
                HttpServletResponse response = (HttpServletResponse) facesContext
                        .getExternalContext().getResponse();
                StringBuffer links = new StringBuffer();
                links.append("<https://schema.org/AboutPage> ; rel=\"type\" ");
                if (mb.getSelectedRecord() instanceof PublicationDTO) {
                    PublicationDTO publicationDTO = (PublicationDTO) mb.getSelectedRecord();
                    if (publicationDTO.getDoi() != null && publicationDTO.getDoi().trim().length() != 0) {
                        if (publicationDTO.getDoi().contains("https://doi.org/"))
                            links.append(", <" + publicationDTO.getDoi() + "> ; rel=\"describedby\" ; type=\"application/vnd.datacite.datacite+json\" ");
                        else
                            links.append(", <https://doi.org/" + publicationDTO.getDoi() + "> ; rel=\"describedby\" ; type=\"application/vnd.datacite.datacite+json\" ");
                    }
                    for (AuthorDTO authorDTO:publicationDTO.getAllAuthors()
                         ) {
                        links.append(", <" + ((HttpServletRequest)extCtx.getRequest()).getRequestURL() +"?recordId=" + authorDTO.getControlNumber().substring(7) + "> ; rel=\"author\" ");
                        if (authorDTO.getORCID() != null && authorDTO.getORCID().trim().length() != 0) {
                            links.append(", <" + authorDTO.getORCID() + "> ; rel=\"author\" ");
                        }
                    }
                    String license = getLicense(publicationDTO);
                    if (license != null){
                        links.append(", " + license + " ; rel=\"license\" ");
                    }

                    FileDTO file = publicationDTO.getFile();
                    if (file != null){
                        HttpServletRequest req = (HttpServletRequest)extCtx.getRequest();
                        String scheme = req.getScheme();
                        String serverName = req.getServerName();
                        int serverPort = req.getServerPort();
                        String contextPath = req.getContextPath();
                        String urlPrefix = scheme + "://" + serverName + ":" + serverPort + contextPath;
                        links.append(", <" + urlPrefix + "/DownloadFileServlet/Disertacija" + file.getFileName() + "?controlNumber=" + file.getControlNumber() + "&fileName=" + file.getFileName() + "&id=" + file.getId() + "&&licenseAccepted=true" + "> ; rel=\"item\" ");
                    }
                }
                if (mb.getSelectedRecord() instanceof StudyFinalDocumentDTO){
                    links.append(", <https://schema.org/Thesis> ; rel=\"type\" ");
                    links.append(", <" + ((HttpServletRequest)extCtx.getRequest()).getRequestURL() +"?recordId=" + ((StudyFinalDocumentDTO)mb.getSelectedRecord()).getInstitution().getControlNumber().substring(7) + "> ; rel=\"https://www.schema.org/sourceOrganization\" ");
                } else if (mb.getSelectedRecord() instanceof PaperJournalDTO || mb.getSelectedRecord() instanceof PaperProceedingsDTO){
                    links.append(", <https://schema.org/Article> ; rel=\"type\" ");
                } else if (mb.getSelectedRecord() instanceof MonographDTO){
                    links.append(", <https://schema.org/Book> ; rel=\"type\" ");
                } else if (mb.getSelectedRecord() instanceof PaperMonographDTO){
                    links.append(", <https://schema.org/Chapter> ; rel=\"type\" ");
                } else if (mb.getSelectedRecord() instanceof ProductDTO){
                    links.append(", <https://schema.org/Dataset> ; rel=\"type\" ");
                } else if (mb.getSelectedRecord() instanceof AuthorDTO){
                    links.append(", <https://schema.org/Person> ; rel=\"type\" ");
                    AuthorDTO authorDTO = (AuthorDTO) mb.getSelectedRecord();
                    if (authorDTO.getORCID() != null && authorDTO.getORCID().trim().length() != 0) {
                        links.append(", <" + authorDTO.getORCID() + "> ; rel=\"describedby\" ");
                    }
                    InstitutionDTO institutionDTO = authorDTO.getInstitution();
                    if (institutionDTO.getSuperInstitution() != null && institutionDTO.getSuperInstitution().getControlNumber() != null)
                        links.append(", <" + ((HttpServletRequest)extCtx.getRequest()).getRequestURL() +"?recordId=" + institutionDTO.getSuperInstitution().getControlNumber().substring(7) + "> ; rel=\"https://schema.org/affiliation\" ");
                } else if (mb.getSelectedRecord() instanceof InstitutionDTO) {
                        links.append(", <https://schema.org/Organization> ; rel=\"type\" ");
                        InstitutionDTO institutionDTO = (InstitutionDTO) mb.getSelectedRecord();
                        if (institutionDTO.getSuperInstitution() != null && institutionDTO.getSuperInstitution().getControlNumber() != null)
                            links.append(", <" + ((HttpServletRequest)extCtx.getRequest()).getRequestURL() +"?recordId=" + institutionDTO.getSuperInstitution().getControlNumber().substring(7) + "> ; rel=\"https://www.schema.org/parentOrganization\" ");
                }

                response.addHeader("Link", links.toString());
            }
        }

        private String getLicense(PublicationDTO publication){
            String license = null;
            String publicationLicense = publication.getFileLicense();
            if (publicationLicense != null && !("".equals(publicationLicense.trim()))){
                if ("Attribution".equals(publicationLicense)){
                    license="<https://spdx.org/licenses/CC-BY-4.0>";
                } else if ("Attribution-NoDerivs".equals(publicationLicense)){
                    license="<https://spdx.org/licenses/CC-BY-ND-4.0>";
                } else if ("Attribution-NonCommercial".equals(publicationLicense)){
                    license="<https://spdx.org/licenses/CC-BY-NC-4.0>";
                } else if ("Attribution-NonCommercial-NoDerivs".equals(publicationLicense)){
                    license="<https://spdx.org/licenses/CC-BY-NC-ND-4.0>";
                } else if ("Attribution-NonCommercial-ShareAlike".equals(publicationLicense)){
                    license="<https://spdx.org/licenses/CC-BY-NC-SA-4.0>";
                } else if ("Attribution-ShareAlike".equals(publicationLicense)){
                    license="<https://spdx.org/licenses/CC-BY-SA-4.0>";
                } else if ("Creative Commons".equals(publicationLicense)){
                    license="<https://spdx.org/licenses/CC-BY-NC-4.0>";
                }
            }
            return license;
        }
}
