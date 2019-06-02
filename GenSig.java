import java.io.*;
import java.security.*;

class GenSig {

    public static void main(String[] args) {

        /* Generate a DSA signature */

        if (args.length != 1) {
            System.out.println("Usage: GenSig nameOfFileToSign");
        }
        else try {

        // Create a Key Pair Generator
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
        
        // Initialize the Key Pair Generator
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(1024, random);
        
        // Generate the Pair of Keys
        KeyPair pair = keyGen.generateKeyPair();
        PrivateKey priv = pair.getPrivate();
        PublicKey pub = pair.getPublic();
        
        // Get a Signature Object
        Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
        
        // Initialize the Signature Object
        dsa.initSign(priv);

        //Supply the Signature Object the Data to Be Signed
        FileInputStream fis = new FileInputStream(args[0]);
        BufferedInputStream bufin = new BufferedInputStream(fis);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = bufin.read(buffer)) >= 0) {
            dsa.update(buffer, 0, len);
        };
        bufin.close();

        // Generate the Signature
        byte[] realSig = dsa.sign();

        /* save the signature in a file */
        FileOutputStream sigfos = new FileOutputStream("sig");
        sigfos.write(realSig);
        sigfos.close();

        /* save the public key in a file */
        byte[] key = pub.getEncoded();
        FileOutputStream keyfos = new FileOutputStream("lizzypk");
        keyfos.write(key);
        keyfos.close();

        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }
}