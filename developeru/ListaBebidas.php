<?php
// Incluir el archivo de conexión
include 'conexion.php';

// Verificar si la conexión se ha realizado correctamente
if ($mysqli->connect_error) {
    die("Conexión fallida: " . $mysqli->connect_error);
}

// Preparar la consulta para obtener las bebidas
$query = "SELECT nombre, precio FROM bebidas"; // Asegúrate de que esta tabla exista

$result = $mysqli->query($query);

// Crear un array para almacenar las bebidas
$bebidas = array();

if ($result->num_rows > 0) {
    // Recorrer los resultados y agregarlos al array
    while ($row = $result->fetch_assoc()) {
        $bebidas[] = $row;
    }
}

// Enviar la respuesta como JSON
header('Content-Type: application/json');
echo json_encode($bebidas);

// Cerrar la conexión
$mysqli->close();
?>

