package br.univali.portugol.plugin.gogoboard.biblioteca;

import br.univali.portugol.plugin.gogoboard.modelo.DCMotor;
import br.univali.portugol.plugin.gogoboard.modelo.Motor;
import br.univali.portugol.plugin.gogoboard.modelo.Led;
import br.univali.portugol.plugin.gogoboard.modelo.Sensor;
import br.univali.portugol.nucleo.bibliotecas.base.Biblioteca;
import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.nucleo.bibliotecas.base.TipoBiblioteca;
import br.univali.portugol.nucleo.bibliotecas.base.anotacoes.Autor;
import br.univali.portugol.nucleo.bibliotecas.base.anotacoes.DocumentacaoBiblioteca;
import br.univali.portugol.nucleo.bibliotecas.base.anotacoes.DocumentacaoFuncao;
import br.univali.portugol.nucleo.bibliotecas.base.anotacoes.DocumentacaoParametro;
import br.univali.portugol.nucleo.bibliotecas.base.anotacoes.PropriedadesBiblioteca;
import br.univali.portugol.plugin.gogoboard.GoGoDriver;
import br.univali.portugol.plugin.gogoboard.modelo.Buzzer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ailton Cardoso Jr
 */
@PropriedadesBiblioteca(tipo = TipoBiblioteca.COMPARTILHADA)
@DocumentacaoBiblioteca(
        descricao = "Esta biblioteca permite manipular a controladora GoGo board",
        versao = "0.01"
)
public final class GoGoBoard extends Biblioteca {

    private static List<Sensor> sensores = new ArrayList<Sensor>() {
        {
            add(new Sensor(0));
            add(new Sensor(1));
            add(new Sensor(2));
            add(new Sensor(3));
            add(new Sensor(4));
            add(new Sensor(5));
            add(new Sensor(6));
            add(new Sensor(7));
        }
    };

    private static Motor saidaA;
    private static Motor saidaB;
    private static Motor saidaC;
    private static Motor saidaD;

    private static Led ledUsuario = new Led(0);
    private static Buzzer buzzer = new Buzzer();

    @DocumentacaoFuncao(
            descricao = "Realiza a consulta do valor de um sensor",
            parametros
            = {
                @DocumentacaoParametro(nome = "sensor", descricao = "o numero do sensor desejado")
            },
            retorno = "Valor do sensor",
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public int consultar_sensor(int numSensor) throws ErroExecucaoBiblioteca, InterruptedException {
        return sensores.get(numSensor - 1).getValor();
    }

    @DocumentacaoFuncao(
            descricao = "Ligar os motores especificados or parametro",
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
                    saidaA = new DCMotor(1);
                    ((DCMotor) saidaA).ligar();
                    break;
                case 'b':
                    saidaB = new DCMotor(2);
                    ((DCMotor) saidaB).ligar();
                    break;
                case 'c':
                    saidaC = new DCMotor(4);
                    ((DCMotor) saidaC).ligar();
                    break;
                case 'd':
                    saidaD = new DCMotor(8);
                    ((DCMotor) saidaD).ligar();
                    break;
                default:
                    throw new ErroExecucaoBiblioteca("Somente são aceitos motores A,B,C e D");
            }
            System.out.println("Ligar motor: " + nomeMotor);
        }
        System.out.println("------------------\n");
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
                    if (saidaA != null) {
                        ((DCMotor) saidaA).desligar();
                    }
                    break;
                case 'b':
                    if (saidaB != null) {
                        ((DCMotor) saidaB).desligar();
                    }
                    break;
                case 'c':
                    if (saidaC != null) {
                        ((DCMotor) saidaC).desligar();
                    }
                    break;
                case 'd':
                    if (saidaD != null) {
                        ((DCMotor) saidaD).desligar();
                    }
                    break;
                default:
                    throw new ErroExecucaoBiblioteca("Somente são aceitos motores A,B,C e D");
            }
            System.out.println("Desligar motor: " + nomeMotor);
        }
        System.out.println("------------------\n");
    }

    @DocumentacaoFuncao(
            descricao = "Mudar sentido dos motores especificados por parametro para a direita, ou seja, Sentido Horário",
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
            descricao = "Mudar sentido dos motores especificados por parametro para a direita, ou seja, Sentido Horário",
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
                    if (saidaA != null) {
                        ((DCMotor) saidaA).inverterDirecao();
                    }
                    break;
                case 'b':
                    if (saidaB != null) {
                        ((DCMotor) saidaB).inverterDirecao();
                    }
                    break;
                case 'c':
                    if (saidaC != null) {
                        ((DCMotor) saidaC).inverterDirecao();
                    }
                    break;
                case 'd':
                    if (saidaD != null) {
                        ((DCMotor) saidaD).inverterDirecao();
                    }
                    break;
                default:
                    throw new ErroExecucaoBiblioteca("Somente são aceitos motores A,B,C e D");
            }
            System.out.println("Inverter direção motor: " + nomeMotor);
        }
        System.out.println("------------------\n");
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
                    if (saidaA != null) {
                        ((DCMotor) saidaA).setarForca(forca);
                    }
                    break;
                case 'b':
                    if (saidaB != null) {
                        ((DCMotor) saidaB).setarForca(forca);
                    }
                    break;
                case 'c':
                    if (saidaC != null) {
                        ((DCMotor) saidaC).setarForca(forca);
                    }
                    break;
                case 'd':
                    if (saidaD != null) {
                        ((DCMotor) saidaD).setarForca(forca);
                    }
                    break;
                default:
                    throw new ErroExecucaoBiblioteca("Somente são aceitos motores A,B,C e D");
            }
            System.out.println("Setar força motor: " + nomeMotor);
        }
        System.out.println("------------------\n");
    }

    private void controlarDirecaoMotor(String motores, int direcao) throws ErroExecucaoBiblioteca, InterruptedException {
        motores = motores.toLowerCase();
        for (char nomeMotor : motores.toCharArray()) {
            switch (nomeMotor) {
                case 'a':
                    if (saidaA != null) {
                        ((DCMotor) saidaA).definirDirecao(direcao);
                    }
                    break;
                case 'b':
                    if (saidaB != null) {
                        ((DCMotor) saidaB).definirDirecao(direcao);
                    }
                    break;
                case 'c':
                    if (saidaC != null) {
                        ((DCMotor) saidaC).definirDirecao(direcao);
                    }
                    break;
                case 'd':
                    if (saidaD != null) {
                        ((DCMotor) saidaD).definirDirecao(direcao);
                    }
                    break;
                default:
                    throw new ErroExecucaoBiblioteca("Somente são aceitos motores A,B,C e D");
            }
            System.out.println("Controlar direção motor: " + nomeMotor);
        }
        System.out.println("------------------\n");
    }

    @DocumentacaoFuncao(
            descricao = "Retorna o estado dos motores especificados or parametro",
            parametros
            = {
                @DocumentacaoParametro(nome = "motores", descricao = "as letras correspondentes aos motores desejados \n Ex: \"abcd\"")
            },
            retorno = "Estado do(s) sensor(es)",
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public boolean estado_motores(String motores) throws ErroExecucaoBiblioteca, InterruptedException {
        Motor motor;
        motores = motores.toLowerCase();
        for (char nomeMotor : motores.toCharArray()) {
            switch (nomeMotor) {
                case 'a':
                    motor = saidaA;
                    break;
                case 'b':
                    motor = saidaB;
                    break;
                case 'c':
                    motor = saidaC;
                    break;
                case 'd':
                    motor = saidaD;
                    break;
                default:
                    throw new ErroExecucaoBiblioteca("Somente são aceitos motores A,B,C e D");
            }
            if (motor == null || !motor.isLigado()) {
                return false;
            }
        }
        return true;
    }

    @DocumentacaoFuncao(
            descricao = "Acionar o beep",
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void acionar_beep() throws ErroExecucaoBiblioteca, InterruptedException {
        buzzer.acionarBeep();
    }

    @DocumentacaoFuncao(
            descricao = "Acender e apagar o LED do usuário",
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void acender_led() throws ErroExecucaoBiblioteca, InterruptedException {
        ledUsuario.ligarLed(true);
    }

    @DocumentacaoFuncao(
            descricao = "Acender e apagar o LED do usuário",
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void apagar_led() throws ErroExecucaoBiblioteca, InterruptedException {
        ledUsuario.ligarLed(false);
    }

    @DocumentacaoFuncao(
            descricao = "Exibir texto no painel de segimentos",
            parametros
            = {
                @DocumentacaoParametro(nome = "texto", descricao = "Valor textual de 4 digitos que vai ser exibido no display de seguimentos.\n Deve ser de até 4 digitos.zn Ex: GoGo")
            },
            autores
            = {
                @Autor(nome = "Ailton Cardoso Jr", email = "ailtoncardosojr@edu.univali.br")
            }
    )
    public void exibeTextoCurto(String texto) throws ErroExecucaoBiblioteca, InterruptedException {
        //goGoDriver.exibirTextoCurto(texto);
    }
}
