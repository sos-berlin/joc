/********************************************************* begin of preamble
**
** Copyright (C) 2003-2014 Software- und Organisations-Service GmbH. 
** All rights reserved.
**
** This file may be used under the terms of either the 
**
**   GNU General Public License version 2.0 (GPL)
**
**   as published by the Free Software Foundation
**   http://www.gnu.org/licenses/gpl-2.0.txt and appearing in the file
**   LICENSE.GPL included in the packaging of this file. 
**
** or the
**  
**   Agreement for Purchase and Licensing
**
**   as offered by Software- und Organisations-Service GmbH
**   in the respective terms of supply that ship with this file.
**
** THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
** IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
** THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
** PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
** BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
** CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
** SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
** INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
** CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
** ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
** POSSIBILITY OF SUCH DAMAGE.
********************************************************** end of preamble*/

// $Id$

// Javascript-Code für HTTP Show log des Schedulers.
// Der Scheduler liefert über HTTP die Ausgaben eines Protokolls bis dieses geschlossen wird.


//-------------------------------------------------------------------------------------------------

var debug_window;

/*
if(false)
{
    debug_window = window.open( "", "",      "width="  + ( window.screen.availWidth - 7 )+
                                           ", height=" + ( Math.floor( window.screen.availHeight * 0.2 ) - 30 ) +
                                           ", left=0"  +
                                           ", top="    +  Math.floor( window.screen.availHeight * 0.8 ) +
                                           ", location=no, menubar=no, toolbar=no, status=no, scrollbars=yes, resizable=yes" );
}
*/
//----------------------------------------------------------------------------------------------var

var timer;
var program_is_scrolling = true;
var error                = false;   // Nach Fehler kein Timer-Intervall mehr

//-------------------------------------------------------------------------------------------------

start_timer();
modify_title();

document.onreadystatechange = document__onreadystatechange;
window.onscroll             = window__onscroll;
document.onmousewheel 			= window__onscroll; /* IE, Chrome */
if(document.addEventListener){ /* Safari, Firefox */
    document.addEventListener('DOMMouseScroll', window__onscroll, false);
}

if( window.navigator.appName == "Netscape" )
//if( window.navigator.vendor == "Firefox" )      // Firefox ruft onscroll nur auf, wenn der Knopf in der Bildlaufleiste mit der Maus verschoben wird.
{                                               // Nicht beim Mausklick auf die Leiste oder bei Page-Up.
    window.onkeydown            = stop_timer;
    window.onmousedown          = stop_timer;
    window.onkeyup              = window__onscroll;
    window.onmouseup            = window__onscroll;
}

//--------------------------------------------------------------------------------------start_timer

function start_timer()
{
    stop_timer();
    
    if( !error )  timer = window.setInterval( "scroll_down()", 200 );
}

//---------------------------------------------------------------------------------------stop_timer

function stop_timer()
{
    if( timer != undefined )
    {
        window.clearInterval( timer );
        timer = undefined;
    }
}

//--------------------------------------------------------------------------------------scroll_down

function scroll_down()
{
    try
    {
        program_is_scrolling = true;
        window.scrollTo( document.body.scrollLeft, document.body.scrollHeight );
    }
    catch( x )
    {
        error = true;
        alert( x.message );
        window.clearInterval( timer );
        timer = undefined;
    }
}

//---------------------------------------------------------------------------------window__onscroll

function window__onscroll()
{
    // Bei Reload scrollt der Browser, bevor der Timer gesetzt ist.
    
    if( debug_window )  debug_window.document.write( "window__onscroll()  timer=" + timer + " program_is_scrolling=" + program_is_scrolling );
    
    if( !program_is_scrolling ) 
    {
        if( document.body.scrollTop + document.body.clientHeight == document.body.scrollHeight )
        {
            if( timer == undefined )  start_timer();
        }
        else
            stop_timer(); 
    }

    program_is_scrolling = false;
}

//-------------------------------------------------------------------------------------modify_title
// document.title += " (running)" oder " (terminated)"

function modify_title()
{
    if( title != undefined )  document.title = "JobScheduler - " + title;

    var title_state;
    
    if( document.readyState == "interactive" )  title_state = "(running)";
    else
    if( document.readyState == "complete" )     title_state = "(terminated)";

    if( title_state )  document.title = document.title.replace( / *(\(.*\))|$/, " " + title_state );
}

//---------------------------------------------------------------------document__onreadystatechange

function document__onreadystatechange()
{
    if( document.readyState == "complete" )
    {
        modify_title();
    }
}

//-------------------------------------------------------------------------------------------------
