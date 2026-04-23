/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package poly.cafe.ui;

import poly.cafe.util.XAuth;
import poly.cafe.util.XImage;

/**
 *
 * @author ADMIN
 */
    public class PolyCafeJFrame extends javax.swing.JFrame implements PolyCafeController {
    
    @Override
    public void init() {
        setIconImage(XImage.getAppIcon());
        setLocationRelativeTo(null);
        setTitle("POLY-CAFE");

        showWelcomeJDialog(this);
        showLoginJDialog(this);
        showUserInfo();
        applyPermissions();
    }
    
    private void showUserInfo() {
    if (XAuth.user != null && XAuth.user.getPhoto() != null) {
        lblPhoto.setToolTipText(XAuth.user.getPhoto());
        lblPhoto.setIcon(XImage.readScaled(XAuth.user.getPhoto(), lblPhoto));
    }

    if (XAuth.user != null) {
        lblFullname.setText(XAuth.user.getFullname());
    }
    }
    
    private void applyPermissions() {
    if (XAuth.user != null && !XAuth.user.isManager()) {
        btnDrinkManager.setVisible(false);
        btnRevenueManager.setVisible(false);
        btnCategoryManager.setVisible(false);
        btnUserManager.setVisible(false);
        btnCardManager.setVisible(false);
        btnBillManager.setVisible(false);
    }   
    }
    
    public PolyCafeJFrame() {
        initComponents();
        init();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PolyCafeJFrame = new javax.swing.JLayeredPane();
        lblFullname = new javax.swing.JLabel();
        btnSales = new javax.swing.JButton();
        btnHistory = new javax.swing.JButton();
        btnChangePassword = new javax.swing.JButton();
        btnDrinkManager = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        btnCategoryManager = new javax.swing.JButton();
        btnCardManager = new javax.swing.JButton();
        btnBillManager = new javax.swing.JButton();
        btnUserManager = new javax.swing.JButton();
        btnRevenueManager = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lblPhoto = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PolyCafeJFrame.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblFullname.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblFullname.setForeground(new java.awt.Color(255, 0, 0));
        lblFullname.setText("Nguyễn Văn A");
        PolyCafeJFrame.add(lblFullname, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 130, -1));

        btnSales.setText("BÁN HÀNG");
        btnSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalesActionPerformed(evt);
            }
        });
        PolyCafeJFrame.add(btnSales, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 110, 50));

        btnHistory.setText("LỊCH SỬ");
        btnHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistoryActionPerformed(evt);
            }
        });
        PolyCafeJFrame.add(btnHistory, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 310, 110, 50));

        btnChangePassword.setText("ĐỔI MẬT KHẨU");
        btnChangePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangePasswordActionPerformed(evt);
            }
        });
        PolyCafeJFrame.add(btnChangePassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 110, 50));

        btnDrinkManager.setText("ĐỒ UỐNG");
        btnDrinkManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDrinkManagerActionPerformed(evt);
            }
        });
        PolyCafeJFrame.add(btnDrinkManager, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 310, 190, 50));

        btnExit.setText("KẾT THÚC");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        PolyCafeJFrame.add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 370, 110, 50));

        btnCategoryManager.setText("LOẠI ĐỒ UỐNG");
        btnCategoryManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCategoryManagerActionPerformed(evt);
            }
        });
        PolyCafeJFrame.add(btnCategoryManager, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 310, 190, 50));

        btnCardManager.setText("THẺ ĐỊNH DANH");
        btnCardManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCardManagerActionPerformed(evt);
            }
        });
        PolyCafeJFrame.add(btnCardManager, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 310, 190, 50));

        btnBillManager.setText("PHIẾU BÁN HÀNG");
        btnBillManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBillManagerActionPerformed(evt);
            }
        });
        PolyCafeJFrame.add(btnBillManager, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 370, 190, 50));

        btnUserManager.setText("NGƯỜI SỬ DỤNG");
        btnUserManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserManagerActionPerformed(evt);
            }
        });
        PolyCafeJFrame.add(btnUserManager, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 370, 190, 50));

        btnRevenueManager.setText("DOANH THU");
        btnRevenueManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRevenueManagerActionPerformed(evt);
            }
        });
        PolyCafeJFrame.add(btnRevenueManager, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 370, 190, 50));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/cafe/icons/coffee-shop.jpg"))); // NOI18N
        PolyCafeJFrame.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, -10, 700, 460));

        lblPhoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/cafe/icons/trump-small.png"))); // NOI18N
        lblPhoto.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        PolyCafeJFrame.add(lblPhoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 120, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PolyCafeJFrame)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PolyCafeJFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistoryActionPerformed
        // TODO add your handling code here:
        showHistoryJDialog(this);
    }//GEN-LAST:event_btnHistoryActionPerformed

    private void btnCardManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCardManagerActionPerformed
        // TODO add your handling code here:
        showCardManagerJDialog(this);
    }//GEN-LAST:event_btnCardManagerActionPerformed

    private void btnChangePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangePasswordActionPerformed
        // TODO add your handling code here:
        showChangePasswordJDialog(this);
    }//GEN-LAST:event_btnChangePasswordActionPerformed

    private void btnSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalesActionPerformed
        // TODO add your handling code here:
        showSalesJDialog(this);
    }//GEN-LAST:event_btnSalesActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        // TODO add your handling code here:
         System.exit(0);
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnDrinkManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDrinkManagerActionPerformed
        // TODO add your handling code here:
        showDrinkManagerJDialog(this);
    }//GEN-LAST:event_btnDrinkManagerActionPerformed

    private void btnCategoryManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCategoryManagerActionPerformed
        // TODO add your handling code here:
        showCategoryManagerJDialog(this);
    }//GEN-LAST:event_btnCategoryManagerActionPerformed

    private void btnBillManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBillManagerActionPerformed
        // TODO add your handling code here:
        showBillManagerJDialog(this);
    }//GEN-LAST:event_btnBillManagerActionPerformed

    private void btnUserManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserManagerActionPerformed
        // TODO add your handling code here:
        showUserManagerJDialog(this);
    }//GEN-LAST:event_btnUserManagerActionPerformed

    private void btnRevenueManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRevenueManagerActionPerformed
        // TODO add your handling code here:
        showRevenueManagerJDialog(this);
    }//GEN-LAST:event_btnRevenueManagerActionPerformed

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
            java.util.logging.Logger.getLogger(PolyCafeJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PolyCafeJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PolyCafeJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PolyCafeJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PolyCafeJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane PolyCafeJFrame;
    private javax.swing.JButton btnBillManager;
    private javax.swing.JButton btnCardManager;
    private javax.swing.JButton btnCategoryManager;
    private javax.swing.JButton btnChangePassword;
    private javax.swing.JButton btnDrinkManager;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnHistory;
    private javax.swing.JButton btnRevenueManager;
    private javax.swing.JButton btnSales;
    private javax.swing.JButton btnUserManager;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblFullname;
    private javax.swing.JLabel lblPhoto;
    // End of variables declaration//GEN-END:variables
}
