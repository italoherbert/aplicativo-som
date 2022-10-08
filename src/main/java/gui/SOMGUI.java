package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import som.SOMConfig;
import gui.config.SOMConfigGUI;
import gui.desenho.DesenhoPNL;

public class SOMGUI extends JFrame implements ActionListener, WindowListener {
    
    public static final int DEFAULT_LARGURA = 800;
    public static final int DEFAULT_ALTURA = 650;
    
    public final static String PAUSAR = "Pausar";
    public final static String CONTINUAR = "Continuar";
    
    public final static int CARREGAR_BT = 1;
    public final static int REINICIAR_BT = 2;
    
    public final static String DESENHO_PNL = "desenho";
    public final static String DESENHO_COMPONENT = "desenho-component";
            
    private final SOMInfoPNL infoPNL = new SOMInfoPNL();
    private JSlider delaySlider;
    private JButton carregarBT = null;
    private JButton configurarBT;
    private JButton randomDadosBT;
    private JButton executarBT;  
    private JButton pausarContinuarBT;

    private JLabel infoLBV;
    
    private final DesenhoPNL desenhoPNL = new DesenhoPNL();    
    private JComponent desenhoComponent = null;
    
    private final CardLayout desenhoCardLayout = new CardLayout();
    private final JPanel desenhoCardPNL = new JPanel( desenhoCardLayout );
    
    private JComponent desenhoPNLOuComponent = desenhoPNL;
    
    private SOMGUIListener listener;

    public SOMGUI( String titulo ) {
        this( titulo, DEFAULT_LARGURA, DEFAULT_ALTURA );
    }

    public SOMGUI( String titulo, int w, int h ) {
        desenhoCardPNL.add( desenhoPNL, DESENHO_PNL );
        desenhoCardPNL.setBorder( BorderFactory.createEtchedBorder() ); 
                
        desenhoPNL.setBackground( Color.WHITE );
        
        infoPNL.setBorder( new TitledBorder( "Informações" ) ); 
        
        delaySlider = new JSlider( JSlider.VERTICAL, 0, 100, 30 );
        delaySlider.setMajorTickSpacing( 10 );
        delaySlider.setMinorTickSpacing( 1 );
        delaySlider.setPaintTicks( true );
        delaySlider.setPaintTrack( true );
        delaySlider.setPaintLabels( true ); 
        delaySlider.setBorder( new TitledBorder( "Delay (MS)" ) ); 
                
        configurarBT = new JButton( "Configurar" );
        carregarBT = new JButton( "Carregar" );        
        randomDadosBT = new JButton( "Gera dados" );
        executarBT = new JButton( "Executar" );
        pausarContinuarBT = new JButton( PAUSAR );
        
        infoLBV = new JLabel( "Iniciado." );
        infoLBV.setForeground( Color.BLUE ); 
        
        JPanel inf_pnl = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        inf_pnl.setBorder( BorderFactory.createEtchedBorder() ); 
        inf_pnl.add( infoLBV );
        
        JPanel delayPNL = new JPanel( new FlowLayout());        
        delayPNL.add( delaySlider );
        
        JPanel lestePNL = new JPanel( new BorderLayout() );
        lestePNL.setBorder( BorderFactory.createEtchedBorder() ); 
        lestePNL.add( BorderLayout.NORTH, infoPNL );
        lestePNL.add( BorderLayout.CENTER, delayPNL );
        
        JPanel botoesPNL = new JPanel( new FlowLayout() );        
        botoesPNL.add( configurarBT );
        botoesPNL.add( carregarBT );        
        botoesPNL.add( randomDadosBT );
        botoesPNL.add( executarBT );
        botoesPNL.add( pausarContinuarBT );               
        
        JPanel centroPNL = new JPanel( new BorderLayout() );
        centroPNL.add( BorderLayout.NORTH, inf_pnl );
        centroPNL.add( BorderLayout.CENTER, desenhoCardPNL );
        centroPNL.add( BorderLayout.SOUTH, botoesPNL );
        
        Container c = super.getContentPane();
        c.setLayout( new BorderLayout() );
        c.add( BorderLayout.CENTER, centroPNL );
        c.add( BorderLayout.EAST, lestePNL );
        
        super.setTitle( titulo ); 
        super.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        super.setSize( w, h );
        super.setLocationRelativeTo( this ); 
        
        super.addWindowListener( this );
        configurarBT.addActionListener( this );
        carregarBT.addActionListener( this ); 
        randomDadosBT.addActionListener( this );
        executarBT.addActionListener( this );
        pausarContinuarBT.addActionListener( this ); 
        
        pausarContinuarBT.setEnabled( false ); 
        
        this.mostrarDesenhoPNL();
    }
     
    public void addDesenhoComponent( JComponent c, String cnome ) {
        desenhoComponent = c;
        desenhoCardPNL.add( c, cnome );
    }
    
    public JComponent getDesenhoPNLOuComponent() {
        return desenhoPNLOuComponent;
    }
    
    public final void mostrarDesenhoPNL() {
        desenhoPNLOuComponent = desenhoPNL;
        desenhoCardLayout.show( desenhoCardPNL, DESENHO_PNL );        
    }
    
    public void mostrarDesenhoComponent( String cnome ) {
        desenhoPNLOuComponent = desenhoComponent;
        desenhoCardLayout.show( desenhoCardPNL, cnome );        
    }
    
    public DesenhoPNL getDesenhoPNL() {
        return desenhoPNL;
    }

    public JPanel getDesenhoCardPNL() {
        return desenhoCardPNL;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if ( listener == null )
            return;
        if ( e.getSource() == executarBT ) {
            listener.executarBTAcionado( this ); 
        } else if ( e.getSource() == configurarBT ) {
            listener.configurarBTAcionado( this );
        } else if ( e.getSource() == randomDadosBT ) {
            listener.geraDadosBTAcionado( this ); 
        } else if ( e.getSource() == pausarContinuarBT ) {
            listener.pausarContinuarBTAcionado( this );
        } else if ( e.getSource() == carregarBT ) {
            listener.carregarBTAcionado( this ); 
        }
    }
    
    @Override
    public void windowClosing(WindowEvent e) {
        if ( listener != null )
            listener.fecharBTAcionado( this ); 
    }

    @Override
    public void windowOpened(WindowEvent e) {}
    
    @Override
    public void windowClosed(WindowEvent e) {}
    
    @Override
    public void windowIconified(WindowEvent e) {}
    
    @Override
    public void windowDeiconified(WindowEvent e) {}
    
    @Override
    public void windowActivated(WindowEvent e) {}
    
    @Override
    public void windowDeactivated(WindowEvent e) {}
    
    public void setSOMGUIListener( SOMGUIListener listener ) {
        this.listener = listener;
    }
    
    public void setToWaitCursor() {
        super.setCursor( new Cursor( Cursor.WAIT_CURSOR ) );        
    }
    
    public void setToDefaultCursor() {
        super.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );        
    }
    
    public void setInfoTexto( String texto ) {
        infoLBV.setForeground( Color.BLUE ); 
        infoLBV.setText( texto ); 
    }
    
    public void setProcessandoTexto( String texto ) {
        infoLBV.setForeground( Color.GREEN );
        infoLBV.setText( texto );
    }
    
    public void setErroTexto( String texto ) {
        infoLBV.setForeground( Color.RED );
        infoLBV.setText( texto ); 
    }
        
    public void carregaInfos( DecimalFormat df, SOMConfig cfg ) {
        infoPNL.getIteracaoLBV().setText( "0" );
        infoPNL.getTaCorrenteLBV().setText( "0" );
        infoPNL.getRvCorrenteLBV().setText( "0" ); 
        infoPNL.getQuantITsLBV().setText( String.valueOf( cfg.getQuantIteracoes() ) ); 
        infoPNL.getGradeDimLBV().setText( String.valueOf( cfg.getGradeQuantNeuroniosHorizontal() ) + "x" + String.valueOf( cfg.getGradeQuantNeuroniosVertical() ) );
        infoPNL.getGradeNDistLBV().setText( String.valueOf( cfg.getGradeNEsp() ) ); 
        infoPNL.getVizinhancaLBV().setText( cfg.isVizinhancaHexagonal() ? SOMConfigGUI.HEXAGONAL : SOMConfigGUI.RETANGULAR );
        infoPNL.getTaIniLBV().setText( df.format( cfg.getTaxaAprendizadoInicial() ) );
        infoPNL.getTaFinalLBV().setText( df.format( cfg.getTaxaAprendizadoFinal() ) );
        infoPNL.getRvIniLBV().setText( df.format( cfg.getRaioVizinhancaInicial() ) );
        infoPNL.getRvFinalLBV().setText( df.format( cfg.getRaioVizinhancaFinal() ) );
    }
    
    public void setIteracaoInfo( DecimalFormat df, int it, double ta, double rv ) {
        infoPNL.getIteracaoLBV().setText( String.valueOf( it ) );
        infoPNL.getTaCorrenteLBV().setText( df.format( ta ) ); 
        infoPNL.getRvCorrenteLBV().setText( df.format( rv ) ); 
    }    
        
    public int getDelay() {
        return delaySlider.getValue();
    }        
    
    public void setExecutarHabilitado( boolean b ) {
        executarBT.setEnabled( b ); 
    }
       
    public void setRandomDadosBTHabilitado( boolean b ) {
        randomDadosBT.setEnabled( b );
    }
    
    public void setConfigurarHabilitado( boolean b ) {
        configurarBT.setEnabled( b ); 
    }
    
    public void setPausarContinuarBTHabilitado( boolean b ) {
        pausarContinuarBT.setEnabled( b ); 
    }
    
    public void setCarregarBTVisivel( boolean b ) {
        carregarBT.setVisible( b ); 
    }
    
    public void setRandomDadosBTVisivel( boolean b ) {
        randomDadosBT.setVisible( b ); 
    }
    
    public void setPausarContinuarBTRotulo( String rotulo ) {
        pausarContinuarBT.setText( rotulo ); 
    }
    
}
