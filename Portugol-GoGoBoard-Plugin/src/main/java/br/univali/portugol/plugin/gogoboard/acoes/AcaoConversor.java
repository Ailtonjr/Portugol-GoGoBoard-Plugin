package br.univali.portugol.plugin.gogoboard.acoes;

import br.univali.portugol.plugin.gogoboard.ConversorLogo;
import br.univali.portugol.plugin.gogoboard.GoGoBoardPlugin;
import br.univali.portugol.nucleo.asa.ASAPrograma;
import br.univali.portugol.nucleo.asa.ExcecaoVisitaASA;
import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.JanelaCodigoLogo;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author Ailton Jr
 */
public class AcaoConversor extends AbstractAction {

    private GoGoBoardPlugin plugin;

    public AcaoConversor(GoGoBoardPlugin plugin) {
        super("Ação para converter de código Portugol em Logo", carregarIcone());
        this.plugin = plugin;
    }

    private static Icon carregarIcone() {
        try {
            String caminho = "br/univali/portugol/plugin/gogoboard/imagens/monitor.png";
            Image imagem = ImageIO.read(AcaoConversor.class.getClassLoader().getResourceAsStream(caminho));

            return new ImageIcon(imagem);
        } catch (IOException ex) {
            System.err.println("Erro ao carregar o icone do plugin na ação Compilar Logo");
            return null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            ASAPrograma asa = plugin.getUtilizador().obterASAProgramaAnalisado();
            ConversorLogo ConversorLogo = new ConversorLogo(asa);
            JanelaCodigoLogo janelaCdigoFonte = new JanelaCodigoLogo();
            final String codigoLogo = ConversorLogo.converterCodigo();
            janelaCdigoFonte.setCodigoFonte(codigoLogo);
            janelaCdigoFonte.setVisible(true);
            System.out.println(codigoLogo);
        } catch (ExcecaoVisitaASA ex) {
            System.err.println("ERRO NO PLUGIN: ");
            ex.printStackTrace(System.err);
        }
    }
}
