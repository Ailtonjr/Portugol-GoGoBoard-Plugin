package br.univali.portugol.plugin.gogoboard.ui.controlador;

import br.univali.portugol.plugin.gogoboard.biblioteca.GoGoBoard;
import br.univali.portugol.plugin.gogoboard.componetes.DispositivoGoGo;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;
import br.univali.portugol.plugin.gogoboard.ui.telas.JanelaMonitor;
import br.univali.ps.ui.telas.TelaCustomBorder;
import org.hid4java.HidServicesListener;
import org.hid4java.event.HidServicesEvent;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class ControladorMonitor implements HidServicesListener {

    private DispositivoGoGo dispositivoGoGo;
    private JanelaMonitor monitor;
    private TelaCustomBorder janelaMonitor;

    public ControladorMonitor() {
        dispositivoGoGo = new DispositivoGoGo();
        monitor = new JanelaMonitor(dispositivoGoGo);
        configurarTela();
    }

    private void configurarTela() {
        janelaMonitor = janelaMonitor = new TelaCustomBorder(monitor, "Monitor de Recursos GoGo Board");
        janelaMonitor.setLocationRelativeTo(null);
        dispositivoGoGo.addServiceListener(this);
    }

    public void exibirMonitor() {
        monitor.atualizarComponentes();
        janelaMonitor.setVisible(true);
        monitor.interromperThread();
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
