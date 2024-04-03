import { Link } from "react-router-dom";

export default function SimpleInfoBlock({digit, label, isNegative=false, link, linkLabel}: {digit: string, label: string, link?: string, linkLabel?: string, isNegative?: boolean}) {
    return (
          <div className="flex flex-col justify-center items-center h-40 w-1/4 bg-gray-400 relative rounded-tl-xl rounded-br-xl">
            <h1 className={`text-5xl ${isNegative ? 'text-red-500' : 'text-white'} pb-1`}>{digit}</h1>
            <p className={`${isNegative ? 'text-red-500' : 'text-white'} font-lato`}>{label}</p>
            {link &&
                <Link to={link} className="text-gray-500 font-lato text-sm underline absolute bottom-5">{linkLabel}</Link>
            }
          </div>
    )
}