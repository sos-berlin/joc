// base.js

//----------------------------------------------------------------------------------------URLs
var scheduler_gui_base_url = ( document.location.href + "" ).replace( /[/][^/]*$/, "/" )  // Dateiname abschneiden, nur das Verzeichnis nehmen
var scheduler_base_url
var scheduler_engine_cpp_url

if ((document.location.href+"").indexOf("/jobscheduler/") >= 0) {    // Aufruf des Jetty-HTTP-Servers mit neuer Verzeichnisstruktur
    scheduler_base_url = scheduler_gui_base_url.replace(/[/][^/]+[/]$/, "/")    // parent directory
    scheduler_engine_cpp_url = scheduler_base_url +"engine-cpp/"
} else {    // Bisheriger C++-HTTP-Server
    scheduler_base_url = scheduler_gui_base_url
    scheduler_engine_cpp_url = scheduler_gui_base_url
}
