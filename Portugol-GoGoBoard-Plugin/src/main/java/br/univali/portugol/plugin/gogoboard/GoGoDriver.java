package br.univali.portugol.plugin.gogoboard;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hid4java.HidDevice;
import org.hid4java.HidException;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.event.HidServicesEvent;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class GoGoDriver implements HidServicesListener {

    private HidServices servicosHID;
    private HidDevice gogoBoard;
    private static GoGoDriver gogoDriver;

    /* Constantes para uso no envio de informações para a GoGoBoard */
    //Categorias
    public final byte CATEGORIA_SAIDA = 0;
    public final byte CATEGORIA_MEMORIA = 1;
    public final byte CATEGORIA_RASPBERRY_PI = 2;

    // Nome dos comandos de saida
    public final byte CMD_MOTOR_ACAO = 2;
    public final byte CMD_MOTOR_DIRECAO = 3;
    public final byte CMD_MOTOR_REV_DIRECAO = 4;
    public final byte CMD_SET_FORCA = 6;
    public final byte CMD_SET_PORTAS_ATIVAS = 7;
    public final byte CMD_ALTERNAR_PORTAS_ATIVAS = 8;
    public final byte CMD_SET_SERVO_POSICAO = 9;
    public final byte CMD_CONTROLE_LED = 10;
    public final byte CMD_BEEP = 11;
    public final byte CMD_AUTORUN_STATUS = 12;
    public final byte CMD_SINCRONIZAR_RELOGIO = 50;
    public final byte CMD_LER_RELOGIO = 51;
    public final byte CMD_EXIBIR_TEXTO_CURTO = 60;
    public final byte CMD_EXIBIR_TEXTO_LONGO = 61;
    public final byte CMD_LIMPAR_TELA = 62;

    // Parametros para comandos de saidas
    public final byte ID_CATEGORIA = 1;
    public final byte ID_COMANDO = 2;
    public final byte PARAMETRO1 = 3;
    public final byte PARAMETRO2 = 4;
    public final byte PARAMETRO3 = 5;
    public final byte PARAMETRO4 = 6;
    public final byte PARAMETRO5 = 7;
    public final byte PARAMETRO6 = 8;
    public final byte PARAMETRO7 = 9;

    // Memory control command names
    public final byte MEM_SETAR_PONTO_LOGO = 1;
    public final byte MEM_SETAR_PONTO = 2;
    public final byte MEM_ESCRITA = 3;
    public final byte MEM_LEITURA = 4;

    public final byte TAMANHO_PACOTE = 64;

    public static GoGoDriver obterInstancia() {
        // Singleton para retornar sempre a mesma instancia
        if (gogoDriver == null) {
            gogoDriver = new GoGoDriver();
        }
        return gogoDriver;
    }

    public GoGoDriver() {
        try {
            carregarServicosHID();
        } catch (HidException ex) {
            ex.printStackTrace(System.err);
            Logger.getLogger(GoGoDriver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarComando(byte[] comando) throws ErroExecucaoBiblioteca {
        if (gogoBoard != null) {
            byte[] cmd = new byte[TAMANHO_PACOTE - 1];
            // Copia o comando passado ignorando o primeiro valor
            for (int i = 0; i < cmd.length; i++) {
                cmd[i] = comando[i + 1];
            }
            gogoBoard.write(cmd, cmd.length, (byte) 0);
        } else {
            throw new ErroExecucaoBiblioteca("Erro, GoGo Board está sendo usada por outro programa ou não está conectada.");
        }
    }

    public void enviarMensagem(byte[] mensagem) throws ErroExecucaoBiblioteca {
        if (gogoBoard != null) {
            gogoBoard.write(mensagem, mensagem.length, (byte) 0);
        } else {
            throw new ErroExecucaoBiblioteca("Erro, GoGo Board está sendo usada por outro programa ou não está conectada.");
        }
    }

    public byte[] receberMensagem(int numBytes) throws ErroExecucaoBiblioteca {
        byte[] mensagem = new byte[numBytes];
        if (gogoBoard != null) {
            gogoBoard.read(mensagem, 500);
            return mensagem;
        } else {
            throw new ErroExecucaoBiblioteca("Erro, GoGo Board está sendo usada por outro programa ou não está conectada.");
        }
    }

    public void enviarByteCode(byte[] byteCode) throws ErroExecucaoBiblioteca {
        setarMemoriaPrograma();
        enviarByteCodeParaMemoria(byteCode, 0);
    }

    private void setarMemoriaPrograma() throws ErroExecucaoBiblioteca {
        byte[] cmd = new byte[TAMANHO_PACOTE];
        cmd[ID_CATEGORIA] = CATEGORIA_MEMORIA;
        cmd[ID_COMANDO] = MEM_SETAR_PONTO_LOGO;
        cmd[PARAMETRO1] = 0;
        cmd[PARAMETRO2] = 0;
        enviarComando(cmd);
    }

    private void enviarByteCodeParaMemoria(byte[] byteCode, int deslocamento) throws ErroExecucaoBiblioteca{
        byte[] cmd = new byte[TAMANHO_PACOTE];
        cmd[ID_CATEGORIA] = CATEGORIA_MEMORIA;
        cmd[ID_COMANDO] = MEM_ESCRITA;

        int tamanhoEnvio = byteCode.length;

        // Informa o tamanho do bytecode que será enviado para GoGo
        if (tamanhoEnvio - deslocamento > (TAMANHO_PACOTE - 4)) {
            cmd[PARAMETRO1] = TAMANHO_PACOTE - 4;
        } else {
            cmd[PARAMETRO1] = (byte) (tamanhoEnvio - deslocamento);
        }

        // Copia do conteudo do byteCode para o vetor de comandos
        for (int i = 0; i < byteCode.length; i++) {
            cmd[4 + i] = byteCode[deslocamento + i];
        }

        // Guarda o deslocamento atual
        deslocamento += TAMANHO_PACOTE - 4;

        enviarComando(cmd);
        // Não mostrar essa excecao ao usuário
        try {
            Thread.sleep(250);
        } catch (InterruptedException ex) {
            Logger.getLogger(GoGoDriver.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Verifica se ja enviou o bytecode completamente, senão chama novamente passando o deslocamento atual
        if (deslocamento > byteCode.length) {
            beep();
        } else {
            enviarByteCodeParaMemoria(byteCode, deslocamento);
        }
    }

    public void beep() throws ErroExecucaoBiblioteca {
        byte[] comando = new byte[TAMANHO_PACOTE];
        comando[ID_COMANDO] = CMD_BEEP;
        enviarComando(comando);
    }

    public void exibeTextoCurto(String texto) throws ErroExecucaoBiblioteca {
        if (gogoBoard != null) {
            if (texto.length() > 4) {
                throw new ErroExecucaoBiblioteca("Erro, o display de segmentos não pode exibir mais de 4 characteres.");
            }
            byte[] cmd = new byte[TAMANHO_PACOTE];
            cmd[ID_COMANDO] = CMD_EXIBIR_TEXTO_CURTO;
            // Copiar o conteudo do texto para enviar para a GoGo
            for (int i = 0; i < texto.length(); i++) {
                cmd[3 + i] = (byte) texto.charAt(i);
            }
            enviarComando(cmd);
        } else {
            throw new ErroExecucaoBiblioteca("Erro, GoGo Board está sendo usada por outro programa ou não está conectada.");
        }
    }

    public void carregarServicosHID() {
        // Pegar os servicos HID e add listener
        servicosHID = HidManager.getHidServices();
        servicosHID.addHidServicesListener(this);

        System.out.println("Iniciando Driver GoGo");
        servicosHID.start();

        // Percorre a lista dos dispositivos conectados
        gogoBoard = servicosHID.getHidDevice(0x461, 0x20, null);
        System.out.println("GoGo Board1: " + gogoBoard);
        System.out.println("");
    }

    @Override
    public void hidDeviceAttached(HidServicesEvent hse) {
        if (hse.getHidDevice().getVendorId() == 0x461
                && hse.getHidDevice().getProductId() == 0x20) {
            System.out.println("GoGo Board: " + hse.getHidDevice());
            gogoBoard = servicosHID.getHidDevice(0x461, 0x20, null);
            System.out.println("");
        }
    }

    @Override
    public void hidDeviceDetached(HidServicesEvent hse) {
        if (hse.getHidDevice().getVendorId() == 0x461
                && hse.getHidDevice().getProductId() == 0x20) {
            System.out.println("GoGo Board: " + hse.getHidDevice());
            gogoBoard = null;
        }
    }

    @Override
    public void hidFailure(HidServicesEvent hse) {
        System.err.println("Falha no HID");
    }
}
