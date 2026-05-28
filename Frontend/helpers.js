function formatCurrency(amount) {
  return '$' + Number(amount).toLocaleString('es-CO');
}

function formatDisponibles(disponibles, total) {
  return disponibles + ' / ' + total + ' disponibles';
}
