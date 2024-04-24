import { Link, useNavigate, useOutletContext, useParams } from "react-router-dom"
import { DashboardContextType } from "../Dashboard";
import FormInput from "../../signIn/FormInput";
import { useEffect, useState } from "react";
import { User } from "../../../models/user";
import Loader from "../Loader";
import { api } from "../../../api/apiClient";
import { toast } from "react-toastify";
import { AxiosError } from "axios";
import { UserWithPassword } from "../../../models/userWithPassword";
import ApiError, {SubError} from "../../../models/apiError";

export default function UserEdit() {
    const { users } = useOutletContext<DashboardContextType>();
    const { id } = useParams();
    const navigate = useNavigate();
    const userValues = users.find(u => u.id == parseInt(id ?? ''))
    const [user, setUser] = useState({
        'firstName': userValues?.firstName,
        'lastName': userValues?.lastName,
        'email': userValues?.email,
        'roleNames': userValues?.roleNames,
        'password': ''
    } as UserWithPassword)
    const [submitting, setSubmitting] = useState(false);
    const [deleting, setDeleting] = useState(false);
    const [validationErrors, setValidationErrors] = useState([] as SubError[]);

    useEffect(() => {
        const existingUser = users.find(u => u.id == parseInt(id ?? ''))
        setUser({
            ...user, ...existingUser
        })
    }, [users])


    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        let { name, value }: {name: any, value: any} = event.target;
        
        if(name === 'roleNames') {
            value = value.split(',');
        }

        setUser((data: any) => ({
            ...data, [name]: value
        }))
    }

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setSubmitting(true);
        if(!user.id) {
            setSubmitting(false);
            return;
        }
        const userBody: Partial<UserWithPassword> = {
            firstName: user.firstName,
            lastName: user.lastName,
            email: user.email,
            roleNames: user.roleNames,
        };
        if(user.password) {
            userBody.password = user.password
        }

        api.patchUser(userBody, user.id).then((_response) => {
            toast('Updated successfully', {type: 'success'});
            navigate('../users')
        }).catch((err: AxiosError) => {
            if(err.response?.status === 400 && err.message == 'Validation error') {

                const data = err.response.data as ApiError;
                setValidationErrors(data.subErrors!) //if it's a validation error then there is at least one subError
            } else {
                toast('Could not update the user: ' + err.message, {type: 'error'});
            }
        }).finally(() => setSubmitting(false))

    }
    const handleDelete: React.MouseEventHandler<HTMLButtonElement> = (_e) => {
        setDeleting(true);

    }

    return (
        <div className="w-[30vw] h-[42vw] border-2 border-gray-300 mx-auto my-auto rounded-md ">
            {!user.email ? <Loader /> :
                <form onSubmit={handleSubmit} className="w-full h-full flex flex-col justify-evenly">
                    <h1 className="text-4xl font-lato w-full text-center">Edit User</h1>
                    <FormInput errors={validationErrors} name="firstName" displayName="Firstname" handleChange={handleChange} style="border-gray-300 h-[3vw]" value={user?.firstName} />
                    <FormInput errors={validationErrors} name="lastName" displayName="Lastname" handleChange={handleChange} style="border-gray-300 h-[3vw]" value={user?.lastName} />
                    <FormInput errors={validationErrors} name="email" displayName="Email" handleChange={handleChange} style="border-gray-300 h-[3vw]" value={user?.email} />
                    <FormInput errors={validationErrors} name="roleNames" displayName="Roles" handleChange={handleChange} style="border-gray-300 h-[3vw]" value={user?.roleNames?.join(',')} />
                    <FormInput errors={validationErrors} name="password" displayName="New password" handleChange={handleChange} style="border-gray-300 h-[3vw]" value={user.password} />
                    <div className="flex px-4 justify-between w-full">
                        <button type="submit" className="bg-green-500 text-white font-lato text-xl w-[8vw] rounded-xl grid place-content-center">{submitting ? <Loader small />:'Submit'}</button>
                        <Link to="../users" className="bg-gray-500 text-white font-lato text-xl w-[8vw] rounded-xl text-center grid place-content-center">Cancel</Link>
                        <button className="bg-red-500 text-white font-lato text-xl w-[8vw] h-[3vw] rounded-xl text-center grid place-content-center" onClick={handleDelete}>{deleting ? <Loader small />:'Delete'}</button>
                    </div>
                </form>}
        </div>
    )
}