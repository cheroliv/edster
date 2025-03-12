package edtech.service.mapper;

import static edtech.domain.QRCodeResourceAsserts.*;
import static edtech.domain.QRCodeResourceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QRCodeResourceMapperTest {

    private QRCodeResourceMapper qRCodeResourceMapper;

    @BeforeEach
    void setUp() {
        qRCodeResourceMapper = new QRCodeResourceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getQRCodeResourceSample1();
        var actual = qRCodeResourceMapper.toEntity(qRCodeResourceMapper.toDto(expected));
        assertQRCodeResourceAllPropertiesEquals(expected, actual);
    }
}
