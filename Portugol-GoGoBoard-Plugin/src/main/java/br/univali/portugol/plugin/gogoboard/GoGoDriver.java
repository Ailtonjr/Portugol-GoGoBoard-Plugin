package br.univali.portugol.plugin.gogoboard;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
    //private HidDevice gogoBoard;
    private static GoGoDriver goGoDriver;

    /* Constantes para uso no envio de informações para a GoGoBoard */
    //Categorias
    private final byte CATEGORIA_SAIDA = 0;
    private final byte CATEGORIA_MEMORIA = 1;
    private final byte CATEGORIA_RASPBERRY_PI = 2;

    // Nome dos comandos de saida
    private final byte CMD_MOTOR_ACAO = 2;
    private final byte CMD_MOTOR_DIRECAO = 3;
    private final byte CMD_MOTOR_REV_DIRECAO = 4;
    private final byte CMD_SET_FORCA = 6;
    private final byte CMD_SET_PORTAS_ATIVAS = 7;
    private final byte CMD_ALTERNAR_PORTAS_ATIVAS = 8;
    private final byte CMD_SET_SERVO_POSICAO = 9;
    private final byte CMD_CONTROLE_LED = 10;
    private final byte CMD_BEEP = 11;
    private final byte CMD_AUTORUN_STATUS = 12;
    private final byte CMD_SINCRONIZAR_RELOGIO = 50;
    private final byte CMD_LER_RELOGIO = 51;
    private final byte CMD_EXIBIR_TEXTO_CURTO = 60;
    private final byte CMD_EXIBIR_TEXTO_LONGO = 61;
    private final byte CMD_LIMPAR_TELA = 62;

    // Parametros para comandos de saidas
    private final byte ID_CATEGORIA = 1;
    private final byte ID_COMANDO = 2;
    private final byte PARAMETRO1 = 3;
    private final byte PARAMETRO2 = 4;
    private final byte PARAMETRO3 = 5;
    private final byte PARAMETRO4 = 6;
    private final byte PARAMETRO5 = 7;
    private final byte PARAMETRO6 = 8;
    private final byte PARAMETRO7 = 9;

    // Memory control command names
    private final byte MEM_SETAR_PONTO_LOGO = 1;
    private final byte MEM_SETAR_PONTO = 2;
    private final byte MEM_ESCRITA = 3;
    private final byte MEM_LEITURA = 4;

    private final byte TAMANHO_PACOTE = 64;

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
        //gogoBoard = getDispositivoGoGoBoard();
    }

    private HidDevice getDispositivoGoGoBoard() {
        HidDevice gogoBoard = null;
        // Percorre a lista dos dispositivos conectados
        List<HidDevice> devices = servicosHID.getAttachedHidDevices();
        for (HidDevice ispositivo : devices) {
            if (ispositivo.isVidPidSerial(0x461, 0x20, null)) {
                gogoBoard = ispositivo;
            }
        }
        System.out.println("Get GoGo: " + gogoBoard);
        return gogoBoard;
    }

    public void enviarMensagem(byte[] mensagem) throws ErroExecucaoBiblioteca {
        HidDevice gogoBoard = getDispositivoGoGoBoard();
        try {
            gogoBoard.open();
            gogoBoard.write(mensagem, mensagem.length, (byte) 0);
            gogoBoard.close();
        } catch (NullPointerException | IllegalStateException ex) {
            lancarExcecaoErroGoGo();
        }
    }

    private byte[] receberMensagem(int numBytes) throws ErroExecucaoBiblioteca {
        HidDevice gogoBoard = getDispositivoGoGoBoard();
        byte[] mensagem = new byte[numBytes];
        try {
            gogoBoard.open();
            gogoBoard.read(mensagem, 500);
            gogoBoard.close();
        } catch (NullPointerException | IllegalStateException ex) {
            lancarExcecaoErroGoGo();
        }
        return mensagem;
    }

    public void enviarComando(byte[] comando) throws ErroExecucaoBiblioteca {
        HidDevice gogoBoard = getDispositivoGoGoBoard();
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
            beep();
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

    public int[] obterValorSensores() throws ErroExecucaoBiblioteca {
        System.err.println("Lendo Sensores\n");
        int[] sensores = new int[8];
        byte[] mensagem;
        do {
            mensagem = receberMensagem(64);
        } while (mensagem[0] != 0);       // Evitar pegar valor zerado do sensor
        for (int i = 0; i < 8; i++) {
            ByteBuffer bb = ByteBuffer.wrap(mensagem, (2 * (i)) + 1, 2);
            bb.order(ByteOrder.BIG_ENDIAN);
            sensores[i] = bb.getShort();
        }
        return sensores;
    }

    public int obterValorSensor(int num) throws ErroExecucaoBiblioteca {
        return obterValorSensores()[num];
    }

    public void beep() throws ErroExecucaoBiblioteca {
        byte[] comando = new byte[TAMANHO_PACOTE];
        comando[ID_COMANDO] = CMD_BEEP;
        enviarComando(comando);
    }

    public void exibirTextoCurto(String texto) throws ErroExecucaoBiblioteca {
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
    }

    public void controlarLed(int idLed, boolean ligar) throws ErroExecucaoBiblioteca {
        byte[] cmd = new byte[TAMANHO_PACOTE];
        cmd[ID_COMANDO] = CMD_CONTROLE_LED;
        cmd[PARAMETRO1] = (byte) idLed;  // 0 = para led do usuário
        cmd[PARAMETRO2] = boolToByte(ligar);   // retorna 0 = desligado, 1 = ligado

        enviarComando(cmd);
    }

    private void selecionarMotor(int numMotor) throws ErroExecucaoBiblioteca {
        byte[] cmd = new byte[TAMANHO_PACOTE];
        cmd[ID_COMANDO] = CMD_SET_PORTAS_ATIVAS;
        cmd[PARAMETRO1] = (byte) numMotor;
        enviarComando(cmd);
    }

    public void controlarMotor(int numMotor, boolean ligar) throws ErroExecucaoBiblioteca {
        selecionarMotor(numMotor);
        byte[] cmd = new byte[TAMANHO_PACOTE];
        cmd[ID_COMANDO] = CMD_MOTOR_ACAO;
        cmd[PARAMETRO1] = 0;
        cmd[PARAMETRO2] = boolToByte(ligar); // retorna 0 = desligado, 1 = ligado
        enviarComando(cmd);
    }

    public void inverterDirecaoMotor(int numMotor) throws ErroExecucaoBiblioteca {
        selecionarMotor(numMotor);
        byte[] cmd = new byte[TAMANHO_PACOTE];
        cmd[ID_COMANDO] = CMD_MOTOR_REV_DIRECAO;
        cmd[PARAMETRO1] = 0;
        enviarComando(cmd);
    }

    public void definirDirecaoMotor(int numMotor, int direcao) throws ErroExecucaoBiblioteca {
        selecionarMotor(numMotor);
        byte[] cmd = new byte[TAMANHO_PACOTE];
        cmd[ID_COMANDO] = CMD_MOTOR_DIRECAO;
        cmd[PARAMETRO1] = 0;
        cmd[PARAMETRO2] = (byte) direcao; // 0 = esquerda, 1 = direita
        enviarComando(cmd);
    }

    public void setarForcaMotor(int numMotor, int forca) throws ErroExecucaoBiblioteca {
        selecionarMotor(numMotor);
        int byteAlto = (forca >> 8);
        int byteBaixo = ((forca & 0xff) & 0xff);

        byte[] cmd = new byte[TAMANHO_PACOTE];
        cmd[ID_COMANDO] = CMD_SET_FORCA;
        cmd[PARAMETRO1] = 0;
        cmd[PARAMETRO2] = (byte) byteAlto;
        cmd[PARAMETRO3] = (byte) byteBaixo;
        enviarComando(cmd);
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

    private byte boolToByte(boolean b) {
        return (byte) (b ? 1 : 0);
    }
}
