package br.univali.portugol.plugin.gogoboard.modelo;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.GoGoDriver;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class Led {

    private boolean ligado;
    private int idLed;

    public Led(int idLed){
        this.idLed = idLed;
    }

    public boolean isLigado() {
        return ligado;
    }

    public int getIdLed() {
        return idLed;
    }

    public void controlarLed(int acao) throws ErroExecucaoBiblioteca {
        GoGoDriver.obterInstancia().controlarLed(idLed, acao);
        ligado = true;
    }
}
