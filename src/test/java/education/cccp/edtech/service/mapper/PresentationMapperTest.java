package education.cccp.edtech.service.mapper;

import static education.cccp.edtech.domain.PresentationAsserts.*;
import static education.cccp.edtech.domain.PresentationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PresentationMapperTest {

    private PresentationMapper presentationMapper;

    @BeforeEach
    void setUp() {
        presentationMapper = new PresentationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPresentationSample1();
        var actual = presentationMapper.toEntity(presentationMapper.toDto(expected));
        assertPresentationAllPropertiesEquals(expected, actual);
    }
}
