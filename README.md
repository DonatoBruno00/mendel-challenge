# Transactions Service

Servicio REST para almacenar transacciones en memoria con soporte para consultas por tipo y cálculo de sumas transitivas por `parent_id`.

## Arquitectura

Decidí seguir un flujo orientado a casos de uso porque me parece la forma más clara de organizar la lógica de negocio:

```
Controller → Service → UseCase → Repository
```

- **Controller**: Adaptación HTTP, validación de requests, mapeo de DTOs.
- **Service**: Orquesta casos de uso y realiza mapeo entre DTOs y entidades de dominio.
- **UseCase**: Lógica de negocio pura, una clase por caso de uso (Single Responsibility).
- **Repository**: Acceso a datos (en memoria).

### Capa de Dominio

Armé una capa de dominio sólida, priorizando que sea declarativa y lo más simple posible. La idea es que el codigo sea de facil lectura

- **Entidad `Transaction`**: Inmutable, representa una transacción financiera.
- **Value Objects**: `TransactionId`, `Amount`, `TransactionType` encapsulan tipos primitivos con validaciones de dominio.

#### ¿Por qué Value Objects?

Quería evitar el anti-patrón **Primitive Obsession**: usar tipos primitivos para representar conceptos de dominio. Por ejemplo, un `amount` no es cualquier `double`, es un concepto de dominio de la aplicacion.

Los Value Objects me permiten:
- Tener las validaciones en un solo lugar
- Que el código sea más expresivo (leer `Amount` es más claro que leer `double`)
- Que el compilador me ayude a no mezclar cosas (no puedo pasar un `TransactionId` donde espero un `Amount`)

Lo aprendí cuando vi todas las capacitaciones de Robert C Martin, como contenido adicional: https://refactoring.guru/smells/primitive-obsession

### Inmutabilidad de Transacciones

Las transacciones son **inmutables**. Una vez creadas, no se pueden modificar. Si alguien intenta crear una transacción con un ID que ya existe, devuelvo `409 Conflict`.

### ¿Por qué Use Cases?

Cada caso de uso es una clase separada que implementa una única operación de negocio:

- **Single Responsibility**: Fácil de testear, mantener y extender.
- **Explícito**: El nombre de la clase describe la intención (`CreateTransaction`, `GetTransactionsByType`, `CalculateTransactionSum`).
- **Desacoplado**: Los use cases no conocen HTTP ni persistencia, solo trabajan con entidades de dominio.

Esta estructura surgió de hacer iteraciones de Red/Green/Refactor. Eso me llevó a orientar el código hacia casos de uso concretos.

## Endpoints

### PUT /transactions/{id}
Crea una transacción. Si el ID ya existe, devuelve `409 Conflict`.

```json
Request:
{
    "amount": 5000,
    "type": "cars",
    "parentId": 10  // opcional
}

Response: 201 Created
{
    "status": "ok"
}
```

### GET /transactions/types/{type}
Devuelve los IDs de todas las transacciones de un tipo.

```json
Response: 200 OK
{
    "transactions": [10, 11, 12]
}
```

### GET /transactions/sum/{id}
Calcula la suma transitiva: el monto de la transacción + el monto de todas las transacciones vinculadas recursivamente.

```json
Response: 200 OK
{
    "sum": 8000
}
```

### GET /transactions/audit/update-attempts (Bonus)
Devuelve los intentos de actualización de transacciones existentes.

```json
Response: 200 OK
[
    {
        "transactionId": 10,
        "attemptedAt": "2026-01-15T15:30:00Z"
    }
]
```

## Bonus: Registro de Intentos de Update

Mientras desarrollaba el challenge, me di cuenta de algo: si las transacciones son inmutables y devuelvo `409 Conflict` cuando alguien intenta crear una con un ID existente.

- Cada vez que se intenta crear una transacción con un ID existente, registro el intento antes de devolver el error
- Un endpoint para consultar todos estos intentos
- Esto permite detectar patrones sospechosos (ej: muchos intentos sobre el mismo ID, o desde el mismo origen)

 Es un primer paso para tener visibilidad sobre comportamientos anómalos.

**Componentes:**
- `UpdateAttempt`: Entidad con `transactionId` y `attemptedAt`
- `RegisterUpdateAttemptUseCase`: Registra el intento antes de lanzar la excepción
- `GetUpdateAttemptsUseCase`: Consulta los intentos registrados

## Cómo correr

### Local
```bash
./mvnw spring-boot:run
```

### Docker
```bash
docker-compose up --build
```

Swagger UI: http://localhost:8080/swagger-ui/index.html

---

*Este README se va a ir actualizando con cada PR.*
