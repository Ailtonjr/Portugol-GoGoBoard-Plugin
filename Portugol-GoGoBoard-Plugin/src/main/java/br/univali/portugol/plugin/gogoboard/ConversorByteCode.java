package br.univali.portugol.plugin.gogoboard;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class ConversorByteCode {

    public ConversorByteCode() {
    }

    public byte[] converterLogoParaByteCode(String logo) {
        byte[] byteCode = null;
        //TODO: Alterar para um caminho relativo
        String caminho = "C:\\Users\\Ailton Cardoso Jr\\Documents\\NetBeansProjects\\Portugol - Novo\\Portugol-GoGoBoard-Plugin\\Portugol-GoGoBoard-Plugin\\src\\main\\resources\\br\\univali\\portugol\\plugin\\gogoboard\\compilador\\logoc.py";

        // Monta a linha de comando com o arquivo e o argumento
        CommandLine cmdLine = new CommandLine("python");
        cmdLine.addArgument("\"" + caminho + "\"");
        cmdLine.addArgument(logo);
        // Cria o Executor e o StreamHandler para capturar a saida e colocar no streamSaida
        ByteArrayOutputStream streamSaida = new ByteArrayOutputStream();
        DefaultExecutor executor = new DefaultExecutor();
        PumpStreamHandler streamHandler = new PumpStreamHandler(streamSaida);
        executor.setStreamHandler(streamHandler);
        try {
            executor.execute(cmdLine);
            System.err.println("SAIDA COMPILADOR:\n\n" + streamSaida.toString());
            byteCode = montarArrayBytecode(streamSaida.toString());
        } catch (IOException ex) {
            System.err.println("Erro ao compilar o bytecode");
            Logger.getLogger(ConversorByteCode.class.getName()).log(Level.SEVERE, null, ex);
        }
        return byteCode;
    }

    private static byte[] montarArrayBytecode(String outputStream) {
        // Quebra a String após o 'Raw byte code'
        String[] stringSplit = outputStream.split("Raw byte code: \r\r\n");
        // Pega a segunda parte, ou seja o byte code
        String rawByteCode = stringSplit[1];
        // Quebra o raw byte code por virgula e armazena num array de String
        String[] arrayString = rawByteCode.split(Pattern.quote(", "));

        byte[] bytecode = new byte[arrayString.length - 1];

        // Transforma os valores em bytes e armazena no array de bytes, ignorando o ultimo valor (vazio " ")
        for (int i = 0; i < arrayString.length - 1; i++) {
            bytecode[i] = Byte.valueOf(arrayString[i]);
        }
        return bytecode;
    }
}
