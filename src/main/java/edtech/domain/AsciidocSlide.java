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
 * A AsciidocSlide.
 */
@Table("asciidoc_slide")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AsciidocSlide implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("title")
    private String title;

    @Column("content")
    private String content;

    @Column("notes")
    private String notes;

    @Min(value = 1)
    @Column("num")
    private Integer num;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "workspace", "slides" }, allowSetters = true)
    private Presentation presentation;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "asciidocSlide" }, allowSetters = true)
    private Set<ImageResource> images = new HashSet<>();

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "asciidocSlide" }, allowSetters = true)
    private Set<LinkResource> links = new HashSet<>();

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "asciidocSlide" }, allowSetters = true)
    private Set<PlantUMLDiagramResource> diagrams = new HashSet<>();

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "asciidocSlide" }, allowSetters = true)
    private Set<QRCodeResource> qrcodes = new HashSet<>();

    @Column("presentation_id")
    private Long presentationId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AsciidocSlide id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public AsciidocSlide title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public AsciidocSlide content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNotes() {
        return this.notes;
    }

    public AsciidocSlide notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getNum() {
        return this.num;
    }

    public AsciidocSlide num(Integer num) {
        this.setNum(num);
        return this;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Presentation getPresentation() {
        return this.presentation;
    }

    public void setPresentation(Presentation presentation) {
        this.presentation = presentation;
        this.presentationId = presentation != null ? presentation.getId() : null;
    }

    public AsciidocSlide presentation(Presentation presentation) {
        this.setPresentation(presentation);
        return this;
    }

    public Set<ImageResource> getImages() {
        return this.images;
    }

    public void setImages(Set<ImageResource> imageResources) {
        if (this.images != null) {
            this.images.forEach(i -> i.setAsciidocSlide(null));
        }
        if (imageResources != null) {
            imageResources.forEach(i -> i.setAsciidocSlide(this));
        }
        this.images = imageResources;
    }

    public AsciidocSlide images(Set<ImageResource> imageResources) {
        this.setImages(imageResources);
        return this;
    }

    public AsciidocSlide addImages(ImageResource imageResource) {
        this.images.add(imageResource);
        imageResource.setAsciidocSlide(this);
        return this;
    }

    public AsciidocSlide removeImages(ImageResource imageResource) {
        this.images.remove(imageResource);
        imageResource.setAsciidocSlide(null);
        return this;
    }

    public Set<LinkResource> getLinks() {
        return this.links;
    }

    public void setLinks(Set<LinkResource> linkResources) {
        if (this.links != null) {
            this.links.forEach(i -> i.setAsciidocSlide(null));
        }
        if (linkResources != null) {
            linkResources.forEach(i -> i.setAsciidocSlide(this));
        }
        this.links = linkResources;
    }

    public AsciidocSlide links(Set<LinkResource> linkResources) {
        this.setLinks(linkResources);
        return this;
    }

    public AsciidocSlide addLinks(LinkResource linkResource) {
        this.links.add(linkResource);
        linkResource.setAsciidocSlide(this);
        return this;
    }

    public AsciidocSlide removeLinks(LinkResource linkResource) {
        this.links.remove(linkResource);
        linkResource.setAsciidocSlide(null);
        return this;
    }

    public Set<PlantUMLDiagramResource> getDiagrams() {
        return this.diagrams;
    }

    public void setDiagrams(Set<PlantUMLDiagramResource> plantUMLDiagramResources) {
        if (this.diagrams != null) {
            this.diagrams.forEach(i -> i.setAsciidocSlide(null));
        }
        if (plantUMLDiagramResources != null) {
            plantUMLDiagramResources.forEach(i -> i.setAsciidocSlide(this));
        }
        this.diagrams = plantUMLDiagramResources;
    }

    public AsciidocSlide diagrams(Set<PlantUMLDiagramResource> plantUMLDiagramResources) {
        this.setDiagrams(plantUMLDiagramResources);
        return this;
    }

    public AsciidocSlide addDiagrams(PlantUMLDiagramResource plantUMLDiagramResource) {
        this.diagrams.add(plantUMLDiagramResource);
        plantUMLDiagramResource.setAsciidocSlide(this);
        return this;
    }

    public AsciidocSlide removeDiagrams(PlantUMLDiagramResource plantUMLDiagramResource) {
        this.diagrams.remove(plantUMLDiagramResource);
        plantUMLDiagramResource.setAsciidocSlide(null);
        return this;
    }

    public Set<QRCodeResource> getQrcodes() {
        return this.qrcodes;
    }

    public void setQrcodes(Set<QRCodeResource> qRCodeResources) {
        if (this.qrcodes != null) {
            this.qrcodes.forEach(i -> i.setAsciidocSlide(null));
        }
        if (qRCodeResources != null) {
            qRCodeResources.forEach(i -> i.setAsciidocSlide(this));
        }
        this.qrcodes = qRCodeResources;
    }

    public AsciidocSlide qrcodes(Set<QRCodeResource> qRCodeResources) {
        this.setQrcodes(qRCodeResources);
        return this;
    }

    public AsciidocSlide addQrcodes(QRCodeResource qRCodeResource) {
        this.qrcodes.add(qRCodeResource);
        qRCodeResource.setAsciidocSlide(this);
        return this;
    }

    public AsciidocSlide removeQrcodes(QRCodeResource qRCodeResource) {
        this.qrcodes.remove(qRCodeResource);
        qRCodeResource.setAsciidocSlide(null);
        return this;
    }

    public Long getPresentationId() {
        return this.presentationId;
    }

    public void setPresentationId(Long presentation) {
        this.presentationId = presentation;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AsciidocSlide)) {
            return false;
        }
        return getId() != null && getId().equals(((AsciidocSlide) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AsciidocSlide{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", notes='" + getNotes() + "'" +
            ", num=" + getNum() +
            "}";
    }
}
