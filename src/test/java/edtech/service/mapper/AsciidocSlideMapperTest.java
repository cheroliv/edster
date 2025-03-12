package edtech.service.mapper;

import static edtech.domain.AsciidocSlideAsserts.*;
import static edtech.domain.AsciidocSlideTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AsciidocSlideMapperTest {

    private AsciidocSlideMapper asciidocSlideMapper;

    @BeforeEach
    void setUp() {
        asciidocSlideMapper = new AsciidocSlideMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAsciidocSlideSample1();
        var actual = asciidocSlideMapper.toEntity(asciidocSlideMapper.toDto(expected));
        assertAsciidocSlideAllPropertiesEquals(expected, actual);
    }
}
