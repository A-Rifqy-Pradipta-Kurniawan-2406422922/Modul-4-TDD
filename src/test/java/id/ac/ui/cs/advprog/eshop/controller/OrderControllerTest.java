package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @InjectMocks
    OrderController orderController;

    @Mock
    OrderService orderService;

    private Order order;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId(UUID.fromString("eb558e9f-1c39-460e-8860-71af6af63bd6"));
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");
    }

    @Test
    void createOrderPage_shouldReturnCreateOrderView() {
        String view = orderController.createOrderPage();
        assertEquals("CreateOrder", view);
    }

    @Test
    void createOrderPost_shouldCreateAndRedirectToHistory() {
        String view = orderController.createOrderPost("Safira Sudrajat");

        assertEquals("redirect:/order/history", view);
        verify(orderService, times(1)).createOrder(any(Order.class));
    }

    @Test
    void orderHistoryPage_shouldReturnFormView() {
        String view = orderController.orderHistoryPage();
        assertEquals("OrderHistoryForm", view);
    }

    @Test
    void orderHistoryPost_shouldPutOrdersAndReturnOrderListView() {
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        doReturn(orders).when(orderService).findAllByAuthor("Safira Sudrajat");

        Model model = new ConcurrentModel();
        String view = orderController.orderHistoryPost("Safira Sudrajat", model);

        assertEquals("OrderList", view);
        assertEquals(orders, model.getAttribute("orders"));
    }

    @Test
    void payOrderPage_shouldPutOrderAndReturnOrderPayView() {
        doReturn(order).when(orderService).findById(order.getId());

        Model model = new ConcurrentModel();
        String view = orderController.payOrderPage(order.getId(), model);

        assertEquals("OrderPay", view);
        assertEquals(order, model.getAttribute("order"));
    }

    @Test
    void payOrderPost_shouldPutOrderIdAndReturnOrderPayResultView() {
        Model model = new ConcurrentModel();
        String view = orderController.payOrderPost(order.getId(), model);

        assertEquals("OrderPayResult", view);
        assertEquals(order.getId(), model.getAttribute("orderId"));
    }
}
