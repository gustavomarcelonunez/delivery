import { Localidad } from './localidad';

export interface DomicilioEntrega {
    
    id: number;

    calle: string;

    altura: number;

    pisoDpto: string;

    localidad: Localidad;
}
