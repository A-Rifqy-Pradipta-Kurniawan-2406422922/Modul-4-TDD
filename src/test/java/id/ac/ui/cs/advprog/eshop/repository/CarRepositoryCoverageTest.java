package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CarRepositoryCoverageTest {

    @Test
    void createCarWithExistingId_shouldKeepProvidedId() {
        CarRepository carRepository = new CarRepository();

        Car car = new Car();
        car.setCarId("fixed-id-123");
        car.setCarName("Kijang");
        car.setCarColor("Black");
        car.setCarQuantity(1);

        Car saved = carRepository.create(car);

        assertEquals("fixed-id-123", saved.getCarId());
    }

    @Test
    void createCarWithoutId_shouldGenerateId() {
        CarRepository carRepository = new CarRepository();

        Car car = new Car();
        car.setCarName("Avanza");
        car.setCarColor("Silver");
        car.setCarQuantity(2);

        Car saved = carRepository.create(car);

        assertNotNull(saved.getCarId());
    }
}
