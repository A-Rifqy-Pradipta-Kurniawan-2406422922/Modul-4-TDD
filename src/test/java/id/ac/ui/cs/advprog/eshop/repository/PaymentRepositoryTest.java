package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {

    PaymentRepository paymentRepository;
    List<Payment> payments;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId(UUID.fromString("eb558e9f-1c39-460e-8860-71af6af63bd6"));
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        Order order1 = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");
        Order order2 = new Order("7f9e15bb-4b15-42f4-aebc-c3af385fb078",
                products, 1708570000L, "Safira Sudrajat");

        Map<String, String> voucherData = new HashMap<>();
        voucherData.put("voucherCode", "ESHOP1234ABC5678");

        Map<String, String> bankTransferData = new HashMap<>();
        bankTransferData.put("bankName", "BCA");
        bankTransferData.put("referenceCode", "TRX-001");

        payments = new ArrayList<>();
        payments.add(new Payment(order1, "VOUCHER_CODE", voucherData));
        payments.add(new Payment(order2, "BANK_TRANSFER", bankTransferData));
    }

    @Test
    void testSaveCreate() {
        Payment payment = payments.get(0);
        Payment result = paymentRepository.save(payment);

        Payment findResult = paymentRepository.findById(result.getId());
        assertNotNull(result.getId());
        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getMethod(), findResult.getMethod());
        assertEquals(payment.getStatus(), findResult.getStatus());
    }

    @Test
    void testFindByIdIfIdFound() {
        Payment payment = payments.get(1);
        paymentRepository.save(payment);

        Payment findResult = paymentRepository.findById(payment.getId());
        assertNotNull(findResult);
        assertEquals(payment.getId(), findResult.getId());
    }

    @Test
    void testFindByIdShouldSkipFirstEntryAndFindSecond() {
        Payment first = payments.get(0);
        Payment second = payments.get(1);

        paymentRepository.save(first);
        paymentRepository.save(second);

        Payment findResult = paymentRepository.findById(second.getId());
        assertNotNull(findResult);
        assertEquals(second.getId(), findResult.getId());
    }

    @Test
    void testFindByIdIfIdNotFound() {
        Payment findResult = paymentRepository.findById("invalid-id");
        assertNull(findResult);
    }

    @Test
    void testFindAll() {
        paymentRepository.save(payments.get(0));
        paymentRepository.save(payments.get(1));

        List<Payment> allPayments = paymentRepository.findAll();
        assertEquals(2, allPayments.size());
    }

    @Test
    void testSaveMultiplePaymentsWithSameId_shouldKeepBothEntries() {
        Payment payment1 = payments.get(0);

        Payment payment2 = new Payment(payment1.getOrder(), payment1.getMethod(), payment1.getPaymentData());
        payment2.setId(payment1.getId());

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        List<Payment> allPayments = paymentRepository.findAll();
        assertEquals(2, allPayments.size());
        assertEquals(payment1.getId(), allPayments.get(0).getId());
        assertEquals(payment2.getId(), allPayments.get(1).getId());
    }
}
