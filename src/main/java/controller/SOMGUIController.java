package controller;

import aplic.SOMAplicContainer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import som.SOM;
import gui.SOMGUI;
import gui.SOMGUIListener;
import java.util.StringTokenizer;

public class SOMGUIController implements SOMGUIListener {

    private final SOMAplicContainer aplic;

    public SOMGUIController(SOMAplicContainer aplic) {
        this.aplic = aplic;
    }
        
    @Override
    public void executarBTAcionado(SOMGUI gui) {                        
        aplic.getSOMGUI().setExecutarHabilitado( false ); 
        aplic.getSOMGUI().setConfigurarHabilitado( false ); 
        
        new Thread() {
            @Override
            public void run() {           
                aplic.getSOMGUI().setInfoTexto( "Executando..." ); 
                
                aplic.getSOMGUI().setPausarContinuarBTHabilitado( true );
                aplic.getSOM().treina( aplic.getSOMCFG() );                 
                aplic.getSOMGUI().setPausarContinuarBTHabilitado( false ); 
                aplic.getSOMGUI().setExecutarHabilitado( true ); 
                aplic.getSOMGUI().setConfigurarHabilitado( true );                
                aplic.getSOMGUI().setExecutarHabilitado( false ); 

                aplic.getSOMGUI().setInfoTexto( "Concluído!" ); 
                
                aplic.getSOMAplicManager().aposTreinoConcluido();                
            }
        }.start();
    }

    @Override
    public void configurarBTAcionado(SOMGUI gui) {
        aplic.getSOMConfigController().somConfigParaGUI( aplic.getSOMCFG(), aplic.getSOMConfigGUI() );
        aplic.getSOMConfigGUI().setVisible( true ); 
    }

    @Override
    public void carregarBTAcionado(SOMGUI gui) {
        File dadosDir = new File( "dados/" );
        
        JFileChooser fc = new JFileChooser();
        if ( dadosDir.exists() )
            fc.setCurrentDirectory( dadosDir ); 
        
        fc.setFileFilter( new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith( ".txt" );
            }
            @Override
            public String getDescription() {
                return ".txt";
            }
        });
        int result = fc.showOpenDialog( fc );        
        if ( result == JFileChooser.APPROVE_OPTION ) {
            gui.setProcessandoTexto( "Carregando arquivo..." ); 
            gui.setToWaitCursor();
            
            File file = fc.getSelectedFile();
            List<double[]> dados = new ArrayList();
            try {
                BufferedReader in = new BufferedReader( new InputStreamReader( new FileInputStream( file ) ) );
                String linha = in.readLine();
                int dim = 0;
                for( int i = 0; linha != null; i++ ) {
                    StringTokenizer st = new StringTokenizer( linha );
                    dim = st.countTokens();                  
                    
                    double[] ds = new double[ dim ];
                    
                    int cdim;
                    for( cdim = 0; st.hasMoreTokens(); cdim++ )
                        ds[ cdim ] = Double.parseDouble( st.nextToken() );                                                                                                        
                    
                    for( ; cdim < dim; cdim++ )
                        ds[ cdim ] = 0;                   
                    
                    dados.add( ds );
                    
                    linha = in.readLine();
                }
                
                int size = dados.size();
                int maior = 0;
                for( int i = 0; i < size; i++ ) {
                    double[] vet = dados.get( i );
                    if ( vet.length > maior )
                        maior = vet.length;
                }
                
                
                double[][] vetorDados = new double[ dados.size() ][ dim ];
                vetorDados = dados.toArray( vetorDados );                
                aplic.carregaDados( vetorDados );
                                         
                gui.setInfoTexto( "Arquivo carregado com sucesso." );
                gui.setToDefaultCursor();
                
                in.close();
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog( null, "Arquivo não encontrado. \nArquivo="+file.getPath(), "Arquivo de dados!", JOptionPane.ERROR_MESSAGE ); 
            } catch (IOException ex) {
                JOptionPane.showMessageDialog( null, "Erro de leitura.", "Arquivo de dados!", JOptionPane.ERROR_MESSAGE ); 
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog( null, "A base de dados possue dados não numéricos.", "Arquivo de dados!", JOptionPane.ERROR_MESSAGE ); 
            }
        }
    }

    @Override
    public void geraDadosBTAcionado(SOMGUI gui) {        
        int status = aplic.getSOM().getStatus();
        if ( status == SOM.EXECUTANDO || status == SOM.PAUSADO ) {
            if ( status == SOM.PAUSADO )
                aplic.getSOM().continuarSePausado();
            
            aplic.getSOM().addSOMFim( (som,config) -> {
                aplic.geraDadosBTAcionado( gui );
            } );
            aplic.setPararSOMExecucao( true );
        } else {
            aplic.geraDadosBTAcionado( gui );
        }
    }
    
    @Override
    public void pausarContinuarBTAcionado(SOMGUI gui ) {
        if ( aplic.getSOM().getStatus() == SOM.PAUSADO ) {
            aplic.getSOM().continuarSePausado();
            gui.setPausarContinuarBTRotulo( SOMGUI.PAUSAR );
            gui.setInfoTexto( "Executando..." );
        } else {
            aplic.getSOM().pausar();
            gui.setPausarContinuarBTRotulo( SOMGUI.CONTINUAR ); 
            gui.setInfoTexto( "Pausado!" ); 
        }
    }

    @Override
    public void fecharBTAcionado(SOMGUI gui) {
        aplic.setPararSOMExecucao( true ); 
        gui.setInfoTexto( "Saindo..." ); 
    }
    
}
