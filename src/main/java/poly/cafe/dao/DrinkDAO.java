/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.cafe.dao;


import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import poly.cafe.entity.Drink;
import poly.cafe.util.XJDBC;

/**
 *
 * @author ADMIN
 */
public class DrinkDAO extends CrudDAO<Drink, String> {

final String INSERT_SQL = "INSERT INTO Drinks (id, name, image, unitPrice, discount, available, categoryId) VALUES (?, ?, ?, ?, ?, ?, ?)";    final String UPDATE_SQL = "UPDATE Drinks SET Name=?, UnitPrice=?, Discount=?, Image=?, Available=?, CategoryId=? WHERE Id = ?";
    final String DELETE_SQL = "DELETE FROM Drinks WHERE Id = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM Drinks";
    final String SELECT_BY_ID_SQL = "SELECT * FROM Drinks WHERE Id = ?";
    final String SELECT_BY_CATEGORY_ID_SQL = "SELECT * FROM Drinks WHERE CategoryId = ?"; // Added

    @Override
    public void insert(Drink entity) {
 XJDBC.update(INSERT_SQL, entity.getId(), entity.getName(), entity.getImage(), entity.getUnitPrice(), entity.getDiscount(), entity.isAvailable(), entity.getCategoryId());    }

    @Override
    public void update(Drink entity) {
        XJDBC.update(UPDATE_SQL, entity.getName(), entity.getUnitPrice(), entity.getDiscount(), entity.getImage(), entity.isAvailable(), entity.getCategoryId(), entity.getId());
    }

    @Override
    public void delete(String id) {
        XJDBC.update(DELETE_SQL, id);
    }

    @Override
    public List<Drink> selectAll() {
        return selectBySQL(SELECT_ALL_SQL);
    }

    @Override
    public Drink selectByID(String id) {
        List<Drink> list = selectBySQL(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<Drink> selectBySQL(String sql, Object... args) {
        List<Drink> list = new ArrayList<>();
        try {
            ResultSet rs = XJDBC.query(sql, args);
            while (rs.next()) {
                Drink entity = new Drink();
                entity.setId(rs.getString("Id"));
                entity.setName(rs.getString("Name")); // Corrected
                entity.setUnitPrice(rs.getDouble("UnitPrice"));
                entity.setDiscount(rs.getDouble("Discount"));
                entity.setImage(rs.getString("Image"));
                entity.setAvailable(rs.getBoolean("Available")); // Corrected spelling
                entity.setCategoryId(rs.getString("CategoryId"));
                list.add(entity);
            }
            //rs.getStatement().getConnection().close(); // Remove this line.  XJDBC should handle closing the connection.
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Added findByCategoryId method
    public List<Drink> findByCategoryId(String categoryId) {
        return selectBySQL(SELECT_BY_CATEGORY_ID_SQL, categoryId);
    }
    
    public List<Drink> selectByCategoryId(String categoryId) {
        String sql = "SELECT * FROM Drinks WHERE CategoryId=?";
        return selectBySQL(sql, categoryId);
    }
}
