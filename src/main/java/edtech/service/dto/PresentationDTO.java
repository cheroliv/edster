package edtech.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link edtech.domain.Presentation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PresentationDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String plan;

    @NotNull(message = "must not be null")
    private String uri;

    private String promptUserMessage;

    private WorkspaceDTO workspace;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPromptUserMessage() {
        return promptUserMessage;
    }

    public void setPromptUserMessage(String promptUserMessage) {
        this.promptUserMessage = promptUserMessage;
    }

    public WorkspaceDTO getWorkspace() {
        return workspace;
    }

    public void setWorkspace(WorkspaceDTO workspace) {
        this.workspace = workspace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PresentationDTO)) {
            return false;
        }

        PresentationDTO presentationDTO = (PresentationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, presentationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PresentationDTO{" +
            "id=" + getId() +
            ", plan='" + getPlan() + "'" +
            ", uri='" + getUri() + "'" +
            ", promptUserMessage='" + getPromptUserMessage() + "'" +
            ", workspace=" + getWorkspace() +
            "}";
    }
}
