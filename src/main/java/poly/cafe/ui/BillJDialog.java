/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package poly.cafe.ui;

import java.awt.Dialog;
import java.util.List;
import poly.cafe.dao.BillDAO;
import poly.cafe.dao.BillDetailDAO;
import poly.cafe.entity.Bill;
import poly.cafe.entity.BillDetail;
import poly.cafe.util.MsgBox;
import java.awt.Frame;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class BillJDialog extends javax.swing.JDialog {

    private int cardId;
    private Bill bill; // The bill to display and edit
    private BillDAO billDAO = new BillDAO();
    private BillDetailDAO billDetailDao = new BillDetailDAO();
    private List<BillDetail> billDetails;

    public BillJDialog(Dialog owner, boolean modal, int cardId) {
        super(owner, modal);
        initComponents();
        this.cardId = cardId;
        setTitle("HÓA ĐƠN - Thẻ #" + cardId);
        setLocationRelativeTo(owner); // căn giữa so với cửa sổ cha
    }

    public BillJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void fillBillDetails() {
        try {
            if (bill != null) {
                billDetails = billDetailDao.selectBillId_lab4(bill.getId());
                DefaultTableModel model = (DefaultTableModel) tblBillDetails.getModel();
                model.setRowCount(0); // Clear existing rows

                for (BillDetail detail : billDetails) {
                    // Calculate discounted price (discount is stored as 0-100)
                    double discountedPrice = detail.getUnitPrice() * (1 - detail.getDiscount());
                    double total = discountedPrice * detail.getQuantity();

                    Object[] row = {
                        false, // checkbox column
                        detail.getDrinkName(),
                        detail.getUnitPrice(),
                        detail.getDiscount(), // Show as number (will be interpreted as percentage)
                        detail.getQuantity(),
                        total // calculated total with percentage discount
                    };
                    model.addRow(row);
                }
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi khi tải chi tiết hóa đơn: " + e.getMessage());
        }
    }

    public void removeDrinks() { // xóa đồ uống được tích chọn
        if (bill != null && bill.getStatus() == 0) {
            for (int i = 0; i < tblBillDetails.getRowCount(); i++) {
                Boolean checked = (Boolean) tblBillDetails.getValueAt(i, 0);
                if (checked) {
                    billDetailDao.delete(billDetails.get(i).getId());
                }
            }
            this.fillBillDetails();
        } else {
            MsgBox.alert(this, "Không thể xóa đồ uống từ hóa đơn đã thanh toán!");
        }
    }

    public void showDrinkJDialog() { // hiển thị cửa sổ chọn và bổ sung đồ uống
        if (bill != null && bill.getStatus() == 0) {
            // Fix: Find the root frame instead of casting owner directly
            Frame parentFrame = null;
            java.awt.Window owner = this.getOwner();

            // Traverse up the window hierarchy to find a Frame
            while (owner != null) {
                if (owner instanceof Frame) {
                    parentFrame = (Frame) owner;
                    break;
                }
                owner = owner.getOwner();
            }

            // Create DrinkJDialog with the found frame (or null if no frame found)
            DrinkJDialog dialog = new DrinkJDialog(parentFrame, true);
            dialog.setBill(bill); // Khai báo vào DrinkJDialog @Setter Bill bill
            dialog.setVisible(true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    BillJDialog.this.fillBillDetails();
                }
            });
        } else {
            MsgBox.alert(this, "Không thể thêm đồ uống vào hóa đơn đã thanh toán!");
        }
    }

    public void updateQuantity() { // thay đổi số lượng đồ uống
        if (bill != null && bill.getStatus() == 0) { // chưa thanh toán hoặc chưa bị canceled
            int selectedRow = tblBillDetails.getSelectedRow();
            if (selectedRow >= 0) {
                String input = MsgBox.prompt(this, "Số lượng mới?");
                if (input != null && input.length() > 0) {
                    try {
                        int newQuantity = Integer.parseInt(input);
                        if (newQuantity > 0) {
                            BillDetail detail = billDetails.get(selectedRow);
                            detail.setQuantity(newQuantity);
                            billDetailDao.update(detail);
                            this.fillBillDetails();
                        } else {
                            MsgBox.alert(this, "Số lượng phải lớn hơn 0!");
                        }
                    } catch (NumberFormatException e) {
                        MsgBox.alert(this, "Vui lòng nhập số hợp lệ!");
                    }
                }
            } else {
                MsgBox.alert(this, "Vui lòng chọn một dòng để cập nhật!");
            }
        } else {
            MsgBox.alert(this, "Không thể cập nhật hóa đơn đã thanh toán!");
        }
    }

    public void checkout() {
        if (bill != null && MsgBox.confirm(this, "Bạn muốn thanh toán phiếu bán hàng?")) {
            try {
                // 1. Complete the current bill
                bill.setStatus(1); // 1 = Completed
                bill.setCheckout(new Date());
                billDAO.update(bill);

                // 2. Create a new bill for the same card with incremented ID logic
                Bill newBill = new Bill();
                newBill.setCardId(bill.getCardId());
                newBill.setUsername(bill.getUsername()); // Use same username
                newBill.setCheckin(new Date());
                newBill.setStatus(0); // 0 = Servicing (new unpaid bill)

                // Insert the new bill (ID will auto-increment)
                billDAO.insert(newBill);

                // 3. Get the newly created bill to display
                Bill createdBill = billDAO.selectByIDStatus0(bill.getCardId());

                if (createdBill != null) {
                    // Update the form to show the new bill
                    this.setForm(createdBill);
                    MsgBox.alert(this, "Thanh toán thành công! Đã tạo hóa đơn mới #" + createdBill.getId());
                } else {
                    // If failed to create new bill, just show the completed bill
                    this.setForm(bill);
                    MsgBox.alert(this, "Thanh toán thành công!");
                }

            } catch (Exception e) {
                MsgBox.alert(this, "Lỗi khi thanh toán: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    void setForm(Bill bill) { // hiển thị bill lên form
        this.bill = bill; // Store the bill reference

        if (bill != null) {
            txtId.setText(String.valueOf(bill.getId()));
            txtCardId.setText("Card #" + bill.getCardId());
            txtUsername.setText(bill.getUsername());

            // Fix date formatting - use SimpleDateFormat
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
            txtCheckin.setText(sdf.format(bill.getCheckin()));

            // Handle status display
            String[] statuses = {"Servicing", "Completed", "Canceled"};
            txtStatus.setText(statuses[bill.getStatus()]);

            // Handle checkout time - check for null
            if (bill.getCheckout() != null) {
                txtCheckout.setText(sdf.format(bill.getCheckout()));
            } else {
                txtCheckout.setText("");
            }

            // Set field editability based on bill status
            boolean editable = (bill.getStatus() == 0);
            btnAdd.setEnabled(editable);
            btnCancel.setEnabled(editable);
            btnCheckout.setEnabled(editable);
            btnRemove.setEnabled(editable);

            // Make text fields read-only
            txtId.setEditable(false);
            txtCardId.setEditable(false);
            txtUsername.setEditable(false);
            txtCheckin.setEditable(false);
            txtCheckout.setEditable(false);
            txtStatus.setEditable(false);

            // Load bill details
            fillBillDetails();
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtCardId = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtCheckin = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtStatus = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtCheckout = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBillDetails = new javax.swing.JTable();
        btnRemove = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnCheckout = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Mã phiếu");

        jLabel2.setText("Thẻ số");

        jLabel3.setText("Thời điểm đặt hàng");
        jLabel3.setToolTipText("");

        jLabel4.setText("Nhân viên");

        jLabel5.setText("Trạng thái");

        jLabel6.setText("Thời điểm thanh toán");

        tblBillDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "", "Đồ uống", "Đơn giá", "Giảm giá", "Số lượng", "Thành tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblBillDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBillDetailsMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblBillDetailsMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblBillDetails);

        btnRemove.setText("Xóa đồ uống");
        btnRemove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRemoveMouseClicked(evt);
            }
        });
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        btnAdd.setText("Thêm đồ uống");
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddMouseClicked(evt);
            }
        });
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnCheckout.setText("Thanh toán");
        btnCheckout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckoutActionPerformed(evt);
            }
        });

        btnCancel.setText("Hủy phiếu");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(txtStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(txtCheckout, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtCardId, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(txtCheckin, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnRemove)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCheckout)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtCheckin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCardId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addGap(28, 28, 28)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtCheckout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addGap(27, 27, 27)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCheckout)
                        .addComponent(btnCancel))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnRemove)
                        .addComponent(btnAdd)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblBillDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBillDetailsMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.updateQuantity();
        }
    }//GEN-LAST:event_tblBillDetailsMouseClicked

    private void btnRemoveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRemoveMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRemoveMouseClicked

    private void btnAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseClicked
        // TODO add your handling code here:
        this.showDrinkJDialog();
    }//GEN-LAST:event_btnAddMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        this.showDrinkJDialog();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnCheckoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckoutActionPerformed
        // TODO add your handling code here:
        checkout();
    }//GEN-LAST:event_btnCheckoutActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        if (bill != null && bill.getStatus() == 0) {
            if (MsgBox.confirm(this, "Bạn có chắc muốn hủy hóa đơn này?")) {
                bill.setStatus(2); // Set status to cancelled
                billDAO.update(bill);
                setForm(bill);
                MsgBox.alert(this, "Hóa đơn đã được hủy!");
            }
        } else {
            MsgBox.alert(this, "Không thể hủy hóa đơn đã thanh toán!");
        }
    }//GEN-LAST:event_btnCancelActionPerformed

    private void tblBillDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBillDetailsMousePressed
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.showDrinkJDialog();
        }
    }//GEN-LAST:event_tblBillDetailsMousePressed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        // TODO add your handling code here:
        removeDrinks();
    }//GEN-LAST:event_btnRemoveActionPerformed

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
            java.util.logging.Logger.getLogger(BillJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BillJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BillJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BillJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BillJDialog dialog = new BillJDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCheckout;
    private javax.swing.JButton btnRemove;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblBillDetails;
    private javax.swing.JTextField txtCardId;
    private javax.swing.JTextField txtCheckin;
    private javax.swing.JTextField txtCheckout;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtStatus;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
