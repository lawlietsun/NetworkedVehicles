<?php
include 'databasecon.php';
$cabsQuery = "CALL getCabs();";

$cabs = mysqli_query($con,$friendsQuery);

echo "<?xml version=\"1.0\" encoding=\"utf-8\"?>";

echo "<cabs>";
while($cab = mysqli_fetch_assoc($friends))
{
  echo "<cabs>";
  echo "<cabID>" . $cab['field1'] . "</cabID>";
  echo "<latitude>" . $cab['latitude'] . "</latitude>";
  echo "<longitude>" . $cab['longitude'] . "</longitude>";
  echo "</cab>";
}
echo "</cabs>";

mysqli_close($con);
?>