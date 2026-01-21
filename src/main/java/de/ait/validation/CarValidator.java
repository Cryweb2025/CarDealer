package de.ait.validation;

import de.ait.model.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarValidator implements Validator<Car> {

    private static final Logger log = LoggerFactory.getLogger(CarValidator.class);

    @Override
    public boolean isValid(Car car) {
        return validateWithErrors(car).isEmpty();
    }

    @Override
    public List<String> validateWithErrors(Car car) {
        List<String> errors = new ArrayList<>();

        if (car.getBrand() == null || car.getBrand().trim().isEmpty())
            errors.add("Brand must not be empty");

        if (car.getModel() == null || car.getModel().trim().isEmpty())
            errors.add("Model must not be empty");

        if (car.getProductionYear() < 1900)
            errors.add("Production year must be >= 1900");

        if (car.getMileage() < 0)
            errors.add("Mileage must be >= 0");

        if (car.getPrice() < 1)
            errors.add("Price must be >= 1");

        if (car.getStatus() == null)
            errors.add("Status must not be null");

        if (car.getColor() == null || car.getColor().trim().isEmpty())
            errors.add("Color must not be empty");

        if (car.getHorsepower() < 1)
            errors.add("Horse power must be >= 1");

        if (car.getFuelType() == null)
            errors.add("FuelType must not be null");

        if (car.getTransmission() == null)
            errors.add("Transmission must not be null");

        if (!errors.isEmpty()) {
            log.warn("Validation for car with ID: {} is failed! | Errors: {}", car.getId(), errors);
        }

        return errors;
    }
}
