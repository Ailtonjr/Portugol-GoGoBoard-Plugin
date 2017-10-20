package br.univali.portugol.plugin.gogoboard.modelo;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.GoGoDriver;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class Led {

    private boolean ligado;
    private int idLed;

    public Led(int idLed){
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

        GoGoDriver.obterInstancia().enviarComando(cmd);
        ligado = true;
    }
}
