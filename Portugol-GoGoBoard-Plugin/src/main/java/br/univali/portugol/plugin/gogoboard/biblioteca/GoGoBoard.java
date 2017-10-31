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
import br.univali.portugol.plugin.gogoboard.componetes.MotorServo;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    DispositivoGoGo dispositivoGoGo = new DispositivoGoGo(GoGoDriver.TIPODRIVER.COMPARTILHADO);
    private final String msgEnvioDeCodigo = "Este método só é suportada no modo envio de código para a GoGo Board";
    private final String msgModulo = "Este método só é suportada se o módulo estiver conectado.";

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
        return dispositivoGoGo.getValorSensor(numSensor - 1, true);
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
    public void ligar_motor(String motores) throws ErroExecucaoBiblioteca, InterruptedException {
        motores = motores.toLowerCase();
        verificarMotores(motores);
        for (char motor : motores.toCharArray()) {
            dispositivoGoGo.getMotoresDC().get(motor).ligar();
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
        ligar_motor(motores);
        Thread.sleep(intervalo);
        desligar_motor(motores);
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
    public void desligar_motor(String motores) throws ErroExecucaoBiblioteca, InterruptedException {
        motores = motores.toLowerCase();
        verificarMotores(motores);
        for (char motor : motores.toCharArray()) {
            dispositivoGoGo.getMotoresDC().get(motor).desligar();
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
        motores = motores.toLowerCase();
        verificarMotores(motores);
        controlar_direcao_motor(motores, 1);
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
        motores = motores.toLowerCase();
        verificarMotores(motores);
        controlar_direcao_motor(motores, 0);
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
        verificarMotores(motores);
        for (char motor : motores.toCharArray()) {
            dispositivoGoGo.getMotoresDC().get(motor).inverterDirecao();
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
        verificarMotores(motores);
        for (char motor : motores.toCharArray()) {
            dispositivoGoGo.getMotoresDC().get(motor).definirForca(forca);
        }
    }

    /**
     * Método para verificar se a string com os nomes dos motores passada por
     * parâmetro está correta.
     *
     * @param motores
     * @param direção inteiro correspendente à direção. 0 = Esquerda e 1 =
     * Direita.
     */
    private void verificarMotores(String string) throws ErroExecucaoBiblioteca {
        final Pattern pattern = Pattern.compile("[abcd]{1,4}");
        Matcher matcher = pattern.matcher(string);
        if (!matcher.matches()) {
            throw new ErroExecucaoBiblioteca("Somente são aceitos motores A,B,C e D");
        }
    }

    /**
     * Método para controlar a direção dos motores.
     *
     * @param motores string correspondente aos motores desejados Ex: "abcd"
     * @param direção inteiro correspendente à direção. 0 = Esquerda e 1 =
     * Direita.
     */
    private void controlar_direcao_motor(String motores, int direcao) throws ErroExecucaoBiblioteca, InterruptedException {
        for (char motor : motores.toCharArray()) {
            dispositivoGoGo.getMotoresDC().get(motor).definirDirecao(direcao);
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
    public boolean estado_motor(String motores) throws ErroExecucaoBiblioteca, InterruptedException {
        motores = motores.toLowerCase();

        verificarMotores(motores);
        for (char nomeMotor : motores.toCharArray()) {
            if (!dispositivoGoGo.getMotoresDC().get(nomeMotor).isLigado()) {
                return false;
            }
        }
        return true;
    }

    @DocumentacaoFuncao(
            descricao = "Define a posição dos motores especificados por parâmetro",
            parametros
            = {
                @DocumentacaoParametro(nome = "motores", descricao = "as letras correspondentes aos motores desejados \n Ex: \"abcd\"")
                ,
                @DocumentacaoParametro(nome = "posição", descricao = "Valor inteiro correspondente à posição. O motor servo aceita valores maiores que 10 e menores ou igual a 40, qualquer outro valor fora deste intervalo, será convertido automaticamente.")
            },
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void definir_posicao_servo(String motores, int posicao) throws ErroExecucaoBiblioteca, InterruptedException {
        motores = motores.toLowerCase();
        for (char nomeMotor : motores.toCharArray()) {
            dispositivoGoGo.getMotoresServo().get(nomeMotor).setPosicao(posicao);
        }
    }

    @DocumentacaoFuncao(
            descricao = "\"Caminha\" para o sentido horário os motores especificados por parâmetro.",
            parametros
            = {
                @DocumentacaoParametro(nome = "motores", descricao = "as letras correspondentes aos motores desejados \n Ex: \"abcd\"")
                ,
                @DocumentacaoParametro(nome = "passos", descricao = "Quantidade de passos que devem ser \"percorridos\". Se a posição inicial não estiver sido definida, será considerado como posição inicial 10.")
            },
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void sentido_horario_servo(String motores, int passos) throws ErroExecucaoBiblioteca, InterruptedException {
        motores = motores.toLowerCase();
        for (char nomeMotor : motores.toCharArray()) {
            dispositivoGoGo.getMotoresServo().get(nomeMotor).setPosicao(dispositivoGoGo.getMotoresServo().get(nomeMotor).getPosicao() - passos);
        }
    }

    @DocumentacaoFuncao(
            descricao = "\"Caminha\" para o sentido anti-horário os motores especificados por parâmetro.",
            parametros
            = {
                @DocumentacaoParametro(nome = "motores", descricao = "as letras correspondentes aos motores desejados \n Ex: \"abcd\"")
                ,
                @DocumentacaoParametro(nome = "passos", descricao = "Quantidade de passos que devem ser \"percorridos\". Se a posição inicial não estiver sido definida, será considerado como posição inicial 10.")
            },
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void sentido_anti_horario_servo(String motores, int passos) throws ErroExecucaoBiblioteca, InterruptedException {
        motores = motores.toLowerCase();
        for (char nomeMotor : motores.toCharArray()) {
            MotorServo servo = dispositivoGoGo.getMotoresServo().get(nomeMotor);
            int pos = dispositivoGoGo.getMotoresServo().get(nomeMotor).getPosicao();
            servo.setPosicao(pos + passos);
        }
    }

    @DocumentacaoFuncao(
            descricao = "Obtém a posição atual do motor servo.",
            parametros
            = {
                @DocumentacaoParametro(nome = "motor", descricao = "a letra correspondente a motor desejado \n Ex: 'a' ou 'A'")
            },
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public int consultar_posicao_servo(char nomeMotor) throws ErroExecucaoBiblioteca, InterruptedException {
        return dispositivoGoGo.getMotoresServo().get(nomeMotor).getPosicao();
    }

    @DocumentacaoFuncao(
            descricao = "Aciona o buzzer para emitir um 'beep'",
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void acionar_beep() throws ErroExecucaoBiblioteca, InterruptedException {
        dispositivoGoGo.getBuzzer().acionar();
    }

    @DocumentacaoFuncao(
            descricao = "Acende o LED do usuário integrado à placa",
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void acender_led() throws ErroExecucaoBiblioteca, InterruptedException {
        dispositivoGoGo.getLedUsuario().controlarLed(1);
    }

    @DocumentacaoFuncao(
            descricao = "Apaga o LED do usuário integrado à placa",
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void apagar_led() throws ErroExecucaoBiblioteca, InterruptedException {
        dispositivoGoGo.getLedUsuario().controlarLed(0);
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
        dispositivoGoGo.getDisplay().exibirPalavra(palavra);
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
        dispositivoGoGo.getModuloDisplayLCD().exibirNumero(numero);
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
        dispositivoGoGo.getModuloDisplayLCD().exibirTexto(texto);
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
        dispositivoGoGo.getModuloDisplayLCD().exibirNumero(numero);
    }

    @DocumentacaoFuncao(
            descricao = "Limpa o conteúdo do display LCD do módulo externo da GoGo Board.",
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void limpar_display_LCD() throws ErroExecucaoBiblioteca, InterruptedException {
        dispositivoGoGo.getModuloDisplayLCD().limparTela();
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
    public void definir_posicao_display_LCD(int posicao) throws ErroExecucaoBiblioteca, InterruptedException {
        throw new ErroExecucaoBiblioteca(msgEnvioDeCodigo);
    }

    @DocumentacaoFuncao(
            descricao = "Obtém o válor do temporizador. " + msgEnvioDeCodigo,
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public int consultar_temporizador() throws ErroExecucaoBiblioteca, InterruptedException {
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
    public int consultar_clock() throws ErroExecucaoBiblioteca, InterruptedException {
        throw new ErroExecucaoBiblioteca(msgEnvioDeCodigo);
    }

    @DocumentacaoFuncao(
            descricao = "Zera o contador do clock e relógio interno da GoGo Board." + msgEnvioDeCodigo,
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void zerar_clock() throws ErroExecucaoBiblioteca, InterruptedException {
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
        int valor = dispositivoGoGo.getInfravermelho().getValor(true);
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
        return dispositivoGoGo.getInfravermelho().getValor(true);
    }

    @DocumentacaoFuncao(
            descricao = "Obtém o dia do mês do módulo externo relógio e calendário da GoGo Board. " + msgModulo,
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public int consultar_dia() throws ErroExecucaoBiblioteca, InterruptedException {
        return dispositivoGoGo.getModuloRelogio().getDia();
    }

    @DocumentacaoFuncao(
            descricao = "Obtém o mês do módulo externo relógio e calendário da GoGo Board. " + msgModulo,
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public int consultar_mes() throws ErroExecucaoBiblioteca, InterruptedException {
        return dispositivoGoGo.getModuloRelogio().getMes();
    }

    @DocumentacaoFuncao(
            descricao = "Obtém o ano do módulo externo relógio e calendário da GoGo Board. " + msgModulo,
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public int consultar_ano() throws ErroExecucaoBiblioteca, InterruptedException {
        return dispositivoGoGo.getModuloRelogio().getAno();
    }

    @DocumentacaoFuncao(
            descricao = "Obtém o dia da semana do módulo externo relógio e calendário da GoGo Board. " + msgModulo,
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public int consultar_dia_semana() throws ErroExecucaoBiblioteca, InterruptedException {
        return dispositivoGoGo.getModuloRelogio().getDiaDaSemana();
    }

    @DocumentacaoFuncao(
            descricao = "Obtém a hora do módulo externo relógio e calendário da GoGo Board. " + msgModulo,
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public int consultar_hora() throws ErroExecucaoBiblioteca, InterruptedException {
        return dispositivoGoGo.getModuloRelogio().getHora();
    }

    @DocumentacaoFuncao(
            descricao = "Obtém o minuto do módulo externo relógio e calendário da GoGo Board. " + msgModulo,
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public int consultar_minuto() throws ErroExecucaoBiblioteca, InterruptedException {
        return dispositivoGoGo.getModuloRelogio().getMinuto();
    }

    @DocumentacaoFuncao(
            descricao = "Obtém o segundo do módulo externo relógio e calendário da GoGo Board. " + msgModulo,
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public int consultar_segundo() throws ErroExecucaoBiblioteca, InterruptedException {
        return dispositivoGoGo.getModuloRelogio().getSegundo();
    }

    @DocumentacaoFuncao(
            descricao = "Define a data e horário atual do PC no módulo externo relógio e calendário da GoGo Board. " + msgModulo,
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void sincronizar_relelogio() throws ErroExecucaoBiblioteca, InterruptedException {
        dispositivoGoGo.getModuloRelogio().sincronizarComPC();
    }

    @DocumentacaoFuncao(
            descricao = "Toca a faixa correspondente ao numero passado por parâmetro no módulo externo gravador de voz." + msgEnvioDeCodigo,
            parametros
            = {
                @DocumentacaoParametro(nome = "faixa", descricao = "número correspondente a faixa desejada.")
            },
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void tocar_faixa(int faixa) throws ErroExecucaoBiblioteca, InterruptedException {
        throw new ErroExecucaoBiblioteca(msgEnvioDeCodigo);
    }

    @DocumentacaoFuncao(
            descricao = "Muda para a próxima faixa no módulo externo gravador de voz." + msgEnvioDeCodigo,
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void tocar_proxima_faixa() throws ErroExecucaoBiblioteca, InterruptedException {
        throw new ErroExecucaoBiblioteca(msgEnvioDeCodigo);
    }

    @DocumentacaoFuncao(
            descricao = "Muda para a faixa anterior no módulo externo gravador de voz." + msgEnvioDeCodigo,
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void tocar_faixa_anterior() throws ErroExecucaoBiblioteca, InterruptedException {
        throw new ErroExecucaoBiblioteca(msgEnvioDeCodigo);
    }

    @DocumentacaoFuncao(
            descricao = "Apaga todas as faixas do módulo externo gravador de voz." + msgEnvioDeCodigo,
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void apagar_todas_as_faixas() throws ErroExecucaoBiblioteca, InterruptedException {
        throw new ErroExecucaoBiblioteca(msgEnvioDeCodigo);
    }
}
