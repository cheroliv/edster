package edtech.domain;

import static edtech.domain.AsciidocSlideTestSamples.*;
import static edtech.domain.PlantUMLDiagramResourceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import edtech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlantUMLDiagramResourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlantUMLDiagramResource.class);
        PlantUMLDiagramResource plantUMLDiagramResource1 = getPlantUMLDiagramResourceSample1();
        PlantUMLDiagramResource plantUMLDiagramResource2 = new PlantUMLDiagramResource();
        assertThat(plantUMLDiagramResource1).isNotEqualTo(plantUMLDiagramResource2);

        plantUMLDiagramResource2.setId(plantUMLDiagramResource1.getId());
        assertThat(plantUMLDiagramResource1).isEqualTo(plantUMLDiagramResource2);

        plantUMLDiagramResource2 = getPlantUMLDiagramResourceSample2();
        assertThat(plantUMLDiagramResource1).isNotEqualTo(plantUMLDiagramResource2);
    }

    @Test
    void asciidocSlideTest() {
        PlantUMLDiagramResource plantUMLDiagramResource = getPlantUMLDiagramResourceRandomSampleGenerator();
        AsciidocSlide asciidocSlideBack = getAsciidocSlideRandomSampleGenerator();

        plantUMLDiagramResource.setAsciidocSlide(asciidocSlideBack);
        assertThat(plantUMLDiagramResource.getAsciidocSlide()).isEqualTo(asciidocSlideBack);

        plantUMLDiagramResource.asciidocSlide(null);
        assertThat(plantUMLDiagramResource.getAsciidocSlide()).isNull();
    }
}
