package gui.config;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;

public class SOMConfigGUI extends JDialog implements ActionListener {
    
    public final static String RETANGULAR = "Retangular";
    public final static String HEXAGONAL = "Hexagonal";
    
    private SOMConfigPNL configPNL = new SOMConfigPNL();
    private SOMConfigGUIListener listener;
        
    public SOMConfigGUI() {
        super.setTitle( "Configurações" );
        super.setContentPane( configPNL ); 
        super.setDefaultCloseOperation( JDialog.HIDE_ON_CLOSE );
        super.pack();
        super.setLocationRelativeTo( this );
        
        configPNL.getVizinhancaCBB().setModel( new DefaultComboBoxModel<>( new String[]{ RETANGULAR, HEXAGONAL } ) );
        configPNL.getOkBT().addActionListener( this );
        configPNL.getConfsPadraoBT().addActionListener( this );
        configPNL.getFecharBT().addActionListener( this ); 
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ( listener == null )
            return;
        
        if ( e.getSource() == configPNL.getOkBT() ) {
            listener.okBTAcionado( this ); 
            super.setVisible( false ); 
        } else if ( e.getSource() == configPNL.getConfsPadraoBT() ) {
            listener.configsPadraoBTAcionado( this );
        } else if ( e.getSource() == configPNL.getFecharBT() ) {
            super.setVisible( false ); 
        }
    }
    
    public String getQuantGruposIteracoes() {
        return configPNL.getQuantGruposITsTF().getText(); 
    }
    
    public Object getTipoVizinhanca() {
        return configPNL.getVizinhancaCBB().getSelectedItem(); 
    }
    
    public String getGradeQNH() {
        return configPNL.getDimLargTF().getText(); 
    }
    
    public String getGradeQNV() {
        return configPNL.getDimAltTF().getText(); 
    }
    
    public String getNeuroniosDist() {
        return configPNL.getDistNeursTF().getText(); 
    }
    
    public String getRaioVizinhancaInicial() {
        return configPNL.getRaioVizinhancaInicialTF().getText(); 
    }
    
    public String getRaioVizinhancaFinal() {
        return configPNL.getRaioVizinhancaFinalTF().getText(); 
    }
    
    public String getTaxaAprendizadoInicial() {
        return configPNL.getTaxaAprendizadoInicialTF().getText(); 
    }
    
    public String getTaxaAprendizadoFinal() {
        return configPNL.getTaxaAprendizadoFinalTF().getText(); 
    }
    
    public void setQuantGruposIteracoes( String quant ) {
        configPNL.getQuantGruposITsTF().setText( quant ); 
    }
    
    public void setTipoVizinhanca( Object tipo ) {
        configPNL.getVizinhancaCBB().setSelectedItem( tipo );
    }
    
    public void setGradeQNH( String qnh ) {
        configPNL.getDimLargTF().setText( qnh );
    }
    
    public void setGradeQNV( String qnv ) {
        configPNL.getDimAltTF().setText( qnv ); 
    }
    
    public void setNeuroniosDist( String dist ) {
        configPNL.getDistNeursTF().setText( dist ); 
    }
    
    public void setRaioVizinhancaInicial( String raio ) {
        configPNL.getRaioVizinhancaInicialTF().setText( raio );
    }
    
    public void setRaioVizinhancaFinal( String raio ) {
        configPNL.getRaioVizinhancaFinalTF().setText( raio ); 
    }
    
    public void setTaxaAprendizadoInicial( String taxa ) {
        configPNL.getTaxaAprendizadoInicialTF().setText( taxa );
    }
    
    public void setTaxaAprendizadoFinal( String taxa ) {
        configPNL.getTaxaAprendizadoFinalTF().setText( taxa ); 
    }
    
    public void setConfigListener( SOMConfigGUIListener listener ) {
        this.listener = listener;
    }
    
}
