<?php
ini_set("soap.wsdl_cache_enabled","0");

try{

  $sClient = new SoapClient('http://www.webservicex.net/globalweather.asmx?WSDL');

  // Get the necessary parameters from the request
  // Use $sClient to call the operation GetWeather
  // echo the returned info as a JSON object

  $parameters = new stdClass();
  $parameters->CountryName = $_GET['country'];
  $parameters->CityName = $_GET['city'];
  
  $parameters->CityName = str_replace("_"," ", $parameters->CityName);
  $info = $sClient->GetWeather($parameters);
  $arrayStrings = str_replace('<?xml version="1.0" encoding="utf-16"?>', '', $info->GetWeatherResult);
  $arrayStrings = new SimpleXMLElement( $arrayStrings);
  echo json_encode($arrayStrings);

  //header(':', true, 501); // Just remove this line to return the successful 
                          // HTTP-response status code 200.
  //echo json_encode(array('Result' => 'Not implemented'));
  
}
catch(SoapFault $e){
  header(':', true, 500);
  echo json_encode($e);
}
?>
