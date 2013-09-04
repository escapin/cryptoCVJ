package de.uni.trier.infsec.functionalities.pkisig;

import de.uni.trier.infsec.environment.Environment;
import de.uni.trier.infsec.functionalities.pkienc.PKIError;
import de.uni.trier.infsec.lib.network.NetworkError;
import de.uni.trier.infsec.utils.MessageTools;

public class RegisterSig {

	public static void registerVerifier(Verifier verifier, int id, byte[] pki_domain) throws PKIError, NetworkError {
		if( Environment.untrustedInput() == 0 ) throw new NetworkError();
		if( registeredAgents.fetch(id, pki_domain) != null ) // verified.ID is registered?
			throw new PKIError();
		registeredAgents.add(id, pki_domain, verifier);
	}

	public static Verifier getVerifier(int id, byte[] pki_domain) throws PKIError, NetworkError {
		if( Environment.untrustedInput() == 0 ) throw new NetworkError();
		Verifier verif = registeredAgents.fetch(id, pki_domain);
		if (verif == null)
			throw new PKIError();
		return verif.copy();
	}

	/// IMPLEMENTATION ///

	private static class RegisteredAgents {
		private static class VerifierList {
			final int id;
			byte[] domain;			
			Verifier verifier;
			VerifierList  next;
			VerifierList(int id, byte[] domain,  Verifier verifier, VerifierList next) {
				this.id = id;
				this.domain = domain;
				this.verifier = verifier;
				this.next = next;
			}
		}

		private VerifierList first = null;

		public void add(int id, byte[] domain, Verifier verif) {
			first = new VerifierList(id, domain, verif, first);
		}

		Verifier fetch(int ID, byte[] domain) {
			for( VerifierList node = first;  node != null;  node = node.next ) {
				if( ID == node.id && MessageTools.equal(domain, node.domain) )
					return node.verifier;
			}
			return null;
		}
	}

	private static RegisteredAgents registeredAgents = new RegisteredAgents();
}
