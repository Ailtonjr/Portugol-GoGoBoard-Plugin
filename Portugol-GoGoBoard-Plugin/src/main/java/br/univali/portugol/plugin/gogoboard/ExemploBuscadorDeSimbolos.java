/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.univali.portugol.plugin.gogoboard;

import br.univali.portugol.nucleo.asa.ASAPrograma;
import br.univali.portugol.nucleo.asa.ExcecaoVisitaASA;
import br.univali.portugol.nucleo.asa.NoBloco;
import br.univali.portugol.nucleo.asa.NoDeclaracao;
import br.univali.portugol.nucleo.asa.NoDeclaracaoFuncao;
import br.univali.portugol.nucleo.asa.NoDeclaracaoMatriz;
import br.univali.portugol.nucleo.asa.NoDeclaracaoParametro;
import br.univali.portugol.nucleo.asa.NoDeclaracaoVariavel;
import br.univali.portugol.nucleo.asa.NoDeclaracaoVetor;
import br.univali.portugol.nucleo.asa.NoEnquanto;
import br.univali.portugol.nucleo.asa.VisitanteASABasico;
import br.univali.portugol.nucleo.asa.VisitanteNulo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class ExemploBuscadorDeSimbolos extends VisitanteNulo {
    private final String padrao;
    private final List<NoDeclaracao> variaveisEncontradas = new ArrayList<>();
    private final ASAPrograma asa;
    
    public ExemploBuscadorDeSimbolos(String padrao, ASAPrograma asa) {
        this.padrao = padrao;
        this.asa = asa;
        
    }
    
    public List<NoDeclaracao> buscar() throws ExcecaoVisitaASA {
        asa.aceitar(this);
        return variaveisEncontradas;
    }

    private void adicionarNaLista(NoDeclaracao declaracao){
        if (declaracao.getNome().toLowerCase().startsWith(padrao.toLowerCase())){
                variaveisEncontradas.add(declaracao);
            }
    }
    
    @Override
    public Object visitar(ASAPrograma asap) throws ExcecaoVisitaASA {
        
        for (NoDeclaracao declaracao : asap.getListaDeclaracoesGlobais()){
            declaracao.aceitar(this);
        }
        
        return null;
    }

    @Override
    public Object visitar(NoDeclaracaoMatriz no) throws ExcecaoVisitaASA {
        adicionarNaLista(no);
        
        return null;
    }

    @Override
    public Object visitar(NoDeclaracaoParametro noDeclaracaoParametro) throws ExcecaoVisitaASA {
        adicionarNaLista(noDeclaracaoParametro);
        
        return null;
    }

    @Override
    public Object visitar(NoDeclaracaoVariavel no) throws ExcecaoVisitaASA {
        adicionarNaLista(no);
        
        return null;
    }

    @Override
    public Object visitar(NoDeclaracaoVetor no) throws ExcecaoVisitaASA {
        adicionarNaLista(no);
        
        return null;
    }

    @Override
    public Object visitar(NoDeclaracaoFuncao declaracaoFuncao) throws ExcecaoVisitaASA {
        
        adicionarNaLista(declaracaoFuncao);
        
        for (NoDeclaracaoParametro no: declaracaoFuncao.getParametros()){
            no.aceitar(this);
        }
        
        for (NoBloco bloco : declaracaoFuncao.getBlocos()){
            bloco.aceitar(this);
        }
        
        return null;
    }

    @Override
    public Object visitar(NoEnquanto noEnquanto) throws ExcecaoVisitaASA {
        
        for (NoBloco no : noEnquanto.getBlocos()){
            no.aceitar(this);
        }
        
        return null;
    }
    
    
}
