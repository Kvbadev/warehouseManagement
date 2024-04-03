export default interface Delivery {
    id: number;
    arrivalDate: string;
    hasArrived: boolean;
    _links: {
        self: {
            href: string;
        };
        deliveries: {
            href: string;
        };
    }
    
}