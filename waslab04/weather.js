
function showCities () {
  
  
   var country = document.getElementById("countryName").value;

   // Replace the two lines below with your implementation
   //var mock_text = "You should show a list of clickable cities of " + country;
   //document.getElementById("left").innerHTML = mock_text;
   var uri = "http://localhost:8080/waslab04/city_names.php";
   var req = new XMLHttpRequest();
   req.open('GET', uri + "?country=" + country, /*async*/true);

   req.onreadystatechange = function() {
		if (req.readyState == 4 && req.status == 200) {
		  document.getElementById("left").innerHTML = "";
		  var ciudades = JSON.parse(req.responseText);
		  //console.log(ciudades.Table);
		  for(var i = 0; i < ciudades.Table.length; ++i) {
		    var ciudad = ciudades.Table[i].City;
		    var pais = ciudades.Table[i].Country;
		    var texto = "";
		    texto += '<p><a href="#" onClick= ';
		    texto += ' showWeather("'+ ciudad.replace(/ /g, "_") +'","'+ pais +'")> ';
		    texto += ciudades.Table[i].City;
		    texto += " </a></p>";
		    document.getElementById("left").innerHTML += texto;
		  }
		  
		}
    };
   req.send(/*no params*/null);
};


function showWeather (ciudad, pais) {


  //console.log("Entro");
  //console.log(ciudad);
  //console.log(pais);
  var uri = "http://localhost:8080/waslab04/city_info.php";
  var req = new XMLHttpRequest();
  req.open('GET', uri + "?country=" + pais + "&city=" + ciudad, true);
  req.onreadystatechange = function() {
		if (req.readyState == 4 && req.status == 200) {
		  document.getElementById("right").innerHTML = "";
		  var info = JSON.parse(req.responseText);
		  var HTML = "<table border=0>";
		  HTML +=  "<tr><td>" + "Location:" + "</td><td>" +info.Location+"</tr>";
		  HTML +=  "<tr><td>" + "Time:" + "</td><td>" +info.Time+"</tr>";
		  HTML +=  "<tr><td>" + "Wind:" + "</td><td>" +info.Wind+"</tr>";
		  HTML +=  "<tr><td>" + "Visibility:" + "</td><td>" +info.Visibility+"</tr>";
		  HTML +=  "<tr><td>" + "SkyConditions:" + "</td><td>" +info.SkyConditions+"</tr>";
		  HTML +=  "<tr><td>" + "Temperature:" + "</td><td>" +info.Temperature+"</tr>";
		  HTML +=  "<tr><td>" + "DewPoint:" + "</td><td>" +info.DewPoint+"</tr>";
		  HTML +=  "<tr><td>" + "RelativeHumidity:" + "</td><td>" +info.RelativeHumidity+"</tr>";
		  HTML +=  "<tr><td>" + "Pressure:" + "</td><td>" +info.Pressure+"</tr>";
		  HTML +=  "<tr><td>" + "Status:" + "</td><td>" +info.Status+"</tr>";
		  HTML += "</table>";
		  document.getElementById("right").innerHTML = HTML;  
		}
    };
  req.send(null);
  
   
}

window.onload = showCities();
