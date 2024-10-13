<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Registro de Usuario</title>
    <link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h2>Registrar Nuevo Usuario</h2>
        <form action="crearbd.php" method="post">
            <div class="form-group">
                <label for="usuario">Usuario:</label>
                <input type="text" class="form-control" id="usuario" name="usuario" placeholder="Ingresa usuario" required>
            </div>
            <div class="form-group">
                <label for="contrasena">Contraseña:</label>
                <input type="password" class="form-control" id="contrasena" name="contrasena" placeholder="Ingresa contraseña" required>
            </div>
            <div class="form-group">
                <label for="nombreCompleto">Nombre Completo:</label>
                <input type="text" class="form-control" id="nombreCompleto" name="nombreCompleto" placeholder="Ingresa nombre completo" required>
            </div>
            <div class="form-group">
                <label for="direccion">Dirección:</label>
                <input type="text" class="form-control" id="direccion" name="direccion" placeholder="Ingresa dirección" required>
            </div>
            <button type="submit" class="btn btn-primary">Registrar</button>
        </form>
    </div>

    <script src="bootstrap/js/bootstrap.min.js"></script>
</body>
</html>
<link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css">
<?php
include ("conexion.php");

// Obtener datos del formulario
$usuario = $_POST['usuario'];
$contrasena = $_POST['contrasena'];
$nombreCompleto = $_POST['nombreCompleto'];
$direccion = $_POST['direccion'];

// Preparar la consulta para evitar inyecciones SQL
$consulta = "INSERT INTO Usuarios (usuario, contrasena, nombreCompleto, direccion) VALUES (?, ?, ?, ?)";
$stmt = $mysqli->prepare($consulta);

if ($stmt) {
    $stmt->bind_param("ssss", $usuario, $contrasena, $nombreCompleto, $direccion);

    // Ejecutar la consulta
    if ($stmt->execute()) {
        echo "Usuario agregado exitosamente.";
        header("Location: crear.php");
        exit();
    } else {
        echo "Error al agregar el usuario: " . $stmt->error;
    }

    // Cerrar la declaración
    $stmt->close();
} else {
    echo "Error en la preparación de la consulta: " . $mysqli->error;
}

// Cerrar la conexión
$mysqli->close();
?>
<a href="inicio.php" target="_self">Volver a la lista</a>
<script src="bootstrap/js/bootstrap.min.js"></script>
----