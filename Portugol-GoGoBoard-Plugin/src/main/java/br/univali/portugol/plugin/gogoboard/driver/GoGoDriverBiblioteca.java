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

/**
 * Classe utilizada na biblioteca com funções para envio e recebimento de
 * pacotes via protocolo HID para a GoGo Board.
 *
 * O dispositivo é aberto em cada comunicação e já é fechado ao final da mesma.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
public final class GoGoDriverBiblioteca extends GoGoDriver {

    /**
     * Construtor padrão do driver da GoGo Board.
     */
    public GoGoDriverBiblioteca() {
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
        servicosHID.start();
    }

    @Override
    public void addHidServicesListener(HidServicesListener listener) {
        servicosHID.addHidServicesListener(listener);
    }

    /**
     * Método para obter a GoGo Board da lista dos dispositivos HID.
     *
     * @return HidDevice GoGo Board.
     */
    public HidDevice getGoGoBoard() {
        HidDevice goGoBoard = null;
        for (HidDevice dispositivo : servicosHID.getAttachedHidDevices()) {
            if (dispositivo.isVidPidSerial(VENDOR_ID, PRODUCT_ID, null)) {
                goGoBoard = dispositivo;
            }
        }
        return goGoBoard;
    }

    @Override
    public void enviarMensagem(byte[] mensagem) throws ErroExecucaoBiblioteca {
        HidDevice goGoBoard = getGoGoBoard();
        try {
            goGoBoard.open();
            goGoBoard.write(mensagem, mensagem.length, (byte) 0);
            goGoBoard.close();
        } catch (NullPointerException | IllegalStateException ex) {
            lancarExcecaoErroGoGo(ex);
        }
    }

    @Override
    public int[] receberMensagem() throws ErroExecucaoBiblioteca {
        HidDevice goGoBoard = getGoGoBoard();

        byte[] mensagem = new byte[TAMANHO_PACOTE];
        try {
            goGoBoard.open();
            goGoBoard.read(mensagem, 500);
            goGoBoard.close();
        } catch (NullPointerException | IllegalStateException ex) {
            lancarExcecaoErroGoGo(ex);
        }
        int[] mensagemUint8 = UtilGoGoBoard.Uint8Array(mensagem);
        return mensagemUint8;
    }

    @Override
    public void enviarComando(byte[] comando) throws ErroExecucaoBiblioteca {
        HidDevice goGoBoard = getGoGoBoard();

        try {
            byte[] cmd = new byte[TAMANHO_PACOTE - 1];
            // Copia o comando passado ignorando o primeiro valor
            for (int i = 0; i < cmd.length; i++) {
                cmd[i] = comando[i + 1];
            }
            goGoBoard.open();
            goGoBoard.write(cmd, cmd.length, (byte) 0);
            goGoBoard.close();
        } catch (NullPointerException | IllegalStateException ex) {
            lancarExcecaoErroGoGo(ex);
        }
    }

    @Override
    public void enviarByteCode(byte[] byteCode) throws ErroExecucaoBiblioteca {
        super.enviarByteCode(byteCode);
    }
}
