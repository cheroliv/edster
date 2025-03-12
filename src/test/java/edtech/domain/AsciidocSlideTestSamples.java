package edtech.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AsciidocSlideTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AsciidocSlide getAsciidocSlideSample1() {
        return new AsciidocSlide().id(1L).title("title1").content("content1").notes("notes1").num(1);
    }

    public static AsciidocSlide getAsciidocSlideSample2() {
        return new AsciidocSlide().id(2L).title("title2").content("content2").notes("notes2").num(2);
    }

    public static AsciidocSlide getAsciidocSlideRandomSampleGenerator() {
        return new AsciidocSlide()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .content(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString())
            .num(intCount.incrementAndGet());
    }
}
