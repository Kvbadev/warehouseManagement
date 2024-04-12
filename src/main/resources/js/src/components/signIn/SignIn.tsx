import { navbarHeight } from "../navbar/Navbar";
import SignInForm from "./SignInForm";

export default function SignIn() {
    return (
        <div className={`w-full h-[calc(100%-${navbarHeight})] flex justify-center items-center flex-col`}>
            <h1 className="font-bold text-4xl p-4">Sign In</h1>
            <SignInForm />
        </div>
    )
}