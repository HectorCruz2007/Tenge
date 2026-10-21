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
| `presentation/components` | Componentes UI por nivel de Atomic Design |
| `presentation/feature` | Pantallas: home, savings, expenses, recurring, investments |
| `presentation/navigation` | Navegación |
| `presentation/theme` | Tema de la app (`TengeTheme`) |

## Estado
- `MainActivity` muestra un `Surface` vacío con `TengeTheme`.
- Modelos creados en `data/model`: `Category`, `Expense`, `Frequency`, `RecurringType`, `Recurring`, `SavingsPlan`, `SavingsTemplate`, `InvestmentFund`, `NoticeSource`, `PendingNotice`, `LocalDateSerializer`.
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
- `data/repository` iniciado (interfaz + implementación por entidad, RNF-04):
  - `BalanceRepository`: saldo y ajuste manual (RF-01, RF-02, RF-03).
  - `ExpenseRepository`: registrar y eliminar gastos, descontando o devolviendo el monto al saldo en la misma transacción (RF-04, RF-06).
  - `CategoryRepository`: catálogo de categorías (solo lectura).
  - `InsufficientBalanceException`: se lanza dentro de la transacción si el saldo quedaría negativo (RF-03).
- `core/di` implementado: interfaz `AppContainer` y `DefaultAppContainer` (crea un solo `AppStateDataSource` y los repositorios con `lazy`). `TengeApplication` mantiene el contenedor y está registrada en `AndroidManifest.xml`.
- Datos semilla (RF-24): `SeedData` + `SeedDataSource`/`AssetSeedDataSource` leen `assets/seed.json`; `SeedRepository` lo carga en `AppState` solo si `isSeeded` es `false`. `TengeApplication` lanza la carga al arrancar. Incluye 8 categorías (ids fijos: `food`, `transport`, …) y 5 plantillas de ahorro.
- Pendiente: repositorios de recurrentes, ahorro, inversiones y avisos; ViewModels y pantallas.
- Las carpetas `presentation/{components,feature,navigation}` están planeadas y aún no existen.

## Decisiones de diseño
- **Inyección de dependencias manual** (sin Hilt/Koin): un contenedor en `Application` que vive lo mismo que el proceso, así sobrevive a la rotación (RNF-12) y DataStore tiene una sola instancia.
- **Eliminar un gasto devuelve su monto al saldo**, porque al registrarlo se descontó (RF-04/RF-06).
- **La nota del ajuste manual de saldo (RF-02) no se guarda**: no hay historial de movimientos que la muestre.
- **Un solo `AppState` en DataStore:** las operaciones que cambian varios valores (ej. confirmar una cuota) se hacen en un solo `updateData`, así que se guardan completas o no se guardan (RNF-09). Si una validación lanza una excepción dentro de la transacción, no se escribe nada.
- **Rango de tasa (RF-25):** mayor a 0 y como máximo el equivalente a 100 % anual: 0.19 % diaria, 1.34 % semanal, 5.95 % mensual.
- **Cuota de ahorro:** se redondea hacia arriba; la última cuota cubre solo lo que falta.
- **Riesgo de fin de mes:** margen = saldo proyectado / (saldo + ingresos restantes). Bajo ≥ 50 %, Medio ≥ 25 %, Alto < 25 %, Crítico si el saldo proyectado es negativo.
- **Ocurrencias mensuales:** se calculan desde la fecha de inicio para que un día 31 no se desplace tras un mes corto.

> `tree.txt` contiene el árbol del proyecto y está excluido de git.
