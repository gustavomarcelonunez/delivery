import { DomicilioEntrega } from './domicilio-entrega';

export interface Remito {
    id: number;

    fechaArmado: Date;

    entregado: boolean;

    domicilioEntrega: DomicilioEntrega;
}