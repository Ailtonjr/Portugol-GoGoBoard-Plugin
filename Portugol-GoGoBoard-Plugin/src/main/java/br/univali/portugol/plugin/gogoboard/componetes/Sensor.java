package br.univali.portugol.plugin.gogoboard.componetes;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.gerenciadores.GerenciadorDriver;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;
import br.univali.portugol.plugin.gogoboard.util.UtilGoGoBoard;

/**
 * Classe que representa o Sensor.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
public class Sensor {

    private final GoGoDriver goGoDriver;
    private final int idSensor;
    private int valor;

    /**
     * Construtor padrão do motor servo.
     *
     * @param numSensor Número correspondente ao índice do motor, iniciando em
     * 0.
     * @param tipoDriver Enum referente ao tipo de driver necessário.
     */
    public Sensor(int numSensor, GoGoDriver.TIPODRIVER tipoDriver) {
        this.goGoDriver = GerenciadorDriver.getGoGoDriver(tipoDriver);
        this.idSensor = numSensor;
    }

    /**
     * Método para obter o ID do sensor.
     *
     * @return ID do sensor.
     */
    public int getIdSensor() {
        return idSensor;
    }

    /**
     * Método para obter o valor do sensor.
     *
     * @param atualizar Verdadeiro indica que o valor será consultado na placa
     * antes de ser retornado.
     * @return valor do sensor.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public int getValor(boolean atualizar) throws ErroExecucaoBiblioteca {
        if (atualizar) {
            valor = atualizaValor();
        }
        return valor;
    }

    /**
     * Método para setar o valor do sensor.
     *
     * @param valor Inteiro correspondente ao valor do sensor.
     */
    public void setValor(int valor) {
        this.valor = valor;
    }

    /**
     * Método para atualizar o valor do sensor com o valor atual.
     *
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    private int atualizaValor() throws ErroExecucaoBiblioteca {
        int[] mensagem;
        do {
            mensagem = goGoDriver.receberMensagem();
        } while (mensagem[0] != GoGoDriver.GOGOBOARD);       // Se não for uma mensagem da GoGo, tenta novamente
        int byteAlto = mensagem[1 + (idSensor * 2)];
        int byteBaixo = mensagem[1 + (idSensor * 2) + 1];
        return UtilGoGoBoard.bytesToInt(byteAlto, byteBaixo);
    }
}
