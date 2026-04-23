/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.cafe.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

public class Revenue {
    
    // SQL: thống kê theo Categories - FIXED DISCOUNT CALCULATION
    public static final String REVENUE_BY_CATEGORY_SQL = 
            "SELECT category.Name AS Category, " +
            "SUM(detail.UnitPrice * detail.Quantity * (1 - detail.Discount/100.0)) AS Revenue, " +
            "SUM(detail.Quantity) AS Quantity, " +
            "MIN(detail.UnitPrice) AS MinPrice, " +
            "MAX(detail.UnitPrice) AS MaxPrice, " +
            "AVG(detail.UnitPrice) AS AvgPrice " +
            "FROM BillDetails AS detail " +
            "JOIN Drinks AS drink ON drink.Id = detail.DrinkId " +
            "JOIN Categories AS category ON category.Id = drink.CategoryId " +
            "JOIN Bills AS bill ON bill.Id = detail.BillId " +
            "WHERE bill.Status = 1 " +
            "AND bill.Checkout IS NOT NULL " +
            "AND bill.Checkout BETWEEN ? AND ? " +
            "GROUP BY category.Name " +
            "ORDER BY Revenue DESC";
    
    // Câu lệnh sql: thống kê theo tài khoản - FIXED DISCOUNT CALCULATION
    public static final String REVENUE_BY_USER_SQL = 
            "SELECT bill.Username AS [User], " +
            "SUM(detail.UnitPrice * detail.Quantity * (1 - detail.Discount/100.0)) AS Revenue, " +
            "COUNT(DISTINCT detail.BillId) AS Quantity, " +
            "MIN(bill.Checkin) AS FirstTime, " +
            "MAX(bill.Checkin) AS LastTime " +
            "FROM BillDetails AS detail " +
            "JOIN Bills AS bill ON bill.Id = detail.BillId " +
            "WHERE bill.Status = 1 " +
            "AND bill.Checkout IS NOT NULL " +
            "AND bill.Checkout BETWEEN ? AND ? " +
            "GROUP BY bill.Username " +
            "ORDER BY Revenue DESC";
    
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class ByCategory {
        private String category;
        private double revenue;
        private int quantity;
        private double minPrice;
        private double maxPrice;
        private double avgPrice;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class ByUser {
        private String user;
        private double revenue;
        private int quantity;
        private Date firstTime;
        private Date lastTime;
    }
}
