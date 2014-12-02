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
_translations['All rights reserved.']                      = 'Все права защищены.'
_translations['OK']                                        = 'OK'

//Tabs
_translations['Jobs']                                      = 'Jobs';
_translations['Job Chains']                                = 'Job-Цепочки';
_translations['Orders']                                    = 'Ордера';
_translations['Schedules']                                 = 'Schedules';
_translations['Process Classes']                           = 'Процесс-Классы';
_translations['Locks']                                     = 'Блокировки';
_translations['Cluster']                                   = 'Кластер';
_translations['Remote Schedulers']                         = 'Удалённый Scheduler';
_translations['Last Activities']                           = 'Последние действия';
                                                           
//Buttons menu, extras, update, hide          
_translations['Menu']                                      = 'Меню';
_translations['Extras']                                    = 'Сервис';
_translations['Help']                                      = 'Помощь';
_translations['Hide']                                      = 'Закрыть Детали';
_translations['Update']                                    = 'Обновить';

//Menu content of scheduler menu
_translations['Show log']                                  = 'Протокол';
_translations['Show job dependencies']                     = 'Job-Зависимости';
_translations['Show job chain dependencies']               = 'Job-Цепочки-Зависимости';
_translations['Show start times']                          = 'Стартовые интервалы';
_translations['Manage filters']                            = 'Настройки фильтров';
_translations['Manage log categories']                     = 'Протокол-Категории';
_translations['Pause']                                     = 'Приостановить работу';
_translations['Continue']                                  = 'Продолжить работу';
_translations['Terminate']                                 = 'Завершить работу';
_translations['Terminate within ~$secs']                   = 'Завершить работу в пределах ~$secs';
_translations['Terminate and restart']                     = 'Завершить работу с перезагрузкой';
_translations['Terminate and restart within ~$secs']       = 'Завершить работу в пределах ~$secs с перезагрузкой';
_translations['Abort immediately']                         = 'Немедленное завершение работы';
_translations['Abort immediately and restart']             = 'Немедленное завершение работы с перезагрузкой';
_translations['Terminate cluster']                         = 'Завершить работу кластер';
_translations['Terminate cluster within ~$secs']           = 'Завершить работу кластер в пределах ~$secs';
_translations['Terminate and restart cluster']             = 'Завершить работу кластер с перезагрузкой';
_translations['Terminate and restart cluster within ~$secs'] = 'Завершить работу кластер в пределах ~$secs с перезагрузкой';
_translations['Terminate fail-safe']                       = 'Завершить работу в безопасном режиме';
_translations['Terminate fail-safe within ~$secs']         = 'Завершить работу в безопасном режиме в пределах ~$secs';
                                                           
//Menu content of extras                      
_translations['Documentation']                             = 'Документация';
_translations['Forum']                                     = 'Форум';
_translations['Downloads']                                 = 'Загрузка';
_translations['Release Notes']                             = 'Заметки о Выпуске';
_translations['Follow us on Twitter']                      = 'Следуйте за нами на Twitter';
_translations['Settings']                                  = 'Настройки';
_translations['About']                                     = 'Насчёт';
_translations['Monitor']                                   = 'Монитор';
_translations['Configuration']                             = 'Конфигурация';
                                                           
//First line                                  
_translations['every $secs']                               = 'каждые $sec секунд';
_translations['Timezone']                                  = 'Часовой пояс;
_translations['local']                                     = 'местное';

                                                      
//Other lines                            
_translations['ID']                                        = 'ID';
_translations['State']                                     = 'Статус';
_translations['Pid']                                       = 'Pid';
_translations['Time']                                      = 'время';
_translations['jobs']                                      = 'Jobs';
_translations['need process']                              = 'ждут процесса';
_translations['tasks']                                     = 'Tasks';
_translations['orders']                                    = 'Ордера';
_translations['$cnt in cluster']                           = '$cnt в кластере';
_translations['Start Time']                                = 'время старта';

//Job Scheduler states
_translations['starting']                                  = 'запускается';
_translations['running']                                   = 'запущено';
_translations['paused']                                    = 'приостановлено';
_translations['stopping']                                  = 'останавливается';
_translations['stopping_let_run']                          = 'останавливается';
_translations['stopped']                                   = 'остановлено';
_translations['waiting_for_activation']                    = 'ждёт активизации';

//Misc.
_translations['unknown']                                   = 'неизвестно';
_translations['THIS IS A BACKUP...']                       = 'ЭТО BACKUP...';
_translations['Backup JobScheduler:']                      = 'Backup JobScheduler:';
_translations['JobScheduler is waiting for database ...']  = 'JobScheduler ждёт базу данных ...';
_translations['Waiting for response from JobScheduler ...'] = 'Ждётся ответ от JobScheduler ...';
_translations['No connection to JobScheduler']            = 'Связь с JobScheduler отсутствует';
_translations['Error at XML answer:']                      = 'Ошибка в XML-Ответе:';
_translations["Error at XSL answer '$xsl':"]               = "Ошибка в XSL-Ответе '$xsl':";
_translations["Error at HTTP answer '$url':"]              = "Ошибка в HTTP-Ответе '$url':";
_translations['$trial trial (of 5) to (re)connect to JobScheduler'] = '$trial попытка соединения (из 5) с JobScheduler';
_translations['First']                                     = 'Первая';
_translations['Second']                                    = 'Вторая';
_translations['Third']                                     = 'Третья';
_translations['Fourth']                                    = 'Четвёртая';
_translations['Last']                                      = 'Последняя';
_translations['never']                                     = 'никогда';
_translations['now']                                       = 'сейчас';
_translations['days']                                      = 'Дни';
_translations['The settings dialog is not available,\nbecause $file is used as settings file.'] = 'Диалог \'Настройки\' недоступен,\nтак как для Настроек необходим файл $file.'; 
_translations['You can enable the settings dialog \nvia the _enable_cookie_settings flag in the settings file'] = 'Вы можете активировать этот диалог используя настройку _enable_cookie_settings \n в вышеуказанном файле.';

//compact
_translations['from']                                      = 'от';
_translations['update']                                    = 'Обновить';
_translations['last update']                               = 'последнее обновление';
_translations['Scheduler is running since']                = 'время старта';


/******************************************************************************
*                                  JOBS/TASKS                                 *
******************************************************************************/
//some labels are already translated above
//Label of Checkboxes
_translations['Show tasks']                                = 'Tasks показать';

//Label and Options of Selectbox
_translations['All jobs']                                  = 'Все Jobs';
_translations['Standalone jobs']                           = 'Standalone-Jobs';
_translations['Order jobs']                                = 'Ордер-Jobs';
_translations['with state']                                = 'со статусом';
_translations['(all)']                                     = '(все)';
_translations['(other)']                                   = '(другие)';
_translations['running or stopped']                        = 'запущено или остановлено';
_translations['and with process class']                    = 'и с Процесс-Классом';
                                                           
//Label and Options in Selectbox of jobs sort                        
_translations['Sort jobs by']                              = 'Jobs сортировать';
_translations['(unsorted)']                                = '(не сортировать)';
_translations['name in ascending order']                   = 'Имя (по восходящей)';
_translations['name in descending order']                  = 'Имя (по нисходящей)';
_translations['next start time in ascending order']        = 'Следующее время старта (по восходящей)';
_translations['next start time in descending order']       = 'Следующее время старта (по нисходящей)';
_translations['state in ascending order']                  = 'Статус (по восходящей)';
_translations['state in descending order']                 = 'Статус (по нисходящей)';
                                                           
//Title of images
_translations['toggle to']                                 = 'Перейти к'; 
_translations['list view']                                 = 'Список'; 
_translations['tree view']                                 = 'Древовидная структура'; 
_translations['open all folders']                          = 'Все папки открыть';   
_translations['close all folders']                         = 'Все папки закрыть';   

//Label and Options of Selectbox of jobs filtering                     
_translations['Filter jobs by']                            = 'Jobs фильтровать';
_translations['(no filtering)']                            = '(без фильтра)';
                                                           
//Table header of jobs list                                
_translations['Job']                                       = 'Job';
_translations['Steps']                                     = 'Шаги';
_translations['Process steps']                             = 'Процесса шаги';
_translations['Next start']                                = 'Следующий старт';
                                                           
//Labels in detail frame                                   
_translations['JOB']                                       = 'JOB';
_translations['TASK']                                      = 'TASK';
_translations['File timestamp']                            = 'Файл-Дата';
_translations['State text']                                = 'Статус-Текст';
_translations['Error']                                     = 'Ошибка';
_translations['Error in configuration file']               = 'Ошибка в конфигурационном файле';
_translations['Error in changed file']                     = 'Ошибка в изменённом файле';
_translations['(not loaded)']                              = '(не загружено)';
_translations['Removing delayed']                          = 'Удаление задержать';
_translations['Tasks']                                     = 'Tasks';
_translations['Run time defined by']                       = 'Время старта установлено в';
_translations['orders to process']                         = 'Ордера к обработке';
_translations['Mail subject']                              = 'Email-Тема';
_translations['To']                                        = 'Кому';
_translations['CC']                                        = 'Копия';
_translations['BCC']                                       = 'Скрытая копия';
_translations['From']                                      = 'От';
_translations['SMTP']                                      = 'SMTP';
_translations['Last error']                                = 'Последняя ошибка';
_translations['Last warning']                              = 'Последнее предупреждение';
_translations['Log level']                                 = 'Log-Level';
_translations['mail on error']                             = 'Сообщение при ошибках';
_translations['mail on warning']                           = 'Сообщение при предупреждениях';
_translations['mail on success']                           = 'Сообщение при успехе';
_translations['mail on process']                           = 'Сообщение про Процесс-Шаг';
_translations['start cause']                               = 'Причина';
_translations['waiting for order']                         = 'ждёт Ордер';
_translations['Idle since']                                = 'Бездействует с';
_translations['In process since']                          = 'В процессе с';
_translations['Running since']                             = 'запущено с';
_translations['Enqueued at']                               = 'В очереди с';
_translations['Subprocesses']                              = 'Суб-процессы';
                                                           
//Task queue                                               
_translations['enqueued tasks']                            = 'Tasks в очереди';
_translations['Id']                                        = 'ID';
_translations['Enqueued']                                  = 'Допущено';
_translations['Start at']                                  = 'Запущено';
_translations['Delete']                                    = 'Удалить';
                                                           
                                                           
//Task history                                             
_translations['No tasks in the history']                   = 'Tasks не обнаружены в истории';
_translations['Started']                                   = 'Запущено';
_translations['Cause']                                     = 'Причина';
_translations['Ended']                                     = 'Закончено';
_translations['Duration']                                  = 'Период';

//Order queue                                              
_translations['Start']                                     = 'Запущено';
                                                           
                                                           
//Title of Tabs                                            
_translations['Task Queue']                                = 'Task-Очередь';
_translations['Task History']                              = 'Task-История';
_translations['Order Queue']                               = 'Ордер-Очередь';
                                                           
                                                           
//Menu content of job menu                                 
_translations['Job menu']                                  = 'Job-Меню';
_translations['Show configuration']                        = 'показать конфигурации';
_translations['Show description']                          = 'Описание';
_translations['Show documentation']                        = 'Документация';
_translations['Show dependencies']                         = 'Зависимости';
_translations['Start task immediately']                    = 'Стартовать Task немедленно';
_translations['Start task unforced now']                   = 'Стартовать Task в следующем периоде';
_translations['Start task at']                             = 'Стартовать Task в...';
_translations['Start task parametrized']                   = 'Стартовать Task с параметрами';
_translations['Set run time']                              = 'Установить время старта';
_translations['Stop']                                      = 'Остановить работу';
_translations['Unstop']                                    = 'Отменить остановку';
_translations['Reread']                                    = 'Скрипт загрузить заново';
_translations['End tasks']                                 = 'Tasks закончить';
_translations['Suspend tasks']                             = 'Tasks приостановить';
_translations['Continue tasks']                            = 'Tasks продолжить';
_translations['Delete job']                                = 'Job удалить';
                                                           
                                                           
//Menu content of task menu                                
_translations['Task menu']                                 = 'Task-Меню';
_translations['End']                                       = 'Закончить работу';
_translations['Kill immediately']                          = 'Прекратить работу немедленно';
                                                           
                                                           
//Mouseover-Title                                          
_translations['show job details']                          = 'показать Job-Детали';
_translations['show task details']                         = 'показать Task-Детали';
_translations['show schedule details']                     = 'показать Schedule-Детали';
_translations['Order is deleted']                          = 'Ордер удалён';
_translations['Order is replaced']                         = 'Ордер заменён';
                                                           
//misc.                          
_translations['Without start time']                        = 'без времени старта';
_translations['Task']                                      = 'Task';
_translations['No jobs found']                             = 'Jobs не найдены';
                                                           
                                                           
//Job/Task states                                          
_translations['pending']                                   = 'ожидающе';
_translations['loaded']                                    = 'загружено';
_translations['initialized']                               = 'инициализировано';
_translations['not_initialized']                           = 'не инициализировано';
_translations['disabled']                                  = 'деактивировано';
_translations['none']                                      = 'неизвестно';
_translations['read_error']                                = 'скрипт нечитаем';
_translations['delayed after error']                       = 'задержано после ошибки';
_translations['needs process']                             = 'ждёт процесс';
_translations['needs lock']                                = 'заблокировано';
_translations['loading']                                   = 'загружается';
_translations['waiting_for_process']                       = 'ждёт процесс';
_translations['opening']                                   = 'запущено';
_translations['opening_waiting_for_locks']                 = 'заблокировано';
_translations['running_process']                           = 'запущено';
_translations['running_remote_process']                    = 'запущено';
_translations['running_waiting_for_order']                 = 'ждёт Ордер';
_translations['running_waiting_for_locks']                 = 'заблокировано';
_translations['running_delayed']                           = 'запускается с задержкой';
_translations['suspended']                                 = 'приостановлено';
_translations['ending_waiting_for_subprocesses']           = 'ждёт Суб-Процесса';
_translations['ending']                                    = 'заканчивается';
_translations['on_success']                                = 'заканчивается';
_translations['on_error']                                  = 'заканчивается';
_translations['exit']                                      = 'заканчивается';
_translations['error']                                     = 'с ошибками';
_translations['release']                                   = 'заканчивается';
_translations['ended']                                     = 'закончено';
_translations['deleting_files']                            = 'StdIn, StdOut и StdErr удаляются';
_translations['closed']                                    = 'закончено';
_translations['Replacement is standing by']                = 'Замена приостановлена';
_translations['changed file has error']                    = 'Ошибка в изменённом файле';
_translations['not an order job']                          = 'не Ордер-Job';
_translations['is incorrect or missing']                   = 'неверна или отсутствует';
_translations['non-excl.']                                 = 'non-excl.';
_translations['is being replaced']                         = 'заменено';
_translations['undefined']                                 = 'неопределено';


                                                           
//Task start causes                                        
_translations['none']                                      = 'неопределено';
_translations['period_once']                               = 'Новый старт';
_translations['period_single']                             = 'Время старта';
_translations['period_repeat']                             = 'Интервал';
_translations['queue']                                     = 'ожидает';
_translations['queue_at']                                  = 'ожидает';
_translations['directory']                                 = 'Папка';
_translations['delay_after_error']                         = 'Задержка';
_translations['order']                                     = 'Ордер';
_translations['wake']                                      = 'разбужено';                                                     


/******************************************************************************
*                                 JOB CHAINS                                  *
******************************************************************************/
//some labels are already translated above
//Label of Checkboxes and Selectboxes
_translations['Job chains containing jobs']                = 'Job-Цепочки с Jobs';
_translations['Show order history']                        = 'Показать Ордер-Историю';
_translations['Show jobs']                                 = 'Показать Jobs';
_translations['Show orders']                               = 'Показать Ордера';
                                                           
//Label of Selectbox of job chains sort                    
_translations['Sort job chains by']                        = 'Job-Цепочки сортировать';
                                                           
//Label of Selectbox of job chains filtering               
_translations['Filter job chains by']                      = 'Job-Цепочки фильтровать';
                                                           
//Table header of job chains list                          
_translations['Order state']                               = 'Ордер-Статус';
_translations['Job chain']                                 = 'Job-Цепочка';
_translations['Job state']                                 = 'Job-Статус';
                                                           
//Table header in detail frame                             
_translations['JOB CHAIN']                                 = 'JOB-ЦЕПОЧКА';
_translations['ORDER HISTORY']                             = 'ОРДЕР-ИСТОРИЯ';
                                                           
//Menu content of job chain menu                           
_translations['Create job chain']                          = 'Новая Job-Цепочка';
_translations['Job chain menu']                            = 'Job-Цепочки-Меню';
_translations['Job node menu']                             = 'Job-Узел-Меню';
_translations['Add order']                                 = 'Добавить Ордер';
_translations['Add persistent order']                      = 'Добавить персистентный Ордер';
_translations['Delete job chain']                          = 'Job-Цепочку удалить';
_translations['Skip']                                      = 'Перепрыгнуть';
_translations['Unskip']                                    = 'Добавить';
_translations['Skip node']                                 = 'Узел перепрыгнуть';
_translations['Stop node']                                 = 'Узел остановить';
_translations['Stop job']                                  = 'Job остановить';
_translations['Unstop job']                                = 'Job продолжить';

//Mouseover-Title
_translations['show job chain details']                    = 'показать Job-Цепочка-Детали';

//job chain (node) states
_translations['under_construction']                        = 'незавершено';
_translations['finished']                                  = 'закончено';
_translations['removing']                                  = 'удаляется';
_translations['Node is stopped']                           = 'Узел остановлен';
_translations['Node is skipped']                           = 'Узел перепрыгнут';

//misc.                                                          
_translations['No job chains found']                       = 'Job-Цепочки не найдены';
_translations['Web service']                               = 'Web-Service';
_translations['file orders']                               = 'Файл-Ордера';
_translations['pattern']                                   = 'Образец';
_translations['delay']                                     = 'Задержка';
_translations['repeat']                                    = 'Повтор';
_translations['blacklist']                                 = 'Blacklist';

/******************************************************************************
*                         ORDERS/ORDERQUEUE/BLACKLIST                         *
******************************************************************************/
//some labels are already translated above
//Label of Selectbox of orders sort
_translations['Sort orders by']                            = 'Ордера сортировать';
                                                           
//Options in Selectbox of orders sort                      
_translations['job chain in ascending order']              = 'Job-Цепочка (по восходящей)';
_translations['job chain in descending order']             = 'Job-Цепочка (по нисходящей)';
                                                           
//Label of Selectbox of jobs filtering                     
_translations['Filter orders by']                          = 'Ордера фильтровать';
                                                                                                                                                                                 
//Mouseover-Title                                          
_translations['Order is processed by Scheduler member']    = 'Ордер обрабатывается Scheduler';
_translations['show order details']                        = 'показать Ордер-Детали';
_translations['This order is a replacement for another order with the same ID'] = 'Этот Ордер заменяет другой Ордер с таким же ID';
                                                           
//Detail frame                                             
_translations['ORDER']                                     = 'ОРДЕР';
                                                                                                                     
//Menu content of order menu                               
_translations['Order menu']                                = 'Ордер-Меню';
_translations['Start order now']                           = 'Стартовать Ордер немедленно';
_translations['Start order at']                            = 'Стартовать Ордер в...';
_translations['Start order parametrized']                  = 'Стартовать Ордер с параметрами';
_translations['Set order state']                           = 'Установить статус';
_translations['Suspend order']                             = 'Ордер приостановить';
_translations['Resume order']                              = 'Ордер продолжить';
_translations['Reset order']                               = 'Ордер сбросить';
_translations['Delete order']                              = 'Ордер удалить';
_translations['Remove setback']                            = 'Задержку удалить';

//misc
_translations['No orders found']                           = 'Ордера не найдены';
_translations['Processed by']                              = 'обработано';
_translations['Order']                                     = 'Ордер';
_translations['Setback']                                   = 'Задержка';
_translations['deleted']                                   = 'удалено';
_translations['Replacement']                               = 'Замена';
_translations['currently processed by']                    = 'в настоящее время обрабатывается'
_translations['on blacklist']                              = 'в Blacklist'
                                                           

                                                           
//order history                                            
_translations['No orders in the history']                  = 'Ордера в Истории не найдены';


/******************************************************************************
*                                PROCESS CLASS                                *
******************************************************************************/
//some labels are already translated above
_translations['Process class']                             = 'Процесс-Классы';
                                                           
//Table header of process class list                       
_translations['Operations']                                = 'Операции';
_translations['Callbacks']                                 = 'Callbacks';
_translations['Current operation']                         = 'текущая операция';
                                                           
_translations['(default)']                                 = '(по умолчанию)';
_translations['max processes']                             = 'макс. процессов';
_translations['Remote Scheduler']                          = 'Удалённый Scheduler';

_translations['No process classes found']                  = 'Процесс-Классы не найдены';
                                                           

/******************************************************************************
*                                   CLUSTER                                   *
******************************************************************************/
//some labels are already translated above
//Table header of process class list
_translations['Last heart beat']                           = 'Последний удар сердца';
_translations['Detected heart beats']                      = 'Обнаруженные удары сердца';
_translations['Backup precedence']                         = 'Backup-Приоритет';
                                                           
//Menu content of cluster member                           
_translations['Cluster member menu']                       = 'Кластер-Меню';
_translations['Delete entry']                              = 'удалить';
_translations['Restart']                                   = 'Стартовать заново';

//misc.
_translations['active Scheduler(s)']                       = 'active Scheduler(s)';
_translations['exclusive Scheduler(s)']                    = 'exclusive Scheduler(s)';
_translations['This Scheduler is active']                  = 'Этот Scheduler активен';
_translations['and exclusive']                             = 'и exclusive';
_translations['Only active JobSchedulers are allowed to start operation.'] = 'Только активным JobScheduler разрешается запуск Операций.';
_translations['This Scheduler']                            = 'Этот Scheduler';
_translations['(was active!)']                             = '(был активный!)';
_translations['active']                                    = 'активный';
_translations['inactive']                                  = 'неактивный';
_translations['distributed orders']                        = 'распределенные Ордера';
_translations['exclusive']                                 = 'exclusive';
_translations['backup']                                    = 'Backup';
_translations['still checking...']                         = 'идёт проверка...';
_translations['dead']                                      = 'мёртвый';
_translations['discovered']                                = 'обнаружено';
_translations['after']                                     = 'после';
_translations['Deactivated by']                            = 'дезактивировано';
_translations['ago']                                       = 'прошло';



/******************************************************************************
*                               REMOTE SCHEDULER                              *
******************************************************************************/
//some labels are already translated above

//Table header of process class list                       
_translations['IP']                                        = 'IP';
_translations['Hostname']                                  = 'Хост';
_translations['Port']                                      = 'Порт';
_translations['Last Update']                               = 'последнее обновление';
_translations['Connected']                                 = 'Соединён';
_translations['Disconnected']                              = 'Разъединён';
_translations['Version']                                   = 'Версия';
                                                           
//misc.                                                    
_translations['Scheduler(s)']                              = 'JobScheduler';
_translations['connected']                                 = 'соединён';



/******************************************************************************
*                                    LOCKS                                    *
******************************************************************************/
//some labels are already translated above
_translations['No locks found']                            = 'Блокировки не найдены';
_translations['Lock']                                      = 'Блокировка';
                                                           
//Table header of process class list                       
_translations['non-exclusively locked']                    = 'заблокировано non-exclusively';
_translations['locked']                                    = 'заблокировано';
_translations['free']                                      = 'свободно';

//misc.
_translations['Holders (non-exclusive)']                   = 'Владельцы (non-exclusive)';
_translations['Holders (exclusive)']                       = 'Владельцы (exclusive)';
_translations['Waiting jobs (exclusive)']                  = 'Ждущие Jobs (exclusive)';
_translations['Waiting jobs (non-exclusive)']              = 'Ждущие Jobs (non-exclusive)';

//Mouseover-Title
_translations['Lock is not available, it is locked']       = 'Блокировка недоступна, она заблокирована';
_translations['Lock is available']                         = 'Блокировка доступна';



/******************************************************************************
*                                LAST ACTIVITIES                              *
******************************************************************************/
//some labels are already translated above
//radio buttons, checkboxes
_translations['Show all']                                  = 'Показать все';
_translations['Show only orders']                          = 'Показать только Ордера';
_translations['Show only tasks']                           = 'Показать только Tasks';
_translations['Show only faulty tasks and orders']         = 'Показать только Tasks/Ордера с ошибками';
_translations['Show last tasks error']                     = 'Показать последие Task-Ошибки';

//Table header of job chains list                          
_translations['Job name']                                  = 'Job-Имя';
_translations['Exitcode']                                  = 'Exitcode';
_translations['Order ID']                                  = 'Ордер-ID';
_translations['Order state']                               = 'Ордер-Статус';

//Button title
_translations['Show order log']                            = 'Ордер-Протокол';
_translations['Show task log']                             = 'Job-Протокол';

//misc.
_translations['No last activities found']                  = 'Последние действия не найдены';


/******************************************************************************
*                                   SCHEDULES                                 *
******************************************************************************/
//some labels are already translated above

//Menu content of schedules                               
_translations['Create schedule']                           = 'Новый Schedule';
_translations['Schedule menu']                             = 'Schedule-Меню';
_translations['Substitute menu']                           = 'Субститут-Меню';
_translations['Add substitute']                            = 'Субститут добавить';
_translations['Edit schedule']                             = 'Schedule редактировать';
_translations['Delete schedule']                           = 'Schedule удалить';

//Table header of job chains list                          
_translations['Schedule']                                  = 'Schedule';
_translations['Valid from']                                = 'действителен от';
_translations['Valid to']                                  = 'действителен по';
_translations['Substituted by']                            = 'Заменён';

//Mouseover-Title
_translations['show schedule details']                     = 'показать Schedule-Детали';

//misc.
_translations['Green']                                     = 'Зелёным';
_translations['marked schedules are currently active']     = 'отмеченные Schedules в данный момент активны';
_translations['No schedules found']                        = 'Schedules не найдены';
_translations['Used by jobs']                              = 'Используется в Jobs';
_translations['Used by orders']                            = 'Используется в Ордерах';
_translations['of job chain']                              = 'Job-Цепочки';
_translations['Substituted schedule']                      = 'Замещенный Schedule';

//State
_translations['incomplete']                                = 'неполно';

//Detail frame                                             
_translations['SCHEDULE']                                  = 'SCHEDULE';
_translations['Valid']                                     = 'Действителен';


/******************************************************************************
*                                LOG CATEGORIES                               *
******************************************************************************/
//some labels are already translated above
_translations['LOG CATEGORIES']                            = 'ПРОТОКОЛ КАТЕГОРИИ';
                                                           
//misc.                                                    
_translations['The default log caregories are marked']     = 'Предварительно установленные Протокол-Категории';
_translations['orange']                                    = 'оранжевый';
_translations['and they are active after each reset.']     = 'отмечены и после каждого сброса становятся снова активными.';
_translations['Current log categories setting']            = 'Протокол-Категории-Настройки на данный момент';
_translations['Set']                                       = 'Установить';
_translations['log categories for a duration of']          = 'Протокол-Категории на период по';
_translations['seconds']                                   = 'секунд(ы)';
_translations['log categories are updated']                = 'Протокол-Категории актуализированы';
_translations['reset is executed']                         = 'Протокол-Категории сброшены';
_translations['reset is executed after']                   = 'Протокол-Категории сброшены на';
_translations['Next reset']                                = 'Следующий сброс';
_translations['implicit']                                  = 'имплицитно';
_translations['explicit']                                  = 'точно';

//Table header of log categories list
_translations['Category']                                  = 'Категория';
_translations['Mode']                                      = 'Модус';
_translations['Description']                               = 'Описание';

_translations['No log categories found']                   = 'Протокол-Категории не найдены';


/******************************************************************************
*                                  CALENDAR                                   *
******************************************************************************/

_translations['CALENDAR']                                  = 'Календарь';
_translations['job']                                       = 'Job';
_translations['job chain']                                 = 'Job-Цепочка';
_translations['of job chain']                              = 'Job-Цепочки';
_translations['all jobs']                                  = 'все Jobs';
_translations['all jobs and orders']                       = 'все Jobs и Ордера';
_translations['Start times for']                           = 'Время старта для';
_translations['by calling']                                = 'при вызове';
_translations['with output format']                        = 'в формате выдачи';
_translations['at']                                        = 'в';


/******************************************************************************
*                                  FILTER                                     *
******************************************************************************/

_translations['FILTER ADMINISTRATION']                     = 'НАСТРОЙКИ ФИЛЬТРОВ';
_translations['close']                                     = 'Закрыть';
_translations['store']                                     = 'Сохранить';
_translations['store as ...']                              = 'Сохранить как ...';
_translations['remove']                                    = 'Удалить';
_translations['quick check']                               = 'Быстрый выбор';
_translations['regular expression for quick check']        = 'Регулярное выражение для быстрого выбора';
_translations['select all']                                = 'Выбрать все';
_translations['deselect all']                              = 'Удалить все';
_translations['new filter']                                = 'Новый фильтр';
_translations['filter was stored']                         = 'Фильтр актуализирован';
_translations['new filter was stored']                     = 'Фильтр создан';
_translations['filter was removed']                        = 'Фильтр удалён';
_translations['Filter name must be stated']                = 'Фильтр не задан';
_translations['Using\n\n|\n=>\n;\n\nis not allowed']       = 'Использование знаков\n\n|\n=>\n;\n\nне разрешено';
_translations['A filter "$filter_name" is already defined in the file $file.'] = 'Фильтр "$filter_name" уже указан в файле $file.';
_translations['The same filter name "$filter" was already defined\n\nDo you want nevertheless to store it ?'] = 'Фильтр "$filter" уже существует\n\nВсё равно сохранить?'; 


/******************************************************************************
*                                   ERROR                                     *
******************************************************************************/

_translations['source']                                    = 'Файл';
_translations['line']                                      = 'Строка';
_translations['column']                                    = 'Колонка';
_translations['unknown error']                             = 'неизвестная ошибка';


/******************************************************************************
*                           DIALOGS OF MENU FUNCTIONS                         *
******************************************************************************/

//Buttons
_translations['submit']                                    = 'Сохранить';
_translations['cancel']                                    = 'Отменить';
_translations['new param']                                 = 'Новый параметр';
_translations['run time editor']                           = 'Runtime-Editor';

//Plausi
_translations['$field must be stated!']                    = '$field должен быть задан!';
_translations['Period from ($field) is invalid date or before 1970-01-01.'] = 'Дата ($field) недействительна или старше чем 01.01.1970.';
_translations['Max. hits ($field) is not a positive number.'] = 'Максимальное количество попадания ($field) - не положительное число.';
_translations['Please add an existing order job to the job chain nodes.'] = 'Пожалуйста добавьте к Job-Цепочка-Узлу существующий Ордер-Job.';
_translations['Please select an existing order job on the left hand side.'] = 'Пожалуйста выберите слева существующий Ордер-Job.';

//Plausi fields
_translations['Start time']                                = 'Стартовая дата';
_translations['Name']                                      = 'Имя';

//Scheduler settings
_translations['The following values will be stored in a cookie. If no cookies are available the values which are set in <code>./custom.js</code> are used.'] = 'Последующие настройки сохраняются в Cookie. Если Cookies недоступны, все значения будут перениматься из <code>./custom.js</code>.';
_translations['Onload Values']                             = 'Настройки при загрузке';
_translations['periodically every']                        = 'периодически все';
_translations['Tabs']                                      = 'Tabs';
_translations['Switch to']                                 = 'В начале Tab';
_translations['as the beginning view']                     = 'показать';
_translations['View Mode']                                 = 'Вид показа';
_translations['for']                                       = 'для';
_translations['Selects, Checkboxes and Radios']            = 'Selects, Checkboxen и Radios';
_translations['in the <i>jobs</i> tab']                    = 'в <i>Jobs</i>-Tab';
_translations['in the <i>job chains</i> tab']              = 'в <i>Job-Цепочка</i>-Tab';
_translations['in the job chain details']                  = 'в <i>Job-Цепочка</i>-Детали';
_translations['in the <i>last activities</i> tab']         = 'в <i>Действия</i>-Tab';
_translations['all orders and tasks']                      = 'все Ордера и Tasks';
_translations['only tasks']                                = 'только Tasks';
_translations['only orders']                               = 'только Ордера';
_translations['Runtime Values']                            = 'Настройки во время действия';
_translations['Limits']                                    = 'Ограничения';
_translations['Max. number of orders per job chain']       = 'Макс. количество Ордеров в Job-Цепочке';
_translations['Max. number of last activities']            = 'Макс. количество последних действий';
_translations['Max. number of history entries per order']  = 'Макс. количество записей в Ордер';
_translations['Max. number of history entries per task']   = 'Макс. количество записей в Task';
_translations['Termintate within']                         = 'Закончить в пределах';
_translations['Max. seconds within the JobScheduler terminates'] = 'Макс. период (секунды), в пределах которого заканчивает свою работу JobScheduler';
_translations['Dialogs']                                   = 'Диалоги';
_translations['Default start time in the &quot;<i>Start task/order at</i> &quot;-Dialog is <i>now</i>'] = 'По умолчанию установленное время старта в &quot;<i>Start Task/Ордер в...</i> &quot;-Диалог <i>сейчас</i>';
_translations['Debugging of the operations GUI']           = 'Debugging Operations GUI';
_translations['Level']                                     = 'Level';

//start task/order
_translations['Start task $task']                          = 'Стартовать Task $task';
_translations['Start order $order']                        = 'Стартовать Ордер $order';
_translations['<b>Enter a start time</b><span class="small"> in ISO format &quot;yyyy-mm-dd HH:MM[:SS]&quot; or &quot;now&quot;. The time at which a task is to be started &lt;run_time&gt; is deactivated. Relative times - &quot;now + HH:MM[:SS]&quot; and &quot;now + SECONDS&quot; - are allowed.<span>' ] = '<b>Ввод времени старта</b><span class="small"> в формате ISO &quot;yyyy-mm-dd HH:MM[:SS] или &quot;now&quot;. Времена старта &lt;run_time&gt;-Елемента недействительны. Относительные задачи времени - &quot;now + HH:MM[:SS]&quot; и &quot;now + SECONDS&quot; - возможны.<span>';
_translations['<b>Enter a run time</b> or use the $editor'] = 'Для задачи времени старта используйте текстовое поле или $editor';
_translations['Start enforced']                            = ' принужденный старт';
_translations['Change parameters']                         = 'Параметр изменить';
_translations['Declare parameters']                        = 'Параметр задать';
_translations['Declare new parameters']                    = 'Задать новый параметр';
_translations['name']                                      = 'Имя';
_translations['value']                                     = 'Значение';


//add order
_translations['Add order to $job_chain']                   = 'Добавить Ордер к $job_chain';
_translations['Enter an order id']                         = 'Id Ордера';
_translations['Enter an order title']                      = 'Название Ордера';
_translations['Select an order state']                     = 'Статус Ордера';
_translations['Select an order end state']                 = 'Конечный статус Ордера';
_translations['In order to store this order in a hot folder you have to state an order id.\nYour order will only be stored permanently, however, it is valid for\nthe lifetime of this JobScheduler session. Do you want to continue?'] = 'Для передачи Ордера в Hot Folder нужно указать Ордер-ID\n.Иначе этот Ордер действителен только в течении актуальной JobScheduler Session.\nПродолжить?';

//set order state
_translations['Set order state of $order']                 = 'Задать статус для $order';
_translations['<b>Select a new order state</b>']           = '<b>Выбрать Ордер-Статус.</b>';
_translations['<span class="small">The current order state is $state.</span>'] = '<span class="small">Актуальный статус $state.</span>';
_translations['<b>Select a new order end state</b>']       = '<b>Выбрать новый конечный Ордер-Статус.</b>';
_translations['<span class="small">The current order end state is $state.</span>'] = '<span class="small">Актуальный конечный Ордер-Статус $state.</span>';

//set run time
_translations['Set run time of $job']                      = 'Задать время старта для $job';
_translations['or choose a schedule']                      = 'или Schedule выбрать';

//schedules
_translations['Add schedule']                              = 'Новый Schedule';
_translations['Add substitute for $schedule']              = 'Субститут для $schedule';
_translations['Edit $schedule']                            = 'Правка $schedule';
_translations['Enter a schedule name']                     = 'Имя Schedules';

//calendar
_translations['Parameterize the start times list']         = 'Задать параметры для опроса времён старта';
_translations['<b>Set the period</b>']                     = '<b>Задать период</b>';
_translations['<span class="small">(Timestamps in ISO format yyyy-mm-dd[ hh:mm:ss])</span>']  = '<span class="small">(указание даты в формате ISO yyyy-mm-dd[ hh:mm:ss])</span>';
_translations['...from']                                   = '...с';
_translations['...to']                                     = '...до';
_translations['Max. hits']                                 = 'Макс. количество';
_translations['Output format']                             = 'Формат выдачи';
_translations['Include order start times']                 = 'Включая Ордер-времена старта';

//job chain
_translations['Sorry, but this feature is only supported for\nJobScheduler version 2.0.204.5774 or higher'] = 'К сожалению эта Функция доступна только в\nJobScheduler 2.0.204.5774.';
_translations['Modify job chain']                          = 'Job-Цепочку изменить';
_translations['Now you can modify the job chain by editing the text area content.'] = 'Обработайте текстовое содержание поля, если необходимы изменения в Job-Цепочке.';
_translations['Orders are stored in the database (orders_recoverable)'] = 'Ордер сохранить в базе данных (orders_recoverable)';
_translations['Existing order jobs']                       = 'Существующие Ордер-Jobs';
_translations['Job chain nodes']                           = 'Job-Цепочка-Узел';
_translations['Enter a folder']                            = 'Папка';
_translations['Enter a job chain name']                    = 'Имя Job-Цепочки';
_translations['Enter a job chain title']                   = 'Название Job-Цепочки';


/******************************************************************************
*                           JOB DESCRIPTION                                   *
******************************************************************************/

_translations['The JobScheduler $scheduler has no jobs with a description.']               = 'У этого JobScheduler $scheduler не имеется описания Jobs.';
_translations['Because of a security sanction of your browser you must reload this site!'] = 'Из-за защитной санкции Вашего браузера страницу нужно вызвать заново!';
_translations['Please select a job to display its description.']                           = 'Выберети пожалуйста Joб для показа его описания.';
_translations['Please enter a job name to display its description.']                       = 'Для показа описания задайте пожалуйста Job-Имя.';


/******************************************************************************
*                           CONFIRMS                                          *
******************************************************************************/

_translations['Do you really want to terminate the JobScheduler?']          = 'Вы действительно хотите завершить работу JobScheduler?';
_translations['Do you really want to restart the JobScheduler?']            = 'Вы действительно хотите перезагрузить работу JobScheduler?';
_translations['Do you really want to abort the JobScheduler?']              = 'Вы действительно хотите прервать работу JobScheduler?';
_translations['Do you really want to pause the JobScheduler?']              = 'Вы действительно хотите приостановить работу JobScheduler?';
_translations['Do you really want to continue the JobScheduler?']           = 'Вы действительно хотите продолжить работу JobScheduler?';
_translations['Do you really want to terminate the JobScheduler cluster?']  = 'Вы действительно хотите завершить работу JobScheduler кластер?';
_translations['Do you really want to restart the JobScheduler cluster?']    = 'Вы действительно хотите перезагрузить работу JobScheduler кластер?';
_translations['Do you really want to delete the dead entry?']               = 'Вы действительно хотите удалить мёртвую запись?';

_translations['Do you really want to stop this job chain?']                 = 'Вы действительно хотите остановить эту Job-Цепочку?';
_translations['Do you really want to unstop this job chain?']               = 'Вы действительно хотите остановить эту Job-Цепочку?';
_translations['Do you really want to delete this job chain?']               = 'Вы действительно хотите продолжить работу этой Job-Цепочки?';
_translations['Do you really want to stop this job chain node?']            = 'Вы действительно хотите остановить этот узел в Job-Цепочке?';
_translations['Do you really want to unstop this job chain node?']          = 'Вы действительно хотите продолжить работу этого узла в Job-Цепочке?';
_translations['Do you really want to skip this job chain node?']            = 'Вы действительно хотите перепрыгнуть этот узел в Job-Цепочке?';
_translations['Do you really want to unskip this job chain node?']          = 'Вы действительно хотите добавить снова этот узел в Job-Цепочку?';

_translations['Do you really want to start this job?']                      = 'Вы действительно хотите стартовать этот Job?';
_translations['Do you really want to stop this job?']                       = 'Вы действительно хотите остановить этот Job?';
_translations['Do you really want to unstop this job?']                     = 'Вы действительно хотите продолжить работу этого Job?';
_translations['Do you really want to delete this job?']                     = 'Вы действительно хотите удалить этот Job?';
_translations['Do you really want to kill this task?']                      = 'Вы действительно хотите прервать и удалить этот Task?';
_translations['Do you really want to delete this task?']                    = 'Вы действительно хотите удалить этот Task?';
_translations['Do you really want to end this task?']                       = 'Вы действительно хотите завершить работу этого Task?';
_translations['Do you really want to end the tasks?']                       = 'Вы действительно хотите завершить работу этих Tasks?';
_translations['Do you really want to suspend the tasks?']                   = 'Вы действительно хотите приостановить работу этих Tasks?';
_translations['Do you really want to continue the tasks?']                  = 'Вы действительно хотите продолжить работу этих Tasks?';

_translations['Do you really want to start this order?']                    = 'Вы действительно хотите стартовать этот Ордер?';
_translations['Do you really want to add an order?']                        = 'Вы действительно хотите добавить Ордер?';
_translations['Do you really want to reset this order?']                    = 'Вы действительно хотите сбросить этот Ордер?';
_translations['Do you really want to suspend this order?']                  = 'Вы действительно хотите приостановить этот Ордер?';
_translations['Do you really want to resume this order?']                   = 'Вы действительно хотите резюмировать этот Ордер?';
_translations['Do you really want to change the order state?']              = 'Вы действительно хотите поменять статус этого Ордера?';
_translations['Do you really want to delete this order?']                   = 'Вы действительно хотите удалить этот Ордер?';
_translations['Do you really want to remove the setback?']                  = 'Вы действительно хотите удалить эту Задержку?';

_translations['Do you really want to set the run time?']                    = 'Вы действительно хотите изменить время старта?';
_translations['Do you really want to add a substituting schedule?']         = 'Вы действительно хотите добавить субституированный Schedule?';
_translations['Do you really want to modify this schedule?']                = 'Вы действительно хотите изменить этот Schedule?';
_translations['Do you really want to delete this schedule?']                = 'Вы действительно хотите удалить этот Schedule?';
