package poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Hand {
	private ArrayList<Card> CardsInHand;

	private int HandStrength;
	private int HiHand;
	private int LoHand;
	private int Kicker;
	private int Natural;
	private boolean bScored = false;

	private boolean Flush;
	private boolean Straight;
	private boolean Ace;
	private boolean Joker;
	private static Deck djoker= new Deck();

	public Hand(Deck d) {
		ArrayList<Card> Import = new ArrayList<Card>();
		for (int x = 0; x < 5; x++) {
			Import.add(d.drawFromDeck());
		}
		CardsInHand = Import;
		handle_joker();
	}
	public Hand(ArrayList<Card> subCard) {
		return;
	}
	public int getNatural(){
		return Natural;
	}

	public ArrayList<Card> getCards() {
		return CardsInHand;
	}

	public int getHandStrength() {
		return HandStrength;
	}

	public int getKicker() {
		return Kicker;
	}

	public int getHighPairStrength() {
		return HiHand;
	}

	public int getLowPairStrength() {
		return LoHand;
	}
	public boolean getJoker(){
		return Joker;
	}

	public boolean getAce() {
		return Ace;
	}

	public static Hand EvalHand(ArrayList<Card> SeededHand) {		 
			Deck d = new Deck(); 
			Hand h = new Hand(d); 
			h.CardsInHand = SeededHand; 
			h.EvalHand();
			
		return h; 
		}
	public static Hand PickBestHand(ArrayList<Hand> PlayerHand) throws exHand{
		Collections.sort(PlayerHand, Hand.HandRank);
		Hand D = PlayerHand.get(0);
		int same = 0;
		for (Hand B: PlayerHand){
			if (D.getHandStrength()== B.getHandStrength()){
				same++;
			}
		}
		if (same >= 2){
			throw new exHand();
		}
		return PlayerHand.get(0);
	}
	public void handle_joker(){
		ArrayList<Hand> PlayerHand = new ArrayList<Hand>();
		PlayerHand.add(this);
		int SubCardNO = 0;
		for (Card CardInHand: this.getCards()){
			PlayerHand = ExplodeHand(PlayerHand, SubCardNO);
			SubCardNO++;
		}
		for (Hand hEval: PlayerHand){
			hEval.EvalHand();
		}
		Collections.sort(PlayerHand, Hand.HandRank);
		try {
			PickBestHand(PlayerHand);
		} catch (exHand e) {
			System.out.println("You have two or more tied hands");
		}
	
	}
	
	public static ArrayList<Hand> ExplodeHand(ArrayList<Hand> CardsIn, int SubCardNO){
		
		ArrayList<Hand> SubHand = new ArrayList<Hand>();
		
		for (Hand h :CardsIn){
			ArrayList<Card> c = h.getCards();
			if (c.get(SubCardNO).getRank() == eRank.JOKER){
				for (Card JokerSub: djoker.getCards()){
					ArrayList<Card> SubCard = new ArrayList<Card>();
					SubCard.add(JokerSub);
					
					for (int d = 0; d < 0; d++){
						if (SubCardNO != d){
							SubCard.add(h.getCards().get(d));
						}
					Hand sub_hand= new Hand(SubCard);
					SubHand.add(sub_hand);
					}
				}
		
			}
			else{
				SubHand.add(h);
			}
		}
		return SubHand;
	}
	


	public void EvalHand() {
		// Evaluates if the hand is a flush and/or straight then figures out
		// the hand's strength attributes

		// Sort the cards!
		Collections.sort(CardsInHand, Card.CardRank);

		// counting Jokers
		int joker_count = 0;
		
		// Ace Evaluation
		if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == eRank.ACE) {
			Ace = true;
		}
		//Joker Count
		if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == eRank.JOKER){
			Joker = true;
			for (int x=0; x<5; x++){
				if (CardsInHand.get(x).getRank() == eRank.JOKER){
					joker_count ++;
				}
			}
		}

		// Flush Evaluation
		if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getSuit() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getSuit()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getSuit() == CardsInHand
						.get(eCardNo.ThirdCard.getCardNo()).getSuit()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getSuit() == CardsInHand
						.get(eCardNo.FourthCard.getCardNo()).getSuit()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getSuit() == CardsInHand
						.get(eCardNo.FifthCard.getCardNo()).getSuit()) {
			Flush = true;
		} else {
			Flush = false;
		}

		// Straight Evaluation
		if (Ace) {
			// Looks for Ace, King, Queen, Jack, 10
			if (CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank() == eRank.KING
					&& CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == eRank.QUEEN
					&& CardsInHand.get(eCardNo.FourthCard.getCardNo())
							.getRank() == eRank.JACK
					&& CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.TEN) {
				Straight = true;
				// Looks for Ace, 2, 3, 4, 5
			} else if (CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.TWO
					&& CardsInHand.get(eCardNo.FourthCard.getCardNo())
							.getRank() == eRank.THREE
					&& CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == eRank.FOUR
					&& CardsInHand.get(eCardNo.SecondCard.getCardNo())
							.getRank() == eRank.FIVE) {
				Straight = true;
			} else {
				Straight = false;
			}
			// Looks for straight without Ace
		} else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
				.getRank() == CardsInHand.get(eCardNo.SecondCard.getCardNo())
				.getRank().getRank() + 1
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
						.getRank() == CardsInHand
						.get(eCardNo.ThirdCard.getCardNo()).getRank().getRank() + 2
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
						.getRank() == CardsInHand
						.get(eCardNo.FourthCard.getCardNo()).getRank()
						.getRank() + 3
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
						.getRank() == CardsInHand
						.get(eCardNo.FifthCard.getCardNo()).getRank().getRank() + 4) {
			Straight = true;
		} else {
			Straight = false;
		}

		// Evaluates the hand type
		
		// Natural royal flush
		if (Straight == true
				&& Flush == true
				&& CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.TEN
				&& Ace
				&& Joker == false) {
			ScoreHand(eHandStrength.NaturalRoyalFlush, 0, 0, 0,0);
		}

		// Straight Flush
		else if (Straight == true && Flush == true) {
			ScoreHand(eHandStrength.StraightFlush,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0, 0,0);
			if (Joker== true){
				ScoreHand(eHandStrength.StraightFlush,
						CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
								.getRank(), 0, 0,1);
			}
		}
		// Five of a Kind
		else if (Joker == true && (CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank()== 
				CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank() || 
				CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()== eRank.JOKER) && 
				(CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank()== 
				CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() || 
				CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()== eRank.JOKER) &&
				(CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank()== 
				CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank() || 
				CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank()== eRank.JOKER)){
			ScoreHand(eHandStrength.FiveOfAKind,
					CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank()
					.getRank(),0,0, 1);
		}
		//Non royal flush
		else if (joker_count == 1 && (CardsInHand.get(eCardNo.SecondCard.getCardNo()).getSuit() == 
				CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getSuit()
				&& CardsInHand.get(eCardNo.SecondCard.getCardNo()).getSuit() == 
				CardsInHand.get(eCardNo.FourthCard.getCardNo()).getSuit()
				&& CardsInHand.get(eCardNo.SecondCard.getCardNo()).getSuit() == 
				CardsInHand.get(eCardNo.FifthCard.getCardNo()).getSuit()) && 
				(CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.TEN || 
				CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.JACK) &&
				(CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank() !=  
				CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()) &&
				(CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() !=  
				CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()) &&
				(CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank() !=  
				CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank())
				){
			ScoreHand(eHandStrength.RoyalFlush, 0,0,0,1);
		}
		else if (joker_count ==2 && (CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getSuit() == 
				CardsInHand.get(eCardNo.FourthCard.getCardNo()).getSuit()
				&& CardsInHand.get(eCardNo.FourthCard.getCardNo()).getSuit() == 
				CardsInHand.get(eCardNo.FifthCard.getCardNo()).getSuit()) &&
				(CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.TEN || 
				CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.JACK ||
				CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.QUEEN)
				&& (CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() !=  
				CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()) &&
				(CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank() !=  
				CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank()) ){
			ScoreHand(eHandStrength.RoyalFlush, 0,0,0, 1);
		}
		else if (joker_count == 3 && (CardsInHand.get(eCardNo.FourthCard.getCardNo()).getSuit() == 
				CardsInHand.get(eCardNo.FifthCard.getCardNo()).getSuit()
				)&&(CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.TEN || 
				CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.JACK ||
				CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.QUEEN
				||CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.KING)&&
				(CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank() !=  
				CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank())){
			ScoreHand(eHandStrength.RoyalFlush, 0,0,0, 1);
		}
		else if (joker_count == 4 && (CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.TEN || 
				CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.JACK ||
				CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.QUEEN
				||CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.KING
				|| CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.ACE)){
			ScoreHand(eHandStrength.RoyalFlush, 0,0,0, 1);
			
		}
		
		// Four of a Kind

		else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.ThirdCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FourthCard.getCardNo()).getRank()) {
			ScoreHand(eHandStrength.FourOfAKind,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0,
					CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank()
							.getRank(), 0);
			if (Joker== true){
				ScoreHand(eHandStrength.FourOfAKind,
						CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
								.getRank(), 0,
						CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank()
								.getRank(), 1);
			}
		}

		else if (CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.ThirdCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FourthCard.getCardNo()).getRank()) {
			ScoreHand(eHandStrength.FourOfAKind,
					CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank()
							.getRank(), 0,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0);
			if (Joker== true){
				ScoreHand(eHandStrength.FourOfAKind,
						CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank()
								.getRank(), 0,
						CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
								.getRank(), 1);
			}
		}

		// Full House
		else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.ThirdCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FifthCard.getCardNo()).getRank()) {
			ScoreHand(eHandStrength.FullHouse,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()
							.getRank(), 0, 0);
			if (Joker== true){
				ScoreHand(eHandStrength.FullHouse,
						CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
								.getRank(),
						CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()
								.getRank(), 0, 1);
			}
		}

		else if (CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.FifthCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.SecondCard.getCardNo()).getRank()) {
			ScoreHand(eHandStrength.FullHouse,
					CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0, 0);
			if (Joker== true){
				ScoreHand(eHandStrength.FullHouse,
						CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
								.getRank(),
						CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
								.getRank(), 0, 1);
			}
		}

		// Flush
		else if (Flush) {
			ScoreHand(eHandStrength.Flush,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0, 0, 0);
			if (Joker== true){
				ScoreHand(eHandStrength.Flush,
						CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
								.getRank(), 0, 0, 1);
			}
		}

		// Straight
		else if (Straight) {
			ScoreHand(eHandStrength.Straight,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0, 0, 0);
			if (Joker== true){
				ScoreHand(eHandStrength.Straight,
						CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
								.getRank(), 0, 0, 1);
			}
		}

		// Three of a Kind
		else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.ThirdCard.getCardNo()).getRank()) {
			ScoreHand(eHandStrength.ThreeOfAKind,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0,
					CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()
							.getRank(), 0);
			if (Joker==true){
				ScoreHand(eHandStrength.ThreeOfAKind,
						CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
								.getRank(), 0,
						CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()
								.getRank(), 1);
			}
		}

		else if (CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.FourthCard.getCardNo()).getRank()) {
			ScoreHand(eHandStrength.ThreeOfAKind,
					CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank()
							.getRank(), 0,
					CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank()
							.getRank(), 0);
			if (Joker==true){
				ScoreHand(eHandStrength.ThreeOfAKind,
						CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank()
								.getRank(), 0,
						CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank()
								.getRank(), 1);
			}
			
		} else if (CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.FifthCard.getCardNo()).getRank()) {
			ScoreHand(eHandStrength.ThreeOfAKind,
					CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
							.getRank(), 0,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0);
			if (Joker==true){
				ScoreHand(eHandStrength.ThreeOfAKind,
						CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
								.getRank(), 0,
						CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
								.getRank(), 1);
			}
		}

		// Two Pair
		else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getRank()
				&& (CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FourthCard.getCardNo()).getRank())) {
			ScoreHand(eHandStrength.TwoPair,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank()
							.getRank(), 0);
			if (Joker== true){
				ScoreHand(eHandStrength.TwoPair,
						CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
								.getRank(),
						CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
								.getRank(),
						CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank()
								.getRank(), 1);
			}
		} else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getRank()
				&& (CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FifthCard.getCardNo()).getRank())) {
			ScoreHand(eHandStrength.TwoPair,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
							.getRank(), 0);
			if (Joker== true){
				ScoreHand(eHandStrength.TwoPair,
						CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
								.getRank(),
						CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()
								.getRank(),
						CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
								.getRank(), 1);
			}
		} else if (CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.ThirdCard.getCardNo()).getRank()
				&& (CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FifthCard.getCardNo()).getRank())) {
			ScoreHand(eHandStrength.TwoPair,
					CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0);
			if (Joker == true){
				ScoreHand(eHandStrength.TwoPair,
						CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank()
								.getRank(),
						CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()
								.getRank(),
						CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
								.getRank(), 1);
			}
		}

		// Pair
		else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getRank()) {
			ScoreHand(eHandStrength.Pair,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0,
					CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
							.getRank(), 0);
			if (Joker==true){
				ScoreHand(eHandStrength.Pair,
						CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
								.getRank(), 0,
						CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
								.getRank(), 1);
			}
		} else if (CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.ThirdCard.getCardNo()).getRank()) {
			ScoreHand(eHandStrength.Pair,
					CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank()
							.getRank(), 0,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0);
			if (Joker==true){
				ScoreHand(eHandStrength.Pair,
						CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank()
								.getRank(), 0,
						CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
								.getRank(), 1);
			}
		} else if (CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.FourthCard.getCardNo()).getRank()) {
			ScoreHand(eHandStrength.Pair,
					CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
							.getRank(), 0,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0);
			if (Joker==true){
				ScoreHand(eHandStrength.Pair,
						CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
								.getRank(), 0,
						CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
								.getRank(), 1);
			}
		} else if (CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.FifthCard.getCardNo()).getRank()) {
			ScoreHand(eHandStrength.Pair,
					CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()
							.getRank(), 0,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0);
			if (Joker== true){
				ScoreHand(eHandStrength.Pair,
						CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()
								.getRank(), 0,
						CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
								.getRank(), 1);
			}
		}

		else {
			ScoreHand(eHandStrength.HighCard,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0,
					CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank()
							.getRank(), 0);
		}
	}
	

	private void ScoreHand(eHandStrength hST, int HiHand, int LoHand, int Kicker, int Natural) {
		this.HandStrength = hST.getHandStrength();
		this.HiHand = HiHand;
		this.LoHand = LoHand;
		this.Kicker = Kicker;
		this.Natural= Natural;
		this.bScored = true;

	}

	/**
	 * Custom sort to figure the best hand in an array of hands
	 */
	public static Comparator<Hand> HandRank = new Comparator<Hand>() {

		public int compare(Hand h1, Hand h2) {

			int result = 0;

			result = h2.HandStrength - h1.HandStrength;

			if (result != 0) {
				return result;
			}

			result = h2.HiHand - h1.HiHand;
			if (result != 0) {
				return result;
			}
			
			result = h2.Natural;
			if (result == 0){
				return result;
			}

			result = h2.LoHand = h1.LoHand;
			if (result != 0) {
				return result;
			}

			result = h2.Kicker = h1.Kicker;
			if (result != 0) {
				return result;
			}

			return 0;
		}
	};
}
