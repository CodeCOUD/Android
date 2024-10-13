<?php
// Incluir archivo de conexión a la base de datos
include 'conexion.php';

// Obtener el JSON enviado desde la solicitud
$data = json_decode(file_get_contents("php://input"), true);

// Verificar si se recibió el carrito
if (!$data || !is_array($data)) {
    echo json_encode(["success" => false, "message" => "No se recibió el carrito o los datos no son válidos."]);
    exit;
}

$carrito = $data;  // Asumiendo que $data contiene directamente los ítems del carrito
$usuario_id = 1;  // Puedes obtener esto desde la sesión o pasar el ID del usuario desde Android
$nombrePedido = "Pedido_" . time();  // Nombre único para el pedido basado en timestamp
$totalPrecio = 0;  // Inicializar el precio total

// Iniciar la transacción
$mysqli->begin_transaction();

try {
    // Preparar la inserción del pedido en la tabla 'pedidos'
    $insertPedidoQuery = "INSERT INTO pedidos (usuario_id, nombre_pedido, precio_total, estado, fechaRegistro) VALUES (?, ?, ?, 1, NOW())";
    $stmtPedido = $mysqli->prepare($insertPedidoQuery);
    
    // Calcular el precio total antes de insertar el pedido
    foreach ($carrito as $item) {
        $totalPrecio += $item['precio'] * $item['cantidad'];
    }
    
    // Ejecutar la consulta de inserción del pedido
    $stmtPedido->bind_param("isd", $usuario_id, $nombrePedido, $totalPrecio);
    if (!$stmtPedido->execute()) {
        throw new Exception("Error al insertar el pedido.");
    }

    // Obtener el ID del último pedido insertado
    $pedido_id = $mysqli->insert_id;

    // Preparar las consultas para insertar platos y bebidas
    $insertPlatoQuery = "INSERT INTO pedidoplato (pedido_id, plato_id, cantidad, estado) VALUES (?, ?, ?, 1)";
    $insertBebidaQuery = "INSERT INTO pedidobebida (pedido_id, bebida_id, cantidad, estado) VALUES (?, ?, ?, 1)";
    
    $stmtPlato = $mysqli->prepare($insertPlatoQuery);
    $stmtBebida = $mysqli->prepare($insertBebidaQuery);

    // Procesar cada ítem del carrito
    foreach ($carrito as $item) {
        $nombre = $item['nombre'];
        $cantidad = $item['cantidad'];
        $tipo = $item['tipo'];  // Verificar si es plato o bebida

        // Dependiendo del tipo, insertar en la tabla correspondiente
        if ($tipo === 'plato') {
            // Obtener el ID del plato por su nombre
            $plato_id = getItemId($mysqli, 'platos', $nombre);
            if ($plato_id) {
                $stmtPlato->bind_param("iii", $pedido_id, $plato_id, $cantidad);
                $stmtPlato->execute();
            } else {
                throw new Exception("El plato '$nombre' no se encontró en la base de datos.");
            }
        } elseif ($tipo === 'bebida') {
            // Obtener el ID de la bebida por su nombre
            $bebida_id = getItemId($mysqli, 'bebidas', $nombre);
            if ($bebida_id) {
                $stmtBebida->bind_param("iii", $pedido_id, $bebida_id, $cantidad);
                $stmtBebida->execute();
            } else {
                throw new Exception("La bebida '$nombre' no se encontró en la base de datos.");
            }
        }
    }

    // Confirmar la transacción
    $mysqli->commit();

    echo json_encode(["success" => true, "message" => "Pedido completado con éxito."]);

} catch (Exception $e) {
    // Si algo falla, deshacer la transacción
    $mysqli->rollback();
    echo json_encode(["success" => false, "message" => "Error al procesar el pedido: " . $e->getMessage()]);
}

// Cerrar las conexiones
$stmtPedido->close();
$stmtPlato->close();
$stmtBebida->close();
$mysqli->close();

// Función para obtener el ID del ítem (plato o bebida) según el nombre
function getItemId($mysqli, $table, $name) {
    $stmt = $mysqli->prepare("SELECT id FROM $table WHERE nombre = ?");
    $stmt->bind_param("s", $name);
    $stmt->execute();
    $result = $stmt->get_result();
    $row = $result->fetch_assoc();
    return $row['id'] ?? null;  // Devolver el ID o null si no se encuentra
}
?>
