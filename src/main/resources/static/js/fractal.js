/**
 * fractal.js --- Holds Javascript code for updating an image element (w/ id "fractalImage")
 * 				  so that it doesn't cache while a fractal is being generated that will
 * 				  be output to fractalImage_src upon completion.
 * @author Scott Wolfskill
 * @created     02/25/2019
 * @last_edit   02/26/2019
 */

var fractalImage_src;

/**
 * Set fractalImage_src, and set update() to be called every 200ms, 
 * with 1st call occurring immediately.
 */
function start() {
	var image = document.getElementById("fractalImage");
	fractalImage_src = stripQueryString(image.getAttribute("src"));
	setInterval(update, 200); // call update once every 200ms
	/* If want to set custom image source as URI parameter, uncomment these lines:
	var inputSrc = document.createElement("input");
	inputSrc.setAttribute("type", "hidden");
	inputSrc.setAttribute("id", "inputSrc")
	inputSrc.setAttribute("name", "src");
	var frmParams = document.getElementById("frmParams");
	frmParams.appendChild(inputSrc);
	*/
	update();
}

/**
 * If the fractal is being generated, set the fractal image src to prevent
 * caching until it is done generating.
 */
function update() {
	var loadingMessage = document.getElementById("loadingMessage").textContent;
	if (loadingMessage == "Generating...") {
		console.log("Querying server for generation status...");
		//Make sure browser keeps checking if there is new content to load.
		setImageSrc_noCache();
		getLoadingMessage();
	}
}

/**
 * Sets element "fractalImage" to not cache while it is being generated on the server.
 */
function setImageSrc_noCache() {
	var newSrc = fractalImage_src + "?" + new Date().getTime();
	var image = document.getElementById("fractalImage");
	image.setAttribute("src", newSrc);
	/* If want to set custom image source as URI parameter, uncomment these lines:
	var inputSrc = document.getElementById("inputSrc");
	inputSrc.setAttribute("value", newSrc);
	*/
}

/**
 * Asynchronously gets the value of the page model's attribute loadingMessage w/ AJAX,
 * and sets doc element loadingMessage's text with its value.
 */
function getLoadingMessage() {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var loadingMessageElement = document
					.getElementById("loadingMessage");
			loadingMessageElement.innerHTML = this.responseText;
		}
	}
	xhttp.open("GET", "/get-loading-message", true);
	xhttp.send();
}

/**
 * Remove a query string from input, if present.
 * e.g. if input="images/fractal-tree.png?112334", return "images/fractal-tree.png"
 * @param input String to remove the query string from, if present.
 * @returns input with query string removed from the end.
 */
function stripQueryString(input) {
	var chars = input.split("");
	var start = 0;
	var end = chars.length;
	for(var i = end - 1; i >= 0; i--) {
		if(chars[i] == '?') { 
			// Don't include query part of filename (if present)
			// e.g. exclude "?112334" in "images/fractal-tree.png?112334"
			end = i;
			break;
		}
	}
	return input.substring(start, end);
}

/**
 * Output current time to the console.
 */
function printCurrentTime() {
	var now = new Date();
	console.log(now.getHours() + ":" + now.getMinutes() + ":"
			+ now.getSeconds() + ":" + now.getMilliseconds()
			+ "   (hh:mm:ss:xxx)");
}