package edtech.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edtech.domain.enumeration.DocumentResourceType;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ImageResource.
 */
@Table("image_resource")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("type")
    private DocumentResourceType type;

    @Column("uri")
    private String uri;

    @Column("resolution")
    private String resolution;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "presentation", "images", "links", "diagrams", "qrcodes" }, allowSetters = true)
    private AsciidocSlide asciidocSlide;

    @Column("asciidoc_slide_id")
    private Long asciidocSlideId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ImageResource id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentResourceType getType() {
        return this.type;
    }

    public ImageResource type(DocumentResourceType type) {
        this.setType(type);
        return this;
    }

    public void setType(DocumentResourceType type) {
        this.type = type;
    }

    public String getUri() {
        return this.uri;
    }

    public ImageResource uri(String uri) {
        this.setUri(uri);
        return this;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getResolution() {
        return this.resolution;
    }

    public ImageResource resolution(String resolution) {
        this.setResolution(resolution);
        return this;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public AsciidocSlide getAsciidocSlide() {
        return this.asciidocSlide;
    }

    public void setAsciidocSlide(AsciidocSlide asciidocSlide) {
        this.asciidocSlide = asciidocSlide;
        this.asciidocSlideId = asciidocSlide != null ? asciidocSlide.getId() : null;
    }

    public ImageResource asciidocSlide(AsciidocSlide asciidocSlide) {
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
        if (!(o instanceof ImageResource)) {
            return false;
        }
        return getId() != null && getId().equals(((ImageResource) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageResource{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", uri='" + getUri() + "'" +
            ", resolution='" + getResolution() + "'" +
            "}";
    }
}
