/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package poly.cafe.ui;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import poly.cafe.dao.CategoryDAO;
import poly.cafe.dao.CategoryDAO;
import poly.cafe.entity.Category;
import poly.cafe.util.MsgBox;
import poly.cafe.util.XAuth;

/**
 *
 * @author ADMIN
 */
public class CategoryManagerJDialog extends javax.swing.JDialog {

    CategoryDAO dao = new CategoryDAO();
    int row = -1;

    /**
     * Creates new form CategoryManagerJDialog
     */
    public CategoryManagerJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
    }

    void init() {
        setLocationRelativeTo(null);
        setTitle("Loai nuoc");
        fillTable();
    }

    void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tblCategory.getModel();
        model.setRowCount(0);
        try {
            List<Category> list = dao.selectAll();
            for (Category cd : list) {
                Object[] row = {
                    cd.getId(), cd.getName(), false

                };
                model.addRow(row);
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vẫn dữ liệu!");
        }
    }

    private void selectAll() {
        for (int i = 0; i < tblCategory.getRowCount(); i++) {
            tblCategory.setValueAt(true, i, 2); // Cột thứ 7 là checkbox
        }
    }

    private void noSelectAll() {
        for (int i = 0; i < tblCategory.getRowCount(); i++) {
            tblCategory.setValueAt(false, i, 2); // Cột thứ 7 là checkbox
        }
    }

    public void deletedCategories() {
        if (!XAuth.isLogin()) {
            MsgBox.alert(this, "Bạn chưa đăng nhập");
            return;
        }

        if (MsgBox.confirm(this, "Bạn đang thực hiện chức năng xóa")) {
            boolean kq = false;
            int demDaXoa = 0;

            for (int i = tblCategory.getRowCount() - 1; i >= 0; i--) {
                Object value = tblCategory.getValueAt(i, 2);
                if (value instanceof Boolean && (Boolean) value) {
                    String CategoryId = tblCategory.getValueAt(i, 0).toString();

                    try {
                        dao.delete(CategoryId);
                        kq = true;
                        demDaXoa++;
                    } catch (Exception e) {
                        MsgBox.alert(this, "Lỗi khi xóa danh mục" + CategoryId + "':" + e.getMessage());
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
    if (txtIDCatelory.getText().trim().isEmpty()) {
        MsgBox.alert(this, "Vui lòng nhập mã danh mục (ID)!");
        return false;
    }
    if (txtIDNameCategory.getText().trim().isEmpty()) {
        MsgBox.alert(this, "Vui lòng nhập tên danh mục!");
        return false;
    }
    return true;
}

    void setForm(Category category) {
        txtIDCatelory.setText(category.getId());
        txtIDNameCategory.setText(category.getName());
    }

    public Category getForm() {
        Category categories = new Category();
        categories.setId(txtIDCatelory.getText());
        categories.setName(txtIDNameCategory.getText());
        return categories;
    }

    public void clearForm() {
        txtIDCatelory.setText("");
        txtIDNameCategory.setText("");
        row = -1;
    }

    void edit() {
        row = tblCategory.getSelectedRow();
        try {
            String id = tblCategory.getValueAt(row, 0).toString();
            Category cd = dao.selectByID(id);
            if (cd != null) {
                this.setForm(cd);
                Tabs.setSelectedIndex(1);
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    private void clearTableSelectionAndEditor() {
        // Bỏ chọn ô đang chỉnh sửa trên bảng nếu có
        TableCellEditor editor = tblCategory.getCellEditor();
        if (editor != null) {
            editor.stopCellEditing();
        }
        // Bỏ chọn tất cả hàng và cột
        tblCategory.clearSelection();
        this.requestFocusInWindow();
    }

    void insert() {
        Category category = getForm();

        // Kiểm tra trùng mã danh mục
        if (dao.selectByID(category.getId()) != null) {
            MsgBox.alert(this, "ID danh mục đã tồn tại!");
            txtIDCatelory.requestFocus();
            return;
        }

        try {
            dao.insert(category);
            fillTable();
            clearForm();
            MsgBox.alert(this, "Thêm danh mục thành công!");
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi thêm danh mục: " + e.getMessage());
        }
    }

    void update() {
        if (!ValidateForm()) {
            return;
        }
        Category category = getForm();

        // Kiểm tra xem ID có tồn tại không
        if (dao.selectByID(category.getId()) == null) {
            MsgBox.alert(this, "Không tồn tại ID danh mục để cập nhật");
            return;
        }

        try {
            dao.update(category);
            this.fillTable();
            this.clearForm();
            MsgBox.alert(this, "Cập nhật danh mục thành công!");
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi cập nhật: " + e.getMessage());
        }
    }

    void delete() {
        if (!XAuth.isLogin()) {
            MsgBox.alert(this, "Bạn chưa đăng nhập!");
            return;
        }

        String id = txtIDCatelory.getText().trim();
        if (id.isEmpty()) {
            MsgBox.alert(this, "Bạn chưa nhập ID danh mục cần xóa!");
            return;
        }

        if (dao.selectByID(id) == null) {
            MsgBox.alert(this, "Không tồn tại ID danh mục để xóa");
            return;
        }

        try {
            dao.delete(id);
            this.fillTable();
            this.clearForm();
            MsgBox.alert(this, "Xóa danh mục thành công!");
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi khi xóa: " + e.getMessage());
        }
    }

    // Chuyển đến bản ghi đầu tiên
    private void First() {
        if (tblCategory.getRowCount() > 0) {
            tblCategory.setRowSelectionInterval(0, 0);
            edit();
        }
    }

// Chuyển đến bản ghi trước
    private void Previous() {
        if (row > 0) {
            tblCategory.setRowSelectionInterval(row - 1, row - 1);
            edit();
        }
    }

// Chuyển đến bản ghi tiếp theo
    private void Next() {
        if (row < tblCategory.getRowCount() - 1) {
            tblCategory.setRowSelectionInterval(row + 1, row + 1);
            edit();
        }
    }

// Chuyển đến bản ghi cuối cùng
    private void Last() {
        int lastRow = tblCategory.getRowCount() - 1;
        if (lastRow >= 0) {
            tblCategory.setRowSelectionInterval(lastRow, lastRow);
            edit();
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        tblU = new javax.swing.JScrollPane();
        tblCategory = new javax.swing.JTable();
        btnSelectAll = new javax.swing.JButton();
        btnNoSelect = new javax.swing.JButton();
        btnDelectSelect = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtIDCatelory = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtIDNameCategory = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnFist = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblCategory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã loại", "Tên loại", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblCategory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblCategoryMousePressed(evt);
            }
        });
        tblU.setViewportView(tblCategory);

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
                .addContainerGap()
                .addComponent(tblU, javax.swing.GroupLayout.PREFERRED_SIZE, 641, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(316, 316, 316)
                .addComponent(btnSelectAll)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNoSelect)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDelectSelect)
                .addContainerGap(83, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(tblU, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSelectAll)
                    .addComponent(btnNoSelect)
                    .addComponent(btnDelectSelect))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        Tabs.addTab("Danh sách", jPanel1);

        jLabel1.setText("Mã loại");

        jLabel2.setText("Tên loại");

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

        btnClear.setText("Nhập mới");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(473, 473, 473)
                        .addComponent(btnFist)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPrev)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNext))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnAdd)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnUpdate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDelete)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnClear))
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIDNameCategory, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                            .addComponent(txtIDCatelory))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLast)
                .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIDCatelory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIDNameCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClear)
                    .addComponent(btnDelete)
                    .addComponent(btnUpdate)
                    .addComponent(btnAdd)
                    .addComponent(btnFist)
                    .addComponent(btnPrev)
                    .addComponent(btnNext)
                    .addComponent(btnLast))
                .addGap(165, 165, 165))
        );

        Tabs.addTab("Biểu mẩu", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Tabs)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFistActionPerformed
        // TODO add your handling code here:
    First();
    }//GEN-LAST:event_btnFistActionPerformed

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
        deletedCategories();
    }//GEN-LAST:event_btnDelectSelectActionPerformed

    private void tblCategoryMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCategoryMousePressed
        // TODO add your handling code here:
        if (evt.getClickCount()==2) 
            edit();
    }//GEN-LAST:event_tblCategoryMousePressed

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

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_btnClearActionPerformed

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
            java.util.logging.Logger.getLogger(CategoryManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CategoryManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CategoryManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CategoryManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CategoryManagerJDialog dialog = new CategoryManagerJDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelectSelect;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFist;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnNoSelect;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSelectAll;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTable tblCategory;
    private javax.swing.JScrollPane tblU;
    private javax.swing.JTextField txtIDCatelory;
    private javax.swing.JTextField txtIDNameCategory;
    // End of variables declaration//GEN-END:variables
}
