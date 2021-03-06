create table general_advice(
    id bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    currency_pair varchar(10) NOT NULL,
    strategy_period varchar(30) NOT NULL,
    strategy_type varchar(10) NOT NULL,
    strategy_advice varchar(10) NOT NULL,
    strategy_time timestamp not null,
    confirmations int default 0,
    exchange varchar(20) NOT NULL,
    strategy_time_first_occurrence timestamp not null
);