const zoneConfig = {
  "norte-alta": {
    label: "Norte Alta",
    capacidad: 800,
    precio: 35000,
    color: "#6366f1",
    posicion: "superior",
    anillo: "exterior"
  },
  "norte-baja": {
    label: "Norte Baja",
    capacidad: 600,
    precio: 45000,
    color: "#8b5cf6",
    posicion: "superior",
    anillo: "medio"
  },
  "sur-alta": {
    label: "Sur Alta",
    capacidad: 800,
    precio: 35000,
    color: "#6366f1",
    posicion: "inferior",
    anillo: "exterior"
  },
  "sur-baja": {
    label: "Sur Baja",
    capacidad: 600,
    precio: 45000,
    color: "#8b5cf6",
    posicion: "inferior",
    anillo: "medio"
  },
  "oriental-norte": {
    label: "Oriental Norte",
    capacidad: 500,
    precio: 65000,
    color: "#3b82f6",
    posicion: "derecha-superior",
    anillo: "exterior"
  },
  "oriental-central": {
    label: "Oriental Central",
    capacidad: 400,
    precio: 120000,
    color: "#f59e0b",
    posicion: "derecha",
    anillo: "interior"
  },
  "oriental-sur": {
    label: "Oriental Sur",
    capacidad: 500,
    precio: 65000,
    color: "#3b82f6",
    posicion: "derecha-inferior",
    anillo: "exterior"
  },
  "occidental-norte": {
    label: "Occidental Norte",
    capacidad: 500,
    precio: 65000,
    color: "#3b82f6",
    posicion: "izquierda-superior",
    anillo: "exterior"
  },
  "occidental-central": {
    label: "Occidental Central",
    capacidad: 400,
    precio: 120000,
    color: "#f59e0b",
    posicion: "izquierda",
    anillo: "interior"
  },
  "occidental-sur": {
    label: "Occidental Sur",
    capacidad: 500,
    precio: 65000,
    color: "#3b82f6",
    posicion: "izquierda-inferior",
    anillo: "exterior"
  },
  "palcos": {
    label: "Palcos",
    capacidad: 200,
    precio: 150000,
    color: "#ec4899",
    posicion: "centro-lateral",
    anillo: "interior"
  },
  "vip": {
    label: "VIP",
    capacidad: 100,
    precio: 250000,
    color: "#a855f7",
    posicion: "centro",
    anillo: "nucleo"
  }
};

function getHeatmapLevel(ocupacion) {
  if (ocupacion >= 1.0) return "agotado";
  if (ocupacion >= 0.85) return "casi-lleno";
  if (ocupacion >= 0.65) return "alta";
  if (ocupacion >= 0.35) return "media";
  return "disponible";
}

const heatmapColors = {
  "disponible": { fill: "var(--heatmap-green)", stroke: "var(--heatmap-green)" },
  "media":      { fill: "var(--heatmap-yellow)", stroke: "var(--heatmap-yellow)" },
  "alta":       { fill: "var(--heatmap-orange)", stroke: "var(--heatmap-orange)" },
  "casi-lleno": { fill: "var(--heatmap-red)", stroke: "var(--heatmap-red)" },
  "agotado":    { fill: "var(--heatmap-gray)", stroke: "var(--heatmap-gray)" }
};

function getZoneColor(zoneName, sold, total) {
  const level = getHeatmapLevel(total > 0 ? sold / total : 0);
  return heatmapColors[level];
}
