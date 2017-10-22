package br.univali.portugol.plugin.gogoboard.gerenciadores;

import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriverBiblioteca;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriverMonitor;

/**
 * Classe que gerencia o a escolha e o retorno de uma instância do driver da 
 * GoGo Board correspondente ao tipo de driver passado por parametro.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
public class GerenciadorDriver {

    private static GoGoDriverBiblioteca driverBiblioteca;
    private static GoGoDriverMonitor driverMonitor;

    /**
     * Metodo para retornar uma instância do driver da GoGo Board correspondente
     * ao tipo de driver passado por parametro.
     *
     * @param tipoDriver Enum referente ao tipo de driver necessário.
     * @return Instância do driver da GoGo Board.
     */
    public static GoGoDriver getGoGoDriver(GoGoDriver.TipoDriver tipoDriver) {
        if (tipoDriver == GoGoDriver.TipoDriver.BIBLIOTECA) {
            if (driverBiblioteca == null) {
                driverBiblioteca = new GoGoDriverBiblioteca();
            }
            return driverBiblioteca;
        } else {
            if (driverMonitor == null) {
                driverMonitor = new GoGoDriverMonitor();
            }
            return driverMonitor;
        }
    }
}
