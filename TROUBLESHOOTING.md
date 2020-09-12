

# Problemas con la aplicación en Android
En ciertos casos, algunos dispositivos con Android pueden bloquear la aplicación de Camello sin consultarle al usuario. Este comportamiento mucho depende mucho del fabricante del dispositivo. A continuación se presentan algunas soluciones a problemas de los que somos conscientes en ciertos dispositivos. Si necesitas ayuda adicional puedes escribirnos a soporte@camello.com

El principal paso que se debe seguir es impedir que el sistema trate de optimizar la batería apagando Camello. No te preocupes, nuestra app ya maneja la optimización de batería por su cuenta.

> Nota: Después de seguir los siguientes pasos, te recomendamos reiniciar el dispositivo para asegurar que todos los cambios se hayan aplicado exitosamente.

## Sistemas operativos Android
### Versión de Android 6.0.1+
Para los dispositivos que ejecutan la versión de Android 6.0.1 y posteriores , verifique que la optimización de la batería no impida que Camello le envíe notificaciones:
1. Diríjase a **Configuraciones** o **Ajustes**.
2. Selecciones **Batería**.
3. Toque el **ícono de menú con los tres puntos** y escoja **Optimización de batería**.
4. Toque el **cursor hacia abajo** y seleccione ** Todas las aplicaciones**.
5. Toque **Camello** y luego **No optimizar**.


### Versión de Android 7+
1. Diríjase a **Configuraciones** o **Ajustes**.
2. Selecciones **Aplicaciones**.
3. Busque y toque **Camello**.
4. Seleccione **Batería**, luego toque **Optimización de batería**
5. Desde aquí, seleccione la opción **Aplicaciones no optimizadas** y cámbiela a **Todas las aplicaciones**.
6. Busque **Camello** en esta lista y configúralo en **No optimizar**.

### Versión de Android 9
Para los dispositivos que ejecutan la versión 9 de Android, asegúrese de que la función de batería adaptable no impida que Camello le envíe notificaciones:
1. Diríjase a **Configuraciones** o **Ajustes**.
2. Selecciones **Batería**.
3. Elija **Batería adaptable**
4. Toque **Aplicaciones restringidas** y asegúrese de que Camello no esté en la lista.

## Aplicaciones de terceros
Algunas aplicaciones de terceros tienen permiso sobre otras aplicaciones para cerrarlas. Asegúrese de que no suceda esto con Camello.
### Clean Master
Esta aplicación maneja el ahorro de batería y la RAM.
1. Visite la pestaña de **Herrameintas** de la aplicación.
2. Seleccione **Limpiador de notificaciones**
3. Presione el **ícono de ajustes**.
4. **Desactive** esta función solo para Camello o para todas las aplicaciones.

### Security Master
Esta aplicación de seguridad de terceros puede estar impidiendo que Camello envíe notificaciones:
1. Visite el **Limpiador de notificaciones** de la aplicación.
2. Presione el **ícono de ajustes**.
3. Asegúrese de que Camello esté **desmarcado** y listado en **Aplicaciones - Notificaciones permitidas**.

> En algunos casos, es posible que deba desinstalar la aplicación de terceros para recibir notificaciones.
## Modelos de dispositivos Android
A continuación se muestra una lista de dispositivos y modelos Android que se sabe que tienen problemas con Camello.
### Azus Zenfone (Android 7.0)
#### Paso 1: Asegúrese de que Camello esté configurado para iniciarse automáticamente.
1. Diríjase a **Configuraciones** o **Ajustes**.
2. Toque **Administración de energía**.
3. Seleccione **Administración de inicio automático**.
4. Encuentre Camello y asegúrese de que está habilitado.
#### Paso 2: Incluya Camello como una aplicación protegida 
1. Diríjase a **Configuraciones** o **Ajustes**.y toque **Administrador Móvil**.
2. Toque **Impulsador**
3. Toque **Habilitar super impulsador**, luego seleccione **Lista de aplicaciones protegidas**.
4. Verifique que Camello esté en la lista.

### Azus Zenfone 2
Asegúrese de que Camello pueda comenzar al inicio desde su administrador de inicio autmático.
1. Diríjase a **Configuraciones** o **Ajustes**, y toque **Aplicaciones**.
2. Presione **Administrador de inicio automático**.
3. Ponga a Camello en **Permitir**.

### Huawei Honor 6
#### Paso 1: Inlcuya a Camello como una aplicación protegida
1. Diríjase a **Configuraciones** o **Ajustes**.
2. Toque **Ahorro de energía**.
3. Toque **Aplicaciones protegidas**.
4. Ponga a Camello en **Habilitar**.
#### Paso 2: Habilite a Camnello en el Administrador de notificaciones
1. Diríjase a **Configuraciones**.
2. Toque **Administrador de notificaciones**.
3. Seleccione **Notificar** a Camello para recibir notificaciones automáticas.

### Huawei Honor 8
#### Paso 1: Incluya a Camello como una aplicación protegida.
1. Diríjase a **Configuraciones** o **Ajustes**.
2. Toque **Configuración avanzada**.
3. Toque **Administrador de batería**.
4. Toque **Aplicaciones protegidas** (o **Cerrar aplicaciones depués del bloqueo de pantalla**).
5. Seleccione **Habilitar** para Camello.
#### Paso 2: Ignora las optimizaciones de batería para Camello
1. Diríjase a **Configuraciones** o **Ajustes**.
2. Toque **Aplicaciones**.
3. Toque **Avanzado**.
4. Seleccione **Ignorar optimización de batería**.
5. Elija **Camello** e ignore la aplicación.
#### Paso 3: Permitir notificaciones de Camello.
1. Diríjase a **Configuraciones**.
2. Seleccione **Panel de notificaciones y barra de estado**.
3. Asegúrese de que **Permitir notificaciones** y ** Visualización de prioridad** estén **activos**.

## Huawei P8 lite
#### Paso 1. Habilita Camello para que se ejecute al inicio.
1. Diríjase a **Administrador del teléfono**.
2. Desliza el dedo hacia la izquierda y toca **Administrador de Inicio**.
3. Toque **Camello** y asegúrese de que esté configurado para ejecutarse automáticamente al iniciar el sistema.

#### Paso 2: Permitir notificaciones de Camello.
1. Diríjase a **Administrador del teléfono**.
2. Deslícese hacia la izquierda y toque **Administrador de notificaciones**.
3. Toque **Reglas** y asegúrese de que Camello esté configurado para enviar notificaciones al panel de notificaciones. Verifique si alguna otra configuración en esta vista puede estar bloqueando las notificaciones.

### Huawei Mate 8 y Huawei Nova Plus
Asegúrese de que Camello esté extentos de las funciones de optimización de la batería de su dispositivo:
1. Diríjase a **Configuraciones** o **Ajustes**.
2. Toque **Configuración avanzada**.
3. Toque **Administración de batería**.
4. Toque **Aplicacione sprotegidas**.
5. Seleccione a **Camello** para habilitar.

### Huawei P20 y P30
1. Diríjase a **Configuraciones** o **Ajustes**.
2. Toque **Aplicaciones**.
3. Toque **Camello**
4. Seleccione **Uso de energía**, seguido de **Inicio de la aplicación**.
5. Cambie **Administrar automáticamente** a **Desactivado**.
6. A continuación, alternar **Auto inicio, Inicio secundario** y **Ejecutar en segundo plano** a **Habilitado**

### LeEco / LeTV
#### Paso 1: Habilita Camello para que se ejecute al inicio
1. Diríjase a **Configuraciones** o **Ajustes**.
2. Toque **Permisos**
3. Toque **Administrador de inicio automático**. Si ha habilitado otras aplicaciones para que se inicien automáticamente, verá **Ha habilitado [x] aplicaciones para que se inicien automáticamente.
4. Alternar **Camello** para habilitar.
#### Paso 2: Incluya  Camello como una aplicación protegida
1. Diríjase a **Configuraciones** o **Ajustes**.
2. Seleccione **Batería**, luego toque **Administración de ahorro de energía**.
3. Seleccione **Protección de aplicaciones**.
4. Alternar **Camello** para habilitar.

### Lenovo
Habilite la configuración de inicio automático para Camello:
1. Diríjase a **Configuraciones** o **Ajustes**.
2. Toque **Administrador de energía**.
3. Seleccione **Gestión de aplicaciones en segundo plano**.
4. Cambie Camello para **Permitir inicio automático**.

### OnePlus (Android 7.0 y anterior)
Para versiones anteriores a Android 8.0 Oreo, asegúrese de que Camello esté configurado para iniciarse automáticamente.
1. Diríjase a **Configuraciones** o **Ajustes**.
2. Toque **Aplicaciones**.
3. Presione el **ícono de ajustes** en la esquina superior derecha.
4. Seleccione **Inicio automático de la aplicación**.
5. Alternar **Camello** para habilitar.

### OnePlus (Android 8.0 y posterior)
#### Paso 1: Verifique que el almacenamiento de la aplicación esté configurado para borrarse normalmente.
1. Diríjase a **Configuraciones** o **Ajustes**.
2. Toque **Avanzado**.
3. Seleccione **Gestión de aplicaciones recientes**.
4. Asegúrese de que el **borrado normal** esté habilitado.

#### Paso 2: Excluir Camello de la optimización de la batería
1. Diríjase a **Configuraciones** o **Ajustes**.
2. Toque  **Batería**, luego **Optimización de la batería**.
3. Toque **Aplicaciones no optimizadas** y seleccione **Todas las aplicaciones**
4. Busque Camello y asegúrese de que esté configurado en **No optimizar**.

### OnePlus 3
Ajuste la optimización de la batería para permitir a Camello funcionar correctamente en segundo plano:
1. Diríjase a **Configuraciones** o **Ajustes**.
2. Toque **Batería**.
3. Seleccione **Optimización de batería**.
4. Toque el **ícono de opciones** con tres puntos en la parte superior derecha de la pantalla.
5. Seleccione **Optimización avanzada**.
6. Alternar **Camello** a **Apagado**.

### OPPO
#### Paso 1: Incluya Camello como una aplicación protegida
1. Diríjase a **Configuraciones** o **Ajustes**.
2. Toque **Configuración avanzada**.
3. Toque **Administrador de batería**, luego **Aplicaciones protegidas**.
4. Alternar **Camello** para habilitar la protección.
#### Paso 2: Ignora la optimización de la batería para Camello
1. Diríjase **Configuraciones** o **Ajustes**.
2. Seleccione **Batería**, luego toque **Ahorro de energía**.
3. Busque **Camello** y asegúrese de que **Congelar en segundo plano**, **Optimización de aplicaciones anormales** y **Descanso** estén desactivados.
#### Paso 3: Permitir notificaciones de Camello
1. Diríjase a **Configuraciones** o **Ajustes**.
2. Toque **Panel de notificaciones y barra de estado**.
3. Toque **Centro de notificaciones**.
4. Encuentra Camello y activa **Permitir notificaciones** y **Visualización de prioridad**.
#### Paso 4: Agrega Camello para iniciar el administrador
1. Diríjase a la aplicación de **Seguridad**.
2. Seleccione **Permisos de privacidad**, luego **Iniciar administrador**.
3. Busque **Camello** y asegúrese de que esta configuración esté habilitada.

### Samsung
Excluya a Camello de la optimización de batería:
1. Diríjase a **Configuraciones** o **Ajustes**.
2. Presione **Mantenimiento del dispositivo**.
3. Seleccione **Mantenimiento del dispositivo**.
4. Seleccione **Batería**.
5. Presione **Aplicaciones no monitoreadas** al final de la pantalla.
6. Agregue **Camello** a esta lista.

### Xiaomi
#### Paso 1: Verifica que Camello tenga permiso para iniciar automáticamente.
1. Diríjase a **Seguridad**
2. Toque **Permisos**
3. Toque **Inicio automático**
4. Alternar **Camello** para habilitar.
#### Paso 2: Ignora las restricciones de batería para Camello.
1. Diríjase a **Configuraciones** o **Ajustes**.
2. Seleccione **Administra el uso de la batería de las aplicaciones**
3. Toque **Aplicaciones**.
4. Toque **Camello** y luego **Sin restricciones**.
#### Paso 3: Permitir notificaciones de Camello
1. Diríjase a **Configuraciones** o **Ajustes**.
2. Seleccione **Notificaciones de la aplicación**.
3. Toque **Camello**.
4. Active **Establecer como prioridad**.
