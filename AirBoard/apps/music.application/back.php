<?php
$state = simplexml_load_file("musicstate.xml");
$state->songid = $state->songid-1;
$songdata =simplexml_load_file("songs/songsdata.xml");
foreach($songdata as $song){

if($song->id==$state->songid.''){
$state->artist=$song->artist;
$state->tune=$song->tune;
$state->file=$song->file;
$state->asXML('musicstate.xml');break;}}
?>