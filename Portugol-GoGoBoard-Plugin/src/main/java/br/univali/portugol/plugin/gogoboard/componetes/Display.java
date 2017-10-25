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
    public Display(GoGoDriver.TIPODRIVER tipoDriver) {
        goGoDriver = GerenciadorDriver.getGoGoDriver(tipoDriver);
    }

    /**
     * Método para exibir texto de até 4 caracteres no display interno da GoGo
     * Board.
     *
     * @param palavra Texto que será exibido.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public void exibirPalavra(String palavra) throws ErroExecucaoBiblioteca {
        if (palavra.length() > 4) {
            throw new ErroExecucaoBiblioteca("Erro, o display de segmentos não pode exibir mais de 4 characteres.");
        }
        byte[] cmd = new byte[GoGoDriver.TAMANHO_PACOTE];
        cmd[GoGoDriver.ID_COMANDO] = GoGoDriver.CMD_EXIBIR_TEXTO_CURTO;
        // Copiar o conteudo do texto para enviar para a GoGo
        byte[] bytes = palavra.getBytes();
        for (int i = 0; i < palavra.length(); i++) {
            cmd[3 + i] = bytes[i];
        }
        goGoDriver.enviarComando(cmd);
    }

    /**
     * Método para exibir numero de até 4 caracteres no display interno da GoGo
     * Board.
     *
     * @param numero Número que será exibido.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    void exibirNumero(int numero) throws ErroExecucaoBiblioteca {
        exibirPalavra(String.valueOf(numero));
    }
}
