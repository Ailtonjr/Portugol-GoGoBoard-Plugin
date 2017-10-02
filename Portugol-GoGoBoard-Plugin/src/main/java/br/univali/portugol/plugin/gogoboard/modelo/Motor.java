package br.univali.portugol.plugin.gogoboard.modelo;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.GoGoDriver;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class Motor
{
    private int numMotor;
    private boolean ligado;
    private boolean direita;

    public Motor(int numMotor)
    {
        this.numMotor = numMotor;
    }

    public void selecionarMotor() throws ErroExecucaoBiblioteca
    {
        byte[] mensagem = new byte[64];
        mensagem[2] = 7;
        mensagem[3] = (byte) numMotor;
        GoGoDriver.obterInstancia().enviarMensagem(mensagem);
    }

    public int getNumMotor()
    {
        return numMotor;
    }

    public void setNumMotor(int numMotor)
    {
        this.numMotor = numMotor;
    }

    public boolean isLigado()
    {
        return ligado;
    }

    public boolean isDireita()
    {
        return direita;
    }

    public void setLigado(boolean ligado)
    {
        this.ligado = ligado;
    }

    public void setDireita(boolean direita)
    {
        this.direita = direita;
    }
}
