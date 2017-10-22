package br.univali.portugol.plugin.gogoboard.componetes;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;

/**
 * Classe que representa o motor DC, extendendo Motor.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
public class DCMotor extends Motor {

    /**
     * Construtor padrão do motor DC.
     *
     * @param numMotor Número correspondente ao índice do motor, iniciando em 0.
     * @param tipoDriver Enum referente ao tipo de driver necessário.
     * @see Motor
     */
    public DCMotor(int numMotor, GoGoDriver.TipoDriver tipoDriver) {
        super(numMotor, tipoDriver);
    }

    /**
     * Método para ligar o motor.
     *
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public void ligar() throws ErroExecucaoBiblioteca {
        controlarStatus(1);
        setLigado(true);
    }

    /**
     * Método para desligar o motor.
     *
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public void desligar() throws ErroExecucaoBiblioteca {
        controlarStatus(0);
        setLigado(false);
    }

    /**
     * Método para inverter a direção atual do motor.
     *
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public void inverterDirecao() throws ErroExecucaoBiblioteca {
        if (isDireita()) {
            definirDirecao(1);
            setDireita(false);
        } else {
            definirDirecao(0);
            setDireita(true);
        }
    }

    /**
     * Método para definir a direção do motor.
     *
     * @param direcao Inteiro correspondente à direção. 0 = Esquerda e 1 =
     * Direita.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public void definirDirecao(int direcao) throws ErroExecucaoBiblioteca {
        if (!isLigado()) {
            ligar();
        }
        controlarDirecao(direcao);
        if (direcao == 1) {
            setDireita(true);
        } else {
            setDireita(false);
        }
    }

    /**
     * Método para definir a força do motor.
     *
     * @param forca Inteiro correspondente à força do motor.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public void definirForca(int forca) throws ErroExecucaoBiblioteca {
        if (isLigado()) {
            controlarForca(forca);
        }
    }

    /**
     * Método privado para controlar o status do motor.
     *
     * @param forca Inteiro correspondente à ação que mudará o status do motor.
     * 0 = desligado, 1 = ligado.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    private void controlarStatus(int acao) throws ErroExecucaoBiblioteca {
        selecionarMotor();
        byte[] cmd = new byte[GoGoDriver.TAMANHO_PACOTE];
        cmd[GoGoDriver.ID_COMANDO] = GoGoDriver.CMD_MOTOR_ACAO;
        cmd[GoGoDriver.PARAMETRO1] = 0;
        cmd[GoGoDriver.PARAMETRO2] = (byte) acao;
        goGoDriver.enviarComando(cmd);
    }

    /**
     * Método privado para controlar a direção do motor.
     *
     * @param direcao Inteiro correspondente à direção do motor. 0 = esquerda, 1
     * = direita.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    private void controlarDirecao(int direcao) throws ErroExecucaoBiblioteca {
        selecionarMotor();
        byte[] cmd = new byte[GoGoDriver.TAMANHO_PACOTE];
        cmd[GoGoDriver.ID_COMANDO] = GoGoDriver.CMD_MOTOR_DIRECAO;
        cmd[GoGoDriver.PARAMETRO1] = 0;
        cmd[GoGoDriver.PARAMETRO2] = (byte) direcao;
        goGoDriver.enviarComando(cmd);
    }

    /**
     * Método privado para controlar a força do motor.
     *
     * @param forca Inteiro correspondente à força do motor.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    private void controlarForca(int forca) throws ErroExecucaoBiblioteca {
        selecionarMotor();
        int byteAlto = (forca >> 8);
        int byteBaixo = ((forca & 0xff) & 0xff);

        byte[] cmd = new byte[GoGoDriver.TAMANHO_PACOTE];
        cmd[GoGoDriver.ID_COMANDO] = GoGoDriver.CMD_SET_FORCA;
        cmd[GoGoDriver.PARAMETRO1] = 0;
        cmd[GoGoDriver.PARAMETRO2] = (byte) byteAlto;
        cmd[GoGoDriver.PARAMETRO3] = (byte) byteBaixo;
        goGoDriver.enviarComando(cmd);
    }
}
