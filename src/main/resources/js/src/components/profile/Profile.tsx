import { Link } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { useLoggedUser } from "../../context/LoggedUserContext";

export default function Profile() {
    const {setToken} = useAuth();
    const {setLoggedUser, loggedUser} = useLoggedUser();

    const logout = () =>  {
        setToken('');
        setLoggedUser(undefined);
        localStorage.removeItem("token");
        localStorage.removeItem("loggedUser");
    }
    return (
        <div className="flex flex-col">
            {loggedUser &&
            <div className="flex flex-col p-4 w-1/3 [&>p]:p-2 [&>p]:text-xl">
                <p className="border-2">{loggedUser?.id}</p>
                <p className="">{loggedUser?.firstName}</p>
                <p className="border-2">{loggedUser?.lastName}</p>
                <p className="">{loggedUser?.email}</p>
                <p className="border-2">{loggedUser?.roleNames.join(',')}</p>
            </div>}
            <Link to="../" onClick={logout} className="flex justify-center items-center w-24 h-12 text-white text-xl font-lato font-bold bg-red-600 m-4 rounded-sm">Log out</Link>
        </div>
    )
}