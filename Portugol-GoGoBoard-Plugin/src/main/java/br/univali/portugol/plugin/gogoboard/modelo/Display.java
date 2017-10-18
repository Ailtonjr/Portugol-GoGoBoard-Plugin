package br.univali.portugol.plugin.gogoboard.modelo;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.GoGoDriver;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class Display {
    GoGoDriver goGoDriver = GoGoDriver.obterInstancia();
    public void exibirTextoCurto(String texto) throws ErroExecucaoBiblioteca{
        goGoDriver.exibirTextoCurto(texto);
    }
    
    public void exibirTextoLongo(String texto) throws ErroExecucaoBiblioteca{
        goGoDriver.exibirTextoLongo(texto);
    }
    
    public void limparTela() throws ErroExecucaoBiblioteca{
        goGoDriver.limparTela();
    }
}
