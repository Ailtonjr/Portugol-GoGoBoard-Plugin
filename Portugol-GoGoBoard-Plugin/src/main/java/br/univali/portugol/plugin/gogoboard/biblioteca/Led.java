package br.univali.portugol.plugin.gogoboard.biblioteca;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class Led
{
    private boolean ligado = false;
    
    public boolean isLigado()
    {
        return ligado;
    }
    
    public void ligar(boolean ligar) throws ErroExecucaoBiblioteca
    {
        byte[] mensagem = new byte[64];
        mensagem[2] = 10;
        if (ligar)
        {
            mensagem[4] = 1;
        }
        else
        {
            mensagem[4] = 0;
        }
        GoGoDriver.obterInstancia().enviarMensagem(mensagem);
    }
}
