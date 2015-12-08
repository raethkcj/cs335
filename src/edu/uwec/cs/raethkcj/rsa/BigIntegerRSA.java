package edu.uwec.cs.raethkcj.rsa;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

public class BigIntegerRSA {
	
	public static ArrayList<BigInteger> generatePublicKeyOfBitLength(int bitLength) {
		ArrayList<BigInteger> pAndQ = findPAndQ(bitLength);
		BigInteger p = pAndQ.get(0);
		BigInteger q = pAndQ.get(1);
		// Find PQ and phiPQ
		BigInteger pq = p.multiply(q);
		BigInteger phiPQ = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		BigInteger e = findE(phiPQ);
		
		ArrayList<BigInteger> publicKey = new ArrayList<BigInteger>();
		publicKey.add(pq);
		publicKey.add(e);
		return publicKey;
	}
	
	public static ArrayList<BigInteger> findPAndQ(int bitLength) {
		// Find P and Q
		SecureRandom r = new SecureRandom();
		BigInteger p = BigInteger.ZERO;
		BigInteger q = BigInteger.ZERO;
		while (p.equals(q)) {
			p = new BigInteger(bitLength / 2, 100, r);
			q = new BigInteger(bitLength / 2, 100, r);
		}
		
		ArrayList<BigInteger> pAndQ = new ArrayList<BigInteger>();
		pAndQ.add(p);
		pAndQ.add(q);
		
		return pAndQ;
	}
	
	public static BigInteger findE(BigInteger phiPQ) {
		// Find E
		BigInteger e = new BigInteger("2");
		while (phiPQ.gcd(e).compareTo(BigInteger.ONE) > 0) {
			e = e.add(BigInteger.ONE);
		}	
		return e;
	}
	
	public static BigInteger[] encrypt(String message, BigInteger pq, BigInteger e) {
		// Encyrpt with:  message.modPow(e, n);
		BigInteger[] cipherMessage = new BigInteger[message.length()];
		
		for (int i=0; i<message.length(); i++) {
			BigInteger m = new BigInteger(((int)message.charAt(i)) + "");
			BigInteger c = m.modPow(e, pq);
			cipherMessage[i] = c;
		}

		return cipherMessage;
	}
	
	public static String decrypt(BigInteger[] cipherMessage, BigInteger pq, BigInteger d) {
	    // decrypt with: message.modPow(d, n);
	    String decodedMessage = "";
	    
	    for (int i=0; i<cipherMessage.length; i++) {
	    	BigInteger c = cipherMessage[i];
	    	BigInteger m = c.modPow(d, pq);
	    	decodedMessage += (char)(m.intValue());
	    }
		
	    return decodedMessage;
	}
}

