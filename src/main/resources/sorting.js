function sortTable(tableId, numberOfHeaderRows, customDataName, sortType) {
	var table, rows, switching, customDataAttributeName, i, x, y, shouldSwitch, dir, switchcount = 0;
	table = document.getElementById(tableId);
	customDataAttributeName = "data-" + customDataName;
	if (sortType == "alphanumeric-internationalized") {
	    var language = localStorage.getItem("asapop-language");
    	if (language == null) {
      		customDataAttributeName = customDataAttributeName + "-en";
    	} else {
    		customDataAttributeName = customDataAttributeName + "-" + language;
    	}
    }
	switching = true;
	dir = "ascending";
	while (switching) {
		switching = false;
		rows = table.rows;
		for (i = numberOfHeaderRows; i < (rows.length - 1); i++) {
			shouldSwitch = false;
			x = rows[i].getAttribute(customDataAttributeName);           
			y = rows[i + 1].getAttribute(customDataAttributeName);
			if (sortType == "numeric") {
				x = parseInt(x);
				y = parseInt(y);
			}
			if (dir == "ascending") {
				if (x > y) {
					shouldSwitch= true;
					break;
				}
			} else if (dir == "descending") {
				if (x < y) {
					shouldSwitch= true;
					break;
				}
			}
		}
		if (shouldSwitch) {
			rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
			switching = true;
			switchcount ++;
		} else {
			if (switchcount == 0 && dir == "ascending") {
				dir = "descending";
				switching = true;
			}
		}
	}
}
