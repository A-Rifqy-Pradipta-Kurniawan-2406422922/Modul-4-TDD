package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class Payment {
    public static final String METHOD_VOUCHER_CODE = "VOUCHER_CODE";
    public static final String METHOD_BANK_TRANSFER = "BANK_TRANSFER";

    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_REJECTED = "REJECTED";
    public static final String STATUS_FAILED = "FAILED";

    private String id;
    private String method;
    private String status;
    private Map<String, String> paymentData;
    private Order order;

    public Payment(Order order, String method, Map<String, String> paymentData) {
        this.id = UUID.randomUUID().toString();
        this.order = order;
        this.method = method;
        this.paymentData = paymentData;
        this.status = resolveInitialStatus(method, paymentData);
    }

    public void setStatus(String status) {
        this.status = status;
        if (STATUS_SUCCESS.equals(status)) {
            this.order.setStatus(STATUS_SUCCESS);
        } else if (STATUS_REJECTED.equals(status)) {
            this.order.setStatus(STATUS_FAILED);
        }
    }

    private String resolveInitialStatus(String method, Map<String, String> paymentData) {
        if (METHOD_VOUCHER_CODE.equals(method)) {
            String voucherCode = paymentData.get("voucherCode");
            if (isVoucherValid(voucherCode)) {
                return STATUS_SUCCESS;
            } else {
                return STATUS_REJECTED;
            }
        } else if (METHOD_BANK_TRANSFER.equals(method)) {
            String bankName = paymentData.get("bankName");
            String referenceCode = paymentData.get("referenceCode");
            if (isBlank(bankName) || isBlank(referenceCode)) {
                return STATUS_REJECTED;
            } else {
                return STATUS_SUCCESS;
            }
        } else {
            return STATUS_REJECTED;
        }
    }

    private boolean isVoucherValid(String voucherCode) {
        return voucherCode != null
                && voucherCode.length() == 16
                && voucherCode.startsWith("ESHOP")
                && countDigits(voucherCode) == 8;
    }

    private int countDigits(String value) {
        int totalDigits = 0;
        for (char c : value.toCharArray()) {
            if (Character.isDigit(c)) {
                totalDigits += 1;
            }
        }
        return totalDigits;
    }

    private boolean isBlank(String value) {
        return value == null || value.isEmpty();
    }
}
