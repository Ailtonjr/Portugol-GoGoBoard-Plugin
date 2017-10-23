package br.univali.portugol.plugin.gogoboard.componetes.modulos;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.componetes.Display;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;
import br.univali.portugol.plugin.gogoboard.gerenciadores.GerenciadorDriver;

/**
 * Classe que representa o módulo display LCD.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
public class DisplayLCD {

    GoGoDriver goGoDriver;

    /**
     * Construtor padrão do display de 7-segmentos.
     *
     * @param tipoDriver Enum referente ao tipo de driver necessário.
     */
    public DisplayLCD(GoGoDriver.TIPODRIVER tipoDriver) {
        goGoDriver = GerenciadorDriver.getGoGoDriver(tipoDriver);
    }

    /**
     * Método para exibir texto de até 60 caracteres no display LCD do módulo
     * externo da GoGo Board.
     *
     * @param texto Texto que será exibido.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public void exibirTexto(String texto) throws ErroExecucaoBiblioteca {
        if (texto.length() > 60) {
            throw new ErroExecucaoBiblioteca("Erro, o modulo display não pode exibir mais de 60 characteres.");
        }
        byte[] cmd = new byte[GoGoDriver.TAMANHO_PACOTE];
        cmd[GoGoDriver.ID_COMANDO] = GoGoDriver.CMD_EXIBIR_TEXTO_LONGO;
        // Copiar o conteudo do texto para enviar para a GoGo
        for (int i = 0; i < texto.length(); i++) {
            cmd[3 + i] = (byte) texto.charAt(i);
        }
        goGoDriver.enviarComando(cmd);
    }

    /**
     * Método para exibir numero no display LCD do módulo externo da GoGo Board.
     *
     * @param numero Número que será exibido.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public void exibirNumero(int numero) throws ErroExecucaoBiblioteca {
        exibirTexto(String.valueOf(numero));
    }

    /**
     * Método para limpar a tela do display LCD do módulo externo da GoGo Board.
     *
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public void limparTela() throws ErroExecucaoBiblioteca {
        byte[] cmd = new byte[GoGoDriver.TAMANHO_PACOTE];
        cmd[GoGoDriver.ID_COMANDO] = GoGoDriver.CMD_LIMPAR_TELA;

        goGoDriver.enviarComando(cmd);
    }
}
