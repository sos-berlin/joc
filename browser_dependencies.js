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

// $Id: browser_dependencies.js 3303 2005-01-11 23:22:22Z jz $

// Anpassungen für DOM und XSL-T

if(!navigator.appVersion.match(/\bMSIE\b/)) {

//----------------------------------------------------------------------------------XMLDocument.xml
// Nachbildung von Microsofts DOMDocument.xml

if( window.XMLDocument  &&  !window.XMLDocument.prototype.xml )
{
	XMLDocument.prototype.__defineGetter__
	( 
	    "xml", 
	    
	    function()
	    {
		      return new XMLSerializer().serializeToString( this );
	    } 
	);
}
	
//--------------------------------------------------------------------------------------Element.xml
// Nachbildung von Microsofts IXMLDOMElement.xml

if( window.Element  &&  !window.Element.prototype.xml )
{
	window.Element.prototype.__defineGetter__
	( 
	    "xml", 
	    
	    function()
	    {
		      return new XMLSerializer().serializeToString( this );
	    } 
	);
}
	
//---------------------------------------------------------------------XMLDocument.selectSingleNode
// Nachbildung von Microsofts DOMDocument.selectSingleNode

if( window.XMLDocument  &&  !window.XMLDocument.prototype.selectSingleNode )
{
    window.XMLDocument.prototype.selectSingleNode = function( path )
    {
        return this.evaluate( path, this, null, 0, null ).iterateNext();
    }
}

//-------------------------------------------------------------------------Element.selectSingleNode
// Nachbildung von Microsofts IXMLDOMElement.selectSingleNode

if( window.Element  &&  !window.Element.prototype.selectSingleNode )
{
    window.Element.prototype.selectSingleNode = function( path )
    {
        return this.ownerDocument.evaluate( path, this, null, 0, null ).iterateNext();
    }
}

//--------------------------------------------------------------------------XMLDocument.selectNodes
// Nachbildung von Microsofts DOMDocument.selectNodes

if( window.XMLDocument  &&  !window.XMLDocument.prototype.selectNodes )
{
    window.XMLDocument.prototype.selectNodes = function( path )
    {
        return this.documentElement.selectNodes( path );
    }
}

//------------------------------------------------------------------------------Element.selectNodes
// Nachbildung von Microsofts IXMLDOMElement.selectNodes

if( window.Element  &&  !window.Element.prototype.selectNodes )
{
    window.Element.prototype.selectNodes = function( path )
    {
        var result = new Array();
        var xslt_result_set = this.ownerDocument.evaluate( path, this, null, 0, null );
        for( var next = xslt_result_set.iterateNext(); next; next = xslt_result_set.iterateNext() )  result.push( next );
        return result;
    }
}

//------------------------------------------------------------------------XMLDocument.transformNode
// Nachbildung von Microsofts DOMDocument.transformNode

if( window.XMLDocument  &&  !window.XMLDocument.prototype.transformNode )
{
    window.XMLDocument.prototype.transformNode = function( stylesheet_dom_document )
    {
        var xslt_processor = new XSLTProcessor();
        xslt_processor.importStylesheet( stylesheet_dom_document );
		    return new XMLSerializer().serializeToString( xslt_processor.transformToDocument( this ) );
    }
}

} // end of navigator restriction

//-------------------------------------------------------------------------------------------------
