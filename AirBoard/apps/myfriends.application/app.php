<!DOCTYPE html>
<html> 
<head> 
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <link rel="stylesheet" type="text/css" href="style.css"></link>
  <title>My friends</title> 
  <script src="http://maps.google.com/maps/api/js?sensor=false" 
          type="text/javascript"></script>
          	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js">
	</script>
</head> 
<body><div class="title"><b><p id="test">My Friends<p></b></div>
<ul>
	
  <li class="googlemap"><div id="map" style="width: inherit; height: inherit;"></div></li></ul>

  <script type="text/javascript">
    $(document).ready(function(){
    var i;
    i=0;
    var locations= new Array();
    
    var location=new Array();
    $.ajax({type: "POST",
    url: "getmyfriends.php",
	 data: {idVehicle: "11"},
	 dataType: "xml",
	 success: function(xml)
	 {
	 i=0;
		 $(xml).find('friend').each(function()
		 {
			locations[i]=[$(this).find('owner').text(),$(this).find('latitude').text(),$(this).find('longitude').text(),$(this).find('time').text()];
			i++;
		 });
    var map = new google.maps.Map(document.getElementById('map'), {
      zoom: 13,
      scrollwheel: true,
      zoomControl: true,
      panControl: false,
      scaleControl: false,
      streetViewControl: false,
      center: new google.maps.LatLng(51.5, -0.11),
      mapTypeId: google.maps.MapTypeId.HYBRID
    });
    
    
    var infowindow = new google.maps.InfoWindow();

    var marker;

    for (i = 0; i < locations.length; i++) {  
      marker = new google.maps.Marker({
        position: new google.maps.LatLng(locations[i][1], locations[i][2]),
        map: map
      });
      
      google.maps.event.addListener(marker, 'click', (function(marker, i) {
        return function() {
          infowindow.setContent('<b><img src="atos.png" style="width: 69px;height: 30px"></img><p style="color:#8b0f0f;font-size: x-large"">'+locations[i][0]+'</p>'+locations[i][3]+'</b>');
          infowindow.open(map, marker);
        }
      })(marker, i));
    }
	 
	 }  
	    
    });
    
    
    
    });
  </script>
</body>
</html>