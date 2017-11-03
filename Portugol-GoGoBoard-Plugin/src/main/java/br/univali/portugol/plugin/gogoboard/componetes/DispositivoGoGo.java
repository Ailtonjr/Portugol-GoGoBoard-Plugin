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
    Map<Character, MotorDC> motoresDC;
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

        motoresDC = new HashMap<Character, MotorDC>() {
            {
                put('a', new MotorDC(1, tipoDriver));
                put('b', new MotorDC(2, tipoDriver));
                put('c', new MotorDC(4, tipoDriver));
                put('d', new MotorDC(8, tipoDriver));
            }
        };
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

        atualizador = new AtualizadorComponentes(sensores, motoresDC, infravermelho, tipoDriver);
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

    /**
     * Método para retornar o componente buzzer;
     *
     * @return Componente buzzer.
     */
    public Buzzer getBuzzer() {
        return buzzer;
    }

    /**
     * Método para retornar o componente led.
     *
     * @return Componente led.
     */
    public Led getLedUsuario() {
        return ledUsuario;
    }

    /**
     * Método para retornar o display de 7 segmentos.
     *
     * @return Display de 7 segmentos.
     */
    public Display getDisplay() {
        return display;
    }

    /**
     * Método para retornar o módulo display LCD.
     *
     * @return Módulo display LCD.
     */
    public DisplayLCD getModuloDisplayLCD() {
        return moduloDisplayLCD;
    }

    /**
     * Método para retornar o componente infravermelho.
     *
     * @return Componente infravermelho.
     */
    public Infravermelho getInfravermelho() {
        return infravermelho;
    }

    /**
     * Método para retornar o HashMap dos motores DC.
     *
     * @return HashMap dos motores Dc.
     */
    public Map<Character, MotorDC> getMotoresDC() {
        return motoresDC;
    }
    
    /**
     * Método para retornar o motor DC correspondente ao chave.
     *
     * @param chave chave correspondente ao motor desejado.
     * @return Motor DC resultante da busca.
     */
    public MotorDC getMotorDC(Character chave) {
        return motoresDC.get(chave);
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

    /**
     * Método para verificar se a GoGo Board está conectada e disponível para
     * uso.
     *
     * @return
     */
    public boolean isConectado() {
        if (!conectado) {
            conectado = verificaConexao();
        }
        return conectado;
    }

    /**
     * Método interno para o status da GoGo Board, se está conectada e
     * disponível para uso.
     *
     * O método tenta ler os dados dos componentes, se não conseguir é porque
     * não está conectada ou está sendo usada por outro programa.
     *
     * @return
     */
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
