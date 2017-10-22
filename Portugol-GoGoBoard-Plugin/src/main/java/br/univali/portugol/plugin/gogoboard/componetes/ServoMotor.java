package br.univali.portugol.plugin.gogoboard.componetes;

import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;

/**
 * Classe que representa o Motor Servo, extendendo Motor.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
public class ServoMotor extends Motor {

    /**
     * Construtor padrão do motor servo.
     *
     * @param numMotor Número correspondente ao índice do motor, iniciando em 0.
     * @param tipoDriver Enum referente ao tipo de driver necessário.
     * @see Motor
     */
    public ServoMotor(int numMotor, GoGoDriver.TIPODRIVER tipoDriver) {
        super(numMotor, tipoDriver);
    }

}
