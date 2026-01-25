package de.ait.controllers;

import de.ait.model.Car;
import de.ait.repository.CarRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class TestProfileLoadsCarsFrom008IT {
    @Autowired
    private CarRepository carRepository;

    @Test
    @DisplayName("Seed-данные из 008 загружаются ТОЛЬКО в test-профиле")
    void seedDataShouldBeLoadedInTestProfile() {
        List<Car> cars = carRepository.findAll();

        assertThat(cars).isNotEmpty();

        assertThat(cars)
                .extracting(Car::getBrand)
                .contains("Toyota", "Volkswagen");
    }
}
