package de.uni.trier.infsec.pkenc.real;

import de.uni.trier.infsec.untrusted.crypto.Encryption;

/**
 * Real functionality for public-key encryption: Encryptor
 */
public final class Encryptor {

	private byte[] publKey = null;
	
	Encryptor(byte[] publicKey) { 
		publKey = publicKey;
	}
		
	public byte[] getPublicKey() {
		return publKey;
	}
	
	public byte[] encrypt(byte[] message) {
		return Encryption.encrypt(publKey, message);
	}
	
}
