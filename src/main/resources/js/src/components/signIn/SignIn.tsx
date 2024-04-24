import { navbarHeight } from "../navbar/Navbar";
import SignInForm from "./SignInForm";

export default function SignIn() {
    const mainHeight = `h-[calc(100%-${navbarHeight})]`;
    return (
        <div className={`${mainHeight} w-full flex justify-center items-center flex-col`}>
            <h1 className="font-bold text-4xl p-4">Sign In</h1>
            <SignInForm />
        </div>
    )
}