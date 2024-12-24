package com.example.delivery2grpcbackend.service;
import com.example.discount.DeliveryResponse;
import com.example.discount.DeliveryRequest;
import io.grpc.stub.StreamObserver;
import com.example.discount.DeliveryCalculationServiceGrpc.DeliveryCalculationServiceImplBase;
import org.springframework.stereotype.Service;

@Service
public class DeliveryCalculationService extends DeliveryCalculationServiceImplBase {

    @Override
    public void calculateDelivery(DeliveryRequest request, StreamObserver<DeliveryResponse> responseObserver) {
        System.out.println("Получен запрос на расчет времени и расстояния для заказа: " + request.getOrderId());

        double distance = Math.sqrt(
                Math.pow(request.getCustomerLatitude() - request.getDelivererLatitude(), 2) +
                        Math.pow(request.getCustomerLongitude() - request.getDelivererLongitude(), 2)
        ) * 111;

        double estimatedTimeMinutesRaw = distance / 0.5;
        int estimatedTimeMinutes = (int) estimatedTimeMinutesRaw;
        int estimatedTimeSeconds = (int) ((estimatedTimeMinutesRaw - estimatedTimeMinutes) * 60);

       DeliveryResponse response = DeliveryResponse.newBuilder()
                .setOrderId(request.getOrderId())
                .setDistance(distance)
                .setEstimatedTimeMinutes(estimatedTimeMinutes)
                .setEstimatedTimeSeconds(estimatedTimeSeconds)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}