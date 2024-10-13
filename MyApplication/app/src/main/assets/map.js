var mapa = L.map("contenedor-del-mapa").setView([-17.487484, -66.178643], 14);
L.tileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png?", {}).addTo(mapa);
var customIcon = L.icon({
  iconUrl: "img.png", // URL o ruta local del ícono
  iconSize: [75, 60], // Tamaño del ícono
  iconAnchor: [22, 38], // Punto del ícono que se colocará en la posición del marcador
  popupAnchor: [15, -40], // Punto desde el que se abrirá el popup
});
// ****CREAR UN ICONO EN MI MAPA
var marcador = L.marker([-17.487484, -66.178643], { icon: customIcon }).addTo(
  mapa
);
var popupContent = `
               <a href="https://wa.me/59163906225" target="_blank" style="text-decoration: underline; font-weight: bold;">RESTAURANT</a><br>
            <ul>
                <li>Lunes: 10:00 - 22:00</li>
                <li>Martes: 10:00 - 22:00</li>
                <li>Miércoles: 10:00 - 22:00</li>
                <li>Jueves: 10:00 - 22:00</li>
                <li>Viernes: 10:00 - 23:00</li>
                <li>Sábado: 10:00 - 23:00</li>
                <li>Domingo: 10:00 - 21:00</li>
            </ul>
        `;

// Vincular un popup al marcador con el contenido de los horarios de atención
marcador.bindPopup(popupContent);

// Variables para controlar la visibilidad del popup y el temporizador
var popupTimer;
var popupVisible = false;

// Mostrar el popup y configurar el temporizador
marcador.on("mouseover", function () {
  if (!popupVisible) {
    marcador.openPopup();
    popupVisible = true;
    clearTimeout(popupTimer);
    popupTimer = setTimeout(function () {
      marcador.closePopup();
      popupVisible = false;
    }, 6000); // 5000 ms = 5 segundos
  }
});
