package aplic;

import java.text.DecimalFormat;
import som.Neuronio;
import som.SOM;
import som.SOMCFG;
import gui.SOMGUI;
import gui.config.SOMConfigGUI;
import som.SOMAlgoritmoDriver;
import som.SOMConfig;
import som.SOMEvent;
import controller.SOMConfigController;
import controller.SOMGUIController;
import controller.SOMRandomDadosController;
import gui.random.SOMRandomDadosGUI;

public abstract class SOMAplicContainer implements SOMAlgoritmoDriver {
                    
    protected SOMGUI gui;
    protected SOMConfigGUI configGUI = new SOMConfigGUI();
    protected SOMRandomDadosGUI randomDadosGUI = new SOMRandomDadosGUI();
        
    protected SOMConfigController cfgController = new SOMConfigController( this );
    protected SOMGUIController somGUIController = new SOMGUIController( this );
    protected SOMRandomDadosController randomDadosController = new SOMRandomDadosController( this );
    
    protected DecimalFormat decimalFormat = new DecimalFormat( "#.######" );
            
    protected SOM som = new SOM();
    protected SOMCFG cfg = new SOMCFG();
    protected SOMConfig padraoConfig = new SOMConfig();
        
    protected SOMAplicManager aplicManager = new SOMAplicManager( this );
    
    protected final int QUANT_GRUPOS_ITERACOES = 15;
    protected final int GRADE_QNH = 20;
    protected final int GRADE_QNV = 20;    
    protected final int GRADE_NEURONIOS_ESP = 5;
    protected final boolean VIZINHANCA_HEXAGONAL = false;
    
    protected final double TAXA_APRENDIZADO_INICIAL = 0.5d;
    protected final double TAXA_APRENDIZADO_FINAL = 0.05d;
        
    protected final double RAIO_VIZINHANCA_INICIAL = GRADE_NEURONIOS_ESP * Math.max( GRADE_QNH, GRADE_QNV )*.5d;
    protected final double RAIO_VIZINHANCA_FINAL = 1; 
        
    protected final int QUANT_DADOS_MULT = 2000;
    protected final double[][] CENTROIDES_2D = { {0,0}, {0,1}, {1,0}, {1,1} };
    protected final double RAIO_DISPERSAO_2D = 0.35;
    
    protected final double[][] CENTROIDES_3D = { {0,0,-1}, {0,0,1}, {1,0,0}, {1,1,1} };        
    protected final double RAIO_DISPERSAO_3D = 0.35;
        
    protected boolean finalizar = false;
                    
    public SOMAplicContainer( String titulo ) {
        gui = new SOMGUI( titulo );        
        gui.setExecutarHabilitado( false );         
        gui.setSOMGUIListener( somGUIController ); 
        
        configGUI.setConfigListener( cfgController ); 
        randomDadosGUI.setRandomDadosGUIListener( randomDadosController );                 
                        
        cfg.setSOMAlgoritmoDriver( this );                
        
        cfg.setSOMListener( aplicManager );
        gui.getDesenhoPNL().setGraficoSource( aplicManager );
    }    
    
    public abstract void inicializaSOM();
    
    public abstract void primeiraVezGrafoGerado();
    
    public abstract void configuraAplicCorrente( double[][] dados );
    
    public abstract SOMAplic getAplicCorrente();
    
    public void geraDadosBTAcionado( SOMGUI gui ) {
        randomDadosGUI.setVisible( true );
    }
          
    public void inicializa() {
        cfg.setGradeQuantNeuroniosHorizontal( GRADE_QNH );
        cfg.setGradeQuantNeuroniosVertical( GRADE_QNV ); 
        cfg.setGradeNEsp( GRADE_NEURONIOS_ESP );
        cfg.setVizinhancaHexagonal( VIZINHANCA_HEXAGONAL );
        
        cfg.setTaxaAprendizadoInicial( TAXA_APRENDIZADO_INICIAL );
        cfg.setTaxaAprendizadoFinal( TAXA_APRENDIZADO_FINAL );
        
        cfg.setRaioVizinhancaInicial( RAIO_VIZINHANCA_INICIAL );
        cfg.setRaioVizinhancaFinal( RAIO_VIZINHANCA_FINAL ); 
        
        cfg.setQuantGrupoIteracoes(QUANT_GRUPOS_ITERACOES );
        
        padraoConfig.setGradeQuantNeuroniosHorizontal( cfg.getGradeQuantNeuroniosHorizontal() );
        padraoConfig.setGradeQuantNeuroniosVertical( cfg.getGradeQuantNeuroniosVertical() );
       
        padraoConfig.setVizinhancaHexagonal( cfg.isVizinhancaHexagonal() );
        
        padraoConfig.setTaxaAprendizadoInicial( cfg.getTaxaAprendizadoInicial() );
        padraoConfig.setTaxaAprendizadoFinal( cfg.getTaxaAprendizadoFinal() );
        padraoConfig.setRaioVizinhancaInicial( cfg.getRaioVizinhancaInicial() );
        padraoConfig.setRaioVizinhancaFinal( cfg.getRaioVizinhancaFinal() ); 
        
        padraoConfig.setQuantGrupoIteracoes( cfg.getQuantGrupoIteracoes() );
                                                
        cfg.setSOMAlgoritmoDriver( this );
        
        gui.carregaInfos( decimalFormat, cfg ); 
        
        randomDadosGUI.carrega2DForms( decimalFormat, QUANT_DADOS_MULT, RAIO_DISPERSAO_2D, CENTROIDES_2D );
        randomDadosGUI.carrega3DForms( decimalFormat, QUANT_DADOS_MULT, RAIO_DISPERSAO_3D, CENTROIDES_3D );
        
        this.inicializaSOM();
    }
    
    public void carregaDados( double[][] amostras ) {
        int gradeQNH = cfg.getGradeQuantNeuroniosHorizontal();
        int gradeQNV = cfg.getGradeQuantNeuroniosVertical();
        int gradeNEsp = cfg.getGradeNEsp();
        boolean vizinhancaHexagonal = cfg.isVizinhancaHexagonal();
        int quantGruposITs = cfg.getQuantGrupoIteracoes();

        double[][][] grade = som.geraGrade( gradeQNH, gradeQNV, gradeNEsp, vizinhancaHexagonal );                
        Neuronio[][] neuronios = som.geraNeuronios( grade, gradeQNH, gradeQNV, amostras );

        cfg.setGrade( grade );
        cfg.setAmostras( amostras );
        cfg.setNeuronios( neuronios );     
        cfg.setQuantIteracoes( quantGruposITs * amostras.length ); 
        
        this.configuraAplicCorrente( amostras );
        
        aplicManager.geraNosEGrafoRede();
        
        this.primeiraVezGrafoGerado();
        this.repintaGrafico();
        
        gui.setExecutarHabilitado( true );
        gui.setConfigurarHabilitado( true ); 
                
        gui.carregaInfos( decimalFormat, cfg ); 
        finalizar = false;          
    }
                
    public void resetaDadosENeuronios() {
        cfg.setAmostras( new double[0][0] );
        cfg.setNeuronios( new Neuronio[0][0] ); 
        
        aplicManager.geraNosEGrafoRede();
        this.repintaGrafico();
    }
    
    
    public void repintaGrafico() {      
        aplicManager.repintaGraficoSOM();
    }
    
    public void setIteracaoInfo( SOMEvent e ) {
        gui.setIteracaoInfo( decimalFormat, e.getIteracao(), e.getTaxaAprendizado(), e.getRaioVizinhanca() ); 
    }
    
    public void carregaConfigInfos() {
        gui.carregaInfos( decimalFormat, cfg ); 
    }
        
    public SOMAplicManager getSOMAplicManager() {
        return aplicManager;
    }
                                
    public SOM getSOM() {
        return som;
    }
    
    public SOMCFG getSOMCFG() {
        return cfg;
    }
    
    public SOMConfig getSOMPadraoConfig() {
        return padraoConfig;
    }
    
    public SOMGUI getSOMGUI() {
        return gui;
    }
    
    public SOMRandomDadosGUI getSOMRandomDadosGUI() {
        return randomDadosGUI;
    }

    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }
    
    public SOMConfigGUI getSOMConfigGUI() {
        return configGUI;
    }
    
    public SOMConfigController getSOMConfigController() {
        return cfgController;
    }
    
    public SOMGUIController getSOMGUIController() {
        return somGUIController;
    }
    
    public boolean isPararSOMExecucao() {
        return finalizar;
    }
    
    public void setPararSOMExecucao(boolean parar) {
        this.finalizar = parar;
    }
    
    @Override
    public boolean isFinalizar() {
        return finalizar;
    }
    
}
