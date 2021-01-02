<?php
session_start();
require_once 'config.php';

$sql = "SELECT keyword FROM keywordcount ORDER BY keycount DESC";
$result = $link->query($sql);
$ressultarray = array();
if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        $index = array();
        $index ['keyword'] = $row['keyword'];
        array_push($ressultarray, $index);
    }
} else {
    echo "0 results";
}
echo json_encode($ressultarray);
$link->close();
?>