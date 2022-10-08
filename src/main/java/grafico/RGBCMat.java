package grafico;

import java.awt.Color;

public class RGBCMat implements CMat {
            
    private int[][][] mat;

    public RGBCMat() {}
    
    public RGBCMat( int[][][] mat ) {
        this.mat = mat;
    }
    
    @Override
    public Color getCor( int x, int y ) {
        return new Color( mat[y][x][0], mat[y][x][1], mat[y][x][2] );        
    }

    @Override
    public int getMatLargura() {
        if ( mat != null )
            if ( mat.length > 0 )
                return mat[0].length;
        return 0;
    }

    @Override
    public int getMatAltura() {
        if ( mat != null )
            return mat.length;
        return 0;
    }
    
}
