<?php
include ("conexion.php");

$codigo = $_POST['codigo'];
$producto = $_POST['producto'];
$precio = $_POST['precio'];
$fabricante = $_POST['fabricante'];

$consulta  = "INSERT INTO producto VALUES (NULL, '$codigo', '$producto', '$precio', '$fabricante')";

if ($mysqli->query($consulta))
{
	echo "Se agrego";
}
else
{
	echo "Error";
}
?>

