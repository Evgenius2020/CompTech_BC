var title = document.getElementById ("exampleInputText0");
var description = document.getElementById ("exampleInputText1");
var options = document.getElementById ("exampleTextarea2");
var begin = dateFormat(new Date(), 'W3C');
var end = dateFormat(new Date(), 'W3C');

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
    body.title = exampleInputText0.value;
    body.description = exampleInputText1.value;
    body.options = exampleTextarea2.value;
    body.begin = begin;
    body.end = end;
    xhr.open("POST", "/rest/votings", true);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send(JSON.stringify(body));
};
