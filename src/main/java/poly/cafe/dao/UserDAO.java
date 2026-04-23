package poly.cafe.dao;

import poly.cafe.util.XJDBC;
import java.util.List;
import poly.cafe.entity.User;

public class UserDAO extends CrudDAO<User, String> {

    final String INSERT_SQL = "INSERT INTO Users(UserName,Password,Enabled,Fullname,Photo,Manager) VALUES (?,?,?,?,?,?)";
    final String UPDATE_SQL = "UPDATE Users SET Password=?, Enabled=?, Fullname=?, Photo=?, Manager=? WHERE UserName=?";
    final String DELETE_SQL = "DELETE FROM Users WHERE UserName=?";
    final String SELECT_ALL_SQL = "SELECT * FROM Users";
    final String SELECT_BY_ID_SQL = "SELECT * FROM Users WHERE UserName=?";

    @Override
    public void insert(User entity) {
        XJDBC.update(INSERT_SQL,
                entity.getUsername(),
                entity.getPassword(),
                entity.isEnabled(),
                entity.getFullname(),
                entity.getPhoto(),
                entity.isManager()
        );
    }

    @Override
    public void update(User entity) {
        XJDBC.update(UPDATE_SQL,
                entity.getPassword(),
                entity.isEnabled(),
                entity.getFullname(),
                entity.getPhoto(),
                entity.isManager(),
                entity.getUsername()
        );
    }

    @Override
    public void delete(String id) {
        XJDBC.update(DELETE_SQL, id);
    }

    @Override
    public List<User> selectAll() {
        return selectBySQL(SELECT_ALL_SQL);
    }

    @Override
    public User selectByID(String id) {
        List<User> list = selectBySQL(SELECT_BY_ID_SQL, id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<User> selectBySQL(String sql, Object... args) {
        List<User> list = new java.util.ArrayList<>();
        try (java.sql.ResultSet rs = XJDBC.query(sql, args)) {
            while (rs.next()) {
                User entity = new User();
                entity.setUsername(rs.getString("UserName"));
                entity.setPassword(rs.getString("Password"));
                entity.setEnabled(rs.getBoolean("Enabled"));
                entity.setFullname(rs.getString("Fullname"));
                entity.setPhoto(rs.getString("Photo"));
                entity.setManager(rs.getBoolean("Manager"));
                list.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // ========================= LOGIN =========================
    public User login(String username, String password) {
        List<User> list = selectBySQL("SELECT * FROM Users WHERE UserName=? AND Password=?", username, password);
        return list.isEmpty() ? null : list.get(0);
    }

    // ========================= CHANGE PASSWORD =========================
    public boolean changePassword(String username, String oldPass, String newPass) {
        User u = login(username, oldPass);
        if (u == null) return false;
        u.setPassword(newPass);
        update(u);
        return true;
    }

    // ========================= DRINK CRUD MOCK =========================
    public boolean testCreateDrink(String name, int price, String category) {
        return name != null && !name.isEmpty() && price > 0;
    }

    public boolean testUpdateDrink(int id, String name, int price, String category) {
        return id > 0 && name != null && !name.isEmpty() && price > 0;
    }

    public boolean testDeleteDrink(int id) {
        return id != 5; // giả lập: id=5 đang được dùng nên không xóa
    }

    // ========================= RECEIPT / BILL MOCK =========================
    public boolean testCreateReceipt(int drinkId, int quantity) {
        return drinkId > 0 && quantity > 0 && quantity < 1000;
    }

    public boolean testUpdateReceipt(int receiptId, int quantity) {
        return true;
    }

    public boolean testUpdateReceiptClosed(int receiptId) {
        return false; // giả lập phiếu đã đóng không được sửa
    }

    // ========================= CATEGORY MOCK =========================
    public boolean testCreateCategory(String name) {
        return name != null && !name.isEmpty();
    }

    public boolean testDeleteCategory(int id) {
        return id != 3; // giả lập id=3 đang có đồ uống → không xóa
    }

    // ========================= USER MANAGEMENT MOCK =========================
    public boolean testCreateUser(String username, String password, String role) {
        return true;
    }

    public boolean testCreateUserConfirm(String username, String password, String confirm) {
        return password.equals(confirm);
    }
}
