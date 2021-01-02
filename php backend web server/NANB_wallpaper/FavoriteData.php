<?php


session_start();
require_once "config.php";


$id = $_POST['id'];
$userid = $_POST['UserId'];
$filename = $_POST['filename'];
$downloadurl = $_POST['downloadurl'];
$filetype = $_POST['filetype'];
$DisplayName = $_POST['DisplayName'];

$sql = "SELECT * FROM favorite WHERE userid = '".$userid."'&& fileid = '".$id."' && filename = '".$filename."'";
$result = $link->query($sql);

if ($result->num_rows > 0) {
    $sql = "DELETE FROM favorite WHERE userid = '".$userid."' && fileId = '".$id."' && filename = '".$filename."'";

		if ($link->query($sql) === TRUE) {
		    echo "Unfavorite";
		} else {
		    echo "Error deleting record: " . $conn->error;
		}
} else {
	$sql = "INSERT INTO favorite (userid,fileId,filename,downloadurl,filetype,DisplayName) VALUES ('".$userid."','".$id."','".$filename."','".$downloadurl."','".$filetype."','".$DisplayName."')";

		if ($link->query($sql) === TRUE) {
		    echo "Favorite";
		} else {
		    echo "Error: " . $sql . "<br>" . $link->error;
		}
}

$link->close();
?>




