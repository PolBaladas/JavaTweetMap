/*
 * Main Class as Example: The lines above are a demo on how to use this classes.
 */

package src;

import java.util.List;
import twitter4j.Status;

public class Main {

	static int num; //Number of tweets to retrieve
	
	public static void main(String[] args) {
		
		List<Status> tweets = null;
		TweetGetter tweetGetter = new TweetGetter();		//Search Parameters
		tweetGetter.params = "Google";						//Keyword / Hashtag
		tweetGetter.num = 100;								//Number of tweets retrieved (historical)
		tweetGetter.since = "2013-12-01";					//Since time (Format is important!!)
		UIClass ui = new UIClass(tweetGetter);				//Call UI Class (executes the getter too).
		
	}

}
