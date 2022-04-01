package geo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;

import java.util.stream.Stream;

public class GeoServiceImplTests {
    GeoServiceImpl sut;

    @BeforeEach
    public void initEach() {
        System.out.println("Тест для метода класса GeoServiceImpl запущен");
    }

    @AfterEach
    public void finishEach() {
        System.out.println("Тест для метода класса GeoServiceImpl завершен");
    }

    @ParameterizedTest
    @MethodSource("sourceByIp")
    void testByIp(String ip, Location expected) {
        sut = new GeoServiceImpl();

        Location result = sut.byIp(ip);

        Assertions.assertEquals(expected, result);
    }

    private static Stream<Arguments> sourceByIp() {
        return Stream.of(Arguments.of("96.54",
                new Location("New York", Country.USA, null,  0)),
                Arguments.of(GeoServiceImpl.NEW_YORK_IP,
                new Location("New York", Country.USA, " 10th Avenue", 32)),
                Arguments.of("172.2",
                        new Location("Moscow", Country.RUSSIA, null,  0)),
                Arguments.of("172",null));
    }

    @Test
    void testBy() {
        Assertions.assertThrows(RuntimeException.class, () -> sut.byCoordinates(12.3, 4));
    }
}
