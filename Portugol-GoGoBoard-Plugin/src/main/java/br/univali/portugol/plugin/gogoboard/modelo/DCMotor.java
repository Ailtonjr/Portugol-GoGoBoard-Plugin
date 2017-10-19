package br.univali.portugol.plugin.gogoboard.modelo;

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
        GoGoDriver.obterInstancia().controlarMotor(numMotor, true);
        setLigado(true);
    }

    public void desligar() throws ErroExecucaoBiblioteca {
        GoGoDriver.obterInstancia().controlarMotor(numMotor, false);
        setLigado(false);
    }

    public void inverterDirecao() throws ErroExecucaoBiblioteca {
        GoGoDriver.obterInstancia().inverterDirecaoMotor(numMotor);
        if (isDireita()) {
            setDireita(false);
        } else {
            setDireita(true);
        }
    }

    public void definirDirecao(int direcao) throws ErroExecucaoBiblioteca {
        if (!isLigado()) {
            ligar();
        }
        GoGoDriver.obterInstancia().definirDirecaoMotor(numMotor, direcao);
        if (direcao == 1) {
            setDireita(true);
        } else {
            setDireita(false);
        }
    }

    public void setarForca(int forca) throws ErroExecucaoBiblioteca {
        if (isLigado()) {
            GoGoDriver.obterInstancia().setarForcaMotor(numMotor, forca);
        }
    }
}
