package br.univali.portugol.plugin.gogoboard.biblioteca;

import br.univali.portugol.nucleo.bibliotecas.base.Biblioteca;
import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.nucleo.bibliotecas.base.TipoBiblioteca;
import br.univali.portugol.nucleo.bibliotecas.base.anotacoes.Autor;
import br.univali.portugol.nucleo.bibliotecas.base.anotacoes.DocumentacaoBiblioteca;
import br.univali.portugol.nucleo.bibliotecas.base.anotacoes.DocumentacaoFuncao;
import br.univali.portugol.nucleo.bibliotecas.base.anotacoes.DocumentacaoParametro;
import br.univali.portugol.nucleo.bibliotecas.base.anotacoes.PropriedadesBiblioteca;
import br.univali.portugol.plugin.gogoboard.componetes.DispositivoGoGo;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;

/**
 * Classe principal da biblioteca 'GoGoBoard'.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
@PropriedadesBiblioteca(tipo = TipoBiblioteca.COMPARTILHADA)
@DocumentacaoBiblioteca(
        descricao = "Esta biblioteca permite manipular a controladora GoGo board",
        versao = "0.01"
)
public final class GoGoBoard extends Biblioteca {

    DispositivoGoGo dispositivo = new DispositivoGoGo(GoGoDriver.TIPODRIVER.BIBLIOTECA);
    private final String msgEnvioDeCodigo = "Este método só é suportada no modo envio de código para a GoGo Board";

    @DocumentacaoFuncao(
            descricao = "Realiza a consulta do valor atual de um sensor",
            parametros
            = {
                @DocumentacaoParametro(nome = "numSensor", descricao = "o numero do sensor desejado")
            },
            retorno = "Valor atual do sensor",
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public int consultar_sensor(int numSensor) throws ErroExecucaoBiblioteca, InterruptedException {
        return dispositivo.getValorSensor(numSensor - 1, true);
    }

    @DocumentacaoFuncao(
            descricao = "Liga os motores especificados por parâmetro",
            parametros
            = {
                @DocumentacaoParametro(nome = "motores", descricao = "letras correspondentes aos motores desejados \n Ex: \"abcd\"")
            },
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void ligar_motores(String motores) throws ErroExecucaoBiblioteca, InterruptedException {
        motores = motores.toLowerCase();
        for (char nomeMotor : motores.toCharArray()) {
            switch (nomeMotor) {
                case 'a':
                    dispositivo.ligarMotor(0);
                    break;
                case 'b':
                    dispositivo.ligarMotor(1);
                    break;
                case 'c':
                    dispositivo.ligarMotor(2);
                    break;
                case 'd':
                    dispositivo.ligarMotor(3);
                    break;
                default:
                    throw new ErroExecucaoBiblioteca("Somente são aceitos motores A,B,C e D");
            }
        }
    }

    @DocumentacaoFuncao(
            descricao = "Liga os motores pelo tempo especificado por parâmetro",
            parametros
            = {
                @DocumentacaoParametro(nome = "motores", descricao = "as letras correspondentes aos motores desejados \n Ex: \"abcd\"")
                ,
                @DocumentacaoParametro(nome = "intervalo", descricao = "o intervalo de tempo (em milissegundos) durante o qual o motor ficará ligado")
            },
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void ligar_motor_por(String motores, int intervalo) throws ErroExecucaoBiblioteca, InterruptedException {
        ligar_motores(motores);
        Thread.sleep(intervalo);
        desligar_motores(motores);
    }

    @DocumentacaoFuncao(
            descricao = "Desliga os motores especificados por parâmetro",
            parametros
            = {
                @DocumentacaoParametro(nome = "motores", descricao = "as letras correspondentes aos motores desejados \n Ex: \"abcd\"")
            },
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void desligar_motores(String motores) throws ErroExecucaoBiblioteca, InterruptedException {
        motores = motores.toLowerCase();
        for (char nomeMotor : motores.toCharArray()) {
            switch (nomeMotor) {
                case 'a':
                    dispositivo.desligarMotor(0);
                    break;
                case 'b':
                    dispositivo.desligarMotor(1);
                    break;
                case 'c':
                    dispositivo.desligarMotor(2);
                    break;
                case 'd':
                    dispositivo.desligarMotor(3);
                    break;
                default:
                    throw new ErroExecucaoBiblioteca("Somente são aceitos motores A,B,C e D");
            }
        }
    }

    @DocumentacaoFuncao(
            descricao = "Muda para o sentido horário os motores especificados por parâmetro.",
            parametros
            = {
                @DocumentacaoParametro(nome = "motores", descricao = "letras correspondentes aos motores desejados \n Ex: \"abcd\"")
            },
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void sentido_horario_motor(String motores) throws ErroExecucaoBiblioteca, InterruptedException {
        controlarDirecaoMotor(motores, 1);
    }

    @DocumentacaoFuncao(
            descricao = "Muda para o sentido anti-horário os motores especificados por parâmetro.",
            parametros
            = {
                @DocumentacaoParametro(nome = "motores", descricao = "as letras correspondentes aos motores desejados \n Ex: \"abcd\"")
            },
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void sentido_anti_horario_motor(String motores) throws ErroExecucaoBiblioteca, InterruptedException {
        controlarDirecaoMotor(motores, 0);
    }

    @DocumentacaoFuncao(
            descricao = "Inverte sentido dos motores especificados por parâmetro",
            parametros
            = {
                @DocumentacaoParametro(nome = "motores", descricao = "as letras correspondentes aos motores desejados \n Ex: \"abcd\"")
            },
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void inverter_direcao_motor(String motores) throws ErroExecucaoBiblioteca, InterruptedException {
        motores = motores.toLowerCase();
        for (char nomeMotor : motores.toCharArray()) {
            switch (nomeMotor) {
                case 'a':
                    dispositivo.inverterDirecaoMotor(0);
                    break;
                case 'b':
                    dispositivo.inverterDirecaoMotor(1);
                    break;
                case 'c':
                    dispositivo.inverterDirecaoMotor(2);
                    break;
                case 'd':
                    dispositivo.inverterDirecaoMotor(3);
                    break;
                default:
                    throw new ErroExecucaoBiblioteca("Somente são aceitos motores A,B,C e D");
            }
        }
    }

    @DocumentacaoFuncao(
            descricao = "Define a força dos motores especificados por parâmetro",
            parametros
            = {
                @DocumentacaoParametro(nome = "motores", descricao = "as letras correspondentes aos motores desejados \n Ex: \"abcd\"")
                ,
                @DocumentacaoParametro(nome = "forca", descricao = "Valor inteiro correspondente à força")
            },
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void definir_forca_motor(String motores, int forca) throws ErroExecucaoBiblioteca, InterruptedException {
        motores = motores.toLowerCase();
        for (char nomeMotor : motores.toCharArray()) {
            switch (nomeMotor) {
                case 'a':
                    dispositivo.definirForcaMotor(0, forca);
                    break;
                case 'b':
                    dispositivo.definirForcaMotor(1, forca);
                    break;
                case 'c':
                    dispositivo.definirForcaMotor(2, forca);
                    break;
                case 'd':
                    dispositivo.definirForcaMotor(3, forca);
                    break;
                default:
                    throw new ErroExecucaoBiblioteca("Somente são aceitos motores A,B,C e D");
            }
        }
    }

    /**
     * Método para controlar a direção dos motores.
     *
     * @param motores
     * @param direção inteiro correspendente à direção. 0 = Esquerda e 1 =
     * Direita.
     */
    private void controlarDirecaoMotor(String motores, int direcao) throws ErroExecucaoBiblioteca, InterruptedException {
        motores = motores.toLowerCase();
        for (char nomeMotor : motores.toCharArray()) {
            switch (nomeMotor) {
                case 'a':
                    dispositivo.definirDirecaoMotor(0, direcao);
                    break;
                case 'b':
                    dispositivo.definirDirecaoMotor(1, direcao);
                    break;
                case 'c':
                    dispositivo.definirDirecaoMotor(2, direcao);
                    break;
                case 'd':
                    dispositivo.definirDirecaoMotor(3, direcao);
                    break;
                default:
                    throw new ErroExecucaoBiblioteca("Somente são aceitos motores A,B,C e D");
            }
        }
    }

    @DocumentacaoFuncao(
            descricao = "Obtém o estado dos motores DC especificados por parâmetro",
            parametros
            = {
                @DocumentacaoParametro(nome = "motores", descricao = "as letras correspondentes aos motores desejados \n Ex: \"abcd\"")
            },
            retorno = "Estado do(s) motor(es) DC. Retorna verdadeiro se todos estiverem ligados e falso se um ou mais estiver desligado.",
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public boolean estado_motores(String motores) throws ErroExecucaoBiblioteca, InterruptedException {
        boolean isLigado;
        motores = motores.toLowerCase();
        for (char nomeMotor : motores.toCharArray()) {
            switch (nomeMotor) {
                case 'a':
                    isLigado = dispositivo.getEstadoMotor(0);
                    break;
                case 'b':
                    isLigado = dispositivo.getEstadoMotor(1);
                    break;
                case 'c':
                    isLigado = dispositivo.getEstadoMotor(2);
                    break;
                case 'd':
                    isLigado = dispositivo.getEstadoMotor(3);
                    break;
                default:
                    throw new ErroExecucaoBiblioteca("Somente são aceitos motores A,B,C e D");
            }
            if (!isLigado) {
                return false;
            }
        }
        return true;
    }

    @DocumentacaoFuncao(
            descricao = "Aciona o buzzer para emitir um 'beep'",
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void acionar_beep() throws ErroExecucaoBiblioteca, InterruptedException {
        dispositivo.acionarBeep();
    }

    @DocumentacaoFuncao(
            descricao = "Acende o LED do usuário integrado à placa",
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void acender_led() throws ErroExecucaoBiblioteca, InterruptedException {
        dispositivo.controlarLed(1);
    }

    @DocumentacaoFuncao(
            descricao = "Apaga o LED do usuário integrado à placa",
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void apagar_led() throws ErroExecucaoBiblioteca, InterruptedException {
        dispositivo.controlarLed(0);
    }

    @DocumentacaoFuncao(
            descricao = "Exibe o texto no display de 7 segmentos interno da GoGo Board",
            parametros
            = {
                @DocumentacaoParametro(nome = "palavra", descricao = "palavra que será exibido no display de 7 segmentos.\n Deve ser de até 4 digitos.\n Ex: 'GoGo'")
            },
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void exibir_palavra(String palavra) throws ErroExecucaoBiblioteca, InterruptedException {
        dispositivo.exibirTexto(palavra);
    }

    @DocumentacaoFuncao(
            descricao = "Exibe o número no display de 7 segmentos interno da GoGo Board",
            parametros
            = {
                @DocumentacaoParametro(nome = "numero", descricao = "números que será exibido no display de 7 segmentos.\n Deve ser de até 4 digitos.\n Ex: '1234'")
            },
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void exibir_numero(int numero) throws ErroExecucaoBiblioteca, InterruptedException {
        dispositivo.exibirNumero(numero);
    }

    @DocumentacaoFuncao(
            descricao = "Exibe o texto no display LCD do módulo externo da GoGo Board.",
            parametros
            = {
                @DocumentacaoParametro(nome = "texto", descricao = "texto que será exibido no display LCD do módulo externo da GoGo Board.\n Deve ser de até 60 digitos.\n Ex: 'GoGo Board'")
            },
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void exibir_texto_display_LCD(String texto) throws ErroExecucaoBiblioteca, InterruptedException {
        dispositivo.exibirTexto(texto);
    }

    @DocumentacaoFuncao(
            descricao = "Exibe o número no display LCD do módulo externo da GoGo Board.",
            parametros
            = {
                @DocumentacaoParametro(nome = "numero", descricao = "número que será exibido no display de seguimentos.\n Deve ser de até 60 digitos.\n Ex: '1234567890'")
            },
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void exibir_numero_display_LCD(int numero) throws ErroExecucaoBiblioteca, InterruptedException {
        dispositivo.exibirNumero(numero);
    }

    @DocumentacaoFuncao(
            descricao = "Limpa o conteúdo do display LCD do módulo externo da GoGo Board.",
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void limpar_display_LCD() throws ErroExecucaoBiblioteca, InterruptedException {
        dispositivo.limparDisplayLCD();
    }

    @DocumentacaoFuncao(
            descricao = "Define a posição do cursor no display LCD do módulo externo da GoGo Board. " + msgEnvioDeCodigo,
            parametros
            = {
                @DocumentacaoParametro(nome = "posicao", descricao = "número correspondete a posição")
            },
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void define_posicao_cursor_display_LCD(int posicao) throws ErroExecucaoBiblioteca, InterruptedException {
        throw new ErroExecucaoBiblioteca(msgEnvioDeCodigo);
    }

    @DocumentacaoFuncao(
            descricao = "Obter o válor do temporizador. " + msgEnvioDeCodigo,
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void consultar_temporizador() throws ErroExecucaoBiblioteca, InterruptedException {
        throw new ErroExecucaoBiblioteca(msgEnvioDeCodigo);
    }

    @DocumentacaoFuncao(
            descricao = "Pausa a execução do programa da durante o intervalo de tempo especificado. Tem a mesma função do aguarde da biblioteca Util.",
            parametros
            = {
                @DocumentacaoParametro(nome = "intervalo", descricao = "o intervalo de tempo (em milissegundos) durante o qual o programa ficará pausado")
            },
            autores
            = {
                @Autor(nome = "Luiz Fernando Noschang", email = "noschang@univali.br")
            }
    )
    public void aguarde(int intervalo) throws ErroExecucaoBiblioteca, InterruptedException, InterruptedException {
        Thread.sleep(intervalo);
    }

    @DocumentacaoFuncao(
            descricao = "Zera o temporizador interno da GoGo Board. " + msgEnvioDeCodigo,
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )

    public void zerar_temporizador() throws ErroExecucaoBiblioteca, InterruptedException, InterruptedException {
        throw new ErroExecucaoBiblioteca(msgEnvioDeCodigo);
    }

    @DocumentacaoFuncao(
            descricao = "Define o tempo entre cada sinal de clock. " + msgEnvioDeCodigo,
            parametros
            = {
                @DocumentacaoParametro(nome = "intervalo", descricao = "o intervalo de tempo (em milissegundos) durante o qual o programa ficará pausado")
            },
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void definir_intervalo_clock(int intervalo) throws ErroExecucaoBiblioteca, InterruptedException {
        throw new ErroExecucaoBiblioteca(msgEnvioDeCodigo);
    }

    @DocumentacaoFuncao(
            descricao = "Obtém o estado do temporizador. Retorna verdadeiro se o temporizador estiver marcando o tempo e falso se não estiver. " + msgEnvioDeCodigo,
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public boolean estado_temporizador() throws ErroExecucaoBiblioteca, InterruptedException {
        throw new ErroExecucaoBiblioteca(msgEnvioDeCodigo);
    }

    @DocumentacaoFuncao(
            descricao = "Obtém o número de clocks que passou desde o último reset. " + msgEnvioDeCodigo,
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public boolean consultar_clock() throws ErroExecucaoBiblioteca, InterruptedException {
        throw new ErroExecucaoBiblioteca(msgEnvioDeCodigo);
    }

    @DocumentacaoFuncao(
            descricao = "Zera o contador do clock e relógio interno da GoGo Board." + msgEnvioDeCodigo,
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public boolean zerar_clock() throws ErroExecucaoBiblioteca, InterruptedException {
        throw new ErroExecucaoBiblioteca(msgEnvioDeCodigo);
    }

    @DocumentacaoFuncao(
            descricao = "Obtém o estado do infravermelho. Retorna verdadeiro quando o infravermelho estiver recebendo algum código e falso se não estiver.",
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public boolean estado_infra_vermelho() throws ErroExecucaoBiblioteca, InterruptedException {
        int valor = dispositivo.getValorIR(true);
        return valor != 0;
    }

    @DocumentacaoFuncao(
            descricao = "Obtém o código recebido pelo sensor infravermelho.",
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public int consultar_infra_vermelho() throws ErroExecucaoBiblioteca, InterruptedException {
        return dispositivo.getValorIR(true);
    }
}
