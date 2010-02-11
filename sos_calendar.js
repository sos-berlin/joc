/********************************************************* begin of preamble
**
** Copyright (C) 2003-2008 Software- und Organisations-Service GmbH. 
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

  if(!window['SOS_Exception']) {
    var SOS_Exception = function(err) { throw err; }
  }
  
  var SOS_Calendar = function(){}
  /** @access public */
  /** DIV Id des Kalenders. DIV wird (falls nicht vorhanden) in der Klasse gemalt */
  SOS_Calendar.id                           = 'sos_calendar';
  /** Imageverzeichnis der Anwendung */
  SOS_Calendar.imgDir                       = 'js/calendar/images/';
  /** Datum + Time zurückgeben, bei false - nur datum */
  SOS_Calendar.fullDatetime                 = true;
  /** Jahr 4- oder 2-stelig zurückgeben */
  SOS_Calendar.fullYear                     = false;
  SOS_Calendar.returnISO                    = false;
  /** default Eigenschaften des Kalenderdivs (wenn Kalender DIV in der Klasse gemalt wird) */
  SOS_Calendar.defaultStylePosition         = 'absolute';
  SOS_Calendar.defaultStyleOverflow         = 'visible'; 
  SOS_Calendar.defaultStyleZindex           = 1; 
  SOS_Calendar.defaultStyleDisplay          = 'none';
  SOS_Calendar.defaultStyleTop              = '0px';
  SOS_Calendar.defaultStyleLeft             = '0px';
  SOS_Calendar.defaultStyleWidth            = '240px';
  SOS_Calendar.defaultStyleHeight           = '210px';
  SOS_Calendar.defaultStyleBorderActiv      = 'solid 1px #0555eb';
  SOS_Calendar.defaultStyleBorderUnActiv    = 'solid 1px #81a7e8';
  SOS_Calendar.defaultStyleBackgroundColor  = '#ffffff';
  /** siehe SOS_Calendar.show() */
  /** Hintergrundbild (linke Seite) bei Listboxen & Navigationsbuttons */
  SOS_Calendar.imgTdBgLeft                  = null;
  /** Hintergrundbild bei Listboxen & Navigationsbuttons */
  SOS_Calendar.imgTdBg                      = null;
  /** Bild "Heute" (roter Kringel) */
  SOS_Calendar.imgCurrent                   = null;
  /** Style für die Überschriften (Mo,Di,Mi,......)*/ 
  SOS_Calendar.styleDaysHeader              = 'font-weight:bold;font-size:12px';
  /** Style für die Listbox */ 
  SOS_Calendar.styleListbox                 = 'font-weight:normal;';
  /** Style für die Überschriften in der Titelleiste (Datum + Uhrzeit) */ 
  SOS_Calendar.styleTitle                   = 'font-size: 11px;font-weight:bold;color:#ffffff';
  /** Border für Feiertag,ausgewählten oder aktuellen Tag */
  SOS_Calendar.borderDays                   = 'border:1px solid blue;';
  /** Hintergrundfarbe für Montag bis Freitag */
  SOS_Calendar.bgWorkdays                   = '#ffffff';  
  SOS_Calendar.showHolidays                 = false;  
  /** Hintergrundfarbe für Feiertage */
  SOS_Calendar.bgHolidays                   = '#ff0000';  
  /** Hintergrundfarbe für Samstag */
  SOS_Calendar.bgSaturday                   = '#F9C697';
  /** Hintergrundfarbe für Sonntag */  
  SOS_Calendar.bgSunday                     = '#F3953D';
  /** Hintergrundfarbe für aktuellen Tag (Heute) */  
  SOS_Calendar.bgDayCurrent                 = '#00ff00';
  /** Hintergrundfarbe für ausgewählten Tag */  
  SOS_Calendar.bgDaySelected                = '#dddddd';  
  /** Styles für die Navigationselemente ("Monat vor" usw)*/
  var sosCalendarBsAdd                      = (navigator.appVersion.match(/\bMSIE\b/)) ? 'border:1 solid #d8d6c7;width:100%;margin:3px;' : 'border:1 solid #d8d6c7;width:96%;padding:0px 3px;';
  SOS_Calendar.buttonStyle                  = 'style="'+sosCalendarBsAdd+'cursor:pointer;background:#ebe8d5;color: black;font-family: tahoma, arial;font-size:13px;height:70%;" ';
  /** */
  SOS_Calendar.buttonOnMouseEffect          = 'onmouseover="this.style.backgroundColor=\'#D8D2AB\'" onmouseout="this.style.backgroundColor=\'#ebe8d5\'"';
  /** Ab diesen Tag Monatsanzeige beginnen : Sonntag = 0, Montag = 1 usw  */
  SOS_Calendar.startDay                     = 1;
  /** SOS_Calendar.startDay automatisch abhängig von der Sprache setzen  */
  SOS_Calendar.autoStartDay                 = true;
  /** Abstand (rechts) zum Formularelement (px) */
  SOS_Calendar.indent                       = 50;
  SOS_Calendar.indentY                      = 0;
  /**  @access private */
  /** Default skin (braucht keine include Datei)
  *   sonst einfach js/calendar/skins/xxx.js includieren 
  *   Images werden aus SOS_Calendar.imgDir+SOS_Calendar.skin genommen
  */
  SOS_Calendar.skin                         = 'default';
  /** Sprache der Anwendung  SOS_LANG wird erwartet*/
  SOS_Calendar.language                     = 'de';
  /** Aktuallisierungsintervall von Uhr/Kalender. 1000 = 1 Sekunde */
  SOS_Calendar.refreshInterval              = 20000;      
  /** Formularelement */
  SOS_Calendar.formElement                  = null;
  /** ausgewählter Tag    (aus dem Formular)*/    
  SOS_Calendar.selectedDay                  = null;
  /** ausgewählter Monat  (aus dem Formular)*/
  SOS_Calendar.selectedMonth                = null;
  /** ausgewähltes Jahr   (aus dem Formular)*/
  SOS_Calendar.selectedYear                 = null;
  /** aktuelle Zeit */
  SOS_Calendar.now                          = new Date();
  SOS_Calendar.nowDay                       = SOS_Calendar.now.getDate();
  SOS_Calendar.nowMonth                     = SOS_Calendar.now.getMonth()+1;
  SOS_Calendar.nowYear                      = SOS_Calendar.now.getYear();
  SOS_Calendar.nowHour                      = 0;
  SOS_Calendar.nowMinute                    = 0;
  SOS_Calendar.monthDays                    = new Array(0,31,28,31,30,31,30,31,31,30,31,30,31);
  if(SOS_Calendar.nowYear < 1900){ SOS_Calendar.nowYear = SOS_Calendar.nowYear +1900; }  // umrechnen fuer Mozilla
  /** aktuell angezeigte Zeit */
  SOS_Calendar.currentDay                   = SOS_Calendar.nowDay;
  SOS_Calendar.currentMonth                 = SOS_Calendar.nowMonth;
  SOS_Calendar.currentYear                  = SOS_Calendar.nowYear;
  SOS_Calendar.currentHour                  = '';
  SOS_Calendar.currentMinute                = '';
  /** Position des ersten Monatstages in der Tabelle */
  SOS_Calendar.startDayPosition             = 1;
  SOS_Calendar.dateFormat                   = null;
  SOS_Calendar.isOpen                       = false;
  /** Start Jahr in der Listbox */
  SOS_Calendar.listboxStartYear             = null;
  /** End Jahr in der Listbox */
  SOS_Calendar.listboxEndYear               = null;
  SOS_Calendar.onFillForm                   = '';
  
  /**
  * Klick Position ermitteln und abhängig von dieser Stelle Kalender anzeigen
  */
  SOS_Calendar.show = function(e,formElement,dateFormat){
    try{ 
      var windowWidth  = 0;
      var windowHeight = 0;
      if(e){
        windowWidth  = document.body.offsetWidth;
        windowHeight = document.body.offsetHeight;
      }
      else{ 
        var e         = window.event;
        // ?????
        windowWidth  = document.body.offsetWidth;
        windowHeight = document.body.offsetHeight;
      } 
      /** Sprachabhängige Beschriftungen setzen */
      if(typeof SOS_LANG != 'undefined'){  SOS_Calendar.language = SOS_LANG;  }
      SOS_Calendar.setLables();
      SOS_Calendar.bgColors = new Array(SOS_Calendar.bgSunday,
                                        SOS_Calendar.bgWorkdays,
                                        SOS_Calendar.bgWorkdays,
                                        SOS_Calendar.bgWorkdays,
                                        SOS_Calendar.bgWorkdays,
                                        SOS_Calendar.bgWorkdays,
                                        SOS_Calendar.bgSaturday                                               
                                        );
       
      SOS_Calendar.imgTdBgLeft  = SOS_Calendar.imgDir+SOS_Calendar.skin+'/bg_td_left.gif';
      SOS_Calendar.imgTdBg      = SOS_Calendar.imgDir+SOS_Calendar.skin+'/bg_td.gif';
      SOS_Calendar.imgCurrent   = SOS_Calendar.imgDir+'icon_calendar_current_day.gif';
      
      if(SOS_Calendar.listboxStartYear == null){
        SOS_Calendar.listboxStartYear = SOS_Calendar.nowYear-5; 
        SOS_Calendar.listboxEndYear   = SOS_Calendar.nowYear+4;
      }
      
      var calDiv = document.getElementById(SOS_Calendar.id);     
     
      if(typeof calDiv == 'undefined' || calDiv == null){
        //document.getElementsByTagName('body')[0].innerHTML +=  '<div id="'+SOS_Calendar.id+'" style="'+SOS_Calendar.divProperties+'"></div>';
        var elDiv                   = document.createElement('div');
        elDiv.id                    = SOS_Calendar.id;
        elDiv.style.position        = SOS_Calendar.defaultStylePosition;
        elDiv.style.width           = SOS_Calendar.defaultStyleWidth;
        if(navigator.appVersion.match(/\bMSIE\b/)) elDiv.style.height = SOS_Calendar.defaultStyleHeight;
        elDiv.style.top             = SOS_Calendar.defaultStyleTop;
        elDiv.style.left            = SOS_Calendar.defaultStyleLeft;
        elDiv.style.border          = SOS_Calendar.defaultStyleBorderActiv;
        elDiv.style.overflow        = SOS_Calendar.defaultStyleOverflow;
        elDiv.style.zIndex          = SOS_Calendar.defaultStyleZindex;
        elDiv.style.display         = SOS_Calendar.defaultStyleDisplay;
        elDiv.style.backgroundColor = SOS_Calendar.defaultStyleBackgroundColor;        
        
        document.getElementsByTagName('body')[0].appendChild(elDiv);
        
        calDiv = document.getElementById(SOS_Calendar.id);
      }
      if(typeof calDiv != 'undefined' && calDiv != null){ 
        SOS_Calendar.dateFormat   = (dateFormat) ? dateFormat : null;
        SOS_Calendar.formElement  = formElement;
        
        var scrollLeft    = 0;
        var scrollTop     = 0;
        
        if(self.innerHeight){
          scrollLeft    = self.pageXOffset;
          scrollTop     = self.pageYOffset;
        }
        else if(document.documentElement && document.documentElement.clientHeight){ //IE 6
          scrollLeft    = document.documentElement.scrollLeft;
          scrollTop     = document.documentElement.scrollTop;
        }
        else if(document.body){
          scrollLeft    = document.body.scrollLeft;
          scrollTop     = document.body.scrollTop;
        }
        var divWidth = parseInt(calDiv.style.width);
        var currentX = e.clientX+scrollLeft; 
        var top      = e.clientY+scrollTop-100+SOS_Calendar.indentY;
        
        if(1*windowWidth-1*SOS_Calendar.indent-1*currentX-1*divWidth < 0 ){
          calDiv.style.left = (1*currentX-300-1*divWidth)+'px'; 
        }
        else{
          calDiv.style.left = (1*e.clientX+1*SOS_Calendar.indent)+'px';
        }
        calDiv.style.top    = (top < 20) ? '20px' : (top)+'px';
        calDiv.style.display = '';   
        // nach top,left zIndex
        SOS_Calendar.makeActiv(calDiv);
        if(formElement){
          try{
            var val = eval('document.'+formElement+'.value;');
            SOS_Calendar.setDateFromForm(val);
          }
          catch(x){ 
            try{
              var a   = formElement.split('.');   
              var val = document.getElementById(a[0]).elements[a[1]].value;
              SOS_Calendar.setDateFromForm(val);
            }
            catch(x){
              throw x;
            }
          } 
        }
        SOS_Calendar.make();
        SOS_Calendar.isOpen = true;
      }
    }
    catch(x){
      throw new SOS_Exception(x,x.message,'',0); 
    }
  }

  /**
  * Sprachabhängige Labels setzen
  */
  SOS_Calendar.setLables = function(){
    try{
      switch(SOS_Calendar.language.toLowerCase()){
        case 'en'     :
                        SOS_Calendar.labelMonths        = new Array('','January','February','March','April','May','June','July','August','September','October','November','December');
                        SOS_Calendar.labelDays          = new Array('Su','Mo','Tu','We','Th','Fr','Sa');
                        SOS_Calendar.labelClock         = 'o\'clock';
                        SOS_Calendar.labelYearBack      = 'one year back';
                        SOS_Calendar.labelYearNext      = 'next year';
                        SOS_Calendar.labelDayCurrent    = 'today';
                        SOS_Calendar.labelMonthCurrent  = 'show current month';
                        SOS_Calendar.labelMonthBack     = 'one month back';
                        SOS_Calendar.labelMonthNext     = 'next month';
                        SOS_Calendar.labelClose         = 'close';
                        SOS_Calendar.labelHoliday       = 'holiday';
                        
                        // Kalenderanzeige ab Sonntag
                        if(SOS_Calendar.autoStartDay){
                          SOS_Calendar.startDay = 0;
                        }
                        break;
        default       :
                        SOS_Calendar.labelMonths        = new Array('','Januar','Februar','M&auml;rz','April','Mai','Juni','Juli','August','September','Oktober','November','Dezember');
                        SOS_Calendar.labelDays          = new Array('So','Mo','Di','Mi','Do','Fr','Sa');
                        SOS_Calendar.labelClock         = 'Uhr';
                        SOS_Calendar.labelYearBack      = 'ein Jahr zur&uuml;ck';
                        SOS_Calendar.labelYearNext      = 'ein Jahr vor';
                        SOS_Calendar.labelDayCurrent    = 'Heute';
                        SOS_Calendar.labelMonthCurrent  = 'aktuellen Monat anzeigen';
                        SOS_Calendar.labelMonthBack     = 'einen Monat zur&uuml;ck';
                        SOS_Calendar.labelMonthNext     = 'einen Monat vor';
                        SOS_Calendar.labelClose         = 'schlie&szlig;en';
                        SOS_Calendar.labelHoliday       = 'Feiertag';
                        
                        // Kalenderanzeige ab Montag
                        if(SOS_Calendar.autoStartDay){
                          SOS_Calendar.startDay = 1;
                        }
      }
    }
    catch(x){
      throw new SOS_Exception(x,x.message,'',0);  
    }
  }

  /**
  * Kalender Fenster als Activ setzen (wenn die Klasse SOS_Div deklariert ist)
  */
  SOS_Calendar.makeActiv = function(div){
    try{
      if(typeof SOS_Div != 'undefined'){
        var sosDivImgDir              = SOS_Div.imgDir;
        var sosDivStyleBorderActiv    = SOS_Div.styleBorderActiv;
        var sosDivStyleBorderUnActiv  = SOS_Div.styleBorderUnActiv;
        
        SOS_Div.styleBorderActiv      = SOS_Calendar.defaultStyleBorderActiv;
        SOS_Div.styleBorderUnActiv    = SOS_Calendar.defaultStyleBorderUnActiv;
        SOS_Div.imgDir                = SOS_Calendar.imgDir+SOS_Calendar.skin+'/'; 
        
        //div.onmousedown             = new Function("SOS_Div.makeActiv(this);SOS_Div.imgDir = '"+sosDivImgDir+"';SOS_Div.styleBorderActiv = '"+sosDivStyleBorderActiv+"';SOS_Div.styleBorderUnActiv = '"+sosDivStyleBorderUnActiv+"';");
        // Kalender Border sollen beim Aktivieren nicht geändert werden
        div.notChangeBorderColor      = true;
        div.onmousedown               = new Function("SOS_Calendar.makeActiv(this);");
        
        SOS_Div.imgDir                = sosDivImgDir;
        SOS_Div.styleBorderActiv      = sosDivStyleBorderActiv;
        SOS_Div.styleBorderUnActiv    = sosDivStyleBorderUnActiv; 
      
        SOS_Div.makeActiv(div);
      }
    }
    catch(x){
      throw new SOS_Exception(x,x.message,'',0); 
    }
  }

  /**
  * Kalender Anzeige zusammenstellen
  */
  SOS_Calendar.make = function() {
    try{
      SOS_Calendar.monthDays[2] = SOS_Calendar.leapYear(SOS_Calendar.currentYear);
      SOS_Calendar.getDatetime();
      SOS_Calendar.getNow();
      SOS_Calendar.paint();
      SOS_Calendar.paintTime();
    }
    catch(x){
      throw new SOS_Exception(x,x.message,'',0);  
    } 
  }
 
  /**
  * setzt Kalender Zeit
  */
  SOS_Calendar.paintTime = function() {
    try{
      SOS_Calendar.getNow();
      window.clearTimeout(SOS_Calendar.timeout);
      SOS_Calendar.timeout = window.setTimeout('SOS_Calendar.paintTime()',SOS_Calendar.refreshInterval);
          
      document.getElementById(SOS_Calendar.id+':time').innerHTML = SOS_Calendar.currentHour+':'+SOS_Calendar.currentMinute; 
    }
    catch(x){}
  }

  /**
  * Kalenderdatum
  */
  SOS_Calendar.getDatetime = function() {
    jdat                          = new Date(SOS_Calendar.currentYear,SOS_Calendar.currentMonth-1,1);
    SOS_Calendar.startDayPosition = jdat.getDay();
    if (SOS_Calendar.startDayPosition == 0) SOS_Calendar.startDayPosition = 7;
  }
  
  /**
  * Aktuellesdatum
  */
  SOS_Calendar.getNow = function(){
    SOS_Calendar.now            = new Date();
    SOS_Calendar.nowMinute      = SOS_Calendar.now.getMinutes();
    SOS_Calendar.nowHour        = SOS_Calendar.now.getHours();
    SOS_Calendar.currentHour    = String(SOS_Calendar.nowHour);
    SOS_Calendar.currentMinute  = String(SOS_Calendar.nowMinute);
    if (SOS_Calendar.nowMinute < 10) SOS_Calendar.currentMinute = '0'+String(SOS_Calendar.nowMinute);
  }
  
  /**
  * Aktuellesdatum setzen
  */
  SOS_Calendar.setCurrent2Now = function(){
    SOS_Calendar.currentDay   = SOS_Calendar.nowDay;
    SOS_Calendar.currentMonth = SOS_Calendar.nowMonth;
    SOS_Calendar.currentYear  = SOS_Calendar.nowYear;
  }

  /**
  * Anzahl Tage im Februar berechnen (Schaltjahr)
  */
  SOS_Calendar.leapYear = function(j) {
    t = 28;
    if (j % 4 == 0) {
      t = 29;
      if (j % 100 == 0 && j % 400 != 0) t = 28;
    }
    return t;
  }
  
  /**
  * Navigation - ein Jahr zurück
  */
  SOS_Calendar.yearBack = function() {
    try{
      SOS_Calendar.currentYear--;
      if (SOS_Calendar.currentYear < 1970) {
        SOS_Calendar.currentYear   = 1970;
      }
      SOS_Calendar.make();
    }
    catch(x){
      throw new SOS_Exception(x,x.message,'',0);  
    }
  }
    
  /**
  * Navigation - ein Jahr vor
  */
  SOS_Calendar.yearNext = function() {
    try{
      SOS_Calendar.currentYear++;
      SOS_Calendar.make();
    }
    catch(x){
      throw new SOS_Exception(x,x.message,'',0);  
    }
  }
   
  
  /**
  * Navigation - ein ausgewählter Monat
  */
  SOS_Calendar.monthSelected = function(el) {
    try{
      SOS_Calendar.currentMonth = 1*el.value; 
      SOS_Calendar.make();
    }
    catch(x){
      throw new SOS_Exception(x,x.message,'',0);  
    }
  }
  
  /**
  * Navigation - ein ausgewähltes Jahr
  */
  SOS_Calendar.yearSelected = function(el) {
    try{
      SOS_Calendar.currentYear = 1*el.value; 
      SOS_Calendar.make();
    }
    catch(x){
      throw new SOS_Exception(x,x.message,'',0);  
    }
  }
  
  /**
  * Navigation - einen Monat zurück
  */
  SOS_Calendar.monthBack = function() {
    try{
      SOS_Calendar.currentMonth--;
      if (SOS_Calendar.currentMonth<1) {
        SOS_Calendar.currentMonth = 12;
        SOS_Calendar.currentYear--;
        if (SOS_Calendar.currentYear < 1970) {
          SOS_Calendar.currentYear   = 1970;
          SOS_Calendar.currentMonth  = 1;
        }
      }
      SOS_Calendar.make();
    }
    catch(x){
      throw new SOS_Exception(x,x.message,'',0);  
    }
  }
    
  /**
  * Navigation - einen Monat vor
  */
  SOS_Calendar.monthNext = function() {   
    try{
      SOS_Calendar.currentMonth++;
      if (SOS_Calendar.currentMonth > 12) {
        SOS_Calendar.currentMonth = 1;
        SOS_Calendar.currentYear++;
      }
      SOS_Calendar.make();
    }
    catch(x){
      throw new SOS_Exception(x,x.message,'',0);  
    }
  }
  
  /**
  * Navigation - aktueller Monat
  */
  SOS_Calendar.monthCurrent = function() {
    try{
      SOS_Calendar.currentDay   = SOS_Calendar.nowDay;
      SOS_Calendar.currentMonth = SOS_Calendar.nowMonth;
      SOS_Calendar.currentYear  = SOS_Calendar.nowYear;
      SOS_Calendar.make();
    }
    catch(x){
      throw new SOS_Exception(x,x.message,'',0);  
    }
  }
  
  
  /**
  * Kalender schliessen
  */
  SOS_Calendar.close = function() {
    try{  
      if(typeof SOS_Div != 'undefined'){
        SOS_Div.close(SOS_Calendar.id); 
      }
      else{
        document.getElementById(SOS_Calendar.id).style.display = 'none';
      }
      window.clearTimeout(SOS_Calendar.timeout);
      SOS_Calendar.isOpen = false;
    }
    catch(x){
      throw new SOS_Exception(x,x.message,'',0);  
    }
  }
  
  /**
  * Kalender malen
  */
  SOS_Calendar.paint = function() {
    try{
      var trTag         = '<tr align="center" valign="middle">';
      var titleDate     = (this.language == 'de') ? SOS_Calendar.nowDay+'.&nbsp;'+SOS_Calendar.labelMonths[SOS_Calendar.nowMonth]+'&nbsp;'+SOS_Calendar.nowYear : SOS_Calendar.labelMonths[SOS_Calendar.nowMonth]+'&nbsp;'+SOS_Calendar.nowDay+',&nbsp;'+SOS_Calendar.nowYear;
      var styleListbox  = (SOS_Calendar.styleListbox != null && SOS_Calendar.styleListbox.length > 0) ? 'style="'+SOS_Calendar.styleListbox+'"' : ''; 
      var content       = '';
      content += ' <table  style="cursor:move" width="100%" border="0" cellpadding="0" cellspacing="0">\n';
      content += '  <tr>\n';
      content += '    <td id="'+SOS_Calendar.id+':1" style="'+SOS_Calendar.styleTitle+'" background="'+SOS_Calendar.imgDir+SOS_Calendar.skin+'/bg_title_activ.gif" style="cursor:move;height:18px">\n';
      content += '      &nbsp;'+titleDate+'&nbsp;&nbsp;&nbsp;<span id="'+SOS_Calendar.id+':time"></span>\n';
      content += '    </td>\n';
      content += '     <td id="'+SOS_Calendar.id+':2" width="1%" background="'+SOS_Calendar.imgDir+SOS_Calendar.skin+'/bg_title_activ.gif" align="right" style="cursor:pointer" title="'+SOS_Calendar.labelClose+'" onclick="SOS_Calendar.close();"><img src="'+SOS_Calendar.imgDir+SOS_Calendar.skin+'/icon_close_activ.gif"/></td>\n';
      content += '  </tr>\n';
      content += ' </table>\n';
      content += '\n';
      
      content += ' <table width="100%" border="0" cellpadding="0" cellspacing="0">\n';
      content += '  <tr style="height:32px">\n';
      content += '    <td style="width:6px;" background="'+SOS_Calendar.imgTdBgLeft+'">&nbsp;</td>\n';
      content += '    <th background="'+SOS_Calendar.imgTdBg+'">';
      content += '<select '+styleListbox+' onchange="SOS_Calendar.monthSelected(this);" name="class_sos_calendar_months">';
      for(var i=1;i< SOS_Calendar.labelMonths.length;i++){
        var mothSelected = (i == SOS_Calendar.currentMonth) ? ' selected="selected" ' : '';
        content += '<option value="'+i+'" '+mothSelected+'>'+SOS_Calendar.labelMonths[i]+'</option>';
      }
      content += '</select>';
      content += '&nbsp;&nbsp;';
      content += '<select '+styleListbox+' onchange="SOS_Calendar.yearSelected(this);" name="class_sos_calendar_years">';
      
      if(SOS_Calendar.listboxStartYear < 1970){
        SOS_Calendar.listboxStartYear = 1970; 
      }
      
      if(SOS_Calendar.currentYear < SOS_Calendar.listboxStartYear){
        SOS_Calendar.listboxStartYear = (SOS_Calendar.currentYear < 1970) ? 1970 : SOS_Calendar.currentYear;  
      }
      
      if(SOS_Calendar.currentYear > SOS_Calendar.listboxEndYear){ // angegeben wurde zB 2090
        SOS_Calendar.listboxEndYear = SOS_Calendar.currentYear;  
      }
      
      if(SOS_Calendar.listboxStartYear > 1970){
        content += '<option value="'+(SOS_Calendar.listboxStartYear-1)+'" >&nbsp;&nbsp;...</option>';
      }
      
      for(var i=1*SOS_Calendar.listboxStartYear;i<= 1*SOS_Calendar.listboxEndYear;i++){
        var yearSelected = (i == SOS_Calendar.currentYear) ? ' selected="selected" ' : '';
        content += '<option value="'+i+'" '+yearSelected+'>'+i+'</option>';
      }
      content += '<option value="'+(i)+'" >&nbsp;&nbsp;...</option>';
      content += '</select>';
      content += '    </th>\n';
      content += '  </tr>\n';
      content += ' </table>\n';
      content += '\n';
      
      content += '<table border="0" cellspacing="0" cellpadding="0" width="100%" style="cursor:auto;font-size:12px;">';
      content += trTag;
      content += '<td height="18" align="center" style="'+SOS_Calendar.styleDaysHeader+'" bgcolor="'+SOS_Calendar.bgColors[(SOS_Calendar.startDay)%7]+'"  width="14%">'+SOS_Calendar.labelDays[(SOS_Calendar.startDay)%7]  +'</td>';
      content += '<td height="18" align="center" style="'+SOS_Calendar.styleDaysHeader+'" bgcolor="'+SOS_Calendar.bgColors[(SOS_Calendar.startDay+1)%7]+'" width="15%">'+SOS_Calendar.labelDays[(SOS_Calendar.startDay+1)%7]+'</td>';
      content += '<td height="18" align="center" style="'+SOS_Calendar.styleDaysHeader+'" bgcolor="'+SOS_Calendar.bgColors[(SOS_Calendar.startDay+2)%7]+'" width="15%">'+SOS_Calendar.labelDays[(SOS_Calendar.startDay+2)%7]+'</td>';
      content += '<td height="18" align="center" style="'+SOS_Calendar.styleDaysHeader+'" bgcolor="'+SOS_Calendar.bgColors[(SOS_Calendar.startDay+3)%7]+'" width="15%">'+SOS_Calendar.labelDays[(SOS_Calendar.startDay+3)%7]+'</td>';
      content += '<td height="18" align="center" style="'+SOS_Calendar.styleDaysHeader+'" bgcolor="'+SOS_Calendar.bgColors[(SOS_Calendar.startDay+4)%7]+'" width="15%">'+SOS_Calendar.labelDays[(SOS_Calendar.startDay+4)%7]+'</td>';
      content += '<td height="18" align="center" style="'+SOS_Calendar.styleDaysHeader+'" bgcolor="'+SOS_Calendar.bgColors[(SOS_Calendar.startDay+5)%7]+'" width="14%">'+SOS_Calendar.labelDays[(SOS_Calendar.startDay+5)%7]+'</td>';
      content += '<td height="18" align="center" style="'+SOS_Calendar.styleDaysHeader+'" bgcolor="'+SOS_Calendar.bgColors[(SOS_Calendar.startDay+6)%7]+'" width="14%">'+SOS_Calendar.labelDays[(SOS_Calendar.startDay+6)%7]+'</td>';
      content += '</tr>';
      count = 0;
      
      SOS_Calendar.startDayPosition -= SOS_Calendar.startDay;
      
      for(asl=1; asl<7; asl++) {
        content += trTag;
        for(bsl=1; bsl<8; bsl++){
          count++;
          /*
          if(bsl < 6)   color = SOS_Calendar.bgWorkdays;
          if(bsl == 6)  color = SOS_Calendar.bgSaturday;
          if(bsl > 6)   color = SOS_Calendar.bgSunday;
          */
          color = SOS_Calendar.bgColors[(SOS_Calendar.startDay+(bsl-1))%7];
          //act = count + 1 - SOS_Calendar.startDayPosition;
          //act = count + SOS_Calendar.startDay - SOS_Calendar.startDayPosition;
          act = count - SOS_Calendar.startDayPosition;
          
          var border  = '';
          var title   = '';
          if(act == SOS_Calendar.currentDay && SOS_Calendar.currentMonth == SOS_Calendar.selectedMonth && SOS_Calendar.currentYear == SOS_Calendar.selectedYear){
            color   = SOS_Calendar.bgDaySelected;
            border  = SOS_Calendar.borderDays;
          }
          // Feiertage farblich darstellen
          if(SOS_Calendar.showHolidays){
          	if (SOS_Calendar.currentMonth == 1  && act  == 1 || 
            	  SOS_Calendar.currentMonth == 5  && act  == 1 || 
              	SOS_Calendar.currentMonth == 10 && act  == 3  && SOS_Calendar.currentYear > 1990 || 
              	SOS_Calendar.currentMonth == 12 && (act == 25 || act == 26)){
          
            	color   = SOS_Calendar.bgHolidays;
            	border  = SOS_Calendar.borderDays;
            	title   = ' title="'+SOS_Calendar.labelHoliday+'" ';
          	}
          }
          
          if (count < SOS_Calendar.startDayPosition || act > SOS_Calendar.monthDays[SOS_Calendar.currentMonth]){
            content += '<td height="18" style="font-style:normal;font-weight:500;font-size:97%;background-color:'+color+';">&nbsp;</td>';
          }
          if (count >= SOS_Calendar.startDayPosition && act <= SOS_Calendar.monthDays[SOS_Calendar.currentMonth]) {
            calOnclick = 'onclick="SOS_Calendar.fillForm(this.innerHTML,SOS_Calendar.currentMonth,SOS_Calendar.currentYear)"';
            if (act == SOS_Calendar.nowDay && SOS_Calendar.currentMonth == SOS_Calendar.nowMonth && SOS_Calendar.currentYear == SOS_Calendar.nowYear){
              content += '<td height="18" background="'+SOS_Calendar.imgCurrent+'" style="background-repeat:no-repeat;background-position:center;cursor:pointer;font-style:normal;font-weight:500;font-size:97%;background-color:'+SOS_Calendar.bgDayCurrent+';'+SOS_Calendar.borderDays+'" '+calOnclick+'>'+String(act)+'</td>';
            }
            else{
              if(act == 0){
                content += '<td height="18" '+title+' style="cursor:pointer;font-style:normal;font-weight:500;font-size:97%;background-color:'+color+';'+border+'">&nbsp;</td>';
              }
              else{
                content += '<td height="18" '+title+' style="cursor:pointer;font-style:normal;font-weight:500;font-size:97%;background-color:'+color+';'+border+'" '+calOnclick+'>'+String(act)+'</td>';
              }
            }
          }
        }
        content+= '</tr>';
      }
      content += '</table>';
    
      content += ' <table width="100%" border="0" cellpadding="0" cellspacing="0">\n';
      content += '  <tr style="height:32px">\n';
      content += '    <td style="width:6px;" background="'+SOS_Calendar.imgTdBgLeft+'">&nbsp;</td>\n';
      
      content += '    <td width="15%" background="'+SOS_Calendar.imgTdBg+'" align="center">';
      content += '    <button '+SOS_Calendar.buttonOnMouseEffect+' '+SOS_Calendar.buttonStyle+' type="button" onclick="SOS_Calendar.yearBack();" title="'+SOS_Calendar.labelYearBack+'"><<</button>';
      content += '    </td>\n';
      
      content += '    <td width="15%" background="'+SOS_Calendar.imgTdBg+'" align="center">';
      content += '    <button '+SOS_Calendar.buttonOnMouseEffect+' '+SOS_Calendar.buttonStyle+' type="button" onclick="SOS_Calendar.monthBack();" title="'+SOS_Calendar.labelMonthBack+'"><</button>';
      content += '    </td>\n';
      
      content += '    <td align="center" background="'+SOS_Calendar.imgTdBg+'" align="center">';
      content += '    <button '+SOS_Calendar.buttonOnMouseEffect+' '+SOS_Calendar.buttonStyle+' type="button" onclick="SOS_Calendar.monthCurrent();" title="'+SOS_Calendar.labelMonthCurrent+'">'+SOS_Calendar.labelDayCurrent+'</button>';
      content += '    </td>\n';
      
      content += '    <td width="15%" background="'+SOS_Calendar.imgTdBg+'" align="center">';
      content += '    <button '+SOS_Calendar.buttonOnMouseEffect+' '+SOS_Calendar.buttonStyle+'type="button" onclick="SOS_Calendar.monthNext();" title="'+SOS_Calendar.labelMonthNext+'">></button>';
      content += '    </td>\n';
      
      content += '    <td width="15%" background="'+SOS_Calendar.imgTdBg+'" align="center">';
      content += '    <button '+SOS_Calendar.buttonOnMouseEffect+' '+SOS_Calendar.buttonStyle+' type="button" onclick="SOS_Calendar.yearNext();" title="'+SOS_Calendar.labelYearNext+'">>></button>';
      content += '    </td>\n';
      
      content += '  </tr>\n';
      content += ' </table>\n';
      content += '\n';
      document.getElementById(SOS_Calendar.id).innerHTML = content;
    }
    catch(x){
      throw new SOS_Exception(x,x.message,'',0);  
    }
  }
  
  /**
  * Formularfeld füllen
  */
  SOS_Calendar.fillForm = function(calDay,calMonth,calYear){
    try{
      window.clearTimeout(SOS_Calendar.timeout);
      if(SOS_Calendar.formElement != null){
        var fieldValue   = '';
        var day     = (String(calDay).length   == 1) ? '0'+String(calDay)   : String(calDay);
        var month   = (String(calMonth).length == 1) ? '0'+String(calMonth) : String(calMonth);
        var year    = String(calYear);
        var hour    = '00';
        var minute  = '00';
        hour    = (SOS_Calendar.currentHour.length    == 1) ? '0'+SOS_Calendar.currentHour    : SOS_Calendar.currentHour;
        minute  = (SOS_Calendar.currentMinute.length  == 1) ? '0'+SOS_Calendar.currentMinute  : SOS_Calendar.currentMinute;
        if(year.length == 4){
          if(SOS_Calendar.fullYear == false){
            if(year.substr(0,1) == '2'){
              year = year.substr(2,4);
            }
          }
        }
        else{
          year = (SOS_Calendar.fullYear) ? '20'+year  : year; 
        }
        var time = '';
        if(SOS_Calendar.dateFormat == null){
            time = (SOS_Calendar.fullDatetime) ? ' '+hour+':'+minute : '';
        }
        else{
            time = (SOS_Calendar.dateFormat.toLowerCase() == 'date') ? '' : ' '+hour+':'+minute;  
        }
        if(SOS_Calendar.returnISO){
          fieldValue = year+'-'+month+'-'+day+time;
        }
        else{
          switch(SOS_Calendar.language.toLowerCase()){
            case 'de'   :
                          fieldValue = day+'.'+month+'.'+year+time;
                        
                          break;
            default
                        :
                          fieldValue = month+'/'+day+'/'+year+time;
          }
        }
        try{
          eval('document.'+SOS_Calendar.formElement+'.focus();');
          eval('document.'+SOS_Calendar.formElement+'.value = fieldValue;');
        }
        catch(x){
          try{
            var a   = SOS_Calendar.formElement.split('.');  
            document.getElementById(a[0]).elements[a[1]].focus();
            document.getElementById(a[0]).elements[a[1]].value = fieldValue;
          }
          catch(x){}  
        }
        if(typeof SOS_Div != 'undefined'){
          SOS_Div.close(SOS_Calendar.id); 
        }
        else{
          document.getElementById(SOS_Calendar.id).style.display = 'none';
        }
        SOS_Calendar.isOpen = false;
        
        if(SOS_Calendar.onFillForm && SOS_Calendar.onFillForm.length > 0){
          eval(SOS_Calendar.onFillForm) 
        }
         
      } 
    }
    catch(x){
      throw new SOS_Exception(x,x.message,'',0);  
    }
  }
  
  /**
  * Hilfsfunktion für Datum 
  * kann gleichzeitig deutsch,englisch und ISO bearbeiten
  */
  SOS_Calendar.setDateFromForm = function(param){
    try{
      SOS_Calendar.setCurrent2Now();
      var matchArray;
      SOS_Calendar.selectedDay    = null;
      SOS_Calendar.selectedMonth  = null;
      SOS_Calendar.selectedYear   = null;
      if(!param || param == null || param.length == 0){
        return;
      }
      //datePatternDE   = /^(\d{1,2})(\.)(\d{1,2})\2(\d{1}|\d{2}|\d{4})(?:\s+\d{1,2}(:\d{1,2}){1,2})?$/;
      //datePatternEN   = /^(\d{1,2})(\/|-)(\d{1,2})\2(\d{1}|\d{2}|\d{4})(?:\s+\d{1,2}(:\d{1,2}){1,2})?$/;
      //datePatternISO  = /^(\d{4})(\-)(\d{2})\2(\d{2})(?:\s+\d{1,2}(:\d{1,2}){1,2})?$/;
      datePatternDE   = /^(\d{1,2})(\.)(\d{1,2})\2(\d{1}|\d{2}|\d{4})/;
      datePatternEN   = /^(\d{1,2})(\/|-)(\d{1,2})\2(\d{1}|\d{2}|\d{4})/;
      datePatternISO  = /^(\d{4})(\-)(\d{2})\2(\d{2})/;
      matchArray   = param.match(datePatternDE); // ist de format?
      if (matchArray == null) {
        matchArray   = param.match(datePatternEN); // ist en format?
        if (matchArray == null) {
          matchArray   = param.match(datePatternISO); // ist iso format?
          if (matchArray == null) {
            return null;
          }
          else{
            matchElements  = new Array(4,3,1);
          }
        }
        else{
          matchElements  = new Array(3,1,4);
        }
      }
      else{
        matchElements  = new Array(1,3,4);
      }
      var d = 1*matchArray[matchElements[0]];
      var m = 1*matchArray[matchElements[1]]; 
      var y = 0;
      switch(matchArray[matchElements[2]].length){
        case 1  :
                  y = 1*('200'+matchArray[matchElements[2]]);
                  break;
                  
        case 2  :
                  y = 1*('20'+matchArray[matchElements[2]]);
                  break;
                  
        default :
                  y = 1*matchArray[matchElements[2]];
                  break;
      }
      var date                  = new Date(y,m-1,d);
      SOS_Calendar.currentDay   = date.getDate();
      SOS_Calendar.currentMonth = date.getMonth()+1; 
      SOS_Calendar.currentYear  = (date.getYear() < 1900 ) ? date.getYear()+1900 : date.getYear(); // umrechnen fuer Mozilla
      SOS_Calendar.selectedDay    = SOS_Calendar.currentDay;
      SOS_Calendar.selectedMonth  = SOS_Calendar.currentMonth;
      SOS_Calendar.selectedYear   = SOS_Calendar.currentYear;
    }
    catch(x){
      throw new SOS_Exception(x,x.message,'',0);  
    }
  }
  
  /** Stunden:Minuten aktualisieren */
  if(SOS_Calendar.isOpen){
    SOS_Calendar.paintTime();
  }