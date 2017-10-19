package br.univali.portugol.plugin.gogoboard.modelo;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.GoGoDriver;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class AtualizadorComponentes {

    private List<Sensor> sensores;
    private Infravermelho infravermelho;

    public AtualizadorComponentes(List<Sensor> sensores, Infravermelho infraVermelho) {
        this.sensores = sensores;
        this.infravermelho = infraVermelho;
    }

    public void atualizar() throws ErroExecucaoBiblioteca {
        byte[] mensagem = GoGoDriver.obterInstancia().receberMensagem(64);

        if (mensagem[0] == GoGoDriver.GOGOBOARD) {
            // Atualizar valores dos sensores
            for (int i = 0; i < 8; i++) {
                ByteBuffer bb = ByteBuffer.wrap(mensagem, (2 * (i)) + 1, 2);
                bb.order(ByteOrder.BIG_ENDIAN);
                sensores.get(i).setValor(bb.getShort());
            }
            // Atualizar Infravermelho
            //int value = Character.getNumericValue(mensagem[GoGoDriver.INDICE_VALOR_IR]);
            infravermelho.setValorRecebido(mensagem[GoGoDriver.INDICE_VALOR_IR]);
        }
    }

}
