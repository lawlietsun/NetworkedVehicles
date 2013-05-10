<?php
$location= file_get_contents("http://localhost/currentlocation.xml");
$json = file_get_contents("http://api.openweathermap.org/data/2.5/weather?lat=".$location['latitude']."&long=".$location['longitude']."&units=metric");
$data = json_decode($json);
echo $data;
$weather = $data->{'weather'};

$sky = $weather->{'main'};

?>