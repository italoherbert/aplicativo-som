package som;

import java.util.ArrayList;
import java.util.List;

public class SOMRandomDados {
    
    public double[][] geraRandomDados3D( int quantDadosMult, double raioDisp, double[][] centroides ) {                                
	List<double[]> vet = new ArrayList();
	int vet_len = 0;
        int mult_len = quantDadosMult/centroides.length;
        
        double[][] amostras = new double[ mult_len*centroides.length ][ 3 ];

	for( int i = 0; i < mult_len; i++ ) {
            for( int j = 0; j < centroides.length; j++ ) {
                int ind = i * centroides.length + j;
                boolean achou = false;
                double x, y, z;
                do {
                    double r2 = raioDisp * Math.random();
                    double phi = Math.PI * Math.random();
                    double theta = 2 * Math.PI * Math.random();
                    x = centroides[j][0] + r2 * Math.sin( phi ) * Math.cos( theta );
                    y = centroides[j][1] + r2 * Math.sin( phi ) * Math.sin( theta );
                    z = centroides[j][2] + r2 * Math.cos( phi );	
                    int l = 0;
                    while( l < vet_len && achou == false ) {
                        double vet_x = vet.get( l )[0];
                        double vet_y = vet.get( l )[1];
                        double vet_z = vet.get( l )[2];
                        if ( x == vet_x && y == vet_y && z == vet_z )
                                achou = true;
                        l++;
                    }
                } while( achou );
                amostras[ind] = new double[] { x, y, z };
                vet.add( new double[] { x, y, z } );
                vet_len++;
            }
        }
        
        return amostras;
    }  
    
    public double[][] geraRandomDados2D( int quantDadosMult, double raioDisp, double[][] centroides ) {
        double[][] dados = new double[ quantDadosMult ][ 2 ];
             
        for( int i = 0; i < quantDadosMult; i++ ) {
            int ic = i % centroides.length;
            double[] coord = centroides[ ic ];
            
            double x, y;
            boolean achou;
            do {
                double raio = Math.random() * raioDisp;
                double ang = -Math.PI + ( Math.random() * 2.0d * Math.PI );
                x = coord[0] + raio * Math.cos( ang );
                y = coord[1] + raio * Math.sin( ang );
                achou = false;
                for( int j = 0; !achou && j < i; j++ )
                    if ( x == dados[j][0] && y == dados[j][1] )
                        achou = true;                
            } while( achou );
            dados[i][0] = x;
            dados[i][1] = y;            
        }
        return dados;
    }
    
}
