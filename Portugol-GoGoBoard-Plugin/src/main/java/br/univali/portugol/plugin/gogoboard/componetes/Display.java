package br.univali.portugol.plugin.gogoboard.componetes;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.gerenciadores.GerenciadorDriver;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;

/**
 * Classe que representa o display de 7-segmentos.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
public class Display {

    GoGoDriver goGoDriver;

    /**
     * Construtor padrão do display de 7-segmentos.
     *
     * @param tipoDriver Enum referente ao tipo de driver necessário.
     */
    public Display(GoGoDriver.TipoDriver tipoDriver) {
        goGoDriver = GerenciadorDriver.getGoGoDriver(tipoDriver);
    }

    /**
     * Método para exibir texto de até 4 caracteres no display interno da GoGo
     * Board.
     *
     * @param texto Texto que será exibido.
     * @throws br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public void exibirTextoCurto(String texto) throws ErroExecucaoBiblioteca {
        if (texto.length() > 4) {
            throw new ErroExecucaoBiblioteca("Erro, o display de segmentos não pode exibir mais de 4 characteres.");
        }
        byte[] cmd = new byte[GoGoDriver.TAMANHO_PACOTE];
        cmd[GoGoDriver.ID_COMANDO] = GoGoDriver.CMD_EXIBIR_TEXTO_CURTO;
        // Copiar o conteudo do texto para enviar para a GoGo
        byte[] bytes = texto.getBytes();
        for (int i = 0; i < texto.length(); i++) {
            cmd[3 + i] = bytes[i];
        }
        goGoDriver.enviarComando(cmd);
    }

    /**
     * Método para exibir texto de até 60 caracteres no display do módulo
     * externo da GoGo Board.
     *
     * @param texto Texto que será exibido.
     * @throws br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public void exibirTextoLongo(String texto) throws ErroExecucaoBiblioteca {
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
     * Método para limpar a tela do display do módulo externo da GoGo Board.
     *
     * @throws br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public void limparTela() throws ErroExecucaoBiblioteca {
        byte[] cmd = new byte[GoGoDriver.TAMANHO_PACOTE];
        cmd[GoGoDriver.ID_COMANDO] = GoGoDriver.CMD_LIMPAR_TELA;

        goGoDriver.enviarComando(cmd);
    }
}
