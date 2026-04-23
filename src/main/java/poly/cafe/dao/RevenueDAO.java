/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.cafe.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import poly.cafe.entity.Revenue;
import poly.cafe.util.XJDBC;

/**
 *
 * @author ADMIN
 */
public class RevenueDAO {
     public List<Revenue.ByCategory> selectRevenueByCategory(Date from, Date to) {
        String sql = """
            SELECT category.Name AS Category,
                   SUM(detail.UnitPrice * detail.Quantity * (1 - detail.Discount)) AS Revenue,
                   SUM(detail.Quantity) AS Quantity,
                   MIN(detail.UnitPrice) AS MinPrice,
                   MAX(detail.UnitPrice) AS MaxPrice,
                   AVG(detail.UnitPrice) AS AvgPrice
            FROM BillDetails AS detail
            JOIN Drinks AS drink ON drink.Id = detail.DrinkId
            JOIN Categories AS category ON category.Id = drink.CategoryId
            JOIN Bills AS bill ON bill.Id = detail.BillId
            WHERE bill.Status = 1
              AND bill.Checkout IS NOT NULL
              AND bill.Checkout BETWEEN ? AND ?
            GROUP BY category.Name
            ORDER BY Revenue DESC
            """;
        
        List<Revenue.ByCategory> list = new ArrayList<>();
        try (ResultSet rs = XJDBC.query(sql, from, to)) {
            while (rs.next()) {
                Revenue.ByCategory item = Revenue.ByCategory.builder()
                    .category(rs.getString("Category"))
                    .revenue(rs.getDouble("Revenue"))
                    .quantity(rs.getInt("Quantity"))
                    .minPrice(rs.getDouble("MinPrice"))
                    .maxPrice(rs.getDouble("MaxPrice"))
                    .avgPrice(rs.getDouble("AvgPrice"))
                    .build();
                list.add(item);
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi truy vấn doanh thu theo loại: " + e.getMessage(), e);
        }
        return list;
    }
    
    // Thống kê doanh thu theo nhân viên
    public List<Revenue.ByUser> selectRevenueByUser(Date from, Date to) {
        String sql = """
            SELECT bill.Username AS [User],
                   SUM(detail.UnitPrice * detail.Quantity * (1 - detail.Discount)) AS Revenue,
                   COUNT(DISTINCT detail.BillId) AS Quantity,
                   MIN(bill.Checkin) AS FirstTime,
                   MAX(bill.Checkin) AS LastTime
            FROM BillDetails AS detail
            JOIN Bills AS bill ON bill.Id = detail.BillId
            WHERE bill.Status = 1
              AND bill.Checkout IS NOT NULL
              AND bill.Checkout BETWEEN ? AND ?
            GROUP BY bill.Username
            ORDER BY Revenue DESC
            """;
        
        List<Revenue.ByUser> list = new ArrayList<>();
        try (ResultSet rs = XJDBC.query(sql, from, to)) {
            while (rs.next()) {
                Revenue.ByUser item = Revenue.ByUser.builder()
                    .user(rs.getString("User"))
                    .revenue(rs.getDouble("Revenue"))
                    .quantity(rs.getInt("Quantity"))
                    .firstTime(rs.getTimestamp("FirstTime"))
                    .lastTime(rs.getTimestamp("LastTime"))
                    .build();
                list.add(item);
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi truy vấn doanh thu theo nhân viên: " + e.getMessage(), e);
        }
        return list;
    }

}
