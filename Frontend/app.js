// ========== CONFIG ==========
const API_AUTH = 'http://localhost:8081/api/auth';
const API_CATALOGO = 'http://localhost:8082/api/conciertos';
const API_NOTIFICACIONES = 'http://localhost:8083/api/notificaciones';
const API_MAPA = 'http://localhost:8084/api/mapa-calor';

// ========== STATE ==========
let isLoginMode = true;
let currentUser = null;
let currentUserId = null;
let selectedZona = null;
let zonasData = [];
let conciertoActual = null;
let boletosComprados = null;
let seatmapRenderer = null;

// ========== DOM REFS ==========
const viewAuth = document.getElementById('view-auth');
const viewCatalog = document.getElementById('view-catalog');
const viewMisBoletos = document.getElementById('view-mis-boletos');
const navLinks = document.getElementById('nav-links');
const userInfo = document.getElementById('user-info');
const userEmailSpan = document.getElementById('user-email');

const authTitle = document.getElementById('auth-title');
const btnSubmit = document.getElementById('btn-submit');
const toggleLink = document.getElementById('toggle-auth-link');
const toggleText = document.getElementById('toggle-auth-text');
const authForm = document.getElementById('auth-form');
const errorMsg = document.getElementById('auth-error');

const concertsGrid = document.getElementById('concerts-grid');
const boletosContainer = document.getElementById('boletos-container');
const modalEstadio = document.getElementById('modal-estadio');
const modalConfirmacion = document.getElementById('modal-confirmacion');

// ========== NAV EVENTS ==========
document.getElementById('nav-login').addEventListener('click', (e) => { e.preventDefault(); showAuth(true); });
document.getElementById('nav-register').addEventListener('click', (e) => { e.preventDefault(); showAuth(false); });
document.getElementById('nav-logout').addEventListener('click', logout);
document.getElementById('nav-mis-boletos').addEventListener('click', (e) => { e.preventDefault(); mostrarMisBoletos(); });

document.getElementById('btn-cerrar-estadio').addEventListener('click', cerrarModalEstadio);
document.getElementById('btn-cancelar-compra').addEventListener('click', cerrarModalEstadio);
document.getElementById('btn-cerrar-confirmacion').addEventListener('click', cerrarModalConfirmacion);
document.getElementById('btn-aceptar-compra').addEventListener('click', comprarBoletos);
document.getElementById('estadio-cantidad').addEventListener('input', actualizarTotal);
document.getElementById('btn-volver-catalogo').addEventListener('click', volverCatalogo);

toggleLink.addEventListener('click', (e) => {
  e.preventDefault();
  showAuth(!isLoginMode);
});

// ========== AUTH ==========
authForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  errorMsg.textContent = '';
  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;

  btnSubmit.disabled = true;
  btnSubmit.textContent = 'Procesando...';

  try {
    if (isLoginMode) {
      const res = await fetch(`${API_AUTH}/iniciar-sesion?correo=${encodeURIComponent(email)}&contraseña=${encodeURIComponent(password)}`, { method: 'POST' });
      const text = await res.text();
      if (!res.ok) throw new Error(text);
      handleLoginSuccess(email, null);
    } else {
      const res = await fetch(`${API_AUTH}/registrar`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ correo: email, contraseña: password, rol: 'USER' })
      });
      if (!res.ok) { const text = await res.text(); throw new Error(text); }
      const user = await res.json();
      fetch(`${API_NOTIFICACIONES}/enviar-verificacion`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: email, usuarioId: user.id })
      }).catch(() => {});
      handleLoginSuccess(email, user.id);
    }
  } catch (err) {
    errorMsg.textContent = err.message || 'Error de conexión';
  } finally {
    btnSubmit.disabled = false;
    btnSubmit.textContent = isLoginMode ? 'Ingresar' : 'Crear Cuenta';
  }
});

function showAuth(login) {
  isLoginMode = login;
  authTitle.textContent = login ? 'Bienvenido a la Música' : 'Únete a MyStadium';
  btnSubmit.textContent = login ? 'Ingresar' : 'Crear Cuenta';
  toggleText.innerHTML = login
    ? '¿No tienes cuenta? <a href="#" id="toggle-auth-link">Regístrate aquí</a>'
    : '¿Ya tienes cuenta? <a href="#" id="toggle-auth-link">Inicia sesión</a>';
  document.getElementById('toggle-auth-link').addEventListener('click', (e) => {
    e.preventDefault();
    showAuth(!isLoginMode);
  });
  switchView(viewAuth);
}

function handleLoginSuccess(email, userId) {
  currentUser = email;
  currentUserId = userId || email.replace(/[^a-zA-Z0-9]/g, '');
  navLinks.classList.add('hidden');
  userInfo.classList.remove('hidden');
  userEmailSpan.textContent = email;
  loadCatalog();
  switchView(viewCatalog);
}

function logout() {
  currentUser = null;
  currentUserId = null;
  navLinks.classList.remove('hidden');
  userInfo.classList.add('hidden');
  showAuth(true);
}

function switchView(viewToShow) {
  document.querySelectorAll('.view').forEach(v => {
    v.classList.remove('active', 'fade-in');
    v.classList.add('hidden');
  });
  viewToShow.classList.remove('hidden');
  void viewToShow.offsetWidth;
  viewToShow.classList.add('active', 'fade-in');
}

// ========== CATALOG ==========
async function loadCatalog() {
  concertsGrid.innerHTML = '<p style="grid-column: 1/-1; text-align:center">Cargando conciertos...</p>';
  try {
    const res = await fetch(API_CATALOGO);
    if (!res.ok) throw new Error('Error al cargar catálogo');
    const data = await res.json();
    if (data.length === 0) {
      await populateDummyData();
      return loadCatalog();
    }
    renderConcerts(data);
  } catch (err) {
    concertsGrid.innerHTML = '<p style="grid-column: 1/-1; text-align:center; color:#ef4444">Fallo al conectar con el servidor de catálogo.</p>';
  }
}

function renderConcerts(concerts) {
  concertsGrid.innerHTML = '';
  concerts.forEach((c, index) => {
    const date = new Date(c.fecha).toLocaleDateString('es-ES', { day: '2-digit', month: 'short' });
    const card = document.createElement('div');
    card.className = 'concert-card';
    card.style.animationDelay = `${index * 0.1}s`;
    const formatPrice = new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 }).format(c.precioBase);
    card.innerHTML = `
      <div class="concert-img" style="background-image: url('${c.imagenUrl}')">
        <div class="concert-date-badge">${date}</div>
      </div>
      <div class="concert-info">
        <div class="concert-artist">${c.artista}</div>
        <h3 class="concert-name">${c.nombre}</h3>
        <div class="concert-location">
          <svg width="16" height="16" fill="currentColor" viewBox="0 0 16 16"><path d="M8 16s6-5.686 6-10A6 6 0 0 0 2 6c0 4.314 6 10 6 10zm0-7a3 3 0 1 1 0-6 3 3 0 0 1 0 6z"/></svg>
          ${c.estadio}
        </div>
        <div class="concert-footer">
          <div class="concert-price">${formatPrice}</div>
          <button class="btn-primary comprar-btn" style="padding: 0.4rem 1rem"
            data-id="${c.id}" data-nombre="${c.nombre}" data-artista="${c.artista}"
            data-estadio="${c.estadio}" data-fecha="${c.fecha}">Comprar</button>
        </div>
      </div>`;
    concertsGrid.appendChild(card);
  });

  document.querySelectorAll('.comprar-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      conciertoActual = {
        id: btn.dataset.id,
        nombre: btn.dataset.nombre,
        artista: btn.dataset.artista,
        estadio: btn.dataset.estadio,
        fecha: btn.dataset.fecha
      };
      abrirModalEstadio();
    });
  });
}

async function populateDummyData() {
  const demos = [
    { nombre: 'Gira de los Estadios', artista: 'Morat', estadio: 'Estadio El Campín', ciudad: 'Bogotá', fecha: '2026-08-15', precioBase: 250000, imagenUrl: 'imagenes/Morat.jpeg' },
    { nombre: 'Mañana Será Bonito Tour', artista: 'Karol G', estadio: 'Estadio Atanasio Girardot', ciudad: 'Medellín', fecha: '2026-10-20', precioBase: 400000, imagenUrl: 'imagenes/KarolG.jpeg' },
    { nombre: 'World Tour 2026', artista: 'Coldplay', estadio: 'Estadio El Campín', ciudad: 'Bogotá', fecha: '2026-11-05', precioBase: 550000, imagenUrl: 'imagenes/ColdPlay.jpeg' }
  ];
  for (let d of demos) {
    await fetch(API_CATALOGO, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(d)
    });
  }
}

// ========== MODAL ESTADIO (SVG) ==========
async function abrirModalEstadio() {
  selectedZona = null;
  zonasData = [];
  document.getElementById('estadio-zona-seleccionada').textContent = 'Ninguna';
  document.getElementById('estadio-cantidad').value = 1;
  document.getElementById('estadio-total').textContent = '$0';
  document.getElementById('btn-aceptar-compra').disabled = true;

  document.getElementById('estadio-concierto-nombre').textContent = conciertoActual.nombre;
  document.getElementById('estadio-concierto-info').textContent = conciertoActual.artista + ' - ' + conciertoActual.estadio;

  modalEstadio.classList.remove('hidden');

  try {
    const res = await fetch(`${API_MAPA}/${conciertoActual.id}/zonas`);
    if (!res.ok) throw new Error('Error al cargar zonas');
    zonasData = await res.json();

    const estadioId = conciertoActual.estadio.toLowerCase().includes('atanasio') ? 'atanasio' : 'campin';
    seatmapRenderer = new SeatmapRenderer('estadio-svg-container');
    await seatmapRenderer.cargarSVG(estadioId);
    seatmapRenderer.aplicarHeatmap(zonasData);
    seatmapRenderer.initInteractions((zoneName) => {
      const zona = zonasData.find(z => z.nombre === zoneName);
      if (!zona) return;
      if (zona.ocupados >= zona.capacidad) {
        alert('Esta zona está completamente ocupada. Selecciona otra.');
        seatmapRenderer.limpiarSeleccion();
        return;
      }
      selectedZona = zona;
      const cfg = zoneConfig[zoneName];
      document.getElementById('estadio-zona-seleccionada').textContent =
        cfg.label + ' (' + formatCurrency(zona.precio) + ')';
      document.getElementById('btn-aceptar-compra').disabled = false;
      actualizarTotal();
    });
  } catch (err) {
    alert('Error al cargar el mapa del estadio');
    cerrarModalEstadio();
  }
}

function cerrarModalEstadio() {
  modalEstadio.classList.add('hidden');
  if (seatmapRenderer) {
    seatmapRenderer.destroy();
    seatmapRenderer = null;
  }
  selectedZona = null;
}

function actualizarTotal() {
  if (!selectedZona) return;
  const cantidad = parseInt(document.getElementById('estadio-cantidad').value) || 1;
  const total = selectedZona.precio * cantidad;
  document.getElementById('estadio-total').textContent = '$' + total.toLocaleString('es-CO');
}

function inferirTipoEntrada(zonaNombre) {
  const name = (zonaNombre || '').toLowerCase();
  if (name.includes('vip')) return 'VIP';
  if (name.includes('palco')) return 'PALCO';
  if (name.includes('oriental-central') || name.includes('occidental-central')) return 'PREFERENCIAL';
  return 'GENERAL';
}

// ========== COMPRA ==========
async function comprarBoletos() {
  if (!selectedZona || !conciertoActual) return;
  const cantidad = parseInt(document.getElementById('estadio-cantidad').value) || 1;

  document.getElementById('btn-aceptar-compra').disabled = true;
  document.getElementById('btn-aceptar-compra').textContent = 'Procesando...';

  try {
    const res = await fetch(`${API_MAPA}/comprar`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        usuarioId: currentUserId,
        conciertoId: conciertoActual.id,
        conciertoNombre: conciertoActual.nombre,
        artista: conciertoActual.artista,
        zonaId: selectedZona.id,
        cantidad: cantidad,
        estadio: conciertoActual.estadio || '',
        fechaEvento: conciertoActual.fecha ? conciertoActual.fecha + 'T20:00:00' : new Date().toISOString(),
        tipoEntrada: inferirTipoEntrada(selectedZona.nombre)
      })
    });
    const data = await res.json();
    if (!res.ok) throw new Error(data.error || 'Error en la compra');

    boletosComprados = data.boletos;
    const total = data.total;

    cerrarModalEstadio();
    mostrarConfirmacion(boletosComprados, total);
  } catch (err) {
    alert('Error: ' + err.message);
  } finally {
    document.getElementById('btn-aceptar-compra').disabled = false;
    document.getElementById('btn-aceptar-compra').textContent = 'Comprar';
  }
}

// ========== CONFIRMACION ==========
function mostrarConfirmacion(boletos, total) {
  const boleto = boletos[0];
  document.getElementById('conf-concierto').textContent = boleto.conciertoNombre;
  document.getElementById('conf-artista').textContent = boleto.artista;
  document.getElementById('conf-zona').textContent = boleto.zonaNombre;
  document.getElementById('conf-asientos').textContent = boletos.map(b => b.asiento).join(', ');
  document.getElementById('conf-total').textContent = '$' + total.toLocaleString('es-CO');
  document.getElementById('conf-codigo').textContent = boleto.codigoUnico;

  modalConfirmacion.classList.remove('hidden');

  const codigos = boletos.map(b => b.codigoUnico);
  const emailBody = { codigos, usuarioId: currentUserId, email: currentUser };
  fetch(`${API_NOTIFICACIONES}/enviar-recibo-compra`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(emailBody)
  }).then(async r => {
    if (!r.ok) console.error('Email error:', emailBody, await r.text());
  }).catch(e => console.error('Email network error:', e));

  // Descargar PDF combinado con todos los boletos
  setTimeout(() => descargarPdfCombinado(codigos), 1000);
}

function cerrarModalConfirmacion() {
  modalConfirmacion.classList.add('hidden');
}

function descargarPdf(codigo) {
  const link = document.createElement('a');
  link.href = `${API_NOTIFICACIONES}/pdf/${codigo}`;
  link.download = `recibo-${codigo}.pdf`;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

async function descargarPdfCombinado(codigos) {
  try {
    const res = await fetch(`${API_NOTIFICACIONES}/pdf-combinado`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ codigos })
    });
    if (!res.ok) throw new Error('Error al descargar PDF combinado');
    const blob = await res.blob();
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = 'recibo-mystadium.pdf';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    setTimeout(() => URL.revokeObjectURL(url), 5000);
  } catch (e) {
    console.error('Error descargando PDF combinado:', e);
  }
}

// ========== MIS BOLETOS ==========
async function mostrarMisBoletos() {
  switchView(viewMisBoletos);
  boletosContainer.innerHTML = '<p style="text-align:center; color:#94a3b8">Cargando tus boletos...</p>';

  try {
    const res = await fetch(`${API_MAPA}/boletos/${currentUserId}`);
    if (!res.ok) throw new Error('Error al cargar boletos');
    const boletos = await res.json();

    if (boletos.length === 0) {
      boletosContainer.innerHTML = '<p style="text-align:center; color:#94a3b8">No has comprado boletos aún.</p>';
      return;
    }

    const grupos = {};
    boletos.forEach(b => {
      if (!grupos[b.codigoUnico]) grupos[b.codigoUnico] = [];
      grupos[b.codigoUnico].push(b);
    });

    boletosContainer.innerHTML = '';
    Object.values(grupos).forEach(grupo => {
      const primero = grupo[0];
      const asientos = grupo.map(b => b.asiento).join(', ');
      const total = grupo.reduce((sum, b) => sum + b.totalPagado, 0);
      const fecha = new Date(primero.fechaCompra).toLocaleDateString('es-ES', {
        day: '2-digit', month: 'long', year: 'numeric'
      });

      const cfg = zoneConfig[primero.zonaNombre];
      const labelZona = cfg ? cfg.label : primero.zonaNombre;

      const card = document.createElement('div');
      card.className = 'boleto-card';
      card.innerHTML = `
        <div class="boleto-header">
          <span class="boleto-artista">${primero.artista}</span>
          <span class="boleto-codigo">${primero.codigoUnico}</span>
        </div>
        <div class="boleto-body">
          <p><strong>${primero.conciertoNombre}</strong></p>
          <p>Zona: ${labelZona} | Asientos: ${asientos}</p>
          <p>Total: $${total.toLocaleString('es-CO')} | ${fecha}</p>
        </div>
        <button class="btn-secondary boleto-descargar-btn" data-codigo="${primero.codigoUnico}">Descargar PDF</button>
      `;
      boletosContainer.appendChild(card);

      card.querySelector('.boleto-descargar-btn').addEventListener('click', () => {
        descargarPdf(primero.codigoUnico);
      });
    });
  } catch (err) {
    boletosContainer.innerHTML = '<p style="text-align:center; color:#ef4444">Error al cargar boletos.</p>';
  }
}

function volverCatalogo() {
  viewMisBoletos.classList.add('hidden');
  viewMisBoletos.classList.remove('active');
  viewCatalog.classList.remove('hidden');
  void viewCatalog.offsetWidth;
  viewCatalog.classList.add('active', 'fade-in');
}
