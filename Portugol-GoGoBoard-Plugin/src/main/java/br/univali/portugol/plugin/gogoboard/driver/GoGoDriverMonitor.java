package br.univali.portugol.plugin.gogoboard.driver;

import br.univali.portugol.plugin.gogoboard.gerenciadores.GerenciadorDriver;
import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.util.UtilGoGoBoard;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hid4java.HidDevice;
import org.hid4java.HidException;
import org.hid4java.HidManager;
import org.hid4java.HidServicesListener;
import org.hid4java.event.HidServicesEvent;

/**
 * Classe utilizada na biblioteca com funções para envio e recebimento de
 * pacotes via protocolo HID para a GoGo Board.
 *
 * O dispositivo é aberto em cada comunicação e já é fechado ao final da mesma.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
public final class GoGoDriverMonitor extends GoGoDriver implements HidServicesListener {

    private HidDevice goGoBoard;

    /**
     * Construtor padrão do driver da GoGo Board.
     */
    public GoGoDriverMonitor() {
        try {
            iniciarServicosHID();
        } catch (HidException ex) {
            ex.printStackTrace(System.err);
            Logger.getLogger(GerenciadorDriver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void iniciarServicosHID() {
        servicosHID = HidManager.getHidServices();
        servicosHID.addHidServicesListener(this);
    }

    /**
     * Método para obter a GoGo Board usando o método getHidDevice na lista dos
     * dispositivos HID.
     */
    public void getGoGoBoard() {
        for (HidDevice hidDevice : servicosHID.getAttachedHidDevices()) {
            if (hidDevice.getVendorId() == VENDOR_ID && hidDevice.getProductId() == PRODUCT_ID) {
                goGoBoard = servicosHID.getHidDevice(VENDOR_ID, PRODUCT_ID, null);
                System.out.println("Inicio GoGo Conectada: " + goGoBoard);
            }
        }
    }

    @Override
    public void enviarMensagem(byte[] mensagem) throws ErroExecucaoBiblioteca {
        try {
            goGoBoard.write(mensagem, mensagem.length, (byte) 0);
        } catch (NullPointerException | IllegalStateException ex) {
            lancarExcecaoErroGoGo(ex);
        }
    }

    @Override
    public int[] receberMensagem() throws ErroExecucaoBiblioteca {
        byte[] mensagem = new byte[TAMANHO_PACOTE];
        try {
            goGoBoard.read(mensagem, 500);
        } catch (NullPointerException | IllegalStateException ex) {
            lancarExcecaoErroGoGo(ex);
        }
        int[] mensagemUint8 = UtilGoGoBoard.Uint8Array(mensagem);
        return mensagemUint8;
    }

    @Override
    public void enviarComando(byte[] comando) throws ErroExecucaoBiblioteca {
        try {
            byte[] cmd = new byte[TAMANHO_PACOTE - 1];
            // Copia o comando passado ignorando o primeiro valor
            for (int i = 0; i < cmd.length; i++) {
                cmd[i] = comando[i + 1];
            }
            goGoBoard.write(cmd, cmd.length, (byte) 0);
        } catch (NullPointerException | IllegalStateException ex) {
            lancarExcecaoErroGoGo(ex);
        }
    }

    /**
     * Método para liberar a GoGo Board para ser utilizada por outro driver.
     */
    public void liberarGoGo() {
        if (goGoBoard != null && goGoBoard.isOpen()) {
            goGoBoard.close();
            goGoBoard = null;
        }
    }

    @Override
    public void enviarByteCode(byte[] byteCode) throws ErroExecucaoBiblioteca {
        super.enviarByteCode(byteCode);
    }

    @Override
    public void hidDeviceAttached(HidServicesEvent evt) {
        if (evt.getHidDevice().getVendorId() == VENDOR_ID
                && evt.getHidDevice().getProductId() == PRODUCT_ID) {
            goGoBoard = servicosHID.getHidDevice(VENDOR_ID, PRODUCT_ID, null);
            System.err.println("[Driver Monitor] - GoGo Conectada: " + goGoBoard);
        }
    }

    @Override
    public void hidDeviceDetached(HidServicesEvent evt) {
        if (evt.getHidDevice().getVendorId() == VENDOR_ID
                && evt.getHidDevice().getProductId() == PRODUCT_ID) {
            goGoBoard = null;
            System.out.println("[Driver Monitor] - GoGo Desconectada");
        }
    }

    @Override
    public void hidFailure(HidServicesEvent evt) {
        System.err.println("[Driver Monitor] - Falha no HID");
    }

    @Override
    public void addHidServicesListener(HidServicesListener listener) {
        servicosHID.addHidServicesListener(listener);
    }
}
