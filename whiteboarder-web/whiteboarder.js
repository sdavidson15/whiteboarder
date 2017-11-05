
// parse id from cookie string
function getCookie(cookieName) {
        var name = cookieName + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
                var c = ca[i];
                while (c.charAt(0) == ' ') {
                        c = c.substring(1);
                }
                if (c.indexOf(name) == 0) {
                        return c.substring(name.length, c.length);
                }
        }
        return "";
}

////////////////////////////////////////////////////////////////////////////////

var DEBUG_MODE = true;

var routePrefix = "http://localhost:8080/whiteboarder";
var sessionID = null;
var id = getCookie("wb_session_id");
var qrcode = null;

$(function() {
    loadSessionIDFromURL();
    switchToSession(sessionID);

    function updateQR() {
        if (!qrcode) {
            qrcode = new QRCode(document.getElementById("qrcode"), {
                width: 100,
                height: 100
            });
        }
        if (sessionID) {
            console.log('making QR from sessionID ' + sessionID);
            qrcode.makeCode(sessionID);
        } else {
            qrcode.clear();
        }
    };

    function loadSessionIDFromURL() {
        var parsedURL = new URL(window.location.href);
        var hash = parsedURL.hash;
        if (hash) {
            // remove # symbol from beginning of hash
            sessionID = hash.substring(1);
            console.log("loaded sessionID from hash: " + sessionID);
        }
    }

    function switchToSession(newSessionID) {
        sessionID = newSessionID;
        updateQR();
        var parsedURL = new URL(window.location.href);
        parsedURL.hash = sessionID;
        window.location.href = parsedURL.href;
        refreshCurrentImage();
    }

    $("#create_session_btn").click(function() {
        if (!DEBUG_MODE) {
            $.ajax(routePrefix + '/session', {
                type: "POST",
                contentType: "application/json",
                dataType: 'text',
                success: switchToSession,
            });
        } else {
            switchToSession('new-session-id');
        }
    });

    var refreshIteration = 0;
    function refreshCurrentImage() {
        if (!sessionID) return;
        refreshIteration = refreshIteration + 1;
        var imgURL = routePrefix + '/image/' + sessionID + '#q=' + refreshIteration;
        console.log("reloading image from " + imgURL);
        var img = $("<img />").attr("src", imgURL);
        img.css('transform', 'rotate(90deg)');
        img.ready(function() {
            $('#imagebox').empty().append(img);
        });
    }

    // refresh the current session image every 5 seconds
    setInterval(refreshCurrentImage, 5000);
});

