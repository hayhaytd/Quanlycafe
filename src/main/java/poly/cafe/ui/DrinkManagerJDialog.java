/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package poly.cafe.ui;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import poly.cafe.entity.Category;
import poly.cafe.entity.Drink;
import poly.cafe.util.MsgBox;
import poly.cafe.util.XAuth;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.table.TableCellEditor;
import poly.cafe.dao.CategoryDAO;
import poly.cafe.dao.CategoryDAO;
import poly.cafe.dao.DrinkDAO;
import poly.cafe.util.XImage;

/**
 *
 * @author ADMIN
 */
public class DrinkManagerJDialog extends JDialog implements PolyCafeController, DrinkController {

    DrinkDAO dao = new DrinkDAO();
    private int row = -1;
    JFileChooser fileChooser = new JFileChooser();
    CategoryDAO cdao = new CategoryDAO() {};
    List<Category> categories = cdao.selectAll();
    private List<Drink> items = new ArrayList<>();

    /**
     * Creates new form DrinkManagerJDialog
     */
    public DrinkManagerJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
        fillToTable();
        fillTable();

    }

    @Override
    public void init() {
        setLocationRelativeTo(null);
        setTitle("QUẢN LÝ NƯỚC UỐNG");
        fillCategories();
        fillTable();
        sliderDiscount.addChangeListener(e -> {
            int discount = sliderDiscount.getValue();
            lblDiscount.setText(discount + "%");
        });
        
    }

    void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tblDrinks.getModel();
        model.setRowCount(0);
        try {
            List<Drink> list = dao.selectAll();
            for (Drink cd : list) {
                Object[] row = {
                    cd.getId(), cd.getName(),
                    cd.getUnitPrice(), cd.getDiscount(),
                    cd.isAvailable()
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vẫn dữ liệu!");
        }
    }

    private void selectAll() {
        for (int i = 0; i < tblDrinks.getRowCount(); i++) {
            tblDrinks.setValueAt(true, i, 5); // Cột thứ 7 là checkbox
        }
    }

    private void noSelectAll() {
        for (int i = 0; i < tblDrinks.getRowCount(); i++) {
            tblDrinks.setValueAt(false, i, 5); // Cột thứ 7 là checkbox
        }
    }

    public void deleteDrinks() {
        if (!XAuth.isLogin()) {
            MsgBox.alert(this, "Bạn chưa đăng nhập");
            return;
        }
        if (MsgBox.confirm(this, "Bạn đang thực hiện chức năng xóa")) {
            boolean kq = false;
            for (int i = tblDrinks.getRowCount() - 1; i >= 0; i--) {
                String username = tblDrinks.getValueAt(i, 0).toString(); // username
                Object value = tblDrinks.getValueAt(i, 5); // checkbox chọn xóa

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
        if (txtId.getText().isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập mã UserName!");
            return false;
        }
        if (txtName.getText().isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập mã Tên đầy đủ!");
            return false;
        }
        if (txtUnitPrice.getText().isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập mã Tên đầy đủ!");
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

    void setForm(Drink drink
    ) {
        txtId.setText(drink.getId());
        txtName.setText(drink.getName());
        txtUnitPrice.setText(String.valueOf(drink.getUnitPrice()));
        if (drink.isAvailable()) {
            rdoAvailable.setSelected(true);
        } else {
            rdoUnAvailable.setSelected(true);
        }
        String FindCatName = null;
        for (Category category : categories) {
            if (category.getId().equals(drink.getCategoryId())) {
                FindCatName = category.getName();
                break;
            }
        }

// Chọn item trong combobox nếu trùng với tên category
        if (FindCatName != null) {
            for (int i = 0; i < cboCategory.getItemCount(); i++) {
                if (cboCategory.getItemAt(i).equals(FindCatName)) {
                    cboCategory.setSelectedIndex(i);
                    break;
                }
            }
        }
        
        lblImage.setToolTipText(drink.getImage());
        lblImage.setIcon(XImage.readScaled(drink.getImage(), lblImage));
    }

    public Drink getForm() {
        Drink drink = new Drink();
        drink.setId(txtId.getText().trim());
        drink.setName(txtName.getText().trim());
        drink.setUnitPrice(Double.parseDouble(txtUnitPrice.getText().trim()));
        drink.setImage(lblImage.getToolTipText());

        try {
            drink.setDiscount(sliderDiscount.getValue());
            drink.setAvailable(rdoAvailable.isSelected());
        } catch (Exception e) {

        }

        return drink;

    }

    public void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtUnitPrice.setText("");
        rdoAvailable.setSelected(true);   // mặc định là enabled
        rdoUnAvailable.setSelected(false);  // mặc định là nhân viên
        // Đặt lại ảnh mặc định
        lblImage.setIcon(null);
        lblImage.setToolTipText("Chưa có hình ảnh!");
        row = -1;
    }

    private void clearTableSelectionAndEditor() {
        // Bỏ chọn ô trên table sau khi click đôi chuột
        TableCellEditor editor = tblDrinks.getCellEditor();
        if (editor != null) {
            editor.stopCellEditing();
        }
        // Bỏ chọn hàng và cột
        tblDrinks.clearSelection();
        this.requestFocusInWindow();
    }

    void edit() {
        row = tblDrinks.getSelectedRow();
        try {
            String username = tblDrinks.getValueAt(row, 0).toString();
            Drink cd = dao.selectByID(username);
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
    if (!ValidateForm()) return;

    Drink drink = getForm();
    if (dao.selectByID(drink.getId()) != null) {
        MsgBox.alert(this, "Mã đồ uống đã tồn tại!");
        txtId.requestFocus();
        return;
    }

    try {
        dao.insert(drink);
        fillTable();
        clearForm();
        MsgBox.alert(this, "Thêm đồ uống thành công!");
    } catch (Exception e) {
        MsgBox.alert(this, "Lỗi thêm đồ uống: " + e.getMessage());
    }
}

    void update() {
        if (!ValidateForm()) {
            return;
        }
        Drink cd = getForm();
        if (dao.selectByID(cd.getId()) == null) {
            MsgBox.alert(this, "Không tồn tại Id để cập nhật");
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
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            MsgBox.alert(this, "Bạn chưa nhập Id cần xóa!");
            return;
        }
        if (dao.selectByID(id) == null) {
            MsgBox.alert(this, "Không tồn tại Id để xóa");
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

    // Chuyển đến bản ghi đầu tiên
    private void First() {
        if (tblDrinks.getRowCount() > 0) {
            tblDrinks.setRowSelectionInterval(0, 0);
            edit();
        }
    }

// Chuyển đến bản ghi trước
    private void Previous() {
        if (row > 0) {
            tblDrinks.setRowSelectionInterval(row - 1, row - 1);
            edit();
        }
    }

// Chuyển đến bản ghi tiếp theo
    private void Next() {
        if (row < tblDrinks.getRowCount() - 1) {
            tblDrinks.setRowSelectionInterval(row + 1, row + 1);
            edit();
        }
    }

// Chuyển đến bản ghi cuối cùng
    private void Last() {
        int lastRow = tblDrinks.getRowCount() - 1;
        if (lastRow >= 0) {
            tblDrinks.setRowSelectionInterval(lastRow, lastRow);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        Tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDrinks = new javax.swing.JTable();
        btnSelectAll = new javax.swing.JButton();
        btnNoSelectAll = new javax.swing.JButton();
        btnDeleteCheckedItems = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCategories = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtUnitPrice = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cboCategory = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        rdoAvailable = new javax.swing.JRadioButton();
        rdoUnAvailable = new javax.swing.JRadioButton();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnMoveFirst = new javax.swing.JButton();
        btnMovePrevious = new javax.swing.JButton();
        btnMoveNext = new javax.swing.JButton();
        btnMoveLast = new javax.swing.JButton();
        sliderDiscount = new javax.swing.JSlider();
        lblImage = new javax.swing.JLabel();
        lblDiscount = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblDrinks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã đồ uống", "Tên đồ uống", "Đơn giá", "Giảm giá", "Trạng thái", ""
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

        btnSelectAll.setText("Chọn tất cả");
        btnSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectAllActionPerformed(evt);
            }
        });

        btnNoSelectAll.setText("Bỏ chọn tất cả");
        btnNoSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNoSelectAllActionPerformed(evt);
            }
        });

        btnDeleteCheckedItems.setText("Xóa các mục chọn");
        btnDeleteCheckedItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteCheckedItemsActionPerformed(evt);
            }
        });

        tblCategories.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSelectAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNoSelectAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteCheckedItems))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE))
                .addGap(21, 21, 21))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSelectAll)
                            .addComponent(btnNoSelectAll)
                            .addComponent(btnDeleteCheckedItems))))
                .addContainerGap(51, Short.MAX_VALUE))
        );

        Tabs.addTab("Danh sách", jPanel1);

        jLabel2.setText("Mã đồ uống");

        jLabel3.setText("Tên đồ uống");

        jLabel4.setText("Đơn giá");

        jLabel5.setText("Giảm giá");

        jLabel6.setText("Loại");

        cboCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel7.setText("Trạng thái");

        buttonGroup1.add(rdoAvailable);
        rdoAvailable.setText("Sẵn có");

        buttonGroup1.add(rdoUnAvailable);
        rdoUnAvailable.setText("Hết hàng");

        btnAdd.setText("Tạo mới");
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

        btnClear.setText("Nhập mới");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnMoveFirst.setText("<");
        btnMoveFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveFirstActionPerformed(evt);
            }
        });

        btnMovePrevious.setText("<-");
        btnMovePrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMovePreviousActionPerformed(evt);
            }
        });

        btnMoveNext.setText("->");
        btnMoveNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveNextActionPerformed(evt);
            }
        });

        btnMoveLast.setText(">");
        btnMoveLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveLastActionPerformed(evt);
            }
        });

        sliderDiscount.setMajorTickSpacing(10);
        sliderDiscount.setPaintLabels(true);
        sliderDiscount.setPaintTicks(true);

        lblImage.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblImage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblImageMousePressed(evt);
            }
        });

        lblDiscount.setText("50%");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 123, Short.MAX_VALUE)
                        .addComponent(btnMoveFirst)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMovePrevious)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMoveNext))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(cboCategory, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtUnitPrice, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(46, 46, 46)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(rdoAvailable)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rdoUnAvailable))
                            .addComponent(jLabel7)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDiscount))
                            .addComponent(jLabel3)
                            .addComponent(sliderDiscount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(43, 43, 43)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMoveLast)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(lblDiscount))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtUnitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sliderDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(7, 7, 7)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdoAvailable)
                            .addComponent(rdoUnAvailable)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete)
                    .addComponent(btnClear)
                    .addComponent(btnMoveFirst)
                    .addComponent(btnMovePrevious)
                    .addComponent(btnMoveNext)
                    .addComponent(btnMoveLast))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        Tabs.addTab("Biễu mẫu", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Tabs, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(Tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNoSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNoSelectAllActionPerformed
        // TODO add your handling code here:
        noSelectAll();
    }//GEN-LAST:event_btnNoSelectAllActionPerformed

    private void btnSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectAllActionPerformed
        // TODO add your handling code here:
        selectAll();
    }//GEN-LAST:event_btnSelectAllActionPerformed

    private void btnDeleteCheckedItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteCheckedItemsActionPerformed
        // TODO add your handling code here:
        deleteDrinks();
    }//GEN-LAST:event_btnDeleteCheckedItemsActionPerformed

    private void tblDrinksMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDrinksMousePressed
        // TODO add your handling code here:
        if (evt.getClickCount() == 2)
            edit();
    }//GEN-LAST:event_tblDrinksMousePressed

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

    private void tblCategoriesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCategoriesMousePressed
        // TODO add your handling code here:
        fillToTable();
    }//GEN-LAST:event_tblCategoriesMousePressed

    private void lblImageMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblImageMousePressed
        // TODO add your handling code here:
        chooseFile();
    }//GEN-LAST:event_lblImageMousePressed

    private void btnMoveFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveFirstActionPerformed
        // TODO add your handling code here:
        First();
    }//GEN-LAST:event_btnMoveFirstActionPerformed

    private void btnMovePreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMovePreviousActionPerformed
        // TODO add your handling code here:
        Previous();
    }//GEN-LAST:event_btnMovePreviousActionPerformed

    private void btnMoveNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveNextActionPerformed
        // TODO add your handling code here:
        Next();
    }//GEN-LAST:event_btnMoveNextActionPerformed

    private void btnMoveLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveLastActionPerformed
        // TODO add your handling code here:
        Last();
    }//GEN-LAST:event_btnMoveLastActionPerformed

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
            java.util.logging.Logger.getLogger(DrinkManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DrinkManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DrinkManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DrinkManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DrinkManagerJDialog dialog = new DrinkManagerJDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDeleteCheckedItems;
    private javax.swing.JButton btnMoveFirst;
    private javax.swing.JButton btnMoveLast;
    private javax.swing.JButton btnMoveNext;
    private javax.swing.JButton btnMovePrevious;
    private javax.swing.JButton btnNoSelectAll;
    private javax.swing.JButton btnSelectAll;
    private javax.swing.JButton btnUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboCategory;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDiscount;
    private javax.swing.JLabel lblImage;
    private javax.swing.JRadioButton rdoAvailable;
    private javax.swing.JRadioButton rdoUnAvailable;
    private javax.swing.JSlider sliderDiscount;
    private javax.swing.JTable tblCategories;
    private javax.swing.JTable tblDrinks;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtUnitPrice;
    // End of variables declaration//GEN-END:variables

    @Override
    public void chooseFile() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            XImage.save(file);
            ImageIcon scaledIcon = XImage.readScaled(file.getName(), lblImage);
            lblImage.setIcon(scaledIcon);
            lblImage.setToolTipText(file.getName());
        }
    }

    @Override
    public void fillCategories() {
        DefaultComboBoxModel cboModel = (DefaultComboBoxModel) cboCategory.getModel();
        cboModel.removeAllElements();
        DefaultTableModel tblModel = (DefaultTableModel) tblCategories.getModel();
        tblModel.setRowCount(0);
        categories = cdao.selectAll();
        categories.forEach(category -> {
            cboModel.addElement(category.getName());
            tblModel.addRow(new Object[]{category.getName()});
        });

        tblCategories.setRowSelectionInterval(0, 0);
    }

    @Override
    public void fillToTable() {
        DefaultTableModel model = (DefaultTableModel) tblDrinks.getModel();
        model.setRowCount(0);

        Category category = categories.get(tblCategories.getSelectedRow());
        items = dao.findByCategoryId(category.getId()); // thay vì findAll() 
        items.forEach(item -> {
            Object[] row = {
                item.getId(), item.getName(),
                item.getUnitPrice(), item.getDiscount(),
                item.isAvailable()
            };
            model.addRow(row);
        });

    }
}
