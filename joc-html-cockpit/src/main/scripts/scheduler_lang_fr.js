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
_translations['All rights reserved.']                      = 'Tous droits réservés.'
_translations['OK']                                        = 'OK'

//Tabs
_translations['Jobs']                                      = 'Tâches';
_translations['Job Chains']                                = 'Chaînes des Tâches';
_translations['Orders']                                    = 'Ordres';
_translations['Schedules']                                 = 'Programmations';
_translations['Process Classes']                           = 'Classes des Processus';
_translations['Locks']                                     = 'Verrous';
_translations['Cluster']                                   = 'Cluster';
_translations['Remote Schedulers']                         = 'Ordonnanceurs Distants';
_translations['Last Activities']                           = 'Activités Récentes';
                                                           
//Buttons menu, extras, update, hide          
_translations['Menu']                                      = 'Menu';
_translations['Extras']                                    = 'Extras';
_translations['Help']                                      = 'Aide';
_translations['Hide']                                      = 'Cacher détails';
_translations['Update']                                    = 'Mettre à jour';

//Menu content of scheduler menu
_translations['Show log']                                  = 'Visualiser log';
_translations['Show job dependencies']                     = 'Visualiser dépendances des tâches';
_translations['Show job chain dependencies']               = 'Visualiser dépendances des chaînes des tâches';
_translations['Show start times']                          = 'Visualiser dates de démarrage';
_translations['Manage filters']                            = 'Gérer les filtres';
_translations['Manage log categories']                     = 'Gérer les catégories de log';
_translations['Pause']                                     = 'Mettre en pause';
_translations['Continue']                                  = 'Continuer';
_translations['Terminate']                                 = 'Arrêter';
_translations['Terminate within ~$secs']                   = 'Arrêter dans $secs';
_translations['Terminate and restart']                     = 'Arrêter et redémarrer';
_translations['Terminate and restart within ~$secs']       = 'Arrêter et redémarrer dans $secs';
_translations['Abort immediately']                         = 'Avorter immédiatement';
_translations['Abort immediately and restart']             = 'Avorter immédiatement et redémarrer';
_translations['Terminate cluster']                         = 'Arrêter le cluster';
_translations['Terminate cluster within ~$secs']           = 'Arrêter le cluster dans $secs';
_translations['Terminate and restart cluster']             = 'Arrêter le cluster et redémarrer le cluster';
_translations['Terminate and restart cluster within ~$secs'] = 'Arrêter le cluster et redémarrer le cluster dans $secs';
_translations['Terminate fail-safe']                       = 'Arrêter (sans échec)';
_translations['Terminate fail-safe within']                = 'Arrêter (sans échec) dans $secs';
                                                           
//Menu content of extras                      
_translations['Documentation']                             = 'Documentation';
_translations['Forum']                                     = 'Forum';
_translations['Downloads']                                 = 'Downloads';
_translations['Release Notes']                             = 'notes de version';
_translations['Follow us on Twitter']                      = 'Suivez-nous sur Twitter';
_translations['About']                                     = 'Sur';
_translations['Settings']                                  = 'Paramètres';
_translations['Monitor']                                   = 'Moniteur';
_translations['Configuration']                             = 'Configuration';
                                                           
//First line                                  
_translations['every $secs']                               = 'toutes les $secs';
                                                      
//Other lines                            
_translations['ID']                                        = 'ID';
_translations['State']                                     = 'Etat';
_translations['Pid']                                       = 'Pid';
_translations['Time']                                      = 'Heure';
_translations['jobs']                                      = 'Tâches';
_translations['need process']                              = 'en attente de processus';
_translations['tasks']                                     = 'Instances';
_translations['orders']                                    = 'Ordres';
_translations['$cnt in cluster']                           = '$cnt dans le Cluster';
_translations['Start Time']                                = 'Heure de démarrage';

//Job Scheduler states
_translations['starting']                                  = 'démarrage en cours';
_translations['running']                                   = 'en cours d\'exécution';
_translations['paused']                                    = 'en pause';
_translations['stopping']                                  = 'arrêt en cours';
_translations['stopping_let_run']                          = 'arrêt en cours';
_translations['stopped']                                   = 'arrêté';
_translations['waiting_for_activation']                    = 'en attente de l\'activation';

//Misc.
_translations['unknown']                                   = 'inconnu';
_translations['THIS IS A BACKUP...']                       = 'CECI EST UNE SAUVEGARDE...';
_translations['Backup JobScheduler:']                      = 'Sauvegarde de JobScheduler:';
_translations['JobScheduler is waiting for database ...']  = 'JobScheduler est en attente de la base de donnée ...';
_translations['Waiting for response from JobScheduler ...'] = 'En attente de réponse de JobScheduler ...';
_translations['No connection to JobScheduler']             = 'Aucune connexion à JobScheduler';
_translations['Error at XML answer:']                      = 'Errerr dans la réponse XML:';
_translations["Error at XSL answer '$xsl':"]               = 'Erreur dans la réponse XSL \'$xsl\':';
_translations["Error at HTTP answer '$url':"]              = 'Erreur dans la réponse HTTP \'$url\':';
_translations['$trial trial (of 5) to (re)connect to JobScheduler'] = '$trial tentative (sur 5) pour se (re)connecter à JobScheduler';
_translations['First']                                     = 'Première';
_translations['Second']                                    = 'Seconde';
_translations['Third']                                     = 'Troisième';
_translations['Fourth']                                    = 'Quatrième';
_translations['Last']                                      = 'Dernière';
_translations['never']                                     = 'jamais';
_translations['now']                                       = 'maintenant';
_translations['days']                                      = 'Jours';
_translations['The settings dialog is not available,\nbecause $file is used as settings file.']                 = 'La fenêtre de dialogue \'Paramètres\' n\'est pas disponible,\ncar le fichier $file est utilisé en tant que fichier de paramétrage.'; 
_translations['You can enable the settings dialog \nvia the _disable_cookie_settings flag in the settings file'] = 'Vous pouvez activer le dialogue par le _disable_cookie_settings \ninterrupteur dans le fichier ci-dessus.';

//compact
_translations['from']                                      = 'de';
_translations['update']                                    = 'Actualiser';
_translations['last update']                               = 'Dernière mise à jour';
_translations['Scheduler is running since']                = 'L\'ordonnanceur est démarré depuis';


/******************************************************************************
*                                  JOBS/TASKS                                 *
******************************************************************************/
//some labels are already translated above
//Label of Checkboxes
_translations['Show tasks']                                = 'Visualiser Instances';

//Label and Options of Selectbox
_translations['All jobs']                                  = 'Toutes les tâches';
_translations['Standalone jobs']                           = 'Tâches indépendantes';
_translations['Order jobs']                                = 'Tâches d\'ordre';
_translations['with state']                                = 'dans l\'état';
_translations['(all)']                                     = '(tout)';
_translations['(other)']                                   = '(autre)';
_translations['running or stopped']                        = 'en cours d\'exécution ou arrêté';
_translations['and with process class']                    = 'et avec la classe de processus';
                                                           
//Label and Options in Selectbox of jobs sort                        
_translations['Sort jobs by']                              = 'Trier les tâches par';
_translations['(unsorted)']                                = '(non trié)';
_translations['name in ascending order']                   = 'Nom (ordre croissant)';
_translations['name in descending order']                  = 'Nom (ordre décroissant)';
_translations['next start time in ascending order']        = 'Prochaine exécution (ordre  croissant)';
_translations['next start time in descending order']       = 'Prochaine exécution (ordre décroissant)';
_translations['state in ascending order']                  = 'Etat (ordre croissant)';
_translations['state in descending order']                 = 'Etat (ordre décroissant)';

//Title of images
_translations['toggle to']                                 = 'passer à'; 
_translations['list view']                                 = 'voir la liste'; 
_translations['tree view']                                 = 'voir l\'arbre';  
_translations['open all folders']                          = 'ouvrir tous les repertoires';
_translations['close all folders']                         = 'fermer tous les repertoires';
                                                           
//Label and Options of Selectbox of jobs filtering                     
_translations['Filter jobs by']                            = 'Tâches triées par';
_translations['(no filtering)']                            = '(pas de filtre)';
                                                           
//Table header of jobs list                                
_translations['Job']                                       = 'Tâche';
_translations['Steps']                                     = 'Etapes';
_translations['Process steps']                             = 'Etapes du processus';
_translations['Next start']                                = 'Prochain démarrage';
                                                           
//Labels in detail frame                                   
_translations['JOB']                                       = 'TACHES';
_translations['TASK']                                      = 'INSTANCE DE TACHE';
_translations['File timestamp']                            = 'Timestamp fichier';
_translations['State text']                                = 'Texte de l\'état';
_translations['Error']                                     = 'Erreur';
_translations['Error in configuration file']               = 'Erreur dans le fichier de configuration';
_translations['Error in changed file']                     = 'Erreur dans le fichier modifié';
_translations['(not loaded)']                              = '(non chargé)';
_translations['Removing delayed']                          = 'Suppression repoussée';
_translations['Tasks']                                     = 'Instance de tâche';
_translations['Run time defined by']                       = 'Date de démarrage définie par';
_translations['orders to process']                         = 'ordres à exécuter';
_translations['Mail subject']                              = 'Sujet';
_translations['To']                                        = 'A';
_translations['CC']                                        = 'CC';
_translations['BCC']                                       = 'BCC';
_translations['From']                                      = 'De';
_translations['SMTP']                                      = 'SMTP';
_translations['Last error']                                = 'Dernière erreur';
_translations['Last warning']                              = 'Dernier Avertissement';
_translations['Log level']                                 = 'Niveau de log';
_translations['mail on error']                             = 'Email si erreur';
_translations['mail on warning']                           = 'Email si avertissement';
_translations['mail on success']                           = 'Email si succès';
_translations['mail on process']                           = 'Email à l\'exécution';
_translations['start cause']                               = 'Cause du démarrage';
_translations['waiting for order']                         = 'En attente d\'un ordre';
_translations['Idle since']                                = 'En attente depuis';
_translations['In process since']                          = 'En cours de traitement depuis';
_translations['Running since']                             = 'En cours d\'exécution depuis';
_translations['Enqueued at']                               = 'En File d\'attente à';
_translations['Subprocesses']                              = 'Sous-Processus';
                                                           
//Task queue                                               
_translations['enqueued tasks']                            = 'Instances des tâches en file d\'attente';
_translations['Id']                                        = 'ID';
_translations['Enqueued']                                  = 'En file d\'attente';
_translations['Start at']                                  = 'Démarrer à';
_translations['Delete']                                    = 'Supprimer';
                                                           
                                                           
//Task history                                             
_translations['No tasks in the history']                   = 'Pas d\'instance de tâche dans l\'historique';
_translations['Started']                                   = 'Démarré';
_translations['Cause']                                     = 'Cause';
_translations['Ended']                                     = 'Terminé';
_translations['Duration']                                  = 'Durée';
                                                          
//Order queue                                              
_translations['Start']                                     = 'Démarrer';
                                                           
                                                           
//Title of Tabs                                            
_translations['Task Queue']                                = 'File des Instances';
_translations['Task History']                              = 'Historique des Instances';
_translations['Order Queue']                               = 'File des Ordres';
                                                           
                                                           
//Menu content of job menu                                 
_translations['Job menu']                                  = 'Menu Tâche';
_translations['Show configuration']                        = 'Visualiser configuration';
_translations['Show description']                          = 'Visualiser description';
_translations['Show documentation']                        = 'Visualiser documentation';
_translations['Show dependencies']                         = 'Visualiser dépendance';
_translations['Start task immediately']                    = 'Lancer instance immédiatement';
_translations['Start task unforced now']                   = 'Lancer instance non forcé immédiatement';
_translations['Start task at']                             = 'Lancer instance à ...';
_translations['Start task parametrized']                   = 'Lancer instance avec paramètres';
_translations['Set run time']                              = 'Programmer';
_translations['Stop']                                      = 'Désactiver';
_translations['Unstop']                                    = 'Activer';
_translations['Reread']                                    = 'Relire';
_translations['End tasks']                                 = 'Terminer instances';
_translations['Suspend tasks']                             = 'Suspendre instances';
_translations['Continue tasks']                            = 'Continuer instances';
_translations['Delete job']                                = 'Supprimer tâche';
                                                           
                                                           
//Menu content of task menu                                
_translations['Task menu']                                 = 'Menu Instance';
_translations['End']                                       = 'Terminer';
_translations['Kill immediately']                          = 'Tuer immédiatement';
                                                           
                                                           
//Mouseover-Title                                          
_translations['show job details']                          = 'Voir les détails de la tâche';
_translations['show task details']                         = 'Voir les détails de l\'instance';
_translations['Order is deleted']                          = 'L\'ordre est supprimé';
_translations['Order is replaced']                         = 'L\'ordre est remplacé';
                                                           
//misc.                          
_translations['Without start time']                        = 'Sans heure de démarrage';
_translations['Task']                                      = 'Instance';
_translations['No jobs found']                             = 'Aucune tâche trouvé';
                                                           
                                                           
//Job/Task states                                          
_translations['pending']                                   = 'en attente';
_translations['loaded']                                    = 'chargé';
_translations['initialized']                               = 'initialisé';
_translations['not_initialized']                           = 'non initialisé';
_translations['disabled']                                  = 'inactif';
_translations['none']                                      = 'aucun';
_translations['read_error']                                = 'erreur de lecture';
_translations['delayed after error']                       = 'retardé après erreur';
_translations['needs process']                             = 'en attente de processus';
_translations['needs lock']                                = 'en attente de verrou';
_translations['loading']                                   = 'chargement';
_translations['waiting_for_process']                       = 'en attente de process';
_translations['opening']                                   = 'ouverture';
_translations['opening_waiting_for_locks']                 = 'ouverture en attente de verrou';
_translations['running_process']                           = 'démarrage processus';
_translations['running_remote_process']                    = 'démarrage processus distant';
_translations['running_waiting_for_order']                 = 'démarrage en attente d\'ordre';
_translations['running_waiting_for_locks']                 = 'démarrage en attente de verrou';
_translations['running_delayed']                           = 'démarrage retardé';
_translations['suspended']                                 = 'suspendu';
_translations['ending_waiting_for_subprocesses']           = 'en cours d\'arrêt en attente de sous-processus';
_translations['ending']                                    = 'en cours d\'arrêt';
_translations['on_success']                                = 'si succès';
_translations['on_error']                                  = 'si erreur';
_translations['exit']                                      = 'sortir';
_translations['error']                                     = 'erreur';
_translations['release']                                   = 'en cours d\'arrêt';
_translations['ended']                                     = 'terminé';
_translations['deleting_files']                            = 'suppression fichiers StdIn, StdOut und StdErr';
_translations['closed']                                    = 'fermé';
_translations['Replacement is standing by']                = 'Le remplacement va être effectué';
_translations['changed file has error']                    = 'le fichier modifié comporte une erreur';
_translations['not an order job']                          = 'n\'est pas tâche d\'ordre';
_translations['is incorrect or missing']                   = 'est incorrecte ou manquante';
_translations['non-excl.']                                 = 'non-excl';
_translations['is being replaced']                         = 'est en cours de remplacement';
_translations['undefined']                                 = 'non défini';


                                                           
//Task start causes                                        
_translations['none']                                      = 'aucun';
_translations['period_once']                               = 'durée_une_fois';
_translations['period_single']                             = 'durée_seule';
_translations['period_repeat']                             = 'durée_répétition';
_translations['queue']                                     = 'file';
_translations['queue_at']                                  = 'file ';
_translations['directory']                                 = 'repertoire';
_translations['delay_after_error']                         = 'retardé après erreur';
_translations['order']                                     = 'ordre';
_translations['wake']                                      = 'réveil';                                                     


/******************************************************************************
*                                 JOB CHAINS                                  *
******************************************************************************/
//some labels are already translated above
//Label of Checkboxes and Selectboxes
_translations['Job chains containing jobs']                = 'Voir les chaînes des tâches contenant des tâches';
_translations['Show order history']                        = 'Voir l\'historique des ordres';
_translations['Show jobs']                                 = 'Voir les tâches';
_translations['Show orders']                               = 'Voir les ordres';
                                                           
//Label of Selectbox of job chains sort                    
_translations['Sort job chains by']                        = 'Trier les châines des tâches par';
                                                           
//Label of Selectbox of job chains filtering               
_translations['Filter job chains by']                      = 'Filtrer les châines des tâches par';
                                                           
//Table header of job chains list                          
_translations['Order state']                               = 'Etat de l\'ordre';
_translations['Job chain']                                 = 'Chaîne des tâches';
_translations['Job state']                                 = 'Etat de la tâche';
                                                           
//Table header in detail frame                             
_translations['JOB CHAIN']                                 = 'CHAINE DES TACHES';
_translations['ORDER HISTORY']                             = 'HISTORIQUE DES ORDRES';
                                                           
//Menu content of job chain menu                           
_translations['Create job chain']                          = 'Créer une chaîne des tâches';
_translations['Job chain menu']                            = 'Menu Chaîne des Tâches';
_translations['Job node menu']                             = 'Menu Tâche Noeud';
_translations['Add order']                                 = 'Ajouter un ordre';
_translations['Add persistent order']                      = 'Ajouter un ordre persistent';
_translations['Delete job chain']                          = 'Supprimer une chaîne des tâches';
_translations['Unskip']                                    = 'Annuler la saute';
_translations['Skip node']                                 = 'Skip le noeud';
_translations['Stop node']                                 = 'Désactiver le noeud';
_translations['Stop job']                                  = 'Désactiver la tâche';
_translations['Unstop job']                                = 'Activer la tâche';

//Mouseover-Title
_translations['show job chain details']                    = 'Voir les détails de la chaîne des tâches';

//job chain (node) states
_translations['under_construction']                        = 'en construction';
_translations['finished']                                  = 'fini';
_translations['removing']                                  = 'en cours de suppression';
_translations['Node is stopped']                           = 'Le noeud est arrété';
_translations['Node is skipped']                           = 'Le noeud est sauté';

//misc.                                                          
_translations['No job chains found']                       = 'Aucune chaîne des tâches trouvé';
_translations['Web service']                               = 'Web service';
_translations['file orders']                               = 'ordres des fichiers';
_translations['pattern']                                   = 'modèle';
_translations['delay']                                     = 'retard';
_translations['repeat']                                    = 'répeter';
_translations['blacklist']                                 = 'liste noire';

/******************************************************************************
*                         ORDERS/ORDERQUEUE/BLACKLIST                         *
******************************************************************************/
//some labels are already translated above
//Label of Selectbox of orders sort
_translations['Sort orders by']                            = 'Trier les ordres par';
                                                           
//Options in Selectbox of orders sort                      
_translations['job chain in ascending order']              = 'Chaînes des tâches triées par ordre croissant';
_translations['job chain in descending order']             = 'Chaînes des tâches triées par ordre décroissant';
                                                           
//Label of Selectbox of jobs filtering                     
_translations['Filter orders by']                          = 'Filtrer les ordres par';
                                                                                                                                                                                 
//Mouseover-Title                                          
_translations['Order is processed by Scheduler member']    = 'L\'ordre est traité par le membre de JobScheduler';
_translations['show order details']                        = 'Voir les détails de l\'ordre';
_translations['This order is a replacement for another order with the same ID'] = 'Cet ordre est un remplaçant d\'un autre ordre avec la même identification';
                                                           
//Detail frame                                             
_translations['ORDER']                                     = 'ORDRE';
                                                                                                                     
//Menu content of order menu                               
_translations['Order menu']                                = 'Menu Ordre';
_translations['Start order now']                           = 'Lancer l\'ordre immédiatement';
_translations['Start order at']                            = 'Lancer l\'ordre à';
_translations['Start order parametrized']                  = 'Lancer l\'ordre avec des paramètres';
_translations['Set order state']                           = 'Positionner l\'état de l\'ordre';
_translations['Suspend order']                             = 'Suspendre l\'ordre';
_translations['Resume order']                              = 'Reprendre l\'ordre';
_translations['Reset order']                               = 'Réinitialiser l\'ordre';
_translations['Delete order']                              = 'Supprimer l\'ordre';
_translations['Remove setback']                            = 'Supprimer le report';

//misc
_translations['No orders found']                           = 'Aucun ordre trouvé';
_translations['Processed by']                              = 'Traité par';
_translations['Order']                                     = 'Ordre';
_translations['Setback']                                   = 'Reporté';
_translations['deleted']                                   = 'supprimé';
_translations['Replacement']                               = 'Remplacement';
_translations['currently processed by']                    = 'actuellement traité par'
_translations['on blacklist']                              = 'sur la liste noire'
                                                           

                                                           
//order history                                            
_translations['No orders in the history']                  = 'Aucun ordre dans l\'historique';


/******************************************************************************
*                                PROCESS CLASS                                *
******************************************************************************/
//some labels are already translated above
_translations['Process class']                             = 'Classe des processus';
                                                           
//Table header of process class list                       
_translations['Operations']                                = 'Opérations';
_translations['Callbacks']                                 = 'Callbacks';
_translations['Current operation']                         = 'Opération actuelle';
                                                           
_translations['(default)']                                 = '(défaut)';
_translations['max processes']                             = 'processus max.';
_translations['Remote Scheduler']                          = 'JobScheduler distant';

_translations['No process classes found']                  = 'Aucune classe des processus trouvé';


/******************************************************************************
*                                   CLUSTER                                   *
******************************************************************************/
//some labels are already translated above
//Table header of process class list
_translations['Last heart beat']                           = 'Dernier \'heart beat\'';
_translations['Detected heart beats']                      = '\'heart beats\' détectés';
_translations['Backup precedence']                         = 'Priorité sauvegarde';
                                                           
//Menu content of cluster member                           
_translations['Cluster member menu']                       = 'Menu Membre de Cluster';
_translations['Delete entry']                              = 'Entrée supprimée';
_translations['Restart']                                   = 'Redémarrage';

//misc.
_translations['active Scheduler(s)']                       = 'JobScheduler(s) actif';
_translations['exclusive Scheduler(s)']                    = 'JobScheduler(s) exclusif(s)';
_translations['This Scheduler is active']                  = 'Ce JobScheduler est actif';
_translations['and exclusive']                             = 'et exclusif';
_translations['Only non-backup Schedulers are allowed to start operation.'] = 'Uniquement les JobSchedulers qui ne font pas de sauvegardes sont autorisés à lancer des opérations.';
_translations['This Scheduler']                            = 'Ce JobScheduler';
_translations['(was active!)']                             = '(était actif!)';
_translations['active']                                    = 'actif';
_translations['inactive']                                  = 'inactif';
_translations['distributed orders']                        = 'ordres distribués';
_translations['exclusive']                                 = 'exclusif';
_translations['backup']                                    = 'sauvegarde';
_translations['still checking...']                         = 'en cours de vérification...';
_translations['dead']                                      = 'mort';
_translations['discovered']                                = 'découvert';
_translations['after']                                     = 'après';
_translations['Deactivated by']                            = 'Désactivé par';
_translations['ago']                                       = 'il y a';



/******************************************************************************
*                               REMOTE SCHEDULER                              *
******************************************************************************/
//some labels are already translated above
                                                           
//Table header of process class list                       
_translations['IP']                                        = 'IP';
_translations['Hostname']                                  = 'Nom d\'hôte';
_translations['Port']                                      = 'Port';
_translations['Last Update']                               = 'Dernière mise à jour';
_translations['Connected']                                 = 'Connecté';
_translations['Disconnected']                              = 'Déconnecté';
_translations['Version']                                   = 'Version';
                                                           
//misc.                                                    
_translations['Scheduler(s)']                              = 'JobScheduler(s)';
_translations['connected']                                 = 'connecté';



/******************************************************************************
*                                    LOCKS                                    *
******************************************************************************/
//some labels are already translated above
_translations['No locks found']                            = 'Aucun verrou trouvé';
_translations['Lock']                                      = 'Verrou';
                                                           
//Table header of process class list                       
_translations['non-exclusively locked']                    = 'verrouillé de manière non-exclusive';
_translations['locked']                                    = 'verrouillé';
_translations['free']                                      = 'libre';

//misc.
_translations['Holders (non-exclusive)']                   = 'Titulaire (non-exclusif)';
_translations['Holders (exclusive)']                       = 'Titulaire (exclusif)';
_translations['Waiting jobs (exclusive)']                  = 'En attente des tâches (exclusif)';
_translations['Waiting jobs (non-exclusive)']              = 'En attente des tâches (non-exclusif)';

//Mouseover-Title
_translations['Lock is not available, it is locked']       = 'Le verrou n\'est pas disponible, il est déjà utilisé';
_translations['Lock is available']                         = 'Le verrou est disponible';



/******************************************************************************
*                                LAST ACTIVITIES                              *
******************************************************************************/
//some labels are already translated above
//radio buttons, checkboxes
_translations['Show all']                                  = 'Voir tout';
_translations['Show only orders']                          = 'Voir uniquement les ordres';
_translations['Show only tasks']                           = 'Voir uniquement les tâches';
_translations['Show only faulty tasks and orders']         = 'Voir uniquement les tâches et les ordres en erreur';
_translations['Show last tasks error']                     = 'Voir les dernières instances échouées';

//Table header of job chains list                          
_translations['Job name']                                  = 'Nom de la tâche';
_translations['Exitcode']                                  = 'Code de sortie';
_translations['Order ID']                                  = 'ID Ordre';
_translations['Order state']                               = 'Etat de l\'ordre';

//Button title
_translations['Show order log']                            = 'Voir le log de l\'ordre';
_translations['Show task log']                             = 'Voir le log de l\'instance';

//misc.
_translations['No last activities found']                  = 'Aucune activité récente trouvé';


/******************************************************************************
*                                   SCHEDULES                                 *
******************************************************************************/
//some labels are already translated above

//Menu content of schedules                               
_translations['Create schedule']                           = 'Créer une programmation ';
_translations['Schedule menu']                             = 'Menu Programmations';
_translations['Substitute menu']                           = 'Menu Substituts';
_translations['Add substitute']                            = 'Ajouter un substitut';
_translations['Edit schedule']                             = 'Editer la programmation';
_translations['Delete schedule']                           = 'Supprimer la programmation';

//Table header of job chains list                          
_translations['Schedule']                                  = 'Programmation';
_translations['Valid from']                                = 'Valable de';
_translations['Valid to']                                  = 'Valable pour';
_translations['Substituted by']                            = 'Substitué par';

//Mouseover-Title
_translations['show schedule details']                     = 'voir les détails de la programmation';

//State
_translations['incomplete']                                = 'incomplète';

//misc.
_translations['Green']                                     = '';
_translations['marked schedules are currently active']     = 'Les programmations en vert sont actuellement actives';
_translations['No schedules found']                        = 'Aucune programmation trouvé';
_translations['Used by jobs']                              = 'Utilisé par les tâches';
_translations['Used by orders']                            = 'Utilisé par les ordres';
_translations['of job chain']                              = 'd\'une chaîne des tâches';
_translations['Substituted schedule']                      = 'Programmation substituée';

//Detail frame                                             
_translations['SCHEDULE']                                  = 'PROGRAMMATION';
_translations['Valid']                                     = 'Valable';


/******************************************************************************
*                                LOG CATEGORIES                               *
******************************************************************************/
//some labels are already translated above
_translations['LOG CATEGORIES']                            = 'CATEGORIES DE LOG';
                                                           
//misc.                                                    
_translations['The default log caregories are marked']     = 'Les catégories de log sont marquées';
_translations['orange']                                    = 'orange';
_translations['and they are active after each reset.']     = 'et elles sont actives après chaque réinitialisation.';
_translations['Current log categories setting']            = 'Paramétrage actifs des catégories de log';
_translations['Set']                                       = 'Activer';
_translations['log categories for a duration of']          = 'catégories de log pour une durée de';
_translations['seconds']                                   = 'secondes';
_translations['log categories are updated']                = 'les catégories de log sont à jour';
_translations['reset is executed']                         = 'la réinitialisation est lancée';
_translations['reset is executed after']                   = 'la réinitialisation est lancée après';
_translations['Next reset']                                = 'Réinitialisation suivante';
_translations['implicit']                                  = 'implicite';
_translations['explicit']                                  = 'explicite';

//Table header of log categories list
_translations['Category']                                  = 'Catégorie';
_translations['Mode']                                      = 'Mode';
_translations['Description']                               = 'Description';

_translations['No log categories found']                   = 'Aucune catégorie de log trouvé';


/******************************************************************************
*                                  CALENDAR                                   *
******************************************************************************/

_translations['CALENDAR']                                  = 'CALENDRIER';
_translations['job']                                       = 'tâche';
_translations['job chain']                                 = 'chaîne des tâches';
_translations['of job chain']                              = 'de la chaîne des tâches';
_translations['all jobs']                                  = 'toutes les tâches';
_translations['all jobs and orders']                       = 'toutes les tâches et tous les ordres';
_translations['Start times for']                           = 'Date de lancement pour';
_translations['by calling']                                = 'en appelant';
_translations['with output format']                        = 'avec le format de sortie';
_translations['at']                                        = 'à';


/******************************************************************************
*                                  FILTER                                     *
******************************************************************************/

_translations['FILTER ADMINISTRATION']                     = 'ADMINISTRATION DES FILTRES';
_translations['close']                                     = 'fermer';
_translations['store']                                     = 'enregistrer';
_translations['store as ...']                              = 'enregistrer sous ...';
_translations['remove']                                    = 'supprimer';
_translations['quick check']                               = 'vérification rapide';
_translations['regular expression for quick check']        = 'expression régulière pour une vérification rapide';
_translations['select all']                                = 'sélectionner tout';
_translations['deselect all']                              = 'désélectionner tout';
_translations['new filter']                                = 'nouveau filtre';
_translations['filter was stored']                         = 'le filtre a été enregistré';
_translations['new filter was stored']                     = 'le nouveau filtre a été enregistré';
_translations['filter was removed']                        = 'le filtre a été supprimé';
_translations['Filter name must be stated']                = 'Un filtre doit être renseigné';
_translations['Using\n\n|\n=>\n;\n\nis not allowed']       = 'Utiliser\n\n|\n=>\n;\n\nest interdit';
_translations['A filter "$filter_name" is already defined in the file $file.'] = 'Le même nom de filtre "$filter_name" est déjà utilisé par le fichier $file.';
_translations['The same filter name "$filter" was already defined\n\nDo you want nevertheless to store it ?'] = 'Le même nom de filtre "$filter" est déjà utilisé.\nToutefois voulez-vous l\'enregistrer ?'; 


/******************************************************************************
*                                   ERROR                                     *
******************************************************************************/

_translations['source']                                    = 'source';
_translations['line']                                      = 'ligne';
_translations['column']                                    = 'colonne';
_translations['unknown error']                             = 'erreur inconnue';


/******************************************************************************
*                           DIALOGS OF MENU FUNCTIONS                         *
******************************************************************************/

//Buttons
_translations['submit']                                    = 'soumettre';
_translations['cancel']                                    = 'annuler';
_translations['new param']                                 = 'nouveau paramètre';
_translations['run time editor']                           = 'Editeur de dates de lancement';

//Plausi
_translations['$field must be stated!']                    = '$field doit être renseigné!';
_translations['Period from ($field) is invalid date or before 1970-01-01.'] = 'La durée de ($field) n\'est pas une date valide ou est antérieur au 01/01/1970.';
_translations['Max. hits ($field) is not a positive number.'] = 'Nombre d\'occurrences max. ($field) n\'est pas un entier positif.';
_translations['Please add an existing order job to the job chain nodes.'] = 'S\'il vous plait, ajoutez un ordre existant à la chaîne des tâches.';
_translations['Please select an existing order job on the left hand side.'] = 'S\'il vous plait, ajoutez un ordre existant du coté gauche.';

//Plausi fields
_translations['Start time']                                = 'Date de lancement';
_translations['Name']                                      = 'Nom';

//Scheduler settings
_translations['The following values will be stored in a cookie. If no cookies are available the values which are set in <code>./custom.js</code> are used.'] = 'Les valeurs suivantes seront enregistrées dans un cookie. Si aucun cookie n\'est disponible, les valeurs qui sont utilisées sont celles du fichier <code>./custom.js</code>.';
_translations['Onload Values']                             = 'Valeurs au démarrage';
_translations['periodically every']                        = 'périodiquement tous les';
_translations['Tabs']                                      = 'Onglets';
_translations['Switch to']                                 = 'Enchaîner sur';
_translations['as the beginning view']                     = 'comme vue de départ';
_translations['View Mode']                                 = 'Mode de voir';
_translations['for']                                       = 'pour les';
_translations['Selects, Checkboxes and Radios']            = 'Selects, Checkboxes et Radios';
_translations['in the <i>jobs</i> tab']                    = 'dans l\'onglet des <i>Tâches</i>';
_translations['in the <i>job chains</i> tab']              = 'dans l\'onglet des <i>Chaînes des Tâches</i>';
_translations['in the <i>job chain</i> details']           = 'dans les détails des <i>Chaînes des Tâches</i>';
_translations['in the <i>last activities</i> tab']         = 'dans l\'onglet des <i>Activités Récentes</i>';
_translations['all orders and tasks']                      = 'tous les ordres et les instances';
_translations['only tasks']                                = 'seulement les instance';
_translations['only orders']                               = 'seulement les ordres';
_translations['Runtime Values']                            = 'Paramètres de démarrage';
_translations['Limits']                                    = 'Limites';
_translations['Max. number of orders per job chain']       = 'Nombre maximum d\'ordres par chaîne des tâches';
_translations['Max. number of last activities']            = 'Nombre maximum d\'activités récentes';
_translations['Max. number of history entries per order']  = 'Nombre maximum d\'entrées par ordre dans l\'historique';
_translations['Max. number of history entries per task']   = 'Nombre maximum d\'entrées par instance dans l\'historique';
_translations['Terminate within']                          = 'Terminer dans la durée';
_translations['Max. seconds within the JobScheduler terminates'] = 'Durée maximum avant que JobScheduler se termine';
_translations['Dialogs']                                   = 'Dialogues';
_translations['Default start time in the &quot;<i>Start task/order at</i> &quot;-Dialog is <i>now</i>'] = 'Date de lancement par défaut pour &quot;<i>Lancer instance/ordre à</i>&#160;&quot; est <i>now</i>';
_translations['Debugging of the operations GUI']           = 'Déverminage d\'operations GUI ';
_translations['Level']                                     = 'Niveau';

//start task/order
_translations['Start task $task']                          = 'Lancer l\'instance $task';
_translations['Start order $order']                        = 'Lancer l\'ordre $order';
_translations['<b>Enter a start time</b><span class="small"> in ISO format &quot;yyyy-mm-dd HH:MM[:SS]&quot; or &quot;now&quot;. The time at which a task is to be started &lt;run_time&gt; is deactivated. Relative times - &quot;now + HH:MM[:SS]&quot; and &quot;now + SECONDS&quot; - are allowed.<span>' ] = '<b>Rentrer une date de lancement</b><span class="small"> au format ISO  &quot;yyyy-mm-dd HH:MM[:SS]&quot; ou &quot;now&quot;.  La date à laquelle l\'instance devrait être lancée selon son &lt;run_time&gt; sera désactivée. Les dates relatives - &quot;now + HH:MM[:SS]&quot; et &quot;now  + SECONDES&quot; - sont acceptées.</span>';
_translations['<b>Enter a run time</b> or use the $editor'] = '<b>Rentrez une date de lancement</b> ou utilisez <span style="display:inline-block;width:100%;text-align:right;">$editor</span>';
_translations['Start enforced']                            = 'Lancement forcé';
_translations['Change parameters']                         = 'Changer des paramètres';
_translations['Declare parameters']                        = 'Déclarer des paramètres';
_translations['Declare new parameters']                    = 'Déclarer de nouveaux paramètres';
_translations['name']                                      = 'nom';
_translations['value']                                     = 'valeur';


//add order
_translations['Add order to $job_chain']                   = 'Ajouter un ordre à $job_chain';
_translations['Enter an order id']                         = 'Id ordre';
_translations['Enter an order title']                      = 'Titre de l\'ordre';
_translations['Select an order state']                     = 'Etat de l\'ordre';
_translations['Select an order end state']                 = 'Etat de fin de l\'ordre';
_translations['In order to store this order in a hot folder you have to state an order id.\nYour order will only be stored permanently, however, it is valid for\nthe lifetime of this JobScheduler session. Do you want to continue?'] = 'Dans le but d\'enregistrer cet ordre dans le \"Hot Folder\" vous devez paramétrer un id d\'ordre.\nVotre ordre sera enregistré de manière permanent mais il sera valide\n pour la durée de validité de la session JobScheduler. Voulez-vous continuer?';


//set order state
_translations['Set order state of $order']                 = 'Fixez l\'état de $order';
_translations['<b>Select a new order state</b>']           = '<b>Choisissez un nouvel état de l\'ordre.</b>';
_translations['<span class="small">The current order state is $state.</span>'] = '<span class="small">L\'état actuel de l\'ordre est $state.</span>';
_translations['<b>Select a new order end state</b>']       = '<b>Sélectionnez un nouvel état de fin pour l\'ordre.</b>';
_translations['<span class="small">The current order end state is $state.</span>'] = '<span class="small">L\'état actuel de fin de l\'ordre est $state.</span>';

//set run time
_translations['Set run time of $job']                      = 'Fixez une date de lancement de $job';
_translations['or choose a schedule']                      = 'ou choisissez une programmation';

//schedules
_translations['Add schedule']                              = 'Ajouter une programmation';
_translations['Add substitute for $schedule']              = 'Ajouter un substitut pour $schedule';
_translations['Edit $schedule']                            = 'Modifier $schedule';
_translations['Enter a schedule name']                     = 'Nom de la programmation';


//calendar
_translations['Parameterize the start times list']         = 'Paramétrez la liste des dates de démarrage';
_translations['<b>Set the period</b>']                     = '<b>Fixez la période</b>';
_translations['<span class="small">(Timestamps in ISO format yyyy-mm-dd[ hh:mm:ss])</span>']  = '<span class="small">(Date au format ISO yyyy-mm-dd[ hh:mm:ss])</span>';
_translations['...from']                                   = '...de';
_translations['...to']                                     = '...pour';
_translations['Max. hits']                                 = 'Nombre  max. d\'occurrences';
_translations['Output format']                             = 'Format de sortie';
_translations['Include order start times']                 = 'Ajoutez les dates de démarrage d\'ordre';

//job chain
_translations['Sorry, but this feature is only supported for\nJobScheduler version 2.0.204.5774 or higher'] = 'Désolé, cette fonctionnalité est supportée à partir de la version 2.0.204.5774 de JobScheduler';
_translations['Modify job chain']                          = 'Modifier la chaîne des tâches';
_translations['Now you can modify the job chain by editing the text area content.'] = 'Dorénavant vous pouvez modifier la chaîne des tâches en éditant le contenu du champ texte.';
_translations['Orders are stored in the database (orders_recoverable)'] = 'Les ordres sont stockés en base de données (orders_recoverable)';
_translations['Existing order jobs']                       = 'Tâches d\'ordre existants';
_translations['Job chain nodes']                           = 'Noeuds de la chaîne des tâches';
_translations['Enter a folder']                            = 'Repertoire';
_translations['Enter a job chain name']                    = 'Nom de la chaîne des tâches';
_translations['Enter a job chain title']                   = 'Titre de la chaîne des tâches';


/******************************************************************************
*                           JOB DESCRIPTION                                   *
******************************************************************************/

_translations['The JobScheduler $scheduler has no jobs with a description.']               = 'The JobScheduler $scheduler has no jobs with a description.';
_translations['Because of a security sanction of your browser you must reload this site!'] = 'Because of a security sanction of your browser you must reload this site!';
_translations['Please select a job to display its description.']                           = 'Please select a job to display its description.';
_translations['Please enter a job name to display its description.']                       = 'Please enter a job name to display its description.';


/******************************************************************************
*                           CONFIRMS                                          *
******************************************************************************/

_translations['Do you really want to terminate the JobScheduler?']          = 'Voules-vous vraiment arrêter le JobScheduler?';
_translations['Do you really want to restart the JobScheduler?']            = 'Voules-vous vraiment redémarrer le JobScheduler?';
_translations['Do you really want to abort the JobScheduler?']              = 'Voules-vous vraiment avorter le JobScheduler?';
_translations['Do you really want to pause the JobScheduler?']              = 'Voules-vous vraiment mettre en pause?';
_translations['Do you really want to continue the JobScheduler?']           = 'Voules-vous vraiment continuer le JobScheduler?';
_translations['Do you really want to terminate the JobScheduler cluster?']  = 'Voules-vous vraiment arrêter le JobScheduler cluster?';
_translations['Do you really want to restart the JobScheduler cluster?']    = 'Voules-vous vraiment redémarrer le JobScheduler cluster?';
_translations['Do you really want to delete the dead entry?']               = 'Voules-vous vraiment supprimer la entrée morts?';

_translations['Do you really want to stop this job chain?']                 = 'Voulez-vous vraiment désactiver cette chaîne des tâches?';
_translations['Do you really want to unstop this job chain?']               = 'Voulez-vous vraiment activer cette chaîne des tâches?';
_translations['Do you really want to delete this job chain?']               = 'Voulez-vous vraiment supprimer cette chaîne des tâches?';
_translations['Do you really want to stop this job chain node?']            = 'Voulez-vous vraiment désactiver cette chaîne des tâches?';
_translations['Do you really want to unstop this job chain node?']          = 'Voulez-vous vraiment activer cette chaîne des tâches?';
_translations['Do you really want to skip this job chain node?']            = 'Voulez-vous vraiment sauter cette noeud de chaîne des tâches?';
_translations['Do you really want to unskip this job chain node?']          = 'Voulez-vous vraiment annuler la saute cette noeud de chaîne des tâches?';

_translations['Do you really want to start this job?']                      = 'Voules-vous vraiment lancer cette tâche?';
_translations['Do you really want to stop this job?']                       = 'Voules-vous vraiment désactiver cette tâche?';
_translations['Do you really want to unstop this job?']                     = 'Voules-vous vraiment activer cette tâche?';
_translations['Do you really want to delete this job?']                     = 'Voules-vous vraiment supprimer cette tâche?';
_translations['Do you really want to kill this task?']                      = 'Voules-vous vraiment tuer cette instance?';
_translations['Do you really want to delete this task?']                    = 'Voules-vous vraiment supprimer cette instance?';
_translations['Do you really want to end this task?']                       = 'Voules-vous vraiment terminer cette instance?';
_translations['Do you really want to end the tasks?']                       = 'Voules-vous vraiment terminer instances?';
_translations['Do you really want to suspend the tasks?']                   = 'Voules-vous vraiment suspendre instances?';
_translations['Do you really want to continue the tasks?']                  = 'Voules-vous vraiment continuer instances?';

_translations['Do you really want to start this order?']                    = 'Voules-vous vraiment lancer cet ordre?';
_translations['Do you really want to add an order?']                        = 'Voules-vous vraiment ajouter un ordre?';
_translations['Do you really want to reset this order?']                    = 'Voules-vous vraiment lancer cet ordre?';
_translations['Do you really want to suspend this order?']                  = 'Voules-vous vraiment suspendre cet ordre?';
_translations['Do you really want to resume this order?']                   = 'Voules-vous vraiment reprendre cet ordre?';
_translations['Do you really want to change the order state?']              = 'Voulez-vous vraiment modifier l\'etat de l\'ordre?';
_translations['Do you really want to delete this order?']                   = 'Voulez-vous vraiment supprimer cet ordre?';
_translations['Do you really want to remove the setback?']                  = 'Voulez-vous vraiment supprimer le report?';

_translations['Do you really want to set the run time?']                    = 'Voulez-vous vraiment modifier la date de démarrage?';
_translations['Do you really want to add a substituting schedule?']         = 'Voulez-vous vraiment ajouter un substitut?';
_translations['Do you really want to modify this schedule?']                = 'Voulez-vous vraiment modifier cette programmation?';
_translations['Do you really want to delete this schedule?']                = 'Voulez-vous vraiment supprimer cette programmation?';

