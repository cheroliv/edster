package edtech.service.mapper;

import static edtech.domain.PlantUMLDiagramResourceAsserts.*;
import static edtech.domain.PlantUMLDiagramResourceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlantUMLDiagramResourceMapperTest {

    private PlantUMLDiagramResourceMapper plantUMLDiagramResourceMapper;

    @BeforeEach
    void setUp() {
        plantUMLDiagramResourceMapper = new PlantUMLDiagramResourceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPlantUMLDiagramResourceSample1();
        var actual = plantUMLDiagramResourceMapper.toEntity(plantUMLDiagramResourceMapper.toDto(expected));
        assertPlantUMLDiagramResourceAllPropertiesEquals(expected, actual);
    }
}
