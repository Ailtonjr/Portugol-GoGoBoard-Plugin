package br.univali.portugol.plugin.gogoboard.modelo;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class Motor {

    protected int numMotor;
    protected boolean ligado;
    protected boolean direita;

    public Motor(int numMotor) {
        this.numMotor = numMotor;
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
