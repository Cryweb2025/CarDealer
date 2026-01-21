package de.ait.controllers;

import de.ait.enums.FuelType;
import de.ait.model.Car;
import de.ait.repository.CarRepository;
import de.ait.validation.Validator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Tag(name = "Car management API")
@RestController
@RequestMapping("/api/cars")
@Slf4j
public class CarController {

    private final CarRepository carRepository;
    private final Validator<Car> carValidator;

    public CarController(CarRepository carRepository, Validator<Car> carValidator) {
        this.carRepository = carRepository;
        this.carValidator = carValidator;
    }

    @Value("${app.dealership.name:AIT Gr.59 API}")
    private String dealerShipName;

    @GetMapping("/info")
    public ResponseEntity<String> getInfo() {
        return ResponseEntity.ok("Welcome to the " + dealerShipName + " car dealership!");
    }

    // ---------- GET ----------

    @Operation(summary = "Get all cars")
    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carRepository.findAll());
    }

    @Operation(summary = "Get car by id")
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return carRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ---------- POST ----------

    @Operation(summary = "Add a new car")
    @PostMapping
    public ResponseEntity<Object> addCar(@RequestBody Car car) {

        List<String> errors = carValidator.validateWithErrors(car);

        if (!errors.isEmpty()) {
            log.warn("Car validation with ID: {} is failed! | Errors: {}", car.getId(), errors);
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", errors));
        }

        Car savedCar = carRepository.save(car);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedCar.getId());
    }

    // ---------- PUT ----------

    @Operation(summary = "Update one car by id")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCar(@PathVariable Long id, @RequestBody Car car) {

        if (!carRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        List<String> errors = carValidator.validateWithErrors(car);

        if (!errors.isEmpty()) {
            log.warn("Car update validation failed for id {} | Errors: {}", id, errors);
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", errors));
        }

        car.setId(id);
        Car updatedCar = carRepository.save(car);
        return ResponseEntity.ok(updatedCar);
    }

    // ---------- DELETE ----------

    @Operation(summary = "Delete a car by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {

        if (!carRepository.existsById(id)) {
            log.warn("Car with id {} does not exist", id);
            return ResponseEntity.notFound().build();
        }

        carRepository.deleteById(id);
        log.warn("Car with id {} has been deleted", id);
        return ResponseEntity.noContent().build();
    }

    // ---------- SEARCH ----------

    @Operation(summary = "Search cars by brand")
    @GetMapping("/search")
    public ResponseEntity<List<Car>> searchCars(@RequestParam String brand) {
        return ResponseEntity.ok(carRepository.findByBrand(brand));
    }

    @Operation(summary = "Search car by price range")
    @GetMapping("/by-price")
    public ResponseEntity<List<Car>> searchByPriceBetween(
            @RequestParam int min,
            @RequestParam int max) {

        if (min < 0 || max < 0 || min > max) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(carRepository.findByPriceBetween(min, max));
    }

    @Operation(summary = "Search car by color")
    @GetMapping("/by-color")
    public ResponseEntity<List<Car>> searchByColor(@RequestParam String color) {

        if (color == null || color.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(carRepository.findByColorIgnoreCase(color));
    }

    @Operation(summary = "Search car by fuel type")
    @GetMapping("/by-fuel")
    public ResponseEntity<List<Car>> searchByFuelType(@RequestParam FuelType fuelType) {

        List<Car> cars = carRepository.findByFuelType(fuelType);

        if (cars.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cars);
    }

    @Operation(summary = "Search cars by horsepower range")
    @GetMapping("/by-power")
    public ResponseEntity<List<Car>> searchByHorsePower(
            @RequestParam int minHp,
            @RequestParam int maxHp) {

        if (minHp < 0 || maxHp < 0 || minHp > maxHp) {
            return ResponseEntity.badRequest().build();
        }

        List<Car> cars = carRepository.findByHorsepowerBetween(minHp, maxHp);

        if (cars.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cars);
    }
}
