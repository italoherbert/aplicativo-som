package controller;

import aplic.SOMAplicContainer;
import som.SOMConfig;
import gui.config.SOMConfigGUI;
import java.text.DecimalFormat;
import java.text.ParseException;
import javax.swing.JOptionPane;
import gui.config.SOMConfigGUIListener;

public class SOMConfigController implements SOMConfigGUIListener {
  
    private final SOMAplicContainer aplic;
    private final DecimalFormat doubleDF = new DecimalFormat( "#.######" );
    
    public SOMConfigController(SOMAplicContainer aplic) {
        this.aplic = aplic;
    }
    
    @Override
    public void okBTAcionado(SOMConfigGUI gui) {
       this.guiParaSOMConfig( gui, aplic.getSOMCFG() );

       aplic.getSOMGUI().setExecutarHabilitado( false );
       aplic.carregaConfigInfos();
       aplic.resetaDadosENeuronios();
    }

    @Override
    public void configsPadraoBTAcionado(SOMConfigGUI gui) {
        this.somConfigParaGUI( aplic.getSOMPadraoConfig(), gui );         
        this.guiParaSOMConfig( gui, aplic.getSOMCFG() ); 

        aplic.getSOMGUI().setExecutarHabilitado( false ); 
        aplic.carregaConfigInfos();
        aplic.resetaDadosENeuronios();
    }
    
    public void guiParaSOMConfig( SOMConfigGUI gui, SOMConfig cfg ) {
         try {
            cfg.setQuantGrupoIteracoes( Integer.parseInt( gui.getQuantGruposIteracoes() ) );
            cfg.setVizinhancaHexagonal( gui.getTipoVizinhanca() == SOMConfigGUI.HEXAGONAL );
            cfg.setGradeNEsp( Integer.parseInt( gui.getNeuroniosDist() ) ); 
            cfg.setGradeQuantNeuroniosHorizontal( Integer.parseInt( gui.getGradeQNH() ) );
            cfg.setGradeQuantNeuroniosVertical( Integer.parseInt( gui.getGradeQNV() ) );
            cfg.setTaxaAprendizadoInicial( doubleDF.parse( gui.getTaxaAprendizadoInicial() ).doubleValue() );
            cfg.setTaxaAprendizadoFinal( doubleDF.parse( gui.getTaxaAprendizadoFinal() ).doubleValue() );
            cfg.setRaioVizinhancaInicial( doubleDF.parse( gui.getRaioVizinhancaInicial() ).doubleValue() );
            cfg.setRaioVizinhancaFinal( doubleDF.parse( gui.getRaioVizinhancaFinal() ).doubleValue() );
        } catch ( NumberFormatException | ParseException ex ) {
            JOptionPane.showMessageDialog( gui, "Erro - Verifique os números informados.", "Erro em configuraçao", JOptionPane.ERROR_MESSAGE );
        }
    }
    
    public void somConfigParaGUI( SOMConfig cfg, SOMConfigGUI gui ) {
        gui.setQuantGruposIteracoes( String.valueOf( cfg.getQuantGrupoIteracoes() ) ); 
        gui.setTipoVizinhanca( cfg.isVizinhancaHexagonal() ? SOMConfigGUI.HEXAGONAL : SOMConfigGUI.RETANGULAR );
        gui.setNeuroniosDist( String.valueOf( cfg.getGradeNEsp() ) );
        gui.setGradeQNH( String.valueOf( cfg.getGradeQuantNeuroniosHorizontal() ) );
        gui.setGradeQNV( String.valueOf( cfg.getGradeQuantNeuroniosVertical() ) );
        gui.setTaxaAprendizadoInicial( doubleDF.format( cfg.getTaxaAprendizadoInicial() ) );
        gui.setTaxaAprendizadoFinal( doubleDF.format( cfg.getTaxaAprendizadoFinal() ) );
        gui.setRaioVizinhancaInicial( doubleDF.format( cfg.getRaioVizinhancaInicial() ) );
        gui.setRaioVizinhancaFinal( doubleDF.format( cfg.getRaioVizinhancaFinal() ) );
    }
    
}
