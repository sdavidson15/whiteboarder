
const routePrefix = "/whiteboarder";

// Put all mutable variables in this object.
var state = {
    refreshIteration: 0,
    sessionID: null,
    username: null,
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

$(function () {
    state.sessionID = getSessionIDFromURL();

    if (state.sessionID) {
        // If we are connected to a session
        $("#section-welcome").hide();
        $("#section-connected").show();

        new QRCode(document.getElementById("qrcode"), {
            width: 140,
            height: 140
        }).makeCode(state.sessionID);

        state.username = window.prompt("Enter a username", "Anonymous");
        console.log(state.username);
        websocketApp.init();
    } else {
        $("#section-welcome").show();
        $("#section-connected").hide();
        websocketApp.close();
    }

    // set up button triggers
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

    refreshCurrentImage();

    /*
    var joinedNames = [];

    function clearNameList() {
        $("#joined-members").empty();
    }

    function addName(name) {
        $("#joined-members").append(
            $("<li />").addClass("list-group-item").addClass("list-group-item-success").text(name)
        );
    }

    clearNameList();
    for (let l : joinedNames) {
        addName(l);
    }
    */
});

// ------------------------------------------- Drawing App ------------------------------------------- //

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

            // This for loop takes care of the actual drawing
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

        resetEditCache = function () {
            cachedX = [];
            cachedY = [];
            cachedColor = [];
            cachedSize = [];
            cachedDrag = [];
        },

        setupListeners = function () {
            document.getElementById("marker").addEventListener("click", function () {
                // TODO: Unlock the other options
                if (currentTool != "marker") {
                    currentColor = stashedColor;
                    currentSize = stashedBrush;
                    currentTool = "marker";
                }
            });
            document.getElementById("eraser").addEventListener("click", function () {
                // TODO: Lock the other options
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

        init = function () {
            canvasWidth = $('#imagebox').width();
            canvasHeight = $('#imagebox').height();
            canvas = document.getElementById('canvas');

            // TODO: Move to .css
            canvas.setAttribute('width', canvasWidth);
            canvas.setAttribute('height', canvasHeight);
            canvas.setAttribute('id', 'canvas');
            canvas.style.border = "2px dashed black";
            canvas.style.position = "absolute";

            if (typeof G_vmlCanvasManager !== "undefined") {
                canvas = G_vmlCanvasManager.initElement(canvas);
            }
            context = canvas.getContext("2d");

            redraw();
            setupListeners();
        };

    return {
        init: init,
        addEdit: addEdit
    };
}());

// ------------------------------------------- Websocket App ------------------------------------------- //

var websocketApp = (function () {
    var socket,
        url,

        handleEdit = function (edit, isRemove) {
            if (state.sessionID == null || state.username == null)
                return;

            h = { edit: edit, isRemove: isRemove };
            socket.send(JSON.stringify(h));
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

                var h = JSON.parse(message);
                if (h.edit.wbID == state.sessionID)
                    drawingApp.addEdit(h.edit);
            };
            socket.onclose = function (event) {
                // drawingApp = null;
                // FIXME: Find a better way to close the drawing app.
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
        handleEdit: handleEdit
    };
}());
