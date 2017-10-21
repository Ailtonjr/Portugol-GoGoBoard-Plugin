package br.univali.portugol.plugin.gogoboard.componetes;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.driver.GerenciadorDeDriver;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;
import br.univali.portugol.plugin.gogoboard.util.UtilGoGoBoard;
import java.util.List;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class AtualizadorComponentes {

    private List<Sensor> sensores;
    private Infravermelho infravermelho;
    private GoGoDriver goGoDriver;

    public AtualizadorComponentes(List<Sensor> sensores, Infravermelho infraVermelho, UtilGoGoBoard.TipoDriver tipoDriver) {
        this.sensores = sensores;
        this.infravermelho = infraVermelho;
        this.goGoDriver = GerenciadorDeDriver.getGoGoDriver(tipoDriver);
    }

    public void atualizar() throws ErroExecucaoBiblioteca {
        int[] mensagem = goGoDriver.receberMensagem();

        if (mensagem[0] == GoGoDriver.GOGOBOARD) {
            // Atualizar valores dos sensores
            for (int i = 0; i < 8; i++) {
                int valor = UtilGoGoBoard.bytesToInt(mensagem[1 + (i * 2)], mensagem[1 + (i * 2) + 1]);
                sensores.get(i).setValor(valor);
            }
            // Atualizar Infravermelho
            infravermelho.setValorRecebido(mensagem[GoGoDriver.INDICE_VALOR_IR]);
        }
    }

}
