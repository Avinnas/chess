
function removePieces(){
    for(let i=0; i<64; i++){
        let element = document.getElementById("tile-"+i)
        element.innerHTML= ''
    }
}
async function goToMove(moveId){

    let state = await getStateAfterMove(moveId)
    removePieces()
    drawPieces(state);
}

async function getStateAfterMove(moveId){
    var state;
    await $.ajax({
        url: "/history/move/"+moveId,
        success: function (data){
            state = data;
        }
    })
    return state;
}

function displayResult(result){
    let resultElement = document.getElementById("result")
    if(result===10){
        resultElement.innerHTML= "Draw"
    }
    else if(result===-1){
        resultElement.innerHTML= "Black won"
    }
    else if(result===1){
        resultElement.innerHTML= "White won"
    }
    else{
        resultElement.innerHTML= "Not finished"
    }
}