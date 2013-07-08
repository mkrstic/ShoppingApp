create table categories (
	_id integer primary key,
	name text unique not null,
	created_timestamp integer
);
create table products (
	_id integer primary key,
	name text not null,
	price integer,
	currency text,
	description text,
	category_id integer,
	location text,
	priority_level integer,
	wish_level integer,
	importance_rate integer,
	created_timestamp integer,
	foreign key (category_id) references categories (_id)
);

create view products_view as
	select products._id as _id, products.name as name, products.price as price, 
	products.currency as currency, products.description as description, 
	products._id as category_id, categories.name as category_name, 
	products.location as location, products.priority_level as priority_level, 
	products.wish_level as wish_level, products.created_timestamp as created_timestamp,
	products.importance_rate as importance_rate
	from products inner join categories on products.category_id = categories._id;

insert into categories (_id, name) values (1, 'Beverage');
insert into categories (_id, name) values (2, 'Clothing');
insert into categories (_id, name) values (3, 'Food');
insert into categories (_id, name) values (4, 'Electronics');
	

insert into products (name, price, currency, description, category_id, location, priority_level, wish_level, importance_rate) values
	('Apple iPhone', 520, 'USD', "Iphone description goes here...", 4, 'Beograd', 1, 4, 11);

insert into products (name, price, currency, description, category_id, location, priority_level, wish_level, importance_rate) values
	('T-shirt', 1800, 'RSD', "This shirt is blue and black on top", 2, 'Zara, Knez', 4, 1, 13);


insert into products (name, price, currency, description, category_id, location, priority_level, wish_level, importance_rate) values
	('Pepsi', 100, 'RSD', "Similar to Coca-cola but cheaper :-)", 1, 'Trafika', 2, 3, 12);

insert into products (name, price, currency, description, category_id, location, priority_level, wish_level, importance_rate) values
	('Big Mac', 210, 'RSD', "Big Mac description", 3, 'Zeleni Venac', 2, 5, 16);