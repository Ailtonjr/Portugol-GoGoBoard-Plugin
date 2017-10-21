/**
 * Classe com funções úteis utilizados no plugin
 */
package br.univali.portugol.plugin.gogoboard.util;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class UtilGoGoBoard {

    public static enum TipoDriver {
        BIBLIOTECA,
        MONITOR,
        Plugin
    };

    /**
     * Converte um array de bytes em um array de numeros 8-bit inteiros sem
     * sinal.
     *
     * @param array Um array de byte[] que contem a mensagem a ser convertida.
     * @return Um array de int[] com numeros 8-bit inteiros sem sinal.
     */
    public static int[] Uint8Array(byte[] array) {
        int[] mensagem = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            mensagem[i] = (0xFF & array[i]);
        }
        return mensagem;
    }

    /**
     * Converte dois de numeros 8-bit inteiros sem sinal em um numero inteiros.
     *
     * @param byteAlto byte[] correspondente ao byte alto.
     * @param byteBaixo byte[] correspondente ao byte baixo.
     * @return Um valor inteiro correspondente aos dois de numeros 8-bit
     * inteiros sem sinal.
     */
    public static int bytesToInt(int byteAlto, int byteBaixo) {
        return ((byteAlto << 8) + byteBaixo);
    }
}
