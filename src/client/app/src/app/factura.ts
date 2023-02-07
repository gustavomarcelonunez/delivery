import { Cliente } from './cliente';

export interface Factura {
    
    id: number;

    fechaEmision: Date;

    fechaPago: Date;

    cliente: Cliente;
}