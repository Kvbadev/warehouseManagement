export class User {
    id?: number;
    firstName: string;
    lastName: string;
    email: string;
    roleNames: string[]; // [ROLE_1,ROLE_0]
    _links?: string[][]
    constructor(firstName: string, lastName: string, email: string, roleNames: string[], id?: number, _links?: string[][]) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roleNames = roleNames;
        this._links = _links;
    }
}