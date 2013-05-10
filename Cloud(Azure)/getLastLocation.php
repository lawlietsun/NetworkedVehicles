<?php
include 'databasecon.php';

$lastLocQuery = "CALL getLastLocation(".$_POST["idVehicle"].");";

$lastLoc = mysqli_query($con,$lastLocQuery);

echo "<?xml version=\"1.0\" encoding=\"utf-8\"?>";


$location = mysqli_fetch_assoc($lastLoc);
  echo "<location>";
  echo "<idLocation>" . $location['idLocation'] . "</idLocation>";
  echo "<latitude>" . $location['latitude'] . "</latitude>";
  echo "<longitude>" . $location['longitude'] . "</longitude>";
  echo "<time>" . $location['time'] . "</time>";
  echo "</location>";

mysqli_close($con);
?>


