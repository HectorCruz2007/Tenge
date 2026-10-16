# Tenge

App Android para administrar dinero: saldo, planes de ahorro, gastos, ingresos/gastos recurrentes e inversiones.

## Stack
- Kotlin + Jetpack Compose (Material 3)
- Jetpack DataStore (persistencia, pendiente de agregar)
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
- Modelos creados en `data/model`: `Category`, `Expense`, `Frequency`, `RecurringType`, `Recurring`, `SavingsPlan`, `SavingsTemplate`.
- Pendiente: modelos `InvestmentFund`, `PendingNotice`; capa DataStore, repositorios, ViewModels y pantallas.
- Las carpetas `core/`, `data/datasource`, `data/repository` y `presentation/{components,feature,navigation}` están planeadas y aún no existen.

> `tree.txt` contiene el árbol del proyecto y está excluido de git.
