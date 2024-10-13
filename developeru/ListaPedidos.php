<?php 
// Incluir el archivo de conexión
include 'conexion.php';

// Verificar si la conexión se ha realizado correctamente
if ($mysqli->connect_error) {
    die("Conexión fallida: " . $mysqli->connect_error);
}

// Preparar la consulta para obtener los pedidos con los detalles del usuario, platos y bebidas
$query = "
    SELECT 
        p.id AS pedido_id,
        u.nombreCompleto,
        p.nombre_pedido,
        p.precio_total,
        p.fechaRegistro,
        GROUP_CONCAT(DISTINCT pl.nombre SEPARATOR ', ') AS platos,
        GROUP_CONCAT(DISTINCT pb.nombre SEPARATOR ', ') AS bebidas,
        SUM(pp.cantidad) AS cantidad_platos,
        SUM(pbeb.cantidad) AS cantidad_bebidas
    FROM 
        pedidos p
    INNER JOIN 
        usuarios u ON p.usuario_id = u.id
    LEFT JOIN 
        pedidoplato pp ON p.id = pp.pedido_id
    LEFT JOIN 
        platos pl ON pp.plato_id = pl.id
    LEFT JOIN 
        pedidobebida pbeb ON p.id = pbeb.pedido_id
    LEFT JOIN 
        bebidas pb ON pbeb.bebida_id = pb.id
    GROUP BY 
        p.id, u.nombreCompleto, p.nombre_pedido, p.precio_total, p.fechaRegistro
"; 

$result = $mysqli->query($query);

// Crear un array para almacenar los pedidos
$pedidos = array();

if ($result->num_rows > 0) {
    // Recorrer los resultados y agregarlos al array
    while ($row = $result->fetch_assoc()) {
        $pedidos[] = $row;
    }
}

// Enviar la respuesta como JSON
header('Content-Type: application/json');
echo json_encode($pedidos);

// Cerrar la conexión
$mysqli->close();
?>



