package br.univali.portugol.plugin.gogoboard.acoes;

import br.univali.portugol.nucleo.ErroCompilacao;
import br.univali.portugol.nucleo.Portugol;
import br.univali.portugol.nucleo.Programa;
import br.univali.portugol.nucleo.analise.ResultadoAnalise;
import br.univali.portugol.plugin.gogoboard.ConversorLogo;
import br.univali.portugol.plugin.gogoboard.GoGoBoardPlugin;
import br.univali.portugol.nucleo.asa.ASAPrograma;
import br.univali.portugol.nucleo.asa.ExcecaoVisitaASA;
import br.univali.portugol.nucleo.asa.TrechoCodigoFonte;
import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.nucleo.bibliotecas.base.GerenciadorBibliotecas;
import br.univali.portugol.nucleo.mensagens.ErroAnalise;
import br.univali.portugol.nucleo.mensagens.ErroSemantico;
import br.univali.portugol.plugin.gogoboard.JanelaCodigoLogo;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
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
        ResultadoAnalise resultadoAnalise = new ResultadoAnalise();
        boolean contemErros = false;
        try {
            final Programa programa = Portugol.compilarParaAnalise(plugin.getUtilizador().obterCodigoFonteUsuario());
            ASAPrograma asa = plugin.getUtilizador().obterASAProgramaAnalisado();
            ConversorLogo ConversorLogo = new ConversorLogo(asa);
            JanelaCodigoLogo janelaCdigoFonte = new JanelaCodigoLogo();
            final String codigoLogo = ConversorLogo.converterCodigo();
            janelaCdigoFonte.setCodigoFonte(codigoLogo);
            janelaCdigoFonte.setVisible(true);
            System.out.println(codigoLogo);

            resultadoAnalise = programa.getResultadoAnalise();
            contemErros = false;
        } catch (ExcecaoVisitaASA ex) {
            System.err.println("Erro ao visitar a ASA no Plugin: ");
            ex.printStackTrace(System.err);
        } catch (ErroCompilacao ex) {
            resultadoAnalise = ex.getResultadoAnalise();
            for (ErroAnalise erro : ex.getResultadoAnalise().getErros()) {
                System.out.println(erro.getMensagem());
            }
            contemErros = true;
        }
        // Exemplo de erro na aba de mensagens
        if (contemErros) {
            resultadoAnalise.adicionarErro(new ErroSemantico(new TrechoCodigoFonte(5, 2, 5)) {
                @Override
                protected String construirMensagem() {
                    return "teste";
                }
            });
            plugin.getUtilizador().exibirErros(resultadoAnalise);
        }
    }
}
