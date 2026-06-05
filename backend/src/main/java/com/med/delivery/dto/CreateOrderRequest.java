package com.med.delivery.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateOrderRequest {
    private Long patientId;
    private Long chemistId;
    private String deliveryAddress;
    private Boolean prescriptionRequired;
    private String prescriptionUrl;
    private Boolean isEmergency;
    private List<OrderItemDTO> items;
    
    @Data
    public static class OrderItemDTO {
        private Long inventoryId;
        private String medicineName;
        private Integer quantity;
        private Double unitPrice;
        private String imageUrl;
    }
}
