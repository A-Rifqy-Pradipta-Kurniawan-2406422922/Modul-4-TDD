package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CarRepositoryCoverageTest {
    private CarRepository carRepository;

    @BeforeEach
    void setUp() {
        carRepository = new CarRepository();
    }

    @Test
    void createCarWithExistingId_shouldKeepProvidedId() {
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
        Car car = new Car();
        car.setCarName("Avanza");
        car.setCarColor("Silver");
        car.setCarQuantity(2);

        Car saved = carRepository.create(car);

        assertNotNull(saved.getCarId());
    }

    @Test
    void findAll_shouldReturnIteratorWithSavedCars() {
        Car first = new Car();
        first.setCarId("car-1");
        first.setCarName("A");
        first.setCarColor("Red");
        first.setCarQuantity(1);
        carRepository.create(first);

        Car second = new Car();
        second.setCarId("car-2");
        second.setCarName("B");
        second.setCarColor("Blue");
        second.setCarQuantity(2);
        carRepository.create(second);

        Iterator<Car> iterator = carRepository.findAll();
        assertTrue(iterator.hasNext());
        assertEquals("car-1", iterator.next().getCarId());
        assertTrue(iterator.hasNext());
        assertEquals("car-2", iterator.next().getCarId());
        assertFalse(iterator.hasNext());
    }

    @Test
    void findById_shouldReturnCarWhenFound() {
        Car car = new Car();
        car.setCarId("target-id");
        car.setCarName("Jazz");
        car.setCarColor("Gray");
        car.setCarQuantity(3);
        carRepository.create(car);

        Car found = carRepository.findById("target-id");
        assertNotNull(found);
        assertEquals("Jazz", found.getCarName());
    }

    @Test
    void findById_shouldReturnNullWhenNotFound() {
        Car car = new Car();
        car.setCarId("existing-id");
        car.setCarName("Civic");
        car.setCarColor("White");
        car.setCarQuantity(4);
        carRepository.create(car);

        Car found = carRepository.findById("missing-id");
        assertNull(found);
    }

    @Test
    void update_shouldUpdateCarWhenFound() {
        Car car = new Car();
        car.setCarId("update-id");
        car.setCarName("Old");
        car.setCarColor("Black");
        car.setCarQuantity(1);
        carRepository.create(car);

        Car updatedCar = new Car();
        updatedCar.setCarName("New");
        updatedCar.setCarColor("Silver");
        updatedCar.setCarQuantity(10);

        Car updated = carRepository.update("update-id", updatedCar);
        assertNotNull(updated);
        assertEquals("New", updated.getCarName());
        assertEquals("Silver", updated.getCarColor());
        assertEquals(10, updated.getCarQuantity());
    }

    @Test
    void update_shouldReturnNullWhenCarNotFound() {
        Car updatedCar = new Car();
        updatedCar.setCarName("Any");
        updatedCar.setCarColor("Any");
        updatedCar.setCarQuantity(1);

        Car updated = carRepository.update("unknown-id", updatedCar);
        assertNull(updated);
    }

    @Test
    void update_shouldSkipNonMatchingCarBeforeUpdatingMatch() {
        Car first = new Car();
        first.setCarId("car-first");
        first.setCarName("First");
        first.setCarColor("White");
        first.setCarQuantity(1);
        carRepository.create(first);

        Car second = new Car();
        second.setCarId("car-second");
        second.setCarName("Second");
        second.setCarColor("Black");
        second.setCarQuantity(2);
        carRepository.create(second);

        Car updatedCar = new Car();
        updatedCar.setCarName("Second Updated");
        updatedCar.setCarColor("Silver");
        updatedCar.setCarQuantity(9);

        Car updated = carRepository.update("car-second", updatedCar);

        assertNotNull(updated);
        assertEquals("Second Updated", updated.getCarName());
    }

    @Test
    void delete_shouldRemoveCarWhenIdExists() {
        Car car = new Car();
        car.setCarId("delete-id");
        car.setCarName("Yaris");
        car.setCarColor("Green");
        car.setCarQuantity(7);
        carRepository.create(car);

        carRepository.delete("delete-id");

        assertNull(carRepository.findById("delete-id"));
    }

    @Test
    void delete_shouldDoNothingWhenIdNotFound() {
        Car car = new Car();
        car.setCarId("still-exists");
        car.setCarName("Innova");
        car.setCarColor("Brown");
        car.setCarQuantity(5);
        carRepository.create(car);

        carRepository.delete("unknown-id");

        assertNotNull(carRepository.findById("still-exists"));
    }
}
