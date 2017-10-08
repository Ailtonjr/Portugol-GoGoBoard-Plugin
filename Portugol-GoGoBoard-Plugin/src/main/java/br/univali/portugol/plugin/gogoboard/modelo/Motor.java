package br.univali.portugol.plugin.gogoboard.modelo;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.GoGoDriver;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class Motor {

    private int numMotor;
    private boolean ligado;
    private boolean direita;
    public GoGoDriver gogoDriver;

    public Motor(int numMotor) {
        this.gogoDriver = GoGoDriver.obterInstancia();
        this.numMotor = numMotor;
    }

    public void selecionarMotor() throws ErroExecucaoBiblioteca {
        byte[] cmd = new byte[gogoDriver.TAMANHO_PACOTE];
        cmd[gogoDriver.ID_COMANDO] = gogoDriver.CMD_SET_PORTAS_ATIVAS;
        cmd[gogoDriver.PARAMETRO1] = (byte) numMotor;
        gogoDriver.enviarComando(cmd);
    }

    public int getNumMotor() {
        return numMotor;
    }

    public void setNumMotor(int numMotor) {
        this.numMotor = numMotor;
    }

    public boolean isLigado() {
        return ligado;
    }

    public boolean isDireita() {
        return direita;
    }

    public void setLigado(boolean ligado) {
        this.ligado = ligado;
    }

    public void setDireita(boolean direita) {
        this.direita = direita;
    }
}
