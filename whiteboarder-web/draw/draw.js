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
        cachedX = [],
        cachedY = [],
        cachedColor = [],
        cachedSize = [],
        cachedDrag = [],
        edits = [],
        paint = false,
        currentColor = colorBlack,
        currentTool = "marker",
        currentSize = brushNormal,

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
                    color.push(edit.color);
                    size.push(edit.brushSize);
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

                context.strokeStyle = color[i];
                context.lineCap = "round";
                context.lineJoin = "round";
                context.lineWidth = size[i];

                context.stroke();
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
                case colorGreen:
                    colorNum = 2;
                case colorRed:
                    colorNum = 3;
            }

            points = [];
            for (i = 0; i < cachedX.length; i++)
                points.push({ editID: -1, xcoord: cachedX[i], ycoord: cachedY[i] });

            edit = {
                editID: -1,
                wbID: websocketApp.getSessionID(),
                username: websocketApp.getUsername(),
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

                redraw();
                return;
            }

            // TODO: Figure out which edits cachedX and cachedY intersect, and loop
            // through them, passing each into websocketApp.handleEdit()
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

                paint = true;
                resetEditCache();
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
                if (currentTool === "eraser")
                    removeEdit(null);
                else
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
        init: init,
        addEdit: addEdit,
        removeEdit: removeEdit
    };
}());