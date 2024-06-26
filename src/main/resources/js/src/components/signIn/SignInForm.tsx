import { useState } from "react";
import FormInput from "./FormInput";
import { api } from "../../api/apiClient";
import { toast } from "react-toastify";
import { useAuth } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";
import Loader from "../utility/Loader";
import { useLoggedUser } from "../../context/LoggedUserContext";
import { User } from "../../models/user";
import { AxiosError } from "axios";

export default function SignInForm() {

    const { setToken } = useAuth();
    const { setLoggedUser, loggedUser } = useLoggedUser();
    const navigate = useNavigate();
    const [submitting, setSubmitting] = useState(false);

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = event.target;
        setLoginData(data => ({
            ...data, [name]: value
        }))
    }

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        setSubmitting(true)
        api.login(loginData).then(async (token) => {
            if(!token) return

            setToken(token);
            api.setAuthorizationToken(token)

            await api.getCurrentUser()
                .then(user => {
                    setLoggedUser({
                        ...user
                    })
                    localStorage.setItem('loggedUser', JSON.stringify(user))
                }).catch((err: AxiosError) => {
                    toast(err.message, {
                        type: "error",
                    });
                })
            navigate('/dashboard/home');
        })
            .catch((err: Error) => {
                toast(err.message, {
                    type: "error",
                });
            })
            .finally(() => setSubmitting(false))
    }

    const [loginData, setLoginData] = useState({
        email: '',
        password: ''
    });

    return (
        <form onSubmit={handleSubmit} className="m-4 w-64 flex flex-wrap bg-gray-50 p-16 box-content">
            <FormInput name="email" displayName="Email" handleChange={handleChange} />
            <FormInput name="password" displayName="Password" handleChange={handleChange} />

            <button type="submit" className="bg-green-600 w-24 h-12 text-white rounded-lg mt-6 text-xl font-semibold hover:bg-green-400 transition-all">{submitting ? <Loader small /> : 'Submit'}</button>
        </form>
    )
}