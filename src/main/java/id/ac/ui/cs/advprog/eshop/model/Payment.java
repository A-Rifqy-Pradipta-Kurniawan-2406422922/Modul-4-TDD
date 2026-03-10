package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class Payment {
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

        if ("VOUCHER_CODE".equals(method)) {
            String voucherCode = paymentData.get("voucherCode");
            if (voucherCode != null
                    && voucherCode.length() == 16
                    && voucherCode.startsWith("ESHOP")
                    && countDigits(voucherCode) == 8) {
                this.status = "SUCCESS";
            } else {
                this.status = "REJECTED";
            }
        } else if ("BANK_TRANSFER".equals(method)) {
            String bankName = paymentData.get("bankName");
            String referenceCode = paymentData.get("referenceCode");

            if (isBlank(bankName) || isBlank(referenceCode)) {
                this.status = "REJECTED";
            } else {
                this.status = "SUCCESS";
            }
        } else {
            this.status = "REJECTED";
        }
    }

    public void setStatus(String status) {
        this.status = status;

        if ("SUCCESS".equals(status)) {
            this.order.setStatus("SUCCESS");
        } else if ("REJECTED".equals(status)) {
            this.order.setStatus("FAILED");
        }
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
