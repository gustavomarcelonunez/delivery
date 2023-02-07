            # language: es

            Característica: Administrar de la Facturación
            proceso que emite facturas de remitos entregados no facturados aún

            Esquema del escenario:
            Dado el remito <remito> que no está entregado aún
            Cuando se marca como entregado al remito número <remito>
            Entonces se marca como entregado se obtiene <resultado>

            Ejemplos:
            | remito | resultado                                                                                        |
            | 1      | "{\"StatusCode\": 200,\"StatusText\": \"Remito entregado correctamente\",\"data\":{\"id\": 1}}"  |
            | 2      | "{\"StatusCode\": 200,\"StatusText\": \"Remito entregado correctamente\",\"data\":{\"id\": 2}}"  |
            | 4      | "{\"StatusCode\": 200,\"StatusText\": \"Remito entregado correctamente\",\"data\":{\"id\": 4}}"  |
            | 5      | "{\"StatusCode\": 200,\"StatusText\": \"Remito entregado correctamente\",\"data\":{\"id\": 5}}"  |
            | 6      | "{\"StatusCode\": 200,\"StatusText\": \"Remito entregado correctamente\",\"data\":{\"id\": 6}}"  |
            | 10     | "{\"StatusCode\": 200,\"StatusText\": \"Remito entregado correctamente\",\"data\":{\"id\": 10}}" |
            | 11     | "{\"StatusCode\": 200,\"StatusText\": \"Remito entregado correctamente\",\"data\":{\"id\": 11}}" |
            | 12     | "{\"StatusCode\": 200,\"StatusText\": \"Remito entregado correctamente\",\"data\":{\"id\": 12}}" |
            | 14     | "{\"StatusCode\": 200,\"StatusText\": \"Remito entregado correctamente\",\"data\":{\"id\": 14}}" |

            Escenario:
            Dado los remitos entregados
            Cuando se solicita facturar con fecha "2020-05-16"
            Entonces se obtiente el siguiente resultado de facturación
            """
            {
                "StatusCode": 200,
                "StatusText": "Facturas generadas exitosamente",
                "data": {
                    "cantidad": 6
                }
            }
            """

            Y se generaron las siguientes facturas
            | Factura | FechaEmision | Cliente | Total   |
            | 1       | 2020-05-16   | 1000    | 1944.80 |
            | 2       | 2020-05-16   | 3000    | 972.40  |
            | 3       | 2020-05-16   | 4000    | 579.70  |
            | 4       | 2020-05-16   | 5000    | 972.40  |
            | 5       | 2020-05-16   | 9000    | 556.70  |
            | 6       | 2020-05-16   | 10000   | 556.70  |

            Esquema del escenario:
            Dado el remito <remito> que no está entregado aún
            Cuando se marca como entregado al remito número <remito>
            Entonces se marca como entregado se obtiene <resultado>

            Ejemplos:
            | remito | resultado                                                                                        |
            | 3      | "{\"StatusCode\": 200,\"StatusText\": \"Remito entregado correctamente\",\"data\":{\"id\": 3}}"  |
            | 7      | "{\"StatusCode\": 200,\"StatusText\": \"Remito entregado correctamente\",\"data\":{\"id\": 7}}"  |
            | 8      | "{\"StatusCode\": 200,\"StatusText\": \"Remito entregado correctamente\",\"data\":{\"id\": 8}}"  |
            | 9      | "{\"StatusCode\": 200,\"StatusText\": \"Remito entregado correctamente\",\"data\":{\"id\": 9}}"  |
            | 13     | "{\"StatusCode\": 200,\"StatusText\": \"Remito entregado correctamente\",\"data\":{\"id\": 13}}" |
            | 15     | "{\"StatusCode\": 200,\"StatusText\": \"Remito entregado correctamente\",\"data\":{\"id\": 15}}" |
            | 16     | "{\"StatusCode\": 200,\"StatusText\": \"Remito entregado correctamente\",\"data\":{\"id\": 16}}" |
            | 17     | "{\"StatusCode\": 200,\"StatusText\": \"Remito entregado correctamente\",\"data\":{\"id\": 17}}" |

            Escenario:
            Dado los remitos entregados
            Cuando se solicita facturar con fecha "2020-05-16"
            Entonces se obtiente el siguiente resultado de facturación
            """
            {
                "StatusCode": 200,
                "StatusText": "Facturas generadas exitosamente",
                "data": {
                    "cantidad": 7
                }
            }
            """

            Y se generaron las siguientes facturas
            | Factura | FechaEmision | Cliente | Total   |
            | 7       | 2020-05-16   | 2000    | 1944.80 |
            | 8       | 2020-05-16   | 4000    | 392.70  |
            | 9       | 2020-05-16   | 6000    | 7555.10 |
            | 10      | 2020-05-16   | 7000    | 7555.10 |
            | 11      | 2020-05-16   | 8000    | 969.70  |

            Esquema del escenario:
            Dada la factura <factura> que no ha sido abonada aún
            Cuando la factura número <factura> se marca como abonada en la fecha "2020-06-03"
            Entonces se obtiene el siguiente <resultado> de facturas abonadas

            Ejemplos:
            | factura | resultado                                                                                           |
            | 1       | "{\"StatusCode\": 200,\"StatusText\": \"Factura abonada correctamente\",\"data\":{\"factura\": 1}}" |
            | 2       | "{\"StatusCode\": 200,\"StatusText\": \"Factura abonada correctamente\",\"data\":{\"factura\": 2}}" |
            | 3       | "{\"StatusCode\": 200,\"StatusText\": \"Factura abonada correctamente\",\"data\":{\"factura\": 3}}" |
            | 5       | "{\"StatusCode\": 200,\"StatusText\": \"Factura abonada correctamente\",\"data\":{\"factura\": 5}}" |
            | 8       | "{\"StatusCode\": 200,\"StatusText\": \"Factura abonada correctamente\",\"data\":{\"factura\": 8}}" |

            Escenario:
            Dada la factura "14" que no existe
            Cuando se intenta marcar como abonada en la fecha "2020-06-03"
            Entonces se obtiene la siguiente respuesta
            """
            {
                "StatusCode": 501,
                "StatusText": "La factura 14 no existe",
                "data": {}
            }
            """

