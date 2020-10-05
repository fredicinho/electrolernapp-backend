create table answer (id bigint not null, answerPhrase varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table question (id bigint not null, questionphrase varchar(255), correctAnswer_id bigint, primary key (id)) engine=InnoDB
create table question_possibleAnswers (Question_id bigint not null, possibleAnswers_id bigint not null) engine=InnoDB
alter table question_possibleAnswers add constraint UK_pp1kbu15k9qnm6qv70mllolyl unique (possibleAnswers_id)
alter table question add constraint FKhaja7xeeunpyc6n94shjggutg foreign key (correctAnswer_id) references answer (id)
alter table question_possibleAnswers add constraint FKkwohpr80b3w118b73rs1qat6r foreign key (possibleAnswers_id) references answer (id)
alter table question_possibleAnswers add constraint FKr60f6ccmb60o4jyo1w423jq96 foreign key (Question_id) references question (id)
create table answer (id bigint not null, answerPhrase varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table question (id bigint not null, questionphrase varchar(255), correctAnswer_id bigint, primary key (id)) engine=InnoDB
create table question_possibleAnswers (Question_id bigint not null, possibleAnswers_id bigint not null) engine=InnoDB
alter table question_possibleAnswers add constraint UK_pp1kbu15k9qnm6qv70mllolyl unique (possibleAnswers_id)
alter table question add constraint FKhaja7xeeunpyc6n94shjggutg foreign key (correctAnswer_id) references answer (id)
alter table question_possibleAnswers add constraint FKkwohpr80b3w118b73rs1qat6r foreign key (possibleAnswers_id) references answer (id)
alter table question_possibleAnswers add constraint FKr60f6ccmb60o4jyo1w423jq96 foreign key (Question_id) references question (id)
create table answer (id bigint not null, answerPhrase varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table question (id bigint not null, questionphrase varchar(255), correctAnswer_id bigint, primary key (id)) engine=InnoDB
create table question_possibleAnswers (Question_id bigint not null, possibleAnswers_id bigint not null) engine=InnoDB
alter table question_possibleAnswers add constraint UK_pp1kbu15k9qnm6qv70mllolyl unique (possibleAnswers_id)
alter table question add constraint FKhaja7xeeunpyc6n94shjggutg foreign key (correctAnswer_id) references answer (id)
alter table question_possibleAnswers add constraint FKkwohpr80b3w118b73rs1qat6r foreign key (possibleAnswers_id) references answer (id)
alter table question_possibleAnswers add constraint FKr60f6ccmb60o4jyo1w423jq96 foreign key (Question_id) references question (id)
create table answer (id bigint not null, answerPhrase varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table question (id bigint not null, questionphrase varchar(255), correctAnswer_id bigint, primary key (id)) engine=InnoDB
create table question_possibleAnswers (Question_id bigint not null, possibleAnswers_id bigint not null) engine=InnoDB
alter table question_possibleAnswers add constraint UK_pp1kbu15k9qnm6qv70mllolyl unique (possibleAnswers_id)
alter table question add constraint FKhaja7xeeunpyc6n94shjggutg foreign key (correctAnswer_id) references answer (id)
alter table question_possibleAnswers add constraint FKkwohpr80b3w118b73rs1qat6r foreign key (possibleAnswers_id) references answer (id)
alter table question_possibleAnswers add constraint FKr60f6ccmb60o4jyo1w423jq96 foreign key (Question_id) references question (id)
create table answer (id bigint not null, answerPhrase varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table question (id bigint not null, questionphrase varchar(255), correctAnswer_id bigint, primary key (id)) engine=InnoDB
create table question_possibleAnswers (Question_id bigint not null, possibleAnswers_id bigint not null) engine=InnoDB
alter table question_possibleAnswers add constraint UK_pp1kbu15k9qnm6qv70mllolyl unique (possibleAnswers_id)
alter table question add constraint FKhaja7xeeunpyc6n94shjggutg foreign key (correctAnswer_id) references answer (id)
alter table question_possibleAnswers add constraint FKkwohpr80b3w118b73rs1qat6r foreign key (possibleAnswers_id) references answer (id)
alter table question_possibleAnswers add constraint FKr60f6ccmb60o4jyo1w423jq96 foreign key (Question_id) references question (id)
create table answer (id bigint not null, answerPhrase varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table question (id bigint not null, questionphrase varchar(255), correctAnswer_id bigint, primary key (id)) engine=InnoDB
create table question_possibleAnswers (Question_id bigint not null, possibleAnswers_id bigint not null) engine=InnoDB
alter table question_possibleAnswers add constraint UK_pp1kbu15k9qnm6qv70mllolyl unique (possibleAnswers_id)
alter table question add constraint FKhaja7xeeunpyc6n94shjggutg foreign key (correctAnswer_id) references answer (id)
alter table question_possibleAnswers add constraint FKkwohpr80b3w118b73rs1qat6r foreign key (possibleAnswers_id) references answer (id)
alter table question_possibleAnswers add constraint FKr60f6ccmb60o4jyo1w423jq96 foreign key (Question_id) references question (id)
create table answer (id bigint not null, answerPhrase varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table question (id bigint not null, questionphrase varchar(255), correctAnswer_id bigint, primary key (id)) engine=InnoDB
create table question_possibleAnswers (Question_id bigint not null, possibleAnswers_id bigint not null) engine=InnoDB
alter table question_possibleAnswers add constraint UK_pp1kbu15k9qnm6qv70mllolyl unique (possibleAnswers_id)
alter table question add constraint FKhaja7xeeunpyc6n94shjggutg foreign key (correctAnswer_id) references answer (id)
alter table question_possibleAnswers add constraint FKkwohpr80b3w118b73rs1qat6r foreign key (possibleAnswers_id) references answer (id)
alter table question_possibleAnswers add constraint FKr60f6ccmb60o4jyo1w423jq96 foreign key (Question_id) references question (id)
create table answer (id bigint not null, answerPhrase varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table question (id bigint not null, questionphrase varchar(255), correctAnswer_id bigint, primary key (id)) engine=InnoDB
create table question_possibleAnswers (Question_id bigint not null, possibleAnswers_id bigint not null) engine=InnoDB
alter table question_possibleAnswers add constraint UK_pp1kbu15k9qnm6qv70mllolyl unique (possibleAnswers_id)
alter table question add constraint FKhaja7xeeunpyc6n94shjggutg foreign key (correctAnswer_id) references answer (id)
alter table question_possibleAnswers add constraint FKkwohpr80b3w118b73rs1qat6r foreign key (possibleAnswers_id) references answer (id)
alter table question_possibleAnswers add constraint FKr60f6ccmb60o4jyo1w423jq96 foreign key (Question_id) references question (id)
create table answer (id bigint not null, answerPhrase varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table question (id bigint not null, questionphrase varchar(255), correctAnswer_id bigint, primary key (id)) engine=InnoDB
create table question_possibleAnswers (Question_id bigint not null, possibleAnswers_id bigint not null) engine=InnoDB
alter table question_possibleAnswers add constraint UK_pp1kbu15k9qnm6qv70mllolyl unique (possibleAnswers_id)
alter table question add constraint FKhaja7xeeunpyc6n94shjggutg foreign key (correctAnswer_id) references answer (id)
alter table question_possibleAnswers add constraint FKkwohpr80b3w118b73rs1qat6r foreign key (possibleAnswers_id) references answer (id)
alter table question_possibleAnswers add constraint FKr60f6ccmb60o4jyo1w423jq96 foreign key (Question_id) references question (id)
create table answer (id bigint not null, answerPhrase varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table question (id bigint not null, questionphrase varchar(255), correctAnswer_id bigint, primary key (id)) engine=InnoDB
create table question_possibleAnswers (Question_id bigint not null, possibleAnswers_id bigint not null) engine=InnoDB
alter table question_possibleAnswers add constraint UK_pp1kbu15k9qnm6qv70mllolyl unique (possibleAnswers_id)
alter table question add constraint FKhaja7xeeunpyc6n94shjggutg foreign key (correctAnswer_id) references answer (id)
alter table question_possibleAnswers add constraint FKkwohpr80b3w118b73rs1qat6r foreign key (possibleAnswers_id) references answer (id)
alter table question_possibleAnswers add constraint FKr60f6ccmb60o4jyo1w423jq96 foreign key (Question_id) references question (id)
create table Answer (id bigint not null, answerPhrase varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table Question (id bigint not null, questionphrase varchar(255), correctAnswer_id bigint, primary key (id)) engine=InnoDB
create table Question_possibleAnswers (Question_id bigint not null, possibleAnswers_id bigint not null) engine=InnoDB
alter table Question_possibleAnswers add constraint UK_8d7sfyh7ic3g0wm87mpqfqq5i unique (possibleAnswers_id)
alter table Question add constraint FKhwj6uv52b4ic6vbe8g6hthuaf foreign key (correctAnswer_id) references Answer (id)
alter table Question_possibleAnswers add constraint FKyxrqoo1ag4c8wtvkclgl8i6a foreign key (possibleAnswers_id) references Answer (id)
alter table Question_possibleAnswers add constraint FKfs8iy7k3fqum1pkdooy92onfs foreign key (Question_id) references Question (id)
create table Answer (id integer not null, answerPhrase varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table Question (id integer not null, questionphrase varchar(255), correctAnswer_id integer, primary key (id)) engine=InnoDB
create table Question_possibleAnswers (Question_id integer not null, possibleAnswers_id integer not null) engine=InnoDB
alter table Question_possibleAnswers add constraint UK_8d7sfyh7ic3g0wm87mpqfqq5i unique (possibleAnswers_id)
alter table Question add constraint FKhwj6uv52b4ic6vbe8g6hthuaf foreign key (correctAnswer_id) references Answer (id)
alter table Question_possibleAnswers add constraint FKyxrqoo1ag4c8wtvkclgl8i6a foreign key (possibleAnswers_id) references Answer (id)
alter table Question_possibleAnswers add constraint FKfs8iy7k3fqum1pkdooy92onfs foreign key (Question_id) references Question (id)
create table Answer (id integer not null, answerPhrase varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table Question (id integer not null, questionphrase varchar(255), correctAnswer_id integer, primary key (id)) engine=InnoDB
create table Question_possibleAnswers (Question_id integer not null, possibleAnswers_id integer not null) engine=InnoDB
alter table Question_possibleAnswers add constraint UK_8d7sfyh7ic3g0wm87mpqfqq5i unique (possibleAnswers_id)
alter table Question add constraint FKhwj6uv52b4ic6vbe8g6hthuaf foreign key (correctAnswer_id) references Answer (id)
alter table Question_possibleAnswers add constraint FKyxrqoo1ag4c8wtvkclgl8i6a foreign key (possibleAnswers_id) references Answer (id)
alter table Question_possibleAnswers add constraint FKfs8iy7k3fqum1pkdooy92onfs foreign key (Question_id) references Question (id)
create table Answer (id integer not null, answerPhrase varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table Question (id integer not null, questionphrase varchar(255), correctAnswer_id integer, primary key (id)) engine=InnoDB
create table Question_possibleAnswers (Question_id integer not null, possibleAnswers_id integer not null) engine=InnoDB
alter table Question_possibleAnswers add constraint UK_8d7sfyh7ic3g0wm87mpqfqq5i unique (possibleAnswers_id)
alter table Question add constraint FKhwj6uv52b4ic6vbe8g6hthuaf foreign key (correctAnswer_id) references Answer (id)
alter table Question_possibleAnswers add constraint FKyxrqoo1ag4c8wtvkclgl8i6a foreign key (possibleAnswers_id) references Answer (id)
alter table Question_possibleAnswers add constraint FKfs8iy7k3fqum1pkdooy92onfs foreign key (Question_id) references Question (id)
create table Answer (id integer not null, answerPhrase varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table Question (id integer not null, questionphrase varchar(255), correctAnswer_id integer, primary key (id)) engine=InnoDB
create table Question_possibleAnswers (Question_id integer not null, possibleAnswers_id integer not null) engine=InnoDB
alter table Question_possibleAnswers add constraint UK_8d7sfyh7ic3g0wm87mpqfqq5i unique (possibleAnswers_id)
alter table Question add constraint FKhwj6uv52b4ic6vbe8g6hthuaf foreign key (correctAnswer_id) references Answer (id)
alter table Question_possibleAnswers add constraint FKyxrqoo1ag4c8wtvkclgl8i6a foreign key (possibleAnswers_id) references Answer (id)
alter table Question_possibleAnswers add constraint FKfs8iy7k3fqum1pkdooy92onfs foreign key (Question_id) references Question (id)
create table Answer (id integer not null, answerPhrase varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table Question (id integer not null, questionphrase varchar(255), correctAnswer_id integer, primary key (id)) engine=InnoDB
create table Question_possibleAnswers (Question_id integer not null, possibleAnswers_id integer not null) engine=InnoDB
alter table Question_possibleAnswers add constraint UK_8d7sfyh7ic3g0wm87mpqfqq5i unique (possibleAnswers_id)
alter table Question add constraint FKhwj6uv52b4ic6vbe8g6hthuaf foreign key (correctAnswer_id) references Answer (id)
alter table Question_possibleAnswers add constraint FKyxrqoo1ag4c8wtvkclgl8i6a foreign key (possibleAnswers_id) references Answer (id)
alter table Question_possibleAnswers add constraint FKfs8iy7k3fqum1pkdooy92onfs foreign key (Question_id) references Question (id)
create table Answer (id integer not null, answerPhrase varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table Question (id integer not null, questionphrase varchar(255), correctAnswer_id integer, primary key (id)) engine=InnoDB
create table Question_possibleAnswers (Question_id integer not null, possibleAnswers_id integer not null) engine=InnoDB
alter table Question_possibleAnswers add constraint UK_8d7sfyh7ic3g0wm87mpqfqq5i unique (possibleAnswers_id)
alter table Question add constraint FKhwj6uv52b4ic6vbe8g6hthuaf foreign key (correctAnswer_id) references Answer (id)
alter table Question_possibleAnswers add constraint FKyxrqoo1ag4c8wtvkclgl8i6a foreign key (possibleAnswers_id) references Answer (id)
alter table Question_possibleAnswers add constraint FKfs8iy7k3fqum1pkdooy92onfs foreign key (Question_id) references Question (id)
create table Answer (id integer not null, answerPhrase varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table Question (id integer not null, questionphrase varchar(255), correctAnswer_id integer, primary key (id)) engine=InnoDB
create table Question_possibleAnswers (Question_id integer not null, possibleAnswers_id integer not null) engine=InnoDB
alter table Question_possibleAnswers add constraint UK_8d7sfyh7ic3g0wm87mpqfqq5i unique (possibleAnswers_id)
alter table Question add constraint FKhwj6uv52b4ic6vbe8g6hthuaf foreign key (correctAnswer_id) references Answer (id)
alter table Question_possibleAnswers add constraint FKyxrqoo1ag4c8wtvkclgl8i6a foreign key (possibleAnswers_id) references Answer (id)
alter table Question_possibleAnswers add constraint FKfs8iy7k3fqum1pkdooy92onfs foreign key (Question_id) references Question (id)
