package som;

public class SOMMath {
    
    public double distancia( double[] p, double[] q, double dim ) {
        double d = 0;
        for( int i = 0; i < dim; i++ )
            d += Math.pow( p[i] - q[i], 2 );        
        return Math.sqrt( d );
    }
    
}
