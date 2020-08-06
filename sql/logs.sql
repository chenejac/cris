create table DOWNLOADENTRY
(
   LOGID                                  int NOT NULL AUTO_INCREMENT,
   CONTROLNUMBER                        text   NOT NULL,
   DATEANDTIME                          datetime   NOT NULL,
   EDAY                                 int    NOT NULL,
   EMONTH                               int    NOT NULL,
   EYEAR                                int    NOT NULL,
   EHOURS                               int    NOT NULL,
   EMINUTES                             int    NOT NULL,
   ESECONDS                             int    NOT NULL,
   EDAYOFWEEK                           int    NOT NULL,
   FILEID                               text    NOT NULL,
   FILENAME                             text    NOT NULL,
   SOURCE                               text    NOT NULL,
   REFERRER                             text    NULL,
   IPADDRESS                            text     NOT NULL,
   PROXY                                text      NULL,
   DISSERTATION                         text       NOT NULL,
   primary key (LOGID)
);


create table READMETADATAENTRY
(
   LOGID                                  int NOT NULL AUTO_INCREMENT,
   CONTROLNUMBER                        text   NOT NULL,
   DATEANDTIME                          datetime   NOT NULL,
   EDAY                                 int    NOT NULL,
   EMONTH                               int    NOT NULL,
   EYEAR                                int    NOT NULL,
   EHOURS                               int    NOT NULL,
   EMINUTES                             int    NOT NULL,
   ESECONDS                             int    NOT NULL,
   EDAYOFWEEK                           int    NOT NULL,
   SOURCE                               text    NOT NULL,
   REFERRER                             text    NULL,
   IPADDRESS                            text     NOT NULL,
   PROXY                                text      NULL,
   DISSERTATION                         text       NOT NULL,
   primary key (LOGID)
);