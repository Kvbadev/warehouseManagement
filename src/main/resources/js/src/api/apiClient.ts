import axios, { InternalAxiosRequestConfig } from "axios";
import { User } from "../models/user";
import Item from "../models/item";
import {LoginRequest} from "../models/loginRequest"
import Delivery from "../models/delivery";

export const instance = axios.create({
    baseURL: "http://localhost:8080/api",
    headers: {
        common: {
            Authorization: "Bearer "+localStorage.getItem('token') ?? ''
        }
    }
})

instance.interceptors.request.use((config: any) => {
    return new Promise(resolve => setTimeout(() => resolve(config), 2000))
})


export class ApiClient {
    async getItems(): Promise<Item[]> {
        const response = await instance.get('/items')
        const data = response.data;
        console.log(data);
        
        return data['_embedded']['itemList']
    }
    async getDeliveries(): Promise<Delivery[]> {
        const response = await instance.get('/deliveries/latest')
        const data = response.data;
        console.log(data);
        
        return data['_embedded']['deliveryList']
    }
    async login({email, password}: LoginRequest): Promise<undefined> {
        const response = await instance.post('/auth/login', new URLSearchParams({
            username: email,
            password: password
        }));
        const token = response.data.token
        instance.defaults.headers.common.Authorization = token;

        return token;
    }
}