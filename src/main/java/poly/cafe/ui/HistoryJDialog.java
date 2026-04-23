/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package poly.cafe.ui;

import java.awt.Component;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import poly.cafe.dao.BillDAO;
import poly.cafe.entity.Bill;
import poly.cafe.util.MsgBox;
import poly.cafe.util.TimeRange;
import poly.cafe.util.XDate;
import poly.cafe.util.XImage;

/**
 *
 * @author ADMIN
 */
public class HistoryJDialog extends javax.swing.JDialog {

    /**
     * Creates new form HistoryJDialog
     */
    public HistoryJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
    }
    
    BillDAO daoBill = new BillDAO();
    int row = -1;
    private long billId = 0;

    void init() {
        setIconImage(XImage.getAppIcon());
        setLocationRelativeTo(null);
        setTitle("QUẢN LÝ PHIẾU BÁN HÀNG");

        // thêm sự kiện hàm selectTimeRange() cho nút cboTimeRanges khi chọn:
        cboTimeRanges.addActionListener(e -> selectTimeRange());
        cboTimeRanges.setSelectedIndex(0);
    }

    //Hàm kiểm tra nhập checkin và checkout:
    private boolean validateDate() {
        String beginText = txtBegin.getText().trim();
        String endText = txtEnd.getText().trim();

        if (beginText.isEmpty() || endText.isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập đầy đủ cả hai ngày.");
            return false;
        }

        Date begin, end;
        try {
            begin = XDate.parse(beginText, "dd/MM/yyyy");
            end = XDate.parse(endText, "dd/MM/yyyy");
        } catch (Exception e) {
            MsgBox.alert(this, "Ngày nhập không đúng định dạng (dd/MM/yyyy).");
            return false;
        }

        if (begin.after(end)) {
            MsgBox.alert(this, "'Từ ngày' phải trước hoặc bằng 'Đến ngày'.");
            return false;
        }

        return true;
    }

    void fillTableBill() {
    DefaultTableModel model = (DefaultTableModel) tblHistory.getModel();
    model.setRowCount(0);

    Date begin = XDate.parse(txtBegin.getText(), "dd/MM/yyyy");
    Date end = XDate.parse(txtEnd.getText(), "dd/MM/yyyy");

    Calendar cal = Calendar.getInstance();
    cal.setTime(end);
    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    cal.set(Calendar.MILLISECOND, 999);
    end = cal.getTime();

    try {
        List<Bill> list = daoBill.findByTimeRange(begin, end);
        for (Bill cd : list) {
            // ✅ Chỉ hiển thị các hóa đơn có trạng thái = 1
            if (cd.getStatus() == 1) {
                Object[] row = {
                    cd.getId(), cd.getCardId(),
                    cd.getCheckin(), cd.getCheckout(), cd.getStatus(),
                    cd.getUsername(), false // Thêm cột checkbox nếu có
                };
                model.addRow(row);
            }
        }
    } catch (Exception e) {
        MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
    }
    autoResizeColumnWidth(tblHistory);
}
    
    public void autoResizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 50; // chiều rộng tối thiểu
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 10, width);
            }

            // So sánh với header
            TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
            Component headerComp = headerRenderer.getTableCellRendererComponent(table, table.getColumnName(column), false, false, -1, column);
            width = Math.max(headerComp.getPreferredSize().width + 10, width);

            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    public void selectTimeRange() {
        TimeRange range = TimeRange.today();
        switch (cboTimeRanges.getSelectedIndex()) {
            case 0 ->
                range = TimeRange.today();
            case 1 ->
                range = TimeRange.thisWeek();
            case 2 ->
                range = TimeRange.thisMonth();
            case 3 ->
                range = TimeRange.thisQuarter();
            case 4 ->
                range = TimeRange.thisYear();
        }
        txtBegin.setText(XDate.format(range.getBegin(), "dd/MM/yyyy"));
        txtEnd.setText(XDate.format(range.getEnd(), "dd/MM/yyyy"));
        this.fillTableBill();
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtBegin = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtEnd = new javax.swing.JTextField();
        btnFilter = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHistory = new javax.swing.JTable();
        cboTimeRanges = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Từ ngày");

        jLabel2.setText("Đến ngày");

        btnFilter.setText("Lọc");
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterActionPerformed(evt);
            }
        });

        tblHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã phiếu", "Thẻ số", "Thời điểm tạo phiếu", "Thời điểm thanh toán", "Trạng thái"
            }
        ));
        tblHistory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblHistoryMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblHistory);

        cboTimeRanges.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hôm nay", "Tuần này", "Tháng này", "Quý này", "Năm nay", " ", " " }));
        cboTimeRanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTimeRangesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtBegin, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnFilter)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboTimeRanges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(108, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtBegin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(txtEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboTimeRanges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilter))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed
        // TODO add your handling code here:
        if (validateDate()) {
            fillTableBill();
        }
    }//GEN-LAST:event_btnFilterActionPerformed

    private void tblHistoryMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHistoryMousePressed
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
        }
    }//GEN-LAST:event_tblHistoryMousePressed

    private void cboTimeRangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTimeRangesActionPerformed
        // TODO add your handling code here:
        selectTimeRange();
    }//GEN-LAST:event_cboTimeRangesActionPerformed

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
            java.util.logging.Logger.getLogger(HistoryJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HistoryJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HistoryJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HistoryJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                HistoryJDialog dialog = new HistoryJDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblHistory;
    private javax.swing.JTextField txtBegin;
    private javax.swing.JTextField txtEnd;
    // End of variables declaration//GEN-END:variables
}
