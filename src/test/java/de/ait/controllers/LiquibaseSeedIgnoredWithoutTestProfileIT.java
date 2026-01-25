package de.ait.controllers;

import de.ait.model.Car;
import de.ait.repository.CarRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LiquibaseSeedIgnoredWithoutTestProfileIT {
    @Nested
    @SpringBootTest
    class SeedNotLoadedWithoutTestProfileIT {

        @Autowired
        private CarRepository carRepository;

        @Test
        @DisplayName("Seed-данные из 008 НЕ загружаются без профиля test")
        void seedDataShouldNotBeLoadedWithoutTestProfile() {
            List<Car> cars = carRepository.findAll();

            assertThat(cars)
                    .noneMatch(car ->
                            "Toyota".equals(car.getBrand()) ||
                                    "Volkswagen".equals(car.getBrand())
                    );
        }
    }
}
