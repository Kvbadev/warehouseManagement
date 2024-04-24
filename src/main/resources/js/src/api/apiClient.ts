import axios, { AxiosError, AxiosInstance } from "axios";
import { User } from "../models/user";
import Item from "../models/item";
import { LoginRequest } from "../models/loginRequest"
import Delivery from "../models/delivery";
import ApiError from "../models/apiError";

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

    async getItems(): Promise<Item[]> {
        const response = await this.instance.get('/items')
        const data = response.data;

        return data['_embedded']['itemList']
    }
    async getDeliveries(): Promise<Delivery[]> {
        const response = await this.instance.get('/deliveries/latest')
        const data = response.data;

        return data['_embedded']['deliveryList']
    }
    async getUsers(): Promise<User[]> {
        const response = await this.instance.get('/users')
        const data = response.data
        return data['_embedded']['userList'];
    }
    async getUser(userId: number): Promise<User> {
        const response = await this.instance.get('/users/'+userId)
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