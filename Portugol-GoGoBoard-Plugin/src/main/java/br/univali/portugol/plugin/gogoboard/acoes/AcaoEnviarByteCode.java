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
import br.univali.portugol.nucleo.mensagens.ErroAnalise;
import br.univali.portugol.nucleo.mensagens.ErroSemantico;
import br.univali.portugol.plugin.gogoboard.AnalisadorASA;
import br.univali.portugol.plugin.gogoboard.ConversorByteCode;
import br.univali.portugol.plugin.gogoboard.GoGoDriver;
import br.univali.portugol.plugin.gogoboard.telas.JanelaCodigoLogo;
import br.univali.ps.plugins.base.ErroExecucaoPlugin;
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
public class AcaoEnviarByteCode extends AbstractAction {

    private GoGoBoardPlugin plugin;

    public AcaoEnviarByteCode(GoGoBoardPlugin plugin) {
        super("Envia o programa para a GoGo Board", carregarIcone());
        this.plugin = plugin;
    }

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

    private void mostrarCodigoLogo(String codigoLogo) {
        JanelaCodigoLogo janelaCodigoLogo = new JanelaCodigoLogo();
        janelaCodigoLogo.setCodigoLogo(codigoLogo);
        janelaCodigoLogo.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ResultadoAnalise resultadoAnalise = new ResultadoAnalise();
        ConversorByteCode conversorByteCode = new ConversorByteCode();

        boolean contemErros;
        try {
            Programa programa = Portugol.compilarParaAnalise(plugin.getUtilizador().obterCodigoFonteUsuario());
            ASAPrograma asa = plugin.getUtilizador().obterASAProgramaAnalisado();
            AnalisadorASA analisadorASA = new AnalisadorASA(asa);
            ConversorLogo ConversorLogo = new ConversorLogo(asa);
            // Analisa a asa para verificar erros no código
            analisadorASA.analisar();
            // Converte o código Portugol para Logo Cricket
            String logo = ConversorLogo.converterPortugolParaLogo();
            // Converte o código Logo para o ByteCode que roda na GoGoBoard
            byte[] byteCode = conversorByteCode.converterLogoParaByteCode(logo);
            resultadoAnalise = programa.getResultadoAnalise();
            mostrarCodigoLogo(logo);
            // Envia o código logo para a GoGoBoard
            GoGoDriver.obterInstancia().enviarByteCode(byteCode);

            contemErros = false;
        } catch (ExcecaoVisitaASA ex) {
            System.err.println("Erro ao visitar a ASA no Plugin: ");

            if (ex.getCause() instanceof ErroExecucaoPlugin) {
                ErroExecucaoPlugin execucaoPlugin = (ErroExecucaoPlugin) ex.getCause();
                resultadoAnalise.adicionarErro((new ErroSemantico(execucaoPlugin.getTrechoCodigoFonte()) {
                    @Override
                    protected String construirMensagem() {
                        return ("[Erro GoGoBoard] - " + ex.getMessage());
                    }
                }));
            }
            contemErros = true;
        } catch (ErroExecucaoBiblioteca ex) {
            resultadoAnalise.adicionarErro((new ErroSemantico(new TrechoCodigoFonte(0,0,0)) {
                @Override
                protected String construirMensagem() {
                    return ("[Erro GoGoBoard] - " + ex.getMessage());
                }
            }));
            contemErros = true;
        } catch (ErroCompilacao ex) {
            resultadoAnalise = ex.getResultadoAnalise();
            for (ErroAnalise erro : ex.getResultadoAnalise().getErros()) {
                System.out.println(erro.getMensagem());
            }
            contemErros = true;
        }
        // Exibe todas as exceções, tanto do programa quanto do plugin
        if (contemErros) {
            plugin.getUtilizador().exibirErros(resultadoAnalise);
        }
    }
}
