<?php
session_start();
require_once 'config.php';
$type = $_GET['Type'];
$tag = $_GET['Tag'];
$stmt = $link->prepare("SELECT Type, DisplayName, downloadurl,backgroundurl FROM items  WHERE Type = '".$type."' AND Tag = '".$tag."' ORDER BY id DESC LIMIT 12;");
	
	                     //executing the query 
	                     $stmt->execute();
	
	                     //binding results to the query 
	                     $stmt->bind_result($type, $displayname, $downloadurl,$backgroundurl);
	                     $resultarray = array();
	                     while ($stmt->fetch()) {
	      	              $index = array();
	      	              $index['Type'] = $type;
	      	              $index['DisplayName'] = $displayname;
	      	              $index['downloadurl'] = $downloadurl;
	      	              $index['backgroundurl'] = $backgroundurl;
	      	              array_push($resultarray, $index);
	      	              }
	      	
	                    echo json_encode($resultarray);
$link->close();
?>