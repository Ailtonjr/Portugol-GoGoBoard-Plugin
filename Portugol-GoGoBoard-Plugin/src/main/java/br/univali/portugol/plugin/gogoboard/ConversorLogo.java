/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.univali.portugol.plugin.gogoboard;

import br.univali.portugol.nucleo.asa.ASAPrograma;
import br.univali.portugol.nucleo.asa.ExcecaoVisitaASA;
import br.univali.portugol.nucleo.asa.NoBitwiseNao;
import br.univali.portugol.nucleo.asa.NoBloco;
import br.univali.portugol.nucleo.asa.NoCadeia;
import br.univali.portugol.nucleo.asa.NoDeclaracao;
import br.univali.portugol.nucleo.asa.NoDeclaracaoFuncao;
import br.univali.portugol.nucleo.asa.NoDeclaracaoMatriz;
import br.univali.portugol.nucleo.asa.NoDeclaracaoParametro;
import br.univali.portugol.nucleo.asa.NoDeclaracaoVariavel;
import br.univali.portugol.nucleo.asa.NoDeclaracaoVetor;
import br.univali.portugol.nucleo.asa.NoEnquanto;
import br.univali.portugol.nucleo.asa.NoEscolha;
import br.univali.portugol.nucleo.asa.NoInclusaoBiblioteca;
import br.univali.portugol.nucleo.asa.NoNao;
import br.univali.portugol.nucleo.asa.NoSe;
import br.univali.portugol.nucleo.asa.VisitanteASABasico;
import br.univali.portugol.nucleo.asa.VisitanteNulo;
import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import jdk.nashorn.internal.runtime.Context;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class ConversorLogo extends VisitanteNulo {

    //private final List<NoDeclaracao> variaveisEncontradas = new ArrayList<>();
    private final ASAPrograma asa;
    private StringBuilder codigoLogo = new StringBuilder();

    public ConversorLogo(ASAPrograma asa) {
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
        if (!noInclusaoBiblioteca.getNome().equalsIgnoreCase("Util")) {
            System.out.println("Biblioteca encontrada: " + noInclusaoBiblioteca.getNome());
            JOptionPane.showMessageDialog(null, "O programa contém bibliotecas que não podem ser enviadas para a GoGoBoard!\n"
                    + noInclusaoBiblioteca.getNome() + "[" + noInclusaoBiblioteca.getTrechoCodigoFonte().getLinha() + "," + noInclusaoBiblioteca.getTrechoCodigoFonte().getColuna() + "]", "Erro!", JOptionPane.INFORMATION_MESSAGE);
            //throw new ExcecaoVisitaASA("O programa contém bibliotecas que não podem ser enviadas para a GoGoBoard!\n"
             //       + noInclusaoBiblioteca.getNome() + "[" + noInclusaoBiblioteca.getTrechoCodigoFonte().getLinha() + "," + noInclusaoBiblioteca.getTrechoCodigoFonte().getColuna() + "]", asa, noInclusaoBiblioteca);
        }
        return null;
    }

    @Override
    public Object visitar(NoDeclaracaoMatriz no) throws ExcecaoVisitaASA {
        //adicionarNaLista(no);

        return null;
    }

    @Override
    public Object visitar(NoDeclaracaoParametro noDeclaracaoParametro) throws ExcecaoVisitaASA {
        //adicionarNaLista(noDeclaracaoParametro);

        return null;
    }

    @Override
    public Object visitar(NoDeclaracaoVariavel no) throws ExcecaoVisitaASA {
        //adicionarNaLista(no);

        return null;
    }

    @Override
    public Object visitar(NoDeclaracaoVetor no) throws ExcecaoVisitaASA {
        //adicionarNaLista(no);

        return null;
    }

    
    @Override
    public Object visitar(NoDeclaracaoFuncao declaracaoFuncao) throws ExcecaoVisitaASA {
        
        codigoLogo.append("to " + declaracaoFuncao.getNome() + "\n");
        for (NoDeclaracaoParametro no : declaracaoFuncao.getParametros()) {
            no.aceitar(this);
        }

        for (NoBloco bloco : declaracaoFuncao.getBlocos()) {
            bloco.aceitar(this);
        }

        return null;
    }

    
    
    @Override
    public Object visitar(NoEnquanto noEnquanto) throws ExcecaoVisitaASA {

        for (NoBloco no : noEnquanto.getBlocos()) {
            no.aceitar(this);
        }

        return null;
    }

    @Override
    public Object visitar(NoSe noSe) throws ExcecaoVisitaASA {
        codigoLogo.append("if ");
        
        for (NoBloco blocosVerdadeiro : noSe.getBlocosFalsos()) {
            blocosVerdadeiro.aceitar(this);
        }
        System.out.println("NoSe");
        JOptionPane.showMessageDialog(null, "NoSe");
        return null;
    }
}
