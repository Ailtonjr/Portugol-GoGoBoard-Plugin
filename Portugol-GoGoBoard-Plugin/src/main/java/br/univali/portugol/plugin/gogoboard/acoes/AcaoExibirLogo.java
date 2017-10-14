package br.univali.portugol.plugin.gogoboard.acoes;

import br.univali.portugol.plugin.gogoboard.GoGoBoardPlugin;
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
public class AcaoExibirLogo extends AbstractAction{

    public AcaoExibirLogo(GoGoBoardPlugin plugin) {
        super("Exibe o código logo gerado para a GoGo Board", carregarIcone());
    }

    private static Icon carregarIcone() {
        try {
            String caminho = "br/univali/portugol/plugin/gogoboard/imagens/code.png";
            Image imagem = ImageIO.read(AcaoEnviarByteCode.class.getClassLoader().getResourceAsStream(caminho));

            return new ImageIcon(imagem);
        } catch (IOException ex) {
            System.err.println("Erro ao carregar o icone do plugin na ação exibir código Logo");
            return null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
