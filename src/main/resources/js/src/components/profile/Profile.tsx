import { Link } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

export default function Profile() {
    const {setToken} = useAuth();

    const logout = () =>  {
        setToken('');
        localStorage.removeItem("token");
    }
    return (
        <Link to="../" onClick={logout} className="flex justify-center items-center w-24 h-12 text-white text-xl font-lato font-bold bg-red-600 m-4 rounded-sm">Log out</Link>
    )
}