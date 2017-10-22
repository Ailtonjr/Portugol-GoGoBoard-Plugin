package br.univali.portugol.plugin.gogoboard.componetes;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.gerenciadores.GerenciadorDriver;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;

/**
 * Classe base do motor.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
public class Motor {

    protected int idMotor;
    protected boolean ligado;
    protected boolean direita;
    protected GoGoDriver goGoDriver;

    /**
     * Construtor padrão do motor.
     *
     * @param numMotor Inteiro correspondente ao ID do led. 0 = Led do usuário.
     * @param tipoDriver Enum referente ao tipo de driver necessário.
     */
    public Motor(int numMotor, GoGoDriver.TIPODRIVER tipoDriver) {
        this.idMotor = numMotor;
        this.goGoDriver = GerenciadorDriver.getGoGoDriver(tipoDriver);
    }

    /**
     * Método para obter o ID do motor.
     *
     * @return ID do motor.
     */
    public int getIdMotor() {
        return idMotor;
    }

    /**
     * Método para obter o status do motor.
     *
     * @return Status do motor.
     */
    public boolean isLigado() {
        return ligado;
    }

    /**
     * Método para obter a direção do motor.
     *
     * @return Direção do motor.
     */
    public boolean isDireita() {
        return direita;
    }

    /**
     * Método para setar o status do motor.
     *
     * @param ligado Boleano que representa o status.
     */
    public void setLigado(boolean ligado) {
        this.ligado = ligado;
    }

    /**
     * Método para setar a direção do motor.
     *
     * @param direita Boleano que representa a direção do motor.
     */
    public void setDireita(boolean direita) {
        this.direita = direita;
    }

    /**
     * Método para selecionar o motor.
     *
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    protected void selecionarMotor() throws ErroExecucaoBiblioteca {
        byte[] cmd = new byte[GoGoDriver.TAMANHO_PACOTE];
        cmd[GoGoDriver.ID_COMANDO] = GoGoDriver.CMD_SET_PORTAS_ATIVAS;
        cmd[GoGoDriver.PARAMETRO1] = (byte) idMotor;
        goGoDriver.enviarComando(cmd);
    }
}
