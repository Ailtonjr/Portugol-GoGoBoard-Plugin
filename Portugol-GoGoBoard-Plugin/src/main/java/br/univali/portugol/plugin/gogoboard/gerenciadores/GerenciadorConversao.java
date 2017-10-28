package br.univali.portugol.plugin.gogoboard.gerenciadores;

import br.univali.portugol.nucleo.ErroCompilacao;
import br.univali.portugol.nucleo.Portugol;
import br.univali.portugol.nucleo.Programa;
import br.univali.portugol.nucleo.analise.ResultadoAnalise;
import br.univali.portugol.nucleo.asa.ASAPrograma;
import br.univali.portugol.nucleo.asa.ExcecaoVisitaASA;
import br.univali.portugol.nucleo.asa.TrechoCodigoFonte;
import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.nucleo.mensagens.ErroSemantico;
import br.univali.portugol.plugin.gogoboard.GoGoBoardPlugin;
import br.univali.portugol.plugin.gogoboard.conversor.AnalisadorASA;
import br.univali.portugol.plugin.gogoboard.conversor.ConversorByteCode;
import br.univali.portugol.plugin.gogoboard.conversor.ConversorLogo;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;
import br.univali.ps.plugins.base.ErroExecucaoPlugin;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe que gerencia as conversões de código do plugin.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
public class GerenciadorConversao {

    private final GoGoBoardPlugin plugin;
    ResultadoAnalise resultadoAnalise;

    public GerenciadorConversao(GoGoBoardPlugin plugin) {
        this.plugin = plugin;
        this.resultadoAnalise = new ResultadoAnalise();
    }

    /**
     * Método para análisar, converter e enviar o bytecode para a GoGo Board.
     */
    public void enviarBytecodeParaGoGo() {
        String logo = converterPortugolParaLogo();
        byte[] bytecode = converterLogoParaByteCode(logo);
        try {
            GerenciadorDriver.getGoGoDriver(GoGoDriver.TIPODRIVER.COMPARTILHADO).enviarByteCode(bytecode);
        } catch (ErroExecucaoBiblioteca ex) {
            Logger.getLogger(GerenciadorConversao.class.getName()).log(Level.SEVERE, null, ex);
            resultadoAnalise.adicionarErro((new ErroSemantico(new TrechoCodigoFonte(0, 0, 0)) {
                @Override
                protected String construirMensagem() {
                    return ("[Erro GoGoBoard] - " + ex.getMessage());
                }
            }));
            exibirErros();
        }
    }

    /**
     * Método para analisar e converter o código do usuário escrito em Portugol
     * para Logo Cricket.
     *
     * @return Código em Logo Cricket se a conversão for finalizada com sucesso
     * ou Null se tiver algum erro na conversão.
     */
    public String converterPortugolParaLogo() {
        ASAPrograma asa = plugin.getUtilizador().obterASAProgramaAnalisado();
        AnalisadorASA analisadorASA = new AnalisadorASA(asa);
        ConversorLogo ConversorLogo = new ConversorLogo(asa);
        String logo = null;
        try {
            // Analisa a asa para verificar erros no código Portugol.
            analisadorASA.analisar();
            logo = ConversorLogo.converterPortugolParaLogo();
        } catch (ExcecaoVisitaASA ex) {
            System.err.println("Erro ao visitar a ASA no Plugin: ");
            Logger.getLogger(GerenciadorConversao.class.getName()).log(Level.SEVERE, null, ex);

            if (ex.getCause() instanceof ErroExecucaoPlugin) {
                ErroExecucaoPlugin execucaoPlugin = (ErroExecucaoPlugin) ex.getCause();
                resultadoAnalise.adicionarErro((new ErroSemantico(execucaoPlugin.getTrechoCodigoFonte()) {
                    @Override
                    protected String construirMensagem() {
                        return ("[Erro GoGoBoard] - " + ex.getMessage());
                    }
                }));
            }
            exibirErros();
        }
        return logo;
    }

    /**
     * Método privado para compilar o código em Logo Cricket para o bytecode que
     * pode ser executado na GoGo Board.
     *
     * @return Bytecode que pode ser executado na GoGo Board.
     */
    private byte[] converterLogoParaByteCode(String logo) {
        ConversorByteCode conversorByteCode = new ConversorByteCode();
        return conversorByteCode.converterLogoParaByteCode(logo);
    }

    /**
     * Método privado para exibir todas as exceções, tanto as do programa em
     * portugol, quanto as dos conversorores
     */
    private void exibirErros() {
        Programa programa;
        try {
            programa = Portugol.compilarParaAnalise(plugin.getUtilizador().obterCodigoFonteUsuario());
            ResultadoAnalise resultAnalisePrograma = programa.getResultadoAnalise();
            resultAnalisePrograma.getErros().forEach((erro) -> {
                resultadoAnalise.adicionarErro(erro);
            });
        } catch (ErroCompilacao | NullPointerException erro) {
            Logger.getLogger(GerenciadorConversao.class.getName()).log(Level.SEVERE, null, erro);
        }
        if (resultadoAnalise.contemErros()) {
            plugin.getUtilizador().exibirErros(resultadoAnalise);
        }
    }
}
