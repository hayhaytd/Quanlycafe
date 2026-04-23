/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package poly.cafe.ui;

import java.awt.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import poly.cafe.dao.BillDAO;
import poly.cafe.dao.BillDetailDAO;
import poly.cafe.entity.Bill;
import poly.cafe.util.MsgBox;
import poly.cafe.util.TimeRange;
import poly.cafe.util.XAuth;
import poly.cafe.util.XDate;
import poly.cafe.util.XImage;
import poly.cafe.entity.BillDetail;

/**
 *
 * @author ADMIN
 */
public class BillManagerJDialog extends javax.swing.JDialog {

    BillDAO daoBill = new BillDAO();
    int row = -1;
    private long billId = 0;

    void init() {
        setIconImage(XImage.getAppIcon());
        setLocationRelativeTo(null);
        setTitle("QUẢN LÝ PHIẾU BÁN HÀNG");

        // thêm sự kiện hàm selectTimeRange() cho nút cboTimeRanges khi chọn:
        cboTimeRanges.addActionListener(e -> selectTimeRange());
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
        DefaultTableModel model = (DefaultTableModel) tblBill.getModel();
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
                Object[] row = {
                    cd.getId(), cd.getCardId(),
                    cd.getCheckin(), cd.getCheckout(), cd.getStatus(),
                    cd.getUsername(), false // Thêm cột checkbox
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
        autoResizeColumnWidth(tblBill);
    }

    void setFormBill(Bill bill) {
        txtId.setText(bill.getId().toString());
        txtCardId.setText(bill.getCardId().toString());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        // Check-in luôn có giá trị
        txtCheckin.setText(sdf.format(bill.getCheckin()));

        // Check-out có thể null
        if (bill.getCheckout() != null) {
            txtCheckout.setText(sdf.format(bill.getCheckout()));
        } else {
            txtCheckout.setText(""); // hoặc ghi rõ là "Chưa checkout"
        }

        txtUsername.setText(bill.getUsername());

        // Xử lý trạng thái
        if (bill.getStatus() == 0) { // chưa thanh toán
            rdoServicing.setSelected(true);
        } else if (bill.getStatus() == 1) { // đã thanh toán
            rdoCompleted.setSelected(true);
        } else {
            rdoCanceled.setSelected(true); // đã hủy
        }
    }

    public Bill getFormBill() {
        try {
            Bill bill = new Bill();
            bill.setId(Long.valueOf(txtId.getText()));
            bill.setCardId(Integer.valueOf(txtCardId.getText()));

            bill.setUsername(XAuth.user.getUsername());

            String input = txtCheckin.getText();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date checkinDate = sdf.parse(input); // có thể ném ParseException
            bill.setCheckin(checkinDate); // hoặc new java.sql.Date(checkinDate.getTime()) nếu cần

            //nếu tình trạng chưa thanh toán or hủy thì checkout null
            if (rdoCanceled.isSelected() || rdoServicing.isSelected()) {
                txtCheckout.setText("");
            } else {
                String formattedNow = sdf.format(new Date());
                txtCheckout.setText(formattedNow);
            }

            if (rdoServicing.isSelected()) {
                bill.setStatus(0);
            } else if (rdoCompleted.isSelected()) {
                bill.setStatus(1);
            } else {
                bill.setStatus(2);
            }

            return bill;
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Ngày check-in không đúng định dạng! Định dạng đúng là: dd/MM/yyyy HH:mm:ss");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID hoặc Card ID không hợp lệ! Phải là số.");
        }
        return null;
    }

    void fillTableBillDetail() {
        DefaultTableModel model = (DefaultTableModel) tblBillDetail.getModel();
        model.setRowCount(0);

        try {
            //billId = Long.valueOf(txtId.getText());
            BillDetailDAO daodt = new BillDetailDAO();
            List<BillDetail> list = daodt.selectBillId_lab4(billId);
            for (BillDetail cd : list) {
                var amount = cd.getUnitPrice() * cd.getQuantity() * (1 - cd.getDiscount());
                Object[] row = {
                    cd.getDrinkName(), cd.getUnitPrice(),
                    cd.getDiscount(), cd.getQuantity(), String.format("%.1f VNĐ", amount)
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vẫn dữ liệu!");
        }
    }
//set chiều rộng cột auto

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

    private void clearTableSelectionAndEditor() {
        // Bỏ chọn ô trên table sau khi click đôi chuột
        TableCellEditor editor = tblBill.getCellEditor();
        if (editor != null) {
            editor.stopCellEditing();
        }
        // Bỏ chọn hàng và cột
        tblBill.clearSelection();
        this.requestFocusInWindow();
    }
    // hàm này dùng khi double click dòng trên Bảng Bill sẽ hiện thị qua Form Biểu Mẫu

    void edit() {
        row = tblBill.getSelectedRow();
        try {
            billId = Long.valueOf(tblBill.getValueAt(row, 0).toString());
            Bill cd = daoBill.selectByID(billId);
            if (cd != null) {
                this.setFormBill(cd);
                tabs.setSelectedIndex(1);
                fillTableBillDetail();
                clearTableSelectionAndEditor();
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi: " + e.getMessage());
        }
    }

    private void selectAll() {
        for (int i = 0; i < tblBill.getRowCount(); i++) {
            tblBill.setValueAt(true, i, 6); // Đổi từ 5 thành 6
        }
    }

    private void noSelectAll() {
        for (int i = 0; i < tblBill.getRowCount(); i++) {
            tblBill.setValueAt(false, i, 6); // Đổi từ 5 thành 6
        }
    }

    public void clearForm() {
        txtId.setText("");
        txtCardId.setText("");
        txtCheckin.setText("");
        txtCheckout.setText("");
        txtUsername.setText("");
        rdoServicing.setSelected(true);   // mặc định là Servicing

        // Xóa bảng chi tiết hóa đơn
        DefaultTableModel model = (DefaultTableModel) tblBillDetail.getModel();
        model.setRowCount(0);

        row = -1;
        billId = 0;
    }

    private void createBill() {
        Bill bill = getFormBill();
        if (bill != null) {
            try {
                daoBill.insert(bill);
                fillTableBill();
                clearForm();
                MsgBox.alert(this, "Tạo mới hóa đơn thành công!");
            } catch (Exception e) {
                MsgBox.alert(this, "Lỗi tạo mới: " + e.getMessage());
            }
        }
    }

    private void checkAllItems() {
        DefaultTableModel model = (DefaultTableModel) tblBill.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(true, i, 6);
        }
    }

// Bỏ chọn tất cả checkbox
    private void uncheckAllItems() {
        DefaultTableModel model = (DefaultTableModel) tblBill.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(false, i, 6);
        }
    }

// Xóa các hóa đơn đã chọn
    private void deleteCheckedItems() {
        if (!XAuth.isLogin()) {
            MsgBox.alert(this, "Bạn chưa đăng nhập!");
            return;
        }

        if (MsgBox.confirm(this, "Bạn có chắc chắn muốn xóa các hóa đơn đã chọn?")) {
            DefaultTableModel model = (DefaultTableModel) tblBill.getModel();
            int deletedCount = 0;

            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                Boolean isChecked = (Boolean) model.getValueAt(i, 6); // Đổi thành index 6
                if (isChecked != null && isChecked) {
                    try {
                        Object idValue = model.getValueAt(i, 0);
                        if (idValue == null) {
                            MsgBox.alert(this, "Giá trị ID tại hàng " + i + " không hợp lệ!");
                            continue;
                        }

                        Long billId = Long.valueOf(idValue.toString());
                        daoBill.delete(billId);
                        model.removeRow(i);
                        deletedCount++;
                    } catch (Exception e) {
                        MsgBox.alert(this, "Lỗi khi xóa hóa đơn ID " + model.getValueAt(i, 0) + ": " + e.getMessage());
                    }
                }
            }

            if (deletedCount > 0) {
                MsgBox.alert(this, "Đã xóa " + deletedCount + " hóa đơn!");
                clearForm();
            } else {
                MsgBox.alert(this, "Không có hóa đơn nào được chọn để xóa!");
            }
        }
    }

// Cập nhật hóa đơn
    private void updateBill() {
        Bill bill = getFormBill();
        if (bill != null) {
            try {
                daoBill.update(bill);
                fillTableBill();
                MsgBox.alert(this, "Cập nhật hóa đơn thành công!");
            } catch (Exception e) {
                MsgBox.alert(this, "Lỗi cập nhật: " + e.getMessage());
            }
        }
    }

// Xóa hóa đơn
    private void deleteBill() {
        if (!XAuth.isLogin()) {
            MsgBox.alert(this, "Bạn chưa đăng nhập!");
            return;
        }

        String id = txtId.getText().trim();
        if (id.isEmpty() || id.equals("0")) {
            MsgBox.alert(this, "Vui lòng chọn hóa đơn cần xóa!");
            return;
        }

        if (MsgBox.confirm(this, "Bạn có chắc chắn muốn xóa hóa đơn này?")) {
            try {
                Long billId = Long.valueOf(id); 
                daoBill.delete(billId);
                fillTableBill();
                clearForm();
                MsgBox.alert(this, "Xóa hóa đơn thành công!");
            } catch (Exception e) {
                MsgBox.alert(this, "Lỗi xóa: " + e.getMessage());
            }
        }
    }

    // Chuyển đến bản ghi đầu tiên
    private void First() {
        if (tblBill.getRowCount() > 0) {
            tblBill.setRowSelectionInterval(0, 0);
            edit();
        }
    }

// Chuyển đến bản ghi trước
    private void Previous() {
        if (row > 0) {
            tblBill.setRowSelectionInterval(row - 1, row - 1);
            edit();
        }
    }

// Chuyển đến bản ghi tiếp theo
    private void Next() {
        if (row < tblBill.getRowCount() - 1) {
            tblBill.setRowSelectionInterval(row + 1, row + 1);
            edit();
        }
    }

// Chuyển đến bản ghi cuối cùng
    private void Last() {
        int lastRow = tblBill.getRowCount() - 1;
        if (lastRow >= 0) {
            tblBill.setRowSelectionInterval(lastRow, lastRow);
            edit();
        }
    }

    private void newForm() {
        clearForm();
        txtId.setText("0");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        txtCheckin.setText(sdf.format(new Date()));
        txtUsername.setText(XAuth.user.getUsername());
        rdoServicing.setSelected(true);
        txtCardId.requestFocus();
    }

    public BillManagerJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBill = new javax.swing.JTable();
        btnCheckall = new javax.swing.JButton();
        btnUncheckAll = new javax.swing.JButton();
        btnDeleteCheckedItems = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtEnd = new javax.swing.JTextField();
        txtBegin = new javax.swing.JTextField();
        btnFilter = new javax.swing.JButton();
        cboTimeRanges = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtCardId = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtCheckin = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCheckout = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        rdoServicing = new javax.swing.JRadioButton();
        rdoCompleted = new javax.swing.JRadioButton();
        rdoCanceled = new javax.swing.JRadioButton();
        jLabel8 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblBillDetail = new javax.swing.JTable();
        btnCreate = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblBill.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã phiếu", "Thẻ số", "Thời điểm tạo", "Thời điểm thanh toán", "Trạng thái", "Người tạo", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblBill.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblBillMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblBill);

        btnCheckall.setText("Chọn tất cả");
        btnCheckall.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckallActionPerformed(evt);
            }
        });

        btnUncheckAll.setText("Bỏ tất cả");
        btnUncheckAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUncheckAllActionPerformed(evt);
            }
        });

        btnDeleteCheckedItems.setText("Xóa các mục đã chọn");
        btnDeleteCheckedItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteCheckedItemsActionPerformed(evt);
            }
        });

        jLabel1.setText("Từ ngày:");

        jLabel2.setText("Đến ngày:");

        btnFilter.setText("Lọc");
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterActionPerformed(evt);
            }
        });

        cboTimeRanges.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hôm nay", "Tuần này", "Tháng này", "Quý này", "Năm nay", " ", " " }));
        cboTimeRanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTimeRangesActionPerformed(evt);
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
                        .addComponent(btnCheckall)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUncheckAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteCheckedItems)))
                .addContainerGap())
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCheckall)
                    .addComponent(btnUncheckAll)
                    .addComponent(btnDeleteCheckedItems))
                .addGap(17, 17, 17))
        );

        tabs.addTab("DANH SÁCH", jPanel1);

        jLabel3.setText("Mã phiếu");

        txtId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdActionPerformed(evt);
            }
        });

        jLabel4.setText("Thẻ số");

        jLabel5.setText("Thời điểm tạo");

        jLabel6.setText("Thời điểm thanh toán");

        jLabel7.setText("Trạng thái");

        buttonGroup1.add(rdoServicing);
        rdoServicing.setText("Servicing");

        buttonGroup1.add(rdoCompleted);
        rdoCompleted.setText("Completed");
        rdoCompleted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCompletedActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoCanceled);
        rdoCanceled.setText("Canceled");

        jLabel8.setText("Người tạo");

        txtUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsernameActionPerformed(evt);
            }
        });

        jLabel9.setText("Phiếu chi tiết");

        tblBillDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Đồ uống", "Đơn giá", "Giảm giá", "Số lượng", "Thành tiền"
            }
        ));
        tblBillDetail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblBillDetailMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(tblBillDetail);

        btnCreate.setText("Tạo mới");
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
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

        btnNew.setText("Nhập mới");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnFirst.setText("<");
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnPrevious.setText("<<");
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
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
                .addContainerGap(25, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(txtCardId, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(txtCheckin, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(rdoServicing)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rdoCompleted)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rdoCanceled)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel6)
                                    .addComponent(txtCheckout)
                                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel9))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addComponent(btnCreate)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnUpdate)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnDelete)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnNew)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnFirst)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnPrevious)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnNext)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnLast))
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 814, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(40, 40, 40))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCardId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(51, 51, 51)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCheckin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCheckout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdoServicing)
                    .addComponent(rdoCompleted)
                    .addComponent(rdoCanceled)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCreate)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete)
                    .addComponent(btnNew)
                    .addComponent(btnFirst)
                    .addComponent(btnPrevious)
                    .addComponent(btnNext)
                    .addComponent(btnLast))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        tabs.addTab("BIỂU MẪU", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDeleteCheckedItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteCheckedItemsActionPerformed
        // TODO add your handling code here:
        deleteCheckedItems();
    }//GEN-LAST:event_btnDeleteCheckedItemsActionPerformed

    private void btnUncheckAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUncheckAllActionPerformed
        // TODO add your handling code here:
        noSelectAll();
    }//GEN-LAST:event_btnUncheckAllActionPerformed

    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed
        // TODO add your handling code here:
        if (validateDate()) {
            fillTableBill();
        }
    }//GEN-LAST:event_btnFilterActionPerformed

    private void txtIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtIdActionPerformed

    private void rdoCompletedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCompletedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoCompletedActionPerformed

    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsernameActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        // TODO add your handling code here:
        First();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void cboTimeRangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTimeRangesActionPerformed
        // TODO add your handling code here:
        selectTimeRange();
    }//GEN-LAST:event_cboTimeRangesActionPerformed

    private void tblBillMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBillMousePressed
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            edit();
        }
    }//GEN-LAST:event_tblBillMousePressed

    private void btnCheckallActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckallActionPerformed
        // TODO add your handling code here:
        selectAll();
    }//GEN-LAST:event_btnCheckallActionPerformed

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        // TODO add your handling code here:
        createBill();
    }//GEN-LAST:event_btnCreateActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        updateBill();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here
        deleteBill();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        newForm();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        // TODO add your handling code here:
        Previous();
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        Next();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        // TODO add your handling code here:
        Last();
    }//GEN-LAST:event_btnLastActionPerformed

    private void tblBillDetailMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBillDetailMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblBillDetailMousePressed

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
            java.util.logging.Logger.getLogger(BillManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BillManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BillManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BillManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BillManagerJDialog dialog = new BillManagerJDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnCheckall;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDeleteCheckedItems;
    private javax.swing.JButton btnFilter;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnUncheckAll;
    private javax.swing.JButton btnUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JComboBox<String> cboTimeRanges;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable2;
    private javax.swing.JRadioButton rdoCanceled;
    private javax.swing.JRadioButton rdoCompleted;
    private javax.swing.JRadioButton rdoServicing;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblBill;
    private javax.swing.JTable tblBillDetail;
    private javax.swing.JTextField txtBegin;
    private javax.swing.JTextField txtCardId;
    private javax.swing.JTextField txtCheckin;
    private javax.swing.JTextField txtCheckout;
    private javax.swing.JTextField txtEnd;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
