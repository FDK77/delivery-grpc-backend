package com.example.delivery2grpcbackend.service;

import com.example.discount.DiscountBackendServiceGrpc;
import com.example.discount.EligibilityRequest;
import com.example.discount.EligibilityResponse;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class DiscountEligibilityService extends DiscountBackendServiceGrpc.DiscountBackendServiceImplBase {

    @Override
    public void checkDiscountEligibility(EligibilityRequest request, StreamObserver<EligibilityResponse> responseObserver) {
        System.out.println("Получен запрос на проверку скидки для заказа с ID: " + request.getOrderId());
        boolean isEligible = new Random().nextBoolean();
        System.out.println("Заказ с ID: " + request.getOrderId() + " " + (isEligible ? "может" : "не может") + " получить скидку.");
        EligibilityResponse response = EligibilityResponse.newBuilder()
                .setIsEligible(isEligible)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
