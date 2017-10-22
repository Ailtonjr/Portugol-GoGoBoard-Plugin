package br.univali.portugol.plugin.gogoboard;

import br.univali.portugol.plugin.gogoboard.acoes.AcaoEnviarByteCode;
import br.univali.portugol.plugin.gogoboard.acoes.AcaoExibirLogo;
import br.univali.portugol.plugin.gogoboard.acoes.AcaoExibirMonitor;
import br.univali.portugol.plugin.gogoboard.biblioteca.GoGoBoard;
import br.univali.ps.plugins.base.Plugin;
import br.univali.ps.plugins.base.UtilizadorPlugins;
import br.univali.ps.plugins.base.VisaoPlugin;

/**
 * Classe principal do plugin.
 *
 * @author Ailton Jr
 * @version 1.0
 */
public final class GoGoBoardPlugin extends Plugin {

    private UtilizadorPlugins utilizador;

    /**
     * Construtor padrão vázio do plugin.
     */
    public GoGoBoardPlugin() {
    }

    @Override
    public VisaoPlugin getVisao() {
        throw new UnsupportedOperationException("O plugin ainda não possui tela para docar na interface.");
    }

    @Override
    protected void inicializar(UtilizadorPlugins utilizador) {
        this.utilizador = utilizador;
        this.utilizador.instalarAcaoPlugin(this, new AcaoExibirMonitor(this));
        this.utilizador.instalarAcaoPlugin(this, new AcaoExibirLogo(this));
        this.utilizador.instalarAcaoPlugin(this, new AcaoEnviarByteCode(this));
        this.utilizador.registrarBiblioteca(GoGoBoard.class);
        super.inicializar(utilizador);
    }

    /**
     * Método para retornar o utilizador de plugin.
     * @return utilizador de plugins.
     */
    public UtilizadorPlugins getUtilizador() {
        return utilizador;
    }
}
