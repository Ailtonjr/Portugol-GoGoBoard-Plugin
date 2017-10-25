package br.univali.portugol.plugin.gogoboard.componetes;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.componetes.modulos.DisplayLCD;
import br.univali.portugol.plugin.gogoboard.componetes.modulos.Relogio;
import br.univali.portugol.plugin.gogoboard.gerenciadores.GerenciadorDriver;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hid4java.HidServicesListener;
import org.hid4java.event.HidServicesEvent;

/**
 * Classe que representa o dispositivo GoGo Board com os seus componentes.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
public class DispositivoGoGo implements HidServicesListener {

    private List<Sensor> sensores;
    private List<MotorCD> motoresDC;
    //private List<ServoMotor> motoresServo;
    Map<Character, MotorServo> motoresServo;

    private Led ledUsuario;
    private Buzzer buzzer;
    private Display display;
    private Infravermelho infravermelho;
    //Módulos externos
    private DisplayLCD moduloDisplayLCD;
    private Relogio moduloRelogio;

    private AtualizadorComponentes atualizador;
    private GoGoDriver goGoDriver;
    private boolean conectado;
    private final GoGoDriver.TIPODRIVER tipoDriver;

    /**
     * Construtor padrão do dispositivo GoGo Board.
     *
     * @param tipoDriver Enum referente ao tipo de driver necessário.
     */
    public DispositivoGoGo(GoGoDriver.TIPODRIVER tipoDriver) {
        this.tipoDriver = tipoDriver;
        inicializarComponentes();
    }

    /**
     * Método para inicializar os componentes da GoGoBoard.
     */
    private void inicializarComponentes() {
        sensores = new ArrayList<Sensor>() {
            {
                add(new Sensor(0, tipoDriver));
                add(new Sensor(1, tipoDriver));
                add(new Sensor(2, tipoDriver));
                add(new Sensor(3, tipoDriver));
                add(new Sensor(4, tipoDriver));
                add(new Sensor(5, tipoDriver));
                add(new Sensor(6, tipoDriver));
                add(new Sensor(7, tipoDriver));
            }
        };

        motoresDC = new ArrayList<MotorCD>() {
            {
                add(new MotorCD(1, tipoDriver));
                add(new MotorCD(2, tipoDriver));
                add(new MotorCD(4, tipoDriver));
                add(new MotorCD(8, tipoDriver));
            }
        };

        /*motoresServo = new ArrayList<MotorServo>() {
            {
                add(new MotorServo(1, tipoDriver));
                add(new MotorServo(2, tipoDriver));
                add(new MotorServo(4, tipoDriver));
                add(new MotorServo(8, tipoDriver));
            }
        };*/
        motoresServo = new HashMap<Character, MotorServo>() {
            {
                put('a', new MotorServo(1, tipoDriver));
                put('b', new MotorServo(2, tipoDriver));
                put('c', new MotorServo(4, tipoDriver));
                put('d', new MotorServo(8, tipoDriver));
            }
        };

        ledUsuario = new Led(0, tipoDriver);
        buzzer = new Buzzer(tipoDriver);
        display = new Display(tipoDriver);
        infravermelho = new Infravermelho(tipoDriver);

        //Módulos externos
        moduloDisplayLCD = new DisplayLCD(tipoDriver);
        moduloRelogio = new Relogio(tipoDriver);

        atualizador = new AtualizadorComponentes(sensores, infravermelho, tipoDriver);
        goGoDriver = GerenciadorDriver.getGoGoDriver(tipoDriver);
        addServiceListener(this);
    }

    /**
     * Método para obter o valor de um sensor.
     *
     * @param idSensor Número correspondete ao sensor que retornará o valor.
     * @param atualizar Boleano para indicar se é necessário atualizar o valor
     * antes de retornar.
     * @return Valor do sensor.
     *
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public int getValorSensor(int idSensor, boolean atualizar) throws ErroExecucaoBiblioteca {
        Sensor sensor = sensores.get(idSensor);
        return sensor.getValor(atualizar);
    }

    public MotorCD getMotorDC(int indice) {
        return motoresDC.get(indice);
    }

    /**
     * Método para chamar o metodo ligar do componente motor DC.
     *
     * @param indice Indice do motor que será ligado.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
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
        motoresDC.get(indice).definirForca(forca);
    }

    public boolean getEstadoMotor(int indice) throws ErroExecucaoBiblioteca {
        return motoresDC.get(indice).isLigado();
    }

    public void acionarBeep() throws ErroExecucaoBiblioteca {
        buzzer.acionarBuzzer();
    }

    public void controlarLed(int acao) throws ErroExecucaoBiblioteca {
        ledUsuario.controlarLed(acao);
    }

    public void exibirTexto(String texto) throws ErroExecucaoBiblioteca {
        display.exibirTexto(texto);
    }

    public void exibirNumero(int numero) throws ErroExecucaoBiblioteca {
        display.exibirNumero(numero);
    }

    public void exibirTextoLCD(String texto) throws ErroExecucaoBiblioteca {
        moduloDisplayLCD.exibirTexto(texto);
    }

    public void exibirNumeroLCD(int numero) throws ErroExecucaoBiblioteca {
        moduloDisplayLCD.exibirNumero(numero);
    }

    public void limparDisplayLCD() throws ErroExecucaoBiblioteca {
        moduloDisplayLCD.limparTela();
    }

    public int getValorIR(boolean atualizar) throws ErroExecucaoBiblioteca {
        return infravermelho.getValor(atualizar);
    }

    /**
     * Método para retornar o HashMap dos motores servos.
     *
     * @return HashMap dos motores servos.
     */
    public Map<Character, MotorServo> getMotoresServo() {
        return motoresServo;
    }

    /**
     * Método para retornar o componente módulo relógio.
     *
     * @return Componente módulo relógio.
     */
    public Relogio getModuloRelogio() {
        return moduloRelogio;
    }

    /**
     * Método para atualizar os componentes da GoGoBoard (inputs/outputs)
     *
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public void atualizarComponetes() throws ErroExecucaoBiblioteca {
        atualizador.atualizar();
    }

    /**
     * Método para adicionar um listener ao serviço HID.
     *
     * @param listener Objeto que implemente a interface HidServicesListener.
     */
    public void addServiceListener(HidServicesListener listener) {
        goGoDriver.addHidServicesListener(listener);
    }

    public boolean isConectado() {
        if (!conectado) {
            conectado = verificaConexao();
        }
        return conectado;
    }

    private boolean verificaConexao() {
        try {
            atualizarComponetes();
            return true;
        } catch (ErroExecucaoBiblioteca ex) {
            return false;
        }
    }

    @Override
    public void hidDeviceAttached(HidServicesEvent hse) {
        if (hse.getHidDevice().getVendorId() == GoGoDriver.VENDOR_ID
                && hse.getHidDevice().getProductId() == GoGoDriver.PRODUCT_ID) {
            conectado = verificaConexao();
        }
    }

    @Override
    public void hidDeviceDetached(HidServicesEvent hse) {
        if (hse.getHidDevice().getVendorId() == GoGoDriver.VENDOR_ID
                && hse.getHidDevice().getProductId() == GoGoDriver.PRODUCT_ID) {
            conectado = false;
        }
    }

    @Override
    public void hidFailure(HidServicesEvent hse) {
    }
}
