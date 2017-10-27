package br.univali.portugol.plugin.gogoboard.componetes;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;
import br.univali.portugol.plugin.gogoboard.gerenciadores.GerenciadorDriver;
import br.univali.portugol.plugin.gogoboard.util.UtilGoGoBoard;

/**
 * Classe que representa o leitor do infravermelho.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
public class Infravermelho {

    private GoGoDriver goGoDriver;
    private int valor;

    /**
     * Construtor padrão do motor servo.
     *
     * @param tipoDriver Enum referente ao tipo de driver necessário.
     */
    public Infravermelho(GoGoDriver.TIPODRIVER tipoDriver) {
        this.goGoDriver = GerenciadorDriver.getGoGoDriver(tipoDriver);
    }

    /**
     * Método para obter o valor recebido no leitor do infravermelho.
     *
     * @param atualizar Verdadeiro indica que o valor será consultado na placa
     * antes de ser retornado.
     * @return Valor recebido no leitor do infravermelho.
     */
    public int getValor(boolean atualizar) throws ErroExecucaoBiblioteca {
        if(atualizar){
            valor = atualizaValor();
        }
        return valor;
    }

    /**
     * Método para setar o valor recebido no leitor do infravermelho.
     *
     * @param valor Valor inteiro que será setado na variável.
     */
    public void setValor(int valor) {
        this.valor = valor;
    }

    /**
     * Método para atualizar o valor do sensor com o valor atual.
     *
     * @return valor recebido pelo leitor infravermelho
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public int atualizaValor() throws ErroExecucaoBiblioteca {
        int[] mensagem;
        do {
            mensagem = goGoDriver.receberMensagem();
        } while (mensagem[0] != GoGoDriver.GOGOBOARD);       // Se não for uma mensagem da GoGo, tenta novamente
        return mensagem[GoGoDriver.INDICE_VALOR_IR];
    }
}
