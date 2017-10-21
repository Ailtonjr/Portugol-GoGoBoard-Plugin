package br.univali.portugol.plugin.gogoboard.componetes;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.driver.GerenciadorDeDriver;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;
import br.univali.portugol.plugin.gogoboard.util.UtilGoGoBoard;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class Sensor {

    private GoGoDriver goGoDriver;
    private int numero;
    private int valor;

    public Sensor(int nome, UtilGoGoBoard.TipoDriver tipoDriver) {
        this.goGoDriver = GerenciadorDeDriver.getGoGoDriver(tipoDriver);
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
            mensagem = goGoDriver.receberMensagem();
        } while (mensagem[0] != GoGoDriver.GOGOBOARD);       // Se não for uma mensagem da GoGo, tenta novamente
        int byteAlto = mensagem[1 + (numero * 2)];
        int byteBaixo = mensagem[1 + (numero * 2) + 1];
        valor = UtilGoGoBoard.bytesToInt(byteAlto, byteBaixo);
    }
}
