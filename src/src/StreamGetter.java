package src;

import java.util.List;

import javax.swing.JTextPane;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class StreamGetter{
	
	public StreamGetter(JMapViewer param_map, JTextPane param_panel)  throws Exception{
		
		final JMapViewer map = param_map;
		final JTextPane panel = param_panel;
		
		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		
		StatusListener listener = new StatusListener(){

			@Override
			public void onStatus(Status tweet) {
				addOnStream(panel, tweet);
				try{
					addOnMap(map, tweet);
				}catch(Exception e){}
			}
			
			@Override
			public void onException(Exception arg0) {}

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {}

			@Override
			public void onScrubGeo(long arg0, long arg1) {}

			@Override
			public void onStallWarning(StallWarning arg0) {}

			@Override
			public void onTrackLimitationNotice(int arg0) {}


		};
		
		FilterQuery query = new FilterQuery();
		String[] keys = {"google code in"};
		query.track(keys);
		
		twitterStream.addListener(listener);
		twitterStream.filter(query);
		
	}
	
	public void addOnMap(JMapViewer map, Status tweet) throws Exception{
		TweetGetter tweetGetter = new TweetGetter();
		String location = tweet.getUser().getLocation().toString();
		String userName = tweet.getUser().getScreenName().toString();
		MapMarkerDot marker = tweetGetter.getLocationMarker(location, userName);
		map.addMapMarker(marker);
		
	}
	
	public void addOnStream(JTextPane panel, Status tweet){
		String text = panel.getText();
		String tweetxt = "\n"
				+tweet.getUser().getScreenName()+" : "
				+tweet.getText()+"\n";
		
		text = tweetxt+text;
		panel.setText(text);
	}
}
