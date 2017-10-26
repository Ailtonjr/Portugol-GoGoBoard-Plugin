package br.univali.portugol.plugin.gogoboard.componetes.modulos;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;
import br.univali.portugol.plugin.gogoboard.gerenciadores.GerenciadorDriver;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Classe que representa o módulo externo Relógio.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
public class Relogio {

    private int dia;
    private int mes;
    private int ano;
    private int hora;
    private int minuto;
    private int segundo;
    private int diaDaSemana;

    private final GoGoDriver goGoDriver;

    /**
     * Construtor padrão do módulo externo relógio.
     *
     * @param tipoDriver Enum referente ao tipo de driver necessário.
     */
    public Relogio(GoGoDriver.TIPODRIVER tipoDriver) {
        this.goGoDriver = GerenciadorDriver.getGoGoDriver(tipoDriver);
    }

    /**
     * Método para obter o dia do mês.
     *
     * @return Dia do mês.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public int getDia() throws ErroExecucaoBiblioteca {
        atualizaValores();
        return dia;
    }

    /**
     * Método para definir o dia do mês.
     *
     * @param dia dia do mês.
     */
    public void setDia(int dia) {
        this.dia = dia;
    }

    /**
     * Método para obter o mês.
     *
     * @return Mês.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public int getMes() throws ErroExecucaoBiblioteca {
        atualizaValores();
        return mes;
    }

    /**
     * Método para definir o mês.
     *
     * @param mes mês.
     */
    public void setMes(int mes) {
        this.mes = mes;
    }

    /**
     * Método para obter o valor do ano.
     *
     * @return Ano.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public int getAno() throws ErroExecucaoBiblioteca {
        atualizaValores();
        return ano;
    }

    /**
     * Método para definir o ano.
     *
     * @param ano ano.
     */
    public void setAno(int ano) {
        this.ano = ano;
    }

    /**
     * Método para obter o valor hora.
     *
     * @return Hora.
     */
    public int getHora() throws ErroExecucaoBiblioteca {
        atualizaValores();
        return hora;
    }

    /**
     * Método para definir a hora.
     *
     * @param hora hora.
     */
    public void setHora(int hora) {
        this.hora = hora;
    }

    /**
     * Método para obter o minuto.
     *
     * @return Minuto.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public int getMinuto() throws ErroExecucaoBiblioteca {
        atualizaValores();
        return minuto;
    }

    /**
     * Método para definir o minuto.
     *
     * @param minuto minuto.
     */
    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }

    /**
     * Método para obter o segundo.
     *
     * @return Segundo.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public int getSegundo() throws ErroExecucaoBiblioteca {
        atualizaValores();
        return segundo;
    }

    /**
     * Método para definir o segundo.
     *
     * @param segundo Segundo.
     */
    public void setSegundo(int segundo) {
        this.segundo = segundo;
    }

    /**
     * Método para obter o valor do dia da semana.
     *
     * @return Dia da semana.
     * @throws br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public int getDiaDaSemana() throws ErroExecucaoBiblioteca {
        atualizaValores();
        return diaDaSemana;
    }

    /**
     * Método para definir o dia da semana.
     * @param diaDaSemana Dia da semana.
     */
    public void setDiaDaSemana(int diaDaSemana) {
        this.diaDaSemana = diaDaSemana;
    }

    /**
     * Método para atualizar os valores do objeto com os valores atuais do
     * módulo.
     *
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    private void atualizaValores() throws ErroExecucaoBiblioteca {
        solicitarAtualizacao();
        int[] mensagem;
        do {
            mensagem = goGoDriver.receberMensagem();
        } while (mensagem[0] != GoGoDriver.GOGOBOARD);       // Se não for uma mensagem da GoGo, tenta novamente
        segundo = mensagem[GoGoDriver.INDICE_VALOR_SEGUNDO];
        minuto = mensagem[GoGoDriver.INDICE_VALOR_MINUTO];
        hora = mensagem[GoGoDriver.INDICE_VALOR_HORA];
        diaDaSemana = mensagem[GoGoDriver.INDICE_VALOR_DIA_SEMANA];
        dia = mensagem[GoGoDriver.INDICE_VALOR_DIA];
        mes = mensagem[GoGoDriver.INDICE_VALOR_MES];
        ano = mensagem[GoGoDriver.INDICE_VALOR_ANO] + 2000;

        if (diaDaSemana == 100) {
            throw new ErroExecucaoBiblioteca("Não é possível usar esta função, pois o módulo de relógio não está conectado");
        }
    }

    /**
     * Método para solicitar a atualização dos valores do módulo. Deste modo, a
     * próxima leitura terá informações sobre o módulo.
     *
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    private void solicitarAtualizacao() throws ErroExecucaoBiblioteca {
        byte[] comando = new byte[GoGoDriver.TAMANHO_PACOTE];
        comando[GoGoDriver.ID_COMANDO] = GoGoDriver.CMD_LER_RELOGIO;
        goGoDriver.enviarComando(comando);
    }

    /**
     * Método para sincronizar os valores do módulo com o do PC.
     *
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public void sincronizarComPC() throws ErroExecucaoBiblioteca {
        Calendar cal = new GregorianCalendar();
        dia = cal.get(Calendar.DAY_OF_MONTH);
        mes = cal.get(Calendar.MONTH);
        ano = cal.get(Calendar.YEAR);
        hora = cal.get(Calendar.HOUR);
        minuto = cal.get(Calendar.MINUTE);
        segundo = cal.get(Calendar.SECOND);
        diaDaSemana = cal.get(Calendar.DAY_OF_WEEK);

        byte[] comando = new byte[GoGoDriver.TAMANHO_PACOTE];
        comando[GoGoDriver.ID_COMANDO] = GoGoDriver.CMD_SINCRONIZAR_RELOGIO;
        comando[GoGoDriver.PARAMETRO1] = (byte) cal.get(Calendar.SECOND);
        comando[GoGoDriver.PARAMETRO2] = (byte) cal.get(Calendar.MINUTE);
        comando[GoGoDriver.PARAMETRO3] = (byte) cal.get(Calendar.HOUR);
        comando[GoGoDriver.PARAMETRO4] = (byte) cal.get(Calendar.DAY_OF_WEEK);
        comando[GoGoDriver.PARAMETRO5] = (byte) (cal.get(Calendar.DAY_OF_MONTH) + 1);
        comando[GoGoDriver.PARAMETRO6] = (byte) cal.get(Calendar.MONTH);
        comando[GoGoDriver.PARAMETRO7] = (byte) cal.get(Calendar.YEAR);

        goGoDriver.enviarComando(comando);
    }
}
