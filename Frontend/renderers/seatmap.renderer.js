class SeatmapRenderer {
  constructor(containerId) {
    this.container = document.getElementById(containerId);
    this.svgElement = null;
    this.zonasData = [];
    this.selectedZonaId = null;
    this.onZonaClick = null;
    this.tooltipEl = document.getElementById('svg-tooltip');
  }

  async cargarSVG(estadioId) {
    const file = estadioId === 'atanasio'
      ? 'estadio-atanasio.svg'
      : 'estadio-campin.svg';
    const svgText = await fetch(file).then(r => {
      if (!r.ok) throw new Error('No se pudo cargar el SVG del estadio');
      return r.text();
    });
    this.container.innerHTML = svgText;
    this.svgElement = this.container.querySelector('svg');
    return this.svgElement;
  }

  aplicarHeatmap(zonasData) {
    this.zonasData = zonasData;
    const zonesLayer = this.svgElement.querySelector('#zones-layer');
    if (!zonesLayer) return;

    zonasData.forEach(zona => {
      const g = zonesLayer.querySelector(`g[data-zona="${zona.nombre}"]`);
      if (!g) return;
      const path = g.querySelector('path');
      if (!path) return;

      const total = zona.capacidad || zoneConfig[zona.nombre]?.capacidad || 1;
      const sold = zona.ocupados || 0;
      const color = getZoneColor(zona.nombre, sold, total);

      path.style.fill = color.fill;
      path.style.stroke = color.stroke;
      g.dataset.ocupacion = (sold / total);
      g.dataset.disponibles = total - sold;
    });
  }

  initInteractions(onZonaClick) {
    this.onZonaClick = onZonaClick;
    if (!this.svgElement) return;

    const zonesLayer = this.svgElement.querySelector('#zones-layer');
    if (!zonesLayer) return;

    zonesLayer.addEventListener('mouseenter', (e) => {
      const zona = e.target.closest('.zona-seatmap');
      if (!zona) return;
      const path = zona.querySelector('path');
      if (path && !zona.classList.contains('selected')) {
        path.style.filter = 'url(#glow-hover)';
      }
      this._mostrarTooltip(zona, e);
    }, true);

    zonesLayer.addEventListener('mousemove', (e) => {
      const zona = e.target.closest('.zona-seatmap');
      if (!zona) return;
      this._moverTooltip(e);
    }, true);

    zonesLayer.addEventListener('mouseleave', (e) => {
      const zona = e.target.closest('.zona-seatmap');
      if (!zona) return;
      const path = zona.querySelector('path');
      if (path && !zona.classList.contains('selected')) {
        path.style.filter = 'none';
      }
      this._ocultarTooltip();
    }, true);

    zonesLayer.addEventListener('click', (e) => {
      const zona = e.target.closest('.zona-seatmap');
      if (!zona) return;
      const zoneName = zona.dataset.zona;
      if (!zoneName) return;

      this._seleccionarZona(zona, zoneName);
      if (this.onZonaClick) this.onZonaClick(zoneName);
    });
  }

  _seleccionarZona(zonaEl, zoneName) {
    const svg = this.svgElement;
    svg.querySelectorAll('.zona-seatmap.selected').forEach(el => {
      el.classList.remove('selected');
      const p = el.querySelector('path');
      if (p) p.style.filter = 'none';
    });

    zonaEl.classList.add('selected');
    const path = zonaEl.querySelector('path');
    if (path) {
      path.style.filter = 'url(#glow-selected)';
      path.style.transition = 'transform 200ms ease, filter 200ms ease';
      path.style.transform = 'scale(1.02)';
      setTimeout(() => { path.style.transform = 'scale(1)'; }, 250);
    }
    this.selectedZonaId = zoneName;
  }

  limpiarSeleccion() {
    if (!this.svgElement) return;
    this.svgElement.querySelectorAll('.zona-seatmap.selected').forEach(el => {
      el.classList.remove('selected');
      const p = el.querySelector('path');
      if (p) p.style.filter = 'none';
    });
    this.selectedZonaId = null;
  }

  _mostrarTooltip(zonaEl, event) {
    if (!this.tooltipEl) return;
    const zoneName = zonaEl.dataset.zona;
    const cfg = zoneConfig[zoneName];
    if (!cfg) return;

    const disponibles = parseInt(zonaEl.dataset.disponibles) || cfg.capacidad;
    const ocupacion = parseFloat(zonaEl.dataset.ocupacion) || 0;
    const nivel = getHeatmapLevel(ocupacion);

    this.tooltipEl.innerHTML = `
      <div class="tip-nombre">${cfg.label}</div>
      <div class="tip-precio">${formatCurrency(cfg.precio)}</div>
      <div class="tip-disponibles">${formatDisponibles(disponibles, cfg.capacidad)}</div>
      <div class="tip-ocupacion">${nivel}</div>
    `;
    this._moverTooltip(event);
    this.tooltipEl.classList.remove('hidden');
  }

  _moverTooltip(event) {
    if (!this.tooltipEl) return;
    let x = event.clientX + 15;
    let y = event.clientY - 10;
    const tw = this.tooltipEl.offsetWidth || 180;
    const th = this.tooltipEl.offsetHeight || 80;
    if (x + tw > window.innerWidth - 10) x = event.clientX - tw - 15;
    if (y + th > window.innerHeight - 10) y = event.clientY - th - 10;
    if (y < 10) y = 10;
    this.tooltipEl.style.left = x + 'px';
    this.tooltipEl.style.top = y + 'px';
  }

  _ocultarTooltip() {
    if (this.tooltipEl) this.tooltipEl.classList.add('hidden');
  }

  destroy() {
    this._ocultarTooltip();
    this.container.innerHTML = '';
    this.svgElement = null;
    this.zonasData = [];
    this.selectedZonaId = null;
  }
}
