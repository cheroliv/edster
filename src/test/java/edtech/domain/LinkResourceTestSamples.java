package edtech.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LinkResourceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static LinkResource getLinkResourceSample1() {
        return new LinkResource().id(1L).uri("uri1").target("target1");
    }

    public static LinkResource getLinkResourceSample2() {
        return new LinkResource().id(2L).uri("uri2").target("target2");
    }

    public static LinkResource getLinkResourceRandomSampleGenerator() {
        return new LinkResource().id(longCount.incrementAndGet()).uri(UUID.randomUUID().toString()).target(UUID.randomUUID().toString());
    }
}
