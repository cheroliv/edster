package edtech.domain;

import static edtech.domain.PresentationTestSamples.*;
import static edtech.domain.WorkspaceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import edtech.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class WorkspaceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Workspace.class);
        Workspace workspace1 = getWorkspaceSample1();
        Workspace workspace2 = new Workspace();
        assertThat(workspace1).isNotEqualTo(workspace2);

        workspace2.setId(workspace1.getId());
        assertThat(workspace1).isEqualTo(workspace2);

        workspace2 = getWorkspaceSample2();
        assertThat(workspace1).isNotEqualTo(workspace2);
    }

    @Test
    void presentationsTest() {
        Workspace workspace = getWorkspaceRandomSampleGenerator();
        Presentation presentationBack = getPresentationRandomSampleGenerator();

        workspace.addPresentations(presentationBack);
        assertThat(workspace.getPresentations()).containsOnly(presentationBack);
        assertThat(presentationBack.getWorkspace()).isEqualTo(workspace);

        workspace.removePresentations(presentationBack);
        assertThat(workspace.getPresentations()).doesNotContain(presentationBack);
        assertThat(presentationBack.getWorkspace()).isNull();

        workspace.presentations(new HashSet<>(Set.of(presentationBack)));
        assertThat(workspace.getPresentations()).containsOnly(presentationBack);
        assertThat(presentationBack.getWorkspace()).isEqualTo(workspace);

        workspace.setPresentations(new HashSet<>());
        assertThat(workspace.getPresentations()).doesNotContain(presentationBack);
        assertThat(presentationBack.getWorkspace()).isNull();
    }
}
