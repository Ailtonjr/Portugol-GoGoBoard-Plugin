package br.univali.portugol.plugin.gogoboard;

import br.univali.portugol.nucleo.asa.ASAPrograma;
import br.univali.portugol.nucleo.asa.ExcecaoVisitaASA;
import br.univali.portugol.nucleo.asa.NoBloco;
import br.univali.portugol.nucleo.asa.NoCadeia;
import br.univali.portugol.nucleo.asa.NoCaracter;
import br.univali.portugol.nucleo.asa.NoChamadaFuncao;
import br.univali.portugol.nucleo.asa.NoDeclaracao;
import br.univali.portugol.nucleo.asa.NoDeclaracaoFuncao;
import br.univali.portugol.nucleo.asa.NoDeclaracaoParametro;
import br.univali.portugol.nucleo.asa.NoDeclaracaoVariavel;
import br.univali.portugol.nucleo.asa.NoEnquanto;
import br.univali.portugol.nucleo.asa.NoExpressao;
import br.univali.portugol.nucleo.asa.NoInclusaoBiblioteca;
import br.univali.portugol.nucleo.asa.NoInteiro;
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

    //private final List<NoDeclaracao> variaveisEncontradas = new ArrayList<>();
    private final ASAPrograma asa;
    private StringBuilder codigoLogo;
    
    private String operacaoLogicaLaco;
    private String nomeContInternoLaco;
    private String contInternoLaco;
    private boolean isLaco;
    private int primeiroValorLaco;
    private int segundoValorLaco;
    private boolean isSegundoValorLaco;
    private boolean isIncrementoLaco;

    public ConversorLogo(ASAPrograma asa) {
        //Exemplo
        /*File arquivoJava = new File("D:\\Documentos\\Desktop\\", "Logo.txt");
        PrintWriter writerArquivoJava = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(arquivoJava), Charset.forName("utf-8"))));
        this.codigoLogo = new PrintWriter(writerArquivoJava);*/
        this.codigoLogo = new StringBuilder();
        this.operacaoLogicaLaco = "";
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
        if (no.getTipoDado().getNome().equalsIgnoreCase("inteiro")) {
            codigoLogo.append("set ").append(no.getNome()).append(" (");
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
        codigoLogo.append("end").append("\n\n");
        return null;
    }

    @Override
    public Object visitar(NoSe noSe) throws ExcecaoVisitaASA {
        System.err.println("Nose");

        if (noSe.getBlocosFalsos() == null) {
            codigoLogo.append("if (");

            noSe.getCondicao().aceitar(this);

            codigoLogo.append(")\n").append("[\n");

            visitarBlocos(noSe.getBlocosVerdadeiros());

            codigoLogo.append("\n]\n");
        } else {
            codigoLogo.append("ifelse (");

            noSe.getCondicao().aceitar(this);

            codigoLogo.append(")\n").append("[\n");

            visitarBlocos(noSe.getBlocosVerdadeiros());

            codigoLogo.append("\n] [\n");

            visitarBlocos(noSe.getBlocosFalsos());

            codigoLogo.append("\n]\n");
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

    private Object visitarOperacao(NoOperacao operacao) throws ExcecaoVisitaASA {
        System.err.println("NoOperacao, Esquerdo: " + operacao.getOperandoEsquerdo().toString() + ", Direito: " + operacao.getOperandoDireito().toString());
        String op = operacao.getOperandoDireito().toString();

        if (isLaco && isIncrementoLaco) {    // Se for um laço e estiver tratando uma operação de incremento/decremento

            if (op.substring(op.length() - 3).equals("- 1")) {    //Pega o final da String para confirmar se é um incremento mesmo ou não
                isIncrementoLaco = false;   // É um decremento
            }
            montarLacoPara();
        }
        operacao.getOperandoEsquerdo().aceitar(this);

        operacao.getOperandoDireito().aceitar(this);
        return null;
    }

    @Override
    public Object visitar(NoOperacaoLogicaIgualdade noOperacaoLogicaIgualdade) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoLogicaIgualdade");
        operacaoLogicaLaco = "==";    //Usado para identificar a operação em laços de repetições
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
        return visitarOperacao(noOperacaoLogicaDiferenca);
    }

    @Override
    public Object visitar(NoOperacaoAtribuicao noOperacaoAtribuicao) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoAtribuicao");
        return visitarOperacao(noOperacaoAtribuicao);
    }

    @Override
    public Object visitar(NoOperacaoLogicaE noOperacaoLogicaE) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoLogicaE");
        return visitarOperacao(noOperacaoLogicaE);
    }

    @Override
    public Object visitar(NoOperacaoLogicaOU noOperacaoLogicaOU) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoLogicaOU");
        return visitarOperacao(noOperacaoLogicaOU);
    }

    @Override
    public Object visitar(NoOperacaoLogicaMaior noOperacaoLogicaMaior) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoLogicaMaior");
        operacaoLogicaLaco = ">";    //Usado para identificar a operação em laços de repetições
        return visitarOperacao(noOperacaoLogicaMaior);
    }

    @Override
    public Object visitar(NoOperacaoLogicaMaiorIgual noOperacaoLogicaMaiorIgual) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoLogicaMaiorIgual");
        operacaoLogicaLaco = ">=";    //Usado para identificar a operação em laços de repetições
        return visitarOperacao(noOperacaoLogicaMaiorIgual);
    }

    @Override
    public Object visitar(NoOperacaoLogicaMenor noOperacaoLogicaMenor) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoLogicaMenor");
        operacaoLogicaLaco = "<";    //Usado para identificar a operação em laços de repetições
        return visitarOperacao(noOperacaoLogicaMenor);
    }

    @Override
    public Object visitar(NoOperacaoLogicaMenorIgual noOperacaoLogicaMenorIgual) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoLogicaMenorIgual");
        operacaoLogicaLaco = "<=";    //Usado para identificar a operação em laços de repetições
        return visitarOperacao(noOperacaoLogicaMenorIgual);
    }

    @Override
    public Object visitar(NoOperacaoSoma noOperacaoSoma) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoSoma");
        return visitarOperacao(noOperacaoSoma);
    }

    @Override
    public Object visitar(NoOperacaoSubtracao noOperacaoSubtracao) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoSubtracao");
        return visitarOperacao(noOperacaoSubtracao);
    }

    @Override
    public Object visitar(NoOperacaoDivisao noOperacaoDivisao) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoDivisao");
        return visitarOperacao(noOperacaoDivisao);
    }

    @Override
    public Object visitar(NoOperacaoMultiplicacao noOperacaoMultiplicacao) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoMultiplicacao");
        return visitarOperacao(noOperacaoMultiplicacao);
    }

    @Override
    public Object visitar(NoOperacaoModulo noOperacaoModulo) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoModulo");
        return visitarOperacao(noOperacaoModulo);
    }

    @Override
    public Object visitar(NoOperacaoBitwiseLeftShift noOperacaoBitwiseLeftShift) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoBitwiseLeftShift");
        return visitarOperacao(noOperacaoBitwiseLeftShift);
    }

    @Override
    public Object visitar(NoOperacaoBitwiseRightShift noOperacaoBitwiseRightShift) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoBitwiseRightShift");
        return visitarOperacao(noOperacaoBitwiseRightShift);
    }

    @Override
    public Object visitar(NoOperacaoBitwiseE noOperacaoBitwiseE) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoBitwiseE");
        return visitarOperacao(noOperacaoBitwiseE);
    }

    @Override
    public Object visitar(NoOperacaoBitwiseOu noOperacaoBitwiseOu) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoBitwiseOu");
        return visitarOperacao(noOperacaoBitwiseOu);
    }

    @Override
    public Object visitar(NoOperacaoBitwiseXOR noOperacaoBitwiseXOR) throws ExcecaoVisitaASA {
        System.err.println("NoOperacaoBitwiseXOR");
        return visitarOperacao(noOperacaoBitwiseXOR);
    }

    @Override
    public Object visitar(NoCadeia noCadeia) throws ExcecaoVisitaASA {
        System.err.println("NoCadeia");
        throw new ExcecaoVisitaASA("Não pode utilizar o tipo Cadeia para enviar para GoGoBoard", asa, noCadeia);
        //return null;
    }

    @Override
    public Object visitar(NoCaracter noCaracter) throws ExcecaoVisitaASA {
        System.err.println("NoCaracter");
        return null;
    }

    @Override
    public Object visitar(NoInteiro noInteiro) throws ExcecaoVisitaASA {
        System.err.println("NoInteiro");
        if (isLaco) {   // Se for um laço, tratar de maneira diferente
            if (!isSegundoValorLaco) {
                primeiroValorLaco = noInteiro.getValor();
                isSegundoValorLaco = true;
            } else {
                segundoValorLaco = noInteiro.getValor();
            }
        } else {    // Se não for, adiciona o inteiro no código
            codigoLogo.append(noInteiro.getValor());
        }
        return null;
    }

    @Override
    public Object visitar(NoChamadaFuncao chamadaFuncao) throws ExcecaoVisitaASA {
        System.err.println("NoChamadaFuncao");

        final List<NoExpressao> parametros = chamadaFuncao.getParametros();

        if ("escreva".equals(chamadaFuncao.getNome())) {
            codigoLogo.append("escreva\n");
        } else if ("acionar_beep".equals(chamadaFuncao.getNome())) {
            codigoLogo.append("beep");
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
        return super.visitar(noEnquanto); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitar(NoPara noPara) throws ExcecaoVisitaASA {
        System.err.println("noPara");
        isLaco = true;

        NoBloco inicializacao = noPara.getInicializacao();
        NoReferenciaVariavel noReferenciaVariavel = (NoReferenciaVariavel) inicializacao;
        NoDeclaracaoVariavel noDeclaracaoVariavel = (NoDeclaracaoVariavel) noReferenciaVariavel.getOrigemDaReferencia();
        nomeContInternoLaco = noDeclaracaoVariavel.getNome();

        // Se for utilizado uma variavel como referencia no primeiro valor
        if (inicializacao != null && (inicializacao instanceof NoReferenciaVariavel)) {
            noDeclaracaoVariavel.getInicializacao().aceitar(this); // Pega somente o valor da variavel
        }
        inicializacao.aceitar(this);

        noPara.getCondicao().aceitar(this);
        if (noPara.getIncremento() != null) {
            isIncrementoLaco = true;
            noPara.getIncremento().aceitar(this);
        }
        isLaco = false;
        for (NoBloco bloco : noPara.getBlocos()) {
            bloco.aceitar(this);
        }
        
        
        codigoLogo.append(contInternoLaco); // Adiciona o contator interno pronto
        codigoLogo.append("\n]\n");
        return null;
    }

    private void montarLacoPara() {
        codigoLogo.append("\nrepeat ");

        if (operacaoLogicaLaco.equals(">=") || operacaoLogicaLaco.equals(">")) {
            if (isIncrementoLaco) {
                if (primeiroValorLaco < segundoValorLaco) {
                    codigoLogo.append(0);   // usuário errou na operação lógica, portanto não vai passar nenhuma vez Ex: 1>10 ++
                } else {
                    codigoLogo.append(Integer.MAX_VALUE);  // usuário errou na operação lógica (loop infinito), maior numero inteiro
                }
            } else {
                if (primeiroValorLaco > segundoValorLaco) {
                    if (operacaoLogicaLaco.equals(">")) {
                        codigoLogo.append(primeiroValorLaco - segundoValorLaco);
                    } else {
                        codigoLogo.append(((primeiroValorLaco - segundoValorLaco) + 1));
                    }
                } else {
                    codigoLogo.append(0);   // usuário errou na operação lógica, portanto não vai passar nenhuma vez
                }
            }
        } else if (operacaoLogicaLaco.equals("<=") || operacaoLogicaLaco.equals("<")) {
            if (isIncrementoLaco) {
                if (primeiroValorLaco < segundoValorLaco) {
                    if (operacaoLogicaLaco.equals("<")) {
                        codigoLogo.append((segundoValorLaco - primeiroValorLaco));
                    } else {
                        codigoLogo.append(((segundoValorLaco - primeiroValorLaco) + 1));
                    }
                } else {
                    codigoLogo.append(0);   // usuário errou na operação lógica, portanto não vai passar nenhuma vez
                }
            } else {
                if (primeiroValorLaco > segundoValorLaco) {
                    codigoLogo.append(0);   // usuário errou na operação lógica (loop infinito negativo), portanto não vai passar nenhuma vez
                } else {
                    codigoLogo.append(Integer.MAX_VALUE);   // usuário errou na operação lógica (loop infinito), maior numero inteiro
                }
            }
        }

        codigoLogo.append("\n[\n");
        // Cria um contator para controle interno do laco que será inserido no código no "Visita noPara"
        if (isIncrementoLaco) {
            contInternoLaco = ("set " + nomeContInternoLaco + " (" + nomeContInternoLaco + " + 1)");
        } else {
            contInternoLaco = ("set " + nomeContInternoLaco + " (" + nomeContInternoLaco + " - 1)");
        }
        
        //Reseta para montar um novo código Para
        operacaoLogicaLaco = "";
        isIncrementoLaco = false;
        isSegundoValorLaco = false;
    }

}
