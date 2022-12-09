function moveToArea(level) {
  var area = document.getElementById('area-selector').value;
  var path = '';
  for (let i = 0; i < level; i++) {
    path += '../';
  }
  window.location = path + area + "/index.html";
}
