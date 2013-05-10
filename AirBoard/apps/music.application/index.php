<!DOCTYPE html>
<html>
	<head>
	<link rel="stylesheet" type="text/css" href="style.css"></link>
			<title>Music</title>

	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js">
	</script>
	<script>
	
	$(document).ready(function(){
	$.post("playpause.php", { value: "false"} );
	<?php
		if(in_array($_SERVER['REMOTE_ADDR'],array("127.0.0.1","localhost","::1")))
		echo '
	var song=new Audio();
	var file;';?>
	$("#pause").hide();
	var change = function changeimg(id,normal,over){
	
	id.mouseenter(function(){
	id.attr("src",over);}).mouseleave(function(){id.attr("src",normal);
		});
	
	};
		
		
	change($("#back"),"images/back.png","images/backover.png");
	change($("#next"),"images/next.png","images/nextover.png");	
	change($("#play"),"images/play.png","images/playover.png");
	change($("#pause"),"images/pause.png","images/pauseover.png");
	
	var playpause = function switchplaypause(){
		if($("#play").is(':visible'))
		{$("#play").hide();$("#pause").show();
			
			
		}
		else
		{$("#pause").hide();$("#play").show();
			
			
		}
	};
			
	$("#play").click(function(){
	playpause();
	$.post("playpause.php", { value: "true"} );
	<?php
		if(in_array($_SERVER['REMOTE_ADDR'],array("127.0.0.1","localhost","::1")))
		echo 'song.play();';?>
	
	});	
		
		$("#pause").click(function(){
		playpause();
		$.post("playpause.php", { value: "false"} );
		<?php
		if(in_array($_SERVER['REMOTE_ADDR'],array("127.0.0.1","localhost","::1")))
		echo 'song.pause();';?>
		
		});	
	$("#next").click(function(){
		$.post("next.php");
	});
	$("#back").click(function(){
		$.post("back.php");
	});		
	setInterval(function(){ $.ajax({
        type: "GET",
	url: "musicstate.xml",
	dataType: "xml",
	success: function(xml) {
	  
      $("#currentsong").text($(xml).find('artist').text()+" "+$(xml).find('tune').text());
      <?php
		if(in_array($_SERVER['REMOTE_ADDR'],array("127.0.0.1","localhost","::1")))
		echo '
      if($(xml).find("file").text()!=file)
      {
      		file = $(xml).find("file").text();
      		song.pause();
      		song = new Audio("songs/"+file);
      		if($(xml).find("play").text()=="true")
      		song.play();
      		else
      		song.pause();
      }
      else
      {
      if($(xml).find("play").text()=="true")
      		song.play();
      		else
      		song.pause();
      }
      ';?>
      if(($(xml).find('play').text()=='false' && !$("#play").is(':visible'))||($(xml).find('play').text()=='true' && $("#play").is(':visible')))
      playpause();
      }
});},200);
	
});
		
	</script>

</head>
<body>
		
	<div class="controller">
	<ul>
	<li><img id="back" src="images/back.png"></img></li>
	<li>
	<img id="play" src="images/play.png"></img>
	<img id="pause" src="images/pause.png"></img>
	</li>
	<li><img id="next" src="images/next.png"></img></li>
	</ul>
	</div>
	<div>
	<b><p class="song" id="currentsong"></p></b>
	</div>
	</body>
</html>
