<?php
$state = simplexml_load_file("musicstate.xml");
$state->play = $_POST['value'];
$state->asXML('musicstate.xml');
?>