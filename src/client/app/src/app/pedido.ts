import { Cliente } from './cliente';
import { DomicilioEntrega } from './domicilio-entrega';
import { ArticulosPedido } from './articulos-pedido';

export interface Pedido {
    id: number;

    total: number;

    cliente: Cliente;

    date: Date;

    domicilioEntrega: DomicilioEntrega;

    articulosPedido: ArticulosPedido [];
}