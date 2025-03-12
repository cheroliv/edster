package edtech.domain;

import static edtech.domain.AsciidocSlideTestSamples.*;
import static edtech.domain.PresentationTestSamples.*;
import static edtech.domain.WorkspaceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import edtech.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
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

    @Test
    void workspaceTest() {
        Presentation presentation = getPresentationRandomSampleGenerator();
        Workspace workspaceBack = getWorkspaceRandomSampleGenerator();

        presentation.setWorkspace(workspaceBack);
        assertThat(presentation.getWorkspace()).isEqualTo(workspaceBack);

        presentation.workspace(null);
        assertThat(presentation.getWorkspace()).isNull();
    }

    @Test
    void slidesTest() {
        Presentation presentation = getPresentationRandomSampleGenerator();
        AsciidocSlide asciidocSlideBack = getAsciidocSlideRandomSampleGenerator();

        presentation.addSlides(asciidocSlideBack);
        assertThat(presentation.getSlides()).containsOnly(asciidocSlideBack);
        assertThat(asciidocSlideBack.getPresentation()).isEqualTo(presentation);

        presentation.removeSlides(asciidocSlideBack);
        assertThat(presentation.getSlides()).doesNotContain(asciidocSlideBack);
        assertThat(asciidocSlideBack.getPresentation()).isNull();

        presentation.slides(new HashSet<>(Set.of(asciidocSlideBack)));
        assertThat(presentation.getSlides()).containsOnly(asciidocSlideBack);
        assertThat(asciidocSlideBack.getPresentation()).isEqualTo(presentation);

        presentation.setSlides(new HashSet<>());
        assertThat(presentation.getSlides()).doesNotContain(asciidocSlideBack);
        assertThat(asciidocSlideBack.getPresentation()).isNull();
    }
}
