
const routePrefix = "/whiteboarder";

// `callback` will be called with one parameter: the new sessionID.
function POST_Session(callback) {
    $.ajax(routePrefix + '/session', {
        type: "POST",
        contentType: "application/json",
        dataType: 'text',
        success: callback,
        timeout: 2000,
        error: function(msg) {
            console.log(msg);
            alert("error creating session: " + msg.statusText);
        }
    });
}

function navigateToURLForSession(sessionID) {
    var parsedURL = new URL(window.location.href);
    var currentURLSessionID = parsedURL.searchParams.get("sessionID");
    if (currentURLSessionID != sessionID) {
        var newParams = new URLSearchParams();
        newParams.set("searchParams", sessionID);
        parsedURL.searchParams.set("sessionID", sessionID);
        parsedURL.searchParams = newParams;
        console.log(parsedURL.searchParams.get("sessionID"));
        console.log(parsedURL.href);
        window.location.href = parsedURL.href;
        console.log("udapted href");
    }
}

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

$(function() {
    // Put all mutable variables in this object.
    var state = {
        sessionID: null,
    };

    state.sessionID = getSessionIDFromURL();

    if (state.sessionID) {
        // If we are connected to a session
        $("#section-welcome").hide();
        $("#section-connected").show();

        new QRCode(document.getElementById("qrcode"), {
            width: 100,
            height: 100
        }).makeCode(state.sessionID);
    } else {
        $("#section-welcome").show();
        $("#section-connected").hide();
    }

    // set up button triggers
    $("#session-id-button-navbar").click(function() {
        navigateToURLForSession($("#session-id-input-navbar").val());
    });
    $("#session-id-button-welcome").click(function() {
        navigateToURLForSession($("#session-id-input-welcome").val());
    });

    $("#create-new-session").click(function() {
        $("#create-new-session").prop("disabled", true);
        $("#create-new-session").text("Creating...");
        POST_Session(function(newSessionID) {
            navigateToURLForSession(newSessionID);
        });
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
});

