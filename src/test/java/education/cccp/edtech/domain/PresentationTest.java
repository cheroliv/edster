package education.cccp.edtech.domain;

import static education.cccp.edtech.domain.PresentationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import education.cccp.edtech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PresentationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Presentation.class);
        Presentation presentation1 = getPresentationSample1();
        Presentation presentation2 = new Presentation();
        assertThat(presentation1).isNotEqualTo(presentation2);

        presentation2.setId(presentation1.getId());
        assertThat(presentation1).isEqualTo(presentation2);

        presentation2 = getPresentationSample2();
        assertThat(presentation1).isNotEqualTo(presentation2);
    }
}
