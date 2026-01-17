package de.ait.model;

import de.ait.enums.CarStatus;
import de.ait.enums.FuelType;
import de.ait.enums.Transmission;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

//В Intity не использовать @Value и @Data!!!

@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Brand must not be empty")
    private String brand;

    @Column(nullable = false)
    @NotBlank(message = "Model must not be empty")
    private String model;

    @Column(name="production_year")
    @Min(value = 1900, message ="Year must be greater than 1900")
    private int productionYear;

    @Min(value = 0, message ="Mileage must be greater than 0")
    private int mileage;

    @Min(value = 1, message ="Price must be greater than 1")
    private int price;

    @Enumerated(EnumType.STRING)
    private CarStatus status;

    private String color;

    private int horsepower;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    private Transmission transmission;

    public Car(String brand, String model, int productionYear, int mileage, int price, String status, String color, int horsepower, String fuelType, String transmission) {
        this.brand = brand;
        this.model = model;
        this.productionYear = productionYear;
        this.mileage = mileage;
        this.price = price;
        this.status = CarStatus.valueOf(status);
        this.color = color;
        this.horsepower = horsepower;
        this.fuelType = FuelType.valueOf(fuelType);
        this.transmission = Transmission.valueOf(transmission);
    }

}
