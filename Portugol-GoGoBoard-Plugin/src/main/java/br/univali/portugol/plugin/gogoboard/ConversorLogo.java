package br.univali.portugol.plugin.gogoboard;

import br.univali.portugol.nucleo.asa.ASAPrograma;
import br.univali.portugol.nucleo.asa.ExcecaoVisitaASA;
import br.univali.portugol.nucleo.asa.NoBloco;
import br.univali.portugol.nucleo.asa.NoCadeia;
import br.univali.portugol.nucleo.asa.NoCaracter;
import br.univali.portugol.nucleo.asa.NoCaso;
import br.univali.portugol.nucleo.asa.NoChamadaFuncao;
import br.univali.portugol.nucleo.asa.NoDeclaracao;
import br.univali.portugol.nucleo.asa.NoDeclaracaoFuncao;
import br.univali.portugol.nucleo.asa.NoDeclaracaoParametro;
import br.univali.portugol.nucleo.asa.NoDeclaracaoVariavel;
import br.univali.portugol.nucleo.asa.NoEnquanto;
import br.univali.portugol.nucleo.asa.NoEscolha;
import br.univali.portugol.nucleo.asa.NoExpressao;
import br.univali.portugol.nucleo.asa.NoInclusaoBiblioteca;
import br.univali.portugol.nucleo.asa.NoInteiro;
import br.univali.portugol.nucleo.asa.NoNao;
import br.univali.portugol.nucleo.asa.NoOperacao;
import br.univali.portugol.nucleo.asa.NoOperacaoAtribuicao;
import br.univali.portugol.nucleo.asa.NoOperacaoBitwiseE;
import br.univali.portugol.nucleo.asa.NoOperacaoBitwiseLeftShift;
import br.univali.portugol.nucleo.asa.NoOperacaoBitwiseOu;
import br.univali.portugol.nucleo.asa.NoOperacaoBitwiseRightShift;
import br.univali.portugol.nucleo.asa.NoOperacaoBitwiseXOR;
import br.univali.portugol.nucleo.asa.NoOperacaoDivisao;
import br.univali.portugol.nucleo.asa.NoOperacaoLogicaDiferenca;
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
import br.univali.portugol.nucleo.asa.NoSe;
import br.univali.portugol.nucleo.asa.VisitanteNulo;
import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.nucleo.execucao.gerador.helpers.Utils;
import java.util.List;
import javax.swing.JOptionPane;

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
        asa.aceitar(this);
        return codigoLogo.toString();
    }

    @Override
    public Object visitar(ASAPrograma asap) throws ExcecaoVisitaASA {

        for (NoInclusaoBiblioteca biblioteca : asap.getListaInclusoesBibliotecas()) {
            biblioteca.aceitar(this);
        }

        for (NoDeclaracao declaracao : asap.getListaDeclaracoesGlobais()) {
            declaracao.aceitar(this);
        }

        return null;
    }

    @Override
    public Object visitar(NoInclusaoBiblioteca noInclusaoBiblioteca) throws ExcecaoVisitaASA {
        if (!noInclusaoBiblioteca.getNome().equalsIgnoreCase("GoGoBoard")) {
            System.out.println("Biblioteca encontrada: " + noInclusaoBiblioteca.getNome());
            JOptionPane.showMessageDialog(null, "O programa contém bibliotecas que não podem ser enviadas para a GoGoBoard!\n"
                    + noInclusaoBiblioteca.getNome() + "[" + noInclusaoBiblioteca.getTrechoCodigoFonte().getLinha() + "," + noInclusaoBiblioteca.getTrechoCodigoFonte().getColuna() + "]", "Erro!", JOptionPane.INFORMATION_MESSAGE);
            //throw new ExcecaoVisitaASA("O programa contém bibliotecas que não podem ser enviadas para a GoGoBoard!\n"
            //       + noInclusaoBiblioteca.getNome() + "[" + noInclusaoBiblioteca.getTrechoCodigoFonte().getLinha() + "," + noInclusaoBiblioteca.getTrechoCodigoFonte().getColuna() + "]", asa, noInclusaoBiblioteca);
        }
        return null;
    }

    @Override
    public Object visitar(NoDeclaracaoVariavel no) throws ExcecaoVisitaASA {
        System.err.println("NoDeclaracaoVariavel");
        String identacao = Utils.geraIdentacao(nivelEscopo);
        if (no.getTipoDado().getNome().equalsIgnoreCase("inteiro")) {
            codigoLogo.append(identacao).append("set ").append(no.getNome()).append(" (");
            if (no.getInicializacao() != null) {
                no.getInicializacao().aceitar(this);
            } else {
                codigoLogo.append("0");
            }
        } else {
            no.getInicializacao().aceitar(this);
        }
        codigoLogo.append(")\n");
        return null;
    }

    @Override
    public Object visitar(NoDeclaracaoFuncao declaracaoFuncao) throws ExcecaoVisitaASA {
        System.err.println("NoDeclaracaoFuncao");
        codigoLogo.append("to ").append(declaracaoFuncao.getNome()).append("\n");
        for (NoDeclaracaoParametro no : declaracaoFuncao.getParametros()) {
            no.aceitar(this);
        }

        for (NoBloco bloco : declaracaoFuncao.getBlocos()) {
            bloco.aceitar(this);
        }
        codigoLogo.append("end");
        return null;
    }

    @Override
    public Object visitar(NoSe noSe) throws ExcecaoVisitaASA {
        System.err.println("Nose");
        String identacao = Utils.geraIdentacao(nivelEscopo);

        if (noSe.getBlocosFalsos() == null) {
            codigoLogo.append(identacao).append("if (");
            nivelEscopo++;
            noSe.getCondicao().aceitar(this);

            codigoLogo.append(")\n");
            codigoLogo.append(identacao).append("[\n");

            visitarBlocos(noSe.getBlocosVerdadeiros());
            codigoLogo.append(identacao).append("]\n");
            nivelEscopo--;
        } else {
            codigoLogo.append(identacao).append("ifelse (");
            nivelEscopo++;
            noSe.getCondicao().aceitar(this);

            codigoLogo.append(")\n");
            codigoLogo.append(identacao).append("[\n");
            nivelEscopo++;

            visitarBlocos(noSe.getBlocosVerdadeiros());
            codigoLogo.append("\n");
            codigoLogo.append(identacao).append("] [\n");

            visitarBlocos(noSe.getBlocosFalsos());

            codigoLogo.append(identacao).append("]\n");
            nivelEscopo--;
        }
        return null;
    }

    private void visitarBlocos(List<NoBloco> blocos) throws ExcecaoVisitaASA {
        System.err.println("Blocos");
        if (blocos != null) {
            for (NoBloco bloco : blocos) {
                bloco.aceitar(this);
            }
        }
    }

    /*private Object visitarOperacao(NoOperacao operacao) throws ExcecaoVisitaASA {
        System.err.println("NoOperacao, Esquerdo: " + operacao.getOperandoEsquerdo().toString() + ", Direito: " + operacao.getOperandoDireito().toString());
        operacao.getOperandoEsquerdo().aceitar(this);
        operacao.getOperandoDireito().aceitar(this);
        return null;
    }*/
    @Override
    public Object visitar(NoOperacaoLogicaIgualdade noOperacaoLogicaIgualdade) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoLogicaIgualdade");
        if (noOperacaoLogicaIgualdade.getOperandoEsquerdo().estaEntreParenteses()) {
            codigoLogo.append("(").append(noOperacaoLogicaIgualdade.getOperandoEsquerdo().toString()).append(")");
        } else {
            codigoLogo.append(noOperacaoLogicaIgualdade.getOperandoEsquerdo().toString());
        }
        codigoLogo.append(" = ");

        if (noOperacaoLogicaIgualdade.getOperandoDireito().estaEntreParenteses()) {
            codigoLogo.append("(").append(noOperacaoLogicaIgualdade.getOperandoDireito().toString()).append(")");
        } else {
            codigoLogo.append(noOperacaoLogicaIgualdade.getOperandoDireito().toString());
        }
        return null;
    }

    @Override
    public Object visitar(NoOperacaoLogicaDiferenca noOperacaoLogicaDiferenca) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoLogicaDiferenca");
        //TODO: Adicionar aviso que não é suportado
        return null;
    }

    @Override
    public Object visitar(NoOperacaoAtribuicao noOperacaoAtribuicao) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoAtribuicao");
        String identacao = Utils.geraIdentacao(nivelEscopo);
        codigoLogo.append(identacao)
                .append("set ")
                .append(noOperacaoAtribuicao.getOperandoEsquerdo())
                .append(" (")
                .append(noOperacaoAtribuicao.getOperandoDireito())
                .append(")\n");
        return null;
    }

    @Override
    public Object visitar(NoOperacaoLogicaE noOperacaoLogicaE) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoLogicaE");
        noOperacaoLogicaE.getOperandoDireito().aceitar(this);
        codigoLogo.append(" and ");
        noOperacaoLogicaE.getOperandoEsquerdo().aceitar(this);
        return null;
    }

    @Override
    public Object visitar(NoOperacaoLogicaOU noOperacaoLogicaOU) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoLogicaOU");
        noOperacaoLogicaOU.getOperandoDireito().aceitar(this);
        codigoLogo.append(" or ");
        noOperacaoLogicaOU.getOperandoEsquerdo().aceitar(this);
        return null;
    }

    @Override
    public Object visitar(NoOperacaoLogicaMaior noOperacaoLogicaMaior) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoLogicaMaior");
        noOperacaoLogicaMaior.getOperandoEsquerdo().aceitar(this);
        codigoLogo.append(" > ");
        noOperacaoLogicaMaior.getOperandoDireito().aceitar(this);
        return null;
    }

    @Override
    public Object visitar(NoOperacaoLogicaMaiorIgual noOperacaoLogicaMaiorIgual) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoLogicaMaiorIgual");
        noOperacaoLogicaMaiorIgual.getOperandoEsquerdo().aceitar(this);
        codigoLogo.append(" >= ");
        noOperacaoLogicaMaiorIgual.getOperandoDireito().aceitar(this);
        return null;
    }

    @Override
    public Object visitar(NoOperacaoLogicaMenor noOperacaoLogicaMenor) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoLogicaMenor");
        noOperacaoLogicaMenor.getOperandoEsquerdo().aceitar(this);
        codigoLogo.append(" < ");
        noOperacaoLogicaMenor.getOperandoDireito().aceitar(this);
        return null;
    }

    @Override
    public Object visitar(NoOperacaoLogicaMenorIgual noOperacaoLogicaMenorIgual) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoLogicaMenorIgual");
        noOperacaoLogicaMenorIgual.getOperandoEsquerdo().aceitar(this);
        codigoLogo.append(" <= ");
        noOperacaoLogicaMenorIgual.getOperandoDireito().aceitar(this);
        return null;
    }

    @Override
    public Object visitar(NoOperacaoSoma noOperacaoSoma) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoSoma");
        codigoLogo.append("(").append(noOperacaoSoma.toString()).append(")");
        return null;
    }

    @Override
    public Object visitar(NoOperacaoSubtracao noOperacaoSubtracao) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoSubtracao");
        codigoLogo.append("(").append(noOperacaoSubtracao.toString()).append(")");
        return null;
    }

    @Override
    public Object visitar(NoOperacaoDivisao noOperacaoDivisao) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoDivisao");
        codigoLogo.append("(").append(noOperacaoDivisao.toString()).append(")");
        return null;
    }

    @Override
    public Object visitar(NoOperacaoMultiplicacao noOperacaoMultiplicacao) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoMultiplicacao");
        codigoLogo.append("(").append(noOperacaoMultiplicacao.toString()).append(")");
        return null;
    }

    @Override
    public Object visitar(NoOperacaoModulo noOperacaoModulo) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoModulo");
        codigoLogo.append("(").append(noOperacaoModulo.toString()).append(")");
        return null;
    }

    @Override
    public Object visitar(NoOperacaoBitwiseLeftShift noOperacaoBitwiseLeftShift) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoBitwiseLeftShift");
        //TODO: Adicionar aviso que não é suportado
        return null;
    }

    @Override
    public Object visitar(NoOperacaoBitwiseRightShift noOperacaoBitwiseRightShift) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoBitwiseRightShift");
        //TODO: Adicionar aviso que não é suportado
        return null;
    }

    @Override
    public Object visitar(NoOperacaoBitwiseE noOperacaoBitwiseE) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoBitwiseE");
        //TODO: Adicionar aviso que não é suportado
        return null;
    }

    @Override
    public Object visitar(NoOperacaoBitwiseOu noOperacaoBitwiseOu) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoBitwiseOu");
        //TODO: Adicionar aviso que não é suportado
        return null;
    }

    @Override
    public Object visitar(NoOperacaoBitwiseXOR noOperacaoBitwiseXOR) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoBitwiseXOR");
        //TODO: Adicionar aviso que não é suportado
        return null;
    }

    @Override
    public Object visitar(NoNao noNao) throws ExcecaoVisitaASA {
        System.err.println("noNao");
        codigoLogo.append(" not ");
        if (noNao.getExpressao().estaEntreParenteses()) {
            codigoLogo.append("(");
            noNao.getExpressao().aceitar(this);
            codigoLogo.append(")");
        }else{
            noNao.getExpressao().aceitar(this);
        }
        return null;//super.visitar(noNao); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitar(NoCadeia noCadeia) throws ExcecaoVisitaASA {
        System.err.println("NoCadeia");
        throw new ExcecaoVisitaASA("Não pode utilizar o tipo Cadeia para enviar para GoGoBoard", asa, noCadeia);
        //TODO: Adicionar aviso que não é suportado
    }

    @Override
    public Object visitar(NoCaracter noCaracter) throws ExcecaoVisitaASA {
        System.err.println("NoCaracter");
        //TODO: Adicionar aviso que não é suportado
        return null;
    }

    @Override
    public Object visitar(NoInteiro noInteiro) throws ExcecaoVisitaASA {
        System.err.println("NoInteiro");
        codigoLogo.append(noInteiro.getValor());
        return null;
    }

    @Override
    public Object visitar(NoReferenciaVariavel noReferenciaVariavel) throws ExcecaoVisitaASA {
        codigoLogo.append(noReferenciaVariavel.getNome());
        return null;
    }

    @Override
    public Object visitar(NoChamadaFuncao chamadaFuncao) throws ExcecaoVisitaASA {
        System.err.println("NoChamadaFuncao");
        String identacao = Utils.geraIdentacao(nivelEscopo);
        final List<NoExpressao> parametros = chamadaFuncao.getParametros();

        if ("escreva".equals(chamadaFuncao.getNome())) {
            codigoLogo.append(identacao).append("escreva\n");
        } else if ("acionar_beep".equals(chamadaFuncao.getNome())) {
            codigoLogo.append(identacao).append("beep");
        }

        /*if (parametros != null && !parametros.isEmpty()) {
            for (NoExpressao noExpressao : parametros) {
                noExpressao.aceitar(this);
            }
        }*/
        return null;
    }

    @Override
    public Object visitar(NoEnquanto noEnquanto) throws ExcecaoVisitaASA {
        System.err.println("NoEnquanto");
        String identacao = Utils.geraIdentacao(nivelEscopo);
        codigoLogo.append(identacao).append("repeat ");
        noEnquanto.getCondicao().aceitar(this);
        return null;
    }

    @Override
    public Object visitar(NoPara noPara) throws ExcecaoVisitaASA {
        System.err.println("NoPara");
        String identacao = Utils.geraIdentacao(nivelEscopo);

        String nomeContInternoLaco = montarInicializacaoPara(noPara, identacao);

        noPara.getCondicao().aceitar(this); // Visitar a condição do laço

        codigoLogo.append("\n").append(identacao).append("[\n");
        nivelEscopo++;
        visitarBlocos(noPara.getBlocos());

        adicionaContInternoLaco(noPara, nomeContInternoLaco);
        codigoLogo.append(identacao).append("]\n");
        nivelEscopo--;
        return null;
    }

    private String montarInicializacaoPara(NoPara noPara, String identacao) throws ExcecaoVisitaASA {
        String nomeContInternoLaco = null;
        if (noPara.getInicializacao() != null) {
            // Se for utilizado somente uma variavel como referencia no primeiro valor
            if (noPara.getInicializacao() instanceof NoReferenciaVariavel) {
                NoReferenciaVariavel noReferenciaVariavel = (NoReferenciaVariavel) noPara.getInicializacao();
                NoDeclaracaoVariavel noDeclaracaoVariavel = (NoDeclaracaoVariavel) noReferenciaVariavel.getOrigemDaReferencia();
                nomeContInternoLaco = noDeclaracaoVariavel.getNome();  // Guarda o nome da variavel para criar o contador interno
                codigoLogo.append(identacao).append("repeat ");
            } else // Se for utilizado atribuicao a uma variavel já declarada 
            if (noPara.getInicializacao() instanceof NoOperacaoAtribuicao) {
                NoOperacaoAtribuicao noOperacaoAtribuicao = (NoOperacaoAtribuicao) noPara.getInicializacao();
                nomeContInternoLaco = noOperacaoAtribuicao.getOperandoEsquerdo().toString();
                //noOperacaoAtribuicao.getOperandoDireito().aceitar(this);
                noOperacaoAtribuicao.aceitar(this); // Visita a operação
                codigoLogo.append(identacao).append("repeat ");
            } else // Se for utilizado uma declaraçao de variável
            if (noPara.getInicializacao() instanceof NoDeclaracaoVariavel) {
                NoDeclaracaoVariavel noDeclaracaoVariavel = (NoDeclaracaoVariavel) noPara.getInicializacao();
                nomeContInternoLaco = noDeclaracaoVariavel.getNome();
                noDeclaracaoVariavel.aceitar(this); // Visita a declaração
                codigoLogo.append(identacao).append("repeat ");
            }
        }
        return nomeContInternoLaco;
    }

    private void adicionaContInternoLaco(NoPara noPara, String nomeContInternoLaco) {
        String identacao = Utils.geraIdentacao(nivelEscopo);

        NoOperacao noOp = (NoOperacao) noPara.getIncremento();
        NoExpressao noExp = noOp.getOperandoDireito();

        codigoLogo.append(identacao).append("set ").append(nomeContInternoLaco).append(" (").append(nomeContInternoLaco);
        if (noExp instanceof NoOperacaoSoma) {
            codigoLogo.append(" + 1)\n");
        } else {
            codigoLogo.append(" - 1)\n");
        }
    }

    @Override
    public Object visitar(NoEscolha noEscolha) throws ExcecaoVisitaASA {
        //return super.visitar(noEscolha); //To change body of generated methods, choose Tools | Templates.
        //TODO: Adicionar aviso que não é suportado
        return null;
    }

    @Override
    public Object visitar(NoCaso noCaso) throws ExcecaoVisitaASA {
        //return super.visitar(noCaso); //To change body of generated methods, choose Tools | Templates.
        //TODO: Adicionar aviso que não é suportado
        return null;
    }
}
