package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentTest {

    private Order order;
    private Map<String, String> voucherPaymentData;
    private Map<String, String> bankTransferPaymentData;

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

        voucherPaymentData = new HashMap<>();
        bankTransferPaymentData = new HashMap<>();
    }

    @Test
    void testCreateVoucherPaymentIfVoucherValid() {
        voucherPaymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment(order, "VOUCHER_CODE", voucherPaymentData);

        assertNotNull(payment.getId());
        assertEquals("VOUCHER_CODE", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals("ESHOP1234ABC5678", payment.getPaymentData().get("voucherCode"));
    }

    @Test
    void testCreateVoucherPaymentIfVoucherInvalid() {
        voucherPaymentData.put("voucherCode", "INVALID");

        Payment payment = new Payment(order, "VOUCHER_CODE", voucherPaymentData);

        assertNotNull(payment.getId());
        assertEquals("VOUCHER_CODE", payment.getMethod());
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreateBankTransferIfDataValid() {
        bankTransferPaymentData.put("bankName", "BCA");
        bankTransferPaymentData.put("referenceCode", "TRX-001");

        Payment payment = new Payment(order, "BANK_TRANSFER", bankTransferPaymentData);

        assertNotNull(payment.getId());
        assertEquals("BANK_TRANSFER", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testCreateBankTransferIfBankNameEmpty() {
        bankTransferPaymentData.put("bankName", "");
        bankTransferPaymentData.put("referenceCode", "TRX-001");

        Payment payment = new Payment(order, "BANK_TRANSFER", bankTransferPaymentData);

        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreateBankTransferIfReferenceCodeNull() {
        bankTransferPaymentData.put("bankName", "BCA");
        bankTransferPaymentData.put("referenceCode", null);

        Payment payment = new Payment(order, "BANK_TRANSFER", bankTransferPaymentData);

        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testSetStatusSuccessShouldSetOrderStatusSuccess() {
        voucherPaymentData.put("voucherCode", "INVALID");
        Payment payment = new Payment(order, "VOUCHER_CODE", voucherPaymentData);

        payment.setStatus("SUCCESS");

        assertEquals("SUCCESS", payment.getStatus());
        assertEquals("SUCCESS", order.getStatus());
    }

    @Test
    void testSetStatusRejectedShouldSetOrderStatusFailed() {
        voucherPaymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment(order, "VOUCHER_CODE", voucherPaymentData);

        payment.setStatus("REJECTED");

        assertEquals("REJECTED", payment.getStatus());
        assertEquals("FAILED", order.getStatus());
    }

    @Test
    void testSetStatusOtherValueShouldNotChangeOrderStatus() {
        voucherPaymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment(order, "VOUCHER_CODE", voucherPaymentData);

        payment.setStatus("PENDING");

        assertEquals("PENDING", payment.getStatus());
        assertEquals("WAITING_PAYMENT", order.getStatus());
    }

    @Test
    void testCreatePaymentWithUnknownMethodShouldBeRejected() {
        Map<String, String> anyPaymentData = new HashMap<>();
        anyPaymentData.put("key", "value");

        Payment payment = new Payment(order, "UNKNOWN_METHOD", anyPaymentData);

        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreateVoucherPaymentIfVoucherCodeNull() {
        voucherPaymentData.put("voucherCode", null);

        Payment payment = new Payment(order, "VOUCHER_CODE", voucherPaymentData);

        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreateVoucherPaymentIfPrefixInvalid() {
        voucherPaymentData.put("voucherCode", "SHOPX1234ABC5678");

        Payment payment = new Payment(order, "VOUCHER_CODE", voucherPaymentData);

        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreateVoucherPaymentIfDigitCountInvalid() {
        voucherPaymentData.put("voucherCode", "ESHOPABCDABC5678");

        Payment payment = new Payment(order, "VOUCHER_CODE", voucherPaymentData);

        assertEquals("REJECTED", payment.getStatus());
    }
}
