package edtech.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import edtech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlantUMLDiagramResourceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlantUMLDiagramResourceDTO.class);
        PlantUMLDiagramResourceDTO plantUMLDiagramResourceDTO1 = new PlantUMLDiagramResourceDTO();
        plantUMLDiagramResourceDTO1.setId(1L);
        PlantUMLDiagramResourceDTO plantUMLDiagramResourceDTO2 = new PlantUMLDiagramResourceDTO();
        assertThat(plantUMLDiagramResourceDTO1).isNotEqualTo(plantUMLDiagramResourceDTO2);
        plantUMLDiagramResourceDTO2.setId(plantUMLDiagramResourceDTO1.getId());
        assertThat(plantUMLDiagramResourceDTO1).isEqualTo(plantUMLDiagramResourceDTO2);
        plantUMLDiagramResourceDTO2.setId(2L);
        assertThat(plantUMLDiagramResourceDTO1).isNotEqualTo(plantUMLDiagramResourceDTO2);
        plantUMLDiagramResourceDTO1.setId(null);
        assertThat(plantUMLDiagramResourceDTO1).isNotEqualTo(plantUMLDiagramResourceDTO2);
    }
}
