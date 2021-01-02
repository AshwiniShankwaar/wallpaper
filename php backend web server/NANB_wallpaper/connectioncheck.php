<?php
session_start();
require_once 'config.php';
$index = array();
$index['message'] = "connected";
echo json_encode($index);
?>