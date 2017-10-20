package br.univali.portugol.plugin.gogoboard.ui.telas;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.componetes.DispositivoGoGo;
import br.univali.ps.ui.swing.ColorController;
import br.univali.ps.ui.swing.Themeable;
import br.univali.ps.ui.swing.weblaf.WeblafUtils;
import br.univali.ps.ui.utils.FabricaDicasInterface;
import java.awt.Component;
import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JProgressBar;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class JanelaMonitor extends javax.swing.JPanel implements Themeable {

    private boolean atualizar = true;
    private Thread threadAtualizaTela;
    private DispositivoGoGo dispositivoGoGo;

    /**
     * Creates new form JanelaMonitor
     */
    public JanelaMonitor(DispositivoGoGo dispositivoGoGo) {
        initComponents();
        this.dispositivoGoGo = dispositivoGoGo;
        configurarCores();
        criarTooltips();
    }

    public void atualizarComponentes() {
        threadAtualizaTela = new Thread(new Runnable() {
            public void run() {
                labelGoGo.setIcon(getIcone("comGoGo"));
                atualizar = true;
                while (dispositivoGoGo.isConectado() && atualizar) {
                    try {
                        dispositivoGoGo.atualizarComponetes();
                        int i = 0;
                        for (Component component : painelSensor.getComponents()) {
                            JProgressBar pb = ((JProgressBar) component);
                            int valor = dispositivoGoGo.getValorSensor(i, false);
                            pb.setValue(valor);
                            pb.setString(String.valueOf(valor));
                            i++;
                        }
                        labelIR.setText("Código  = " + dispositivoGoGo.getValorRecebidoIR());

                        Thread.sleep(100);
                    } catch (ErroExecucaoBiblioteca | InterruptedException ex) {
                        Logger.getLogger(JanelaMonitor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                zerarBarraSensores();
                labelGoGo.setIcon(getIcone("semGoGo"));
            }
        });
        threadAtualizaTela.start();
    }

    private void zerarBarraSensores() {
        for (Component component : painelSensor.getComponents()) {
            if (component instanceof JProgressBar) {
                JProgressBar pb = ((JProgressBar) component);
                pb.setValue(0);
                pb.setString(String.valueOf(0));
            }
        }
    }

    @Override
    public void configurarCores() {
        painelPrincipal.setBackground(ColorController.FUNDO_CLARO);
        painelMotores.setBackground(ColorController.FUNDO_CLARO);
        painelOutrasAcoes.setBackground(ColorController.FUNDO_CLARO);
        painelMotorDC.setBackground(ColorController.FUNDO_CLARO);
        painelMotorServo.setBackground(ColorController.FUNDO_CLARO);
        painelSensor.setBackground(ColorController.FUNDO_MEDIO);

        // Cor da aba selecionada
        painelTabMotor.setBackground(ColorController.COR_DESTAQUE);
        painelTabMotor.setSelectedTopBg(ColorController.COR_DESTAQUE);
        // Cor da aba em segundo plano
        painelTabMotor.setBottomBg(ColorController.FUNDO_CLARO);
        painelTabMotor.setTopBg(ColorController.FUNDO_CLARO);
        painelTabMotor.setContentBorderColor(ColorController.FUNDO_CLARO);
        // Cor das letras
        painelTabMotor.setForeground(ColorController.COR_LETRA);
        labelLigarMotor.setForeground(ColorController.COR_LETRA);
        labelDesligarMotor.setForeground(ColorController.COR_LETRA);
        labelDireitaMotor.setForeground(ColorController.COR_LETRA);
        labelReverterMotor.setForeground(ColorController.COR_LETRA);
        labelEsquerdaMotor.setForeground(ColorController.COR_LETRA);
        labelSetForcaMotor.setForeground(ColorController.COR_LETRA);

        // Cor letras botoes dos motores
        labelMotorA.setForeground(ColorController.COR_LETRA);
        labelMotorB.setForeground(ColorController.COR_LETRA);
        labelMotorC.setForeground(ColorController.COR_LETRA);
        labelMotorD.setForeground(ColorController.COR_LETRA);

        // Cor Set Forca motor
        textFieldForcaMotor.setBackground(ColorController.FUNDO_CLARO);
        textFieldForcaMotor.setForeground(ColorController.COR_LETRA);

        // Cor Set caracter display
        textFieldSetDisplay.setBackground(ColorController.FUNDO_CLARO);
        textFieldSetDisplay.setForeground(ColorController.COR_LETRA);

        // Cor da barra lado esquerdo
        sliderForcaMotor.setProgressTrackBgBottom(ColorController.COR_DESTAQUE);
        sliderForcaMotor.setProgressTrackBgTop(ColorController.COR_DESTAQUE);
        // Cor da barra lado direito
        sliderForcaMotor.setTrackBgBottom(ColorController.FUNDO_CLARO);
        sliderForcaMotor.setTrackBgTop(ColorController.FUNDO_CLARO);
        // Cor do pino
        sliderForcaMotor.setThumbBgBottom(ColorController.FUNDO_MEDIO);
        sliderForcaMotor.setThumbBgTop(ColorController.FUNDO_MEDIO);

        // Cor da label do display de segmentos
        labelTituloDisplay.setBackground(ColorController.COR_DESTAQUE);
        labelTituloDisplay.setForeground(ColorController.COR_LETRA);
        // Cor da label das ações extras
        labelBeep.setForeground(ColorController.COR_LETRA);
        labelLed.setForeground(ColorController.COR_LETRA);
        labelIR.setForeground(ColorController.COR_LETRA);
        labelSetDisplay.setForeground(ColorController.COR_LETRA);

        if (WeblafUtils.weblafEstaInstalado()) {
            // Botoes acao dos motores
            WeblafUtils.configurarBotao(botaoMotorOn, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, 0);
            WeblafUtils.configurarBotao(botaoMotorOff, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, 0);
            WeblafUtils.configurarBotao(botaoMotorEsquerda, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, 0);
            WeblafUtils.configurarBotao(botaoMotorReverte, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, 0);
            WeblafUtils.configurarBotao(botaoMotorDireita, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, 0);
            WeblafUtils.configurarBotao(botaoMotorForca, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, 0);
            // Botoes motores
            WeblafUtils.configurarToogleBotao(botaoMotorA, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, 0);
            WeblafUtils.configurarToogleBotao(botaoMotorB, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, 0);
            WeblafUtils.configurarToogleBotao(botaoMotorC, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, 0);
            WeblafUtils.configurarToogleBotao(botaoMotorD, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, 0);
            // Botoes outras acoes
            WeblafUtils.configurarBotao(botaoBeep, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, 0);
            WeblafUtils.configurarToogleBotao(botaoLedOn, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, 0);
            WeblafUtils.configurarBotao(botaoSetDisplay, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, ColorController.FUNDO_CLARO, 0);

            //configuraBarraDeProgresso(jProgressBar1);
        }
    }

    private void criarTooltips() {
        FabricaDicasInterface.criarTooltip(botaoMotorOn, "Ligar motores selecionados");
        FabricaDicasInterface.criarTooltip(textFieldForcaMotor, "Força do motor");
        FabricaDicasInterface.criarTooltip(textFieldSetDisplay, "Letras/Numeros");
    }

    //Exemplo retirado do WeblafUtils
    /*public static void configuraBarraDeProgresso(JProgressBar field) {
        ((WebProgressBarUI) field.getUI()).setProgressTopColor(ColorController.PROGRESS_BAR);
        ((WebProgressBarUI) field.getUI()).setProgressBottomColor(ColorController.PROGRESS_BAR);
        ((WebProgressBarUI) field.getUI()).setBgBottom(ColorController.FUNDO_ESCURO);
        ((WebProgressBarUI) field.getUI()).setBgTop(ColorController.FUNDO_ESCURO);
        ((WebProgressBarUI) field.getUI()).setIndeterminateBorder(null);
        ((WebProgressBarUI) field.getUI()).setPaintIndeterminateBorder(false);
        ((WebProgressBarUI) field.getUI()).setInnerRound(0);
        ((WebProgressBarUI) field.getUI()).setRound(0);
        ((WebProgressBarUI) field.getUI()).setHighlightWhite(ColorController.PROGRESS_BAR);
        ((WebProgressBarUI) field.getUI()).setShadeWidth(0);
        ((WebProgressBarUI) field.getUI()).setHighlightDarkWhite(ColorController.PROGRESS_BAR);
        field.setBorder(new EmptyBorder(15, 15, 15, 15));
        field.setOpaque(true);
        field.setBackground(ColorController.COR_DESTAQUE);
        field.setForeground(ColorController.COR_LETRA);
        field.setBorderPainted(false);
    }*/
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        painelPrincipal = new javax.swing.JPanel();
        painelSensor = new javax.swing.JPanel();
        progressBarSensor1 = new javax.swing.JProgressBar(progressBarSensor1.VERTICAL);
        progressBarSensor2 = new javax.swing.JProgressBar(progressBarSensor1.VERTICAL);
        progressBarSensor3 = new javax.swing.JProgressBar(progressBarSensor1.VERTICAL);
        progressBarSensor4 = new javax.swing.JProgressBar(progressBarSensor1.VERTICAL);
        progressBarSensor5 = new javax.swing.JProgressBar(progressBarSensor1.VERTICAL);
        progressBarSensor6 = new javax.swing.JProgressBar(progressBarSensor1.VERTICAL);
        progressBarSensor7 = new javax.swing.JProgressBar(progressBarSensor1.VERTICAL);
        progressBarSensor8 = new javax.swing.JProgressBar(progressBarSensor1.VERTICAL);
        painelTabMotor = new com.alee.laf.tabbedpane.WebTabbedPane();
        painelMotorDC = new javax.swing.JPanel();
        botaoMotorOn = new com.alee.laf.button.WebButton();
        botaoMotorOff = new com.alee.laf.button.WebButton();
        botaoMotorEsquerda = new com.alee.laf.button.WebButton();
        botaoMotorReverte = new com.alee.laf.button.WebButton();
        botaoMotorDireita = new com.alee.laf.button.WebButton();
        botaoMotorForca = new com.alee.laf.button.WebButton();
        sliderForcaMotor = new com.alee.laf.slider.WebSlider();
        labelLigarMotor = new javax.swing.JLabel();
        labelDesligarMotor = new javax.swing.JLabel();
        labelEsquerdaMotor = new javax.swing.JLabel();
        labelReverterMotor = new javax.swing.JLabel();
        labelDireitaMotor = new javax.swing.JLabel();
        textFieldForcaMotor = new com.alee.laf.text.WebTextField();
        labelSetForcaMotor = new javax.swing.JLabel();
        separadorForcaMotor = new com.alee.laf.separator.WebSeparator();
        painelMotorServo = new javax.swing.JPanel();
        painelMotores = new javax.swing.JPanel();
        labelGoGo = new javax.swing.JLabel();
        labelMotorA = new javax.swing.JLabel();
        labelMotorB = new javax.swing.JLabel();
        labelMotorC = new javax.swing.JLabel();
        labelMotorD = new javax.swing.JLabel();
        botaoMotorA = new com.alee.laf.button.WebToggleButton();
        botaoMotorB = new com.alee.laf.button.WebToggleButton();
        botaoMotorC = new com.alee.laf.button.WebToggleButton();
        botaoMotorD = new com.alee.laf.button.WebToggleButton();
        painelOutrasAcoes = new javax.swing.JPanel();
        botaoBeep = new com.alee.laf.button.WebButton();
        labelIconeIR = new javax.swing.JLabel();
        textFieldSetDisplay = new com.alee.laf.text.WebTextField();
        separadorInputDisplay = new com.alee.laf.separator.WebSeparator();
        botaoSetDisplay = new com.alee.laf.button.WebButton();
        labelSetDisplay = new javax.swing.JLabel();
        labelTituloDisplay = new javax.swing.JLabel();
        labelBeep = new javax.swing.JLabel();
        labelLed = new javax.swing.JLabel();
        labelIR = new javax.swing.JLabel();
        botaoLedOn = new com.alee.laf.button.WebToggleButton();

        painelPrincipal.setDoubleBuffered(false);
        painelPrincipal.setFocusable(false);
        painelPrincipal.setPreferredSize(new java.awt.Dimension(1200, 614));

        progressBarSensor1.setBackground(new java.awt.Color(102, 255, 102));
        progressBarSensor1.setForeground(new java.awt.Color(153, 153, 255));
        progressBarSensor1.setMaximum(1024);
        progressBarSensor1.setOrientation(1);
        progressBarSensor1.setBorder(null);
        progressBarSensor1.setBorderPainted(false);
        progressBarSensor1.setEnabled(false);
        progressBarSensor1.setFocusable(false);
        progressBarSensor1.setName(""); // NOI18N
        progressBarSensor1.setRequestFocusEnabled(false);
        progressBarSensor1.setString("0");
        progressBarSensor1.setStringPainted(true);

        progressBarSensor2.setForeground(new java.awt.Color(153, 153, 255));
        progressBarSensor2.setMaximum(1024);
        progressBarSensor2.setOrientation(1);
        progressBarSensor2.setBorder(null);
        progressBarSensor2.setEnabled(false);
        progressBarSensor2.setFocusable(false);
        progressBarSensor2.setName(""); // NOI18N
        progressBarSensor2.setRequestFocusEnabled(false);
        progressBarSensor2.setString("0");
        progressBarSensor2.setStringPainted(true);

        progressBarSensor3.setForeground(new java.awt.Color(153, 153, 255));
        progressBarSensor3.setMaximum(1024);
        progressBarSensor3.setOrientation(1);
        progressBarSensor3.setBorder(null);
        progressBarSensor3.setEnabled(false);
        progressBarSensor3.setFocusable(false);
        progressBarSensor3.setName(""); // NOI18N
        progressBarSensor3.setRequestFocusEnabled(false);
        progressBarSensor3.setString("0");
        progressBarSensor3.setStringPainted(true);

        progressBarSensor4.setForeground(new java.awt.Color(153, 153, 255));
        progressBarSensor4.setMaximum(1024);
        progressBarSensor4.setOrientation(1);
        progressBarSensor4.setBorder(null);
        progressBarSensor4.setEnabled(false);
        progressBarSensor4.setFocusable(false);
        progressBarSensor4.setName(""); // NOI18N
        progressBarSensor4.setRequestFocusEnabled(false);
        progressBarSensor4.setString("0");
        progressBarSensor4.setStringPainted(true);

        progressBarSensor5.setForeground(new java.awt.Color(153, 153, 255));
        progressBarSensor5.setMaximum(1024);
        progressBarSensor5.setOrientation(1);
        progressBarSensor5.setBorder(null);
        progressBarSensor5.setEnabled(false);
        progressBarSensor5.setFocusable(false);
        progressBarSensor5.setName(""); // NOI18N
        progressBarSensor5.setRequestFocusEnabled(false);
        progressBarSensor5.setString("0");
        progressBarSensor5.setStringPainted(true);

        progressBarSensor6.setForeground(new java.awt.Color(153, 153, 255));
        progressBarSensor6.setMaximum(1024);
        progressBarSensor6.setOrientation(1);
        progressBarSensor6.setBorder(null);
        progressBarSensor6.setEnabled(false);
        progressBarSensor6.setFocusable(false);
        progressBarSensor6.setName(""); // NOI18N
        progressBarSensor6.setRequestFocusEnabled(false);
        progressBarSensor6.setString("0");
        progressBarSensor6.setStringPainted(true);

        progressBarSensor7.setForeground(new java.awt.Color(153, 153, 255));
        progressBarSensor7.setMaximum(1024);
        progressBarSensor7.setOrientation(1);
        progressBarSensor7.setBorder(null);
        progressBarSensor7.setEnabled(false);
        progressBarSensor7.setFocusable(false);
        progressBarSensor7.setName(""); // NOI18N
        progressBarSensor7.setRequestFocusEnabled(false);
        progressBarSensor7.setString("0");
        progressBarSensor7.setStringPainted(true);

        progressBarSensor8.setForeground(new java.awt.Color(153, 153, 255));
        progressBarSensor8.setMaximum(1024);
        progressBarSensor8.setOrientation(1);
        progressBarSensor8.setBorder(null);
        progressBarSensor8.setEnabled(false);
        progressBarSensor8.setFocusable(false);
        progressBarSensor8.setName(""); // NOI18N
        progressBarSensor8.setRequestFocusEnabled(false);
        progressBarSensor8.setString("0");
        progressBarSensor8.setStringPainted(true);

        javax.swing.GroupLayout painelSensorLayout = new javax.swing.GroupLayout(painelSensor);
        painelSensor.setLayout(painelSensorLayout);
        painelSensorLayout.setHorizontalGroup(
            painelSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(painelSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(painelSensorLayout.createSequentialGroup()
                    .addGap(27, 27, 27)
                    .addComponent(progressBarSensor1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(progressBarSensor2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(progressBarSensor3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(progressBarSensor4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(progressBarSensor5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(progressBarSensor6, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(progressBarSensor7, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(progressBarSensor8, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        painelSensorLayout.setVerticalGroup(
            painelSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 271, Short.MAX_VALUE)
            .addGroup(painelSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(painelSensorLayout.createSequentialGroup()
                    .addGap(11, 11, 11)
                    .addGroup(painelSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(progressBarSensor1, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(progressBarSensor2, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(progressBarSensor3, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(progressBarSensor4, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(progressBarSensor5, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(progressBarSensor6, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(progressBarSensor7, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(progressBarSensor8, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(12, Short.MAX_VALUE)))
        );

        painelTabMotor.setToolTipText("");
        painelTabMotor.setFont(new java.awt.Font("Dialog", 0, 22)); // NOI18N
        painelTabMotor.setTabbedPaneStyle(com.alee.laf.tabbedpane.TabbedPaneStyle.attached);

        botaoMotorOn.setBorder(null);
        botaoMotorOn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/univali/portugol/plugin/gogoboard/imagens/monitor/liga.png"))); // NOI18N
        botaoMotorOn.setToolTipText("");
        botaoMotorOn.setContentAreaFilled(true);
        botaoMotorOn.setDisabledIcon(null);
        botaoMotorOn.setFocusable(false);
        botaoMotorOn.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        botaoMotorOn.setOpaque(false);
        botaoMotorOn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoMotorOnActionPerformed(evt);
            }
        });

        botaoMotorOff.setBorder(null);
        botaoMotorOff.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/univali/portugol/plugin/gogoboard/imagens/monitor/desliga.png"))); // NOI18N
        botaoMotorOff.setToolTipText("");
        botaoMotorOff.setContentAreaFilled(true);
        botaoMotorOff.setDisabledIcon(null);
        botaoMotorOff.setFocusable(false);
        botaoMotorOff.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        botaoMotorOff.setOpaque(false);

        botaoMotorEsquerda.setBorder(null);
        botaoMotorEsquerda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/univali/portugol/plugin/gogoboard/imagens/monitor/esquerda_sel.png"))); // NOI18N
        botaoMotorEsquerda.setToolTipText("");
        botaoMotorEsquerda.setContentAreaFilled(true);
        botaoMotorEsquerda.setFocusable(false);
        botaoMotorEsquerda.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        botaoMotorEsquerda.setOpaque(false);

        botaoMotorReverte.setBorder(null);
        botaoMotorReverte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/univali/portugol/plugin/gogoboard/imagens/monitor/reverter.png"))); // NOI18N
        botaoMotorReverte.setToolTipText("");
        botaoMotorReverte.setContentAreaFilled(true);
        botaoMotorReverte.setDisabledIcon(null);
        botaoMotorReverte.setFocusable(false);
        botaoMotorReverte.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        botaoMotorReverte.setOpaque(false);

        botaoMotorDireita.setBorder(null);
        botaoMotorDireita.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/univali/portugol/plugin/gogoboard/imagens/monitor/direita.png"))); // NOI18N
        botaoMotorDireita.setToolTipText("");
        botaoMotorDireita.setContentAreaFilled(true);
        botaoMotorDireita.setDisabledIcon(null);
        botaoMotorDireita.setFocusable(false);
        botaoMotorDireita.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        botaoMotorDireita.setOpaque(false);

        botaoMotorForca.setBorder(null);
        botaoMotorForca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/univali/portugol/plugin/gogoboard/imagens/monitor/ok.png"))); // NOI18N
        botaoMotorForca.setToolTipText("");
        botaoMotorForca.setContentAreaFilled(true);
        botaoMotorForca.setFocusable(false);
        botaoMotorForca.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        botaoMotorForca.setOpaque(false);

        sliderForcaMotor.setBorder(null);
        sliderForcaMotor.setToolTipText("");
        sliderForcaMotor.setFocusable(false);
        sliderForcaMotor.setTrackShadeWidth(0);
        sliderForcaMotor.setVerifyInputWhenFocusTarget(false);
        sliderForcaMotor.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderForcaMotorStateChanged(evt);
            }
        });

        labelLigarMotor.setText("Ligar");

        labelDesligarMotor.setText("Desligar");

        labelEsquerdaMotor.setText("Esquerda");

        labelReverterMotor.setText("Reverter");

        labelDireitaMotor.setText("Direita");

        textFieldForcaMotor.setForeground(new java.awt.Color(255, 255, 255));
        textFieldForcaMotor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldForcaMotor.setText("50");
        textFieldForcaMotor.setToolTipText("");
        textFieldForcaMotor.setDrawBackground(false);
        textFieldForcaMotor.setDrawFocus(false);
        textFieldForcaMotor.setDrawShade(false);
        textFieldForcaMotor.setFont(new java.awt.Font("Dialog", 0, 22)); // NOI18N
        textFieldForcaMotor.setSelectionColor(new java.awt.Color(0, 153, 153));
        textFieldForcaMotor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textFieldForcaMotorActionPerformed(evt);
            }
        });
        textFieldForcaMotor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textFieldForcaMotorKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textFieldForcaMotorKeyTyped(evt);
            }
        });

        labelSetForcaMotor.setText("Set Força");

        javax.swing.GroupLayout painelMotorDCLayout = new javax.swing.GroupLayout(painelMotorDC);
        painelMotorDC.setLayout(painelMotorDCLayout);
        painelMotorDCLayout.setHorizontalGroup(
            painelMotorDCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelMotorDCLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(painelMotorDCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, painelMotorDCLayout.createSequentialGroup()
                        .addComponent(sliderForcaMotor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addGroup(painelMotorDCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(separadorForcaMotor, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                            .addComponent(textFieldForcaMotor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(24, 24, 24)
                        .addGroup(painelMotorDCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelSetForcaMotor, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botaoMotorForca, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, painelMotorDCLayout.createSequentialGroup()
                        .addGroup(painelMotorDCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelMotorDCLayout.createSequentialGroup()
                                .addComponent(botaoMotorOn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(painelMotorDCLayout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(labelLigarMotor)
                                .addGap(28, 28, 28)))
                        .addGroup(painelMotorDCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botaoMotorOff, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(painelMotorDCLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(labelDesligarMotor)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(painelMotorDCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(painelMotorDCLayout.createSequentialGroup()
                                .addComponent(botaoMotorEsquerda, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(botaoMotorReverte, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(painelMotorDCLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(labelEsquerdaMotor)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(labelReverterMotor)))
                        .addGroup(painelMotorDCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(painelMotorDCLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(botaoMotorDireita, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(painelMotorDCLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(labelDireitaMotor, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        painelMotorDCLayout.setVerticalGroup(
            painelMotorDCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelMotorDCLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelMotorDCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(botaoMotorOff, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botaoMotorDireita, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botaoMotorReverte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botaoMotorEsquerda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botaoMotorOn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painelMotorDCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelLigarMotor)
                    .addComponent(labelDesligarMotor)
                    .addComponent(labelEsquerdaMotor)
                    .addComponent(labelReverterMotor)
                    .addComponent(labelDireitaMotor))
                .addGroup(painelMotorDCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelMotorDCLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(sliderForcaMotor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelMotorDCLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                        .addGroup(painelMotorDCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(botaoMotorForca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(painelMotorDCLayout.createSequentialGroup()
                                .addComponent(textFieldForcaMotor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(separadorForcaMotor, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(8, 8, 8)
                        .addComponent(labelSetForcaMotor)
                        .addContainerGap())))
        );

        painelTabMotor.addTab("      Motor DC      ", painelMotorDC);

        javax.swing.GroupLayout painelMotorServoLayout = new javax.swing.GroupLayout(painelMotorServo);
        painelMotorServo.setLayout(painelMotorServoLayout);
        painelMotorServoLayout.setHorizontalGroup(
            painelMotorServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 384, Short.MAX_VALUE)
        );
        painelMotorServoLayout.setVerticalGroup(
            painelMotorServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 203, Short.MAX_VALUE)
        );

        painelTabMotor.addTab("      Motor Servo      ", painelMotorServo);

        labelGoGo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/univali/portugol/plugin/gogoboard/imagens/monitor/semGoGo.png"))); // NOI18N

        labelMotorA.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        labelMotorA.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMotorA.setText("A  = 50");

        labelMotorB.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        labelMotorB.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMotorB.setText("B  = 50");

        labelMotorC.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        labelMotorC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMotorC.setText("C  = 50");

        labelMotorD.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        labelMotorD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMotorD.setText("D  = 50");

        botaoMotorA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/univali/portugol/plugin/gogoboard/imagens/monitor/esquerda.png"))); // NOI18N
        botaoMotorA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoMotorAActionPerformed(evt);
            }
        });

        botaoMotorB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/univali/portugol/plugin/gogoboard/imagens/monitor/esquerda.png"))); // NOI18N
        botaoMotorB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoMotorBActionPerformed(evt);
            }
        });

        botaoMotorC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/univali/portugol/plugin/gogoboard/imagens/monitor/esquerda.png"))); // NOI18N
        botaoMotorC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoMotorCActionPerformed(evt);
            }
        });

        botaoMotorD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/univali/portugol/plugin/gogoboard/imagens/monitor/esquerda.png"))); // NOI18N
        botaoMotorD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoMotorDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout painelMotoresLayout = new javax.swing.GroupLayout(painelMotores);
        painelMotores.setLayout(painelMotoresLayout);
        painelMotoresLayout.setHorizontalGroup(
            painelMotoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelMotoresLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(painelMotoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelGoGo)
                    .addGroup(painelMotoresLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(painelMotoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(botaoMotorA, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelMotorA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(painelMotoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(botaoMotorB, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelMotorB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(painelMotoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(botaoMotorC, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelMotorC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(painelMotoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(botaoMotorD, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelMotorD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(40, 40, 40))
        );
        painelMotoresLayout.setVerticalGroup(
            painelMotoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelMotoresLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(painelMotoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botaoMotorA, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelMotoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(botaoMotorB, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(botaoMotorC, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(botaoMotorD, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painelMotoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelMotorA)
                    .addComponent(labelMotorB)
                    .addComponent(labelMotorC)
                    .addComponent(labelMotorD))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelGoGo, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        botaoBeep.setBorder(null);
        botaoBeep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/univali/portugol/plugin/gogoboard/imagens/monitor/beep.png"))); // NOI18N
        botaoBeep.setToolTipText("");
        botaoBeep.setContentAreaFilled(true);
        botaoBeep.setFocusable(false);
        botaoBeep.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        botaoBeep.setOpaque(false);
        botaoBeep.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/br/univali/portugol/plugin/gogoboard/imagens/monitor/beep_pres.png"))); // NOI18N
        botaoBeep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoBeepActionPerformed(evt);
            }
        });

        labelIconeIR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/univali/portugol/plugin/gogoboard/imagens/monitor/controle_remoto.png"))); // NOI18N
        labelIconeIR.setToolTipText("");

        textFieldSetDisplay.setForeground(new java.awt.Color(255, 255, 255));
        textFieldSetDisplay.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldSetDisplay.setText("50");
        textFieldSetDisplay.setToolTipText("");
        textFieldSetDisplay.setDrawBackground(false);
        textFieldSetDisplay.setDrawFocus(false);
        textFieldSetDisplay.setDrawShade(false);
        textFieldSetDisplay.setFont(new java.awt.Font("Dialog", 0, 22)); // NOI18N
        textFieldSetDisplay.setSelectionColor(new java.awt.Color(0, 153, 153));
        textFieldSetDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textFieldSetDisplayActionPerformed(evt);
            }
        });
        textFieldSetDisplay.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textFieldSetDisplayKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textFieldSetDisplayKeyTyped(evt);
            }
        });

        botaoSetDisplay.setBorder(null);
        botaoSetDisplay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/univali/portugol/plugin/gogoboard/imagens/monitor/ok.png"))); // NOI18N
        botaoSetDisplay.setToolTipText("");
        botaoSetDisplay.setContentAreaFilled(true);
        botaoSetDisplay.setFocusable(false);
        botaoSetDisplay.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        botaoSetDisplay.setOpaque(false);
        botaoSetDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoSetDisplayActionPerformed(evt);
            }
        });

        labelSetDisplay.setText("Exibir");

        labelTituloDisplay.setFont(new java.awt.Font("Dialog", 0, 22)); // NOI18N
        labelTituloDisplay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTituloDisplay.setText("Display 7 seguimentos");
        labelTituloDisplay.setOpaque(true);

        labelBeep.setText("Beep");

        labelLed.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelLed.setText("Ligar Led");

        labelIR.setText("Código  = 0");

        botaoLedOn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/univali/portugol/plugin/gogoboard/imagens/monitor/led_on.png"))); // NOI18N
        botaoLedOn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoLedOnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout painelOutrasAcoesLayout = new javax.swing.GroupLayout(painelOutrasAcoes);
        painelOutrasAcoes.setLayout(painelOutrasAcoesLayout);
        painelOutrasAcoesLayout.setHorizontalGroup(
            painelOutrasAcoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelOutrasAcoesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelOutrasAcoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelOutrasAcoesLayout.createSequentialGroup()
                        .addGroup(painelOutrasAcoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botaoBeep, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(painelOutrasAcoesLayout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(labelBeep)))
                        .addGroup(painelOutrasAcoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(painelOutrasAcoesLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelLed, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(painelOutrasAcoesLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(botaoLedOn, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(painelOutrasAcoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelIR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(painelOutrasAcoesLayout.createSequentialGroup()
                                .addComponent(labelIconeIR)
                                .addGap(0, 23, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelOutrasAcoesLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(painelOutrasAcoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(textFieldSetDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(separadorInputDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(painelOutrasAcoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(painelOutrasAcoesLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(botaoSetDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(painelOutrasAcoesLayout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(labelSetDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(labelTituloDisplay, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        painelOutrasAcoesLayout.setVerticalGroup(
            painelOutrasAcoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelOutrasAcoesLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(painelOutrasAcoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, painelOutrasAcoesLayout.createSequentialGroup()
                        .addComponent(labelIconeIR)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(botaoBeep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botaoLedOn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painelOutrasAcoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelBeep)
                    .addComponent(labelLed)
                    .addComponent(labelIR))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelTituloDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(painelOutrasAcoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelOutrasAcoesLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldSetDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(separadorInputDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelOutrasAcoesLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(botaoSetDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelSetDisplay)))
                .addGap(40, 40, 40))
        );

        javax.swing.GroupLayout painelPrincipalLayout = new javax.swing.GroupLayout(painelPrincipal);
        painelPrincipal.setLayout(painelPrincipalLayout);
        painelPrincipalLayout.setHorizontalGroup(
            painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelPrincipalLayout.createSequentialGroup()
                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelPrincipalLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(painelSensor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(painelPrincipalLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(painelTabMotor, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(painelMotores, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(painelOutrasAcoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(515, 515, 515))
        );
        painelPrincipalLayout.setVerticalGroup(
            painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelPrincipalLayout.createSequentialGroup()
                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelPrincipalLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(painelTabMotor, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(painelMotores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(painelPrincipalLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(painelOutrasAcoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(painelSensor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(painelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 1073, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(painelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void botaoMotorOnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoMotorOnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botaoMotorOnActionPerformed

    private void textFieldForcaMotorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldForcaMotorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textFieldForcaMotorActionPerformed

    private void sliderForcaMotorStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderForcaMotorStateChanged
        textFieldForcaMotor.setText(Integer.toString(sliderForcaMotor.getValue()));
    }//GEN-LAST:event_sliderForcaMotorStateChanged

    private void textFieldForcaMotorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldForcaMotorKeyReleased
        try {
            int forca = Integer.parseInt(textFieldForcaMotor.getText());
            if (forca <= 100) {
                sliderForcaMotor.setValue(forca);
            } else {
                sliderForcaMotor.setValue(100);
                textFieldForcaMotor.setText(String.valueOf(100));
            }
        } catch (NumberFormatException ex) {
            sliderForcaMotor.setValue(0);
            textFieldForcaMotor.setText(String.valueOf(0));
        }
    }//GEN-LAST:event_textFieldForcaMotorKeyReleased

    private void textFieldForcaMotorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldForcaMotorKeyTyped
        String caracteresAceitos = "0987654321";
        if (!caracteresAceitos.contains(String.valueOf(evt.getKeyChar()))) {
            evt.consume();
        }
    }//GEN-LAST:event_textFieldForcaMotorKeyTyped

    private void botaoMotorDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoMotorDActionPerformed
        if (dispositivoGoGo.isConectado()) {
            if (botaoMotorD.isSelected()) {
                botaoMotorD.setIcon(getIcone("esquerda_sel"));
            } else {
                botaoMotorD.setIcon(getIcone("esquerda"));
            }
        }
    }//GEN-LAST:event_botaoMotorDActionPerformed

    private void botaoMotorBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoMotorBActionPerformed
        if (dispositivoGoGo.isConectado()) {
            if (botaoMotorB.isSelected()) {
                botaoMotorB.setIcon(getIcone("esquerda_sel"));
            } else {
                botaoMotorB.setIcon(getIcone("esquerda"));
            }
        }
    }//GEN-LAST:event_botaoMotorBActionPerformed

    private void botaoMotorCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoMotorCActionPerformed
        if (dispositivoGoGo.isConectado()) {
            if (botaoMotorC.isSelected()) {
                botaoMotorC.setIcon(getIcone("esquerda_sel"));
            } else {
                botaoMotorC.setIcon(getIcone("esquerda"));
            }
        }
    }//GEN-LAST:event_botaoMotorCActionPerformed

    private void botaoMotorAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoMotorAActionPerformed
        if (dispositivoGoGo.isConectado()) {
            if (botaoMotorA.isSelected()) {
                botaoMotorA.setIcon(getIcone("esquerda_sel"));
            } else {
                botaoMotorA.setIcon(getIcone("esquerda"));
            }
        }
    }//GEN-LAST:event_botaoMotorAActionPerformed

    private void textFieldSetDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldSetDisplayActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textFieldSetDisplayActionPerformed

    private void textFieldSetDisplayKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldSetDisplayKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_textFieldSetDisplayKeyReleased

    private void textFieldSetDisplayKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldSetDisplayKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_textFieldSetDisplayKeyTyped

    private void botaoLedOnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoLedOnActionPerformed

        if (dispositivoGoGo.isConectado()) {
            try {
                if (botaoLedOn.isSelected()) {
                    botaoLedOn.setIcon(getIcone("led_off"));
                    labelLed.setText("Desigar Led");
                    dispositivoGoGo.controlarLed(1);
                } else {
                    botaoLedOn.setIcon(getIcone("led_on"));
                    labelLed.setText("Ligar Led");
                    dispositivoGoGo.controlarLed(0);
                }
            } catch (ErroExecucaoBiblioteca ex) {
                Logger.getLogger(JanelaMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_botaoLedOnActionPerformed

    private void botaoBeepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoBeepActionPerformed
        if (dispositivoGoGo.isConectado()) {
            try {
                dispositivoGoGo.acionarBeep();
            } catch (ErroExecucaoBiblioteca ex) {
                Logger.getLogger(JanelaMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_botaoBeepActionPerformed

    private void botaoSetDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoSetDisplayActionPerformed
        if (dispositivoGoGo.isConectado()) {
            try {
                dispositivoGoGo.exibirTextoCurto(textFieldSetDisplay.getText());
            } catch (ErroExecucaoBiblioteca ex) {
                Logger.getLogger(JanelaMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_botaoSetDisplayActionPerformed

    private ImageIcon getIcone(String nome) {
        try {
            String caminho = "br/univali/portugol/plugin/gogoboard/imagens/monitor/" + nome + ".png";
            Image imagem = ImageIO.read(JanelaMonitor.class.getClassLoader().getResourceAsStream(caminho));
            return new ImageIcon(imagem);
        } catch (IOException ex) {
            System.err.println("Erro ao carregar o icone do plugin na ação Compilar Logo");
            return null;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.alee.laf.button.WebButton botaoBeep;
    private com.alee.laf.button.WebToggleButton botaoLedOn;
    private com.alee.laf.button.WebToggleButton botaoMotorA;
    private com.alee.laf.button.WebToggleButton botaoMotorB;
    private com.alee.laf.button.WebToggleButton botaoMotorC;
    private com.alee.laf.button.WebToggleButton botaoMotorD;
    private com.alee.laf.button.WebButton botaoMotorDireita;
    private com.alee.laf.button.WebButton botaoMotorEsquerda;
    private com.alee.laf.button.WebButton botaoMotorForca;
    private com.alee.laf.button.WebButton botaoMotorOff;
    private com.alee.laf.button.WebButton botaoMotorOn;
    private com.alee.laf.button.WebButton botaoMotorReverte;
    private com.alee.laf.button.WebButton botaoSetDisplay;
    private javax.swing.JLabel labelBeep;
    private javax.swing.JLabel labelDesligarMotor;
    private javax.swing.JLabel labelDireitaMotor;
    private javax.swing.JLabel labelEsquerdaMotor;
    private javax.swing.JLabel labelGoGo;
    private javax.swing.JLabel labelIR;
    private javax.swing.JLabel labelIconeIR;
    private javax.swing.JLabel labelLed;
    private javax.swing.JLabel labelLigarMotor;
    private javax.swing.JLabel labelMotorA;
    private javax.swing.JLabel labelMotorB;
    private javax.swing.JLabel labelMotorC;
    private javax.swing.JLabel labelMotorD;
    private javax.swing.JLabel labelReverterMotor;
    private javax.swing.JLabel labelSetDisplay;
    private javax.swing.JLabel labelSetForcaMotor;
    private javax.swing.JLabel labelTituloDisplay;
    private javax.swing.JPanel painelMotorDC;
    private javax.swing.JPanel painelMotorServo;
    private javax.swing.JPanel painelMotores;
    private javax.swing.JPanel painelOutrasAcoes;
    private javax.swing.JPanel painelPrincipal;
    private javax.swing.JPanel painelSensor;
    private com.alee.laf.tabbedpane.WebTabbedPane painelTabMotor;
    private javax.swing.JProgressBar progressBarSensor1;
    private javax.swing.JProgressBar progressBarSensor2;
    private javax.swing.JProgressBar progressBarSensor3;
    private javax.swing.JProgressBar progressBarSensor4;
    private javax.swing.JProgressBar progressBarSensor5;
    private javax.swing.JProgressBar progressBarSensor6;
    private javax.swing.JProgressBar progressBarSensor7;
    private javax.swing.JProgressBar progressBarSensor8;
    private com.alee.laf.separator.WebSeparator separadorForcaMotor;
    private com.alee.laf.separator.WebSeparator separadorInputDisplay;
    private com.alee.laf.slider.WebSlider sliderForcaMotor;
    private com.alee.laf.text.WebTextField textFieldForcaMotor;
    private com.alee.laf.text.WebTextField textFieldSetDisplay;
    // End of variables declaration//GEN-END:variables

    public void interromperThread() {
        if (threadAtualizaTela != null) {
            threadAtualizaTela.interrupt();
            threadAtualizaTela = null;
            atualizar = false;
        }
    }
}
