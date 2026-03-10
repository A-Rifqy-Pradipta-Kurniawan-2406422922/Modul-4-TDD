package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @InjectMocks
    CarController carController;

    @Mock
    CarServiceImpl carService;

    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setCarId("car-1");
        car.setCarName("Kijang");
        car.setCarColor("Black");
        car.setCarQuantity(2);
    }

    @Test
    void createCarPage_shouldReturnCreateCarView() {
        Model model = new ConcurrentModel();
        String view = carController.createCarPage(model);

        assertEquals("createCar", view);
    }

    @Test
    void createCarPost_shouldCallServiceAndRedirect() {
        Model model = new ConcurrentModel();
        String view = carController.createCarPost(car, model);

        verify(carService, times(1)).create(car);
        assertEquals("redirect:listCar", view);
    }

    @Test
    void carListPage_shouldPutCarsAndReturnListView() {
        List<Car> cars = new ArrayList<>();
        cars.add(car);
        doReturn(cars).when(carService).findAll();

        Model model = new ConcurrentModel();
        String view = carController.carListPage(model);

        assertEquals("carList", view);
        assertEquals(cars, model.getAttribute("cars"));
    }

    @Test
    void editCarPage_shouldPutCarAndReturnEditView() {
        doReturn(car).when(carService).findById("car-1");

        Model model = new ConcurrentModel();
        String view = carController.editCarPage("car-1", model);

        assertEquals("editCar", view);
        assertEquals(car, model.getAttribute("car"));
    }

    @Test
    void editCarPost_shouldCallServiceAndRedirect() {
        Model model = new ConcurrentModel();
        String view = carController.editCarPost(car, model);

        verify(carService, times(1)).update(car.getCarId(), car);
        assertEquals("redirect:listCar", view);
    }

    @Test
    void deleteCar_shouldCallServiceAndRedirect() {
        String view = carController.deleteCar("car-1");

        verify(carService, times(1)).deleteCarById("car-1");
        assertEquals("redirect:listCar", view);
    }
}
