package br.univali.portugol.plugin.gogoboard.componetes;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.driver.GerenciadorDeDriver;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;
import br.univali.portugol.plugin.gogoboard.util.UtilGoGoBoard;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class Led {

    private GoGoDriver goGoDriver;
    private boolean ligado;
    private int idLed;

    public Led(int idLed, UtilGoGoBoard.TipoDriver tipoDriver){
        goGoDriver = GerenciadorDeDriver.getGoGoDriver(tipoDriver);
        this.idLed = idLed;
    }

    public boolean isLigado() {
        return ligado;
    }

    public int getIdLed() {
        return idLed;
    }
    
public void controlarLed(int acao) throws ErroExecucaoBiblioteca {
        byte[] cmd = new byte[GoGoDriver.TAMANHO_PACOTE];
        cmd[GoGoDriver.ID_COMANDO] = GoGoDriver.CMD_CONTROLE_LED;
        cmd[GoGoDriver.PARAMETRO1] = (byte) idLed;  // 0 = para led do usuário
        cmd[GoGoDriver.PARAMETRO2] = (byte) acao;   // 0 = desligado, 1 = ligado

        goGoDriver.enviarComando(cmd);
        ligado = true;
    }
}
