package edtech.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class QRCodeResourceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static QRCodeResource getQRCodeResourceSample1() {
        return new QRCodeResource().id(1L).uri("uri1").data("data1");
    }

    public static QRCodeResource getQRCodeResourceSample2() {
        return new QRCodeResource().id(2L).uri("uri2").data("data2");
    }

    public static QRCodeResource getQRCodeResourceRandomSampleGenerator() {
        return new QRCodeResource().id(longCount.incrementAndGet()).uri(UUID.randomUUID().toString()).data(UUID.randomUUID().toString());
    }
}
