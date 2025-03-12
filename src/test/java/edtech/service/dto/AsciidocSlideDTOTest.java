package edtech.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import edtech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AsciidocSlideDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AsciidocSlideDTO.class);
        AsciidocSlideDTO asciidocSlideDTO1 = new AsciidocSlideDTO();
        asciidocSlideDTO1.setId(1L);
        AsciidocSlideDTO asciidocSlideDTO2 = new AsciidocSlideDTO();
        assertThat(asciidocSlideDTO1).isNotEqualTo(asciidocSlideDTO2);
        asciidocSlideDTO2.setId(asciidocSlideDTO1.getId());
        assertThat(asciidocSlideDTO1).isEqualTo(asciidocSlideDTO2);
        asciidocSlideDTO2.setId(2L);
        assertThat(asciidocSlideDTO1).isNotEqualTo(asciidocSlideDTO2);
        asciidocSlideDTO1.setId(null);
        assertThat(asciidocSlideDTO1).isNotEqualTo(asciidocSlideDTO2);
    }
}
