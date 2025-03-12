package edtech.domain;

import static edtech.domain.AsciidocSlideTestSamples.*;
import static edtech.domain.ImageResourceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import edtech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageResourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageResource.class);
        ImageResource imageResource1 = getImageResourceSample1();
        ImageResource imageResource2 = new ImageResource();
        assertThat(imageResource1).isNotEqualTo(imageResource2);

        imageResource2.setId(imageResource1.getId());
        assertThat(imageResource1).isEqualTo(imageResource2);

        imageResource2 = getImageResourceSample2();
        assertThat(imageResource1).isNotEqualTo(imageResource2);
    }

    @Test
    void asciidocSlideTest() {
        ImageResource imageResource = getImageResourceRandomSampleGenerator();
        AsciidocSlide asciidocSlideBack = getAsciidocSlideRandomSampleGenerator();

        imageResource.setAsciidocSlide(asciidocSlideBack);
        assertThat(imageResource.getAsciidocSlide()).isEqualTo(asciidocSlideBack);

        imageResource.asciidocSlide(null);
        assertThat(imageResource.getAsciidocSlide()).isNull();
    }
}
