import { DomicilioEntrega } from './domicilio-entrega';

export interface Cliente {
    id: number;

    cuit: string;

    razonSocial: string;

    domiciliosEntrega: DomicilioEntrega [];   
}