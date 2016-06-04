<?php

$URI = 'http://localhost:8080/waslab02/wall.php';
$resp = file_get_contents($URI);
echo $http_response_header[0], "\n"; // Print the first HTTP response header
//echo $resp;  // Print HTTP response body

//tasca 3

$alltweets = new SimpleXMLElement($resp);
foreach ($alltweets->tweet as $tweet){
  echo '[tweet #', $tweet['id'],'] ' ,$tweet->author , ': ', $tweet->text ,' [', $tweet->time,']', "\n";
}

//tasca 4
$postdata = new SimpleXMLElement("<tweet></tweet>");
$postdata->addChild('author', 'Test Author');
$postdata->addChild('text', 'Test Text');


$opts = array('http' =>
    array(
        'method'  => 'PUT',
        'header'  => 'Content-type: text/xml',  
        'content' => $postdata->asXML()
    )
);

$context = stream_context_create($opts);
$result = file_get_contents($URI, false, $context);

echo $result;


//tasca 5

$opts = array('http' =>
    array(
        'method'  => 'DELETE',
        'header'  => 'Content-type: text/xml',  
    )
);

$context = stream_context_create($opts);
$result = file_get_contents('http://localhost:8080/waslab02/wall.php?twid=16', false, $context);

echo $result;
?>