package org.komparator.security;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.crypto.*;



public class CryptoUtil {
	private static final String ASYM_ALGO = "RSA/ECB/PKCS1Padding";
	private static final String SIGNATURE_ALGO = "SHA256withRSA";
	
	public static boolean outputFlag = true;

	public static PublicKey getPublicKey(String publicKeyPath){
		PublicKey pub = null;
		try {
			
			InputStream in = getResourceAsStream(publicKeyPath);
			CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
			Certificate cert = certFactory.generateCertificate(in);
			
			pub = getPublicKeyFromCertificate(cert);
		
		}  catch (CertificateException e) {
			throw new RuntimeException("could not create certificate");
		}
		return pub;
	}
	

    public static byte[] asymCipher(byte[] data, PublicKey key ) {
    	Cipher cipher = null;
    	byte[] cipherBytes = null;
    	try {
			cipher = Cipher.getInstance(ASYM_ALGO);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			cipherBytes = cipher.doFinal(data);	
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new RuntimeException("NoSuchPaddingException");
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			throw new RuntimeException("IllegalBlockSizeException");
    	} catch (InvalidKeyException e) {
    		throw new RuntimeException("InvalidKeyException");
		}
    	return cipherBytes;
    }
    
    public static byte[] asymDecipher(byte[] encrypData, PrivateKey priv) {
    	Cipher cipher = null;
    	byte[] decipheredBytes = null;
    	
    	try {
			cipher = Cipher.getInstance(ASYM_ALGO);
			cipher.init(Cipher.DECRYPT_MODE, priv);
			decipheredBytes = cipher.doFinal(encrypData);
    	} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new RuntimeException("NoSuchAlgorithmException");
		} catch (InvalidKeyException e) {
			throw new RuntimeException("InvalidKeyException");
		} catch (BadPaddingException e) {
			throw new RuntimeException("BadPaddingException");
		} catch (IllegalBlockSizeException e){
			throw new RuntimeException("IllegalBlockSizeException");
		}
		
    	return decipheredBytes;
    }
    
    public static KeyPair read(String publicKeyPath, String privateKeyPath ,String keyStorePassword,String keyPassword , String alias) throws Exception {


		byte[] pubEncoded = readFile(publicKeyPath);

		InputStream in = new ByteArrayInputStream(pubEncoded);
		CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
		Certificate cert = certFactory.generateCertificate(in);
		
		PublicKey pub = getPublicKeyFromCertificate(cert);

		
		PrivateKey priv = getPrivateKeyFromKeyStoreResource(privateKeyPath, keyStorePassword.toCharArray(),
		 alias ,  keyPassword.toCharArray());

		
		KeyPair keys = new KeyPair(pub, priv);
		return keys;
	}
    
    
    
    /*functions for digital signatures*/
    
   
	public static byte[] makeDigitalSignature(byte[] bytes, PrivateKey priv) throws Exception {

		// get a signature object and sign the plain text with the private key
		Signature sig = Signature.getInstance(SIGNATURE_ALGO);
		sig.initSign(priv);
		sig.update(bytes);
		byte[] signature = sig.sign();

		return signature;
	}

	public static boolean verifyDigitalSignature(byte[] cipherDigest, byte[] bytes, PublicKey pub) throws Exception {

		// verify the signature with the public key
		Signature sig = Signature.getInstance(SIGNATURE_ALGO);
		sig.initVerify(pub);
		sig.update(bytes);
		try {
			return sig.verify(cipherDigest);
		} catch (SignatureException se) {
			System.err.println("Caught exception while verifying " + se);
			return false;
		}
	}
    
	
	
    
    
    /* auxiliary functions necessary for the main methods*/
    
    public static PrivateKey getPrivateKeyFromKeyStoreResource(String keyStoreResourcePath, char[] keyStorePassword,
			String keyAlias, char[] keyPassword)
			throws FileNotFoundException, KeyStoreException, UnrecoverableKeyException {
		KeyStore keystore = readKeystoreFromResource(keyStoreResourcePath, keyStorePassword);
		return getPrivateKeyFromKeyStore(keyAlias, keyPassword, keystore);
	}
    
	public static KeyStore readKeystoreFromResource(String keyStoreResourcePath, char[] keyStorePassword)
			throws KeyStoreException {
		InputStream is = getResourceAsStream(keyStoreResourcePath);
		return readKeystoreFromStream(is, keyStorePassword);
	}
	
	private static KeyStore readKeystoreFromStream(InputStream keyStoreInputStream, char[] keyStorePassword)
			throws KeyStoreException {
		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		
		try {
			keystore.load(keyStoreInputStream, keyStorePassword);
		} catch (NoSuchAlgorithmException | CertificateException | IOException e) {
			throw new KeyStoreException("Could not load key store", e);
		} finally {
			closeStream(keyStoreInputStream);
		}
		return keystore;
	}
	
	public static PrivateKey getPrivateKeyFromKeyStore(String keyAlias, char[] keyPassword, KeyStore keystore)
			throws KeyStoreException, UnrecoverableKeyException {
		PrivateKey key;
		try {
			key = (PrivateKey) keystore.getKey(keyAlias, keyPassword);
		} catch (NoSuchAlgorithmException e) {
			throw new KeyStoreException(e);
		}
		return key;
	}
	
	private static void closeStream(InputStream in) {
		try {
			if (in != null)
				in.close();
		} catch (IOException e) {
			// ignore
		}
	}
	
	private static InputStream getResourceAsStream(String resourcePath) {
		// uses current thread's class loader to also work correctly inside
		// application servers
		// reference: http://stackoverflow.com/a/676273/1294979
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
		return is;
	}
    
    public static PublicKey getPublicKeyFromCertificate(Certificate certificate) {
		return certificate.getPublicKey();
	}
    
    private static byte[] readFile(String path) throws FileNotFoundException, IOException {
		FileInputStream fis = new FileInputStream(path);
		byte[] content = new byte[fis.available()];
		fis.read(content);
		fis.close();
		return content;
	}
    
    
    
}
