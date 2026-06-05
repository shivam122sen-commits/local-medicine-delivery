package com.med.delivery.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStats {
    private Long totalOrders;
    private Long activeOrders;
    private Long pendingOrders;
    private Long deliveredOrders;
    private Double totalRevenue;
    private Double todayRevenue;
    private Long lowStockItems;
    private Long pendingPrescriptions;
    private Long todayDeliveries;
    private Double todayEarnings;
    private Double totalDistance;
    private Double rating;
}
