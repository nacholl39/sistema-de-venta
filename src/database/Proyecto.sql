DROP DATABASE IF EXISTS proyecto;
CREATE DATABASE proyecto;
USE proyecto;
CREATE TABLE cliente (
idCliente INT,
nif VARCHAR(10),
nombre VARCHAR(50),
direccion VARCHAR(100),
correo VARCHAR(50),
telefono INT,
edad INT,
fechaNac DATE,
CONSTRAINT cliente_id_pk PRIMARY KEY (idCliente)
);
CREATE TABLE vendedor (
idVendedor INT,
nif VARCHAR(10),
nombre VARCHAR(50),
direccion VARCHAR(100),
correo VARCHAR(50),
telefono INT,
edad INT,
fechaNac DATE,
CONSTRAINT vendedor_id_pk PRIMARY KEY (idVendedor)
);
CREATE TABLE venta (
idVenta INT,
fechaHora DATETIME,
idCliente INT,
idVendedor INT,
CONSTRAINT venta_id_pk PRIMARY KEY (idVenta),
CONSTRAINT venta_cliente_fk FOREIGN KEY (idCliente) REFERENCES cliente (idCliente),
CONSTRAINT venta_vendedor_fk FOREIGN KEY (idVendedor) REFERENCES vendedor (idVendedor)
);
CREATE TABLE factura (
idFactura INT,
fechaHora DATETIME,
idCliente INT,
idVendedor INT,
idVenta INT,
importeTotalSinImpuestos DOUBLE,
importeTotalConImpuestos DOUBLE,
impuestos DOUBLE,
CONSTRAINT factura_id_pk PRIMARY KEY (idFactura),
CONSTRAINT factura_cliente_fk FOREIGN KEY (idCliente) REFERENCES cliente (idCliente),
CONSTRAINT factura_vendedor_fk FOREIGN KEY (idVendedor) REFERENCES vendedor (idVendedor),
CONSTRAINT factura_venta_fk FOREIGN KEY (idVenta) REFERENCES venta (idVenta)
);
CREATE TABLE producto (
referencia VARCHAR(50),
descripcion TEXT,
marca INT,
modelo INT,
categoria INT,
CONSTRAINT producto_id_ref PRIMARY KEY (referencia)
);
CREATE TABLE atributoCategoria (
idAtributo INT,
atributo VARCHAR(50),
valor VARCHAR(30),
unidad VARCHAR(30),
referenciaProducto VARCHAR(50),
CONSTRAINT atrb_id_pk PRIMARY KEY (idAtributo),
CONSTRAINT atrb_ref_fk FOREIGN KEY (referenciaProducto) REFERENCES producto (referencia)
);
CREATE TABLE detalleFactura (
idDetalle INT,
lineaFactura INT,
cantidad INT,
idFactura INT,
referenciaProducto VARCHAR(50),
CONSTRAINT detll_id_pk PRIMARY KEY (idDetalle),
CONSTRAINT detll_ref_fk FOREIGN KEY (referenciaProducto) REFERENCES producto (referencia),
CONSTRAINT detll_factura_fk FOREIGN KEY (idFactura) REFERENCES factura (idFactura)
);