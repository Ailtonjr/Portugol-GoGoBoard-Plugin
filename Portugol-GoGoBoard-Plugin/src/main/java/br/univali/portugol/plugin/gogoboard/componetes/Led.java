package br.univali.portugol.plugin.gogoboard.componetes;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.gerenciadores.GerenciadorDriver;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;

/**
 * Classe que representa o led.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
public class Led {

    private GoGoDriver goGoDriver;
    private boolean ligado;
    private int idLed;

    /**
     * Construtor padrão do led.
     *
     * @param idLed Inteiro correspondente ao ID do led. 0 = Led do usuário.
     * @param tipoDriver Enum referente ao tipo de driver necessário.
     */
    public Led(int idLed, GoGoDriver.TipoDriver tipoDriver) {
        goGoDriver = GerenciadorDriver.getGoGoDriver(tipoDriver);
        this.idLed = idLed;
    }

    /**
     * Método para obter o status do led.
     *
     * @return Status do led.
     */
    public boolean isLigado() {
        return ligado;
    }

    /**
     * Método para obter o ID do led.
     *
     * @return ID do led.
     */
    public int getIdLed() {
        return idLed;
    }

    /**
     * Método para controlar o status do led.
     *
     * @param acao Inteiro correspondente à ação do led. 0 = Desligado e 1 =
     * Ligado
     * @throws br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public void controlarLed(int acao) throws ErroExecucaoBiblioteca {
        byte[] cmd = new byte[GoGoDriver.TAMANHO_PACOTE];
        cmd[GoGoDriver.ID_COMANDO] = GoGoDriver.CMD_CONTROLE_LED;
        cmd[GoGoDriver.PARAMETRO1] = (byte) idLed;
        cmd[GoGoDriver.PARAMETRO2] = (byte) acao;

        goGoDriver.enviarComando(cmd);
        ligado = true;
    }
}
