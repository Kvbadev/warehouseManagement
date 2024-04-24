import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { navbarHeight } from "./navbar/Navbar";

export default function Main() {
    const {token} = useAuth();
    const mainHeight = `h-[calc(100%-${navbarHeight})]`;
    return (
        <div className={`${mainHeight} w-full flex justify-center items-center flex-col`}>
            {token ? 
            <Link to="dashboard/home" className="h-20 w-80 bg-gray-600 text-white text-3xl font-lato flex items-center justify-center w-86 rounded-md mb-6">Enter Dashboard</Link>
            : <Link to="sign-in" className="h-20 w-80 px-16 bg-gray-600 text-white text-3xl font-lato flex items-center justify-center w-86 rounded-md">Log in</Link> }
        </div>
    )
}