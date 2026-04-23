/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.cafe.ui;

import java.util.List;
import poly.cafe.dao.UserDAO;
import poly.cafe.entity.Drink;
import poly.cafe.entity.User;

/**
 *
 * @author ADMIN
 */
public class test {
    public static void main(String[] args) {
        UserDAO dao = new UserDAO();
        List<User> ls = dao.selectAll();
        for (User us : ls) {
            System.out.println(us.toString());
        }
        
    }
}
