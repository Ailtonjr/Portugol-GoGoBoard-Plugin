package br.univali.portugol.plugin.gogoboard.modelo;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.GoGoDriver;
import br.univali.ps.plugins.base.ErroExecucaoPlugin;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class Led {

    private GoGoDriver gogoDriver;
    private boolean ligado;
    private int idLed;

    public Led(int idLed){
        this.idLed = idLed;
        this.gogoDriver = GoGoDriver.obterInstancia();
    }

    public boolean isLigado() {
        return ligado;
    }

    public int getIdLed() {
        return idLed;
    }

    public void controlarLed(int acao) throws ErroExecucaoBiblioteca {
        byte[] cmd = new byte[gogoDriver.TAMANHO_PACOTE];
        cmd[gogoDriver.ID_COMANDO] = gogoDriver.CMD_CONTROLE_LED;
        cmd[gogoDriver.PARAMETRO1] = (byte) idLed;  // 0 = para led do usuário
        cmd[gogoDriver.PARAMETRO2] = (byte) acao;   // 0 = desligado, 1 = ligado

        gogoDriver.enviarComando(cmd);
    }
}
