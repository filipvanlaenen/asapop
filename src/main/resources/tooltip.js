function showPieChartTooltip(evt, label, dividend, divisor, percentage) {
  let tooltip = document.getElementById("pieChartTooltip");
  tooltip.style.display = "block";
  tooltip.style.left = evt.pageX + 10 + 'px';
  tooltip.style.top = evt.pageY + 10 + 'px';
  let tooltipLabel = document.getElementById("pieChartTooltipLabel");
  tooltipLabel.className = label;
  let tooltipDividend = document.getElementById("pieChartTooltipDividend");
  tooltipDividend.innerHTML = dividend;
  let tooltipDivisor = document.getElementById("pieChartTooltipDivisor");
  tooltipDivisor.innerHTML = divisor;
  let tooltipPercentage = document.getElementById("pieChartTooltipPercentage");
  tooltipPercentage.innerHTML = percentage;
  loadLanguage();
}
function hideTooltip(id) {
  var tooltip = document.getElementById(id);
  tooltip.style.display = "none";
}	
