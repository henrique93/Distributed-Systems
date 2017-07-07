package org.komparator.security;

import java.io.*;
import java.security.*;
import javax.crypto.*;
import java.util.*;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

import org.junit.*;

import static org.junit.Assert.*;

public class CryptoUtilTest {

    // static members
	
	
	
	/** Asymmetric cryptography algorithm. */
	private static final String ASYM_ALGO = "RSA";
	
	/** Asymmetric cryptography key size. */
	private static final int ASYM_KEY_SIZE = 2048;
	
	/**
	 * Asymmetric cipher: combination of algorithm, block processing, and
	 * padding.
	 */
	private static final String ASYM_CIPHER = "RSA/ECB/PKCS1Padding";
	
	/** Digital signature algorithm. */
	private static final String SIGNATURE_ALGO = "SHA256withRSA";
	
	private final String plainText = "This is the plain text!";
	private final byte[] plainBytes = plainText.getBytes();
	
	private PublicKey pubKey ;
	private	PrivateKey privKey;
	
    // one-time initialization and clean-up
    @BeforeClass
    public static void oneTimeSetUp() {
        // runs once before all tests in the suite
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // runs once after all tests in the suite
    }

    // members

    // initialization and clean-up for each test
    @Before
    public void setUp() throws Exception {
    	pubKey = CryptoUtil.getPublicKey("example.cer");
    	privKey = CryptoUtil.getPrivateKeyFromKeyStoreResource("example.jks", "1nsecure".toCharArray(), "example", "ins3cur3".toCharArray());
    }

    @After
    public void tearDown() {
        // runs after each test
    }

    // tests
    @Test
    public void test() {
        // do something ...

        // assertEquals(expected, actual);
        // if the assert fails, the test fails
    }

    
    @Test
	public void testCipherPublicDecipherPrivate() throws Exception {
    	// encrypt the plain text using the public key
		byte[] cipherBytes = CryptoUtil.asymCipher(plainBytes, pubKey);

		//  decipher the ciphered digest using the private key
		byte[] decipheredBytes = CryptoUtil.asymDecipher(cipherBytes, privKey);
		String newPlainText = new String(decipheredBytes);
		
		assertEquals(plainText, newPlainText);
	}
 
    @Test
  	public void testDigitalSignature() throws Exception {
  	
  		// get keyPair from the certificate and keystore 
  		
  		byte[] cipherDigest = CryptoUtil.makeDigitalSignature(plainBytes,privKey);

  		boolean result = CryptoUtil.verifyDigitalSignature(cipherDigest, plainBytes, pubKey);

  		assertTrue(result);
  	}
    
    @Test
  	public void testModifiedDigitalSignature() throws Exception {
  	  	
  		// get keyPair from the certificate and keystore 
  		
  		byte[] cipherDigest = CryptoUtil.makeDigitalSignature(plainBytes, privKey);

  		boolean result = CryptoUtil.verifyDigitalSignature(cipherDigest, plainBytes, pubKey);
  		
  		plainBytes[3] = 12;
  		
  		
  		KeyPair keys = new KeyPair(pubKey, privKey);
  		result = redigestDecipherCompare(cipherDigest, plainBytes, keys);

		assertFalse(result);

  	}
    
    
    
    
    private static boolean redigestDecipherCompare(byte[] cipherDigest, byte[] text, KeyPair keyPair) throws Exception {

		// get a message digest object
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

		// calculate the digest and print it out
		messageDigest.update(text);
		byte[] digest = messageDigest.digest();
		System.out.println("New digest:");
		System.out.println(printHexBinary(digest));

		// get a cipher object
		Cipher cipher = Cipher.getInstance(ASYM_CIPHER);

		// decipher the ciphered digest using the public key
		cipher.init(Cipher.DECRYPT_MODE, keyPair.getPublic());
		byte[] decipheredDigest = cipher.doFinal(cipherDigest);
		System.out.println("Deciphered digest:");
		System.out.println(printHexBinary(decipheredDigest));

		// compare digests
		if (digest.length != decipheredDigest.length)
			return false;

		for (int i = 0; i < digest.length; i++)
			if (digest[i] != decipheredDigest[i])
				return false;
		return true;
	}
    
}
