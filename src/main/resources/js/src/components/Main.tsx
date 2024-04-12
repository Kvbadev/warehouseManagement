import { Link } from "react-router-dom";
import { navbarHeight } from "./navbar/Navbar";

export default function Main() {
    const nav = navbarHeight;
    return (
        <div className={`w-full h-[calc(100%-${nav})] flex justify-center items-center`}>
            <Link to="dashboard/home" className="h-20 p-8 bg-blue-700 text-white text-3xl font-lato flex items-center justify-center w-86 rounded-md">Enter Dashboard</Link>
        </div>
    )
}