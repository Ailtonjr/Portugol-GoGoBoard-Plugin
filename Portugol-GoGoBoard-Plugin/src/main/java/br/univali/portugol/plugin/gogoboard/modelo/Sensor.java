package br.univali.portugol.plugin.gogoboard.modelo;

import br.univali.portugol.plugin.gogoboard.GoGoDriver;
import br.univali.ps.plugins.base.ErroExecucaoPlugin;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class Sensor
{
    //private static GoGoDriver gogoDriver = new GoGoDriver();
    private int numero;
    private int valor;

    public Sensor(int nome)
    {
        this.numero = nome;
    }

    public int getNumero()
    {
        return numero;
    }

    public void setNumero(int numero)
    {
        this.numero = numero;
    }

    public int getValor() throws ErroExecucaoPlugin
    {
        System.err.println("Lendo Sensor\n");
        byte[] mensagem;
        do
        {
            mensagem = GoGoDriver.obterInstancia().receberMensagem(64);
        }
        while (mensagem[0] != 0);       // Evitar pegar valor zerado do sensor
        ByteBuffer bb = ByteBuffer.wrap(mensagem, (2 * (numero)) + 1, 2); // Nome-1 pois o sensor começa em 0 na mensagem
        bb.order(ByteOrder.BIG_ENDIAN);
        valor = bb.getShort();
        return valor;
    }
}
