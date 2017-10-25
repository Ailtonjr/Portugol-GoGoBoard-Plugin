package br.univali.portugol.plugin.gogoboard.componetes;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;

/**
 * Classe que representa o Motor Servo, extendendo Motor.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
public class MotorServo extends Motor {

    private int posicao = 10;

    /**
     * Construtor padrão do motor servo.
     *
     * @param idMotor Inteiro correspondente ao ID do motor, A = 1, B = 2, C = 4
     * e D = 8.
     * @param tipoDriver Enum referente ao tipo de driver necessário.
     * @see Motor
     */
    public MotorServo(int idMotor, GoGoDriver.TIPODRIVER tipoDriver) {
        super(idMotor, tipoDriver);
    }

    public int getPosicao() {
        return posicao;
    }

    /**
     * Método para setar a posição do motor servo.
     *
     * @param posicao O motor servo aceita valores maiores que 10 e menores ou
     * igual a 40, qualquer outro valor fora deste intervalo, será convertido
     * automaticamente.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public void setPosicao(int posicao) throws ErroExecucaoBiblioteca {
        if (posicao < 10) {
            posicao = 10;
        } else if (posicao > 40) {
            posicao = 40;
        }
        controlarPosicao(posicao);
        this.posicao = posicao;
    }

    /**
     * Método privado para controlar a posição do motor.
     *
     * @param posicao Inteiro correspondente posição do motor.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    private void controlarPosicao(int posicao) throws ErroExecucaoBiblioteca {
        selecionar();
        int byteAlto = (posicao >> 8);
        int byteBaixo = (posicao & 0xff);

        byte[] cmd = new byte[GoGoDriver.TAMANHO_PACOTE];
        cmd[GoGoDriver.ID_COMANDO] = GoGoDriver.CMD_SET_SERVO_POSICAO;
        cmd[GoGoDriver.PARAMETRO1] = 0;
        cmd[GoGoDriver.PARAMETRO2] = (byte) byteAlto;
        cmd[GoGoDriver.PARAMETRO3] = (byte) byteBaixo;
        goGoDriver.enviarComando(cmd);
    }
}
