package edtech.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link edtech.domain.Workspace} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkspaceDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String name;

    private String description;

    private String promptSystemMessage;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPromptSystemMessage() {
        return promptSystemMessage;
    }

    public void setPromptSystemMessage(String promptSystemMessage) {
        this.promptSystemMessage = promptSystemMessage;
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
        if (!(o instanceof WorkspaceDTO)) {
            return false;
        }

        WorkspaceDTO workspaceDTO = (WorkspaceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workspaceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkspaceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", promptSystemMessage='" + getPromptSystemMessage() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
