<?php
 
ini_set("soap.wsdl_cache_enabled","0");
$server = new SoapServer("http://localhost:8080/waslab04/WSLabService.wsdl");

function FahrenheitToCelsius($fdegree){
    $cresult = ($fdegree - 32) * (5/9);
    return array("cresult"=> $cresult, "timeStamp"=> date('c', time()) );
}

function CurrencyConverter($from_Currency,$to_Currency,$amount) {
	$uri = "http://currencies.apps.grandtrunk.net/getlatest/$from_Currency/$to_Currency";
	$rate = doubleval(file_get_contents($uri));
	return round($amount * $rate, 2);
};

// Task #4: Implement here the CurrencyConverterPlus function and add it to $server

function CurrencyConverterPlus($objeto) {
	$from_Currency = $objeto->from_Currency;
	$to_Currencies = $objeto->to_Currencies;
	$amount = $objeto->amount;
	  
	$object_list = array();
	  
	  for($i = 0; $i < count($to_Currencies); ++$i){
	    $object = new stdClass();
	    $valor = $to_Currencies[$i];
	    $object->currency = $valor;
	    $object->amount = CurrencyConverter($from_Currency, $valor, $amount);
	    $object_list[$i] = $object;
	  }
	
	return $object_list;
};

 $server->addFunction("CurrencyConverterPlus");

$server->addFunction("FahrenheitToCelsius");

// Task #3 -> Uncomment the following line:
 $server->addFunction("CurrencyConverter");

$server->handle();
 
?>
