package edtech.service.dto;

import edtech.domain.enumeration.DocumentResourceType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link edtech.domain.LinkResource} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LinkResourceDTO implements Serializable {

    private Long id;

    private DocumentResourceType type;

    private String uri;

    private String target;

    private AsciidocSlideDTO asciidocSlide;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentResourceType getType() {
        return type;
    }

    public void setType(DocumentResourceType type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public AsciidocSlideDTO getAsciidocSlide() {
        return asciidocSlide;
    }

    public void setAsciidocSlide(AsciidocSlideDTO asciidocSlide) {
        this.asciidocSlide = asciidocSlide;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LinkResourceDTO)) {
            return false;
        }

        LinkResourceDTO linkResourceDTO = (LinkResourceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, linkResourceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LinkResourceDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", uri='" + getUri() + "'" +
            ", target='" + getTarget() + "'" +
            ", asciidocSlide=" + getAsciidocSlide() +
            "}";
    }
}
