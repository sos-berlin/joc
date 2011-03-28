//Control flag (if false then default language is used)
_lang_file_exists = true;

/******************************************************************************
*                            SCHEDULER (top_frame)                            *
******************************************************************************/

//Window title 
_translations['Job Scheduler']                             = 'Planificador de trabajo';
_translations['-id']                                       = '-ID';

//Product info
_translations['Operations GUI']                            = 'GUI de operaciones';
_translations['Build']                                     = 'Build';
_translations['All rights reserved.']                      = 'Todos los derechos reservados.'
_translations['OK']                                        = 'OK'

//Tabs
_translations['Jobs']                                      = 'Trabajos';
_translations['Job Chains']                                = 'Cadenas de trabajo';
_translations['Orders']                                    = 'Ordenes';
_translations['Schedules']                                 = 'Horarios';
_translations['Process Classes']                           = 'Clases de procesos';
_translations['Locks']                                     = 'Bloqueos';
_translations['Cluster']                                   = 'Cluster';
_translations['Remote Schedulers']                         = 'Programadores remotos';
_translations['Last Activities']                           = 'Ultimas actividades';
                                                           
//Buttons menu, extras, update, hide          
_translations['Menu']                                      = 'Menu';
_translations['Extras']                                    = 'Extras';
_translations['Hide']                                      = 'Ocultar detalles';
_translations['Update']                                    = 'Actualizar';

//Menu content of scheduler menu
_translations['Show log']                                  = 'Mostrar registro';
_translations['Show job dependencies']                     = 'Mostrar dependencies del trabajo';
_translations['Show job chain dependencies']               = 'Mostrar dependencies de la cadena de trabajo';
_translations['Show start times']                          = 'Mostrar horarios de inicio';
_translations['Manage filters']                            = 'Gestionar filtros';
_translations['Manage log categories']                     = 'Gestionar categories de registro';
_translations['Pause']                                     = 'Pausa';
_translations['Continue']                                  = 'Continuar';
_translations['Terminate']                                 = 'Terminar';
_translations['Terminate within ~$secs']                   = 'Terminar sin ~$secs';
_translations['Terminate and restart']                     = 'Terminar y reiniciar';
_translations['Terminate and restart within ~$secs']       = 'Terminar y reiniciar sin ~$secs';
_translations['Abort immediately']                         = 'Abortar inmediatamente';
_translations['Abort immediately and restart']             = 'Abortar inmediatamente y reiniciar';
_translations['Terminate cluster']                         = 'Terminar cluster';
_translations['Terminate cluster within ~$secs']           = 'Terminar cluster sin ~$secs';
_translations['Terminate and restart cluster']             = 'Terminar y reiniciar cluster';
_translations['Terminate and restart cluster within ~$secs'] = 'Terminar y reiniciar cluster sin ~$secs';
_translations['Terminate fail-safe']                       = 'Terminar modo a prueba de fallos';
_translations['Terminate fail-safe within ~$secs']         = 'Terminar  modo a prueba de fallos sin ~$secs';
                                                           
//Menu content of extras                      
_translations['Settings']                                  = 'Ajustes';
_translations['Monitor']                                   = 'Monitor';
_translations['Configuration']                             = 'ConfiguraciÛn';
                                                           
//First line                                  
_translations['Documentation']                             = 'Documentacion';
_translations['every $secs']                               = 'Todos los $secs';
_translations['incl. Hot Folders']                         = 'incl. carpetas activas';
                                                      
//Other lines                            
_translations['ID']                                        = 'ID';
_translations['State']                                     = 'Estado';
_translations['Pid']                                       = 'Pid';
_translations['Time']                                      = 'Tiempo';
_translations['jobs']                                      = 'Trabajos';
_translations['need process']                              = 'Necesidad de procedimientos';
_translations['tasks']                                     = 'Tareas';
_translations['orders']                                    = 'Ordenes';
_translations['$cnt in cluster']                           = '$cnt en cluster';
_translations['Start Time']                                = 'Hora de inicio';

//Job Scheduler states
_translations['starting']                                  = 'Iniciando';
_translations['running']                                   = 'ejecutando';
_translations['paused']                                    = 'En pausa';
_translations['stopping']                                  = 'Parando';
_translations['stopping_let_run']                          = 'Se detiene';
_translations['stopped']                                   = 'Detenido';
_translations['waiting_for_activation']                    = 'Esperando activacion';

//Misc.
_translations['unknown']                                   = 'Desconocido';
_translations['THIS IS A BACKUP...']                       = 'Esto es una copia de seguridad...';
_translations['Backup Job Scheduler:']                     = 'Copia de seguridad de planificador de trabajo:';
_translations['Job Scheduler is waiting for database ...'] = 'Planificador de trabajo espera base de datos ...';
_translations['Waiting for response from Job Scheduler ...'] = 'Esperando respuesta de planificador de trabajo ...';
_translations['No connection to Job Scheduler']            = 'No hay conexiÛn con el planificador de trabajos';
_translations['Error at XML answer:']                      = 'Error en respuesta XML:';
_translations["Error at XSL answer '$xsl':"]               = " Error en respuesta XSL '$xsl':";
_translations["Error at HTTP answer '$url':"]              = " Error en respuesta HTTP '$url':";
_translations['$trial trial (of 5) to (re)connect to Job Scheduler'] = '$trial Intentos (de 5) con el planificador de trabajos';
_translations['First']                                     = 'Primero';
_translations['Second']                                    = 'Segundo';
_translations['Third']                                     = 'Tercero';
_translations['Fourth']                                    = 'Cuarto';
_translations['Last']                                      = 'Ultimo';
_translations['never']                                     = 'Nunca';
_translations['now']                                       = 'Ahora';
_translations['days']                                      = 'Dias';
_translations['The settings dialog is not available,\nbecause $file is used as settings file.'] = 'El dialogo de configuracion no esta disponible, \nporque $ archivo se utiliza como archivo de configuracion.'; 

//compact
_translations['from']                                      = 'de';
_translations['update']                                    = 'Actualizar';
_translations['last update']                               = 'Ultima Actualizacion';
_translations['Scheduler is running since']                = 'El planificador se ejecuta desde';


/******************************************************************************
*                                  JOBS/TASKS                                 *
******************************************************************************/
//some labels are already translated above
//Label of Checkboxes
_translations['Show tasks']                                = 'Mostrar tareas';

//Label and Options of Selectbox
_translations['All jobs']                                  = 'Todos los trabajos';
_translations['Standalone jobs']                           = 'Trabajos independientes';
_translations['Order jobs']                                = 'Ordenar trabajos';
_translations['with state']                                = 'Con estado';
_translations['(all)']                                     = '(todos)';
_translations['(other)']                                   = '(otros)';
_translations['running or stopped']                        = 'Funcionando o detenidos';
_translations['and with process class']                    = 'y con clase de proceso';
                                                           
//Label and Options in Selectbox of jobs sort                        
_translations['Sort jobs by']                              = 'Ordenar trabajos por';
_translations['(unsorted)']                                = '(desordenados)';
_translations['name in ascending order']                   = 'nombre en orden ascendente';
_translations['name in descending order']                  = 'nombre en orden descendente';
_translations['next start time in ascending order']        = 'Proximo inicio en orden ascendente';
_translations['next start time in descending order']       = 'Proximo inicio en orden descendente';
_translations['state in ascending order']                  = 'Estado en orden ascendente';
_translations['state in descending order']                 = 'Estado en orden descendente';
                                                           
//Title of images
_translations['toggle to']                                 = 'Cambiar a'; 
_translations['list view']                                 = 'Ver lista'; 
_translations['tree view']                                 = 'Ver arbol'; 
_translations['open all folders']                          = 'Abrir todas las carpetas';   
_translations['close all folders']                         = 'Cerrar todas las carpetas';   

//Label and Options of Selectbox of jobs filtering                     
_translations['Filter jobs by']                            = 'Filtrar trabajos por';
_translations['(no filtering)']                            = '(sin filtro)';
                                                           
//Table header of jobs list                                
_translations['Job']                                       = 'Trabajo';
_translations['Steps']                                     = 'Pasos';
_translations['Next start']                                = 'Proximo inicio';
                                                           
//Labels in detail frame                                   
_translations['JOB']                                       = 'TRABAJO';
_translations['TASK']                                      = 'TAREA';
_translations['File timestamp']                            = 'Fecha de archivo';
_translations['State text']                                = 'Estado de texto';
_translations['Error']                                     = 'Error';
_translations['Error in configuration file']               = 'Error en archivo de configuracion';
_translations['Error in changed file']                     = 'Error en archivo modificado';
_translations['(not loaded)']                              = '(no cargado)';
_translations['Removing delayed']                          = 'Remover retrasados';
_translations['Tasks']                                     = 'Tareas';
_translations['Run time defined by']                       = 'Tiempo de ejecuciÛn definido por';
_translations['orders to process']                         = 'Ordener por procesar';
_translations['Mail subject']                              = 'Asunto';
_translations['To']                                        = 'Para';
_translations['CC']                                        = 'CC';
_translations['BCC']                                       = 'BCC';
_translations['From']                                      = 'De';
_translations['SMTP']                                      = 'SMTP';
_translations['Last error']                                = 'Ultimo error';
_translations['Last warning']                              = 'Ultima advertencia';
_translations['Log level']                                 = 'Nivel de registro';
_translations['mail on error']                             = 'Email en caso de error';
_translations['mail on warning']                           = 'Email con advertencia';
_translations['mail on success']                           = 'Email exitoso';
_translations['mail on process']                           = 'Email en proceso';
_translations['start cause']                               = 'Causa';
_translations['waiting for order']                         = 'esperando orden';
_translations['Idle since']                                = 'Sin utilizar desde';
_translations['In process since']                          = 'En proceso desde';
_translations['Running since']                             = 'Funcionando desde';
_translations['Enqueued at']                               = 'En cola desde';
_translations['Subprocesses']                              = 'Sub-Procesos';
                                                           
//Task queue                                               
_translations['enqueued tasks']                            = 'Tareas en cola';
_translations['Id']                                        = 'ID';
_translations['Enqueued']                                  = 'En cola';
_translations['Start at']                                  = 'Comienza desde';
_translations['Delete']                                    = 'Suprimir';
                                                           
                                                           
//Task history                                             
_translations['No tasks in the history']                   = 'No hay tareas en el historial';
_translations['Started']                                   = 'Funcionando';
_translations['Cause']                                     = 'Causa';
_translations['Ended']                                     = 'Finalizado';
                                                           
//Order queue                                              
_translations['Start']                                     = 'Inicio';
                                                           
                                                           
//Title of Tabs                                            
_translations['Task Queue']                                = 'Cola de tareas';
_translations['Task History']                              = 'Historial de tareas';
_translations['Order Queue']                               = 'Cola de ordenes';
                                                           
                                                           
//Menu content of job menu                                 
_translations['Job menu']                                  = 'Menu de trabajo';
_translations['Show description']                          = 'Descripcion';
_translations['Show dependency']                           = 'Dependencia';
_translations['Start task immediately']                    = 'Iniciar tarea ahora';
_translations['Start task unforced now']                   = 'Iniciar tarea sin forzar ahora';
_translations['Start task at']                             = 'Iniciar tarea a las...';
_translations['Start task parametrized']                   = 'Inicio de la tarea parametrizada';
_translations['Set run time']                              = 'Establecer tiempo de ejecuciÛn';
_translations['Stop']                                      = 'Parar';
_translations['Unstop']                                    = 'Reanudar';
_translations['Reread']                                    = 'Volver a cargar';
_translations['End tasks']                                 = 'Finalizar tareas';
_translations['Suspend tasks']                             = 'Suspender tareas';
_translations['Continue tasks']                            = 'Continuar tareas';
_translations['Delete job']                                = 'Eliminar trabajo';
_translations['Do you really want to delete this job?']    = 'Desea usted realmente eliminar este trabajo?';
                                                           
                                                           
//Menu content of task menu                                
_translations['Task menu']                                 = 'Menu de tareas';
_translations['End']                                       = 'Finalizar';
_translations['Kill immediately']                          = 'Detener inmediatamente';
                                                           
                                                           
//Mouseover-Title                                          
_translations['show job details']                          = 'mostrar detalles del trabajo';
_translations['show task details']                         = 'mostrar detalles de la tarea';
_translations['show schedule details']                     = 'mostrar detalles de programaciÛn';
_translations['Order is deleted']                          = 'Orden eliminada';
_translations['Order is replaced']                         = 'Orden reemplazada';
                                                           
//misc.                          
_translations['Without start time']                        = 'Sin tiempo de inicio';
_translations['Task']                                      = 'Tarea';
_translations['No jobs found']                             = 'No se han encontrado trabajos';
                                                           
                                                           
//Job/Task states                                          
_translations['pending']                                   = 'pendiente';
_translations['loaded']                                    = 'cargado';
_translations['initialized']                               = 'inicializado';
_translations['not_initialized']                           = 'no inicializado';
_translations['none']                                      = 'ninguno';
_translations['read_error']                                = 'error de lectura';
_translations['delayed after error']                       = 'retrasado luego de error';
_translations['needs process']                             = 'necesita proceso';
_translations['needs lock']                                = 'necesita bloqueo';
_translations['loading']                                   = 'cargando';
_translations['waiting_for_process']                       = 'esperando proceso';
_translations['opening']                                   = 'abriendo';
_translations['opening_waiting_for_locks']                 = 'abriendo esperando bloqueos';
_translations['running_process']                           = 'ejecutando proceso';
_translations['running_remote_process']                    = 'ejecutando proceso remoto';
_translations['running_waiting_for_order']                 = 'ejecutando esperando ordenes';
_translations['running_waiting_for_locks']                 = 'ejecutando esperando bloqueos';
_translations['running_delayed']                           = 'ejecutando con retraso';
_translations['suspended']                                 = 'suspendido';
_translations['ending_waiting_for_subprocesses']           = 'terminando esperando por subproceso';
_translations['ending']                                    = 'terminando';
_translations['on_success']                                = 'con exito';
_translations['on_error']                                  = 'con error';
_translations['exit']                                      = 'salir';
_translations['error']                                     = 'error';
_translations['release']                                   = 'liberar';
_translations['ended']                                     = 'terminado';
_translations['deleting_files']                            = 'eliminando archivos';
_translations['closed']                                    = 'cerrado';
_translations['Replacement is standing by']                = 'Reemplazo est† en espera';
_translations['changed file has error']                    = 'Archivo de reemplazo tiene error';
_translations['is missing']                                = 'falta';
_translations['non-excl.']                                 = 'no excl.';
_translations['is being replaced']                         = 'ser† reemplazado';
_translations['undefined']                                 = 'indefinido';


                                                           
//Task start causes                                        
_translations['none']                                      = 'indefinido';
_translations['period_once']                               = 'reinicio';
_translations['period_single']                             = 'hora de inicio';
_translations['period_repeat']                             = 'intervalo';
_translations['queue']                                     = 'cola';
_translations['queue_at']                                  = 'cola en';
_translations['directory']                                 = 'directorio';
_translations['delay_after_error']                         = 'retraso luego de error';
_translations['order']                                     = 'orden';
_translations['wake']                                      = 'despert¢';                                                     


/******************************************************************************
*                                 JOB CHAINS                                  *
******************************************************************************/
//some labels are already translated above
//Label of Checkboxes and Selectboxes
_translations['Job chains containing jobs']                = 'Cadena de trabajos con trabajos';
_translations['Show order history']                        = 'Mostrar historial de ordenes';
_translations['Show jobs']                                 = 'Mostrar trabajos';
_translations['Show orders']                               = 'Mostrar ordenes';
                                                           
//Label of Selectbox of job chains sort                    
_translations['Sort job chains by']                        = 'Ordenar cadenas de trabajo por';
                                                           
//Label of Selectbox of job chains filtering               
_translations['Filter job chains by']                      = 'Filtrar cadenas de trabajo por';
                                                           
//Table header of job chains list                          
_translations['Order state']                               = 'Estado de orden';
_translations['Job chain']                                 = 'Cadena de trabajo';
_translations['Job chain/']                                = 'Cadena de trabajo-/';
_translations['Job state']                                 = 'Estado de trabajo';
                                                           
//Table header in detail frame                             
_translations['JOB CHAIN']                                 = 'CADENA DE TRABAJO';
_translations['ORDER HISTORY']                             = 'HISTORIAL DE ORDENES';
                                                           
//Menu content of job chain menu                           
_translations['Create job chain']                          = 'Nueva cadena de trabajo';
_translations['Job chain menu']                            = 'Men£ de cadenas de trabajo';
_translations['Job node menu']                             = 'Men£ de nodo de trabajo';
_translations['Add order']                                 = 'A§adir orden';
_translations['Add persistent order']                      = 'A§adir orden persistente';
_translations['Delete job chain']                          = 'Eliminar cadena de trabajo';
_translations['Skip']                                      = 'Omitir';
_translations['Unskip']                                    = 'No omitir';
_translations['Do you really want to delete this job chain?']  = 'Desea usted realmente eliminar esta cadena de trabajo?';

//Mouseover-Title
_translations['show job chain details']                    = 'Mostrar detalles de cadena de trabajo';

//job chain (node) states
_translations['under_construction']                        = 'incompleta';
_translations['finished']                                  = 'terminada';
_translations['removing']                                  = 'removiendo';
_translations['Node is stopped']                           = 'Nodo detenido';
_translations['Node is skipped']                           = 'Nodo omitido';

//misc.                                                          
_translations['No job chains found']                       = 'No han sido encontradas cadenas de trabajo';
_translations['Web service']                               = 'Servicio web';
_translations['file orders']                               = 'ordenes de archivo';
_translations['pattern']                                   = 'patr¢n';
_translations['delay']                                     = 'retraso';
_translations['repeat']                                    = 'repetir';
_translations['blacklist']                                 = 'lista negra';

/******************************************************************************
*                         ORDERS/ORDERQUEUE/BLACKLIST                         *
******************************************************************************/
//some labels are already translated above
//Label of Selectbox of orders sort
_translations['Sort orders by']                            = 'Ordenar ordenes por';
                                                           
//Options in Selectbox of orders sort                      
_translations['job chain in ascending order']              = 'Cadena de trabajo en orden ascendente';
_translations['job chain in descending order']             = 'Cadena de trabajo en orden descendente';
                                                           
//Label of Selectbox of jobs filtering                     
_translations['Filter orders by']                          = 'Filtrar ordenes por';
                                                                                                                                                                                 
//Mouseover-Title                                          
_translations['Order is processed by Scheduler member']    = 'Orden es procesada por programador miembro';
_translations['show order details']                        = 'mostrar detalles de orden';
_translations['This order is a replacement for another order with the same ID'] = 'Esta orden es el reemplazo de otra con la misma ID';
                                                           
//Detail frame                                             
_translations['ORDER']                                     = 'ORDEN';
                                                                                                                     
//Menu content of order menu                               
_translations['Order menu']                                = 'Menu de ordenes';
_translations['Start order now']                           = 'Iniciar orden ahora';
_translations['Start order at']                            = 'Iniciar orden a las...';
_translations['Start order parametrized']                  = 'Iniciar orden parametrizada';
_translations['Set order state']                           = 'Establecer estado de la orden';
_translations['Suspend order']                             = 'Suspender orden';
_translations['Resume order']                              = 'Resumir orden';
_translations['Reset order']                               = 'Reiniciar orden';
_translations['Delete order']                              = 'Eliminar orden';
_translations['Remove setback']                            = 'Remover contratiempo';
_translations['Do you really want to delete this order?']  = 'Desea usted realmente eliminar esta orden?';

//misc
_translations['No orders found']                           = 'No se han encontrado ordenes';
_translations['Processed by']                              = 'Procesadas por';
_translations['Order']                                     = 'Orden';
_translations['Setback']                                   = 'Contratiempo';
_translations['deleted']                                   = 'eliminada';
_translations['Replacement']                               = 'Reemplazo';
_translations['currently processed by']                    = 'procesada ahora por'
_translations['on blacklist']                              = 'en lista negra'
                                                           

                                                           
//order history                                            
_translations['No orders in the history']                  = 'No hay ordenes en el historial';


/******************************************************************************
*                                PROCESS CLASS                                *
******************************************************************************/
//some labels are already translated above
_translations['Process class']                             = 'Clase de proceso';
                                                           
//Table header of process class list                       
_translations['Operations']                                = 'Operaciones';
_translations['Callbacks']                                 = 'Devoluciones de llamada';
_translations['Current operation']                         = 'Operacion actual';
                                                           
_translations['(default)']                                 = '(por defecto)';
_translations['max processes']                             = 'max. procesos';
_translations['Remote Scheduler']                          = 'Planificador remoto';

_translations['No process classes found']                  = 'No se han encontrado clases de proceso';
                                                           

/******************************************************************************
*                                   CLUSTER                                   *
******************************************************************************/
//some labels are already translated above
//Table header of process class list
_translations['Last heart beat']                           = 'Ultimo latido del coraz¢n';
_translations['Detected heart beats']                      = 'Latidos detectados';
_translations['Backup precedence']                         = 'Prioridad de copia de seguridad';
                                                           
//Menu content of cluster member                           
_translations['Cluster member menu']                       = 'Menu de cluster';
_translations['Delete entry']                              = 'Remover entrada';
_translations['Restart']                                   = 'Reiniciar';

//misc.
_translations['active Scheduler(s)']                       = 'Planificador(es) activo(s)';
_translations['exclusive Scheduler(s)']                    = 'Planificador(es) exclusivo(s)';
_translations['This Scheduler is active']                  = 'Este planificador est† activo';
_translations['and exclusive']                             = 'y exclusivo';
_translations['Only active Job Schedulers are allowed to start operation.'] = 'Solo planificadores de trabajo activos est†n autorizados a iniciar operacion.';
_translations['This Scheduler']                            = 'Este planificador';
_translations['(was active!)']                             = '(estaba activo!)';
_translations['active']                                    = 'activo';
_translations['inactive']                                  = 'inactivo';
_translations['distributed orders']                        = 'ordenes distribuidas';
_translations['exclusive']                                 = 'exclusivo';
_translations['backup']                                    = 'copia de seguridad';
_translations['still checking...']                         = 'checkeando...';
_translations['dead']                                      = 'muerto';
_translations['discovered']                                = 'descuvierto';
_translations['after']                                     = 'luego';
_translations['Deactivated by']                            = 'Desactivado por';
_translations['ago']                                       = 'hace';



/******************************************************************************
*                               REMOTE SCHEDULER                              *
******************************************************************************/
//some labels are already translated above

//Table header of process class list                       
_translations['IP']                                        = 'IP';
_translations['Hostname']                                  = 'Host';
_translations['Port']                                      = 'Puerto';
_translations['Connected']                                 = 'Conectado';
_translations['Disconnected']                              = 'Desconectado';
_translations['Version']                                   = 'Version';
                                                           
//misc.                                                    
_translations['Scheduler(s)']                              = 'Planificador de trabajo';
_translations['connected']                                 = 'conectado';



/******************************************************************************
*                                    LOCKS                                    *
******************************************************************************/
//some labels are already translated above
_translations['No locks found']                            = 'No han sido encontrado bloqueos';
_translations['Lock']                                      = 'Bloqueo';
                                                           
//Table header of process class list                       
_translations['non-exclusively locked']                    = 'no exclusivamente bloqueado';
_translations['locked']                                    = 'bloqueado';
_translations['free']                                      = 'libre';

//misc.
_translations['Holders (non-exclusive)']                   = 'Titulares de licencia no exclusivos';
_translations['Holders (exclusive)']                       = 'Titulares de licencia exclusivos';
_translations['Waiting jobs (exclusive)']                  = 'Trabajos en espera exclusivos';
_translations['Waiting jobs (non-exclusive)']              = 'Trabajos en espera no exclusivos';

//Mouseover-Title
_translations['Lock is not available, it is locked']       = 'Bloqueo no disponible, est† bloqueado';
_translations['Lock is available']                         = 'Bloqueo disponible';



/******************************************************************************
*                                LAST ACTIVITIES                              *
******************************************************************************/
//some labels are already translated above
//radio buttons, checkboxes
_translations['Show all']                                  = 'Mostrar todo';
_translations['Show only orders']                          = 'Mostrar solo ordenes';
_translations['Show only tasks']                           = 'Mostrar solo tareas';
_translations['Show only faultily tasks and orders']       = 'Mostrar solo tareas y ordenes defectuosas';
_translations['Show last tasks error']                     = 'Mostrar ultimos errores de tarea';

//Table header of job chains list                          
_translations['Order ID/']                                 = 'ID de orden/';
_translations['Job name']                                  = 'Nombre de trabajo';
_translations['Order state/']                              = 'Estado de orden/';
_translations['Exitcode']                                  = 'Codigo de salida';
_translations['Order ID']                                  = 'ID de orden';
_translations['Order state']                               = 'Estado de orden';

//Button title
_translations['Show order log']                            = 'Mostrar registro de ordenes';
_translations['Show task log']                             = 'Mostrar registro de tareas';

//misc.
_translations['No last activities found']                  = 'No han sido encontradas actividades pasadas';


/******************************************************************************
*                                   SCHEDULES                                 *
******************************************************************************/
//some labels are already translated above

//Menu content of schedules                               
_translations['Create schedule']                           = 'Crear planificacion';
_translations['Schedule menu']                             = 'Menu de planificacion';
_translations['Substitute menu']                           = 'Menu subtituto';
_translations['Add substitute']                            = 'A§adir substituto';
_translations['Edit schedule']                             = 'Editar planificacion';
_translations['Delete schedule']                           = 'Eliminar planificacion';
_translations['Do you really want to delete this scheduler?']  = 'Desea usted realmente eliminar este planificador?';

//Table header of job chains list                          
_translations['Schedule']                                  = 'Planificacion';
_translations['Valid from']                                = 'Valido desde';
_translations['Valid to']                                  = 'Valido hasta';
_translations['Substituted by']                            = 'Sustituido por';

//Mouseover-Title
_translations['show schedule details']                     = 'mostrar detalles de planificacion';

//misc.
_translations['Green']                                     = 'Verde';
_translations['marked schedules are currently active']     = 'planificaciones marcadas estan activas';
_translations['No schedules found']                        = 'No se han encontrado planificaciones';
_translations['Used by jobs']                              = 'Utilizado por trabajos';
_translations['Used by orders']                            = 'Utilizado por ordenes';
_translations['of job chain']                              = 'de cadena de trabajo';
_translations['Substituted schedule']                      = 'Planificacion reemplazada';

//State
_translations['incomplete']                                = 'incompleto';

//Detail frame                                             
_translations['SCHEDULE']                                  = 'PLANIFICACION';
_translations['Valid']                                     = 'VALIDO';


/******************************************************************************
*                                LOG CATEGORIES                               *
******************************************************************************/
//some labels are already translated above
_translations['LOG CATEGORIES']                            = 'CATEGORIAS DE REGISTRO';
                                                           
//misc.                                                    
_translations['The default log caregories are marked']     = 'Las categorias de registro predeterminadas est†n marcadas';
_translations['orange']                                    = 'naranjo';
_translations['and they are active after each reset.']     = 'y siguen activas despues de reiniciar.';
_translations['Current log categories setting']            = 'Configuraci¢n de categorias de registro actuales';
_translations['Set']                                       = 'Ajustar';
_translations['log categories for a duration of']          = 'Categorias de registro por una duracion de';
_translations['seconds']                                   = 'Segundos';
_translations['log categories are updated']                = 'Categorias de registro actualizadas';
_translations['reset is executed']                         = 'Categorias de registro seran reseteadas';
_translations['reset is executed after']                   = 'Categorias de registro seran reseteadas despuÇs';
_translations['Next reset']                                = 'Proximo reset';
_translations['implicit']                                  = 'implicito';
_translations['explicit']                                  = 'explicito';

//Table header of log categories list
_translations['Category']                                  = 'Categoria';
_translations['Mode']                                      = 'Modo';
_translations['Description']                               = 'Descripci¢n';

_translations['No log categories found']                   = 'No se han encontrado categorias de registro';


/******************************************************************************
*                                  CALENDAR                                   *
******************************************************************************/

_translations['CALENDAR']                                  = 'CALENDARIO';
_translations['job']                                       = 'trabajo';
_translations['job chain']                                 = 'cadena de trabajo';
_translations['of job chain']                              = 'de cadena de trabajo';
_translations['all jobs']                                  = 'todos los trabajos';
_translations['all jobs and orders']                       = 'todos los trabajos y ordenes';
_translations['Start times for']                           = 'Horas de inicio para';
_translations['by calling']                                = 'llamando';
_translations['with output format']                        = 'con el formato de salida';
_translations['at']                                        = 'a las';


/******************************************************************************
*                                  FILTER                                     *
******************************************************************************/

_translations['FILTER ADMINISTRATION']                     = 'ADMINISTRACION DE FILTROS';
_translations['close']                                     = 'cerrar';
_translations['store']                                     = 'guardar';
_translations['store as ...']                              = 'guardar como ...';
_translations['remove']                                    = 'eliminar';
_translations['quick check']                               = 'comprobacion rapida';
_translations['regular expression for quick check']        = 'expresion regular para comprobacion rapida';
_translations['select all']                                = 'seleccionar todo';
_translations['deselect all']                              = 'desseleccionar todo';
_translations['new filter']                                = 'nuevo filtro';
_translations['filter was stored']                         = 'el filtro fue guardado';
_translations['new filter was stored']                     = 'nuevo filtro fue guardado';
_translations['filter was removed']                        = 'filtro fue eliminado';
_translations['Filter name must be stated']                = 'Escriba un nombre para el filtro';
_translations['Using\n\n|\n=>\n;\n\nis not allowed']       = 'Usando\n\n|\n=>\n;\n\nno es permitido';
_translations['A filter "$filter_name" is already defined in the file $file.'] = 'Un filtro "$filter_name" ya esta definido en el archivo $file.';
_translations['The same filter name "$filter" was already defined\n\nDo you want nevertheless to store it ?'] = 'El mismo nombre de filtro "$filter" ya existe\n\ndesea sobreescribir?'; 


/******************************************************************************
*                                   ERROR                                     *
******************************************************************************/

_translations['source']                                    = 'fuente';
_translations['line']                                      = 'linea';
_translations['column']                                    = 'columna';
_translations['unknown error']                             = 'error desconocido';


/******************************************************************************
*                           DIALOGS OF MENU FUNCTIONS                         *
******************************************************************************/

//Buttons
_translations['submit']                                    = 'guardar';
_translations['submit and reload']                         = 'guardar y reiniciar';
_translations['cancel']                                    = 'cancelar';
_translations['new param']                                 = 'nuevo parametro';
_translations['run time editor']                           = 'editor de tiempo de ejecucion';

//Plausi
_translations['$field must be stated!']                    = '$field debe se§alarse!';
_translations['Period from ($field) is invalid date or before 1970-01-01.'] = 'El periodo de ($field) es una fecha invalida o antes de 01.01.1970.';
_translations['Max. hits ($field) is not a positive number.'] = 'Los Çxitos m†ximos ($field) no es un numero positivo.';
_translations['Please add an existing order job to the job chain nodes.'] = 'Por favor agregue una orden de trabajo existente a los nodos de cadena de trabajo.';
_translations['Please select an existing order job on the left hand side.'] = 'Por favor selecciones una orden de trabajo existente al lado izquierdo.';

//Plausi fields
_translations['Start time']                                = 'Hora de inicio';
_translations['Name']                                      = 'Nombre';

//Scheduler settings
_translations['The following values will be stored in a cookie. If no cookies are available the values which are set in <code>./custom.js</code> are used.'] = 'Los siguientes valores seran guardados en una cookie. Si no hay cookies disponibles los calores establecidos en <code>./custom.js</code> seran usados.';
_translations['Onload Values']                             = 'Valores de carga';
_translations['periodically every']                        = 'periodicamente todos';
_translations['inclusive &quot;<i>Hot Folders</i> &quot;'] = 'inclusiva &quot;<i>Hot Folders</i> &quot;';
_translations['Tabs']                                      = 'Etiquetas';
_translations['Switch to']                                 = 'Cambiar a';
_translations['as the beginning view']                     = 'ver desde el principio';
_translations['View Mode']                                 = 'Ver moda';
_translations['for']                                       = 'para';
_translations['Selects, Checkboxes and Radios']            = 'Selecciones, casillas de verificacion y radios';
_translations['in the <i>jobs</i> tab']                    = 'en la <i>Jobs</i>-Tab';
_translations['in the <i>job chains</i> tab']              = 'en la <i>Job-Ketten</i>-Tab';
_translations['in the job chain details']                  = 'en los <i>Job-Ketten</i>-detalles';
_translations['in the <i>last activities</i> tab']         = 'en las <i>Aktivit√§ten</i>-Etiquetas';
_translations['all orders and tasks']                      = 'todas las ordenes y tareas';
_translations['only tasks']                                = 'solo tareas';
_translations['only orders']                               = 'solo ordenes';
_translations['Runtime Values']                            = 'Valores de tiempo de ejecuci¢n';
_translations['Limits']                                    = 'Limites';
_translations['Max. number of orders per job chain']       = 'Max. numero de ordenes por cadena de trabajo';
_translations['Max. number of last activities']            = 'Max. numero de actividades pasadas';
_translations['Max. number of history entries per order']  = 'Max. numero de entradas en el historial por orden';
_translations['Max. number of history entries per task']   = 'Max. numero de entradas en el historial por tarea';
_translations['Termintate within']                         = 'Terminara dentro de';
_translations['Max. seconds within the Job Scheduler terminates'] = 'Max. Tiempo (Segundos) para terminar el planificador de trabajos';
_translations['Dialogs']                                   = 'Dialogos';
_translations['Default start time in the &quot;<i>Start task/order at</i> &quot;-Dialog is <i>now</i>'] = 'Hora de inicio por defecto en las &quot;<i>Comenzar Tarea/Orden a las...</i> &quot;-Dialog es <i>now</i>';
_translations['Debugging of the operations GUI']           = 'Depuracion de la GUI de operaciones';
_translations['Level']                                     = 'Nivel';

//start task/order
_translations['Start task $task']                          = 'Iniciar tarea $task';
_translations['Start order $order']                        = 'Iniciar orden $order';
_translations['<b>Enter a start time</b><span class="small"> in ISO format &quot;yyyy-mm-dd HH:MM[:SS] or &quot;now&quot;. The time at which a task is to be started &lt;<p align="left">run</p>_time&gt; is deactivated. Relative times - &quot;now + HH:MM[:SS]&quot; and &quot;now + SECONDS&quot; - are allowed.<span>' ] = '<b>Introduzca hora de inicio</b><span class="small"> im ISO-Format &quot;yyyy-mm-dd HH:MM[:SS] o &quot;now&quot;. Hora de inicio de &lt;run_time&gt;-Elements est† desactivada. Horas relativas - &quot;now + HH:MM[:SS]&quot; and &quot;now + SECONDS&quot; - son permitidas.<span>';
_translations['<b>Enter a run time</b> or use the $editor'] = 'Para horas de inicio use el campo de texto o use el $editor';
_translations['Start enforced']                            = 'Inicio forzado';
_translations['Change parameters']                         = 'Modificar parametros';
_translations['Declare parameters']                        = 'Establecer parametros';
_translations['Declare new parameters']                    = 'Establecer nuevo parametro';
_translations['name']                                      = 'Nombre';
_translations['value']                                     = 'Valor';


//add order
_translations['Add order to $job_chain']                   = 'A§ada orden a $job_chain';
_translations['Enter an order id']                         = 'Ingrese la id de la orden';
_translations['Enter an order title']                      = 'Ingrese titulo de la orden';
_translations['Select an order state']                     = 'Seleccione el estado de la orden';
_translations['Select an order end state']                 = 'Seleccione el estado final de la orden';
_translations['In order to store this order in a hot folder you have to state an order id.\nYour order will only be stored permanently, however, it is valid for\nthe lifetime of this Job Scheduler session. Do you want to continue?'] = 'Con el fin de almacenar esta orden en una carpeta activa tiene que establecer una id de orden. \ nSu orden solo se almacena de forma permanente, sin embargo, es valida \nde por vida para esta sesi¢n del programador de trabajo. Desea continuar?';

//set order state
_translations['Set order state of $order']                 = 'Establezca estado de $order';
_translations['<b>Select a new order state</b>']           = '<b>Seleccione un nuevo estado de orden.</b>';
_translations['<span class="small">The current order state is $state.</span>'] = '<span class="small">El estado actual es $state.</span>';
_translations['<b>Select a new order end state</b>']       = '<b>Seleccione un nuevo estado final de orden.</b>';
_translations['<span class="small">The current order end state is $state.</span>'] = '<span class="small">El estado final de orden actual es $state.</span>';

//set run time
_translations['Set run time of $job']                      = 'Establezca tiempo de ejecucion de $job';
_translations['or choose a schedule']                      = 'o elija una planificacion';

//schedules
_translations['Add schedule']                              = 'Agregar planificacion';
_translations['Add substitute for $schedule']              = 'Agregar reemplazo para $schedule';
_translations['Edit $schedule']                            = 'Editar $schedule';
_translations['Enter a schedule name']                     = 'Ingrese el nombre de la planificacion';

//calendar
_translations['Parameterize the start times list']         = 'Parametrizar la lista de horas de inicio';
_translations['<b>Set the period</b>']                     = '<b>Establezca el periodo</b>';
_translations['<span class="small">(Timestamps in ISO format yyyy-mm-dd[ hh:mm:ss])</span>']  = '<span class="small">(Fecha y hora en formato ISO yyyy-mm-dd[ hh:mm:ss])</span>';
_translations['...from']                                   = '...desde';
_translations['...to']                                     = '...hasta';
_translations['Max. hits']                                 = 'Max. exito';
_translations['Output format']                             = 'Formato de salida';
_translations['Include order start times']                 = 'Incluir horas de inicio de la orden';

//job chain
_translations['Sorry, but this feature is only supported for\nJob Scheduler version 2.0.204.5774 or higher'] = 'Lo sentimos, pero esta funcion es solamente soportada\ndesde la version 2.0.204.5774 en adelante.';
_translations['Modify job chain']                          = 'Modificar cadena de trabajo';
_translations['Now you can modify the job chain by editing the text area content.'] = 'Ahora puede modificar la cadena de trabajo editando el contenido en el area de texto.';
_translations['Orders are stored in the database (orders_recoverable)'] = 'Ordenes guardadas en la base de datos (orders_recoverable)';
_translations['Existing order jobs']                       = 'Ordenes de trabajo existentes';
_translations['Job chain nodes']                           = 'Nodos de cadenas de trabajo';
_translations['Enter a folder']                            = 'Ingrese una carpeta';
_translations['Enter a job chain name']                    = 'Ingrese nombre de cadena de trabajo';
_translations['Enter a job chain title']                   = 'Ingrese titulo de cadena de trabajo';


/******************************************************************************
*                           JOB DESCRIPTION                                   *
******************************************************************************/

_translations['The Job Scheduler $scheduler has no jobs with a description.']              = 'El planificador de trabajo $scheduler no tiene trabajos con  una descripci¢n.';
_translations['Because of a security sanction of your browser you must reload this site!'] = 'Debido a una sanci¢n de seguridad de su navegador usted debe volver a cargar este sitio!';
_translations['Please select a job to display its description.']                           = 'Seleccione un trabajo para ver su descripci¢n.';
_translations['Please enter a job name to display its description.']                       = 'Introduzca un nombre de trabajo para ver su descripci¢n.';

                           
