# Tenge

App Android para administrar dinero: saldo, planes de ahorro, gastos, ingresos/gastos recurrentes e inversiones.

## Stack
- Kotlin + Jetpack Compose (Material 3)
- Jetpack DataStore 1.2.1 tipado con kotlinx.serialization 1.9.0 (persistencia local en JSON)
- Navigation Compose 2.10.2 · Lifecycle 2.11.0 (ViewModel y `collectAsStateWithLifecycle`)
- Arquitectura MVVM con UDF y StateFlow
- Principios SOLID y Atomic Design para la UI

## Configuración
- Paquete: `com.zukesito.tenge`
- minSdk 26 · targetSdk/compileSdk 37 · Java 11

## Estructura (`app/src/main/java/com/zukesito/tenge`)
| Carpeta | Propósito |
|---|---|
| `core/di` | Inyección de dependencias |
| `core/finance` | Lógica financiera compartida |
| `data/datasource` | Fuentes de datos (DataStore) |
| `data/model` | Modelos de datos |
| `data/repository` | Repositorios |
| `presentation/components` | Componentes UI stateless, agrupados por función (ver tabla de Atomic Design) |
| `presentation/feature` | Pantallas, `UiState` y `ViewModel`: home, expenses, recurring, savings, investments |
| `presentation/navigation` | Rutas tipadas (`Destination`) y estructura general (`TengeApp`) |
| `presentation/theme` | Tema Material 3 claro/oscuro, tipografía, colores de riesgo e íconos |
| `presentation/labels` | Textos de los enums (`@StringRes`) |

## Estado
- `MainActivity` muestra `TengeApp` (navegación con barra inferior) dentro de `TengeTheme`.
- Modelos creados en `data/model`: `Category`, `Expense`, `Frequency`, `RecurringType`, `Recurring`, `SavingsPlan`, `SavingsTemplate`, `InvestmentFund`, `NoticeSource`, `PendingNotice`, `BalanceAdjustment`, `LocalDateSerializer`.
- `core/finance` implementado con funciones de extensión puras (RNF-06):

| Archivo | Contenido | Requisitos |
|---|---|---|
| `FrequencyExt.kt` | Fechas por periodo, ocurrencias, tasa máxima por frecuencia | RF-10, RF-12, RF-25 |
| `FormatExt.kt` | Formato MXN y fecha es-MX, texto → centavos | RNF-15, RNF-10 |
| `ValidationExt.kt` | Validación de montos, nombres, plazos, tasas y saldo | RF-03, RF-25 |
| `ExpenseExt.kt` | Gastos del mes, total y desglose por categoría | RF-05 |
| `RecurringExt.kt` | Próximo vencimiento y ocurrencias pendientes | RF-10, RF-12 |
| `SavingsPlanExt.kt` / `SavingsStatus.kt` | Cuota, progreso, fecha estimada y estado del plan | RF-14, RF-16, RF-17, RF-18 |
| `InvestmentFundExt.kt` | Interés compuesto, valor futuro y patrimonio | RF-01, RF-21, RF-22 |
| `MonthEndProjection.kt` / `RiskLevel.kt` | Proyección a fin de mes y nivel de riesgo | RF-07, RF-08 |
| `Occurrence.kt` | Fecha + monto de una ocurrencia pendiente | RF-12 |

- Dependencias agregadas: DataStore, kotlinx.serialization, ViewModel/Lifecycle Compose y Navigation Compose.
- Serialización: `LocalDateSerializer` guarda fechas como texto ISO; `Category`, `SavingsTemplate`, `Expense`, `Recurring`, `SavingsPlan` e `InvestmentFund` son `@Serializable` (`PendingNotice` no se persiste).
- `data/datasource` implementado: `AppState` (estado completo serializable), `AppStateSerializer` (JSON con `ignoreUnknownKeys`), interfaz `AppStateDataSource` e implementación `DataStoreAppStateDataSource` (archivo `tenge_state.json`).
- `data/repository` (interfaz + implementación por entidad, RNF-04):
  - `BalanceRepository`: saldo y ajuste manual; cada ajuste se guarda con su nota en `AppState.adjustments` (RF-01, RF-02, RF-03).
  - `ExpenseRepository`: registrar y eliminar gastos, descontando o devolviendo el monto al saldo en la misma transacción (RF-04, RF-06).
  - `CategoryRepository`: catálogo de categorías (solo lectura).
  - `RecurringRepository`: crear y eliminar recurrentes (RF-09, RF-11).
  - `SavingsRepository`: planes y plantillas; crear y eliminar planes (RF-15, RF-19).
  - `InvestmentRepository`: registrar y eliminar fondos (RF-20, RF-23).
  - `NoticeRepository`: genera los avisos vencidos y los confirma o ignora en una sola transacción (RF-12, RF-13, RF-14).
  - `InsufficientBalanceException`: se lanza dentro de la transacción si el saldo quedaría negativo (RF-03).
- `core/di` implementado: interfaz `AppContainer` y `DefaultAppContainer` (crea un solo `AppStateDataSource` y los repositorios con `lazy`). `TengeApplication` mantiene el contenedor y está registrada en `AndroidManifest.xml`.
- Datos semilla (RF-24): `SeedData` + `SeedDataSource`/`AssetSeedDataSource` leen `assets/seed.json`; `SeedRepository` lo carga en `AppState` solo si `isSeeded` es `false`. `TengeApplication` lanza la carga al arrancar. Incluye 8 categorías (ids fijos: `food`, `transport`, …) y 5 plantillas de ahorro.
- Capa visual implementada a partir de los 13 wireframes HTML:

| Wireframe | Implementación |
|---|---|
| 01 Inicio | `HomeScreen` |
| 02 Gastos | `ExpensesScreen` |
| 03 Recurrentes | `RecurringScreen` |
| 04 Ahorro | `SavingsScreen` |
| 05 Inversiones | `InvestmentsScreen` (fondo expandible con proyección) |
| 06 Modificar saldo | `AdjustBalanceScreen`: nota opcional y resumen Saldo actual / Ajuste / Nuevo saldo |
| 07 Nuevo gasto | `ExpenseFormScreen`: categorías en varias filas y vista previa "Riesgo después de este gasto" |
| 08 Nuevo recurrente | `RecurringFormScreen`: frecuencia y recuadro "Así funcionará" |
| 09 Nuevo plan | `SavingsFormScreen`: plantillas, plazo en una línea y recuadro "Calculado por la app" (cuota, número de cuotas, fecha meta) |
| 10 Nuevo fondo | `FundFormScreen`: vista previa a 6 y 12 periodos con interés compuesto |
| 11 Diálogo eliminar | `ConfirmDeleteDialog` con título y mensaje según lo que se elimina |
| 12 Ahorro vacío | `EmptyPlaceholder` en `SavingsScreen` con ilustración y botón "Crear plan" |
| 13 Error de validación | Errores junto al campo con ícono, saldo insuficiente en el campo Monto y botón Guardar deshabilitado |
- `core/finance/FormatExt.kt` agrega `toSignedMxn`, `toMxnWhole`, `toMxShortDate`, `toMxMonthYear` y `toMxShortMonthYear`; `RecurringExt.kt` agrega `monthlyEstimateCents`.
- Todos los textos visibles están en `strings.xml` (RNF-17), incluidos plurales de periodos.
- Los comentarios del código no citan requisitos (RF/RNF) ni usan corchetes de referencia; los de documentación siguen el formato KDoc (`/** … */`); la relación entre código y requisitos se documenta solo en este README.

## Pantallas (`presentation/feature`)
Cada pantalla tiene `UiState` inmutable, `ViewModel` con `_uiState`/`uiState` y una función `Screen` que lo consume con `collectAsStateWithLifecycle()` (RNF-02, RNF-03). Los ViewModels se crean en `AppViewModelProvider` con repositorios del contenedor.

| Pantalla | Archivos | Requisitos |
|---|---|---|
| Inicio | `HomeScreen`, `HomeViewModel`, `HomeUiState` | RF-01, RF-07, RF-08, RF-12 a RF-14 |
| Modificar saldo | `AdjustBalanceScreen`, `AdjustBalanceViewModel`, `AdjustBalanceUiState` | RF-02, RF-03 |
| Gastos | `ExpensesScreen`, `ExpensesViewModel`, `ExpensesUiState` | RF-05, RF-06, RF-08 |
| Nuevo gasto | `ExpenseFormScreen`, `ExpenseFormViewModel`, `ExpenseFormUiState` | RF-03, RF-04, RF-25 |
| Recurrentes | `RecurringScreen`, `RecurringViewModel`, `RecurringUiState` | RF-10, RF-11 |
| Nuevo recurrente | `RecurringFormScreen`, `RecurringFormViewModel`, `RecurringFormUiState` | RF-09, RF-25 |
| Ahorro | `SavingsScreen`, `SavingsViewModel`, `SavingsUiState` | RF-17, RF-18, RF-19 |
| Nuevo plan | `SavingsFormScreen`, `SavingsFormViewModel`, `SavingsFormUiState` | RF-15, RF-16, RF-25 |
| Inversiones | `InvestmentsScreen`, `InvestmentsViewModel`, `InvestmentsUiState` | RF-21, RF-22, RF-23 |
| Nuevo fondo | `FundFormScreen`, `FundFormViewModel`, `FundFormUiState` | RF-20, RF-25 |

## Atomic Design (`presentation/components`)
Las carpetas se agrupan por función; esta tabla indica el nivel de cada componente (RNF-05). Ningún componente guarda estado de la app: reciben datos y lambdas.

| Componente | Carpeta | Nivel | Uso |
|---|---|---|---|
| `MoneyText` | `text/` | Átomo | Monto MXN con cifras tabulares |
| `FieldLabel` | `text/` | Átomo | Etiqueta arriba de un campo |
| `ErrorText` | `text/` | Átomo | Mensaje de error bajo un campo |
| `PrimaryPillButton`, `OutlinedPillButton` | `buttons/` | Átomo | Botones redondeados principal y secundario |
| `AddButton` | `buttons/` | Átomo | Botón flotante "+" |
| `ProgressTrack` | `indicators/` | Átomo | Barra de progreso (riesgo, ahorro, categorías) |
| `StatusBadge` | `indicators/` | Átomo | Etiqueta de estado ("Bajo", "En curso", "Cumplido") |
| `CountBadge` | `indicators/` | Átomo | Contador circular de avisos |
| `FrequencyTag` | `indicators/` | Átomo | Etiqueta "Mensual" / "Diario" |
| `IconBubble` | `indicators/` | Átomo | Ícono dentro de un círculo |
| `OptionButton` | `inputs/` | Átomo | Opción seleccionable con borde y ✓ |
| `NumberBox` | `inputs/` | Átomo | Campo numérico corto (cantidad del plazo) |
| `LabeledTextField` | `inputs/` | Molécula | Etiqueta arriba, campo con ejemplo e ícono/mensaje de error (RF-25) |
| `MoneyField` | `inputs/` | Molécula | Campo de monto con prefijo `$` |
| `DateField` | `inputs/` | Molécula | Campo de fecha con calendario |
| `SegmentedSelector` | `inputs/` | Molécula | Selector "Ingresos \| Gastos", frecuencias |
| `ChoiceChips` | `inputs/` | Molécula | Opciones en varias filas (categoría, plantilla) |
| `OptionRow` | `inputs/` | Molécula | Opciones del mismo ancho (frecuencia) |
| `TermSelector` | `inputs/` | Molécula | Plazo: cantidad + unidad en una línea |
| `PeriodSlider` | `inputs/` | Molécula | Deslizador de periodos con valor y extremos |
| `LabeledValue` | `lists/` | Molécula | Etiqueta pequeña sobre un valor |
| `CategoryBarRow` | `lists/` | Molécula | Categoría + barra + total |
| `ExpenseRow` | `lists/` | Molécula | Movimiento de gasto |
| `SectionTitle` | `lists/` | Molécula | Título de sección con contador |
| `ProjectionTable` | `lists/` | Organismo | Tabla "Periodo \| Valor" |
| `ScreenHeader` | `navigation/` | Molécula | Encabezado con título y ✕ opcional para cerrar |
| `BottomNavBar` | `navigation/` | Organismo | Barra inferior de 5 secciones |
| `EmptyState` | `feedback/` | Molécula | Mensaje de lista vacía (RNF-14) |
| `ConfirmDeleteDialog` | `feedback/` | Organismo | Confirmación antes de eliminar |
| `EmptyPlaceholder` | `feedback/` | Organismo | Pantalla vacía con ilustración y acción |
| `SectionCard` | `cards/` | Molécula | Contenedor base con borde o relleno suave |
| `BalanceCard` | `cards/` | Organismo | Saldo, patrimonio y botones de ajuste |
| `RiskCard` | `cards/` | Organismo | Riesgo de fin de mes con color dinámico |
| `NoticeCard` | `cards/` | Organismo | Aviso con Confirmar / Ignorar |
| `RecurringCard` | `cards/` | Organismo | Recurrente con frecuencia y próximo vencimiento |
| `SavingsPlanCard` | `cards/` | Organismo | Plan con progreso, cuota y fechas |
| `FundCard` | `cards/` | Organismo | Fondo expandible con proyección |
| `CategoryBreakdownCard` | `cards/` | Organismo | Total del mes y desglose por categoría |
| `SummaryPanel` | `cards/` | Organismo | Recuadro de cálculos (resumen de saldo, cuota, vista previa) |
| `InfoPanel` | `cards/` | Molécula | Recuadro informativo ("Así funcionará") |
| `RiskPreviewPanel` | `cards/` | Molécula | Riesgo después de un gasto |
| `TabScreenTemplate` | `templates/` | Plantilla | Encabezado + lista + botón flotante |
| `FormScreenTemplate` | `templates/` | Plantilla | Encabezado con ✕ + campos + botón Guardar (se deshabilita con errores) |
| `*Screen` | `feature/` | Página | Pantallas con estado (ViewModel) |

- Pendiente: probar la app completa en emulador y ajustar detalles visuales.

## Decisiones de diseño
- **Dependencias en `presentation`:** los `ViewModel` y `UiState` no importan nada de `components/`; usan sus propios modelos (`CategoryTotal`, `ProjectionPoint`) y la `Screen` los convierte a los modelos de cada componente (`CategoryShare`, `ProjectionRow`). Dirección: `components` ← `Screen` → `ViewModel` → `repository` (RNF-01).
- **Inyección de dependencias manual** (sin Hilt/Koin): un contenedor en `Application` que vive lo mismo que el proceso, así sobrevive a la rotación (RNF-12) y DataStore tiene una sola instancia.
- **Eliminar un gasto devuelve su monto al saldo**, porque al registrarlo se descontó (RF-04/RF-06).
- **Nota del ajuste de saldo (RF-02):** cada ajuste se guarda como `BalanceAdjustment` (monto con signo, nota y fecha) en la misma transacción que cambia el saldo.
- **Un solo `AppState` en DataStore:** las operaciones que cambian varios valores (ej. confirmar una cuota) se hacen en un solo `updateData`, así que se guardan completas o no se guardan (RNF-09). Si una validación lanza una excepción dentro de la transacción, no se escribe nada.
- **Rango de tasa (RF-25):** mayor a 0 y como máximo el equivalente a 100 % anual: 0.19 % diaria, 1.34 % semanal, 5.95 % mensual.
- **Cuota de ahorro:** se redondea hacia arriba; la última cuota cubre solo lo que falta.
- **Riesgo de fin de mes:** margen = saldo proyectado / (saldo + ingresos restantes). Bajo ≥ 50 %, Medio ≥ 25 %, Alto < 25 %, Crítico si el saldo proyectado es negativo.
- **Ocurrencias mensuales:** se calculan desde la fecha de inicio para que un día 31 no se desplace tras un mes corto.

- **Validación de formularios (RF-25):** los errores aparecen después del primer intento de guardar y luego se actualizan en vivo; mientras haya errores el botón Guardar está deshabilitado. El saldo insuficiente y la tasa fuera de rango se avisan en cuanto se escriben. Los errores de monto se modelan con el enum `AmountError` en el ViewModel y la pantalla los traduce a texto.
- **Fecha meta del plan:** es la fecha de la última cuota (la primera cuota es la fecha de inicio), igual que la "Fecha límite" de la pantalla de Ahorro.
- **Eliminar con pulsación larga:** los mockups no muestran cómo borrar; se mantiene presionado un elemento y aparece un diálogo de confirmación (RF-06, RF-11, RF-19, RF-23).
- **Avisos en orden:** solo se muestra el aviso más antiguo de cada recurrente o plan; al atenderlo aparece el siguiente. Así no se puede saltar una ocurrencia.
- **Colores de riesgo:** los mockups son en grises, pero RF-08 pide verde a rojo; la barra y el badge usan verde (Bajo), amarillo (Medio), naranja (Alto) y rojo (Crítico).
- **Fuente:** los mockups usan IBM Plex Sans; la app usa la fuente del sistema porque agregarla requiere archivos de fuente o la librería de Google Fonts.
- **Íconos:** trazos de Material Icons dibujados con `ImageVector`, sin agregar dependencias.
- **Estimado mensual de recurrentes:** diario × 30, semanal × 52/12, mensual × 1.
- **Componentes y estado:** `DateField` solo recuerda si su calendario está abierto (estado visual), no datos de la app.

> `tree.txt` contiene el árbol del proyecto y está excluido de git.
