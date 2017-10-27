package br.univali.portugol.plugin.gogoboard.acoes;

import br.univali.portugol.plugin.gogoboard.GoGoBoardPlugin;
import br.univali.portugol.plugin.gogoboard.gerenciadores.GerenciadorConversao;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Classe inicial do botão da ação enviar bytecode para a GoGo Board.
 *
 * @author Ailton Jr
 * @version 1.0
 */
public class AcaoEnviarByteCode extends AbstractAction {

    private GoGoBoardPlugin plugin;

    /**
     * Construtor da ação enviar byte code.
     *
     * @param plugin Instancia de plugin.
     */
    public AcaoEnviarByteCode(GoGoBoardPlugin plugin) {
        super("Envia o programa para a GoGo Board", carregarIcone());
        this.plugin = plugin;
    }

    /**
     * Método para carregar o icone da ação.
     *
     * @return ImageIcon
     */
    private static Icon carregarIcone() {
        try {
            String caminho = "br/univali/portugol/plugin/gogoboard/imagens/submit.png";
            Image imagem = ImageIO.read(AcaoEnviarByteCode.class.getClassLoader().getResourceAsStream(caminho));

            return new ImageIcon(imagem);
        } catch (IOException ex) {
            System.err.println("Erro ao carregar o icone do plugin na ação Compilar Logo");
            return null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GerenciadorConversao gerenciadorConversao = new GerenciadorConversao(plugin);
        gerenciadorConversao.enviarBytecodeParaGoGo();
    }
}
