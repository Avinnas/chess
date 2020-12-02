/*<![CDATA[*/
function drawBoard(x, y) {
    var chessBoard = document.getElementById("chessBoard");
    for (var i = 0; i < 64; i++) {
        var tile = document.createElement('div');
        tile.id = "tile-" + i.toString();
        tile.className = "tile"

        if (Math.floor((i / 8)) % 2 === 0) {
            if (i % 2 === 0) {
                tile.classList.add("black");
            }
        } else {
            if (i % 2 === 1) {
                tile.classList.add("black");
            }
        }

        chessBoard.appendChild(tile);
    }
}

function drawPieces(tiles) {
    removeAllImages();
    for (const tileId in tiles) {
        let element = document.getElementById("tile-" + tileId)
        element.innerHTML = ''
        let imageName = ''
        if (tiles[tileId].color === "BLACK") {
            imageName = '/assets/' + tiles[tileId].name + 'b' + '.png'
        } else {
            imageName = '/assets/' + tiles[tileId].name + '.png'
        }
        const image = document.createElement("img")
        element.appendChild(image);
        image.src = imageName;

    }
}

function handleReset() {
    let elements = document.getElementsByClassName("marker");
    while (elements.length > 0) {
        recreateElement(elements[0].parentNode);
        elements[0].parentNode.addEventListener("click", handleReset);
        elements[0].parentNode.removeChild(elements[0]);
    }
}

function handlePieceClick(id, pieceMoves) {
    handleReset();
    for (const tileToMoveId of pieceMoves) {
        let parentSpan = document.getElementById("tile-" + tileToMoveId)
        parentSpan = recreateElement(parentSpan)
        let div = document.createElement("div");
        div.className = "marker"
        parentSpan.addEventListener("click", () => handleMove(id, tileToMoveId))
        parentSpan.appendChild(div);
    }

}

function recreateElement(element) {
    var new_element = element.cloneNode(true);
    element.parentNode.replaceChild(new_element, element);
    return new_element;
}

function recreateAll() {
    for (let i = 0; i < 64; i++) {
        let element = document.getElementById("tile-" + i);
        recreateElement(element);
        element.addEventListener("click", handleReset)
    }
}

function removeAllImages() {
    for (let i = 0; i < 64; i++) {
        let element = document.getElementById("tile-" + i);
        element.innerHTML = ''
    }
}

function markLastMove(startId, destinationId) {
    let previousMove = document.getElementsByClassName("lastMoveStart");
    if(previousMove.length>0){
        previousMove[0].classList.remove("lastMoveStart")
    }

    previousMove = document.getElementsByClassName("lastMoveEnd");
    if(previousMove.length>0) {
        previousMove[0].classList.remove("lastMoveEnd")
    }


    let element = document.getElementById("tile-"+ startId);
    console.log(startId);
    console.log(destinationId);
    element.classList.add("lastMoveStart");
    let element2 = document.getElementById("tile-"+ destinationId);
    element2.classList.add("lastMoveEnd");

}

async function handleMove(pieceId, destinationId) {
    handleReset()
    recreateAll()
    addResetEvent();

    await sendMove(pieceId, destinationId);
    drawPieces(await getCurrentState())
    markLastMove(pieceId, destinationId);

    if (await checkFinished()) {
        document.getElementById("result").innerText = "GAME FINISHED"
        return false;
    }

    const move = await makeAIMove();
    console.log(move)
    drawPieces(await getCurrentState())
    markLastMove(move.startTile, move.destinationTile);

    await addMoveEventToPieces();
    if (await checkFinished()) {
        document.getElementById("result").innerText = "GAME FINISHED"
        return false;
    }

    // alert(" MOVE HANDLE " + pieceId + " -> " + destinationId);


}

async function makeAIMove() {
    return await getLastAIMove()

}

async function getLastAIMove() {
    let lastMove;

    await $.ajax({
        url: "/game/last_move",
        success: function (data) {
            lastMove = data;
        }
    })

    return await lastMove
}

async function sendMove(pieceId, destinationId) {
    var move = {
        startTile: pieceId,
        destinationTile: destinationId
    }
    await $.ajax({
        type: "POST",
        url: "/game",
        data: JSON.stringify(move),
        contentType: "application/json",
        success: function () {
            // alert("data sent")
        }

    })

}

function movePieceAtInterface(pieceId, destinationId) {
    // alert(pieceId + " " + destinationId)
    let startElement = document.getElementById("tile-" + pieceId);
    let text = startElement.innerHTML;
    startElement.innerHTML = ''
    document.getElementById("tile-" + destinationId).innerHTML = text;

}

function addResetEvent() {
    for (let i = 0; i < 64; i++) {
        document.getElementById("tile-" + i).addEventListener("click", handleReset)
    }
}

async function addMoveEventToPieces() {
    const moves = await getMoves();
    for (const tileId in moves) {
        let element = document.getElementById("tile-" + tileId)
        element = recreateElement(element);
        console.log("Adding event " + tileId)
        element.addEventListener("click", () => handlePieceClick(tileId, moves[tileId]))
    }
}

async function getMoves() {
    var moves;
    await $.ajax({
        url: "/game/player_moves",
        success: function (data) {
            moves = data;
        }
    });

    return await moves
}

async function getCurrentState() {
    var state;
    await $.ajax({
            url: "game/current_state",
            success: function (data) {
                state = data
                console.log(state)
            }
        }
    )
    return await state;
}

async function checkFinished() {
    var finished;
    await $.ajax({
            url: "game/finished",
            success: function (data) {
                finished = data
            }
        }
    )
    return await finished;
}
