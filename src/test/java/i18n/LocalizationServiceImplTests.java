package i18n;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.i18n.LocalizationServiceImpl;

import java.util.stream.Stream;

public class LocalizationServiceImplTests {
    LocalizationServiceImpl sut;

    @BeforeEach
    public void initEach() {
        System.out.println("Тест для метода класса LocalizationServiceImpl запущен");
    }

    @AfterEach
    public void finishEach() {
        System.out.println("Тест для метода класса LocalizationServiceImpl завершен");
    }

    @ParameterizedTest
    @MethodSource("sourceLocale")
    void testLocale(Country country, String expected) {
        sut = new LocalizationServiceImpl();

        String result = sut.locale(country);

        Assertions.assertEquals(result, expected);
    }

    private static Stream<Arguments> sourceLocale() {
        return Stream.of(Arguments.of(Country.USA, "Welcome"),
                Arguments.of(Country.RUSSIA, "Добро пожаловать"),
                Arguments.of(Country.BRAZIL, "Welcome"),
                Arguments.of(Country.GERMANY, "Welcome"));
    }
}
