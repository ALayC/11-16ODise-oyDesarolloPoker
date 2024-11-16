/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package IU;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import panelCartasPoker.CartaPoker;
import panelCartasPoker.PanelCartasListener;
import panelCartasPoker.PanelCartasPokerException;

/**
 *
 * @author Santiago
 */
public class DialogPanelCartas extends javax.swing.JDialog implements PanelCartasListener{

    /**
     * Creates new form DialogPanelCartas
     */
    public DialogPanelCartas(java.awt.Frame parent) {
        super(parent);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelCartasPoker1 = new panelCartasPoker.PanelCartasPoker();
        btnPasar = new javax.swing.JButton();
        btnApostar = new javax.swing.JButton();
        btnPagar = new javax.swing.JButton();
        btnRetirarse = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        btnPasar.setText("Pasar");

        btnApostar.setText("Apostar");

        btnPagar.setText("Pagar");
        btnPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagarActionPerformed(evt);
            }
        });

        btnRetirarse.setText("Salir");
        btnRetirarse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRetirarseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(panelCartasPoker1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(262, 262, 262)
                        .addComponent(btnPasar)
                        .addGap(66, 66, 66)
                        .addComponent(btnApostar)
                        .addGap(64, 64, 64)
                        .addComponent(btnPagar)
                        .addGap(66, 66, 66)
                        .addComponent(btnRetirarse)))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addComponent(panelCartasPoker1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPasar)
                    .addComponent(btnApostar)
                    .addComponent(btnPagar)
                    .addComponent(btnRetirarse))
                .addGap(36, 36, 36))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPagarActionPerformed

    private void btnRetirarseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRetirarseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRetirarseActionPerformed

    /**
     * @param args the command line arguments
     */
    
    private void habilitarPanelActionPerformed(java.awt.event.ActionEvent evt) {                                               
        // TODO add your handling code here:
        panelCartasPoker1.setEnabled(habilitarPanel.isSelected());
    }                                              

    private void checkListenerActionPerformed(java.awt.event.ActionEvent evt) {                                              
        // TODO add your handling code here:
        if(checkListener.isSelected()){
            panelCartasPoker1.setListener(this);
        }else panelCartasPoker1.setListener(null);
    }   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApostar;
    private javax.swing.JButton btnPagar;
    private javax.swing.JButton btnPasar;
    private javax.swing.JButton btnRetirarse;
    private panelCartasPoker.PanelCartasPoker panelCartasPoker1;
    // End of variables declaration//GEN-END:variables
    
        private javax.swing.JCheckBox checkListener;
    private javax.swing.JCheckBox habilitarPanel;
    private javax.swing.JLabel jLabel1;

    
    
    public void cargarCartas(ArrayList<CartaPoker> cartas){
        try {
            panelCartasPoker1.cargarCartas(cartas);
        } catch (PanelCartasPokerException ex) {
             JOptionPane.showMessageDialog(this, ex.getMessage(), "ERROR",JOptionPane.ERROR_MESSAGE);
        }
   }
    @Override
    public void clickEnCarta(CartaPoker carta) {
        JOptionPane.showMessageDialog(this, carta.toString(), "Click en carta",JOptionPane.INFORMATION_MESSAGE);
    }
}