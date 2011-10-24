<?xml version='1.0' encoding="utf-8"?> 
<!--
/********************************************************* begin of preamble
**
** Copyright (C) 2003-2011 Software- und Organisations-Service GmbH. 
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
//-->
<xsl:stylesheet xmlns:xsl   = "http://www.w3.org/1999/XSL/Transform" 
                version     = "1.0">
    <xsl:output method="xml" encoding="UTF-8" omit-xml-declaration="yes"/>
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Gesamtsicht-->
    <!-- Fuer Antwort auf <show_state> -->

    <xsl:template match="/spooler/answer">
        
        <!--xsl:variable name="is_inactive_backup_scheduler" select="state/cluster/@backup='yes' and not(state/cluster/@active='yes')"/-->
        <xsl:variable name="is_inactive_backup_scheduler" select="state/@state='waiting_for_activation'"/>
    
        <xsl:choose>
          <xsl:when test="/spooler/@my_frame='list'">
            <xsl:choose>
                
                <xsl:when test="$is_inactive_backup_scheduler and /spooler/@my_show_card != 'cluster' and /spooler/@my_show_card != 'remote_schedulers'">
                      <table width="100%" cellpadding="0" cellspacing="0" border="0" class="bottom">
                         <thead>
                             <xsl:call-template name="after_head_space">
                               <xsl:with-param name="colspan" select="'1'"/>
                             </xsl:call-template>
                         </thead>      
                         <tbody><tr><td>
                           <span class="translate" style="font-weight:bold;">Backup Job Scheduler:</span>
                           <xsl:text> </xsl:text>
                           <span class="translate" style="font-weight:bold;">No <xsl:value-of select="translate(/spooler/@my_show_card,'_',' ')"/> found</span>
                         </td></tr></tbody>
                         <tfoot>
                             <xsl:call-template name="after_body_space">
                               <xsl:with-param name="colspan" select="'1'"/>
                             </xsl:call-template>
                         </tfoot>
                     </table>
                </xsl:when>
                
                <xsl:when test="/spooler/@my_show_card='orders'">
                  <!-- orders -->
                  <xsl:apply-templates select="state/job_chains" mode="order_list"/>
                </xsl:when>
                
                <xsl:when test="/spooler/@my_show_card='last_activities'">
                  <!-- last_activities -->
                  <xsl:apply-templates select="state" mode="history"/>
                </xsl:when>
                
                <xsl:otherwise>
                    <!-- jobs, job_chains, schedules, process_classes, locks, remote_schedulers, cluster -->
                    <xsl:apply-templates select="state/child::*[name()=/spooler/@my_show_card]"/>
                </xsl:otherwise>
            
            </xsl:choose>
          </xsl:when>
          
          <xsl:when test="/spooler/@my_frame='tree'">
            <xsl:choose>
                
                <xsl:when test="$is_inactive_backup_scheduler and /spooler/@my_show_card != 'cluster' and /spooler/@my_show_card != 'remote_schedulers'">
                  <div><ul id="{concat(/spooler/@my_show_card,'/')}" class="tree" style="display:block" sos_mode="open"><li>
                    <span class="translate" style="font-weight:bold;">Backup Job Scheduler:</span>
                    <xsl:text> </xsl:text>
                    <span class="translate" style="font-weight:bold;">No <xsl:value-of select="translate(/spooler/@my_show_card,'_',' ')"/> found</span>
                  </li></ul></div>
                </xsl:when>
                
                <xsl:when test="/spooler/@my_show_card='all'">
                  <!-- tree view with all objects (not yet implemented)-->
                  <xsl:apply-templates select="state" mode="tree"/>
                </xsl:when>
                
                <xsl:when test="count(state/folder/descendant::*[name()=/spooler/@my_object_name] | state/folder/folders/folder) = 0">
                  <div><ul id="{concat(/spooler/@my_show_card,'/')}" class="tree" style="display:block" sos_mode="open"><li>
                    <span class="translate" style="font-weight:bold;">No <xsl:value-of select="translate(/spooler/@my_show_card,'_',' ')"/> found</span>
                  </li></ul></div>
                </xsl:when>
                
                <xsl:otherwise>
                    <div>
                      <xsl:apply-templates select="state/folder" mode="tree">
                          <xsl:with-param name="children" select="/spooler/@my_show_card" />
                          <xsl:with-param name="grandchildren" select="/spooler/@my_object_name" />
                      </xsl:apply-templates>
                    </div>
                </xsl:otherwise>
            
            </xsl:choose>
          </xsl:when>
          
          <xsl:when test="/spooler/@my_frame='detail'">
        
            <xsl:choose>
                
                <xsl:when test="state/@detail='job_chain' and state/@detail_path">
                    <!-- nested job_chain -->
                    <xsl:apply-templates select="state//job_chains/job_chain[@path=current()/state/@detail_path]"/>
                </xsl:when>
            
                <xsl:when test="state/@detail='schedule' and state/@detail_path">
                    <!-- schedule -->
                    <xsl:apply-templates select="state//schedules/schedule[@path=current()/state/@detail_path]"/>
                </xsl:when>
                
                <xsl:otherwise>
                    <!-- job, task, job_chain (not nested), order -->
                    <xsl:apply-templates select="child::*[1]"/>
                </xsl:otherwise>
            
            </xsl:choose>
            
          </xsl:when>
          
          <xsl:otherwise>
              <!-- log_categories -->
              <xsl:apply-templates select="child::*[1]"/>
          </xsl:otherwise>
        </xsl:choose>            
        
    </xsl:template>
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~card_selector-->
    <!-- Zeigt einen Selektor an, z.B. Jobs, Jobketten, Prozessklassen -->
    
    <xsl:template mode="card_selector" match="*">
        <xsl:param name="name" />
        <xsl:param name="title"/>
        <xsl:param name="pos" select="''"/>
        <xsl:variable name="class_name">
            <xsl:choose>
              <xsl:when test="@my_show_card=$name">active<xsl:value-of select="$pos"/></xsl:when>
              <xsl:otherwise>inactive<xsl:value-of select="$pos"/></xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        
        <li>
          <xsl:attribute name="onclick">callErrorChecked( 'show_card', '<xsl:value-of select="$name"/>' )</xsl:attribute>
          <xsl:choose>
            <xsl:when test="/spooler/@my_ie_version &gt; 0 and /spooler/@my_ie_version &lt; 7">
               <a href="javascript:void(0);" class="{$class_name}">
                 <span class="translate"><xsl:value-of select="$title"/></span>
               </a>
            </xsl:when>
            <xsl:otherwise>
               <span class="{$class_name}">
                 <span class="translate"><xsl:value-of select="$title"/></span>
               </span>
            </xsl:otherwise>
          </xsl:choose>
        </li>        
    </xsl:template>
    
    
    <!--left-->
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Tree-->
    <xsl:template match="folder" mode="tree">
      <xsl:param    name="children"       select="''"/>
      <xsl:param    name="grandchildren"  select="''"/>
                          
      <ul id="{concat($children,@path)}" style="display:block" sos_mode="open">
        <xsl:if test="@path = '/'">
          <xsl:attribute name="class">tree</xsl:attribute>
        </xsl:if>
        <xsl:if test="@path != '/' and not(contains(substring-after(@path,'/'),'/'))">
          <xsl:attribute name="sos_level">1</xsl:attribute>
        </xsl:if>
        <xsl:for-each select="folders/folder" >
          <xsl:sort select="translate( @path, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending" />
          <li>
            <xsl:choose>
              <xsl:when test="($grandchildren != '' and count(descendant::*[name() = $grandchildren]) &gt; 0 ) or ($grandchildren = '' and count(descendant::*) &gt; 0)">
                <img class="folder" src="explorer_folder_open.gif" onclick="callErrorChecked('toggle_tree_node',this.parentNode);" />
                <span class="bold"><xsl:value-of select="@name" /></span>
                <xsl:apply-templates select="." mode="tree">
                  <xsl:with-param name="children"      select="$children" />
                  <xsl:with-param name="grandchildren" select="$grandchildren" />
                </xsl:apply-templates>
              </xsl:when>
              <xsl:when test = "contains( /spooler/@my_open_folders, concat('|',@path,'|') )">
                <img class="folder" src="explorer_folder_open.gif" onclick="callErrorChecked('toggle_tree_node',this.parentNode);" />
                <span class="bold"><xsl:value-of select="@name" /></span>
                <ul id="{concat($children,@path)}" style="display:none;" sos_mode="closed">&#160;</ul>
              </xsl:when>
              <xsl:otherwise>
                <img class="folder" src="explorer_folder_closed.gif" onclick="callErrorChecked('toggle_tree_node',this.parentNode);" />
                <span class="bold"><xsl:value-of select="@name" /></span>
                <ul id="{concat($children,@path)}" style="display:none;" sos_mode="closed">&#160;</ul>
              </xsl:otherwise>
            </xsl:choose>
          </li>
        </xsl:for-each>
        <xsl:choose>
          <xsl:when test="$children = ''">
            <xsl:apply-templates select="child::*[name() != 'folders']/child::*" mode="leaf">
              <xsl:sort select="translate( @path, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending" />
            </xsl:apply-templates>
            <xsl:if test="count(child::*[name() != 'folders']/child::* | folders/folder) = 0">
              <xsl:call-template name="no_objects_found"/>
            </xsl:if>
          </xsl:when>
          <xsl:when test="$children = 'job_chains'">
            <xsl:apply-templates select="job_chains/job_chain[not(@order_id_space)] | job_chains/job_chain[@order_id_space and job_chain_node.job_chain]" mode="leaf">
              <xsl:sort select="translate( @path, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending" />
            </xsl:apply-templates>
            <xsl:if test="count(job_chains/job_chain[not(@order_id_space)] | job_chains/job_chain[@order_id_space and job_chain_node.job_chain] | folders/folder) = 0">
              <xsl:call-template name="no_objects_found">
                 <xsl:with-param name="children" select="$children" />
              </xsl:call-template>
            </xsl:if>
          </xsl:when>
          <xsl:when test="$children = 'orders'">
            <xsl:apply-templates select="job_chains/job_chain/job_chain_node/order_queue/order" mode="leaf">
              <xsl:sort select="translate( @path, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending" />
            </xsl:apply-templates>
            <xsl:if test="count(job_chains/job_chain/job_chain_node/order_queue/order | folders/folder) = 0">
              <xsl:call-template name="no_objects_found">
                 <xsl:with-param name="children" select="$children" />
              </xsl:call-template>
            </xsl:if>
          </xsl:when>
          <xsl:otherwise>
            <xsl:apply-templates select="child::*[name() = $children]/child::*" mode="leaf">
              <xsl:sort select="translate( @path, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending" />
            </xsl:apply-templates>
            <xsl:if test="count(child::*[name() = $children]/child::* | folders/folder) = 0">
              <xsl:call-template name="no_objects_found">
                 <xsl:with-param name="children" select="$children" />
              </xsl:call-template>
            </xsl:if>
          </xsl:otherwise>
        </xsl:choose>
      </ul>
            
    </xsl:template>
    
    
    <xsl:template name="no_objects_found">
      <xsl:param name="children" select="'objects'" />
      <li class="tree">
        <div style="cursor:auto;">
          <span class="status">&#160;</span>
          <span class="translate" style="font-weight:bold;color:gray;">No <xsl:value-of select="translate($children,'_',' ')"/> found</span>
        </div>
      </li>
    </xsl:template>
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Tree-Leaf for Job-->
    <xsl:template match="job" mode="leaf">
      <xsl:variable name="icon_color">
        <xsl:choose>
          <xsl:when test="@enabled = 'no'">gray</xsl:when>
          <xsl:when test="file_based/ERROR or file_based/removed or replacement">crimson</xsl:when>
          <xsl:when test="file_based/requisites/requisite/@is_missing = 'yes'">crimson</xsl:when>
          <xsl:when test="@remove = 'yes'">crimson</xsl:when>
          <xsl:when test="lock.requestor/lock.use/@is_missing = 'yes'">crimson</xsl:when>
          <xsl:when test="@delay_until">darkorange</xsl:when>
          <xsl:when test="@state = 'pending' or @state = 'initialized' or @state = 'loaded'">gold</xsl:when>
          <xsl:when test="@state = 'running'">forestgreen</xsl:when>
          <xsl:when test="@state = 'not_initialized'">gray</xsl:when>
          <xsl:when test="not(@state)">white</xsl:when>
          <xsl:otherwise>crimson</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="font_color">
        <xsl:choose>
          <xsl:when test="$icon_color = 'crimson'">crimson</xsl:when>
          <xsl:when test="$icon_color = 'darkorange'">crimson</xsl:when>
          <xsl:when test="$icon_color = 'gray'">gray</xsl:when>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="icon_title">
        <xsl:choose>
          <xsl:when test="@enabled = 'no'">disabled</xsl:when>
          <xsl:when test="file_based/ERROR or file_based/removed or replacement">error</xsl:when>
          <xsl:when test="file_based/requisites/requisite/@is_missing = 'yes'">error</xsl:when>
          <xsl:when test="@remove = 'yes'">error</xsl:when>
          <xsl:when test="lock.requestor/lock.use/@is_missing = 'yes'">error</xsl:when>
          <xsl:when test="@delay_until">delayed after error</xsl:when>
          <xsl:when test="@state"><xsl:value-of select="@state" /></xsl:when>
          <xsl:when test="not(@state)">none</xsl:when>
          <xsl:otherwise>error</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <li class="tree">
        <div title="show job details">
          <xsl:attribute name="onclick">show_job_details( '<xsl:value-of select="@path"/>' )</xsl:attribute>
          <xsl:attribute name="oncontextmenu">job_menu__onclick( '<xsl:value-of select="@path"/>' );return false;</xsl:attribute>
          
          <!--xsl:choose>
            <xsl:when test="@order='yes'">
              <span class="status" title="{$icon_title}" style="font-size:18px;margin:0px 9px 0px 0px;border:0px;{concat('color:',$icon_color)}">&#x25CF;</span>
            </xsl:when>
            <xsl:otherwise-->
              <span class="status" title="{$icon_title}" style="{concat('background-color:',$icon_color)}">&#160;</span>
            <!--/xsl:otherwise>
          </xsl:choose-->
          <span class="bold" style="{concat('color:',$font_color)}"><xsl:value-of select="@name" /></span>
          
          <xsl:if test="@state = 'stopped'">
            <xsl:text>&#160;&#160;- &#160;</xsl:text><span class="red_label">stopped</span>
          </xsl:if>
          <xsl:if test="tasks/task[not( @id )] and @waiting_for_process='yes'">
            <xsl:text>&#160;&#160;- &#160;</xsl:text><span class="red_label">needs process</span>
          </xsl:if>
          <xsl:if test="@order='yes' and order_queue/@length &gt; 0">
            <span>&#160;&#160;- &#160;<span class="green_label">Orders</span><span class="green_value">: </span><span class="green_value"><xsl:value-of select="order_queue/@length"/></span></span>
          </xsl:if>
          <xsl:if test="@next_start_time">
            <span>&#160;&#160;- &#160;<span class="green_label">Next start</span><span class="green_value">: </span><span class="green_value"><xsl:apply-templates mode="date_time_nowrap" select="@next_start_time__xslt_datetime_with_diff"/></span></span>
          </xsl:if>
          <xsl:if test="@title">
            <span>&#160;&#160;- &#160;<xsl:apply-templates select="@title"/></span>
          </xsl:if>
          
          <xsl:if test="ERROR or file_based/ERROR or replacement or file_based/removed/ERROR or file_based/requisites/requisite/@is_missing='yes' or lock.requestor/lock.use/@is_missing='yes'">
            <ul style="margin-left:31px;">
              <li><xsl:apply-templates mode="file_based_line" select="."/></li>
            </ul>
          </xsl:if>
          <xsl:if test="/spooler/@show_tasks_checkbox and tasks/task">
            <ul style="margin-left:31px;">
                <xsl:apply-templates select="tasks" mode="job_tree"/>
            </ul>
          </xsl:if>
        </div>
      </li>
    </xsl:template>
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Tree-Leaf for job_chain-->
    <xsl:template match="job_chain" mode="leaf">
      <xsl:param name="name" select="@name" />
      <xsl:variable name="icon_color">
        <xsl:choose>
          <xsl:when test="file_based/ERROR or file_based/removed or replacement">crimson</xsl:when>
          <xsl:when test="file_based/requisites/requisite/@is_missing='yes'">crimson</xsl:when>
          <!--xsl:when test="job_chain_node[ @job ] and not(job_chain_node/job)">crimson</xsl:when-->
          <xsl:when test="@state = 'running' or @state = 'active'">gold</xsl:when>
          <xsl:when test="@state = 'not_initialized'">gray</xsl:when>
          <xsl:when test="not(@state)">white</xsl:when>
          <xsl:otherwise>crimson</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="font_color">
        <xsl:choose>
          <xsl:when test="$icon_color = 'crimson'">crimson</xsl:when>
          <xsl:when test="$icon_color = 'gray'">gray</xsl:when>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="icon_title">
        <xsl:choose>
          <xsl:when test="file_based/ERROR or file_based/removed or replacement">error</xsl:when>
          <xsl:when test="file_based/requisites/requisite/@is_missing ='yes'">under_construction</xsl:when>
          <xsl:when test="@state = 'running'">active</xsl:when>
          <xsl:when test="@state"><xsl:value-of select="@state" /></xsl:when>
          <xsl:when test="not(@state)">none</xsl:when>
          <xsl:otherwise>error</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="web_services" select="/spooler/answer/state[1]/http_server/web_service[ @job_chain = current()/@path ]" />
      <xsl:variable name="jobs" select="/spooler/@show_job_chain_jobs_checkbox or job_chain_node.job_chain or (/spooler/@show_job_chain_orders_checkbox and (job_chain_node/order_queue/order or file_order_source))" />
      <xsl:variable name="error" select="ERROR or file_based/ERROR or replacement or file_based/removed/ERROR or file_based/requisites/requisite/@is_missing='yes'" />
      
      <li class="tree">
        <div title="show job chain details">  
          <xsl:attribute name="onclick">show_job_chain_details( '<xsl:value-of select="@path"/>' )</xsl:attribute>
          <xsl:attribute name="oncontextmenu">job_chain_menu__onclick( '<xsl:value-of select="@path"/>', <xsl:value-of select="1+@orders"/>, <xsl:value-of select="1+number(boolean(@order_id_space and job_chain_node.job_chain))"/> );return false;</xsl:attribute>
          
          <span class="status" title="{$icon_title}" style="{concat('background-color:',$icon_color)}">&#160;</span>
          <span class="bold" style="{concat('color:',$font_color)}"><xsl:value-of select="$name" /></span>
          
          <xsl:if test="@orders &gt; 0" >
            <span class="status_text">&#160;&#160;-&#160;
              <span class="label" style="white-space:nowrap;">Orders</span><span class="small">: </span><xsl:value-of select="@orders"/>
            </span>
          </xsl:if>
          <xsl:if test="@title">
            <span>&#160;&#160;-&#160;&#160;<xsl:apply-templates select="@title"/></span>
          </xsl:if>
          
          <xsl:if test="$error or $web_services">
            <ul style="margin-left:31px;">
              <xsl:if test="$web_services">
                <li style="font-size:8pt">
                  <span class="translate">Web service</span><xsl:text> </xsl:text>
                  <xsl:for-each select="$web_services">
                        <xsl:value-of select="@name"/>
                        <xsl:if test="not(position()=last())"><xsl:text>, </xsl:text></xsl:if>
                  </xsl:for-each>
                </li>
              </xsl:if>
              <xsl:if test="$error">
                <li><xsl:apply-templates mode="file_based_line" select="."/></li>
              </xsl:if>
            </ul>
          </xsl:if>
        </div>
      </li>
      
      <xsl:if test="$jobs">
          <xsl:choose>
            <xsl:when test="job_chain_node.job_chain">
              <li class="job_chains">
                <ul style="margin-left:9px;">
                   <xsl:apply-templates mode="leaf" select="job_chain_node.job_chain"/>
                </ul>
              </li>  
            </xsl:when>
            <xsl:otherwise>
              <li class="jobs">
                <table cellspacing="0" cellpadding="0" border="0" width="100%">
                  <colgroup>
                    <col width="1%"/>
                    <col width="1%"/>
                    <col width="96%"/>
                    <col width="1%"/>
                    <col width="1%"/>
                  </colgroup>
                  <xsl:apply-templates mode="leaf" select="job_chain_node[ @job ]"/>
                  <xsl:if test="/spooler/@show_job_chain_orders_checkbox and file_order_source">    
                    <tr>
                      <td colspan="2" style="vertical-align:top;border-right:1px solid #8B919F">
                        <span class="translate" style="white-space:nowrap;">file orders</span>
                      </td>
                      <td colspan="3" style="padding-left:2ex;">
                        <xsl:apply-templates select="file_order_source"/>
                      </td>
                    </tr>
                  </xsl:if>
                  <xsl:if test="/spooler/@show_job_chain_orders_checkbox and blacklist/@count &gt;= 1">
                    <tr><td colspan="5" style="line-height: 1ex">&#160;</td></tr>
                    <tr>
                        <td style="vertical-align:top;border-right:1px solid #8B919F">
                          <span class="translate" style="white-space:nowrap;">blacklist</span>
                        </td>
                        <td colspan="2"></td>
                        <td style="text-align:right;"><xsl:value-of select="blacklist/@count"/></td>
                    </tr>
                    <!--TODO evtl.-->
                    <!--xsl:apply-templates select="blacklist" mode="job_chain_leaf">
                         <xsl:with-param name="orders" select="blacklist/order[ @job_chain = current()/@path ]"/>
                         <xsl:with-param name="max_orders" select="$max_orders"/>
                    </xsl:apply-templates-->                
                  </xsl:if>
                </table>
              </li>
            </xsl:otherwise>
          </xsl:choose>
      </xsl:if>
    </xsl:template>
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Tree-Leaf for job_chain_node.job_chain-->
    <xsl:template match="job_chain_node.job_chain" mode="leaf">
      <xsl:variable name="job_chain" select="/spooler/answer/state//job_chains/job_chain[@path = current()/@job_chain]" />
      <xsl:choose>
        <xsl:when test="$job_chain">
          <xsl:apply-templates select="$job_chain" mode="leaf">
            <xsl:with-param name="name">
              <xsl:apply-templates mode="trim_slash" select="@job_chain" />
            </xsl:with-param>
          </xsl:apply-templates>
        </xsl:when>
        <xsl:otherwise>
          <li class="tree">
            <table cellpadding="0" cellspacing="0" border="0" width="100%">
              <tr>
                <td><span class="status" title="is missing" style="background-color:crimson;">&#160;</span>
                    <span class="bold" style="color:crimson;"><xsl:apply-templates mode="trim_slash" select="@job_chain" /></span>
                </td><td align="right"><span class="red_value">is missing</span></td>
              </tr>
            </table>
          </li>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:template>
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Tree-Leaf for job_chain_node-->
    <!--xsl:template match="job_chain_node | job_chain_node.job_chain" mode="leaf"-->
    <xsl:template match="job_chain_node" mode="leaf"> 
      <xsl:variable name="icon_color">
        <xsl:choose>
          <xsl:when test="not(job)">crimson</xsl:when>
          <xsl:when test="@action='next_state'">darkorange</xsl:when>
          <xsl:when test="@action='stop'">crimson</xsl:when>
          <xsl:when test="job/file_based/ERROR or job/file_based/removed or job/replacement">crimson</xsl:when>
          <xsl:when test="job/file_based/requisites/requisite/@is_missing = 'yes'">crimson</xsl:when>
          <xsl:when test="job/@remove = 'yes'">crimson</xsl:when>
          <xsl:when test="job/lock.requestor/lock.use/@is_missing = 'yes'">crimson</xsl:when>
          <xsl:when test="not(job/@order) or job/@order = 'no'">crimson</xsl:when>
          <xsl:when test="job/@delay_until">darkorange</xsl:when>
          <xsl:when test="job/@state = 'pending' or job/@state = 'initialized' or job/@state = 'loaded'">gold</xsl:when>
          <xsl:when test="job/@state = 'running'">forestgreen</xsl:when>
          <xsl:when test="job/@state = 'not_initialized'">gray</xsl:when>
          <xsl:when test="not(job/@state)">white</xsl:when>
          <xsl:otherwise>crimson</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="font_color">
        <xsl:choose>
          <xsl:when test="$icon_color = 'crimson'">crimson</xsl:when>
          <xsl:when test="$icon_color = 'darkorange'">crimson</xsl:when>
          <xsl:when test="$icon_color = 'gray'">gray</xsl:when>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="icon_title">
        <xsl:choose>
          <xsl:when test="not(job)">is missing</xsl:when>
          <xsl:when test="@action='next_state'">Node is skipped</xsl:when>
          <xsl:when test="@action='stop'">Node is stopped</xsl:when>
          <xsl:when test="job/file_based/ERROR or job/file_based/removed or job/replacement">error</xsl:when>
          <xsl:when test="job/file_based/requisites/requisite/@is_missing = 'yes'">error</xsl:when>
          <xsl:when test="job/@remove = 'yes'">error</xsl:when>
          <xsl:when test="job/lock.requestor/lock.use/@is_missing = 'yes'">error</xsl:when>
          <xsl:when test="not(job/@order) or job/@order = 'no'">not an order job</xsl:when>
          <xsl:when test="job/@delay_until">delayed after error</xsl:when>
          <xsl:when test="job/@state"><xsl:value-of select="job/@state" /></xsl:when>
          <xsl:when test="not(job/@state)">none</xsl:when>
          <xsl:otherwise>error</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="node_action">
        <xsl:apply-templates mode="node_action" select=".">
          <xsl:with-param name="all_states" select="false()" />
        </xsl:apply-templates>
      </xsl:variable>
      <xsl:variable name="colspan">
        <xsl:choose>
          <xsl:when test="$node_action = '' and /spooler/@my_show_card = 'orders'">3</xsl:when>
          <xsl:when test="$node_action = '' or /spooler/@my_show_card = 'orders'">2</xsl:when>
          <xsl:otherwise>1</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      
      <tr class="tree" title="show job details">
        <xsl:attribute name="onclick">show_job_details( '<xsl:value-of select="@job"/>' )</xsl:attribute>
        <xsl:attribute name="oncontextmenu">job_chain_node_menu__onclick( '<xsl:value-of select="@state"/>', '<xsl:value-of select="../@path"/>' );return false;</xsl:attribute>
        
        <td><span class="status" title="{$icon_title}" style="{concat('margin-top:4px;background-color:',$icon_color)}">&#160;</span></td>
        <td><xsl:value-of select="@state"/></td>
        <td colspan="{$colspan}" style="{concat('padding:0px 4px;color:',$font_color)}">
          <xsl:apply-templates mode="trim_slash" select="@job" />
        </td>
        <xsl:if test="$node_action != ''">
          <td style="white-space:nowrap;text-align:right;"><xsl:copy-of select="$node_action" /></td>
        </xsl:if>
        <xsl:if test="not(/spooler/@my_show_card = 'orders')">
          <td style="padding-left:4px;text-align:right;"><xsl:value-of select="order_queue/@length"/></td>
        </xsl:if>
      </tr>
      
      <xsl:if test="job/ERROR or job/file_based/ERROR or job/replacement or job/file_based/removed/ERROR or job/file_based/requisites/requisite/@is_missing='yes' or job/lock.requestor/lock.use/@is_missing='yes'">
        <tr><td colspan="5" style="padding-left:31px;"><xsl:apply-templates mode="file_based_line" select="job"/></td></tr>  
      </xsl:if>
      <xsl:if test="not(/spooler/@my_show_card = 'orders') and /spooler/@show_job_chain_orders_checkbox and order_queue/order">
          <tr><td colspan="5" style="margin:0px;"><table cellspacing="0" cellpadding="0" border="0" width="100%">
            <colgroup>
              <col width="1"/>
              <col width="1"/>
              <col width="*"/>
              <col width="1"/>
              <col width="1"/>
            </colgroup>
          <xsl:apply-templates select="order_queue" mode="job_chain_list">
          <!--xsl:apply-templates select="order_queue" mode="job_chain_leaf"-->
              <xsl:with-param name="max_orders" select="/spooler/@my_max_orders"/>
              <xsl:with-param name="orders"     select="order_queue/order[ @job_chain = current()/parent::job_chain/@path and @state = current()/@state ]"/>
              <xsl:with-param name="treeview"   select="true()"/>
          </xsl:apply-templates>
          </table></td></tr>
      </xsl:if>                  
    </xsl:template>
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Tree-Leaf for order-->
    <!--TODO see above 'todo'-->
    <xsl:template match="order_queue | blacklist | order" mode="job_chain_leaf">
        <xsl:param name="max_orders" select="999999999"/>
        <xsl:param name="state_text" select="false()"/>
        <xsl:param name="orders"/>    
    </xsl:template>
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Tree-Leaf for order-->
    <xsl:template match="order" mode="leaf">
      <xsl:param name="children" select="''" />
      <xsl:param name="icon_color">
        <xsl:choose>
          <xsl:when test="@enabled = 'no'">gray</xsl:when>
          <xsl:when test="file_based/ERROR or file_based/removed or replacement">crimson</xsl:when>
          <xsl:when test="file_based/requisites/requisite/@is_missing='yes'">crimson</xsl:when>
          <xsl:when test="@on_blacklist='yes'">darkorange</xsl:when>
          <xsl:otherwise>gold</xsl:otherwise>
        </xsl:choose>
      </xsl:param>
      <xsl:param name="font_color">
        <xsl:choose>
          <xsl:when test="$icon_color = 'crimson'">crimson</xsl:when>
          <xsl:when test="$icon_color = 'darkorange'">crimson</xsl:when>
          <xsl:when test="$icon_color = 'gray'">gray</xsl:when>
          <xsl:otherwise>black</xsl:otherwise>
        </xsl:choose>
      </xsl:param>
      <xsl:variable name="icon_title">
        <xsl:choose>
          <xsl:when test="@enabled = 'no'">disabled</xsl:when>
          <xsl:when test="file_based/ERROR or file_based/removed or replacement">error</xsl:when>
          <xsl:when test="file_based/requisites/requisite/@is_missing = 'yes'">error</xsl:when>
          <xsl:when test="@on_blacklist='yes'">blacklist</xsl:when>
          <xsl:otherwise></xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="normalized_order_id">
        <xsl:apply-templates mode="normalized_order_id" select="@order" />
      </xsl:variable>
      <xsl:variable name="menu_caller">
         <xsl:choose>
           <xsl:when test="@on_blacklist='yes'">on blacklist</xsl:when>
           <xsl:otherwise>order_queue</xsl:otherwise>
         </xsl:choose>
      </xsl:variable>
      <xsl:variable name="job_chain_path">
        <xsl:choose>
           <xsl:when test="order.job_chain_stack/order.job_chain_stack.entry/@job_chain"><xsl:value-of select="order.job_chain_stack/order.job_chain_stack.entry/@job_chain" /></xsl:when>
           <xsl:otherwise><xsl:value-of select="ancestor::job_chain/@path"/></xsl:otherwise>
         </xsl:choose>
      </xsl:variable>
      
      <li class="tree">
        <div title="show job chain details" style="float:right;">
          <xsl:attribute name="onclick">show_job_chain_details( '<xsl:value-of select="$job_chain_path"/>' )</xsl:attribute>
          <xsl:attribute name="oncontextmenu">order_menu__onclick( '<xsl:value-of select="@job_chain"/>', '<xsl:value-of select="@id"/>', '<xsl:value-of select="$menu_caller"/>' );return false;</xsl:attribute>
          <xsl:apply-templates select="ancestor::job_chain/@state"/>
        </div>
        <div title="show job chain details">
          <!--xsl:attribute name="onclick">show_order_details( '<xsl:value-of select="$normalized_order_id"/>','<xsl:value-of select="@job_chain"/>' )</xsl:attribute-->
          <xsl:attribute name="onclick">show_job_chain_details( '<xsl:value-of select="$job_chain_path"/>' )</xsl:attribute>
          <xsl:attribute name="oncontextmenu">order_menu__onclick( '<xsl:value-of select="@job_chain"/>', '<xsl:value-of select="@id"/>', '<xsl:value-of select="$menu_caller"/>' );return false;</xsl:attribute>
          
          <span class="status" title="{$icon_title}" style="{concat('background-color:',$icon_color)}">&#160;</span>
          <xsl:choose>
            <xsl:when test="@name">
              <span class="bold" style="{concat('color:',$font_color)}"><xsl:value-of select="@name" /></span>
            </xsl:when>
            <xsl:otherwise>
              <span class="bold" style="{concat('color:',$font_color)}"><xsl:value-of select="ancestor::job_chain/@name" />,<xsl:value-of select="@id" /></span>
            </xsl:otherwise>
          </xsl:choose>
          
          <span>&#160;&#160;-&#160;<xsl:apply-templates mode="properties" select="." /></span>
          
          <xsl:if test="@title">
            <span>&#160;&#160;-&#160;&#160;<xsl:apply-templates select="@title"/></span>
          </xsl:if>
          
          <xsl:if test="ERROR or file_based/ERROR or replacement or file_based/removed/ERROR or file_based/requisites/requisite/@is_missing='yes'">
            <ul style="margin-left:31px;">
              <li><xsl:apply-templates mode="file_based_line" select="."/></li>
            </ul>
          </xsl:if>
        </div>
      </li>
      
      <xsl:if test="not(@on_blacklist='yes')">
        <li class="jobs">
          <table cellspacing="0" cellpadding="0" border="0" width="100%">
            <colgroup>
              <col width="1%"/>
              <col width="1%"/>
              <col width="96%"/>
              <col width="1%"/>
              <col width="1%"/>
            </colgroup>
            <xsl:apply-templates mode="leaf" select="ancestor::job_chain_node[ @job ]"/>            
          </table>
        </li>
      </xsl:if>
    </xsl:template>
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Tree-Leaf for schedule-->
    <xsl:template match="schedule | run_time" mode="leaf">
      <xsl:param name="children" select="''" />
      <xsl:param name="icon_color">
        <xsl:choose>
          <xsl:when test="file_based/ERROR or file_based/removed or replacement">crimson</xsl:when>
          <xsl:when test="file_based/requisites/requisite/@is_missing='yes'">crimson</xsl:when>
          <xsl:when test="@active = 'yes'">forestgreen</xsl:when>
          <xsl:when test="@state = 'not_initialized'">gray</xsl:when>
          <xsl:otherwise>gold</xsl:otherwise>
        </xsl:choose>
      </xsl:param>
      <xsl:param name="font_color">
        <xsl:choose>
          <xsl:when test="$icon_color = 'crimson'">crimson</xsl:when>
          <xsl:when test="$icon_color = 'gray'">gray</xsl:when>
          <xsl:otherwise>black</xsl:otherwise>
        </xsl:choose>
      </xsl:param>
      <xsl:variable name="icon_title">
        <xsl:choose>
          <xsl:when test="file_based/ERROR or file_based/removed or replacement">error</xsl:when>
          <xsl:when test="file_based/requisites/requisite/@is_missing = 'yes'">error</xsl:when>
          <xsl:when test="@active='yes'">active</xsl:when>
          <xsl:when test="not(@active) or @active='no'">inactive</xsl:when>
          <xsl:otherwise></xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="substitutes" select="//schedule[@substitute = current()/@path] | self::*[file_based/requisites/requisite/@is_missing='yes']"/>
      <xsl:variable name="used_by" select="schedule.users/schedule.user[@job or @order]"/>
      <xsl:variable name="file_error" select="ERROR or file_based/ERROR or file_based/removed or replacement or file_based/removed/ERROR or file_based/requisites/requisite/@is_missing='yes'"/>
      <li class="tree">
        <div title="show schedule details">
          <xsl:attribute name="onclick">show_schedule_details( '<xsl:value-of select="@path"/>' )</xsl:attribute>
          <xsl:attribute name="oncontextmenu">
            <xsl:choose>
              <xsl:when test="@substitute">
                schedule_menu__onclick( '<xsl:value-of select="@path"/>', '<xsl:value-of select="@substitute"/>', 1, <xsl:value-of select="1 + count(file_based/@file)"/>, '<xsl:value-of select="@title"/>' );
              </xsl:when>
              <xsl:when test="local-name(.) = 'schedule' or local-name(.) = 'run_time'">
                schedule_menu__onclick( '<xsl:value-of select="@path"/>', '/', <xsl:value-of select="1 + count($substitutes) + count($used_by)"/>, <xsl:value-of select="1 + count(file_based/@file)"/>, '<xsl:value-of select="@title"/>' );
              </xsl:when>
            </xsl:choose>  
            return false;
          </xsl:attribute>
          
          <span class="status" title="{$icon_title}" style="{concat('background-color:',$icon_color)}">&#160;</span>
          <span class="bold" style="{concat('color:',$font_color)}"><xsl:value-of select="@name" /></span>
          <xsl:if test="@title">
            <span>&#160;&#160;-&#160;&#160;<xsl:apply-templates select="@title"/></span>
          </xsl:if>
          <xsl:if test="@valid_from">
            <xsl:text>&#160;&#160;- &#160;</xsl:text><span class="small">
              <xsl:apply-templates mode="date_time_nowrap" select="@valid_from__xslt_datetime"/> - 
              <xsl:apply-templates mode="date_time_nowrap" select="@valid_to__xslt_datetime"/></span>
          </xsl:if>
          <xsl:if test="@substitute">
            <span>&#160;&#160;-&#160;&#160;<span class="label" style="font-style:italic;">Substituted schedule</span>: <xsl:apply-templates mode="trim_slash" select="@substitute" /></span>
          </xsl:if>
                
          <xsl:if test="$file_error">
            <ul style="margin-left:31px;">
              <li><xsl:apply-templates mode="file_based_line" select="."/></li>
            </ul>
          </xsl:if>          
        </div>
      </li>
      
      <xsl:if test="($used_by or $substitutes) and not($file_error)">
        <li class="jobs">
          <table cellspacing="0" cellpadding="0" border="0" width="100%">
            <colgroup>
              <col width="1%"/>
              <col width="1%"/>
              <col width="96%"/>
              <col width="1%"/>
              <col width="1%"/>
            </colgroup>
            <xsl:apply-templates select="$substitutes" mode="substitute">
                <xsl:sort select="@valid_from" order="ascending"/>
                <xsl:sort select="@valid_to" order="ascending"/>
            </xsl:apply-templates>
            <xsl:apply-templates select="schedule.users/schedule.user" mode="used_schedules"/>                        
          </table>
        </li>
      </xsl:if>
    </xsl:template>
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Tree-Leaf for process_class and lock-->
    <xsl:template match="process_class | lock" mode="leaf">
      <xsl:param name="children" select="''" />
      <xsl:param name="icon_color">
        <xsl:choose>
          <xsl:when test="file_based/ERROR or file_based/removed or replacement">crimson</xsl:when>
          <xsl:when test="file_based/requisites/requisite/@is_missing='yes'">crimson</xsl:when>
          <xsl:when test="@state = 'not_initialized'">gray</xsl:when>
          <xsl:otherwise>gold</xsl:otherwise>
        </xsl:choose>
      </xsl:param>
      <xsl:param name="font_color">
        <xsl:choose>
          <xsl:when test="$icon_color = 'crimson'">crimson</xsl:when>
          <xsl:when test="$icon_color = 'gray'">gray</xsl:when>
          <xsl:otherwise>black</xsl:otherwise>
        </xsl:choose>
      </xsl:param>
      <xsl:variable name="icon_title">
        <xsl:choose>
          <xsl:when test="file_based/ERROR or file_based/removed or replacement">error</xsl:when>
          <xsl:when test="file_based/requisites/requisite/@is_missing = 'yes'">error</xsl:when>
          <xsl:otherwise></xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <li class="tree">
        <div>
          <span class="status" title="{$icon_title}" style="{concat('background-color:',$icon_color)}">&#160;</span>
          <span class="bold" style="{concat('color:',$font_color)}"><xsl:value-of select="@name" /></span>
        </div>
      </li>
    </xsl:template>
    
    <!--left-->
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Orders-Tab-->
    <!--~~ called by <xsl:template match="/spooler/answer"> -->
    <xsl:template match="job_chains" mode="order_list">
    
        <xsl:variable name="orders" select="job_chain[not(@visible) or @visible='yes']/descendant::order_queue/order"/>
        
          <table cellpadding="0" cellspacing="0" width="100%" border="0" class="bottom">
            
            <colgroup>
              <col width="10"/>
              <col width="*"/>
              <col width="*"/>
              <col width="1%" align="right"/>
              <col width="40" align="right"/>
            </colgroup>
            
            <thead>
                <tr><td colspan="5" class="before_head_space">&#160;</td></tr>
                <tr style="">
                    <td class="head1" style="padding-left:8px;"><span class="translate">State</span></td>
                    <td class="head"><span class="translate" style="white-space:nowrap;">Job chain</span>&#160;/ <span class="translate">Job</span></td>
                    <td class="head"><span class="translate" style="white-space:nowrap;">Next start</span>&#160;/ <span class="translate" style="white-space:nowrap;">Setback</span></td>
                    <td class="head" colspan="2" align="left"><span class="translate" style="white-space:nowrap;">Job chain</span>&#160;/  <span class="translate" style="white-space:nowrap;">Job state</span></td>
                </tr>
                <xsl:call-template name="after_head_space">
                  <xsl:with-param name="colspan" select="'5'"/>
                </xsl:call-template>
            </thead>
                
            <tbody>
              <xsl:if test="count($orders) = 0">
                  <tr><td colspan="5"><span class="translate" style="font-weight:bold;">No orders found</span></td></tr>
              </xsl:if>
              <xsl:choose>
                <xsl:when test="/spooler/@sort_orders_select = 'unsorted'">
                  <xsl:apply-templates mode="list" select="$orders" />
                </xsl:when>
                <xsl:when test="/spooler/@sort_orders_select = 'name_desc'">
                  <xsl:apply-templates mode="list" select="$orders" >
                    <xsl:sort select="translate( @order, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="descending"/>
                    <xsl:sort select="@next_start_time" order="ascending"/>
                  </xsl:apply-templates>
                </xsl:when>
                <xsl:when test="/spooler/@sort_orders_select = 'next_start_time_asc'">
                  <xsl:apply-templates mode="list" select="$orders" >
                    <xsl:sort select="@next_start_time" order="ascending"/>
                    <xsl:sort select="translate( @order, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending"/>
                  </xsl:apply-templates>
                </xsl:when>
                <xsl:when test="/spooler/@sort_orders_select = 'next_start_time_desc'">
                  <xsl:apply-templates mode="list" select="$orders" >
                    <xsl:sort select="@next_start_time" order="descending"/>
                    <xsl:sort select="translate( @order, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending"/>
                  </xsl:apply-templates>
                </xsl:when>
                <xsl:when test="/spooler/@sort_orders_select = 'job_chain_asc'">
                  <xsl:apply-templates mode="list" select="$orders" >
                    <xsl:sort select="@job_chain" order="ascending"/>
                    <xsl:sort select="@next_start_time" order="ascending"/>
                    <xsl:sort select="translate( @order, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending"/>
                  </xsl:apply-templates>
                </xsl:when>
                <xsl:when test="/spooler/@sort_orders_select = 'job_chain_desc'">
                  <xsl:apply-templates mode="list" select="$orders" >
                    <xsl:sort select="@job_chain" order="descending"/>
                    <xsl:sort select="@next_start_time" order="ascending"/>
                    <xsl:sort select="translate( @order, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending"/>
                  </xsl:apply-templates>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:apply-templates mode="list" select="$orders" >
                    <xsl:sort select="translate( @order, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending"/>
                  </xsl:apply-templates>
                </xsl:otherwise>
              </xsl:choose>
            </tbody>
            <tfoot>
              <xsl:call-template name="after_body_space">
                  <xsl:with-param name="colspan" select="'5'"/>
              </xsl:call-template>
            </tfoot>
          </table>
    </xsl:template>
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Orders-Tab-->
    <!--~~~ called by <xsl:template match="job_chains" mode="order_list"> -->
    <xsl:template match="order" mode="list">
        
        <xsl:variable name="menu_caller">
           <xsl:choose>
             <xsl:when test="@on_blacklist='yes'">blacklist</xsl:when>
             <xsl:otherwise>order_queue</xsl:otherwise>
           </xsl:choose>
        </xsl:variable>
        
        <xsl:variable name="normalized_order_id">
           <xsl:apply-templates mode="normalized_order_id" select="@order" />
        </xsl:variable>
        
        <xsl:variable name="job_chain" select="ancestor::job_chain"/>
        <xsl:variable name="job" select="/spooler/answer/state/jobs/job[ @path = current()/@job ] | $job_chain/job_chain_node/job[ @path = current()/@job ] "/>
        <xsl:variable name="job_chain_node" select="$job_chain/job_chain_node[ @state = current()/@state ]"/>
        <xsl:variable name="job_chain_path">
          <xsl:choose>
            <xsl:when test="order.job_chain_stack/order.job_chain_stack.entry/@job_chain"><xsl:value-of select="order.job_chain_stack/order.job_chain_stack.entry/@job_chain" /></xsl:when>
            <xsl:otherwise><xsl:value-of select="$job_chain/@path"/></xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
                
        <xsl:if test="position() &gt; 1">
           <tr><td colspan="5" class="line">&#160;</td></tr>
        </xsl:if>
         
        <tr class="list" title="show order details">
          <td colspan="4">
              <xsl:attribute name="onclick">callErrorChecked( 'show_order_details', '<xsl:value-of select="$normalized_order_id"/>','<xsl:value-of select="@job_chain"/>' )</xsl:attribute>
              <b><xsl:apply-templates mode="trim_slash" select="@order"/></b>
              <xsl:if test="@title">&#160;&#160;- &#160;<xsl:apply-templates select="@title"/></xsl:if>
          </td>
          <td style="padding:0px 2px;text-align:right;" title="Order menu">
             <xsl:call-template name="command_menu">
                 <xsl:with-param name="title"              select="'Order menu'"/>
                 <xsl:with-param name="onclick_call"       select="'order_menu__onclick'"/>
                 <xsl:with-param name="onclick_param1_str" select="@job_chain"/>
                 <xsl:with-param name="onclick_param2_str" select="@order"/>
                 <xsl:with-param name="onclick_param3_str" select="$menu_caller"/>
             </xsl:call-template>
          </td>
        </tr>
        
        <tr class="list" title="show job chain details">
          <td style="padding:0px 2px 0px 8px">
            <xsl:attribute name="onclick">callErrorChecked( 'show_job_chain_details', '<xsl:value-of select="$job_chain_path"/>' )</xsl:attribute>
            <nobr><xsl:value-of select="@state"/></nobr>
          </td>
          <td>
            <xsl:attribute name="onclick">callErrorChecked( 'show_job_chain_details', '<xsl:value-of select="$job_chain_path"/>' )</xsl:attribute>
            <xsl:if test="$job_chain/@state='stopped' or $job_chain/@state='under_construction'">
              <xsl:attribute name="class">red</xsl:attribute>    
            </xsl:if>
            <xsl:apply-templates mode="trim_slash" select="@job_chain"/>
          </td>
          <td>
            <xsl:attribute name="onclick">callErrorChecked( 'show_job_chain_details', '<xsl:value-of select="$job_chain_path"/>' )</xsl:attribute>
            <xsl:apply-templates mode="properties" select="." />
          </td>
          <td>
            <xsl:attribute name="onclick">callErrorChecked( 'show_job_chain_details', '<xsl:value-of select="$job_chain_path"/>' )</xsl:attribute>
            <xsl:apply-templates select="$job_chain/@state"/>
          </td>
          <td style="padding:0px 2px;text-align:right;" title="Job chain menu">
                <xsl:call-template name="command_menu">
                    <xsl:with-param name="title"                select="'Job chain menu'"/>
                    <xsl:with-param name="onclick_call"         select="'job_chain_menu__onclick'"/>
                    <xsl:with-param name="onclick_param1_str"   select="@job_chain"/>
                    <xsl:with-param name="onclick_param2"       select="1+$job_chain/@orders"/>
                    <xsl:with-param name="onclick_param3"       select="1+number(boolean($job_chain/@order_id_space and $job_chain/job_chain_node.job_chain))"/>
                </xsl:call-template>
          </td>
        </tr>
        
        <xsl:if test="not(@on_blacklist='yes') and $job[1]">
          <tr class="list" title="show job details">
            <td>
              <xsl:attribute name="onclick">callErrorChecked( 'show_job_details','<xsl:value-of select="@job"/>' )</xsl:attribute>
              &#160;
            </td>
            <td>
              <xsl:attribute name="onclick">callErrorChecked( 'show_job_details','<xsl:value-of select="@job"/>' )</xsl:attribute>
              <xsl:apply-templates mode="job_path" select="$job[1]">
                 <xsl:with-param name="style" select="''"/>
                 <xsl:with-param name="order_job" select="true()"/>
              </xsl:apply-templates>
            </td>
            <td>
              <xsl:attribute name="onclick">callErrorChecked( 'show_job_details','<xsl:value-of select="@job"/>' )</xsl:attribute>
              <xsl:if test="@task">
                  <span class="green_label">Task</span><span class="green_value">: <xsl:value-of select="@task"/></span>
              </xsl:if>&#160;
            </td>
            <td style="white-space:nowrap">
                <xsl:attribute name="onclick">callErrorChecked( 'show_job_details','<xsl:value-of select="@job"/>' )</xsl:attribute>
                <xsl:apply-templates mode="node_action" select="$job_chain_node">
                   <xsl:with-param name="job" select="$job[1]"/>
                </xsl:apply-templates>
            </td>
            <td style="padding:0px 2px;text-align:right;" title="Job node menu">
                  <xsl:call-template name="command_menu">
                      <xsl:with-param name="title"                select="'Job node menu'"/>
                      <xsl:with-param name="onclick_call"         select="'job_chain_node_menu__onclick'"/>
                      <xsl:with-param name="onclick_param1_str"   select="@state"/>
                      <xsl:with-param name="onclick_param2_str"   select="@job_chain"/>
                  </xsl:call-template>
            </td>
          </tr>
        </xsl:if>
        
        <xsl:if test="ERROR or file_based/ERROR or file_based/removed or replacement or file_based/removed/ERROR or file_based/requisites/requisite/@is_missing='yes'">
          <tr>
              <td colspan="5">
                  <xsl:apply-templates mode="file_based_line" select="."/>
              </td>
          </tr>
      </xsl:if>
    </xsl:template>
    
    <!--left-->
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Schedules-Tab-->
    <xsl:template match="schedules">
      
        <table cellpadding="0" cellspacing="0" width="100%" border="0" class="bottom">
            
          <colgroup>
            <col width="*"/>
            <col width="*"/>
            <col width="20%"/>
            <col width="20%"/>
            <col width="94"/>
          </colgroup>
          
          <thead>
              <tr><td colspan="5" class="before_head_space">&#160;</td></tr>
              <xsl:choose>
                <xsl:when test="count(schedule) = 0">
                </xsl:when>
                <xsl:when test="count(schedule[@substitute]) &gt; 0">
                  <tr>
                    <td class="head1"><span class="translate" style="white-space:nowrap;">Schedule</span></td>
                    <td class="head1">&#160;</td>
                    <td class="head"><span class="translate" style="white-space:nowrap;">Valid from</span></td>
                    <td class="head"><span class="translate" style="white-space:nowrap;">Valid to</span></td>
                    <td class="head1">&#160;</td>
                  </tr>
                </xsl:when>
                <xsl:otherwise>
                  <tr>
                    <td class="head1"><span class="translate" style="white-space:nowrap;">Schedule</span></td>
                    <td class="head1">&#160;</td>
                    <td class="head1">&#160;</td>
                    <td class="head1">&#160;</td>
                    <td class="head1">&#160;</td>
                  </tr>
                </xsl:otherwise>
              </xsl:choose>
              <xsl:call-template name="after_head_space">
                  <xsl:with-param name="colspan" select="'5'"/>
              </xsl:call-template>
          </thead>
          
          <tbody>
              <xsl:choose>
                <xsl:when test="count(schedule) = 0">
                    <tr><td colspan="5"><span class="translate" style="font-weight:bold;">No schedules found</span></td></tr>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:apply-templates select="schedule[not(@substitute)] | run_time[not(@substitute)] | schedule[file_based/requisites/requisite/@is_missing='yes']" mode="list">
                      <xsl:sort select="translate( @path, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending"/>
                  </xsl:apply-templates>
                </xsl:otherwise>
              </xsl:choose>
          </tbody>
          <tfoot>
              <xsl:call-template name="after_body_space">
                  <xsl:with-param name="colspan" select="'5'"/>
              </xsl:call-template>
            </tfoot>
          </table>
    </xsl:template>  
      
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Schedule-->
    <xsl:template match="schedule | run_time" mode="list">
      <xsl:param name="single" select="false()" />
      <xsl:variable name="file_error" select="ERROR or file_based/ERROR or file_based/removed or replacement or file_based/removed/ERROR or file_based/requisites/requisite/@is_missing='yes'"/>
        
      <xsl:if test="position() &gt; 1">
          <tr><td colspan="5" class="line">&#160;</td></tr>
      </xsl:if>
      
      <xsl:variable name="substitutes" select="parent::schedules/schedule[@substitute = current()/@path] | self::*[file_based/requisites/requisite/@is_missing='yes']"/>
      <xsl:variable name="class">
        <xsl:choose>
          <xsl:when test="@active='yes'">task</xsl:when>
          <xsl:when test="$file_error">red</xsl:when>
          <xsl:otherwise></xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      
      <xsl:if test="not($single)">
        <tr class="list">
          <td colspan="4" title="show schedule details">
            <xsl:attribute name="onclick">callErrorChecked( 'show_schedule_details','<xsl:value-of select="@path"/>' )</xsl:attribute>
            <b class="{$class}"><xsl:apply-templates mode="trim_slash" select="@path" /></b>
            <xsl:if test="@title">
              <span>&#160;&#160;-&#160;&#160;<xsl:apply-templates select="@title"/></span>
            </xsl:if>
          </td>
          <td style="text-align:right;padding:0px 2px;" title="Schedule menu">
            <xsl:call-template name="command_menu">
                <xsl:with-param name="title"                select="'Schedule menu'"/>
                <xsl:with-param name="onclick_call"         select="'schedule_menu__onclick'"/>
                <xsl:with-param name="onclick_param1_str"   select="@path"/>
                <xsl:with-param name="onclick_param2_str"   select="'/'"/>
                <xsl:with-param name="onclick_param3"       select="1 + count($substitutes) + count(schedule.users/schedule.user[@job or @order])"/>
                <xsl:with-param name="onclick_param4"       select="1 + count(file_based/@file)"/>
                <xsl:with-param name="onclick_param5_str"   select="@title"/>
            </xsl:call-template>
         </td>
        </tr>
      </xsl:if>
      <xsl:apply-templates select="$substitutes" mode="substitute">
          <xsl:sort select="@valid_from" order="ascending"/>
          <xsl:sort select="@valid_to" order="ascending"/>
          <xsl:with-param name="with_header" select="not($single)"/>
          <xsl:with-param name="list_mode"  select="true()"/>
      </xsl:apply-templates>      
      <xsl:apply-templates select="schedule.users/schedule.user" mode="used_schedules"/>
      
      <xsl:if test="$file_error">
          <tr>
              <td colspan="5">
                  <xsl:apply-templates mode="file_based_line" select="."/>
              </td>
          </tr>
      </xsl:if>
    </xsl:template>
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Schedule (Substitutes)-->
    <xsl:template match="schedule.user" mode="used_schedules">
             <xsl:for-each select="self::*[@job]">
                <xsl:sort select="translate( @job, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending"/>
                <xsl:if test="position() = 1">
                   <tr><td class="small" colspan="5"><span class="label" style="font-style:italic;">Used by jobs</span>:</td></tr>
                </xsl:if>
                <tr title="show job details" class="list">
                  <xsl:attribute name="onclick">callErrorChecked( 'show_job_details','<xsl:value-of select="@job"/>' )</xsl:attribute>
                  <td colspan="5" style="padding-left:20px;">
                    <xsl:apply-templates mode="trim_slash" select="@job" />
                  </td>
                </tr>      
            </xsl:for-each>
            <xsl:for-each select="self::*[@order]">
                <xsl:sort select="translate( @job_chain, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending"/>
                <xsl:sort select="translate( @order, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending"/>
                <xsl:if test="position() = 1">
                   <tr><td class="small" colspan="5"><span class="label" style="font-style:italic;">Used by orders</span>:</td></tr>
                </xsl:if>
                <xsl:variable name="normalized_order_id">
                   <xsl:apply-templates mode="normalized_order_id" select="@order" />
                </xsl:variable>
                <tr title="show order details" class="list">
                  <xsl:attribute name="onclick">callErrorChecked( 'show_order_details','<xsl:value-of select="$normalized_order_id"/>','<xsl:value-of select="@job_chain"/>' )</xsl:attribute>
                  <td colspan="5" style="padding-left:20px;">
                    <xsl:apply-templates mode="trim_slash" select="@order" />
                    <xsl:text> </xsl:text><span class="label">of job chain</span><xsl:text> </xsl:text>
                    <xsl:apply-templates mode="trim_slash" select="@job_chain" />
                  </td>
                </tr>      
            </xsl:for-each>
     </xsl:template>
     
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Schedule (Substitutes)-->
    <xsl:template match="schedule" mode="substitute">
      <xsl:param name="with_header"  select="true()"/>
      <xsl:param name="list_mode"  select="false()"/>
      <xsl:variable name="file_error" select="ERROR or file_based/ERROR or file_based/removed or replacement or file_based/removed/ERROR or file_based/requisites/requisite/@is_missing='yes'"/>
      
      
      <xsl:variable name="class">
        <xsl:choose>
          <xsl:when test="@active='yes'">task</xsl:when>
          <xsl:when test="$file_error">red</xsl:when>
          <xsl:otherwise></xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:if test="position() = 1 and $with_header">
        <tr><td class="small" colspan="5"><span class="label" style="font-style:italic;">Substituted by</span>:</td></tr>
      </xsl:if>
      <xsl:if test="$list_mode">
        <tr class="list" title="show schedule details">
            <td colspan="2">
              <xsl:attribute name="onclick">callErrorChecked( 'show_schedule_details','<xsl:value-of select="@path"/>' )</xsl:attribute>
              <span class="{$class}" style="font-size:8pt;font-family:Courier;">&#8594; </span><b class="{$class}"><xsl:apply-templates mode="trim_slash" select="@path" /></b>
              <xsl:if test="@title">
                 <xsl:text>&#160;&#160;- &#160;</xsl:text><xsl:apply-templates select="@title"/>
              </xsl:if>
            </td>
            <td class="small">
              <xsl:attribute name="onclick">callErrorChecked( 'show_schedule_details','<xsl:value-of select="@path"/>' )</xsl:attribute>
              <xsl:apply-templates mode="date_time_nowrap" select="@valid_from__xslt_datetime"/>
            </td>
            <td class="small">
              <xsl:attribute name="onclick">callErrorChecked( 'show_schedule_details','<xsl:value-of select="@path"/>' )</xsl:attribute>
              <xsl:apply-templates mode="date_time_nowrap" select="@valid_to__xslt_datetime"/>
            </td>
            <td style="text-align:right;padding:0px 2px;" title="Substitute menu">
              <xsl:call-template name="command_menu">
                  <xsl:with-param name="title"                select="'Substitute menu'"/>
                  <xsl:with-param name="onclick_call"         select="'schedule_menu__onclick'"/>
                  <xsl:with-param name="onclick_param1_str"   select="@path"/>
                  <xsl:with-param name="onclick_param2_str"   select="@substitute"/>
                  <xsl:with-param name="onclick_param3"       select="1"/>
                  <xsl:with-param name="onclick_param4"       select="1 + count(file_based/@file)"/>
                  <xsl:with-param name="onclick_param5_str"   select="@title"/>
              </xsl:call-template>
           </td>
        </tr>
      </xsl:if>
      <xsl:if test="not($list_mode)">
        <tr class="tree" title="show schedule details">
            <td colspan="5" style="padding-left:20px;">
              <xsl:attribute name="onclick">callErrorChecked( 'show_schedule_details','<xsl:value-of select="@path"/>' )</xsl:attribute>
              <xsl:attribute name="oncontextmenu">schedule_menu__onclick( '<xsl:value-of select="@path"/>', '<xsl:value-of select="@substitute"/>', 1, <xsl:value-of select="1 + count(file_based/@file)"/>, '<xsl:value-of select="@title"/>' );return false;</xsl:attribute>
              <span class="{$class}"><xsl:apply-templates mode="trim_slash" select="@path" /></span>
              <xsl:if test="@title">
                 <xsl:text>&#160;&#160;- &#160;</xsl:text><xsl:apply-templates select="@title"/>
              </xsl:if>
              <xsl:text>&#160;&#160;- &#160;</xsl:text><span class="small">
              <xsl:apply-templates mode="date_time_nowrap" select="@valid_from__xslt_datetime"/> - 
              <xsl:apply-templates mode="date_time_nowrap" select="@valid_to__xslt_datetime"/></span>
            </td>
        </tr>
      </xsl:if>
      <xsl:if test="$file_error">
          <tr>
              <td colspan="5">
                  <xsl:apply-templates mode="file_based_line" select="."/>
              </td>
          </tr>
      </xsl:if>
      
    </xsl:template>
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Detailsicht eines Schedules-->
    
    <xsl:template match="schedule">
        
      <xsl:variable name="file_error" select="ERROR or file_based/ERROR or file_based/removed or replacement or file_based/removed/ERROR or file_based/requisites/requisite/@is_missing='yes'"/>
      <xsl:variable name="class">
        <xsl:choose>
          <xsl:when test="@active='yes'">task</xsl:when>
          <xsl:when test="$file_error">red</xsl:when>
          <xsl:otherwise></xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      
      <div> <!--google chrome-->
        <div class="top">
          <xsl:call-template name="round_top_corners"/>
          
          <table cellpadding="0" cellspacing="0" width="100%" border="0" class="top">
            <thead>
               <tr>
                  <td colspan="3" class="before_head_space">&#160;</td>
               </tr>
               <tr>
                 <xsl:choose>
                   <xsl:when test="@substitute">
                     <td align="left" valign="top" width="20%">
                           <xsl:call-template name="command_menu">
                               <xsl:with-param name="title"              select="'Substitute menu'"/>
                               <xsl:with-param name="onclick_call"       select="'schedule_menu__onclick'"/>
                               <xsl:with-param name="onclick_param1_str" select="@path"/>
                               <xsl:with-param name="onclick_param2_str" select="@substitute"/>
                               <xsl:with-param name="onclick_param3"     select="1"/>
                               <xsl:with-param name="onclick_param4"     select="1 + count(file_based/@file)"/>
                               <xsl:with-param name="onclick_param5_str" select="@title"/>
                           </xsl:call-template>
                     </td> 
                   </xsl:when>    
                   <xsl:otherwise>
                     <td align="left" valign="top" width="20%">
                           <xsl:call-template name="command_menu">
                               <xsl:with-param name="title"              select="'Schedule menu'"/>
                               <xsl:with-param name="onclick_call"       select="'schedule_menu__onclick'"/>
                               <xsl:with-param name="onclick_param1_str" select="@path"/>
                               <xsl:with-param name="onclick_param2_str" select="'/'"/>
                               <xsl:with-param name="onclick_param3"     select="1 + count(parent::schedules/schedule[@substitute = current()/@path]) + count(schedule.users/schedule.user)"/>
                               <xsl:with-param name="onclick_param4"     select="1 + count(file_based/@file)"/>
                               <xsl:with-param name="onclick_param5_str" select="@title"/>
                           </xsl:call-template>
                     </td>
                   </xsl:otherwise>
                 </xsl:choose>
                 <td align="center" width="60%">
                     <span class="caption">SCHEDULE</span>
                 </td>
                 <xsl:choose>
                   <xsl:when test="@substitute">
                     <td align="right" valign="top" width="20%">
                           <xsl:call-template name="command_menu">
                               <xsl:with-param name="title"              select="'Substitute menu'"/>
                               <xsl:with-param name="onclick_call"       select="'schedule_menu__onclick'"/>
                               <xsl:with-param name="onclick_param1_str" select="@path"/>
                               <xsl:with-param name="onclick_param2_str" select="@substitute"/>
                               <xsl:with-param name="onclick_param3"     select="1"/>
                               <xsl:with-param name="onclick_param4"     select="1 + count(file_based/@file)"/>
                               <xsl:with-param name="onclick_param5_str" select="@title"/>
                           </xsl:call-template>
                     </td> 
                   </xsl:when>    
                   <xsl:otherwise>
                     <td align="right" valign="top" width="20%">
                           <xsl:call-template name="command_menu">
                               <xsl:with-param name="title"              select="'Schedule menu'"/>
                               <xsl:with-param name="onclick_call"       select="'schedule_menu__onclick'"/>
                               <xsl:with-param name="onclick_param1_str" select="@path"/>
                               <xsl:with-param name="onclick_param2_str" select="'/'"/>
                               <xsl:with-param name="onclick_param3"     select="1 + count(parent::schedules/schedule[@substitute = current()/@path]) + count(schedule.users/schedule.user)"/>
                               <xsl:with-param name="onclick_param4"     select="1 + count(file_based/@file)"/>
                               <xsl:with-param name="onclick_param5_str" select="@title"/>
                           </xsl:call-template>
                     </td>
                   </xsl:otherwise>
                 </xsl:choose>
               </tr>
               <tr>
                   <td colspan="3">
                       <b class="{$class}"><xsl:apply-templates mode="trim_slash" select="@path" /></b>
                       <xsl:if test="@title">
                           <xsl:text>&#160;&#160;- &#160;</xsl:text><xsl:apply-templates select="@title"/>
                       </xsl:if>
                   </td>
               </tr>
               <tr><td colspan="3" class="before_head_space">&#160;</td></tr>
            </thead>
          </table>
        </div>  
        <xsl:choose>
          <xsl:when test="@substitute">
            <div class="middle">
              <table cellpadding="0" cellspacing="0" width="100%" border="0" class="middle">
                <colgroup>
                  <col width="*"/>
                  <col width="80"/>
                </colgroup>
                <thead>
                  <xsl:call-template name="after_head_space">
                      <xsl:with-param name="colspan" select="'2'"/>
                  </xsl:call-template>
                </thead>
                <tbody>
                  <tr><td class="small" colspan="2"><span class="label" style="font-style:italic;">Substituted schedule</span>:</td></tr>
                  <tr class="list" title="show schedule details">
                    <td style="padding-left:20px;">
                      <xsl:attribute name="onclick">callErrorChecked( 'show_schedule_details','<xsl:value-of select="@substitute"/>' )</xsl:attribute>
                      <xsl:apply-templates mode="trim_slash" select="@substitute" />
                    </td>
                    <td valign="top" style="text-align:right;padding:0px 2px;" title="Schedule menu">
                         <xsl:call-template name="command_menu">
                             <xsl:with-param name="title"              select="'Schedule menu'"/>
                             <xsl:with-param name="onclick_call"       select="'schedule_menu__onclick'"/>
                             <xsl:with-param name="onclick_param1_str" select="@substitute"/>
                             <xsl:with-param name="onclick_param2_str" select="'/'"/>
                             <xsl:with-param name="onclick_param3"     select="2"/>
                             <xsl:with-param name="onclick_param4"     select="1 + count(parent::schedules/schedule[@path = current()/@substitute]/file_based/@file)"/>
                             <xsl:with-param name="onclick_param5_str" select="parent::schedules/schedule[@path = current()/@substitute]/@title"/>
                         </xsl:call-template>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </xsl:when>
          <xsl:otherwise>
            <div class="middle">
              <table class="middle" cellpadding="0" cellspacing="0" border="0" width="100%">
                <colgroup>    
                  <col width="*"/>
                  <col width="*"/>
                  <col width="20%"/>
                  <col width="20%"/>
                  <col width="94"/>  
                </colgroup>
                <thead>
                    <tr>
                      <td colspan="5" class="before_head_space">&#160;</td>
                    </tr>
                                         
                    <xsl:choose>
                      <xsl:when test="count(parent::schedules/schedule[@substitute = current()/@path]) &gt; 0">
                        <tr>
                          <td class="head1"><span class="translate" style="white-space:nowrap;">Substituted by</span></td>
                          <td class="head1">&#160;</td>
                          <td class="head"><span class="translate" style="white-space:nowrap;">Valid from</span></td>
                          <td class="head"><span class="translate" style="white-space:nowrap;">Valid to</span></td>
                          <td class="head1">&#160;</td>
                        </tr>
                      </xsl:when>
                      <xsl:when test="count(parent::schedules/schedule[@substitute = current()/@path]) = 0">
                      </xsl:when>
                      <xsl:otherwise>
                        <tr>
                          <td class="head1"><span class="translate" style="white-space:nowrap;">Schedule</span></td>
                          <td class="head1">&#160;</td>
                          <td class="head1">&#160;</td>
                          <td class="head1">&#160;</td>
                          <td class="head1">&#160;</td>
                        </tr>
                      </xsl:otherwise>
                    </xsl:choose>                 
                    
                    <xsl:call-template name="after_head_space">
                        <xsl:with-param name="colspan" select="'5'"/>
                    </xsl:call-template>
                </thead>
                
                <tbody>
                  <xsl:apply-templates select="." mode="list">
                    <xsl:with-param name="single" select="true()" />
                  </xsl:apply-templates>
                </tbody>
              </table>
            </div>
          </xsl:otherwise>
        </xsl:choose> 
        
        <div class="bottom">
          <table cellpadding="0" cellspacing="0" width="100%" border="0" class="bottom">
            <colgroup>
              <col width="1%"/>
              <col width="99%"/>
            </colgroup>
            <tbody>
              <tr><td class="before_head_space" colspan="2">&#160;</td></tr>
              <tr>
                   <td valign="top" class="label"><span class="label">State</span>:</td>
                   <td class="small" valign="top">
                     <xsl:choose>
                       <xsl:when test="@active='yes' and file_based/@state='active'"><span class="green_value">active</span></xsl:when>
                       <xsl:when test="file_based/@state != 'active'"><span class="red_label"><xsl:value-of select="file_based/@state"/></span></xsl:when>
                       <xsl:otherwise><span class="translate">inactive</span></xsl:otherwise>
                     </xsl:choose>
                   </td>
              </tr>
              <xsl:if test="@valid_from">
                <tr>
                   <td valign="top" class="label"><span class="label">Valid</span>:</td>
                   <td valign="top">
                     <xsl:apply-templates mode="date_time_nowrap" select="@valid_from__xslt_datetime"/>
                     -
                     <xsl:apply-templates mode="date_time_nowrap" select="@valid_to__xslt_datetime"/>
                   </td>
                </tr>
              </xsl:if> 
              <xsl:apply-templates mode="file_timestamp" select="file_based/@last_write_time__xslt_datetime_zone_support"/>
              <xsl:if test="$file_error">
                  <tr>
                      <td colspan="2">
                          <xsl:apply-templates mode="file_based_line" select="."/>
                      </td>
                  </tr>
              </xsl:if>
            </tbody>
            <tfoot>
              <xsl:call-template name="after_body_space">
                 <xsl:with-param name="colspan" select="'6'"/>
              </xsl:call-template>
            </tfoot>
          </table>
          <xsl:call-template name="round_bottom_corners"/>
        </div>
        </div> <!--google chrome-->
    </xsl:template>
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Schedule in Job/Order Details-->
    
    <xsl:template match="@schedule">
        <xsl:param name="schedule_missing" select="false()" />
        <xsl:param name="colspan" select="'3'" />
        <tr>
            <xsl:if test="not($schedule_missing)">
                <xsl:attribute name="title">show schedule details</xsl:attribute>
                <xsl:attribute name="class">list</xsl:attribute>
                <xsl:attribute name="onclick">callErrorChecked( 'show_schedule_details','<xsl:value-of select="."/>' )</xsl:attribute>
            </xsl:if>
            <td class="label"><span class="label">Run time defined by</span>:</td>
            <td colspan="{$colspan}">
              <xsl:apply-templates mode="trim_slash" select="." />
              <!--xsl:if test="$schedule_missing">
                <xsl:attribute name="class">job_error</xsl:attribute>
                <xsl:text> </xsl:text><span class="translate">is missing</span>
              </xsl:if-->
            </td>
        </tr>
    </xsl:template>
    
    
    <!--left-->
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Last Activities-Tab-->
    <xsl:template match="state" mode="history">
       
        <table cellpadding="0" cellspacing="0" width="100%" border="0" class="bottom">
          <colgroup>    
            <col width="*"/>
            <col width="110"/>
            <col width="110"/>
            <col width="*"/>  
            <col width="1%"/>  
            <col width="40" align="right"/>  
          </colgroup>  
          
          <thead>
              <tr>
                  <td colspan="6" class="before_head_space">&#160;</td>
              </tr>
              <tr>
                  <xsl:choose>
                    <xsl:when test="/spooler/@last_activities_radios = 'all'">
                      <td class="head1"><span class="translate" style="white-space:nowrap;">Order ID</span>&#160;/&#160;<span class="translate" style="color:#808080;white-space:nowrap;">Job name</span></td>
                      <td class="head"><span class="translate">Started</span></td>
                      <td class="head"><span class="translate">Ended</span></td>
                      <td class="head"><span class="translate" style="white-space:nowrap;">Job chain</span>&#160;/&#160;<span class="translate" style="color:#009933;">Cause</span></td>
                      <td class="head" colspan="2"><span class="translate" style="white-space:nowrap;">Order state</span>&#160;/&#160;<span class="translate" style="color:crimson;white-space:nowrap;">Exitcode</span></td>
                    </xsl:when>
                    <xsl:when test="/spooler/@last_activities_radios = 'orders'">
                      <td class="head1"><span class="translate" style="white-space:nowrap;">Order ID</span></td>
                      <td class="head"><span class="translate">Started</span></td>
                      <td class="head"><span class="translate">Ended</span></td>
                      <td class="head"><span class="translate" style="white-space:nowrap;">Job chain</span></td>
                      <td class="head" colspan="2"><span class="translate" style="white-space:nowrap;">Order state</span></td>
                    </xsl:when>
                    <xsl:when test="/spooler/@last_activities_radios = 'tasks'">
                      <td class="head1"><span class="translate" style="white-space:nowrap;">Job name</span></td>
                      <td class="head"><span class="translate">Started</span></td>
                      <td class="head"><span class="translate">Ended</span></td>
                      <td class="head"><span class="translate">Cause</span></td>
                      <td class="head" colspan="2"><span class="translate" style="white-space:nowrap;">Exitcode</span></td>
                    </xsl:when>
                  </xsl:choose>
              </tr>
              <xsl:call-template name="after_head_space">
                  <xsl:with-param name="colspan" select="'6'"/>
              </xsl:call-template>
          </thead>
                 
          <tbody>
            <xsl:variable name="ohistory" select="job_chains/job_chain/order_history/order" />
            <xsl:variable name="thistory" select="jobs/job/history/history.entry" />
            <xsl:variable name="ohistoryerr" select="$ohistory[@state = ancestor::job_chain[@path=$ohistory/@job_chain or substring(@path,2)=$ohistory/@job_chain]/job_chain_node/@error_state]" />
            <xsl:variable name="thistoryerr" select="$thistory/ERROR/parent::history.entry" />
            
            <xsl:choose>
              <xsl:when test="/spooler/@last_activities_radios = 'all'">
                <xsl:choose>
                  <xsl:when test="/spooler/@show_error_checkbox">
                    <xsl:if test="not( $ohistoryerr | $thistoryerr )">
                       <tr><td colspan="6"><span class="translate" style="font-weight:bold;">No last activities found</span></td></tr>
                    </xsl:if>
                    <xsl:for-each select="$ohistoryerr | $thistoryerr">
                      <xsl:sort select="@start_time" order="descending"/>
                      <xsl:if test="/spooler/@my_max_last_activities = 0 or position() &lt;= /spooler/@my_max_last_activities">
                        <xsl:apply-templates select="." mode="history">
                          <xsl:with-param name="highlightning" select="true()"/>
                        </xsl:apply-templates>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:if test="not( $ohistory | $thistory )">
                       <tr><td colspan="6"><span class="translate" style="font-weight:bold;">No last activities found</span></td></tr>
                    </xsl:if>
                    <xsl:for-each select="$ohistory | $thistory">
                      <xsl:sort select="@start_time" order="descending"/>
                      <xsl:if test="/spooler/@my_max_last_activities = 0 or position() &lt;= /spooler/@my_max_last_activities">
                        <xsl:apply-templates select="." mode="history">
                          <xsl:with-param name="highlightning" select="true()"/>
                        </xsl:apply-templates>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:when>
              <xsl:when test="/spooler/@last_activities_radios = 'orders'">
                <xsl:choose>
                  <xsl:when test="/spooler/@show_error_checkbox">
                    <xsl:if test="not( $ohistoryerr )">
                       <tr><td colspan="6"><span class="translate" style="font-weight:bold;">No last activities found</span></td></tr>
                    </xsl:if>
                    <xsl:for-each select="$ohistoryerr">
                      <xsl:sort select="@start_time" order="descending"/>
                      <xsl:if test="/spooler/@my_max_last_activities = 0 or position() &lt;= /spooler/@my_max_last_activities">
                        <xsl:apply-templates select="." mode="history"/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:if test="not( $ohistory )">
                       <tr><td colspan="6"><span class="translate" style="font-weight:bold;">No last activities found</span></td></tr>
                    </xsl:if>
                    <xsl:for-each select="$ohistory">
                      <xsl:sort select="@start_time" order="descending"/>
                      <xsl:if test="/spooler/@my_max_last_activities = 0 or position() &lt;= /spooler/@my_max_last_activities">
                        <xsl:apply-templates select="." mode="history"/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:otherwise>
                </xsl:choose>
             </xsl:when>
              <xsl:when test="/spooler/@last_activities_radios = 'tasks'">
                <xsl:choose>
                  <xsl:when test="/spooler/@show_error_checkbox">
                    <xsl:if test="not( $thistoryerr )">
                       <tr><td colspan="6"><span class="translate" style="font-weight:bold;">No last activities found</span></td></tr>
                    </xsl:if>
                    <xsl:for-each select="$thistoryerr">
                      <xsl:sort select="@start_time" order="descending"/>
                      <xsl:if test="/spooler/@my_max_last_activities = 0 or position() &lt;= /spooler/@my_max_last_activities">
                        <xsl:apply-templates select="." mode="history"/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:if test="not( $thistory )">
                       <tr><td colspan="6"><span class="translate" style="font-weight:bold;">No last activities found</span></td></tr>
                    </xsl:if>
                    <xsl:for-each select="$thistory">
                      <xsl:sort select="@start_time" order="descending"/>
                      <xsl:if test="/spooler/@my_max_last_activities = 0 or position() &lt;= /spooler/@my_max_last_activities">
                        <xsl:apply-templates select="." mode="history"/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:when>
            </xsl:choose>
          </tbody>
          
          <tfoot>
              <xsl:call-template name="after_body_space">
                  <xsl:with-param name="colspan" select="'6'"/>
              </xsl:call-template>
          </tfoot>
        </table>
    </xsl:template>
    
    <!--left-->
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Jobs-Tab-->

    <xsl:template match="jobs">
       
          <table cellpadding="0" cellspacing="0" width="100%" border="0" class="bottom">
            
            <colgroup>
              <col width="40%"/>
              <col width="110"/>  
              <col width="10"  align="right"/>
              <col width="40%"/>
              <col width="80"  align="right"/>
            </colgroup>
            
            <thead>
                <tr><td colspan="5" class="before_head_space">&#160;</td></tr>
                <tr style="">
                    <td class="head1"><span class="translate">Job</span> </td>
                    <td class="head"> <span class="translate">Time</span> </td>
                    <td class="head"> <span class="translate">Steps</span> </td>
                    <td class="head"><span class="translate" style="white-space:nowrap">Next start</span>
                        <xsl:if test="/spooler/@show_jobs_select != 'standalone'">
                            <span>&#160;/&#160;<span class="translate">Orders</span></span>
                        </xsl:if>
                    </td>
                    <td class="head1">&#160;</td>
                </tr>
                <xsl:call-template name="after_head_space">
                  <xsl:with-param name="colspan" select="'5'"/>
                </xsl:call-template>
            </thead>
             
            <!--tbody-->
              <xsl:variable name="jobs" select="job [ not(@visible) or @visible='yes' ]" />
              <xsl:if test="count($jobs) = 0">
                 <tr><td colspan="5"><span class="translate" style="font-weight:bold;">No jobs found</span></td></tr>
              </xsl:if>
              <xsl:choose>
                <xsl:when test="/spooler/@sort_jobs_select = 'unsorted'">
                  <xsl:apply-templates mode="list" select="$jobs" />
                </xsl:when>
                <xsl:when test="/spooler/@sort_jobs_select = 'name_desc'">
                  <xsl:apply-templates mode="list" select="$jobs" >
                    <xsl:sort select="translate( @path, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="descending"/>
                  </xsl:apply-templates>
                </xsl:when>
                <xsl:when test="/spooler/@sort_jobs_select = 'next_start_time_asc'">
                  <xsl:apply-templates mode="list" select="$jobs" >
                    <xsl:sort select="@next_start_time" order="ascending"/>
                    <xsl:sort select="translate( @path, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending"/>
                  </xsl:apply-templates>
                </xsl:when>
                <xsl:when test="/spooler/@sort_jobs_select = 'next_start_time_desc'">
                  <xsl:apply-templates mode="list" select="$jobs" >
                    <xsl:sort select="@next_start_time" order="descending"/>
                    <xsl:sort select="translate( @path, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending"/>
                  </xsl:apply-templates>
                </xsl:when>
                <xsl:when test="/spooler/@sort_jobs_select = 'status_asc'">
                  <xsl:apply-templates mode="list" select="$jobs" >
                    <xsl:sort select="@state" order="ascending"/>
                    <xsl:sort select="translate( @path, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending"/>
                  </xsl:apply-templates>
                </xsl:when>
                <xsl:when test="/spooler/@sort_jobs_select = 'status_desc'">
                  <xsl:apply-templates mode="list" select="$jobs" >
                    <xsl:sort select="@state" order="descending"/>
                    <xsl:sort select="translate( @path, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending"/>
                  </xsl:apply-templates>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:apply-templates mode="list" select="$jobs" >
                    <xsl:sort select="translate( @path, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending"/>
                  </xsl:apply-templates>
                </xsl:otherwise>
              </xsl:choose>
            <!--/tbody-->
            
            <tfoot>
              <xsl:call-template name="after_body_space">
                  <xsl:with-param name="colspan" select="'5'"/>
              </xsl:call-template>
            </tfoot>
          </table>
    </xsl:template>

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Tasks (in Jobs list view)-->
<!-- call by <xsl:template match="job" mode="list"> -->

    <xsl:template match="tasks" mode="job_list">
        <xsl:for-each select="task[@id]">            
            <tr> 
              <td class="task">
                  <span style="margin-left: 2ex">
                      <span class="translate">Task</span>&#160;<xsl:value-of select="@id"/>
                  </span>
              </td>
              
              <td class="task"> &#160;
                  <xsl:if test="@running_since!=''">
                      <span class="small"><xsl:apply-templates mode="date_time_nowrap" select="@running_since__xslt_datetime_diff"/></span>
                  </xsl:if>
              </td>
              
              <td align="right">
                  <xsl:if test="@steps &gt; 0">
                    <xsl:value-of select="@steps"/>
                  </xsl:if>&#160;&#160;
              </td>
              
              <td colspan="2">
                 <xsl:apply-templates select="." mode="task_line"/>
              </td>
            </tr>
        </xsl:for-each>
    </xsl:template>

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Tasks (in Jobs tree view)-->    
    <xsl:template match="tasks" mode="job_tree">
        <xsl:for-each select="task[@id]">            
            <li> 
              <span class="green_label">Task</span><span class="green_value">&#160;<xsl:value-of select="@id"/></span>
              <xsl:if test="@running_since!=''">
                  <span class="green_value">&#160;&#160;- &#160;</span><span class="green_label">since</span><span class="green_value">&#160;<xsl:apply-templates mode="date_time_nowrap" select="@running_since__xslt_datetime_diff"/></span>
              </xsl:if>
              <xsl:if test="@steps &gt; 0">
                <span class="green_value">&#160;&#160;- &#160;<xsl:value-of select="@steps"/></span>&#160;<span class="green_label">Steps</span>
              </xsl:if>
              <span class="small">&#160;&#160;- &#160;<xsl:apply-templates select="@state" /></span>
              <xsl:if test="order/@path">
                &#160;&#160;- &#160;<span class="label">Order</span><span class="small">:&#160;<xsl:value-of select="order/@name" /></span>
              </xsl:if>
            </li>
        </xsl:for-each>
    </xsl:template>
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~task-->

    <xsl:template match="task" mode="task_line">
        <span class="small"><xsl:apply-templates select="@state" /></span>
        <xsl:if test="@in_process_since!=''">
            <span class="green_value"><xsl:text> &#160;</xsl:text>(<xsl:apply-templates mode="date_time_nowrap" select="@in_process_since__xslt_datetime_diff"/>)</span>
        </xsl:if>
        <xsl:if test="order/@path">
            <!--&#160;-&#160;<span class="label">Order</span><span class="small">:&#160;</span><xsl:apply-templates mode="trim_slash" select="order/@path" />-->
            &#160;-&#160;<span class="label">Order</span><span class="small">:&#160;<xsl:value-of select="order/@id" /></span>
        </xsl:if>
    </xsl:template>
    
    <!--left-->    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Job_chains-->

    <xsl:template match="job_chains">
        <xsl:variable    name="job_chain_select" select="job_chain[ not(@visible) or @visible='yes' ]" />
        
          <table cellpadding="0" cellspacing="0" width="100%" border="0" class="bottom">  
            
            <colgroup>
              <col width="10"/>
              <col width="*"/>
              <col width="20"/>
              <col width="1%"/>
              <col width="40" align="right"/>
            </colgroup>
            
            <thead>
                <tr>
                    <td colspan="5" class="before_head_space">&#160;</td>
                </tr>
                <tr>
                    <xsl:choose>
                        <xsl:when test="/spooler/@show_job_chain_orders_checkbox or /spooler/@show_job_chain_jobs_checkbox">
                            <td class="head1"><span class="translate">Order state</span></td>
                            <td class="head"><span class="translate">Job</span></td>
                            <td class="head"><span class="translate" style="white-space:nowrap;">Job chain</span>&#160;/&#160;<span class="translate" style="white-space:nowrap;">Job state</span></td>
                            <td class="head" colspan="2"><span class="translate">Orders</span></td>
                        </xsl:when>
                        <xsl:otherwise>
                             <td class="head1"><span class="translate" style="white-space:nowrap;">Job chain</span></td>
                             <td class="head1">&#160;</td>
                             <td class="head"><span class="translate">State</span></td>
                             <td class="head" colspan="2"><span class="translate">Orders</span></td>
                        </xsl:otherwise>
                    </xsl:choose>    
                </tr>
                <xsl:call-template name="after_head_space">
                  <xsl:with-param name="colspan" select="'5'"/>
                </xsl:call-template>
            </thead>
             
            <tbody>
              <xsl:if test="not( $job_chain_select )">
                  <tr><td colspan="5"><span class="translate" style="font-weight:bold;">No job chains found</span></td></tr>
              </xsl:if>
              <xsl:choose>
                <xsl:when test="/spooler/@sort_job_chains_select = 'unsorted'">
                  <xsl:apply-templates mode="list" select="$job_chain_select">
                      <xsl:with-param name="max_orders" select="/spooler/@my_max_orders"/>
                  </xsl:apply-templates>
                </xsl:when>
                <xsl:when test="/spooler/@sort_job_chains_select = 'name_desc'">
                  <xsl:apply-templates mode="list" select="$job_chain_select">
                      <xsl:sort select="translate( @path, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="descending"/>
                      <xsl:with-param name="max_orders" select="/spooler/@my_max_orders"/>
                  </xsl:apply-templates>
                </xsl:when>
                <xsl:when test="/spooler/@sort_job_chains_select = 'next_start_time_asc'">
                  <xsl:apply-templates mode="list" select="$job_chain_select">
                      <xsl:sort select="job_chain_node/order_queue/order/@next_start_time" order="ascending"/>
                      <xsl:sort select="translate( @path, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending"/>
                      <xsl:with-param name="max_orders" select="/spooler/@my_max_orders"/>
                  </xsl:apply-templates>
                </xsl:when>
                <xsl:when test="/spooler/@sort_job_chains_select = 'next_start_time_desc'">
                  <xsl:apply-templates mode="list" select="$job_chain_select">
                      <xsl:sort select="job_chain_node/order_queue/order/@next_start_time" order="descending"/>
                      <xsl:sort select="translate( @path, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending"/>
                      <xsl:with-param name="max_orders" select="/spooler/@my_max_orders"/>
                  </xsl:apply-templates>
                </xsl:when>
                <xsl:when test="/spooler/@sort_job_chains_select = 'status_asc'">
                  <xsl:apply-templates mode="list" select="$job_chain_select">
                      <xsl:sort select="@state" order="ascending"/>
                      <xsl:sort select="job_chain_node/@action" order="ascending"/>
                      <xsl:sort select="translate( @path, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending"/>
                      <xsl:with-param name="max_orders" select="/spooler/@my_max_orders"/>
                  </xsl:apply-templates>
                </xsl:when>
                <xsl:when test="/spooler/@sort_job_chains_select = 'status_desc'">
                  <xsl:apply-templates mode="list" select="$job_chain_select">
                      <xsl:sort select="@state" order="descending"/>
                      <xsl:sort select="job_chain_node/@action" order="descending"/>
                      <xsl:sort select="translate( @path, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending"/>
                      <xsl:with-param name="max_orders" select="/spooler/@my_max_orders"/>
                  </xsl:apply-templates>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:apply-templates mode="list" select="$job_chain_select">
                      <xsl:sort select="translate( @path, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending"/>
                      <xsl:with-param name="max_orders" select="/spooler/@my_max_orders"/>
                  </xsl:apply-templates>
                </xsl:otherwise>
              </xsl:choose>
            </tbody>
            
            <tfoot>
              <xsl:call-template name="after_body_space">
                  <xsl:with-param name="colspan" select="'5'"/>
              </xsl:call-template>
            </tfoot>
          </table>
               
    </xsl:template>
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Job_chain-->

    <xsl:template match="job_chain">
        
        <div> <!--google chrome-->
          <div class="top">
              <xsl:call-template name="round_top_corners"/>
              <table cellpadding="0" cellspacing="0" width="100%" border="0" class="top">
                <thead>
                  <tr>
                     <td colspan="3" class="before_head_space">&#160;</td>
                  </tr>
                  <tr>
                      <td width="35%">
                         <input id="show_order_history_checkbox" type="checkbox" onclick="callErrorChecked( 'show_order_history_checkbox__onclick' );">
                             <xsl:if test="/spooler/@show_order_history_checkbox">
                                 <xsl:attribute name="checked">checked</xsl:attribute>
                             </xsl:if>
                         </input>
                         <label for="show_order_history_checkbox" style="white-space:nowrap;">Show order history</label>
                      </td>
                      <td width="30%" align="center">
                          <span class="caption">JOB CHAIN</span>
                      </td>
                      <td width="35%">&#160;</td>
                  </tr>
                </thead>
              </table>
          </div>
            
          <div class="bottom">  
          <table cellpadding="0" cellspacing="0" width="100%" border="0" class="bottom">  
            
            <colgroup>
              <col width="10"/>
              <col width="*"/>
              <col width="20"/>
              <col width="1%"/>
              <col width="40" align="right"/>
            </colgroup>
            
            <thead class="list"> 
                
                <tr>
                    <td colspan="5" class="before_head_space">&#160;</td>
                </tr>
                <tr>
                    <td class="head1"><span class="translate">Order state</span></td>
                    <td class="head"><span class="translate">Job</span></td>
                    <td class="head"><span class="translate" style="white-space:nowrap;">Job chain</span>&#160;/&#160; <span class="translate" style="white-space:nowrap;">Job state</span></td>
                    <td class="head" colspan="2"><span class="translate">Orders</span></td>    
                </tr>
                <xsl:call-template name="after_head_space">
                  <xsl:with-param name="colspan" select="'5'"/>
                </xsl:call-template>
            </thead>
            
            <xsl:apply-templates mode="list" select="." >
                <xsl:with-param name="single" select="true()" />
            </xsl:apply-templates>
        
            <tfoot>
              <xsl:call-template name="after_body_space">
                  <xsl:with-param name="colspan" select="'5'"/>
              </xsl:call-template>
            </tfoot>
          </table>
          <xsl:call-template name="round_bottom_corners"/>
          </div>
          
          <xsl:if test="/spooler/@show_order_history_checkbox">
            <div class="top" style="margin-top:4px;">
              <xsl:call-template name="round_top_corners"/>
              
              <table cellpadding="0" cellspacing="0" width="100%" border="0" class="top">
                <thead>
                  <tr>
                     <td class="before_head_space">&#160;</td>
                  </tr>
                  <tr>
                     <td align="center">
                         <span class="caption">ORDER HISTORY</span>
                     </td>
                  </tr>
                </thead>
              </table>
            </div>
            <div class="bottom">
              <xsl:apply-templates select="order_history" mode="list">
                <xsl:with-param name="max_order_history" select="/spooler/@my_max_order_history"/>
              </xsl:apply-templates>
              
              <xsl:call-template name="round_bottom_corners"/>
            </div>            
          </xsl:if>
        </div> <!--google chrome-->
        
    </xsl:template>
    
    

    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Order in Job chain list-->

    <xsl:template match="order_queue | blacklist | order" mode="job_chain_list">
        <xsl:param name="max_orders" select="999999999"/>
        <xsl:param name="state_text" select="false()"/>
        <xsl:param name="orders"/>
        <xsl:param name="single"     select="false()"/>
        <xsl:param name="treeview"   select="false()"/>
        
        <xsl:for-each select="$orders">
              <xsl:sort select="@next_start_time" order="ascending"/>
              
              <xsl:variable name="normalized_order_id">
                  <xsl:apply-templates mode="normalized_order_id" select="@id" />
              </xsl:variable>
        
              <xsl:if test="@occupied_by_cluster_member_id">
                <tr>
                    <td></td>
                    <td colspan="3" style="padding-left:2ex;">
                      <xsl:apply-templates mode="cluster_member" select="." />
                    </td>
                    <td></td>
                </tr>
              </xsl:if> 
              
              <tr class="list" title="show order details">
                <xsl:if test="$treeview">
                  <xsl:attribute name="oncontextmenu">order_menu__onclick( '<xsl:value-of select="@job_chain"/>', '<xsl:value-of select="@id"/>', '<xsl:value-of select="name(parent::*)"/>' );return false;</xsl:attribute>
                  <xsl:attribute name="onclick">callErrorChecked( 'show_order_details','<xsl:value-of select="$normalized_order_id"/>','<xsl:value-of select="@job_chain"/>' )</xsl:attribute>
                </xsl:if> 
                <td>
                  <xsl:if test="not($treeview)">
                    <xsl:attribute name="onclick">callErrorChecked( 'show_order_details','<xsl:value-of select="$normalized_order_id"/>','<xsl:value-of select="@job_chain"/>' )</xsl:attribute>
                  </xsl:if>
                  <xsl:if test="name(parent::*) = 'blacklist'">
                    <xsl:attribute name="style">border-right:1px solid #8B919F</xsl:attribute>
                  </xsl:if>
                  <xsl:text>&#160;</xsl:text>
                </td>
                
                <xsl:choose>
                  <xsl:when test="$treeview">
                    <td colspan="1" style="padding-left:2ex;white-space:nowrap;">
                      <span class="label">Order</span><span class="small">:&#160;</span>
                      <span style="white-space:nowrap;">
                          <xsl:apply-templates mode="only_trim_slash" select="@id" />
                      </span>
                    </td>  
                    <td colspan="3" style="padding-left:4px;white-space:nowrap;">
                      <xsl:if test="name(parent::*) = 'order_queue'">
                        <xsl:apply-templates mode="properties" select="." />
                      </xsl:if>
                    </td>
                  </xsl:when>
                  <xsl:otherwise>
                    <td colspan="3" style="padding-left:2ex;white-space:nowrap;">
                      <xsl:attribute name="onclick">callErrorChecked( 'show_order_details','<xsl:value-of select="$normalized_order_id"/>','<xsl:value-of select="@job_chain"/>' )</xsl:attribute>
                      <span class="label">Order</span><span class="small">:&#160;</span>
                      <span style="white-space:normal;">
                          <xsl:apply-templates mode="trim_slash" select="@id" />
                      </span>
                      <xsl:if test="name(parent::*) = 'order_queue'">
                        <br/>
                        <xsl:apply-templates mode="properties" select="." />
                      </xsl:if>
                    </td>
                    <td style="text-align:right;padding:0px 2px;" title="Order menu">
                      <xsl:call-template name="command_menu">
                        <xsl:with-param name="title"              select="'Order menu'"/>
                        <xsl:with-param name="onclick_call"       select="'order_menu__onclick'"/>
                        <xsl:with-param name="onclick_param1_str" select="@job_chain"/>
                        <xsl:with-param name="onclick_param2_str" select="@id"/>
                        <xsl:with-param name="onclick_param3_str" select="name(parent::*)"/>
                      </xsl:call-template>
                    </td>
                  </xsl:otherwise>
                </xsl:choose>
              </tr>
              
              <xsl:if test="name(parent::*) = 'order_queue'">
                <xsl:if test="$state_text and @state_text">
                  <tr>
                    <td></td>
                    <td colspan="4" class="status_text">
                      <xsl:value-of select="@state_text"/>
                    </td>
                  </tr>
                </xsl:if>
                <xsl:if test="ERROR or file_based/ERROR or file_based/removed or replacement or file_based/removed/ERROR or file_based/requisites/requisite/@is_missing='yes'">
                  <tr>
                    <td></td>
                    <td colspan="4" style="padding-left: 2ex;">
                       <xsl:apply-templates mode="file_based_line" select="."/>
                    </td>
                  </tr>
                </xsl:if>
              </xsl:if>
              <xsl:if test="position() = last()">
                <tr><td colspan="5" style="line-height: 4px;">&#160;</td></tr>
              </xsl:if>              
        </xsl:for-each>
        
        <xsl:if test="@length &gt;= $max_orders  or  order[ ../@length &gt; last() ]">
            <tr>
                <td></td>
                <td>
                    <span style="margin-left: 2ex">...</span>
                </td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
        </xsl:if>
    </xsl:template>
    
    
    <xsl:template match="order" mode="cluster_member">
         <xsl:attribute name="arg"> <xsl:value-of select="@occupied_by_cluster_member_id"/></xsl:attribute>
         <xsl:attribute name="title">Order is processed by Scheduler member <xsl:value-of select="@occupied_by_cluster_member_id"/></xsl:attribute>
         <span class="label">Processed by</span><span class="small">:&#160;</span>
         <span class="cluster" style="white-space:nowrap;font-size:8pt;">
             <xsl:choose>
                 <xsl:when test="@occupied_by_http_url">
                     <span class="link">
                         <xsl:attribute name="onclick">open_remote_scheduler( '<xsl:value-of select="@occupied_by_http_url"/>' );</xsl:attribute>
                         <xsl:choose>
                             <xsl:when test="contains( @occupied_by_http_url, 'http://' )">
                                 <xsl:value-of select="substring-after( @occupied_by_http_url, 'http://' )"/>
                             </xsl:when>
                             <xsl:otherwise>
                                 <xsl:value-of select="@occupied_by_cluster_member_id"/>
                             </xsl:otherwise>
                         </xsl:choose>
                     </span>
                 </xsl:when>
                 <xsl:otherwise>
                     <xsl:value-of select="@occupied_by_cluster_member_id"/>
                 </xsl:otherwise>
             </xsl:choose>
         </span>
    </xsl:template>
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Order in Job chain list-->
    
    <xsl:template match="order" mode="properties">
        <xsl:param name="with_datetimes"     select="true()"/>
        
        <xsl:choose>
           <xsl:when test="@occupied_by_cluster_member_id">
               <xsl:apply-templates mode="cluster_member" select="." />
           </xsl:when>
           <xsl:when test="@on_blacklist='yes'">
               <span class="red_label">on blacklist</span>
           </xsl:when>
           <xsl:when test="@suspended = 'yes'">
               <span style="white-space:nowrap;"><span class="red_label">suspended</span>
               <xsl:if test="$with_datetimes">
                 &#160;( <xsl:apply-templates mode="datetimes" select="."/>)
               </xsl:if></span>
           </xsl:when>
           <xsl:when test="@removed">
               <span class="red_label">deleted</span>
           </xsl:when>
           <xsl:when test="@replacement">
               <xsl:attribute name="title">This order is a replacement for another order with the same ID</xsl:attribute>
               <span class="label">
                 <xsl:if test="@replaced_order_occupator">
                     <span class="translate">Replacement</span>
                     <xsl:text> (</xsl:text><span class="translate">currently processed by</span><xsl:text> </xsl:text>
                     <xsl:value-of select="@replaced_order_occupator"/>
                     <xsl:text>)</xsl:text>
                 </xsl:if>
               </span>
           </xsl:when>
           <xsl:when test="$with_datetimes">
              <xsl:apply-templates mode="datetimes" select="."/>
           </xsl:when>
         </xsl:choose>
         
     </xsl:template>
     
           
     <xsl:template match="order" mode="datetimes">
         <xsl:choose>
           <xsl:when test="@setback">
               <span class="red_label">Setback</span><span class="red_value">:&#160;</span>
               <span class="red_value"><xsl:apply-templates mode="date_time_nowrap" select="@setback__xslt_date_or_time_with_diff"/></span>
           </xsl:when>
           <xsl:when test="@next_start_time">
               <span class="green_label">Next start</span><span class="green_value">:&#160;</span>
               <span class="green_value"><xsl:apply-templates mode="date_time_nowrap" select="@next_start_time__xslt_datetime_with_diff"/></span>
           </xsl:when>
           <xsl:when test="@start_time">
               <span class="green_label">Running since</span><span class="green_value">:&#160;</span>
               <span class="green_value"><xsl:apply-templates mode="date_time_nowrap" select="@start_time__xslt_datetime_with_diff"/></span>
           </xsl:when>
           <!-- TODO @start_time und @task wird gleichzeitig geliefert -->
           <!--xsl:when test="@task">
               <span class="green_label">Task</span><span class="green_value">:&#160;</span>
               <span class="green_value"><xsl:value-of select="@task"/></span>
           </xsl:when-->
        </xsl:choose>
    </xsl:template>
    
    
    <!--left-->
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Locks-->

    <xsl:template match="locks">
          <table width="100%" cellpadding="0" cellspacing="0" class="bottom">
            <thead>
               <xsl:call-template name="after_head_space">
                  <xsl:with-param name="colspan" select="'5'"/>
               </xsl:call-template>
            </thead>
            
            <tbody>
              <xsl:if test="count(lock) = 0">
                  <tr><td colspan="5" ><span class="translate" style="font-weight:bold">No locks found</span></td></tr>
              </xsl:if>
              <xsl:for-each select="lock">
                <xsl:if test="position() &gt; 1">
                  <tr><td colspan="5" class="before_head_space">&#160;</td></tr>
                </xsl:if>
                <tr>
                    <td>
                      <xsl:apply-templates mode="lock_path" select="."/>
                    </td>
                    
                    <td colspan="4" style="padding-left:4px;">
                        <xsl:choose>
                            <xsl:when test="lock.holders/lock.holder/@exclusive='no'">
                                <span class="red_label">non-exclusively locked</span>
                            </xsl:when>
                            <xsl:when test="lock.holders/lock.holder">
                                <span class="red_label">locked</span>
                            </xsl:when>
                            <xsl:when test="@is_free='yes'">
                                <span class="translate">free</span>
                            </xsl:when>
                        </xsl:choose>
                        
                    </td>
                </tr>
                
                <xsl:if test="ERROR or file_based/ERROR or file_based/removed or replacement or file_based/removed/ERROR or file_based/requisites/requisite/@is_missing='yes'">
                    <tr>
                        <td colspan="5" style="padding-left: 4ex; padding-bottom: 0.5em;">
                            <xsl:apply-templates mode="file_based_line" select="."/>
                        </td>
                    </tr>
                </xsl:if>

                <tr>
                    <td colspan="5" style="padding-left: 4ex">
                        <xsl:if test="lock.holders/lock.holder">
                            <xsl:choose>
                                <xsl:when test="lock.holders/@exclusive='no'">
                                    <span class="label" style="display:block;">Holders (non-exclusive)</span>
                                </xsl:when>
                                <xsl:otherwise>
                                    <span class="label" style="display:block;">Holders (exclusive)</span>
                                </xsl:otherwise>
                            </xsl:choose>
                            <xsl:apply-templates select="lock.holders"/>
                        </xsl:if>

                        <xsl:if test="lock.queue [ not( @exclusive='no' ) ]/lock.queue.entry">
                            <span class="label" style="display:block;margin-top:5pt;">Waiting jobs (exclusive)</span>
                            <xsl:apply-templates select="lock.queue [ not( @exclusive='no' ) ] "/>
                        </xsl:if>

                        <xsl:if test="lock.queue [ @exclusive='no' ]/lock.queue.entry">
                            <span class="label" style="display:block;margin-top:5pt;">Waiting jobs (non-exclusive)</span>
                            <xsl:apply-templates select="lock.queue [ @exclusive='no' ]"/>
                        </xsl:if>
                    </td>
                </tr>
              </xsl:for-each>
            </tbody>
            
            <tfoot>
              <xsl:call-template name="after_body_space">
                  <xsl:with-param name="colspan" select="'5'"/>
              </xsl:call-template>
            </tfoot>
          </table>
          
    </xsl:template>
    
    
    <xsl:template match="lock" mode="list">
       <xsl:if test="position() &gt; 1">
           <tr><td colspan="5" class="before_head_space">&#160;</td></tr>
       </xsl:if>
    
    </xsl:template>

    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~lock.queue-->

    <xsl:template match="lock.queue">
        <table width="100%" cellpadding="0" cellspacing="0" class="lock">
            <tbody>
            <xsl:for-each select="lock.queue.entry">
                <tr class="list">
                    <td title="show job details">
                        <xsl:attribute name="onclick">callErrorChecked( 'show_job_details', '<xsl:value-of select="@job"/>' )</xsl:attribute>
                        <xsl:apply-templates mode="trim_slash" select="@job" />
                    </td>
                    <td>
                        <xsl:apply-templates mode="job_state_line" select="/spooler/answer/state/jobs/job[ @path = current()/@job ]" />
                    </td>
                </tr>
            </xsl:for-each>
            </tbody>
        </table>
    </xsl:template>

    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~lock.holders-->

    <xsl:template match="lock.holders">
        <table width="100%" cellpadding="0" cellspacing="0" class="lock">
          <tbody>
            <xsl:for-each select="lock.holder">
                <tr class="list">
                    <td title="show task details">
                        <xsl:attribute name="onclick">callErrorChecked( 'show_task_details', '<xsl:value-of select="@task"/>' )</xsl:attribute>
                        <span style="padding-right: 2ex">
                            <xsl:text>Task</xsl:text>
                            <xsl:value-of select="@job"/>:<xsl:value-of select="@task"/>
                        </span>
                        <span>
                            <xsl:apply-templates select="/spooler/answer/state/jobs/job [ @path=current()/@job ]/tasks/task[ @task=current()/@task ]" mode="task_line"/>
                        </span>
                    </td>
                </tr>
            </xsl:for-each>
          </tbody>
        </table>
    </xsl:template>
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~lock.use-->

    <xsl:template match="lock.use" mode="short">

        <span>
            <xsl:if test="@is_available='no'">
                <xsl:attribute name="class">job_error</xsl:attribute>
                <xsl:attribute name="title">Lock is not available, it is locked</xsl:attribute>
            </xsl:if>
            
            <xsl:if test="@is_available='yes'">
                <xsl:attribute name="title">Lock is available</xsl:attribute>
            </xsl:if>

            <xsl:apply-templates mode="trim_slash" select="@lock" />
            
            <!--xsl:if test="@is_missing='yes'">
                <xsl:attribute name="class">job_error</xsl:attribute>
                <xsl:text> </xsl:text><span class="translate">is missing</span>
            </xsl:if-->
            
            <xsl:if test="@exclusive='no'">
                <xsl:text> </xsl:text><span class="label">non-excl.</span>
            </xsl:if>
        </span>

    </xsl:template>
            
    <!--left-->
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~process_classes-->

    <xsl:template match="process_classes">
        
        <table width="100%" cellpadding="0" cellspacing="0" border="0" class="bottom">
            <colgroup>  
              <col width="50"/>
              <col width="100"/>  
              <col width="50"/>  
              <col width="*"/>  
              <col width="10"  align="right"/>
              <col width="10"  align="right"/>
              <col width="*"/>
            </colgroup>
            
            <thead>
                <tr>
                    <td colspan="7" class="before_head_space">&#160;</td>
                </tr>
                <tr style="">
                    <td class="head1" style="padding-left: 2ex"><span class="translate">Pid</span> </td>
                    <td class="head"><span class="translate">Task</span></td>
                    <td class="head1" align="right" style="padding-right: 4px"><span class="translate">-id</span></td>
                    <td class="head"><span class="translate">Running since</span></td>
                    <td class="head"><span class="translate">Operations</span></td>
                    <td class="head"><span class="translate">Callbacks</span></td>
                    <td class="head"><span class="translate">Current operation</span></td>
                </tr>
                <xsl:call-template name="after_head_space">
                  <xsl:with-param name="colspan" select="'7'"/>
                </xsl:call-template>
            </thead>
            
            <tbody>
                <xsl:for-each select="process_class">
                
                    <tr style="padding-top: 1ex">
                        <td colspan="7">
                            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                              <tbody><tr>
                                <td style="font-weight:bold;width:200px;">
                                    <xsl:choose>
                                        <xsl:when test="@path!=''">
                                            <xsl:apply-templates mode="only_trim_slash" select="@path"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <span class="translate">(default)</span>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                    &#160;
                                </td>  
                                <td class="label" style="width:100px;"><span class="label">max processes</span>:&#160;<xsl:value-of select="@max_processes"/></td>
                                <td class="label" >&#160; 
                                  <xsl:if test="@remote_scheduler">
                                    <span class="translate">Remote Scheduler</span>:&#160;<a class="list" href="#">
                                        <xsl:choose>
                                          <xsl:when test="contains(@remote_scheduler,'/')">
                                            <xsl:attribute name="title"><xsl:value-of select="concat( 'http://', substring-before(@remote_scheduler,'/'), ':', substring-after(@remote_scheduler,':') )"/></xsl:attribute>
                                            <xsl:attribute name="onclick">open_remote_scheduler( '<xsl:value-of select="concat(substring-before(@remote_scheduler,'/'), ':', substring-after(@remote_scheduler,':'))"/>' );</xsl:attribute>
                                          </xsl:when>
                                          <xsl:otherwise>
                                            <xsl:attribute name="title"><xsl:value-of select="concat( 'http://', @remote_scheduler )"/></xsl:attribute>
                                            <xsl:attribute name="onclick">open_remote_scheduler( '<xsl:value-of select="@remote_scheduler"/>' );</xsl:attribute>
                                          </xsl:otherwise>
                                        </xsl:choose>
                                        <xsl:value-of select="@remote_scheduler"/>
                                    </a>
                                  </xsl:if>
                                </td>
                              </tr></tbody>
                            </table>
                        </td>
                    </tr>
                    <xsl:if test="ERROR or file_based/ERROR or file_based/removed or replacement or file_based/removed/ERROR or file_based/requisites/requisite/@is_missing='yes'">
                        <tr>
                            <td colspan="7" style="padding-left: 4ex; padding-bottom: 0.5em;">
                                <xsl:apply-templates mode="file_based_line" select="."/>
                            </td>
                        </tr>
                    </xsl:if>
                    
                    <xsl:for-each select="processes/process">
                        <tr>
                            <td style="padding-left: 2ex"><xsl:value-of select="@pid"/></td>
                            <td><xsl:value-of select="@job"/><xsl:text>&#160;</xsl:text></td>
                            <td align="right" style="padding-right: 4px"><xsl:value-of select="@task_id"/></td>
                            <td class="task"><xsl:apply-templates mode="date_time_nowrap" select="@running_since__xslt_datetime_with_diff"/></td>
                            <td class="small"><xsl:value-of select="@operations"/></td>
                            <td class="small"><xsl:value-of select="@callbacks"/></td>
                            <td class="small"><xsl:value-of select="@operation"/></td>
                        </tr>
                    </xsl:for-each>
                    
                </xsl:for-each>
            </tbody>
            
            <tfoot>
                <xsl:call-template name="after_body_space">
                  <xsl:with-param name="colspan" select="'7'"/>
                </xsl:call-template>
            </tfoot>
        </table>
        
      
    </xsl:template>


<!--left-->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~cluster-->

    <xsl:template match="cluster">
       
          <table cellpadding="0" cellspacing="0" width="100%" border="0" class="bottom">
            
            <colgroup>
              <col width="160"/>
              <col width="240"/>  
              <col width="240"/>  
              <col width="80"/>
              <col width="160"/>
              <col width="80"/>
              <col width="80"/>
              <col width="120"/>
            </colgroup>
            
            <thead>
                <tr>
                    <td colspan="8">
                        <span style="margin-right: 1em; font-weight:bold;">
                            <xsl:value-of select="count( cluster_member [ @active='yes' ] )" />
                            <xsl:text> </xsl:text><span class="translate">active Scheduler(s)</span>
                            <xsl:text>. </xsl:text>
                        </span>
                        
                        <span style="margin-right: 1em; font-weight:bold;">
                            <xsl:value-of select="count( cluster_member [ @exclusive='yes' ] )" />
                            <xsl:text> </xsl:text><span class="translate">exclusive Scheduler(s)</span>
                            <xsl:text>. </xsl:text>
                        </span>
                        
                        <xsl:if test="@active='yes'">
                            <span style="margin-right: 1em; font-weight:bold; color:#009933">
                                <span class="translate">This Scheduler is active</span>

                                <xsl:if test="@exclusive='yes'">
                                    <xsl:text> </xsl:text><span class="translate">and exclusive</span>
                                </xsl:if>

                                <xsl:text>. </xsl:text>
                            </span>
                        </xsl:if>

                        <xsl:if test="@active!='yes'">
                            <span class="translate" style="margin-right: 1em; color:#800040">Only active Job Schedulers are allowed to start operation.</span>
                        </xsl:if>
                    </td>
                </tr>
                <tr>
                    <td style="line-height: 5pt">&#160;</td>
                </tr>

                <tr>
                    <td class="head1">Scheduler</td>
                    <td class="head"  style="width: 20ex"><span class="translate">Started</span></td>
                    <td class="head"><span class="translate">State</span></td>
                    <td class="head"><span class="translate">Pid</span></td>
                    <td class="head"><span class="translate">Last heart beat</span></td>
                    <td class="head"><span class="translate">Detected heart beats</span></td>
                    <td class="head"><span class="translate">Backup precedence</span></td>
                    <td class="head1">&#160;</td>
                </tr>

                <xsl:call-template name="after_head_space">
                  <xsl:with-param name="colspan" select="'8'"/>
                </xsl:call-template>
            </thead>
             
            <tbody>
                <xsl:apply-templates select="cluster_member">
                    <xsl:sort select="@http_url"/>
                    <xsl:sort select="@dead" data-type="number"/>
                    <xsl:sort select="@heart_beat_count = 0"/>
                </xsl:apply-templates>
            </tbody>
            
            <tfoot>
              <xsl:call-template name="after_body_space">
                  <xsl:with-param name="colspan" select="'8'"/>
              </xsl:call-template>
            </tfoot>

          </table>
          

            
    </xsl:template>

    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~cluster_member-->

    <xsl:template match="cluster_member">

        <tr class="list">
            <xsl:attribute name="style">
                <xsl:text>cursor: default;</xsl:text>
                <xsl:choose>
                    <xsl:when test="@heart_beat_count=0">color:gray;</xsl:when>
                    <xsl:when test="@dead='yes'">color:crimson;</xsl:when>
                    <xsl:when test="@active='yes'">color:forestgreen;</xsl:when>
                </xsl:choose>
            </xsl:attribute>

            <td>
                <xsl:element name="span">
                    <xsl:attribute name="style">
                        <xsl:choose>
                            <xsl:when test="@dead='yes'">text-decoration: line-through;</xsl:when>
                            <xsl:otherwise              >text-decoration: none;</xsl:otherwise>
                        </xsl:choose>
                    </xsl:attribute>
                    
                    <xsl:attribute name="arg">=<xsl:value-of select="@cluster_member_id"/><xsl:text>, &#160;</xsl:text><xsl:value-of select="@version"/></xsl:attribute>
                    <xsl:attribute name="title">ID=<xsl:value-of select="@cluster_member_id"/><xsl:text>, &#160;</xsl:text><xsl:value-of select="@version"/></xsl:attribute>
                    
                    <xsl:if test="not( @active='yes' ) and @cluster_member_id != ancestor::cluster/@cluster_member_id">
                        <xsl:attribute name="class">link</xsl:attribute>
                    </xsl:if>

                    <xsl:if test="not( @dead='yes' ) and @cluster_member_id != ancestor::cluster/@cluster_member_id">
                        <xsl:attribute name="style">
                            <xsl:text>cursor: pointer;</xsl:text>
                        </xsl:attribute>

                        <xsl:attribute name="onclick">open_remote_scheduler( '<xsl:value-of select="@http_url"/>' );</xsl:attribute>
                    </xsl:if>

                    <xsl:choose>
                        <xsl:when test="@cluster_member_id=ancestor::cluster/@cluster_member_id">
                            <span class="translate">This Scheduler</span>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="@cluster_member_id"/>
                        </xsl:otherwise>
                    </xsl:choose>
                    <xsl:text> </xsl:text>
                </xsl:element>
            </td>

            <td>
                <xsl:apply-templates mode="date_time_nowrap" select="@running_since__xslt_date_or_time_with_diff"/>
            </td>

            <td>
                <xsl:choose>
                    <xsl:when test="@active='yes'">
                        <xsl:choose>
                            <xsl:when test="@dead='yes'">
                                <span class="translate" style="margin-right: 1ex;">(was active!)</span><xsl:text> </xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <span class="translate" style="margin-right: 1ex; font-weight: bold">active</span><xsl:text> </xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:when test="not( @dead='yes' )">
                        <span class="translate" style="margin-right: 1ex;">inactive</span><xsl:text> </xsl:text>
                    </xsl:when>
                </xsl:choose>

                <xsl:if test="@distributed_orders='yes'">
                    <span class="translate" style="margin-right: 1ex; white-space: nowrap; font-weight: bold">distributed orders</span><xsl:text> </xsl:text>
                </xsl:if>

                <xsl:if test="@exclusive='yes'">
                    <span class="translate" style="margin-right: 1ex; font-weight: bold">exclusive</span><xsl:text> </xsl:text>
                </xsl:if>

                <xsl:if test="@backup='yes'">
                    <span class="translate" style="margin-right: 1ex;">backup</span><xsl:text> </xsl:text>
                </xsl:if>

                <xsl:if test="not( @dead='yes' ) and not( @last_detected_heart_beat_age )">
                    <span class="translate" style="margin-right: 1ex;">still checking...</span><xsl:text> </xsl:text>
                </xsl:if>

                <xsl:if test="@dead='yes'">
                    <span style="margin-right: 1ex; font-weight: normal">
                        <span class="translate">dead</span>
                        <xsl:choose>
                            <xsl:when test="@last_detected_heart_beat">
                                <xsl:text>, </xsl:text>
                                <span style="white-space: nowrap; font-size: 8pt;">
                                    <span class="translate">discovered</span><xsl:text> </xsl:text>
                                    <xsl:value-of select="@last_detected_heart_beat"/>
                                </span>
                            </xsl:when>
                            <xsl:when test="@database_last_heart_beat">
                                <xsl:text>, </xsl:text>
                                <span style="white-space: nowrap; font-size: 8pt">
                                    <span class="translate">after</span><xsl:text> </xsl:text>
                                    <xsl:value-of select="@database_last_heart_beat"/>
                                </span>
                            </xsl:when>
                        </xsl:choose>
                        <xsl:text> </xsl:text>
                    </span>
                </xsl:if>

                <xsl:if test="@deactivating_scheduler_member_id">
                    <span style="margin-right: 1ex;">
                        <xsl:text> </xsl:text><span class="translate">Deactivated by</span><xsl:text> </xsl:text>
                        <xsl:value-of select="@deactivating_scheduler_member_id"/>
                        <xsl:text> </xsl:text>
                    </span>
                </xsl:if>
            </td>

            <td>
                <xsl:value-of select="@pid"/>
            </td>

            <td>
                <xsl:choose>
                    <xsl:when test="@last_detected_heart_beat_age">
                        <xsl:value-of select="@last_detected_heart_beat_age"/>
                        <xsl:text>s </xsl:text><span class="translate">ago</span>
                        <xsl:if test="@heart_beat_quality">
                            <xsl:element name="span">
                                <xsl:attribute name="style">
                                    <xsl:text>margin-left: 1ex;</xsl:text>
                                    <xsl:choose>
                                        <xsl:when test="@heart_beat_quality != 'good'">color: red;</xsl:when>
                                    </xsl:choose>
                                </xsl:attribute>

                                <xsl:text>(</xsl:text>
                                <xsl:value-of select="@heart_beat_quality"/>
                                <xsl:text>)</xsl:text>
                            </xsl:element>
                        </xsl:if>
                    </xsl:when>
                    <xsl:when test="@dead != 'yes'  and  @database_last_heart_beat">
                        <span style="font-size: 8pt">
                            <xsl:value-of select="@database_last_heart_beat"/>
                        </span>
                    </xsl:when>
                </xsl:choose>
            </td>

            <td>
                <xsl:value-of select="@heart_beat_count"/>
            </td>

            <td>
                <xsl:value-of select="@backup_precedence"/>
            </td>

            <td class="cluster" style="text-align:right;padding:0px 2px;">
                <!--xsl:if test="@cluster_member_id != ancestor::cluster/@cluster_member_id"-->
                <xsl:call-template name="command_menu">
                    <xsl:with-param name="style"              select="'width:120px;'"/>
                    <xsl:with-param name="title"              select="'Cluster member menu'"/>
                    <xsl:with-param name="onclick_call"       select="'cluster_member__onclick'"/>
                    <xsl:with-param name="onclick_param1_str" select="@cluster_member_id"/>
                </xsl:call-template>
                <!--/xsl:if-->
            </td>
        </tr>


        <tr>
            <td colspan="8" style="line-height: 1px;">
            </td>
        </tr>

    </xsl:template>
    
    
    <!--left-->
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~remote_schedulers-->

    <xsl:template match="remote_schedulers">
        
          <table width="100%" cellpadding="0" cellspacing="0" border="0" class="bottom">
            
            <thead>
                <tr>
                    <td colspan="7" class="before_head_space">&#160;</td>
                </tr>
                <tr>
                    <td colspan="7">
                        <xsl:value-of select="@count" />&#160;<span class="translate">Scheduler(s)</span>&#160;
                        (<xsl:value-of select="@connected" />&#160;<span class="translate">connected</span>)
                    </td>
                </tr>
                <tr style="">
                    <td class="head1" style="padding-left: 2ex"><span class="translate">IP</span><xsl:text> </xsl:text></td>
                    <td class="head"><span class="translate">Hostname</span></td>
                    <td class="head"><span class="translate">Port</span></td>
                    <td class="head"><span class="translate">Id</span></td>
                    <td class="head"><span class="translate">Connected</span></td>
                    <td class="head"><span class="translate">Disconnected</span></td>
                    <td class="head"><span class="translate">Version</span></td>
                </tr>
                <xsl:call-template name="after_head_space">
                  <xsl:with-param name="colspan" select="'7'"/>
                </xsl:call-template>
            </thead>
            
            <tbody>
                <xsl:for-each select="remote_scheduler">
                    <tr class="list">
                        <xsl:choose>
                          <xsl:when test="@hostname">
                            <xsl:attribute name="title"><xsl:value-of select="concat( 'http://', @hostname, ':', @tcp_port, '/' )"/></xsl:attribute>
                            <xsl:attribute name="onclick">open_remote_scheduler( '<xsl:value-of select="@hostname"/>', '<xsl:value-of select="@tcp_port"/>' )</xsl:attribute>
                            <xsl:attribute name="onmouseover">map_url( '<xsl:value-of select="@hostname"/>', '<xsl:value-of select="@tcp_port"/>', this );</xsl:attribute>
                          </xsl:when>
                          <xsl:otherwise>
                            <xsl:attribute name="title"><xsl:value-of select="concat( 'http://', @ip, ':', @tcp_port, '/' )"/></xsl:attribute>
                            <xsl:attribute name="onclick">open_remote_scheduler( '<xsl:value-of select="@ip"/>', '<xsl:value-of select="@tcp_port"/>' )</xsl:attribute>
                            <xsl:attribute name="onmouseover">map_url( '<xsl:value-of select="@ip"/>', '<xsl:value-of select="@tcp_port"/>', this );</xsl:attribute>
                          </xsl:otherwise>
                        </xsl:choose>
                           
                        <td><xsl:value-of select="@ip"/></td>
                        <td><xsl:value-of select="@hostname"/></td>
                        <td><xsl:value-of select="@tcp_port"/></td>
                        <td><xsl:value-of select="@scheduler_id"/></td>
                        <td><xsl:apply-templates mode="date_time_nowrap" select="@connected_at__xslt_date_or_time_with_diff"/></td>
                        <td style="color: crimson">
                            <xsl:if test="@connected='no'">
                                <xsl:apply-templates mode="date_time_nowrap" select="@disconnected_at__xslt_date_or_time_with_diff"/>
                            </xsl:if>
                        </td>
                        <td><xsl:value-of select="@version"/></td>
                    </tr>
                </xsl:for-each>
            </tbody>
            
            <tfoot>
              <xsl:call-template name="after_body_space">
                  <xsl:with-param name="colspan" select="'7'"/>
              </xsl:call-template>
            </tfoot>
            
          </table>
    </xsl:template>
    

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~job_chain-->    
    
    <xsl:template match="job_chain" mode="list">
        <xsl:param name="position" select="position()" />
        <xsl:param name="single" select="false()" />
        <xsl:param name="max_orders" select="999999999"/>
        <xsl:param name="big_chain" select="false()"/>
        
        <xsl:if test="$single or $big_chain or not(@order_id_space) or (@order_id_space and job_chain_node.job_chain)">
        
          <xsl:if test="( $single or /spooler/@show_job_chain_orders_checkbox or /spooler/@show_job_chain_jobs_checkbox ) and $position &gt; 1 and not($big_chain)">
            <tr><td colspan="5" class="line">&#160;</td></tr>
          </xsl:if>
        
          <tr class="list">
            <xsl:if test="not( $single ) or $big_chain">
                <xsl:attribute name="style">cursor: pointer;</xsl:attribute>
                <xsl:attribute name="title">show job chain details</xsl:attribute>
            </xsl:if>
            
            <td colspan="2">
              <xsl:if test="not( $single ) or $big_chain">
                <xsl:attribute name="onclick">callErrorChecked( 'show_job_chain_details', '<xsl:value-of select="@path"/>' )</xsl:attribute>
              </xsl:if>
              <xsl:if test="$big_chain">
                <xsl:attribute name="style">padding-left:4px;</xsl:attribute>
                <span style="font-size:8pt;font-family:Courier;">&#8594; </span>
              </xsl:if>
              <xsl:if test="@state='stopped' or @state='under_construction'">
                <xsl:attribute name="class">red</xsl:attribute>    
              </xsl:if>
              <b><xsl:apply-templates mode="trim_slash" select="@path" /></b>&#160;
              <xsl:if test="@title">
                 <nobr><xsl:apply-templates select="@title"/></nobr>
              </xsl:if>
            </td>
            
            <td>
              <xsl:if test="not( $single ) or $big_chain">
                <xsl:attribute name="onclick">callErrorChecked( 'show_job_chain_details', '<xsl:value-of select="@path"/>' )</xsl:attribute>       
              </xsl:if>
              <xsl:apply-templates select="@state"/>
            </td>
            
            <td>
              <xsl:if test="not( $single ) or $big_chain">
                <xsl:attribute name="onclick">callErrorChecked( 'show_job_chain_details', '<xsl:value-of select="@path"/>' )</xsl:attribute>       
                <xsl:attribute name="style">padding-right:8px;</xsl:attribute>
              </xsl:if>
              <xsl:choose>
                <xsl:when test="@order_id_space and job_chain_node.job_chain">  
                  <xsl:value-of select="sum(parent::*/job_chain[@order_id_space = current()/@order_id_space]/@orders)"/>
                </xsl:when>
                <xsl:otherwise>  
                  <xsl:value-of select="@orders"/>
                </xsl:otherwise>  
              </xsl:choose>
            </td>
            
            <td style="text-align:right;padding:0px 2px;" title="Job chain menu">
                <xsl:call-template name="command_menu">
                    <xsl:with-param name="title"                select="'Job chain menu'"/>
                    <xsl:with-param name="onclick_call"         select="'job_chain_menu__onclick'"/>
                    <xsl:with-param name="onclick_param1_str"   select="@path"/>
                    <xsl:with-param name="onclick_param2"       select="1+@orders"/>
                    <xsl:with-param name="onclick_param3"       select="1+number(boolean(@order_id_space and job_chain_node.job_chain))"/>
                </xsl:call-template>
            </td>
          </tr>
          
          <xsl:if test="ERROR or file_based/ERROR or file_based/removed or replacement or file_based/removed/ERROR or file_based/requisites/requisite/@is_missing='yes'">
            <tr>
                <td colspan="5" style="padding-left:4ex;">
                    <xsl:apply-templates mode="file_based_line" select="."/>
                </td>
            </tr>
          </xsl:if>
        
          <xsl:if test="/spooler/answer/state[1]/http_server/web_service [ @job_chain = current()/@path ]">
            <tr>
                <td colspan="5" style="font-size: 8pt">
                    <span class="translate">Web service</span><xsl:text> </xsl:text>
                    <xsl:for-each select="/spooler/answer/state[1]/http_server/web_service [ @job_chain = current()/@path ]">
                        <xsl:value-of select="@name"/>
                        <xsl:text> </xsl:text>
                    </xsl:for-each>
                </td>
            </tr>
          </xsl:if>                
          <xsl:if test="$single  or  /spooler/@show_job_chain_jobs_checkbox  or  job_chain_node.job_chain or (/spooler/@show_job_chain_orders_checkbox and (job_chain_node/descendant::order_queue/order or file_order_source))">
            <xsl:for-each select="job_chain_node[ @job ] | job_chain_node.job_chain">
                <!-- $show_orders vergroessert den Abstand zwischen den Job_chain_nodes. Aber nur, wenn ueberhaupt ein Order in der Jobkette ist -->
                <xsl:variable name="show_orders" select="( /spooler/@show_job_chain_orders_checkbox or $single )"/>
                <xsl:variable name="job" select="/spooler/answer/state/jobs/job[ @path = current()/@job ] | job[ @path = current()/@job ]"/>
                <xsl:variable name="job_chain" select="/spooler/answer/state/job_chains/job_chain[ @path = current()/@job_chain ]"/>
                
                <xsl:if test="$job_chain">
                  <xsl:apply-templates mode="list" select="$job_chain" >
                      <xsl:with-param name="position" select="position()" />
                      <xsl:with-param name="single" select="$single" />
                      <xsl:with-param name="max_orders" select="$max_orders"/>
                      <xsl:with-param name="big_chain" select="true()"/>
                  </xsl:apply-templates>
                </xsl:if>
                <xsl:if test="not($job_chain)">  
                
                  <tr class="list" title="show job details">                                
                    <td>
                        <xsl:attribute name="onclick">callErrorChecked( 'show_job_details','<xsl:value-of select="@job"/>' )</xsl:attribute>
                        <span style="margin-left: 2ex;white-space:nowrap;"><xsl:value-of select="@state"/></span>
                    </td>
        
                    <td>
                        <xsl:attribute name="onclick">callErrorChecked( 'show_job_details','<xsl:value-of select="@job"/>' )</xsl:attribute>
                        <xsl:if test="not($job)">
                           <xsl:attribute name="class">job_error</xsl:attribute>
                        </xsl:if>
                        <xsl:apply-templates mode="job_path" select="$job">
                           <xsl:with-param name="style" select="''"/>
                           <xsl:with-param name="order_job" select="true()"/>
                        </xsl:apply-templates>
                        <xsl:apply-templates mode="trim_slash" select="@job_chain" />
                    </td>                            
                    
                    <td>
                        <xsl:attribute name="onclick">callErrorChecked( 'show_job_details','<xsl:value-of select="@job"/>' )</xsl:attribute>
                        <xsl:apply-templates mode="node_action" select=".">
                            <xsl:with-param name="job" select="$job"/>
                        </xsl:apply-templates>
                    </td>
                    
                    <td>
                        <xsl:attribute name="onclick">callErrorChecked( 'show_job_details','<xsl:value-of select="@job"/>' )</xsl:attribute>
                        <xsl:value-of select="descendant::order_queue/@length"/>
                    </td>
                    
                    <td style="text-align:right;padding:0px 2px;" title="Job node menu">
                        <xsl:call-template name="command_menu">
                           <xsl:with-param name="title"                select="'Job node menu'"/>
                           <xsl:with-param name="onclick_call"         select="'job_chain_node_menu__onclick'"/>
                           <xsl:with-param name="onclick_param1_str"   select="@state"/>
                           <xsl:with-param name="onclick_param2_str"   select="../@path"/>
                        </xsl:call-template>         
                    </td>
                  </tr>
                  
                  <xsl:if test="$job/ERROR or $job/file_based/ERROR or $job/file_based/removed or $job/replacement or $job/file_based/removed/ERROR or $job/file_based/requisites/requisite/@is_missing='yes' or $job/lock.requestor/lock.use/@is_missing='yes'">
                      <tr>
                          <td>&#160;</td>
                          <td colspan="4">
                              <xsl:apply-templates mode="file_based_line" select="$job"/>
                          </td>
                      </tr>
                  </xsl:if>
                  
                  <xsl:choose>
                    <xsl:when test="$show_orders and self::job_chain_node/job/order_queue/order">
                      <tr><td colspan="5"><xsl:value-of select="$show_orders" /></td></tr>
                      <xsl:apply-templates select="job/order_queue" mode="job_chain_list">
                          <xsl:with-param name="max_orders" select="$max_orders"/>
                          <xsl:with-param name="orders" select="job/order_queue/order[ @job_chain = current()/parent::job_chain/@path  and  @state = current()/@state ]"/>
                      </xsl:apply-templates>
                    </xsl:when>
                    <xsl:when test="$show_orders and self::job_chain_node/order_queue/order">
                      <xsl:apply-templates select="order_queue" mode="job_chain_list">
                          <xsl:with-param name="max_orders" select="$max_orders"/>
                          <xsl:with-param name="state_text" select="$single"/>
                          <xsl:with-param name="orders" select="order_queue/order[ @job_chain = current()/parent::job_chain/@path  and  @state = current()/@state ]"/>
                      </xsl:apply-templates>
                    </xsl:when>
                  </xsl:choose>
                  
                </xsl:if>
        
            </xsl:for-each>
            
            <xsl:if test="($single or /spooler/@show_job_chain_orders_checkbox) and file_order_source">    
                <tr>
                  <td style="vertical-align:top;border-right:1px solid #8B919F">
                    <span class="translate" style="white-space:nowrap;">file orders</span>
                  </td>
                  <td colspan="3" style="padding-left:2ex;">
                    <xsl:apply-templates select="file_order_source"/>
                  </td>
                  <td></td>
                </tr>
            </xsl:if>
            
            <xsl:if test="($single or /spooler/@show_job_chain_orders_checkbox) and blacklist/@count &gt;= 1">
        
                <tr>
                    <td colspan="5" style="line-height: 1ex">&#160;</td>
                </tr>
        
                <tr>
                    <td style="vertical-align:top;border-right:1px solid #8B919F">
                      <span class="translate" style="white-space:nowrap;">blacklist</span>
                    </td>
                    <td colspan="2"></td>
                    <td><xsl:value-of select="blacklist/@count"/></td>
                    <td></td>
                </tr>
        
                <xsl:apply-templates select="blacklist" mode="job_chain_list">
                     <xsl:with-param name="orders" select="blacklist/order[ @job_chain = current()/@path ]"/>
                     <xsl:with-param name="max_orders" select="$max_orders"/>
                </xsl:apply-templates>
                
            </xsl:if>
          </xsl:if>                          
        </xsl:if>
    </xsl:template>


<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Action/State of Job Chain Node in Job Chain list-->   

    <xsl:template match="job_chain_node" mode="node_action">
      <xsl:param name="job" select="job" />
      <xsl:param name="all_states" select="true()" />
      <xsl:choose>
          <xsl:when test="@action">
              <xsl:choose>
                  <xsl:when test="@action='stop'">
                      <span class="red_label">Node is stopped</span>
                  </xsl:when>
                  <xsl:when test="@action='next_state'">
                      <span class="red_label">Node is skipped</span>
                  </xsl:when>
                  <xsl:when test="$all_states">
                      <span class="green_label"><xsl:value-of select="@action"/></span>
                  </xsl:when>
              </xsl:choose>
          </xsl:when>
          <xsl:when test="not($job)">
            <span class="red_label">is missing</span>
          </xsl:when>
          <xsl:when test="not($job/@order) or $job/@order='no'">
            <span class="red_label">not an order job</span>
          </xsl:when>
          <xsl:when test="$job/@state='not_initialized' or $job/@state='stopped' or $job/@state='read_error' or $job/@state='error'">
            <span class="red_label"><xsl:value-of select="$job/@state"/></span>
          </xsl:when>
          <xsl:when test="$all_states">
            <span>
              <xsl:if test="$job/@state='running'">
                <xsl:attribute name="class">green_value</xsl:attribute>
              </xsl:if>
              <span class="label"><xsl:value-of select="$job/@state"/></span>
              <xsl:if test="$job/tasks/@count>0">
                  <span class="green_value"><xsl:text>, </xsl:text><xsl:value-of select="$job/tasks/@count"/> <span class="translate">tasks</span></span>
              </xsl:if>
            </span>
          </xsl:when>
      </xsl:choose>    
    </xsl:template>    

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Job in Jobs list-->   
<!-- called by <xsl:template match="jobs"> -->
    
    <xsl:template match="job" mode="list">
        
        <xsl:if test="position() &gt; 1">
           <tr><td colspan="5" class="line">&#160;</td></tr>
        </xsl:if>
        <tbody class="list">
            <tr>
                <td colspan="4" title="show job details">
                    <xsl:attribute name="onclick">callErrorChecked( 'show_job_details','<xsl:value-of select="@path"/>' )</xsl:attribute>
                    <xsl:apply-templates mode="job_path" select="."/>
                    <xsl:if test="@title">&#160;&#160;- &#160;<xsl:apply-templates select="@title"/></xsl:if>&#160;
                </td>
                <td valign="top" align="right" title="Job menu">
                    <xsl:call-template name="command_menu">
                       <xsl:with-param name="title"              select="'Job menu'"/>
                       <xsl:with-param name="onclick_call"       select="'job_menu__onclick'"/>
                       <xsl:with-param name="onclick_param1_str" select="@path"/>
                    </xsl:call-template>
                </td>
            </tr>
            
            <xsl:if test="@state_text!=''">
                <tr title="show job details">
                  <xsl:attribute name="onclick">callErrorChecked( 'show_job_details','<xsl:value-of select="@path"/>' )</xsl:attribute>
                  <td colspan="5" class="state_text">
                      <xsl:call-template name="show_text_with_url">
                          <xsl:with-param name="text" select="@state_text"/>
                      </xsl:call-template>
                  </td>
                </tr>
            </xsl:if>
            
            <tr title="show job details" style="padding-bottom:4px">
                <xsl:attribute name="onclick">callErrorChecked( 'show_job_details','<xsl:value-of select="@path"/>' )</xsl:attribute>
                <td colspan="2">
                    <xsl:apply-templates select="@state"/>
                    <xsl:if test="tasks/@count>0">
                         <span class="green_value">
                            <xsl:text>,&#160;</xsl:text><xsl:value-of select="tasks/@count"/>&#160;<span class="translate">tasks</span>
                         </span>
                    </xsl:if>
                    <xsl:if test="tasks/task[not( @id )] and @waiting_for_process='yes'">
                         <xsl:text>,&#160;</xsl:text><span class="red_label">needs process</span>
                    </xsl:if>
                </td>
                
                <td align="right">
                    <xsl:value-of select="@all_steps"/>&#160;&#160;
                </td>
                
                <td class="green_value" colspan="2">
                  <xsl:choose>
                      <xsl:when test="@order='yes'">
                           <xsl:if test="@next_start_time">
                               <xsl:apply-templates mode="date_time_nowrap" select="@next_start_time__xslt_datetime_with_diff"/>
                           </xsl:if>
                           &#160;
                           <xsl:value-of select="order_queue/@length"/>&#160;<span class="translate">orders</span>
                      </xsl:when>
                      <xsl:when test="@next_start_time">
                           <xsl:apply-templates mode="date_time_nowrap" select="@next_start_time__xslt_datetime_with_diff"/>
                      </xsl:when>
                      <xsl:otherwise>
                           <span class="label">Without start time</span>
                      </xsl:otherwise>
                  </xsl:choose>
                </td>
            </tr>
        </tbody>
        <tbody>
            <xsl:if test="ERROR or file_based/ERROR or file_based/removed or replacement or file_based/removed/ERROR or file_based/requisites/requisite/@is_missing='yes' or lock.requestor/lock.use/@is_missing='yes'">
                <tr>
                    <td colspan="5">
                        <xsl:apply-templates mode="file_based_line" select="."/>
                    </td>
                </tr>
            </xsl:if>
            
            <xsl:if test="/spooler/@show_tasks_checkbox and tasks/task">
                <xsl:apply-templates select="tasks" mode="job_list"/>
            </xsl:if>
        </tbody>
    </xsl:template>
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Detailsicht eines Jobs-->
    
    <xsl:template match="job">
        <div> <!--google chrome-->
        <div class="top">
           <xsl:call-template name="round_top_corners"/>
        
           <table cellpadding="0" cellspacing="0" width="100%" border="0" class="top">
             <thead>
               <tr>
                  <td colspan="3" class="before_head_space">&#160;</td>
               </tr>
               <tr>
                   <td align="left" valign="top" width="20%">
                         <xsl:call-template name="command_menu">
                             <xsl:with-param name="title"              select="'Job menu'"/>
                             <xsl:with-param name="onclick_call"       select="'job_menu__onclick'"/>
                             <xsl:with-param name="onclick_param1_str" select="@path"/>
                         </xsl:call-template>
                   </td>
                   <td align="center" width="60%">
                       <span class="caption">JOB</span>
                   </td>
                   <td align="right" valign="top" width="20%" style="padding-right:2px;">
                         <xsl:call-template name="command_menu">
                             <xsl:with-param name="title"              select="'Job menu'"/>
                             <xsl:with-param name="onclick_call"       select="'job_menu__onclick'"/>
                             <xsl:with-param name="onclick_param1_str" select="@path"/>
                         </xsl:call-template>
                   </td>
               </tr>
               <tr>
                   <td colspan="3">
                       <xsl:if test="@enabled='no'">
                         <xsl:attribute name="class">gray</xsl:attribute>
                       </xsl:if>
                       <xsl:apply-templates mode="job_path" select="." />
                       <xsl:if test="@title">&#160;&#160;- &#160;<xsl:apply-templates select="@title"/></xsl:if>
                   </td>
               </tr>
               <xsl:call-template name="after_head_space">
                  <xsl:with-param name="colspan" select="'3'"/>
               </xsl:call-template>
             </thead>
           </table>
           
         </div>
         <div class="bottom" style="margin-bottom:4px;">
          
           <table cellpadding="0" cellspacing="0" width="100%" class="bottom">
           
              <colgroup>    
                <col align="left" width="1"/>
                <col align="left" width="50%"/>
                <col align="left" width="1"/>
                <col align="left" width="50%"/>  
              </colgroup>
              <tbody>  
                <tr>
                    <td colspan="4" class="before_head_space">&#160;</td>
                </tr>
                
                <xsl:apply-templates mode="file_timestamp" select="file_based/@last_write_time__xslt_datetime_zone_support">
                   <xsl:with-param name="colspan" select="'3'"/>
                </xsl:apply-templates>
                
                <tr>
                    <td class="label"><span class="label">State</span>:</td>
                    <td colspan="3" valign="top">
                        <xsl:apply-templates select="@state"/>
                    </td>
                </tr>
                
                <tr>
                    <td class="label"><span class="label">State text</span>:</td>
                    <td class="status_text" colspan="3" valign="top">
                        <xsl:value-of select="@state_text"/>
                    </td>
                </tr>
                
                <xsl:if test="lock.requestor/lock.use">
                  <tr>
                    <td class="label">
                        <span class="label">Locks</span>:
                    </td>
                    <td colspan="3">
                        <xsl:for-each select="lock.requestor/lock.use">
                            <xsl:if test="position() &gt; 1">
                                <xsl:text>, </xsl:text>
                            </xsl:if>
           
                            <xsl:apply-templates select="." mode="short"/>
                            <!-- For mode="long" fehlt /state/locks -->
                        </xsl:for-each>
                    </td>
                  </tr>
                </xsl:if>
                
                <xsl:if test="@process_class">
                  <tr>
                    <td class="label">
                        <span class="label">Process class</span>:
                    </td>
                    <td colspan="3">
                      <span>
                        <xsl:apply-templates mode="only_trim_slash" select="@process_class"/>
                      </span>
                    </td>
                  </tr>
                </xsl:if>
                    
                <xsl:if test="run_time/@schedule">
                  <xsl:apply-templates select="run_time/@schedule">
                    <xsl:with-param name="schedule_missing" select="file_based/requisites/requisite[translate(@type,'S','s') = 'schedule' and @path = current()/run_time/@schedule]/@is_missing = 'yes'"/>
                    <xsl:with-param name="colspan" select="'3'"/>
                  </xsl:apply-templates>
                </xsl:if>
                
                <xsl:if test="ERROR or file_based/ERROR or file_based/removed or replacement or file_based/removed/ERROR or file_based/requisites/requisite/@is_missing='yes' or lock.requestor/lock.use/@is_missing='yes'">
                  <tr>
                      <!--td class="label"><span class="label">Error</span>:</td-->
                      <td colspan="4">
                        <xsl:apply-templates mode="file_based_line" select="."/>
                    </td>
                  </tr>
                </xsl:if>
           
                <tr>
                    <td class="label"><span class="label">Next start</span>:</td>
                    <td class="task" colspan="3" valign="top">
                        <xsl:apply-templates mode="date_time_nowrap" select="@next_start_time__xslt_datetime_with_diff_plus"/>
                    </td>
                </tr>
                
                <tr>
                    <td class="label"><span class="label">Steps</span>:</td>
                    <td><xsl:value-of select="@all_steps"/></td>
                    <td class="label"><span class="label">Tasks</span>:</td>
                    <td><xsl:value-of select="@all_tasks"/></td>
                </tr>
           
                <xsl:if test="@order='yes'">
                    <tr>
                        <td class="label"><span class="label">Orders</span>:</td>
                        <td colspan="3" valign="top">
                            <xsl:if test="order_queue/@length!=''">
                                <xsl:value-of select="order_queue/@length"/>&#160; <span class="translate">orders to process</span>
                            </xsl:if>
                        </td>
                    </tr>
                </xsl:if>
                <xsl:apply-templates mode="trs_from_log_attributes" select="log">
                  <xsl:with-param name="colspan" select="'4'"/>
                </xsl:apply-templates>
              </tbody>
              <tfoot>
                <xsl:call-template name="after_body_space">
                  <xsl:with-param name="colspan" select="'4'"/>
                </xsl:call-template>
              </tfoot>  
            </table>
            
            <xsl:call-template name="round_bottom_corners"/>
        </div>
        
        <xsl:for-each select="tasks/task">
            <xsl:apply-templates select="."/>
        </xsl:for-each>

        
        <!-- Task-Warteschlange, Task-Historie oder Auftragswarteschlange zeigen? -->
        <xsl:if test="@state and @state!='not_initialized' and @state!='error'">
          
          <div>
            <img id="corner_tr" src="corner_tr.gif" alt="" />
            <ul class="tabs">
                <xsl:apply-templates mode="card_selector" select=".">
                    <xsl:with-param name="name"  select="'task_queue'"/>
                    <xsl:with-param name="title" select="'Task Queue'"/>
                    <xsl:with-param name="pos" select="'1'"/>
                </xsl:apply-templates> 
                
                <xsl:apply-templates mode="card_selector" select=".">
                    <xsl:with-param name="name"  select="'task_history'"/>
                    <xsl:with-param name="title" select="'Task History'"/>
                </xsl:apply-templates>                
                
                <xsl:if test="@order='yes'">
                    <xsl:apply-templates mode="card_selector" select=".">
                        <xsl:with-param name="name"  select="'order_queue'"/>
                        <xsl:with-param name="title" select="'Order Queue'"/>
                    </xsl:apply-templates>
                </xsl:if>
            </ul>
          </div>
          
          <xsl:if test="@my_show_card='task_queue'">
              <xsl:apply-templates select="queued_tasks" mode="list"/>
          </xsl:if>
          
          <xsl:if test="@my_show_card='task_history'">
              <xsl:apply-templates select="history" mode="list"/>
          </xsl:if>
          
          <xsl:if test="@my_show_card='order_queue'">
              <xsl:apply-templates select="order_queue" mode="list"/>
          </xsl:if>
        </xsl:if>
        </div> <!--google chrome--> 
    </xsl:template>
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~job/@state-->

    <xsl:template match="job/@state">
        <xsl:choose>
            <xsl:when test="parent::job/@enabled='no'">
                <span class="label">disabled</span>
            </xsl:when>
            
            <xsl:when test=".='pending'">
                <span class="label"><xsl:value-of select="."/></span>
            </xsl:when>

            <xsl:when test=".='running'">
                <span class="green_label"><xsl:value-of select="."/></span>
            </xsl:when>
            
            <xsl:when test="parent::job/@delay_until">
                <span class="red_label">delayed after error</span>
            </xsl:when>
            
            <xsl:otherwise>
                <span class="red_label"><xsl:value-of select="."/></span>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~job_chain/@state-->

    <xsl:template match="job_chain/@state">
        <xsl:choose>
          <xsl:when test=".='stopped' or .='under_construction'">
            <span class="red_label"><xsl:value-of select="."/></span>    
          </xsl:when>
          <xsl:when test=".='running'">
            <span class="green_label">active</span>    
          </xsl:when>
          <xsl:otherwise>
            <span class="green_label"><xsl:value-of select="."/></span>
          </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Detailsicht einer Task-->
    
    <xsl:template match="task">
        
        <div> <!--google chrome-->  
        <div class="top">
          <xsl:call-template name="round_top_corners"/>
        
          <table cellpadding="0" cellspacing="0" width="100%" border="0" class="top">
            <thead>
                <tr>
                   <td colspan="3" class="before_head_space">&#160;</td>
                </tr>
                <tr>
                    <td align="left" valign="top" width="20%">
                        <xsl:if test="@id">
                        <xsl:call-template name="command_menu">
                            <xsl:with-param name="title"              select="'Task menu'"/>
                            <xsl:with-param name="onclick_call"       select="'task_menu__onclick'"/>
                            <xsl:with-param name="onclick_param1_str" select="@id"/>
                        </xsl:call-template>
                        </xsl:if>
                    </td>
                    <td align="center" width="60%">
                        <span class="caption">TASK</span>
                    </td>
                    <td align="right" valign="top" width="20%" style="padding-right:2px;">
                        <xsl:if test="@id">
                        <xsl:call-template name="command_menu">
                            <xsl:with-param name="title"              select="'Task menu'"/>
                            <xsl:with-param name="onclick_call"       select="'task_menu__onclick'"/>
                            <xsl:with-param name="onclick_param1_str" select="@id"/>
                        </xsl:call-template>
                        </xsl:if>
                    </td>
                </tr>
                <xsl:call-template name="after_head_space">
                  <xsl:with-param name="colspan" select="'3'"/>
                </xsl:call-template>
              </thead>
              <tbody>
                <tr>
                    <td colspan="3">
                        <xsl:choose>
                            <xsl:when test="not( @id )">
                                <xsl:if test="../../@waiting_for_process='yes'">
                                    (<span class="translate">needs process</span>)
                                </xsl:if>
                            </xsl:when>
                            <xsl:otherwise>
                                <b><xsl:apply-templates mode="trim_slash" select="@id" /></b>
                            </xsl:otherwise>
                        </xsl:choose>
                        
                        <xsl:if test="@pid">
                            <xsl:text>, </xsl:text><span class="translate">Pid</span><xsl:text> </xsl:text>
                            <xsl:value-of select="@pid"/>
                        </xsl:if>
                        <xsl:if test="@cause!=''">
                            <xsl:text> (</xsl:text><span class="translate" style="white-space:nowrap;">start cause</span><xsl:text>:&#160;</xsl:text><span class="translate"><xsl:value-of select="@cause"/></span><xsl:text>)</xsl:text>
                        </xsl:if>
                        <xsl:if test="@state!=''">
                            <xsl:text>, </xsl:text>
                            <xsl:apply-templates select="@state" />
                        </xsl:if>
                        <xsl:if test="@calling">
                            <xsl:text> </xsl:text>(<xsl:value-of select="@calling"/>)
                        </xsl:if>
                        <xsl:if test="@steps!=''">
                            <xsl:text>, </xsl:text>
                            <xsl:value-of select="@steps"/><xsl:text> </xsl:text><span class="translate">steps</span>
                        </xsl:if>
                    </td>
                </tr>
            </tbody>
          </table>
          
        </div>
        <div class="bottom" style="margin-bottom:4px;">
          
          <table cellpadding="0" cellspacing="0" class="bottom" width="100%" >
            <colgroup>
                <col align="left"  width="1"/>
                <col align="left"  /> 
            </colgroup>
            
            <tbody>
              <tr><td colspan="2"><span class="before_head_space">&#160;</span></td></tr>
                
              <xsl:if test="order or ../../@order='yes'">
                  <tr>
                    <td class="label"><span class="label">Order</span>:</td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="order">
                                <xsl:attribute name="style">cursor: pointer;
                                    <xsl:if test="order/@removed='yes' or order/@replaced='yes'">
                                        text-decoration: line-through;
                                    </xsl:if>
                                </xsl:attribute>

                                <xsl:if test="order/@removed='yes'">
                                    <xsl:attribute name="title">Order is deleted</xsl:attribute>
                                </xsl:if>
                                
                                <xsl:if test="order/@replaced='yes'">
                                    <xsl:attribute name="title">Order is replaced</xsl:attribute>
                                </xsl:if>
                                
                                <b>
                                    <xsl:apply-templates mode="trim_slash" select="order/@order" />
                                    &#160;
                                    <xsl:value-of select="order/@title"/>
                                </b>
                            </xsl:when>
                            <xsl:when test="@state='running_waiting_for_order'">
                                <span class="translate">waiting for order</span> ...
                            </xsl:when>
                        </xsl:choose>
                    </td>
                  </tr>
              </xsl:if>

              <xsl:choose>
                <xsl:when test="@idle_since">
                    <tr>
                        <td class="label"><span class="label">Idle since</span>:</td>
                        <td>
                            <span class="task">
                                  <xsl:apply-templates mode="date_time_nowrap" select="@idle_since__xslt_datetime_with_diff"/>
                            </span>
                        </td>
                    </tr>
                </xsl:when>
                <xsl:otherwise>
                    <tr>
                        <td class="label"><span class="label">In process since</span>:</td>
                        <td>
                            <span class="task">
                                <xsl:apply-templates mode="date_time_nowrap" select="@in_process_since__xslt_datetime_with_diff"/>
                            </span>
                        </td>
                    </tr>
                </xsl:otherwise>
              </xsl:choose>

              <tr>
                <td class="label"><span class="label">Running since</span>:</td>
                <td>
                  <span class="task">
                    <xsl:apply-templates mode="date_time_nowrap" select="@running_since__xslt_datetime_with_diff"/>
                  </span>
                </td>
              </tr>

              <xsl:if test="@enqueued">
                <tr>
                    <td class="label"><span class="label">Enqueued at</span>:</td>
                    <td>
                        <span class="task">
                            <xsl:apply-templates mode="date_time_nowrap" select="@enqueued__xslt_datetime_with_diff"/>
                        </span>
                    </td>
                </tr>
              </xsl:if>
              <xsl:apply-templates mode="trs_from_log_attributes" select="log">
                  <xsl:with-param name="colspan" select="'1'"/>
              </xsl:apply-templates>
              <xsl:if test="subprocesses/subprocess">
                <tr><td colspan="2"><span class="translate">Subprocesses</span>:</td></tr>
                <xsl:for-each select="subprocesses/subprocess">
                    <tr>
                        <td class="label"><span class="label">Pid</span> <xsl:value-of select="@pid" /></td>
                        <td class="task"><xsl:value-of select="@title"/></td>
                    </tr>
                </xsl:for-each>
              </xsl:if>
           </tbody>
           <tfoot>
              <xsl:call-template name="after_body_space">
                  <xsl:with-param name="colspan" select="'2'"/>
              </xsl:call-template>            
           </tfoot>
        </table>
        
        <xsl:call-template name="round_bottom_corners"/>
      </div>
      </div> <!--google chrome-->
    </xsl:template>
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~task/@state-->

    <xsl:template match="task/@state">
        <xsl:choose>
            <xsl:when test=". = 'suspended' or . = 'waiting_for_process' or . = 'deleting_files'">
                <span class="job_error"><xsl:value-of select="."/></span>
            </xsl:when>
            <xsl:otherwise>
                <span class="translate"><xsl:value-of select="."/></span>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>

    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~queued_tasks-->

    <xsl:template match="queued_tasks" mode="list">
        
      <div class="bottom">
        <table valign="top" cellpadding="0" cellspacing="0" width="100%" class="bottom"> 
          <colgroup>
              <col align="left" width="40"/>
              <col align="left" width="70"/>
              <col align="left" width="*"/>
              <col align="left" width="80"/>
          </colgroup>
            
          <xsl:if test="not( queued_task )">
            <thead>
              <xsl:call-template name="after_head_space">
                  <xsl:with-param name="colspan" select="'4'"/>
              </xsl:call-template>
            </thead>
            <tbody>
              <tr>
                <td colspan="4"><b> <xsl:value-of select="@length"/>&#160; <span class="translate">enqueued tasks</span></b></td>
              </tr>
              <xsl:call-template name="after_body_space">
                  <xsl:with-param name="colspan" select="'4'"/>
              </xsl:call-template>
            </tbody>
          </xsl:if>
          <xsl:if test="queued_task"> 
            <thead>
               <tr>
                   <td colspan="4" class="before_head_space">&#160;</td>
               </tr>
               <tr>
                   <td class="head1"><span class="translate">Id</span></td>
                   <td class="head"><span class="translate" style="white-space:nowrap;">Enqueued</span></td>
                   <td class="head"><span class="translate" style="white-space:nowrap;">Start at</span></td>
                   <td class="head1">&#160;</td>
               </tr>
               <xsl:call-template name="after_head_space">
                  <xsl:with-param name="colspan" select="'4'"/>
              </xsl:call-template>
            </thead>
            <tbody>
                <xsl:for-each select="queued_task">
                    <tr>
                        <td>
                            <xsl:value-of select="@task"/>
                            <xsl:if test="@name!=''">
                                &#160; <xsl:value-of select="@name"/>
                            </xsl:if>
                        </td>

                        <td><xsl:apply-templates mode="date_time_nowrap" select="@enqueued__xslt_date_or_time"/></td>
                        <td class="task"><xsl:apply-templates mode="date_time_nowrap" select="@start_at__xslt_datetime_with_diff"/></td>
                        <td align="right" valign="top" style="padding:0px 2px;">
                            <xsl:call-template name="command_menu">
                                <xsl:with-param name="title"              select="'Delete'"/>
                                <xsl:with-param name="onclick_call"       select="'queued_task_menu__onclick'"/>
                                <xsl:with-param name="onclick_param1_str" select="@task"/>
                            </xsl:call-template>                    
                        </td>
                    </tr>
                </xsl:for-each> 
              </tbody>
              <tfoot>
                <xsl:call-template name="after_body_space">
                  <xsl:with-param name="colspan" select="'4'"/>
                </xsl:call-template>
             </tfoot>
          </xsl:if>
        </table>
        
        <xsl:call-template name="round_bottom_corners"/>
      </div>
    </xsl:template>
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~history-->

    <xsl:template match="history" mode="list">

      <div class="bottom">
        <table valign="top" cellpadding="0" cellspacing="0" width="100%" class="bottom">
          <colgroup>
            <col align="left" width="10"/>
            <col align="left" width="10"/>
            <col align="left" width="*"/>
            <col align="right" width="10"/>
            <col align="left" width="*"/>
            <col align="left" width="80"/>
          </colgroup>
          
            <xsl:choose>
              <xsl:when test="count(history.entry)=0"> 
                <thead>
                  <xsl:call-template name="after_head_space">
                    <xsl:with-param name="colspan" select="'6'"/>
                  </xsl:call-template>
                </thead>
                <tbody>
                  <tr>
                    <td colspan="6"><span class="translate" style="font-weight:bold;">No tasks in the history</span></td>
                  </tr>
                </tbody>
              </xsl:when>
              <xsl:otherwise>
                
                <thead>
                    <tr>
                        <td colspan="6" class="before_head_space">&#160;</td>
                    </tr>
                
                    <tr>
                        <td class="head1"><span class="translate">Id</span></td>
                        <td class="head"><span class="translate">Cause</span></td>
                        <td class="head"><span class="translate">Started</span></td>
                        <td class="head"><span class="translate">Steps</span></td>
                        <td class="head" colspan="2"><span class="translate">Ended</span></td>
                    </tr>
                    <xsl:call-template name="after_head_space">
                      <xsl:with-param name="colspan" select="'6'"/>
                    </xsl:call-template>
                </thead>
                <tbody>
                
                <xsl:for-each select="history.entry">
                    <tr>
                        <td><xsl:value-of select="@task"/>&#160;</td>
                        <td><nobr><span class="translate"><xsl:value-of select="@cause"/></span></nobr>&#160;</td>
                        <td><xsl:apply-templates mode="date_time_nowrap" select="@start_time__xslt_datetime"/></td>
                        <td align="right"><xsl:value-of select="@steps"/>&#160;</td>
                        <td><xsl:apply-templates mode="date_time_nowrap" select="@end_time__xslt_datetime"/></td>
                
                        <td align="right" valign="top" style="padding:0px 2px;">
                            
                            <xsl:call-template name="command_menu">
                                <xsl:with-param name="title"              select="'Show log'"/>
                                <xsl:with-param name="onclick_call"       select="'history_task_menu__onclick'"/>
                                <xsl:with-param name="onclick_param1_str" select="@task"/>
                            </xsl:call-template>                    
                        </td>
                    </tr>
                
                    <xsl:if test="ERROR">
                        <tr>
                            <td>&#160;</td>
                            <td colspan="4" class="job_error"><xsl:apply-templates select="ERROR"/></td>
                            <td>&#160;</td>
                        </tr>
                    </xsl:if>
                </xsl:for-each>
                </tbody>
              </xsl:otherwise>
            </xsl:choose>
            
            <tfoot>
              <xsl:call-template name="after_body_space">
                  <xsl:with-param name="colspan" select="'6'"/>
              </xsl:call-template>
            </tfoot>
        </table>
        
        <xsl:call-template name="round_bottom_corners"/>
      </div>
      
    </xsl:template>
        
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Order_queue-->

    <xsl:template match="order_queue" mode="list">
        <xsl:param name="max_orders" select="999999999"/>
        
        <div class="bottom">
          <table class="bottom" cellpadding="0" cellspacing="0" width="100%">
            
              <colgroup>
                <col width="*"/>
                <col width="1%"/>
                <col width="1%"/>  
                <col width="80" align="right"/>  
              </colgroup> 
              <xsl:if test="@length=0">
                <thead>
                    <xsl:call-template name="after_head_space">
                      <xsl:with-param name="colspan" select="'4'"/>
                    </xsl:call-template>
                </thead>
                <tbody>
                    <tr>
                        <td colspan="4"><b>0&#160; <span class="translate">orders to process</span></b></td>
                    </tr>
                </tbody>
              </xsl:if>
              <xsl:if test="not(@length=0)">
                <thead>
                    <tr>
                        <td colspan="4" class="before_head_space">&#160;</td>
                    </tr>
                    <tr>
                        <td class="head1"><span class="translate">Id</span></td>
                        <td class="head"><span class="translate">Next start</span></td>
                        <td class="head"><span class="translate">State</span></td>
                        <td class="head1">&#160;</td>
                    </tr>
                    <xsl:call-template name="after_head_space">
                      <xsl:with-param name="colspan" select="'4'"/>
                    </xsl:call-template>
                </thead>
                
                <tbody>
                    <xsl:for-each select="order[ position() &lt;= $max_orders ]">
                        <tr class="list" title="show job chain details">
                            <td>
                              <xsl:attribute name="onclick" >callErrorChecked( 'show_job_chain_details', '<xsl:value-of select="@job_chain"/>' )</xsl:attribute>
                              <xsl:apply-templates mode="trim_slash" select="@job_chain" />,<xsl:value-of select="@id"/>
                            </td>
                            <td>
                              <xsl:attribute name="onclick" >callErrorChecked( 'show_job_chain_details', '<xsl:value-of select="@job_chain"/>' )</xsl:attribute>
                              <xsl:apply-templates mode="properties" select="." />
                            </td>
                            <td style="white-space:nowrap;">
                              <xsl:attribute name="onclick" >callErrorChecked( 'show_job_chain_details', '<xsl:value-of select="@job_chain"/>' )</xsl:attribute>
                              <xsl:value-of select="@state"/>
                            </td>
                            
                            <td style="padding:0px 2px;text-align:right;" title="Order menu">
                                <xsl:call-template name="command_menu">
                                    <xsl:with-param name="title"              select="'Order menu'"/>
                                    <xsl:with-param name="onclick_call"       select="'order_menu__onclick'"/>
                                    <xsl:with-param name="onclick_param1_str" select="@job_chain"/>
                                    <xsl:with-param name="onclick_param2_str" select="@id"/>
                                    <xsl:with-param name="onclick_param3_str" select="'order_queue'"/>
                                </xsl:call-template>
                            </td>
                        </tr>
                    </xsl:for-each>
                  </tbody>
                </xsl:if>
                <tfoot>
                  <xsl:call-template name="after_body_space">
                      <xsl:with-param name="colspan" select="'4'"/>
                  </xsl:call-template>
                </tfoot>
          </table>
          
          <xsl:call-template name="round_bottom_corners"/>
        </div>
    </xsl:template>
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Order_history-->

    <xsl:template match="order_history" mode="list">
        <xsl:param name="max_order_history" select="999999999"/>
        <xsl:param name="big_chain" select="@big_chain"/>
        
          <table class="bottom" cellpadding="0" cellspacing="0" width="100%">
            
              <colgroup>
                <col width="*"/>
                <col width="*"/>
                <col width="*"/>
                <col width="40"/>  
                <col width="60" align="right"/>  
              </colgroup>
              <xsl:if test="count(child::*)=0">
                <thead>
                    <xsl:call-template name="after_head_space">
                      <xsl:with-param name="colspan" select="'5'"/>
                    </xsl:call-template>
                </thead>
                <tbody>
                    <tr>
                        <td colspan="5"><span class="translate" style="font-weight:bold;">No orders in the history</span></td>
                    </tr>
                </tbody>
              </xsl:if>
              <xsl:if test="not(count(child::*)=0)">
                <thead>
                    <tr>
                        <td colspan="5" class="before_head_space">&#160;</td>
                    </tr>
                    <tr>
                        <td class="head1"><span class="translate">Id</span></td>
                        <xsl:choose>
                          <xsl:when test="$big_chain">
                            <td class="head"><span class="translate">Ended</span></td>  
                            <td class="head"><span class="translate">Job chain</span></td>
                          </xsl:when>
                          <xsl:otherwise>
                            <td colspan="2" class="head"><span class="translate">Ended</span></td>
                          </xsl:otherwise>
                        </xsl:choose>
                        <td class="head"><span class="translate">State</span></td>
                        <td class="head1">&#160;</td>
                    </tr>
                    <xsl:call-template name="after_head_space">
                      <xsl:with-param name="colspan" select="'5'"/>
                    </xsl:call-template>
                </thead>
                
                <tbody>
                    <xsl:if test="ERROR">
                        <tr><td colspan="5" class="job_error"><xsl:value-of select="ERROR/@text"/></td></tr>
                    </xsl:if> 
                    <xsl:for-each select="order[ position() &lt;= $max_order_history ]">
                        <xsl:sort select="concat(@end_time,@start_time)" order="descending"/>
                        <tr>
                            <td>                
                              <xsl:if test="@state = ancestor::job_chain[@path=current()/@job_chain or substring(@path,2)=current()/@job_chain]/job_chain_node/@error_state">
                                <xsl:attribute name="style">color: crimson</xsl:attribute>
                              </xsl:if>
                              <b><xsl:apply-templates mode="trim_slash" select="@id" /></b>
                            </td>
                            <xsl:choose>
                              <xsl:when test="$big_chain">
                                <td><xsl:apply-templates mode="date_time_nowrap" select="@end_time__xslt_datetime"/></td>  
                                <td><xsl:apply-templates mode="trim_slash" select="@job_chain" /></td>
                              </xsl:when>
                              <xsl:otherwise>
                                <td colspan="2"><xsl:apply-templates mode="date_time_nowrap" select="@end_time__xslt_datetime"/></td>
                              </xsl:otherwise>
                            </xsl:choose>
                            <td><nobr><xsl:value-of select="@state"/></nobr></td>
                            
                            <td tyle="padding:0px 2px;text-align:right;">
                                <xsl:call-template name="command_menu">
                                    <xsl:with-param name="title"              select="'Show log'"/>
                                    <xsl:with-param name="onclick_call"       select="'order_history_menu__onclick'"/>
                                    <xsl:with-param name="onclick_param1_str" select="@job_chain"/>
                                    <xsl:with-param name="onclick_param2_str" select="@id"/>
                                    <xsl:with-param name="onclick_param3"     select="@history_id"/>
                                    <xsl:with-param name="onclick_param4_str" select="@end_time"/>
                                </xsl:call-template>
                            </td>
                        </tr>
                    </xsl:for-each>
                </tbody>
                </xsl:if>
                <tfoot>
                  <xsl:call-template name="after_body_space">
                      <xsl:with-param name="colspan" select="'5'"/>
                  </xsl:call-template>
                </tfoot>
          </table>
    </xsl:template>
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Order-->

    <xsl:template match="order">
        <xsl:variable name="menu_caller">
           <xsl:choose>
             <xsl:when test="@on_blacklist='yes'">blacklist</xsl:when>
             <xsl:otherwise>order</xsl:otherwise>
           </xsl:choose>
        </xsl:variable>
        <xsl:variable name="job_chain_path">
          <xsl:choose>
            <xsl:when test="order.job_chain_stack/order.job_chain_stack.entry/@job_chain"><xsl:apply-templates mode="trim_slash" select="order.job_chain_stack/order.job_chain_stack.entry/@job_chain" /></xsl:when>
            <xsl:otherwise><xsl:apply-templates mode="trim_slash" select="@job_chain" /></xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        
        <div> <!--google chrome-->
        <div class="top">
           <xsl:call-template name="round_top_corners"/>
        
           <table cellpadding="0" cellspacing="0" width="100%" border="0" class="top">
             <thead>
               <tr>
                  <td colspan="3" class="before_head_space">&#160;</td>
               </tr>
               <xsl:choose>
                 <xsl:when test="not(parent::answer/ERROR)">
                   <tr>
                       <td align="left" valign="top" width="20%">
                             <xsl:call-template name="command_menu">
                                 <xsl:with-param name="title"              select="'Order menu'"/>
                                 <xsl:with-param name="onclick_call"       select="'order_menu__onclick'"/>
                                 <xsl:with-param name="onclick_param1_str" select="@job_chain"/>
                                 <xsl:with-param name="onclick_param2_str" select="@id"/>
                                 <xsl:with-param name="onclick_param3_str" select="$menu_caller"/>
                             </xsl:call-template>
                       </td>
                       <td align="center" width="60%">
                           <span class="caption">ORDER</span>
                       </td>
                       <td align="right" valign="top" width="20%" style="padding:0px 2px;">
                             <xsl:call-template name="command_menu">
                                 <xsl:with-param name="title"              select="'Order menu'"/>
                                 <xsl:with-param name="onclick_call"       select="'order_menu__onclick'"/>
                                 <xsl:with-param name="onclick_param1_str" select="@job_chain"/>
                                 <xsl:with-param name="onclick_param2_str" select="@id"/>
                                 <xsl:with-param name="onclick_param3_str" select="$menu_caller"/>
                             </xsl:call-template>
                       </td>
                   </tr>
                   <tr>
                       <td colspan="3">
                           <b><xsl:value-of select="$job_chain_path" />,<xsl:value-of select="@id"/></b>
                           <xsl:if test="@title">
                               <xsl:text>&#160;&#160;- &#160;</xsl:text><xsl:apply-templates select="@title"/>
                           </xsl:if>
                       </td>
                   </tr>
                 </xsl:when>
                 <xsl:otherwise>
                   <tr>
                     <td width="20%">&#160;</td>
                     <td align="center" width="60%"><span class="caption">ORDER</span></td>
                     <td width="20%">&#160;</td>
                   </tr>
                 </xsl:otherwise>
               </xsl:choose>
               <xsl:call-template name="after_head_space">
                  <xsl:with-param name="colspan" select="'3'"/>
               </xsl:call-template>
             </thead>
           </table>
           
         </div>
         <div class="bottom">
            
           <table cellpadding="0" cellspacing="0" width="100%" class="bottom">
           
              <colgroup>    
                <col align="left" width="1%"/>
                <col align="left" width="99%"/>
                <col align="right" width="94"/>
              </colgroup>
              <tbody>
                <tr class="list" title="show job chain details" style="vertical-align:top;">
                    <xsl:attribute name="onclick" >callErrorChecked( 'show_job_chain_details', '<xsl:value-of select="concat('/',$job_chain_path)"/>' )</xsl:attribute>
                    
                    <td class="label"><span class="label">Job chain</span>:</td>
                    <td colspan="2">
                      <xsl:value-of select="$job_chain_path" />
                    </td>
                </tr>
                <tr class="list" title="show job details" style="vertical-align:top;">
                    <xsl:attribute name="onclick" >callErrorChecked( 'show_job_details', '<xsl:value-of select="@job"/>' )</xsl:attribute>
                    
                    <td class="label"><span class="label">Job</span>:</td>
                    <td colspan="2">
                        <xsl:apply-templates mode="trim_slash" select="@job"/>
                    </td>
                </tr>
                
                <tr>
                    <td colspan="3" class="before_head_space">&#160;</td>
                </tr>
                
                <xsl:apply-templates mode="file_timestamp" select="file_based/@last_write_time__xslt_datetime_zone_support">
                   <xsl:with-param name="colspan" select="'2'"/>
                </xsl:apply-templates>
                
                <tr>
                    <td class="label"><span class="label">State</span>:&#160;</td>
                    <td colspan="2">
                        <xsl:apply-templates select="@state"/>
                    </td>
                </tr>
                
                <xsl:if test="parent::answer/ERROR">
                  <tr>
                        <td class="label"><span class="label">Error</span>:</td>
                        <td colspan="2">
                            <span class="job_error"><xsl:value-of select="parent::answer/ERROR/@text"/></span>
                        </td>
                    </tr>
                </xsl:if>
                
                <xsl:if test="file_based/ERROR or file_based/removed or replacement or file_based/removed/ERROR or file_based/requisites/requisite/@is_missing='yes'">
                    <tr>
                        <td colspan="3">
                            <xsl:apply-templates mode="file_based_line" select="."/>
                        </td>
                    </tr>
                </xsl:if>
                
                <tr>
                  <xsl:choose>
                     <xsl:when test="@setback">
                         <td class="label"><span class="label">Setback</span>:&#160;</td>
                         <td class="red_value" valign="top" colspan="2"><xsl:apply-templates mode="date_time_nowrap" select="@setback__xslt_date_or_time_with_diff"/></td>
                     </xsl:when>
                     <xsl:when test="@next_start_time">
                         <td class="label"><span class="label">Next start</span>:&#160;</td>
                         <td class="green_value" valign="top" colspan="2"><xsl:apply-templates mode="date_time_nowrap" select="@next_start_time__xslt_datetime_with_diff"/></td>
                     </xsl:when>
                     <xsl:when test="@start_time">
                         <td class="label"><span class="label">Running since</span>:&#160;</td>
                         <td class="green_value" valign="top" colspan="2"><xsl:apply-templates mode="date_time_nowrap" select="@start_time__xslt_datetime_with_diff"/></td>
                     </xsl:when>
                  </xsl:choose>
                </tr>
                
                <tr>
                  <td>&#160;</td>
                  <td colspan="">
                    <xsl:apply-templates mode="properties" select="." >
                      <xsl:with-param name="with_datetimes" select="false()"/>
                    </xsl:apply-templates>
                  </td>
                </tr>
                
                
                <xsl:if test="run_time/@schedule">
                  <xsl:apply-templates select="run_time/@schedule">
                    <xsl:with-param name="schedule_missing" select="file_based/requisites/requisite[translate(@type,'S','s') = 'schedule' and @path = current()/run_time/@schedule]/@is_missing = 'yes'"/>
                    <xsl:with-param name="colspan" select="'2'"/>
                  </xsl:apply-templates>
                </xsl:if>
                
              </tbody>
              <tfoot>
                <xsl:call-template name="after_body_space">
                  <xsl:with-param name="colspan" select="'3'"/>
                </xsl:call-template>
              </tfoot>  
            </table>
            
            <xsl:call-template name="round_bottom_corners"/>
        </div>
        </div>
    
    </xsl:template>
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Order in History-->

    <xsl:template match="order" mode="history">
        <xsl:param name="faultily" select="@state = ancestor::job_chain[@path=current()/@job_chain or substring(@path,2)=current()/@job_chain]/job_chain_node/@error_state"/>

        <tr class="list" style="vertical-align:top" title="show job chain details">
            <td>
              <xsl:attribute name="onclick">callErrorChecked( 'show_job_chain_details', '<xsl:value-of select="@job_chain"/>' )</xsl:attribute>
              <xsl:if test="$faultily">
                <xsl:attribute name="class">red</xsl:attribute>
              </xsl:if>
              <b><xsl:apply-templates mode="trim_slash" select="@id" /></b>
            </td>
            <td>
              <xsl:attribute name="onclick">callErrorChecked( 'show_job_chain_details', '<xsl:value-of select="@job_chain"/>' )</xsl:attribute>
              <xsl:apply-templates mode="date_time_nowrap" select="@start_time__xslt_datetime"/>
            </td>
            <td>
              <xsl:attribute name="onclick">callErrorChecked( 'show_job_chain_details', '<xsl:value-of select="@job_chain"/>' )</xsl:attribute>
              <xsl:apply-templates mode="date_time_nowrap" select="@end_time__xslt_datetime"/>
            </td>
            <td style="padding-right:2px;">
              <xsl:attribute name="onclick">callErrorChecked( 'show_job_chain_details', '<xsl:value-of select="@job_chain"/>' )</xsl:attribute>
              <xsl:apply-templates mode="trim_slash" select="@job_chain" />
            </td>
            <td style="padding-right:2px;">
              <xsl:attribute name="onclick">callErrorChecked( 'show_job_chain_details', '<xsl:value-of select="@job_chain"/>' )</xsl:attribute>
              <nobr><xsl:value-of select="@state"/></nobr>
            </td>
            
            <td style="padding:0px 2px;text-align:right;" title="Show order log">
                <xsl:call-template name="command_menu">
                    <xsl:with-param name="title"              select="'Show order log'"/>
                    <xsl:with-param name="onclick_call"       select="'order_history_menu__onclick'"/>
                    <xsl:with-param name="onclick_param1_str" select="@job_chain"/>
                    <xsl:with-param name="onclick_param2_str" select="@id"/>
                    <xsl:with-param name="onclick_param3"     select="@history_id"/>
                    <xsl:with-param name="onclick_param4_str" select="@end_time"/>
                </xsl:call-template>
            </td>
        </tr>
    </xsl:template>
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~history.entry-->

    <xsl:template match="history.entry" mode="history">
        <xsl:param name="highlightning" select="false()"/>
        <tr class="list" style="vertical-align:top" title="show job details">
            <td>
              <xsl:attribute name="onclick">callErrorChecked( 'show_job_details', '<xsl:value-of select="@job_name"/>' )</xsl:attribute>
              <xsl:choose>
                <xsl:when test="ERROR">
                  <xsl:attribute name="class">red</xsl:attribute>
                </xsl:when>
                <xsl:when test="$highlightning">
                  <xsl:attribute name="style">color:#808080;</xsl:attribute>
                </xsl:when>
              </xsl:choose>
              <b><xsl:apply-templates mode="trim_slash" select="@job_name" /></b>
            </td>
            <td>
              <xsl:attribute name="onclick">callErrorChecked( 'show_job_details', '<xsl:value-of select="@job_name"/>' )</xsl:attribute>
              <xsl:apply-templates mode="date_time_nowrap" select="@start_time__xslt_datetime"/>
            </td>
            <td>
              <xsl:attribute name="onclick">callErrorChecked( 'show_job_details', '<xsl:value-of select="@job_name"/>' )</xsl:attribute>
              <xsl:apply-templates mode="date_time_nowrap" select="@end_time__xslt_datetime"/>
            </td>
            <td style="padding-right:2px;">
              <xsl:attribute name="onclick">callErrorChecked( 'show_job_details', '<xsl:value-of select="@job_name"/>' )</xsl:attribute>
              <xsl:if test="$highlightning">
                  <xsl:attribute name="class">task</xsl:attribute>
              </xsl:if>
              <span class="translate"><xsl:value-of select="@cause"/></span>
            </td>
            <td style="padding-right:2px;">
              <xsl:attribute name="onclick">callErrorChecked( 'show_job_details', '<xsl:value-of select="@job_name"/>' )</xsl:attribute>
              <xsl:if test="$highlightning">
                  <xsl:attribute name="class">task</xsl:attribute>
              </xsl:if>
              <xsl:value-of select="@exit_code"/>
            </td>
            
            <td style="padding:0px 2px;text-align:right;" title="Show task log">
                <xsl:call-template name="command_menu">
                    <xsl:with-param name="title"              select="'Show task log'"/>
                    <xsl:with-param name="onclick_call"       select="'history_task_menu__onclick'"/>
                    <xsl:with-param name="onclick_param1_str" select="@task"/>
                </xsl:call-template>
            </td>
        </tr>
        <xsl:if test="ERROR and /spooler/@show_task_error_checkbox">
            <tr>
                <td colspan="6" class="job_error" style="padding-left:10px;">
                    <xsl:apply-templates select="ERROR"/>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ERROR-->

    <xsl:template match="ERROR">
        <xsl:if test="@time != ''">
            <xsl:value-of select="@time"/><xsl:text> </xsl:text>
        </xsl:if>

        <xsl:value-of select="@text"/>
        
        <xsl:if test="@source or @line or @col"><xsl:text> [</xsl:text>
        </xsl:if>
        
        <xsl:if test="@source">
            <xsl:text> </xsl:text>
            <span class="translate">source</span> <xsl:value-of select="@source"/>
        </xsl:if>

        <xsl:if test="@line">
            <xsl:text> </xsl:text>
            <span class="translate">line</span> <xsl:value-of select="@line"/>
        </xsl:if>

        <xsl:if test="@col">
            <xsl:text> </xsl:text>
            <span class="translate">column</span> <xsl:value-of select="@col"/>
        </xsl:if>
        
        <xsl:if test="@source or @line or @col"><xsl:text>]</xsl:text>
        </xsl:if>
    </xsl:template>
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~log-->

    <xsl:template mode="trs_from_log_attributes" match="log">
        <xsl:param name="colspan"/>
        
        <tr>
            <td>
                <xsl:attribute name="colspan"><xsl:value-of select="number($colspan)+1"/></xsl:attribute>
                <xsl:apply-templates mode="string_from_log_attributes" select="."/>
            </td>
        </tr>
        
        <xsl:if test="@mail_subject">
            <tr>
                <td class="label"><span class="label">Mail subject</span>:</td>
                <td>
                    <xsl:attribute name="colspan"><xsl:value-of select="$colspan"/></xsl:attribute>
                    <xsl:value-of select="@mail_subject"/>
                </td>
            </tr>
        </xsl:if>
        
        <xsl:if test="@last_error">
            <tr>
                <td class="label"><span class="label">Last error</span>:</td>
                <td>
                    <xsl:attribute name="colspan"><xsl:value-of select="$colspan"/></xsl:attribute>
                    <xsl:value-of select="@last_error"/>
                </td>
            </tr>
        </xsl:if>
        
        <xsl:if test="@last_warning">
            <tr>
                <td class="label"><span class="label">Last warning</span>:</td>
                <td>
                    <xsl:attribute name="colspan"><xsl:value-of select="$colspan"/></xsl:attribute>
                    <xsl:value-of select="@last_warning"/>
                </td>
            </tr>
        </xsl:if>
        
    </xsl:template>
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~log-->

    <xsl:template mode="string_from_log_attributes" match="log">
    
        <xsl:if test="@level">
            <span class="label">Log level</span><span class="small">:</span>
            <xsl:value-of select="@level"/>
            <span>&#160; </span> 
        </xsl:if>
        
        <xsl:if test="@mail_on_error='yes'">
            <span class="label">mail on error</span><span> &#160; </span>
        </xsl:if>
        
        <xsl:if test="@mail_on_warning='yes'">
            <span class="label">mail on warning</span><span> &#160; </span>
        </xsl:if>
        
        <xsl:if test="@mail_on_success='yes'">
            <span class="label">mail on success</span><span> &#160; </span>
        </xsl:if>
        
        <xsl:if test="@mail_on_process">
            <span style="white-space:nowrap;">
            <span class="label">mail on process</span><span class="small">:</span>
            <xsl:value-of select="@mail_on_process"/>
            </span>
            <span>&#160; </span> 
        </xsl:if>

        <xsl:if test="@mail_to">
            <span style="white-space:nowrap;">
            <span class="label">To</span><span class="small">:</span>
            <xsl:value-of select="@mail_to"/>
            </span>
            <span>&#160; </span> 
        </xsl:if>
        
        <xsl:if test="@mail_cc">
            <span style="white-space:nowrap;">
            <span class="label">CC</span><span class="small">:</span>
            <xsl:value-of select="@mail_cc"/>
            </span>
            <span>&#160; </span> 
        </xsl:if>
        
        <xsl:if test="@mail_bcc">
            <span style="white-space:nowrap;">
            <span class="label">BCC</span><span class="small">:</span>
            <xsl:value-of select="@mail_bcc"/>
            </span>
            <span>&#160; </span>
        </xsl:if>
        
        <xsl:if test="@mail_from">
            <span style="white-space:nowrap;">
            <span class="label">From</span><span class="small">:</span>
            <xsl:value-of select="@mail_from"/>
            </span>
            <span>&#160; </span> 
        </xsl:if>
        
        <xsl:if test="@smtp">
            <span style="white-space:nowrap;">
            <span class="label">SMTP</span><span class="small">:</span>
            <xsl:value-of select="@smtp"/>
            </span>
            <span>&#160; </span>
        </xsl:if>
        
    </xsl:template>
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~log_categories-->
    <xsl:template match="log_categories">
        
        <form name="log_categories" id="log_categories" method="post" action="#" onsubmit="reset_categories(this.elements.delay);return false;" style="margin:0px;">
        <div class="top">
          <xsl:call-template name="round_top_corners"/>
          <table cellpadding="0" cellspacing="1" width="100%" border="0" class="top">
          
            <colgroup>
              <col width="88"/>
              <col width="100"/>
              <col width="60"/>
              <col width="1"/>
              <col width="*"/>
              <col width="10"/>
            </colgroup>
            
            <thead>
                <tr>
                   <td colspan="6" class="before_head_space">&#160;</td>
                </tr>
                <tr>
                   <td colspan="6">&#160;<span class="translate">The default log caregories are marked</span>&#160;<span class="translate" style="color:#EA7A14;">orange</span>&#160;<span class="translate">and they are active after each reset.</span></td>
                </tr>
                <tr>
                   <td colspan="6" class="before_head_space">&#160;</td>
                </tr>
                <tr>
                   <td colspan="2" align="right" style="line-height:12px;padding:4px 0px;">&#160;<span class="translate" style="white-space:nowrap;">Current log categories setting</span>:&#160;</td>
                   <td colspan="3" style="line-height:12px;background-color:white;padding:2px;border:1px solid #8B919F"><code style="color:graytext;"><xsl:value-of select="@categories"/>&#160;</code></td>
                   <td style="line-height:12px;">&#160;&#160;</td>
                </tr>
                <tr>
                   <td style="padding-left:4px;vertical-align:middle;"><div class="button" onclick="reset_categories(document.forms.log_categories.elements.delay);">
                     <xsl:choose>
                       <xsl:when test="/spooler/@my_ie_version &gt; 0 and /spooler/@my_ie_version &lt; 8">
                         <a class="translate" href="javascript:void(0);">Set</a>
                       </xsl:when>
                       <xsl:otherwise><span class="translate">Set</span></xsl:otherwise>
                     </xsl:choose>
                   </div></td>
                   <td style="vertical-align:middle;padding-left:4px;"><span class="translate" style="white-space:nowrap;">log categories for a duration of</span>&#160;&#160;</td>
                   <td>
                     <input type="text" name="delay" value="{/spooler/@reset_delay}" style="text-align:right;width:60px;border:1px solid #8B919F" />
                   </td>
                   <td style="vertical-align:middle;padding-left:4px;"><span class="translate" style="white-space:nowrap;">seconds</span>&#160;</td>
                   <td id="next_reset" class="status_text" style="vertical-align:middle;">&#160;
                     <xsl:choose>
                       <xsl:when test="/spooler/@next_reset = -1">
                         (<span class="translate">log categories are updated</span>)
                       </xsl:when>
                       <xsl:when test="/spooler/@next_reset = 0">
                         (<span class="translate">reset is executed</span>)
                       </xsl:when>
                       <xsl:when test="/spooler/@next_reset &gt; 0">
                         (<span class="translate">reset is executed after</span>&#160;<xsl:value-of select="/spooler/@next_reset"/>&#160;<span class="translate">seconds</span>)
                       </xsl:when>
                     </xsl:choose>
                   </td>
                   <td>&#160;&#160;</td>
                </tr>
                <xsl:if test="@reset_at__xslt_datetime_with_diff">
                  <tr>
                    <td colspan="2" style="line-height:12px;text-align:right;vertical-align:middle;padding:4px 0px;"><span class="translate" style="white-space:nowrap;">Next reset</span>:&#160;</td>
                    <td colspan="4" class="task" style="line-height:12px;vertical-align:middle;"><xsl:apply-templates mode="date_time_nowrap" select="@reset_at__xslt_datetime_with_diff"/></td>
                  </tr>
                </xsl:if>
                <tr>
                   <td colspan="6" class="before_head_space">&#160;</td>
                </tr>
            </thead>
          </table>
        </div>  
        <div class="bottom">
          <table cellpadding="0" cellspacing="0" width="100%" border="0" class="bottom">
        
            <colgroup>
              <col width="*"/>
              <col width="60"/>
              <col width="*"/>
            </colgroup>
            
            <thead>
                <tr>
                  <td class="head1"><span class="translate">Category</span></td>
                  <td class="head"><span class="translate">Mode</span></td>
                  <td class="head"><span class="translate">Description</span></td>
                </tr>
                <xsl:call-template name="after_head_space">
                  <xsl:with-param name="colspan" select="'3'"/>
                </xsl:call-template>
            </thead>
            
            <tbody>
                <xsl:apply-templates select="log_category" >
                  <xsl:sort select="translate( @path, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz' )" order="ascending"/>
                  <xsl:with-param name="sub_category" select="contains(@path, '.')" />
                  <xsl:with-param name="sub_all_category" select="contains(@path, '.*')" />
                </xsl:apply-templates>
            </tbody>
            
            <tfoot>
              <xsl:call-template name="after_body_space">
                  <xsl:with-param name="colspan" select="'3'"/>
              </xsl:call-template>
            </tfoot>
            
          </table>
        
          <xsl:call-template name="round_bottom_corners"/>
        </div>
        </form>
        
    </xsl:template>
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~log_category-->
    <xsl:template match="log_category">
        <xsl:param name="sub_category" select="true()" />
        <xsl:param name="sub_all_category" select="true()" />
        
        <xsl:if test="position() &gt; 1 and not($sub_category)">
           <tr><td colspan="3" class="line">&#160;</td></tr>
        </xsl:if>
        <tr>
          <td>
            <!--xsl:choose>
              <xsl:when test="$sub_all_category">
                <xsl:element name="a">
                  <xsl:attribute name="name"><xsl:value-of select="@path"/></xsl:attribute>
                  <xsl:attribute name="value"><xsl:value-of select="@path"/></xsl:attribute>
                  <xsl:attribute name="type">link</xsl:attribute>
                  <xsl:attribute name="class">link</xsl:attribute>
                  <xsl:attribute name="style">margin-left:24px;</xsl:attribute>
                  <xsl:attribute name="onclick">apply_category(this,document.forms.log_categories);</xsl:attribute>
                  <xsl:if test="/spooler/@my_ie_version &gt; 0">&#160;&#160;</xsl:if>
                  <span style="margin-right:2px">&gt;&gt;</span><xsl:value-of select="substring-before(@path,'.')"/>.all
                </xsl:element>
              </xsl:when>
              <xsl:otherwise-->
                <input type="checkbox" name="{@path}" value="{@path}" onclick="apply_category(this,this.form);">
                  <xsl:if test="@value=1"><xsl:attribute name="checked">true</xsl:attribute></xsl:if>
                  <xsl:if test="$sub_category"><xsl:attribute name="style">margin-left:24px;</xsl:attribute></xsl:if>
                </input>
              <!--/xsl:otherwise>
            </xsl:choose-->
            <xsl:choose>
              <xsl:when test="@default='yes'">
                <span style="font-weight:bold;color:#EA7A14;position:relative;bottom:2px;">&#160;
                <xsl:call-template name="replace">
                    <xsl:with-param name="string" select="@path"/>
                    <xsl:with-param name="old" select="'.*'"/>
                    <xsl:with-param name="new" select="'.all'"/>
                </xsl:call-template>
                </span>
              </xsl:when>
              <!--xsl:when test="$sub_all_category">
              </xsl:when-->
              <xsl:otherwise>
                <span style="font-weight:bold;position:relative;bottom:2px;">&#160;
                <xsl:call-template name="replace">
                    <xsl:with-param name="string" select="@path"/>
                    <xsl:with-param name="old" select="'.*'"/>
                    <xsl:with-param name="new" select="'.all'"/>
                </xsl:call-template>
                </span>
              </xsl:otherwise>
            </xsl:choose>
          </td>
          <td><span class="translate"><xsl:value-of select="@mode"/></span></td>
          <td><xsl:value-of select="@title"/></td> 
        </tr>
        
    </xsl:template>
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~translate_target-->
    <!-- Fuer <a target="..."/> -->
    
    <xsl:template name="translate_target">
        <xsl:param name="target"/>
        <xsl:value-of select="translate( $target, ':/.-', '____' )" />
    </xsl:template>
                                    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~command_menu-->

    <xsl:template name="command_menu">
        <xsl:param name="title" select="'Menu'"/>
        <xsl:param name="style" select="''"/>
        <xsl:param name="onclick"/>
        <xsl:param name="onclick_call"/>
        <xsl:param name="onclick_param1"/>
        <xsl:param name="onclick_param1_str"/>
        <xsl:param name="onclick_param2"/>
        <xsl:param name="onclick_param2_str"/>
        <xsl:param name="onclick_param3"/>
        <xsl:param name="onclick_param3_str"/>
        <xsl:param name="onclick_param4"/>
        <xsl:param name="onclick_param4_str"/>
        <xsl:param name="onclick_param5"/>
        <xsl:param name="onclick_param5_str"/>
        <xsl:param name="onclick_param6"/>
        <xsl:param name="onclick_param6_str"/>
        <xsl:param name="onclick_param7"/>
        <xsl:param name="onclick_param7_str"/>

        <div class="button" style="{$style}">
            <xsl:attribute name="onclick">
                <xsl:choose>
                    <xsl:when test="$onclick">
                        <xsl:value-of select="$onclick"/>
                    </xsl:when>

                    <xsl:otherwise>
                        <xsl:value-of select="$onclick_call"/>
                        <xsl:text>( </xsl:text>

                        <xsl:call-template name="command_menu_param">
                            <xsl:with-param name="first"     select="true()"/>
                            <xsl:with-param name="param"     select="$onclick_param1"/>
                            <xsl:with-param name="param_str" select="$onclick_param1_str"/>
                        </xsl:call-template>

                        <xsl:call-template name="command_menu_param">
                            <xsl:with-param name="param"     select="$onclick_param2"/>
                            <xsl:with-param name="param_str" select="$onclick_param2_str"/>
                        </xsl:call-template>

                        <xsl:call-template name="command_menu_param">
                            <xsl:with-param name="param"     select="$onclick_param3"/>
                            <xsl:with-param name="param_str" select="$onclick_param3_str"/>
                        </xsl:call-template>

                        <xsl:call-template name="command_menu_param">
                            <xsl:with-param name="param"     select="$onclick_param4"/>
                            <xsl:with-param name="param_str" select="$onclick_param4_str"/>
                        </xsl:call-template>

                        <xsl:call-template name="command_menu_param">
                            <xsl:with-param name="param"     select="$onclick_param5"/>
                            <xsl:with-param name="param_str" select="$onclick_param5_str"/>
                        </xsl:call-template>
                        
                        <xsl:call-template name="command_menu_param">
                            <xsl:with-param name="param"     select="$onclick_param6"/>
                            <xsl:with-param name="param_str" select="$onclick_param6_str"/>
                        </xsl:call-template>
                        
                        <xsl:call-template name="command_menu_param">
                            <xsl:with-param name="param"     select="$onclick_param7"/>
                            <xsl:with-param name="param_str" select="$onclick_param7_str"/>
                        </xsl:call-template>


                        <xsl:text> )</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:choose>
              <xsl:when test="/spooler/@my_ie_version &gt; 0 and /spooler/@my_ie_version &lt; 8">
                <a class="translate" href="javascript:void(0);"><xsl:value-of select="$title"/></a>
              </xsl:when>
              <xsl:otherwise>
                <span class="translate"><xsl:value-of select="$title"/></span>
              </xsl:otherwise>
            </xsl:choose>
        </div>
    </xsl:template>

    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~command_menu_param-->

    <xsl:template name="command_menu_param">
        <xsl:param name="param"/>
        <xsl:param name="param_str"/>
        <xsl:param name="first" select="false()"/>

        <xsl:if test="not( $first ) and ( $param or $param_str )">
            <xsl:text>,</xsl:text>
        </xsl:if>
        
        <xsl:choose>
            <xsl:when test="$param">
                <xsl:value-of select="$param"/>
            </xsl:when>
            <xsl:when test="$param_str">
                <xsl:text>"</xsl:text>
                <!-- Anfuehrungszeichen in $param_str werden nicht ersetzt -->
                <xsl:call-template name="replace">
                    <xsl:with-param name="string" select="$param_str"/>
                    <xsl:with-param name="old"    select="'\'"/>
                    <xsl:with-param name="new"    select="'\\'"/>
                </xsl:call-template>
                <xsl:text>"</xsl:text>
            </xsl:when>
        </xsl:choose>

    </xsl:template>
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~file_order_source-->

    <xsl:template match="file_order_source">
       
       <nobr><span class="label">directory</span><span class="small">:</span> <xsl:value-of select="@directory"/></nobr>
       <nobr><span class="label" style="padding-left:2px;">pattern</span><span class="small">:</span> <xsl:value-of select="@regex"/></nobr>
       <nobr><span class="label" style="padding-left:2px;">delay</span><span class="small">:</span> <xsl:value-of select="@delay_after_error"/></nobr>
       <nobr><span class="label" style="padding-left:2px;">repeat</span><span class="small">:</span> <xsl:value-of select="@repeat"/></nobr>
       <xsl:if test="not(position()=last())"><br/></xsl:if>
       
    </xsl:template>
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~replace-->

    <xsl:template name="replace">
        <xsl:param name="string"/>
        <xsl:param name="old"/>
        <xsl:param name="new"/>
        <xsl:param name="wbr" select="false()"/>
        
        <xsl:choose>
            <xsl:when test="contains( $string, $old )">
                <xsl:if test="not($wbr)">
                    <xsl:value-of select="concat( substring-before( $string, $old ), $new )"/>
                </xsl:if>
                <xsl:if test="$wbr">  
                    <xsl:value-of select="concat( substring-before( $string, $old ), $old )"/>
                    <xsl:call-template name="wbr"/>
                </xsl:if>  
                <xsl:call-template name="replace">
                    <xsl:with-param name="string" select="substring-after( $string, $old)"/>
                    <xsl:with-param name="old" select="$old"/>
                    <xsl:with-param name="new" select="$new"/>
                    <xsl:with-param name="wbr" select="$wbr"/>
                </xsl:call-template>
            </xsl:when>
            
            <xsl:otherwise>
                <xsl:value-of select="$string"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~job_path-->
    
    <xsl:template mode="job_path" match="job">
        
        <xsl:param name="style" select="'font-weight:bold;'" />
        <xsl:param name="order_job" select="false()"/>
        
        <xsl:variable name="class">
            <xsl:choose>
                <xsl:when test="@enabled='no'">gray</xsl:when>
                <xsl:when test="@remove='yes'">red</xsl:when>
                <xsl:when test="@state='not_initialized' or @state='stopped' or @state='read_error' or @state='error'">red</xsl:when>
                <xsl:when test="lock.requestor/lock.use/@is_missing='yes'">red</xsl:when>
                <xsl:when test="file_based/requisites/requisite/@is_missing ='yes'">red</xsl:when>
                <xsl:when test="$order_job and (not(@order) or @order='no')">red</xsl:when>
                <!--xsl:when test="$job/@state='pending' or $job/@state='running'"></xsl:when-->
            </xsl:choose>
        </xsl:variable>
        <span class="{$class}" style="{$style}"><xsl:apply-templates mode="trim_slash" select="@path" /></span>
        
    </xsl:template>
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~job_state_line-->

    <xsl:template mode="job_state_line" match="job">

        <xsl:apply-templates select="@state"/>

        <xsl:if test="@waiting_for_process='yes'">
            <xsl:text>, </xsl:text>
            <span class="translate" style="color: crimson; font-weight: bold">needs process</span>
        </xsl:if>

        <xsl:if test="lock.requestor">
            <xsl:text>, </xsl:text>

            <xsl:choose>
                <xsl:when test="lock.requestor[ @enqueued='yes' ]">
                    <span class="translate" style="color: crimson; font-weight: bold;">needs lock</span><xsl:text> </xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <span class="translate">Lock</span><xsl:text> </xsl:text>
                </xsl:otherwise>
            </xsl:choose>

            <xsl:if test="lock.requestor/lock.use">
                <xsl:for-each select="lock.requestor/lock.use">
                    <xsl:if test="position() &gt; 1">
                        <xsl:text>, </xsl:text>
                    </xsl:if>

                    <xsl:apply-templates select="." mode="short"/>
                </xsl:for-each>
            </xsl:if>
        </xsl:if>

        <xsl:if test="replacement">
            <xsl:choose>
                <xsl:when test="replacement/job/file_based/ERROR">
                    <xsl:text>, </xsl:text>
                    <span class="file_based_error">changed file has error</span>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>, </xsl:text><span class="translate">is being replaced</span>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    </xsl:template>
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~lock_path-->
    
    <xsl:template mode="lock_path" match="lock">
        
        <xsl:param name="style" select="'font-weight:bold;'" />
        <xsl:variable name="class">
            <xsl:choose>
                <xsl:when test="lock.holders/lock.holder">red</xsl:when>
                <xsl:otherwise></xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <span class="{$class}" style="{$style}"><xsl:apply-templates mode="trim_slash" select="@path" /></span>
        
    </xsl:template>
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~@title-->
    
    <xsl:template match="@title">
        
        <i>
          <xsl:call-template name="show_text_with_url">
             <xsl:with-param name="text" select="."/>
          </xsl:call-template>
        </i>
        
    </xsl:template> 
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~show_text_with_url-->
    <!-- Wickelt http://… in <a href="http://…">http://…</a> ein -->

    <xsl:template name="show_text_with_url">

        <xsl:param name="text"/>

        <xsl:choose>
            <xsl:when test="contains( $text, '&lt;a ' )">
                <xsl:call-template name="show_text_with_url">
                    <xsl:with-param name="text" select="substring-before( $text, '&lt;a ' )"/>
                </xsl:call-template>
                <xsl:call-template name="show_text_with_url.url_tag_end">
                		<xsl:with-param name="tail" select="substring-after( $text, '&lt;a ' )"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="contains( $text, 'http://' )">
                <xsl:value-of select="substring-before( $text, 'http://' )"/>
                <xsl:call-template name="show_text_with_url.url_end">
                		<xsl:with-param name="tail" select="substring-after( $text, 'http://' )"/>
                		<xsl:with-param name="protocol" select="'http://'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="contains( $text, 'https://' )">
                <xsl:value-of select="substring-before( $text, 'https://' )"/>
                <xsl:call-template name="show_text_with_url.url_end">
                		<xsl:with-param name="tail" select="substring-after( $text, 'https://' )"/> 
                		<xsl:with-param name="protocol" select="'https://'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="contains( $text, 'www.' )">
                <xsl:value-of select="substring-before( $text, 'www.' )"/>
                <xsl:call-template name="show_text_with_url.url_end">
                		<xsl:with-param name="tail" select="substring-after( $text, 'www.' )"/> 
                		<xsl:with-param name="protocol" select="'www.'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$text"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~show_text_with_url.url_end-->
 
    <xsl:template name="show_text_with_url.url_end">
        <xsl:param name="tail"/>
        <xsl:param name="protocol"/>
        
        <xsl:choose>
            <xsl:when test="contains( $tail, ' ' )">
                <xsl:call-template name="show_text_with_url.url_with_punctuation">
                    <xsl:with-param name="url" select="concat( $protocol, substring-before( $tail, ' ' ) )"/>
                </xsl:call-template>

                <xsl:text> </xsl:text>

                <xsl:call-template name="show_text_with_url">
                    <xsl:with-param name="text" select="substring-after( $tail, ' ' )"/>
                </xsl:call-template>
            </xsl:when>

            <xsl:otherwise>
                <xsl:call-template name="show_text_with_url.url_with_punctuation">
                    <xsl:with-param name="url" select="concat( $protocol, $tail )"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~show_text_with_url.url_tag_end-->
 
    <xsl:template name="show_text_with_url.url_tag_end">
        <xsl:param name="tail"/>
        
        <xsl:choose>
            <xsl:when test="contains( $tail, '&lt;/a&gt;' )">
                <xsl:variable name="before_end_tag" select="substring-before( $tail, '&lt;/a&gt;' )" />
                <xsl:choose>
                   <xsl:when test="contains( $before_end_tag, 'href=&quot;' )">
                       <xsl:variable name="after_href" select="substring-after( $before_end_tag, 'href=&quot;' )" />
                       <xsl:choose>
                         <xsl:when test="contains( $after_href, '&quot;' )">
                           <xsl:variable name="url" select="substring-before( $after_href, '&quot;' )" />
                           <xsl:variable name="after_url" select="substring-after( $after_href, '&quot;' )" />
                           <xsl:choose>
                             <xsl:when test="contains( $after_url, '&gt;' )">
                               <xsl:call-template name="show_text_with_url.url">
                                 <xsl:with-param name="url" select="$url"/>
                                 <xsl:with-param name="link_text" select="substring-after( $after_url, '&gt;' )"/>
                               </xsl:call-template>
                               <xsl:call-template name="show_text_with_url">
                                 <xsl:with-param name="text" select="substring-after( $tail, '&lt;/a&gt;' )"/>
                               </xsl:call-template>
                             </xsl:when>
                             <xsl:otherwise>
                               <xsl:value-of select="concat( '&lt;a ', $tail )"/>
                             </xsl:otherwise>
                           </xsl:choose>
                         </xsl:when>
                         <xsl:otherwise>
                           <xsl:value-of select="concat( '&lt;a ', $tail )"/>
                         </xsl:otherwise>
                       </xsl:choose>
                   </xsl:when>
                   <xsl:otherwise>
                     <xsl:value-of select="concat( '&lt;a ', $tail )"/>
                   </xsl:otherwise>
                </xsl:choose>
            </xsl:when>

            <xsl:otherwise>
                <xsl:value-of select="concat( '&lt;a ', $tail )"/>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>

    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~show_text_with_url.url_with_punctuation-->
 
    <xsl:template name="show_text_with_url.url_with_punctuation">
        <xsl:param name="url"/>
        
        <xsl:choose>
            <xsl:when test="not( contains( 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789/', substring( $url, string-length( $url ), 1 ) ) )">
                <xsl:call-template name="show_text_with_url.url_with_punctuation">
                    <xsl:with-param name="url" select="substring( $url, 1, string-length( $url ) - 1 )"/>
                </xsl:call-template>
                <xsl:value-of select="substring( $url, string-length( $url ), 1 )"/>
            </xsl:when>
            
            <xsl:otherwise>
                <xsl:call-template name="show_text_with_url.url">
                    <xsl:with-param name="url" select="$url"/>
                    <xsl:with-param name="link_text" select="$url"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>

    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~show_text_with_url.url-->
    
    <xsl:template name="show_text_with_url.url">
        <xsl:param name="url"/>
        <xsl:param name="link_text"/>
        
        <xsl:element name="a">
            <xsl:attribute name="target">_blank</xsl:attribute>
            <xsl:attribute name="href">
                <xsl:value-of select="$url"/>
            </xsl:attribute>

            <xsl:value-of select="$link_text"/>
        </xsl:element>

    </xsl:template>
    
    
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~file_timestamp-->
    
    <xsl:template mode="file_timestamp" match="file_based/@last_write_time__xslt_datetime_zone_support">
       <xsl:param name="colspan" select="'1'"/>
       <tr>
           <td valign="top" class="label"><span class="label">File timestamp</span>:</td>
           <td colspan="{$colspan}" valign="top"><span class="small"><xsl:value-of select="." disable-output-escaping="yes"/></span></td>
       </tr>
    </xsl:template>
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~file_based_line-->
    
    <xsl:template mode="file_based_line" match="*">
        <xsl:choose>
            <xsl:when test="ERROR">
                <span class="label">Error</span>:
                <span class="job_error"><xsl:apply-templates select="ERROR"/></span>
            </xsl:when>
            <xsl:when test="file_based/ERROR">
                <span class="label">Error in configuration file</span>:
                <xsl:apply-templates select="file_based" mode="file_based_error"/>
            </xsl:when>
            <xsl:when test="replacement/*/file_based/ERROR">
                <span class="label">Error in changed file</span><xsl:text> </xsl:text>
                <span class="label">(not loaded)</span>:
                <xsl:apply-templates select="replacement/*/file_based" mode="file_based_error"/>
            </xsl:when>
            <xsl:when test="replacement">
                <span class="label">Replacement is standing by</span>
            </xsl:when>
            <xsl:when test="file_based/removed/ERROR">
                <span class="label">Removing delayed</span>:
                <xsl:apply-templates select="file_based/removed" mode="file_based_error"/>
            </xsl:when>
            <xsl:when test="file_based/requisites/requisite/@is_missing='yes' or lock.requestor/lock.use/@is_missing='yes'">
                <xsl:apply-templates select="file_based/requisites/requisite[@is_missing='yes'] | lock.requestor/lock.use[@is_missing='yes']" mode="file_based_error"/>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~file_based/ERROR-->

    <xsl:template mode="file_based_error" match="file_based">
        <span class="file_based_error">
            <xsl:apply-templates select="ERROR"/>
        </span>
    </xsl:template>
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~file_based/requisites/requisite[@is_missing='yes']-->

    <xsl:template mode="file_based_error" match="requisite">
        <span class="file_based_error">
            <xsl:if test="position() &gt; 1"><xsl:text>, </xsl:text></xsl:if>
            <xsl:value-of select="translate(@type,'_JLOPS',' jlops')"/>
            <xsl:text> (</xsl:text>
            <xsl:value-of select="@path"/>
            <xsl:text>) </xsl:text>
            <span class="translate">is missing</span>
        </span>
    </xsl:template>
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~lock.requestor/lock.use[@is_missing='yes']-->

    <xsl:template mode="file_based_error" match="lock.use">
        <span class="file_based_error">
            <xsl:if test="position() &gt; 1"><xsl:text>, </xsl:text></xsl:if>
            <xsl:text>lock (</xsl:text>
            <xsl:value-of select="@lock"/>
            <xsl:text>) </xsl:text>
            <span class="translate">is missing</span>
        </span>
    </xsl:template>
    
    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~date_time_nowrap-->

    <xsl:template mode="date_time_nowrap" match="@*">
      <xsl:choose>
        <xsl:when test="contains(.,' ')">
          <span class="small" style="padding-right:2px;white-space:nowrap;">
            <xsl:value-of select="substring-before(.,' ')" disable-output-escaping="yes"/>
          </span>
          <xsl:text> </xsl:text>
          <span class="small" style="padding-right:2px;white-space:nowrap;">
            <xsl:value-of select="substring-after(.,' ')" disable-output-escaping="yes"/>
          </span>
        </xsl:when>
        <xsl:otherwise>
          <span class="small" style="padding-right:2px;white-space:nowrap;">
            <xsl:value-of select="." disable-output-escaping="yes"/>
          </span>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:template>
    
    
    <xsl:template mode="only_trim_slash" match="@*">
        <xsl:choose>
          <xsl:when test="starts-with(.,'/')">
             <xsl:value-of select="substring-after(.,'/')"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="."/>
          </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    
    <xsl:template mode="trim_slash" match="@*">
        <xsl:choose>
          <xsl:when test="starts-with(.,'/')">
            <xsl:call-template name="normalize_path">
              <xsl:with-param name="text" select="substring-after(.,'/')" />
            </xsl:call-template>
          </xsl:when>
          <xsl:otherwise>
            <xsl:choose>
              <xsl:when test="contains(.,'\')">
                <xsl:call-template name="normalize_path">
                  <xsl:with-param name="text" select="." />
                  <xsl:with-param name="delim" select="'\'" />
                </xsl:call-template>
              </xsl:when>    
              <xsl:otherwise>
                <xsl:call-template name="normalize_path">
                  <xsl:with-param name="text" select="." />
                </xsl:call-template>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    
    <xsl:template name="wbr">
       <xsl:choose>
            <xsl:when test="/spooler/@my_ie_version &lt; 8"><wbr/></xsl:when>
            <xsl:otherwise>&#8203;</xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    
    <xsl:template mode="normalized_order_id"  match="@*">
       <xsl:call-template name="replace">
         <xsl:with-param name="string" select="."/>
         <xsl:with-param name="old"    select="'\'"/>
         <xsl:with-param name="new"    select="'\\'"/>
       </xsl:call-template>
    </xsl:template>
    
    
    <xsl:template name="normalize_path">
        <xsl:param name="text"/>
        <xsl:param name="delim" select="/"/>
        
        <xsl:choose>
            <xsl:when test="/spooler/@my_ie_version &lt; 8">
                <xsl:choose>
                  <xsl:when test="contains($text,'_')">
                    <xsl:call-template name="replace">
                      <xsl:with-param name="string" select="concat(substring-before($text,'_'),'_')"/>
                      <xsl:with-param name="old"    select="$delim"/>
                      <xsl:with-param name="wbr"    select="true()"/>
                    </xsl:call-template>
                    <wbr/>
                    <xsl:call-template name="normalize_path">
                      <xsl:with-param name="text" select="substring-after($text,'_')" />
                    </xsl:call-template>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:call-template name="replace">
                      <xsl:with-param name="string" select="$text"/>
                      <xsl:with-param name="old"    select="$delim"/>
                      <xsl:with-param name="wbr"    select="true()"/>
                    </xsl:call-template>
                  </xsl:otherwise>
                </xsl:choose>     
            </xsl:when>
            <xsl:otherwise>
                <xsl:choose>
                  <xsl:when test="contains($text,'_')">
                    <xsl:call-template name="replace">
                      <xsl:with-param name="string" select="concat(substring-before($text,'_'),'_&#8203;')"/>
                      <xsl:with-param name="old"    select="$delim"/>
                      <xsl:with-param name="new"    select="concat($delim,'&#8203;')"/>
                    </xsl:call-template>
                    <xsl:call-template name="normalize_path">
                      <xsl:with-param name="text" select="substring-after($text,'_')" />
                    </xsl:call-template>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:call-template name="replace">
                      <xsl:with-param name="string" select="$text"/>
                      <xsl:with-param name="old"    select="$delim"/>
                      <xsl:with-param name="new"    select="concat($delim,'&#8203;')"/>
                    </xsl:call-template>
                  </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    
    <xsl:template name="after_head_space">
        <xsl:param name="colspan" select="'1'"/>
        <tr><td class="after_head_space1" colspan="{$colspan}"></td></tr>
        <tr><td class="after_head_space2" colspan="{$colspan}"></td></tr>
        <tr><td class="after_head_space3" colspan="{$colspan}"></td></tr>
        <tr><td class="after_head_space4" colspan="{$colspan}"></td></tr>
        <tr><td class="after_head_space5" colspan="{$colspan}"></td></tr>
        <tr><td class="after_head_space6" colspan="{$colspan}"></td></tr>
        <tr><td class="after_head_space7" colspan="{$colspan}"></td></tr>
    </xsl:template>
    
    <xsl:template name="after_body_space">
        <xsl:param name="colspan" select="'1'"/>
        <tr><td class="after_head_space7" colspan="{$colspan}"></td></tr>
        <tr><td class="after_head_space6" colspan="{$colspan}"></td></tr>
        <tr><td class="after_head_space5" colspan="{$colspan}"></td></tr>
        <tr><td class="after_head_space4" colspan="{$colspan}"></td></tr>
        <tr><td class="after_head_space3" colspan="{$colspan}"></td></tr>
        <tr><td class="after_head_space2" colspan="{$colspan}"></td></tr>
        <tr><td class="after_head_space1" colspan="{$colspan}"></td></tr>
    </xsl:template> 
    
    <xsl:template name="round_top_corners">
       <img class="corner_tl" src="corner_tl.gif" alt="" />
       <img class="corner_tr" src="corner_tr.gif" alt="" />
    </xsl:template>
    
    <xsl:template name="round_bottom_corners">
      <img class="corner_bl" src="corner_bl.gif" alt="" />
      <img class="corner_br" src="corner_br.gif" alt="" />
    </xsl:template> 
    
</xsl:stylesheet>