<?php
session_start();
require 'config.php';
$Type = $_GET['Type'];
$datarequested = $_GET['data'];
$message = "";
if(is_null($Type) && is_null($datarequested)){
$message = "both empty";
}elseif (is_null($Type)) {
	$message = "type";
}
elseif ( is_null($datarequested)) {
	$message = "datarequested";
}else{
	$message = $Type ." ". $datarequested;
}

switch ($Type) {
			case 'Themes':
				switch ($datarequested) {
					case 'Latest':
						 $stmt = $link->prepare("SELECT Type, DisplayName, downloadurl FROM items  WHERE Type = 'Themes' ORDER BY id DESC LIMIT 12;");
	
	                     //executing the query 
	                     $stmt->execute();
	
	                     //binding results to the query 
	                     $stmt->bind_result($type, $displayname, $downloadurl);
	                     $resultarray = array();
	                     while ($stmt->fetch()) {
	      	              $index = array();
	      	              $index['Type'] = $type;
	      	              $index['DisplayName'] = $displayname;
	      	              $index['downloadurl'] = $downloadurl;
	      	              array_push($resultarray, $index);
	      	              }
	      	
	                    echo json_encode($resultarray);
						break;
					case 'Treading':
						$sql = "SELECT fileid FROM downloadcount WHERE filetype = 'Themes' ORDER BY downloadcount DESC LIMIT 12 ";
						$result = $link->query($sql);
                        $resultarray = array();
						if ($result->num_rows > 0) {
						    // output data of each row
						    while($row = $result->fetch_assoc()) {
							        $fileid = $row['fileid'];
						    $stmt = $link->prepare("SELECT Type, DisplayName, downloadurl FROM items  WHERE Type = 'Wallpapers' && id = '".$fileid."';");
	
	                     //executing the query 
	                     $stmt->execute();
	
	                     //binding results to the query 
	                     $stmt->bind_result($type, $displayname, $downloadurl);
	                     
	                     while ($stmt->fetch()) {
	      	              $index = array();
	      	              $index['Type'] = $type;
	      	              $index['DisplayName'] = $displayname;
	      	              $index['downloadurl'] = $downloadurl;
	      	              array_push($resultarray, $index);
	      	              }
						    }
						      
						echo json_encode($resultarray);
						} else {
						    echo "0 results";
						}
						break;
					case 'Favorite':
						$currentuser = $_GET['userid'];
                        $stmt = $link->prepare("SELECT filetype, DisplayName, downloadurl FROM favorite  WHERE userid = '".$currentuser."'&& filetype = 'Themes';");
	
	                     //executing the query 
	                     $stmt->execute();
	
	                     //binding results to the query 
	                     $stmt->bind_result($type,$displayname,$downloadurl);
	                     $resultarray = array();
	                     while ($stmt->fetch()) {
	      	              $index = array();
	      	              $index['Type'] = $type;
	      	              $index['DisplayName'] = $displayname;
	      	              $index['downloadurl'] = $downloadurl;
	      	              array_push($resultarray, $index);
	      	              }
	      	
	                    echo json_encode($resultarray);
						break;
					default:
						echo "Enter datarequested";
						break;
				}
				break;
			case 'Ringtones':
				switch ($datarequested) {
					case 'Latest':
						 $stmt = $link->prepare("SELECT Type, DisplayName, downloadurl FROM items  WHERE Type = 'Ringtones' ORDER BY id DESC LIMIT 12;");
	
	                     //executing the query 
	                     $stmt->execute();
	
	                     //binding results to the query 
	                     $stmt->bind_result($type, $displayname, $downloadurl);
	                     $resultarray = array();
	                     while ($stmt->fetch()) {
	      	              $index = array();
	      	              $index['Type'] = $type;
	      	              $index['DisplayName'] = $displayname;
	      	              $index['downloadurl'] = $downloadurl;
	      	              array_push($resultarray, $index);
	      	              }
	      	
	                    echo json_encode($resultarray);
						break;
					case 'Treading':
						$sql = "SELECT fileid FROM downloadcount WHERE filetype = 'Ringtones' ORDER BY downloadcount DESC LIMIT 12 ";
						$result = $link->query($sql);
                        $resultarray = array();
						if ($result->num_rows > 0) {
						    // output data of each row
						    while($row = $result->fetch_assoc()) {
							        $fileid = $row['fileid'];
							        //echo "$fileid";
						    $stmt = $link->prepare("SELECT Type, DisplayName, downloadurl FROM items  WHERE Type = 'Ringtones' && id = '".$fileid."';");
	
	                     //executing the query 
	                     $stmt->execute();
	
	                     //binding results to the query 
	                     $stmt->bind_result($type, $displayname, $downloadurl);
	                     
	                     while ($stmt->fetch()) {
	      	              $index = array();
	      	              $index['Type'] = $type;
	      	              $index['DisplayName'] = $displayname;
	      	              $index['downloadurl'] = $downloadurl;
	      	              array_push($resultarray, $index);
	      	              }
						    }
						      
						echo json_encode($resultarray);
						} else {
						    echo "0 results";
						}
						break;
					case 'Favorite':
						$currentuser = $_GET['userid'];
                        $stmt = $link->prepare("SELECT filetype, DisplayName, downloadurl FROM favorite  WHERE userid = '".$currentuser."'&& filetype = 'Ringtones';");
	
	                     //executing the query 
	                     $stmt->execute();
	
	                     //binding results to the query 
	                     $stmt->bind_result($type,$displayname,$downloadurl);
	                     $resultarray = array();
	                     while ($stmt->fetch()) {
	      	              $index = array();
	      	              $index['Type'] = $type;
	      	              $index['DisplayName'] = $displayname;
	      	              $index['downloadurl'] = $downloadurl;
	      	              array_push($resultarray, $index);
	      	              }
	      	
	                    echo json_encode($resultarray);
						break;
					default:
						echo "Enter datarequested";
						break;
				}
				break;
			case 'Wallpapers':

				switch ($datarequested) {
					case 'Latest':
						 $stmt = $link->prepare("SELECT Type, DisplayName, downloadurl FROM items  WHERE Type = 'Wallpapers' ORDER BY id DESC LIMIT 12;");
	
	                     //executing the query 
	                     $stmt->execute();
	
	                     //binding results to the query 
	                     $stmt->bind_result($type, $displayname, $downloadurl);
	                     $resultarray = array();
	                     while ($stmt->fetch()) {
	      	              $index = array();
	      	              $index['Type'] = $type;
	      	              $index['DisplayName'] = $displayname;
	      	              $index['downloadurl'] = $downloadurl;
	      	              array_push($resultarray, $index);
	      	              }
	      	
	                    echo json_encode($resultarray);
						break;
					case 'Treading':
						$sql = "SELECT fileid FROM downloadcount WHERE filetype = 'Wallpapers' ORDER BY downloadcount DESC LIMIT 12 ";
						$result = $link->query($sql);
                        $resultarray = array();
						if ($result->num_rows > 0) {
						    // output data of each row
						    while($row = $result->fetch_assoc()) {
							        $fileid = $row['fileid'];
						    $stmt = $link->prepare("SELECT Type, DisplayName, downloadurl FROM items  WHERE Type = 'Wallpapers' && id = '".$fileid."';");
	
	                     //executing the query 
	                     $stmt->execute();
	
	                     //binding results to the query 
	                     $stmt->bind_result($type, $displayname, $downloadurl);
	                     
	                     while ($stmt->fetch()) {
	      	              $index = array();
	      	              $index['Type'] = $type;
	      	              $index['DisplayName'] = $displayname;
	      	              $index['downloadurl'] = $downloadurl;
	      	              array_push($resultarray, $index);
	      	              }
						    }
						      
						echo json_encode($resultarray);
						} else {
						    echo "0 results";
						}
						break;
					case 'Favorite':
					$currentuser = $_GET['userid'];
                        $stmt = $link->prepare("SELECT filetype, DisplayName, downloadurl FROM favorite  WHERE userid = '".$currentuser."'&& filetype = 'Wallpapers';");
	
	                     //executing the query 
	                     $stmt->execute();
	
	                     //binding results to the query 
	                     $stmt->bind_result($type,$displayname,$downloadurl);
	                     $resultarray = array();
	                     while ($stmt->fetch()) {
	      	              $index = array();
	      	              $index['Type'] = $type;
	      	              $index['DisplayName'] = $displayname;
	      	              $index['downloadurl'] = $downloadurl;
	      	              array_push($resultarray, $index);
	      	              }
	      	
	                    echo json_encode($resultarray);
					
						break;
					default:
						echo "Enter datarequested";
						break;
				}
			case 'Fonts':
				switch ($datarequested) {
					case 'Latest':
						 $stmt = $link->prepare("SELECT Type, DisplayName, downloadurl,backgroundurl FROM items  WHERE Type = 'Fonts' ORDER BY id DESC LIMIT 12;");
	
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
						break;
					case 'Treading':
						$sql = "SELECT fileid FROM downloadcount WHERE filetype = 'Fonts' ORDER BY downloadcount DESC LIMIT 12 ";
						$result = $link->query($sql);
                        $resultarray = array();
						if ($result->num_rows > 0) {
						    // output data of each row
						    while($row = $result->fetch_assoc()) {
							        $fileid = $row['fileid'];
						    $stmt = $link->prepare("SELECT Type, DisplayName, downloadurl,backgroundurl FROM items  WHERE Type = 'Fonts' && id = '".$fileid."';");
	
	                     //executing the query 
	                     $stmt->execute();
	
	                     //binding results to the query 
	                     $stmt->bind_result($type, $displayname, $downloadurl,$backgroundurl);
	                     
	                     while ($stmt->fetch()) {
	      	              $index = array();
	      	              $index['Type'] = $type;
	      	              $index['DisplayName'] = $displayname;
	      	              $index['downloadurl'] = $downloadurl;
	      	              $index['backgroundurl'] = $backgroundurl;
	      	              array_push($resultarray, $index);
	      	              }
						    }
						      
						echo json_encode($resultarray);
						} else {
						    echo "0 results";
						}
						break;
					case 'Favorite':
						$currentuser = $_GET['userid'];
                        $stmt = $link->prepare("SELECT filetype, DisplayName, downloadurl FROM favorite  WHERE userid = '".$currentuser."'&& filetype = 'Fonts';");
	
	                     //executing the query 
	                     $stmt->execute();
	
	                     //binding results to the query 
	                     $stmt->bind_result($type,$displayname,$downloadurl);
	                     $resultarray = array();
	                     while ($stmt->fetch()) {
	      	              $index = array();
	      	              $index['Type'] = $type;
	      	              $index['DisplayName'] = $displayname;
	      	              $index['downloadurl'] = $downloadurl;
	      	              array_push($resultarray, $index);
	      	              }
	      	
	                    echo json_encode($resultarray);
						break;
					default:
						echo "Enter datarequested";
						break;
				}
			default:
				//echo "enter type of data you required";
				break;
		}
?>