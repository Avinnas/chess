/*<![CDATA[*/
function drawBoard(x, y, color) {
    var chessBoard = document.getElementById("chessBoard");
    for (var i = 0; i < 64; i++) {
        var tile = document.createElement('div');
        tile.id = "tile-" + i.toString();
        tile.className = "tile"

        if (Math.floor((i / 8)) % 2 === 0) {
            if (i % 2 === 0) {
                tile.style.backgroundColor = color;
            }
        } else {
            if (i % 2 === 1) {
                tile.style.backgroundColor = color;
            }
        }

        chessBoard.appendChild(tile);
    }
}

function drawPieces(tiles) {
    for (const tileId in tiles) {
        let element = document.getElementById("tile-" + tileId)
        let imageName = ''
        if(tiles[tileId].color === "BLACK"){
            imageName =  '/assets/' + tiles[tileId].name + 'b' + '.png'
        }
        else{
            imageName =  '/assets/' + tiles[tileId].name + '.png'
        }
        const image = document.createElement("img")
        element.appendChild(image);
        image.src = imageName;

    }
}

function handleReset() {
    let elements = document.getElementsByClassName("marker");
    while (elements.length > 0) {
        elements[0].parentNode.removeChild(elements[0]);
    }
}

function handlePieceClick(id, pieceMoves) {
    handleReset();
    console.log(pieceMoves)
    for (const tileToMoveId of pieceMoves) {
        let parentSpan = document.getElementById("tile-" + tileToMoveId)
        let div = document.createElement("div");
        div.className = "marker"
        parentSpan.addEventListener("click", () => handleMove(id, tileToMoveId))

        parentSpan.appendChild(div);
    }

}

function recreateElement(element) {
    var new_element = element.cloneNode(true);
    element.parentNode.replaceChild(new_element, element);
}

function recreateAll() {
    for (let i = 0; i < 64; i++) {
        let element = document.getElementById("tile-" + i);
        recreateElement(element);
    }
}

async function handleMove(pieceId, destinationId) {
    handleReset()
    recreateAll()
    movePieceAtInterface(pieceId, destinationId)

    await sendMove(pieceId, destinationId);
    if(await checkFinished()){
        document.getElementById("result").innerText = "GAME FINISHED"
        return false;
    }
    await addMoveEventToPieces();
    await makeAIMove();
    if(await checkFinished()){
        document.getElementById("result").innerText = "GAME FINISHED"
        return false;
    }

    // alert(" MOVE HANDLE " + pieceId + " -> " + destinationId);


}

async function makeAIMove(){
    let move = await getLastAIMove()
    console.log(move)
    movePieceAtInterface(move.startTile, move.destinationTile)

}

async function getLastAIMove(){
    var lastMove

    await $.ajax({
        url: "/game/last_move",
        success: function (data){
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
