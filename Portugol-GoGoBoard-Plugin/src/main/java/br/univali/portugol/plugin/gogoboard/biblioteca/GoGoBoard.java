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
            descricao = "Ligar os motores especificados por parametro",
            parametros
            = {
                @DocumentacaoParametro(nome = "motores", descricao = "as letras correspondentes aos motores desejados \n Ex: \"abcd\"")
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
            descricao = "Desligar os motores especificados por parametro",
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
            descricao = "Mudar sentido dos motores especificados por parametro para o sentido Horário",
            parametros
            = {
                @DocumentacaoParametro(nome = "motores", descricao = "as letras correspondentes aos motores desejados \n Ex: \"abcd\"")
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
            descricao = "Mudar sentido dos motores especificados por parametro para o sentido Horário",
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
            descricao = "Inverter sentido dos motores especificados por parametro",
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
            descricao = "Setar força dos motores especificados por parametro",
            parametros
            = {
                @DocumentacaoParametro(nome = "motores", descricao = "Letras correspondentes aos motores desejados \n Ex: \"abcd\"")
                ,
                @DocumentacaoParametro(nome = "forca", descricao = "Valor inteiro correspondente à força")
            },
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void setar_forca_motor(String motores, int forca) throws ErroExecucaoBiblioteca, InterruptedException {
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
     * @param  motores
     * @param direção inteiro correspendente à direção. 0 = Esquerda e 1 = Direita.
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
            descricao = "Retorna o estado dos motores DC especificados por parametro",
            parametros
            = {
                @DocumentacaoParametro(nome = "motores", descricao = "as letras correspondentes aos motores desejados \n Ex: \"abcd\"")
            },
            retorno = "Estado do(s) motor(es)",
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
            descricao = "Acionar o buzzer para emitir um 'beep'",
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void acionar_beep() throws ErroExecucaoBiblioteca, InterruptedException {
        dispositivo.acionarBeep();
    }

    @DocumentacaoFuncao(
            descricao = "Acender o LED do usuário integrado à placa",
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void acender_led() throws ErroExecucaoBiblioteca, InterruptedException {
        dispositivo.controlarLed(1);
    }

    @DocumentacaoFuncao(
            descricao = "Apagar o LED do usuário integrado à placa",
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void apagar_led() throws ErroExecucaoBiblioteca, InterruptedException {
        dispositivo.controlarLed(0);
    }

    @DocumentacaoFuncao(
            descricao = "Exibir texto ou números no display de segmentos",
            parametros
            = {
                @DocumentacaoParametro(nome = "texto", descricao = "Palavra ou números que será exibido no display de seguimentos.\n Deve ser de até 4 digitos.\n Ex: 'GoGo' ou '1234'")
            },
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void exibeTextoCurto(String texto) throws ErroExecucaoBiblioteca, InterruptedException {
        dispositivo.exibirTextoCurto(texto);
    }
}
