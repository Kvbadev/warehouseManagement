export default interface Parcel {
    id: number;
    name: string;
    weight: number;
    
    _links: {
        self: {
            href: string;
        };
        parcels: {
            href: string;
        };
    };
}