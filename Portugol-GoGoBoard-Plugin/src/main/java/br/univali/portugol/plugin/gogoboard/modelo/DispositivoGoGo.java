package br.univali.portugol.plugin.gogoboard.modelo;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class DispositivoGoGo {

    private List<Sensor> sensores;
    private List<DCMotor> motoresDC;
    private List<ServoMotor> motoresServo;

    private Led ledUsuario;
    private Buzzer buzzer;
    private Display display;
    private Infravermelho infravermelho;

    private AtualizadorComponentes atualizador;

    public DispositivoGoGo() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        sensores = new ArrayList<Sensor>() {
            {
                add(new Sensor(0));
                add(new Sensor(1));
                add(new Sensor(2));
                add(new Sensor(3));
                add(new Sensor(4));
                add(new Sensor(5));
                add(new Sensor(6));
                add(new Sensor(7));
            }
        };

        motoresDC = new ArrayList<DCMotor>() {
            {
                add(new DCMotor(1));
                add(new DCMotor(2));
                add(new DCMotor(4));
                add(new DCMotor(8));
            }
        };

        motoresServo = new ArrayList<ServoMotor>() {
            {
                add(new ServoMotor(1));
                add(new ServoMotor(2));
                add(new ServoMotor(4));
                add(new ServoMotor(8));
            }
        };

        ledUsuario = new Led(0);
        buzzer = new Buzzer();
        display = new Display();
        infravermelho = new Infravermelho();

        atualizador = new AtualizadorComponentes(sensores, infravermelho);
    }

    public int getValorSensor(int numSensor, boolean atualizar) throws ErroExecucaoBiblioteca {
        Sensor sensor = sensores.get(numSensor);
        if (atualizar) {
            sensor.atualizaValor();
        }
        return sensor.getValor();
    }

    public void ligarMotor(int indice) throws ErroExecucaoBiblioteca {
        motoresDC.get(indice).ligar();
    }

    public void desligarMotor(int indice) throws ErroExecucaoBiblioteca {
        motoresDC.get(indice).desligar();
    }

    public void inverterDirecaoMotor(int indice) throws ErroExecucaoBiblioteca {
        motoresDC.get(indice).inverterDirecao();
    }

    public void definirDirecaoMotor(int indice, int direcao) throws ErroExecucaoBiblioteca {
        motoresDC.get(indice).definirDirecao(direcao);
    }

    public void definirForcaMotor(int indice, int forca) throws ErroExecucaoBiblioteca {
        motoresDC.get(indice).setarForca(forca);
    }

    public boolean getEstadoMotor(int indice) throws ErroExecucaoBiblioteca {
        return motoresDC.get(indice).isLigado();
    }

    public void acionarBeep() throws ErroExecucaoBiblioteca {
        buzzer.acionarBeep();
    }

    public void controlarLed(int acao) throws ErroExecucaoBiblioteca {
        ledUsuario.controlarLed(acao);
    }

    public void exibirTextoCurto(String texto) throws ErroExecucaoBiblioteca {
        display.exibirTextoCurto(texto);
    }
    
    public int getValorRecebidoIR(){
        return infravermelho.getValorRecebido();
    }
    
    public void atualizarComponetes() throws ErroExecucaoBiblioteca {
        atualizador.atualizar();
    }
}
