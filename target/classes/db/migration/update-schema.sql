ALTER TABLE clases
DROP
FOREIGN KEY clases_ibfk_2;

ALTER TABLE disponibilidad_profesores
DROP
FOREIGN KEY disponibilidad_profesores_ibfk_1;

ALTER TABLE clases
    ADD CONSTRAINT FK_CLASES_ON_PROFESOR FOREIGN KEY (profesor_id) REFERENCES profesores (id);

ALTER TABLE disponibilidad_profesores
    ADD CONSTRAINT FK_DISPONIBILIDAD_PROFESORES_ON_PROFESOR FOREIGN KEY (profesor_id) REFERENCES profesores (id);

ALTER TABLE disponibilidad_profesores
DROP
COLUMN dia_semana;

ALTER TABLE disponibilidad_profesores
DROP
COLUMN hora_fin;

ALTER TABLE disponibilidad_profesores
DROP
COLUMN hora_inicio;

ALTER TABLE disponibilidad_profesores
DROP
COLUMN id_profesor;

ALTER TABLE clases
DROP
COLUMN id_alumno;

ALTER TABLE clases
DROP
COLUMN id_profesor;

ALTER TABLE alumnos
    MODIFY email VARCHAR (255);

ALTER TABLE profesores
    MODIFY email VARCHAR (255);

ALTER TABLE profesores
DROP
COLUMN id;

ALTER TABLE profesores
    ADD id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY;

ALTER TABLE alumnos
    MODIFY nombre VARCHAR (255);

ALTER TABLE profesores
    MODIFY nombre VARCHAR (255);