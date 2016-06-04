<?php
require_once("DbHandler.php");

setlocale(LC_TIME,"en_US");

$dbhandler = new DbHandler();

	$resp = new SimpleXMLElement("<rss></rss>");
	$resp->addAttribute('version', '2.0');
	$resp->addChild('channel');
	$resp->channel->addChild('title', 'Wall of Tweets 2 - RSS Version');
	$resp->channel->addChild('link', 'http://localhost:8080/waslab02/rss.php');
	$resp->channel->addChild('description', 'RSS 2.0 feed that retrieves the tweets posted to the web app "Wall of Tweets 2"');
	$res = $dbhandler->getTweets();
	foreach($res as $tweet) {
	       
		$item = $resp->channel->addChild('item');
		$item->addChild('title', $tweet['text']);
		$item->addChild('link', 'http://localhost:8080/waslab02/wall.php#item_'.$tweet['id']);
		$item->addChild('description', 'This is WoT tweet #'.$tweet['id'].' posted by <em><strong>'.$tweet['author'].'</strong></em>. It has been liked by <strong>'.$tweet['likes'].'</strong> people');
		$item->addChild('pubDate', date(DATE_W3C,$tweet['time']));
	}
	
	header('Content-type: text/xml');
	echo $resp->asXML();
?>
