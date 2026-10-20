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
- Modelos creados en `data/model`: `Category`, `Expense`, `Frequency`, `RecurringType`, `Recurring`, `SavingsPlan`, `SavingsTemplate`, `InvestmentFund`, `NoticeSource`, `PendingNotice`.
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
- Pendiente: serialización de modelos; capa DataStore, repositorios, ViewModels y pantallas.
- Las carpetas `core/di`, `data/datasource`, `data/repository` y `presentation/{components,feature,navigation}` están planeadas y aún no existen.

## Decisiones de diseño
- **Rango de tasa (RF-25):** mayor a 0 y como máximo el equivalente a 100 % anual: 0.19 % diaria, 1.34 % semanal, 5.95 % mensual.
- **Cuota de ahorro:** se redondea hacia arriba; la última cuota cubre solo lo que falta.
- **Riesgo de fin de mes:** margen = saldo proyectado / (saldo + ingresos restantes). Bajo ≥ 50 %, Medio ≥ 25 %, Alto < 25 %, Crítico si el saldo proyectado es negativo.
- **Ocurrencias mensuales:** se calculan desde la fecha de inicio para que un día 31 no se desplace tras un mes corto.

> `tree.txt` contiene el árbol del proyecto y está excluido de git.
