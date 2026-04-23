/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package poly.cafe.ui;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import poly.cafe.dao.CardDAO;
import poly.cafe.entity.Card;
import poly.cafe.util.MsgBox;
import poly.cafe.util.XAuth;

/**
 *
 * @author ADMIN
 */
public class CardManagerJDialog extends javax.swing.JDialog {

    CardDAO dao = new CardDAO();
    int row = -1;

    /**
     * Creates new form CardManagerJDialog
     */
    public CardManagerJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
    }

    void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tblCard.getModel();
        model.setRowCount(0);
        try {
            List<Card> list = dao.selectAll();
            for (Card cd : list) {
                Object[] row = {
                    cd.getId(), cd.getStatus(), false

                };
                model.addRow(row);
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vẫn dữ liệu!");
        }
    }

    void init() {
        setLocationRelativeTo(null);
        setTitle("Loai nuoc");
        fillTable();
    }

    private void selectAll() {
        for (int i = 0; i < tblCard.getRowCount(); i++) {
            tblCard.setValueAt(true, i, 2); // Cột thứ 7 là checkbox
        }
    }

    private void noSelectAll() {
        for (int i = 0; i < tblCard.getRowCount(); i++) {
            tblCard.setValueAt(false, i, 2); // Cột thứ 7 là checkbox
        }
    }

    public void deletedCards() {
        if (!XAuth.isLogin()) {
            MsgBox.alert(this, "Bạn chưa đăng nhập");
            return;
        }

        if (MsgBox.confirm(this, "Bạn đang thực hiện chức năng xóa")) {
            boolean kq = false;
            int demDaXoa = 0;

            for (int i = tblCard.getRowCount() - 1; i >= 0; i--) {
                Object value = tblCard.getValueAt(i, 2);
                if (value instanceof Boolean && (Boolean) value) {
                    // Lấy cardId kiểu Integer
                    Integer cardId = null;
                    Object idObj = tblCard.getValueAt(i, 0);
                    if (idObj instanceof Integer) {
                        cardId = (Integer) idObj;
                    } else {
                        try {
                            cardId = Integer.parseInt(idObj.toString());
                        } catch (NumberFormatException e) {
                            MsgBox.alert(this, "Mã danh mục không hợp lệ tại dòng " + i);
                            continue;
                        }
                    }

                    // Giả sử status = 0 là đã xóa (thay vì xóa vật lý)
                    int status = 0;

                    try {
                        // Nếu bạn muốn xóa vật lý thì dùng dao.delete(cardId);
                        // Nếu muốn cập nhật status thì gọi hàm cập nhật:
                        dao.delete(i);

                        kq = true;
                        demDaXoa++;
                    } catch (Exception e) {
                        MsgBox.alert(this, "Lỗi khi cập nhật trạng thái danh mục " + cardId + ": " + e.getMessage());
                    }
                }
            }

            if (kq) {
                MsgBox.alert(this, "Cập nhật trạng thái xóa thành công " + demDaXoa + " danh mục");
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
        String idText = txtIDCard.getText().trim();
        if (idText.isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập mã danh mục (ID)!");
            return false;
        }
        try {
            Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            MsgBox.alert(this, "Mã danh mục (ID) phải là số nguyên!");
            return false;
        }

        // Thay vì kiểm tra txtName, kiểm tra radio button trạng thái đã chọn
        if (!rdbOperating.isSelected() && !rdoError.isSelected() && !rdoLose.isSelected()) {
            MsgBox.alert(this, "Vui lòng chọn trạng thái!");
            return false;
        }

        return true;
    }
    
    void setForm(Card Card) {
    txtIDCard.setText(String.valueOf(Card.getId())); // chuyển Integer sang String

    // Đặt trạng thái radio dựa theo status int
    int status = Card.getStatus();
    if (status == 0) {
        rdbOperating.setSelected(true);  // ví dụ 0 tương ứng với jRadioButton4
    } else if (status == 1) {
        rdoError.setSelected(true);  // ví dụ 1 tương ứng với jRadioButton5
    } else if (status == 2) {
        rdoLose.setSelected(true);  // ví dụ 2 tương ứng với jRadioButton6
    } else {
        // Nếu không thuộc 3 trạng thái trên thì bỏ chọn hết
        rdbOperating.setSelected(false);
        rdoError.setSelected(false);
        rdoLose.setSelected(false);
    }
}
    
    public Card getForm() {
    Card card = new Card();

    // Chuyển id từ String sang Integer
    try {
        int id = Integer.parseInt(txtIDCard.getText().trim());
        card.setId(id);
    } catch (NumberFormatException e) {
        card.setId(0); // hoặc xử lý lỗi phù hợp
    }

    // Lấy status từ radio button
    int status = 0; // mặc định
    if (rdbOperating.isSelected()) {
        status = 0;
    } else if (rdoError.isSelected()) {
        status = 1;
    } else if (rdoLose.isSelected()) {
        status = 2;
    }
    card.setStatus(status);

    return card;
}
    
    public void clearForm() {
    txtIDCard.setText("");
    // Không có txtName nữa, thay bằng reset trạng thái radio button
    // Ví dụ nếu có 3 radio button: rdbStatus1, rdbStatus2, rdbStatus3
    rdbOperating.setSelected(true); // chọn mặc định trạng thái 0
    rdoError.setSelected(false);
    rdoLose.setSelected(false);
    row = -1;
}
    
    void edit() {
    row = tblCard.getSelectedRow();
    if (row < 0) {
        MsgBox.alert(this, "Vui lòng chọn một thẻ để sửa!");
        return;
    }
    try {
        Object idObj = tblCard.getValueAt(row, 0);
        Integer id = null;
        if (idObj instanceof Integer) {
            id = (Integer) idObj;
        } else {
            id = Integer.parseInt(idObj.toString());
        }

        Card card = dao.selectByID(id);
        if (card != null) {
            this.setForm(card);
            Tabs.setSelectedIndex(1);  // Chuyển sang tab biểu mẫu nếu dùng jTabbedPane1
        }
    } catch (Exception e) {
        MsgBox.alert(this, "Lỗi truy vấn dữ liệu: " + e.getMessage());
    }
}

    
    private void clearTableSelectionAndEditor() {
    // Bỏ chọn ô đang chỉnh sửa trên bảng nếu có
    TableCellEditor editor = tblCard.getCellEditor();
    if (editor != null) {
        editor.stopCellEditing();
    }
    // Bỏ chọn tất cả hàng và cột
    tblCard.clearSelection();
    this.requestFocusInWindow();
}

    void insert() {
    Card card = getForm();

    // Kiểm tra trùng mã thẻ
    if (dao.selectByID(card.getId()) != null) {
        MsgBox.alert(this, "ID thẻ đã tồn tại!");
        txtIDCard.requestFocus();
        return;
    }

    try {
        dao.insert(card);
        fillTable();
        clearForm();
        MsgBox.alert(this, "Thêm thẻ thành công!");
    } catch (Exception e) {
        MsgBox.alert(this, "Lỗi thêm thẻ: " + e.getMessage());
    }
}

    
    void update() {
    if (!ValidateForm()) {
        return;
    }
    Card card = getForm();

    // Kiểm tra xem ID có tồn tại không
    if (dao.selectByID(card.getId()) == null) {
        MsgBox.alert(this, "Không tồn tại ID thẻ để cập nhật");
        return;
    }

    try {
        dao.update(card);
        this.fillTable();
        this.clearForm();
        MsgBox.alert(this, "Cập nhật thẻ thành công!");
    } catch (Exception e) {
        MsgBox.alert(this, "Lỗi cập nhật: " + e.getMessage());
    }
}

    void delete() {
    if (!XAuth.isLogin()) {
        MsgBox.alert(this, "Bạn chưa đăng nhập!");
        return;
    }

    String text = txtIDCard.getText().trim();
    if (text.isEmpty()) {
        MsgBox.alert(this, "Bạn chưa nhập ID thẻ cần xóa!");
        return;
    }
    int id = Integer.parseInt(text);
    if (dao.selectByID(id) == null) {
        MsgBox.alert(this, "Không tồn tại ID thẻ để xóa");
        return;
    }

    try {
        dao.delete(id);
        this.fillTable();
        this.clearForm();
        MsgBox.alert(this, "Xóa thẻ thành công!");
    } catch (Exception e) {
        MsgBox.alert(this, "Lỗi khi xóa: " + e.getMessage());
    }
}
    
    // Chuyển đến bản ghi đầu tiên
    private void First() {
        if (tblCard.getRowCount() > 0) {
            tblCard.setRowSelectionInterval(0, 0);
            edit();
        }
    }

// Chuyển đến bản ghi trước
    private void Previous() {
        if (row > 0) {
            tblCard.setRowSelectionInterval(row - 1, row - 1);
            edit();
        }
    }

// Chuyển đến bản ghi tiếp theo
    private void Next() {
        if (row < tblCard.getRowCount() - 1) {
            tblCard.setRowSelectionInterval(row + 1, row + 1);
            edit();
        }
    }

// Chuyển đến bản ghi cuối cùng
    private void Last() {
        int lastRow = tblCard.getRowCount() - 1;
        if (lastRow >= 0) {
            tblCard.setRowSelectionInterval(lastRow, lastRow);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCard = new javax.swing.JTable();
        btnSelectAll = new javax.swing.JButton();
        btnNoSelect = new javax.swing.JButton();
        btnDeleteSelect = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtIDCard = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        rdbOperating = new javax.swing.JRadioButton();
        rdoError = new javax.swing.JRadioButton();
        rdoLose = new javax.swing.JRadioButton();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClearForm = new javax.swing.JButton();
        btnFist = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        Tabs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                TabsMousePressed(evt);
            }
        });

        tblCard.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã thẻ", "Trang  thái", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblCardMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblCard);

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

        btnDeleteSelect.setText("Xóa mục đã chọn");
        btnDeleteSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSelectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnSelectAll)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNoSelect)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDeleteSelect))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSelectAll)
                    .addComponent(btnNoSelect)
                    .addComponent(btnDeleteSelect))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        Tabs.addTab("Danh sách", jPanel1);

        jLabel3.setText("Mã thẻ");

        txtIDCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDCardActionPerformed(evt);
            }
        });

        jLabel4.setText("Trạng thái");

        rdbOperating.setText("Operating");

        rdoError.setText("Error");

        rdoLose.setText("Lose");

        btnAdd.setText("Nhập mới");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnUpdate.setText("Cập nhập");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setText("Xóa");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnClearForm.setText("Nhập mới");
        btnClearForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearFormActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(txtIDCard, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(rdbOperating)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rdoError)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rdoLose)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClearForm)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnFist)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPrev)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNext)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLast)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIDCard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdbOperating)
                    .addComponent(rdoError)
                    .addComponent(rdoLose))
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete)
                    .addComponent(btnClearForm)
                    .addComponent(btnFist)
                    .addComponent(btnPrev)
                    .addComponent(btnNext)
                    .addComponent(btnLast))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Tabs.addTab("Biểu mẫu", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Tabs)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(Tabs)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblCardMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCardMousePressed
        // TODO add your handling code here:
        if (evt.getClickCount()==2)
        edit();
    }//GEN-LAST:event_tblCardMousePressed

    private void btnSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectAllActionPerformed
        // TODO add your handling code here:
        selectAll();
    }//GEN-LAST:event_btnSelectAllActionPerformed

    private void btnNoSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNoSelectActionPerformed
        // TODO add your handling code here:
        noSelectAll();
    }//GEN-LAST:event_btnNoSelectActionPerformed

    private void btnDeleteSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSelectActionPerformed
        // TODO add your handling code here:
        deletedCards();
    }//GEN-LAST:event_btnDeleteSelectActionPerformed

    private void txtIDCardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIDCardActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIDCardActionPerformed

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

    private void TabsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabsMousePressed
        // TODO add your handling code here:

    }//GEN-LAST:event_TabsMousePressed

    private void btnFistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFistActionPerformed
        // TODO add your handling code here:
        First();
    }//GEN-LAST:event_btnFistActionPerformed

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
            java.util.logging.Logger.getLogger(CardManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CardManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CardManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CardManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CardManagerJDialog dialog = new CardManagerJDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDeleteSelect;
    private javax.swing.JButton btnFist;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnNoSelect;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSelectAll;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rdbOperating;
    private javax.swing.JRadioButton rdoError;
    private javax.swing.JRadioButton rdoLose;
    private javax.swing.JTable tblCard;
    private javax.swing.JTextField txtIDCard;
    // End of variables declaration//GEN-END:variables
}
