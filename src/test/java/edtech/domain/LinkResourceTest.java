package edtech.domain;

import static edtech.domain.AsciidocSlideTestSamples.*;
import static edtech.domain.LinkResourceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import edtech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LinkResourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LinkResource.class);
        LinkResource linkResource1 = getLinkResourceSample1();
        LinkResource linkResource2 = new LinkResource();
        assertThat(linkResource1).isNotEqualTo(linkResource2);

        linkResource2.setId(linkResource1.getId());
        assertThat(linkResource1).isEqualTo(linkResource2);

        linkResource2 = getLinkResourceSample2();
        assertThat(linkResource1).isNotEqualTo(linkResource2);
    }

    @Test
    void asciidocSlideTest() {
        LinkResource linkResource = getLinkResourceRandomSampleGenerator();
        AsciidocSlide asciidocSlideBack = getAsciidocSlideRandomSampleGenerator();

        linkResource.setAsciidocSlide(asciidocSlideBack);
        assertThat(linkResource.getAsciidocSlide()).isEqualTo(asciidocSlideBack);

        linkResource.asciidocSlide(null);
        assertThat(linkResource.getAsciidocSlide()).isNull();
    }
}
