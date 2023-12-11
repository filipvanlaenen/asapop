function showPieChartTooltip(evt, label, numerator, denominator, percentage) {
  let tooltip = document.getElementById("pieChartTooltip");
  tooltip.style.display = "block";
  tooltip.style.left = evt.pageX + 10 + 'px';
  tooltip.style.top = evt.pageY + 10 + 'px';
  let tooltipLabel = document.getElementById("pieChartTooltipLabel");
  tooltipLabel.className = label;
  let tooltipNumerator = document.getElementById("pieChartTooltipNumerator");
  tooltipNumerator.innerHTML = numerator;
  let tooltipDenominator = document.getElementById("pieChartTooltipDenominator");
  tooltipDenominator.innerHTML = denominator;
  let tooltipPercentage = document.getElementById("pieChartTooltipPercentage");
  tooltipPercentage.innerHTML = percentage;
  loadLanguage();
}
function hideTooltip(id) {
  var tooltip = document.getElementById(id);
  tooltip.style.display = "none";
}	
