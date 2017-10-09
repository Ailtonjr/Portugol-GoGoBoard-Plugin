package br.univali.portugol.plugin.gogoboard.telas;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.GoGoDriver;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author Luiz Fernando Noschang
 */
public class JanelaCodigoLogo extends javax.swing.JDialog {

    private byte[] bytecodeTeste = {1, 10, 3, 5, 12, 1, 10, 16, 4, 9, 0,};

    private GoGoDriver goGoDriver;

    public JanelaCodigoLogo() {
        initComponents();
        setModal(true);
        this.goGoDriver = GoGoDriver.obterInstancia();
    }

    public void setCodigoLogo(String codigoFonte) {
        textAreaCodigo.setText(codigoFonte);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                painelRolagem.getVerticalScrollBar().setValue(0);
                painelRolagem.getHorizontalScrollBar().setValue(0);
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rotuloDescricao = new javax.swing.JLabel();
        painelRolagem = new javax.swing.JScrollPane();
        textAreaCodigo = new javax.swing.JTextArea();
        botaoOK = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Código Logo");

        rotuloDescricao.setText("Código logo:");

        textAreaCodigo.setEditable(false);
        textAreaCodigo.setColumns(20);
        textAreaCodigo.setRows(5);
        painelRolagem.setViewportView(textAreaCodigo);

        botaoOK.setText("OK");
        botaoOK.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoOKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(painelRolagem, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(rotuloDescricao)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(botaoOK, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rotuloDescricao)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(painelRolagem, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botaoOK, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void botaoOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_botaoOKActionPerformed
    {//GEN-HEADEREND:event_botaoOKActionPerformed
        /*try {
            goGoDriver.enviarCodigoLogo(bytecodeTeste);
        } catch (ErroExecucaoBiblioteca ex) {
            Logger.getLogger(JanelaCodigoLogo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(JanelaCodigoLogo.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        dispose();
    }//GEN-LAST:event_botaoOKActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botaoOK;
    private javax.swing.JScrollPane painelRolagem;
    private javax.swing.JLabel rotuloDescricao;
    private javax.swing.JTextArea textAreaCodigo;
    // End of variables declaration//GEN-END:variables
}
