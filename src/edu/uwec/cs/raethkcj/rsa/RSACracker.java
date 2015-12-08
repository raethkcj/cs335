package edu.uwec.cs.raethkcj.rsa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;

public class RSACracker {
	public static void main(String[] args) throws FileNotFoundException {
//		final long TWO_MINUTES = 120 * 1000;
//		long executionTime = 0;
//		int bitLength = 14;
//		
//		File f = new File("RSACrackerTimes.xls");
//		PrintWriter pw = new PrintWriter(f);
//		pw.println("Bitlength\tTime (ms)");
//		
//		while(executionTime < TWO_MINUTES) {
//			ArrayList<BigInteger> publicKey = BigIntegerRSA.generatePublicKeyOfBitLength(bitLength);
//			BigInteger pq = publicKey.get(0);
//			long startTime = System.currentTimeMillis();
//			BigInteger[] pAndQ = primeFactorsOf(pq);
//			executionTime = System.currentTimeMillis() - startTime;
//			System.out.println("Bitlength: " + bitLength + " Time (ms): " + executionTime);
//			pw.println("" + bitLength + "\t" + executionTime);
//			bitLength++;
//		}
//		
//		pw.close();
		decodeMessage();
	}
	
	public static void decodeMessage() {
		BigInteger pq = new BigInteger("608485549753");
		BigInteger e = new BigInteger("7");
		
		BigInteger[] message = new BigInteger[8];
		message[0] = new BigInteger("576322461849");
		message[1] = new BigInteger("122442824098");
		message[2] = new BigInteger("34359738368");
		message[3] = new BigInteger("29647771149");
		message[4] = new BigInteger("140835578744");
		message[5] = new BigInteger("546448062804");
		message[6] = new BigInteger("120078454173");
		message[7] = new BigInteger("42618442977");
		
		BigInteger[] pAndQ = primeFactorsOf(pq);
		BigInteger p = pAndQ[0];
		BigInteger q = pAndQ[1];
		BigInteger phiPQ = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		BigInteger d = e.modInverse(phiPQ);
		
		String decodedMessage = BigIntegerRSA.decrypt(message, pq, d);
		System.out.println(decodedMessage);
	}
	
	public static BigInteger[] primeFactorsOf(BigInteger pq) {
		BigInteger p = new BigInteger("3");
		while(!pq.mod(p).equals(BigInteger.ZERO)) {
			p = nextPrime(p);
		}
		return new BigInteger[] { p, pq.divide(p) };
	}
	
	public static BigInteger nextPrime(BigInteger p) {
		BigInteger two = new BigInteger("2");
		p = p.add(two);
		while(!isPrime(p)) {
			//Skip all the even numbers
			p = p.add(two);
		}
		return p;
	}
	
	public static boolean isPrime(BigInteger p) {
		BigInteger i = new BigInteger("2");
		//Runs until sqrt(p), or p % i == 0
		while(i.compareTo(p.divide(i)) < 0 && !p.mod(i).equals(BigInteger.ZERO) ) {
			i = i.add(BigInteger.ONE);
		}
		return !p.mod(i).equals(BigInteger.ZERO);
	}
}
