package br.univali.portugol.plugin.gogoboard.ui.telas;

import br.univali.ps.ui.telas.TelaCustomBorder;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class TesteTela {
    public static void main(String[] args) {
        TelaCustomBorder janelaMonitor = new TelaCustomBorder(new JanelaMonitor(), "Monitor de Recursos GoGo Board");
        janelaMonitor.setLocationRelativeTo(null);
        janelaMonitor.setVisible(true);
    }
}
