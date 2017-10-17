package br.univali.portugol.plugin.gogoboard.modelo;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.GoGoDriver;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class Sensor {

    private int numero;
    private int valor;

    public Sensor(int nome) {
        this.numero = nome;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getValor() throws ErroExecucaoBiblioteca {
        valor = GoGoDriver.obterInstancia().obterValorSensor(numero);
        return valor;
    }
}
