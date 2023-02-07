            # language: es

            Característica: cargar un pedido para cliente domicilio
            módulo de administración y cara de pedidosde productospara delivery

            Esquema del escenario: Cargar nuevo pedido a cliente que existe con domicilio ya registrado
            Dado que llama el cliente existente con razón social "<RazonSocial>" con cuit "<cuit>"
            Y que solicita se envíe el pedido al domicilio existente en la localidad "<Localidad>" calle "<Calle>" y altura <Altura>
            Y que pide los siguientes artículos
            | articulo | nombre                  | cantidad | precioUnitario |
            | 1000     | "remache corto"         | 10       | 2.3            |
            | 1001     | "remache mediano"       | 40       | 3.1            |
            | 2000     | "tornillo largo"        | 125      | 3.4            |
            | 3000     | "lija 0.5"              | 6        | 0.45           |
            | 3001     | "lija 1"                | 10       | 0.77           |
            | 4000     | "pintura blanca x 4lts" | 1        | 390            |
            Cuando guarda el nuevo pedido con fecha "<Fecha>"
            Entonces obtiene la siguiente respuesta <Respuesta>

            Ejemplos:
            | Cliente | RazonSocial   | cuit     | Localidad     | Calle      | Altura | Fecha      | Respuesta                                                                                                                                                                               |
            | 1000    | Juan Perez    | 10100100 | Trelew        | San Martín | 100    | 2020-05-12 | "{\"StatusCode\": 200,\"StatusText\": \"Se cargó el pedido exitosamente\",\"data\":{\"pedido\":1,\"fecha\":\"2020-05-12\",\"cliente\": 1000,\"domicilio\": 1001,\"total\": 972.4 }}"    |
            | 1000    | Juan Perez    | 10100100 | Trelew        | Belgrano   | 100    | 2020-05-12 | "{\"StatusCode\": 200,\"StatusText\": \"Se cargó el pedido exitosamente\",\"data\":{\"pedido\":2,\"fecha\":\"2020-05-12\",\"cliente\": 1000,\"domicilio\": 1002,\"total\": 972.4 }}"    |
            | 2000    | Raul Iriarte  | 20200200 | Puerto Madryn | Roca       | 200    | 2020-05-12 | "{\"StatusCode\": 200,\"StatusText\": \"Se cargó el pedido exitosamente\",\"data\":{\"pedido\":3,\"fecha\":\"2020-05-12\",\"cliente\": 2000,\"domicilio\": 2001,\"total\": 972.4 }}"    |
            | 2000    | Raul Iriarte  | 20200200 | Puerto Madryn | Roca       | 200    | 2020-05-12 | "{\"StatusCode\": 200,\"StatusText\": \"Se cargó el pedido exitosamente\",\"data\":{\"pedido\":4,\"fecha\":\"2020-05-12\",\"cliente\": 2000,\"domicilio\": 2001,\"total\": 972.4 }}"    |
            | 3000    | Rosana Lirios | 30300300 | Puerto Madryn | Maiz       | 300    | 2020-05-12 | "{\"StatusCode\": 200,\"StatusText\": \"Se cargó el pedido exitosamente\",\"data\":{\"pedido\":5,\"fecha\":\"2020-05-12\",\"cliente\": 3000,\"domicilio\": 3001,\"total\": 972.4 }}"    |
            | 4000    | Marta Ríos    | 40400400 | Rawson        | Maiz       | 400    | 2020-05-12 | "{\"StatusCode\": 200,\"StatusText\": \"Se cargó el pedido exitosamente\",\"data\":{\"pedido\":6,\"fecha\":\"2020-05-12\",\"cliente\": 4000,\"domicilio\": 4001,\"total\": 972.4 }}"    |
            | 5000    | José Quintana | 50500500 | Puerto Madryn | Sarmiento  | 500    | 2020-05-12 | "{\"StatusCode\": 200,\"StatusText\": \"Se cargó el pedido exitosamente\",\"data\":{\"pedido\":7,\"fecha\":\"2020-05-12\",\"cliente\": 5000,\"domicilio\": 5002,\"total\": 972.4 }}"    |
            | 8000    | Laura Agosto  | 80800800 | Puerto Madryn | Bvd. Brown | 800    | 2020-05-15 | "{\"StatusCode\": 200,\"StatusText\": \"Se cargó el pedido exitosamente\",\"data\":{\"pedido\":8,\"fecha\":\"2020-05-15\",\"cliente\": 8000,\"domicilio\": 8001,\"total\": 972.4 }}"    |
            | 9000    | Martín Burla  | 90900900 | Puerto Madryn | Chiquichan | 900    | 2020-05-15 | "{\"StatusCode\": 200,\"StatusText\": \"Se cargó el pedido exitosamente\",\"data\":{\"pedido\":9,\"fecha\":\"2020-05-15\",\"cliente\": 9000,\"domicilio\": 9002,\"total\": 972.4 }}"    |
            | 10000   | Pedro Villa   | 11001000 | Trelew        | Yrigoyen   | 1000   | 2020-05-15 | "{\"StatusCode\": 200,\"StatusText\": \"Se cargó el pedido exitosamente\",\"data\":{\"pedido\":10,\"fecha\":\"2020-05-15\",\"cliente\": 10000,\"domicilio\": 10001,\"total\": 972.4 }}" |

            Esquema del escenario: Cargar nuevo pedido a cliente registrado con domicilio existente
            Dado que llama el cliente existente con razón social "<RazonSocial>" con cuit "<cuit>"
            Y que solicita se envíe el pedido al domicilio existente en la localidad "<Localidad>" calle "<Calle>" y altura <Altura>
            Y que pide los siguientes artículos
            | articulo | nombre                | cantidad | precioUnitario |
            | 5000     | Machimbre 10mm        | 20       | 355.25         |
            | 6000     | Aceite de lino x 4lts | 1        | 450.10         |
            Cuando guarda el nuevo pedido con fecha "<Fecha>"
            Entonces obtiene la siguiente respuesta <Respuesta>

            Ejemplos:
            | Cliente | RazonSocial    | cuit     | Localidad     | Calle     | Altura | Fecha      | Respuesta                                                                                                                                                                              |
            | 6000    | Juan Perez     | 60600600 | Puerto Madryn | Av. Gales | 600    | 2020-05-14 | "{\"StatusCode\": 200,\"StatusText\": \"Se cargó el pedido exitosamente\",\"data\":{\"pedido\":8, \"fecha\":\"2020-05-14\",\"cliente\": 6000,\"domicilio\": 6001,\"total\": 7555.1 }}" |
            | 7000    | Dora Barrancos | 70700700 | Puerto Madryn | Mitre     | 700    | 2020-05-14 | "{\"StatusCode\": 200,\"StatusText\": \"Se cargó el pedido exitosamente\",\"data\":{\"pedido\":9, \"fecha\":\"2020-05-14\",\"cliente\": 7000,\"domicilio\": 7001,\"total\": 7555.1 }}" |

            Esquema del escenario: Cargar nuevo pedido a cliente que no existe
            Dado que llama la el cliente que no existe con razón social "<RazonSocial>" con cuit "<cuit>"
            Y que solicita se envíe el pedido al domicilio que no existe con localidad "<Localidad>" calle "<Calle>" y altura <Altura>
            Y que pide los siguientes artículos
            | articulo | nombre                | cantidad | precioUnitario |
            | 5000     | Machimbre 10mm        | 20       | 355.25         |
            | 6000     | Aceite de lino x 4lts | 1        | 450.10         |

            Cuando guarda el nuevo pedido con fecha "<Fecha>"
            Entonces obtiene la siguiente respuesta <Respuesta>

            Ejemplos:
            | RazonSocial    | cuit     | Localidad     | Calle     | Altura | Fecha      | Respuesta                                                         |
            | Juan Perez     | 60600606 | Puerto Madryn | Av. Gales | 800    | 2020-05-14 | "{\"StatusCode\": 400,\"StatusText\": \"El cliente NO existe.\"}" |
            | Dora Barrancos | 70700707 | Puerto Madryn | Mitre     | 800    | 2020-05-14 | "{\"StatusCode\": 400,\"StatusText\": \"El cliente NO existe.\"}" |