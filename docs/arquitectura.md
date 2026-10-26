# Arquitectura

El paquete raíz se escanea automáticamente: cada controlador de `modules/{modulo}/api` registra sus propios servicios REST. No hay un router central adicional en Spring MVC. Cada módulo separa dominio, aplicación, infraestructura y API. El dominio no depende de Spring; la comunicación entre módulos se realiza por puertos o eventos, nunca leyendo tablas ajenas.

Esta entrega es una implementación dummy: no se accede a Oracle, no se invoca PL/SQL y no se llama a satélites. Antes de conectar persistencia deben aprobarse las migraciones, los permisos append-only de auditoría, la conciliación y las pruebas de integración. Seguridad JWT y autorización de cierre operan en el backend, independientemente de la interfaz de usuario.
