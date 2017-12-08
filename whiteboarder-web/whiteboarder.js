
const routePrefix = "/whiteboarder";

// State contains all the mutable variables of the program.
// No state variables are allowed to be declared outside of this singleton.
var state = {
    refreshIteration: 0,
    sessionID: null,
    username: null,
    users: null,
    messages: []
};

// `callback` will be called with one parameter: the new sessionID.
function POST_Session(callback) {
    $.ajax(routePrefix + '/session', {
        type: "POST",
        contentType: "application/json",
        dataType: 'text',
        success: callback,
        timeout: 2000,
        error: function (msg) {
            console.log(msg);
            alert("error creating session: " + msg.statusText);
        }
    });
}

// Redirect the current browser page to the page for the given sessionID. Note
// that if the sessionID is equivalent to the current sessionID, the page will
// **not** be refreshed.
function navigateToURLForSession(sessionID) {
    var parsedURL = new URL(window.location.href);
    var currentURLSessionID = parsedURL.searchParams.get("sessionID");
    if (currentURLSessionID != sessionID) {
        if (sessionID) {
            parsedURL.searchParams.set("sessionID", sessionID);
        } else {
            parsedURL.searchParams.delete("sessionID");
        }
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

function refreshCurrentImage() {
    var sessionID = state.sessionID;
    if (!sessionID) return;
    var imgURL = routePrefix + '/image/' + sessionID + '#q=' + ++state.refreshIteration;
    console.log('reloading image from ' + imgURL);
    var img = $('<img />').attr({
        'src': imgURL,
        'id': 'image'
    });
    img.ready(function () {
        var oldCanvas = $('#canvas');
        $('#imagebox').empty().append(img);
        $('#imagebox').append(oldCanvas);
    });
}

function refreshUsersList() {
    if (!state.sessionID) return;
    var usersURL = routePrefix + '/users/' + state.sessionID + '#q=' + ++state.refreshIteration;
    console.log('reloading users from ' + usersURL);

    var redrawUsers = function () {
        $('#joined-members').empty();
        for (var i = 0; i < state.users.length; i++) {
            var user = state.users[i];
            $("#joined-members").append(
                $("<li />").addClass("list-group-item").addClass("list-group-item-success").text(user['username'])
            );
        }
    }

    $.getJSON(usersURL, function (data) {
        state.users = data;
        redrawUsers();
    });
}

function refreshMessagesList() {
    if (!state.sessionID) return;
    if (state.messages.length > 50) state.messages = state.messages.slice(state.messages.length - 50);

    $('#messagebox').empty();
    $("#message-input").empty();
    for (var i = 0; i < state.messages.length; i++) {
        var currentMsg = state.messages[i];
        var currentAuth = currentMsg.username;
        var currentMsgTime = currentMsg.timestamp;
        var messageElement = $("<li />").addClass("list-group-item");       
        $("#messagebox").append(
            (currentAuth == state.username) ? messageElement.addClass("list-group-item-success") : messageElement
        );
        messageElement.append(
            $("<p />").text(currentMsg.msg)
        );
        var messageStamps = $(messageElement).append($("<div />").addClass("msgmeta"));
        messageStamps.append(
            $("<span />").addClass("namestamp").text(currentAuth)
        );
        messageStamps.append(
            $("<span />").addClass("timestamp").text(currentMsgTime)
        );
    }
}

$(function () {
    state.sessionID = getSessionIDFromURL();

    if (state.sessionID) {
        // If we are connected to a session, show the connected section and hide
        // the welcome page.
        $("#section-welcome").hide();
        $("#section-connected").show();

        new QRCode(document.getElementById("qrcode"), {
            width: 140,
            height: 140
        }).makeCode(state.sessionID);

        state.username = window.prompt("Enter a username", "Anonymous");
        console.log(state.username);
        websocketApp.init();
        refreshCurrentImage();
        refreshUsersList();
    } else {
        $("#section-welcome").show();
        $("#section-connected").hide();
        websocketApp.close();
    }

    /**
     * All button triggers are set up here.
     */
    $("#navbar-title").click(function () { navigateToURLForSession(null); });
    $("#navbar-home-link").click(function () { navigateToURLForSession(null); });
    $("#session-id-button-navbar").click(function () {
        navigateToURLForSession($("#session-id-input-navbar").val());
    });
    $("#session-id-button-welcome").click(function () {
        navigateToURLForSession($("#session-id-input-welcome").val());
    });
    $("#create-new-session").click(function () {
        $("#create-new-session").prop("disabled", true);
        $("#create-new-session").text("Creating...");
        POST_Session(function (newSessionID) {
            navigateToURLForSession(newSessionID);
        });
    });

    var sendMessage = function () {
        var msgStr = $("#message-input").val();
        if (msgStr == null || msgStr.trim().length == 0) return;
        $('#message-input').val('');

        var m = {
            wbID: state.sessionID,
            messageID: -1,
            username: state.username,
            message: $("#message-input").val(),
            msg: msgStr.trim(),
            timestamp: null
        }
        websocketApp.handleMessage(m);
    }

    $("#message-submit").click(sendMessage);
    $('#message-input').on('keypress', function(e) {
        if (e.which == 13) {
            sendMessage();
        }
    });
});

// drawingApp contains all the logic of the "drawing" capability in the
// application, including the different colors and sizes of markers.
var drawingApp = (function () {
    var canvas,
        context,
        canvasWidth,
        canvasHeight,
        colorBlack = "#000000",
        colorBlue = "#0000ff",
        colorGreen = "#00c142",
        colorRed = "#ff0000",
        colorEraser = "rgba(0,0,0,1)",
        stashedColor = colorBlack,
        brushHuge = 20,
        brushLarge = 10,
        brushNormal = 5,
        brushSmall = 2,
        stashedBrush = brushNormal,
        cachedX = [],
        cachedY = [],
        cachedColor = [],
        cachedSize = [],
        cachedDrag = [],
        edits = [],
        paint = false,
        currentColor = colorBlack,
        currentSize = brushNormal,
        currentTool = "marker",

        redraw = function () {
            context.clearRect(0, 0, canvas.width, canvas.height);

            // Combine the edits from the server with any cached
            // "in progress" edits stored only on this client.
            var x = [], y = [], drag = [], color = [], size = [];
            for (i = 0; i < edits.length; i++) {
                var edit = edits[i];
                for (j = 0; j < edit.points.length; j++) {
                    var point = edit.points[j];
                    x.push(point.xcoord);
                    y.push(point.ycoord);
                    drag.push(j != 0);
                    size.push(edit.brushSize);

                    switch (edit.color) {
                        case 0:
                            color.push(colorBlack);
                            break;
                        case 1:
                            color.push(colorBlue);
                            break;
                        case 2:
                            color.push(colorGreen);
                            break;
                        case 3:
                            color.push(colorRed);
                            break;
                        case -1:
                            color.push(colorEraser);
                            break;
                    }
                }
            }
            x = x.concat(cachedX);
            y = y.concat(cachedY);
            drag = drag.concat(cachedDrag);
            color = color.concat(cachedColor);
            size = size.concat(cachedSize);

            // This `for` loop takes care of the actual drawing on the canvas.
            for (i = 0; i < x.length; i++) {
                context.beginPath();
                if (drag[i] && i > 0) {
                    context.moveTo(x[i - 1], y[i - 1]);
                } else {
                    context.moveTo(x[i] - 1, y[i]);
                }
                context.lineTo(x[i], y[i]);


                var oldGlobalCompOp = context.globalCompositeOperation;
                if (color[i] == colorEraser) { context.globalCompositeOperation = "destination-out"; }

                context.strokeStyle = color[i];
                context.lineCap = "round";
                context.lineJoin = "round";
                context.lineWidth = size[i];

                context.stroke();
                context.globalCompositeOperation = oldGlobalCompOp;
            }
            context.closePath();
            context.restore();
        },

        // Begin a new click.
        addClick = function (x, y, isDragging) {
            cachedX.push(x);
            cachedY.push(y);
            cachedColor.push(currentColor);
            cachedSize.push(currentSize);
            cachedDrag.push(isDragging);
        },

        addEdit = function (edit) {
            if (edit != null) {
                edits.push(edit);
                redraw();
                return;
            }

            var colorNum, edit, points;

            colorNum = 0;
            switch (currentColor) {
                case colorBlue:
                    colorNum = 1;
                    break;
                case colorGreen:
                    colorNum = 2;
                    break;
                case colorRed:
                    colorNum = 3;
                    break;
                case colorEraser:
                    colorNum = -1;
                    break;
            }

            points = [];
            for (i = 0; i < cachedX.length; i++)
                points.push({ editID: -1, xcoord: cachedX[i], ycoord: cachedY[i] });

            edit = {
                editID: -1,
                wbID: state.sessionID,
                username: state.username,
                color: colorNum,
                brushSize: currentSize,
                points: points,
                timestamp: null
            }

            websocketApp.handleEdit(edit, false);
        },

        // Clear the client-side cache of edits.
        resetEditCache = function () {
            cachedX = [];
            cachedY = [];
            cachedColor = [];
            cachedSize = [];
            cachedDrag = [];
        },

        // Add triggers to the HTML elements to ensure we are notified when the
        // user performs an action. This includes the different marker colors
        // and sizing buttons.
        setupListeners = function () {
            document.getElementById("marker").addEventListener("click", function () {
                if (currentTool != "marker") {
                    currentColor = stashedColor;
                    currentSize = stashedBrush;
                    currentTool = "marker";
                }
            });
            document.getElementById("eraser").addEventListener("click", function () {
                if (currentTool != "eraser") {
                    stashedColor = currentColor;
                    stashedBrush = currentSize;
                    currentColor = colorEraser;
                    currentSize = brushHuge;
                    currentTool = "eraser";
                }
            });
            document.getElementById("black").addEventListener("click", function () {
                currentColor = colorBlack;
                stashedColor = colorBlack;
            });
            document.getElementById("blue").addEventListener("click", function () {
                currentColor = colorBlue;
                stashedColor = colorBlue;
            });
            document.getElementById("green").addEventListener("click", function () {
                currentColor = colorGreen;
                stashedColor = colorGreen;
            });
            document.getElementById("red").addEventListener("click", function () {
                currentColor = colorRed;
                stashedColor = colorRed;
            });
            document.getElementById("huge").addEventListener("click", function () {
                if (currentTool != "eraser") {
                    currentSize = brushHuge;
                    stashedBrush = brushHuge;
                }
            });
            document.getElementById("large").addEventListener("click", function () {
                if (currentTool != "eraser") {
                    currentSize = brushLarge;
                    stashedBrush = brushLarge;
                }
            });
            document.getElementById("normal").addEventListener("click", function () {
                if (currentTool != "eraser") {
                    currentSize = brushNormal;
                    stashedBrush = brushNormal;
                }
            });
            document.getElementById("small").addEventListener("click", function () {
                if (currentTool != "eraser") {
                    currentSize = brushSmall;
                    stashedBrush = brushSmall;
                }
            });

            var press = function (e) {
                var offsetLeft = this.offsetLeft + this.parentElement.offsetLeft + this.parentElement.parentElement.offsetLeft + this.parentElement.parentElement.parentElement.offsetLeft,
                    offsetTop = this.offsetTop + this.parentElement.offsetTop + this.parentElement.parentElement.offsetTop + this.parentElement.parentElement.parentElement.offsetTop,
                    mouseX = e.pageX - offsetLeft,
                    mouseY = e.pageY - offsetTop;

                paint = true;
                resetEditCache();
                addClick(mouseX, mouseY, false);
                redraw();
            };
            var drag = function (e) {
                var offsetLeft = this.offsetLeft + this.parentElement.offsetLeft + this.parentElement.parentElement.offsetLeft + this.parentElement.parentElement.parentElement.offsetLeft,
                    offsetTop = this.offsetTop + this.parentElement.offsetTop + this.parentElement.parentElement.offsetTop + this.parentElement.parentElement.parentElement.offsetTop,
                    mouseX = e.pageX - offsetLeft,
                    mouseY = e.pageY - offsetTop;

                if (paint) {
                    addClick(mouseX, mouseY, true);
                    redraw();
                }
            };
            var release = function () {
                paint = false;
                addEdit(null);
                redraw();
            };
            var cancel = function () {
                paint = false;
            };

            canvas.addEventListener("mousedown", press, false);
            canvas.addEventListener("mousemove", drag, false);
            canvas.addEventListener("mouseup", release);
            canvas.addEventListener("mouseout", cancel, false);
        },

        // Begin the program. Note that this function calls `setupListeners()`.
        init = function () {
            canvasWidth = $('#imagebox').width();
            canvasHeight = $('#imagebox').height();
            canvas = document.getElementById('canvas');

            canvas.setAttribute('width', canvasWidth);
            canvas.setAttribute('height', canvasHeight);
            canvas.setAttribute('id', 'canvas');
            canvas.style.border = "2px dashed black";
            canvas.style.position = "absolute";

            if (typeof G_vmlCanvasManager !== "undefined") {
                canvas = G_vmlCanvasManager.initElement(canvas);
            }
            context = canvas.getContext("2d");

            // Retrieve existing edits from the server
            var editsURL = routePrefix + '/session/' + state.sessionID + '#q=' + ++state.refreshIteration;
            $.getJSON(editsURL, function (data) {
                edits = data['edits'];
                redraw();
            });

            setupListeners();
        };

    return {
        init: init,
        addEdit: addEdit
    };
}());


// The websocketApp encapsulates the entirety of the websocket logic, making it
// easier to code the rest of the application over this abstraction.
var websocketApp = (function () {
    var socket,
        url,

        handleEdit = function (edit, isRemove) {
            if (state.sessionID == null || state.username == null)
                return;

            h = { edit: edit, isRemove: isRemove };
            socket.send(JSON.stringify(h));
        },

        handleMessage = function (message) {
            if (state.sessionID == null || state.username == null) return;

            socket.send("message:" + JSON.stringify(message));
        },

        setupEventHandlers = function () {
            socket.onopen = function (event) {
                socket.send("login:" + state.sessionID + "," + state.username);
                drawingApp.init();
            };
            socket.onerror = function (error) {
                alert('WebSocket Error: ' + JSON.stringify(error));
            };
            socket.onmessage = function (event) {
                var message = event.data;
                if (message.startsWith('refreshImage')) {
                    if (message.substring(message.indexOf(':') + 1) == state.sessionID) { refreshCurrentImage(); }
                    return;
                }

                if (message.startsWith('refreshUsers')) {
                    if (message.substring(message.indexOf(':') + 1) == state.sessionID) { refreshUsersList(); }
                    return;
                }

                if (message.startsWith('message')) {
                    var msg = JSON.parse(message.substring(8));
                    if (msg.wbID == state.sessionID) {
                        state.messages.push(msg);
                        refreshMessagesList();
                    }
                    return;
                }

                var h = JSON.parse(message);
                if (h.edit.wbID == state.sessionID)
                    drawingApp.addEdit(h.edit);
            };
            socket.onclose = function (event) {
                // There's not much we can do with an onclose event, so we
                // essentially wait for the user to refresh. This shouldn't
                // occur unless there are serious connectivity issues.
            };
        },

        init = function () {
            href = window.location.href;
            url = href.replace(window.location.protocol, 'ws:').replace(href.substring(href.indexOf('?')), 'ws/session');
            socket = new WebSocket(url);
            setupEventHandlers();
        },

        close = function () {
            if (socket != null)
                socket.close();
        };

    return {
        init: init,
        close: close,
        handleEdit: handleEdit,
        handleMessage: handleMessage
    };
}());

