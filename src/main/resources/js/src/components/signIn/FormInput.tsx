import { ChangeEventHandler } from "react";

export default function FormInput(props: {name: string, displayName?: string, handleChange: ChangeEventHandler}) {
    return (
        <>
        <label htmlFor={props.name} className="font-semibold">{props.displayName ?? props.name}</label>
        <input type="text" name={props.name} onChange={props.handleChange} className="rounded-md mb-4 mt-2 h-8 w-full border-2 text-center focus:border-gray-600 focus:outline-none"/>
        </>
    )
}