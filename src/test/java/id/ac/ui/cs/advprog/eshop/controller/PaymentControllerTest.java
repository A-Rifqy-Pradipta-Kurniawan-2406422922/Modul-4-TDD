package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @InjectMocks
    PaymentController paymentController;

    @Mock
    PaymentService paymentService;

    private Payment payment;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId(UUID.fromString("eb558e9f-1c39-460e-8860-71af6af63bd6"));
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        Order order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");

        Map<String, String> voucherData = new HashMap<>();
        voucherData.put("voucherCode", "ESHOP1234ABC5678");
        payment = new Payment(order, "VOUCHER_CODE", voucherData);
    }

    @Test
    void paymentDetailFormPage_shouldReturnFormView() {
        String view = paymentController.paymentDetailFormPage();
        assertEquals("PaymentDetailForm", view);
    }

    @Test
    void paymentDetailPage_whenPaymentFound_shouldReturnPaymentDetailWithId() {
        doReturn(payment).when(paymentService).getPayment(payment.getId());

        Model model = new ConcurrentModel();
        String view = paymentController.paymentDetailPage(payment.getId(), model);

        assertEquals("PaymentDetail", view);
        assertEquals(payment.getId(), model.getAttribute("detailMessage"));
    }

    @Test
    void paymentDetailPage_whenPaymentNotFound_shouldReturnNotFoundMessage() {
        doReturn(null).when(paymentService).getPayment("invalid-id");

        Model model = new ConcurrentModel();
        String view = paymentController.paymentDetailPage("invalid-id", model);

        assertEquals("PaymentDetail", view);
        assertEquals("not found", model.getAttribute("detailMessage"));
    }

    @Test
    void paymentAdminListPage_shouldReturnListViewAndPayments() {
        List<Payment> payments = new ArrayList<>();
        payments.add(payment);
        doReturn(payments).when(paymentService).getAllPayments();

        Model model = new ConcurrentModel();
        String view = paymentController.paymentAdminListPage(model);

        assertEquals("PaymentAdminList", view);
        assertEquals(payments, model.getAttribute("payments"));
    }

    @Test
    void paymentAdminDetailPage_shouldReturnDetailViewAndPayment() {
        doReturn(payment).when(paymentService).getPayment(payment.getId());

        Model model = new ConcurrentModel();
        String view = paymentController.paymentAdminDetailPage(payment.getId(), model);

        assertEquals("PaymentAdminDetail", view);
        assertEquals(payment, model.getAttribute("payment"));
    }

    @Test
    void paymentAdminSetStatus_whenPaymentExists_shouldSetStatusAndRedirect() {
        doReturn(payment).when(paymentService).getPayment(payment.getId());

        Model model = new ConcurrentModel();
        String view = paymentController.paymentAdminSetStatus(payment.getId(), "SUCCESS", model);

        verify(paymentService, times(1)).setStatus(payment, "SUCCESS");
        assertEquals("redirect:/payment/admin/detail/" + payment.getId(), view);
    }

    @Test
    void paymentAdminSetStatus_whenPaymentNotExists_shouldOnlyRedirect() {
        doReturn(null).when(paymentService).getPayment("invalid-id");

        Model model = new ConcurrentModel();
        String view = paymentController.paymentAdminSetStatus("invalid-id", "SUCCESS", model);

        verify(paymentService, times(0)).setStatus(any(Payment.class), anyString());
        assertEquals("redirect:/payment/admin/detail/invalid-id", view);
    }
}
