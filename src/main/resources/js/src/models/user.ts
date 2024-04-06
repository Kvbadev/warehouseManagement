export class User {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    roles: string[];
    constructor(id: number, firstName: string, lastName: string, email: string, roles: string[]) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
    }
}