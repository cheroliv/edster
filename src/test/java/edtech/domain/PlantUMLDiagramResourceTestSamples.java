package edtech.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PlantUMLDiagramResourceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PlantUMLDiagramResource getPlantUMLDiagramResourceSample1() {
        return new PlantUMLDiagramResource().id(1L).uri("uri1").umlCode("umlCode1");
    }

    public static PlantUMLDiagramResource getPlantUMLDiagramResourceSample2() {
        return new PlantUMLDiagramResource().id(2L).uri("uri2").umlCode("umlCode2");
    }

    public static PlantUMLDiagramResource getPlantUMLDiagramResourceRandomSampleGenerator() {
        return new PlantUMLDiagramResource()
            .id(longCount.incrementAndGet())
            .uri(UUID.randomUUID().toString())
            .umlCode(UUID.randomUUID().toString());
    }
}
