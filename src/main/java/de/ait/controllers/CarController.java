package de.ait.controllers;

import de.ait.enums.FuelType;
import de.ait.model.Car;
import de.ait.repository.CarRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Car management API")
@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarRepository carRepository;

    private final List<Car> allCars = new ArrayList<>(List.of(
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

    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Value("${app.dealership.name:AIT Gr.59 API}")
    private String dealerShipName;

    @GetMapping("/info")
    public ResponseEntity<String> getInfo() {
        return ResponseEntity.ok("Welcome to the " + dealerShipName + " car dealership!");
    }

    @Operation(summary = "Get all cars")
    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        if (!carRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ofNullable(carRepository.findById(id).orElse(null));
    }

    @Operation(summary = "Delete a car by id")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteCar(@PathVariable Long id) {
        if (!carRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        carRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    //api/cars/search?brand=BMW
    @Operation(summary = "Search a car by brand")
    @GetMapping("/search")
    public ResponseEntity<List<Car>> searchCars(@RequestParam String brand) {
        return ResponseEntity.ok(carRepository.findByBrand(brand));
    }


    @Operation(summary = "Add a new car")
    @PostMapping
    public ResponseEntity<Long> addCar(@RequestBody Car car) {
        Car savedCar = carRepository.save(car);
        if (savedCar == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity(HttpStatusCode.valueOf(201));
    }

    @Operation(summary = "Update one car by id")
    @PutMapping("/{id}")
    public ResponseEntity updateCar(@PathVariable Long id, @RequestBody Car car) {
        if (carRepository.existsById(id)) {
            Car carToUpdate = carRepository.findById(id).orElse(null);
            carToUpdate.setBrand(car.getBrand());
            carToUpdate.setModel(car.getModel());
            carToUpdate.setProductionYear(car.getProductionYear());
            carToUpdate.setMileage(car.getMileage());
            carToUpdate.setPrice(car.getPrice());
            carToUpdate.setStatus(car.getStatus());
            carRepository.save(carToUpdate);
            return ResponseEntity.ok("updated car with id = " + id);
        }
        return ResponseEntity.notFound().build();
    }

    // GET /api/cars/by-price?min=10000&max=20000
    @Operation(summary = "Search car by price between")
    @GetMapping("/by-price")
    public ResponseEntity<List<Car>> searchByPriceBetween(
            @RequestParam int min, @RequestParam int max
    ) {
        return ResponseEntity.ok(carRepository.findByPriceBetween(min, max));
    }

    // GET /api/cars/by-color?color=black
    @Operation(summary = "Search a car by color")
    @GetMapping("/by-color")
    public ResponseEntity<List<Car>> searchByColor(@RequestParam String color){
        if(color == null || color.isEmpty()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(carRepository.findByColorIgnoreCase(color));
    }

    // GET /api/cars/by-fuel?fuelType=DIESEL
    @Operation(summary = "Search a car by fuel type")
    @GetMapping("/by-fuel")
    public ResponseEntity<List<Car>> searchByFuelType(@RequestParam FuelType fuelType) {
        if (fuelType == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Car> cars = carRepository.findByFuelType(fuelType);
        if (cars.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cars);
    }

    // GET /api/cars/by-power?minHp=150&maxHp=300
    @Operation(summary = "Search cars with horsepower between given range")
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
