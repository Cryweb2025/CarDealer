package de.ait.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ait.enums.CarStatus;
import de.ait.enums.FuelType;
import de.ait.enums.Transmission;
import de.ait.model.Car;
import de.ait.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CarControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        carRepository.deleteAll();
    }

    private Car buildValidCar(String brand, String model) {
        Car car = new Car();
        car.setBrand(brand);
        car.setModel(model);
        car.setProductionYear(2020);
        car.setMileage(30000);
        car.setPrice(30000);
        car.setStatus(CarStatus.AVAILABLE);
        car.setColor("red");
        car.setFuelType(FuelType.DIESEL);
        car.setHorsepower(150);
        car.setTransmission(Transmission.AUTOMATIC);
        return car;
    }

    @Test
    @DisplayName("GET /cars/{id} should return car if exists")
    void testGetCarByIdShouldReturnCar() throws Exception {
        Car saved = carRepository.save(buildValidCar("BMW", "X5"));

        mockMvc.perform(get("/api/cars/{id}", saved.getId()))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("GET /cars/{id} should not return car ")
    void testGetCarByIdShouldNotReturnCar() throws Exception {

        mockMvc.perform(get("/api/cars/{id}", 1L))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("POST /api/cars should save car in H2 return 201")
    void testCreateNewCarShouldReturn201() throws Exception {
        Car car = buildValidCar("Audi", "A6");

        String jsonBody = objectMapper.writeValueAsString(car);

        mockMvc.perform(
                        post("/api/cars")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonBody))
                .andExpect(status().isCreated());

        List<Car> cars = carRepository.findAll();
        assertEquals(1, cars.size());

        assertEquals(car.getBrand(), cars.getFirst().getBrand());
        assertEquals(car.getModel(), cars.getFirst().getModel());
        assertEquals(car.getPrice(), cars.getFirst().getPrice());
        assertEquals(car.getStatus(), cars.getFirst().getStatus());
        assertEquals(car.getColor(), cars.getFirst().getColor());
        assertEquals(car.getFuelType(), cars.getFirst().getFuelType());
        assertEquals(car.getHorsepower(), cars.getFirst().getHorsepower());
        assertEquals(car.getTransmission(), cars.getFirst().getTransmission());
        assertEquals(car.getProductionYear(), cars.getFirst().getProductionYear());
        assertEquals(car.getMileage(), cars.getFirst().getMileage());

    }

    @Test
    @DisplayName("GET /api/cars should return all cars")
    void testGetAllCarsShouldReturnAllCars() throws Exception {
        Car carAudi = buildValidCar("Audi", "A6");
        Car carKia = buildValidCar("Kia", "Rio");

        carRepository.save(carAudi);
        carRepository.save(carKia);

        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].brand", containsInAnyOrder("Audi", "Kia")))
                .andExpect(jsonPath("$[*].model", containsInAnyOrder("A6", "Rio")));

        List<Car> cars = carRepository.findAll();
        assertEquals(2, cars.size());
    }

    @Test
    @DisplayName("POST /api/cars should not save car in H2, invalid JSON return 400")
    void testCreateNewCarShouldReturn400() throws Exception {
        Car car = buildValidCar("Audi", "A6");
        car.setStatus(null);

        String jsonBody = objectMapper.writeValueAsString(car);

        mockMvc.perform(
                        post("/api/cars")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonBody))
                .andExpect(status().isBadRequest());

        List<Car> cars = carRepository.findAll();
        assertTrue(cars.isEmpty());
    }

    @Test
    @DisplayName("POST /api/cars with valid data — should return 201 Created and persist the car")
    void validPost_shouldReturn201() throws Exception {
        Car car = new Car("BMW", "X5", 2020, 20000, 30000, "AVAILABLE", "Black", 250, "PETROL", "AUTOMATIC");

        mockMvc.perform(post("/api/cars")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isCreated());

        assert carRepository.count() == 1;
    }

    @Test
    @DisplayName("POST /api/cars with invalid data — should return 400 Bad Request and not save the car")
    void invalidPost_shouldReturn400() throws Exception {

        Car car = new Car();
        car.setBrand("");
        car.setModel("X5");
        car.setProductionYear(1890);
        car.setMileage(-200);
        car.setPrice(-1);
        car.setStatus(null);
        car.setColor(null);
        car.setHorsepower(0);
        car.setFuelType(null);
        car.setTransmission(null);

        mockMvc.perform(post("/api/cars")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray());

        assertEquals(0, carRepository.count());
    }

    @Test
    @DisplayName("PUT /api/cars/{id} with valid data — should return 200 OK and update the car")
    void validPut_shouldReturn200() throws Exception {
        Car existing = new Car("Toyota", "Camry", 2015, 100000, 15000, "AVAILABLE", "White", 200, "PETROL", "MANUAL");
        carRepository.save(existing);

        existing.setColor("Blue");

        mockMvc.perform(put("/api/cars/" + existing.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(existing)))
                .andExpect(status().isOk());

        Optional<Car> optionalCar = carRepository.findById(existing.getId());
        assertTrue(optionalCar.isPresent());
        assertEquals("Blue", optionalCar.get().getColor());

    }

    @Test
    @DisplayName("PUT /api/cars/{id} with invalid data — should return 400 Bad Request and not update the car")
    void invalidPut_shouldReturn400() throws Exception {
        Car existing = new Car("Toyota", "Camry", 2015, 100000, 15000, "AVAILABLE", "White", 200, "PETROL", "MANUAL");
        carRepository.save(existing);

        existing.setBrand("");
        existing.setPrice(-100);

        mockMvc.perform(put("/api/cars/" + existing.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(existing)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray());

        Optional<Car> updatedCar = carRepository.findById(existing.getId());
        assertTrue(updatedCar.isPresent());
        assertEquals("Toyota", updatedCar.get().getBrand());
        assertEquals(15000, updatedCar.get().getPrice());
    }







}
