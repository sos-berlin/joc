// $Id$
//Control flag (if false then default language is used)
_lang_file_exists = true;

/******************************************************************************
*                            SCHEDULER (top_frame)                            *
******************************************************************************/

//Window title 
_translations['JobScheduler']                              = 'JobScheduler';
_translations['-id']                                       = '-ID';

//Product info
_translations['Operations GUI']                            = 'Operations GUI';
_translations['Build']                                     = 'Build';
_translations['All rights reserved.']                      = 'Alle Rechte vorbehalten.'
_translations['OK']                                        = 'OK'

//Tabs
_translations['Jobs']                                      = 'Jobs';
_translations['Job Chains']                                = 'Job-Ketten';
_translations['Orders']                                    = 'Aufträge';
_translations['Schedules']                                 = 'Schedules';
_translations['Process Classes']                           = 'Prozess-Klassen';
_translations['Locks']                                     = 'Sperren';
_translations['Cluster']                                   = 'Cluster';
_translations['Remote Schedulers']                         = 'Remote Scheduler';
_translations['Last Activities']                           = 'Letzte Aktivitäten';
                                                           
//Buttons menu, extras, update, hide          
_translations['Menu']                                      = 'Menü';
_translations['Extras']                                    = 'Extras';
_translations['Help']                                      = 'Hilfe';
_translations['Hide']                                      = 'Details schließen';
_translations['Update']                                    = 'Aktualisieren';

//Menu content of scheduler menu
_translations['Show log']                                  = 'Protokoll';
_translations['Show job dependencies']                     = 'Job-Abhängigkeiten';
_translations['Show job chain dependencies']               = 'Job-Ketten-Abhängigkeiten';
_translations['Show start times']                          = 'Start-Intervalle';
_translations['Manage filters']                            = 'Filterverwaltung';
_translations['Manage log categories']                     = 'Protokoll-Kategorien';
_translations['Pause']                                     = 'Anhalten';
_translations['Continue']                                  = 'Fortsetzen';
_translations['Terminate']                                 = 'Beenden';
_translations['Terminate within ~$secs']                   = 'Binnen ~$secs beenden';
_translations['Terminate and restart']                     = 'Beenden mit Neustart';
_translations['Terminate and restart within ~$secs']       = 'Binnen ~$secs beenden mit Neustart';
_translations['Abort immediately']                         = 'Sofort abbrechen';
_translations['Abort immediately and restart']             = 'Sofort abbrechen mit Neustart';
_translations['Terminate cluster']                         = 'Cluster beenden';
_translations['Terminate cluster within ~$secs']           = 'Binnen ~$secs Cluster beenden';
_translations['Terminate and restart cluster']             = 'Cluster mit Neustart beenden';
_translations['Terminate and restart cluster within ~$secs'] = 'Binnen ~$secs Cluster mit Neustart beenden';
_translations['Terminate fail-safe']                       = 'Ausfallsicher beenden';
_translations['Terminate fail-safe within ~$secs']         = 'Binnen ~$secs ausfallsicher beenden';
                                                           
//Menu content of extras                      
_translations['Documentation']                             = 'Dokumentation';
_translations['Forum']                                     = 'Forum';
_translations['Downloads']                                 = 'Downloads';
_translations['Follow us on Twitter']                      = 'Folgen Sie uns auf Twitter';
_translations['About']                                     = 'Über';
_translations['Settings']                                  = 'Einstellungen';
_translations['Monitor']                                   = 'Monitor';
_translations['Configuration']                             = 'Konfiguration';
                                                           
//First line                                  
_translations['every $secs']                               = 'alle $secs';
_translations['Time zone']                                 = 'Zeitzone';
_translations['local']                                     = 'lokal';

                                                      
//Other lines                            
_translations['ID']                                        = 'ID';
_translations['State']                                     = 'Status';
_translations['Pid']                                       = 'Pid';
_translations['Time']                                      = 'Zeit';
_translations['jobs']                                      = 'Jobs';
_translations['need process']                              = 'warten auf Prozess';
_translations['tasks']                                     = 'Tasks';
_translations['orders']                                    = 'Aufträge';
_translations['$cnt in cluster']                           = '$cnt in Cluster';
_translations['Start Time']                                = 'Startzeit';

//Job Scheduler states
_translations['starting']                                  = 'startet';
_translations['running']                                   = 'gestartet';
_translations['paused']                                    = 'angehalten';
_translations['stopping']                                  = 'wird gestoppt';
_translations['stopping_let_run']                          = 'wird gestoppt';
_translations['stopped']                                   = 'gestoppt';
_translations['waiting_for_activation']                    = 'wartet auf Aktivierung';

//Misc.
_translations['unknown']                                   = 'unbekannt';
_translations['THIS IS A BACKUP...']                       = 'DAS IST EIN BACKUP...';
_translations['Backup JobScheduler:']                      = 'Backup JobScheduler:';
_translations['JobScheduler is waiting for database ...']  = 'JobScheduler wartet auf Datenbank ...';
_translations['JobScheduler is waiting for activation.']   = 'JobScheduler wartet auf Aktivierung.';
_translations['Waiting for response from JobScheduler ...'] = 'Auf Antwort des JobScheduler warten ...';
_translations['No connection to JobScheduler']             = 'Keine Verbindung zum JobScheduler';
_translations['Error at XML answer:']                      = 'Fehler in XML-Antwort:';
_translations["Error at XSL answer '$xsl':"]               = "Fehler in XSL-Antwort '$xsl':";
_translations["Error at HTTP answer '$url':"]              = "Fehler in HTTP-Antwort '$url':";
_translations['$trial trial (of 5) to (re)connect to JobScheduler'] = '$trial Verbindungsversuch (von 5) mit dem JobScheduler';
_translations['First']                                     = 'Erster';
_translations['Second']                                    = 'Zweiter';
_translations['Third']                                     = 'Dritter';
_translations['Fourth']                                    = 'Vierter';
_translations['Last']                                      = 'Letzter';
_translations['never']                                     = 'nie';
_translations['now']                                       = 'jetzt';
_translations['days']                                      = 'Tage';
_translations['The settings dialog is not available,\nbecause $file is used as settings file.'] = 'Der Dialog \'Einstellungen\' steht nicht zur Verfügung,\nda die Datei $file für die Einstellungen benutzt wird.'; 
_translations['You can enable the settings dialog \nvia the _disable_cookie_settings flag in the settings file'] = 'Sie können den Dialog durch den _disable_cookie_settings \nSchalter in der obigen Datei aktivieren.';

//compact
_translations['from']                                      = 'von';
_translations['update']                                    = 'Aktualisieren';
_translations['last update']                               = 'letzte Aktualisierung';
_translations['Scheduler is running since']                = 'Startzeit';


/******************************************************************************
*                                  JOBS/TASKS                                 *
******************************************************************************/
//some labels are already translated above
//Label of Checkboxes
_translations['Show tasks']                                = 'Tasks anzeigen';

//Label and Options of Selectbox
_translations['All jobs']                                  = 'Alle Jobs';
_translations['Standalone jobs']                           = 'Standalone-Jobs';
_translations['Order jobs']                                = 'Auftrags-Jobs';
_translations['with state']                                = 'im Status';
_translations['(all)']                                     = '(alle)';
_translations['(other)']                                   = '(andere)';
_translations['running or stopped']                        = 'gestartet oder gestoppt';
_translations['and with process class']                    = 'und der Prozess-Klasse';
                                                           
//Label and Options in Selectbox of jobs sort                        
_translations['Sort jobs by']                              = 'Jobs sortieren nach';
_translations['(unsorted)']                                = '(unsortiert)';
_translations['name in ascending order']                   = 'Name (aufsteigend)';
_translations['name in descending order']                  = 'Name (absteigend)';
_translations['next start time in ascending order']        = 'Nächste Startzeit (aufsteigend)';
_translations['next start time in descending order']       = 'Nächste Startzeit (absteigend)';
_translations['state in ascending order']                  = 'Status (aufsteigend)';
_translations['state in descending order']                 = 'Status (absteigend)';
                                                           
//Title of images
_translations['toggle to']                                 = 'Wechseln zur'; 
_translations['list view']                                 = 'Listen-Ansicht'; 
_translations['tree view']                                 = 'Baum-Ansicht'; 
_translations['open all folders']                          = 'Alle Ordner öffnen';   
_translations['close all folders']                         = 'Alle Ordner schließen';   

//Label and Options of Selectbox of jobs filtering                     
_translations['Filter jobs by']                            = 'Jobs filtern durch';
_translations['(no filtering)']                            = '(ohne Filter)';
                                                           
//Table header of jobs list                                
_translations['Job']                                       = 'Job';
_translations['Steps']                                     = 'Schritte';
_translations['Process steps']                             = 'Prozess-Schritte';
_translations['Next start']                                = 'Nächster Start';
                                                           
//Labels in detail frame                                   
_translations['JOB']                                       = 'JOB';
_translations['TASK']                                      = 'TASK';
_translations['File timestamp']                            = 'Datei-Datum';
_translations['State text']                                = 'Status-Text';
_translations['Error']                                     = 'Fehler';
_translations['Error in configuration file']               = 'Fehler in Konfigurations-Datei';
_translations['Error in changed file']                     = 'Fehler in geänderter Datei';
_translations['(not loaded)']                              = '(nicht geladen)';
_translations['Removing delayed']                          = 'Löschen verzögert';
_translations['Tasks']                                     = 'Tasks';
_translations['Run time defined by']                       = 'Startzeiten gesetzt in';
_translations['orders to process']                         = 'Aufträge in Warteschlange';
_translations['Mail subject']                              = 'Email-Betreff';
_translations['To']                                        = 'An';
_translations['CC']                                        = 'CC';
_translations['BCC']                                       = 'BCC';
_translations['From']                                      = 'Von';
_translations['SMTP']                                      = 'SMTP';
_translations['Last error']                                = 'Letzter Fehler';
_translations['Last warning']                              = 'Letzte Warnung';
_translations['Log level']                                 = 'Log-Level';
_translations['mail on error']                             = 'Email im Fehlerfall';
_translations['mail on warning']                           = 'Email bei Warnungen';
_translations['mail on success']                           = 'Email im Erfolgsfall';
_translations['mail on process']                           = 'Email pro Prozess-Schritt';
_translations['start cause']                               = 'Grund';
_translations['waiting for order']                         = 'wartet auf Auftrag';
_translations['Idle since']                                = 'Im Leerlauf seit';
_translations['In process since']                          = 'In Verarbeitung seit';
_translations['Running since']                             = 'Gestartet seit';
_translations['Enqueued at']                               = 'In Warteschlange seit';
_translations['Subprocesses']                              = 'Sub-Prozesse';
                                                           
//Task queue                                               
_translations['enqueued tasks']                            = 'Tasks in der Warteschlange';
_translations['Id']                                        = 'ID';
_translations['Enqueued']                                  = 'Eingereiht';
_translations['Start at']                                  = 'Gestartet';
_translations['Delete']                                    = 'Löschen';
                                                           
                                                           
//Task history                                             
_translations['No tasks in the history']                   = 'Es sind keine Tasks in der Historie vorhanden';
_translations['Started']                                   = 'Gestartet';
_translations['Started by']                                = 'Gestartet von';
_translations['Cause']                                     = 'Grund';
_translations['Ended']                                     = 'Beendet';
_translations['Duration']                                  = 'Dauer';
                                                           
//Order queue                                              
_translations['Start']                                     = 'Gestartet';
                                                           
                                                           
//Title of Tabs                                            
_translations['Task Queue']                                = 'Task-Warteschlange';
_translations['Task History']                              = 'Task-Historie';
_translations['Order Queue']                               = 'Auftrags-Warteschlange';
                                                           
                                                           
//Menu content of job menu                                 
_translations['Job menu']                                  = 'Job-Menü';
_translations['Show configuration']                        = 'Konfiguration anzeigen';
_translations['Show description']                          = 'Beschreibung';
_translations['Show documentation']                        = 'Dokumentation';
_translations['Show dependencies']                         = 'Abhängigkeiten';
_translations['Start task immediately']                    = 'Starte Task sofort';
_translations['Start task unforced now']                   = 'Starte Task in nächster Periode';
_translations['Start task at']                             = 'Starte Task um...';
_translations['Start task parametrized']                   = 'Starte Task parametrisiert';
_translations['Set run time']                              = 'Setze Startzeiten';
_translations['Stop']                                      = 'Stoppen';
_translations['Unstop']                                    = 'Fortsetzen';
_translations['Reread']                                    = 'Skript neu laden';
_translations['End tasks']                                 = 'Tasks beenden';
_translations['Suspend tasks']                             = 'Tasks suspendieren';
_translations['Continue tasks']                            = 'Tasks fortsetzen';
_translations['Delete job']                                = 'Job löschen';
                                                           
                                                           
//Menu content of task menu                                
_translations['Task menu']                                 = 'Task-Menü';
_translations['End (API job)']                             = 'Beenden (API job)';
_translations['Kill immediately']                          = 'Sofort abbrechen';
_translations['Terminate (UNIX)']                          = 'Beenden (UNIX)';
_translations['Terminate with timeout (UNIX)']             = 'Beenden mit Timeout (UNIX)';
                                                           
                                                           
//Mouseover-Title                                          
_translations['show job details']                          = 'zeige Job-Details';
_translations['show task details']                         = 'zeige Task-Details';
_translations['show schedule details']                     = 'zeige Schedule-Details';
_translations['Order is deleted']                          = 'Auftrag ist gelöscht';
_translations['Order is replaced']                         = 'Auftrag ist ersetzt';
                                                           
//misc.                          
_translations['Without start time']                        = 'ohne Startzeit';
_translations['Task']                                      = 'Task';
_translations['No jobs found']                             = 'Es wurden keine Jobs gefunden';
                                                           
                                                           
//Job/Task states                                          
_translations['pending']                                   = 'bereit';
_translations['loaded']                                    = 'geladen';
_translations['initialized']                               = 'initialisiert';
_translations['not_initialized']                           = 'nicht initialisiert';
_translations['disabled']                                  = 'deaktiviert';
_translations['none']                                      = 'unbekannt';
_translations['read_error']                                = 'Skript nicht lesbar';
_translations['delayed after error']                       = 'verzögert nach Fehler';
_translations['needs process']                             = 'wartet auf Prozess';
_translations['needs lock']                                = 'gesperrt';
_translations['loading']                                   = 'wird geladen';
_translations['waiting_for_process']                       = 'wartet auf Prozess';
_translations['opening']                                   = 'gestartet';
_translations['opening_waiting_for_locks']                 = 'gesperrt';
_translations['running_process']                           = 'gestartet';
_translations['running_remote_process']                    = 'gestartet';
_translations['running_waiting_for_order']                 = 'wartet auf Auftrag';
_translations['running_waiting_for_locks']                 = 'gesperrt';
_translations['running_delayed']                           = 'wird verzögert gestartet';
_translations['suspended']                                 = 'angehalten';
_translations['ending_waiting_for_subprocesses']           = 'wartet auf Sub-Prozesse';
_translations['ending']                                    = 'endend';
_translations['on_success']                                = 'endend';
_translations['on_error']                                  = 'endend';
_translations['exit']                                      = 'endend';
_translations['error']                                     = 'fehlerhaft';
_translations['release']                                   = 'endend';
_translations['ended']                                     = 'beendet';
_translations['deleting_files']                            = 'StdIn, StdOut und StdErr werden gelöscht';
_translations['closed']                                    = 'beendet';
_translations['Replacement is standing by']                = 'Ersetzung ist angehalten';
_translations['changed file has error']                    = 'Fehler in geänderter Datei';
_translations['is incorrect or missing']                   = 'fehlt oder ist fehlerhaft';
_translations['not an order job']                          = 'kein Auftrags-Job';
_translations['non-excl.']                                 = 'nicht exkl.';
_translations['is being replaced']                         = 'wurde ersetzt';
_translations['undefined']                                 = 'undefiniert';


                                                           
//Task start causes                                        
_translations['none']                                      = 'undefiniert';
_translations['period_once']                               = 'Neustart';
_translations['period_single']                             = 'Startzeit';
_translations['period_repeat']                             = 'Intervall';
_translations['queue']                                     = 'eingereiht';
_translations['queue_at']                                  = 'eingereiht';
_translations['directory']                                 = 'Verzeichnis';
_translations['delay_after_error']                         = 'Verzögerung';
_translations['order']                                     = 'Auftrag';
_translations['wake']                                      = 'geweckt';                                                     


/******************************************************************************
*                                 JOB CHAINS                                  *
******************************************************************************/
//some labels are already translated above
//Label of Checkboxes and Selectboxes
_translations['Job chains containing jobs']                = 'Job-Ketten mit Jobs';
_translations['Show order history']                        = 'Auftrags-Historie anzeigen';
_translations['Show jobs']                                 = 'Jobs anzeigen';
_translations['Show orders']                               = 'Aufträge anzeigen';
                                                           
//Label of Selectbox of job chains sort                    
_translations['Sort job chains by']                        = 'Job-Ketten sortieren nach';
                                                           
//Label of Selectbox of job chains filtering               
_translations['Filter job chains by']                      = 'Job-Ketten filtern durch';
                                                           
//Table header of job chains list                          
_translations['Order state']                               = 'Auftrags-Status';
_translations['Job chain']                                 = 'Job-Kette';
_translations['Job state']                                 = 'Job-Status';
                                                           
//Table header in detail frame                             
_translations['JOB CHAIN']                                 = 'JOB-KETTE';
_translations['ORDER HISTORY']                             = 'AUFTRAGS-HISTORIE';
                                                           
//Menu content of job chain menu                           
_translations['Create job chain']                          = 'Neue Job-Kette';
_translations['Job chain menu']                            = 'Job-Ketten-Menü';
_translations['Job node menu']                             = 'Job-Knoten-Menü';
_translations['Add order']                                 = 'Auftrag hinzufügen';
_translations['Add persistent order']                      = 'Auftrag persistent hinzufügen';
_translations['Delete temp. orders']                       = 'Temp. Aufträge löschen';
_translations['Delete job chain']                          = 'Job-Kette löschen';
_translations['Skip node']                                 = 'Knoten überspringen';
_translations['Stop node']                                 = 'Knoten stoppen';
_translations['Stop job']                                  = 'Job stoppen';
_translations['Unstop job']                                = 'Job fortsetzen';
_translations['Unskip']                                    = 'Einreihen';

//Mouseover-Title
_translations['show job chain details']                    = 'zeige Job-Ketten-Details';

//job chain (node) states
_translations['under_construction']                        = 'unvollständig';
_translations['finished']                                  = 'beendet';
_translations['removing']                                  = 'wird gelöscht';
_translations['Node is stopped']                           = 'Knoten ist gestoppt';
_translations['Node is skipped']                           = 'Knoten wird übersprungen';

//misc.                                                          
_translations['No job chains found']                       = 'Es wurden keine Job-Ketten gefunden';
_translations['Web service']                               = 'Web-Service';
_translations['file orders']                               = 'Datei-Aufträge';
_translations['pattern']                                   = 'Muster';
_translations['delay']                                     = 'Verzögerung';
_translations['repeat']                                    = 'Wiederholung';
_translations['blacklist']                                 = 'Blacklist';
_translations['max. Orders']                               = 'Max. Aufträge';

/******************************************************************************
*                         ORDERS/ORDERQUEUE/BLACKLIST                         *
******************************************************************************/
//some labels are already translated above
//Label of Selectbox of orders sort
_translations['Sort orders by']                            = 'Aufträge sortieren nach';
                                                           
//Options in Selectbox of orders sort                      
_translations['job chain in ascending order']              = 'Job-Kette (aufsteigend)';
_translations['job chain in descending order']             = 'Job-Kette (absteigend)';
                                                           
//Label of Selectbox of jobs filtering                     
_translations['Filter orders by']                          = 'Aufträge filtern durch';
                                                                                                                                                                                 
//Mouseover-Title                                          
_translations['Order is processed by Scheduler member']    = 'Auftrag wird verarbeitet vom Scheduler';
_translations['show order details']                        = 'zeige Auftrags-Details';
_translations['This order is a replacement for another order with the same ID'] = 'This order is a replacement for another order with the same ID';
                                                           
//Detail frame                                             
_translations['ORDER']                                     = 'AUFTRAG';
                                                                                                                     
//Menu content of order menu                               
_translations['Order menu']                                = 'Auftrags-Menü';
_translations['Start order now']                           = 'Starte Auftrag sofort';
_translations['Start order at']                            = 'Starte Auftrag um...';
_translations['Start order parametrized']                  = 'Starte Auftrag parametrisiert';
_translations['Set order state']                           = 'Status setzen';
_translations['Suspend order']                             = 'Auftrag suspendieren';
_translations['Resume order']                              = 'Auftrag fortsetzen';
_translations['Resume order parametrized']                 = 'Auftrag parametrisiert fortsetzen';
_translations['Resume order $order']                       = 'Auftrag fortsetzen $order';
_translations['Reset order']                               = 'Auftrag zurücksetzen';
_translations['Delete order']                              = 'Auftrag löschen';
_translations['Remove setback']                            = 'Verzögerung löschen';

//misc
_translations['No orders found']                           = 'Es wurden keine Aufträge gefunden';
_translations['Processed by']                              = 'Verarbeitet von';
_translations['Order']                                     = 'Auftrag';
_translations['Setback']                                   = 'Verzögerung';
_translations['deleted']                                   = 'gelöscht';
_translations['Replacement']                               = 'Ersatz';
_translations['currently processed by']                    = 'momentan verarbeitet von'
_translations['on blacklist']                              = 'in der Blacklist'
                                                           

                                                           
//order history                                            
_translations['No orders in the history']                  = 'Es sind keine Aufträge in der Historie vorhanden';


/******************************************************************************
*                                PROCESS CLASS                                *
******************************************************************************/
//some labels are already translated above
_translations['Process class']                             = 'Prozess-Klasse';
                                                           
//Table header of process class list                       
_translations['Operations']                                = 'Operationen';
_translations['Callbacks']                                 = 'Callbacks';
_translations['Current operation']                         = 'Aktuelle Operation';
                                                           
_translations['(default)']                                 = '(default)';
_translations['max processes']                             = 'max. Prozesse';
_translations['Remote Scheduler']                          = 'Remote Scheduler';

_translations['No process classes found']                  = 'Es wurden keine Prozess-Klassen gefunden';
                                                           

/******************************************************************************
*                                   CLUSTER                                   *
******************************************************************************/
//some labels are already translated above
//Table header of process class list
_translations['Last heart beat']                           = 'Letzter Herzschlag';
_translations['Detected heart beats']                      = 'Gezählte Herzschläge';
_translations['Backup precedence']                         = 'Backup-Priorität';
                                                           
//Menu content of cluster member                           
_translations['Cluster member menu']                       = 'Cluster-Menü';
_translations['Delete entry']                              = 'Eintrag löschen';
_translations['Restart']                                   = 'Erneut starten';

//misc.
_translations['active Scheduler(s)']                       = 'aktive(r) Scheduler';
_translations['exclusive Scheduler(s)']                    = 'exklusive(r) Scheduler';
_translations['This Scheduler is active']                  = 'Dieser Scheduler ist aktiv';
_translations['and exclusive']                             = 'und exclusiv';
_translations['Only active JobSchedulers are allowed to start operation.'] = 'Nur aktiven JobSchedulern ist es erlaubt Operationen zu startet.';
_translations['This Scheduler']                            = 'Dieser Scheduler';
_translations['(was active!)']                             = '(war aktiv!)';
_translations['active']                                    = 'aktiv';
_translations['inactive']                                  = 'inaktiv';
_translations['distributed orders']                        = 'verteilte Aufträge';
_translations['exclusive']                                 = 'exklusiv';
_translations['backup']                                    = 'Backup';
_translations['still checking...']                         = 'wird peprüft...';
_translations['dead']                                      = 'tot';
_translations['discovered']                                = 'entdeckt';
_translations['after']                                     = 'nach';
_translations['Deactivated by']                            = 'Deaktiviert durch';
_translations['ago']                                       = 'vergangen';



/******************************************************************************
*                               REMOTE SCHEDULER                              *
******************************************************************************/
//some labels are already translated above

//Table header of process class list                       
_translations['IP']                                        = 'IP';
_translations['Hostname']                                  = 'Host';
_translations['Port']                                      = 'Port';
_translations['Last Update']                               = 'letzte Aktualisierung';
_translations['Connected']                                 = 'Verbunden';
_translations['Disconnected']                              = 'Getrennt';
_translations['Version']                                   = 'Version';
                                                           
//misc.                                                    
_translations['Scheduler(s)']                              = 'JobScheduler';
_translations['connected']                                 = 'verbunden';



/******************************************************************************
*                                    LOCKS                                    *
******************************************************************************/
//some labels are already translated above
_translations['No locks found']                            = 'Es wurden keine Sperren gefunden';
_translations['Lock']                                      = 'Sperre';
                                                           
//Table header of process class list                       
_translations['non-exclusively locked']                    = 'nicht exklusiv gesperrt';
_translations['locked']                                    = 'gesperrt';
_translations['free']                                      = 'frei';

//misc.
_translations['Holders (non-exclusive)']                   = 'Nicht exklusive Eigentümer';
_translations['Holders (exclusive)']                       = 'Exklusive Eigentümer';
_translations['Waiting jobs (exclusive)']                  = 'Nicht exklusiv wartende Jobs';
_translations['Waiting jobs (non-exclusive)']              = 'Exklusiv wartende Jobs';

//Mouseover-Title
_translations['Lock is not available, it is locked']       = 'Sperre ist nicht verfügbar; sie ist gesperrt';
_translations['Lock is available']                         = 'Sperre ist verfügbar';



/******************************************************************************
*                                LAST ACTIVITIES                              *
******************************************************************************/
//some labels are already translated above
//radio buttons, checkboxes
_translations['Show all']                                  = 'Alle anzeigen';
_translations['Show only orders']                          = 'Nur Aufträge anzeigen';
_translations['Show only tasks']                           = 'Nur Tasks anzeigen';
_translations['Show only faulty tasks and orders']         = 'Nur fehlerhafte Tasks/Aufträge anzeigen';
_translations['Show last tasks error']                     = 'Letzten Task-Fehler anzeigen';

//Table header of job chains list                          
_translations['Job name']                                  = 'Job-Name';
_translations['Exitcode']                                  = 'Exitcode';
_translations['Order ID']                                  = 'Auftrags-ID';
_translations['Order state']                               = 'Auftrags-Status';

//Button title
_translations['Show order log']                            = 'Auftr.-Protokoll';
_translations['Show task log']                             = 'Job-Protokoll';

//misc.
_translations['No last activities found']                  = 'Es wurden keine Aktivitäten gefunden';


/******************************************************************************
*                                   SCHEDULES                                 *
******************************************************************************/
//some labels are already translated above

//Menu content of schedules                               
_translations['Create schedule']                           = 'Neuer Schedule';
_translations['Schedule menu']                             = 'Schedule-Menü';
_translations['Substitute menu']                           = 'Substitut-Menü';
_translations['Add substitute']                            = 'Substitut hinzufügen';
_translations['Edit schedule']                             = 'Schedule bearbeiten';
_translations['Delete schedule']                           = 'Schedule löschen';

//Table header of job chains list                          
_translations['Schedule']                                  = 'Schedule';
_translations['Valid from']                                = 'Gültig von';
_translations['Valid to']                                  = 'Gültig bis';
_translations['Substituted by']                            = 'Ersetzt durch';

//Mouseover-Title
_translations['show schedule details']                     = 'zeige Schedule-Details';

//misc.
_translations['Green']                                     = 'Grün';
_translations['marked schedules are currently active']     = 'markierte Schedules sind momentan aktiv';
_translations['No schedules found']                        = 'Es wurden keine Schedules gefunden';
_translations['Used by jobs']                              = 'Benutzt von den Jobs';
_translations['Used by orders']                            = 'Benutzt von den Aufträgen';
_translations['of job chain']                              = 'der Job-Kette';
_translations['Substituted schedule']                      = 'Ersetzender Schedule';

//State
_translations['incomplete']                                = 'unvollständig';

//Detail frame                                             
_translations['SCHEDULE']                                  = 'SCHEDULE';
_translations['Valid']                                     = 'Gültig';


/******************************************************************************
*                                LOG CATEGORIES                               *
******************************************************************************/
//some labels are already translated above
_translations['LOG CATEGORIES']                            = 'PROTOKOLL KATEGORIEN';
                                                           
//misc.                                                    
_translations['The default log caregories are marked']     = 'Voreingestellte Protokoll-Kategorien sind';
_translations['orange']                                    = 'orange';
_translations['and they are active after each reset.']     = 'markiert und sind nach jedem Zurücksetzen wieder aktiv.';
_translations['Current log categories setting']            = 'Derzeitige Protokoll-Kategorien-Einstellung';
_translations['Set']                                       = 'Setzen';
_translations['log categories for a duration of']          = 'der Protokoll-Kategorien für einen Zeitraum von';
_translations['seconds']                                   = 'Sekunden';
_translations['log categories are updated']                = 'Protokoll-Kategorien wurden aktualisiert';
_translations['reset is executed']                         = 'Protokoll-Kategorien wurden zurückgesetzt';
_translations['reset is executed after']                   = 'Protokoll-Kategorien wurden zurückgesetzt nach';
_translations['Next reset']                                = 'Nächstes Zurücksetzen';
_translations['implicit']                                  = 'implizit';
_translations['explicit']                                  = 'explizit';

//Table header of log categories list
_translations['Category']                                  = 'Kategorie';
_translations['Mode']                                      = 'Modus';
_translations['Description']                               = 'Beschreibung';

_translations['No log categories found']                   = 'Es wurden keine Protokoll-Kategorien gefunden';


/******************************************************************************
*                                  CALENDAR                                   *
******************************************************************************/

_translations['CALENDAR']                                  = 'KALENDER';
_translations['job']                                       = 'Job';
_translations['job chain']                                 = 'Job-Kette';
_translations['of job chain']                              = 'der Job-Kette';
_translations['all jobs']                                  = 'alle Jobs';
_translations['all jobs and orders']                       = 'alle Jobs und Aufträge';
_translations['Start times for']                           = 'Startzeiten für';
_translations['by calling']                                = 'durch die Abfrage';
_translations['with output format']                        = 'im Ausgabeformat';
_translations['at']                                        = 'um';


/******************************************************************************
*                                  FILTER                                     *
******************************************************************************/

_translations['FILTER ADMINISTRATION']                     = 'FILTERVERWALTUNG';
_translations['close']                                     = 'Schließen';
_translations['store']                                     = 'Speichern';
_translations['store as ...']                              = 'Speichern unter ...';
_translations['remove']                                    = 'Löschen';
_translations['quick check']                               = 'Schnellauswahl';
_translations['regular expression for quick check']        = 'Regulärer Ausdruck für Schnellauswahl';
_translations['select all']                                = 'Alle auswählen';
_translations['deselect all']                              = 'Alle abwählen';
_translations['new filter']                                = 'Neuer Filter';
_translations['filter was stored']                         = 'Filter wurde aktualisiert';
_translations['new filter was stored']                     = 'Filter wurde angelegt';
_translations['filter was removed']                        = 'Filter wurde gelöscht';
_translations['Filter name must be stated']                = 'Bitte geben Sie einen Filter an';
_translations['Using\n\n|\n=>\n;\n\nis not allowed']       = 'Die Benutzung der Zeichen\n\n|\n=>\n;\n\nist nicht erlaubt';
_translations['A filter "$filter_name" is already defined in the file $file.'] = 'Ein Filter "$filter_name" ist bereits in der Datei $file definiert.';
_translations['The same filter name "$filter" was already defined\n\nDo you want nevertheless to store it ?'] = 'Es ist bereits ein Filter "$filter" definiert\n\nWollen Sie diesen trotzdem speichern?'; 


/******************************************************************************
*                                   ERROR                                     *
******************************************************************************/

_translations['source']                                    = 'Datei';
_translations['line']                                      = 'Zeile';
_translations['column']                                    = 'Spalte';
_translations['unknown error']                             = 'unbekannter Fehler';


/******************************************************************************
*                           DIALOGS OF MENU FUNCTIONS                         *
******************************************************************************/

//Buttons
_translations['submit']                                    = 'Speichern';
_translations['cancel']                                    = 'Abbrechen';
_translations['new param']                                 = 'Neuer Parameter';
_translations['run time editor']                           = 'Runtime-Editor';

//Plausi
_translations['$field must be stated!']                    = '$field muss angegeben werden!';
_translations['Period from ($field) is invalid date or before 1970-01-01.'] = 'Die Periode von ($field) ist ein ungültiges Datum oder ist älter als 01.01.1970.';
_translations['Max. hits ($field) is not a positive number.'] = 'Die maximale Trefferanzahl ($field) ist keine positive Zahl.';
_translations['Please add an existing order job to the job chain nodes.'] = 'Bitte fügen den Job-Ketten-Knoten einen existierenden Auftrags-Job hinzu.';
_translations['Please select an existing order job on the left hand side.'] = 'Bitte wählen Sie links einen existierenden Auftrags-Job.';

//Plausi fields
_translations['Start time']                                = 'Startzeitpunkt';
_translations['Name']                                      = 'Name';

//Scheduler settings
_translations['The following values will be stored in a cookie. If no cookies are available the values which are set in <code>./custom.js</code> are used.'] = 'Die folgenden Einstellungen werden in einem Cookie gespeichert. Falls keine Cookies zur Verfügung stehen, werden die entsprechenden Werte aus <code>./custom.js</code> herangezogen.';
_translations['Onload Values']                             = 'Einstellungen beim Laden';
_translations['periodically every']                        = 'periodisch alle';
_translations['Tabs']                                      = 'Tabs';
_translations['Switch to']                                 = 'Am Anfang den Tab';
_translations['as the beginning view']                     = 'anzeigen';
_translations['View Mode']                                 = 'Art der Ansicht';
_translations['for']                                       = 'für';
_translations['Selects, Checkboxes and Radios']            = 'Auswahllisten, Checkboxen und Radios';
_translations['in the <i>jobs</i> tab']                    = 'im <i>Jobs</i>-Tab';
_translations['in the <i>job chains</i> tab']              = 'im <i>Job-Ketten</i>-Tab';
_translations['in the job chain details']                  = 'in den <i>Job-Ketten</i>-Details';
_translations['in the <i>last activities</i> tab']         = 'im <i>Aktivitäten</i>-Tab';
_translations['all orders and tasks']                      = 'alle Aufträge und Tasks';
_translations['only tasks']                                = 'nur Tasks';
_translations['only orders']                               = 'nur Aufträge';
_translations['Runtime Values']                            = 'Einstellungen zur Laufzeit';
_translations['Limits']                                    = 'Beschränkungen';
_translations['Max. number of orders per job chain']       = 'Max. Anzahl der Aufträge pro Job-Kette';
_translations['Max. number of last activities']            = 'Max. Anzahl der letzten Aktivitäten';
_translations['Max. number of history entries per order']  = 'Max. Anzahl der Historien-Einträge pro Auftrag';
_translations['Max. number of history entries per task']   = 'Max. Anzahl der Historien-Einträge pro Task';
_translations['Termintate within']                         = 'Beenden innerhalb';
_translations['Max. seconds within the JobScheduler terminates'] = 'Max. Zeitraum (Sekunden) in der sich der JobScheduler beendet';
_translations['Dialogs']                                   = 'Dialoge';
_translations['Default start time in the &quot;<i>Start task/order at</i> &quot;-Dialog is <i>now</i>'] = 'Voreingestellter Startzeitpunkt im &quot;<i>Start Task/Auftrag um...</i> &quot;-Dialog ist <i>now</i>';
_translations['Debugging of the operations GUI']           = 'Debugging der Operations GUI';
_translations['Level']                                     = 'Level';

//start task/order
_translations['Start task $task']                          = 'Starte Task $task';
_translations['Start order $order']                        = 'Starte Auftrag $order';
_translations['<b>Enter a start time</b><span class="small"> in ISO format &quot;yyyy-mm-dd HH:MM[:SS]&quot; or &quot;now&quot;. The time at which a task is to be started &lt;run_time&gt; is deactivated. Relative times - &quot;now + HH:MM[:SS]&quot; and &quot;now + SECONDS&quot; - are allowed.<span>' ] = '<b>Setze eine Startzeit</b><span class="small"> im ISO-Format &quot;yyyy-mm-dd HH:MM[:SS] oder &quot;now&quot;. Startzeiten des &lt;run_time&gt;-Elements sind deaktiviert. Relativive Zeitangaben - &quot;now + HH:MM[:SS]&quot; and &quot;now + SECONDS&quot; - sind möglich.<span>';
_translations['<b>Enter a run time</b> or use the $editor'] = 'Für die Startzeiten nutze das Textfeld oder den $editor';
_translations['Start enforced']                            = ' erzwungender Start';
_translations['Change parameters']                         = 'Parameter ändern';
_translations['Declare parameters']                        = 'Parameter angeben';
_translations['Declare new parameters']                    = 'Neue Parameter angeben';
_translations['name']                                      = 'Name';
_translations['value']                                     = 'Wert';

//add order
_translations['Add order to $job_chain']                   = 'Füge Auftrag hinzu für $job_chain';
_translations['Enter an order id']                         = 'Id des Auftrags';
_translations['Enter an order title']                      = 'Title des Auftrags';
_translations['Select an order state']                     = 'Status des Auftrags';
_translations['Select an order end state']                 = 'Endstatus des Auftrags';
_translations['In order to store this order in a hot folder you have to state an order id.\nYour order will only be stored permanently, however, it is valid for\nthe lifetime of this JobScheduler session. Do you want to continue?'] = 'Um den Auftrag dem Hot Folder zu übergeben, muss eine Auftrags-ID angegeben\nwerden. Ansonsten hat dieser nur die Lebenszeit der aktuellen JobScheduler Session.\nWollen Sie fortfahren?';

//remove temp. orders
_translations['Delete temporary orders from $job_chain']   = 'Temporäre Aufträge der Job-Kette $job_chain löschen';
_translations['all']                                       = 'alle'; 

//set order state
_translations['Set order state of $order']                 = 'Setze Status für $order';
_translations['<b>Select a new order state</b>']           = '<b>Wähle einen neuen Auftrags-Status.</b>';
_translations['<span class="small">The current order state is $state.</span>'] = '<span class="small">Der aktuelle Status ist $state.</span>';
_translations['<b>Select a new order end state</b>']       = '<b>Wähle einen neuen Auftrags-Endstatus.</b>';
_translations['<span class="small">The current order end state is $state.</span>'] = '<span class="small">Der aktuelle Endstatus ist $state.</span>';

//set run time
_translations['Set run time of $job']                      = 'Setze Startzeit für $job setzen';
_translations['or choose a schedule']                      = 'oder wähle einen Schedule';

//terminate task
_translations['Terminate task $task_id']                   = 'Task $task_id beenden';
_translations['with timeout']                              = 'mit Timeout';
_translations['Should the task not terminate within the specified timeout then it will be killed.'] = 'Sollte die Task innerhalb des angegebenen Timeouts nicht beendet ein, dann wird sie sofort abgebrochen.';
_translations['Please enter a number for the timeout.']    = 'Bitte geben Sie eine Zahl für das Timeout an.';

//schedules
_translations['Add schedule']                              = 'Neuer Schedule';
_translations['Add substitute for $schedule']              = 'Neues Substitut für $schedule';
_translations['Edit $schedule']                            = 'Bearbeite $schedule';
_translations['Enter a schedule name']                     = 'Name des Schedules';

//calendar
_translations['Parameterize the start times list']         = 'Startzeiten-Abfrage parametrisieren';
_translations['<b>Set the period</b>']                     = '<b>Bestimme einen Zeitraum</b>';
_translations['<span class="small">(Timestamps in ISO format yyyy-mm-dd[ hh:mm:ss])</span>']  = '<span class="small">(Datumsangaben im ISO-Format yyyy-mm-dd[ hh:mm:ss])</span>';
_translations['...from']                                   = '...von';
_translations['...to']                                     = '...bis';
_translations['Max. hits']                                 = 'Max. Trefferanzahl';
_translations['Output format']                             = 'Ausgabeformat';
_translations['Include order start times']                 = 'Inklusive Auftrags-Startzeiten';

//job chain
_translations['Sorry, but this feature is only supported for\nJobScheduler version 2.0.204.5774 or higher'] = 'Leider kann diese Funktion erst für JobScheduler\nab der Version 2.0.204.5774 angeboten werden.';
_translations['Modify job chain']                          = 'Job-Kette ändern';
_translations['Now you can modify the job chain by editing the text area content.'] = 'Bearbeiten Sie den Textfeld-Inhalt, falls noch Änderungen an der Job-Kette nötig sind.';
_translations['Orders are stored in the database (orders_recoverable)'] = 'Aufträge in die Datenbank speichern (orders_recoverable)';
_translations['Existing order jobs']                       = 'Existierende Auftrags-Jobs';
_translations['Job chain nodes']                           = 'Job-Kette-Knoten';
_translations['Enter a folder']                            = 'Verzeichnis';
_translations['Enter a job chain name']                    = 'Name der Job-Kette';
_translations['Enter a job chain title']                   = 'Title der Job-Kette';


/******************************************************************************
*                           JOB DESCRIPTION                                   *
******************************************************************************/

_translations['The JobScheduler $scheduler has no jobs with a description.']               = 'Der JobScheduler $scheduler hat keine Jobs mit einer Beschreibung.';
_translations['Because of a security sanction of your browser you must reload this site!'] = 'Wegen einer Sicherheitssanktion Ihres Browsers muss die Seite neu geladen werden!';
_translations['Please select a job to display its description.']                           = 'Wählen Sie einen Job um dessen Beschreibung anzuzeigen.';
_translations['Please enter a job name to display its description.']                       = 'Geben Sie einen Job-Namen an, um dessen Beschreibung anzuzeigen.';


/******************************************************************************
*                           CONFIRMS                                          *
******************************************************************************/

_translations['Do you really want to terminate the JobScheduler?']          = 'Wollen Sie wirklich den JobScheduler beenden?';
_translations['Do you really want to restart the JobScheduler?']            = 'Wollen Sie wirklich den JobScheduler neu starten?';
_translations['Do you really want to abort the JobScheduler?']              = 'Wollen Sie wirklich den JobScheduler abbrechen?';
_translations['Do you really want to pause the JobScheduler?']              = 'Wollen Sie wirklich den JobScheduler anhalten?';
_translations['Do you really want to continue the JobScheduler?']           = 'Wollen Sie wirklich den JobScheduler fortsetzen?';
_translations['Do you really want to terminate the JobScheduler cluster?']  = 'Wollen Sie wirklich den JobScheduler Cluster beenden?';
_translations['Do you really want to restart the JobScheduler cluster?']    = 'Wollen Sie wirklich den JobScheduler Cluster neu starten?';
_translations['Do you really want to delete the dead entry?']               = 'Wollen Sie wirklich den toten Eintrag löschen?';

_translations['Do you really want to stop this job chain?']                 = 'Wollen Sie wirklich diese Job-Kette stoppen?';
_translations['Do you really want to unstop this job chain?']               = 'Wollen Sie wirklich diese Job-Kette fortsetzen?';
_translations['Do you really want to delete this job chain?']               = 'Wollen Sie wirklich diese Job-Kette löschen?';
_translations['Do you really want to stop this job chain node?']            = 'Wollen Sie wirklich diesen Job-Kette-Knoten stoppen?';
_translations['Do you really want to unstop this job chain node?']          = 'Wollen Sie wirklich diesen Job-Kette-Knoten fortsetzen?';
_translations['Do you really want to skip this job chain node?']            = 'Wollen Sie wirklich diesen Job-Kette-Knoten überspringen?';
_translations['Do you really want to unskip this job chain node?']          = 'Wollen Sie wirklich diesen Job-Kette-Knoten wieder einreihen?';

_translations['Do you really want to start this job?']                      = 'Wollen Sie wirklich diesen Job starten?';
_translations['Do you really want to stop this job?']                       = 'Wollen Sie wirklich diesen Job stoppen?';
_translations['Do you really want to unstop this job?']                     = 'Wollen Sie wirklich diesen Job fortsetzen?';
_translations['Do you really want to delete this job?']                     = 'Wollen Sie wirklich diesen Job löschen?';
_translations['Do you really want to kill this task?']                      = 'Wollen Sie wirklich diese Task abbrechen?';
_translations['Do you really want to terminate this task?']                 = 'Wollen Sie wirklich diese Task beenden?';
_translations['Do you really want to delete this task?']                    = 'Wollen Sie wirklich diese Task löschen?';
_translations['Do you really want to end this task?']                       = 'Wollen Sie wirklich diese Task beenden?';
_translations['Do you really want to end the tasks?']                       = 'Wollen Sie wirklich die Tasks beenden?';
_translations['Do you really want to suspend the tasks?']                   = 'Wollen Sie wirklich die Tasks suspendieren?';
_translations['Do you really want to continue the tasks?']                  = 'Wollen Sie wirklich die Tasks fortsetzen?';

_translations['Do you really want to start this order?']                    = 'Wollen Sie wirklich diesen Auftrag starten?';
_translations['Do you really want to add an order?']                        = 'Wollen Sie wirklich einen Auftrag hinzufügen?';
_translations['Do you really want to reset this order?']                    = 'Wollen Sie wirklich diesen Auftrag zurücksetzen?';
_translations['Do you really want to suspend this order?']                  = 'Wollen Sie wirklich diesen Auftrag suspendieren?';
_translations['Do you really want to resume this order?']                   = 'Wollen Sie wirklich diesen Auftrag fortsetzen?';
_translations['Do you really want to change the order state?']              = 'Wollen Sie wirklich den Auftragsstatus ändern?';
_translations['Do you really want to delete this order?']                   = 'Wollen Sie wirklich diesen Auftrag löschen?';
_translations['Do you really want to delete selected orders?']              = 'Wollen Sie wirklich die ausgewählten Aufträge löschen?';
_translations['Do you really want to remove the setback?']                  = 'Wollen Sie wirklich die Verzögerung löschen?';

_translations['Do you really want to set the run time?']                    = 'Wollen Sie wirklich die Startzeiten ändern?';
_translations['Do you really want to add a substituting schedule?']         = 'Wollen Sie wirklich einen substituierenden Schedule hinzufügen?';
_translations['Do you really want to modify this schedule?']                = 'Wollen Sie wirklich diesen Schedule ändern?';
_translations['Do you really want to delete this schedule?']                = 'Wollen Sie wirklich diesen Schedule löschen?';

                           