<?php
// Incluir el archivo de conexión
include 'conexion.php';

// Verificar si los datos fueron enviados por POST
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Obtener los datos del POST
    $nombre = $_POST['nombre'];
    $precio = $_POST['precio'];
    $cantidad = $_POST['cantidad']; // Añadido para la cantidad

    // Validar que no estén vacíos
    if (!empty($nombre) && !empty($precio) && !empty($cantidad)) { // Incluir cantidad en la validación
        // Preparar la consulta
        $stmt = $mysqli->prepare("INSERT INTO bebidas (nombre, precio, cantidad_disponible) VALUES (?, ?, ?)");

        // Vincular parámetros
        $stmt->bind_param("ssi", $nombre, $precio, $cantidad); // Cambiado a "ssi" para incluir un entero

        // Ejecutar la consulta
        if ($stmt->execute()) {
            // Responder con un JSON indicando éxito
            $response = array("success" => true, "message" => "Bebida registrada exitosamente");
        } else {
            // Error al registrar en la base de datos
            $response = array("success" => false, "message" => "Error al registrar la bebida");
        }

        // Cerrar la declaración
        $stmt->close();
    } else {
        // Campos vacíos
        $response = array("success" => false, "message" => "Por favor, completa todos los campos");
    }

    // Enviar la respuesta como JSON
    echo json_encode($response);
}

// Cerrar la conexión
$mysqli->close();
?>
