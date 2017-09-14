package br.univali.portugol.plugin.gogoboard.acoes;

import br.univali.portugol.plugin.gogoboard.ExemploBuscadorDeSimbolos;
import br.univali.portugol.plugin.gogoboard.GoGoBoardPlugin;
import br.univali.portugol.nucleo.asa.ASAPrograma;
import br.univali.portugol.nucleo.asa.ExcecaoVisitaASA;
import br.univali.portugol.nucleo.asa.NoDeclaracao;
import com.sun.webkit.plugin.Plugin;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        //JOptionPane.showMessageDialog(null, "O plugin executou uma ação para converter de código Portugol em Logo!!", "Plugin GoGoBoard", JOptionPane.INFORMATION_MESSAGE);
        try {

            //String codigoFonte = plugin.

            ASAPrograma asa = plugin.getUtilizador().obterASAProgramaAnalisado();
            ExemploBuscadorDeSimbolos buscadorDeSimbolos = new ExemploBuscadorDeSimbolos("ma", asa);
            List<NoDeclaracao> declaracoes;
            declaracoes = buscadorDeSimbolos.buscar();
            
            StringBuilder sb = new StringBuilder("Símbolos encontrados:\n\n ");

            for (NoDeclaracao no : declaracoes) {
                sb.append(no.getNome() + "[" + no.getTrechoCodigoFonte().getLinha() + "," + no.getTrechoCodigoFonte().getColuna() + "]");
                sb.append("\n");
            }
            System.out.println(sb.toString());
            //janelaCodigoLogo.setCodigoFonte(sb.toString());
            //janelaCodigoLogo.setLocationRelativeTo(null);
            //janelaCodigoLogo.setVisible(true);
        } catch (ExcecaoVisitaASA ex) {
            //Logger.getLogger(AcaoConversor.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("ERRO NO PLUGIN: ");
            ex.printStackTrace(System.err);
        }
    }
}
