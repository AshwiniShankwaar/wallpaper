<?php
session_start();
require_once "config.php";
$commenttext = $_POST['comment'];
$fileid = $_POST['id'];
$filename = $_POST['filename'];
$userid = $_POST['userid'];

$sql = "INSERT INTO comment(fileid,user_id,comment,filename) VALUES('".$fileid."','".$userid."','".$commenttext."','".$filename."')";
if ($link->query($sql) === TRUE) {
    echo "record inserted";
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

$link->close();
?>