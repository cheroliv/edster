package edtech.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ImageResourceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ImageResource getImageResourceSample1() {
        return new ImageResource().id(1L).uri("uri1").resolution("resolution1");
    }

    public static ImageResource getImageResourceSample2() {
        return new ImageResource().id(2L).uri("uri2").resolution("resolution2");
    }

    public static ImageResource getImageResourceRandomSampleGenerator() {
        return new ImageResource()
            .id(longCount.incrementAndGet())
            .uri(UUID.randomUUID().toString())
            .resolution(UUID.randomUUID().toString());
    }
}
