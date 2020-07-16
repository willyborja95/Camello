package com.apptec.camello.util;

import com.apptec.camello.R;

import org.jetbrains.annotations.NotNull;

/**
 * A class that returns the corresponding resource according to the server code error
 */
public class ErrorDictionary {


//    AuthException	400	Email o password incorrectos.
//    AuthException	300	Usuario no autenticado
//    AuthException	301	Usuario no autorizado
//    AuthException	302	Refresh Token inválido
//    AuthException	303	Refresh Token expirado, porfavor inicie sesión nuevamente
//    AuthException	304	Se debe especificar el token de acceso


//    JsonWebToken	501	Token inválido
//    JsonWebToken	502	Token expirado


//    NotFoundException	404	Recurso no encontrado/a


//    ValidationException	403	Campos invalidos


    //    AttendanceException	600	Fuera de horario laboral
    public static int get600() {
        return R.string.server_error_600_outside_working_hours;
    }

    //    AttendanceException	601	No se pudo sincronizar la asistencia correctamente
    public static int get601() {
        return R.string.server_error_601;
    }


//    DeviceException	700	No se puede registrar desde el dispositivo de otro empleado
//    DeviceException	701	Dispositivo deshabilitado, contacte al Administrador de su empresa


//    AppError	801	Llave expirada
//    AppError	800	Llave invalida
//    AppError	802	Archivo con extensión: .${ext} inválida, las extensiones validas son:
//    AppError	803	No se pudo sincronizar correctamente
//    AppError	805	No se pudo completar la acción, Límite de cuota alcanzado contáctenos a: info@camello.com.ec para solicitar un aumento de cuota
//    AppError	806	No se pudo completar la acción, actualmente se encuentra con el servicio deshabilitado, contáctenos a: info@camello.com.ec para solicitar su reactivación


//    ForeignKeyConstraintError	804	No se puede eliminar dicho recurso, o no existe su referencia


//    NotificationException	900	El codigo (pushtoken) del dispositivo no esta registrado...


    public static int getErrorMessageByCode(@NotNull int server_code) {
        switch (server_code) {
            case 600:
                return get600();
            case 601:
                return get601();
            default:
                return R.string.unknown_error;
        }

    }


}
