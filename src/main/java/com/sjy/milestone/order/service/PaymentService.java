package com.sjy.milestone.order.service;

import com.sjy.milestone.Exception.PaymentCancellationException;
import com.sjy.milestone.Exception.PaymentPreparationException;
import com.sjy.milestone.Exception.PaymentVerificationException;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service @RequiredArgsConstructor @Slf4j
public class PaymentService {

    private final PortOneService portOneService;

    public void preparePayment(String merchantUid, Long amount) {
        try {
            portOneService.preparePayment(merchantUid, amount);
        } catch (IamportResponseException | IOException e) {
            throw new PaymentPreparationException("결제 준비 중 오류가 발생했습니다.", e);
        }
    }

    public void verifyPayment(String impUid, String merchantUid) {
        Payment payment = portOneService.verifyPayment(impUid);
        if (payment == null || !payment.getMerchantUid().trim().equals(merchantUid.trim())) {
            log.error("결제 검증 실패: 예상 merchantUid {}와 일치하지 않음", merchantUid);
            throw new PaymentVerificationException("결제 검증 실패: 예상 merchantUid와 일치하지 않음", null);
        }
    }

    public void requestRefund(String impUid) {
        try {
            portOneService.requestRefund(impUid);
        } catch (IamportResponseException | IOException e) {
            throw new PaymentCancellationException("결제 취소 중 오류가 발생했습니다.", e);
        }
    }
}