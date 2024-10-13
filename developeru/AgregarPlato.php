<?php
// Incluir el archivo de conexión
include 'conexion.php';

// Verificar si los datos fueron enviados por POST
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Obtener los datos del POST
    $nombre = $_POST['nombre'] ?? ''; // Uso de null coalescing para evitar errores
    $precio = $_POST['precio'] ?? '';
    $cantidad = $_POST['cantidad'] ?? '';

    // Validar que no estén vacíos
    if (!empty($nombre) && !empty($precio) && !empty($cantidad)) {
        // Preparar la consulta
        $stmt = $mysqli->prepare("INSERT INTO platos (nombre, precio, cantidad_disponible) VALUES (?, ?, ?)");

        // Validar si la preparación de la consulta fue exitosa
        if ($stmt === false) {
            $response = array("success" => false, "message" => "Error preparando la consulta: " . $mysqli->error);
        } else {
            // Vincular parámetros (considera que precio es double y cantidad es integer)
            $stmt->bind_param("sdi", $nombre, $precio, $cantidad); // "s" para string, "d" para double, "i" para integer

            // Ejecutar la consulta
            if ($stmt->execute()) {
                // Responder con un JSON indicando éxito
                $response = array("success" => true, "message" => "Plato registrado exitosamente");
            } else {
                // Error al registrar en la base de datos
                $response = array("success" => false, "message" => "Error al registrar el Plato: " . $stmt->error);
            }

            // Cerrar la declaración
            $stmt->close();
        }
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
