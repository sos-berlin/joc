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

/**
* SOS_JobGraph: Jobs in einer graphischen Darstellung
*
* @copyright    Software- und Oranisation-Service GmbH, Germany
* @author       Uwe Risse <uwe.risse@sos-berlin.com>
* @since        1.0-2006/08/13
*
* @access       public
* @package      UTIL
*/

  /**
  *
  * @constructor
  * @param    name
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    function SOS_JobGraph(jg) {
      	this.jg = jg;
      	this.order = 0;
      	this.jobs = new Object();
        this.allPreds = new Object();  // Alle Vorgänger eines Jobs.
        this.allSuccs = new Object();  // Alle Nachfolger eines Jobs.
        this.relations = new Object();  // Alle nicht gedruckten Relationen.
        
        this.lookFor = null;
        
        this.JobDefaultWidth = 120;
        this.JobDefaultheight = 40;
        this.JobYDistance = 50;
        this.JobXDistance = 300;

        this.ExitcodeWidth = 90;
        this.ExitcodeHeight = 30;
        
    }

   SOS_JobGraph.prototype.maxY = 0;
   SOS_JobGraph.prototype.aktMmaxY = 100;
   SOS_JobGraph.prototype.yOffset = 0;
   SOS_JobGraph.prototype.moveleft_offset = 0;
   SOS_JobGraph.prototype.moveleft = false;
   SOS_JobGraph.prototype.headersize = 0;
   SOS_JobGraph.prototype.linesize = 16;
    
    
  /** 
  * add-Funktion<br/>
  * Fügt einen Job dem Graphen hinzu.
  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_JobGraph.prototype.add = function(job) { 
    	  this.jobs[job.name] = job;    	
    }    


  /** 
  * setSucc-Funktion<br/>
  * Setzt für alle Nachfolger Jobs die Koordinaten
  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
  
    SOS_JobGraph.prototype.setSucc = function(p,j,aktX,aktY) { 
        if (j.isSucc){
        	this.aktMaxY = 0;
        }
       	 for (var name in j.succs){
       	 
       	 
  
       	       var j2 = j.succs[name];
       	      
 
                if (aktY <= this.aktMaxY && j2.countSuccs >0){
               	  aktY = this.aktMaxY+this.JobYDistance;
               	}

//
    	         if (j2.state==0){  // Noch nicht gezeigt

    	           var p2 = j2.getPoint();
         	       p2.y = aktY;
     	   
            	   aktY = aktY + this.JobYDistance;
                  
            	   aktX = aktX + this.JobXDistance;
            	   p2.x = p.x + aktX;
        	       j2.state=1;
        	       j2.succPrinted = j2.succPrinted+1;

         	       if (p2.y < this.maxY && this.aktMaxY==0){
         	       	p2.y = this.maxY;
        	       }
       	         
        	       
        	       if (p2.y > this.aktMaxY){
         	       	this.aktMaxY = p2.y;
        	       }
        	       if (p2.y > this.maxY){
         	       	this.maxY = p2.y;
        	      }
 
        	      if (j2.countSuccs > 0){
             	       this.setSucc(p,j2,aktX,p2.y);
       	     	  }
       	     	  
       	     	  
// Alle Nachfolger auf der gleichen Stufe zeigen (mehrere addJobs zu einem Exitcode  
                 var p3 = new SOS_Point(p2.x,p2.y);
                  
                 for (var nameS in j2.exitJobs){
               	
                   je = this.jobs[nameS];
                   je.succPrinted = j2.succPrinted+1;
                   var pe = je.getPoint();
                   pe.x = p3.x+5;
                   pe.y=p3.y + this.JobDefaultheight-2;
                   je.state=1;
                   if (pe.y > this.aktMaxY){
         	          	this.aktMaxY = pe.y+5;
        	         }
        	         if (pe.y > this.maxY){
         	       	  this.maxY = pe.y;
        	         }
         	         p3.x = pe.x;
        	         p3.y=pe.y;
                 }         	 
                 
                  aktX = aktX - this.JobXDistance;         	     	  
        	      }
        	     
       	     }
    }
    
    SOS_JobGraph.prototype.generateGraph = function() { 
     var y = this.JobYDistance;
     this.aktMaxY = this.maxY;
     var yExitcode = 0;
     var x = this.JobXDistance;
     for (var name in this.jobs){
     	 var j = this.jobs[name];
   	   if ( (j.state==0 && !j.isSucc )){
      	
  	    var p = j.getPoint();
  	
        if (y <= this.maxY){
         	 y = this.maxY+this.JobYDistance;
        	}
       	 p.y = y;
       	 p.x = this.JobYDistance;
   	  	 j.state=1;
      	 this.setSucc(p,j,0,p.y);
         if (p.y > this.maxY){
         	   this.maxY = p.y;
         }
         
      	 y = this.aktMaxY+this.JobYDistance ;
       }
    }
  }        

 
  /** 
  * setVisible-Funktion<br/>
  * Setzt alle Jobs ohne Bezug zum angegebenen Job auf visible=false 
  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */    
      SOS_JobGraph.prototype.setVisible = function(job) { 
  	   var start=job;
  	   var y = 99999;
  	   
       if (job != undefined){
      	  for (var name in this.jobs){
        	 var j = this.jobs[name];

      	     if (!j.isClone && !j.isSucc){
               if (!j.search(job)){
                	    j.setAllVisible(false);
               }else{
               	if (y > j.getPoint().y){
               	   y = j.getPoint().y
               	  }
               }
     	      }
          }
        }
        // Nach oben verschieben
        var d =  y - this.JobYDistance-this.linesize;
    	  for (var name in this.jobs){
        	 var j = this.jobs[name];        
        	 p = j.getPoint();
           p.y = p.y - d + this.yOffset;
      	}
     }
 SOS_JobGraph.prototype.setMoveleft = function (x){
 	  // Nach links/rechts verschieben
        this.moveleft  = x;
  }
 
 SOS_JobGraph.prototype.textOut = function (f,x,y,s){
   	if (y < 0){
    		y = this.yOffset + this.headersize;
    		this.yOffset = this.yOffset + this.linesize;
 	  }
 	  
 	  
    this.jg.setFont(f.font_name,f.font_size+f.font_unit, f.font_mode);
    this.jg.drawStringRect( s, x , y ,1024,"left");
   }
 
  /** 
  * show-Funktion<br/>
  * Zeigt den Graphen an 
  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_JobGraph.prototype.show = function(jobname) { 
     
    var added=false;   

    for (var name in this.jobs){
     	 var j = this.jobs[name];
      	   j.init();
       
      	 if (j.jobname == jobname){
      	 	this.lookFor = j;
      	}
       j.setDimension(this);
       }
       

     if (this.lookFor != null ){
        this.setVisible(this.lookFor);
      } 
     
     
     	     var m = 0;
     	     for (var name2 in this.jobs){
           	 var jm = this.jobs[name2];

          	 if (jm.visible && jm.showrec && jm.title.length > m){
           	 	m = jm.title.length;
          	}
         	}
         	this.JobDefaultWidth = 8*m;
          this.JobXDistance = this.JobDefaultWidth+ this.ExitcodeWidth+80;
          this.moveleft_offset = this.JobYDistance + this.JobDefaultWidth-25;    
          
          for (var name2 in this.jobs){
           	 var jm = this.jobs[name2];      
           	 jm.setDimension(this);    
           	 jm.init();
           	}
             	this.generateGraph();	
              if (this.lookFor != null ){
                
                this.setVisible(this.lookFor);
              }else{
      	         for (var name2 in this.jobs){
               	 var j = this.jobs[name2];        
               	 p = j.getPoint();
                 p.y = p.y +  this.yOffset;
                 }
             	}      	
                
     
      
     for (var name in this.jobs){ 
     	j = this.jobs[name];
     
      if (j.getPoint().y > this.yOffset && j.visible){
         this.yOffset  = j.getPoint().y
      }
     }
     
     if (this.moveleft){
       for (var name in this.jobs){
        	 var j = this.jobs[name];        
        	 p = j.getPoint();
           p.x = p.x - this.moveleft_offset;
       	}
      }
 
     for (var name in this.jobs){
     	 var j = this.jobs[name];
      	 j.show(this,false);
       }
     for (var name in this.jobs){
     	 var j = this.jobs[name];
     	 j.show(this,true);
       }

       this.jg.paint();
    }
    
/**
* SOS_JobRectangle: Darstellung eines Jobs
*
* @copyright    Software- und Oranisation-Service GmbH, Germany
* @author       Uwe Risse <uwe.risse@sos-berlin.com>
* @since        1.0-2006/08/13
*
* @access       public
* @package      UTIL
*/

  /**
  *
  * @constructor
  * @param    caption
  * @param    p  
  * @param    width    
  * @param    height  
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    function SOS_JobRectangle(caption, p, width, height ) {
      	this.rectangle=new SOS_Rectangle(caption, p, width, height);
        this.rectangle.backgroundColour="#ffffff";
        this.rectangle.shadow=true;
    }

  /** 
  * getPoint-Funktion<br/>
  * Liefert Koordinaten des Rechtecks.

  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
   SOS_JobRectangle.prototype.getPoint = function() { 
   	return this.rectangle.point;
  } 
    
  /** 
  * show-Funktion<br/>
  * Zeigt das Rechteck an.

  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_JobRectangle.prototype.show = function(jg) { 
       this.rectangle.show(jg);
    }    
    
  /** 
  * setStrokeWidth-Funktion<br/>
  * Setzt im Rechteck die Linienbreite

  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_JobRectangle.prototype.setStrokeWidth = function(s) { 
       this.rectangle.stroke_width=s;
    }            
    
  /** 
  * setShadow-Funktion<br/>
  * Setzt im Rechteck den Schatten

  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_JobRectangle.prototype.setShadow = function(s) { 
       this.rectangle.shadow=s;
    }                
  /** 
  * setBackgroundColour-Funktion<br/>
  * Setzt im Rechteck die Farbe des Hintergrundes

  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_JobRectangle.prototype.setBackgroundColour = function(c) { 
       this.rectangle.backgroundColour=c;
    }                    
   
/**
* SOS_Rectangle: Darstellung eines Rechtecks
*
* @copyright    Software- und Oranisation-Service GmbH, Germany
* @author       Uwe Risse <uwe.risse@sos-berlin.com>
* @since        1.0-2006/08/13
*
* @access       public
* @package      UTIL
*/

  /**
  *
  * @constructor
  * @param    caption
  * @param    x  
  * @param    y     
  * @param    width    
  * @param    height  
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    function SOS_Line(from,to)  {
      	this.stroke_width=1;
      	this.stroke_colour="#000000";
        this.from=from;
        this.to=to;
    }

    
  /** 
  * show-Funktion<br/>
  * Zeigt das Rechteck an.

  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_Line.prototype.show = function(jg) { 
    	  
    	 jg.setStroke(this.stroke_width);
       jg.setColor(this.stroke_colour); 
       jg.drawLine(this.from.x, this.from.y, this.to.x, this.to.y);

    } 
    
/**
* SOS_Point: Ein Punkt
*
* @copyright    Software- und Oranisation-Service GmbH, Germany
* @author       Uwe Risse <uwe.risse@sos-berlin.com>
* @since        1.0-2006/08/13
*
* @access       public
* @package      UTIL
*/

  /**
  *
  * @constructor
  * @param    x  
  * @param    y     
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    function SOS_Point(x,y)  {
    	 
      	this.x=x;
      	this.y=y;
    }

/**
* SOS_Rectangle: Darstellung eines Rechtecks
*
* @copyright    Software- und Oranisation-Service GmbH, Germany
* @author       Uwe Risse <uwe.risse@sos-berlin.com>
* @since        1.0-2006/08/13
*
* @access       public
* @package      UTIL
*/

  /**
  *
  * @constructor
  * @param    caption
  * @param    x  
  * @param    y     
  * @param    width    
  * @param    height  
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    function SOS_Rectangle(caption, p, width,height ) {
    	 
       	this.stroke=0;
      	this.stroke_width=1;
      	this.stroke_colour="#000000";
      	this.font_name="courier";
      	this.font_name="lucida console";
      	this.font_size="8"
      	this.font_unit="pt";
      	this.font_mode= Font.PLAIN;
      	this.font_colour="#000000";
      	this.text_align="left";
      	this.shadow = false;
      
        this.caption=caption;
        this.width=width;
        this.height=height;
        this.point = p;
        this.offset_x = 5;
        this.offset_y = 5;
        this.backgroundColour="#ffffff";
    }

  
    
  /** 
  * show-Funktion<br/>
  * Zeigt das Rechteck an.

  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_Rectangle.prototype.show = function(jg) { 
    	  
    	  var align = this.text_align;
    	  if (this.text_align=="center"){
           this.offset_x=0;
           this.offset_y=this.height/2-this.font_size;      
    	  }

    	  if (this.text_align=="centerX"){
           this.offset_x=0;
           align = "center";
     	  }

    	  if (this.text_align=="centerY"){
           this.offset_y=this.height/2-this.font_size;      
           align = "left";
     	  }
     	
     	 if (this.shadow) {
     	 	  add = 1;
     	 	  this.stroke_width = this.stroke_width+1;
     	 } else{
     	 	   add = this.stroke_width;
     	 }
     	 
    	 jg.setStroke(this.stroke_width);
 

       jg.setColor(this.stroke_colour); 
       jg.drawRect(this.point.x, this.point.y, this.width, this.height);       
       jg.setColor(this.backgroundColour); 
       jg.fillRect(this.point.x+add, this.point.y+add, this.width-add, this.height-add);


       
       //print caption
       jg.setColor(this.font_colour); 
       jg.setFont(this.font_name,this.font_size+this.font_unit, this.font_mode);
//       jg.drawStringRect( this.caption+this.point.x + "/" + this.point.y,this.point.x+this.offset_x, this.point.y+this.offset_y,this.width,align);
      s = this.caption;
//      s=s.replace(/[_]/g,"_<br/>");
      jg.drawStringRect(s,this.point.x+this.offset_x, this.point.y+this.offset_y,this.width,align);

    }
    
/**
* SOS_JobRectangle: Darstellung eines Jobs
*
* @copyright    Software- und Oranisation-Service GmbH, Germany
* @author       Uwe Risse <uwe.risse@sos-berlin.com>
* @since        1.0-2006/08/13
*
* @access       public
* @package      UTIL
*/

  /**
  *
  * @constructor
  * @param    caption
  * @param    p
  * @param    width    
  * @param    height  
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    function SOS_ExitCodeRectangle(caption, p,width,height ) {
    
      	this.rectangle=new SOS_Rectangle(caption, p, width, height);
        this.rectangle.backgroundColour="#ceecfd";
        this.rectangle.text_align="center";
 
    }

 
    
  /** 
  * show-Funktion<br/>
  * Zeigt das Rechteck an.

  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_ExitCodeRectangle.prototype.show = function(jg) { 
       this.rectangle.show(jg);
    }    
    
    
  /** 
  * setStrokeWidth-Funktion<br/>
  * Setzt im Rechteck die Linienbreite

  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_ExitCodeRectangle.prototype.setStrokeWidth = function(s) { 
       this.rectangle.stroke_width=s;
    }
    
/**
* SOS_Font: Ein Font
*
* @copyright    Software- und Oranisation-Service GmbH, Germany
* @author       Uwe Risse <uwe.risse@sos-berlin.com>
* @since        1.0-2006/08/13
*
* @access       public
* @package      UTIL
*/

  /**
  *
  * @constructor
  * @param    name
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    function SOS_Font(name ) {
       	this.font_name=name;
       
      	this.font_size="8"
      	this.font_unit="pt";
      	this.font_mode= Font.PLAIN;
      	this.font_colour="#000000";    
    }

/**
* SOS_JobChain: Eine Jobkette
*
* @copyright    Software- und Oranisation-Service GmbH, Germany
* @author       Uwe Risse <uwe.risse@sos-berlin.com>
* @since        1.0-2006/08/13
*
* @access       public
* @package      UTIL
*/

  /**
  *
  * @constructor
  * @param    name
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    function SOS_JobChain(name ) {
      	this.name=name;
        this.nodes = new Object();
    }

 
    
  /** 
  * add-Funktion<br/>
  * Einen Knoten hinzufügen

  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_JobChain.prototype.add = function(node) { 
    copies = 0;	
     for (var name in this.nodes){
    	  n = this.nodes[name];
    		if (n.job==node.job){
    			copies = copies + 1;
    		}
    	}
  
    	if (node.job != "Endnode" && node.job != "File Sink" && copies > 0)node.job = node.job + "/" + copies;
    	this.nodes[node.state + ":" + node.job] = node;
    }    
    
  /** 
  * getJobname-Funktion<br/>
  * Liefert den Namen des Jobs mit dem angegebenen state

  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_JobChain.prototype.getJobname = function(state) { 
    	var erg = "" ;
    	for (var name in this.nodes){
    	 n = this.nodes[name];
       
    		if (n.state==state){
     			erg=n.job;
    		}
    	}
    	return erg;
    }    
      
     
  /** 
  * getJob -Funktion<br/>
  * Liefert den Jobs mit dem angegebenen next_state

  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_JobChain.prototype.getNodeBySuccState = function(state) { 
    	var erg = null;
    	for (var name in this.nodes){
     		var n = this.nodes[name];
    		if (n.next_state==state ){
    		 		erg=n;
    		}
    	}
    	return erg;
    }
     
    
  /** 
  * getJob -Funktion<br/>
  * Liefert den Jobs mit dem angegebenen state

  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_JobChain.prototype.getNodeByState = function(state) { 
    	var erg = null;
    	for (var name in this.nodes){
    		var n = this.nodes[name];
    		if (n.state==state){
     			erg=n;
    		}
    	}
    	return erg;
    }    
                      
     
  /** 
  * sortJobchain<br/>
  * Sortiert die Knoten aufsteigend nach Reihenfolge ihrer Verarbeitung. 
  * 

  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2007/03/08
  */
    SOS_JobChain.prototype.sortNodes = function() { 
    	var erg = "" ;
    	var tNodes = new Object();
    	var aktState = null;
   	
   		for (var name in this.nodes){
    		var n = this.nodes[name];
    		if (this.getNodeBySuccState(n.state)==null && aktState==null) {
        	aktState = n.state;
      		}
    	}
    
      if (aktState != null){
         do {
         	var j = this.getNodeByState(aktState);
          j.sorted=true;
    	    tNodes[j.state + ":" + j.job] = j;
         	n = this.getNodeByState(aktState);
        	aktState = n.next_state;
         } while (aktState != null);
       }
    	
    		for (var name in this.nodes){
    		  var n = this.nodes[name];
    	   	if (!n.sorted){
    		     tNodes[n.state + ":" + n.job] = n;
             n = this.getNodeByState(aktState);
      		}
    	  }
    	  
    	this.nodes = tNodes
    }    
        
/**
* SOS_JobChainNode: Ein Knoten in einer Jobkette
*
* @copyright    Software- und Oranisation-Service GmbH, Germany
* @author       Uwe Risse <uwe.risse@sos-berlin.com>
* @since        1.0-2006/08/13
*
* @access       public
* @package      UTIL
*/

  /**
  *
  * @constructor
  * @param    in
  * @param    out  
  * @param    error    
  * @param    job  
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    function SOS_JobChainNode(state, next_state , error,job ) {
				if (error==null){
					error = "";
				}
  
      	this.state=state;
        this.next_state=next_state;
        this.error=error;
        this.job = job;
        this.sorted=false;
    }

/**
* SOS_Job: Ein Job zur graphischen Darstellung
*
* @copyright    Software- und Oranisation-Service GmbH, Germany
* @author       Uwe Risse <uwe.risse@sos-berlin.com>
* @since        1.0-2006/08/13
*
* @access       public
* @package      UTIL
*/

  /**
  *
  * @constructor
  * @param    name
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    function SOS_Job(name,title,jobname) {
   
        this.JobYDistance = 100;
        this.JobXDistance = 400;
 
        this.ExitcodeWidth = 100;
        this.ExitcodeHeight = 30;
       
        this.name = name;
        
        this.title = title;
         
        if (this.title ==  undefined ){
           this.title = name;
        }
         
        var p1 = new SOS_Point(0,0);
        this.jobrectangle = new SOS_JobRectangle(this.title , p1,120,40) ;
        this.state=0; // 1: Koordinaten sind gesetzt.
        this.isSucc = false;   // Ist der Job ein Nachfolger
        this.countSuccs = 0;   // wieviele Nachfolger hat der Job.
        this.countPreds = 0;   // Für wieviele Jobs ist das der Nachfolger
        this.succPrinted = 0;
        this.preds = new Object();     // Alle direktenVorgänger dieses Jobs.
        this.succs = new Object();     // Alle direkten Nachfolger dieses Jobs.
        this.exitcodes = new Object();
        this.visible=true;  // Rechteck und Nachfolger zeigen
        this.showrec=true;  // Das Rechteck nicht zeigen
        this.jobname = jobname;
        
        this.jobchain = "";
        this.first = false;
        if (typeof (this.jobname) == "undefined" ){
        	this.jobname = name;
        }
        
        this.exitJobs=new Object(); // Dieser Job ist ein weiterer zu einem bereits eingetragenen des gleichen Exitcodes (2 addJobs für einen Exitcode)
        this.exitcodes_isEmpty=true;
        this.isClone=false;
    }
    

  /** 
  * init-Funktion<br/>
  * Fügt einen exitcode hinzu.

  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_Job.prototype.init = function(exitCode,job) { 
        	this.state=0;
         	p = this.getPoint();
        	p.x=0;
        	p.y=0;
        }

  /** 
  * clone-Funktion<br/>
  * Fügt eine Clone seiner selbst hinzu.

  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_Job.prototype.clone = function(g) { 
        var newJob = new SOS_Job(this.name + this.countPreds,this.name + '*',this.name);
        newJob.isClone=true;
        g.add(newJob);
        if (newJob.jobname != "File Sink"){
        		newJob.jobrectangle.rectangle.caption = this.jobname + "*";
        		newJob.jobrectangle.rectangle.backgroundColour= "#fbfbfb";
        		newJob.jobrectangle.rectangle.font_mode= Font.BOLD        
        	}
     		g.add(newJob);
     	 
        return newJob;
     }
    
  /** 
  * addExitcode-Funktion<br/>
  * Fügt einen exitcode hinzu.

  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_Job.prototype.addExitcode = function(g,exitCode,job,asOrder,colour) { 
    this.exitcodes_isEmpty=false;
    var found = false;
    var clone = false;

    var n = job.jobname;
    //alert(this.jobname + " --> " + exitCode +  " --> " + job.jobname);
      //  job.exit = exitCode;
      // Einen neuen Job verwenden, wenn der Job schon in den Exitcodes enthalten ist.
         for (var jobname in g.jobs){
 
         	aktj = g.jobs[jobname];
          for (var name in aktj.exitcodes){
                  var jj = aktj.exitcodes[name].job;
                  if (jj.jobname == job.jobname ){
           	           	  if (jj.isClone){
           	           	  	 clone = true;
           	           	  }else{
                         	   found = true;
           	           	  }

                
                  }
            }
          
          for (var name in aktj.exitcodes){
                
                 var jj = aktj.exitcodes[name].job;

                 for (var name2 in jj.exitJobs){
                  
                       var jjj = jj.exitJobs[name2];
           	           if (jjj.jobname == job.jobname){
           	           	  if (jjj.isClone){
           	           	  	 clone = true;
           	           	  }else{
                         	   found = true;
           	           	  }
           	           	  
                
                 }        
               }
          }
        }
        
          job.countPreds = job.countPreds + 1;
          if (found){
              newJob = new SOS_Job(job.name + job.countPreds,job.name + '*',job.name);
              if (newJob.jobname=="File Sink"){
                    newJob.jobrectangle.rectangle.caption = job.name;              
              }  	
              newJob.isClone=true;
              newJob.showrec = job.showrec;
              g.add(newJob);
              job = newJob;
            }
            
          if ((clone || found) && job.jobname != "File Sink"){
           		job.jobrectangle.rectangle.caption = n + "!!!!!*";
           		job.jobrectangle.rectangle.backgroundColour= "#fbfbfb";
           		job.jobrectangle.rectangle.font_mode= Font.BOLD;
          }
        
           var e = new SOS_Exitcode(exitCode,job,asOrder,colour);
           e.endstate = !job.showrec;
           
          this.exitcodes[exitCode] = e;    
          this.countSuccs = this.countSuccs + 1;
          job.isSucc = true;   
          jjj = new SOS_Job(this.name);
          job.preds[job.countPreds] = jjj;
          this.succs[this.countSuccs] = job;
          g.add(this);
       }    

  /** 
  * addOrderstate-Funktion<br/>
  * Fügt einen orderstate für eine Jobkette hinzu.

  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_Job.prototype.addOrderstate = function(g,orderstate,job,colour) { 
    	this.addExitcode(g,orderstate,job,true,colour)
    }


    SOS_Job.prototype.addNextExitcode = function(g,exitCode,job) { 

    var found = false;
    var clone = false;


      //  job.exit = exitCode;
      // Einen neuen Job verwenden, wenn der Job schon in den Exitcodes enthalten ist.
         for (var jobname in g.jobs){
        
         	aktj = g.jobs[jobname];
          for (var name in aktj.exitcodes){
                
                 var jj = aktj.exitcodes[name].job;
                 if ( jj.jobname == job.jobname && !jj.isClone){
       	           	  if (jj.isClone){
           	           	  	 clone = true;
           	           	  }else{
                         	   found = true;
           	           	  }
                      
              }
            }
            
            for (var name in aktj.exitcodes){
                 
                 var jj = aktj.exitcodes[name].job;

                 for (var name2 in jj.exitJobs){
                  
                       var jjj = jj.exitJobs[name2];
           	           if (jjj.jobname == job.jobname ){
          	           	  if (jjj.isClone){
           	           	  	 clone = true;
           	           	  }else{
                         	   found = true;
           	           	  }
                         
                    }        
                }
             }
          }

          job.countPreds = job.countPreds + 1;
          job.isSucc=true;
         	if ((clone || found) && newJob.jobname != "File Sink"){
            	newJob = new SOS_Job( job.name +  job.countPreds,job.name+ '*',job.name);
        			newJob.jobrectangle.rectangle.backgroundColour= "#fbfbfb";
           		newJob.jobrectangle.rectangle.font_mode= Font.BOLD;
            }else{
             	newJob = new SOS_Job( job.name +  job.countPreds,job.name,job.name);
            }
         
              
        	newJob.isClone=true;
          g.add(newJob);
          this.exitcodes[exitCode].job.exitJobs[newJob.name] = newJob;
          g.add(this);          
        }

    SOS_Job.prototype.getPoint = function() { 
      return this.jobrectangle.getPoint();
    }

    SOS_Job.prototype.setPoint = function(p) { 
        this.jobrectangle.rectangle.point=p1;
    }
    SOS_Job.prototype.setStrokeWidth = function(s) { 
        this.jobrectangle.rectangle.stroke_width=s;
    }
    SOS_Job.prototype.getStrokeWidth = function(s) { 
        return this.jobrectangle.rectangle.stroke_width;
    }
    
    

  /** 
  * show-Funktion<br/>
  * Zeigt den Job als Rechteckt.

  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_Job.prototype.show = function(g,showClone) { 
    	if(this.visible && showClone==this.isClone && this.state > 0){
    		 if (this.showrec){
           this.jobrectangle.show(g.jg)
          }
       	 this.showExitcodes(g.jg);
     	}
    }    
    
  /** 
  * connectToExitcode-Funktion<br/>
  * Zeichnet eine Linie zum ersten Exitcode.

  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_Job.prototype.connectToExitcode = function(jg) { 
      if (!this.exitcodes_isEmpty){
        var from = new SOS_Point(0,0);
        var to = new SOS_Point(0,0);
        var p = this.getPoint();
        
        from.x = this.getPoint().x + this.jobrectangle.rectangle.width ;
        from.y = this.getPoint().y + this.jobrectangle.rectangle.height/2 ;
            
        var d = p.x + this.JobXDistance;
        var b = p.x + j.jobrectangle.rectangle.width;
        var x = b+(d-b-this.ExitcodeWidth)/2;

        to.x = x;
        to.y = from.y;
            
        var l = new SOS_Line(from,to);
        l.show(jg);

       }
   }        
    
  /** 
  * connectToJob-Funktion<br/>
  * Zeichnet eine Linie vom Exitcode zum Job

  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_Job.prototype.connectToJob = function(jg,p_exit) { 
     
        var x = p_exit.x + this.ExitcodeWidth;
        var y = p_exit.y + this.ExitcodeHeight/2;
        var from = new SOS_Point(x,y);
     
        p = this.getPoint();
        
        var x = p.x + this.JobXDistance;;
        var to = new SOS_Point(x,y);
        var l = new SOS_Line(from,to);
        l.show(jg);
   }         

  /** 
  * getMaxSuccY-Funktion<br/>
  * Liefert das größte y aller Nachfolger
  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_Job.prototype.getMaxSuccY = function() { 
    	 if (this.countSuccs > 1){
    	 	   m = 0;
    	 	   for (var name in this.exitcodes){
               
     	    	   		 j = this.exitcodes[name].job;
    	 	      		 p = j.getPoint();
    	 	      		 if (m < p.y){
    	 	        		 	 m = p.y;
    	 	    	    	}
    	 	    	   
    	 	    }
    	 	    return m;
    	}else{
    		return 0;
    	}
    }
  /** 
  * showBar-Funktion<br/>
  * Zeigt die senkrechte Linie, sowie die Verbindung zu den weiteren Nachfolgern bei mehr als 2 Nachfolgern an.

  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_Job.prototype.showBar = function(jg) { 
    	 if (this.countSuccs > 1){
    	 	 // Die senkrechte Linie
    	 	 
    	 	    var maxY = this.getMaxSuccY();
    	 	    var p = this.getPoint();
            var y = p.y + this.jobrectangle.rectangle.height/2;
            var d = p.x + this.JobXDistance;
            var b = p.x + j.jobrectangle.rectangle.width;
            var x = b+(d-b-this.ExitcodeWidth)/4;
            var from = new SOS_Point(x,y);
            var to = new SOS_Point(x, maxY+this.jobrectangle.rectangle.height/2);
            var l = new SOS_Line(from,to);
            l.show(jg);
         // Auf dem selben X die waagerechten
            for (var name in this.exitcodes){
                
                 	j = this.exitcodes[name].job;
                	p = j.getPoint();
                	y = p.y;
                	from.x = x;
                	from.y = y+this.jobrectangle.rectangle.height/2;
                	
                	to.x = x+x-b;
                	to.y = y+this.jobrectangle.rectangle.height/2;
                  l = new SOS_Line(from,to);
                  l.show(jg);
              }
             
                        
            
      	}
    }

  /** 
  * showExitcodes-Funktion<br/>
  * Zeigt die Exitcodes des Jobs als Rechteckt.

  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_Job.prototype.showExitcodes = function(jg) { 
 
      var jh = this.jobrectangle.rectangle.height;
      var  y=(jh-this.ExitcodeHeight)/2;
   
      for (var name in this.exitcodes){
      	 
             var e =  this.exitcodes[name];
          	 j = e.job;
             var p = new SOS_Point(0,0);
             var p2 = this.getPoint();
             
             
             var d = p2.x + this.JobXDistance;
             var b = p2.x + j.jobrectangle.rectangle.width;
             
             p.x=b+(d-b)/2 - this.ExitcodeWidth / 2;
             p.y=j.getPoint().y+y;
  
             var exitcode = new SOS_ExitCodeRectangle(name, p,this.ExitcodeWidth,this.ExitcodeHeight );
             
             if (e.asOrder){
              	if (typeof e.colour != "undefined"){
                	exitcode.rectangle.backgroundColour = e.colour;
                }
                if (e.endstate){
                	exitcode.rectangle.stroke_width = 2;
                	
                  var xx = p2.x + this.JobXDistance;
                  var yy = p.y  ;
                  var from = new SOS_Point(xx,yy);
      
                  yy = yy + this.ExitcodeHeight;
                  var to = new SOS_Point(xx,yy);
                  var l = new SOS_Line(from,to);
                  l.stroke_width=4;

                  l.show(jg);                	
                	
                }
             }
             //alert(j.name + "-" + j.getPoint().x + ":" + j.getPoint().y + "-> " +  name + "," + p.x + "," + p.y + "," + this.ExitcodeWidth + "," + this.ExitcodeHeight + ")");
             exitcode.show(jg);
             this.connectToJob(jg,p);
            
        }
        
        this.connectToExitcode(jg);
        this.showBar(jg);
    }    


    

  /** 
  * setDimension-Funktion<br/>
  * Setzt Standardwerte der aktuellen Graphik

  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_Job.prototype.setDimension = function(g) { 
     
       this.jobrectangle.rectangle.width = g.JobDefaultWidth;
       this.jobrectangle.rectangle.height = g.JobDefaultheight;

       this.JobYDistance = g.JobYDistance;
       this.JobXDistance = g.JobXDistance;

       this.ExitcodeWidth = g.ExitcodeWidth;
       this.ExitcodeHeight = g.ExitcodeHeight;

    }    
  /** 
  * search-Funktion<br/>
  * Such job in allen Nachkommen von this

  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_Job.prototype.search = function(job) { 
 
    	 var found = false;
    	 if (job.jobname == this.jobname){
    	 	return true;
    	}
       for (var name2 in this.exitcodes){
       	  
         	 j = this.exitcodes[name2].job;
         	 for (var name3 in j.exitJobs){
             	 j2 = j.exitJobs[name3];       
             	     
        	 
         	     if (j2.jobname == job.jobname || found){
                	 found=true;
          	       break;          	     
         	 	       }
       	 	      }
            
      }
      	 		
    	 
    	 
    	 if (!found){
       	 for (var name in this.succs){
         	 j = this.succs[name];        	 	 
       	 	 ;
         	 if (j.jobname == job.jobname || found){
         	 	 found=true;
         	 	 break;
        	 	}else{
       	  		found = j.search(job);
        	 	}
          }
        }
       
       return found;
    	 
   }

  /** 
  * search-Funktion<br/>
  * Setzt alle Nachkommen von this auf visible=x

  * @return   
  * @type     String
  * @author   Uwe Risse <uwe.risse@sos-berlin.com>
  * @version  1.1-2006/02/13
  */
    SOS_Job.prototype.setAllVisible = function(visible) { 
       this.visible = false;
    	 for (var name in this.exitcodes){
       	 j = this.exitcodes[name].job;
      	 for (var name2 in j.exitJobs){
      	 	 jj = j.exitJobs[name2];
         	 jj.visible =false;
        	}
       	}

    	 for (var name in this.succs){
       	 j = this.succs[name];
      	 j.setAllVisible(false);
       	}
    	this.exitcodes=new Object();
    	this.exitJobs=new Object();
    	
    	 
   }


/**
* SOS_Job: Ein Job zur graphischen Darstellung
*
* @copyright    Software- und Oranisation-Service GmbH, Germany
* @author       Uwe Risse <uwe.risse@sos-berlin.com>
* @since        1.0-2006/08/13
*
* @access       public
* @package      UTIL
*/
    function SOS_Exitcode(name,job,asOrder,colour) {
     	this.name = name;
    	this.asOrder = asOrder;
    	this.colour = colour;
    	this.endstate=false;
     	this.job = job;
    }
         