package com.example.delivery2grpcbackend.config;

import com.example.delivery2grpcbackend.service.DiscountEligibilityService;
import com.example.delivery2grpcbackend.service.DeliveryCalculationService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Component
public class GrpcConfig {

    private final DiscountEligibilityService discountEligibilityService;
    private final DeliveryCalculationService deliveryCalculationService;

    @Autowired
    public GrpcConfig(DiscountEligibilityService discountEligibilityService,
                      DeliveryCalculationService deliveryCalculationService) {
        this.discountEligibilityService = discountEligibilityService;
        this.deliveryCalculationService = deliveryCalculationService;
    }

    private Server server;

    @PostConstruct
    public void startServer() throws IOException {
        server = ServerBuilder.forPort(9091)
                .addService(discountEligibilityService)
                .addService(deliveryCalculationService)
                .build();
        server.start();
        System.out.println("gRPC сервер запущен на порту 9091");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Завершаем работу gRPC сервера...");
            stopServer();
        }));
    }

    @PreDestroy
    public void stopServer() {
        if (server != null) {
            try {
                server.shutdown().awaitTermination();
                System.out.println("gRPC сервер остановлен");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
