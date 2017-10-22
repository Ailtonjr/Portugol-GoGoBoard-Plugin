package br.univali.portugol.plugin.gogoboard.conversor;

import br.univali.portugol.nucleo.asa.ASAPrograma;
import br.univali.portugol.nucleo.asa.ExcecaoVisitaASA;
import br.univali.portugol.nucleo.asa.NoBitwiseNao;
import br.univali.portugol.nucleo.asa.NoBloco;
import br.univali.portugol.nucleo.asa.NoCaso;
import br.univali.portugol.nucleo.asa.NoChamadaFuncao;
import br.univali.portugol.nucleo.asa.NoContinue;
import br.univali.portugol.nucleo.asa.NoDeclaracao;
import br.univali.portugol.nucleo.asa.NoDeclaracaoVariavel;
import br.univali.portugol.nucleo.asa.NoEnquanto;
import br.univali.portugol.nucleo.asa.NoEscolha;
import br.univali.portugol.nucleo.asa.NoExpressao;
import br.univali.portugol.nucleo.asa.NoFacaEnquanto;
import br.univali.portugol.nucleo.asa.NoInclusaoBiblioteca;
import br.univali.portugol.nucleo.asa.NoLogico;
import br.univali.portugol.nucleo.asa.NoOperacaoBitwiseE;
import br.univali.portugol.nucleo.asa.NoOperacaoBitwiseLeftShift;
import br.univali.portugol.nucleo.asa.NoOperacaoBitwiseOu;
import br.univali.portugol.nucleo.asa.NoOperacaoBitwiseRightShift;
import br.univali.portugol.nucleo.asa.NoOperacaoLogicaDiferenca;
import br.univali.portugol.nucleo.asa.NoPara;
import br.univali.portugol.nucleo.asa.NoReal;
import br.univali.portugol.nucleo.asa.VisitanteASA;
import br.univali.portugol.nucleo.asa.VisitanteNulo;
import br.univali.ps.plugins.base.ErroExecucaoPlugin;
import java.util.List;

/**
 * Classe responsável por percorrer e analisar a ASA, gerando exceções quando o
 * código não é possível converter para Logo.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
public class AnalisadorASA extends VisitanteNulo {

    private final ASAPrograma asa;

    /**
     * Construtor padrão do analisador asa.
     *
     * @param asa ASAPrograma que será visitada.
     * @see VisitanteNulo
     * @see VisitanteASA
     */
    public AnalisadorASA(ASAPrograma asa) {
        this.asa = asa;
    }

    /**
     * Método para iniciar a análise.
     *
     * @throws br.univali.portugol.nucleo.asa.ExcecaoVisitaASA
     */
    public void analisar() throws ExcecaoVisitaASA {
        System.out.println("------------- Visita ASA Analizador -------------");
        asa.aceitar(this);
    }

    @Override
    public Object visitar(ASAPrograma asap) throws ExcecaoVisitaASA {

        // Vesitar bibliotecas incluidas
        for (NoInclusaoBiblioteca biblioteca : asap.getListaInclusoesBibliotecas()) {
            biblioteca.aceitar(this);
        }

        // Pegar somente as váriaveis globais
        for (NoDeclaracao declaracao : asap.getListaDeclaracoesGlobais()) {
            if (declaracao instanceof NoDeclaracaoVariavel) {
                declaracao.aceitar(this);
            }
        }

        // Pegar o restante das declarações
        for (NoDeclaracao declaracao : asap.getListaDeclaracoesGlobais()) {
            if (!(declaracao instanceof NoDeclaracaoVariavel)) {
                declaracao.aceitar(this);
            }
        }
        return null;
    }

    @Override
    public Object visitar(NoInclusaoBiblioteca no) throws ExcecaoVisitaASA {
        System.out.println("NoInclusaoBiblioteca");
        if (!no.getNome().equalsIgnoreCase("GoGoBoard")) {
            throw new ExcecaoVisitaASA(new ErroExecucaoPlugin(String.format("A biblioteca " + no.getNome() + " não pode ser utilizada ao enviar código para GoGoBoard!"), no.getTrechoCodigoFonte()), asa, no);
        }
        return null;
    }

    @Override
    public Object visitar(NoDeclaracaoVariavel no) throws ExcecaoVisitaASA {
        System.out.println("NoDeclaracaoVariavel");
        switch (no.getTipoDado().getNome()) {
            case "logico":
                throw new ExcecaoVisitaASA(new ErroExecucaoPlugin(String.format("Variáveis do tipo Logico não são suportadas pela GoGo Board, exceto o valor \"verdadeiro\" quando utilizado como condição para laços de repetição \"enquanto\" e \"faca - enquanto\". Exemplo: \"enquanto(verdadeiro)\""), no.getTrechoCodigoFonteTipoDado()), asa, no);

            case "cadeia":
                throw new ExcecaoVisitaASA(new ErroExecucaoPlugin(String.format("Variáveis do tipo Cadeia não são suportadas pela GoGo Board, portanto não podem ser enviadas a ela."), no.getTrechoCodigoFonteTipoDado()), asa, no);

            case "caracter":
                throw new ExcecaoVisitaASA(new ErroExecucaoPlugin(String.format("Variáveis do tipo Caracter não são suportadas pela GoGo Board, portanto não podem ser enviadas a ela."), no.getTrechoCodigoFonteTipoDado()), asa, no);

            case "real":
                throw new ExcecaoVisitaASA(new ErroExecucaoPlugin(String.format("Variáveis do tipo Real não são suportadas pela GoGo Board, portanto não podem ser enviadas a ela."), no.getTrechoCodigoFonteTipoDado()), asa, no);

            /*default:
                if (!(no.getInicializacao() instanceof NoChamadaFuncao)) {
                    no.getInicializacao().aceitar(this);
                }*/
        }
        return null;
    }

    @Override
    public Object visitar(NoOperacaoLogicaDiferenca no) throws ExcecaoVisitaASA {
        System.out.println("NoOperacaoLogicaDiferenca");
        throw new ExcecaoVisitaASA(new ErroExecucaoPlugin(String.format("Operação lógica diferença não é suportada pela GoGo Board, portanto não pode ser enviada a ela."), no.getTrechoCodigoFonte()), asa, no);
    }

    @Override
    public Object visitar(NoOperacaoBitwiseLeftShift no) throws ExcecaoVisitaASA {
        System.out.println("NoOperacaoBitwiseLeftShift");
        throw new ExcecaoVisitaASA(new ErroExecucaoPlugin(String.format("Operações do tipo Bitwise Shift não são suportadas pela GoGo Board, portanto não podem ser enviadas a ela"), no.getTrechoCodigoFonte()), asa, no);
    }

    @Override
    public Object visitar(NoOperacaoBitwiseRightShift no) throws ExcecaoVisitaASA {
        System.out.println("NoOperacaoBitwiseRightShift");
        throw new ExcecaoVisitaASA(new ErroExecucaoPlugin(String.format("Operações do tipo Bitwise Shift não são suportadas pela GoGo Board, portanto não podem ser enviadas a ela"), no.getTrechoCodigoFonte()), asa, no);
    }

    @Override
    public Object visitar(NoOperacaoBitwiseE no) throws ExcecaoVisitaASA {
        System.out.println("NoOperacaoBitwiseE");
        throw new ExcecaoVisitaASA(new ErroExecucaoPlugin(String.format("Operações do tipo Bitwise AND não são suportadas pela GoGo Board, portanto não podem ser enviadas a ela"), no.getTrechoCodigoFonte()), asa, no);
    }

    @Override
    public Object visitar(NoOperacaoBitwiseOu no) throws ExcecaoVisitaASA {
        System.out.println("NoOperacaoBitwiseOu");
        throw new ExcecaoVisitaASA(new ErroExecucaoPlugin(String.format("Operações do tipo Bitwise OR não são suportadas pela GoGo Board, portanto não podem ser enviadas a ela"), no.getTrechoCodigoFonte()), asa, no);
    }

    @Override
    public Object visitar(NoBitwiseNao no) throws ExcecaoVisitaASA {
        System.out.println("NoBitwiseNao");
        throw new ExcecaoVisitaASA(new ErroExecucaoPlugin(String.format("Operações do tipo Bitwise NÃO não são suportadas pela GoGo Board, portanto não podem ser enviadas a ela"), no.getTrechoCodigoFonte()), asa, no);
    }

    @Override
    public Object visitar(NoReal no) throws ExcecaoVisitaASA {
        System.out.println("NoReal");
        throw new ExcecaoVisitaASA(new ErroExecucaoPlugin(String.format("Variáveis do tipo Real não são suportadas pela GoGo Board, portanto não podem ser enviadas a ela"), no.getTrechoCodigoFonte()), asa, no);
    }

    @Override
    public Object visitar(NoChamadaFuncao no) throws ExcecaoVisitaASA {
        System.out.println("NoChamadaFuncao");

        switch (no.getNome()) {
            case "escreva":
            case "leia":
            case "limpa":
                throw new ExcecaoVisitaASA(new ErroExecucaoPlugin(String.format("A função '" + no.getNome() + "' não pode ser utilizada ao enviar código para GoGoBoard!"), no.getTrechoCodigoFonte()), asa, no);
        }

        if (no.getParametros() != null) {
            for (NoExpressao noExpressao : no.getParametros()) {
                noExpressao.aceitar(this);
            }
        }
        return null;
    }

    /**
     * Método privado para visitar os blocos de comandos.
     *
     * @throws br.univali.portugol.nucleo.asa.ExcecaoVisitaASA
     */
    private void visitarBlocos(List<NoBloco> blocos) throws ExcecaoVisitaASA {
        System.out.println("Blocos");
        if (blocos != null) {
            for (NoBloco bloco : blocos) {
                bloco.aceitar(this);
            }
        }
    }

    @Override
    public Object visitar(NoEnquanto no) throws ExcecaoVisitaASA {
        System.out.println("NoEnquanto");
        // TODO: Verificar se é um laço válido para monar o repet
        // Se for um "enquanto(verdadeiro)" trasformar em um forever
        if (no.getCondicao() instanceof NoLogico) {
            // TODO: Se for falso, lançar mensagem de erro
        } else {
            no.getCondicao().aceitar(this); // TODO: verificar a condição do laço
        }
        visitarBlocos(no.getBlocos());
        return null;
    }

    @Override
    public Object visitar(NoFacaEnquanto no) throws ExcecaoVisitaASA {
        System.out.println("noFacaEnquanto");
        // TODO: Verificar se é um laço válido para monar o repet
        // Se for um "faça-enquanto(verdadeiro)" trasformar em um forever, que não necessita de uma interação antes da checagem
        if (no.getCondicao() instanceof NoLogico) {
            // TODO: Se for falso, lançar mensagem de erro
        } else {
            // Visita os blocos antes para criar uma interação antes da checagem do laço

            visitarBlocos(no.getBlocos());

            no.getCondicao().aceitar(this); // TODO: verificar a condição do laço
        }
        visitarBlocos(no.getBlocos());
        return null;
    }

    @Override
    public Object visitar(NoPara no) throws ExcecaoVisitaASA {
        System.out.println("NoPara");
        // TODO: Verificar se é um laço válido para monar o repet
        no.getCondicao().aceitar(this); // TODO: verificar a condição do laço

        visitarBlocos(no.getBlocos());

        return null;
    }

    @Override
    public Object visitar(NoEscolha no) throws ExcecaoVisitaASA {
        throw new ExcecaoVisitaASA(new ErroExecucaoPlugin(String.format("Operações do tipo Escolha não são suportadas pela GoGo Board, portanto não podem ser enviadas a ela"), no.getTrechoCodigoFonte()), asa, no);
    }

    @Override
    public Object visitar(NoCaso no) throws ExcecaoVisitaASA {
        throw new ExcecaoVisitaASA(new ErroExecucaoPlugin(String.format("Operações do tipo Caso não são suportadas pela GoGo Board, portanto não podem ser enviadas a ela"), no.getTrechoCodigoFonte()), asa, no);
    }

    @Override
    public Object visitar(NoContinue noContinue) throws ExcecaoVisitaASA {
        throw new ExcecaoVisitaASA(new ErroExecucaoPlugin(String.format("Operações do tipo Continue não são suportadas pela GoGo Board, portanto não podem ser enviadas a ela"), noContinue.getTrechoCodigoFonte()), asa, noContinue);
    }
}
