package edtech.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import edtech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LinkResourceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LinkResourceDTO.class);
        LinkResourceDTO linkResourceDTO1 = new LinkResourceDTO();
        linkResourceDTO1.setId(1L);
        LinkResourceDTO linkResourceDTO2 = new LinkResourceDTO();
        assertThat(linkResourceDTO1).isNotEqualTo(linkResourceDTO2);
        linkResourceDTO2.setId(linkResourceDTO1.getId());
        assertThat(linkResourceDTO1).isEqualTo(linkResourceDTO2);
        linkResourceDTO2.setId(2L);
        assertThat(linkResourceDTO1).isNotEqualTo(linkResourceDTO2);
        linkResourceDTO1.setId(null);
        assertThat(linkResourceDTO1).isNotEqualTo(linkResourceDTO2);
    }
}
