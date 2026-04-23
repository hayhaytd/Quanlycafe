package poly.cafe.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import poly.cafe.entity.Category;
import poly.cafe.util.XJDBC;

public class CategoryDAO extends CrudDAO<Category, String> {
    final String INSERT_SQL = "INSERT INTO Categories(Id, Name) VALUES (?, ?)";
    final String UPDATE_SQL = "UPDATE Categories SET Name = ? WHERE Id = ?";
    final String DELETE_SQL = "DELETE FROM Categories WHERE Id = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM Categories";
    final String SELECT_BY_ID_SQL = "SELECT * FROM Categories WHERE Id = ?";
    
    @Override
    public void insert(Category entity) {
        XJDBC.update(INSERT_SQL, entity.getId(), entity.getName());
    }

    @Override
    public void update(Category entity) {
        XJDBC.update(UPDATE_SQL, entity.getName(), entity.getId());
    }

    @Override
    public void delete(String id) {
        XJDBC.update(DELETE_SQL, id);
    }

    @Override
    public List<Category> selectAll() {
        return selectBySQL(SELECT_ALL_SQL);
    }

    @Override
    public Category selectByID(String id) {
        List<Category> list = selectBySQL(SELECT_BY_ID_SQL, id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Category> selectBySQL(String sql, Object... args) {
        List<Category> list = new ArrayList<>();
        try {
            ResultSet rs = XJDBC.query(sql, args);
            while (rs.next()) {
                Category entity = new Category();
                entity.setId(rs.getString("Id"));
                entity.setName(rs.getString("Name"));
                
                list.add(entity);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}