/**
 * fractal.js --- Holds Javascript code for updating an image element (w/ id "fractalImage")
 * 				  so that it doesn't cache while a fractal is being generated that will
 * 				  be output to fractalImage_src upon completion.
 * @author Scott Wolfskill
 * @created     02/25/2019
 * @last_edit   02/25/2019
 */

var fractalImage_src;

/**
 * Set fractalImage_src, and set update() to be called every 200ms, 
 * with 1st call occurring immediately.
 */
function start() {
	var image = document.getElementById("fractalImage");
	fractalImage_src = image.getAttribute("src");
	setInterval(update, 200); // call update once every 200ms
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
		//Check if there is new content to load.
		var image = document.getElementById("fractalImage");
		image.setAttribute("src", fractalImage_src + "?	"
				+ new Date().getTime());
		getLoadingMessage();
	}
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
 * Output current time to the console.
 */
function printCurrentTime() {
	var now = new Date();
	console.log(now.getHours() + ":" + now.getMinutes() + ":"
			+ now.getSeconds() + ":" + now.getMilliseconds()
			+ "   (hh:mm:ss:xxx)");
}