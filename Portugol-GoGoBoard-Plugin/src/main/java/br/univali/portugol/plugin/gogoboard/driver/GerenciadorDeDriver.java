package br.univali.portugol.plugin.gogoboard.driver;

import br.univali.portugol.plugin.gogoboard.util.UtilGoGoBoard;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class GerenciadorDeDriver {

    private static GoGoDriverBiblioteca driverBiblioteca;
    private static GoGoDriverMonitor driverMonitor;

    /**
     * Metodo para retornar uma instância do driver da GoGo Board correspondente
     * ao tipo de driver.
     *
     * @param tipoDriver Enum referente ao tipo de driver necessário.
     * @return Instância do driver da GoGo Board.
     */
    public static GoGoDriver getGoGoDriver(UtilGoGoBoard.TipoDriver tipoDriver) {
        if (tipoDriver == UtilGoGoBoard.TipoDriver.BIBLIOTECA) {
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
