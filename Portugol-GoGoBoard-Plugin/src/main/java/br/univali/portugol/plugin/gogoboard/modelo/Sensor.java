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
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public void atualizaValor() throws ErroExecucaoBiblioteca {
        //System.err.println("Lendo Sensores\n");
        int[] mensagem;
        do {
            mensagem = GoGoDriver.obterInstancia().receberMensagem(64);
        } while (mensagem[0] != GoGoDriver.GOGOBOARD);       // Se não for uma mensagem da GoGo, tenta novamente
        int byteAlto = mensagem[1 + (numero * 2)];
        int byteBaixo = mensagem[1 + (numero * 2) + 1];
        valor = GoGoDriver.obterInstancia().bytesToInt(byteAlto, byteBaixo);
    }
}
