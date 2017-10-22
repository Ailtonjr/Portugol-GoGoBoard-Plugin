package br.univali.portugol.plugin.gogoboard.acoes;

import br.univali.portugol.plugin.gogoboard.GoGoBoardPlugin;
import br.univali.portugol.plugin.gogoboard.gerenciadores.GerenciadorMonitor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Classe inicial do botão da ação exibir monitor de recursos.
 *
 * @author Ailton Cardoso Jr
 * @version  1.0
 */
public class AcaoExibirMonitor extends AbstractAction {

    GerenciadorMonitor controladorMonitor;

    /**
     * Construtor da ação exibir código Logo.
     * @param plugin Instancia de plugin.
     */
    public AcaoExibirMonitor(GoGoBoardPlugin plugin) {
        super("Exibe o monitor de recursos da GoGo Board", carregarIcone());
    }

    /**
     * Método para carregar o icone da ação.
     *
     * @return ImageIcon
     */
    private static Icon carregarIcone() {
        try {
            String caminho = "br/univali/portugol/plugin/gogoboard/imagens/monitor.png";
            Image imagem = ImageIO.read(AcaoEnviarByteCode.class.getClassLoader().getResourceAsStream(caminho));

            return new ImageIcon(imagem);
        } catch (IOException ex) {
            System.err.println("Erro ao carregar o icone do plugin na ação Compilar Logo");
            return null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controladorMonitor = new GerenciadorMonitor();
        controladorMonitor.exibirMonitor();
    }
}
