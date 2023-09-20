package som;

import java.util.ArrayList;
import java.util.List;

public class SOM {
    
    public final static int NAO_INICIADO = 0;
    public final static int EXECUTANDO = 1;
    public final static int CONCLUIDO = 2;
    public final static int PAUSADO = 3;
    
    private final List<SOMFim> fins = new ArrayList();
    private int status = NAO_INICIADO;
    private boolean pausar = false;
    
    private int it = 0;
       
    /**
     * Esse método é o responsável pelo treinamento da rede de acordo com a configuração 
     * passada para ele como parâmetro. O objeto de configuração passado como parâmetro tem alguns de 
     * seus atributos alterados durante o treinamento. Assim, o resultado do treinamento corresponde a 
     * alteração dos pesos durante a submissão dos sinais de entrada (amostras) a cada iteração (ou época) do 
     * altoritmo.
     * 
     * @param config - Objeto utilizado para configuração do treinamento da rede. 
     * 
     */
    public void treina( SOMCFG config ) {
        status = EXECUTANDO;
        
        int gradeQNH = config.getGradeQuantNeuroniosHorizontal();
        int gradeQNV = config.getGradeQuantNeuroniosVertical();
        
        Neuronio[][] neuronios = config.getNeuronios();
        double[][] amostras = config.getAmostras();
        
        double taInicial = config.getTaxaAprendizadoInicial();
        double taFinal = config.getTaxaAprendizadoFinal();
        double rvInicial = config.getRaioVizinhancaInicial();
        double rvFinal = config.getRaioVizinhancaFinal();
                
        double taxaAprendizado = taInicial;
        double raioVizinhanca = rvInicial;
                
        if ( config.getSOMListener() != null )
            config.getSOMListener().execIT( new SOMEvent( config, 0, taxaAprendizado, raioVizinhanca ) );         
        
        int[] indices = new int[ amostras.length ];        
        
        int qITs = config.getQuantGrupoIteracoes() * amostras.length;
        
        config.setQuantIteracoes( qITs ); 

        for( it = 1; !config.isFinalizar() && it <= qITs; it++ ) {
            while ( pausar ) {
                synchronized( this ) {
                    try {
                        status = PAUSADO;
                        wait();
                    } catch ( InterruptedException e ) {
                        
                    }
                }
            }
            status = EXECUTANDO;                                    
            
            /*
            int amostraI = (int)Math.floor( Math.random() * amostras.length );
            if ( amostraI == amostras.length )
                amostraI = amostras.length-1;            
            */
            //int amostraI = (it-1) % amostras.length;
                        
            int aI = (it-1) % amostras.length;
            if ( aI == 0 )
                this.calculaIndicesRandomicos( indices );             
            int amostraI = indices[ aI ];            
            
            double[] amostra = amostras[ amostraI ];
            
            int bmuXI = 0;
            int bmuYI = 0;
            double bmuD = Double.MAX_VALUE;
            for( int i = 0; i < gradeQNV; i++ ) {
                for( int j = 0; j < gradeQNH; j++ ) {
                    double[] peso = neuronios[i][j].getPeso();
                    
                    double d = 0;
                    for( int k = 0; k < amostra.length; k++ )
                        d += Math.pow( amostra[k] - peso[k], 2 );        
                    d = Math.sqrt( d );
                    
                    if ( d < bmuD ) {
                        bmuXI = j;
                        bmuYI = i;
                        bmuD = d;
                    }
                }
            }  
            
            for( int i = 0; i < gradeQNV; i++ ) {
                for( int j = 0; j < gradeQNH; j++ ) {
                    double[] peso = neuronios[i][j].getPeso();
                    double x1 = neuronios[i][j].getX();
                    double y1 = neuronios[i][j].getY();
                    double x2 = neuronios[bmuYI][bmuXI].getX();
                    double y2 = neuronios[bmuYI][bmuXI].getY();
                    double d = Math.pow( x2-x1, 2 ) + Math.pow( y2-y1, 2 );
                    double vizinhanca = Math.exp( - d / ( 2*Math.pow( raioVizinhanca, 2 ) ) );
                                        
                    for( int k = 0; k < amostra.length; k++ )
                        peso[k] += taxaAprendizado * vizinhanca * ( amostra[k] - peso[k] );                    
                }
            }
            
            if ( config.getSOMListener() != null )
                config.getSOMListener().execIT( new SOMEvent( config, it, taxaAprendizado, raioVizinhanca ) ); 
            
            taxaAprendizado = taInicial * Math.pow( taFinal/taInicial, (double)it / (double)qITs );
            raioVizinhanca = rvInicial * Math.pow( rvFinal/rvInicial, (double)it / (double)qITs );            
            //taxaAprendizado = taFinal + taInicial * (double)(qITs-it-1)/qITs;
            //raioVizinhanca = rvFinal + rvInicial * (double)(qITs-it-1)/qITs;            
        }
        status = CONCLUIDO;
        
        if ( config.getSOMListener() != null )
            config.getSOMListener().execIT( new SOMEvent( config, qITs, taxaAprendizado, raioVizinhanca ) );
                
        fins.forEach( f -> f.fim( this, config ) );
        fins.clear();
    }    
        
    /** Gera aleatoriamente os pesos de cada neurônio.
     * 
     * @param grade - Matriz que representa a grade de neurônio. Perceba que cada neurônio da grade tem uma posição 
     * bidimensional (Isto é, valores de x e y). A grade é bidimensional, mas os pesos dos neurônios são N dimensionais! 
     * É necessário compreender a diferença entre os valores dos pesos dos neurônios e os valores das posições dos neurônios 
     * na grade de neurônios.
     * 
     * @param gradeQNH - Quantidade de neurônios na horizontal. Corresponde ao número de colunas da grade
     * @param gradeQNV - Quantidade de neurônios na vertical. Corresponde ao número de linhas da grade
     * 
     * @param amostras - Vetor com as amostras. Perceba que cada amostra deve ter número de atributos 
     * (elementos) compatível com o dos pesos dos neurônios
     * 
     * @return Neuronio[][] - Retorna uma matriz de neurônios ca
     */
    public Neuronio[][] geraNeuronios( double[][][] grade, int gradeQNH, int gradeQNV, double[][] amostras ) {        
        if ( amostras.length < 1 )
            return new Neuronio[0][0];
                        
        return this.geraNeuroniosNaoAleatorios( grade, gradeQNH, gradeQNV, amostras );
        
        //return this.geraNeuroniosAleatorios( grade, gradeQNH, gradeQNV, amostras );        
    }
    
    public Neuronio[][] geraNeuroniosNaoAleatorios( double[][][] grade, int gradeQNH, int gradeQNV, double[][] amostras ) {
        int amostrasDIM = amostras[ 0 ].length;
        Neuronio[][] neuronios = new Neuronio[gradeQNV][gradeQNH];
        
        double[] medias = new double[ amostrasDIM ];
        double[] minDs = new double[ amostrasDIM ];
        double[] maxDs = new double[ amostrasDIM ];   
        
        for( int j = 0; j < amostrasDIM; j++ ) {                        
            medias[ j ] = 0;
            for( int i = 0; i < amostras.length; i++ )
                medias[ j ] += amostras[ i ][ j ];                                            
            medias[ j ] /= amostras.length;                                                
        }         
        
        for( int j = 0; j < amostrasDIM; j++ ) {
            minDs[ j ] = Double.POSITIVE_INFINITY;
            maxDs[ j ] = Double.NEGATIVE_INFINITY;
        }
        
        for( int j = 0; j < amostrasDIM; j++ ) {
            for( int i = 0; i < amostras.length; i++ ) {
                double d = Math.abs( amostras[ i ][ j ] - medias[ j ] );
                if ( d < minDs[ j ] )
                    minDs[ j ] = d;
                if ( d > maxDs[ j ] )
                    maxDs[ j ] = d;     
            }
        }     
                
        for( int i = 0; i < gradeQNV; i++ ) {
            for( int j = 0; j < gradeQNH; j++ ) {
                double[] peso = new double[ amostrasDIM ];
                
                for( int k = 0; k < amostrasDIM; k++ ) {
                    int l = ( ( i * gradeQNH ) + j ) % amostras.length;                         
                    double d = Math.abs( amostras[ l ][ k ] - medias[ k ] );
                    
                    double p;
                    if ( maxDs[ k ] == minDs[ k ] )
                        p = Math.random();
                    else p = 1.0d - ( ( d - minDs[ k ] ) / ( maxDs[ k ] - minDs[ k ] ) );
                    
                    peso[ k ] = -1.0d + ( p * 2.0d );  
                }
                
                double x = grade[i][j][0];
                double y = grade[i][j][1];                 

                neuronios[ i ][ j ] = new Neuronio( x, y, peso );
            }
        } 
        
        return neuronios;
    }
    
    public Neuronio[][] geraNeuroniosAleatorios( double[][][] grade, int gradeQNH, int gradeQNV, double[][] amostras ) {
        int amostrasDIM = amostras[ 0 ].length;
        
        Neuronio[][] neuronios = new Neuronio[gradeQNV][gradeQNH];
        
        double[] mins = new double[amostrasDIM];
        double[] maxs = new double[amostrasDIM];
        
        for( int i = 0; i < amostrasDIM; i++ ) {
            mins[i] = Double.MAX_VALUE;
            maxs[i] = Double.MIN_VALUE;
        } 

        for( int i = 0; i < amostras.length; i++ ) {            
            for( int k = 0; k < amostrasDIM; k++ ) {
                if ( amostras[i][k] < mins[k] )
                    mins[k] = amostras[i][k];
                if ( amostras[i][k] > maxs[k] )
                    maxs[k] = amostras[i][k];
            }
        } 

        for( int i = 0; i < gradeQNV; i++ ) {
            for( int j = 0; j < gradeQNH; j++ ) {
                double[] peso = new double[ amostrasDIM ];            
                for ( int k = 0; k < amostrasDIM; k++ )
                   peso[k] += mins[k] + ( Math.random() * Math.abs( maxs[k] - mins[k] ) );                                  

                double x = grade[i][j][0];
                double y = grade[i][j][1];                 

                neuronios[i][j] = new Neuronio( x, y, peso );
            }
        }

        return neuronios;
    }
        
    /** Gera a grade que será processada para a geração de neurônio. A matriz retornada por esse método é 
     * bidimensional e cada elemento seu é um vetor de dois elementos que representam as posições (x,y) de cada 
     * neurônio (Independente da dimensão dos neurônios que podem ser N dimensionais). Obs: a função de vizinhança 
     * não é aplicada aos valores dos neurônios, mas, sim, a sua posição bidimensional que, inicialmente, antes de 
     * quaisquer alterações, tem formato de grade com vizinhança entre os elementos quadrada ou hexagonal
     *
     * @param gradeQNH - Número de neurônios na Horizontal da grade
     * @param gradeQNV - Número de neurônios na Vertical da grade
     * 
     * @param gradeNEsp - Espaçamento entre os neurônios. Esse atributo vai ter influência na posição inicial 
     * dos neurônios, onde, esse valor corresponde a distância, tanto horizontal, quanto vertical entre os neurônios 
     * que compõem a grade
     * 
     * @param hexagonal - Atributo booleano que corresponde ao tipo de visinhança. Se valer true, uma grade de vizinhança 
     * hexagonal entre os neurônios é criada. Caso contrário, os a grade é gerada com vizinhança quadrada mesmo
     * 
     * @return double[][][] - Retorna a grade representada por uma matriz de posições bidimensionais. Onde, cada 
     * elemento da matriz é um vetor de dois elementos (x e y)
     */
    public double[][][] geraGrade( int gradeQNH, int gradeQNV, int gradeNEsp, boolean hexagonal ) {
        double gradeNEspY = Math.sqrt( Math.pow( gradeNEsp, 2 ) - Math.pow( gradeNEsp*.5d, 2 ) );
        
        double[][][] grade = new double[gradeQNV][gradeQNH][2];
        for( int i = 0; i < gradeQNV; i++ ) {
            for( int j = 0; j < gradeQNH; j++ ) {             
                double x;
                double y;
                
                if ( hexagonal ) {
                    if ( i % 2 == 1 ) {
                        x = j*gradeNEsp + gradeNEsp*.5f;
                    } else {
                        x = j*gradeNEsp;
                    }
                    y = i*gradeNEspY; 
                } else {
                    x = j * gradeNEsp;
                    y = i * gradeNEsp;
                }    
                grade[i][j][0] = x;
                grade[i][j][1] = y;
            }
        }
        return grade;
    }
    
    /**
     *
     * @param config
     * @return
     */
    public SOMMatrizU geraMatrizU( SOMCFG config ) {
        int grade_larg = config.getGradeQuantNeuroniosHorizontal();
        int grade_alt = config.getGradeQuantNeuroniosVertical();
        Neuronio[][] neuronios = config.getNeuronios();
                
        int mat_u_larg = 2*grade_larg - 1;
        int mat_u_alt = 2*grade_alt - 1;
        
        double[][] mat_u = new double[ mat_u_alt ][ mat_u_larg ];
        
        for( int i = 0; i < grade_alt; i++ ) {
            for( int j = 0; j < grade_larg; j++ ) {
                double[] peso = neuronios[i][j].getPeso();
                if ( i > 0 && j > 0 && (i-1)*2 < mat_u_alt && (j-1)*2+1 < mat_u_larg ) {
                    double[] peso2 = neuronios[i-1][j].getPeso();
                    mat_u[(i-1)*2][(j-1)*2+1] = this.distancia( peso, peso2 );				
                }
                			
                if ( i > 0 && j > 0 && (i-1)*2+1 < mat_u_alt && (j-1)*2 < mat_u_larg ) {
                    double[] peso2 = neuronios[i][j-1].getPeso();
                    mat_u[(i-1)*2+1][(j-1)*2] = this.distancia( peso, peso2 );
                }
                					
                if ( (i-1)*2+2 < mat_u_alt && (j-1)*2 >= 0 && (j-1)*2 < mat_u_larg && i+1 < grade_alt && j-1 >= 0 ) {
                    double[] peso2 = neuronios[i+1][j-1].getPeso();                   
                    mat_u[(i-1)*2+2][(j-1)*2] = this.distancia( peso, peso2 );
                }                
            }
        }
        
        for( int i = 0; i < mat_u_alt; i++ ) {
            for( int j = 0; j < mat_u_larg; j++ ) {        
                if ( i % 2 == 0 && j % 2 == 0 )	{
                    double soma_d = 0;
		    int cont = 0;
                    if ( i-1 >= 0 ) {
                        soma_d += mat_u[i-1][j];
                        cont++;
                    }				
                    if ( j-1 >= 0 ) {
                        soma_d += mat_u[i][j-1];
                        cont++;
                    }
                    if ( j+1 < mat_u_larg ) {
                        soma_d += mat_u[i][j+1];
                        cont++;
                    }
                    if ( i+1 < mat_u_alt && j-1 >= 0 ) {
                        soma_d += mat_u[i+1][j-1];
                        cont++;
                    }
                    if ( i+1 < mat_u_alt && j+1 < mat_u_larg ) {
                        soma_d += mat_u[i+1][j+1];
                        cont++;
                    }
                    if ( i+1 < mat_u_alt ) {
                        soma_d += mat_u[i+1][j];
                        cont++;
                    }								
                    if ( cont >= 0 ) {
                        mat_u[i][j] = soma_d / cont;
                    }
                }
            }
        }
        return new SOMMatrizU( mat_u, mat_u_larg, mat_u_alt, config );
    }
        
    /**
     *
     * @param mu
     * @return
     */
    public SOMMatrizUSuperficie geraMatrizUSuperficie( SOMMatrizU mu ) {
        double[][] mat_u = mu.getMatrizU();                
        int nl = mu.getAltura();
        int nc = mu.getLargura();
        
        SOMCFG config = mu.getConfig();
        int gradeQNV = config.getGradeQuantNeuroniosVertical();
        int gradeQNH = config.getGradeQuantNeuroniosHorizontal();        
        
        int qw = 5;
        int qh = 5;
        
        double minMatUValor = Double.MAX_VALUE;
        double maxMatUValor = Double.MIN_VALUE;
        for( int i = 0; i < nl; i++ ) {
            for( int j = 0; j < nc; j++ ) {
                if ( i % 2 == 0 && j % 2 == 0 ) { 
                    double v = mat_u[i][j];
                    if ( v < minMatUValor )
                        minMatUValor = v;
                    if ( v > maxMatUValor )
                        maxMatUValor = v;
                }
            }
        }
        
        double[] vx = new double[ gradeQNV ];
        double[] vz = new double[ gradeQNH ];
        double[][] my = new double[ gradeQNV ][ gradeQNH ];
        
        for( int i = 0; i < gradeQNV; i++ )
            vx[i] = i * qh;
        for( int j = 0; j < gradeQNH; j++ ) 
            vz[j] = j * qw;
        
        int k = 0;
	for( int i = 0; i < nl; i++ ) {
            for( int j = 0; j < nc; j++ ) {
                if ( i % 2 == 0 && j % 2 == 0 ) { 
                    int gi = k / gradeQNH;
                    int gj = k % gradeQNH;                
                    double v = mat_u[i][j];
                    int sinza = (int) Math.round( 255.0d * ( ( v - minMatUValor ) / ( maxMatUValor - minMatUValor ) ) );
                    my[gi][gj] = sinza;
                                        
                    k++;
                }
            }
        }	            
        
        SOMMatrizUSuperficie superficie = new SOMMatrizUSuperficie();
        superficie.setVetorX( vx );
        superficie.setVetorZ( vz );
        superficie.setMatrizY( my );
        
        return superficie;
    }         

    /**
     *
     * @param p
     * @param q
     * @return
     */
    public double distancia( double[] p, double[] q ) {                        
        double d = 0;
        for( int k = 0; k < p.length; k++ )
            d += Math.pow( p[k] - q[k], 2 );        
        return Math.sqrt( d );
    }
    
    public void calculaIndicesRandomicos( int[] indices ) {
        int n = indices.length;
                
        for( int i = 0; i < n; i++ )
            indices[ i ] = i;
        
        for( int k = 0; k < 3; k++ ) {
            for( int i = 0; i < n; i++ ) {
                int r = (int)Math.round( Math.random() * (n-1) );
                int aux = indices[ i ];
                indices[ i ] = indices[ r ];
                indices[ r ] = aux;
            }
        }
    }
    
    /**
     *
     */
    public void continuarSePausado() {
        if ( status == PAUSADO ) {
            pausar = false;
            synchronized( this ) {
                notifyAll();
            }
        }
    }
    
    /**
     *
     * @return
     */
    public int getIteracao() {
        return it;
    }
    
    /**
     *
     */
    public void pausar() {
        pausar = true;
    }
    
    /**
     *
     * @param fim
     * @return
     */
    public boolean addSOMFim( SOMFim fim ) {
        return fins.add( fim );
    }
    
    /**
     *
     * @param fim
     * @return
     */
    public boolean removeSOMFim( SOMFim fim ) {
        return fins.remove( fim );
    }
    
    /**
     *
     * @return
     */
    public int getStatus() {
        return status;
    }

    /**
     *
     * @return
     */
    public boolean isPausar() {
        return pausar;
    }        
    
}
