CREATE TABLE doctor
(
    id             int generated by default as identity primary key,
    name           varchar not null,
    specialization varchar not null
);

CREATE TABLE patient
(
    id   int generated by default as identity primary key,
    name varchar               not null,
    age  int check ( age > 0 ) not null,
    sex  varchar               not null
    doctor_id int,
    foreign key (doctor_id) references doctor(id)
);
CREATE TABLE drug
(
    id    int generated by default as identity primary key,
    title varchar not null
);
create table patient_drug
(
    patient_id bigint not null,
    drug_id    bigint not null,
    primary key (patient_id, drug_id),
    constraint fk_patient_id foreign key (patient_id) references patient (id),
    constraint fk_drug_id foreign key (drug_id) references drug (id)
);
