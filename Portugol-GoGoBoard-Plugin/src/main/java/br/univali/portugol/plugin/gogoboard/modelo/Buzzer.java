package br.univali.portugol.plugin.gogoboard.modelo;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.GoGoDriver;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class Buzzer {
    public void acionarBeep() throws ErroExecucaoBiblioteca{
        GoGoDriver.obterInstancia().acionarBeep();
    }
}
