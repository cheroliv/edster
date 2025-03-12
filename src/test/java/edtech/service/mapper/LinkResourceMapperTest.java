package edtech.service.mapper;

import static edtech.domain.LinkResourceAsserts.*;
import static edtech.domain.LinkResourceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkResourceMapperTest {

    private LinkResourceMapper linkResourceMapper;

    @BeforeEach
    void setUp() {
        linkResourceMapper = new LinkResourceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLinkResourceSample1();
        var actual = linkResourceMapper.toEntity(linkResourceMapper.toDto(expected));
        assertLinkResourceAllPropertiesEquals(expected, actual);
    }
}
