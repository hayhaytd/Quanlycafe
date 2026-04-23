/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package poly.cafe.ui;

import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import poly.cafe.dao.RevenueDAO;
import poly.cafe.entity.Revenue;
import poly.cafe.util.TimeRange;

/**
 *
 * @author tranb
 */
public class RevenueManagerJDialog extends javax.swing.JDialog {
    
    private RevenueDAO revenueDAO;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private DecimalFormat currencyFormat = new DecimalFormat("#,##0");
    
    public RevenueManagerJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        revenueDAO = new RevenueDAO();
        init();
    }
    
    private void init() {
        // Sử dụng TimeRange để thiết lập ngày mặc định (tháng hiện tại)
        TimeRange currentMonth = TimeRange.thisMonth();
        txtBegin.setText(dateFormat.format(currentMonth.getBegin()));
        txtEnd.setText(dateFormat.format(currentMonth.getEnd()));
        
        // Thiết lập event listeners cho ComboBox
        cboTimeRanges.addActionListener(this::cboTimeRangesActionPerformed);
        
        // Load dữ liệu ban đầu
        loadRevenueData();
    }
   
    
    private void cboTimeRangesActionPerformed(ActionEvent evt) {
        String selected = (String) cboTimeRanges.getSelectedItem();
        if (selected == null) return;
        
        TimeRange timeRange = null;
        
        switch (selected) {
            case "Hôm nay":
                timeRange = TimeRange.today();
                break;
            case "Tuần này":
                timeRange = TimeRange.thisWeek();
                break;
            case "Tháng này":
                timeRange = TimeRange.thisMonth();
                break;
            case "Năm nay":
                timeRange = TimeRange.thisYear();
                break;
        }
        
        if (timeRange != null) {
            txtBegin.setText(dateFormat.format(timeRange.getBegin()));
            txtEnd.setText(dateFormat.format(timeRange.getEnd()));
            loadRevenueData();
        }
    }
    
    private void loadRevenueData() {
        try {
            // Parse ngày từ text fields
            Date fromDate = dateFormat.parse(txtBegin.getText());
            Date toDate = dateFormat.parse(txtEnd.getText());
            
            // Điều chỉnh thời gian để bao gồm cả ngày cuối
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(toDate);
            cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
            cal.set(java.util.Calendar.MINUTE, 59);
            cal.set(java.util.Calendar.SECOND, 59);
            toDate = cal.getTime();
            
            // Load dữ liệu cho cả hai tab
            loadRevenueByCategory(fromDate, toDate);
            loadRevenueByUser(fromDate, toDate);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tải dữ liệu: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadRevenueByCategory(Date fromDate, Date toDate) {
        try {
            List<Revenue.ByCategory> data = revenueDAO.selectRevenueByCategory(fromDate, toDate);
            
            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{
                "Loại", "Doanh thu", "Số lượng", "Giá thấp nhất", "Giá trung bình"
            });
            
            double totalRevenue = 0;
            for (Revenue.ByCategory item : data) {
                model.addRow(new Object[]{
                    item.getCategory(),
                    currencyFormat.format(item.getRevenue()) + " VNĐ",
                    item.getQuantity(),
                    currencyFormat.format(item.getMinPrice()) + " VNĐ",
                    currencyFormat.format(item.getAvgPrice()) + " VNĐ"
                });
                totalRevenue += item.getRevenue();
            }
            
            // Thêm dòng tổng
            model.addRow(new Object[]{
                "TỔNG CỘNG",
                currencyFormat.format(totalRevenue) + " VNĐ",
                "",
                "",
                ""
            });
            
            tblByCategory.setModel(model);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tải dữ liệu doanh thu theo loại: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadRevenueByUser(Date fromDate, Date toDate) {
        try {
            List<Revenue.ByUser> data = revenueDAO.selectRevenueByUser(fromDate, toDate);
            
            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{
                "Nhân viên", "Doanh thu", "Số lượng", "Giá thấp nhất", "Giá cao nhất", "Giá trung bình"
            });
            
            SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            double totalRevenue = 0;
            
            for (Revenue.ByUser item : data) {
                model.addRow(new Object[]{
                    item.getUser(),
                    currencyFormat.format(item.getRevenue()) + " VNĐ",
                    item.getQuantity(),
                    "", // Không có giá thấp nhất cho user
                    "", // Không có giá cao nhất cho user  
                    ""  // Không có giá trung bình cho user
                });
                totalRevenue += item.getRevenue();
            }
            
            // Thêm dòng tổng
            model.addRow(new Object[]{
                "TỔNG CỘNG",
                currencyFormat.format(totalRevenue) + " VNĐ",
                "",
                "",
                "",
                ""
            });
            
            tblByUser.setModel(model);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tải dữ liệu doanh thu theo nhân viên: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
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
        jLabel2 = new javax.swing.JLabel();
        txtEnd = new javax.swing.JTextField();
        txtBegin = new javax.swing.JTextField();
        btnFilter = new javax.swing.JButton();
        cboTimeRanges = new javax.swing.JComboBox<>();
        tabs = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblByCategory = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblByUser = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Từ ngày:");

        jLabel2.setText("Đến ngày:");

        btnFilter.setText("Lọc");
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterActionPerformed(evt);
            }
        });

        cboTimeRanges.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hôm nay", "Tuần này", "Tháng này", "Năm nay", " ", " " }));

        tblByCategory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Loại", "Doanh thu", "Số lượng", "Giá thấp nhất", "Giá cao nhất", "Giá trung bình"
            }
        ));
        jScrollPane2.setViewportView(tblByCategory);

        tabs.addTab("Doanh thu từng loại", jScrollPane2);

        tblByUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Nhân viên", "Doanh thu", "Số Bill", "Bill đầu tiên", "Bill cuối cùng"
            }
        ));
        jScrollPane3.setViewportView(tblByUser);

        tabs.addTab("Doanh thu từng nhân viên", jScrollPane3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addGap(3, 3, 3)
                .addComponent(txtBegin, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFilter)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboTimeRanges, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(193, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabs)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(txtEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBegin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFilter)
                    .addComponent(cboTimeRanges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 879, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 486, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 22, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 21, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed
        // TODO add your handling code here:
        loadRevenueData();
    }//GEN-LAST:event_btnFilterActionPerformed

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
            java.util.logging.Logger.getLogger(RevenueManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RevenueManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RevenueManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RevenueManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                RevenueManagerJDialog dialog = new RevenueManagerJDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnFilter;
    private javax.swing.JComboBox<String> cboTimeRanges;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblByCategory;
    private javax.swing.JTable tblByUser;
    private javax.swing.JTextField txtBegin;
    private javax.swing.JTextField txtEnd;
    // End of variables declaration//GEN-END:variables
}
