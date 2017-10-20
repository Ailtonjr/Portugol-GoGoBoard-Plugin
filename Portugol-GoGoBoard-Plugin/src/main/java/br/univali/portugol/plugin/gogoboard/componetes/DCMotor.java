package br.univali.portugol.plugin.gogoboard.componetes;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.GoGoDriver;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class DCMotor extends Motor {

    public DCMotor(int numMotor) {
        super(numMotor);
    }

    public void ligar() throws ErroExecucaoBiblioteca {
        controlarMotor(numMotor, 1);
        setLigado(true);
    }

    public void desligar() throws ErroExecucaoBiblioteca {
        controlarMotor(numMotor, 0);
        setLigado(false);
    }

    public void inverterDirecao() throws ErroExecucaoBiblioteca {
        if (isDireita()) {
            definirDirecao(1);
            setDireita(false);
        } else {
            definirDirecao(0);
            setDireita(true);
        }
    }

    public void definirDirecao(int direcao) throws ErroExecucaoBiblioteca {
        if (!isLigado()) {
            ligar();
        }
        definirDirecao(numMotor, direcao);
        if (direcao == 1) {
            setDireita(true);
        } else {
            setDireita(false);
        }
    }

    public void setarForca(int forca) throws ErroExecucaoBiblioteca {
        if (isLigado()) {
            definirForca(numMotor, forca);
        }
    }

    private void controlarMotor(int numMotor, int acao) throws ErroExecucaoBiblioteca {
        selecionarMotor(numMotor);
        byte[] cmd = new byte[GoGoDriver.TAMANHO_PACOTE];
        cmd[GoGoDriver.ID_COMANDO] = GoGoDriver.CMD_MOTOR_ACAO;
        cmd[GoGoDriver.PARAMETRO1] = 0;
        cmd[GoGoDriver.PARAMETRO2] = (byte) acao; // 0 = desligado, 1 = ligado
        goGoDriver.enviarComando(cmd);
    }

    public void definirDirecao(int numMotor, int direcao) throws ErroExecucaoBiblioteca {
        selecionarMotor(numMotor);
        byte[] cmd = new byte[GoGoDriver.TAMANHO_PACOTE];
        cmd[GoGoDriver.ID_COMANDO] = GoGoDriver.CMD_MOTOR_DIRECAO;
        cmd[GoGoDriver.PARAMETRO1] = 0;
        cmd[GoGoDriver.PARAMETRO2] = (byte) direcao; // 0 = esquerda, 1 = direita
        goGoDriver.enviarComando(cmd);
    }

    public void definirForca(int numMotor, int forca) throws ErroExecucaoBiblioteca {
        selecionarMotor(numMotor);
        int byteAlto = (forca >> 8);
        int byteBaixo = ((forca & 0xff) & 0xff);

        byte[] cmd = new byte[GoGoDriver.TAMANHO_PACOTE];
        cmd[GoGoDriver.ID_COMANDO] = GoGoDriver.CMD_SET_FORCA;
        cmd[GoGoDriver.PARAMETRO1] = 0;
        cmd[GoGoDriver.PARAMETRO2] = (byte) byteAlto;
        cmd[GoGoDriver.PARAMETRO3] = (byte) byteBaixo;
        goGoDriver.enviarComando(cmd);
    }
}
