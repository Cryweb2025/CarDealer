package de.ait.repository;

import de.ait.enums.CarStatus;
import de.ait.enums.FuelType;
import de.ait.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {

    //SELECT * FROM CARS WHERE brand = ? SQL запрос

    List<Car> findByBrand(String brand);

    List<Car> findCarByStatus(CarStatus status);

    boolean existsCarById(@NotNull  Long id);

    List<Car> findByPriceBetween(int min, int max);

    List<Car> findByColorIgnoreCase(String color);

    List<Car> findByFuelType(FuelType fuelType);

    List<Car> findByHorsepowerBetween(int minHp, int maxHp);

    Optional<Car> findById(Long id);

}
