package src;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;

import org.openstreetmap.gui.jmapviewer.JMapViewer;

import twitter4j.Status;

public class UIClass extends JFrame{
	
	JSplitPane panel;
	TweetGetter tweetGetter;
	JMapViewer map = new JMapViewer();
	JTextPane twitterStream = new JTextPane();
	JScrollPane streamContainer = new JScrollPane(twitterStream);
	
	
	public UIClass(TweetGetter getter){
		super("TweetMap");
		setSize(1800,900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tweetGetter = getter;
		setMap();
		setStream();
		
		try {
			StreamGetter stream = new StreamGetter(map, twitterStream, tweetGetter.params);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, map, streamContainer);
		panel.setResizeWeight(0.5);
		setContentPane(panel);
		setVisible(true);
	}	
	
	public void setMap(){
		tweetGetter.populateMap(map);
		map.setMaximumSize(new Dimension(1300,900));
		map.setMinimumSize(new Dimension(900,900));
		map.setSize(new Dimension(1300,900));
	}
	
	public void setStream(){
		streamContainer.setMaximumSize(new Dimension(900,900));
		streamContainer.setSize(new Dimension(900,900));
		tweetGetter.populateTwitterStream(twitterStream);
	}
		
}

