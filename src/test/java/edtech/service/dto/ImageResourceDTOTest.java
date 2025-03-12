package edtech.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import edtech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageResourceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageResourceDTO.class);
        ImageResourceDTO imageResourceDTO1 = new ImageResourceDTO();
        imageResourceDTO1.setId(1L);
        ImageResourceDTO imageResourceDTO2 = new ImageResourceDTO();
        assertThat(imageResourceDTO1).isNotEqualTo(imageResourceDTO2);
        imageResourceDTO2.setId(imageResourceDTO1.getId());
        assertThat(imageResourceDTO1).isEqualTo(imageResourceDTO2);
        imageResourceDTO2.setId(2L);
        assertThat(imageResourceDTO1).isNotEqualTo(imageResourceDTO2);
        imageResourceDTO1.setId(null);
        assertThat(imageResourceDTO1).isNotEqualTo(imageResourceDTO2);
    }
}
