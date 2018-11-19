function play(){
    $( "#background" ).animate({
        left: "-=20px",
        opacity: .9,
    }, 500);
}

function stop(){
    $( "#background" ).animate({
        left: "+=20px",
        opacity: 0,
    }, 500);
}

var data

function update(data){
    console.log("nice")
    var fixedData = data.replace(/^(<templateData>|<componentData>|<data>)|(<\/templateData>|<\/componentData>|<\/data>)$/ig, );
    data = JSON.parse(decodeURI(fixedData))
    console.log("artist: " + data["artist"])
    console.log("song: " + data["song"])
    console.log("song: " + data["album"])
    $("#artist").text(data["artist"])
    $("#song").text(data["song"])
    $("#album").text(data["album"])
}

function next(){

}