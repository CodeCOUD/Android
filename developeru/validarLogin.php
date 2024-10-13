<?php
// Incluir el archivo de conexión
include 'conexion.php'; // Asegúrate de que este archivo contiene la conexión a la base de datos

// Verificar si la conexión fue exitosa
if ($mysqli->connect_error) {
    die("Conexión fallida: " . $mysqli->connect_error);
}

// Recibir los datos enviados desde la app
$usuario = $_POST['usuario'];
$contrasena = $_POST['contrasena'];

// Preparar la consulta para evitar inyecciones SQL
$stmt = $mysqli->prepare("SELECT contrasena FROM usuarios WHERE usuario = ?");
$stmt->bind_param("s", $usuario); // Vincular parámetros
$stmt->execute();
$result = $stmt->get_result();

$response = array();
$response['success'] = false;

// Verificar si se encontró el usuario
if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $hashedPassword = $row['contrasena']; // Obtener la contraseña encriptada

    // Verificar la contraseña usando password_verify
    if (password_verify($contrasena, $hashedPassword)) {
        // Autenticación exitosa
        $response['success'] = true;
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
header('Content-Type: application/json'); // Asegúrate de enviar el tipo de contenido adecuado
echo json_encode($response);
?>
