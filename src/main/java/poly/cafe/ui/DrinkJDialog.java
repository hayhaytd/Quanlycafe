/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package poly.cafe.ui;

import java.util.List;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import poly.cafe.dao.CategoryDAO;
import poly.cafe.dao.DrinkDAO;
import poly.cafe.entity.Bill;
import poly.cafe.entity.Category;
import poly.cafe.entity.Drink;
import poly.cafe.dao.BillDetailDAO;
import poly.cafe.entity.BillDetail;
import poly.cafe.util.MsgBox;

/**
 *
 * @author ADMIN
 */
public class DrinkJDialog extends javax.swing.JDialog {

    private BillDetailDAO billDetailDAO = new BillDetailDAO();

    /**
     * Creates new form DrinkJDialog
     */
    private Bill bill;

    public void setBill(Bill bill) {
        this.bill = bill;
    }
    private CategoryDAO categoryDAO = new CategoryDAO();
    private DrinkDAO drinksDAO = new DrinkDAO();
    int row = -1;
    private List<Category> categories;
    private DefaultTableModel catModel, drinkModel;

    /**
     * Creates new form DrinkJDialog
     */
    public DrinkJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
    }

    void init() {
        setLocationRelativeTo(null);
        setTitle("Đồ Uống");
        // 3. Bảng Categories (bên trái) chỉ 1 cột "Tên loại nước"
        catModel = new DefaultTableModel(new String[]{"Tên loại nước"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tblCategories.setModel(catModel);
        tblCategories.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 4. Load dữ liệu Categories vào cả List và TableModel
        categories = categoryDAO.selectAll();
        catModel.setRowCount(0);
        for (Category c : categories) {
            catModel.addRow(new Object[]{c.getName()});
        }
        drinkModel = new DefaultTableModel(
                new String[]{"Mã", "Tên", "Đơn giá", "Giảm giá"}, 0);
        tblDrinks.setModel(drinkModel);
        // giữ bảng phải trống (chỉ header) khi mới mở
        drinkModel.setRowCount(0);

        // 7. Listener: khi chọn 1 dòng bên tblNameDrinks thì load đồ uống tương ứng
        tblCategories.getSelectionModel().addListSelectionListener(evt -> {
            if (!evt.getValueIsAdjusting()) {
                int r = tblCategories.getSelectedRow();
                if (r >= 0) {
                    // lấy categoryId từ list đã cache
                    String catId = categories.get(r).getId();
                    // clear model
                    drinkModel.setRowCount(0);
                    // load lại dữ liệu
                    for (Drink d : drinksDAO.selectByCategoryId(catId)) {
                        drinkModel.addRow(new Object[]{
                            d.getId(),
                            d.getName(),
                            String.format("%.1f VND", d.getUnitPrice()),
                            String.format("%.0f%%", d.getDiscount()),});
                    }
                }
            }
        });
    }

    private void addDrinkToBill() {
        int selectedRow = tblDrinks.getSelectedRow();
        if (selectedRow >= 0 && bill != null) {
            try {
                // Get drink data from the selected row
                String drinkId = (String) drinkModel.getValueAt(selectedRow, 0);
                String drinkName = (String) drinkModel.getValueAt(selectedRow, 1);

                // Find the actual drink object to get price and discount
                Drink selectedDrink = null;
                for (Drink d : drinksDAO.selectAll()) {
                    if (d.getId().equals(drinkId)) {
                        selectedDrink = d;
                        break;
                    }
                }

                if (selectedDrink == null) {
                    MsgBox.alert(this, "Không tìm thấy thông tin đồ uống!");
                    return;
                }

                // Show quantity input dialog
                String quantityStr = MsgBox.prompt(this, "Nhập số lượng cho " + drinkName + ":");
                if (quantityStr == null || quantityStr.trim().isEmpty()) {
                    return; // User cancelled
                }

                int quantity;
                try {
                    quantity = Integer.parseInt(quantityStr.trim());
                    if (quantity <= 0) {
                        MsgBox.alert(this, "Số lượng phải lớn hơn 0!");
                        return;
                    }
                } catch (NumberFormatException e) {
                    MsgBox.alert(this, "Vui lòng nhập số hợp lệ!");
                    return;
                }

                // Check if this drink already exists in the bill
                BillDetail existingDetail = billDetailDAO.selectByBillDrinkId(bill.getId(), drinkId);

                if (existingDetail != null) {
                    // Update existing quantity
                    existingDetail.setQuantity(existingDetail.getQuantity() + quantity);
                    billDetailDAO.update(existingDetail);
                    MsgBox.alert(this, "Đã cập nhật số lượng " + drinkName + "!");
                } else {
                    // Create new bill detail
                    BillDetail newDetail = new BillDetail();
                    newDetail.setBillId(bill.getId());
                    newDetail.setDrinkId(drinkId);
                    newDetail.setDrinkName(drinkName);
                    newDetail.setUnitPrice(selectedDrink.getUnitPrice());
                    newDetail.setDiscount(selectedDrink.getDiscount());
                    newDetail.setQuantity(quantity);

                    billDetailDAO.insert(newDetail);
                    MsgBox.alert(this, "Đã thêm " + quantity + " " + drinkName + " vào hóa đơn!");
                }

                // Close the dialog
                this.dispose();

            } catch (Exception e) {
                MsgBox.alert(this, "Lỗi khi thêm đồ uống: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            MsgBox.alert(this, "Vui lòng chọn một đồ uống!");
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tblCategories = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDrinks = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblCategories.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Loại đồ uống"
            }
        ));
        tblCategories.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblCategoriesMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblCategories);

        tblDrinks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã đồ uống", "Tên đồ uống", "Đơn giá", "Giảm giá", "Trạng thái", "Title 6"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblDrinks.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDrinksMousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(tblDrinks);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(33, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(22, 22, 22))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblCategoriesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCategoriesMousePressed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_tblCategoriesMousePressed

    private void tblDrinksMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDrinksMousePressed
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) { // Double click to add drink
            addDrinkToBill();
        }
    }//GEN-LAST:event_tblDrinksMousePressed

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
            java.util.logging.Logger.getLogger(DrinkJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DrinkJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DrinkJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DrinkJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DrinkJDialog dialog = new DrinkJDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblCategories;
    private javax.swing.JTable tblDrinks;
    // End of variables declaration//GEN-END:variables
}
