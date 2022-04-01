package sender;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MessageSenderImplTests {
    MessageSenderImpl sut;

    @BeforeEach
    public void initEach() {
        System.out.println("Тест для метода класса MessageSenderImpl запущен");
    }

    @AfterEach
    public void finishEach() {
        System.out.println("Тест для метода класса MessageSenderImpl завершен");
    }

    @ParameterizedTest
    @MethodSource("sourceSend")
    void testSend(Location location,Country country,
                  String greeting, String key, String value, String expected) {
        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp(Mockito.anyString()))
                .thenReturn(location);

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(country))
                .thenReturn(greeting);

        sut = new MessageSenderImpl(geoService, localizationService);
        Map<String, String> headers = new HashMap<>();
        headers.put(key, value);

        String result = sut.send(headers);
        System.out.println();//для переноса предложений

        Assertions.assertEquals(expected, result);
    }

    private static Stream<Arguments> sourceSend() {
        return Stream.of(
                (Arguments.of(new Location("Moscow", Country.RUSSIA, "Lenina", 15),
                        Country.RUSSIA, "Добро пожаловать",
                        MessageSenderImpl.IP_ADDRESS_HEADER, GeoServiceImpl.MOSCOW_IP,
                        "Добро пожаловать")),
                (Arguments.of(new Location("New York", Country.USA, " 10th Avenue", 32),
                        Country.USA, "Welcome",
                        MessageSenderImpl.IP_ADDRESS_HEADER, GeoServiceImpl.NEW_YORK_IP,
                        "Welcome")),
                (Arguments.of(new Location("New York", Country.USA, null,  0),
                        Country.USA, "Welcome",
                        MessageSenderImpl.IP_ADDRESS_HEADER, "95.56",
                        "Welcome")),
                (Arguments.of(new Location(null, Country.USA, "Lenina", 15),
                        Country.USA, "Welcome",
                        MessageSenderImpl.IP_ADDRESS_HEADER, "96.890",
                        "Welcome")),
                (Arguments.of(null,
                        null, null,
                        MessageSenderImpl.IP_ADDRESS_HEADER, "96.890",
                        null)));
    }

    @Test
    void testSendExc() {
        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp(Mockito.anyString()))
                .thenReturn(null);

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Mockito.any(Country.class)))
                .thenReturn("Добро пожаловать");

        sut = new MessageSenderImpl(geoService, localizationService);

        Assertions.assertThrows(NullPointerException.class, ()-> sut.send(new HashMap<>()));
    }
}
