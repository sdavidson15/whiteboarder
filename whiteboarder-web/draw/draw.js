var drawingApp = (function () {
    var canvas,
        context,
        canvasWidth = 900,
        canvasHeight = 540,
        colorBlack = "#000000",
        colorBlue = "#0000ff",
        colorGreen = "#00c142",
        colorRed = "#ff0000",
        brushHuge = 20,
        brushLarge = 10,
        brushNormal = 5,
        brushSmall = 2,
        mousedownIndex = 0,
        clickX = [],
        clickY = [],
        clickColor = [],
        clickTool = [],
        clickSize = [],
        clickDrag = [],
        edits = [],
        paint = false,
        currentColor = colorBlack,
        currentTool = "marker",
        currentSize = brushNormal,

        redraw = function () {
            // FIXME: Redraw from edits. Then you can get rid of clickColor, clickTools, and clickSize.
            // Also, you can get rid of mousedownIndex and reset clickX, clickY, and clickDrag after each stroke.
            context.clearRect(0, 0, canvas.width, canvas.height);

            for (i = 0; i < clickX.length; i += 1) {
                context.beginPath();
                if (clickDrag[i] && i > 0) {
                    context.moveTo(clickX[i - 1], clickY[i - 1]);
                } else {
                    context.moveTo(clickX[i] - 1, clickY[i]);
                }
                context.lineTo(clickX[i], clickY[i]);

                context.strokeStyle = (clickTool[i] === "eraser") ? 'white' : clickColor[i];
                context.lineCap = "round";
                context.lineJoin = "round";
                context.lineWidth = clickSize[i];

                context.stroke();
            }
            context.closePath();
            context.restore();
        },

        addClick = function (x, y, dragging) {
            clickX.push(x);
            clickY.push(y);
            clickTool.push(currentTool);
            clickColor.push(currentColor);
            clickSize.push(currentSize);
            clickDrag.push(dragging);
        },

        addEdit = function (edit) {
            if (edit != null) {
                edits.push(edit);
                return;
            }

            var colorNum, edit, points = [];

            colorNum = 0;
            switch (currentColor) {
                case colorBlue:
                    colorNum = 1;
                case colorGreen:
                    colorNum = 2;
                case colorRed:
                    colorNum = 3;
            }

            points = [];
            for (i = mousedownIndex; i < clickX.length; i++)
                points.push({ x: clickX[i], y: clickY[i] });

            edit = {
                editID: -1,
                wbID: websocketApp.sessionID,
                username: websocketApp.username,
                color: colorNum,
                brushSize: currentSize,
                points: points,
                timestamp: null
            }

            websocketApp.handleEdit(edit, false);
        },

        removeEdit = function (edit) {
            if (edit != null) {
                var index = edits.indexOf(edit);
                if (index > -1)
                    edits.splice(index, 1);

                return;
            }

            // TODO: Figure out which edits clickX and clickY intersect, and loop
            // through them, passing each into websocketApp.handleEdit()
        },

        setupListeners = function () {
            document.getElementById("marker").addEventListener("click", function () {
                currentTool = "marker";
            });
            document.getElementById("eraser").addEventListener("click", function () {
                currentTool = "eraser"
            });
            document.getElementById("black").addEventListener("click", function () {
                currentColor = colorBlack;
            });
            document.getElementById("blue").addEventListener("click", function () {
                currentColor = colorBlue;
            });
            document.getElementById("green").addEventListener("click", function () {
                currentColor = colorGreen;
            });
            document.getElementById("red").addEventListener("click", function () {
                currentColor = colorRed;
            });
            document.getElementById("huge").addEventListener("click", function () {
                currentSize = brushHuge;
            });
            document.getElementById("large").addEventListener("click", function () {
                currentSize = brushLarge;
            });
            document.getElementById("normal").addEventListener("click", function () {
                currentSize = brushNormal;
            });
            document.getElementById("small").addEventListener("click", function () {
                currentSize = brushSmall;
            });

            var press = function (e) {
                var mouseX = e.pageX - this.offsetLeft,
                    mouseY = e.pageY - this.offsetTop;

                mousedownIndex = clickX.length;
                paint = true;
                addClick(mouseX, mouseY, false);
                redraw();
            };
            var drag = function (e) {
                var mouseX = e.pageX - this.offsetLeft,
                    mouseY = e.pageY - this.offsetTop;

                if (paint) {
                    addClick(mouseX, mouseY, true);
                    redraw();
                }
            };
            var release = function () {
                paint = false;
                addEdit();
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
            canvas = document.getElementById('canvas');
            canvas.setAttribute('width', canvasWidth);
            canvas.setAttribute('height', canvasHeight);
            canvas.setAttribute('id', 'canvas');
            canvas.style.border = "1px solid black";
            canvas.style.position = "absolute";
            if (typeof G_vmlCanvasManager !== "undefined") {
                canvas = G_vmlCanvasManager.initElement(canvas);
            }
            context = canvas.getContext("2d");

            redraw();
            setupListeners();
        };

    return {
        init: init
    };
}());

var websocketApp = (function () {
    var socket,
        url,
        username,
        sessionID,
        nameBox,
        nameBtn,

        handleEdit = function (edit, isRemove) {
            if (sessionID == null || username == null)
                return;

            h = { edit: edit, isRemove: isRemove };
            websocketApp.socket.send(JSON.stringify(h));
        },

        login = function () {
            username = nameBox.value;
            if (sessionID == null) {
                // TODO: parse url for session id
                sessionID = "test-session";
                socket.send("login:" + sessionID + "," + username);
                nameBtn.value = "Change name";
            } else {
                // TODO: Send http user rename request
            }
        },

        setupEventHandlers = function () {
            socket.onopen = function (event) {
                drawingApp.init();
            };
            socket.onerror = function (error) {
                alert('WebSocket Error: ' + JSON.stringify(error));
            };
            socket.onmessage = function (event) {
                var message = event.data;
                // TODO: Deserialize the message into a handle edit
                var h = { edit: null, isRemove: false };
                if (h.isRemove) {
                    drawingApp.removeEdit(h.edit);
                    return;
                }
                drawingApp.addEdit(h.edit);
            };
            socket.onclose = function (event) {
                drawingApp = null;
                nameBtn.value = "Join";
            };

            document.getElementById("nameForm").onsubmit = function () {
                login();
            };
        },

        init = function () {
            url = 'ws://' + window.location.hostname + '/ws/session';
            socket = new WebSocket(url);
            nameBox = document.getElementById("nameBox");
            nameBtn = document.getElementById("nameBtn");
            setupEventHandlers();
        };

    return {
        init: init
    };
}());