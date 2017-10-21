package br.univali.portugol.plugin.gogoboard.componetes;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.driver.GerenciadorDeDriver;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;
import br.univali.portugol.plugin.gogoboard.util.UtilGoGoBoard;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class Buzzer {

    GoGoDriver goGoDriver;

    public Buzzer(UtilGoGoBoard.TipoDriver tipoDriver) {
        this.goGoDriver = GerenciadorDeDriver.getGoGoDriver(tipoDriver);
    }

    public void acionarBeep() throws ErroExecucaoBiblioteca {
        byte[] comando = new byte[GoGoDriver.TAMANHO_PACOTE];
        comando[GoGoDriver.ID_COMANDO] = GoGoDriver.CMD_BEEP;
        goGoDriver.enviarComando(comando);
    }
}
