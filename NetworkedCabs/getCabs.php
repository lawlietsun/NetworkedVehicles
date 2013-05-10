<?php
include 'databasecon.php';
$cabsQuery = "CALL getCabs();";

$cabs = mysqli_query($con,$cabsQuery);

echo "<?xml version=\"1.0\" encoding=\"utf-8\"?>";

echo "<cabs>";
while($cab = mysqli_fetch_assoc($cabs))
{
  echo "<cab>";
  echo "<cabid>" . $cab['field1'] . "</cabid>";
  echo "<latitude>" . $cab['latitude'] . "</latitude>";
  echo "<longitude>" . $cab['longitude'] . "</longitude>";
  echo "</cab>";
}
echo "</cabs>";

mysqli_close($con);
?>