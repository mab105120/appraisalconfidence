
CREATE TABLE IF NOT EXISTS `ACTIVITY` (
  `ID` varchar(64) NOT NULL,
  `ACTIVITY` varchar(10) NOT NULL,
  `TIME` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `EVAL_ACTIVITY` (
  `ID` varchar(64) NOT NULL,
  `EVAL_CODE` varchar(10) NOT NULL,
  `REVIEW_CODE` varchar(200) NOT NULL,
  `OPEN_TIME` varchar(30) NOT NULL,
  `CLOSE_TIME` varchar(30) NOT NULL,
  `SUBMISSION_ID` decimal(10,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

 CREATE TABLE ABS_EVALUATION (
	ID varchar(64) not null,
    EVAL_CODE varchar(5) not null,
    SUBMISSION_ID numeric not null,
    STD_LEARNING numeric not null,
    INST_PRACTICE numeric not null,
    PROFESSIONALISM numeric not null,
    OVERALL numeric not null,
    PROMOTE tinyint(1) not null,
    REL_CONFIDENCE numeric not null,
    ABS_CONFIDENCE numeric not null,
    COMMENT varchar(300) null,
    OPEN_TIME varchar(30) not null,
    CLOSE_TIME varchar(30) not null
 );
 
 CREATE TABLE `EVALUATION` (
  `ID` varchar(64) NOT NULL,
  `EVAL_CODE` varchar(5) NOT NULL,
  `RECOMMENDATION_PICK` varchar(5) NOT NULL,
  `ABS_CONFIDENCE` decimal(10,0) NOT NULL,
  `REL_CONFIDENCE` decimal(10,0) NOT NULL,
  `COMMENT` varchar(250) NOT NULL,
  `SUBMISSION_ID` decimal(10,0) NOT NULL,
  `OPEN_TIME` varchar(30) DEFAULT NULL,
  `CLOSE_TIME` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`ID`,`SUBMISSION_ID`,`EVAL_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `PERFORMANCE_REVIEW` (
  `ID` varchar(10) NOT NULL,
  `JOB_FUNCTION` varchar(64) NOT NULL,
  `SUPERVISOR` varchar(64) NOT NULL,
  `REVIEW` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `STATUS` (
  `ID` varchar(64) NOT NULL,
  `STATUS` varchar(64) NOT NULL,
  `TIME` timestamp NULL DEFAULT NULL,
  `SUBMISSION_ID` decimal(10,0) NOT NULL,
  PRIMARY KEY (`ID`,`STATUS`,`SUBMISSION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `USER_CONFIDENCE` (
  `ID` varchar(64) NOT NULL,
  `ITEM_CODE` varchar(64) NOT NULL,
  `RESPONSE` decimal(10,0) NOT NULL,
  `SUBMISSION_ID` decimal(10,0) NOT NULL,
  PRIMARY KEY (`ID`,`ITEM_CODE`,`SUBMISSION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `USER_DEMOGRAPHIC` (
  `ID` varchar(64) NOT NULL,
  `AGE` varchar(64) NOT NULL,
  `GENDER` varchar(10) NOT NULL,
  `EDUCATION` varchar(64) NOT NULL,
  `DIVISION` varchar(64) NOT NULL,
  `SUBMISSION_ID` decimal(10,0) NOT NULL,
  `EDUCATION_EXPERIENCE` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`ID`,`SUBMISSION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `USER_EXPERIENCE` (
  `ID` varchar(64) NOT NULL,
  `TITLE` varchar(64) NOT NULL,
  `TOTAL_SUBORDINATES` varchar(10) NOT NULL,
  `PROF_EXPERIENCE` varchar(10) NOT NULL,
  `APPRAISAL_EXPERIENCE` varchar(10) NOT NULL,
  `TOTAL_REVIEWS` varchar(10) NOT NULL,
  `TOTAL_REVIEWEES` varchar(10) NOT NULL,
  `PERSONNEL_SELECTION` varchar(5) NOT NULL,
  `CANDIDATES_REVIEWS` varchar(10) DEFAULT NULL,
  `SUBMISSION_ID` decimal(10,0) NOT NULL,
  `EDUCATION_EMPLOYMENT` varchar(10) DEFAULT NULL,
  `TRAINING_FREQUENCY` varchar(64) DEFAULT NULL,
  `TRAINING_TYPE` varchar(64) DEFAULT NULL,
  `TRAINING_TYPE_COMMENT` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`ID`,`SUBMISSION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `EXPERT_EVAL` (
  `ID` varchar(64) NOT NULL,
  `EVAL_CODE` varchar(5) NOT NULL,
  `STD_LEARNING` decimal(10,2) NOT NULL,
  `INST_PRACTICE` decimal(10,2) NOT NULL,
  `PROFESSIONALISM` decimal(10,2) NOT NULL,
  `OVERALL` decimal(10,2) NOT NULL,
  PRIMARY KEY (`ID`,`EVAL_CODE`)
);