function play(){
    $( "#city_state" ).animate({
        top: "-=20px",
        opacity: 1,
    }, 500);
}

function stop(){
    $( "#city_state" ).animate({
        top: "+=20px",
        opacity: 0,
    }, 500);
}

var data

function update(data){
    var fixedData = data.replace(/^(<templateData>|<componentData>|<data>)|(<\/templateData>|<\/componentData>|<\/data>)$/ig, );
    data = JSON.parse(decodeURI(fixedData))
    console.log("city: " + data["city"])
    console.log("state: " + data["state"])
    $("#background_text").text(data["city"])
    $("#background_subtext").text(data["state"])
}

function next(){

}