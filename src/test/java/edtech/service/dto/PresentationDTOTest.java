package edtech.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import edtech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PresentationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PresentationDTO.class);
        PresentationDTO presentationDTO1 = new PresentationDTO();
        presentationDTO1.setId(1L);
        PresentationDTO presentationDTO2 = new PresentationDTO();
        assertThat(presentationDTO1).isNotEqualTo(presentationDTO2);
        presentationDTO2.setId(presentationDTO1.getId());
        assertThat(presentationDTO1).isEqualTo(presentationDTO2);
        presentationDTO2.setId(2L);
        assertThat(presentationDTO1).isNotEqualTo(presentationDTO2);
        presentationDTO1.setId(null);
        assertThat(presentationDTO1).isNotEqualTo(presentationDTO2);
    }
}
