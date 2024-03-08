import { Item } from "../models/item";

export class ApiClient {
    readonly url: string = "http://localhost:8080/api";

    async getItems(): Promise<Item[]> {
        return await fetch(this.url + '/inventory/items', {
            method: 'GET',
            headers: {
                'Authorization': 'Basic ' + btoa('staff@gmail.com:password_staff')
            }
        }).then(res => res.json())
        .then(res => {
            
            const items = res as Item[]
            return items;
        });
    }
}