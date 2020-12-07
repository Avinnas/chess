/*<![CDATA[*/
function drawBoard(x, y, playerColor) {
    document.getElementById("chessBoard").innerHTML = ""
    var isWhite = playerColor !== "BLACK";
    var chessBoard = document.getElementById("chessBoard");
    for (var i = 0; i < 64; i++) {
        var tile = document.createElement('div');
        tile.id = "tile-" + (isWhite ? i : 63-i).toString();
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

    // elements = document.getElementsByClassName("movingPiece")
    //
    // while (elements.length > 0) {
    //     recreateElement(elements[0].parentNode);
    //     elements[0].parentNode.addEventListener("click", handleReset);
    //     elements[0].parentNode.removeChild(elements[0]);
    // }
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
function removeLastMoveMarkers(){
    let previousMove = document.getElementsByClassName("lastMoveStart");
    if(previousMove.length>0){
        previousMove[0].classList.remove("lastMoveStart")
    }

    previousMove = document.getElementsByClassName("lastMoveEnd");
    if(previousMove.length>0) {
        previousMove[0].classList.remove("lastMoveEnd")
    }

}

function markLastMove(startId, destinationId) {
    removeLastMoveMarkers();

    let element = document.getElementById("tile-"+ startId);
    element.classList.add("lastMoveStart");
    let element2 = document.getElementById("tile-"+ destinationId);
    element2.classList.add("lastMoveEnd");

}

async function handleMove(pieceId, destinationId) {

    await sendMove(pieceId, destinationId);
    if(await onEachMove(pieceId, destinationId)=== false) {
        return false;
    }
    handleReset()
    recreateAll()
    addResetEvent();

    if(humanPlayerColor === "WHITE"){
        display("Czarne")
    }
    else {
        display("Białe")
    }
    // await addMoveEventToPieces();

     const move = await makeAIMove();
     if(await onEachMove(move.startTile, move.destinationTile) === false){
         return false;
     }
    await addMoveEventToPieces();

    if(humanPlayerColor === "WHITE"){
        display("Białe")
    }
    else {
        display("Czarne")
    }

}
function display(text){
    document.getElementById("result").innerText = text
}
function displayColor(color){
    if(color === "WHITE"){
        var c = "Białe"
    }
    else{
        var c = "Czarne"
    }
    document.getElementById("result").innerText = c
}



async function onEachMove(pieceId, destinationId){

    drawPieces(await getCurrentState())
    markLastMove(pieceId, destinationId);

    if (await checkFinished()) {
        document.getElementById("result").innerText = "GRA ZAKOŃCZONA"
        return false;
    }

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
        }

    })

}
async function newGame(){

    var color = document.getElementById("color").value

    display("Białe")

    await sendNewGameRequest(color);
    drawBoard(8,8, color)
    drawPieces(await getCurrentState())

    if(color === "BLACK"){
        const move = await makeAIMove();
        await onEachMove(move.startTile, move.destinationTile);
        display("Czarne")
    }

    addResetEvent();
    await addMoveEventToPieces()

    humanPlayerColor = color

}

async function refreshBoard(){
    removeLastMoveMarkers();
    drawPieces(await getCurrentState())
    addResetEvent();
    await addMoveEventToPieces()
}

async function sendNewGameRequest(c){

    let obj = {color: c}
    await $.ajax({
        type: "POST",
        url: "/game/new",
        data: JSON.stringify(obj),
        contentType: "application/json",
        success: function () {
            //
        }
    });

}

function movePieceAtInterface(pieceId, destinationId) {
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
        element.addEventListener("click", () => handlePieceClick(tileId, moves[tileId]))
        // let possibleMovingPiece = document.createElement("div");
        // possibleMovingPiece.className = "movingPiece";
        // element.appendChild(possibleMovingPiece);
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

async function getGame() {
    var state;
    await $.ajax({
            url: "game/show_game",
            success: function (data) {
                state = data
            }
        }
    )
    return await state;
}

