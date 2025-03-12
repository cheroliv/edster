package edtech.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edtech.domain.enumeration.DocumentResourceType;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PlantUMLDiagramResource.
 */
@Table("plant_uml_diagram_resource")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlantUMLDiagramResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("type")
    private DocumentResourceType type;

    @Column("uri")
    private String uri;

    @Column("uml_code")
    private String umlCode;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "presentation", "images", "links", "diagrams", "qrcodes" }, allowSetters = true)
    private AsciidocSlide asciidocSlide;

    @Column("asciidoc_slide_id")
    private Long asciidocSlideId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PlantUMLDiagramResource id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentResourceType getType() {
        return this.type;
    }

    public PlantUMLDiagramResource type(DocumentResourceType type) {
        this.setType(type);
        return this;
    }

    public void setType(DocumentResourceType type) {
        this.type = type;
    }

    public String getUri() {
        return this.uri;
    }

    public PlantUMLDiagramResource uri(String uri) {
        this.setUri(uri);
        return this;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUmlCode() {
        return this.umlCode;
    }

    public PlantUMLDiagramResource umlCode(String umlCode) {
        this.setUmlCode(umlCode);
        return this;
    }

    public void setUmlCode(String umlCode) {
        this.umlCode = umlCode;
    }

    public AsciidocSlide getAsciidocSlide() {
        return this.asciidocSlide;
    }

    public void setAsciidocSlide(AsciidocSlide asciidocSlide) {
        this.asciidocSlide = asciidocSlide;
        this.asciidocSlideId = asciidocSlide != null ? asciidocSlide.getId() : null;
    }

    public PlantUMLDiagramResource asciidocSlide(AsciidocSlide asciidocSlide) {
        this.setAsciidocSlide(asciidocSlide);
        return this;
    }

    public Long getAsciidocSlideId() {
        return this.asciidocSlideId;
    }

    public void setAsciidocSlideId(Long asciidocSlide) {
        this.asciidocSlideId = asciidocSlide;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlantUMLDiagramResource)) {
            return false;
        }
        return getId() != null && getId().equals(((PlantUMLDiagramResource) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlantUMLDiagramResource{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", uri='" + getUri() + "'" +
            ", umlCode='" + getUmlCode() + "'" +
            "}";
    }
}
