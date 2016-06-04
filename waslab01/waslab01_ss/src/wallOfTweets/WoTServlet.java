
package wallOfTweets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Locale;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class WoTServlet extends HttpServlet {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5207702297571272100L;
	Locale currentLocale = new Locale("en");
	String ENCODING = "ISO-8859-1";


	@Override
	public void doGet (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {
			Vector<Tweet> tweets = Database.getTweets();
			if(req.getHeader("Accept").equals("text/plain")){
				printPLAINresult(tweets, req,res);
			}
			else printHTMLresult(tweets, req, res);
		}

		catch (SQLException ex ) {
			throw new ServletException(ex);
		}
	}

	@Override
	public void doPost (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		PrintWriter  out = res.getWriter ( );
		String author = req.getParameter("author");
		String text = req.getParameter("tweet_text");
		String tweetID = req.getParameter("ultimoTweetId");
		Cookie[] cookies = req.getCookies();
		Long id = 0L;
		
		if(tweetID == null){
			
			try {
				id = Database.insertTweet(author, text);
				Cookie c = new Cookie("CookieId" + String.valueOf(id), sha256( String.valueOf(id)));
				res.addCookie(c);
			} catch (SQLException e) {
				throw new ServletException(e);
			}
		}else{
			if(cookies != null){
				for(Cookie c : cookies){		
					if(c.getValue().equals(sha256(tweetID))){
						Database.deleteTweet(Long.parseLong(tweetID));
					}
				}
			}
			
		}
		
		
		if(req.getHeader("Accept").equals("text/plain")) {
			out.print(String.valueOf(id));
		}
		else res.sendRedirect("wot");

	}
	
	private void printHTMLresult (Vector<Tweet> tweets, HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.FULL, currentLocale);
		DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.DEFAULT, currentLocale);
		String currentDate = dateFormatter.format(new java.util.Date());
		res.setContentType ("text/html");
		res.setCharacterEncoding(ENCODING);
		PrintWriter  out = res.getWriter ( );
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<html>");
		out.println("<head><title>Wall of Tweets</title>");
		out.println("<link href=\"wallstyle.css\" rel=\"stylesheet\" type=\"text/css\" />");
		out.println("</head>");
		out.println("<body class=\"wallbody\">");
		out.println("<h1>Wall of Tweets</h1>");
		out.println("<div class=\"walltweet\">"); 
		out.println("<form action=\"wot\" method=\"post\">");
		out.println("<table border=0 cellpadding=2>");
		out.println("<tr><td>Your name:</td><td><input name=\"author\" type=\"text\" size=70></td><td/></tr>");
		out.println("<tr><td>Your tweet:</td><td><textarea name=\"tweet_text\" rows=\"2\" cols=\"70\" wrap></textarea></td>"); 
		out.println("<td><input type=\"submit\" name=\"action\" value=\"Tweet!\"></td></tr>"); 
		out.println("</table></form></div>"); 
		for (Tweet tweet: tweets) {
			String messDate = dateFormatter.format(tweet.getDate());
			if (!currentDate.equals(messDate)) {
				out.println("<br><h3>...... " + messDate + "</h3>");
				currentDate = messDate;
			}
			out.println("<div class=\"wallitem\">");
			out.println("<h4><em>" + tweet.getAuthor() + "</em> @ "+ timeFormatter.format(tweet.getDate()) +"</h4>");
			out.println("<p>" + tweet.getText() + "</p>");
			
			out.println("<form action=\"wot\" method=\"post\">");
			out.println("<table border=0 cellpadding=2>");
			out.println("<input type=\"submit\" name=\"action\" value=\"Esborrar Tweet\">");
			out.println("<tr><td><input type=\"hidden\" name=\"ultimoTweetId\" value="+tweet.getTwid()+"></td></tr>");
			out.println("</table></form>"); 
			
			out.println("</div>");
			
		}
		out.println ( "</body></html>" );
	}
	
	private void printPLAINresult (Vector<Tweet> tweets, HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.FULL, currentLocale);
		DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.DEFAULT, currentLocale);
		String currentDate = dateFormatter.format(new java.util.Date());
		res.setContentType ("text/plain");
		res.setCharacterEncoding(ENCODING);
		PrintWriter  out = res.getWriter ( );
		
		for (Tweet tweet: tweets) {
			out.print("tweet #" + tweet.getTwid() + ": ");
			String messDate = dateFormatter.format(tweet.getDate());
			if (!currentDate.equals(messDate)) {
				currentDate = messDate;
			}
			
			out.print(tweet.getAuthor() + ": " + tweet.getText() + " [" + tweet.getDate() + "]");
			out.println();
		}
	}
	
	public static String sha256(String base) {
	    try{
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = digest.digest(base.getBytes("UTF-8"));
	        StringBuffer hexString = new StringBuffer();

	        for (int i = 0; i < hash.length; i++) {
	            String hex = Integer.toHexString(0xff & hash[i]);
	            if(hex.length() == 1) hexString.append('0');
	            hexString.append(hex);
	        }

	        return hexString.toString();
	    } catch(Exception ex){
	       throw new RuntimeException(ex);
	    }
	}
}
