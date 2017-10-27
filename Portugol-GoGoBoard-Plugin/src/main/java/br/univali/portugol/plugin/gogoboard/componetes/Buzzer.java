package br.univali.portugol.plugin.gogoboard.componetes;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.gerenciadores.GerenciadorDriver;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;

/**
 * Classe que representa o buzzer.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
public class Buzzer {

    GoGoDriver goGoDriver;

    /**
     * Construtor padrão do buzzer.
     *
     * @param tipoDriver Enum referente ao tipo de driver necessário.
     */
    public Buzzer(GoGoDriver.TIPODRIVER tipoDriver) {
        this.goGoDriver = GerenciadorDriver.getGoGoDriver(tipoDriver);
    }

    /**
     * Método para acionar o buzzer.
     *
     * @throws br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public void acionar() throws ErroExecucaoBiblioteca {
        controlarBuzzer();
    }

    /**
     * Método privado para controlar a força do motor.
     *
     * @throws br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public void controlarBuzzer() throws ErroExecucaoBiblioteca {
        byte[] comando = new byte[GoGoDriver.TAMANHO_PACOTE];
        comando[GoGoDriver.ID_COMANDO] = GoGoDriver.CMD_BEEP;
        goGoDriver.enviarComando(comando);
    }
}
