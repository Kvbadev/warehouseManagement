import { useState } from "react";
import { User } from "../../models/user";
import FormInput from "./FormInput";

export default function SignInForm(){
    
    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = event.target;
        setUser((prevUser: User) => ({...prevUser, [name]: value}));
    }

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        console.log(user);
    }

    const [user, setUser] = useState<User>({
        firstName: '',
        lastName: '',
        email: '',
        password: ''
    });

    return (
        <form onSubmit={handleSubmit} className="m-4 w-64 flex flex-wrap bg-gray-50 p-16 box-content">
            <FormInput name="firstName" displayName="First Name" handleChange={handleChange} />
            <FormInput name="lastName" displayName="Last Name" handleChange={handleChange} />
            <FormInput name="email" handleChange={handleChange} />
            <FormInput name="password" handleChange={handleChange} />

           <button type="submit" className="bg-green-500 w-24 h-12 rounded-lg mt-6 text-lg font-semibold">Submit</button>
        </form>
    )
}