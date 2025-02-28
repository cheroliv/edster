package education.cccp.edtech.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link education.cccp.edtech.domain.Presentation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PresentationDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String name;

    private String plan;

    private String uri;

    @NotNull
    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
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
            ", name='" + getName() + "'" +
            ", plan='" + getPlan() + "'" +
            ", uri='" + getUri() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
