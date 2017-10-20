package br.univali.portugol.plugin.gogoboard;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import java.util.List;
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
    private static GoGoDriver goGoDriver;

    /* constantes para leitura de pacotes */
    //Tipos de pacotes
    public static final byte GOGOBOARD = 0;
    public static final byte DEBUG = 1;
    public static final byte RASPBERRYPI = 2;
    public static final byte KEYVALUE = 7;
    
    // Indices e deslocamento
    public static final byte INDICE_TIPO_PLACA = 17;
    public static final byte INDICE_VERSAO_PLACA = 18;
    public static final byte INDICE_VERSAO_FIRMWARE = 20;
    public static final byte DESLOCAMENTO_FORCA_MOTOR = 25;
    public static final byte INDICE_VALOR_IR = 33;
    
    /* Constantes para uso no envio de informações para a GoGoBoard */
    //Categorias
    public static final byte CATEGORIA_SAIDA = 0;
    public static final byte CATEGORIA_MEMORIA = 1;
    public static final byte CATEGORIA_RASPBERRY_PI = 2;

    // Nome dos comandos de saida
    public static final byte CMD_MOTOR_ACAO = 2;
    public static final byte CMD_MOTOR_DIRECAO = 3;
    public static final byte CMD_MOTOR_REV_DIRECAO = 4;
    public static final byte CMD_SET_FORCA = 6;
    public static final byte CMD_SET_PORTAS_ATIVAS = 7;
    public static final byte CMD_ALTERNAR_PORTAS_ATIVAS = 8;
    public static final byte CMD_SET_SERVO_POSICAO = 9;
    public static final byte CMD_CONTROLE_LED = 10;
    public static final byte CMD_BEEP = 11;
    public static final byte CMD_AUTORUN_STATUS = 12;
    public static final byte CMD_SINCRONIZAR_RELOGIO = 50;
    public static final byte CMD_LER_RELOGIO = 51;
    public static final byte CMD_EXIBIR_TEXTO_CURTO = 60;
    public static final byte CMD_EXIBIR_TEXTO_LONGO = 61;
    public static final byte CMD_LIMPAR_TELA = 62;

    // Parametros para comandos de saidas
    public static final byte ID_CATEGORIA = 1;
    public static final byte ID_COMANDO = 2;
    public static final byte PARAMETRO1 = 3;
    public static final byte PARAMETRO2 = 4;
    public static final byte PARAMETRO3 = 5;
    public static final byte PARAMETRO4 = 6;
    public static final byte PARAMETRO5 = 7;
    public static final byte PARAMETRO6 = 8;
    public static final byte PARAMETRO7 = 9;

    // Memory control command names
    private final byte MEM_SETAR_PONTO_LOGO = 1;
    private final byte MEM_SETAR_PONTO = 2;
    private final byte MEM_ESCRITA = 3;
    private final byte MEM_LEITURA = 4;

    public static final byte TAMANHO_PACOTE = 64;

    public static GoGoDriver obterInstancia() {
        // Singleton para retornar sempre a mesma instancia
        if (goGoDriver == null) {
            goGoDriver = new GoGoDriver();
        }
        return goGoDriver;
    }

    private GoGoDriver() {
        try {
            carregarServicosHID();
        } catch (HidException ex) {
            ex.printStackTrace(System.err);
            Logger.getLogger(GoGoDriver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void carregarServicosHID() {
        // Pegar os servicos HID e add listener
        servicosHID = HidManager.getHidServices();
        servicosHID.addHidServicesListener(this);
        servicosHID.start();
    }

    public void addHidServicesListener(HidServicesListener listener) {
        servicosHID.addHidServicesListener(listener);
    }

    public HidDevice getGoGoBoard() {
        HidDevice gogoBoard = null;
        // Percorre a lista dos dispositivos conectados
        List<HidDevice> devices = servicosHID.getAttachedHidDevices();
        for (HidDevice ispositivo : devices) {
            if (ispositivo.isVidPidSerial(0x461, 0x20, null)) {
                gogoBoard = ispositivo;
            }
        }
        //System.out.println("Get GoGo: " + gogoBoard);
        return gogoBoard;
    }

    public void enviarMensagem(byte[] mensagem) throws ErroExecucaoBiblioteca {
        HidDevice gogoBoard = getGoGoBoard();
        try {
            gogoBoard.open();
            gogoBoard.write(mensagem, mensagem.length, (byte) 0);
            gogoBoard.close();
        } catch (NullPointerException | IllegalStateException ex) {
            lancarExcecaoErroGoGo();
        }
    }

    public int[] receberMensagem(int numBytes) throws ErroExecucaoBiblioteca {
        HidDevice gogoBoard = getGoGoBoard();
        byte[] mensagem = new byte[numBytes];
        try {
            gogoBoard.open();
            gogoBoard.read(mensagem, 500);
            gogoBoard.close();
        } catch (NullPointerException | IllegalStateException ex) {
            lancarExcecaoErroGoGo();
        }
        
        int[] mensagemUint8 = Uint8Array(mensagem);
        return mensagemUint8;
    }

    public void enviarComando(byte[] comando) throws ErroExecucaoBiblioteca {
        HidDevice gogoBoard = getGoGoBoard();
        try {

            byte[] cmd = new byte[TAMANHO_PACOTE - 1];
            // Copia o comando passado ignorando o primeiro valor
            for (int i = 0; i < cmd.length; i++) {
                cmd[i] = comando[i + 1];
            }
            gogoBoard.open();
            gogoBoard.write(cmd, cmd.length, (byte) 0);
            gogoBoard.close();
        } catch (NullPointerException | IllegalStateException ex) {
            lancarExcecaoErroGoGo();
        }
    }

    public void enviarByteCode(byte[] byteCode) throws ErroExecucaoBiblioteca {
        setarMemoriaPrograma();
        enviarByteCodeParaMemoria(byteCode, 0);
    }

    private void enviarByteCodeParaMemoria(byte[] byteCode, int deslocamento) throws ErroExecucaoBiblioteca {
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
            acionarBeep();
        } else {
            enviarByteCodeParaMemoria(byteCode, deslocamento);
        }
    }

    private void setarMemoriaPrograma() throws ErroExecucaoBiblioteca {
        byte[] cmd = new byte[TAMANHO_PACOTE];
        cmd[ID_CATEGORIA] = CATEGORIA_MEMORIA;
        cmd[ID_COMANDO] = MEM_SETAR_PONTO_LOGO;
        cmd[PARAMETRO1] = 0;
        cmd[PARAMETRO2] = 0;
        enviarComando(cmd);
    }

    private void lancarExcecaoErroGoGo() throws ErroExecucaoBiblioteca {
        throw new ErroExecucaoBiblioteca("Erro, GoGo Board está sendo usada por outro programa ou não está conectada.");
    }

    public void acionarBeep() throws ErroExecucaoBiblioteca {
        byte[] comando = new byte[TAMANHO_PACOTE];
        comando[ID_COMANDO] = CMD_BEEP;
        enviarComando(comando);
    }

    @Override
    public void hidDeviceAttached(HidServicesEvent hse) {
        if (hse.getHidDevice().getVendorId() == 0x461
                && hse.getHidDevice().getProductId() == 0x20) {
        }
    }

    @Override
    public void hidDeviceDetached(HidServicesEvent hse) {
        /*if (hse.getHidDevice().getVendorId() == 0x461
                && hse.getHidDevice().getProductId() == 0x20) {
        }*/
    }

    @Override
    public void hidFailure(HidServicesEvent hse) {
        System.err.println("Falha no HID");
    }
    
    
    /**
     * Converte um array de bytes em um array de numeros 8-bit inteiros sem sinal.
     *
     * @param array Um array de byte[] que contem a mensagem a ser convertida.
     * @return Um array de int[] com numeros 8-bit inteiros sem sinal.
     * 
     */
    private int[] Uint8Array(byte[] array){
        int[] mensagem = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            mensagem[i] = (0xFF & array[i]);
        }
        return mensagem;
    }
    
    /**
     * Converte dois de numeros 8-bit inteiros sem sinal em um numero inteiros.
     *
     * @param byteAlto byte[] correspondente ao byte alto.
     * @param byteBaixo byte[] correspondente ao byte baixo.
     * @return Um valor inteiro correspondente aos dois de numeros 8-bit inteiros sem sinal.
     * 
     */
    public int bytesToInt(int byteAlto, int byteBaixo) {
        return ((byteAlto << 8) + byteBaixo);
    }
}
