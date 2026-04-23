/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package poly.cafe.ui;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import poly.cafe.dao.UserDAO;
import poly.cafe.util.XImage;
import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.table.TableCellEditor;
import poly.cafe.entity.User;
import poly.cafe.util.MsgBox;
import poly.cafe.util.XAuth;
/**
 *
 * @author ADMIN
 */
public class UserManagerJDialog extends javax.swing.JDialog {
    
    
    
    public UserManagerJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
    }

    JFileChooser fileChooser =new JFileChooser();
    UserDAO dao =new UserDAO();
    int row = -1;
    
    void init(){
        setIconImage(XImage.getAppIcon());
        setLocationRelativeTo(null);
        setTitle("QUẢN LÝ TÀI KHOẢN");
        fillTable();
    }
    
    
    public void chooseFile() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            XImage.save(file);
            ImageIcon scaledIcon = XImage.readScaled(file.getName(), lblImage);
            lblImage.setIcon(scaledIcon);
            lblImage.setToolTipText(file.getName());
        }
    }
    
    // Chuyển đến bản ghi đầu tiên
    private void First() {
        if (tblUser.getRowCount() > 0) {
            tblUser.setRowSelectionInterval(0, 0);
            edit();
        }
    }

// Chuyển đến bản ghi trước
    private void Previous() {
        if (row > 0) {
            tblUser.setRowSelectionInterval(row - 1, row - 1);
            edit();
        }
    }

// Chuyển đến bản ghi tiếp theo
    private void Next() {
        if (row < tblUser.getRowCount() - 1) {
            tblUser.setRowSelectionInterval(row + 1, row + 1);
            edit();
        }
    }

// Chuyển đến bản ghi cuối cùng
    private void Last() {
        int lastRow = tblUser.getRowCount() - 1;
        if (lastRow >= 0) {
            tblUser.setRowSelectionInterval(lastRow, lastRow);
            edit();
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        Tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        tblU = new javax.swing.JScrollPane();
        tblUser = new javax.swing.JTable();
        btnSelectAll = new javax.swing.JButton();
        btnNoSelect = new javax.swing.JButton();
        btnDelectSelect = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblImage = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtUserName = new javax.swing.JTextField();
        txtPassword1 = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        txtFullName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtPassword2 = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        rdoManager = new javax.swing.JRadioButton();
        rdoEmployee = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        rdoEnabled = new javax.swing.JRadioButton();
        rdoDisabled = new javax.swing.JRadioButton();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnFist = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnClearForm = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Tên Đăng nhập", "Mật khẩu", "Họ và tên", "Hình ảnh", "Vai trò", "Trạng thái", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblUserMousePressed(evt);
            }
        });
        tblU.setViewportView(tblUser);

        btnSelectAll.setText("Chọn tất cả");
        btnSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectAllActionPerformed(evt);
            }
        });

        btnNoSelect.setText("Bỏ chọn tất cả");
        btnNoSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNoSelectActionPerformed(evt);
            }
        });

        btnDelectSelect.setText("Xóa các mục chọn");
        btnDelectSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelectSelectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(310, 310, 310)
                        .addComponent(btnSelectAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnNoSelect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDelectSelect))
                    .addComponent(tblU, javax.swing.GroupLayout.PREFERRED_SIZE, 692, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 28, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(tblU, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSelectAll)
                    .addComponent(btnNoSelect)
                    .addComponent(btnDelectSelect))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        Tabs.addTab("Danh sách", jPanel1);

        lblImage.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblImage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblImageMousePressed(evt);
            }
        });

        jLabel1.setText("Tên đăng nhập");

        jLabel2.setText("Họ và tên");

        jLabel3.setText("Mật khẩu");

        jLabel4.setText("Xác nhận mật khẩu");

        jLabel5.setText("Vai trò");

        rdoManager.setText("Quản lý");

        rdoEmployee.setText("Nhân viên");

        jLabel6.setText("Trạng thái");

        rdoEnabled.setText("Hoạt động");

        rdoDisabled.setText("Tạm dừng");
        rdoDisabled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDisabledActionPerformed(evt);
            }
        });

        btnAdd.setText("Tạo mới");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnDelete.setText("Xóa");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnFist.setText("<");
        btnFist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFistActionPerformed(evt);
            }
        });

        btnPrev.setText("<<");
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnNext.setText(">>");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnLast.setText(">");
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        btnUpdate.setText("Cập nhập");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnClearForm.setText("Nhập mới");
        btnClearForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearFormActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(303, 303, 303)
                                .addComponent(btnFist)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPrev)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnNext))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtFullName))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtPassword1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(rdoManager, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(rdoEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtPassword2, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(rdoEnabled, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(rdoDisabled, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(btnAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClearForm)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLast)
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPassword1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPassword2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdoEnabled)
                            .addComponent(rdoDisabled)
                            .addComponent(rdoManager)
                            .addComponent(rdoEmployee)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(lblImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(74, 74, 74)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClearForm)
                    .addComponent(btnDelete)
                    .addComponent(btnUpdate)
                    .addComponent(btnAdd)
                    .addComponent(btnFist)
                    .addComponent(btnPrev)
                    .addComponent(btnNext)
                    .addComponent(btnLast))
                .addGap(165, 165, 165))
        );

        btnLast.getAccessibleContext().setAccessibleName("");

        Tabs.addTab("Biểu mẩu", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(34, Short.MAX_VALUE)
                .addComponent(Tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(Tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Tabs.getAccessibleContext().setAccessibleName("Ten dang nhap");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rdoDisabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDisabledActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoDisabledActionPerformed

    private void btnFistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFistActionPerformed
        // TODO add your handling code here:
        First();
    }//GEN-LAST:event_btnFistActionPerformed

    private void tblUserMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUserMousePressed
        // TODO add your handling code here:
        if (evt.getClickCount()==2) 
            edit();
    }//GEN-LAST:event_tblUserMousePressed

    private void btnSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectAllActionPerformed
        // TODO add your handling code here:
        selectAll();
    }//GEN-LAST:event_btnSelectAllActionPerformed

    private void btnNoSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNoSelectActionPerformed
        // TODO add your handling code here:
        noSelectAll();
    }//GEN-LAST:event_btnNoSelectActionPerformed

    private void btnDelectSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelectSelectActionPerformed
        // TODO add your handling code here:
        deleteUsers();
    }//GEN-LAST:event_btnDelectSelectActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        insert();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        update();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        delete();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnClearFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearFormActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_btnClearFormActionPerformed

    private void lblImageMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblImageMousePressed
        // TODO add your handling code here:
        chooseFile();
    }//GEN-LAST:event_lblImageMousePressed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
        Previous();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        Next();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        // TODO add your handling code here:
        Last();
    }//GEN-LAST:event_btnLastActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UserManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UserManagerJDialog dialog = new UserManagerJDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane Tabs;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClearForm;
    private javax.swing.JButton btnDelectSelect;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFist;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnNoSelect;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSelectAll;
    private javax.swing.JButton btnUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblImage;
    private javax.swing.JRadioButton rdoDisabled;
    private javax.swing.JRadioButton rdoEmployee;
    private javax.swing.JRadioButton rdoEnabled;
    private javax.swing.JRadioButton rdoManager;
    private javax.swing.JScrollPane tblU;
    private javax.swing.JTable tblUser;
    private javax.swing.JTextField txtFullName;
    private javax.swing.JPasswordField txtPassword1;
    private javax.swing.JPasswordField txtPassword2;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tblUser.getModel();
        model.setRowCount(0);
        try {
            List<User> list = dao.selectAll();
            for (User cd : list) {
                Object[] row = {
                    cd.getUsername(), cd.getPassword(),
                    cd.getFullname(), cd.getPhoto(), cd.isEnabled(),
                    cd.isManager()
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vẫn dữ liệu!");
        }
    }
private void selectAll() {
        for (int i = 0; i < tblUser.getRowCount(); i++) {
            tblUser.setValueAt(true, i, 6); // Cột thứ 7 là checkbox
        }
    }
private void noSelectAll() {
        for (int i = 0; i < tblUser.getRowCount(); i++) {
            tblUser.setValueAt(false, i, 6); // Cột thứ 7 là checkbox
        }
    }
// Xóa nhiều dòng sau khi check chon trên table:
    public void deleteUsers() {
        if (!XAuth.isLogin()) {
            MsgBox.alert(this, "Bạn chưa đăng nhập");
            return;
        }
        if (MsgBox.confirm(this, "Bạn đang thực hiện chức năng xóa")) {
            boolean kq = false;
            for (int i = tblUser.getRowCount() - 1; i >= 0; i--) {
                String username = tblUser.getValueAt(i, 0).toString(); // username
                Object value = tblUser.getValueAt(i, 6); // checkbox chọn xóa

                if (value != null && (Boolean) value) {
                    if (username.equalsIgnoreCase("admin")
                            || XAuth.user.getUsername().equalsIgnoreCase(username)) {
                        MsgBox.alert(this, "Không thể xóa tk admin hoặc tk đang được sử dụng!");
                        continue;
                    }
                    try {
                        dao.delete(username);
                        kq = true;
                    } catch (Exception e) {
                        // chi tiết phân lỗi cụ thể các bạn tự tham khảo thêm...
                        MsgBox.alert(this, "Lỗi: " + e.getMessage());
                        continue;
                    }
                }
            }

            if (kq) {
                MsgBox.alert(this, "Xóa thành công");
            } else {
                MsgBox.alert(this, "Bạn chưa chọn hàng nào để xóa!");
            }
            this.fillTable();
        }
    }
public boolean ValidateForm() {
        if (!XAuth.isLogin()) {
            MsgBox.alert(this, "Bạn chưa đăng nhập!");
            return false;
        }
        if (txtUserName.getText().isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập mã UserName!");
            return false;
        }
        if (txtFullName.getText().isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập mã Tên đầy đủ!");
            return false;
        }
        String pw1 = new String(txtPassword1.getPassword()).trim();
        String pw2 = new String(txtPassword2.getPassword()).trim();
        if (pw1.isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập Password1!");
            return false;
        }
        if (pw2.isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập Password2!");
            return false;
        }
        if (!pw1.equals(pw2)) {
            MsgBox.alert(this, "Password xác nhận chưa đúng!");
            return false;
        }
        if (lblImage.getIcon() == null) {
            MsgBox.alert(this, "Vui lòng upload hình!");
            return false;
        }
        return true;
    }

    void selectPicture() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            XImage.save(file);
            ImageIcon scaledIcon = XImage.readScaled(file.getName(), lblImage);
            lblImage.setIcon(scaledIcon);
            lblImage.setToolTipText(file.getName());
        }
    }
void setForm(User user) {
        txtUserName.setText(user.getUsername());
        txtPassword1.setText(user.getPassword());
        txtPassword2.setText(user.getPassword());
        txtFullName.setText(user.getFullname());

        if (user.isEnabled()) {
            rdoEnabled.setSelected(true);
        } else {
            rdoDisabled.setSelected(true);
        }

        if (user.isManager()) {
            rdoManager.setSelected(true);
        } else {
            rdoEmployee.setSelected(true);
        }
        lblImage.setToolTipText(user.getPhoto());
        lblImage.setIcon(XImage.readScaled(user.getPhoto(), lblImage));
    }
public User getForm() {
        User user = new User();
        user.setUsername(txtUserName.getText());
        user.setPassword(new String(txtPassword1.getPassword()));
        user.setFullname(txtFullName.getText());
        user.setPhoto(lblImage.getToolTipText());
        if (rdoManager.isSelected()) {
            user.setManager(true);
        } else {
            user.setManager(false);
        }
        if (rdoEnabled.isSelected()) {
            user.setEnabled(true);
        } else {
            user.setEnabled(false);
        }
        return user;
    }
public void clearForm() {
        txtUserName.setText("");
        txtPassword1.setText("");
        txtPassword2.setText("");
        txtFullName.setText("");
        rdoEnabled.setSelected(true);   // mặc định là enabled
        rdoManager.setSelected(false);  // mặc định là nhân viên
        // Đặt lại ảnh mặc định
        lblImage.setIcon(null);
        lblImage.setToolTipText("Chưa có hình ảnh!");
        row = -1;
    }
private void clearTableSelectionAndEditor() {
        // Bỏ chọn ô trên table sau khi click đôi chuột
        TableCellEditor editor = tblUser.getCellEditor();
        if (editor != null) {
            editor.stopCellEditing();
        }
        // Bỏ chọn hàng và cột
        tblUser.clearSelection();
        this.requestFocusInWindow();
    }
void edit() {
        row = tblUser.getSelectedRow();
        try {
            String username = tblUser.getValueAt(row, 0).toString();
            User cd = dao.selectByID(username);
            if (cd != null) {
                this.setForm(cd);
                Tabs.setSelectedIndex(1);
                clearTableSelectionAndEditor();
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }
void insert() {
        if (!ValidateForm()) {
            return;
        }
        User cd = getForm();
        // Kiểm tra trùng username
        if (dao.selectByID(cd.getUsername()) != null) {
            MsgBox.alert(this, "Username đã tồn tại!");
            txtUserName.requestFocus();
            return;
        }
        try {
            dao.insert(cd);
            this.fillTable();
            this.clearForm();
            MsgBox.alert(this, "Thêm dữ liệu thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi: " + e.getMessage());
        }
    }
void update() {
        if (!ValidateForm()) {
            return;
        }
        User cd = getForm();
        if (dao.selectByID(cd.getUsername()) == null) {
            MsgBox.alert(this, "Không tồn tại Username để cập nhật");
            return;
        }
        try {
            dao.update(cd);
            this.fillTable();
            this.clearForm();
            MsgBox.alert(this, "Cập nhật dữ liệu thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi: " + e.getMessage());
        }
    }
void delete() {
        if (!XAuth.isLogin()) {
            MsgBox.alert(this, "Bạn chưa đăng nhập!");
            return;
        }
        String id = txtUserName.getText().trim();
        if (id.isEmpty()) {
            MsgBox.alert(this, "Bạn chưa nhập Username cần xóa!");
            return;
        }
        if (dao.selectByID(id) == null) {
            MsgBox.alert(this, "Không tồn tại Username để xóa");
            return;
        }
        //ko cho xóa admin và tk đang dùng:
        if (id.equalsIgnoreCase("admin") || id.equalsIgnoreCase(XAuth.user.getUsername())) {
            MsgBox.alert(this, "Không được xóa tài khoản admin và tk đang sử dụng!");
            return;
        }

        try {
            dao.delete(id);
            this.fillTable();
            this.clearForm();
            MsgBox.alert(this, "Xóa dữ liệu thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi: " + e.getMessage());
        }
    }


}
