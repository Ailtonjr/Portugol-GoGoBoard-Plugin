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
        //JOptionPane.showMessageDialog(null, "O plugin executou uma ação para converter de código Portugol em Logo!!", "Plugin GoGoBoard", JOptionPane.INFORMATION_MESSAGE);

        // Exemplo de como buscar variaveis declaradas pelo nome
        /*try {

            ASAPrograma asa = plugin.getUtilizador().obterASAProgramaAnalisado();
            ExemploBuscadorDeSimbolos buscadorDeSimbolos = new ExemploBuscadorDeSimbolos("ma", asa);
            List<NoDeclaracao> declaracoes = buscadorDeSimbolos.buscar();
            
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
        }*/
        try {
            ASAPrograma asa = plugin.getUtilizador().obterASAProgramaAnalisado();
            ConversorLogo ConversorLogo = new ConversorLogo(asa);
            JanelaCodigoLogo janelaCdigoFonte = new JanelaCodigoLogo();
            janelaCdigoFonte.setCodigoFonte(ConversorLogo.converterCodigo());
            janelaCdigoFonte.setVisible(true);
            //System.out.println(ConversorLogo.converterCodigo());
        } catch (ExcecaoVisitaASA ex) {
            System.err.println("ERRO NO PLUGIN: ");
            ex.printStackTrace(System.err);
        }
    }
}
