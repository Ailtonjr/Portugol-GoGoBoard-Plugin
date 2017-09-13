package br.univali.portugol.plugin.gogoboard;

import br.univali.portugol.plugin.gogoboard.acoes.AcaoCompilarLogo;
import br.univali.ps.plugins.base.Plugin;
import br.univali.ps.plugins.base.UtilizadorPlugins;
import br.univali.ps.plugins.base.VisaoPlugin;

/**
 *
 * @author Ailton Jr
 */
public class GoGoBoardPlugin extends Plugin{
    
    private UtilizadorPlugins utilizador;

    @Override
    public VisaoPlugin getVisao() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void inicializar(UtilizadorPlugins utilizador) {
        this.utilizador = utilizador;
        this.utilizador.instalarAcaoPlugin(this, new AcaoCompilarLogo());
        super.inicializar(utilizador); //To change body of generated methods, choose Tools | Templates.
    }
}
