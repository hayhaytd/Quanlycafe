/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.cafe.dao;
import java.util.List;
/**
 *
 * @author ADMIN
 */
public abstract class CrudDAO <EntityType,KeyType> {
    public abstract void insert(EntityType entity);
    public abstract void update(EntityType entity);
    public abstract void delete(KeyType id);
    public abstract List<EntityType> selectAll();
    public abstract EntityType selectByID(KeyType id);
    public abstract List<EntityType> selectBySQL(String sql, Object...args); 
}
