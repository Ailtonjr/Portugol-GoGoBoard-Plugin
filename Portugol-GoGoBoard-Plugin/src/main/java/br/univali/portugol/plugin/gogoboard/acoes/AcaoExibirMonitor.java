package br.univali.portugol.plugin.gogoboard.acoes;

import br.univali.portugol.plugin.gogoboard.GoGoBoardPlugin;
import br.univali.portugol.plugin.gogoboard.ui.controlador.ControladorMonitor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class AcaoExibirMonitor extends AbstractAction {
    ControladorMonitor controladorMonitor;
    public AcaoExibirMonitor(GoGoBoardPlugin plugin) {
        super("Exibe o monitor de recursos da GoGo Board", carregarIcone());
        controladorMonitor = new ControladorMonitor();
    }

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
        controladorMonitor.exibirMonitor();
    }
}
