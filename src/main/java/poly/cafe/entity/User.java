    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.cafe.entity;

/**
 *
 * @author ADMIN
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@NoArgsConstructor // Tạo constructor không tham số
@AllArgsConstructor // Tạo constructor có tham số
@Builder // Cho phép tạo object bằng builder pattern
@Data // Sinh sẵn getter, setter, toString, equals, hashCode
@ToString
public class User {
    private String username;
    private String password;
    private boolean enabled;
    private String fullname;

    @Builder.Default
    private String photo = "photo.png";

    private boolean manager;
}
