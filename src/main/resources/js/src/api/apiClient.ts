import axios, { AxiosInstance } from "axios";
import { User } from "../models/user";
import Item from "../models/item";
import { LoginRequest } from "../models/loginRequest"
import Delivery from "../models/delivery";

class ApiClient {
    private instance: AxiosInstance;

    constructor() {
        this.instance = axios.create({
                baseURL: "http://localhost:8080/api",
                headers: {
                    common: {
                        Authorization: "Bearer " + localStorage.getItem('token') ?? ''
                    }
                }
        })
        this.instance.interceptors.request.use((config: any) => {
            return new Promise(resolve => setTimeout(() => resolve(config), 2000))
        })
    }

    async getItems(): Promise<Item[]> {
        const response = await this.instance.get('/items')
        const data = response.data;
        console.log(data);

        return data['_embedded']['itemList']
    }
    async getDeliveries(): Promise<Delivery[]> {
        const response = await this.instance.get('/deliveries/latest')
        const data = response.data;
        console.log(data);

        return data['_embedded']['deliveryList']
    }
    async getUsers(): Promise<User[]> {
        const response = await this.instance.get('/users')
        const data = response.data
        console.log(data);
        return data['_embedded']['userList'];
    }
    async login({ email, password }: LoginRequest): Promise<undefined> {
        const response = await this.instance.post('/auth/login', new URLSearchParams({
            username: email,
            password: password
        }));
        const token = response.data.token

        return token;
    }

    setAuthorizationToken(token: string) {
        this.instance.defaults.headers.common.Authorization = `Bearer ${token}`;
    }
}

const api = new ApiClient()

export {api};