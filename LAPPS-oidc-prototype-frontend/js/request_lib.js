/**
* Example Request Library for LAPPS Frontend
*/
function OpenIdRequestLibrary(endpointUrl){
		
	this._serviceEndpoint = endpointUrl;
	
	//Fetch the OIDC tokens and print them to the console, trigger an alert in case of problems
	if(localStorage.getItem("access_token") !== null && localStorage.getItem("id_token") !== null){
		this._access_token = localStorage.getItem("access_token");
		this._id_token = localStorage.getItem("id_token");
		console.log("Access Token:");
		console.log(this._access_token);
		console.log("ID Token:");
		console.log(this._id_token);
	}
	else{
		alert("No token provided, script will not work!");
	}
};


/**
* Sends a request to the BASE_SERVICE_ENDPOINT.
* @param relativePath the (relative) uri to call
* @param method the method to be called on the uri (get, post, put, delete)
* @param content the content of the request to send
* @param callback Callback function, called when the result has been retrieved
*/
OpenIdRequestLibrary.prototype.sendRequest = function(relativePath, method, content, callback){
	var requestURI = encodeURI(this._serviceEndpoint + "/" + relativePath);
	var ajaxObj = {
		url: requestURI,
		
		type: method.toUpperCase(),
		data: content,
		contentType: "text/plain; charset=UTF-8",
		crossDomain: true,
		headers: {'access_token': this._access_token},
		
		error: function (xhr, errorType, error) {
			var errorText = error;
			if (xhr.responseText != null && xhr.responseText.trim().length > 0)
				errorText = xhr.responseText;
			if (xhr.status == 0) {
				errorText = "Server does not respond..";
			}
			
			callback();
		},
		success: function (data, status, xhr) {
			var type = xhr.getResponseHeader("content-type");
			callback(xhr.responseText, type);
		},
	};
	
	$.ajax(ajaxObj);
};
