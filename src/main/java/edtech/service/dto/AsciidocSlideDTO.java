package edtech.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link edtech.domain.AsciidocSlide} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AsciidocSlideDTO implements Serializable {

    private Long id;

    private String title;

    private String content;

    private String notes;

    @Min(value = 1)
    private Integer num;

    private PresentationDTO presentation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public PresentationDTO getPresentation() {
        return presentation;
    }

    public void setPresentation(PresentationDTO presentation) {
        this.presentation = presentation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AsciidocSlideDTO)) {
            return false;
        }

        AsciidocSlideDTO asciidocSlideDTO = (AsciidocSlideDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, asciidocSlideDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AsciidocSlideDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", notes='" + getNotes() + "'" +
            ", num=" + getNum() +
            ", presentation=" + getPresentation() +
            "}";
    }
}
