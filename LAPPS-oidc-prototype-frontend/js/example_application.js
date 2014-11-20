
/**
* LAPPS openID connect prototype
* 
*/
var SERVICE_ENDPOINT_URI = "http://127.0.0.1:8080/lapps" //Please adjust this variable
var myOpenIdRequestLibrary;

//The two perspectives of the client.
var loginNode				= document.getElementById("login"),
	mainViewNode			= document.getElementById("mainView");
	welcomeMessageNode 		= document.getElementById("welcomeMessage");
	exemplaryMethodsNode 	= document.getElementById("exemplaryMethods");

/**
* Shows the login perspective.
*/
var show_login_perspective = function(){
	$(loginNode).show();
	$(mainViewNode).hide();
	
	//Load login button (and its logic) asynchronously, since it can take some time to connect with server
	var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
	po.src = './js/oidc_button.js';
	var s = document.getElementsByTagName('script')[0];
	s.parentNode.insertBefore(po, s);
	
};


/**
* Part of the OpenID Connect login process, name is defined in "index.html" and function is called from "oidc_button.js".
*/
function signinCallback(result) {
	
	//Successful login triggers perspective switch after 3 seconds
	if(result === "success"){	
		$("#status").html("Hello, " + oidc_userinfo.name + "!<br>You are being redirected..");
		loginRefresh = self.setInterval(function(){show_main_perspective()},3000);
	
	//Else, nothing happens
	} else {
		console.log("not signed in...");
		console.log(result);
		$("#status").html("Please log in:-)");
	}
}


/**
* Shows the main perspective (where users can send requests).
*/
var show_main_perspective = function(){
	window.clearInterval(loginRefresh);
	$(loginNode).hide();
	$(mainViewNode).show();
	
	myOpenIdRequestLibrary	= new OpenIdRequestLibrary(SERVICE_ENDPOINT_URI);
	myOpenIdRequestLibrary.sendRequest("users/", "get", null, function(result){
		welcomeMessageNode.innerHTML = "<p> All user ids:<br>" + result + "</p>"
	});
};


//show login perspective
show_login_perspective();