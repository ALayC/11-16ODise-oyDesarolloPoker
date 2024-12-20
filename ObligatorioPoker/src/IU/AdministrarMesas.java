package IU;

import Controladores.AdministrarMesasControlador;
import Controladores.ControlMesa;
import Dominio.EstadoMesa;
import Dominio.Jugador;
import Dominio.Mano;
import Dominio.Mesa;
import Interfaces.AdministrarMesasVista;
import Interfaces.VistaControlMesa;

import Observador.Observable;
import Servicios.ServicioMesas;
import java.util.ArrayList;

import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

public class AdministrarMesas extends javax.swing.JFrame implements AdministrarMesasVista {

    private AdministrarMesasControlador controlador;

    public AdministrarMesas() {
        controlador = new AdministrarMesasControlador(this); // Pasar la vista al controlador

        initComponents();
        setLocationRelativeTo(null); // Centrar la ventana
        cargarDatosLista();
        // Agregar listener a listMesas
        listMesas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = listMesas.getSelectedIndex();
                if (selectedIndex != -1) {
                    Mesa mesaSeleccionada = controlador.obtenerMesas().get(selectedIndex);
                    mostrarInformacionMano(mesaSeleccionada);
                }
            }
        });
    }

    private void cargarDatosLista() {
        DefaultListModel<String> listModel = new DefaultListModel<>();

        double totalRecaudado = 0;

        // Obtener la lista de mesas desde el controlador
        for (Mesa mesa : controlador.obtenerMesas()) {
            int numeroMesa = mesa.getNumeroMesa();
            int jugadoresRequeridos = mesa.getCantidadJugadores();
            double apuestaBase = mesa.getApuestaBase();
            int jugadoresActuales = mesa.getCantidadActualJugadores();
            double montoTotalApostado = mesa.getMontoTotalApostado();
            double porcentajeComision = mesa.getPorcentajeComision();
            double montoTotalRecaudadoMesa = mesa.getMontoTotalRecaudado();
            String estadoMesa = mesa.getEstado().toString();

            totalRecaudado += montoTotalRecaudadoMesa;

            String infoMesa = String.format(
                    "Mesa %d | Jugadores Requeridos: %d | Apuesta Base: %.2f | "
                    + "Jugadores Actuales: %d | Total Apostado: %.2f | "
                    + "Comisión: %.2f%% | Recaudado: %.2f | Estado: %s",
                    numeroMesa, jugadoresRequeridos, apuestaBase,
                    jugadoresActuales, montoTotalApostado,
                    porcentajeComision, montoTotalRecaudadoMesa, estadoMesa
            );

            listModel.addElement(infoMesa);
        }

        listMesas.setModel(listModel);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblMontoRecaudado = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listMesas = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        txtNumMesa = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNumMano = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtJugPar = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtApostado = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtEstadoMano = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtJugadorGanadorMano = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtFiguraGanadora = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblMontoRecaudado.setText("Monto total recaudado en mesas:");

        jScrollPane2.setViewportView(listMesas);

        jLabel1.setText("N° mesa: ");

        txtNumMesa.setText("Sin datos");
        txtNumMesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumMesaActionPerformed(evt);
            }
        });

        jLabel2.setText("N° mano:");

        txtNumMano.setText("Sin datos");
        txtNumMano.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumManoActionPerformed(evt);
            }
        });

        jLabel3.setText("Jugadores participantes de la mano:");

        txtJugPar.setText("Sin datos");

        jLabel4.setText("Total apostado en la mano:");

        txtApostado.setText("Sin datos");
        txtApostado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApostadoActionPerformed(evt);
            }
        });

        jLabel5.setText("Estado actual de la mano:");

        txtEstadoMano.setText("Sin datos");

        jLabel6.setText("Nombre del jugador ganador de la mano:");

        txtJugadorGanadorMano.setText("Sin datos");
        txtJugadorGanadorMano.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtJugadorGanadorManoActionPerformed(evt);
            }
        });

        jLabel7.setText("Nombre de la figura con la que gano la mano: ");

        txtFiguraGanadora.setText("Sin datos");
        txtFiguraGanadora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFiguraGanadoraActionPerformed(evt);
            }
        });

        jLabel8.setText("Informacion de Mano");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblMontoRecaudado)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNumMesa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNumMano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtJugPar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtApostado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtEstadoMano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtJugadorGanadorMano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtFiguraGanadora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblMontoRecaudado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNumMesa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNumMano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtJugPar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtApostado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtEstadoMano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtJugadorGanadorMano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtFiguraGanadora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtJugadorGanadorManoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJugadorGanadorManoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtJugadorGanadorManoActionPerformed

    private void txtFiguraGanadoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiguraGanadoraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFiguraGanadoraActionPerformed

    private void txtApostadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApostadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApostadoActionPerformed

    private void txtNumMesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumMesaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumMesaActionPerformed

    private void txtNumManoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumManoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumManoActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblMontoRecaudado;
    private javax.swing.JList<String> listMesas;
    private javax.swing.JTextField txtApostado;
    private javax.swing.JTextField txtEstadoMano;
    private javax.swing.JTextField txtFiguraGanadora;
    private javax.swing.JTextField txtJugPar;
    private javax.swing.JTextField txtJugadorGanadorMano;
    private javax.swing.JTextField txtNumMano;
    private javax.swing.JTextField txtNumMesa;
    // End of variables declaration//GEN-END:variables

    public void fijarNombre(String nombreCompleto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void cargarMesas(ArrayList<Mesa> mesas) {

        DefaultListModel<String> listModel = new DefaultListModel<>();

        double totalRecaudado = 0;
        for (Mesa mesa : mesas) {
            int numeroMesa = mesa.getNumeroMesa();
            int jugadoresRequeridos = mesa.getCantidadJugadores();
            double apuestaBase = mesa.getApuestaBase();
            int jugadoresActuales = mesa.getCantidadActualJugadores();
            double montoTotalApostado = mesa.getMontoTotalApostado();
            double porcentajeComision = mesa.getPorcentajeComision();
            double montoTotalRecaudadoMesa = mesa.getMontoTotalRecaudado();
            String estadoMesa = mesa.getEstado().toString();

            totalRecaudado += montoTotalRecaudadoMesa;

            String mesaInfo = String.format(
                    "Mesa %d | Jugadores Requeridos: %d | Apuesta Base: %.2f | "
                    + "Jugadores Actuales: %d | Total Apostado: %.2f | "
                    + "Comisión: %.2f%% | Recaudado: %.2f | Estado: %s",
                    numeroMesa, jugadoresRequeridos, apuestaBase,
                    jugadoresActuales, montoTotalApostado,
                    porcentajeComision, montoTotalRecaudadoMesa, estadoMesa
            );

            listModel.addElement(mesaInfo);
        }

        listMesas.setModel(listModel);
    }

    @Override
    public void mostrarError(String saldo_insuficiente_para_ingresar_a_la_mes) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mostrarMensaje(String la_mesa_ha_sido_iniciada) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void actualizar(Observable origen, Object evento) {
        if (origen instanceof ServicioMesas) {

            cargarDatosLista();
        }
    }

    @Override
    public void mostrarInformacionMano(Mesa mesa) {
        if (mesa != null) {
            txtNumMesa.setText(String.valueOf(mesa.getNumeroMesa()));
            txtNumMano.setText(String.valueOf(mesa.getNumeroManoActual()));
            txtJugPar.setText(String.valueOf(mesa.getCantidadActualJugadores()));
            txtEstadoMano.setText(String.valueOf(mesa.getManoActual().getEstado()));

        }
    }

}
