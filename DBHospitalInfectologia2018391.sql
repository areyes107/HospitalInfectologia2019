drop database if exists DBHospitalInfectologia2018391;
create database DBHospitalInfectologia2018391;
use DBHospitalInfectologia2018391;

-- Tablas --

create table TipoUsuario (
	codigoTipoUsuario int not null auto_increment,
    descripcion varchar(150) not null,
    primary key PK_codigoTipoUsuario (codigoTipoUsuario)
);

create table Usuarios (
	codigoUsuario int not null auto_increment,
    usuarioLogin varchar(45) not null,
    usuarioContraseña varchar(45) not null,
    usuarioEstado boolean not null default false,
    usuarioFecha date not null,
    usuarioHora varchar(45),
    codigoTipoUsuario int not null,
    primary key PK_codigoUsuario (codigoUsuario),
    constraint FK_Usuarios_TipoUsuario foreign key (codigoTipoUsuario) references TipoUsuario(codigoTipoUsuario)
);

create table Areas (
	codigoArea int not null auto_increment,
    nombreArea varchar (45) not null,
    primary key PK_codigoArea (codigoArea)
);

create table Cargos (
	codigoCargo int not null auto_increment,
    nombreCargo varchar (45) not null,
    primary key PK_codigoCargo (codigoCargo)
);

create table Especialidades (
	codigoEspecialidad int not null auto_increment,
    nombreEspecialidad varchar (45) not null,
    primary key PK_codigoEspecialidad (codigoEspecialidad)
);

create table Horarios (
	codigoHorario int not null auto_increment,
    horarioInicio varchar(25) not null,
    horarioSalida varchar(25) not null,
    lunes tinyint default false,
    martes tinyint default false,
    miercoles tinyint default false,
    jueves tinyint default false,
    viernes tinyint default false,
    primary key PK_codigoHorario (codigoHorario)
);

create table Pacientes (
	codigoPaciente int not null auto_increment,
    DPI varchar (20) not null,
    apellidos varchar (100) not null,
    nombres varchar (100) not null,
    fechaNacimiento date not null,
    edad int, 
    direccion varchar (150) not null,
    ocupacion varchar (100) not null,
    sexo varchar (15) not null,
    primary key PK_codigoPaciente (codigoPaciente)
);

create table Medicos (
	codigoMedico int not null auto_increment,
    licenciaMedica int not null,
    nombres varchar(100) not null,
    apellidos varchar(100) not null,
    horaEntrada varchar(10) not null,
    horaSalida varchar(10) not null,
    turnoMaximo int default 0,
    sexo varchar(20) not null,
    primary key PK_codigoMedico (codigoMedico)
);

create table ContactoUrgencia (
	codigoContactoUrgencia int not null auto_increment,
    nombres varchar (100) not null,
    apellidos varchar (100) not null,
    numeroContacto varchar (10) not null,
    codigoPaciente int not null,
    primary key PK_codigoContactoUrgencia (codigoContactoUrgencia),
    constraint FK_ContactoUrgencia_Pacientes foreign key (codigoPaciente) references Pacientes (codigoPaciente) on delete cascade ON UPDATE CASCADE 
);

create table MedicoEspecialidad (
	codigoMedicoEspecialidad int not null auto_increment,
    codigoMedico int not null,
    codigoEspecialidad int not null,
    codigoHorario int not null,
    primary key PK_codigoMedicoEspecialidad (codigoMedicoEspecialidad),
    constraint FK_MedicoEspecialidad_Medicos foreign key (codigoMedico) references Medicos (codigoMedico) on delete cascade ON UPDATE CASCADE ,
    constraint FK_MedicoEspecialidad_Especialidades foreign key (codigoEspecialidad) references Especialidades (codigoEspecialidad) ON Delete cascade ON UPDATE CASCADE ,
    constraint FK_MedicoEspecialidad_Horarios foreign key (codigoHorario) references Horarios (codigoHorario) on delete cascade ON UPDATE CASCADE 
);

create table TelefonoMedico (
	codigoTelefonoMedico int not null auto_increment,
    telefonoPersonal varchar (15) not null,
    telefonoTrabajo varchar (15) not null,
    codigoMedico int not null,
    primary key PK_codigoTelefonoMedico (codigoTelefonoMedico), 
    constraint FK_TelefonoMedico_Medicos foreign key (codigoMedico) references Medicos (codigoMedico) on delete cascade ON UPDATE CASCADE 
);

create table ResponsableTurno (
	codigoResponsableTurno int not null auto_increment,
    nombreResponsable varchar(75) not null,
    apellidosResponsable varchar(45) not null,
    telefonoPersonal varchar(10) not null,
    codigoArea int not null,
    codigoCargo int not null,
    primary key PK_codigoResponsableTurno (codigoResponsableTurno),
    constraint FK_ResponsableTurno_Areas foreign key (codigoArea) references Areas (codigoArea) on delete cascade ON UPDATE CASCADE ,
    constraint FK_ResponsableTurno_Cargos foreign key (codigoCargo) references Cargos (codigoCargo) on delete cascade ON UPDATE CASCADE 
);

create table Turno (
	codigoTurno int not null auto_increment,
    fechaTurno date not null,
    fechaCita date not null,
    valorCita decimal (10,2) not null,
    codigoMedicoEspecialidad int not null,
    codigoResponsableTurno int not null,
    codigoPaciente int not null,
    primary key PK_codigoTurno (codigoTurno),
    constraint FK_Turno_MedicoEspecialidad foreign key (codigoMedicoEspecialidad) references MedicoEspecialidad (codigoMedicoEspecialidad)on delete cascade ON UPDATE CASCADE ,
    constraint FK_Turno_ResponsableTurno foreign key (codigoResponsableTurno) references ResponsableTurno (codigoResponsableTurno)on delete cascade ON UPDATE CASCADE ,
    constraint FK_Turno_Pacientes foreign key (codigoPaciente) references Pacientes (codigoPaciente) on delete cascade ON UPDATE CASCADE 
);


-- procedimientos para agregar --

-- agregar usuario
Delimiter $$
create procedure sp_AgregarUsuario (IN usuarioLogin varchar(45), IN usuarioContraseña varchar(45), IN codigoTipoUsuario int)
	Begin 
		insert into Usuarios (usuarioLogin, usuarioContraseña, usuarioFecha, usuarioHora, codigoTipoUsuario)
			values(usuarioLogin, usuarioContraseña, current_date(), DATE_FORMAT(NOW( ), "%H:%i" ), codigoTipoUsuario);
    End$$	
Delimiter ;

-- agregar tipoUsuario 
Delimiter $$
create procedure sp_AgregarTipoUsuario (IN descripcion varchar(45))
	Begin 
		insert into TipoUsuario (descripcion)
			values (descripcion);
    End$$
Delimiter ;

-- Agregar area--
Delimiter $$
create procedure sp_AgregarArea (IN nombreArea varchar(45))
	Begin 
		insert into Areas (nombreArea)
			values(nombreArea);
	End$$
Delimiter ;


-- Agregar cargo--
Delimiter $$
create procedure sp_AgregarCargo (IN nombreCargo varchar(45))
	Begin 
		insert into Cargos (nombreCargo)
			values(nombreCargo);
	End$$
Delimiter ;


-- Agregar especialidad--
Delimiter $$
create procedure sp_AgregarEspecialidad (IN nombreEspecialidad varchar(45))
	Begin 
		insert into Especialidades (nombreEspecialidad)
			values(nombreEspecialidad);
    End$$
Delimiter ;


-- Agregar horario--
Delimiter $$
create procedure sp_AgregarHorario(IN horarioInicio time, IN horarioSalida time, IN lunes tinyint, IN martes tinyint, IN miercoles tinyint, IN jueves tinyint, IN viernes tinyint)
	Begin 
		insert into Horarios (horarioInicio, horarioSalida, lunes, martes, miercoles, jueves, viernes)
			values(horarioInicio, horarioSalida, lunes, martes, miercoles, jueves, viernes);
    End$$
Delimiter ;


-- Agregar paciente--
Delimiter $$
create procedure sp_AgregarPaciente (IN dpi varchar(20), IN apellidos varchar(100), IN nombres varchar(100), IN fechaNacimiento date, IN direccion varchar(150), IN ocupacion varchar(100), IN sexo varchar(15))
	Begin 
		insert into Pacientes (dpi, apellidos, nombres, fechaNacimiento, direccion, ocupacion, sexo)
			values(dpi, apellidos, nombres, fechaNacimiento, direccion, ocupacion, sexo);
    End$$
Delimiter ;


-- Agregar medico --
Delimiter $$
create procedure sp_AgregarMedico (IN licenciaMedica int, IN nombres varchar(100), IN apellidos varchar(100), IN horaEntrada varchar(10), IN horaSalida varchar(10), IN sexo varchar (20))
	Begin 
		insert into Medicos (licenciaMedica, nombres, apellidos, horaEntrada, horaSalida, sexo)
			values(licenciaMedica, nombres, apellidos, horaEntrada, horaSalida, sexo);
    End$$
Delimiter ;


-- Agregar contactoUrgencia --
Delimiter $$
create procedure sp_AgregarContactoUrgencia(IN nombres varchar(100), IN apellidos varchar (100), IN numeroContacto varchar(10), IN codigoPaciente int)
	Begin 
		insert into ContactoUrgencia (nombres, apellidos, numeroContacto, codigoPaciente)
			values(nombres, apellidos, numeroContacto, codigoPaciente);
    End$$
Delimiter ;


-- Agregar medicoEspecialidad --
Delimiter $$
create procedure sp_AgregarMedicoEspecialidad (IN codigoMedico int, IN codigoEspecialidad int, IN codigoHorario int)
	Begin 
		insert into MedicoEspecialidad(codigoMedico, codigoEspecialidad, codigoHorario )
			values(codigoMedico, codigoEspecialidad, codigoHorario );
    End$$
Delimiter ;


-- Agregar telefonoMedico --
Delimiter $$
create procedure sp_AgregarTelefonoMedico (IN telefonoPersonal varchar(15),IN telefonoTrabajo varchar(15), IN codigoMedico int)
	Begin 
		insert into TelefonoMedico(telefonoPersonal, telefonoTrabajo, codigoMedico)
			values(telefonoPersonal, telefonoTrabajo, codigoMedico);
    End$$
Delimiter ;


-- Agregar responsableTurno --
Delimiter $$
create procedure sp_AgregarResponsableTurno (IN nombreResponsable varchar(75), IN apellidosResponsable varchar(45), IN telefonoPersonal varchar(10), IN codigoArea int, IN codigoCargo int)
	Begin 
		insert into ResponsableTurno(nombreResponsable, apellidosResponsable, telefonoPersonal, codigoArea, codigoCargo)
			values(nombreResponsable, apellidosResponsable, telefonoPersonal, codigoArea, codigoCargo);
    End$$
Delimiter ; 

-- Agregar turno --
Delimiter $$
create procedure sp_AgregarTurno (IN fechaTurno date, IN fechaCita date, IN valorCita decimal(10,2), IN codigoMedicoEspecialidad int, IN codigoResponsableTurno int, IN codigoPaciente int)
	Begin 
		insert into Turno(fechaTurno, fechaCita, valorCita, codigoMedicoEspecialidad, codigoResponsableTurno, codigoPaciente)
			values(fechaTurno, fechaCita, valorCita, codigoMedicoEspecialidad, codigoResponsableTurno, codigoPaciente);
    End$$
Delimiter ; 

-- Procedimientos para eliminar --


-- eliminar usuarios
Delimiter $$
create procedure sp_EliminarUsuario(IN userCode int)
	Begin
		delete from Usuarios
			where codigoUsuario = userCode;
    End$$
Delimiter ;

-- Eliminar Areas --
Delimiter $$
create procedure sp_EliminarArea (IN areaCode int)
	Begin 
		Delete from Areas 
			where codigoArea = areaCode;
    End$$
Delimiter ;

-- Eliminar Cargos --
Delimiter $$
create procedure sp_EliminarCargo (IN chargeCode int)
	Begin 
		Delete from Cargos 
			where codigoCargo = chargeCode;
    End$$
Delimiter ;

-- Eliminar Especialidades --
Delimiter $$
create procedure sp_EliminarEspecialidad (IN specialityCode int)
	Begin 
		Delete from Especialidades 
			where codigoEspecialidad = specialityCode;
    End$$
Delimiter ;

-- Eliminar Horarios --
Delimiter $$
create procedure sp_EliminarHorario (IN scheduleCode int)
	Begin 
		Delete from Horarios 
			where codigoHorario = scheduleCode;
    End$$
Delimiter ;

-- Eliminar Pacientes --
Delimiter $$
create procedure sp_EliminarPaciente (IN patientCode int)
	Begin 
		Delete from Pacientes 
			where codigoPaciente = patientCode;
    End$$
Delimiter ;

-- Eliminar Medicos --
Delimiter $$
create procedure sp_EliminarMedico (IN doctorCode int)
	Begin 
		 Delete from Medicos 
			where codigoMedico = doctorCode;
    End$$
Delimiter ;
-- Eliminar ContactoUrgencia --
Delimiter $$
create procedure sp_EliminarContactoUrgencia (IN contactCode int)
	Begin 
		Delete from ContactoUrgencia 
			where codigoContactoUrgencia = contactCode;
    End$$
Delimiter ;

-- Eliminar MedicoEspecialidad --
Delimiter $$
create procedure sp_EliminarMedicoEspecialidad (IN doctorSpecialityCode int)
	Begin 
		Delete from MedicoEspecialidad 
			where codigoMedicoEspecialidad = doctorSpecialityCode;
    End$$
Delimiter ;

-- Eliminar TelefonoMedico --
Delimiter $$
create procedure sp_EliminarTelefonoMedico (IN doctorPhoneCode int)
	Begin 
		Delete from TelefonoMedico 
			where codigoTelefonoMedico = doctorPhoneCode;
    End$$
Delimiter ;

-- Eliminar ResponsableTurno --
Delimiter $$
create procedure sp_EliminarResponsableTurno (IN responsableCode int)
	Begin 
		Delete from ResponsableTurno 
			where codigoResponsableTurno = responsableCode;
    End$$
Delimiter ;

-- Eliminar Turno --
Delimiter $$
create procedure sp_EliminarTurno (IN turnCode int)
	Begin 
		Delete from Turno 
			where codigoTurno = turnCode;
    End$$
Delimiter ;

-- Procedimientos para listar --

-- listar usuarios
Delimiter $$
create procedure sp_ListarUsuarios()
	Begin 
		select * from Usuarios;
    End$$
Delimiter ;

-- listar tipousuario
Delimiter $$
create procedure sp_ListarTiposUsuarios()
	Begin 
		select * from TipoUsuario;
    End$$
Delimiter ; 

-- listar areas --
Delimiter $$
create procedure sp_ListarAreas()
	Begin 
		select * from Areas;
    End$$
Delimiter ;

-- listar cargos --
Delimiter $$
create procedure sp_ListarCargos()
	Begin 
		select * from Cargos;
    End$$
Delimiter ;

-- listar especialidades
Delimiter $$
create procedure sp_ListarEspecialidades()
	Begin 
		select * from Especialidades;
    End$$
Delimiter ;

-- listar horarios 
Delimiter $$
create procedure sp_ListarHorarios()
	Begin 
		select * from Horarios;
    End$$
Delimiter ;

-- listar Pacientes 
Delimiter $$
create procedure sp_ListarPacientes()
	Begin 
		select * from Pacientes;
    End$$
Delimiter ;

-- listar Medicos
Delimiter $$
create procedure sp_ListarMedicos()
	Begin 
		select * from Medicos;
    End$$
Delimiter ;

-- listar ContactoUrgencia 
Delimiter $$
create procedure sp_ListarContactosUrgencia()
	Begin 
		select * from ContactoUrgencia;
    End$$
Delimiter ;

-- Listar medicoEspecialidad
Delimiter $$
create procedure sp_ListarMedicosEspecialidades()
	Begin 
		select * from MedicoEspecialidad;
    End$$
Delimiter ;

-- listar telefonoMedico
Delimiter $$
create procedure sp_ListarTelefonosMedicos()
	Begin 
		select * from TelefonoMedico;
    End$$
Delimiter ;

-- listar responsableTurno
Delimiter $$
create procedure sp_ListarResponsablesTurnos()
	Begin 
		select * from ResponsableTurno;
    End$$
Delimiter ;

-- listar turno
Delimiter $$
create procedure sp_ListarTurnos()
	Begin 
		select * from Turno;
    End$$
Delimiter ;

-- Procedimientos para editar

-- editar usuarios
Delimiter $$
create procedure sp_EditarUsuario(IN userCode int, IN userLogin varchar(45), IN userPassword varchar(45))
	Begin 	
		update Usuarios
			set usuarioLogin = userLogin, usuarioContraseña = userPassword, usuarioFecha = current_date(), usuarioHora = DATE_FORMAT(NOW( ), "%H:%i" )
				where codigoUsuario = userCode;
    End$$
Delimiter ;

-- editar Areas
Delimiter $$
create procedure sp_EditarArea(IN codeArea int, IN nameArea varchar(45))
	Begin 
		update Areas 
			set nombreArea = nameArea
				where codigoArea = codeArea;
    End$$
Delimiter ;

-- editar cargos
Delimiter $$
create procedure sp_EditarCargo(IN codeCharge int, IN nameCharge varchar(45))
	Begin 
		update Cargos 
			set nombreCargo = nameCharge
				where codigoCargo = codeCharge;
    End$$
Delimiter ;

-- editar especialidades
Delimiter $$
create procedure sp_EditarEspecialidad (IN codeSpeciality int, IN nameSpeciality varchar(45))
	Begin 
		update Especialidades
			set nombreEspecialidad = nameSpeciality
				where codigoEspecialidad = codeSpeciality;
    End$$
Delimiter ;

-- editar horarios--
Delimiter $$
create procedure sp_EditarHorario(IN codeSchedule int, IN startSchedule varchar(25), IN endSchedule varchar(25), IN monday tinyint, IN tuesday tinyint, IN wednesday tinyint, IN thursday tinyint, IN friday tinyint)
	Begin 
		update Horarios
			set horarioInicio = startSchedule, horarioSalida = endSchedule, lunes = monday, martes = tuesday, miercoles = wednesday, jueves = thursday, viernes = friday
				where codigoHorario = codeSchedule;
    End$$
Delimiter ;

-- editar Pacientes
Delimiter $$
create procedure sp_EditarPaciente(IN patientCode int, IN DPII varchar(20), IN lastNames varchar(100),  IN patientNames varchar(100) , IN bornDate date, IN direction varchar(150), IN ocupation varchar(100), IN sex varchar(20))
	Begin 
		update Pacientes 
			set DPI = DPII,apellidos = lastNames ,nombres = patientNames, fechaNacimiento= bornDate, direccion= direction, ocupacion=ocupation, sexo = sex
				where codigoPaciente = patientCode;
    End$$
Delimiter ;

-- editar medicos
Delimiter $$
create procedure sp_EditarMedico(IN doctorCode int, IN medicalLicence int, IN doctorNames varchar(100), IN lastNames varchar(100) , IN entranceHour varchar(10), IN exitHour varchar(10) , IN sex varchar(20))
	Begin 
		update Medicos 
			set licenciaMedica = medicalLicence, nombres = doctorNames, apellidos = lastNames, horaEntrada = entranceHour, horaSalida = exitHour, sexo = sex
				where codigoMedico = doctorCode;
    End$$
Delimiter ;

-- editar contactoUrgencia 
Delimiter $$
create procedure sp_EditarContactoUrgencia(contactEmergencyCode int, namesContact varchar (100) , lastNames varchar (100), contactPhone varchar (10) , patientCode int)
	Begin 
		update ContactoUrgencia
			set nombres = namesContact, apellidos = lastNames, numeroContacto = contactPhone, codigoPaciente = patientCode
            where codigoContactoUrgencia = contactEmergencyCode;
    End$$
Delimiter ;

-- editar medicoEspecialidad
Delimiter $$
create procedure sp_EditarMedicoEspecialidad(IN doctorSpecialityCode int ,IN doctorCode int, IN specialityCode int, IN scheduleCode int)
	Begin
		update MedicoEspecialidad
			set codigoMedico = doctorCode, codigoEspecialidad = specialityCode, codigoHorario = scheduleCode
				where codigoMedicoEspecialidad = doctorSpecialityCode;
    End$$
Delimiter ;

-- editar telefonoMedico 
Delimiter $$
create procedure sp_EditarTelefonoMedico (IN codeDoctorPhone int, IN personalPhone varchar(15), IN workPhone varchar(15), IN doctorCode int)
	Begin 
		update TelefonoMedico 	
			set telefonoPersonal = personalPhone, telefonoTrabajo = workPhone, codigoMedico = doctorCode
				where codigoTelefonoMedico = codeDoctorPhone;
    End$$
Delimiter ;

-- editar ReponsableTUrno
Delimiter $$
create procedure sp_EditarResponsableTurno (IN codeResponsableTurn int, IN responsableName varchar(75), IN responsableLastNames varchar(45), IN personalPhone varchar(10), IN areaCode int, IN chargeCode int)
	Begin 
		update ResponsableTurno
			set nombreResponsable = responsableName, apellidosResponsable = responsableLastNames, telefonoPersonal = personalPhone, codigoArea = areaCode, codigoCargo = chargeCode
				where codigoResponsableTurno = codeResponsableTurn;
    End$$
Delimiter ;

-- editar Turno
Delimiter $$
create procedure sp_EditarTurno (IN turnCode int, IN dateTurn date, IN appointmentDate date, IN appointmentAmount Decimal(10,2), IN doctorSpecialotyCode int, responsableTurnCode int, IN patientCode int)
	Begin 
		update Turno
			set fechaTurno = dateTurn, fechaCita = appointmentDate, valorCita = appointmentAmount, codigoMedicoEspecialidad =doctorSpecialityCode, codigoResponsableTurno = responsableTurnCode, codigoPaciente = patientCode
				where codigoTurno = turnCode;
    End$$
Delimiter ;


-- procedimientos para buscar

-- buscar areas
Delimiter $$
create procedure sp_BuscarArea(IN areaCode int)
	Begin 
		select * from Areas
			where codigoArea = areaCode;
    End$$
Delimiter ;

-- buscar cargos
Delimiter $$
create procedure sp_BuscarCargo(IN chargeCode int)
	Begin 
		select * from Cargos
			where codigoCargo = chargeCode;
    End$$
Delimiter ;

-- buscar Especialidades
Delimiter $$
create procedure sp_BuscarEspecialidad(IN specialityCode int)
	Begin 
		select * from Especialidades
			where codigoEspecialidad = specialityCode;
    End$$
Delimiter ;

-- buscar Horarios
Delimiter $$
create procedure sp_BuscarHorario(IN scheduleCode int)
	Begin 
		select * from Horarios
			where codigoHorario = scheduleCode;
    End$$
Delimiter ;

-- buscar Pacientes
Delimiter $$
create procedure sp_BuscarPaciente(IN patientCode int)
	Begin 
		select * from Pacientes
			where codigoPaciente= patientCode;
    End$$
Delimiter ;

-- buscar medicos
Delimiter $$
create procedure sp_BuscarMedico(IN doctorCode int)
	Begin 
		select * from Medicos
			where codigoMedico = doctorCode;
    End$$
Delimiter ;

-- buscar ContactoUrgencia
Delimiter $$
create procedure sp_BuscarContactoUrgencia(IN contactEmergencyCode int)
	Begin 
		select * from ContactoUrgencia
			where codigoContactoUrgencia = contactEmergencyCode;
    End$$
Delimiter ;

-- buscar MedicoEspecialidad
Delimiter $$
create procedure sp_BuscarMedicoEspecialidad(IN doctorSpecialityCode int)
	Begin 
		select * from MedicoEspecialidad
			where codigoMedicoEspecialidad = doctorSpecialityCode;
    End$$
Delimiter ;

-- buscar TelefonoMedico
Delimiter $$
create procedure sp_BuscarTelefonoMedico(IN doctorPhoneCode int)
	Begin 
		select * from TelefonoMedico
			where codigoTelefonoMedico = doctorPhoneCode;
    End$$
Delimiter ;

-- buscar ResponsableTurno
Delimiter $$
create procedure sp_BuscarResponsableTurno(IN responsableTurnCode int)
	Begin 
		select * from ReponsableTurno
			where codigoResponsableTurno = responsableTurnCode;
    End$$
Delimiter ;

-- buscar Turno
Delimiter $$
create procedure sp_BuscarTurno(IN TurnCode int)
	Begin 
		select * from Turno
			where codigoTurno = TurnCode;
    End$$
Delimiter ;

-- extra 
Delimiter $$
create function fn_CalcularEdad (fechaNacimiento date) returns int
reads sql data deterministic
	Begin 
		declare edad int;
        set edad = timestampdiff(year, fechaNacimiento, curdate());
        return edad;
    End$$
Delimiter ;


Delimiter $$
create trigger tr_Pacientes_Before_Insert
before insert on Pacientes
for each row
	Begin 
			set new.edad = fn_CalcularEdad(new.fechaNacimiento);
    End$$
Delimiter ;


Delimiter $$
create procedure sp_BuscarUsuario(IN userLogin varchar(45), IN userPassword varchar(45))
	Begin 
		select usuarioLogin, usuarioContraseña 
        from Usuarios
			where usuarioLogin = userLogin and usuarioContraseña = userPassword;
            
    End$$
Delimiter ; 



-- Llamadas a los Procedimientos --
select * from usuarios;
-- tipoUsuario agregar 
call sp_AgregarTipoUsuario('admin');
call sp_AgregarTipoUsuario('root');
call sp_AgregarTipoUsuario('guest');

-- usuario agregar
call sp_AgregarUsuario('areyes2018391', 'admin', 1);

-- Areas agregar--
call sp_AgregarArea ('Areas medicas');
call sp_AgregarArea ('Areas quirurgicas');
call sp_AgregarArea ('Areas de diagnostico y apoyo clinico');

-- Cargos agregar--
call sp_AgregarCargo ('Director General');
call sp_AgregarCargo ('Subdirector de gestion administrativa');
call sp_AgregarCargo ('Subdirector de gestion asistencial');
call sp_AgregarCargo ('Subdirector de gestion de cuidado');
call sp_AgregarCargo ('Subdirector de logistica y operaciones');
call sp_AgregarCargo ('Gestor de usuarios');
call sp_AgregarCargo ('Abastecimiento');
call sp_AgregarCargo ('Administracion y finanzas');
call sp_AgregarCargo ('Gestion de las personas');
call sp_AgregarCargo ('Apoyo diagnostico y terapeutico');
call sp_AgregarCargo ('Hospitalizacion');
call sp_AgregarCargo ('Unidad de emergencias');
call sp_AgregarCargo ('Consultorio de especialidades');
call sp_AgregarCargo ('Traslado de pacientes');
call sp_AgregarCargo ('Gestion de camas');

-- Especialidades agregar --
call sp_AgregarEspecialidad ('Antologia');
call sp_AgregarEspecialidad ('Cardiologia');
call sp_AgregarEspecialidad ('Cuidados intensivos');
call sp_AgregarEspecialidad ('Digestivo');
call sp_AgregarEspecialidad ('Hematologia');
call sp_AgregarEspecialidad ('Medicina interna');
call sp_AgregarEspecialidad ('Nefrologia');
call sp_AgregarEspecialidad ('Neumologia');
call sp_AgregarEspecialidad ('Oncologia');
call sp_AgregarEspecialidad ('Pediatria');
call sp_AgregarEspecialidad ('Neonatologia');
call sp_AgregarEspecialidad ('Rehabilitacion');
call sp_AgregarEspecialidad ('Urgencias');
call sp_AgregarEspecialidad ('Cirugia general y digestiva');
call sp_AgregarEspecialidad ('Cirugia ortopedica y traumatologia');
call sp_AgregarEspecialidad ('Dermatologia');
call sp_AgregarEspecialidad ('Ginecologia y obstetricia');
call sp_AgregarEspecialidad ('Oftalmologia');
call sp_AgregarEspecialidad ('Otorrinolaringologia');
call sp_AgregarEspecialidad ('Urologia');
call sp_AgregarEspecialidad ('Laboratorios clinicos');
call sp_AgregarEspecialidad ('Radiodiagnostico');
call sp_AgregarEspecialidad ('Farmacia');
call sp_AgregarEspecialidad ('Medicina preventiva');
-- Horarios agregar --
call sp_AgregarHorario ('04:30:00', '12:30:00', 0, 1, 1, 1,0);
call sp_AgregarHorario ('11:00:00', '19:00:00',0,0,1,1,1);
call sp_AgregarHorario ('12:00:00','00:00:00',1,1,0,0,0);

-- Pacientes agregar --
call sp_AgregarPaciente ('2200981265138','Ramos Perez','Marta Julia', '1995-12-22','14 calle, 3ra avenida zona 1, guatemala', 'Maestra de diversificado', 'F');
call sp_AgregarPaciente ('3589124923583','Martinez Juarez', 'Alberto Javier','1989-04-17', '8va calle, 1ra avenida, zona 4 mixco', 'Tecnico en soporte de telecomunicacione', 'M');
call sp_AgregarPaciente ('2606781269127', 'Hernandez Perez', 'Juan Manuel', '1975-11-09',  '5ta calle, 10ma avenida, zona 1 Mixco', 'Mecanico en Diesel', 'M');
call sp_AgregarPaciente ('2945139713467', 'Suarez Caal', 'Fabiola Adela', '1991-03-25', '7ma calle, 2da avenida, zona 4 Guatemala', 'Secretaria Bilingüe', 'F');
call sp_AgregarPaciente ('3225459837291', 'Pineda Merida', 'Javier Estuardo', '1989-04-22', '15va calle, 2da avenida, zona 9 San Miguel Petapa', 'Tecnico en redes de claro','M');
call sp_AgregarPaciente ('2894236578135', 'Mendez Rivera', 'Jose Mauricio', '1998-04-23', '16va calle, 4ta avenida, zona 1 Guatemala', 'contador', 'M');

-- Medicos agregar --
call sp_AgregarMedico (9001, 'Juan Carlos', 'Gomez Fernandez', '12:00:00', '00:00:00','M');
call sp_AgregarMedico (9002, 'Maria Juana', 'Marquez Tol', '11:00:00', '19:00:00', 'F');
call sp_AgregarMedico (9003, 'Maria Rene', 'Menendez Perez', '04:30:00', '12:30:00', 'F');
call sp_AgregarMedico (9004, 'Alma Esperanza', 'Ventura Paiz', '04:30:00', '12:30:00', 'F');
call sp_AgregarMedico (9005, 'Marta Julia', 'Lopez Rodriguez', '11:00:00', '19:00:00','F');
call sp_AgregarMedico (9006, 'Peter Gabriel', 'Parker Smith', '12:00:00', '00:00:00', 'M');

-- Contacto Urgencia agregar --
call sp_AgregarContactoUrgencia ('Mynor Rafael', 'Martinez Juarez', '5522-3032', 1);
call sp_AgregarContactoUrgencia ('Karla Daniela', 'Rodas Lopez', '4761-2189', 2);
call sp_AgregarContactoUrgencia ('Daniel Alejandro', 'Flores Duarte', '3298-1078', 3);
call sp_AgregarContactoUrgencia ('Victor Javier', 'Gonzales Menendez', '5990-5617', 4);
call sp_AgregarContactoUrgencia ('Luisa Mariela', 'Fernandez Cigarroa', '4189-3698', 5);
call sp_AgregarContactoUrgencia ('Luisa Fernanda', 'Mejia Flores', '5398-2314', 6);

-- Medico Especialidad agregar --
call sp_AgregarMedicoEspecialidad (1, 2, 3);
call sp_AgregarMedicoEspecialidad (2, 10, 2);
call sp_AgregarMedicoEspecialidad (3, 14, 1);
call sp_AgregarMedicoEspecialidad (4, 17, 1);
call sp_AgregarMedicoEspecialidad (5, 19, 2);
call sp_AgregarMedicoEspecialidad (6, 6, 3);

-- Telefono Medico agregar --
call sp_AgregarTelefonoMedico ('5533-1571', '6631-4971', 1);
call sp_AgregarTelefonoMedico ('5990-1322', '6631-8128',2);
call sp_AgregarTelefonoMedico ('4761-9822', '6646-3522',3);
call sp_AgregarTelefonoMedico ('5306-0791', '6636-2498',4);
call sp_AgregarTelefonoMedico ('4216-2906', '6696-2989', 5);
call sp_AgregarTelefonoMedico ('4150-3031', '2284-2356', 6);

-- Responsable Turno agregar --
call sp_AgregarResponsableTurno ('Ernesto', 'Cortez Perez', '5980-5687', 1, 12);
call sp_AgregarResponsableTurno ('Emma', 'Fernandez Gomez', '4237-9281', 2, 11);
call sp_AgregarResponsableTurno ('Amelia', 'Sanchez Alvarez', '5523-9267', 3, 14);

-- Turno agregar --
call sp_AgregarTurno ('2019-06-26', '2019-07-26', 800, 1, 1, 1);
call sp_AgregarTurno ('2019-06-12', '2019-07-12', 900, 2, 2, 2);
call sp_AgregarTurno ('2019-07-05', '2019-08-05', 700, 3, 3, 3);
call sp_AgregarTurno ('2019-06-30', '2019-06-30', 500, 1 ,1, 4);
call sp_AgregarTurno ('2019-08-01', '2019-08-16', 1000, 2, 2, 5);
call sp_AgregarTurno ('2019-09-02', '2019-09-12', 2000, 3, 3, 6);


-- Areas listar
call sp_ListarAreas();

-- Cargos Listar 
call sp_ListarCargos();

-- Especialidades Listar
call sp_ListarEspecialidades();

-- Horarios Listar
call sp_ListarHorarios();

-- Pacientes listar
call sp_ListarPacientes();

-- Medicos Listar 
call sp_ListarMedicos();

-- ContactoUrgencia Listar
call sp_ListarContactosUrgencia();

-- medicoEspecialidad listar
call sp_ListarMedicosEspecialidades();

-- TelefonoMedico Listar
call sp_ListarTelefonosMedicos();

-- ResponsableTurno Listar
call sp_ListarResponsablesTurnos();

-- Turno Listar
call sp_ListarTurnos();

-- Areas editar
call sp_EditarArea();

-- Cargos editar
call sp_EditarCargo();

-- Especialidades editar
call sp_EditarEspecialidad();

-- horarios editar
call sp_EditarHorario();

-- Medicos editar 
call sp_EditarMedico();

-- contactoUrgencia editar
call sp_EditarContactoUrgencia();

-- medicoEspecialidad editar 
call sp_EditarMedicoEspecialidad();

-- telefonoMedico editar
call sp_EditarTelefonoMedico();

-- responsableTurno editar
call spEditarResponsableTurno();

-- turno editar
call sp_EditarTurno();

-- areas buscar
call sp_BuscarArea();

-- cargos buscar
call sp_BuscarCargo();

-- Especialidades buscar
call sp_BuscarEspecialidad();

-- Pacientes buscar 
call sp_BuscarPaciente();

-- medicos buscar 
call sp_BuscarMedico();

-- ContactoUrgencia buscar
call sp_BuscarContactoUrgencia();

-- MedicoEspecialidad buscar
call sp_BuscarMedicoEspecialidad();

-- TelefonoMedico buscar
call sp_BuscarTelefonoMedico();

-- ResponsableTurno buscar
call sp_BuscarResponsableTurno();

-- Turno buscar
call sp_BuscarTurno();

--  Areas Eliminar --
call sp_EliminarArea();

-- Cargos Elimiar --
call sp_EliminarCargo();

-- Especialidades Eliminar --
call sp_EliminarEspecialidad();

-- Horarios eliminar --
call sp_EliminarHorario();

-- Pacientes eliminar -
call sp_EliminarPaciente();

-- Medicos eliminar --
call sp_EliminarMedico();

-- ContactoUrgencia eliminar --
call sp_EliminarContactoUrgencia();

-- MedicoEspecialidad eliminar --
call sp_EliminarMedicoEspecialidad();

-- TelefonoMedico eliminar --
call sp_EliminarTelefonoMedico();

-- ResponsbaleTurno eliminar --
call sp_EliminarResponsableTurno();

-- Turno eliminar --
call sp_EliminarTurno();

select * from Pacientes INNER JOIN ContactoUrgencia ON Pacientes.codigoPaciente = contactoUrgencia.codigoPaciente;


 alter user 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'admin';
 
 
 call sp_BuscarUsuario('areyes2018391', 'admin');
 



