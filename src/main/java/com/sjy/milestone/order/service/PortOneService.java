package com.sjy.milestone.order.service;

import com.sjy.milestone.exception.internal_servererror.PaymentCancellationException;
import com.sjy.milestone.exception.internal_servererror.PaymentPreparationException;
import com.sjy.milestone.exception.internal_servererror.PaymentVerificationException;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.siot.IamportRestClient.response.Prepare;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service @Slf4j
public class PortOneService {

    private final IamportClient iamportClient;

    public PortOneService(@Value("${port_one.api.key}") String apiKey,
                          @Value("${port_one.api.secret}") String apiSecret) {
        this.iamportClient = new IamportClient(apiKey, apiSecret);
    }

    public void preparePayment(String merchantUid, Long amount) throws IamportResponseException, IOException {
        BigDecimal amountBigDecimal = BigDecimal.valueOf(amount);
        PrepareData prepareData = new PrepareData(merchantUid, amountBigDecimal);
        IamportResponse<Prepare> response = iamportClient.postPrepare(prepareData);

        if (response.getResponse() == null) {
            throw new PaymentPreparationException(response.getMessage(), null);
        }

        log.info("준비된 merchantUid: {}", merchantUid);
    }

    public Payment verifyPayment(String impUid) {
        try {
            IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);

            if (response.getResponse() == null) {
                log.error("결제 검증 실패: {}", response.getMessage());
                throw new PaymentVerificationException(response.getMessage(), null);
            }
            return response.getResponse();
        } catch (IamportResponseException | IOException e) {
            throw new PaymentVerificationException("결제 검증 중 오류가 발생했습니다.", e);
        }
    }

    public void requestRefund(String impUid) throws IamportResponseException, IOException {
        CancelData cancelData = new CancelData(impUid, true);
        IamportResponse<Payment> response = iamportClient.cancelPaymentByImpUid(cancelData);

        if (response.getResponse() == null) {
            throw new PaymentCancellationException("환불 요청에 실패했습니다: " + response.getMessage());
        }
    }
}