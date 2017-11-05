
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

var routePrefix = "/whiteboarder";
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
            qrcode.makeCode(sessionID);
        } else {
            qrcode.clear();
        }
    };

    function loadSessionIDFromURL() {
        var parsedURL = new URL(window.location.href);
        console.log(parsedURL.searchParams.hash);
    }

    function switchToSession(newSessionID) {
        sessionID = newSessionID;
        updateQR();
        var parsedURL = new URL(window.location.href);
        parsedURL.hash = sessionID;
        window.location.href = parsedURL.href;
    }

    $("#create_session_btn").click(function() {
        if (!DEBUG_MODE) {
            $.ajax('http://localhost:8080/whiteboarder/session', {
                type: "POST",
                contentType: "application/json",
                dataType: 'text',
                success: switchToSession,
            });
        } else {
            switchToSession('new-session-id');
        }
    });
});

