package br.univali.portugol.plugin.gogoboard.gerenciadores;

import br.univali.portugol.plugin.gogoboard.componetes.DispositivoGoGo;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriverExclusivo;
import br.univali.portugol.plugin.gogoboard.ui.telas.JanelaMonitor;
import br.univali.ps.ui.telas.TelaCustomBorder;
import org.hid4java.HidServicesListener;
import org.hid4java.event.HidServicesEvent;

/**
 * Classe que gerencia a criação, configuração e atualização do monitor de recursos.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
public class GerenciadorMonitor implements HidServicesListener {

    private final DispositivoGoGo dispositivoGoGo;
    private final JanelaMonitor monitor;
    private TelaCustomBorder janelaMonitor;

    /**
     * Construtor padrão do gerenciado do monitor.
     *
     * @see JanelaMonitor
     */
    public GerenciadorMonitor() {
        dispositivoGoGo = new DispositivoGoGo(GoGoDriver.TIPODRIVER.EXCLUSIVO);
        monitor = new JanelaMonitor(dispositivoGoGo);
        configurarTela();
    }

    /**
     * Metodo para configurar a tela.
     */
    private void configurarTela() {
        janelaMonitor = janelaMonitor = new TelaCustomBorder(monitor, "Monitor de Recursos GoGo Board");
        janelaMonitor.setLocationRelativeTo(null);
        dispositivoGoGo.addServiceListener(this);
    }

    /**
     * Metodo para exibir a tela.
     */
    public void exibirMonitor() {
        ((GoGoDriverExclusivo) GerenciadorDriver.getGoGoDriver(GoGoDriver.TIPODRIVER.EXCLUSIVO)).getGoGoBoard();
        monitor.atualizarComponentes();
        janelaMonitor.setVisible(true);
        monitor.interromperThread();
        ((GoGoDriverExclusivo) GerenciadorDriver.getGoGoDriver(GoGoDriver.TIPODRIVER.EXCLUSIVO)).liberarGoGo();
    }

    @Override
    public void hidDeviceAttached(HidServicesEvent hse) {
        if (hse.getHidDevice().getVendorId() == GoGoDriver.VENDOR_ID
                && hse.getHidDevice().getProductId() == GoGoDriver.PRODUCT_ID) {
            monitor.atualizarComponentes();
        }
    }

    @Override
    public void hidDeviceDetached(HidServicesEvent hse) {
        if (hse.getHidDevice().getVendorId() == GoGoDriver.VENDOR_ID
                && hse.getHidDevice().getProductId() == GoGoDriver.PRODUCT_ID) {
            monitor.interromperThread();
        }
    }

    @Override
    public void hidFailure(HidServicesEvent hse) {
    }
}
