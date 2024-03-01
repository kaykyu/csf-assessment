-- TODO Task 3
drop database if exists ecommerce;

create database ecommerce;

use ecommerce;

create table orders (

    id char(26) not null,
    date timestamp not null,
    name varchar(128) not null,
    address varchar(128) not null,
    priority boolean,
    comments text,

    primary key(id)
);

create table cart_items (

    id int auto_increment,
    order_id char(26) not null,
    prod_id varchar(128) not null,
    name varchar(128) not null,
    quantity int not null,
    price decimal(10,2) not null,

    primary key(id),
    constraint fk_order_id foreign key(order_id) references orders(id)    
)