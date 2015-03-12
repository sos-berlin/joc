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
_translations['All rights reserved.']                      = 'All rights reserved.'
_translations['OK']                                        = 'OK'

//Tabs
_translations['Jobs']                                      = 'ジョブ';
_translations['Job Chains']                                = 'ジョブチェーン';
_translations['Orders']                                    = 'オーダー';
_translations['Schedules']                                 = 'スケジュール';
_translations['Process Classes']                           = 'プロセスクラス';
_translations['Locks']                                     = 'ロック';
_translations['Cluster']                                   = 'クラスタ';
_translations['Remote Schedulers']                         = 'リモートJobScheduler';
_translations['Last Activities']                           = '最新履歴';
                                                           
//Buttons menu, extras, update, hide          
_translations['Menu']                                      = 'メニュー';
_translations['Extras']                                    = '情報';
_translations['Help']                                      = 'ヘルプ';
_translations['Hide']                                      = '非表示';
_translations['Update']                                    = 'リフレッシュ';

//Menu content of scheduler menu
_translations['Show log']                                  = 'ログ表示';
_translations['Show job dependencies']                     = 'ジョブフロー表示';
_translations['Show job chain dependencies']               = 'ジョブチェーンフロー表示';
_translations['Show start times']                          = '実行予定表示';
_translations['Manage filters']                            = 'フィルタ設定';
_translations['Manage log categories']                     = 'ログ設定';
_translations['Pause']                                     = '一時停止';
_translations['Continue']                                  = '再開';
_translations['Terminate']                                 = '終了';
_translations['Terminate within ~$secs']                   = '~$sec秒後に終了';
_translations['Terminate and restart']                     = '終了後再開';
_translations['Terminate and restart within ~$secs']       = '終了後 ~$sec秒後再開';
_translations['Abort immediately']                         = '即時強制終了';
_translations['Abort immediately and restart']             = '即時強制終了後再開';
_translations['Terminate cluster']                         = 'クラスタ終了';
_translations['Terminate cluster within ~$secs']           = '~$sec秒後クラスタ終了';
_translations['Terminate and restart cluster']             = 'クラスタ終了再開';
_translations['Terminate and restart cluster within ~$secs'] = '~$sec秒後クラスタ終了再開';
_translations['Terminate fail-safe']                       = 'フェイルセーフ終了';
_translations['Terminate fail-safe within ~$secs']         = '~$sec秒後フェイルセーフ終了';
                                                           
//Menu content of extras                      
_translations['Documentation']                             = 'ドキュメント';
_translations['Forum']                                     = 'フォーラム(英語)';
_translations['Downloads']                                 = 'ダウンロード';
_translations['Follow us on Twitter']                      = 'Twitterでフォロー';
_translations['About']                                     = '情報';
_translations['Settings']                                  = '環境設定';
_translations['Monitor']                                   = 'Monitor';
_translations['Configuration']                             = 'Configuration';
                                                           
//First line                                  
_translations['every $secs']                               = '$sec秒毎';
_translations['Time zone']                                 = 'タイムゾーン';
_translations['local']                                     = 'ローカル';

                                                      
//Other lines                            
_translations['ID']                                        = 'ID';
_translations['State']                                     = 'ステート';
_translations['Pid']                                       = 'Pid';
_translations['Time']                                      = '時刻';
_translations['jobs']                                      = 'ジョブ';
_translations['need process']                              = '必要プロセス';
_translations['tasks']                                     = 'タスク';
_translations['orders']                                    = 'オーダー';
_translations['$cnt in cluster']                           = '$cnt台のクラスタ';
_translations['Start Time']                                = '起動日時';

//Job Scheduler states
_translations['starting']                                  = '開始中';
_translations['running']                                   = '実行中';
_translations['paused']                                    = '一時停止中';
_translations['stopping']                                  = '停止中';
_translations['stopping_let_run']                          = 'タスクを残して停止';
_translations['stopped']                                   = '停止';
_translations['waiting_for_activation']                    = '待機中';

//Misc.
_translations['unknown']                                   = 'unknown';
_translations['THIS IS A BACKUP...']                       = 'THIS IS A BACKUP...';
_translations['Backup JobScheduler:']                      = 'Backup JobScheduler:';
_translations['JobScheduler is waiting for database ...']  = 'JobScheduler is waiting for database ...';
_translations['Waiting for response from JobScheduler ...'] = 'Waiting for response from JobScheduler ...';
_translations['No connection to JobScheduler']             = 'No connection to JobScheduler';
_translations['Error at XML answer:']                      = 'Error at XML answer:';
_translations["Error at XSL answer '$xsl':"]               = "Error at XML answer '$xsl':";
_translations["Error at HTTP answer '$url':"]              = "Error at HTTP　answer '$url':";
_translations['$trial trial (of 5) to (re)connect to JobScheduler'] = '$trial trial (of 5) to (re)connect to JobScheduler';
_translations['First']                                     = '1回目';
_translations['Second']                                    = '2回目';
_translations['Third']                                     = '3回目';
_translations['Fourth']                                    = '4回目';
_translations['Last']                                      = '最終';
_translations['never']                                     = '未実行';
_translations['now']                                       = '即時';
_translations['days']                                      = '日';
_translations['The settings dialog is not available,\nbecause $file is used as settings file.'] = 'The settings dialog is not available,\nbecause $file is used as settings file.'; 
_translations['You can enable the settings dialog \nvia the _disable_cookie_settings flag in the settings file'] = 'You can enable the settings dialog \nvia the _disable_cookie_settings flag in the settings file';

//compact
_translations['from']                                      = 'from';
_translations['update']                                    = 'update';
_translations['last update']                               = 'last update';
_translations['Scheduler is running since']                = 'JobScheduler起動日時';


/******************************************************************************
*                                  JOBS/TASKS                                 *
******************************************************************************/
//some labels are already translated above
//Label of Checkboxes
_translations['Show tasks']                                = 'タスク表示';

//Label and Options of Selectbox
_translations['All jobs']                                  = '全ジョブ表示';
_translations['Standalone jobs']                           = 'スタンドアロンジョブ';
_translations['Order jobs']                                = 'オーダージョブ';
_translations['with state']                                = 'ステート';
_translations['(all)']                                     = '(全て)';
_translations['(other)']                                   = '(その他)';
_translations['running or stopped']                        = '実行中、又は停止';
_translations['and with process class']                    = 'プロセスクラス';
                                                           
//Label and Options in Selectbox of jobs sort                        
_translations['Sort jobs by']                              = 'ジョブ表示順';
_translations['(unsorted)']                                = '(非ソート)';
_translations['name in ascending order']                   = 'ジョブ名(昇順)';
_translations['name in descending order']                  = 'ジョブ名(降順)';
_translations['next start time in ascending order']        = '次回開始時刻 (昇順)';
_translations['next start time in descending order']       = '次回開始時刻 (降順)';
_translations['state in ascending order']                  = 'ステート (昇順)';
_translations['state in descending order']                 = 'ステート (降順)';
                                                           
//Title of images
_translations['toggle to']                                 = '表示切替'; 
_translations['list view']                                 = 'リスト'; 
_translations['tree view']                                 = 'ツリー'; 
_translations['open all folders']                          = '全て開く';   
_translations['close all folders']                         = '全て閉じる';   

//Label and Options of Selectbox of jobs filtering                     
_translations['Filter jobs by']                            = 'フィルタリング';
_translations['(no filtering)']                            = '(フィルタリング無し)';
                                                           
//Table header of jobs list                                
_translations['Job']                                       = 'ジョブ';
_translations['Steps']                                     = 'ステップ';
_translations['Process steps']                             = '完了';
_translations['Next start']                                = '次回開始時刻';
                                                           
//Labels in detail frame                                   
_translations['JOB']                                       = 'ジョブ';
_translations['TASK']                                      = 'タスク';
_translations['File timestamp']                            = 'ファイル更新日付';
_translations['State text']                                = '最終実行ログ';
_translations['Error']                                     = 'エラー';
_translations['Error in configuration file']               = 'ジョブ定義エラー';
_translations['Error in changed file']                     = 'ファイル変更エラー';
_translations['(not loaded)']                              = '(not loaded)';
_translations['Removing delayed']                          = 'Removing delayed';
_translations['Tasks']                                     = 'タスク';
_translations['Run time defined by']                       = 'Run time defined by';
_translations['orders to process']                         = '実行予定オーダー';
_translations['Mail subject']                              = 'メール件名';
_translations['To']                                        = 'To';
_translations['CC']                                        = 'CC';
_translations['BCC']                                       = 'BCC';
_translations['From']                                      = 'From';
_translations['SMTP']                                      = 'SMTP';
_translations['Last error']                                = '最新エラー';
_translations['Last warning']                              = '最新ワーニング';
_translations['Log level']                                 = 'ログレベル';
_translations['mail on error']                             = 'エラー時メール';
_translations['mail on warning']                           = 'ワーニング時メール';
_translations['mail on success']                           = '成功時メール';
_translations['mail on process']                           = '処理時メール';
_translations['start cause']                               = '開始理由';
_translations['waiting for order']                         = 'オーダー待機';
_translations['Idle since']                                = 'アイドル中';
_translations['In process since']                          = '処理中';
_translations['Running since']                             = '起動日時';
_translations['Enqueued at']                               = '待機中';
_translations['Subprocesses']                              = 'サブプロセス';
                                                           
//Task queue                                               
_translations['enqueued tasks']                            = '待機タスク';
_translations['Id']                                        = 'ID';
_translations['Enqueued']                                  = '待機中';
_translations['Start at']                                  = '開始時間';
_translations['Delete']                                    = '削除';
                                                           
                                                           
//Task history                                             
_translations['No tasks in the history']                   = 'タスク履歴はありません';
_translations['Started']                                   = '開始時刻';
_translations['Cause']                                     = '理由';
_translations['Ended']                                     = '終了時刻';
_translations['Duration']                                  = '時間';
                                                           
//Order queue                                              
_translations['Start']                                     = '開始時刻';
                                                           
                                                           
//Title of Tabs                                            
_translations['Task Queue']                                = '待機タスク';
_translations['Task History']                              = 'タスク履歴';
_translations['Order Queue']                               = '待機オーダー';
                                                           
                                                           
//Menu content of job menu                                 
_translations['Job menu']                                  = 'ジョブメニュー';
_translations['Show configuration']                        = 'ジョブ定義表示';
_translations['Show description']                          = 'ジョブ説明表示';
_translations['Show documentation']                        = 'ジョブ説明表示';
_translations['Show dependencies']                         = 'ジョブフロー表示';
_translations['Start task immediately']                    = 'タスク即時実行';
_translations['Start task unforced now']                   = 'タスクスケジュール実行';
_translations['Start task at']                             = 'タスク実行時刻設定';
_translations['Start task parametrized']                   = 'タスク実行パラメータ設定';
_translations['Set run time']                              = 'スケジュール設定';
_translations['Stop']                                      = '停止';
_translations['Unstop']                                    = '再開';
_translations['Reread']                                    = '';
_translations['End tasks']                                 = 'タスク終了';
_translations['Suspend tasks']                             = 'タスク保留';
_translations['Continue tasks']                            = 'タスク再開';
_translations['Delete job']                                = 'ジョブ削除';
                                                           
                                                           
//Menu content of task menu                                
_translations['Task menu']                                 = 'タスクメニュー';
_translations['End']                                       = 'タスク終了';
_translations['Kill immediately']                          = 'タスク強制終了';
                                                           
                                                           
//Mouseover-Title                                          
_translations['show job details']                          = 'ジョブ詳細表示';
_translations['show task details']                         = 'タスク詳細表示';
_translations['show schedule details']                     = 'スケジュール詳細表示';
_translations['Order is deleted']                          = 'オーダーは削除されました';
_translations['Order is replaced']                         = 'オーダーは置換されました';
                                                           
//misc.                          
_translations['Without start time']                        = '開始時刻無し';
_translations['Task']                                      = 'タスク';
_translations['No jobs found']                             = 'ジョブはありません';
                                                           
                                                           
//Job/Task states                                          
_translations['pending']                                   = '待機中';
_translations['loaded']                                    = 'ロードされました';
_translations['initialized']                               = '初期化されました';
_translations['not_initialized']                           = '初期化されていません';
_translations['disabled']                                  = '無効';
_translations['none']                                      = '未指定';
_translations['read_error']                                = 'リードエラー';
_translations['delayed after error']                       = '再試行';
_translations['needs process']                             = 'プロセスが必要';
_translations['needs lock']                                = 'ロックが必要';
_translations['loading']                                   = 'ロード中';
_translations['waiting_for_process']                       = '処理待ち';
_translations['opening']                                   = 'オープン中';
_translations['opening_waiting_for_locks']                 = 'ロック開放待ち';
_translations['running_process']                           = '処理中';
_translations['running_remote_process']                    = 'リモート処理中';
_translations['running_waiting_for_order']                 = 'オーダー待機中';
_translations['running_waiting_for_locks']                 = 'ロック待機中';
_translations['running_delayed']                           = '再試行待機中';
_translations['suspended']                                 = '保留中';
_translations['ending_waiting_for_subprocesses']           = '子プロセス多雨期中';
_translations['ending']                                    = '終了';
_translations['on_success']                                = '成功';
_translations['on_error']                                  = 'エラー';
_translations['exit']                                      = '終了';
_translations['error']                                     = 'エラー';
_translations['release']                                   = 'リリース';
_translations['ended']                                     = '終了';
_translations['deleting_files']                            = 'ファイル削除';
_translations['closed']                                    = 'クローズ';
_translations['Replacement is standing by']                = '次によって置換されました';
_translations['changed file has error']                    = '変更されたファイルはエラーになります';
_translations['is incorrect or missing']                   = 'は、不正か見つかりません';
_translations['not an order job']                          = 'オーダージョブではありません';
_translations['non-excl.']                                 = 'non-excl.';
_translations['is being replaced']                         = 'は置換されます';
_translations['undefined']                                 = '未定義';


                                                           
//Task start causes                                        
_translations['none']                                      = '未指定';
_translations['period_once']                               = '初期実行';
_translations['period_single']                             = '単独実行';
_translations['period_repeat']                             = '繰返し実行';
_translations['queue']                                     = 'キュー';
_translations['queue_at']                                  = 'キュー時刻';
_translations['directory']                                 = 'ディレクトリ';
_translations['delay_after_error']                         = '再試行';
_translations['order']                                     = 'オーダー';
_translations['wake']                                      = '再開';                                                     


/******************************************************************************
*                                 JOB CHAINS                                  *
******************************************************************************/
//some labels are already translated above
//Label of Checkboxes and Selectboxes
_translations['Job chains containing jobs']                = 'ジョブを含むジョブチェーン';
_translations['Show order history']                        = 'オーダー履歴表示';
_translations['Show jobs']                                 = 'ジョブ表示';
_translations['Show orders']                               = 'オーダー表示';
                                                           
//Label of Selectbox of job chains sort                    
_translations['Sort job chains by']                        = 'ジョブチェーンのソート順';
                                                           
//Label of Selectbox of job chains filtering               
_translations['Filter job chains by']                      = 'ジョブチェーンのフィルタ';
                                                           
//Table header of job chains list                          
_translations['Order state']                               = 'ステート';
_translations['Job chain']                                 = 'ジョブチェーン';
_translations['Job state']                                 = '状態';
                                                           
//Table header in detail frame                             
_translations['JOB CHAIN']                                 = 'ジョブチェーン';
_translations['ORDER HISTORY']                             = 'オーダー履歴';
                                                           
//Menu content of job chain menu                           
_translations['Create job chain']                          = 'ジョブチェーン作成';
_translations['Job chain menu']                            = 'ジョブチェーンメニュー';
_translations['Job node menu']                             = 'ジョブノードメニュー';
_translations['Add order']                                 = 'オーダー作成';
_translations['Add persistent order']                      = '永続オーダー作成';
_translations['Delete job chain']                          = 'ジョブチェーン削除';
_translations['Delete temp. orders']                       = '一時的な受注を削除';
_translations['Skip node']                                 = 'ノードスキップ';
_translations['Stop node']                                 = 'ノード停止';
_translations['Stop job']                                  = 'ジョブ停止';
_translations['Unstop job']                                = 'ジョブ再開';
_translations['Unskip']                                    = 'スキップしない';

//Mouseover-Title
_translations['show job chain details']                    = 'ジョブチェーン詳細表示';

//job chain (node) states
_translations['under_construction']                        = '作成中';
_translations['finished']                                  = '終了';
_translations['removing']                                  = '削除中';
_translations['Node is stopped']                           = 'ノード停止';
_translations['Node is skipped']                           = 'ノードスキップ';

//misc.                                                          
_translations['No job chains found']                       = 'ジョブチェーンはありません';
_translations['Web service']                               = 'Webサービス';
_translations['file orders']                               = 'ファイルオーダー';
_translations['pattern']                                   = '条件';
_translations['delay']                                     = '間隔';
_translations['repeat']                                    = '繰返回数';
_translations['blacklist']                                 = 'ブラックリスト';
_translations['max. Orders']                               = '最大のオーダー';


/******************************************************************************
*                         ORDERS/ORDERQUEUE/BLACKLIST                         *
******************************************************************************/
//some labels are already translated above
//Label of Selectbox of orders sort
_translations['Sort orders by']                            = 'オーダーソート順';
                                                           
//Options in Selectbox of orders sort                      
_translations['job chain in ascending order']              = 'ジョブチェーン(昇順)';
_translations['job chain in descending order']             = 'ジョブチェーン(降順)';
                                                           
//Label of Selectbox of jobs filtering                     
_translations['Filter orders by']                          = 'オーダーフィルタ';
                                                                                                                                                                                 
//Mouseover-Title                                          
_translations['Order is processed by Scheduler member']    = '他のJobSchedulerからオーダーが実行されています';
_translations['show order details']                        = 'オーダー詳細表示';
_translations['This order is a replacement for another order with the same ID'] = '同じIDの他のオーダーによって置換されました';
                                                           
//Detail frame                                             
_translations['ORDER']                                     = 'オーダー';
                                                                                                                     
//Menu content of order menu                               
_translations['Order menu']                                = 'オーダーメニュー';
_translations['Start order now']                           = 'オーダー即時実行';
_translations['Start order at']                            = 'オーダー実行時刻設定';
_translations['Start order parametrized']                  = 'オーダー実行パラメータ設定';
_translations['Set order state']                           = 'オーダーステート設定';
_translations['Suspend order']                             = 'オーダー保留';
_translations['Resume order']                              = 'オーダー再開';
_translations['Resume order parametrized']                 = 'オーダー再開パラメータ設定';
_translations['Resume order $order']                       = 'オーダー再開 $order';
_translations['Reset order']                               = 'オーダー初期化';
_translations['Delete order']                              = 'オーダー削除';
_translations['Remove setback']                            = '再試行設定削除';

//misc
_translations['No orders found']                           = 'オーダーはありません';
_translations['Processed by']                              = '処理';
_translations['Order']                                     = 'オーダー';
_translations['Setback']                                   = '再試行';
_translations['deleted']                                   = '削除';
_translations['Replacement']                               = '置換';
_translations['currently processed by']                    = '処理中'
_translations['on blacklist']                              = 'ブラックリスト内'
                                                           

                                                           
//order history                                            
_translations['No orders in the history']                  = 'オーダー履歴はありません';


/******************************************************************************
*                                PROCESS CLASS                                *
******************************************************************************/
//some labels are already translated above
_translations['Process class']                             = 'プロセスクラス';
                                                           
//Table header of process class list                       
_translations['Operations']                                = 'オペレーション';
_translations['Callbacks']                                 = 'コールバック';
_translations['Current operation']                         = 'オペレーション中';
                                                           
_translations['(default)']                                 = '(初期値)';
_translations['max processes']                             = '最大プロセス数';
_translations['Remote Scheduler']                          = 'リモートJobScheduler';

_translations['No process classes found']                  = 'プロセスクラスはありません';
                                                           

/******************************************************************************
*                                   CLUSTER                                   *
******************************************************************************/
//some labels are already translated above
//Table header of process class list
_translations['Last heart beat']                           = '前回のハートビート';
_translations['Detected heart beats']                      = 'ハートビート検出数';
_translations['Backup precedence']                         = 'Backup precedence';
                                                           
//Menu content of cluster member                           
_translations['Cluster member menu']                       = 'クラスタメニュー';
_translations['Delete entry']                              = 'エントリ削除';
_translations['Restart']                                   = 'リスタート';

//misc.
_translations['active Scheduler(s)']                       = 'アクティブJobScheduler';
_translations['exclusive Scheduler(s)']                    = '優先JobScheduler';
_translations['This Scheduler is active']                  = 'このJobSchedulerはアクティブです';
_translations['and exclusive']                             = '優先です';
_translations['Only active JobSchedulers are allowed to start operation.'] = 'アクティブなJobSchedulerからしか操作開始できません';
_translations['This Scheduler']                            = 'このJobScheduler';
_translations['(was active!)']                             = '(アクティブでした)';
_translations['active']                                    = 'アクティブ';
_translations['inactive']                                  = '非アクティブ';
_translations['distributed orders']                        = '分散オーダー';
_translations['exclusive']                                 = '優先';
_translations['backup']                                    = 'バックアップ';
_translations['still checking...']                         = '確認中...';
_translations['dead']                                      = 'デッド';
_translations['discovered']                                = 'ディスカバー';
_translations['after']                                     = '後';
_translations['Deactivated by']                            = '非活化';
_translations['ago']                                       = '前';



/******************************************************************************
*                               REMOTE SCHEDULER                              *
******************************************************************************/
//some labels are already translated above

//Table header of process class list                       
_translations['IP']                                        = 'IP';
_translations['Hostname']                                  = 'ホスト名';
_translations['Port']                                      = 'ポート';
_translations['Last Update']                               = 'Last Update';
_translations['Connected']                                 = '接続';
_translations['Disconnected']                              = '切断';
_translations['Version']                                   = 'バージョン';
                                                           
//misc.                                                    
_translations['Scheduler(s)']                              = 'JobScheduler';
_translations['connected']                                 = '接続';



/******************************************************************************
*                                    LOCKS                                    *
******************************************************************************/
//some labels are already translated above
_translations['No locks found']                            = 'ロックはありません';
_translations['Lock']                                      = 'ロック';
                                                           
//Table header of process class list                       
_translations['non-exclusively locked']                    = '共有ロック';
_translations['locked']                                    = 'ロック中';
_translations['free']                                      = '開放中';

//misc.
_translations['Holders (non-exclusive)']                   = 'ロック保持(共有)';
_translations['Holders (exclusive)']                       = 'ロック保持(排他)';
_translations['Waiting jobs (exclusive)']                  = 'ジョブ待機(排他)';
_translations['Waiting jobs (non-exclusive)']              = 'ジョブ待機(共有)';

//Mouseover-Title
_translations['Lock is not available, it is locked']       = 'ロックされています';
_translations['Lock is available']                         = 'ロックできます';



/******************************************************************************
*                                LAST ACTIVITIES                              *
******************************************************************************/
//some labels are already translated above
//radio buttons, checkboxes
_translations['Show all']                                  = '全て表示';
_translations['Show only orders']                          = 'オーダーのみ表示';
_translations['Show only tasks']                           = 'タスクのみ表示';
_translations['Show only faulty tasks and orders']         = '失敗したタスク/オーダーのみ表示';
_translations['Show last tasks error']                     = 'エラーのある最新タスクを表示';

//Table header of job chains list                          
_translations['Job name']                                  = 'ジョブ名';
_translations['Exitcode']                                  = '終了コード';
_translations['Order ID']                                  = 'オーダーID';
_translations['Order state']                               = 'ステート';

//Button title
_translations['Show order log']                            = 'オーダーログ表示';
_translations['Show task log']                             = 'タスクログ表示';

//misc.
_translations['No last activities found']                  = '実行状況はありません';


/******************************************************************************
*                                   SCHEDULES                                 *
******************************************************************************/
//some labels are already translated above

//Menu content of schedules                               
_translations['Create schedule']                           = 'スケジュール作成';
_translations['Schedule menu']                             = 'スケジュールメニュー';
_translations['Substitute menu']                           = '置換スケジュールメニュー';
_translations['Add substitute']                            = '置換スケジュール作成';
_translations['Edit schedule']                             = 'スケジュール編集';
_translations['Delete schedule']                           = 'スケジュール作成';

//Table header of job chains list                          
_translations['Schedule']                                  = 'スケジュール';
_translations['Valid from']                                = 'から有効';
_translations['Valid to']                                  = 'まで有効';
_translations['Substituted by']                            = '置換スケジュール';

//Mouseover-Title
_translations['show schedule details']                     = 'スケジュール詳細表示';

//misc.
_translations['Green']                                     = '緑色';
_translations['marked schedules are currently active']     = 'スケジュールはアクティブ';
_translations['No schedules found']                        = 'スケジュールはありません';
_translations['Used by jobs']                              = '使用中のジョブ';
_translations['Used by orders']                            = '使用中のオーダー';
_translations['of job chain']                              = 'ジョブチェーン';
_translations['Substituted schedule']                      = '置換されたスケジュール';

//State
_translations['incomplete']                                = '未完了';

//Detail frame                                             
_translations['SCHEDULE']                                  = 'スケジュール';
_translations['Valid']                                     = '有効';


/******************************************************************************
*                                LOG CATEGORIES                               *
******************************************************************************/
//some labels are already translated above
_translations['LOG CATEGORIES']                            = 'ログ設定';
                                                           
//misc.                                                    
_translations['The default log caregories are marked']     = 'ログ初期設定は';
_translations['orange']                                    = 'オレンジ色';
_translations['and they are active after each reset.']     = 'で、リセット後有効です';
_translations['Current log categories setting']            = '現在のログ設定は';
_translations['Set']                                       = '設定';
_translations['log categories for a duration of']          = '経過時間の設定';
_translations['seconds']                                   = '秒';
_translations['log categories are updated']                = '設定が更新されました';
_translations['reset is executed']                         = 'リセットされます';
_translations['reset is executed after']                   = 'リセットされるまで後';
_translations['Next reset']                                = '次のリセット';
_translations['implicit']                                  = 'implicit';
_translations['explicit']                                  = 'explicit';

//Table header of log categories list
_translations['Category']                                  = '設定';
_translations['Mode']                                      = 'モード';
_translations['Description']                               = '備考';

_translations['No log categories found']                   = 'ログ設定はありません';


/******************************************************************************
*                                  CALENDAR                                   *
******************************************************************************/

_translations['CALENDAR']                                  = '実行予定';
_translations['job']                                       = 'ジョブ';
_translations['job chain']                                 = 'ジョブチェーン';
_translations['of job chain']                              = 'ジョブチェーン';
_translations['all jobs']                                  = '全ジョブ';
_translations['all jobs and orders']                       = '全てのJob/Order';
_translations['Start times for']                           = '実行予定';
_translations['by calling']                                = '出力条件';
_translations['with output format']                        = '出力形式';
_translations['at']                                        = '日時';


/******************************************************************************
*                                  FILTER                                     *
******************************************************************************/

_translations['FILTER ADMINISTRATION']                     = 'フィルタ設定';
_translations['close']                                     = '終了';
_translations['store']                                     = '保存';
_translations['store as ...']                              = '別名で保存...';
_translations['remove']                                    = '削除';
_translations['quick check']                               = 'クイックチェック';
_translations['regular expression for quick check']        = '正規表現チュック';
_translations['select all']                                = '全て選択';
_translations['deselect all']                              = '選択解除';
_translations['new filter']                                = '新規フィルタ';
_translations['filter was stored']                         = 'フィルタは保存されました';
_translations['new filter was stored']                     = '新規フィルタは保存されました';
_translations['filter was removed']                        = 'フィルタは削除されました';
_translations['Filter name must be stated']                = 'フィルタ名を入力して下さい';
_translations['Using\n\n|\n=>\n;\n\nis not allowed']       = '\n\n|\n=>\n;\n\nは使用で来ません';
_translations['A filter "$filter_name" is already defined in the file $file.'] = '"$filter_name"は、すでに$fileで使用されています。';
_translations['The same filter name "$filter" was already defined\n\nDo you want nevertheless to store it ?'] = '同じ名前の"$filter"が既に使われています。\n\n無視して保存しますか?'; 


/******************************************************************************
*                                   ERROR                                     *
******************************************************************************/

_translations['source']                                    = 'source';
_translations['line']                                      = 'line';
_translations['column']                                    = 'column';
_translations['unknown error']                             = 'unknown error';


/******************************************************************************
*                           DIALOGS OF MENU FUNCTIONS                         *
******************************************************************************/

//Buttons
_translations['submit']                                    = 'OK';
_translations['cancel']                                    = 'キャンセル';
_translations['new param']                                 = '新規パラメータ';
_translations['run time editor']                           = 'ランタイムエディタ';

//Plausi
_translations['$field must be stated!']                    = '$fieldを入力してください!';
_translations['Period from ($field) is invalid date or before 1970-01-01.'] = '($field)からのピリオドは無効か、1970-01-01以前です';
_translations['Max. hits ($field) is not a positive number.'] = '最大数($field)が、正数ではありません';
_translations['Please add an existing order job to the job chain nodes.'] = '既存のオーダージョブをジョブチェーンノードに登録してください';
_translations['Please select an existing order job on the left hand side.'] = '既存のオーダージョブを左側から選択してください';

//Plausi fields
_translations['Start time']                                = '開始時刻';
_translations['Name']                                      = 'ジョブ名';

//Scheduler settings
_translations['The following values will be stored in a cookie. If no cookies are available the values which are set in <code>./custom.js</code> are used.'] = '下欄の値はcookieに保存されます。cookieが有効ではない場合は<code>./custom.js</code>に保存されます';
_translations['Onload Values']                             = '表示設定';
_translations['periodically every']                        = 'リフレッシュ間隔';
_translations['Tabs']                                      = 'タブ';
_translations['Switch to']                                 = '初期画面は';
_translations['as the beginning view']                     = 'を表示';
_translations['View Mode']                                 = '表示切替';
_translations['for']                                       = '表示';
_translations['Selects, Checkboxes and Radios']            = '表示設定';
_translations['in the <i>jobs</i> tab']                    = '（「ジョブ」タブ）';
_translations['in the <i>job chains</i> tab']              = '（「ジョブチェーン」タブ）';
_translations['in the job chain details']                  = '（「ジョブチェーン」詳細）';
_translations['in the <i>last activities</i> tab']         = '（「実行状況」タブ）';
_translations['all orders and tasks']                      = '全オーダー/タスク';
_translations['only tasks']                                = 'タスクのみ';
_translations['only orders']                               = 'オーダーのみ';
_translations['Runtime Values']                            = '実行設定';
_translations['Limits']                                    = '制限値';
_translations['Max. number of orders per job chain']       = 'ジョブチェーン当たり最大オーダー数';
_translations['Max. number of last activities']            = '実行状況の最大表示数';
_translations['Max. number of history entries per order']  = 'オーダー当たり最大履歴表示数';
_translations['Max. number of history entries per task']   = 'タスク当たり最大履歴表示数';
_translations['Termintate within']                         = '終了設定';
_translations['Max. seconds within the JobScheduler terminates'] = 'JobScheduler終了時の最大猶予時間（秒）';
_translations['Dialogs']                                   = 'ダイアログ設定';
_translations['Default start time in the &quot;<i>Start task/order at</i> &quot;-Dialog is <i>now</i>'] = '「タスク/オーダー実行」ダイアログの初期値が「即時」';
_translations['Debugging of the operations GUI']           = 'WEB GUIのデバッグレベル';
_translations['Level']                                     = 'レベル';

//start task/order
_translations['Start task $task']                          = 'タスク実行 $task';
_translations['Start order $order']                        = 'オーダー実行 $order';

//_translations['<b>Enter a start time</b><span class="small"> in ISO format &quot;yyyy-mm-dd HH:MM[:SS]&quot; or &quot;now&quot;. The time at which a task is to be started &lt;<p align="left">run</p>_time&gt; is deactivated. Relative times - &quot;now + HH:MM[:SS]&quot; and &quot;now + SECONDS&quot; - are allowed.<span>'] = '<b>開始時刻を入力してください</b> ISOフォーマットの 「yyyy-mm-dd HH:MM[:SS] 」、即時実行の場合は「now」、 ジョブ定義内で設定された &lt;<p align="left">run</p>_time&gt; は無効となります。相対時刻指定も可能です、 「now + HH:MM[:SS]」、又は「now + 秒数」と指定可能です.<span>';
_translations['<b>Enter a start time</b><span class="small"> in ISO format &quot;yyyy-mm-dd HH:MM[:SS]&quot; or &quot;now&quot;. The time at which a task is to be started &lt;run_time&gt; is deactivated. Relative times - &quot;now + HH:MM[:SS]&quot; and &quot;now + SECONDS&quot; - are allowed.<span>'] = '<b>開始時刻を入力してください</b> ISOフォーマットの 「yyyy-mm-dd HH:MM[:SS] 」、即時実行の場合は「now」、 ジョブ定義内で設定された &lt;run_time&gt; は無効となります。相対時刻指定も可能です、 「now + HH:MM[:SS]」、又は「now + 秒数」と指定可能です.';

_translations['<b>Enter a run time</b> or use the $editor'] = 'ランタイム定義を記述するか、$editorを使って定義してください';
_translations['Start enforced']                            = '強制開始';
_translations['Change parameters']                         = 'パラメータ変更';
_translations['Declare parameters']                        = 'パラメータ設定';
_translations['Declare new parameters']                    = '新規パラメータ設定';
_translations['name']                                      = 'パラメータ名';
_translations['value']                                     = '値';

//add order
_translations['Add order to $job_chain']                   = 'オーダーを$job_chainに登録';
_translations['Enter an order id']                         = 'オーダーIDを入力';
_translations['Enter an order title']                      = 'オーダー名を入力';
_translations['Select an order state']                     = 'オーダーのステートを選択';
_translations['Select an order end state']                 = 'オーダーの終了ステートを選択';
_translations['In order to store this order in a hot folder you have to state an order id.\nYour order will only be stored permanently, however, it is valid for\nthe lifetime of this JobScheduler session. Do you want to continue?'] = 'ホットフォルダにこのオーダーを保存するためにはオーダーIDが必要です。\n保存されたオーダーはこのJobSchedulerセッション中有効になります。\n保存しますか?';

//remove temp. orders
_translations['Delete temporary orders from $job_chain']   = '$job_chainから一時的な受注を削除';
_translations['all']                                       = 'すべて'; 

//set order state
_translations['Set order state of $order']                 = '$orderにステートを設定';
_translations['<b>Select a new order state</b>']           = '<b>新規オーダーステートを選択</b>';
_translations['<span class="small">The current order state is $state.</span>'] = '<span class="small">オーダーの現在ステートは$stateです</span>';
_translations['<b>Select a new order end state</b>']       = '新規オーダーの終了ステートを選択';
_translations['<span class="small">The current order end state is $state.</span>'] = 'オーダーの終了ステートは$stateです</span>';

//set run time
_translations['Set run time of $job']                      = '$jobのランタイム設定';
_translations['or choose a schedule']                      = '又はスケジュールを選択';

//terminate task
_translations['Terminate task $task_id']                   = 'Terminate task $task_id';
_translations['with timeout']                              = 'with timeout';
_translations['Should the task not terminate within the specified timeout then it will be killed.'] = 'Should the task not terminate within the specified timeout then it will be killed.';
_translations['Please enter a number for the timeout.']    = 'Please enter a number for the timeout.';


//schedules
_translations['Add schedule']                              = 'スケジュールを追加';
_translations['Add substitute for $schedule']              = '$scheduleを置換';
_translations['Edit $schedule']                            = '$scheduleを編集';
_translations['Enter a schedule name']                     = 'スケジュール名を入力';

//calendar
_translations['Parameterize the start times list']         = '実行予定表示設定';
_translations['<b>Set the period</b>']                     = '<b>期間設定</b>';
//_translations['<span class="small">(ISO format yyyy-mm-dd[ hh:mm:ss])</span>']  = '(ISOフォーマット「yyyy-mm-dd[ hh:mm:ss]」)';
_translations['<span class="small">(Timestamps in ISO format yyyy-mm-dd[ hh:mm:ss])</span>']  = '<span class="small">(ISOフォーマット「yyyy-mm-dd[ hh:mm:ss]」)</span>';
_translations['...from']                                   = '開始';
_translations['...to']                                     = '終了';
_translations['Max. hits']                                 = '最大件数';
_translations['Output format']                             = '出力形式';
_translations['Include order start times']                 = 'オーダー開始時刻を含む';

//job chain
_translations['Sorry, but this feature is only supported for\nJobScheduler version 2.0.204.5774 or higher'] = 'Sorry, but this feature is only supported for\nJobScheduler version 2.0.204.5774 or higher';
_translations['Modify job chain']                          = 'ジョブチェーンの編集';
_translations['Now you can modify the job chain by editing the text area content.'] = 'テキストエリアの内容を編集して、ジョブチェーンを変更できます';
_translations['Orders are stored in the database (orders_recoverable)'] = 'オーダーはデータベースに保存されます(orders_recoverable)';
_translations['Existing order jobs']                       = '既にオーダージョブが存在します';
_translations['Job chain nodes']                           = 'ジョブチェーンノード';
_translations['Enter a folder']                            = 'フォルダ名を入力';
_translations['Enter a job chain name']                    = 'ジョブチェーン名を入力';
_translations['Enter a job chain title']                   = 'ジョブチェーンのタイトルを入力';


/******************************************************************************
*                           JOB DESCRIPTION                                   *
******************************************************************************/

_translations['The JobScheduler $scheduler has no jobs with a description.']               = '$schedulerは説明があるジョブはありません';
_translations['Because of a security sanction of your browser you must reload this site!'] = 'Because of a security sanction of your browser you must reload this site!';
_translations['Please select a job to display its description.']                           = '説明を表示するジョブを選択してください';
_translations['Please enter a job name to display its description.']                       = '説明を表示するジョブ名を入力してください';

                           
/******************************************************************************
*                           CONFIRMS                                          *
******************************************************************************/

_translations['Do you really want to terminate the JobScheduler?']          = 'Do you really want to terminate the JobScheduler?';
_translations['Do you really want to restart the JobScheduler?']            = 'Do you really want to restart the JobScheduler?';
_translations['Do you really want to abort the JobScheduler?']              = 'Do you really want to abort the JobScheduler?';
_translations['Do you really want to pause the JobScheduler?']              = 'Do you really want to pause the JobScheduler?';
_translations['Do you really want to continue the JobScheduler?']           = 'Do you really want to continue the JobScheduler?';
_translations['Do you really want to terminate the JobScheduler cluster?']  = 'Do you really want to terminate the JobScheduler cluster?';
_translations['Do you really want to restart the JobScheduler cluster?']    = 'Do you really want to restart the JobScheduler cluster?';
_translations['Do you really want to delete the dead entry?']               = 'Do you really want to delete the dead entry?';

_translations['Do you really want to stop this job chain?']                 = 'Do you really want to stop this job chain?';
_translations['Do you really want to unstop this job chain?']               = 'Do you really want to unstop this job chain?';
_translations['Do you really want to delete this job chain?']               = '本当にジョブチェーン削除しますか?';
_translations['Do you really want to stop this job chain node?']            = 'Do you really want to stop this job chain node?';
_translations['Do you really want to unstop this job chain node?']          = 'Do you really want to unstop this job chain node?';
_translations['Do you really want to skip this job chain node?']            = 'Do you really want to skip this job chain node?';
_translations['Do you really want to unskip this job chain node?']          = 'Do you really want to unskip this job chain node?';

_translations['Do you really want to start this job?']                      = 'Do you really want to start this job?';
_translations['Do you really want to stop this job?']                       = 'Do you really want to stop this job?';
_translations['Do you really want to unstop this job?']                     = 'Do you really want to unstop this job?';
_translations['Do you really want to delete this job?']                     = '本当にジョブ削除しますか';
_translations['Do you really want to kill this task?']                      = 'Do you really want to kill this task?';
_translations['Do you really want to terminate this task?']                 = 'Do you really want to terminate this task?';
_translations['Do you really want to delete this task?']                    = '本当にジョブ削除しますか';
_translations['Do you really want to end this task?']                       = 'Do you really want to end this task?';
_translations['Do you really want to end the tasks?']                       = 'Do you really want to end the tasks?';
_translations['Do you really want to suspend the tasks?']                   = 'Do you really want to suspend the tasks?';
_translations['Do you really want to continue the tasks?']                  = 'Do you really want to continue the tasks?';

_translations['Do you really want to start this order?']                    = 'Do you really want to start this order?';
_translations['Do you really want to add an order?']                        = 'Do you really want to add an order?';
_translations['Do you really want to reset this order?']                    = 'Do you really want to reset this order?';
_translations['Do you really want to suspend this order?']                  = 'Do you really want to suspend this order?';
_translations['Do you really want to resume this order?']                   = 'Do you really want to resume this order?';
_translations['Do you really want to change the order state?']              = 'Do you really want to change the order state?';
_translations['Do you really want to delete this order?']                   = '本当にオーダー削除しますか?';
_translations['Do you really want to delete selected orders?']              = 'Do you really want to delete selected orders?';
_translations['Do you really want to remove the setback?']                  = 'Do you really want to remove the setback?';

_translations['Do you really want to set the run time?']                    = 'Do you really want to set the run time?';
_translations['Do you really want to add a substituting schedule?']         = 'Do you really want to add a substituting schedule?';
_translations['Do you really want to modify this schedule?']                = 'Do you really want to modify this schedule?';
_translations['Do you really want to delete this schedule?']                = '本当にスケジュール削除しますか?';
