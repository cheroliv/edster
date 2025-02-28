package education.cccp.edtech.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PresentationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Presentation getPresentationSample1() {
        return new Presentation().id(1L).name("name1").plan("plan1").uri("uri1");
    }

    public static Presentation getPresentationSample2() {
        return new Presentation().id(2L).name("name2").plan("plan2").uri("uri2");
    }

    public static Presentation getPresentationRandomSampleGenerator() {
        return new Presentation()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .plan(UUID.randomUUID().toString())
            .uri(UUID.randomUUID().toString());
    }
}
