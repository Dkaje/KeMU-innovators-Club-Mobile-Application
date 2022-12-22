<?php
include('connection.php');
if(mysqli_query($con,"CREATE TABLE official(
    stud_id varchar(20) primary key,
    fname varchar(50),
    lname varchar(50),
    gender varchar(50),
    email varchar(50),
    password varchar(250),
    phone varchar(50),
    role varchar(50),
    department varchar(50),
    classification varchar(50),
    status float default 0,
    remarks text,
    date varchar(50)
)")){
    echo 'official table created successfully';
}else{
    echo 'Failed to create table';
}