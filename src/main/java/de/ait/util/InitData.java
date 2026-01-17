package de.ait.util;

import de.ait.model.Car;
import de.ait.repository.CarRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class InitData {

    @Bean
    CommandLineRunner initDatabase(CarRepository carRepository) {
        return args -> {
            if (carRepository.count() == 0) {
                carRepository.saveAll(List.of(
                        new Car("BMW", "X5", 2000, 30000, 35000, "AVAILABLE", "Black", 250, "PETROL", "AUTOMATIC"),
                        new Car("Audi", "A4", 2025, 2000, 25000, "SOLD", "White", 200, "PETROL", "AUTOMATIC"),
                        new Car("BMW", "320i", 2016, 85000, 18500, "AVAILABLE", "Blue", 180, "DIESEL", "MANUAL"),
                        new Car("Audi", "A6", 2020, 50000, 22000, "AVAILABLE", "Gray", 220, "PETROL", "AUTOMATIC"),
                        new Car("Mercedes", "C200", 2018, 40000, 27000, "AVAILABLE", "Silver", 190, "PETROL", "AUTOMATIC"),
                        new Car("Toyota", "Camry", 2019, 60000, 23000, "SOLD", "Red", 178, "HYBRID", "AUTOMATIC"),
                        new Car("Honda", "Civic", 2021, 15000, 21000, "AVAILABLE", "Black", 158, "PETROL", "MANUAL"),
                        new Car("Tesla", "Model 3", 2022, 10000, 40000, "AVAILABLE", "White", 283, "ELECTRIC", "AUTOMATIC"),
                        new Car("BMW", "M3", 2021, 10000, 55000, "AVAILABLE", "Blue", 473, "PETROL", "MANUAL"),
                        new Car("Audi", "Q5", 2019, 30000, 28000, "SOLD", "Gray", 248, "PETROL", "AUTOMATIC"),
                        new Car("Toyota", "Corolla", 2018, 70000, 16000, "AVAILABLE", "Silver", 132, "PETROL", "MANUAL"),
                        new Car("Toyota", "RAV4", 2020, 25000, 27000, "AVAILABLE", "Green", 203, "HYBRID", "AUTOMATIC")
                ));
            }
        };
    }
}
