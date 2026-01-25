DELETE FROM cars;

INSERT INTO cars (
    brand, model, production_year, mileage, price,
    status, color, horsepower, fuel_type, transmission, created_at, updated_at, deleted
) VALUES (
             'Toyota', 'Camry', 2020, 35000, 18000,
             'AVAILABLE', 'Black', 200, 'PETROL', 'AUTOMATIC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE
         );

INSERT INTO cars (
    brand, model, production_year, mileage, price,
    status, color, horsepower, fuel_type, transmission, created_at, updated_at, deleted
) VALUES (
             'BMW', 'X5', 2018, 78000, 28000,
             'SOLD', 'White', 265, 'DIESEL', 'AUTOMATIC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE
         );
