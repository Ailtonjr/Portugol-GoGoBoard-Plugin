package br.univali.portugol.plugin.gogoboard.componetes;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.driver.GerenciadorDeDriver;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;
import br.univali.portugol.plugin.gogoboard.util.UtilGoGoBoard;
import java.util.ArrayList;
import java.util.List;
import org.hid4java.HidServicesListener;
import org.hid4java.event.HidServicesEvent;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class DispositivoGoGo implements HidServicesListener {

    private List<Sensor> sensores;
    private List<DCMotor> motoresDC;
    private List<ServoMotor> motoresServo;

    private Led ledUsuario;
    private Buzzer buzzer;
    private Display display;
    private Infravermelho infravermelho;

    private AtualizadorComponentes atualizador;
    private GoGoDriver goGoDriver;
    private boolean conectado;
    private UtilGoGoBoard.TipoDriver tipoDriver;

    public DispositivoGoGo(UtilGoGoBoard.TipoDriver tipoDriver) {
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

        motoresDC = new ArrayList<DCMotor>() {
            {
                add(new DCMotor(1, tipoDriver));
                add(new DCMotor(2, tipoDriver));
                add(new DCMotor(4, tipoDriver));
                add(new DCMotor(8, tipoDriver));
            }
        };

        motoresServo = new ArrayList<ServoMotor>() {
            {
                add(new ServoMotor(1, tipoDriver));
                add(new ServoMotor(2, tipoDriver));
                add(new ServoMotor(4, tipoDriver));
                add(new ServoMotor(8, tipoDriver));
            }
        };

        ledUsuario = new Led(0, tipoDriver);
        buzzer = new Buzzer(tipoDriver);
        display = new Display(tipoDriver);
        infravermelho = new Infravermelho();

        atualizador = new AtualizadorComponentes(sensores, infravermelho, tipoDriver);
        goGoDriver = GerenciadorDeDriver.getGoGoDriver(tipoDriver);
        addServiceListener(this);
    }

    /**
     * Método para obter o valor de um sensor.
     *
     * @param numSensor Número correspondete ao sensor que retornará o valor.
     * @param atualizar Booleano para indicar se é necessário atualizar o valor
     * antes de retornar
     *
     * @throws ErroExecucaoBiblioteca
     */
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

    public int getValorRecebidoIR() {
        return infravermelho.getValorRecebido();
    }

    /**
     * Método para atualizar os componentes da GoGoBoard (inputs/outputs)
     *
     * @throws ErroExecucaoBiblioteca
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
