<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8" />
<title>Glass :: Dashboard</title>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.2/jquery-ui.js"></script>
<link rel="stylesheet" type="text/css" href="style.css" />
<style>

</style>
<script>
$(function() {
$( "#sortable" ).sortable();
$( "#sortable" ).disableSelection();
});
</script>
</head>
<body>

<div id="wrapper">
	<div id="apps">
	<ul id="sortable">
		<?php 
		$manifests = glob("*/manifest.xml");
		
		foreach (glob("apps/*/manifest.xml") as $app) {
    		$xml = simplexml_load_file($app);
    		if (!isset($xml->xSize) || !isset($xml->ySize)){
    			echo "xml file for ". $xml->name . " is malformed";
    			break;
    		}
    		echo '<li class="app-block xsize' . $xml->xSize . ' ysize' . $xml->ySize . '"><iframe width="100%" height="100%" frameborder=0 src="';
    		$indexfile = rtrim($app, "manifest.xml") . "index.php";
    		echo $indexfile . '"></iframe></li>';
		}
		?>
	</ul>
	</div>
	<div class="clear"></div>
</div>
<div>
<iframe width="100%" height="100%" frameborder="0" src="apps/signs.application/index.php"></iframe>
</div>
</body>
</html>

<script>
window.requestFileSystem  = window.requestFileSystem || window.webkitRequestFileSystem;
 
window.requestFileSystem(window.TEMPORARY, 5*1024*1024, initFS, errorHandler);
 
function initFS(){
  // alert("Welcome to Filesystem! It's showtime :)"); // Just to check if everything is OK :)
  // place the functions you will learn bellow here
}
 
function errorHandler(){
  console.log('An error occured');
}
</script>