/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package polycafe.test;

import static org.junit.Assert.*;
import org.junit.Test;
import poly.cafe.dao.UserDAO;
import poly.cafe.entity.User;


public class JunitTestY3 {

    UserDAO userDAO = new UserDAO();

    @Test
    public void TC01_loginSuccess() {
        User u = userDAO.login("dinh", "1234");
        assertNotNull("Phải đăng nhập thành công", u);
    }

    @Test
    public void TC02_loginWrongPass() {
        User u = userDAO.login("admin", "999");
        assertNull("Hệ thống phải báo sai mật khẩu", u);
    }

    @Test
    public void TC03_loginWrongUser() {
        User u = userDAO.login("khong_ton_tai", "123");
        assertNull("Không được đăng nhập khi user không tồn tại", u);
    }

    @Test
    public void TC04_loginWrongUserPass() {
        User u = userDAO.login("sai_user", "sai_pass");
        assertNull("Sai username và password phải báo lỗi", u);
    }

    @Test
    public void TC05_loginEmptyUserOrPass() {
        User u = userDAO.login("", "");
        assertNull("Không được để trống username/password", u);
    }

    // ========================= CHANGE PASSWORD ==============================

    @Test
    public void TC06_changePasswordSuccess() {
        boolean ok = userDAO.changePassword("admin", "1234", "123");
        assertTrue("Đổi mật khẩu thành công", ok);
    }

    @Test
    public void TC07_changePasswordWrongOld() {
        boolean ok = userDAO.changePassword("admin", "sai_mat_khau", "new123");
        assertFalse("Mật khẩu cũ sai phải báo lỗi", ok);
    }

    // ========================= DRINK CRUD ==============================

    @Test
    public void TC08_createDrinkSuccess() {
        boolean ok = userDAO.testCreateDrink("Trà đào", 20000, "Tea");
        assertTrue("Tạo đồ uống thành công", ok);
    }

    @Test
    public void TC09_createDrinkEmptyName() {
        boolean ok = userDAO.testCreateDrink("", 20000, "Tea");
        assertFalse("Tên rỗng → phải báo lỗi", ok);
    }

    @Test
    public void TC10_createDrinkNegativePrice() {
        boolean ok = userDAO.testCreateDrink("Cà phê sữa", -10, "Coffee");
        assertFalse("Giá âm → phải báo lỗi", ok);
    }

    // ========================= DRINK UPDATE ==============================

    @Test
    public void TC11_updateDrinkSuccess() {
        boolean ok = userDAO.testUpdateDrink(1, "Cà phê đen", 15000, "Coffee");
        assertTrue("Cập nhật thành công", ok);
    }

    @Test
    public void TC12_deleteDrinkUsedInOrder() {
        boolean ok = userDAO.testDeleteDrink(5); // món đã có bill
        assertFalse("Không thể xóa khi đang được sử dụng", ok);
    }

    // ========================= BILL / RECEIPT ==============================

    @Test
    public void TC13_createReceiptSuccess() {
        boolean ok = userDAO.testCreateReceipt(1, 2);
        assertTrue("Tạo phiếu thành công", ok);
    }

    @Test
    public void TC14_createReceiptOverQuantity() {
        boolean ok = userDAO.testCreateReceipt(1, 9999);
        assertFalse("Số lượng vượt tồn kho phải báo lỗi", ok);
    }

    @Test
    public void TC15_createReceiptEmpty() {
        boolean ok = userDAO.testCreateReceipt(-1, 0);
        assertFalse("Không chọn đồ uống phải báo lỗi", ok);
    }

    @Test
    public void TC16_updateReceiptSuccess() {
        boolean ok = userDAO.testUpdateReceipt(1, 3);
        assertTrue("Cập nhật phiếu thành công", ok);
    }

    @Test
    public void TC17_updateReceiptClosed() {
        boolean ok = userDAO.testUpdateReceiptClosed(1);
        assertFalse("Phiếu đã đóng không được sửa", ok);
    }


    @Test
    public void TC18_createCategorySuccess() {
        boolean ok = userDAO.testCreateCategory("Trà Sữa");
        assertTrue("Tạo loại thành công", ok);
    }

    @Test
    public void TC19_deleteCategoryUsedByDrink() {
        boolean ok = userDAO.testDeleteCategory(3);
        assertFalse("Loại đang có đồ uống → không được xóa", ok);
    }

    @Test
    public void TC20_createUserSuccess() {
        boolean ok = userDAO.testCreateUser("nv01", "123", "staff");
        assertTrue("Tạo user thành công", ok);
    }

    @Test
    public void TC21_createUserWrongConfirm() {
        boolean ok = userDAO.testCreateUserConfirm("nv02", "123", "124");
        assertFalse("Confirm password khác phải báo lỗi", ok);
    }
}

