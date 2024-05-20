import axios, { AxiosError, AxiosInstance } from "axios";
import { User } from "../models/user";
import Item from "../models/item";
import { LoginRequest } from "../models/loginRequest"
import Delivery from "../models/delivery";
import ApiError from "../models/apiError";
import Parcel from "../models/parcel";

class ApiClient {
    private instance: AxiosInstance;

    constructor() {
        this.instance = axios.create({
            baseURL: process.env.NODE_ENV === 'development' ? "https://localhost:8080/api" :
                "https://ec2-52-59-237-24.eu-central-1.compute.amazonaws.com:8080/api",
            headers: {
                common: {
                    Authorization: "Bearer " + localStorage.getItem('token') ?? ''
                }
            }
        })
        this.instance.interceptors.request.use((config: any) => {
            return new Promise(resolve => setTimeout(() => resolve(config), 2000))
        })
        this.instance.interceptors.response.use(response => response,
            (err: AxiosError) => {
                if (err.response && err.response.status === 403) {
                    err.message = "You do not have sufficient priviliges. Use another account.";
                    console.log(`You do not have sufficient priviliges to use ${err.response.config.method} on ${err.response.config.url}. Use another account.`);
                } else {
                    err.message = (err.response?.data as ApiError).message
                }
                throw err;
            }
        )
    }

    async getItems(): Promise<any[]> {
        const response = await this.instance.get('/items')
        const data = response.data;

        return [data['_embedded']['itemList'], response.headers['x-total-count'], response.headers['x-total-price']]
    }
    async patchItem(item: Partial<Item>, itemId: number) {
        return await this.instance.patch('/items/' + itemId, item);
    }
    async deleteItem(itemId: number) {
        return await this.instance.delete('/items/' + itemId);
    }
    async getParcels(): Promise<Parcel[]> {
        const response = await this.instance.get('/parcels')
        const data = response.data

        return data['_embedded']['parcelList']
    }
    async patchParcel(parcel: Partial<Parcel>, parcelId: number) {
        return await this.instance.patch('/parcels/' + parcelId, parcel);
    }
    async deleteParcel(parcelId: number) {
        return await this.instance.delete('/parcels/' + parcelId);
    }
    async getDeliveries(): Promise<any[]> {
        const response = await this.instance.get('/deliveries')
        const totalDelayed = response.headers['x-total-delayed']
        const data = response.data;

        return [data['_embedded']['deliveryList'], totalDelayed]
    }
    async patchDelivery(delivery: Partial<Delivery>, deliveryId: number) {
        return await this.instance.patch('/deliveries/' + deliveryId, delivery);
    }
    async deleteDelivery(deliveryId: number) {
        return await this.instance.delete('/deliveries/' + deliveryId);
    }
    async getUsers(): Promise<User[]> {
        const response = await this.instance.get('/users')
        const data = response.data
        return data['_embedded']['userList'];
    }
    async getUser(userId: number): Promise<User> {
        const response = await this.instance.get('/users/' + userId)
        const data = response.data
        return data
    }
    async getCurrentUser(): Promise<User> {
        const response = await this.instance.get('/users/current')
        const data = response.data
        return data
    }
    async patchUser(user: Partial<User>, userId: number) {
        return await this.instance.patch('/users/' + userId, user);
    }
    async deleteUser(userId: number) {
        return await this.instance.delete('/users/' + userId);
    }
    async login({ email, password }: LoginRequest): Promise<undefined> {
        const response = await this.instance.post('/auth/login', new URLSearchParams({
            username: email,
            password: password
        })).catch((err: AxiosError) => {
            if (err.response?.status === 403) {
                err.message = "Invalid credentials";
            }
            throw err;
        });
        const token = response.data.token

        return token;
    }

    setAuthorizationToken(token: string) {
        this.instance.defaults.headers.common.Authorization = `Bearer ${token}`;
    }
}

const api = new ApiClient()

export { api };