<!DOCTYPE html>
<html> 
<head> 
  <title>My friends</title> 
  <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js">
	</script>
</head>

	<script>
	
	$(document).ready(function(){ 
	
	$("#open").click(function(){
		
		window.top.location.href="app.php";
	});
	});
	</script>
<body style="width:360px;height:360px;position:fixed;background:black;">
<a id="open"><img src="myfriendslogo.png"></img></a>
</body>
</html>