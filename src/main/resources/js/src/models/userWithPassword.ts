import { User } from "./user";

export class UserWithPassword extends User {
    password: string
    constructor(firstName: string, lastName: string, email: string, roleNames: string[], password: string, id?: number) {
        super(firstName, lastName, email, roleNames, id);
        this.password = password;
    }
}