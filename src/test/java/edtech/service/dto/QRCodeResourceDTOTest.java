package edtech.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import edtech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QRCodeResourceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QRCodeResourceDTO.class);
        QRCodeResourceDTO qRCodeResourceDTO1 = new QRCodeResourceDTO();
        qRCodeResourceDTO1.setId(1L);
        QRCodeResourceDTO qRCodeResourceDTO2 = new QRCodeResourceDTO();
        assertThat(qRCodeResourceDTO1).isNotEqualTo(qRCodeResourceDTO2);
        qRCodeResourceDTO2.setId(qRCodeResourceDTO1.getId());
        assertThat(qRCodeResourceDTO1).isEqualTo(qRCodeResourceDTO2);
        qRCodeResourceDTO2.setId(2L);
        assertThat(qRCodeResourceDTO1).isNotEqualTo(qRCodeResourceDTO2);
        qRCodeResourceDTO1.setId(null);
        assertThat(qRCodeResourceDTO1).isNotEqualTo(qRCodeResourceDTO2);
    }
}
