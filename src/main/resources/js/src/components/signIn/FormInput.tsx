import { ChangeEventHandler, useEffect } from "react";
import { SubError } from "../../models/apiError";

export default function FormInput({ name, displayName, handleChange, value, style, errors }: { name: string, displayName?: string, handleChange: ChangeEventHandler, value?: string, style?: string, errors?: SubError[] }) {

    const fieldError = errors?.find(e => e.field == name);
    
    return (
        <div className="relative">
            <p className="left-24 absolute text-red-500 text-ellipsis max-w-[18vw] overflow-hidden text-nowrap hover:overflow-visible cursor-pointer">{fieldError?.message}</p>
            <label htmlFor={name} className="font-semibold">{displayName ?? name}</label>
            <input type="text" name={name} onChange={handleChange} className={`${fieldError && 'bg-red-200'} rounded-md mb-4 mt-2 h-8 w-full border-2 text-center focus:border-gray-600 focus:outline-none text-lg ${style}`} value={value} />
        </div>
    );
}