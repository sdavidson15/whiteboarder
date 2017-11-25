
const debugMode = false;
const routePrefix = "http://localhost:8080/whiteboarder";

// `callback` will be called with one parameter: the new sessionID.
function POST_Session(callback) {
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
}

function updateURLIfNecessary(sessionID) {
    var parsedURL = new URL(window.location.href);
    var currentURLSessionID = parsedURL.searchParams.get("sessionID");
    if (currentURLSessionID != sessionID) {
        parsedURL.searchParams.set("sessionID", sessionID);
        window.location.href = parsedURL.href;
    }
}

$(function() {
    // Put all mutable variables in this object.
    var state = {
        sessionID:  null,
        qrcode:     null,
    };

    state.sessionID = getSessionIDFromURL();
    updateURLIfNecessary(state.sessionID);

    function updateQR() {
        if (!state.qrcode) {
            state.qrcode = new QRCode(document.getElementById("qrcode"), {
                width: 100,
                height: 100
            });
        }
        if (sessionID) {
            console.log('making QR from sessionID ' + sessionID);
            state.qrcode.makeCode(sessionID);
        } else {
            state.qrcode.clear();
        }
    };

    function getSessionIDFromURL() {
        var parsedURL = new URL(window.location.href);
        var query = parsedURL.searchParams.get("sessionID");
        console.log(query);
        if (query) {
            console.log("parsed sessionID from url: " + query);
            return query;
        }
        return null;
    }

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
});

