package voteManaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import controller.Controller;
import voteVerification.AnonymizedVote;
/*
 * Authors: Arzum & Burcu
 * 
 * This class is responsible for shuffle the anonymized votes
 */
public class Shuffler implements IShuffling {

	/*instance variables*/
	private ArrayList<AnonymizedVote> avotes;
	private ArrayList<AnonymizedVote> shuffledAVotes;


	//constructor
	public Shuffler(Controller c) {
		this.avotes = c.getDbHelper().getAnonymizedVoteList(c.getPreparedElection().getElectionID());
		shuffledAVotes = new ArrayList<AnonymizedVote>();
		shuffleVotes();
	}
	
	//getters and setters
	public ArrayList<AnonymizedVote> getAvotes() {
		return avotes;
	}

	public void setAvotes(ArrayList<AnonymizedVote> avotes) {
		this.avotes = avotes;
	}

	public ArrayList<AnonymizedVote> getShuffledVotes() {
		return shuffledAVotes;
	}

	public void setShuffledAVotes(ArrayList<AnonymizedVote> shuffledAVotes) {
		this.shuffledAVotes = shuffledAVotes;
	}

	/*
	 * 
	 */
	private ArrayList<AnonymizedVote> shuffleVotes(){
		shuffledAVotes = permuteVotes(avotes);
		return shuffledAVotes;
	}
	

	private ArrayList<AnonymizedVote> permuteVotes(ArrayList<AnonymizedVote> votes){
		
		ArrayList<AnonymizedVote> permutedVotes;
		Random rnd = new Random();

	    /**
	     * Fisher–Yates shuffle .
	     */
	     /*  for (int i = votes.size() - 1; i > 0; i--) {
	            int index = rnd.nextInt(i + 1);
	            AnonymizedVote av = votes.get(index);
	            votes.set(index, votes.get(i));
	            votes.set(i, av);
	        }
	   */
		permutedVotes = votes;
	    Collections.shuffle(permutedVotes);
		return permutedVotes;
	}
	

}
