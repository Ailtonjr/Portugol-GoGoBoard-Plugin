package br.univali.portugol.plugin.gogoboard.modelo;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.GoGoDriver;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class Led {

    private GoGoDriver gogoDriver;
    private boolean ligado = false;

    public Led() throws ErroExecucaoBiblioteca {
        this.gogoDriver = GoGoDriver.obterInstancia();
    }

    public boolean isLigado() {
        return ligado;
    }

    public void ligar(boolean ligar) throws ErroExecucaoBiblioteca {
        byte[] cmd = new byte[gogoDriver.TAMANHO_PACOTE];
        cmd[gogoDriver.ID_COMANDO] = gogoDriver.CMD_CONTROLE_LED;
        if (ligar) {
            cmd[gogoDriver.PARAMETRO2] = 1;
        } else {
            cmd[gogoDriver.PARAMETRO2] = 0;
        }
        gogoDriver.enviarComando(cmd);
    }
}
