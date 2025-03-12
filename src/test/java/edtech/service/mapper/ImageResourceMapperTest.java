package edtech.service.mapper;

import static edtech.domain.ImageResourceAsserts.*;
import static edtech.domain.ImageResourceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImageResourceMapperTest {

    private ImageResourceMapper imageResourceMapper;

    @BeforeEach
    void setUp() {
        imageResourceMapper = new ImageResourceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getImageResourceSample1();
        var actual = imageResourceMapper.toEntity(imageResourceMapper.toDto(expected));
        assertImageResourceAllPropertiesEquals(expected, actual);
    }
}
