import { Articulo } from './articulo';
import { Pedido } from './pedido';
import { Factura } from './factura';
import { Remito } from './remito';

export interface ArticulosPedido {

    id: number;

    cantidad: number;

    precioUnitario: number;

    articulo: Articulo;

    precio: number;

    pedido: Pedido;

    factura: Factura;

    remito: Remito;
    
}