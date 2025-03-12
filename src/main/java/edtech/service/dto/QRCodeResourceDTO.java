package edtech.service.dto;

import edtech.domain.enumeration.DocumentResourceType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link edtech.domain.QRCodeResource} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QRCodeResourceDTO implements Serializable {

    private Long id;

    private DocumentResourceType type;

    private String uri;

    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
        if (!(o instanceof QRCodeResourceDTO)) {
            return false;
        }

        QRCodeResourceDTO qRCodeResourceDTO = (QRCodeResourceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, qRCodeResourceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QRCodeResourceDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", uri='" + getUri() + "'" +
            ", data='" + getData() + "'" +
            ", asciidocSlide=" + getAsciidocSlide() +
            "}";
    }
}
