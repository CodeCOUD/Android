<?php
// Incluir el archivo de conexión
include 'conexion.php'; // Asegúrate de que este archivo contiene la conexión a la base de datos

// Verificar si los datos fueron enviados por POST
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Obtener los datos del POST
    $usuario = $_POST['usuario'] ?? '';
    $contrasena = $_POST['contrasena'] ?? '';
    $nombreCompleto = $_POST['nombreCompleto'] ?? '';
    $direccion = $_POST['direccion'] ?? '';

    // Validar que no estén vacíos
    if (!empty($usuario) && !empty($contrasena) && !empty($nombreCompleto) && !empty($direccion)) {
        // Verificar si el usuario ya existe
        $stmt = $mysqli->prepare("SELECT * FROM usuarios WHERE usuario = ?");
        $stmt->bind_param("s", $usuario);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows > 0) {
            // El usuario ya existe
            $response = array("success" => false, "message" => "El usuario ya existe.");
        } else {
            // Preparar la consulta para insertar el nuevo usuario
            $stmt = $mysqli->prepare("INSERT INTO usuarios (usuario, contrasena, nombreCompleto, direccion) VALUES (?, ?, ?, ?)");
            $hashedPassword = password_hash($contrasena, PASSWORD_DEFAULT); // Hash de la contraseña
            $stmt->bind_param("ssss", $usuario, $hashedPassword, $nombreCompleto, $direccion);

            // Ejecutar la consulta
            if ($stmt->execute()) {
                // Responder con un JSON indicando éxito
                $response = array("success" => true, "message" => "Usuario registrado exitosamente");
            } else {
                // Error al registrar en la base de datos
                $response = array("success" => false, "message" => "Error al registrar el usuario");
            }
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


