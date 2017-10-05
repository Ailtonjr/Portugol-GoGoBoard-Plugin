package br.univali.portugol.plugin.gogoboard.modelo;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class DCMotor extends Motor {

    public DCMotor(int numMotor) throws ErroExecucaoBiblioteca {
        super(numMotor);
    }

    public void ligar() throws ErroExecucaoBiblioteca {
        selecionarMotor();
        byte[] cmd = new byte[gogoDriver.TAMANHO_PACOTE];
        cmd[gogoDriver.ID_COMANDO] = gogoDriver.CMD_MOTOR_ACAO;
        cmd[gogoDriver.PARAMETRO1] = 0;
        cmd[gogoDriver.PARAMETRO2] = 1;
        gogoDriver.enviarComando(cmd);
        setLigado(true);
    }

    public void desligar() throws ErroExecucaoBiblioteca {
        selecionarMotor();
        byte[] cmd = new byte[gogoDriver.TAMANHO_PACOTE];
        cmd[gogoDriver.ID_COMANDO] = gogoDriver.CMD_MOTOR_ACAO;
        cmd[gogoDriver.PARAMETRO1] = 0;
        cmd[gogoDriver.PARAMETRO2] = 0;
        gogoDriver.enviarComando(cmd);
        setLigado(false);
    }

    public void inverterDirecao() throws ErroExecucaoBiblioteca {
        selecionarMotor();
        byte[] cmd = new byte[gogoDriver.TAMANHO_PACOTE];
        cmd[gogoDriver.ID_COMANDO] = gogoDriver.CMD_MOTOR_REV_DIRECAO;
        cmd[gogoDriver.PARAMETRO1] = 0;
        gogoDriver.enviarComando(cmd);
        if (isDireita()) {
            setDireita(false);
        } else {
            setDireita(true);
        }
    }

    public void definirDirecao(int direcao) throws ErroExecucaoBiblioteca {
        selecionarMotor();
        if (!isLigado()) {
            ligar();
        }
        byte[] cmd = new byte[gogoDriver.TAMANHO_PACOTE];
        cmd[gogoDriver.ID_COMANDO] = gogoDriver.CMD_MOTOR_DIRECAO;
        cmd[gogoDriver.PARAMETRO1] = 0;
        cmd[gogoDriver.PARAMETRO2] = (byte) direcao;
        gogoDriver.enviarComando(cmd);
        if (direcao == 1) {
            setDireita(true);
        } else {
            setDireita(false);
        }
    }

    public void setarForca(int forca) throws ErroExecucaoBiblioteca {
        if (isLigado()) {
            selecionarMotor();
            byte num1 = (byte) ((byte) forca << 8);
            byte num2 = (byte) ((forca & 0xff) & 0xff);

            byte[] cmd = new byte[gogoDriver.TAMANHO_PACOTE];
            cmd[gogoDriver.ID_COMANDO] = gogoDriver.CMD_SET_FORCA;
            cmd[gogoDriver.PARAMETRO1] = 0;
            cmd[gogoDriver.PARAMETRO2] = num1;
            cmd[gogoDriver.PARAMETRO3] = num2;
            gogoDriver.enviarComando(cmd);
        }
    }
}
