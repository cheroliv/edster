package edtech.domain;

import static edtech.domain.AsciidocSlideTestSamples.*;
import static edtech.domain.ImageResourceTestSamples.*;
import static edtech.domain.LinkResourceTestSamples.*;
import static edtech.domain.PlantUMLDiagramResourceTestSamples.*;
import static edtech.domain.PresentationTestSamples.*;
import static edtech.domain.QRCodeResourceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import edtech.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AsciidocSlideTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AsciidocSlide.class);
        AsciidocSlide asciidocSlide1 = getAsciidocSlideSample1();
        AsciidocSlide asciidocSlide2 = new AsciidocSlide();
        assertThat(asciidocSlide1).isNotEqualTo(asciidocSlide2);

        asciidocSlide2.setId(asciidocSlide1.getId());
        assertThat(asciidocSlide1).isEqualTo(asciidocSlide2);

        asciidocSlide2 = getAsciidocSlideSample2();
        assertThat(asciidocSlide1).isNotEqualTo(asciidocSlide2);
    }

    @Test
    void presentationTest() {
        AsciidocSlide asciidocSlide = getAsciidocSlideRandomSampleGenerator();
        Presentation presentationBack = getPresentationRandomSampleGenerator();

        asciidocSlide.setPresentation(presentationBack);
        assertThat(asciidocSlide.getPresentation()).isEqualTo(presentationBack);

        asciidocSlide.presentation(null);
        assertThat(asciidocSlide.getPresentation()).isNull();
    }

    @Test
    void imagesTest() {
        AsciidocSlide asciidocSlide = getAsciidocSlideRandomSampleGenerator();
        ImageResource imageResourceBack = getImageResourceRandomSampleGenerator();

        asciidocSlide.addImages(imageResourceBack);
        assertThat(asciidocSlide.getImages()).containsOnly(imageResourceBack);
        assertThat(imageResourceBack.getAsciidocSlide()).isEqualTo(asciidocSlide);

        asciidocSlide.removeImages(imageResourceBack);
        assertThat(asciidocSlide.getImages()).doesNotContain(imageResourceBack);
        assertThat(imageResourceBack.getAsciidocSlide()).isNull();

        asciidocSlide.images(new HashSet<>(Set.of(imageResourceBack)));
        assertThat(asciidocSlide.getImages()).containsOnly(imageResourceBack);
        assertThat(imageResourceBack.getAsciidocSlide()).isEqualTo(asciidocSlide);

        asciidocSlide.setImages(new HashSet<>());
        assertThat(asciidocSlide.getImages()).doesNotContain(imageResourceBack);
        assertThat(imageResourceBack.getAsciidocSlide()).isNull();
    }

    @Test
    void linksTest() {
        AsciidocSlide asciidocSlide = getAsciidocSlideRandomSampleGenerator();
        LinkResource linkResourceBack = getLinkResourceRandomSampleGenerator();

        asciidocSlide.addLinks(linkResourceBack);
        assertThat(asciidocSlide.getLinks()).containsOnly(linkResourceBack);
        assertThat(linkResourceBack.getAsciidocSlide()).isEqualTo(asciidocSlide);

        asciidocSlide.removeLinks(linkResourceBack);
        assertThat(asciidocSlide.getLinks()).doesNotContain(linkResourceBack);
        assertThat(linkResourceBack.getAsciidocSlide()).isNull();

        asciidocSlide.links(new HashSet<>(Set.of(linkResourceBack)));
        assertThat(asciidocSlide.getLinks()).containsOnly(linkResourceBack);
        assertThat(linkResourceBack.getAsciidocSlide()).isEqualTo(asciidocSlide);

        asciidocSlide.setLinks(new HashSet<>());
        assertThat(asciidocSlide.getLinks()).doesNotContain(linkResourceBack);
        assertThat(linkResourceBack.getAsciidocSlide()).isNull();
    }

    @Test
    void diagramsTest() {
        AsciidocSlide asciidocSlide = getAsciidocSlideRandomSampleGenerator();
        PlantUMLDiagramResource plantUMLDiagramResourceBack = getPlantUMLDiagramResourceRandomSampleGenerator();

        asciidocSlide.addDiagrams(plantUMLDiagramResourceBack);
        assertThat(asciidocSlide.getDiagrams()).containsOnly(plantUMLDiagramResourceBack);
        assertThat(plantUMLDiagramResourceBack.getAsciidocSlide()).isEqualTo(asciidocSlide);

        asciidocSlide.removeDiagrams(plantUMLDiagramResourceBack);
        assertThat(asciidocSlide.getDiagrams()).doesNotContain(plantUMLDiagramResourceBack);
        assertThat(plantUMLDiagramResourceBack.getAsciidocSlide()).isNull();

        asciidocSlide.diagrams(new HashSet<>(Set.of(plantUMLDiagramResourceBack)));
        assertThat(asciidocSlide.getDiagrams()).containsOnly(plantUMLDiagramResourceBack);
        assertThat(plantUMLDiagramResourceBack.getAsciidocSlide()).isEqualTo(asciidocSlide);

        asciidocSlide.setDiagrams(new HashSet<>());
        assertThat(asciidocSlide.getDiagrams()).doesNotContain(plantUMLDiagramResourceBack);
        assertThat(plantUMLDiagramResourceBack.getAsciidocSlide()).isNull();
    }

    @Test
    void qrcodesTest() {
        AsciidocSlide asciidocSlide = getAsciidocSlideRandomSampleGenerator();
        QRCodeResource qRCodeResourceBack = getQRCodeResourceRandomSampleGenerator();

        asciidocSlide.addQrcodes(qRCodeResourceBack);
        assertThat(asciidocSlide.getQrcodes()).containsOnly(qRCodeResourceBack);
        assertThat(qRCodeResourceBack.getAsciidocSlide()).isEqualTo(asciidocSlide);

        asciidocSlide.removeQrcodes(qRCodeResourceBack);
        assertThat(asciidocSlide.getQrcodes()).doesNotContain(qRCodeResourceBack);
        assertThat(qRCodeResourceBack.getAsciidocSlide()).isNull();

        asciidocSlide.qrcodes(new HashSet<>(Set.of(qRCodeResourceBack)));
        assertThat(asciidocSlide.getQrcodes()).containsOnly(qRCodeResourceBack);
        assertThat(qRCodeResourceBack.getAsciidocSlide()).isEqualTo(asciidocSlide);

        asciidocSlide.setQrcodes(new HashSet<>());
        assertThat(asciidocSlide.getQrcodes()).doesNotContain(qRCodeResourceBack);
        assertThat(qRCodeResourceBack.getAsciidocSlide()).isNull();
    }
}
