package br.univali.portugol.plugin.gogoboard.gerenciadores;

import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriverCompartilhado;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriverExclusivo;

/**
 * Classe que gerencia o a escolha e o retorno de uma instância do driver da 
 * GoGo Board correspondente ao tipo de driver passado por parametro.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
public class GerenciadorDriver {

    private static GoGoDriverCompartilhado driverBiblioteca;
    private static GoGoDriverExclusivo driverMonitor;

    /**
     * Metodo para retornar uma instância do driver da GoGo Board correspondente
     * ao tipo de driver passado por parametro.
     *
     * @param tipoDriver Enum referente ao tipo de driver necessário.
     * @return Instância do driver da GoGo Board.
     */
    public static GoGoDriver getGoGoDriver(GoGoDriver.TIPODRIVER tipoDriver) {
        if (tipoDriver == GoGoDriver.TIPODRIVER.COMPARTILHADO) {
            if (driverBiblioteca == null) {
                driverBiblioteca = new GoGoDriverCompartilhado();
            }
            return driverBiblioteca;
        } else {
            if (driverMonitor == null) {
                driverMonitor = new GoGoDriverExclusivo();
            }
            return driverMonitor;
        }
    }
}
