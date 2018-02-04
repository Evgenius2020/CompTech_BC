var sendButton = document.getElementById ("button-form-submit");
var exampleInputFile = document.getElementById ("exampleInputFile");
var exampleInputPassword = document.getElementById ("exampleInputPassword");

var xhr = new XMLHttpRequest();

xhr.onload = function (e) {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                alert(xhr.responseText);
                document.location = ("index.html");
            } else {
                alert(xhr.statusText);
            }
        }
    }
;
xhr.onerror = function (e) {
    console.error(xhr.statusText);
};

sendButton.onclick = function() {
    var body = {};
    body.walletFile = JSON.parse(exampleInputFile.value);
    body.password = exampleInputPassword.value;
    xhr.open("POST", "/rest/login", true);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send(JSON.stringify(body));
};
