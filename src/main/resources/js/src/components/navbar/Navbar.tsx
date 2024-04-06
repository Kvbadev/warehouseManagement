import { Link } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

interface navItem {
  id: number;
  text: string;
  slug: string;
}
export const navbarHeight = "48px";
export function Navbar() {
  const { token } = useAuth();
  const navItems: Array<navItem> = [
    { id: 1, text: "About", slug: "about-us" },
    token ? { id: 2, text: 'Profile', slug: 'profile' } : { id: 2, text: "Sign in", slug: "sign-in" },
  ];

  return (
    <div className="sticky top-0 w-screen box-border h-12 bg-slate-400/90 flex-row flex justify-between items-center backdrop-blur-sm">
      <div className="logo text-gray-700 text-2xl justify-self-start m-2">
        <a className="" href="/">WMS.</a>
      </div>
      <div className="flex flex-row px-4">
        {navItems.map((i) => {
          return (
            <Link
              to={i.slug}
              key={i.id}
              className="w-28 text-xl text-center font-lato hidden md:block"
            >
              {i.text}
            </Link>
          );
        })}
        <div className="md:hidden size-30">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="w-6 h-6"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d="M4 6h16M4 12h16M4 18h16"
            />
          </svg>
        </div>
      </div>
    </div>
  );
}
