package br.univali.portugol.plugin.gogoboard.componetes;

/**
 * Classe que representa o leitor do infravermelho.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
public class Infravermelho {

    private int valorRecebido;

    /**
     * Método para obter o valor recebido no leitor do infravermelho.
     *
     * @return Valor recebido no leitor do infravermelho.
     */
    public int getValorRecebido() {
        return valorRecebido;
    }

    /**
     * Método para setar o valor recebido no leitor do infravermelho.
     */
    public void setValorRecebido(int valorRecebido) {
        this.valorRecebido = valorRecebido;
    }
}
