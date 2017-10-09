package br.univali.portugol.plugin.gogoboard;

import br.univali.portugol.nucleo.asa.ASAPrograma;
import br.univali.portugol.nucleo.asa.ExcecaoVisitaASA;
import br.univali.portugol.nucleo.asa.NoBloco;
import br.univali.portugol.nucleo.asa.NoChamadaFuncao;
import br.univali.portugol.nucleo.asa.NoDeclaracao;
import br.univali.portugol.nucleo.asa.NoDeclaracaoFuncao;
import br.univali.portugol.nucleo.asa.NoDeclaracaoParametro;
import br.univali.portugol.nucleo.asa.NoDeclaracaoVariavel;
import br.univali.portugol.nucleo.asa.NoEnquanto;
import br.univali.portugol.nucleo.asa.NoExpressao;
import br.univali.portugol.nucleo.asa.NoFacaEnquanto;
import br.univali.portugol.nucleo.asa.NoInclusaoBiblioteca;
import br.univali.portugol.nucleo.asa.NoInteiro;
import br.univali.portugol.nucleo.asa.NoLogico;
import br.univali.portugol.nucleo.asa.NoNao;
import br.univali.portugol.nucleo.asa.NoOperacao;
import br.univali.portugol.nucleo.asa.NoOperacaoAtribuicao;
import br.univali.portugol.nucleo.asa.NoOperacaoBitwiseXOR;
import br.univali.portugol.nucleo.asa.NoOperacaoDivisao;
import br.univali.portugol.nucleo.asa.NoOperacaoLogicaE;
import br.univali.portugol.nucleo.asa.NoOperacaoLogicaIgualdade;
import br.univali.portugol.nucleo.asa.NoOperacaoLogicaMaior;
import br.univali.portugol.nucleo.asa.NoOperacaoLogicaMaiorIgual;
import br.univali.portugol.nucleo.asa.NoOperacaoLogicaMenor;
import br.univali.portugol.nucleo.asa.NoOperacaoLogicaMenorIgual;
import br.univali.portugol.nucleo.asa.NoOperacaoLogicaOU;
import br.univali.portugol.nucleo.asa.NoOperacaoModulo;
import br.univali.portugol.nucleo.asa.NoOperacaoMultiplicacao;
import br.univali.portugol.nucleo.asa.NoOperacaoSoma;
import br.univali.portugol.nucleo.asa.NoOperacaoSubtracao;
import br.univali.portugol.nucleo.asa.NoPara;
import br.univali.portugol.nucleo.asa.NoReferenciaVariavel;
import br.univali.portugol.nucleo.asa.NoRetorne;
import br.univali.portugol.nucleo.asa.NoSe;
import br.univali.portugol.nucleo.asa.VisitanteNulo;
import br.univali.portugol.nucleo.execucao.gerador.helpers.Utils;
import br.univali.ps.plugins.base.ErroExecucaoPlugin;
import java.util.List;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class ConversorLogo extends VisitanteNulo {

    private final ASAPrograma asa;
    private StringBuilder codigoLogo;
    private int nivelEscopo;

    public ConversorLogo(ASAPrograma asa) {
        //Exemplo
        /*File arquivoJava = new File("D:\\Documentos\\Desktop\\", "Logo.txt");
        PrintWriter writerArquivoJava = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(arquivoJava), Charset.forName("utf-8"))));
        this.codigoLogo = new PrintWriter(writerArquivoJava);*/
        this.codigoLogo = new StringBuilder();
        this.nivelEscopo = 1;
        this.asa = asa;
    }

    public String converterCodigo() throws ExcecaoVisitaASA {
        System.out.println("\n------------- Visita ASA Conversor -------------");
        asa.aceitar(this);
        return codigoLogo.toString();
    }

    @Override
    public Object visitar(ASAPrograma asap) throws ExcecaoVisitaASA {

        // Vesitar bibliotecas incluidas
        for (NoInclusaoBiblioteca biblioteca : asap.getListaInclusoesBibliotecas()) {
            biblioteca.aceitar(this);
        }

        codigoLogo.append("to inicio\n");

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
    public Object visitar(NoDeclaracaoVariavel no) throws ExcecaoVisitaASA {
        System.out.println("NoDeclaracaoVariavel");
        String identacao = Utils.geraIdentacao(nivelEscopo);

        switch (no.getTipoDado().getNome()) {
            case "inteiro":
                codigoLogo.append(identacao).append("set ").append(no.getNome()).append(" (");
                if (no.getInicializacao() != null) {
                    if (no.getInicializacao() instanceof NoChamadaFuncao) {
                        montarChamadaFuncao((NoChamadaFuncao) no.getInicializacao(), this);
                    } else {
                        no.getInicializacao().aceitar(this);
                    }
                } else {
                    codigoLogo.append("0");
                }
                break;
            default:
                if (no.getInicializacao() instanceof NoChamadaFuncao) {
                    montarChamadaFuncao((NoChamadaFuncao) no.getInicializacao(), this);
                } else {
                    no.getInicializacao().aceitar(this);
                }
        }
        codigoLogo.append(")\n");
        return null;
    }

    @Override
    public Object visitar(NoDeclaracaoFuncao no) throws ExcecaoVisitaASA {
        System.out.println("NoDeclaracaoFuncao");
        // Ignorado quando for a função 'inicio' pois já foi adicionada no código
        if (!no.getNome().equalsIgnoreCase("inicio")) {
            codigoLogo.append("to ").append(no.getNome());
        }

        for (NoDeclaracaoParametro param : no.getParametros()) {
            param.aceitar(this);
        }

        codigoLogo.append("\n");

        nivelEscopo++;
        for (NoBloco bloco : no.getBlocos()) {
            bloco.aceitar(this);
        }
        nivelEscopo--;
        codigoLogo.append("\nend\n\n");
        return null;
    }

    @Override
    public Object visitar(NoDeclaracaoParametro no) throws ExcecaoVisitaASA {
        System.out.println("NoDeclaracaoParametro");
        codigoLogo.append(" :" + no.getNome());
        return null;
    }

    @Override
    public Object visitar(NoRetorne no) throws ExcecaoVisitaASA {
        System.out.println("NoRetorne");
        String identacao = Utils.geraIdentacao(nivelEscopo);
        codigoLogo.append(identacao).append("output ");
        no.getExpressao().aceitar(this);
        codigoLogo.append("\n");
        return null;
    }

    @Override
    public Object visitar(NoSe no) throws ExcecaoVisitaASA {
        System.out.println("Nose");
        String identacao = Utils.geraIdentacao(nivelEscopo);

        if (no.getBlocosFalsos() == null) {
            codigoLogo.append(identacao).append("if (");
            nivelEscopo++;
            no.getCondicao().aceitar(this);

            codigoLogo.append(")\n");
            codigoLogo.append(identacao).append("[\n");

            visitarBlocos(no.getBlocosVerdadeiros());
            codigoLogo.append(identacao).append("]\n");
            nivelEscopo--;
        } else {
            codigoLogo.append(identacao).append("ifelse (");
            nivelEscopo++;
            no.getCondicao().aceitar(this);

            codigoLogo.append(")\n");
            codigoLogo.append(identacao).append("[\n");
            nivelEscopo++;

            visitarBlocos(no.getBlocosVerdadeiros());
            codigoLogo.append("\n");
            codigoLogo.append(identacao).append("] [\n");

            visitarBlocos(no.getBlocosFalsos());

            codigoLogo.append(identacao).append("]\n");
            nivelEscopo--;
        }
        return null;
    }

    private void visitarBlocos(List<NoBloco> blocos) throws ExcecaoVisitaASA {
        System.out.println("Blocos");
        if (blocos != null) {
            for (NoBloco bloco : blocos) {
                bloco.aceitar(this);
            }
        }
    }

    @Override
    public Object visitar(NoOperacaoLogicaIgualdade no) throws ExcecaoVisitaASA {
        System.out.println("NoOperacaoLogicaIgualdade");
        if (no.estaEntreParenteses()) {
            codigoLogo.append("(");
            no.getOperandoEsquerdo().aceitar(this);
            codigoLogo.append(" = ");
            no.getOperandoDireito().aceitar(this);
            codigoLogo.append(")");
        } else {
            no.getOperandoEsquerdo().aceitar(this);
            codigoLogo.append(" = ");
            no.getOperandoDireito().aceitar(this);
        }
        return null;
    }

    @Override
    public Object visitar(NoOperacaoAtribuicao no) throws ExcecaoVisitaASA {
        System.out.println("NoOperacaoAtribuicao");
        String identacao = Utils.geraIdentacao(nivelEscopo);
        codigoLogo.append(identacao).append("set ");

        no.getOperandoEsquerdo().aceitar(this);
        codigoLogo.append(" (");
        no.getOperandoDireito().aceitar(this);
        codigoLogo.append(")\n");
        return null;
    }

    @Override
    public Object visitar(NoOperacaoLogicaE no) throws ExcecaoVisitaASA {
        System.out.println("NoOperacaoLogicaE");
        no.getOperandoDireito().aceitar(this);
        codigoLogo.append(" and ");
        no.getOperandoEsquerdo().aceitar(this);
        return null;
    }

    @Override
    public Object visitar(NoOperacaoLogicaOU no) throws ExcecaoVisitaASA {
        System.out.println("NoOperacaoLogicaOU");
        no.getOperandoDireito().aceitar(this);
        codigoLogo.append(" or ");
        no.getOperandoEsquerdo().aceitar(this);
        return null;
    }

    @Override
    public Object visitar(NoOperacaoLogicaMaior no) throws ExcecaoVisitaASA {
        System.out.println("NoOperacaoLogicaMaior");
        no.getOperandoEsquerdo().aceitar(this);
        codigoLogo.append(" > ");
        no.getOperandoDireito().aceitar(this);
        return null;
    }

    @Override
    public Object visitar(NoOperacaoLogicaMaiorIgual no) throws ExcecaoVisitaASA {
        System.out.println("NoOperacaoLogicaMaiorIgual");
        no.getOperandoEsquerdo().aceitar(this);
        codigoLogo.append(" >= ");
        no.getOperandoDireito().aceitar(this);
        return null;
    }

    @Override
    public Object visitar(NoOperacaoLogicaMenor no) throws ExcecaoVisitaASA {
        System.out.println("NoOperacaoLogicaMenor");
        no.getOperandoEsquerdo().aceitar(this);
        codigoLogo.append(" < ");
        no.getOperandoDireito().aceitar(this);
        return null;
    }

    @Override
    public Object visitar(NoOperacaoLogicaMenorIgual no) throws ExcecaoVisitaASA {
        System.out.println("NoOperacaoLogicaMenorIgual");
        no.getOperandoEsquerdo().aceitar(this);
        codigoLogo.append(" <= ");
        no.getOperandoDireito().aceitar(this);
        return null;
    }

    @Override
    public Object visitar(NoOperacaoSoma no) throws ExcecaoVisitaASA {
        System.out.println("NoOperacaoSoma");
        if (no.estaEntreParenteses()) {
            codigoLogo.append("(");
            no.getOperandoEsquerdo().aceitar(this);
            codigoLogo.append(" + ");
            no.getOperandoDireito().aceitar(this);
            codigoLogo.append(")");
        } else {
            no.getOperandoEsquerdo().aceitar(this);
            codigoLogo.append(" + ");
            no.getOperandoDireito().aceitar(this);
        }
        return null;
    }

    @Override
    public Object visitar(NoOperacaoSubtracao no) throws ExcecaoVisitaASA {
        System.out.println("NoOperacaoSubtracao");
        if (no.estaEntreParenteses()) {
            codigoLogo.append("(");
            no.getOperandoDireito().aceitar(this);
            codigoLogo.append(" - ");
            no.getOperandoEsquerdo().aceitar(this);
            codigoLogo.append(")");
        } else {
            no.getOperandoDireito().aceitar(this);
            codigoLogo.append(" - ");
            no.getOperandoEsquerdo().aceitar(this);
        }
        return null;
    }

    @Override
    public Object visitar(NoOperacaoDivisao no) throws ExcecaoVisitaASA {
        System.out.println("NoOperacaoDivisao");
        if (no.estaEntreParenteses()) {
            codigoLogo.append("(");
            no.getOperandoDireito().aceitar(this);
            codigoLogo.append(" / ");
            no.getOperandoEsquerdo().aceitar(this);
            codigoLogo.append(")");
        } else {
            no.getOperandoDireito().aceitar(this);
            codigoLogo.append(" / ");
            no.getOperandoEsquerdo().aceitar(this);
        }
        return null;
    }

    @Override
    public Object visitar(NoOperacaoMultiplicacao no) throws ExcecaoVisitaASA {
        System.out.println("NoOperacaoMultiplicacao");
        if (no.estaEntreParenteses()) {
            codigoLogo.append("(");
            no.getOperandoDireito().aceitar(this);
            codigoLogo.append(" * ");
            no.getOperandoEsquerdo().aceitar(this);
            codigoLogo.append(")");
        } else {
            no.getOperandoDireito().aceitar(this);
            codigoLogo.append(" * ");
            no.getOperandoEsquerdo().aceitar(this);
        }
        return null;
    }

    @Override
    public Object visitar(NoOperacaoModulo no) throws ExcecaoVisitaASA {
        System.out.println("NoOperacaoModulo");
        if (no.estaEntreParenteses()) {
            codigoLogo.append("(");
            no.getOperandoDireito().aceitar(this);
            codigoLogo.append(" % ");
            no.getOperandoEsquerdo().aceitar(this);
            codigoLogo.append(")");
        } else {
            no.getOperandoDireito().aceitar(this);
            codigoLogo.append(" % ");
            no.getOperandoEsquerdo().aceitar(this);
        }
        return null;
    }

    @Override
    public Object visitar(NoOperacaoBitwiseXOR no) throws ExcecaoVisitaASA {
        System.out.println("NoOperacaoBitwiseXOR");
        if (no.estaEntreParenteses()) {
            codigoLogo.append("(");
            no.getOperandoDireito().aceitar(this);
            codigoLogo.append(" ^ ");
            no.getOperandoEsquerdo().aceitar(this);
            codigoLogo.append(")");
        } else {
            no.getOperandoDireito().aceitar(this);
            codigoLogo.append(" ^ ");
            no.getOperandoEsquerdo().aceitar(this);
        }
        return null;
    }

    @Override
    public Object visitar(NoNao no) throws ExcecaoVisitaASA {
        System.out.println("noNao");
        codigoLogo.append(" not ");
        if (no.getExpressao().estaEntreParenteses()) {
            codigoLogo.append("(");
            no.getExpressao().aceitar(this);
            codigoLogo.append(")");
        } else {
            no.getExpressao().aceitar(this);
        }
        return null;
    }

    @Override
    public Object visitar(NoInteiro no) throws ExcecaoVisitaASA {
        System.out.println("NoInteiro");
        codigoLogo.append(no.getValor());
        return null;
    }

    @Override
    public Object visitar(NoReferenciaVariavel no) throws ExcecaoVisitaASA {
        System.out.println("NoReferenciaVariavel");
        codigoLogo.append(no.getNome());
        return null;
    }

    @Override
    public Object visitar(NoChamadaFuncao no) throws ExcecaoVisitaASA {
        System.out.println("NoChamadaFuncao");
        String identacao = Utils.geraIdentacao(nivelEscopo);

        switch (no.getNome()) {
            case "acionar_beep":
                codigoLogo.append(identacao).append("beep\n");
                break;
            default:
                if (no.isFuncaoDeBiblioteca()) {
                    throw new ExcecaoVisitaASA(new ErroExecucaoPlugin(String.format("A função '" + no.getNome() + "' ainda não esta pronta para ser enviada a GoGoBoard! Contate o desenvolvedor do plugin"), no.getTrechoCodigoFonte()), asa, no);
                }else{
                    codigoLogo.append(identacao).append(no.getNome());
                }
        }

        if (no.getParametros() != null) {
            for (NoExpressao noExpressao : no.getParametros()) {
                codigoLogo.append(" (");
                noExpressao.aceitar(this);
                codigoLogo.append(")");
            }
            codigoLogo.append("\n");
        }
        return null;
    }

    private void montarChamadaFuncao(NoChamadaFuncao no, ConversorLogo aThis) throws ExcecaoVisitaASA {
        codigoLogo.append(no.getNome());
        if (no.getParametros() != null) {
            for (NoExpressao parametro : no.getParametros()) {
                codigoLogo.append(" (");
                parametro.aceitar(aThis);
                codigoLogo.append(")");
            }
        }
    }

    @Override
    public Object visitar(NoEnquanto no) throws ExcecaoVisitaASA {
        System.out.println("NoEnquanto");
        String identacao = Utils.geraIdentacao(nivelEscopo);
        // Se for um "enquanto(verdadeiro)" trasformar em um forever
        if (no.getCondicao() instanceof NoLogico) {
            codigoLogo.append(identacao).append("forever").append("\n");
        } else {
            codigoLogo.append(identacao).append("repeat (");
            no.getCondicao().aceitar(this); // Visitar a condição do laço

            codigoLogo.append(")\n");
        }
        codigoLogo.append(identacao).append("[\n");
        nivelEscopo++;
        visitarBlocos(no.getBlocos());
        codigoLogo.append(identacao).append("]\n");
        nivelEscopo--;
        return null;
    }

    @Override
    public Object visitar(NoPara no) throws ExcecaoVisitaASA {
        System.out.println("NoPara");
        String identacao = Utils.geraIdentacao(nivelEscopo);

        String nomeContInternoLaco = montarInicializacaoPara(no, identacao);

        no.getCondicao().aceitar(this); // Visitar a condição do laço

        codigoLogo.append("\n").append(identacao).append("[\n");
        nivelEscopo++;
        visitarBlocos(no.getBlocos());

        adicionaContInternoLaco(no, nomeContInternoLaco);
        codigoLogo.append(identacao).append("]\n");
        nivelEscopo--;
        return null;
    }

    private String montarInicializacaoPara(NoPara no, String identacao) throws ExcecaoVisitaASA {
        String nomeContInternoLaco = null;

        if (no.getInicializacoes() != null) {
            // Se for utilizado somente uma variavel como referencia no primeiro valor

            for (NoBloco inicializacao : no.getInicializacoes()) {

                if (inicializacao instanceof NoReferenciaVariavel) {
                    NoReferenciaVariavel noReferenciaVariavel = (NoReferenciaVariavel) inicializacao;
                    NoDeclaracaoVariavel noDeclaracaoVariavel = (NoDeclaracaoVariavel) noReferenciaVariavel.getOrigemDaReferencia();
                    nomeContInternoLaco = noDeclaracaoVariavel.getNome();  // Guarda o nome da variavel para criar o contador interno
                    //codigoLogo.append(identacao).append("repeat ");
                } else // Se for utilizado atribuicao a uma variavel já declarada 
                if (inicializacao instanceof NoOperacaoAtribuicao) {
                    NoOperacaoAtribuicao noOperacaoAtribuicao = (NoOperacaoAtribuicao) inicializacao;
                    nomeContInternoLaco = noOperacaoAtribuicao.getOperandoEsquerdo().toString();
                    //noOperacaoAtribuicao.getOperandoDireito().aceitar(this);
                    noOperacaoAtribuicao.aceitar(this); // Visita a operação
                    //codigoLogo.append(identacao).append("repeat ");
                } else // Se for utilizado uma declaraçao de variável
                if (inicializacao instanceof NoDeclaracaoVariavel) {
                    NoDeclaracaoVariavel noDeclaracaoVariavel = (NoDeclaracaoVariavel) inicializacao;
                    nomeContInternoLaco = noDeclaracaoVariavel.getNome();
                    noDeclaracaoVariavel.aceitar(this); // Visita a declaração
                    //codigoLogo.append(identacao).append("repeat ");
                }
            }
            codigoLogo.append(identacao).append("repeat ");
        }
        return nomeContInternoLaco;
    }

    private void adicionaContInternoLaco(NoPara no, String nomeContInternoLaco) {
        String identacao = Utils.geraIdentacao(nivelEscopo);

        NoOperacao noOp = (NoOperacao) no.getIncremento();
        NoExpressao noExp = noOp.getOperandoDireito();

        codigoLogo.append(identacao).append("set ").append(nomeContInternoLaco).append(" (").append(nomeContInternoLaco);
        if (noExp instanceof NoOperacaoSoma) {
            codigoLogo.append(" + 1)\n");
        } else {
            codigoLogo.append(" - 1)\n");
        }
    }

    @Override
    public Object visitar(NoFacaEnquanto no) throws ExcecaoVisitaASA {
        System.out.println("noFacaEnquanto");
        String identacao = Utils.geraIdentacao(nivelEscopo);

        // Se for um "faça-enquanto(verdadeiro)" trasformar em um forever, que não necessita de uma interação antes da checagem
        if (no.getCondicao() instanceof NoLogico) {
            codigoLogo.append(identacao).append("forever").append("\n");
        } else {
            // Visita os blocos antes para criar uma interação antes da checagem do laço
            codigoLogo.append("\n\n");
            visitarBlocos(no.getBlocos());

            codigoLogo.append(identacao).append("repeat (");
            no.getCondicao().aceitar(this); // Visitar a condição do laço

            codigoLogo.append(")\n");
        }
        codigoLogo.append(identacao).append("[\n");
        nivelEscopo++;
        visitarBlocos(no.getBlocos());
        codigoLogo.append(identacao).append("]\n");
        nivelEscopo--;
        return null;
    }
}
