/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package poly.cafe.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import poly.cafe.dao.BillDAO;
import poly.cafe.dao.CardDAO;
import poly.cafe.entity.Bill;
import poly.cafe.entity.Card;
import poly.cafe.util.MsgBox;
import poly.cafe.util.XImage;

/**
 *
 * @author ADMIN
 */
public class SalesJDialog extends javax.swing.JDialog {

    private BillDAO billDAO = new BillDAO(); // Fix: Add this field declaration
    private CardDAO cardDAO = new CardDAO();

    /**
     * Creates new form SalesJDialog
     */
    public SalesJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
    }

    public void init() {
        setIconImage(XImage.getAppIcon());
        setLocationRelativeTo(null);
        setTitle("BÁN HÀNG - THẺ KHÁCH HÀNG");
        loadCards();
    }

    public void open() {
        this.setLocationRelativeTo(null);
        this.loadCards(); // Load and display cards
    }

    private void loadCards() {
        CardDAO dao = new CardDAO();
        List<Card> cards = dao.selectAll();
        pnlCard.removeAll();

        // Layout có 6 cột, các hàng tự động thêm, khoảng cách giữa các nút là 10px
        pnlCard.setLayout(new GridLayout(0, 6, 10, 10));

        for (Card card : cards) {
            pnlCard.add(createButton(card));
        }

        pnlCard.revalidate();  // Cập nhật layout
        pnlCard.repaint();     // Vẽ lại panel
    }

    private JButton createButton(Card card) {
        JButton btnCard = new JButton(String.format("Card #%d", card.getId()));
        btnCard.setPreferredSize(new Dimension(100, 100));
        btnCard.setEnabled(card.getStatus() == 0);  // Thẻ đang sd sẽ có màu xanh
        btnCard.setBackground(btnCard.isEnabled() ? Color.GREEN : Color.GRAY);

        // Gắn ID vào NÚT để dễ truy xuất
        btnCard.setActionCommand(String.valueOf(card.getId()));

        btnCard.addActionListener((ActionEvent e) -> {
            int cardId = Integer.parseInt(e.getActionCommand());
            //Gọi phương thức showBillJDialog(int cardId) từ lớp ngoài SalesJDialog, 
            SalesJDialog.this.showBillJDialog(cardId);
        });

        return btnCard;
    }

    public void showBillJDialog(int cardId) {
        try {
            // First try to get unpaid bill (status = 0)
            Bill bill = billDAO.selectByIDStatus0(cardId);

            if (bill == null) {
                // If no unpaid bill, get the most recent bill for this card
                bill = getLatestBillForCard(cardId);

                if (bill == null) {
                    // Create new bill if no bill exists at all
                    Bill newBill = new Bill();
                    newBill.setCardId(cardId);
                    newBill.setCheckin(new Date());
                    newBill.setStatus(0); // Servicing status
                    newBill.setUsername("admin"); // You should get this from current user session
                    billDAO.insert(newBill);

                    // Get the newly created bill
                    bill = billDAO.selectByIDStatus0(cardId);
                }
            }

            // Fix: Use 'this' directly instead of casting getOwner()
            BillJDialog dialog = new BillJDialog(this, true, cardId);
            dialog.setForm(bill); // Pass the bill data to the dialog
            dialog.setVisible(true);

            // Refresh cards after dialog closes
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    SalesJDialog.this.loadCards(); // Refresh the card display
                }
            });

        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi khi tải hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Bill getLatestBillForCard(int cardId) {
        try {
            // Get all bills for this card and return the most recent one
            List<Bill> allBills = billDAO.selectAll();
            Bill latestBill = null;

            for (Bill bill : allBills) {
                if (bill.getCardId() == cardId) {
                    if (latestBill == null || bill.getId() > latestBill.getId()) {
                        latestBill = bill;
                    }
                }
            }

            return latestBill;
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        pnlCard = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridLayout());

        javax.swing.GroupLayout pnlCardLayout = new javax.swing.GroupLayout(pnlCard);
        pnlCard.setLayout(pnlCardLayout);
        pnlCardLayout.setHorizontalGroup(
            pnlCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 686, Short.MAX_VALUE)
        );
        pnlCardLayout.setVerticalGroup(
            pnlCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 405, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(pnlCard);

        getContentPane().add(jScrollPane1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        this.open();
    }//GEN-LAST:event_formWindowOpened

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
            java.util.logging.Logger.getLogger(SalesJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SalesJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SalesJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SalesJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SalesJDialog dialog = new SalesJDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JPanel pnlCard;
    // End of variables declaration//GEN-END:variables
}
