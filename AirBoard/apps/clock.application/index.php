<?php 
error_reporting(E_ERROR);
try{$location= file_get_contents("../../currentlocation.xml");
$json = file_get_contents("http://api.openweathermap.org/data/2.5/weather?lat=".$location['latitude']."&long=".$location['longitude']."&units=metric");
$data = json_decode($json,true);
$weather = $data['weather'][0];
$sky = $weather['main'];
$description = $weather['description'];
$city = $data['name'];
$temp = $data['main']['temp'];
}catch(exception $e)
{}
if(!$json)
{$sky='clouds';
$city='Not Available';
}
echo '<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Clock</title>
	<link rel="stylesheet" type="text/css" href="styles.css">
	
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
	
<script type="text/javascript">
function startTime()
{
var today=new Date();
var h=today.getHours();
var m=today.getMinutes();
var s=today.getSeconds();
var d=today.getDate();
var month=new Array();
month[0]="Jan";
month[1]="Feb";
month[2]="Mar";
month[3]="Apr";
month[4]="May";
month[5]="Jun";
month[6]="Jul";
month[7]="Aug";
month[8]="Sep";
month[9]="Oct";
month[10]="Nov";
month[11]="Dec";
var mo=month[today.getMonth()];
// add a zero in front of numbers<10
m=checkTime(m);
s=checkTime(s);
document.getElementById("hours").innerHTML=h+"";
document.getElementById("minutes").innerHTML=m+"";
document.getElementById("day").innerHTML=d+"";
switch (month){
	case 1:
}
document.getElementById("month").innerHTML=mo+"";
t=setTimeout(function(){startTime()},500);
}

function checkTime(i)
{
if (i<10)
  {
  i="0" + i;
  }
return i;
}
</script>

</head>
<body onload="startTime()">

	<div class="full">
	<div class="half">
	<ul class="clock">
		<li class="time"><span id="hours"></span></li>
		<li class="time"><span id="minutes"></span></li>
		<li class="time"><span id="day"></span></li>
		<li class="time"><span id="month" class="date"></span></li>
	</ul>
	</div>
	<div class="half">
	<div style="background: url(';
	echo 'images/';
	$conditions=array("clear","rain","drizzle","thunderstorm","snow","clouds","extreme");
	if(in_array($sky,$conditions))
	echo $sky;
	else
	echo 'clear';
	echo '.png) no-repeat;height:110px;width:360px;padding-top:250px;">
	<div style="height:110px;">
	<b><p class="temp">';
	echo $city;
	echo " ";
	echo floor($temp);echo '&degC</p></b>
	</div>
	</div>';
	echo '
	</div>
	</div>
	

</body>
</html>';?>