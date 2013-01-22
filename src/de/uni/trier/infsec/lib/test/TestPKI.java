package de.uni.trier.infsec.lib.test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import junit.framework.TestCase;

import org.junit.Test;

import de.uni.trier.infsec.functionalities.pki.real.PKIEnc;
import de.uni.trier.infsec.functionalities.pki.real.PKIEnc.Decryptor;
import de.uni.trier.infsec.functionalities.pki.real.PKIEnc.Encryptor;
import de.uni.trier.infsec.functionalities.pki.real.PKIServer;
import de.uni.trier.infsec.utils.Utilities;

public class TestPKI extends TestCase {

	public static byte[] TEST_ID1  = { 0x11, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 };
	public static byte[] TEST_ID2  = { 0x11, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x10 };
	public static byte[] TEST_DATA = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 };
	
	@Test
	public void testRealPKIRemote() throws IOException {
		Process pr = null;
		try {
			String cmd = "java";
			ProcessBuilder p = new ProcessBuilder(cmd, "-cp", System.getProperties().getProperty("java.class.path", null), "de.uni.trier.infsec.functionalities.pki.real.PKIServer");
			p.redirectErrorStream(true);
			pr = p.start();

			System.setProperty("remotemode", Boolean.toString(true));

			Decryptor d1 = PKIEnc.register(TEST_ID1);
			Decryptor d2 = PKIEnc.register(TEST_ID2);

			Encryptor e1 = PKIEnc.getEncryptor(TEST_ID1);
			Encryptor e2 = PKIEnc.getEncryptor(TEST_ID2);
			
			System.out.println("plaintxt: " + Utilities.byteArrayToHexString(TEST_DATA));
			byte[] ctxt1 = e1.encrypt(TEST_DATA);
			System.out.println("ciphertxt: " + Utilities.byteArrayToHexString(ctxt1));
			byte[] ptxt = d1.decrypt(ctxt1);
			System.out.println("plaintxt: " + Utilities.byteArrayToHexString(ptxt));
			assertTrue("Plaintext has changed during encryption", Arrays.equals(ptxt, TEST_DATA));

			System.out.println("plaintxt: " + Utilities.byteArrayToHexString(TEST_DATA));
			byte[] ctxt2 = e2.encrypt(TEST_DATA);
			System.out.println("ciphertxt: " + Utilities.byteArrayToHexString(ctxt2));
			byte[] ptxt2 = d2.decrypt(ctxt2);
			System.out.println("plaintxt: " + Utilities.byteArrayToHexString(ptxt2));
			assertTrue("Plaintext has changed during encryption", Arrays.equals(ptxt2, TEST_DATA));
		} finally {
			if (pr != null) {
				pr.destroy();
			}
		}
	}

//	@Test
//	public void testRealPKILocal() throws Exception {
//		System.setProperty("remotemode", Boolean.toString(false));
//		Decryptor d1 = PKIEnc.register(TEST_ID1);
//		Encryptor e1 = PKIEnc.getEncryptor(TEST_ID1);
//
//		System.out.println("plaintxt: " + Utilities.byteArrayToHexString(TEST_DATA));
//		byte[] ctxt1 = e1.encrypt(TEST_DATA);
//		System.out.println("ciphertxt: " + Utilities.byteArrayToHexString(ctxt1));
//		byte[] ptxt = d1.decrypt(ctxt1);
//		System.out.println("plaintxt: " + Utilities.byteArrayToHexString(ptxt));
//		assertTrue("Plaintext has changed during encryption", Arrays.equals(ptxt, TEST_DATA));
//	}
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		File f = new File(PKIServer.DEFAULT_DATABASE);
		f.delete();
	}

}