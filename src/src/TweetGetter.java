package src;

import java.net.URI;
import java.util.List;

import javax.swing.JTextPane;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

public class TweetGetter {
	String params = "";
	int num = 100;
	String since ="2013-12-01";
	public List<Status> getTweets(){
		List<Status> tweets = null;
		try{
			Twitter twitter = TwitterFactory.getSingleton();
			Query query = new Query(params);
			query.setCount(num);//Max number of tweets
			query.setSince(since);
			QueryResult result = twitter.search(query);
			tweets = result.getTweets();
		}catch(Exception e){e.printStackTrace();}
		return tweets;
	}
	
	
	public void populateTwitterStream(JTextPane panel){
		
		List<Status> tweets = getTweets();
		
		String text = "";
		for(Status tweet:tweets){
			text = text+"\n"
					+tweet.getUser().getScreenName()+" : "
					+tweet.getText()+"\n";
		}
		panel.setText(text);
	}
	
	public void populateMap(JMapViewer map){
		
		List<Status> tweets = getTweets();
		
		for(Status tweet : tweets){
			MapMarkerDot marker = null;
			if(tweet.getGeoLocation()!=null){
				double lat = tweet.getGeoLocation().getLatitude();
				double lon = tweet.getGeoLocation().getLongitude();
				if(lat!=0.0 && lon!=0.0){
					marker = new MapMarkerDot(lat,lon);
					marker.setName(tweet.getUser().getScreenName());
					map.addMapMarker(marker);
				}	
			}
			else{
				if(tweet.getUser().getLocation().toString()!=null){
					String location = tweet.getUser().getLocation().toString();
					System.out.println(location);
					String name = tweet.getUser().getScreenName();
					try{
						map.addMapMarker(getLocationMarker(location, name));
					}catch(Exception e){}
				}
			}
		}
	}
	
	public MapMarkerDot getLocationMarker(String location, String userName)throws Exception{
		
		String jsonString = getHttpCoordinates(location);
		
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(jsonString);
		
		JSONArray results = (JSONArray) obj.get("results");
		JSONObject data = (JSONObject) results.get(0);
		JSONObject geometry = (JSONObject)data.get("geometry");
		JSONObject JsonLocation = (JSONObject) geometry.get("location"); 
		
		double lat =  (Double) JsonLocation.get("lat");
		double lon =  (Double) JsonLocation.get("lng");
		MapMarkerDot marker = new MapMarkerDot(lat,lon); 
		marker.setName(userName);
		return marker;
	}
	
	public String getHttpCoordinates(String location)throws Exception{
		String jsonString = "";
		URI url = new URIBuilder()
		.setScheme("http")
		.setHost("maps.googleapis.com")
		.setPath("/maps/api/geocode/json")
		.setParameter("address", location.replaceAll(",","").replaceAll(" ",""))
		.setParameter("sensor", "true")
		.build();
	
		System.out.println(url.toString());
		HttpGet get = new HttpGet(url);			//Assign the URI to the get request
		CloseableHttpClient client = HttpClients.createDefault();	
		CloseableHttpResponse response = client.execute(get);
		jsonString = EntityUtils.toString(response.getEntity());		//Gets a JSON String as a response
		return jsonString;
	}
	
}