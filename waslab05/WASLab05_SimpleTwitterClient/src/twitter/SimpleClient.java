package twitter;

import java.util.Date;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;

public class SimpleClient {

	public static void main(String[] args) throws Exception {
		
		//final Twitter twitter = new TwitterFactory().getInstance();
		/*
		User u = twitter.showUser("fib_was");
		String text = u.getStatus().getText();
		System.out.println(text);
		//System.out.println(u.getStatus().getId());
		twitter.retweetStatus(u.getStatus().getId());
		Date now = new Date();
		String latestStatus = "I want to increase the Klout score of @cfarre [task #4 completed "+now+"]";
		Status status = twitter.updateStatus(latestStatus);
		System.out.println("Successfully updated the status to: " + status.getText());  
		*/
		
	    StatusListener listener = new StatusListener() {
	        @Override
	        public void onStatus(Status status) {
	            System.out.println(status.getUser().getName() + " (@" + status.getUser().getScreenName() + "): " + status.getText() );
	        }

			@Override
			public void onException(Exception arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTrackLimitationNotice(int arg0) {
				// TODO Auto-generated method stub
				
			}
	     };
	     
		TwitterStreamFactory tsf = new TwitterStreamFactory() ;
		TwitterStream ts = tsf.getInstance();
		FilterQuery fq = new FilterQuery();
		String keywords[] = {"#barcelona"};
		fq.track(keywords);
		
		ts.addListener(listener);
		ts.filter(fq);
		
	}
}
