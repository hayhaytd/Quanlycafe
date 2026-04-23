/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package poly.cafe.ui;

import poly.cafe.dao.UserDAO;
import poly.cafe.entity.User;
import poly.cafe.util.MsgBox;
import poly.cafe.util.XAuth;

/**
 *
 * @author ADMIN
 */
public interface LoginController {
    /**
     * Opens and displays the login dialog
     */
    void open();
    
    /**
     * Performs the login authentication
     */
    void login();
    
    /**
     * Exits the application with confirmation
     */
    default void exit() {
        if(MsgBox.confirm(null, "Bạn muốn kết thúc?")) {
            System.exit(0);
        }
    }
}