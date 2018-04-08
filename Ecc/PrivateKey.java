package Ecc;

import java.math.BigInteger;



/**
 * The private key of the El Gamal Elliptic Curve Cryptography.
 * 
 * The key consists of:
 * c, the elliptic curve used in the calculations,
 * k is the private key, a randomly-generated integer, satisfying 1 <= k < p-1.
 */
public class PrivateKey {
    private EllipticCurve c;
    private BigInteger k;
    
    public PrivateKey(EllipticCurve c, BigInteger k) {
        this.c = c;
        this.k = k;
    }
    
    public void setCurve(EllipticCurve c) {
        this.c = c;
    }
    
    public EllipticCurve getCurve() {
        return c;
    }
    
    public void setK(BigInteger k) {
        this.k = k;
    }
    
    public BigInteger getK() {
        return k;
    }
}
