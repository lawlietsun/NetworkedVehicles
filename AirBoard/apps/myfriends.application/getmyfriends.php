<?php
$con = mysqli_connect("eu-cdbr-azure-west-a.cloudapp.net","b471ba38db07c5","11896dab","networkedV","3306");
if (!$con)
  die('Could not connect: ' . mysql_error());
 
$friendsQuery = "CALL getMyFriends(".$_POST["idVehicle"].");";

$friends = mysqli_query($con,$friendsQuery);

echo "<?xml version=\"1.0\" encoding=\"utf-8\"?>";

echo "<friends>";
while($friend = mysqli_fetch_assoc($friends))
{
  echo "<friend>";
  echo "<owner>" . $friend['owner'] . "</owner>";
  echo "<latitude>" . $friend['latitude'] . "</latitude>";
  echo "<longitude>" . $friend['longitude'] . "</longitude>";
  echo "<time>" . $friend['time'] . "</time>";
  echo "<id>" . $friend['idVehicle'] . "</id>";
  echo "</friend>";
}
echo "</friends>";

mysqli_close($con);
?>