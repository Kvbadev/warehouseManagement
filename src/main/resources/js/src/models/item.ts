export default interface Item {
    id: number;
    name: string;
    description: string;
    quantity: number;
    netPrice: number;
    
    _links: {
        self: {
            href: string;
        };
        items: {
            href: string;
        };
        parcel?: {
            href: string;
        };
    };
}
