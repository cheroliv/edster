package edtech.domain;

import static edtech.domain.AsciidocSlideTestSamples.*;
import static edtech.domain.QRCodeResourceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import edtech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QRCodeResourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QRCodeResource.class);
        QRCodeResource qRCodeResource1 = getQRCodeResourceSample1();
        QRCodeResource qRCodeResource2 = new QRCodeResource();
        assertThat(qRCodeResource1).isNotEqualTo(qRCodeResource2);

        qRCodeResource2.setId(qRCodeResource1.getId());
        assertThat(qRCodeResource1).isEqualTo(qRCodeResource2);

        qRCodeResource2 = getQRCodeResourceSample2();
        assertThat(qRCodeResource1).isNotEqualTo(qRCodeResource2);
    }

    @Test
    void asciidocSlideTest() {
        QRCodeResource qRCodeResource = getQRCodeResourceRandomSampleGenerator();
        AsciidocSlide asciidocSlideBack = getAsciidocSlideRandomSampleGenerator();

        qRCodeResource.setAsciidocSlide(asciidocSlideBack);
        assertThat(qRCodeResource.getAsciidocSlide()).isEqualTo(asciidocSlideBack);

        qRCodeResource.asciidocSlide(null);
        assertThat(qRCodeResource.getAsciidocSlide()).isNull();
    }
}
