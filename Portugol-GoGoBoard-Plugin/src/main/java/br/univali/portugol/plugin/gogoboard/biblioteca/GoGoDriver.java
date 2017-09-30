package br.univali.portugol.plugin.gogoboard.biblioteca;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hid4java.HidDevice;
import org.hid4java.HidException;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.event.HidServicesEvent;




/**
 *
 * @author Ailton Cardoso Jr
 */
public class GoGoDriver implements HidServicesListener
{
    private HidServices servicosHID;
    private HidDevice gogoBoard;
    private static GoGoDriver gogoDriver;

    public static GoGoDriver obterInstancia()
    {
        if (gogoDriver == null)
        {
            gogoDriver = new GoGoDriver();
        }
        return gogoDriver;
    }

    public GoGoDriver()
    {
        try
        {
            carregarServicosHID();
        }
        catch (HidException ex)
        {
            ex.printStackTrace(System.err);
            Logger.getLogger(GoGoDriver.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ErroExecucaoBiblioteca ex)
        {
            ex.printStackTrace(System.err);
            Logger.getLogger(GoGoDriver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarMensagem(byte[] mensagem) throws ErroExecucaoBiblioteca
    {
        if (gogoBoard != null)
        {
            gogoBoard.write(mensagem, mensagem.length, (byte) 0);
        }
        else
        {
            throw new ErroExecucaoBiblioteca("Erro, GoGo Board está sendo usada por outro programa ou não está conectada.");
        }
    }

    public byte[] receberMensagem(int numBytes) throws ErroExecucaoBiblioteca
    {
        byte[] mensagem = new byte[numBytes];
        if (gogoBoard != null)
        {
            gogoBoard.read(mensagem, 500);
            return mensagem;
        }
        else
        {
            throw new ErroExecucaoBiblioteca("Erro, GoGo Board está sendo usada por outro programa ou não está conectada.");
        }
        //return null;
    }

    public void beep() throws ErroExecucaoBiblioteca
    {
        byte[] mensagem = new byte[64];
        mensagem[2] = 11;
        enviarMensagem(mensagem);
    }

    public void exibeTextoCurto(String texto) throws ErroExecucaoBiblioteca
    {
        if (gogoBoard != null)
        {
            if (texto.length() > 4)
            {
                throw new ErroExecucaoBiblioteca("Erro, o display de segmentos não pode exibir mais de 4 characteres.");
            }
            byte[] mensagem = new byte[64];
            mensagem[2] = 60;
            for (int i = 0; i < texto.length(); i++)
            {
                mensagem[3 + i] = (byte) texto.charAt(i);
            }
            enviarMensagem(mensagem);
        }
        else
        {
            throw new ErroExecucaoBiblioteca("Erro, GoGo Board está sendo usada por outro programa ou não está conectada.");
        }

    }

    public void carregarServicosHID() throws HidException, ErroExecucaoBiblioteca
    {
        // Pegar os servicos HID e add listener
        servicosHID = HidManager.getHidServices();
        servicosHID.addHidServicesListener(this);

        System.out.println("Iniciando Driver GoGo");
        servicosHID.start();

        // Percorre a lista dos dispositivos conectados
                gogoBoard = servicosHID.getHidDevice(0x461, 0x20, null);
                System.out.println("GoGo Board1: " + gogoBoard);
                System.out.println("");
    }

    @Override
    public void hidDeviceAttached(HidServicesEvent hse)
    {
        if (hse.getHidDevice().getVendorId() == 0x461
                && hse.getHidDevice().getProductId() == 0x20)
        {
            System.out.println("GoGo Board: " + hse.getHidDevice());
            gogoBoard = servicosHID.getHidDevice(0x461, 0x20, null);
        }
    }

    @Override
    public void hidDeviceDetached(HidServicesEvent hse)
    {
        if (hse.getHidDevice().getVendorId() == 0x461
                && hse.getHidDevice().getProductId() == 0x20)
        {
            System.out.println("GoGo Board: " + hse.getHidDevice());
            gogoBoard = null;
        }
    }

    @Override
    public void hidFailure(HidServicesEvent hse)
    {
        System.err.println("Falha no HID");
    }

}
