package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @InjectMocks
    CarServiceImpl carService;

    @Mock
    CarRepository carRepository;

    @Test
    void create_shouldCallRepositoryAndReturnCar() {
        Car car = new Car();
        when(carRepository.create(car)).thenReturn(car);

        Car result = carService.create(car);

        verify(carRepository, times(1)).create(car);
        assertEquals(car, result);
    }

    @Test
    void findAll_shouldReturnAllCars() {
        Car car1 = new Car();
        Car car2 = new Car();
        Iterator<Car> iterator = Arrays.asList(car1, car2).iterator();
        when(carRepository.findAll()).thenReturn(iterator);

        List<Car> result = carService.findAll();

        verify(carRepository, times(1)).findAll();
        assertEquals(2, result.size());
        assertEquals(car1, result.get(0));
        assertEquals(car2, result.get(1));
    }

    @Test
    void findById_shouldReturnCar() {
        Car car = new Car();
        when(carRepository.findById("car-1")).thenReturn(car);

        Car result = carService.findById("car-1");

        verify(carRepository, times(1)).findById("car-1");
        assertEquals(car, result);
    }

    @Test
    void update_shouldCallRepository() {
        Car car = new Car();

        carService.update("car-1", car);

        verify(carRepository, times(1)).update("car-1", car);
    }

    @Test
    void deleteCarById_shouldCallRepository() {
        carService.deleteCarById("car-1");

        verify(carRepository, times(1)).delete("car-1");
    }
}
