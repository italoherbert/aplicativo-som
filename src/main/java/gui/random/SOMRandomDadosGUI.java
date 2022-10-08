package gui.random;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class SOMRandomDadosGUI extends JFrame implements ActionListener, ItemListener {        
    
    public final static String PNL_CENTROIDES2D = "centroides2d";
    public final static String PNL_CENTROIDES3D = "centroides3d";
    
    public final static Object CBB_CENTROIDES2D = "Centroides 2D";
    public final static Object CBB_CENTROIDES3D = "Centroides 3D";
    
    private JComboBox randomTipoCBB;
    private JButton geraRandomDadosBT;
    private JButton fecharBT;
    
    private JPanel randomConfigPNL;
    private CardLayout randomCardLayout;
    
    private JTable centroides2DTBL;
    private JTable centroides3DTBL;
    
    private JButton addCentroide2DBT;
    private JButton removeCentroides2DBT;
    
    private JButton addCentroide3DBT;
    private JButton removeCentroides3DBT;
    
    private JLabel raioDispersao2DLB;
    private JTextField raioDispersao2DTF;
    
    private JLabel raioDispersao3DLB;
    private JTextField raioDispersao3DTF;
    
    private JLabel quantDadosMult2DLB;
    private JTextField quantDadosMult2DTF;
    
    private JLabel quantDadosMult3DLB;
    private JTextField quantDadosMult3DTF;
    
    private SOMRandomDadosGUIListener listener;
        
    public SOMRandomDadosGUI() {        
        String[] centroides2DTBLColunas = { "X", "Y" };
        int[] centroides2DTBLColunasLarguras = { 50, 50 };
        
        String[] centroides3DTBLColunas = { "X", "Y", "Z" };
        int[] centroides3DTBLColunasLarguras = { 50, 50, 50 };
        
        addCentroide2DBT = new JButton( "+" );
        addCentroide3DBT = new JButton( "+" );
        removeCentroides2DBT = new JButton( "-" );
        removeCentroides3DBT = new JButton( "-" );
                
        centroides2DTBL = new JTable();          
        centroides2DTBL.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
        this.addColunas( centroides2DTBL, centroides2DTBLColunas, centroides2DTBLColunasLarguras );
        
        centroides3DTBL = new JTable();          
        centroides3DTBL.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
        this.addColunas( centroides3DTBL, centroides3DTBLColunas, centroides3DTBLColunasLarguras );        
        
        JPanel btsTBL2D_PNL = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
        btsTBL2D_PNL.add( addCentroide2DBT );
        btsTBL2D_PNL.add( removeCentroides2DBT );
        
        JPanel btsTBL3D_PNL = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
        btsTBL3D_PNL.add( addCentroide3DBT );
        btsTBL3D_PNL.add( removeCentroides3DBT );
        
        JPanel centroides2DTBL_PNL = new JPanel( new BorderLayout() );
        centroides2DTBL_PNL.setBorder( new TitledBorder( "Tabela de centroides 2D" ) );
        centroides2DTBL_PNL.add( BorderLayout.CENTER, new JScrollPane( centroides2DTBL ) );
        centroides2DTBL_PNL.add( BorderLayout.SOUTH, btsTBL2D_PNL );
        
        JPanel centroides3DTBL_PNL = new JPanel( new BorderLayout() );
        centroides3DTBL_PNL.setBorder( new TitledBorder( "Tabela de centroides 3D" ) );
        centroides3DTBL_PNL.add( BorderLayout.CENTER, new JScrollPane( centroides3DTBL ) );
        centroides3DTBL_PNL.add( BorderLayout.SOUTH, btsTBL3D_PNL );
                
        raioDispersao2DLB = new JLabel( "Raio dispersão: " );
        raioDispersao2DTF = new JTextField( 10 );        
        
        raioDispersao3DLB = new JLabel( "Raio dispersão: " );
        raioDispersao3DTF = new JTextField( 10 );
        
        quantDadosMult2DLB = new JLabel( "Quant. Dados (mult): " );
        quantDadosMult2DTF = new JTextField( 10 );

        quantDadosMult3DLB = new JLabel( "Quant. Dados (mult): " );
        quantDadosMult3DTF = new JTextField( 10 );
        
        JPanel raioDispersao2D_PNL = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        raioDispersao2D_PNL.add( raioDispersao2DLB );
        raioDispersao2D_PNL.add( raioDispersao2DTF );
        
        JPanel raioDispersao3D_PNL = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        raioDispersao3D_PNL.add( raioDispersao3DLB );
        raioDispersao3D_PNL.add( raioDispersao3DTF );
        
        JPanel quantDadosMult2D_PNL = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        quantDadosMult2D_PNL.add( quantDadosMult2DLB );
        quantDadosMult2D_PNL.add( quantDadosMult2DTF );
        
        JPanel quantDadosMult3D_PNL = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        quantDadosMult3D_PNL.add( quantDadosMult3DLB );
        quantDadosMult3D_PNL.add( quantDadosMult3DTF );
        
        JPanel quantDadosERaio2D_PNL = new JPanel( new BorderLayout() );
        quantDadosERaio2D_PNL.add( BorderLayout.NORTH, raioDispersao2D_PNL );
        quantDadosERaio2D_PNL.add( BorderLayout.SOUTH, quantDadosMult2D_PNL );
        
        JPanel quantDadosERaio3D_PNL = new JPanel( new BorderLayout() );
        quantDadosERaio3D_PNL.add( BorderLayout.NORTH, raioDispersao3D_PNL );
        quantDadosERaio3D_PNL.add( BorderLayout.SOUTH, quantDadosMult3D_PNL );
        
        JPanel centroides2DPNL = new JPanel( new BorderLayout() );
        centroides2DPNL.add( BorderLayout.CENTER, centroides2DTBL_PNL );
        centroides2DPNL.add( BorderLayout.SOUTH, quantDadosERaio2D_PNL );
               
        JPanel centroides3DPNL = new JPanel( new BorderLayout() );
        centroides3DPNL.add( BorderLayout.CENTER, centroides3DTBL_PNL );
        centroides3DPNL.add( BorderLayout.SOUTH, quantDadosERaio3D_PNL );
        
        randomConfigPNL = new JPanel( randomCardLayout = new CardLayout() );
        randomConfigPNL.add( centroides2DPNL, PNL_CENTROIDES2D );
        randomConfigPNL.add( centroides3DPNL, PNL_CENTROIDES3D );
        
        randomTipoCBB = new JComboBox();
        randomTipoCBB.setModel(new DefaultComboBoxModel( new Object[]{ CBB_CENTROIDES2D, CBB_CENTROIDES3D } ) );
        
        geraRandomDadosBT = new JButton( "Gera random dados" );
        fecharBT = new JButton( "Fechar" );
        
        JPanel btnPNL = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
        btnPNL.setBorder( BorderFactory.createEtchedBorder() ); 
        btnPNL.add( geraRandomDadosBT );
        btnPNL.add( fecharBT );
        
        Container c = super.getContentPane();
        c.setLayout( new BorderLayout() );
        c.add( BorderLayout.NORTH, randomTipoCBB );
        c.add( BorderLayout.CENTER, randomConfigPNL );
        c.add( BorderLayout.SOUTH, btnPNL );
        
        super.setTitle( "Geração de dados aleatórios" );
        super.setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );
        super.setSize( 500, 400 ); 
        super.setLocationRelativeTo( this );        
        
        geraRandomDadosBT.addActionListener( this ); 
        fecharBT.addActionListener( this ); 
        addCentroide2DBT.addActionListener( this );
        addCentroide3DBT.addActionListener( this );
        removeCentroides2DBT.addActionListener( this );
        removeCentroides3DBT.addActionListener( this ); 
        
        randomTipoCBB.addItemListener( this );
        
        randomTipoCBB.setSelectedItem(CBB_CENTROIDES3D ); 
        randomCardLayout.show(randomConfigPNL, PNL_CENTROIDES3D ); 
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if ( e.getSource() == randomTipoCBB ) {            
            if ( randomTipoCBB.getSelectedItem() == CBB_CENTROIDES2D ) {
                randomCardLayout.show( randomConfigPNL, PNL_CENTROIDES2D ); 
            } else if ( randomTipoCBB.getSelectedItem() == CBB_CENTROIDES3D ) {
                randomCardLayout.show( randomConfigPNL, PNL_CENTROIDES3D ); 
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ( e.getSource() == addCentroide2DBT ) {
            this.addCentroide( centroides2DTBL, new String[] { "0", "0" } );                                
        } else if ( e.getSource() == addCentroide3DBT ) {
            this.addCentroide( centroides3DTBL, new String[] { "0", "0", "0" } );                                
        } else if ( e.getSource() == removeCentroides2DBT ) {
            this.removeCentroidesSelecionados( centroides2DTBL ); 
        } else if ( e.getSource() == removeCentroides3DBT ) {
            this.removeCentroidesSelecionados( centroides3DTBL ); 
        } else if ( e.getSource() == fecharBT ) {
            super.setVisible( false ); 
        }
        
        if ( listener == null )
            return;
        
        if ( e.getSource() == geraRandomDadosBT ) {
            listener.geraRandomDadosBTAcionado( this, randomTipoCBB.getSelectedItem() ); 
        }
    }
    
    public void carrega2DForms( DecimalFormat df, int quantDM, double raioDisp, double[][] centroides ) {
        this.removeTodosOsCentroides( centroides2DTBL ); 
        for( double[] c : centroides ) {
            String sx = df.format( c[0] );
            String sy = df.format( c[1] );
            this.addCentroide( centroides2DTBL, new String[] { sx, sy }  );
        }
        raioDispersao2DTF.setText( df.format( raioDisp ) );
        quantDadosMult2DTF.setText( df.format( quantDM ) );
    }
        
    public void carrega3DForms( DecimalFormat df, int quantDM, double raioDisp, double[][] centroides ) {        
        this.removeTodosOsCentroides( centroides3DTBL );
        for( double[] c : centroides ) {
            String sx = df.format( c[0] );
            String sy = df.format( c[1] );
            String sz = df.format( c[2] );
            this.addCentroide( centroides3DTBL, new String[] { sx, sy, sz }  );
        }
        raioDispersao3DTF.setText( df.format( raioDisp ) );
        quantDadosMult3DTF.setText( df.format( quantDM ) );
    }      
    
    public String[][] getCentroides2D() {
        return this.getCentroides( centroides2DTBL );
    }
    
    public String[][] getCentroides3D() {
        return this.getCentroides( centroides3DTBL );
    }
            
    public Object getRandomTipo() {
        return randomTipoCBB.getSelectedItem();
    }
    
    public String getRaioDispersao2D() {
        return raioDispersao2DTF.getText();
    }
    
    public String getRaioDispersao3D() {
        return raioDispersao3DTF.getText();
    }
    
    public String getQuantDadosMult2D() {
        return quantDadosMult2DTF.getText();
    }
    
    public String getQuantDadosMult3D() {
        return quantDadosMult3DTF.getText();
    }
    
    public void addCentroide2D( String[] centroide ) {
        this.addCentroide( centroides2DTBL, centroide );
    }
    
    public void addCentroide3D( String[] centroide ) {
        this.addCentroide( centroides3DTBL, centroide );
    }
    
    private String[][] getCentroides( JTable table ) {
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        int qrows = model.getRowCount();
        int qcols = model.getColumnCount();
        
        String[][] centroides = new String[ qrows ][ qcols ];
        for( int i = 0; i < qrows; i++ ) {
            for( int j = 0; j < qcols; j++ ) {
                centroides[i][j] = String.valueOf( model.getValueAt( i, j ) );  
            }
        }
        
        return centroides;
    }
    
    private void removeCentroidesSelecionados( JTable table ) {
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        int[] selectsIDs = table.getSelectedRows();
        int quantSelects = selectsIDs.length;
        while( quantSelects > 0 ) {
            model.removeRow( selectsIDs[ quantSelects-1 ] );         
            quantSelects--;
        }
    }
    
    public void removeTodosOsCentroides( JTable table ) {
        DefaultTableModel model = (DefaultTableModel)centroides3DTBL.getModel();
        while( model.getRowCount() > 0 )
            model.removeRow( 0 );
    }
        
    private void addCentroide( JTable table, String[] centroide ) {
        ((DefaultTableModel)table.getModel()).addRow( centroide );                                            
    }
    
    private void addColunas( JTable table, String[] colunas, int[] larguras ) {
        for( int i = 0; i < colunas.length; i++ ) {
            ((DefaultTableModel)table.getModel()).addColumn( colunas[i] ); 
            table.getColumnModel().getColumn( i ).setPreferredWidth( larguras[i] );
        }    
    }                
    
    public void setRandomDadosGUIListener( SOMRandomDadosGUIListener listener ) {
        this.listener = listener;
    }
    
}
