<?php
	$hostname = "localhost";
	$user = "root";
	$password = "";
	$bbdd = "android1";
	// private $servername="localhost";
	// private $user="root";
	// private $password="";
	// private $bd="android1";
	// mysql://root:qTMlikRrnEomJUrwvVhjFAnSWBOtzPWr@autorack.proxy.rlwy.net:29996/railway
	$mysqli = new mysqli($hostname, $user, $password, $bbdd);
	if ($mysqli->connect_error) {
		die("Conexión fallida: " . $mysqli->connect_error);
	}
?>