var baseURI = "http://localhost:8080/waslab03";
var tweetsURI = baseURI+"/tweets";

var req;
var tweetBlock = "	<div id='tweet_{0}' class='wallitem'>\n\
	<div class='likes'>\n\
	<span class='numlikes'>{1}</span><br /> <span\n\
	class='plt'>people like this</span><br /> <br />\n\
	<button onclick='{5}Handler(\"{0}\")'>{5}</button>\n\
	<br />\n\
	</div>\n\
	<div class='item'>\n\
	<h4>\n\
	<em>{2}</em> on {4}\n\
	</h4>\n\
	<p>{3}</p>\n\
	</div>\n\
	</div>\n";

String.prototype.format = function() {
	var args = arguments;
	return this.replace(/{(\d+)}/g, function(match, number) { 
		return typeof args[number] != 'undefined'
			? args[number]
		: match
		;
	});
};

function likeHandler(tweetID) {
	var target = 'tweet_' + tweetID;
	var uri = tweetsURI+ "/" + tweetID +"/likes";
	// e.g. to like tweet #6 we call http://localhost:8080/waslab03/tweets/6/like
	
	req = new XMLHttpRequest();
	req.open('POST', uri, /*async*/true);
	req.onreadystatechange = function() {
		if (req.readyState == 4 && req.status == 200) {
			document.getElementById(target).getElementsByClassName("numlikes")[0].innerHTML = req.responseText;
		}
	};
	req.send(/*no params*/null);
}

function deleteHandler(tweetID) {


	req = new XMLHttpRequest();
	var token = localStorage.getItem("token" + tweetID);

	console.log(tweetID);
	console.log(token);
	req.open('DELETE', tweetsURI+ "/" + tweetID+ "?token=" + token, /*async*/true);
	req.onreadystatechange = function() {
		if (req.readyState == 4 && req.status == 200) {
			var d = document.getElementById("tweet_"+tweetID);
			d.parentNode.removeChild(d);
			localStorage.removeItem("token" + tweetID);
			//document.getElementById(target).getElementsByClassName("numlikes")[0].innerHTML = req.responseText;
		}
	};
	req.send(/*no params*/null);
}

function getTweetHTML(tweet, action) {  // action :== "like" xor "delete"
	var dat = new Date(tweet.date);
	var dd = dat.toDateString()+" @ "+dat.toLocaleTimeString();
	return tweetBlock.format(tweet.id, tweet.likes, tweet.author, tweet.text, dd, action);
	
}

function getTweets() {
	
	req = new XMLHttpRequest(); 
	req.open("GET", tweetsURI, true); 
	req.onreadystatechange = function() {
		if (req.readyState == 4 && req.status == 200) {
			var tweet_list = req.responseText;
			var array = JSON.parse(tweet_list);
			var aux ="";
			for(var i = 0; i < array.length; i++){
				var tt = array[i];
				if(localStorage.getItem("id"+tt.id) != null && tt.id == localStorage.getItem("id"+tt.id)){
					aux += getTweetHTML(tt, "delete");
				} else{
					aux += getTweetHTML(tt, "like");
				}
			}
			document.getElementById("tweet_list").innerHTML = aux;
		};
	};
	req.send(null); 
};


function tweetHandler() {
	var author = document.getElementById("tweet_author").value;
	var text = document.getElementById("tweet_text").value;
	req = new XMLHttpRequest();
	req.open('POST', tweetsURI, /*async*/true);
	req.onreadystatechange = function() {
		if (req.readyState == 4 && req.status == 200) {
			var nt = JSON.parse(req.responseText);
			var token = nt['token'];
			var id = nt['id'];
			localStorage.setItem("token" + id, token);
			localStorage.setItem("id" + id, id);
			var aux = getTweetHTML(nt, "delete");
			var tl = document.getElementById("tweet_list").innerHTML;
			document.getElementById("tweet_list").innerHTML = aux + document.getElementById("tweet_list").innerHTML;
			//document.getElementById(target).getElementsByClassName("numlikes")[0].innerHTML = aux;
		}
	};
	req.setRequestHeader("Content-Type","application/json");
	req.send(JSON.stringify({ tweet_author: author, tweet_text: text}));
	
	//var mes1 = "Someone ({0}) wants to insert a new tweet ('{1}'),\n but this feature is not implemented yet!";
	//alert(mes1.format(author, text));
	
	// clear form fields
	document.getElementById("tweet_author").value = "";
	document.getElementById("tweet_text").value = "";

};

//main
function main() {
	document.getElementById("tweet_submit").onclick = tweetHandler;
	getTweets();
};
