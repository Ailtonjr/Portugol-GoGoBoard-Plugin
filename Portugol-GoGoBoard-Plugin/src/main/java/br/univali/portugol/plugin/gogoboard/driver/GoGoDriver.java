package br.univali.portugol.plugin.gogoboard.driver;

import br.univali.portugol.plugin.gogoboard.gerenciadores.GerenciadorDriver;
import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;

/**
 * Classe abstrata base para o driver da GoGo Board.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
public abstract class GoGoDriver {

    /**
     * Enum referente ao tipo de driver.
     */
    public static enum TIPODRIVER {
        BIBLIOTECA,
        MONITOR,
        Plugin
    };

    /**
     * Constantes para leitura de pacotes.
     *
     * Tipos de pacotes
     */
    public static final byte GOGOBOARD = 0;
    public static final byte DEBUG = 1;
    public static final byte RASPBERRYPI = 2;
    public static final byte KEYVALUE = 7;

    /**
     * Indices e deslocamento
     */
    public static final byte INDICE_TIPO_PLACA = 17;
    public static final byte INDICE_VERSAO_PLACA = 18;
    public static final byte INDICE_VERSAO_FIRMWARE = 20;
    public static final byte DESLOCAMENTO_FORCA_MOTOR = 25;
    public static final byte INDICE_VALOR_IR = 33;

    /**
     * Constantes para uso no envio de informações para a GoGoBoard
     *
     * Categorias
     */
    public static final byte CATEGORIA_SAIDA = 0;
    public static final byte CATEGORIA_MEMORIA = 1;
    public static final byte CATEGORIA_RASPBERRY_PI = 2;

    /**
     * Nome dos comandos de saida
     */
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

    /**
     * Parametros para comandos de saídas
     */
    public static final byte ID_CATEGORIA = 1;
    public static final byte ID_COMANDO = 2;
    public static final byte PARAMETRO1 = 3;
    public static final byte PARAMETRO2 = 4;
    public static final byte PARAMETRO3 = 5;
    public static final byte PARAMETRO4 = 6;
    public static final byte PARAMETRO5 = 7;
    public static final byte PARAMETRO6 = 8;
    public static final byte PARAMETRO7 = 9;

    /**
     * Parametros para controle de memoria
     */
    public static final byte MEM_SETAR_PONTO_LOGO = 1;
    public static final byte MEM_SETAR_PONTO = 2;
    public static final byte MEM_ESCRITA = 3;
    public static final byte MEM_LEITURA = 4;

    /**
     * Tamanho dos pacotes enviados.
     */
    public static final byte TAMANHO_PACOTE = 64;

    /**
     * Constantes para as informações do dispositivo HID.
     */
    public static final int VENDOR_ID = 0x461;
    public static final int PRODUCT_ID = 0x20;

    protected HidServices servicosHID;

    /**
     * Método para iniciar os serviços HID.
     */
    public abstract void iniciarServicosHID();

    /**
     * Método para adicionar um listener ao serviço HID.
     *
     * @param listener Objeto que implemente a interface HidServicesListener.
     */
    public void addHidServicesListener(HidServicesListener listener) {
        servicosHID.addHidServicesListener(listener);
    }

    /**
     * Método para enviar uma mensagem à GoGo Board.
     *
     * @param mensagem Mensagem a ser enviada.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public abstract void enviarMensagem(byte[] mensagem) throws ErroExecucaoBiblioteca;

    /**
     * Método para enviar uma mensagem à GoGo Board.
     *
     * @return Array de int[] com números 8-bit inteiros sem sinal.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public abstract int[] receberMensagem() throws ErroExecucaoBiblioteca;

    /**
     * Método para enviar uma mensagem à GoGo Board.
     *
     * @param comando Array de bytes com os comandos a ser enviados.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public abstract void enviarComando(byte[] comando) throws ErroExecucaoBiblioteca;

    /**
     * Método para enviar o bytecode à GoGo Board.
     *
     * @param byteCode Array de bytes que contem o bytecode a ser enviados.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public void enviarByteCode(byte[] byteCode) throws ErroExecucaoBiblioteca {
        setarMemoriaPrograma();
        enviarByteCodeParaMemoria(byteCode, 0);
    }

    /**
     * Método recursivo para enviar o bytecode para a memória da GoGo Board.
     *
     * @param byteCode Array de bytes que contem o bytecode a ser enviados.
     * @param deslocamento Indice de deslocamento para a recursividade. Deve ser
     * 0 para a primeira iteração.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
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

        // Copia do conteudo do byteCode para o array de comandos
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
            Logger.getLogger(GerenciadorDriver.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Verifica se ja enviou o bytecode completamente, senão chama novamente passando o deslocamento atual
        if (deslocamento > byteCode.length) {
            acionarBeep();
        } else {
            enviarByteCodeParaMemoria(byteCode, deslocamento);
        }
    }

    /**
     * Método para setar o ponteiro para a memória de programas da GoGo Board.
     *
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    private void setarMemoriaPrograma() throws ErroExecucaoBiblioteca {
        byte[] cmd = new byte[TAMANHO_PACOTE];
        cmd[ID_CATEGORIA] = CATEGORIA_MEMORIA;
        cmd[ID_COMANDO] = MEM_SETAR_PONTO_LOGO;
        cmd[PARAMETRO1] = 0;
        cmd[PARAMETRO2] = 0;
        enviarComando(cmd);
    }

    /**
     * Método interno para acionar o beep após envio do bytecode para a GoGo
     * Board.
     *
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    private void acionarBeep() throws ErroExecucaoBiblioteca {
        byte[] comando = new byte[TAMANHO_PACOTE];
        comando[ID_COMANDO] = CMD_BEEP;
        enviarComando(comando);
    }

    /**
     * Método para lançar uma exceção padrão de indisponibilidade daGoGo Board.
     *
     * @param ex Exceção capturada.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    protected void lancarExcecaoErroGoGo(Exception ex) throws ErroExecucaoBiblioteca {
        Logger.getLogger(GoGoDriver.class.getName()).log(Level.SEVERE, null, ex);
        throw new ErroExecucaoBiblioteca("Erro, GoGo Board está sendo usada por outro programa ou não está conectada.");
    }
}
