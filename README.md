# Prestamos-2 — Laboratorio 6: Testing

Proyecto del Laboratorio 6 (Testing) de Ingeniería de Software II, UNAL.
Dominio: préstamo de equipos. Maven multi-módulo con los módulos `domain` y
`data` (el módulo `data` depende de `domain`).

## Stack (Java 21 · Maven)

| Pieza | Tecnología |
|---|---|
| Framework de pruebas | JUnit 5 (Jupiter) |
| Dobles (mocks / stubs) | Mockito |
| Cliente HTTP + JSON | `java.net.http.HttpClient` + Jackson (`jackson-databind`) |
| Servidor HTTP de prueba | MockWebServer (OkHttp) |
| Cobertura | JaCoCo |

## Comandos

```bash
# Pruebas de todos los módulos
mvn test

# Cobertura: ejecuta las pruebas y genera el reporte JaCoCo por módulo
mvn verify
```

### Reportes de cobertura

Tras ejecutar `mvn verify`:

- HTML (módulo `domain`): `domain/target/site/jacoco/index.html`
- HTML (módulo `data`): `data/target/site/jacoco/index.html`

Ábrelos en el navegador. El resumen de cobertura por clase también queda en
`target/site/jacoco/` de cada módulo.

## Estructura

```
.
├── domain/
│   ├── pom.xml
│   └── src
│       ├── main/java/edu/unal/prestamos/domain   # Reglas de negocio
│       └── test/java/edu/unal/prestamos/domain   
├── data/
│   ├── pom.xml
│   └── src
│       ├── main/java/edu/unal/prestamos/data     # Adaptadores
│       └── test/java/edu/unal/prestamos/data     
└── pom.xml                                      
```
