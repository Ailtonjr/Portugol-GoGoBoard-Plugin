package br.univali.portugol.plugin.gogoboard.componetes;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class Display {

    GoGoDriver goGoDriver = GoGoDriver.obterInstancia();

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

    public void limparTela() throws ErroExecucaoBiblioteca {
        byte[] cmd = new byte[GoGoDriver.TAMANHO_PACOTE];
        cmd[GoGoDriver.ID_COMANDO] = GoGoDriver.CMD_LIMPAR_TELA;

        goGoDriver.enviarComando(cmd);
    }
}
