package edtech.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Workspace.
 */
@Table("workspace")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Workspace implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("prompt_system_message")
    private String promptSystemMessage;

    @org.springframework.data.annotation.Transient
    private User user;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "workspace", "slides" }, allowSetters = true)
    private Set<Presentation> presentations = new HashSet<>();

    @Column("user_id")
    private Long userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Workspace id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Workspace name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Workspace description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPromptSystemMessage() {
        return this.promptSystemMessage;
    }

    public Workspace promptSystemMessage(String promptSystemMessage) {
        this.setPromptSystemMessage(promptSystemMessage);
        return this;
    }

    public void setPromptSystemMessage(String promptSystemMessage) {
        this.promptSystemMessage = promptSystemMessage;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }

    public Workspace user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Presentation> getPresentations() {
        return this.presentations;
    }

    public void setPresentations(Set<Presentation> presentations) {
        if (this.presentations != null) {
            this.presentations.forEach(i -> i.setWorkspace(null));
        }
        if (presentations != null) {
            presentations.forEach(i -> i.setWorkspace(this));
        }
        this.presentations = presentations;
    }

    public Workspace presentations(Set<Presentation> presentations) {
        this.setPresentations(presentations);
        return this;
    }

    public Workspace addPresentations(Presentation presentation) {
        this.presentations.add(presentation);
        presentation.setWorkspace(this);
        return this;
    }

    public Workspace removePresentations(Presentation presentation) {
        this.presentations.remove(presentation);
        presentation.setWorkspace(null);
        return this;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long user) {
        this.userId = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Workspace)) {
            return false;
        }
        return getId() != null && getId().equals(((Workspace) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Workspace{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", promptSystemMessage='" + getPromptSystemMessage() + "'" +
            "}";
    }
}
