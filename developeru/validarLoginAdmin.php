<?php
// Incluir el archivo de conexión
include 'conexion.php'; 

// Verificar si la conexión fue exitosa
if ($mysqli->connect_error) {
    die("Conexión fallida: " . $mysqli->connect_error);
}

// Recibir los datos enviados desde la app
$usuario = $_POST['usuario'];
$contrasena = $_POST['contrasena'];

// Preparar la consulta para obtener la contraseña y el rol
$stmt = $mysqli->prepare("SELECT contrasena, rol FROM usuarios WHERE usuario = ?");
$stmt->bind_param("s", $usuario); // Vincular parámetros
$stmt->execute();
$result = $stmt->get_result();

$response = array();
$response['success'] = false;

// Verificar si se encontró el usuario
if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $hashedPassword = $row['contrasena']; // Obtener la contraseña encriptada
    $rol = $row['rol']; // Obtener el rol del usuario

    // Verificar la contraseña usando password_verify
    if (password_verify($contrasena, $hashedPassword)) {
        // Autenticación exitosa, devolver el rol del usuario
        $response['success'] = true;
        $response['rol'] = $rol;
    } else {
        // Contraseña incorrecta
        $response['success'] = false;
    }
} else {
    // Usuario no encontrado
    $response['success'] = false;
}

// Cerrar la conexión
$stmt->close();
$mysqli->close();

// Devolver la respuesta como JSON
header('Content-Type: application/json'); 
echo json_encode($response);
?>
