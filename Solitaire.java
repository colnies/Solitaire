package solitaire;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
import java.util.NoSuchElementException;

/**
 * This class implements a simplified version of Bruce Schneier's Solitaire Encryption algorithm.
 * 
 * @author RU NB CS112
 */
public class Solitaire {
	
	/**
	 * Circular linked list that is the deck of cards for encryption
	 */
	CardNode deckRear;
	
	/**
	 * Makes a shuffled deck of cards for encryption. The deck is stored in a circular
	 * linked list, whose last node is pointed to by the field deckRear
	 */
	public void makeDeck() {
		// start with an array of 1..28 for easy shuffling
		int[] cardValues = new int[28];
		// assign values from 1 to 28
		for (int i=0; i < cardValues.length; i++) {
			cardValues[i] = i+1;
		}
		
		// shuffle the cards
		Random randgen = new Random();
 	        for (int i = 0; i < cardValues.length; i++) {
	            int other = randgen.nextInt(28);
	            int temp = cardValues[i];
	            cardValues[i] = cardValues[other];
	            cardValues[other] = temp;
	        }
	     
	    // create a circular linked list from this deck and make deckRear point to its last node
	    CardNode cn = new CardNode();
	    cn.cardValue = cardValues[0];
	    cn.next = cn;
	    deckRear = cn;
	    for (int i=1; i < cardValues.length; i++) {
	    	cn = new CardNode();
	    	cn.cardValue = cardValues[i];
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
	    }
	}
	
	/**
	 * Makes a circular linked list deck out of values read from scanner.
	 */
	public void makeDeck(Scanner scanner) 
	throws IOException {
		CardNode cn = null;
		if (scanner.hasNextInt()) {
			cn = new CardNode();
		    cn.cardValue = scanner.nextInt();
		    cn.next = cn;
		    deckRear = cn;
		}
		while (scanner.hasNextInt()) {
			cn = new CardNode();
	    	cn.cardValue = scanner.nextInt();
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
		}
	}
	
	/**
	 * Implements Step 1 - Joker A - on the deck.
	 */
	void jokerA() {
		CardNode bd=deckRear;
		CardNode tgt=deckRear.next;
		CardNode front=deckRear.next.next;
		
		do{
			
			if(tgt.cardValue==27){
				//Special Conditions//
				if(tgt==deckRear){
					deckRear=front;
				}else if(tgt.next==deckRear){
					deckRear=tgt;
				}
				
				bd.next=front;
				tgt.next=front.next;
				front.next=tgt;
				return;
			}
			
			bd=bd.next;
			tgt=tgt.next;
			front=front.next;
			
		}while(tgt!=deckRear.next);
	}
	
	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB() {
		
		CardNode bd=deckRear;
		CardNode tgt=deckRear.next;
		CardNode front=tgt.next.next;
		
			do{
				
				if(tgt.cardValue==28){
					
					if(tgt==deckRear||tgt.next==deckRear){
						deckRear=deckRear.next;
					}else if(front==deckRear){
						deckRear=tgt;
					}
						bd.next=tgt.next;
						tgt.next=front.next;
						front.next=tgt;
					return;
				}
				
				bd=bd.next;
				tgt=tgt.next;
				front=front.next;
				
			}while(tgt!=deckRear.next);
	}
	
	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut() {
		CardNode bdfirst=deckRear;
		CardNode first;
		CardNode second;
		CardNode secNext;
		CardNode bdSec;
		
			while(bdfirst.next.cardValue!=27&&bdfirst.next.cardValue!=28){
				bdfirst=bdfirst.next;
			}
			
				first=bdfirst.next;
				bdSec=first;
				
			while(bdSec.next.cardValue!=27&&bdSec.next.cardValue!=28){
				bdSec=bdSec.next;
			}
			
				second=bdSec.next;
				secNext=second.next;
				
					//Special Conditions//
					if(first==deckRear.next){
						deckRear=second;
						return;
					}if(second==deckRear){
						deckRear=bdfirst;
						return;
					}
					
				second.next=deckRear.next;
				deckRear.next=first;
				bdfirst.next=secNext;
				deckRear=bdfirst;
	}
	
	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
	void countCut() {	
		int cnt=deckRear.cardValue;
		CardNode end=deckRear;
		CardNode ptr=deckRear;
		
			if(cnt==28||cnt==27){
				return;
			}
			for(int i=0;i<cnt;i++){
				ptr=ptr.next;
			}
			do{
				end=end.next;
			}while(end.next!=deckRear);
			
				end.next=deckRear.next;
				deckRear.next=ptr.next;
				ptr.next=deckRear;	
	}
	
	/**
	 * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count Cut, then
	 * counts down based on the value of the first card and extracts the next card value 
	 * as key. But if that value is 27 or 28, repeats the whole process (Joker A through Count Cut)
	 * on the latest (current) deck, until a value less than or equal to 26 is found, which is then returned.
	 * 
	 * @return Key between 1 and 26
	 */
	int getKey() {
		
		int data=0;
		CardNode ptr;
		
			for(int j=0;j<28;j++){
				jokerA();
				//printList(deckRear);
				jokerB();
				//printList(deckRear);
				tripleCut();
				//printList(deckRear);
				countCut();
				//printList(deckRear);
				
				ptr=deckRear.next;
				data=ptr.cardValue;
					if(data==28){
						data=27;
					}
				for(int i=0;i<data;i++){
					ptr=ptr.next;
				}
				if(ptr.cardValue!=27&&ptr.cardValue!=28)
					return ptr.cardValue;
		}
	    return -1;
	}
	
	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear Rear pointer
	 */
	private static void printList(CardNode rear) {
		if (rear == null) { 
			return;
		}
		System.out.print(rear.next.cardValue);
		CardNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.cardValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {	
		String enc="";
		char tmp;
		int alp;
		int key;
		int pos;
			for(int i=0;i<message.length();i++){
				tmp=message.charAt(i);
					if(Character.isLetter(tmp)==true){
						key=getKey();
						//System.out.println("Key found: "+key);
						alp=tmp-'A'+1;
						pos=key+alp;
							if(pos>26)
								pos-=26;
							
							tmp=(char)(pos-1+'A');
							enc+=tmp;
					}
			}
	    return enc;
	}
	
	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {	
		String dec="";
		char tmp;
		int alp;
		int key;
		int pos;
			for(int i=0;i<message.length();i++){
				tmp=message.charAt(i);
					if(Character.isLetter(tmp)==true){
						key=getKey();
						//System.out.println("Key found: "+key);
						alp=tmp-'A'+1;
						pos=alp-key;
							if(pos<=0)
								pos+=26;
							
							tmp=(char)(pos-1+'A');
							dec+=tmp;
					}
			}
	    return dec;
	}
}
