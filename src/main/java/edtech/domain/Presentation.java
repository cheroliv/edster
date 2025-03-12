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
 * A Presentation.
 */
@Table("presentation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Presentation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("plan")
    private String plan;

    @NotNull(message = "must not be null")
    @Column("uri")
    private String uri;

    @Column("prompt_user_message")
    private String promptUserMessage;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "user", "presentations" }, allowSetters = true)
    private Workspace workspace;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "presentation", "images", "links", "diagrams", "qrcodes" }, allowSetters = true)
    private Set<AsciidocSlide> slides = new HashSet<>();

    @Column("workspace_id")
    private Long workspaceId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Presentation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlan() {
        return this.plan;
    }

    public Presentation plan(String plan) {
        this.setPlan(plan);
        return this;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getUri() {
        return this.uri;
    }

    public Presentation uri(String uri) {
        this.setUri(uri);
        return this;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPromptUserMessage() {
        return this.promptUserMessage;
    }

    public Presentation promptUserMessage(String promptUserMessage) {
        this.setPromptUserMessage(promptUserMessage);
        return this;
    }

    public void setPromptUserMessage(String promptUserMessage) {
        this.promptUserMessage = promptUserMessage;
    }

    public Workspace getWorkspace() {
        return this.workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
        this.workspaceId = workspace != null ? workspace.getId() : null;
    }

    public Presentation workspace(Workspace workspace) {
        this.setWorkspace(workspace);
        return this;
    }

    public Set<AsciidocSlide> getSlides() {
        return this.slides;
    }

    public void setSlides(Set<AsciidocSlide> asciidocSlides) {
        if (this.slides != null) {
            this.slides.forEach(i -> i.setPresentation(null));
        }
        if (asciidocSlides != null) {
            asciidocSlides.forEach(i -> i.setPresentation(this));
        }
        this.slides = asciidocSlides;
    }

    public Presentation slides(Set<AsciidocSlide> asciidocSlides) {
        this.setSlides(asciidocSlides);
        return this;
    }

    public Presentation addSlides(AsciidocSlide asciidocSlide) {
        this.slides.add(asciidocSlide);
        asciidocSlide.setPresentation(this);
        return this;
    }

    public Presentation removeSlides(AsciidocSlide asciidocSlide) {
        this.slides.remove(asciidocSlide);
        asciidocSlide.setPresentation(null);
        return this;
    }

    public Long getWorkspaceId() {
        return this.workspaceId;
    }

    public void setWorkspaceId(Long workspace) {
        this.workspaceId = workspace;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Presentation)) {
            return false;
        }
        return getId() != null && getId().equals(((Presentation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Presentation{" +
            "id=" + getId() +
            ", plan='" + getPlan() + "'" +
            ", uri='" + getUri() + "'" +
            ", promptUserMessage='" + getPromptUserMessage() + "'" +
            "}";
    }
}
