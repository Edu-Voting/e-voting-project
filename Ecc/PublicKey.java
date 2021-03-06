package Ecc;


/**
 * The public key for El Gamal Elliptic Curve Cryptography.
 * 
 * The key consists of:
 * c, the elliptic curve used in the calculations,
 * P_K, the point obtained from k * G, where k is the corresponding private key
 * and G is the base point of c.
 */
public class PublicKey {
    private EllipticCurve c;
    private ECPoint P_K;

    public PublicKey(EllipticCurve c, ECPoint P_K) {
        this.c = c;
        this.P_K = P_K;
    }
    
    public EllipticCurve getCurve() {
        return c;
    }
    
    public void setCurve(EllipticCurve c) {
        this.c = c;
    }

    public ECPoint getKey() {
        return P_K;
    }

    public void setKey(ECPoint P_K) {
        this.P_K = P_K;
    }
}
